package com.valhalla.engine.screen;

import com.valhalla.engine.GameLoop;
import com.valhalla.engine.internal.Internal;

import java.awt.*;

public abstract class ScreenImplementation {

    protected final Frame _frame;

    protected GameLoop _gameLoop;
    protected String _title;

    protected boolean _shutDownRequested = false;

    protected boolean _isFullScreen = false;

    protected final int _baseWidth;
    protected final int _baseHeight;
    protected int _frameWidth;
    protected int _frameHeight;

    public ScreenImplementation(GameLoop gameLoop, Frame frame, String title, int width, int height) {
        this._gameLoop = gameLoop;
        this._frame = frame;
        this._title = title;
        this._baseWidth = width;
        this._baseHeight = height;
        this._frameWidth = width;
        this._frameHeight = height;
    }

    @Internal
    public void closeScreen() {
        _shutDownRequested = true;
    }

    public void resize(int width, int height) {
        this._frameWidth = width;
        this._frameHeight = height;
        changeSize(width, height);
    }

    public void toggleFullScreen(int monitor) {
        if(_isFullScreen) {
            exitFullScreen();
        }else {
            setFullScreen(monitor);
        }
    }

    // Abstract functions
    //public abstract void initialise();
    public abstract void shutdown();
    public abstract void changeSize(int newWidth, int newHeight);
    public abstract void setFullScreen(int monitor);
    public abstract void exitFullScreen();
    public abstract void setIcon(Image icon);

    //Getters and setters
    public boolean getFullScreen() {
        return _isFullScreen;
    }

    public int getWidth() {
        return _frameWidth;
    }

    public int getHeight() {
        return _frameHeight;
    }

    public double getScaleFactor() {
        return (double) _frameWidth/_baseWidth;
    }

    public Frame getFrame() {
        return _frame;
    }
}
