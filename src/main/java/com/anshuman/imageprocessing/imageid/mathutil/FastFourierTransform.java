package com.anshuman.imageprocessing.imageid.mathutil;

/**
 * User: Anshuman
 * Date: Oct 5, 2010
 * Time: 11:29:21 AM
 */
public class FastFourierTransform {
    private int nu;

    public final double[] doFFT(double[] x) {
        // assume n is a power of 2
        int n = x.length;
        double ld = (Math.log(n) / Math.log(2.0));

        if ((double) ((int) ld) - ld != 0) {
            System.out.println("Klasse FastFourierTransformation:");
            System.out.println("Der uebergebene Vektor hat keine laenge von 2 hoch n.");
            System.out.println("Die Laenge ist:" + n + " Der Logarithmus Dualis ist:" + ld);
            return x;
        }
        nu = (int) ld;
        int n2 = n / 2;
        int nu1 = nu - 1;
        double[] xre = new double[n];
        double[] xim = new double[n];
        double[] mag = new double[n2];
        double tr, ti, p, arg, c, s;
        for (int i = 0; i < n; i++) {
            xre[i] = x[i];
            xim[i] = 0.0f;
        }
        int k = 0;

        for (int l = 1; l <= nu; l++) {
            while (k < n) {
                for (int i = 1; i <= n2; i++) {
                    p = bitrev(k >> nu1);
                    arg = 2 * Math.PI * p / n;
                    c = Math.cos(arg);
                    s = Math.sin(arg);
                    tr = xre[k + n2] * c + xim[k + n2] * s;
                    ti = xim[k + n2] * c - xre[k + n2] * s;
                    xre[k + n2] = xre[k] - tr;
                    xim[k + n2] = xim[k] - ti;
                    xre[k] += tr;
                    xim[k] += ti;
                    k++;
                }
                k += n2;
            }
            k = 0;
            nu1--;
            n2 = n2 / 2;
        }
        k = 0;
        int r;
        while (k < n) {
            r = bitrev(k);
            if (r > k) {
                tr = xre[k];
                ti = xim[k];
                xre[k] = xre[r];
                xim[k] = xim[r];
                xre[r] = tr;
                xim[r] = ti;
            }
            k++;
        }
        // calculate Magnitude
        mag[0] = (Math.sqrt(xre[0] * xre[0] + xim[0] * xim[0])) / n;
        for (int i = 1; i < n / 2; i++)
            mag[i] = 2 * (Math.sqrt(xre[i] * xre[i] + xim[i] * xim[i])) / n;
        return mag;
    }

    private int bitrev(int j) {

        int j2;
        int j1 = j;
        int k = 0;
        for (int i = 1; i <= nu; i++) {
            j2 = j1 / 2;
            k = 2 * k + j1 - 2 * j2;
            j1 = j2;
        }
        return k;
    }
}

