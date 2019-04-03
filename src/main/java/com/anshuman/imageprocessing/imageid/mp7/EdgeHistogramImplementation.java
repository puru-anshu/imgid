package com.anshuman.imageprocessing.imageid.mp7;

import java.awt.image.BufferedImage;
import java.util.StringTokenizer;

/**
 * .
 * User: anshu
 * Date: Dec 7, 2010
 * Time: 3:52:55 PM
 */
public class EdgeHistogramImplementation {
    public static final int BIN_COUNT = 80;
    private int[] bins = new int[80];
    private int treshold = 11;
    private double width;
    private double height;
    private int num_block = 1100;

    /**
     * Constant used in case there is no edge in the image.
     */
    private static final int NoEdge = 0;

    /**
     * Constant used in case there is a vertical edge.
     */
    private static final int vertical_edge = 1;

    /**
     * Constant used in case there is a horizontal edge.
     */
    private static final int horizontal_edge = 2;

    /**
     * Constant used in case there is no directional edge.
     */
    private static final int non_directional_edge = 3;

    /**
     * Constant used in case there is a diagonal of 45 degree.
     */
    private static final int diagonal_45_degree_edge = 4;

    /**
     * Constant used in case there is a digaonal of 135 degree.
     */
    private static final int diagonal_135_degree_edge = 5;

    /**
     * The grey level that has been found after converting from RGB.
     */
    private double[][] grey_level;

    /**
     * The bins have to be quantized with help of this quantization table.
     * The first row is used for the vertical bins,
     * the second for the horizontal bins,
     * the third for the 45 degree bins,
     * the fourth for the 135 degree and the last for the non-directional edges.
     */

    public static double[][] QuantTable =
            {{0.010867, 0.057915, 0.099526, 0.144849, 0.195573, 0.260504, 0.358031, 0.530128},
                    {0.012266, 0.069934, 0.125879, 0.182307, 0.243396, 0.314563, 0.411728, 0.564319},
                    {0.004193, 0.025852, 0.046860, 0.068519, 0.093286, 0.123490, 0.161505, 0.228960},
                    {0.004174, 0.025924, 0.046232, 0.067163, 0.089655, 0.115391, 0.151904, 0.217745},
                    {0.006778, 0.051667, 0.108650, 0.166257, 0.224226, 0.285691, 0.356375, 0.450972}};

    /**
     * Array, where the bins are saved before they have been quantized.
     */


    private double Local_Edge_Histogram[] = new double[80];
    private int blockSize = -1;
    private BufferedImage image;

    /**
     * The actual edge histogram.
     */
    protected int[] edgeHistogram;


    public EdgeHistogramImplementation(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();

        extractFeature();
        edgeHistogram = setEdgeHistogram();
    }


    /**
     * Responsible for setting the Bin Counts in view of the position of the bins.
     *
     * @param pos   position of the bins
     * @param value value of the bin
     * @throws ArrayIndexOutOfBoundsException is necessary, since the bins are stored in an array
     */

    public void setBinCounts(int pos, int[] value) throws ArrayIndexOutOfBoundsException {
        bins[pos] = value[pos];
    }


    public int getBinCounts() throws Exception {
        int i = 0;
        return bins[i];
    }

    /**
     * If the maximum of the five edge strengths is greater than a thrshold, then the image block is
     * considered to habe the corresponding edge in it. Otherwise, the image block contains no edge.
     * The default value is 11.
     */

    public void setThreshold() {
        treshold = 11;
    }

    public int getThreshold() {
        return treshold;
    }

    /**
     * The image is splitted into 16 local regions, each of them is divided into a fixed number
     * of image_blocks, depending on width and height of the image.
     * The size of this image_block is here computed.
     *
     * @return b_size
     */
    private int getblockSize() {
        if (blockSize < 0) {
            double a = (int) (Math.sqrt((width * height) / num_block));
            blockSize = (int) (Math.floor((a / 2)) * 2);
            if (blockSize == 0)
                blockSize = 2;
        }
        return blockSize;
    }

    /**
     * The width and height of the media is taken to convert the RGB values into luminance values.
     * returns returns the grey_level of the pixel
     */

    public void makeGreyLevel() {
        grey_level = new double[(int) width][(int) height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grey_level[x][y] = getYfromRGB(image.getRGB(x, y));
            }
        }

    }


    public int[] getEdgeHistogram() {

        return edgeHistogram;
    }

    /**
     * The image_block is divided into four sub-blocks, for these the luminance mean values
     * are used for the edge detection later.
     * This is the first sub-block.
     *
     * @param i indices from the image_block
     * @param j indices from the image_block
     * @return returns the average brightness of the first sub-block.
     */

    private double getFirstBlockAVG(int i, int j) {
        double average_brightness = 0;
        if (grey_level[i][j] != 0) {

            for (int m = 0; m <= (getblockSize() >> 1) - 1; m++) {
                for (int n = 0; n <= (getblockSize() >> 1) - 1; n++) {
                    average_brightness = average_brightness + grey_level[i + m][j + n];
                }
            }
        } else {
            System.err.println("Grey level not initialized.");
        }
        double bs = getblockSize() * getblockSize();
        double div = 4 / bs;
        average_brightness = average_brightness * div;
        return average_brightness;
    }


    /**
     * The image_block is divided into four sub-blocks, for these the luminance mean values
     * are used for the edge detection later.
     * This is the second sub-block.
     *
     * @param i indices from the image_block
     * @param j indices from the image_block
     * @return returns the average brightness of the seconc sub-block.
     */


    private double getSecondBlockAVG(int i, int j) {
        double average_brightness = 0;
        if (grey_level[i][j] != 0)

            for (int m = (getblockSize() >> 1); m <= getblockSize() - 1; m++) {
                for (int n = 0; n <= (getblockSize() >> 1) - 1; n++) {
                    average_brightness += grey_level[i + m][j + n];


                }
            }
        else {
            System.err.println("Grey level not initialized.");
        }
        double bs = getblockSize() * getblockSize();
        double div = 4 / bs;
        average_brightness = average_brightness * div;
        return average_brightness;
    }


    /**
     * The image_block is divided into four sub-blocks, for these the luminance mean values
     * are used for the edge detection later.
     * This is the third sub-block.
     *
     * @param i indices from the image_block
     * @param j indices from the image_block
     * @return returns the average brightness of the third sub-block.
     */

    private double getThirdBlockAVG(int i, int j) {
        double average_brightness = 0;
        if (grey_level[i][j] != 0) {

            for (int m = 0; m <= (getblockSize() >> 1) - 1; m++) {
                for (int n = (getblockSize() >> 1); n <= getblockSize() - 1; n++) {
                    average_brightness += grey_level[i + m][j + n];
                }
            }
        } else {
            System.err.println("Grey level not initialized.");
        }
        double bs = getblockSize() * getblockSize();
        double div = 4 / bs;
        average_brightness = average_brightness * div;
        return average_brightness;
    }

    /**
     * The image_block is divided into four sub-blocks, for these the luminance mean values
     * are used for the edge detection later.
     * This is the fourth sub-block.
     *
     * @param i indices from the image_block
     * @param j indices from the image_block
     * @return returns the average brightness of the fourth sub-block.
     */


    private double getFourthBlockAVG(int i, int j) {
        double average_brightness = 0;

        for (int m = (getblockSize() >> 1); m <= getblockSize() - 1; m++) {
            for (int n = (getblockSize() >> 1); n <= getblockSize() - 1; n++) {
                average_brightness += grey_level[i + m][j + n];
            }
        }
        double bs = getblockSize() * getblockSize();
        double div = 4 / bs;
        average_brightness = average_brightness * div;
        return average_brightness;
    }


    /**
     * The four mean values from the sub-blocks are convolved with filter coefficients.
     * Then the maximum of these results is found and compared with a threshold.
     *
     * @param i indices of image_block
     * @param j indices of image_block
     * @return e_index  returns the index, where the maximum edge is found.
     */

    private int getEdgeFeature(int i, int j) {
        double average[] = {getFirstBlockAVG(i, j), getSecondBlockAVG(i, j),
                getThirdBlockAVG(i, j), getFourthBlockAVG(i, j)};
        double th = this.treshold;
        double edge_filter[][] = {{1.0, -1.0, 1.0, -1.0},
                {1.0, 1.0, -1.0, -1.0},
                {Math.sqrt(2), 0.0, 0.0, -Math.sqrt(2)},
                {0.0, Math.sqrt(2), -Math.sqrt(2), 0.0},
                {2.0, -2.0, -2.0, 2.0}};
        double[] strengths = new double[5];
        int e_index;

        for (int e = 0; e < 5; e++) {
            for (int k = 0; k < 4; k++) {
                strengths[e] += average[k] * edge_filter[e][k];
            }
            strengths[e] = Math.abs(strengths[e]);
        }
        double e_max;
        e_max = strengths[0];
        e_index = EdgeHistogramImplementation.vertical_edge;
        if (strengths[1] > e_max) {
            e_max = strengths[1];
            e_index = EdgeHistogramImplementation.horizontal_edge;
        }
        if (strengths[2] > e_max) {
            e_max = strengths[2];
            e_index = EdgeHistogramImplementation.diagonal_45_degree_edge;
        }
        if (strengths[3] > e_max) {
            e_max = strengths[3];
            e_index = EdgeHistogramImplementation.diagonal_135_degree_edge;
        }
        if (strengths[4] > e_max) {
            e_max = strengths[4];
            e_index = EdgeHistogramImplementation.non_directional_edge;
        }
        if (e_max < th) {
            e_index = EdgeHistogramImplementation.NoEdge;
        }

        return (e_index);
    }


    /**
     * <code>extractFeature()</code> takes all the information about the types of edge, that
     * have been found in the sub-images and applies the methods getFirstBlockAVG(int i,int j) through
     * getFourthBlockAVG(int i, int j) on the whole image. returns Local_Edge_Histogram returns the 80 bins
     */

    public void extractFeature() {
        makeGreyLevel();
        int sub_local_index;
        int EdgeTypeOfBlock;
        int[] count_local = new int[16];

        for (int i = 0; i < 16; i++) {
            count_local[i] = 0;
        }

        for (int j = 0; j <= height - getblockSize(); j += getblockSize())
            for (int i = 0; i <= width - getblockSize(); i += getblockSize()) {
                sub_local_index = (int) ((i << 2) / width) + ((int) ((j << 2) / height) << 2);
                count_local[sub_local_index]++;

                EdgeTypeOfBlock = getEdgeFeature(i, j);

                switch (EdgeTypeOfBlock) {
                    case EdgeHistogramImplementation.NoEdge:
                        break;
                    case EdgeHistogramImplementation.vertical_edge:
                        Local_Edge_Histogram[sub_local_index * 5]++;
                        break;
                    case EdgeHistogramImplementation.horizontal_edge:
                        Local_Edge_Histogram[sub_local_index * 5 + 1]++;
                        break;
                    case EdgeHistogramImplementation.diagonal_45_degree_edge:
                        Local_Edge_Histogram[sub_local_index * 5 + 2]++;
                        break;
                    case EdgeHistogramImplementation.diagonal_135_degree_edge:
                        Local_Edge_Histogram[sub_local_index * 5 + 3]++;
                        break;
                    case EdgeHistogramImplementation.non_directional_edge:
                        Local_Edge_Histogram[sub_local_index * 5 + 4]++;
                        break;
                }//switch(EdgeTypeOfBlock)

            }//for(i)
        for (int k = 0; k < 80; k++) {
            Local_Edge_Histogram[k] /= count_local[k / 5];
        }


    }

    public void histoQuanti() {
        double iQuantValue = 0;
        int e; //Edge type between [0,4]

        //Iterate on each bins of the histograms
        for (int i = 0; i < 80; i++) {
            e = i % 5;
            //Find the interval using QuantTable and associate a number between [0-7]
            for (int j = 0; j < 8; j++) {
                edgeHistogram[i] = (byte) j;
                if (j < 7)
                    iQuantValue = (QuantTable[e][j] + QuantTable[e][j + 1]) / 2.0;  //Get the boundary value.
                else
                    iQuantValue = 1.0;
                if (Local_Edge_Histogram[i] <= iQuantValue) {
                    break;
                }
            }
        }
    }


    /**
     * In the <code>setEdgeHistogram()</code> method the value for each edgeHistogram bin are quantized by
     * quantization tables. Five quantisation tables for five different edge types are existing.
     *
     * @return returns 80 bins.
     */


    public int[] setEdgeHistogram() {
        double iQuantValue;
        double value[] = Local_Edge_Histogram;

        for (int i = 0; i < 80; i++) {
            for (int j = 0; j < 8; j++) {
                bins[i] = j;
                if (j < 7)
                    iQuantValue = (EdgeHistogramImplementation.QuantTable[i % 5][j] + EdgeHistogramImplementation.QuantTable[i % 5][j + 1]) / 2.0;
                else
                    iQuantValue = 1.0;
                if (value[i] <= iQuantValue) {
                    break;
                }
            }
        }
        return bins;
    }

    public static float getDistance(EdgeHistogramImplementation a, EdgeHistogramImplementation b) {
        int[] edgeHistogramA = a.getEdgeHistogram();
        int[] edgeHistogramB = b.getEdgeHistogram();
        float result = 0f;
        // Todo: this first for loop should sum up the differences of the non quantized edges. Check if this code is right!
        for (int i = 0; i < edgeHistogramA.length; i++) {
            // first version is the un-quantisized version, according to the mpeg-7 docs part 8 this version is quite okay as though its nearly linear quantization
            // result += Math.abs((float) edgeHistogramA[i] - (float) edgeHistogramB[i]);
            result += Math.abs((float) EdgeHistogramImplementation.QuantTable[i % 5][edgeHistogramA[i]] - (float) EdgeHistogramImplementation.QuantTable[i % 5][edgeHistogramB[i]]);
        }
        for (int i = 0; i <= 4; i++) {
            result += 5f * Math.abs((float) edgeHistogramA[i] - (float) edgeHistogramB[i]);
        }
        for (int i = 5; i < 80; i++) {
            result += Math.abs((float) edgeHistogramA[i] - (float) edgeHistogramB[i]);
        }
        return result;
    }

/*
    private static int[] RGB2YCRCB(int[] pixel, int[] result) {
        double yy = (0.299 * pixel[0] + 0.587 * pixel[1] + 0.114 * pixel[2]) / 256.0;
        result[0] = (int) (219.0 * yy + 16.5);
        result[1] = (int) (224.0 * 0.564 * (pixel[2] / 256.0 * 1.0 - yy) + 128.5);
        result[2] = (int) (224.0 * 0.713 * (pixel[0] / 256.0 * 1.0 - yy) + 128.5);
        return result;
    }
*/

    private static int getYfromRGB(int rgb) {
        int b = rgb & 255;
        int g = rgb >> 8 & 255;
        int r = rgb >> 16 & 255;
        double yy = (0.299 * r + 0.587 * g + 0.114 * b) / 256.0;
        return (int) (219.0 * yy + 16.5);
    }


    /**
     * Creates a String representation from the descriptor.
     *
     * @return the descriptor as String.
     */
    public String getStringRepresentation() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("edgehistogram;");
        stringBuilder.append(edgeHistogram[0]);
        for (int i = 1; i < edgeHistogram.length; i++) {
            stringBuilder.append(' ');
            stringBuilder.append(edgeHistogram[i]);
        }
        return stringBuilder.toString();
    }

    /**
     * Sets the descriptor value from a String.
     *
     * @param descriptor the descriptor as String.
     */
    public void setStringRepresentation(String descriptor) {
        String[] parts = descriptor.split(";");
        if (!parts[0].equals("edgehistogram")) {
            throw new UnsupportedOperationException("This is no valid representation of a EdgeHistogram descriptor!");
        }
        int[] bins = new int[80];
        StringTokenizer st = new StringTokenizer(parts[1], " ");
        int count = 0;
        while (st.hasMoreElements()) {
            bins[count] = Integer.parseInt(st.nextToken());
            count++;
        }
        edgeHistogram = bins;
    }
}
