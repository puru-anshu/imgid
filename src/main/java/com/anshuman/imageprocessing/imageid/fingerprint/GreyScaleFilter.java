package com.anshuman.imageprocessing.imageid.fingerprint;

import java.awt.image.RGBImageFilter;

/**
 * .
 * User: anshu
 * Date: Jan 10, 2011
 * Time: 5:14:24 PM
 */
public class GreyScaleFilter extends RGBImageFilter {

    protected boolean canFilterIndexColorModel = true;

    public int filterRGB(int x, int y, int rgb) {

        int red = rgb & 0x00FF0000;
        red >>>= 16;
        int green = rgb & 0x0000FF00;
        green >>>= 8;
        int blue = rgb & 0x0000FF;
        int grey = (red + green + blue) / 3;
        return
                (0x000000FF << 24) | (grey << 16) | (grey << 8) | grey;

    }

}