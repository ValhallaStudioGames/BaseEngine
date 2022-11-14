package com.valhalla.engine;

import java.awt.image.BufferedImage;

import com.valhalla.engine.internal.Internal;

public class Animation {

	private Handler _handler;
	
	private int _animationFrame;
	private long _frameRate, _ticksPassed = 0;
	private BufferedImage[] _animationFrames;
	
	private boolean _isPlaying;
	
	/**
	 * Creates a new animation that will update the animationFrame according to the specified framerate.
	 * This constructor does not start the animation. In order to start the animation, see {@link #start(Handler)}.
	 * @see #start(Handler)
	 * @see #stop()
	 * @see #getAnimationFrame()
	 * @see #getCurrentAnimationFrame()
	 * @param animationFrames <b>(BufferedImage[])</b> pointer to an array that contains all animation frames
	 * @param frameRate <b>(Integer)</b> the framerate for the animation formatted in frames per second (make sure value is below tickrate for optimal performance)
	 * @author BauwenDR
	 */
	public Animation(BufferedImage[] animationFrames, int frameRate) {
		this._animationFrames = animationFrames;
		this._frameRate = (long) (GameLoop.getTickRate() / frameRate);
		_isPlaying = false;
	}
	
	/**
	 * Creates a new animation that will update the animationFrame according to the specified framerate.
	 * This constructor will automatically start the animation.
	 *  @see #start(Handler)
	 * @see #stop()
	 * @see #getAnimationFrame()
	 * @see #getCurrentAnimationFrame()
	 * @param animationFrames <b>(BufferedImage[])</b> pointer to an array that contains all animation frames
	 * @param frameRate <b>(Integer)</b> the framerate for the animation formatted in frames per second (make sure value is below tickrate for optimal performance)
	 * @author BauwenDR
	 */
	public Animation(Handler handler, BufferedImage[] animationFrames, int frameRate) {
		this._animationFrames = animationFrames;
		this._frameRate = (long) (GameLoop.getTickRate() / frameRate);
		start(handler);
	}
	
	/**
	 * Starts the animation if it is was not already playing.
	 * @see #stop()
	 * @param handler <b>(Handler)</b> The handler which will tick the Animation
	 * @author BauwenDR
	 */
	public void start(Handler handler) {
		this._handler = handler;
		if(!_isPlaying) {
			handler.addAnimation(this);
			_isPlaying = true;
		}
	}
	
	/**
	 * Stops the animation if it is playing.
	 * @see #start(Handler)
	 * @author BauwenDR
	 */
	public void stop() {
		if(_isPlaying) {
			_handler.removeAnimation(this);
			_isPlaying = false;
		}
	}
	
	
	/**
	 * If an animation is playing this function will be called to update the currently displayed frame. (thus the @Internal)<br>
	 * If you want to control the flow of the animation yourself (which is not recommended), you would need to call this function every tick.
	 * @see #start(Handler)
	 * @author BauwenDR
	 */
	@Internal
	public void tick() {
		if(_ticksPassed%_frameRate == 0 && _ticksPassed > 0) {
			_animationFrame++;
			if(_animationFrame == _animationFrames.length) {
				_animationFrame = 0;
			}
		}
		_ticksPassed++;
	}
	
	/**
	 * Getter for the total amount of images in the animation.
	 * @return animationFramesLength (Integer)
	 * @author BauwenDR
	 */
	public int getAnimationLength() {
		return _animationFrames.length;
	}
	
	/**
	 * Getter for the current frame the animation
	 * @return animationFrame (Integer)
	 * @author BauwenDR
	 */
	public int getCurrentAnimationFrame() {
		return _animationFrame;
	}
	
	/**
	 * Getter for the current image of the animation
	 * @return animationFrame (BufferedImage)
	 * @author BauwenDR
	 */
	public BufferedImage getAnimationFrame() {
		return _animationFrames[_animationFrame];
	}
}
