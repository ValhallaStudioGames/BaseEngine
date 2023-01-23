package com.valhalla.engine.input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.valhalla.engine.Screen;
import com.valhalla.engine.internal.Internal;


/**
 * Class used by GameLoop to detect mouse input. Most functions in this class are static.<br>
 * <br>
 * <b>Contains:</b>
 * <ul>
 * 	<li>{@link #getKeyHeld}</li>
 *  <li>{@link #getKeyPressed}</li>
 *  <li>{@link #getX}</li>
 *  <li>{@link #getY}</li>
 *  <li>{@link #getPoint}</li>
 *  <li>{@link #getScrollingUp}</li>
 *  <li>{@link #getScrollingDown}</li>
 *  <li>{@link #getScrollingAmount}</li>
 * </ul>
 * @author BauwenDR
 */
public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener{
	
	private static final int _keyAmount = 5;
	private static final boolean[] _keyHeld = new boolean[_keyAmount];
	private static final boolean[] _keyPressed = new boolean[_keyAmount];
	private static final boolean[] _keyLowering = new boolean[_keyAmount];
	private static final boolean[] _lastTick = new boolean[_keyAmount];
	private static boolean _scrollingUp, _scrollingDown;
	private static double _scrollingAmount;
	
	private static double _mouseX, _mouseY;
	private static double _mouseMovedX, _mouseMovedY;
	private static double _lastMouseX = 0, _lastMouseY = 0;
	
	@Internal
	public MouseInput() {
		for(int key = 0; key < _keyAmount; key++) {
			_keyHeld[key] = false;
			_keyPressed[key] = true;
			_lastTick[key]  = false;
		}
	}
	
	@Internal
	public void tick() {
		//mouse movement
		_mouseMovedX = _mouseX-_lastMouseX;
		_mouseMovedY = _mouseY-_lastMouseY;
		_lastMouseX = _mouseX;
		_lastMouseY = _mouseY;
		
		//scroll wheel
		_scrollingUp = false;
		_scrollingDown = false;
		_scrollingAmount = 0;
		
		for (int key = 0; key < _keyAmount; key++) {

			//falling edge -> button is going down, beginning of a button press
			_keyPressed[key] = _lastTick[key] && !_keyHeld[key];
			
			//rising edge -> button is going up, end of a button press
			_keyLowering[key] = !_lastTick[key] && _keyHeld[key];

			//copy keyHeld to LastTick
			_lastTick[key] = _keyHeld[key];
		}
		
	}
	
	@Internal @Override
	public void mouseDragged(MouseEvent e) {
		_mouseX = e.getX();
		_mouseY = e.getY();
	}

	@Internal @Override
	public void mouseMoved(MouseEvent e) {
		_mouseX = e.getX();
		_mouseY = e.getY();
	}

	@Internal @Override
	public void mousePressed(MouseEvent e) {
		int key = e.getButton();
		_keyHeld[key] = true;
	}

	@Internal @Override
	public void mouseReleased(MouseEvent e) {
		int key = e.getButton();
		_keyHeld[key] = false;
	}
	
	@Internal  @Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		_scrollingAmount += e.getPreciseWheelRotation();
		if(_scrollingAmount > 0) {
			_scrollingDown = true;
		}else {
			_scrollingUp = true;
		}
	}

	@Internal @Override
	public void mouseEntered(MouseEvent e) {}

	@Internal @Override
	public void mouseExited(MouseEvent e) {}
	
	@Internal @Override
	public void mouseClicked(MouseEvent e) {}
	
	/**
	 * Get if a certain key is being held down.<br>
	 * Left mouse button is 1, and right mouse button is 3.
	 * @param button <b>(Integer)</b> Code for the key that is being checked.
	 * @return True if key is held down
	 */
	public static boolean getKeyHeld(int button) {
		return _keyHeld[button];
	}
	
	/**
	 * Get if a certain key was pressed down.<br>
	 * Left mouse button is 1, and right mouse button is 3.
	 * @param button <b>(Integer)</b> Code for the key that is being checked.
	 * @return True for one tick if key was pressed down (falling edge).
	 */
	public static boolean getKeyPressed(int button) {
		return _keyPressed[button];
	}
	
	/**
	 * Get if a certain key is beginning to be pressed down.<br>
	 * Left mouse button is 1, and right mouse button is 3.
	 * @param button <b>(Integer)</b> Code for the key that is being checked.
	 * @return True for one tick if key is beginning to be pressed down (rising edge).
	 */
	public static boolean getKeyLowering(int button) {
		return _keyLowering[button];
	}
	
	/**
	 * Get x-position of the mouse cursor relative to the top left of the Screen.
	 * @return (Integer) x-position of the mouse cursor.
	 */
	public static double getX() {
		return (_mouseX / Screen.getScaleFactor());
	}
	
	/**
	 * Get y-position of the mouse cursor relative to the top left of the Screen.
	 * @return (Integer) y-position of the mouse cursor.
	 */
	public static double getY() {
		return (_mouseY / Screen.getScaleFactor());
	}
	
	/**
	 * Get the amount the mouse has moved horizontally since the last tick
	 * @return (double) mouseMovedX
	 */
	public static double getXMoved() {
		return _mouseMovedX;
	}
	
	/**
	 * Get the amount the mouse has moved vertically since the last tick
	 * @return (double) mouseMovedY
	 */
	public static double getYMoved() {
		return _mouseMovedY;
	}
	
	/**
	 * Get position of the mouse cursor relative to the top left of the Screen.
	 * @return (Point) position of the mouse cursor.
	 */
	public static Point getPoint() {
		Point p = new Point();
		p.setLocation((int) (_mouseX/Screen.getScaleFactor()), (int) (_mouseY/Screen.getScaleFactor()));
		return p;
	}
	
	/**
	 * Get whether the scrollwheel has scrolled up in the last frame.
	 * @return True if the scrollwheel scrolled up.
	 */
	public static boolean getScrollingUp() {
		return _scrollingUp;
	}
	
	/**
	 * Get whether the scrollwheel has scrolled down in the last frame.
	 * @return True if the scrollwheel scrolled down.
	 */
	public static boolean getScrollingDown() {
		return _scrollingDown;
	}
	
	/**
	 * Get the amount the scrollwheel has scrolled in the last frame.
	 * It returns a positive value if the mouse scrolled down and positive if the mouse scrolled up.<br>
	 * <u>Note:</u> If you scroll twice in one frame, it will only detect the latter of the 2 scrolls. The change of this happening at a decent framerate is very unlikely.
	 * @return The amount of scrolling the scrollwheel did the last tick.
	 */
	public static double getScrollingAmount() {
		return _scrollingAmount;
	}
	
	/**
	 * Get if the left mouse button is being held down.
	 * @return True if the left mouse button is being held down
	 */
	public static boolean getLeftMouseHeld() {
		return getKeyHeld(1);
	}
	
	/**
	 * Get if the right mouse button is being held down.
	 * @return True if the right mouse button is being held down
	 */
	public static boolean getRightMouseHeld() {
		return getKeyHeld(3);
	}

	/**
	 * Get if the left mouse button is being released.
	 * @return True if the left mouse button has just been released
	 */
	public static boolean getLeftMousePressed() {
		return getKeyPressed(1);
	}
	
	/**
	 * Get if the right mouse button is being released.
	 * @return True if the right mouse button has just been released
	 */
	public static boolean getRightMousePressed() {
		return getKeyPressed(3);
	}
	
	/**
	 * Get if the left mouse button is lowering.
	 * @return True if the left mouse button has just been pressed down
	 * @author BauwenDR
	 */
	public static boolean getLeftMouseLowering() {
		return getKeyLowering(1);
	}
	
	/**
	 * Get if the right mouse button is lowering.
	 * @return True if the right mouse button has just been pressed down
	 * @author BauwenDR
	 */
	public static boolean getRightMouseLowering() {
		return getKeyLowering(3);
	}
}
