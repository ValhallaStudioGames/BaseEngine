package com.valhalla.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.valhalla.engine.internal.Internal;

/**
 * Class used by GameLoop to detect keyboard input. Most functions in this class are static.<br>
 * <br>
 * <b>Contains:</b>
 * <ul>
 * 	<li>{@link #getKeyHeld}</li>
 *  <li>{@link #getKeyPressed}</li>
 *  <li>{@link #wHeld}</li>
 *  <li>{@link #aHeld}</li>
 *  <li>{@link #sHeld}</li>
 *  <li>{@link #dHeld}</li>
 *  <li>{@link #spaceHeld}</li>
 *  <li>{@link #shiftHeld}</li>
 *  <li>{@link #enterPressed}</li>
 * </ul>
 * @author BauwenDR
 */
public class KeyInput implements KeyListener{
	
	private static final int _keyAmount = 255;
	private static final boolean[] _keyHeld = new boolean[_keyAmount];
	private static final boolean[] _keyPressed = new boolean[_keyAmount];
	private static final boolean[] _keyLowering = new boolean[_keyAmount];
	private static final boolean[] _lastTick = new boolean[_keyAmount];
	
	@Internal
	public KeyInput() {
		for(int key = 0; key < _keyAmount; key++) {
			_keyHeld[key] = false;
			_keyPressed[key] = false;
			_lastTick[key] = false;
		}
	}
	
	@Internal
	public void tick() {
		for (int key = 0; key < _keyAmount; key++) {
			
			_keyPressed[key] = false;
			_keyLowering[key] = false;

			//falling edge -> button is going down, beginning of a button press
			_keyPressed[key] = _lastTick[key] && !_keyHeld[key];

			//rising edge -> button is going up, end of a button press
			_keyLowering[key] = !_lastTick[key] && _keyHeld[key];

			_lastTick[key] = _keyHeld[key];	//copy keyHeld to LastTick
		}
	}
	
	@Internal
	public void keyPressed(KeyEvent e) {
		int key = e.getExtendedKeyCode();
		_keyHeld[key] = true;
	}
	
	@Internal
	public void keyReleased(KeyEvent e) {
		int key = e.getExtendedKeyCode();
		_keyHeld[key] = false;
	}
	
	@Internal
	public void keyTyped(KeyEvent e) {}

	/**
	 * Get if a certain key is being held down.<br>
	 * Keycodes can be found as constants in {@link java.awt.event.KeyEvent}
	 * @param keyCode <b>(Integer)</b> Code for the key that is being checked.
	 * @return True if key is held down.
	 */
	public static boolean getKeyHeld(int keyCode) {
		return _keyHeld[keyCode];
	}
	
	/**
	 * Get if a certain key was pressed down.<br>
	 * Keycodes can be found as constants in {@link java.awt.event.KeyEvent}
	 * @param keyCode <b>(Integer)</b> Code for the key that is being checked.
	 * @return True for one tick if key was pressed down (falling edge).
	 */
	public static boolean getKeyPressed(int keyCode) {
		return _keyPressed[keyCode];
	}
	
	/**
	 * Get if a certain key is beginning to be pressed down.<br>
	 * Keycodes can be found as constants in {@link java.awt.event.KeyEvent}
	 * @param keyCode <b>(Integer)</b> Code for the key that is being checked.
	 * @return True for one tick if key is beginning to be pressed down (rising edge).
	 */
	public static boolean getKeyLowering(int keyCode) {
		return _keyLowering[keyCode];
	}
	
	/**
	 * Method for commonly used key.
	 * @return True if 'W' key is held down.
	 */
	public static boolean wHeld() {
		return _keyHeld[KeyEvent.VK_W];
	}
	
	/**
	 * Method for commonly used key.
	 * @return True if 'A' key is held down.
	 */
	public static boolean aHeld() {
		return _keyHeld[KeyEvent.VK_A];
	}
	
	/**
	 * Method for commonly used key.
	 * @return True if 'S' key is held down.
	 */
	public static boolean sHeld() {
		return _keyHeld[KeyEvent.VK_S];
	}
	
	/**
	 * Method for commonly used key.
	 * @return True if 'D' key is held down.
	 */
	public static boolean dHeld() {
		return _keyHeld[KeyEvent.VK_D];
	}
	
	/**
	 * Method for commonly used key.
	 * @return True if 'SPACE' key is held down.
	 */
	public static boolean spaceHeld() {
		return _keyHeld[KeyEvent.VK_SPACE];
	}
	
	/**
	 * Method for commonly used key.
	 * @return True if 'SHIFT' key is held down.
	 */
	public static boolean shiftHeld() {
		return _keyHeld[KeyEvent.VK_SHIFT];
	}
	
	/**
	 * Method for commonly used key.
	 * @return True if 'ENTER' was pressed down.
	 */
	public static boolean enterPressed() {
		return _keyPressed[KeyEvent.VK_ENTER];
	}
}
