package com.anshuman.imageprocessing.imageid.processing;

import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

/**
 * User: Anshuman
 * Date: Oct 14, 2010
 * Time: 11:51:12 AM
 */
public class LogTransform {


    private int[] lut;

    public LogTransform() {
        lut = new int[256];
        for (int i = 0; i < 256; i++) {
            double logscale = 255 / Math.log(255 + 1.0);
            int processed = (int) (logscale * Math.log((float) (i + 1.0)));
            if (processed > 255) processed = 255;
            if (processed < 0) processed = 0;
            lut[i] = processed;
        }

    }


    public byte[] applyTransform(byte[] pixels) {
        byte[] output = new byte[pixels.length];

        for (int i = 0; i < pixels.length; i++) {
            output[i] = (byte) lut[pixels[i] & 0xff];
        }
        return output;
    }

    public int[] applyTransform(int[] pixels) {
        int[] output = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            int c = pixels[i];
            int red = (c & 0xff0000) >> 16;
            int green = (c & 0x00ff00) >> 8;
            int blue = (c & 0x0000ff);
            red = lut[red & 0xff];
            green = lut[green & 0xff];
            blue = lut[blue & 0xff];
            output[i] = ((red & 0xff) << 16) + ((green & 0xff) << 8) + (blue & 0xff);
        }
        return output;
    }


    public ImageProcessor createProcessed8bitImage(FloatProcessor img) {
        ImageProcessor processor = img.convertToByte(true);
        byte[] processed_pixels = (byte[]) processor.getPixels();
        byte[] output = applyTransform(processed_pixels);
        System.arraycopy(output, 0, processed_pixels, 0, output.length);
        return processor;
    }


    public void createProcessedRGBImage(ImagePlus img) {
        
        int[] processed_pixels = (int[]) (img.getProcessor()).getPixels();
        int[] output = applyTransform(processed_pixels);
        System.arraycopy(output, 0, processed_pixels, 0, output.length);
    }
}

