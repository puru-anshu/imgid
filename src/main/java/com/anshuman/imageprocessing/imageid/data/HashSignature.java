package com.anshuman.imageprocessing.imageid.data;

/**
 * .
 * User: anshu
 * Date: Nov 19, 2010
 * Time: 12:37:57 PM
 */
public class HashSignature implements ImageSignature {
    private String filePath;
    private int[] signatures;


    public HashSignature(String filePath, int[] signatures) {
        this.filePath = filePath;
        this.signatures = signatures;
    }

    public String getFilePath() {
        return filePath;
    }

    public int[] getSignature() {
        return signatures;
    }

    public double getBerr(ImageSignature other) {
        return 1 - crossCorrelate(signatures, other.getSignature());
    }

    public boolean isMatching(double val) {
        return val <= 0.1;
    }


    private double crossCorrelate(int[] x, int[] y) {
        int N = y.length;
        double[] r = new double[N];
        double sumx = 0.0;
        double sumy = 0.0;
        for (int i = 0; i < N; i++) {
            sumx += x[i];
            sumy += y[i];
        }
        double meanx = sumx / N;
        double meany = sumy / N;
        double max = 0;
        for (int d = 0; d < N; d++) {
            double num = 0.0;
            double denx = 0.0;
            double deny = 0.0;
            for (int i = 0; i < N; i++) {
                num += (x[i] - meanx) * (y[(N + i - d) % N] - meany);
                denx += Math.pow((x[i] - meanx), 2);
                deny += Math.pow((y[(N + i - d) % N] - meany), 2);
            }
            r[d] = num / Math.sqrt(denx * deny);
            if (r[d] > max)
                max = r[d];
        }
        return max;
    }
}
