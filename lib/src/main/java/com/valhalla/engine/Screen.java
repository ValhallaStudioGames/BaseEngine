package com.valhalla.engine;

import java.awt.*;
import java.util.Arrays;

import javax.swing.JFrame;

import com.valhalla.engine.internal.Internal;
import com.valhalla.engine.screen.ScreenImplementation;

/**
 * Class is in charge of the physical screen.
 * @author BauwenDR
 */
public class Screen implements Runnable {

	//not static variables
	private Thread _renderer;

	//static variables
	private static Screen _screen;
	private static ScreenImplementation _screenImplementation;

	private static GameLoop _gameLoop;

	private static boolean _screenRunning = false;
	private static boolean _shutDownRequested = false;
	private static boolean _showErrors = false; //TODO could maybe be moved to GameLoop?

	private static double _amountOfTicks = 0;

	private static final GraphicsEnvironment _graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

	private Screen() { }

	synchronized static Screen initialiseScreen(GameLoop gameLoop) {
		if(_screen == null) {
			_gameLoop = gameLoop;
			_screen = new Screen();
		}

		return _screen;
	}

	synchronized static void setScreenImplementation(ScreenImplementation implementation) {
		_screenImplementation = implementation;
		_screenImplementation.initialise();
	}

	@Internal
	synchronized void start() {
		_renderer = new Thread(this);
		_renderer.start();
		_renderer.setName("renderer");
		_screenRunning = true;
	}
	
	@Internal
	synchronized void stop() {
		try {
			_renderer.join();
			_screenRunning = false;
		}catch(Exception e) {
			GameLoop.engineOutput.println(Arrays.toString(e.getStackTrace()));
		}
	}
	
	@Internal @Override
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
				_screenImplementation.startDrawCycle();

				try {
					_gameLoop.render();
				}catch (Exception e) {
					if(_showErrors) {
						e.printStackTrace();
					}
					System.out.println("BaseEngine Error " + e.getLocalizedMessage());
				}

				_screenImplementation.endDrawCycle();
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
		
		if(_screenImplementation.getFullScreen()) {
			exitFullScreen();
		}

		_screenImplementation.shutdown();
		stop();
	}
	
	@Internal
	public void closeScreen() {
		_shutDownRequested = true;
	}
	
	/**
	 * Resizes the Screen to the new width and height and readjusts the ScaleFactor.
	 * @param newWidth <b>(Integer)</b> New width for the Screen.
	 * @param newHeight <b>(Integer)</b> New height for the Screen.
	 */
	public static void resize(int newWidth, int newHeight) {
		_screenImplementation.resize(newWidth, newHeight);
	}

	/**
	 * Toggles fullscreen on and off (calls functions {@link #setFullScreen(int)} and {@link #exitFullScreen()} internally).
	 * @param monitor <b>(Integer)</b> number of monitor to display fullscreen frame on.
	 */
	public static void toggleFullScreen(int monitor) {
		_screenImplementation.toggleFullScreen(monitor);
	}

	/**
	 * Sets display mode to fullscreen on a monitor of choosing.<br>
	 * <u>note:</u> You can view an array of all displays with {@link #getGraphicsEnvironment}
	 * @param monitor <b>(Integer)</b> number of monitor to display fullscreen frame on.
	 */
	public static void setFullScreen(int monitor) {
		_screenImplementation.setFullScreen(monitor);
	}

	/**
	 * Exits out of fullscreen display if display mode is set to fullscreen
	 */
	public static void exitFullScreen() {
		_screenImplementation.exitFullScreen();
	}

	/**
	 * Function that checks if Screen is being displayed in full screen or not
	 * @return isFullScreen (boolean)
	 */
	public static boolean getFullScreen() {
		return _screenImplementation.getFullScreen();
	}

	/**
	 * Getter for the current graphics environment, see more {@link java.awt.GraphicsEnvironment}
	 * @return graphicsEnviroment (GraphicsEnviroment)
	 */
	public static GraphicsEnvironment getGraphicsEnvironment() {
		return _graphicsEnvironment;
	}

	public static Frame getScreen() {
		return _screenImplementation.getFrame();
	}
	
	/**
	 * Sets the icon for the Screen.
	 * @param icon <b>(Image)</b> The new image for the Screen.
	 */
	public static void setIcon(Image icon) {
		_screenImplementation.setIcon(icon);
	}
	
	/**
	 * Getter for the current screen width.
	 * @return width (Integer)
	 */
	public static int getWidth() {
		return _screenImplementation.getWidth();
	}
	
	/**
	 * Getter for the current screen height.
	 * @return height (Integer)
	 */
	public static int getHeight() {
		return _screenImplementation.getHeight();
	}
	
	
	/**
	 * Getter for the current screen ScaleFactor
	 * @return scalefactor (Double)
	 */
	public static double getScaleFactor() {
		return _screenImplementation.getScaleFactor();
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

	public static boolean getShowErrorLogs() {
		return _showErrors;
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
