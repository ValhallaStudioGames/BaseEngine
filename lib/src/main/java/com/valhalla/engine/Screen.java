package com.valhalla.engine;

import java.awt.*;
import java.util.Arrays;

import javax.swing.JFrame;

import com.valhalla.engine.internal.Internal;

/**
 * Class is in charge of the physical screen.
 * @author BauwenDR
 */
public class Screen implements Runnable {
	
	private static boolean _screenRunning = false;
	private static Thread _renderer;
	private static GameLoop _gameloop;

	private static int _currentWindow = 0;
	private static boolean _isFullScreen = false;

	private static boolean _shutDownRequested = false;
	
	private static int _baseWidth;
	private static int _frameWidth,frameHeight;
	private static double _scalefactor = 1;
	
	private static JFrame _frame;
	private static Component _component;
	
	private static double _amountOfTicks = 0;

	private static final GraphicsEnvironment _graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
	
	static boolean _showErrors = false;
	
	@Internal
	public Screen(GameLoop gl, String title, int width, int height) {
		Screen._gameloop = gl;
		_baseWidth = width;
		_frameWidth = width;
		frameHeight = height;
		
		_frame = new JFrame(title);
		
		_frame.setBounds(0, 0, width, height);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.setResizable(false);
		_frame.setLocationRelativeTo(null);
		_frame.setVisible(true);
		_frame.add(gl);
		
		gl.start();
		_component = _frame.getComponent(0);
	}
	
	@Internal
	synchronized void start() {
		_renderer = new Thread(this);
		_renderer.start();
		_renderer.setName("renderer");
		_screenRunning = true;
	}
	
	@Internal
	synchronized static void stop() {
		try {
			_renderer.join();
			_screenRunning = false;
		}catch(Exception e) {
			GameLoop.engineOutput.println(Arrays.toString(e.getStackTrace()));
		}
	}
	
	@Internal
	public synchronized void run() {
	    if(_amountOfTicks == 0) {
	    	resetRefreshRate();
	    }
	    
		long lastTime = System.nanoTime();
		double ns;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(_screenRunning) {
			long now = System.nanoTime();
			ns = 1000000000 / _amountOfTicks;
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >=1) {
				_gameloop.render();
				frames++;
				delta--;
				
				if(System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					GameLoop.engineOutput.println("fps: " + frames);
					frames = 0;
					}

				if(_shutDownRequested) {
					_screenRunning = false;
				}
			}
		}
		
		if(_isFullScreen) {
			exitFullScreen();
		}
		
		_frame.setVisible(false);
		_frame.setEnabled(false);
		_frame.dispose();
		_frame = null;
		
		stop();
	}
	
	@Internal
	public void closeScreen() {
		_shutDownRequested = true;
	}
	
	/**
	 * Resizes the Screen to the new width and height and readjusts the ScaleFactor.
	 * @param width <b>(Integer)</b> New width for the Screen.
	 * @param height <b>(Integer)</b> New height for the Screen.
	 */
	public static void resize(int width, int height) {
		_frameWidth = width;
		frameHeight = height;
		changeSize(width, height);
	}

	@Internal
	private static void changeSize(int width, int height) {
		_frame.setSize(width, height);
		_scalefactor = (double) width / (double) _baseWidth;
	}

	/**
	 * Sets display mode to fullscreen on a monitor of choosing.<br>
	 * <u>note:</u> You can view an array of all displays with {@link #getGraphicsEnvironment}
	 * @param monitor <b>(Integer)</b> number of monitor to display fullscreen frame on.
	 */
	public static void setFullScreen(int monitor){
		_currentWindow = monitor;
		GraphicsDevice fullScreenMonitor = _graphicsEnvironment.getScreenDevices()[_currentWindow];

		int fullScreenScale = Toolkit.getDefaultToolkit().getScreenResolution()+4;
		int fullScreenWidth = fullScreenMonitor.getDisplayMode().getWidth() / fullScreenScale * 100;
		int fullScreenHeight = fullScreenMonitor.getDisplayMode().getHeight() / fullScreenScale * 100;

		_frame.dispose();
		
		_frame.setUndecorated(true);
		changeSize(fullScreenWidth, fullScreenHeight);
		
		setRefreshRate(fullScreenMonitor.getDisplayMode().getRefreshRate());
		_frame.setLocationRelativeTo(null);	
		
		_frame.setVisible(true);
		_gameloop.requestFocus();

		_isFullScreen = true;
	}

	/**
	 * Exits out of fullscreen display if display mode is set to fullscreen
	 */
	public static void exitFullScreen() {
		resetRefreshRate();

		_frame.dispose();
		
		_frame.setUndecorated(false);
		changeSize(_frameWidth, frameHeight);
		_frame.setLocationRelativeTo(null);
		
		_frame.setVisible(true);
		_gameloop.requestFocus();
		
		_isFullScreen = false;
	}
	
	/**
	 * Toggles fullscreen on and off (calls functions {@link #setFullScreen(int)} and {@link #exitFullScreen()} internally.
	 * @param monitor <b>(Integer)</b> number of monitor to display fullscreen frame on.
	 */
	public static void toggleFullScreen(int monitor) {
		if(_isFullScreen) {
			exitFullScreen();
		}else {
			setFullScreen(monitor);
		}
	}
	
	/**
	 * Function that checks if Screen is being displayed in full screen or not
	 * @return isFullScreen (boolean)
	 */
	public static boolean getFullScreen() {
		return _isFullScreen;
	}

	/**
	 * Getter for the current graphics environment, see more {@link java.awt.GraphicsEnvironment}
	 * @return graphicsEnviroment (GraphicsEnviroment)
	 */
	public static GraphicsEnvironment getGraphicsEnvironment() {
		return _graphicsEnvironment;
	}
	
	/**
	 * Sets the icon for the Screen.
	 * @param icon <b>(Image)</b> The new image for the Screen.
	 */
	public static void setIcon(Image icon) {
		_frame.setIconImage(icon);
	}
	
	/**
	 * Getter for the current screen width.
	 * @return width (Integer)
	 */
	public static int getWidth() {
		return _frame.getWidth();
	}
	
	/**
	 * Getter for the current screen height.
	 * @return height (Integer)
	 */
	public static int getHeight() {
		return _frame.getHeight();
	}
	
	
	/**
	 * Getter for the current screen ScaleFactor
	 * @return scalefactor (Double)
	 */
	public static double getScaleFactor() {
		return _scalefactor;
	}
	
	/**
	 * Getter for the underlying JFrame object.<br>
	 * <br>
	 * This allows for further manipulation of the screen.
	 * @return frame (JFrame)
	 */
	public static JFrame getScreen() {
		return _frame;
	}
	
	/**
	 * Getter to see if the Screen rendering thread is running.
	 * @return screenRunning (Boolean)
	 */
	public Boolean getScreenRunning() {
		return _screenRunning;
	}
	
	/**
	 * Getter for the current refreshRate or TPS of the screen.
	 * @return refreshRate (Double)
	 */
	public static double getRefreshRate() {
		return _amountOfTicks;
	}

	/**
	 * Setter for the current refreshRate or TPS of the screen.<br>
	 * if set to -1, the framerate will be unlimited.<br>
	 * <u>note:</u> setting framerate to unlimited is a debug feature as this will cause the video card to work at 100% capacity the whole time.
	 * @param refreshRate (double) new refresh rate
	 */
	public static void setRefreshRate(double refreshRate) {
		if(refreshRate == -1) {
			refreshRate = Double.MAX_VALUE;
		}
		
		_amountOfTicks = refreshRate;
	}

	/**
	 * Sets the refresh rate to the default refresh rate.
	 * The default refresh rate is the refresh rate of a computers primary monitor.
	 */
	public static void resetRefreshRate() {
		setRefreshRate(_graphicsEnvironment.getDefaultScreenDevice().getDisplayMode().getRefreshRate());
	}

	/**
	 * Sets the Cursor type (Image)
	 * @param cursor (Cursor) The new Cursor type.
	 */
	public static void setCursor(Cursor cursor) {
		_component.setCursor(cursor);
	}
	/**
	 * Sets whether the stacktrace gets printed when the render loop encounters an error.<br>
	 * <u>note:</u> this is intended as a debugging feature
	 * @param showErrors if stacktraces should be shown
	 */
	public static void setShowErrorLogs(boolean showErrors) {
		_showErrors = showErrors;
	}
}
