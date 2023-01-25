package com.valhalla.engine.render;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.valhalla.engine.Screen;
import com.valhalla.engine.internal.Internal;

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
	
	private static Graphics2D _grapics2D;
	private static double _scaleFactor;
	
	@Internal
	public Draw() {}
	
	@Internal
	public void setGraphics(Graphics graphics) {
		_grapics2D = (Graphics2D) graphics;
	}
	
	/**
	 * Getter for the current Screen ScaleFactor.
	 * @return (Double) ScaleFactor.
	 */
	public static double getScaleFactor() {
		return _scaleFactor;
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
		_scaleFactor = Screen.getScaleFactor();
		_grapics2D.setColor(colour);
		_grapics2D.drawLine((int) (x1 * _scaleFactor), (int) (y1 * _scaleFactor), (int) (x2 * _scaleFactor), (int) (y2 * _scaleFactor));
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
		_scaleFactor = Screen.getScaleFactor();
		_grapics2D.setColor(colour);
		_grapics2D.drawRect((int) (x*_scaleFactor), (int)(y*_scaleFactor) , (int) (width*_scaleFactor) , (int) (height*_scaleFactor));
	}
	
	/**
	 * Draws the border of a rectangle to the screen, adjusted by the ScaleFactor.
	 * @param rectangle <b>(Rectangle)</b> The rectangle of which the border will be drawn.
	 * @param colour <b>(Color)</b> Colour of the border.
	 */
	public static void drawRect(Rectangle rectangle, Color colour) {
		_scaleFactor = Screen.getScaleFactor();
		_grapics2D.setColor(colour);
		_grapics2D.drawRect((int) (rectangle.x * _scaleFactor), (int) (rectangle.y * _scaleFactor), (int) (rectangle.width * _scaleFactor), (int) (rectangle.height * _scaleFactor));
	}

	/**
	 * Draws a rectangle to the screen, adjusted by the ScaleFactor.
	 * @param x <b>(Integer)</b> x-position for the rectangle.
	 * @param y <b>(Integer)</b> y-position for the rectangle.
	 * @param width <b>(Integer)</b> Width of the rectangle.
	 * @param height <b>(Integer)</b> Height of the rectangle.
	 * @param colour <b>(Color)</b> Colour of the Area.
	 */
	public static void fillRect(int x, int y, int width, int height, Color colour) {
		_scaleFactor = Screen.getScaleFactor();
		_grapics2D.setColor(colour);
		_grapics2D.fillRect((int) (x*_scaleFactor), (int)(y*_scaleFactor) , (int) (width*_scaleFactor) , (int) (height*_scaleFactor));
	}
	
	/**
	 * Draws a rectangle to the screen, adjusted by the ScaleFactor.
	 * @param rectangle <b>(Rectangle)</b> The rectangle that will be drawn.
	 * @param colour <b>(Color)</b> Colour of the border.
	 */
	public static void fillRect(Rectangle rectangle, Color colour) {
		_scaleFactor = Screen.getScaleFactor();
		_grapics2D.setColor(colour);
		_grapics2D.fillRect((int) (rectangle.x * _scaleFactor), (int) (rectangle.y * _scaleFactor), (int) (rectangle.width * _scaleFactor), (int) (rectangle.height * _scaleFactor));
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
		_scaleFactor = Screen.getScaleFactor();
		_grapics2D.setColor(colour);
		Font font = new Font(fontName, 0, (int) (fontSize*_scaleFactor));
		_grapics2D.setFont(font);
		_grapics2D.drawString(string, (int) (x*_scaleFactor), (int) (y*_scaleFactor));
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
		_scaleFactor = Screen.getScaleFactor();
		_grapics2D.setColor(colour);
		Font font = new Font(fontName, 0, (int) (fontSize*_scaleFactor));
		_grapics2D.setFont(font);
		
		FontRenderContext frc = new FontRenderContext(null, true, true);
		Rectangle2D r2d = font.getStringBounds(string, frc);
			
		int rWidth = (int) (Math.round(r2d.getWidth()));
		int rHeight = (int) (Math.round(r2d.getHeight()));
		int rX = (int) Math.round(r2d.getX());
		int rY = (int) Math.round(r2d.getY());
			
		int X = (rectangle.width / 2) - (rWidth / 2) - rX;
		int Y = (rectangle.height / 2) - (rHeight / 2) - rY;
			
		int x = rectangle.x + X;
		int y = rectangle.y + Y;
			
		_grapics2D.drawString(string, (int) (x*_scaleFactor), (int) (y*_scaleFactor));
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
		FontRenderContext frc = new FontRenderContext(null, true, true);
		Rectangle2D stringBounds = font.getStringBounds(string, frc);

		BufferedImage stringImage = new BufferedImage((int) stringBounds.getWidth()+1, (int) stringBounds.getHeight()+1, BufferedImage.TRANSLUCENT);
		Graphics2D stringGraphics = stringImage.createGraphics();
		stringGraphics.setFont(font);
		stringGraphics.drawString(string, 0, 0);
		stringGraphics.dispose();

		drawImage(stringImage, x, y);
	}

	/**
	 * Draws a String in an external font to the screen, centred inside a given rectangle, adjusted by ScaleFactor.
	 * @param string <b>(String)</b> String to be drawn.
	 * @param font <b>(Font)</b> Custom font for the String.
	 * @param rectangle <b>(Rectangle)</b> Rectangle in which the String will be centred.
	 * @param colour <b>(Color)</b> Colour of the text.
	 */
	public static void drawCenteredCustomString(String string, Font font, Rectangle rectangle, Color colour) {
		FontRenderContext frc = new FontRenderContext(null, true, true);
		Rectangle2D stringBounds = font.getStringBounds(string, frc);

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

	/**
	 *	Draw an image with its own width and height to the screen adjusted by ScaleFactor
	 *	@param image <b>(BufferedImage)</b> Image to be drawn to the screen.
	 *	@param x <b>(Integer)</b> x-position of the image.
	 *	@param y <b>(Integer)</b> y-position for the image.
	 */
	public static void drawImage(BufferedImage image, int x, int y) {
		drawImage(image, x, y, image.getWidth(), image.getHeight());
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
		_scaleFactor = Screen.getScaleFactor();
		_grapics2D.drawImage(image, (int) (x*_scaleFactor), (int)(y*_scaleFactor) , (int) (width*_scaleFactor) , (int) (height*_scaleFactor), null);
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
	public static void drawCroppedImage(BufferedImage image, int x, int y, int width, int height, int cropX, int cropY, int cropWidth, int cropHeight) {
		_scaleFactor = Screen.getScaleFactor();
		image = image.getSubimage(cropX, cropY, cropWidth, cropHeight);
		_grapics2D.drawImage(image, x, y, width, height, null);
	}
	
	/**
	 * Draws an oval inside a given rectangular area, adjusted by the ScaleFactor.
	 * <u>Note:</u> The x- and y-coords are the top left coordinates for the bounding box, not the position of the ovals centre.
	 * @param x <b>(Integer)</b> x-position of the rectangular area.
	 * @param y <b>(Integer)</b> y-position of the rectangular area.
	 * @param width <b>(Integer)</b> width of the rectangular area.
	 * @param height<b>(Integer)</b> height of the rectangular area.
	 */
	public static void drawOval(int x, int y, int width, int height) {
		_scaleFactor = Screen.getScaleFactor();
		_grapics2D.drawOval((int) (x*_scaleFactor), (int) (y*_scaleFactor), (int) (width*_scaleFactor), (int) (height*_scaleFactor));
	}
	
	/**
	 * Draws a circle inside a given rectangular area, adjusted by the ScaleFactor.
	 * <u>Note:</u> This function uses drawOval internally, so x and y are the position of the top left corner of the bounding box, not the centre of the circle.
	 * @param x <b>(Integer)</b> x-position of the rectangular area.
	 * @param y <b>(Integer)</b> y-position of the rectangular area.
	 * @param diameter <b>(Integer)</b> diameter/ width of the oval
	 */
	public static void drawCircle(int x, int y, int diameter) {
		drawOval(x, y, diameter, diameter);
	}
	
	/**
	 * Sets the opacity for all the functions in Draw.
	 * <u>Note:</u> The alpha value never gets reset inside the library.
	 * @param alpha <b>(float)</b> alpha value from (1 = everything) to (0 = fully transparent).
	 */
	public static void setOpacity(float alpha) {
		_grapics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
	}
}
