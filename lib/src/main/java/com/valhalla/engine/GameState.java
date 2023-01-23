package com.valhalla.engine;

import com.valhalla.engine.internal.Internal;

/**
 * Abstract class/ blueprint for a simple GameState/ GateStateController.<br>
 * A GameState object will usually be responsible for drawing UI and managing BaseClasses.
 * @author BauwenDR
 */
public abstract class GameState {
	
	protected Handler handler;
	
	/**
	 * Default constructor for a GameState, the object will receive its link to the Handler upon being set as the GameState.
	 */
	public GameState() {}
	
	/**
	 * Constructor for a GameState, this one will automatically set itself as the GameState in Handler.
	 * @param handler <b>(Handler)</b> Link to the Handler object for which it will be the GameState.
	 */
	public GameState(Handler handler, boolean clearClasses) {
		handler.setGameState(this, clearClasses);
	}

	/**
	 * Default method through witch the GameState object should be created (method acts as a constructor).
	 */
	protected abstract void initialise();

	/**
	 * Method used for detecting game updates and managing all BaseClasses
	 */
	public abstract void tick();

	/**
	 * Method used for drawing a HUD or UI.
	 */
	public abstract void render();
	
	@Internal
	void _setFields(Handler handler) {
		this.handler = handler;
	}
}
