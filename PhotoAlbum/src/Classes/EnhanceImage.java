/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.Arrays;


/**
 * @author Tran Trong Tin
 */
public class EnhanceImage {
    public EnhanceImage(JPEGImage jpegImage) {
    }

    /**
     * Create an empty image. We will read an image from a file into
     * this object
     *
     * @param name name of the image
     * @return the created image
     */
    public static JPEGImage input(String name) {
        JPEGImage image = new JPEGImage();

	/* Try to open the file. This may cause an exception if the name 
       given is not a valid JPEG file so we need to catch the exceptions */
        try {
            image.read(name);
        } catch (Exception e) {
        /* An exception has been thrown. This is usually because the file
	       either does not exist, or is not a JPEG image */
            System.out.println("Error reading file " + name);
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return image;
    }

    /**
     * Write the new image out to a file. Again exceptions might occur
     *
     * @param image the output image file
     * @param name  name of the image file
     */
    public static void output(JPEGImage image, String name) {
        try {
            image.write(name);
        } catch (Exception e) {
            System.out.println("Error writing file " + name);
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static JPEGImage toBlackWhite(JPEGImage image) {
        JPEGImage output = new JPEGImage(image.getWidth(), image.getHeight());
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
                int grey = (image.getRed(x, y) + image.getGreen(x, y) + image.getGreen(x, y)) / 3;
                output.setRGB(x, y, grey, grey, grey);
            }
        return output;
    }

    public static JPEGImage toNegative(JPEGImage image) {
        JPEGImage output = new JPEGImage(image.getWidth(), image.getHeight());
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
                output.setRGB(x, y, 255 - image.getRed(x, y), 255 - image.getGreen(x, y), 255 - image.getBlue(x, y));
            }
        return output;
    }

    public static JPEGImage toBlur(JPEGImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int dx, dy;
        final double DOMAIN_FILTER = 1.4;
        final long RANGE_FILTER = 30;
        final double E = Math.E;
        double weight[][] = new double[width][height];
        double SumPixel;
        JPEGImage imageTwo = new JPEGImage(width, height);
        //-----------------
        /* Initialize */
        for (int i = 0; i < width; i++) Arrays.fill(weight[i], 0);
        System.out.println(width + " " + height);
	/* Calculate Bilateral Filter */
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                SumPixel = 0;
                for (int Dx = x - 3; Dx <= x + 3; Dx++)
                    for (int Dy = y - 3; Dy <= y + 3; Dy++) {
                        if (x == Dx && y == Dy) continue;
                        if (Dx < 0 || Dx >= width) dx = 2 * x - Dx;
                        else dx = Dx;
                        if (Dy < 0 || Dy >= height) dy = 2 * y - Dy;
                        else dy = Dy;
                        double value = -((dx - x) * (dx - x) + (dy - y) * (dy - y)) / (2 * DOMAIN_FILTER * DOMAIN_FILTER)
                                - Math.pow(image.getRed(x, y) - image.getRed(dx, dy), 2)
                                / (2 * RANGE_FILTER * RANGE_FILTER);
                        weight[x][y] += Math.pow(E, value);
                        SumPixel += image.getRed(dx, dy) * Math.pow(E, value);
                    }
                int Bpixel = (int) (SumPixel / weight[x][y]);
                imageTwo.setRGB(x, y, Bpixel, Bpixel, Bpixel);
                //System.out.println();
            }
        return image;
    }

    public static JPEGImage toContrast(JPEGImage image) {
        JPEGImage imageThree = new JPEGImage(image.getWidth(),
                image.getHeight());
        double scale_factor = 255.0 / (image.getWidth() * image.getHeight());
        int sumRed = 0, sumGreen = 0, sumBlue = 0;
        int R_histogram[] = new int[256], red[] = new int[256];
        int G_histogram[] = new int[256], green[] = new int[256];
        int B_histogram[] = new int[256], blue[] = new int[256];
        Arrays.fill(R_histogram, 0);
        Arrays.fill(G_histogram, 0);
        Arrays.fill(B_histogram, 0);
        //Compute histogram value of RGB
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
                R_histogram[image.getRed(x, y)]++;
                G_histogram[image.getGreen(x, y)]++;
                B_histogram[image.getBlue(x, y)]++;
            }
	/*Histogram equalization*/
        for (int i = 0; i < R_histogram.length; i++) {
            sumRed += R_histogram[i];
            sumGreen += G_histogram[i];
            sumBlue += B_histogram[i];
            //---------
            red[i] = (int) (sumRed * scale_factor);
            green[i] = (int) (sumGreen * scale_factor);
            blue[i] = (int) (sumBlue * scale_factor);
            //---------
            if (red[i] > 255) red[i] = 255;
            if (green[i] > 255) green[i] = 255;
            if (blue[i] > 255) blue[i] = 255;
        }
        /* Copy and proceed the pixel information from image that was read in to the
	   new image */
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
                int r = red[image.getRed(x, y)];
                int g = green[image.getGreen(x, y)];
                int b = blue[image.getBlue(x, y)];
                imageThree.setRGB(x, y, r, g, b);
            }
        return imageThree;
    }

    //==============================================================================
    public static void main(String args[]) {
        /* Check that the user has provided the right number of arguments */
        if (args.length != 2) {
            System.out.println("Usage: java JPEGCopy <source JPEG file> " +
                    "<target JPEG file>");
            System.exit(1);
        }
        //-----------------Var declare
        JPEGImage imageOne = input(args[0]);
        int width = imageOne.getWidth();
        int height = imageOne.getHeight();
        int dx, dy;
        final double DOMAIN_FILTER = 1.4;
        final long RANGE_FILTER = 30;
        final double E = Math.E;
        double weight[][] = new double[width][height];
        double SumPixel;
        JPEGImage imageTwo = new JPEGImage(width, height);
        //-----------------
        /* Initialize */
        for (int i = 0; i < width; i++) Arrays.fill(weight[i], 0);
        System.out.println(width + " " + height);
	/* Calculate Bilateral Filter */
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                SumPixel = 0;
                for (int Dx = x - 3; Dx <= x + 3; Dx++)
                    for (int Dy = y - 3; Dy <= y + 3; Dy++) {
                        if (x == Dx && y == Dy) continue;
                        if (Dx < 0 || Dx >= width) dx = 2 * x - Dx;
                        else dx = Dx;
                        if (Dy < 0 || Dy >= height) dy = 2 * y - Dy;
                        else dy = Dy;
                        double value = -((dx - x) * (dx - x) + (dy - y) * (dy - y)) / (2 * DOMAIN_FILTER * DOMAIN_FILTER)
                                - Math.pow(imageOne.getRed(x, y) - imageOne.getRed(dx, dy), 2)
                                / (2 * RANGE_FILTER * RANGE_FILTER);
                        weight[x][y] += Math.pow(E, value);
                        SumPixel += imageOne.getRed(dx, dy) * Math.pow(E, value);
                    }
                int Bpixel = (int) (SumPixel / weight[x][y]);
                imageTwo.setRGB(x, y, Bpixel, Bpixel, Bpixel);
                //System.out.println();
            }
        output(imageTwo, args[1]);
    }
}
