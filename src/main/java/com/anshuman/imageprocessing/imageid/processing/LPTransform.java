package com.anshuman.imageprocessing.imageid.processing;


import com.anshuman.imageprocessing.imageid.fingerprint.ImgOperation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * .
 * User: anshu
 * Date: Dec 29, 2010
 * Time: 11:59:39 AM
 */
public class LPTransform {
    private BufferedImage sourceImage;   // original image
    private BufferedImage logPolarImage; // transformed image
    private Rectangle imageRect;         // rectangle holding image dimensions
    private float cx, cy;                // image centre coordinates
    private float dr, dtheta;            // increments in r and theta
    private float[] ctheta;              // lookup table for cos
    private float[] stheta;              // lookup table for sin


    public LPTransform(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
        init();
        createLogPolarImage();
    }

    /**
     * function output = logpolar(input)
     * oRows = size(input, 1);
     * oCols = size(input, 2);
     * dTheta = 2*pi / oCols; % the step size for theta
     * b = 10 ^ (log10(oRows) / oRows); % base for the log-polar conversion
     * for i = 1:oRows % rows
     * for j = 1:oCols % columns
     * r = b ^ i - 1; % the log-polar
     * theta = j * dTheta;
     * x = round(r * cos(theta) + size(input,2) / 2);
     * y = round(r * sin(theta) + size(input,1) / 2);
     * if (x>0) & (y>0) & (x<size(input,2)) & (y<size(input,1))
     * output(i,j) = input(y,x);
     * end
     * end
     * end
     */
    public BufferedImage createLogPolarImage() {

        int oRows = sourceImage.getWidth();
        int oCols = sourceImage.getHeight();
        dtheta = (float) Math.PI * 2 / oCols;
        double b = Math.pow(10, (Math.log10(oRows) / oRows));
        logPolarImage =
                new BufferedImage(oRows, oCols, sourceImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : sourceImage.getType());

        /* // Fill first ring (r = 0) with data
        int centralValue = sourceImage.getRGB(Math.round(cx), Math.round(cy));
        for (int j = 0; j < numSectors; ++j) {
            logPolarImage.setRGB(j, 0, centralValue);
        }*/

        // Fill rings with r > 0

     /*   cx = oRows / 2;
        cy = oCols / 2;*/
        double r, theta;
        for (int i = 0; i < oRows; i++) {
            for (int j = 0; j < oCols; j++) {
                r = Math.pow(b, i);
                theta = (j + 1) * dtheta;
                int x = (int) Math.round(r * Math.cos(theta) + cy);
                int y = (int) Math.round(r * Math.sin(theta) + cx);
                if (x > 0 && y > 0 && x < oCols && y < oRows) {
                    logPolarImage.setRGB(i, j, sourceImage.getRGB(y, x));
                }

            }
        }
        return logPolarImage;
    }

    public static double[] logspace(double pmin, double pmax, int Npts) {

        double[] ret = new double[Npts];
        for (int jj = 0; jj < Npts; jj++) {
            ret[jj] = Math.pow(10, pmin + jj * (pmax - pmin) / (Npts - 1.));
        }
        return ret;
    }


    private double[] rrange;

    private void init() {
        imageRect = new Rectangle(sourceImage.getWidth(), sourceImage.getHeight());
        cx = sourceImage.getWidth() / 2.0f;
        cy = sourceImage.getHeight() / 2.0f;

        /* // Compute r and theta increments
                rrange = logspace(Math.log10(1), Math.log10(Math.sqrt(cx * cx + cy * cy)), numRings);
                dr = (float) (Math.sqrt(cx * cx + cy * cy) / numRings);
                dtheta = 360.0f / numSectors;
                // Compute lookup tables for cos and sin

                ctheta = new float[numSectors];
                stheta = new float[numSectors];
                double theta = 0.0, dt = dtheta * (Math.PI / 180.0);
                for (int j = 0; j < numSectors; ++j, theta += dt) {
                    ctheta[j] = (float) Math.cos(theta);
                    stheta[j] = (float) Math.sin(theta);
                }
        */
    }


    public BufferedImage getLogPolarImage() {
        return logPolarImage;
    }

    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File("d:\\Lena.png"));
        LPTransform logPolar = new LPTransform(img);
        BufferedImage image = logPolar.getLogPolarImage();
        ImgOperation.drawWindow(image);
    }

}


