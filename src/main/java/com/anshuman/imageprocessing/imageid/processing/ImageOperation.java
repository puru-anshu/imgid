package com.anshuman.imageprocessing.imageid.processing;

import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.awt.*;

/**
 * User: Anshuman
 * Date: Oct 1, 2010
 * Time: 11:35:50 AM
 */
public class ImageOperation {
    public static int[] getImagePixels(BufferedImage image) {
        PixelGrabber grabber;
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        try {
            grabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
            grabber.grabPixels(0);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return pixels;
    }

    /*public static BufferedImage getImageFromArray(int[] pixels, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(0, 0, width, height, pixels);
        return image;
    }*/

    public static Image getImageFromArray(int[] pixels, int width, int height) {
        MemoryImageSource mis = new MemoryImageSource(width, height, pixels, 0, width);
        Toolkit tk = Toolkit.getDefaultToolkit();
        return tk.createImage(mis);
    }


}
