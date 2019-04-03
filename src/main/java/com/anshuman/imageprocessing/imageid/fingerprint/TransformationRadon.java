package com.anshuman.imageprocessing.imageid.fingerprint;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * User: Anshuman
 * Date: Sep 9, 2010
 * Time: 4:12:22 PM
 */
public class TransformationRadon {
    private static final Logger logger = Logger.getLogger(TransformationRadon.class.getName());

    int views = 180;
    int scans = 128;



    float phi = 0, stepsize = 1;
    int ang1 = 0, ang2 = 180;
    private int type;
    int w;
    int h;
    int outsize = 64;
    int outdepth = 8;
    int stacksize = 1;
    int projstacksize = 1;


    private ImagePlus imp;
    ImageStack inputstack;
    ByteProcessor bip;
    ShortProcessor sip;
    byte[] bpixels;
    short[] spixels;
    private double[][] pix;
    private static final double HALF_PI = Math.PI / 2;
    private static final double SANG = Math.sqrt(2) / 2;

    public TransformationRadon(ImagePlus imp) {
        this.imp = imp;
        type = imp.getType();
        ImageProcessor currentip = imp.getProcessor();
        getImagepixels(currentip);
        if (w != h) {
            logger.warning("Error :Image dimensions should be equal");
            return;
        }
        outsize = w;

        if (isPow2(w)) {
            scans = w;
        } else {
            //closest power of 2 rounded up
            int power = (int) ((Math.log(w) / Math.log(2))) + 1;
            scans = (int) Math.pow(2, power);
        }

    }

    private static boolean isPow2(int value) {
        return (value == (int) roundPow2(value));
    }

    private static int pow2(int power) {
        return (1 << power);
    }

    private static double roundPow2(double value) {
        double power = (Math.log(value) / Math.log(2));
        int intPower = (int) Math.round(power);
        return (double) (pow2(intPower));
    }

    private void getImagepixels(ImageProcessor ip) {
        //imp = IJ.getImage();
        //type = imp.getType();
        stacksize = imp.getStackSize();

        if (imp.getType() == ImagePlus.GRAY8) {
            //bip= (ByteProcessor) imp.getProcessor();
            bip = (ByteProcessor) ip;
            bpixels = (byte[]) bip.getPixels();
            w = bip.getWidth();
            h = bip.getHeight();
        } else if (imp.getType() == ImagePlus.GRAY16) {
            //sip = (ShortProcessor) imp.getProcessor();
            sip = (ShortProcessor) ip;
            spixels = (short[]) sip.getPixels();
            w = sip.getWidth();
            h = sip.getHeight();
        } else if (imp.getType() == ImagePlus.COLOR_256) {
            System.out.println("Info Image will be converted to greyscale.");
            ImageConverter conv = new ImageConverter(imp);
            conv.setDoScaling(true);
            conv.convertToGray8();
            bip = (ByteProcessor) imp.getProcessor();
            bpixels = (byte[]) bip.getPixels();
            w = bip.getWidth();
            h = bip.getHeight();
        } else if (imp.getType() == ImagePlus.COLOR_RGB) {
            logger.info("Image will be converted to greyscale.");
            //new ImageConverter(imp).convertToGray8();
            //ImageProcessor ip = imp.getProcessor();
            ImageProcessor cp = new ColorProcessor(imp.getImage());
            imp.setProcessor(null, cp.convertToByte(true));
            bip = (ByteProcessor) imp.getProcessor();
            bpixels = (byte[]) bip.getPixels();
            w = bip.getWidth();
            h = bip.getHeight();
        }

        //get pixel values from current image
        pix = new double[w][h];
        if (type == ImagePlus.GRAY8) {
            System.out.println("type = " + type);
            for (int x = 0; x < pix[0].length; x++) {
                for (int y = 0; y < pix.length; y++) {
                    pix[x][y] = bpixels[y + x * pix.length] & 0xFF;
                }
            }
        } else if (type == ImagePlus.GRAY16) {
            System.out.println("type = " + type);
            for (int x = 0; x < pix[0].length; x++) {
                for (int y = 0; y < pix.length; y++) {
                    pix[x][y] = spixels[y + x * pix.length] & 0xFFFF;
                }
            }
        }


    }


    public double[][] getProjection() {

        return doTransformation(pix);
    }

    /**
     * Creates a set of projections from the pixel values in a greyscale image
     * This projection data can then be reconstructed using fbp or iterative methods.
     * The forward projection can be done using nearest neighbour,or linear
     * interpolation
     *
     * @param pix image data
     * @return radon transformation array
     */

    private double[][] doTransformation(double[][] pix) {
        int i;
        i = 0;

        double[][] proj = new double[views][scans];
        double val;
        int x, y, Xcenter, Ycenter;
        double[] sintab = new double[views];
        double[] costab = new double[views];

        int s = 0;
        int inputimgsize = pix[0].length;
        //int min = getMin(pix);
        //int max = getMax(pix);

        //zero all values in projections array
        blank(proj, 0);

        double phi;
        for (phi = ang1; phi < ang2; phi = phi + stepsize) {
            sintab[i] = Math.sin( phi * Math.PI / 180 - HALF_PI);
            costab[i] = Math.cos( phi * Math.PI / 180 - HALF_PI);
            i++;
        }

        // Project each pixel in the image
        Xcenter = inputimgsize / 2;
        Ycenter = inputimgsize / 2;
        i = 0;
        //if no. scans is greater than the image width, then scale will be <1

        double scale = inputimgsize * 1.42 / scans;
        logger.info("Generating projection data from image pixels.. ");

        int N = 0;
        val = 0;
        double weight = 0;
        double progr = 0;

        for (phi = ang1; phi < ang2; phi = phi + stepsize) {
            double a = -costab[i] / sintab[i];
            double aa = 1 / a;
            if (Math.abs(sintab[i]) > SANG) {
                for (s = 0; s < scans; s++) {
                    N = s - scans / 2; //System.out.print("N="+N+" ");
                    double b = (N - costab[i] - sintab[i]) / sintab[i];
                    b = b * scale;
                    //IJ.write("b="+b+" ");
                    for (x = -Xcenter; x < Xcenter; x++) {

                        //linear interpolation
                        y = (int) Math.round(a * x + b);
                        weight = Math.abs((a * x + b) - Math.ceil(a * x + b));
                        if (y >= -Xcenter && y + 1 < Xcenter)
                            val += (1 - weight) * pix[(x + Xcenter)][(y + Ycenter)]
                                    + weight * pix[(x + Xcenter)][(y + Ycenter + 1)];


                    }
                    proj[i][s] = val / Math.abs(sintab[i]);
                    val = 0;

                }
            }
            if (Math.abs(sintab[i]) <= SANG) {
                for (s = 0; s < scans; s++) {
                    N = s - scans / 2;
                    double bb = (N - costab[i] - sintab[i]) / costab[i];
                    bb = bb * scale;
                    //IJ.write("bb="+bb+" ");
                    for (y = -Ycenter; y < Ycenter; y++) {

                        x = (int) Math.round(aa * y + bb);
                        weight = Math.abs((aa * y + bb) - Math.ceil(aa * y + bb));

                        if (x >= -Xcenter && x + 1 < Xcenter)
                            val += (1 - weight) * pix[(x + Xcenter)][(y + Ycenter)]
                                    + weight * pix[(x + Xcenter + 1)][(y + Ycenter)];


                    }
                    proj[i][s] = val / Math.abs(costab[i]);
                    val = 0;

                }

            }
            i++;
            progr += .0055 * (180 / views);
            logger.fine(" progress " + progr); //update progress bar
        }
        normalize2DArray(proj, 0, 255);
        return proj;
    }


    private void blank(double data[][], double value) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = value;
            }
        }
    }

    private void normalize2DArray(double data[][], double min, double max) {

        double datamax = getMax(data);
        logger.info("Normalizing array; MAX:" + datamax);
        zeronegvals2DArray(data);
        double datamin = 0;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = (((data[i][j] - datamin) * (max)) / datamax);

            }
        }
    }

    private void zeronegvals2DArray(double data[][]) {

        double datamin = getMin(data);

        if (datamin < 0) {
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    if (data[i][j] < 0) {
                        data[i][j] = 0;
                    }
                }
            }
        }
    }

    private double getMin(double data[][]) {

        double min = data[0][0];
        for (double[] aData : data) {
            for (int j = 0; j < data[0].length; j++) {
                if (aData[j] < min) min = aData[j];
            }
        }
        return min;
    }


    private double getMax(double data[][]) {

        double max = data[0][0];
        for (double[] aData : data) {
            for (int j = 0; j < data[0].length; j++) {
                if (aData[j] > max) max = aData[j];
            }
        }
        return max;
    }


    public ImageProcessor createSinogram(double[][] projection) {
        ImageProcessor resp = new ByteProcessor(views, scans);
        ImagePlus result = new ImagePlus("Sinogram", resp);
        byte[] barray = new byte[views * scans];

        for (int i = 0; i < views; i++) {
            for (int j = 0; j < scans; j++) {
                barray[i + j * views] = (byte) projection[i][j];
            }
        }
        resp.setPixels(barray);

        return resp;
    }


    /*public static void main(String[] args) throws Exception {
        float[][] doubles = getProjectionData("e:\\test1.png");
        System.out.println("doubles.length = " + doubles.length);

    }

    public static float[][] getProjectionData(String filePath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(filePath));
        ImagePlus imagePlus = new ImagePlus("xx", bufferedImage.getScaledInstance(256, 256, Image.SCALE_FAST));
        ImageConverter conv = new ImageConverter(imagePlus);
        conv.setDoScaling(true);
        conv.convertToGray16();
        ImageProcessor processor = imagePlus.getProcessor();
        imagePlus = new ImagePlus("gray", processor);
        TransformationRadon transformationRadon = new TransformationRadon(imagePlus);
        double[][] doubles = transformationRadon.getProjection();
        ImageProcessor processor1 = transformationRadon.createSinogram(doubles);
        return processor1.getFloatArray();
    }*/

}
