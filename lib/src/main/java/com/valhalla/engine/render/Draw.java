package com.valhalla.engine.render;

import com.valhalla.engine.internal.Internal;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Functions for drawing onto the Screen.<br>
 * <br>
 * <b>Contains:</b>
 * <ul>
 *  <li>{@link #drawRect}</li>
 *  <li>{@link #fillRect}</li>
 *  <li>{@link #drawString}</li>
 *  <li>{@link #drawCenteredString}</li>
 *  <li>{@link #drawCustomString}</li>
 *  <li>{@link #drawCenteredCustomString}</li>
 *  <li>{@link #drawImage}</li>
 *  <li>{@link #drawCroppedImage}</li>
 *  <li>{@link #drawLine}</li>
 *  <li>{@link #setOpacity}</li>
 * </ul>
 * @author BauwenDR
 */
public class Draw {

	private static DrawImplementation _drawImplementation;

	@Internal
	public static void initialise(DrawImplementation drawImplementation) {
		if(_drawImplementation == null) {
			_drawImplementation = drawImplementation;
		}
	}

	/**
	 * Draws a line to the screen, adjusted by the ScaleFactor.
	 * @param x1 <b>(Integer)</b> Starting x-position of the line.
	 * @param y1 <b>(Integer)</b> Starting y-position of the line.
	 * @param x2 <b>(Integer)</b> Ending x-position of the line
	 * @param y2 <b>(Integer)</b> Ending y-position of the line
	 * @param colour <b>(Color)</b> Colour of the text.
	 */
	public static void drawLine(int x1, int y1, int x2, int y2, Color colour) {
		_drawImplementation.drawLine(x1, y1, x2, y2, colour);
	}

	/**
	 * Draws the border of a rectangle to the screen, adjusted by the ScaleFactor.
	 * @param x <b>(Integer)</b> x-position for the rectangle.
	 * @param y <b>(Integer)</b> y-position for the rectangle.
	 * @param width <b>(Integer)</b> Width of the rectangle.
	 * @param height <b>(Integer)</b> Height of the rectangle.
	 * @param colour <b>(Color)</b> Colour of the border.
	 */
	public static void drawRect(int x, int y, int width, int height, Color colour) {
		_drawImplementation.drawRect(x, y, width, height, colour);
	}
	
	/**
	 * Draws the border of a rectangle to the screen, adjusted by the ScaleFactor.
	 * @param rectangle <b>(Rectangle)</b> The rectangle of which the border will be drawn.
	 * @param colour <b>(Color)</b> Colour of the border.
	 */
	public static void drawRect(Rectangle rectangle, Color colour) {
		_drawImplementation.drawRect(rectangle, colour);
	}

	/**
	 * Draws and fills a rectangle to the screen, adjusted by the ScaleFactor.
	 * @param x <b>(Integer)</b> x-position for the rectangle.
	 * @param y <b>(Integer)</b> y-position for the rectangle.
	 * @param width <b>(Integer)</b> Width of the rectangle.
	 * @param height <b>(Integer)</b> Height of the rectangle.
	 * @param colour <b>(Color)</b> Colour of the Area.
	 */
	public static void fillRect(int x, int y, int width, int height, Color colour) {
		_drawImplementation.fillRect(x, y, width, height, colour);
	}
	
	/**
	 * Draws and fills a rectangle to the screen, adjusted by the ScaleFactor.
	 * @param rectangle <b>(Rectangle)</b> The rectangle that will be drawn.
	 * @param colour <b>(Color)</b> Colour of the border.
	 */
	public static void fillRect(Rectangle rectangle, Color colour) {
		_drawImplementation.fillRect(rectangle, colour);
	}

	/**
	 * Draws an oval inside a given rectangular area, adjusted by the ScaleFactor.
	 * <u>Note:</u> The x- and y-coords are the top left coordinates for the bounding box, not the position of the ovals centre.
	 * @param x <b>(Integer)</b> x-position of the rectangular area.
	 * @param y <b>(Integer)</b> y-position of the rectangular area.
	 * @param width <b>(Integer)</b> width of the rectangular area.
	 * @param height<b>(Integer)</b> height of the rectangular area.
	 */
	public static void drawOval(int x, int y, int width, int height, Color colour) {
		_drawImplementation.drawOval(x, y, width, height, colour);
	}

	/**
	 * Draws and fills an oval inside a given rectangular area, adjusted by the ScaleFactor.
	 * <u>Note:</u> The x- and y-coords are the top left coordinates for the bounding box, not the position of the ovals centre.
	 * @param x <b>(Integer)</b> x-position of the rectangular area.
	 * @param y <b>(Integer)</b> y-position of the rectangular area.
	 * @param width <b>(Integer)</b> width of the rectangular area.
	 * @param height<b>(Integer)</b> height of the rectangular area.
	 */
	public static void fillOval(int x, int y, int width, int height, Color colour) {
		_drawImplementation.fillOval(x, y, width, height, colour);
	}

	/**
	 * Draws a circle inside a given rectangular area, adjusted by the ScaleFactor.
	 * <u>Note:</u> This function uses drawOval internally, so x and y are the position of the top left corner of the bounding box, not the centre of the circle.
	 * @param x <b>(Integer)</b> x-position of the rectangular area.
	 * @param y <b>(Integer)</b> y-position of the rectangular area.
	 * @param diameter <b>(Integer)</b> diameter/ width of the oval
	 */
	public static void drawCircle(int x, int y, int diameter, Color colour) {
		_drawImplementation.drawCircle(x, y, diameter, colour);
	}

	/**
	 * Draws and fills a circle inside a given rectangular area, adjusted by the ScaleFactor.
	 * <u>Note:</u> This function uses drawOval internally, so x and y are the position of the top left corner of the bounding box, not the centre of the circle.
	 * @param x <b>(Integer)</b> x-position of the rectangular area.
	 * @param y <b>(Integer)</b> y-position of the rectangular area.
	 * @param diameter <b>(Integer)</b> diameter/ width of the oval
	 */
	public static void fillCircle(int x, int y, int diameter, Color colour) {
		_drawImplementation.fillCircle(x, y, diameter, colour);
	}

	/**
	 * Draws a String to the screen, adjusted by the ScaleFactor.
	 * @param string <b>(String)</b> String to be drawn.
	 * @param x <b>(Integer)</b> x-position for the String.
	 * @param y <b>(Integer)</b> y-position for the String.
	 * @param fontName <b>(String)</b> Name of the font to be used for drawing the String.
	 * @param fontSize <b>(Integer)</b> Size of the font.
	 * @param colour <b>(Color)</b> Colour of the text.
	 */
	public static void drawString(String string, int x, int y, String fontName, int fontSize, Color colour) {
		_drawImplementation.drawString(string, x, y, fontName, fontSize, colour);
	}
	
	/**
	 * Draws a String centred inside a given Rectangle, adjusted by the ScaleFactor.
	 * @param string <b>(String)</b> String to be drawn.
	 * @param rectangle <b>(Rectangle)</b> Rectangle in which the String will be centred.
	 * @param fontName <b>(String)</b> Name of the font to be used for drawing the String.
	 * @param fontSize <b>(Integer)</b> Size of the font.
	 * @param colour <b>(Color)</b> Colour of the text.
	 */
	public static void drawCenteredString(String string, Rectangle rectangle, String fontName, int fontSize, Color colour) {
		_drawImplementation.drawCenteredString(string, rectangle, fontName, fontSize, colour);
	}
	
	/**
	 * Draws a String in an external font to the screen, adjusted by ScaleFactor.
	 * @param string <b>(String)</b> String to be drawn.
	 * @param x <b>(Integer)</b> x-position for the String.
	 * @param y <b>(Integer)</b> y-position for the String.
	 * @param font <b>(Font)</b> Custom font for the String.
	 * @param colour <b>(Color)</b> Colour of the text.
	 */
	public static void drawCustomString(String string, int x, int y, Font font, Color colour) {
		_drawImplementation.drawCustomString(string, x, y, font, colour);
	}

	/**
	 * Draws a String in an external font to the screen, centred inside a given rectangle, adjusted by ScaleFactor.
	 * @param string <b>(String)</b> String to be drawn.
	 * @param font <b>(Font)</b> Custom font for the String.
	 * @param rectangle <b>(Rectangle)</b> Rectangle in which the String will be centred.
	 * @param colour <b>(Color)</b> Colour of the text.
	 */
	public static void drawCenteredCustomString(String string, Font font, Rectangle rectangle, Color colour) {
		_drawImplementation.drawCenteredCustomString(string, font, rectangle, colour);
	}

	/**
	 *	Draw an image with its own width and height to the screen adjusted by ScaleFactor
	 *	@param image <b>(BufferedImage)</b> Image to be drawn to the screen.
	 *	@param x <b>(Integer)</b> x-position of the image.
	 *	@param y <b>(Integer)</b> y-position for the image.
	 */
	public static void drawImage(BufferedImage image, int x, int y) {
		_drawImplementation.drawImage(image, x, y);
	}

	/**
	 * Draw an image to the Screen, adjusted by the ScaleFactor.
	 * @param image <b>(BufferedImage)</b> Image to be drawn to the screen.
	 * @param x <b>(Integer)</b> x-position of the image.
	 * @param y <b>(Integer)</b> y-position for the image.
	 * @param width <b>(Integer)</b> Width of the image.
	 * @param height <b>(Integer)</b> Height of the image.
	 */
	public static void drawImage(BufferedImage image, int x, int y, int width, int height) {
		_drawImplementation.drawImage(image, x, y ,width, height);
	}
	
	/**
	 * Crops an image and draws it to the screen, adjusted by the ScaleFactor.<br>
	 * <u>Note:</u> In most situations it is better to crop the image yourself and then call {@link #drawImage}.
	 * @param image <b>(BufferedImage)</b> Image to be cropped and then drawn to the screen.
	 * @param x <b>(Integer)</b> x-position of the image.
	 * @param y <b>(Integer)</b> y-position for the image.
	 * @param width <b>(Integer)</b> Width of the image.
	 * @param height <b>(Integer)</b> Height of the image.
	 * @param cropX <b>(Integer)</b> x-position from which to start cropping.
	 * @param cropY <b>(Integer)</b> y-position from which to start cropping.
	 * @param cropWidth <b>(Integer)</b> Width of the cropped image.
	 * @param cropHeight <b>(Integer)</b> Height of the cropped image.
	 */
	@Deprecated
	public static void drawCroppedImage(BufferedImage image, int x, int y, int width, int height, int cropX, int cropY, int cropWidth, int cropHeight) {
		image = image.getSubimage(cropX, cropY, cropWidth, cropHeight);
		_drawImplementation.drawImage(image, x, y, width, height);
	}

	/**
	 * Sets the opacity for all the functions in Draw.
	 * <u>Note:</u> The alpha value never gets reset inside the library.
	 * @param alpha <b>(float)</b> alpha value from (1 = everything) to (0 = fully transparent).
	 */
	public static void setOpacity(float alpha) {
		_drawImplementation.setOpacity(alpha);
	}
}
