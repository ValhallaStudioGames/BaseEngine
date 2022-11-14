package com.valhalla.engine;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.valhalla.engine.exception.DrawLayerOutOfBoundsException;
import com.valhalla.engine.internal.Internal;

public class Handler {
	
	private Hashtable<Integer, List<BaseClass> > _classes = new Hashtable<>();
	private BlockingQueue<BaseClass> _removeQueue = new LinkedBlockingQueue<>();
	private Hashtable<BaseClass, Integer> _addQueue = new Hashtable<>();
	private Hashtable<BaseClass, Integer> _addQueuePostReset = new Hashtable<>();
	
	private LinkedList<Animation> _animations = new LinkedList<>();
	
	private GameState _gameState;
	private final int _layers = 10;
	private int _renderIterator;
	private int _tickIterator;
	
	private boolean _clearClasses;
	private boolean _isClassPresentInHandler;
	
	@Internal
	public Handler() {
		_classes.clear();
		for(_renderIterator = 0; _renderIterator < _layers; ++_renderIterator) {
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
	 * @author BauwenDR
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
	 * @see #addClass
	 * @author BauwenDR
	 */
	public boolean addUniqueClass(BaseClass baseClass, int drawLayer) {
		boolean  classPresent = false;
			 
		for(_tickIterator = 0; _tickIterator < _layers; ++_tickIterator) { //check if already present
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
	 * @author BauwenDR
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
		for(_tickIterator = 0; _tickIterator < _layers; ++_tickIterator) {
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
		_addQueue.forEach((baseClass, drawLayer) -> addImmediateClass(baseClass, drawLayer));
		_addQueue.clear();
		
		//removing all classes if clearClasses()  was previously run
		if(_clearClasses) {
			removeImmediateClasses();
			_clearClasses = false;
			
			//adding all classes that were added after calling clear
			_addQueuePostReset.forEach((baseClass, drawLayer) -> 	addImmediateClass(baseClass, drawLayer));
			_addQueuePostReset.clear();
		}
	}
	
	/**
	 * Removes all BaseClasses from the Handler.
	 * This function does however not remove the GameState.
	 * @author BauwenDR
	 */
	public void clearClasses() {
		_clearClasses = true;
	}
	
	/**
	 *  Removes all BaseClasses immediately from the Handler.<br>
	 * <u>Warning:</u> Function might be unstable if not used at the end of a Tick.<br>
	 * It is strongly recommended to use {@link #clearClasses instead.
	 * @see #clearClasses
	 * @author BauwenDR
	 */
	public void removeImmediateClasses() {
		_classes.forEach((layer, classList) -> classList.clear());
	}
	
	/**
	 * Sets the current GameState to the given GameState, and links to Handler in the GameState to this object.
	 * @param gameState <b>(GameState or inherited)</b> The new GameState.
	 * @author BauwenDR
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
	 * @author BauwenDR
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
	 * <u>Note:</u> It is advised to use the {@link  Animation#start(Handler)} function, to insure animtions are started correctly.
	 * @param animtion <b>(Animation)</b> The animation that will be added to the animations list.
	 * @author BauwenDR
	 */
	public void addAnimation(Animation animtion) {
		_animations.add(animtion);
	}
	
	/**
	 * Removes  an animation to the list of playing animations.<br>
	 * <u>Note:</u> It is advised to use the {@link  Animation#stop()} function, to insure animtions are stopped correctly.
	 * @param animtion <b>(Animation)</b> The animation that will be removed from the animations list.
	 * @author BauwenDR
	 */
	public void removeAnimation(Animation animtion) {
		_animations.remove(animtion);
	}
	
	@Internal
	void tick(){
		_classes.forEach((layer, classList) -> classList.forEach((baseClass) -> baseClass.tick()));
		_animations.forEach((animation) -> animation.tick());
		
		if(_gameState != null) {
			_gameState.tick();
		}
		
		emptyQueue();		//empty queue at end of iteration
	}
	
	@Internal
	void render() {
		for(_renderIterator = 0; _renderIterator < _layers; _renderIterator++) {
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
