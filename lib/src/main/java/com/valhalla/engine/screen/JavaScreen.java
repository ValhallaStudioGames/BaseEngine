package com.valhalla.engine.screen;

import com.valhalla.engine.GameLoop;
import com.valhalla.engine.Screen;
import com.valhalla.engine.render.Draw;
import com.valhalla.engine.render.JavaDraw;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class JavaScreen extends ScreenImplementation {

    private static JavaDraw _javaDrawImplementation;

    public JavaScreen(GameLoop gameLoop, String title, int width, int height) {
        super(gameLoop, new JFrame(title), title, width, height);

        _frame.setBounds(0, 0, width, height);
        ((JFrame) _frame).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.setResizable(false);
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
        _frame.add(_gameLoop);
    }

    @Override
    public void startDrawCycle() {
        BufferStrategy currentBufferStrategy = _gameLoop.getBufferStrategy();
        if(currentBufferStrategy == null) {
            _gameLoop.createBufferStrategy(3);
            return;
        }

        _javaDrawImplementation.startDrawCycle((Graphics2D) currentBufferStrategy.getDrawGraphics());
    }

    @Override
    public void endDrawCycle() {
        _gameLoop.getBufferStrategy().show();
    }

    @Override
    public void initialise() {
        _javaDrawImplementation = new JavaDraw();
        Draw.initialise(_javaDrawImplementation);
    }

    @Override
    public void shutdown() {
        _frame.setVisible(false);
        _frame.setEnabled(false);
        _frame.dispose();
    }

    @Override
    public void changeSize(int newWidth, int newHeight) {
        _frame.setSize(newWidth, newHeight);
    }

    @Override
    public void setFullScreen(int monitor) {
        GraphicsDevice fullScreenMonitor = Screen.getGraphicsEnvironment().getScreenDevices()[monitor];

        int fullScreenScale = Toolkit.getDefaultToolkit().getScreenResolution()+4;
        int fullScreenWidth = fullScreenMonitor.getDisplayMode().getWidth() / fullScreenScale * 100;
        int fullScreenHeight = fullScreenMonitor.getDisplayMode().getHeight() / fullScreenScale * 100;

        _frame.dispose();

        _frame.setUndecorated(true);
        resize(fullScreenWidth, fullScreenHeight);

        Screen.setRefreshRate(fullScreenMonitor.getDisplayMode().getRefreshRate());
        _frame.setLocationRelativeTo(null);

        _frame.setVisible(true);
        _gameLoop.requestFocus();

        _isFullScreen = true;
    }

    @Override
    public void exitFullScreen() {
        Screen.resetRefreshRate();

        _frame.dispose();

        _frame.setUndecorated(false);
        resize(_baseWidth, _baseHeight);
        _frame.setLocationRelativeTo(null);

        _frame.setVisible(true);
        _gameLoop.requestFocus();

        _isFullScreen = false;
    }

    @Override
    public void setIcon(Image icon) {
        _frame.setIconImage(icon);
    }
}
