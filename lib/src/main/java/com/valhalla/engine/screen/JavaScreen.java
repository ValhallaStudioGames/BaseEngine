package com.valhalla.engine.screen;

import com.valhalla.engine.GameLoop;
import com.valhalla.engine.Screen;

import javax.swing.*;
import java.awt.*;

public class JavaScreen extends ScreenImplementation {


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
