package com.anshuman.imageprocessing.imageid.fingerprint;


import com.anshuman.imageprocessing.imageid.data.ImageSignature;
import com.anshuman.imageprocessing.imageid.data.IntSignature;
import com.anshuman.imageprocessing.imageid.mathutil.BIJstats;
import com.anshuman.imageprocessing.imageid.mathutil.FFTN;
import ij.ImagePlus;
import ij.plugin.filter.Binary;
import ij.plugin.filter.GaussianBlur;
import ij.plugin.ContrastEnhancer;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.awt.*;
import java.io.File;

/**
 * User: Anshuman
 * Date: Oct 5, 2010
 * Time: 3:17:17 PM
 */
public final class MPImageSig implements ImageSignatureGenerator {


    private static Rectangle run(ImageProcessor ip) {

        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;

        for (int y = 0; y < ip.getHeight(); y++) {
            for (int x = 0; x < ip.getWidth(); x++) {
                if (ip.get(x, y) == 255) {
                    maxY = y;
                    break;
                }
            }
        }

        for (int x = 0; x < ip.getWidth(); x++) {
            for (int y = 0; y < ip.getHeight(); y++) {
                if (ip.get(x, y) == 255) {
                    maxX = x;
                    break;
                }
            }
        }

        for (int y = ip.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < ip.getWidth(); x++) {
                if (ip.get(x, y) == 255) {
                    minY = y;
                    break;
                }
            }
        }


        for (int x = ip.getWidth() - 1; x >= 0; x--) {
            for (int y = 0; y < ip.getHeight(); y++) {
                if (ip.get(x, y) == 255) {
                    minX = x;
                    break;
                }
            }
        }
        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }


    public static ImageProcessor getCropProcessor(ImagePlus ip) {
        ImageProcessor processor1 = ip.getProcessor().duplicate();
        processor1.autoThreshold();
        Rectangle rectangle = run(processor1);
        ImageProcessor processor = ip.getProcessor();
        processor.setRoi(rectangle);
        processor = processor.crop();
        return processor;

    }

    private ImageProcessor preprocessImage(String filePath) {
        ImagePlus imagePlus = new ImagePlus(filePath);
        ImageConverter iConv = new ImageConverter(imagePlus);
        iConv.convertToGray8();
        ImageProcessor processor = getCropProcessor(imagePlus);
        processor.setInterpolate(true);
        processor = processor.resize(256);
        processor.filter(ImageProcessor.MEDIAN_FILTER);
        new GaussianBlur().blur(processor, 8d);
        return processor;

    }


    public ImageSignature getSignature(String filePath) {
        System.out.println("MPImageSig.getSignature");
        int[] sig = new int[4];
        long t = System.currentTimeMillis();
        ImageProcessor ip = preprocessImage(filePath);
        FloatProcessor houghProcessor = new LinearHT(ip, 256, 256).getAccumulatorImage();
        float[][] floatArray = houghProcessor.getFloatArray();
        int i = 0;
        int width = houghProcessor.getWidth();
        double[] dArr = new double[width];
        double[] dArr2 = new double[width];
        int height = houghProcessor.getHeight();
        float[] cols = new float[height];
        for (i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cols[j] = floatArray[i][j];//houghProcessor.getf(i, j);
            }
            dArr[i] = BIJstats.var(cols);
            dArr2[i] = BIJstats.sem(cols);
        }
        FFTN fftn = new FFTN(256);
        new FFTN(256).fft(dArr);
        fftn.fft(dArr2);
        double[] spec1 = FFTN.PowerSpectrum(dArr);
        double[] spec2 = FFTN.PowerSpectrum(dArr2);
        StringBuilder b = new StringBuilder();
        StringBuilder b1 = new StringBuilder();
        for (i = 0; i < 64; i++) {
            if (spec1[i] - spec1[i + 1] <= 0)
                b.append('0');
            else
                b.append('1');

            if (spec2[i] - spec2[i + 1] <= 0)
                b1.append('0');
            else
                b1.append('1');

        }
        sig[0] = Integer.parseInt(b.substring(0, 31), 2);
        sig[1] = Integer.parseInt(b.substring(32, 63), 2);
        sig[2] = Integer.parseInt(b1.substring(0, 31), 2);
        sig[3] = Integer.parseInt(b1.substring(32, 63), 2);

        return new IntSignature(filePath, sig);

    }

}
