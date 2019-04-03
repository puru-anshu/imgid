package com.anshuman.imageprocessing.imageid.fingerprint;

import com.anshuman.imageprocessing.imageid.data.Signature;
import com.anshuman.imageprocessing.imageid.mathutil.BIJstats;
import com.anshuman.imageprocessing.imageid.mathutil.FFTUtil;
import com.anshuman.imageprocessing.imageid.processing.Differential;
import com.anshuman.imageprocessing.imageid.processing.RTransfomation;
import ij.process.ImageProcessor;

import java.io.FileNotFoundException;
import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * User: Anshuman
 * Date: Sep 9, 2010
 * Time: 11:29:53 AM
 */
public final class HoughSigGenerator {
    public static final Logger logger = Logger.getLogger(HoughSigGenerator.class.getName());
    private static final int RESIZE_VAL = 192;

    static final float[] kernel = {
            0.0751f, 0.1238f, 0.0751f,
            0.1238f, 0.2042f, 0.1238f,
            0.0751f, 0.1238f, 0.0751f
    };


    public Signature generateSignature(String filePath) throws Exception {
        long t = System.currentTimeMillis();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("No image found at " + filePath);
        }

        ImageProcessor pr2 = preprocessImage(filePath);
        if (null == pr2)
            return null;
        final int ang = 256;
        final int rad = 256;

        /*LinearHT ht = new LinearHT(pr2, ang, rad);
        FloatProcessor image = ht.getAccumulatorImage();
        ImageProcessor shortP = image.convertToShort(true);*/
        float[][] projection = RTransfomation.getInstance().getProjection(pr2.getFloatArray(), true, 256);
        FloatProcessor image = new FloatProcessor(projection);
        ImageProcessor shortP = image.convertToShort(true);

        int w = shortP.getWidth();
        int h = shortP.getHeight();
        System.out.println(w + "X " + h);
        double[] if3 = new double[w];
        double[] if6 = new double[w];
        for (int j = 0; j < w; j++) {
            int[] vals = new int[h];
            shortP.getColumn(0, j, vals, h);
            if6[j] = getMax(vals);
        }

        if3 = new Differential().getVerticalGradient(image);


        doFFT(if3);
        doFFT(if6);
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
        logger.info(new StringBuilder().append("Time taken in sig generation for ").append(file.getName()).append(" ").append(System.currentTimeMillis() - t).append("ms").toString());
        long[] longs = {Long.parseLong(bd1.substring(1), 2), Long.parseLong(bd1.substring(1), 2)};
        return new Signature(filePath, longs);

    }

    private double getSumAbs(int[] fls) {
        /* double sum = 0.0f;
       int i = 0;
       for (; i < fls.length - 1; i++) {
           sum += Math.abs(fls[i] - fls[i + 1]);                                  
       }
       return sum;*/
        float[] x = new float[fls.length];
        for (int i = 0; i < fls.length; i++) {
            x[i] = fls[i];
        }
        return BIJstats.sem(x);
    }


    private double getMax(int[] fls) {
        int max = 0;
        for (int f : fls) {
            if (f > max) {
                max = f;
            }
        }
        return max;
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

    private void doFFT(double[] if3) {
        double[] spectrum = new double[if3.length];
        double[] imag = new double[if3.length];
        System.arraycopy(if3, 0, spectrum, 0, Math.min(256, if3.length));
        FFTUtil.FFT(1, if3.length, spectrum, imag);
        for (int i = 0; i < 256; i++) {
            if3[i] = Math.sqrt(spectrum[i] * spectrum[i] + imag[i] * imag[i]);
        }


    }

    private static ImageProcessor preprocessImage(String filePath) throws IOException {
        ImagePlus imagePlus = new ImagePlus(filePath);
        ImageConverter iConv = new ImageConverter(imagePlus);
        iConv.convertToGray16();
        ImageProcessor processor = ImageSig.getCropProcessor(imagePlus);
        processor.setInterpolate(true);
        int w = processor.getWidth();
        int h = processor.getHeight();
        if (w > h) {
            double factor = (double) w / (double) h;
            w = (int) Math.rint(factor * 256);
            h = 256;
        } else {
            double factor = (double) h / (double) w;
            h = (int) Math.rint(factor * 256);
            w = 256;
        }

        processor = processor.resize(w, h);
        processor.convolve(kernel, 1, 1);

        /* w = processor.getWidth();
      h = processor.getHeight();
      int nr = Math.min(w, h);
      Ellipse2D.Double circle = new Ellipse2D.Double(w / 2 - nr / 2, h / 2 - nr / 2, nr, nr);
      Rectangle rectangle = circle.getBounds();
      processor.setRoi(rectangle);
      processor = processor.crop();*/
//        new ImagePlus(filePath, processor).show();
        return processor;
    }


    public static void main(String[] args) throws Exception {
        String file1 = "E:\\temp\\test-image\\aisha1.jpg";
//        new ImagePlus(file1).show();
        String file2 = "E:\\imageid\\testdata\\Gal_Meethi_Meethi_bol-Aisha\\frame00189.jpg";
//        file1 = "e:\\Lena.png";
        file1 = "e:\\Tere-Mast-Mast-Do-Nain.png";
//        file2 = "e:\\Lena.jpg";
        Signature signature = new HoughSigGenerator().generateSignature(file1);
        Signature signature1 = new HoughSigGenerator().generateSignature(file2);
        double berr = signature1.getBerr(signature);
        System.out.println("berr = " + berr);
        File[] files = new File("E:\\imageid\\testdata\\Tere_Mast_Mast_Do-Dabbang").listFiles();
//        File[] files = new File("E:\\temp\\capture").listFiles();
        //
        for (File f : files) {
            if (!f.getName().endsWith("jpg")) continue;
            Signature sig = new HoughSigGenerator().generateSignature(f.getPath());
            double berr1 = sig.getBerr(signature);
            System.out.println(f + " :: " + berr1);
            if (berr1 < 16) {
                String s = String.format("%s : %2.3f", f.getName(), berr1);
                ImagePlus imagePlus = new ImagePlus(f.getPath());
                imagePlus.setTitle(s);
                imagePlus.show();
            }
        }
    }

}
