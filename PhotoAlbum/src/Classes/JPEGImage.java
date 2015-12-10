/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

/**
 * @author win8.1
 */

import javafx.embed.swing.SwingFXUtils;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.*;
import javax.imageio.stream.*;
import java.util.*;

/**
 * <p>
 * This class provides basic image manupulation routines.
 * This class is essentially a wrapper around a <tt>BufferedImage</tt>, and
 * provides simple methods for reading and writing images in JPEG format,
 * accessing pixel values, and creating new images.
 * </p>
 * <p>
 * There is no range checking either on image coordinates or on pixel
 * colour values. Entering values outside of the allowed ranges may
 * generate runtime exceptions, or result in garbled images.
 * </p>
 *
 * @author <a href="mailto:smx@cs.nott.ac.uk">Steven Mills</a>
 * @version 1.0 (Jan 8 2003)
 */
public class JPEGImage {

    public BufferedImage img;


    /**
     * Default image constructor.
     * <p>
     * Creates an empty JPEGImage. Since this object does not actually
     * contain an image, any attempts to access the image (through get
     * or set methods or write) will result in runtime exceptions.
     */
    public JPEGImage() {

    }

    /**
     * Blank image constructor.
     * <p>
     * Creates a new image with the given dimensions, and
     * all pixels in the image are set to be black. The dimensions
     * should be positive integers.
     *
     * @param width  the width of the image in pixels
     * @param height the height of the image in pixels
     */
    public JPEGImage(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                img.setRGB(x, y, 0xff000000);
            }
        }
    }

    public static JPEGImage FxToJPEG(javafx.scene.image.Image image, int width, int height) {
        JPEGImage jpegImage = new JPEGImage(width, height);
        jpegImage.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        jpegImage.img = SwingFXUtils.fromFXImage(image, null);
        return jpegImage;
    }

    /**
     * Reads a JPEG from a file into an existing JPEGImage.
     * <p>
     * Reading from a file
     * that does not exist, or which does not contain a JPEG image will
     * generate an exception
     *
     * @param filename the file containing the image
     * @throws IOException          indicates a general problem reading the file
     * @throws ImageFormatException indicates that the file is not a JPEG image
     */
    public void read(String filename)
            throws IOException {

        // File ifile = new File(filename);
        InputStream istream = new FileInputStream(filename);
        img = ImageIO.read(istream);

        /**
         JPEGDecodec not supported under SDK6 - 05/10/12

         InputStream istream = new FileInputStream(filename);
         JPEGImageDecoder jpegDec = JPEGCodec.createJPEGDecoder(istream);
         img = jpegDec.decodeAsBufferedImage();
         */
    }

    /**
     * Writes an image to a file in JPEG format.
     * <p>
     * Writing to a file that does not exist, or which is protected
     * will generate an exception
     *
     * @param filename the file to write the image to
     * @throws IOException indicates a general problem reading the file
     */
    public void write(String filename)
            throws IOException {

        // File ofile = new File(filename);
        OutputStream ostream = new FileOutputStream(filename);
        // String formatName = getFormatName(ofile);  
        ImageIO.write(img, "jpg", ostream);

        /**
         JPEGCodec not supported under SDK6 - 05/10/12

         OutputStream ostream = new FileOutputStream(filename);
         JPEGImageEncoder jpegEnc = JPEGCodec.createJPEGEncoder(ostream);
         jpegEnc.encode(img);
         */
    }

    /**
     * Returns the height of the JPEGImage.
     *
     * @return the height of the image in pixels
     */
    public int getHeight() {
        return img.getHeight();

    }

    /**
     * Returns the width of the JPEGImage.
     *
     * @return the width of the image in pixels
     */
    public int getWidth() {
        return img.getWidth();
    }

    /**
     * Returns the red value of the image at the given coordinates.
     * <p>
     * The coordinates should be non-negative and less than the width (x)
     * or height (y) of the image. The red value is returned as an
     * integer in the range [0,255].
     *
     * @param x the horizontal coordinate of the pixel
     * @param y the vertical coordinate of the pixel
     * @return the red value at the given coordinates
     */
    public int getRed(int x, int y) {
        return (img.getRGB(x, y) & 0x00ff0000) >> 16;
    }

    /**
     * Returns the green value of the image at the given coordinates.
     * <p>
     * The coordinates should be non-negative and less than the width (x)
     * or height (y) of the image. The green value is returned as an
     * integer in the range [0,255].
     *
     * @param x the horizontal coordinate of the pixel
     * @param y the vertical coordinate of the pixel
     * @return the green value at the given coordinates
     */
    public int getGreen(int x, int y) {
        return (img.getRGB(x, y) & 0x0000ff00) >> 8;
    }

    /**
     * Returns the blue value of the image at the given coordinates.
     * <p>
     * The coordinates should be non-negative and less than the width (x)
     * or height (y) of the image. The blue value is returned as an
     * integer in the range [0,255].
     *
     * @param x the horizontal coordinate of the pixel
     * @param y the vertical coordinate of the pixel
     * @return the blue value at the given coordinates
     */
    public int getBlue(int x, int y) {
        return (img.getRGB(x, y) & 0x000000ff);
    }

    /**
     * Sets the red value of the image at the given coordinates.
     * <p>
     * The coordinates should be non-negative and less than the width (x)
     * or height (y) of the image. The red value should be an
     * integer in the range [0,255].
     *
     * @param x     the horizontal coordinate of the pixel
     * @param y     the vertical coordinate of the pixel
     * @param value the new red value at the given coordinates
     */
    public void setRed(int x, int y, int value) {
        img.setRGB(x, y, (img.getRGB(x, y) & 0xff00ffff) | (value << 16));
    }

    /**
     * Sets the green value of the image at the given coordinates.
     * <p>
     * The coordinates should be non-negative and less than the width (x)
     * or height (y) of the image. The green value should be an
     * integer in the range [0,255].
     *
     * @param x     the horizontal coordinate of the pixel
     * @param y     the vertical coordinate of the pixel
     * @param value the new green value at the given coordinates
     */
    public void setGreen(int x, int y, int value) {
        img.setRGB(x, y, (img.getRGB(x, y) & 0xffff00ff) | (value << 8));
    }

    /**
     * Sets the blue value of the image at the given coordinates.
     * <p>
     * The coordinates should be non-negative and less than the width (x)
     * or height (y) of the image. The blue value should be an
     * integer in the range [0,255].
     *
     * @param x     the horizontal coordinate of the pixel
     * @param y     the vertical coordinate of the pixel
     * @param value the new blue value at the given coordinates
     */
    public void setBlue(int x, int y, int value) {
        img.setRGB(x, y, (img.getRGB(x, y) & 0xffffff00) | value);
    }

    /**
     * Sets the red, green, and blue values of the image at the
     * given coordinates.
     * <p>
     * The coordinates should be non-negative and less than the width (x)
     * or height (y) of the image. The colour values should be
     * integers in the range [0,255].
     *
     * @param x the horizontal coordinate of the pixel
     * @param y the vertical coordinate of the pixel
     * @param r the new red value at the given coordinates
     * @param g the new green value at the given coordinates
     * @param b the new blue value at the given coordinates
     */
    public void setRGB(int x, int y, int r, int g, int b) {
        img.setRGB(x, y, 0xff000000 | (r << 16) | (g << 8) | b);
    }

    /**
     Returns the format name of the image in the object 'o'.
     'o' can be either a File or InputStream object.
     Returns null if the format is not known - 05/10/12
     */

    /**
     private static String getFormatName(Object o) {
     try {
     // Create an image input stream on the image
     ImageInputStream iis = ImageIO.createImageInputStream(o);

     // Find all image readers that recognize the image format
     Iterator iter = ImageIO.getImageReaders(iis);
     if (!iter.hasNext()) {
     // No readers found
     return null;
     }

     // Use the first reader
     ImageReader reader = (ImageReader)iter.next();

     // Close stream
     iis.close();

     // Return the format name
     return reader.getFormatName();
     } catch (IOException e) {
     }
     // The image could not be read
     return null;
     }
     */
}
