package com.anshuman.imageprocessing.imageid.fingerprint;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * .
 * User: anshu
 * Date: Dec 17, 2010
 * Time: 12:57:04 PM
 */
public class LogPolar2 {
    private double[][] cartImage;


    private int w;
    private int h;

//    private int scaleFactor = 1;

    private double[][] outVals;


    private double rp(int x, int y, int px, int py) {
        return Math.log(Math.sqrt(((double) ((px - x) * (px - x) + (py - y) * (py - y)))));
    }


    private double theta(int x, int y, int px, int py) {
        return Math.atan2((double) (py - y), (double) (px - x));
    }

    private int x(double r, double t, int px) {
        return (int) Math.floor((double) (px + Math.exp(r) * Math.cos(t)));
    }

    private int y(double r, double t, int py) {
        return (int) Math.floor((double) (py + Math.exp(r) * Math.sin(t)));
    }

    private double max2(double a, double b) {
        return a > b ? a : b;
    }

    private double min2(double a, double b) {
        return a > b ? b : a;
    }

    private double max4(double a, double b, double c, double d) {
        return max2(max2(a, b), max2(c, d));
    }

    private double min4(double a, double b, double c, double d) {
        return min2(min2(a, b), min2(c, d));
    }

    public LogPolar2(double[][] cartImage) {
        this.cartImage = cartImage;
        w = cartImage.length;
        h = cartImage[0].length;
        System.out.println(w + " X  " + h);
        init();
    }

    private void init() {

        int px, py, i, j;
        int pixelx, pixely;
        int op = 1;

        double rxy, rxY, rXy, rXY, rmin, rmax, r;
        double tmin, tmax, t;

        px = w / 2;
        py = h / 2;
        tmin = -1.0 * Math.PI;
        tmax = 1.0 * Math.PI;
        rmax = rmin = 0.0;

        rxy = rp(0, py, px, py);
        rXy = rp(w - 1, py, px, py);
        rxY = rp(px, h - 1, px, py);
        rXY = rp(px, 0, px, py);
        rmax = max4(rxy, rXy, rxY, rXY);


        outVals = new double[w][h];
        // inverse mapping
        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {

                if ((i == 0) && (j == 0)) continue; // singularity

                t = tmin + (tmax - tmin) * j / (double) (w);
                r = rmin + (rmax - rmin) * i / (double) (h);

                pixelx = x(r, t, px);
                pixely = y(r, t, py);


                try {
                    if ((pixelx >= 0) && (pixelx < w) && (pixely >= 0) && (pixely < h)) {
                        outVals[j][i] = cartImage[pixelx][pixely];
                    } else {
                        outVals[j][i] = 0;
                    }
                } catch (Exception e) {
                    System.err.println(pixelx + " "+ pixely);
                    throw new ArrayIndexOutOfBoundsException();
                }


            }
        }

    }

    public double[][] getOutVals() {
        return outVals;
    }

    public static void main
            (String[] args) throws Exception {
        BufferedImage bufferedImage = ImgOperation.loadImage(new File("d:\\Lena.png"));
        LogPolar2 logPolar = new LogPolar2(ImgOperation.get2dGrey(bufferedImage));
        double[][] pVals = logPolar.getOutVals();
        BufferedImage image = ImgOperation.createGreyImage(pVals);
        ImgOperation.drawWindow(image, true);

    }

    public static double[] logspace
            (
                    double pmin,
                    double pmax,
                    int Npts) {

        double[] ret = new double[Npts];
        for (int jj = 0; jj < Npts; jj++) {
            ret[jj] = Math.pow(10, pmin + jj * (pmax - pmin) / (Npts - 1.));
        }
        return ret;
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
    public static strictfp double[] logspace
            (
                    double d1,
                    double d2,
                    int n,
                    double base) {
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
    public static strictfp double[] linspace
            (
                    double d1,
                    double d2,
                    int n) {

        double[] y = new double[n];
        double dy = (d2 - d1) / (n - 1);
        for (int i = 0; i < n; i++) {
            y[i] = d1 + (dy * i);
        }

        return y;

    }


}
