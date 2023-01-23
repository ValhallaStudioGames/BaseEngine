package com.valhalla.engine.exception;

/**
 * Exception that gets thrown if the specified drawLayer for a new BaseClass is out of range for possible drawLayer values (0 to 9 inclusive).
 * @author BauwenDR
 */
public class DrawLayerOutOfBoundsException extends IndexOutOfBoundsException {
	private static final long serialVersionUID = -3835069958358464690L;
	
	private final int _drawLayer;

	/**
	 * Creates a new DrawLayerOutOfBoundsException.<br>
	 * <br>
	 * This exception is thrown when adding a BaseClass to the Handler with an invalid drawLayer value.
	 * @param drawLayer the drawLayer that was being requested
	 */
	public DrawLayerOutOfBoundsException(int drawLayer) {
		this._drawLayer = drawLayer;
	}

	@Override
	public String getMessage() {
		return "drawLayer " + _drawLayer + " out of bounds for bounds 10";
	}
}
