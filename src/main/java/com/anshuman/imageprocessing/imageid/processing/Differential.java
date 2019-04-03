package com.anshuman.imageprocessing.imageid.processing;

import ij.process.ImageProcessor;

/**
 * User: Anshuman
 * Date: Oct 15, 2010
 * Time: 4:37:15 PM
 */
public class Differential {
    public final double FLT_EPSILON = (double) Float.intBitsToFloat((int) 0x33FFFFFF);

    void getSplineInterpolationCoefficients(
            double[] c,
            double tolerance
    ) {
        double z[] = {Math.sqrt(3.0) - 2.0};
        double lambda = 1.0;

        if (c.length == 1) {
            return;
        }
        for (int k = 0; (k < z.length); k++) {
            lambda = lambda * (1.0 - z[k]) * (1.0 - 1.0 / z[k]);
        }
        for (int n = 0; (n < c.length); n++) {
            c[n] = c[n] * lambda;
        }
        for (int k = 0; (k < z.length); k++) {
            c[0] = getInitialCausalCoefficientMirrorOnBounds(c, z[k], tolerance);
            for (int n = 1; (n < c.length); n++) {
                c[n] = c[n] + z[k] * c[n - 1];
            }
            c[c.length - 1] = getInitialAntiCausalCoefficientMirrorOnBounds(c, z[k],
                    tolerance);
            for (int n = c.length - 2; (0 <= n); n--) {
                c[n] = z[k] * (c[n + 1] - c[n]);
            }
        }
    } /* end getSplineInterpolationCoefficients */

    double getInitialCausalCoefficientMirrorOnBounds(
            double[] c,
            double z,
            double tolerance
    ) {
        double z1 = z, zn = Math.pow(z, c.length - 1);
        double sum = c[0] + zn * c[c.length - 1];
        int horizon = c.length;

        if (0.0 < tolerance) {
            horizon = 2 + (int) (Math.log(tolerance) / Math.log(Math.abs(z)));
            horizon = (horizon < c.length) ? (horizon) : (c.length);
        }
        zn = zn * zn;
        for (int n = 1; (n < (horizon - 1)); n++) {
            zn = zn / z;
            sum = sum + (z1 + zn) * c[n];
            z1 = z1 * z;
        }
        return (sum / (1.0 - Math.pow(z, 2 * c.length - 2)));
    } /* end getInitialCausalCoefficientMirrorOnBounds */


    double getInitialAntiCausalCoefficientMirrorOnBounds(
            double[] c,
            double z,
            double tolerance
    ) {
        return ((z * c[c.length - 2] + c[c.length - 1]) * z / (z * z - 1.0));
    } /*

    double getInitialCausalCoefficientMirrorOnBounds (
        double[] c,
        double z,
        double tolerance
    ) {
        double z1 = z, zn = Math.pow(z, c.length - 1);
        double sum = c[0] + zn * c[c.length - 1];
        int horizon = c.length;

        if (0.0 < tolerance) {
            horizon = 2 + (int)(Math.log(tolerance) / Math.log(Math.abs(z)));
            horizon = (horizon < c.length) ? (horizon) : (c.length);
        }
        zn = zn * zn;
        for (int n = 1; (n < (horizon - 1)); n++) {
            zn = zn / z;
            sum = sum + (z1 + zn) * c[n];
            z1 = z1 * z;
        }
        return(sum / (1.0 - Math.pow(z, 2 * c.length - 2)));
    } /* end getInitialCausalCoefficientMirrorOnBounds */


    public double[] getVerticalGradient(
            ImageProcessor ip) {
        if (!(ip.getPixels() instanceof float[])) {
            throw new IllegalArgumentException("Float image required");
        }

        int width = ip.getWidth();
        int height = ip.getHeight();
        double line[] = new double[height];
        double[] toR = new double[width];
        for (int x = 0; (x < width); x++) {
            getColumn(ip, x, line);
            getSplineInterpolationCoefficients(line, FLT_EPSILON);
            getGradient(line);
            toR[x] = getSumAbs(line);
        }

        return toR;
    }

    private double getSumAbs(double[] line) {
        double sumAbs = 0;
        for (double aLine : line) {
            sumAbs += Math.abs(aLine);

        }
        return sumAbs;
    }

    void getGradient(double[] c) {
        double h[] = {0.0, -1.0 / 2.0};
        double s[] = new double[c.length];

        antiSymmetricFirMirrorOnBounds(h, c, s);
        System.arraycopy(s, 0, c, 0, s.length);
    } /* end getGradient */

    void antiSymmetricFirMirrorOnBounds(
            double[] h,
            double[] c,
            double[] s
    ) {
        if (h.length != 2) {
            throw new IndexOutOfBoundsException("The half-length filter size should be 2");
        }
        if (h[0] != 0.0) {
            throw new IllegalArgumentException("Antisymmetry violation (should have h[0]=0.0)");
        }
        if (c.length != s.length) {
            throw new IndexOutOfBoundsException("Incompatible size");
        }
        if (2 <= c.length) {
            s[0] = 0.0;
            for (int i = 1; (i < (s.length - 1)); i++) {
                s[i] = h[1] * (c[i + 1] - c[i - 1]);
            }
            s[s.length - 1] = 0.0;
        } else {
            if (c.length == 1) {
                s[0] = 0.0;
            } else {
                throw new NegativeArraySizeException("Invalid length of data");
            }
        }
    } /* end antiSymmetricFirMirrorOnBounds */


    void getColumn(
            ImageProcessor ip,
            int x,
            double[] column
    ) {
        int width = ip.getWidth();

        if (ip.getHeight() != column.length) {
            throw new IndexOutOfBoundsException("Incoherent array sizes");
        }
        if (ip.getPixels() instanceof float[]) {
            float[] floatPixels = (float[]) ip.getPixels();
            for (int i = 0; (i < column.length); i++) {
                column[i] = (double) floatPixels[x];
                x += width;
            }
        } else {
            throw new IllegalArgumentException("Float image required");
        }
    } /* end getColumn */

}
