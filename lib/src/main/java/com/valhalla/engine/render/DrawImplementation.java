package com.valhalla.engine.render;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface DrawImplementation {

    void drawLine(int x1, int y1, int x2, int y2, Color colour);

    void drawRect(int x, int y, int width, int height, Color colour);
    void fillRect(int x, int y, int width, int height, Color colour);

    void drawOval(int x, int y, int width, int height, Color colour);
    void fillOval(int x, int y, int width, int height, Color colour);

    void drawString(String string, int x, int y, String fontName, int fontSize, Color colour);
    void drawCenteredString(String string, Rectangle rectangle, String fontName, int fontSize, Color colour);
    void drawCustomString(String string, int x, int y, Font font, Color colour);
    void drawCenteredCustomString(String string, Font font, Rectangle rectangle, Color colour);

    void drawImage(BufferedImage image, int x, int y, int width, int height);

    void setOpacity(float alpha);

    //default implementations
    default void drawRect(Rectangle rectangle, Color colour) {
        drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, colour);
    }

    default void fillRect(Rectangle rectangle, Color colour) {
        fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height, colour);
    }

    default void drawCircle(int x, int y, int diameter, Color colour) {
        drawOval(x, y, diameter, diameter, colour);
    }

    default void fillCircle(int x, int y, int diameter, Color colour) {
        fillOval(x, y, diameter, diameter, colour);
    }

    default void drawImage(BufferedImage image, int x, int y) {
        drawImage(image, x, y, image.getWidth(), image.getHeight());
    }

}
