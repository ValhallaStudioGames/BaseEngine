package com.valhalla.engine;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.valhalla.engine.exception.DrawLayerOutOfBoundsException;
import com.valhalla.engine.internal.Internal;

/**
 * Class that handles the ticking and rendering for all BaseClasses, GameStates and Animations.
 * @author BauwenDR
 */
public class Handler {
	
	private Hashtable<Integer, List<BaseClass> > _classes = new Hashtable<>();
	private BlockingQueue<BaseClass> _removeQueue = new LinkedBlockingQueue<>();
	private Hashtable<BaseClass, Integer> _addQueue = new Hashtable<>();
	private Hashtable<BaseClass, Integer> _addQueuePostReset = new Hashtable<>();
	
	private LinkedList<Animation> _animations = new LinkedList<>();

	private static final int _LAYERS = 10;

	private GameState _gameState;
	private int _renderIterator;
	private int _tickIterator;
	
	private boolean _clearClasses;
	private boolean _isClassPresentInHandler;
	
	@Internal
	public Handler() {
		_classes.clear();
		for(_renderIterator = 0; _renderIterator < _LAYERS; ++_renderIterator) {
			_classes.put(_renderIterator, new LinkedList<>());
		}
	}
	
	/**
	 * Function used for adding a BaseClass to the Handler at a specific draw-layer.<br>
	 * <br>
	 * This function will add the BaseClass without checking whether it is already present in the Handler.
	 * To add a unique instance of a BaseClass, use the function {@link #addUniqueClass}.<br>
	 * <br>
	 * <u>Note:</u> Some of the BaseClass constructors call this functions internally.<br>
	 * @param baseClass <b>(BaseClass or inherited)</b> BaseClass to be added to the Handler.
	 * @param drawLayer <b>(Integer)</b> The layer at which the BaseClass should be rendered from 0 (= bottom) to 10 (= top).
	 * @see #addUniqueClass
	 */
	public void addClass(BaseClass baseClass, int drawLayer) throws DrawLayerOutOfBoundsException {
		if(drawLayer > 10 || drawLayer < 0) {
			throw new DrawLayerOutOfBoundsException(drawLayer);
		}
		if(_clearClasses) {
			_addQueuePostReset.put(baseClass, drawLayer);
		}else {
			_addQueue.put(baseClass, drawLayer);
		}
	}
	
	@Internal
	private void addImmediateClass(BaseClass baseClass, int drawLayer) {
		_classes.get(drawLayer).add(baseClass);
		baseClass._setFields(this, drawLayer);
	}
	
	/**
	 * Adds a unique instance of a BaseClass. BaseClass won' t be added if it is already present in the Handler.
	 * Returns true if the class was added, and false if it was already in the Handler and thus not added.<br>
	 * <br>
	 * This function is a little slower than {@link #addClass} due to having to check all classes in the Handler, only use when you are not 100% sure if class is already present.<br>
	 * <br>
	 * <u>Note:</u> If the BaseClass is already present on another layer this function will also return false.
	 * @param baseClass <b>(BaseClass or inherited)</b> BaseClass to be added if it is not already present.
	 * @param drawLayer <b>(Integer)</b> The layer at which the BaseClass should be rendered from 0 (= bottom) to 10 (= top).
	 * @return True if the class was added, and false if it was already in the Handler and thus not added.
	 * @see #addClass(BaseClass, int)
	 */
	public boolean addUniqueClass(BaseClass baseClass, int drawLayer) {
		boolean  classPresent = false;
			 
		for(_tickIterator = 0; _tickIterator < _LAYERS; ++_tickIterator) { //check if already present
			if(_classes.get(_tickIterator).contains(baseClass)) {
				classPresent = true;
				break;
			}
		}
			
		if(!classPresent)  {
			addClass(baseClass, drawLayer); 	//add if not present
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a baseClass to the removal queue in order to be removed. If adding to the queue fails it will be printed out in console.
	 * @param baseClass <b>(BaseClass or inherited)</b> The class to be removed from the Handler at the end of the tick.
	 */
	public void removeClass(BaseClass baseClass) {
		try {
			_removeQueue.put(baseClass);
		} catch (InterruptedException e) {
			GameLoop.engineOutput.println("BaseEngine: could not add class " + baseClass.getClass().getName() + "to the remove queue.");
		}
	}

	@Internal
	private void removeImmediateClass(BaseClass baseClass) {
		for(_tickIterator = 0; _tickIterator < _LAYERS; ++_tickIterator) {
			if(_classes.get(_tickIterator).remove(baseClass))
				break;
		}
	}
	
	@Internal
	private void emptyQueue() {
		//remove queue
		for(BaseClass baseClass : _removeQueue) {
			removeImmediateClass(baseClass);
		}
		_removeQueue.clear();
		
		//add queue
		_addQueue.forEach(this::addImmediateClass);
		_addQueue.clear();
		
		//removing all classes if clearClasses()  was previously run
		if(_clearClasses) {
			removeImmediateClasses();
			_clearClasses = false;
			
			//adding all classes that were added after calling clear
			_addQueuePostReset.forEach(this::addImmediateClass);
			_addQueuePostReset.clear();
		}
	}
	
	/**
	 * Removes all BaseClasses from the Handler.
	 * This function does however not remove the GameState.
	 */
	public void clearClasses() {
		_clearClasses = true;
	}
	
	/**
	 *  Removes all BaseClasses immediately from the Handler.<br>
	 * <u>Warning:</u> Function might be unstable if not used at the end of a Tick.<br>
	 * It is strongly recommended to use {@link #clearClasses} instead.
	 * @see #clearClasses
	 */
	public void removeImmediateClasses() {
		_classes.forEach((layer, classList) -> classList.clear());
	}
	
	/**
	 * Sets the current GameState to the given GameState, and links to Handler in the GameState to this object.
	 * @param gameState <b>(GameState or inherited)</b> The new GameState
	 * @param clearClasses <b>(Boolean)</b> clear the classes in the associated handler upon setting the new state
	 */
	public void setGameState(GameState gameState, boolean clearClasses) {
		if(clearClasses) {
			clearClasses();
		}
		_gameState = gameState;
		gameState._setFields(this);
		gameState.initialise();
	}
	
	/**
	 * Checks the Handler to see if a class is already in the Handler, and thus drawn and ticked.
	 * @param baseClass <b>(BaseClass or inherited)</b> The class to check
	 * @return see if a class is already present in the Handler
	 */
	public boolean isClassPresentInHandler(BaseClass baseClass) {
		_isClassPresentInHandler=false;
		_classes.forEach((layer,  classList) ->  {
			if(classList.contains(baseClass)) {
				_isClassPresentInHandler = true;
			}
		});
		return _isClassPresentInHandler;
	}
	
	/**
	 * Adds an animation to the list of playing animations.<br>
	 * <u>Note:</u> It is advised to use the {@link  Animation#start(Handler)} function, to insure animations are started correctly.
	 * @param animation <b>(Animation)</b> The animation that will be added to the animations list.
	 */
	public void addAnimation(Animation animation) {
		_animations.add(animation);
	}
	
	/**
	 * Removes  an animation to the list of playing animations.<br>
	 * <u>Note:</u> It is advised to use the {@link  Animation#stop()} function, to insure animations are stopped correctly.
	 * @param animation <b>(Animation)</b> The animation that will be removed from the animations list.
	 */
	public void removeAnimation(Animation animation) {
		_animations.remove(animation);
	}
	
	@Internal
	void tick(){
		_classes.forEach((layer, classList) -> classList.forEach(BaseClass::tick));
		_animations.forEach(Animation::tick);
		
		if(_gameState != null) {
			_gameState.tick();
		}
		
		emptyQueue();		//empty queue at end of iteration
	}
	
	@Internal
	void render() {
		for(_renderIterator = 0; _renderIterator < _LAYERS; _renderIterator++) {
			Iterator<BaseClass> renderClassIterator  = _classes.get(_renderIterator).iterator();
			while(renderClassIterator.hasNext()) {
				renderClassIterator.next().render();
			}
		}
		
		if(_gameState != null) {
			_gameState.render();
		}
	}
}
