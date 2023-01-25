package com.valhalla.engine;

import com.valhalla.engine.internal.Internal;
import com.valhalla.engine.util.Renderable;
import com.valhalla.engine.util.Tickable;

/**
 * Abstract class/ blueprint for a simple Game Object that can be ticked and rendered.
 * @author BauwenDR
 */
public abstract class BaseClass implements Tickable, Renderable {
	
	protected int drawLayer;
	protected Handler handler;
	
	/**
	 * Default constructor the BaseClass class.
	 * For animations, it is recommended to use {@link Animation}<br>
	 * <u>Note:</u> this constructor does in order to add this to the Handler use {@link Handler#addClass(BaseClass, int)}
	 */
	public BaseClass() {}
	
	/**
	 * Default constructor for the BaseClass, this constructor will automatically add the object to the Handler upon creation.
	 * For animations, it is recommended to use {@link Animation}<br>
	 * @param handler <b>(Handler)</b> Handler to which the BaseClass will be added.
	 * @param draw <b>(Integer)</b> Layer at which to draw, going from 0 (= bottom) to 10 (= top).
	 */
	public BaseClass(Handler handler, int draw) {
		this._setFields(handler, draw);
		handler.addClass(this, draw);
	}

	/**
	 * Method used for updating an objects position or registering updates
	 */
	@Override
	public abstract void tick();

	/**
	 * Method used for drawing the object on screen
	 */
	@Override
	public abstract void render();
	
	/**
	 * Getter for the current layer at which the class is being drawn
	 * @return DrawLayer (Integer)
	 */
	protected int getDrawLayer() {
		return drawLayer;
	}
	
	@Internal
	void _setFields(Handler handler, int draw) {
		this.handler = handler;
		this.drawLayer = draw;
	}
}
