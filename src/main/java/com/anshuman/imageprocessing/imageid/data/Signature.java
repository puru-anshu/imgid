package com.anshuman.imageprocessing.imageid.data;

import java.io.Serializable;

/**
 * User: Anshuman
 * Date: Sep 10, 2010
 * Time: 11:41:53 AM
 */
public class Signature implements Serializable {
    String filePath;
    long[] sig1;


    public Signature(String filePath, long[] sig1) {
        this.filePath = filePath;
        this.sig1 = sig1;

    }


    public String getFilePath() {
        return filePath;
    }


    public double getBerr(Signature other) {

        double berr = 0;
        for (int i = 0; i < sig1.length; i++) {
            berr += Long.bitCount(sig1[i] ^ other.sig1[i]);
        }
        return berr * 100d / (sig1.length * 63d);
    }


    public String toString() {
        return String.format("%s : %d %d", filePath, sig1[0], sig1[1]);
    }

}
