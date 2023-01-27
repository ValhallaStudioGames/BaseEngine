package com.valhalla.engine.render;

import com.valhalla.engine.Screen;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class JavaDraw implements DrawImplementation {

    private static Graphics2D _graphics2D;
    private static double _scaleFactor;

    public void startDrawCycle(Graphics2D graphics2D) {
        _graphics2D = graphics2D;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Color colour) {
        _scaleFactor = Screen.getScaleFactor();
        _graphics2D.setColor(colour);
        _graphics2D.drawLine((int) (x1 * _scaleFactor), (int) (y1 * _scaleFactor), (int) (x2 * _scaleFactor), (int) (y2 * _scaleFactor));
    }

    @Override
    public void drawRect(int x, int y, int width, int height, Color colour) {
        _scaleFactor = Screen.getScaleFactor();
        _graphics2D.setColor(colour);
        _graphics2D.drawRect((int) (x*_scaleFactor), (int)(y*_scaleFactor) , (int) (width*_scaleFactor) , (int) (height*_scaleFactor));
    }

    @Override
    public void fillRect(int x, int y, int width, int height, Color colour) {
        _scaleFactor = Screen.getScaleFactor();
        _graphics2D.setColor(colour);
        _graphics2D.fillRect((int) (x*_scaleFactor), (int)(y*_scaleFactor) , (int) (width*_scaleFactor) , (int) (height*_scaleFactor));
    }

    @Override
    public void drawOval(int x, int y, int width, int height, Color colour) {
        _scaleFactor = Screen.getScaleFactor();
        _graphics2D.drawOval((int) (x*_scaleFactor), (int) (y*_scaleFactor), (int) (width*_scaleFactor), (int) (height*_scaleFactor));
    }

    @Override
    public void fillOval(int x, int y, int width, int height, Color colour) {
        _scaleFactor = Screen.getScaleFactor();
        _graphics2D.fillOval((int) (x*_scaleFactor), (int) (y*_scaleFactor), (int) (width*_scaleFactor), (int) (height*_scaleFactor));
    }

    @Override
    public void drawString(String string, int x, int y, String fontName, int fontSize, Color colour) {
        Font font = new Font(fontName, Font.PLAIN, (int) (fontSize*_scaleFactor));
        _graphics2D.setFont(font);
        _graphics2D.drawString(string, (int) (x*_scaleFactor), (int) (y*_scaleFactor));
    }

    @Override
    public void drawCenteredString(String string, Rectangle rectangle, String fontName, int fontSize, Color colour) {
        _scaleFactor = Screen.getScaleFactor();
        _graphics2D.setColor(colour);
        Font font = new Font(fontName, Font.PLAIN, (int) (fontSize*_scaleFactor));
        _graphics2D.setFont(font);

        FontRenderContext fontRenderContext = new FontRenderContext(null, true, true);
        Rectangle2D stringBounds = font.getStringBounds(string, fontRenderContext);

        int rWidth = (int) (Math.round(stringBounds.getWidth()));
        int rHeight = (int) (Math.round(stringBounds.getHeight()));
        int rX = (int) Math.round(stringBounds.getX());
        int rY = (int) Math.round(stringBounds.getY());

        int X = (rectangle.width / 2) - (rWidth / 2) - rX;
        int Y = (rectangle.height / 2) - (rHeight / 2) - rY;

        int x = rectangle.x + X;
        int y = rectangle.y + Y;

        _graphics2D.drawString(string, (int) (x*_scaleFactor), (int) (y*_scaleFactor));
    }

    @Override
    public void drawCustomString(String string, int x, int y, Font font, Color colour) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D stringBounds = font.getStringBounds(string, frc);

        BufferedImage stringImage = new BufferedImage((int) stringBounds.getWidth()+1, (int) stringBounds.getHeight()+1, BufferedImage.TRANSLUCENT);
        Graphics2D stringGraphics = stringImage.createGraphics();
        stringGraphics.setFont(font);
        stringGraphics.drawString(string, 0, 0);
        stringGraphics.dispose();

        drawImage(stringImage, x, y);
    }

    @Override
    public void drawCenteredCustomString(String string, Font font, Rectangle rectangle, Color colour) {
        FontRenderContext fontRenderContext = new FontRenderContext(null, true, true);
        Rectangle2D stringBounds = font.getStringBounds(string, fontRenderContext);

        //draw string as image
        int rWidth = (int) (Math.round(stringBounds.getWidth()));
        int rHeight = (int) (Math.round(stringBounds.getHeight()));
        int rX = (int) Math.round(stringBounds.getX());
        int rY = (int) Math.round(stringBounds.getY());

        int X = (rectangle.width / 2) - (rWidth / 2) - rX;
        int Y = (rectangle.height / 2) - (rHeight / 2) - rY;

        int x = rectangle.x + X;
        int y = rectangle.y + Y;

        BufferedImage stringImage = new BufferedImage((int) stringBounds.getWidth()+1, (int) stringBounds.getHeight()+1, BufferedImage.TRANSLUCENT);
        Graphics2D stringGraphics = stringImage.createGraphics();
        stringGraphics.setFont(font);
        stringGraphics.drawString(string, 0, 0);
        stringGraphics.dispose();

        drawImage(stringImage, (int) (x*_scaleFactor), (int) (y*_scaleFactor));

    }

    @Override
    public void drawImage(BufferedImage image, int x, int y, int width, int height) {
        _scaleFactor = Screen.getScaleFactor();
        _graphics2D.drawImage(image, (int) (x*_scaleFactor), (int)(y*_scaleFactor) , (int) (width*_scaleFactor) , (int) (height*_scaleFactor), null);
    }

    @Override
    public void setOpacity(float alpha) {
        _graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
    }
}
