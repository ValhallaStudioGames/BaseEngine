package com.valhalla.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.PrintStream;

import com.valhalla.engine.input.KeyInput;
import com.valhalla.engine.input.MouseInput;
import com.valhalla.engine.internal.Internal;
import com.valhalla.engine.io.SoundEffectPlayer;
import com.valhalla.engine.io.SoundInterface;
import com.valhalla.engine.render.Draw;
import com.valhalla.engine.screen.JavaScreen;

/**
 * Entry point of the BaseEngine.<br>
 * This class provides the engine itself along with Screen, Handler and io functionality.
 * @author BauwenDR
 */
public class GameLoop extends Canvas implements Runnable {
	private static final long serialVersionUID = 6896684776789891156L;

	private Thread _gameThread;
	private boolean _running = false;
	private final Handler _handler;
	private static double _tickRate;
	private boolean _shutDownRequested = false;

	private final Screen _screen;
	private final Draw _draw;
	
	private final KeyInput _keyinput;
	private final MouseInput _mouseinput;
	
	private static long _ticksPassed = 0;
	public static PrintStream engineOutput;

	/**
	 * Constructor for a GameLoop, this constructor also constructs a Screen and Handler.<br>
	 * It will internally call the tick and render methods for all classes in the Handler.<br>
	 * <br>
	 * Use the functions in the {@link com.valhalla.engine.render} class in order to draw to the screen. For input use the classes in {@link com.valhalla.engine.input}.
	 * @param title <b>(String)</b> Name for the window that the baseEngine creates.
	 * @param width <b>(Integer)</b> Width for the internal Screen object.
	 * @param height <b>(Integer)</b> Height for the internal Screen object.
	 * @param tickRate <b>(Double)</b> The amount of ticks per second under normal circumstances.
	 * @param engineOutputMethod <b>PrintStream</b> Output stream for engine, stream will not be closed when shutting down engine
	 * @see com.valhalla.engine.render.Draw
	 */
	public GameLoop(String title, int width, int height, double tickRate, PrintStream engineOutputMethod) {
		GameLoop._tickRate = tickRate;
		engineOutput = engineOutputMethod;

		if(engineOutputMethod == null) {	//create closed version of output
			engineOutput = System.out;	//console as default to prevent null pointers
			engineOutput.close();
		}
		
		_handler = new Handler();
		_screen = Screen.getInstance(this);
		Screen.setScreenImplementation(new JavaScreen(this, title, width, height));
		_draw = new Draw();
		
		//initialise input
		this.setFocusTraversalKeysEnabled(false);
		
		_keyinput = new KeyInput();
		_mouseinput = new MouseInput();
		this.addKeyListener(_keyinput);
		this.addMouseListener(_mouseinput);
		this.addMouseMotionListener(_mouseinput);
		
		//initialise OpenAL
		new SoundInterface();
		new SoundEffectPlayer();

		start();
	}
	
	/**
	 * Constructor for a GameLoop, this constructor also constructs a Screen and Handler.<br>
	 * It will internally call the tick and render methods for all classes in the Handler.<br>
	 * <br>
	 * Use the functions in the {@link com.valhalla.engine.render} class in order to draw to the screen. For input use the classes in {@link com.valhalla.engine.input}.<br>
	 * <br>
	 * <b>engineOutputMethod</b> is defaulted to System.out
	 * @param title <b>(String)</b> Name for the window that the baseEngine creates.
	 * @param width <b>(Integer)</b> Width for the internal Screen object.
	 * @param height <b>(Integer)</b> Height for the internal Screen object.
	 * @param tickRate <b>(Double)</b> The amount of ticks per second under normal circumstances.
	 * @see com.valhalla.engine.render.Draw
	 */
	public GameLoop(String title, int width, int height, double tickRate) {
		this(title, width, height, tickRate, System.out);
	}
	
	/**
	 * Constructor for a GameLoop, this constructor also constructs a Screen and Handler.<br>
	 * It will internally call the tick and render methods for all classes in the Handler.<br>
	 * <br>
	 * Use the functions in the {@link com.valhalla.engine.render} class in order to draw to the screen. For input use the classes in {@link com.valhalla.engine.input}.<br>
	 * <br>
	 * <b>tickRate</b> is defaulted to 60.0
	 * <b>engineOutputMethod</b> is defaulted to System.out
	 * @param title <b>(String)</b> Name for the window that the baseEngine creates.
	 * @param width <b>(Integer)</b> Width for the internal Screen object.
	 * @param height <b>(Integer)</b> Height for the internal Screen object.
	 * @see com.valhalla.engine.render.Draw
	 */
	public GameLoop(String title, int width, int height) {
		this(title, width, height, 60.0);
	}
	
	@Internal
	synchronized void start() {
		_gameThread = new Thread(this);
		_gameThread.setName("Main-Ticks");
		_gameThread.start();
		_running = true;
	}
	
	@Internal
	synchronized void stop() {
		try {
			_gameThread.join();
			_running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Internal
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = _tickRate;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		_screen.start();
		
		while(_running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now; 
			while(delta >=1) {
				tick();
				frames++;
				delta--;
				}
				if(System.currentTimeMillis() - timer > 1000) {
					timer += 1000;
					System.out.println("tps: " + frames);
					frames = 0;
				}
				if(_shutDownRequested) {
					_running = false;
				}
			}
		stop();
	}
	
	@Internal
	private void tick() {
		_handler.tick();
		_keyinput.tick();
		_mouseinput.tick();
		_ticksPassed++;
		
		if(_shutDownRequested) {
			_handler.removeImmediateClasses();	//used outside a tick so won't error out
		}
	}
	
	@Internal
	void render() {
		BufferStrategy currentBufferStrategy = this.getBufferStrategy();
		if(currentBufferStrategy == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		try {
			Graphics _graphics = currentBufferStrategy.getDrawGraphics();
			_draw.setGraphics(_graphics);
	
			Draw.fillRect(0,0, Screen.getWidth()+50 ,Screen.getHeight()+50, Color.white);
		
			_handler.render();
		
			_graphics.dispose();
			currentBufferStrategy.show();
		}catch (Exception e) {
			if(Screen._showErrors) {
				e.printStackTrace();
			}else {
				System.out.println("BaseEngine Error " + e.getLocalizedMessage());
			}
		}	
	}
	
	/**
	 * Getter for the draw used to render everything to screen.
	 * @return draw (Draw)
	 */
	public Draw getDraw() {
		return _draw;
	}
	
	/**
	 * Getter for the internal Handler used for rendering and ticking all BaseClasses.
	 * @return handler (Handler)
	 */
	public Handler getHandler() {
		return _handler;
	}
	
	/**
	 * Getter for the amount of gameticks that have passed since startup.
	 * @return ticksPassed (Long)
	 */
	public static long getTicksPassed() {
		return _ticksPassed;
	}
	
	/**
	 * Deconstructs the GameLoop along with underlying handler and screen at the end of a gametick.
	 */
	public void close() {
		_shutDownRequested=true;
		_screen.closeScreen();
		SoundInterface.cleanUp();
	}
	
	/**
	 * Getter for the internal Screen object for the GameLoop class.<br>
	 * <u>Note:</u> Most functions in the Screen class are static functions.
	 * @return screen (Screen)
	 */
	public Screen getScreen() {
		return _screen;
	}
	
	/**
	 * Getter for the current tickrate the GameLoop is running at.<br>
	 * This should be equal to amount specified.
	 * @return tickRate (double)
	 */
	public static double getTickRate() {
		return _tickRate;
	}
}
