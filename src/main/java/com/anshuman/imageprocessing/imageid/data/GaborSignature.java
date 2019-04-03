package com.anshuman.imageprocessing.imageid.data;



/**
 * .
 * User: anshu
 * Date: Nov 19, 2010
 * Time: 12:37:57 PM
 */
public class GaborSignature implements ImageSignature {
    private String filePath;
    //    private int[] signature;
    protected double[] signature;


    public GaborSignature(String filePath, double[] fts) {
        this.filePath = filePath;
        this.signature = fts;
    }

    public String getFilePath() {
        return filePath;
    }

    public int[] getSignature() {
        return null;
    }


    public double getBerr(ImageSignature other) {

        return getDistance(signature, ((GaborSignature) other).signature);

    }

    private static final int M = 5, N = 6; // scale & orientation

    public double getDistance(double[] targetFeatureVector, double[] queryFeatureVector) {
        double distance = 0;
        for (int m = 0; m < M; m++) {
            for (int n = 0; n < N; n++) {
                distance += Math.sqrt(Math.pow(queryFeatureVector[m * N + n * 2] - targetFeatureVector[m * N + n * 2], 2)
                        + Math.pow(queryFeatureVector[m * N + n * 2 + 1] - targetFeatureVector[m * N + n * 2 + 1], 2));
            }
        }

        return distance;
    }


    public boolean isMatching(double val) {
        return true;
    }


}
