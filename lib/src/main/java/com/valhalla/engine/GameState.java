package com.valhalla.engine;

import com.valhalla.engine.internal.Internal;

public abstract class GameState {
	
	protected Handler handler;
	
	/**
	 * Default constructor for a GameState, the object will recieve its link to the Handler upon being set as the GameState.
	 * @author BauwenDR
	 */
	public GameState() {}
	
	/**
	 * Constructor for a GameState, this one will automaticly set itself as the GameState in Handler.
	 * @param handler <b>(Handler)</b> Link to the Handler object for which it will be the GameState.
	 * @author BauwenDR
	 */
	public GameState(Handler handler, boolean clearClasses) {
		handler.setGameState(this, clearClasses);
	}
	
	protected abstract void initialise();
	public abstract void tick();
	public abstract void render();
	
	@Internal
	void _setFields(Handler handler) {
		this.handler = handler;
	}
}
