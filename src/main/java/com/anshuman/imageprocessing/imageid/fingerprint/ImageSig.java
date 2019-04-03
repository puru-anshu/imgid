package com.anshuman.imageprocessing.imageid.fingerprint;


import com.anshuman.imageprocessing.imageid.data.ImageSignature;
import com.anshuman.imageprocessing.imageid.data.IntSignature;
import com.anshuman.imageprocessing.imageid.dft2.Dft2d;
import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * User: Anshuman
 * Date: Oct 5, 2010
 * Time: 3:17:17 PM
 */
public final class ImageSig implements ImageSignatureGenerator {


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
        /*int srcWidth = ip.getWidth();
        int srcHeight = ip.getHeight();
        int gcd = gcd(srcWidth, srcHeight);
        ImageProcessor imp = ip.getProcessor();
        imp.setInterpolate(true);
        if ((srcWidth / gcd) == 16 && (srcHeight / gcd) == 9) {
            System.out.println("Aspect ratio is 16:9 " + srcWidth + " X " + srcHeight);
            int x = (int) ((double) srcHeight / 4.5d);
            imp.setRoi(0, x, srcWidth, srcHeight - 2 * x);
            imp = imp.crop();
        } else if ((srcWidth / gcd) == 4 && (srcHeight / gcd) == 3) {
            System.out.println("Aspect ratio is 4:3 " + srcWidth + " X " + srcHeight);
            int h2 = srcHeight / 2;
            imp.setRoi(0, h2 / 2, srcWidth, h2);
            imp = imp.crop();

        } else {
            System.out.println("normal  " + srcWidth + " X " + srcHeight);
        }

        return imp;*/
    }

    private ImageProcessor preprocessImage(String filePath) {
        ImagePlus imagePlus = new ImagePlus(filePath);
        ImageConverter iConv = new ImageConverter(imagePlus);
        iConv.convertToGray8();
        ImageProcessor processor = getCropProcessor(imagePlus);
//        processor.setInterpolate(true);
        processor = processor.resize(128, 128);
//        processor.smooth();
//        new GaussianBlur().blurGaussian(processor, 1, 1, 0.001);
//        processor.gamma(1.0);
        return processor;
    }


    public ImageSignature getSignature(String filePath) {
        return null;
    }


    private int[][] generateBinaryString(ImageProcessor powerSpect) {
        powerSpect.setRoi(0, 0, 21, 21);
        ImageProcessor processor11 = powerSpect.crop();
        float[][] floatArray = processor11.getFloatArray();
        return doFiltering(floatArray);
    }

    private int[][] doFiltering(float[][] vals) {

        int w = vals.length;
        int h = vals[0].length;
        int[][] toR = new int[w - 1][h - 1];
        final int[][] filter = {{-1, 1}, {1, -1}};
        for (int v = 0; v < h - 1; v++) {
            for (int u = 0; u < w - 1; u++) {
                //compute filter result for position (u,v)
                float sum = 0;
                for (int j = 0; j <= 1; j++) {
                    for (int i = 0; i <= 1; i++) {
                        float p = vals[u + i][v + j];
                        //get the corresponding filter coefficient
                        float c = filter[j][i];
                        sum = sum + c * p;
                    }
                }

                toR[u][v] = sum >= 0 ? 1 : 0;
            }
        }

        return toR;
    }


    private FloatProcessor autoCorrCalc(ImageProcessor ip) {
        float[][] floatArray = ip.getFloatArray();
        float[][] autoCorr = new float[floatArray.length][];
        int i = 0;
        for (float[] fls : floatArray) {
            autoCorr[i++] = autocorrelate(fls);
        }
        ip = null;
        return new FloatProcessor(autoCorr);
    }


    public static float[] autocorrelate(float[] input) {
        float mean = 0;
        float sumSq = 0;
        for (float f : input) {
            mean += f;
            sumSq += f * f;
        }
        mean = mean / input.length;

        float[] output = new float[input.length];
        for (int i = 0; i < input.length; i++) {//lag
            for (int j = 0; j < input.length; j++) {//value being correlated
                if ((i + j) < input.length) {
                    output[i] += (input[i + j] - mean) * (input[j] - mean);
                } else {
                    break;
                }
            }
        }
        for (int i = 0; i < output.length; i++) {
            output[i] /= sumSq;
        }
        return output;
    }


    private double[] featureVector(float[][] projections, int[] ptsPerLine) {

        int N = projections.length;
        int D = projections[0].length;
        double[] featureVectors = new double[projections.length];
        double sum = 0.0f;
        double sum_sqd = 0.0f;
        for (int k = 0; k < N; k++) {
            double line_sum = 0.0f;
            double line_sum_sqd = 0.0f;
            int nb_pixels = ptsPerLine[k];
            for (int i = 0; i < D; i++) {
                line_sum += projections[k][i];
                line_sum_sqd += projections[k][i] * projections[k][i];
            }
            featureVectors[k] = (line_sum_sqd / nb_pixels) - (line_sum * line_sum) / (nb_pixels * nb_pixels);
            sum += featureVectors[k];
            sum_sqd += featureVectors[k] * featureVectors[k];
        }
        double mean = sum / N;
        double var = (float) Math.sqrt((sum_sqd / N) - (sum * sum) / (N * N));

        for (int i = 0; i < N; i++) {
            featureVectors[i] = (featureVectors[i] - mean) / var;
        }

        return featureVectors;

    }


}
