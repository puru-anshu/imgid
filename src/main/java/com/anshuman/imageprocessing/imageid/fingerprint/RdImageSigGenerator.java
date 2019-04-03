package com.anshuman.imageprocessing.imageid.fingerprint;


import com.anshuman.imageprocessing.imageid.data.Signature;
import com.anshuman.imageprocessing.imageid.dft2.Complex;
import com.anshuman.imageprocessing.imageid.dft2.Dft1d;
import com.anshuman.imageprocessing.imageid.mathutil.BIJstats;
import com.anshuman.imageprocessing.imageid.processing.RTransfomation;
import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.io.File;
import java.util.logging.Logger;

/**
 * User: Anshuman
 * Date: Sep 9, 2010
 * Time: 11:29:53 AM
 */
public final class RdImageSigGenerator {
    public static final Logger logger = Logger.getLogger(RdImageSigGenerator.class.getName());

    static float[] kernel = {
            0.0751f, 0.1238f, 0.0751f,
            0.1238f, 0.2042f, 0.1238f,
            0.0751f, 0.1238f, 0.0751f
    };


    public Signature generateSignature(String filePath) throws Exception {
        long t = System.currentTimeMillis();

        ImageProcessor processor = getProcessedImage(filePath);
        int[] scans = {256};
        long[] sigArray = new long[2 * scans.length];

        int ct = 0;
        for (int scan : scans) {
            long[] l = getSigPair(processor, scan);
            sigArray[ct++] = l[0];
            sigArray[ct++] = l[1];
        }
        System.out.println("Time taken  = " + (System.currentTimeMillis() - t) + " ms");
        return new Signature(filePath, sigArray);

    }

    private static ImageProcessor getProcessedImage(String filePath) {
        ImagePlus imagePlus = new ImagePlus(filePath);
        ImageConverter iConv = new ImageConverter(imagePlus);
        iConv.convertToGray8();
        ImageProcessor processor = imagePlus.getProcessor();
        processor.setInterpolate(true);
        processor = processor.resize(256);
        return processor;
    }

    private FloatProcessor autoCorrCalc(ImageProcessor ip) {
        float[][] floatArray = ip.getFloatArray();
        float[][] autoCorr = new float[floatArray.length][];
        int i = 0;
        for (float[] fls : floatArray) {
            autoCorr[i++] = BIJstats.autocorr(fls, fls.length);
        }
//        BIJstats.unitvar(autoCorr);
        return new FloatProcessor(autoCorr);
    }

    private long[] getSigPair(ImageProcessor processor, int scan) {
        float[][] floatArray = RTransfomation.getInstance().getProjection(processor.getFloatArray(), true, scan);
        FloatProcessor fp = new FloatProcessor(floatArray);
        FloatProcessor autoCorr = autoCorrCalc(fp);
        autoCorr.log();
        floatArray = autoCorr.getFloatArray();

        int numCols = floatArray.length;
        double[] if3 = new double[numCols];
        double[] if6 = new double[numCols];
        int x = 0;
        for (float[] vals : floatArray) {
            double max = Float.MIN_VALUE;
            double pr = vals[vals.length - 1];
            double sum = 0d;
            for (double f : vals) {
                if (f > max) {
                    max = f;
                }
                sum += Math.abs(f - pr);
                pr = f;
            }

            if3[x] = sum;
            if6[x] = max;
            x++;
        }
        int ang = floatArray.length;
        doFFT(ang, if3);
        doFFT(ang, if6);
        StringBuilder bd1 = new StringBuilder();
        StringBuilder bd2 = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            if (if3[i] - if3[i + 1] < 0) {
                bd1.append('0');
            } else {
                bd1.append('1');
            }

            if (if6[i] - if6[i + 1] < 0) {
                bd2.append('0');
            } else {
                bd2.append('1');
            }

        }
        long[] l = new long[2];
        l[0] = Long.parseLong(bd1.substring(1), 2);
        l[1] = Long.parseLong(bd2.substring(1), 2);
        return l;
    }

    public final boolean isPow2(int value) {
        return (value == (int) roundPow2(value));
    }

    public final int pow2(int power) {
        return (1 << power);
    }

    public final double roundPow2(double value) {
        double power = (Math.log(value) / Math.log(2));
        int intPower = (int) Math.round(power);
        return (double) (pow2(intPower));
    }

    private void doFFT(int ang, double[] if3) {

        Dft1d dft = new Dft1d(ang);
        Complex[] out = dft.DFT(Complex.makeComplexVector(if3), true);
        for (int i = 0; i < out.length; i++) {
            if3[i] = Math.sqrt(out[i].re * out[i].re + out[i].im * out[i].im);
        }


    }

    private void toMagnitude(double[] rawData) {
        for (int i = 0; i < rawData.length; i++) {
            rawData[i] = (float) Math.sqrt(rawData[i]);
        }
    }

    public static void main(String[] args) throws Exception {
//        new RdImageSigGenerator().generateDigest("e:\\Lena.png");


        Signature signature = new RdImageSigGenerator().generateSignature("e:\\Lena.png");
        Signature signature1 = new RdImageSigGenerator().generateSignature("e:\\Lena.jpg");
        double berr1 = signature1.getBerr(signature);
        System.out.println("berr1 = " + berr1);

        String file1 = "E:\\temp\\capture\\aisha_28.jpg";
        file1 = "e:\\temp\\test-image\\aisha3.jpg";
        String file2 = "E:\\imageid\\testdata\\Teri_Ore-Singh _Is _Kinng";
        file2 = "E:\\imageid\\testdata\\Gal_Meethi_Meethi_bol-Aisha";

        File[] files = new File(file2).listFiles();
        Signature l = new RdImageSigGenerator().generateSignature(file1);
        Signature l1 = new RdImageSigGenerator().generateSignature("E:\\imageid\\testdata\\Gal_Meethi_Meethi_bol-Aisha\\frame00163.jpg");


        double berr = l1.getBerr(l);
        System.out.println("berr = " + berr);
//        double berr1 = l3.getBerr(l);
//        System.out.println("berr1 = " + berr1);
        for (File f : files) {

            System.out.println("f = " + f);
            Signature l2 = new RdImageSigGenerator().generateSignature(f.getPath());
            double berrn = l.getBerr(l2);
            System.out.println(f.getPath() + " berrn = " + berrn);
            if (berrn < 27) {
                String s = String.format("%s : %2.3f", f.getName(), berrn);
                ImagePlus imagePlus = new ImagePlus("", getProcessedImage(f.getPath()));
                imagePlus.setTitle(s);
                imagePlus.show();
            }
        }
    }


}
