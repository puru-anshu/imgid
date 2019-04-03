package com.anshuman.imageprocessing.imageid.fingerprint;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

/**
 * .
 * User: anshu
 * Date: Dec 17, 2010
 * Time: 12:57:04 PM
 */
public class HoughTr {
    private double[][] cartImage;


    private int w;
    private int h;

    final int maxTheta = 256;
    final double thetaStep = Math.PI / maxTheta;
    int doubleHeight;
    private double[][] outVals;

    public HoughTr(double[][] cartImage) {
        this.cartImage = cartImage;
        w = cartImage.length;
        h = cartImage[0].length;
        init();
    }

    private void init() {
        // Calculate the maximum height the hough array needs to have
        int houghHeight = (int) (Math.sqrt(2) * Math.max(h, w)) / 2;
        doubleHeight = 2 * houghHeight;
        outVals = new double[maxTheta][doubleHeight];
        for (double[] dArr : outVals) {
            Arrays.fill(dArr, 0d);
        }
        int centerX = w / 2;
        int centerY = h / 2;

        double[] sinCache = new double[maxTheta];
        double[] cosCache = new double[maxTheta];
        for (int t = 0; t < maxTheta; t++) {
            double realTheta = t * thetaStep;
            sinCache[t] = Math.sin(realTheta);
            cosCache[t] = Math.cos(realTheta);
        }
        // Now find edge points and update the hough array
        for (int x = 0; x < w; x++) {

            for (int y = 0; y < h; y++) {
                // Go through each value of theta
                for (int t = 0; t < maxTheta; t++) {
                    //Work out the r values for each theta step
                    int r = (int) (((x - centerX) * cosCache[t]) + ((y - centerY) * sinCache[t]));
                    // this copes with negative values of r
                    r += houghHeight;
                    if (r < 0 || r >= doubleHeight) continue;
                    outVals[t][r] += cartImage[x][y];
                }
            }
        }
    }

    public double[][] getOutVals() {
        return outVals;
    }

    public static void main(String[] args) throws Exception {
        BufferedImage bufferedImage = ImgOperation.loadImage(new File("d:\\Lena.png"));
        HoughTr logPolar = new HoughTr(ImgOperation.get2dGrey(bufferedImage));
        double[][] pVals = logPolar.getOutVals();
        BufferedImage image = ImgOperation.createGreyImage(pVals);
        ImgOperation.drawWindow(image, true);

    }


    /**
     * generates n logarithmically-spaced points between d1 and d2 using the
     * provided base.
     *
     * @param d1   The min value
     * @param d2   The max value
     * @param n    The number of points to generated
     * @param base the logarithmic base to use
     * @return an array of lineraly space points.
     */
    public static strictfp double[] logspace(double d1, double d2, int n, double base) {
        double[] y = new double[n];
        double[] p = linspace(d1, d2, n);
        for (int i = 0; i < y.length - 1; i++) {
            y[i] = Math.pow(base, p[i]);
        }
        y[y.length - 1] = Math.pow(base, d2);
        return y;
    }

    /**
     * generates n linearly-spaced points between d1 and d2.
     *
     * @param d1 The min value
     * @param d2 The max value
     * @param n  The number of points to generated
     * @return an array of lineraly space points.
     */
    public static strictfp double[] linspace(double d1, double d2, int n) {

        double[] y = new double[n];
        double dy = (d2 - d1) / (n - 1);
        for (int i = 0; i < n; i++) {
            y[i] = d1 + (dy * i);
        }

        return y;

    }


}
