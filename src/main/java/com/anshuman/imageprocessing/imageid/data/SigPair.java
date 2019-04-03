package com.anshuman.imageprocessing.imageid.data;

import java.io.Serializable;

/**
 * User: Anshuman
 * Date: Sep 10, 2010
 * Time: 11:41:53 AM
 */
public class SigPair implements Serializable {
    String filePath;
    long sig1;
    long sig2;


    public SigPair(String filePath, long sig1, long sig2) {
        this.filePath = filePath;
        this.sig1 = sig1;
        this.sig2 = sig2;
    }


    public String getFilePath() {
        return filePath;
    }

    public boolean equals(Object obj) {

        if (obj instanceof SigPair) {
            SigPair other = (SigPair) obj;
            int berr = Long.bitCount(this.sig1 ^ other.sig1) + Long.bitCount(this.sig2 ^ other.sig2);
            if (berr < 12) {
                System.out.println(this.filePath + " === " + other.filePath);
                return true;
            }
        }

        return false;
    }

    public double getBerr(SigPair other) {

        double berr = Long.bitCount(this.sig1 ^ other.sig1) + Long.bitCount(this.sig2 ^ other.sig2);
        return berr / 126d;
    }


    public int hashCode() {
        return super.hashCode();
    }

    public boolean isValid() {
        return sig1 != 0 && sig2 != 0;
    }
}
