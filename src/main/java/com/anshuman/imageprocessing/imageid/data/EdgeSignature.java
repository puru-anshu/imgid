package com.anshuman.imageprocessing.imageid.data;


/**
 * .
 * User: anshu
 * Date: Nov 19, 2010
 * Time: 12:37:57 PM
 */
public class EdgeSignature implements ImageSignature {
    private static final int NOF_SUBIMAGE_ROW = 4;

    private static final int NOF_EDGE_TYPE = 5;
    private String filePath;
    private int[] signature;
    private String colorStr;


    public EdgeSignature(String filePath, int[] edgeHisto, String colorStr) {
        this.filePath = filePath;
        this.signature = edgeHisto;
        this.colorStr = colorStr;
    }

    public String getFilePath() {
        return filePath;
    }

    public int[] getSignature() {
        return signature;
    }


    public String getColorStr() {
        return colorStr;
    }

    public double getBerr(ImageSignature other) {
        return compare(signature, other.getSignature());
    }

    public boolean isMatching(double val) {
        return val <= 0.11;
//        return true;
    }

    public float compare(int[] f1, int[] f2) {
        float val = -1f;

        if (f1 == null || f2 == null) {
            System.err.print("One of the Descriptors as its feature Vector null");
            return val;
        }
        val = 0;
        val += histoL1Norm(f1, f2); //LocalHisto
        val += histoL1Norm(getGlobalHisto(f1), getGlobalHisto(f2));
        val += histoL1Norm(getSemiGlobalHisto(f1), getSemiGlobalHisto(f2));

        //This is how we normalize the value between 0 and 1.
        val /= 8 * (80 + 5 * 5 + 13 * 5);
        return val;
    }


    private int histoL1Norm(int[] h1, int[] h2) {
        int val = 0;
        for (int i = 0; i < h1.length; i++) {
            val += Math.abs(h1[i] - h2[i]);
        }
        return val;
    }

    private int[] getGlobalHisto(int[] histo) {
        int[] gHisto = new int[5];
        //Build and return the result
        for (int i = 0; i < histo.length; i++) {
            gHisto[i % NOF_EDGE_TYPE] += histo[i];
        }
        for (int i = 0; i < gHisto.length; i++) {
            gHisto[i] *= 5 / 16; //We have build image 16 by 16 but we put a factor of 5 has suggested in MPEG7.
        }
        return gHisto;
    }

    private int[] getSemiGlobalHisto(int[] histo) {
        //Create the 13 (groups) x 5 (edge type) = 65 bins semi-global histo
        int[] sgHisto = new int[65];

        //Create the 13 groups of 4 neighbor sub-images
        int neighbor[][][] = {
                {{0, 0}, {0, 1}, {0, 2}, {0, 3}},  //Row 1		 	(#01)
                {{1, 0}, {1, 1}, {1, 2}, {1, 3}},  //Row 2		 	(#02)
                {{2, 0}, {2, 1}, {2, 2}, {2, 3}},  //Row 3	 		(#03)
                {{3, 0}, {3, 1}, {3, 2}, {3, 3}},  //Row 4		 	(#04)
                {{0, 0}, {1, 0}, {2, 0}, {3, 0}},  //Column 1	 	(#05)
                {{0, 1}, {1, 1}, {2, 1}, {3, 1}},  //Column 2	 	(#06)
                {{0, 2}, {1, 2}, {2, 2}, {3, 2}},  //Column 3	 	(#07)
                {{0, 3}, {1, 3}, {2, 3}, {3, 3}},  //Column 4	 	(#08)
                {{0, 0}, {0, 1}, {1, 0}, {1, 1}},  //Top-Left	 	(#09)
                {{0, 2}, {0, 3}, {1, 2}, {1, 3}},    //Top-Rigth 	(#10)
                {{2, 0}, {2, 1}, {3, 0}, {3, 1}},    //Bottom-Left 	(#11)
                {{2, 2}, {2, 3}, {3, 2}, {3, 3}},    //Bottom-Right	(#12)
                {{1, 1}, {1, 2}, {2, 1}, {2, 2}}    //Center 		(#13)
        };
        int histoOffset = 0;
        //Iterate on each semi-global subset
        for (int g = 0; g < neighbor.length; g++) {
            //Iterate on each subimage in a subset
            for (int j = 0; j < neighbor[g].length; j++) {
                histoOffset = neighbor[g][j][0] * NOF_EDGE_TYPE; //OffsetPosition in X
                histoOffset += neighbor[g][j][1] * NOF_EDGE_TYPE * NOF_SUBIMAGE_ROW;
                for (int e = 0; e < NOF_EDGE_TYPE; e++) {
                    sgHisto[g * NOF_EDGE_TYPE + e] += histo[e + histoOffset];
                }
            }
        }
        //Finally normalize value between 0 and 7
        for (int i = 0; i < sgHisto.length; i++) {
            sgHisto[i] /= 4; //Because each group consist of 4 subimages
        }
        return sgHisto;
    }


}
