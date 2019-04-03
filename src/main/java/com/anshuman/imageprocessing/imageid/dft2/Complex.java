package com.anshuman.imageprocessing.imageid.dft2;

public class Complex {
    public double re;
    public double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public static Complex[] makeComplexVector(int M) {
        Complex[] g = new Complex[M];
        for (int i = 0; i < M; i++) {
            g[i] = new Complex(0, 0);
        }
        return g;
    }

    public double magnitude() {
        return Math.sqrt(re * re + im * im);
    }

    public static Complex[] makeComplexVector(double[] signal) {
        int M = signal.length;
        Complex[] g = new Complex[M];
        for (int i = 0; i < M; i++) {
            g[i] = new Complex(signal[i], 0);
        }
        return g;
    }

    static Complex[] makeComplexVector(double[] real, double[] imag) {
        int M = real.length;
        Complex[] g = new Complex[M];
        for (int i = 0; i < M; i++) {
            g[i] = new Complex(real[i], imag[i]);
        }
        return g;
    }

    public static void printComplexVector(Complex[] g, String title) {
        System.out.println("Printing " + title);
        for (int i = 0; i < g.length; i++) {
            if (g[i] == null)
                System.out.println(i + ": ******");
            else {
                double gr = g[i].re;
                double gi = g[i].im;
                gr = (Math.rint(gr * 1000) / 1000);
                gi = (Math.rint(gi * 1000) / 1000);
                if (gi >= 0)
                    System.out.println(i + ": " + gr + " + " + Math.abs(gi) + "i " + g[i].magnitude());
                else
                    System.out.println(i + ": " + gr + " - " + Math.abs(gi) + "i " + + g[i].magnitude());
            }
        }
    }
}
