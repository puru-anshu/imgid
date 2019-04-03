package com.anshuman.imageprocessing.imageid.fingerprint;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * .
 * User: anshu
 * Date: Dec 17, 2010
 * Time: 12:57:04 PM
 */
public class LogPolar {
    private double[][] cartImage;


    private int w;
    private int h;

    private int scaleFactor = 1;

    private double[][] outVals;

    public LogPolar(double[][] cartImage) {
        this.cartImage = cartImage;
        w = cartImage.length;
        h = cartImage[0].length;
        init();
    }

    private void init() {
        int largestdim = Math.max(w, h);
        double cx = w / 2.0d;
        double cy = h / 2.0d;
        double maxradius = Math.sqrt(cx * cx + cy * cy);
        System.out.println("maxradius = " + maxradius);
        int nptsperdim = scaleFactor * largestdim;
        System.out.println("nptsperdim = " + nptsperdim);
        double[] rrange = logspace(0, Math.log10(maxradius), nptsperdim, 10);
        double[] thrange = linspace(0,  Math.PI, nptsperdim);
        outVals = new double[nptsperdim][nptsperdim];
        double centralValue = cartImage[(int) cx][(int) cy];
        for (int j = 0; j < nptsperdim; ++j) {
            outVals[j][0] = centralValue;
        }
        int x, y;
        Rectangle srcRec = new Rectangle(0, 0, w, h);
        for (int j = 0; j < nptsperdim; j++) {
            for (int i = 0; i < nptsperdim; i++) {
                x = (int) Math.round(cx + rrange[j] * Math.cos(thrange[i]));
                y = (int) Math.round(cy + rrange[j] * Math.sin(thrange[i]));
//                System.out.println(String.format("%d %d,%d",j,x,y) );
                if (srcRec.contains(x, y)) {
                    outVals[i][j] += cartImage[x][y];
                } else {
                    outVals[i][j] = 0;
                }
            }
        }
    }


    public double[][] getOutVals() {
        return outVals;
    }

    public static void main(String[] args) throws Exception {
        BufferedImage bufferedImage = ImgOperation.loadImage(new File("d:\\Lena.png"));
        LogPolar logPolar = new LogPolar(ImgOperation.get2dGrey(bufferedImage));
        double[][] pVals = logPolar.getOutVals();
        BufferedImage image = ImgOperation.createGreyImage(pVals);
        ImgOperation.drawWindow(image, true);

    }

    public static double[] logspace(double pmin, double pmax, int Npts) {

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
