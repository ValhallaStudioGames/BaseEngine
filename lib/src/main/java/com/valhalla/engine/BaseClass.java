package com.valhalla.engine;

import com.valhalla.engine.internal.Internal;

public abstract class BaseClass {
	
	protected int drawLayer;
	protected Handler handler;
	
	/**
	 * Default constructor the BaseClass class.
	 * For animation it is recommended to use {@link Animation}<br>
	 * <u>Note:</u> this constructor does in order to add this to the Handler use {@link Handler#addClass(BaseClass, int)}
	 * @author BauwenDR
	 */
	public BaseClass() {}
	
	/**
	 * Default constructor for the BaseClass, this constructor will automatically add the object to the Handler upon creation.
	 * For animation it is recommended to use {@link Animation}<br>
	 * @param handler <b>(Handler)</b> Handler to which the BaseClass will be added.
	 * @param draw <b>(Integer)</b> Layer at which to draw, going from 0 (= bottom) to 10 (= top).
	 * @author BauwenDR
	 */
	public BaseClass(Handler handler, int draw) {
		this._setFields(handler, draw);
		handler.addClass(this, draw);
	}
	
	public abstract void tick();
	public abstract void render();
	
	/**
	 * Getter for the current layer at which the class is being drawn
	 * @return DrawLayer (Integer)
	 * @author BauwenDR
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
