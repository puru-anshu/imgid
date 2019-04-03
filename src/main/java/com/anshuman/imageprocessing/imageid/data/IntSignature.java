package com.anshuman.imageprocessing.imageid.data;

/**
 * User: Anshuman
 * Date: Sep 10, 2010
 * Time: 11:41:53 AM
 */
public class IntSignature implements ImageSignature {
    String filePath;
    int[] sig1;


    public IntSignature(String filePath, int[] sig1) {
        this.filePath = filePath;
        this.sig1 = sig1;

    }


    public String getFilePath() {
        return filePath;
    }

    public int[] getSignature() {
        return sig1;
    }


    public double getBerr(ImageSignature other) {
        double berr = 0;
        int[] signature = other.getSignature();
        for (int i = 0; i < sig1.length; i++) {
            berr += Integer.bitCount(sig1[i] ^ signature[i]);
        }
        return berr * 100d / (sig1.length * 31d);
    }

    public boolean isMatching(double val) {
        return val < 20;
    }


    public String toString() {
        return String.format("%s : %d %d", filePath, sig1[0], sig1[1]);
    }

}
