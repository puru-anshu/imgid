package com.anshuman.imageprocessing.imageid.imageutil;

/**
 * User: Anshuman
 * Date: Oct 2, 2010
 * Time: 10:56:24 AM
 */
public class MathUtil {

    public static float[][] rowsToColumns(float[][] indata) {
        int cols = indata[0].length;
        int rows = indata.length;
        float[][] outdata = new float[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                outdata[j][i] = indata[i][j];
            }
        }

        return outdata;
    }
}
