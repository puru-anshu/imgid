package com.anshuman.imageprocessing.imageid.imageutil;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.filter.RankFilters;
import ij.process.Blitter;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

/**
 * User: Anshuman
 * Date: Sep 16, 2010
 * Time: 5:52:06 AM
 */
public class AutoLocalThreshold {

    public void Bernsen(ImagePlus imp, int radius, double par1, double par2, boolean doIwhite) {
        // Bernsen recommends WIN_SIZE = 31 and CONTRAST_THRESHOLD = 15.
        //  1) Bernsen J. (1986) "Dynamic Thresholding of Grey-Level Images"
        //    Proc. of the 8th Int. Conf. on Pattern Recognition, pp. 1251-1255
        //  2) Sezgin M. and Sankur B. (2004) "Survey over Image Thresholding
        //   Techniques and Quantitative Performance Evaluation" Journal of
        //   Electronic Imaging, 13(1): 146-165
        //  http://citeseer.ist.psu.edu/sezgin04survey.html
        // Ported to ImageJ plugin from E Celebi's fourier_0.8 routines
        // This version uses a circular local window, instead of a rectagular one
        ImagePlus Maximp, Minimp;
        ImageProcessor ip = imp.getProcessor(), ipMax, ipMin;
        int contrast_threshold = 15;
        int local_contrast;
        int mid_gray;
        byte object;
        byte backg;
        int temp;

        if (par1 != 0) {
            System.out.println("Bernsen: changed contrast_threshold from :" + contrast_threshold + "  to:" + par1);
            contrast_threshold = (int) par1;
        }

        if (doIwhite) {
            object = (byte) 0xff;
            backg = (byte) 0;
        } else {
            object = (byte) 0;
            backg = (byte) 0xff;
        }

        Maximp = duplicateImage(ip);
        ipMax = Maximp.getProcessor();
        RankFilters rf = new RankFilters();
        rf.rank(ipMax, radius, rf.MAX);// Maximum
        //Maximp.show();
        Minimp = duplicateImage(ip);
        ipMin = Minimp.getProcessor();
        rf.rank(ipMin, radius, rf.MIN); //Minimum
        //Minimp.show();
        byte[] pixels = (byte[]) ip.getPixels();
        byte[] max = (byte[]) ipMax.getPixels();
        byte[] min = (byte[]) ipMin.getPixels();

        for (int i = 0; i < pixels.length; i++) {
            local_contrast = (int) ((max[i] & 0xff) - (min[i] & 0xff));
            mid_gray = (int) ((min[i] & 0xff) + (max[i] & 0xff)) / 2;
            temp = (int) (pixels[i] & 0x0000ff);
            if (local_contrast < contrast_threshold)
                pixels[i] = (mid_gray >= 128) ? object : backg;  //Low contrast region
            else
                pixels[i] = (temp >= mid_gray) ? object : backg;
        }
        //imp.updateAndDraw();

    }

    public void Mean(ImagePlus imp, int radius, double par1, boolean doIwhite) {
        // See: Image processing Learning Resourches HIPR2
        // http://homepages.inf.ed.ac.uk/rbf/HIPR2/adpthrsh.htm
        ImagePlus Meanimp;
        ImageProcessor ip = imp.getProcessor(), ipMean;
        int c_value = 0;
        byte object;
        byte backg;

        if (par1 != 0) {
            System.out.println("Mean: changed c_value from :" + c_value + "  to:" + par1);
            c_value = (int) par1;
        }

        if (doIwhite) {
            object = (byte) 0xff;
            backg = (byte) 0;
        } else {
            object = (byte) 0;
            backg = (byte) 0xff;
        }

        Meanimp = duplicateImage(ip);
        ImageConverter ic = new ImageConverter(Meanimp);
        ic.convertToGray32();

        ipMean = Meanimp.getProcessor();
        RankFilters rf = new RankFilters();
        rf.rank(ipMean, radius, rf.MEAN);// Mean
        //Meanimp.show();
        byte[] pixels = (byte[]) ip.getPixels();
        float[] mean = (float[]) ipMean.getPixels();

        for (int i = 0; i < pixels.length; i++)
            pixels[i] = ((int) (pixels[i] & 0xff) > (int) (mean[i] - c_value)) ? object : backg;
        //imp.updateAndDraw();

    }

    public void Median(ImagePlus imp, int radius, double par1, double par2, boolean doIwhite) {
        // See: Image processing Learning Resourches HIPR2
        // http://homepages.inf.ed.ac.uk/rbf/HIPR2/adpthrsh.htm
        ImagePlus Medianimp;
        ImageProcessor ip = imp.getProcessor(), ipMedian;
        int c_value = 0;
        byte object;
        byte backg;

        if (par1 != 0) {
            System.out.println("Median: changed c_value from :" + c_value + "  to:" + par1);
            c_value = (int) par1;
        }

        if (doIwhite) {
            object = (byte) 0xff;
            backg = (byte) 0;
        } else {
            object = (byte) 0;
            backg = (byte) 0xff;
        }

        Medianimp = duplicateImage(ip);
        ipMedian = Medianimp.getProcessor();
        RankFilters rf = new RankFilters();
        rf.rank(ipMedian, radius, rf.MEDIAN);
        //Medianimp.show();
        byte[] pixels = (byte[]) ip.getPixels();
        byte[] median = (byte[]) ipMedian.getPixels();

        for (int i = 0; i < pixels.length; i++)
            pixels[i] = ((int) (pixels[i] & 0xff) > (int) ((median[i] & 0xff) - c_value)) ? object : backg;
        //imp.updateAndDraw();

    }

    public void MidGrey(ImagePlus imp, int radius, double par1, double par2, boolean doIwhite) {
        // See: Image processing Learning Resourches HIPR2
        // http://homepages.inf.ed.ac.uk/rbf/HIPR2/adpthrsh.htm
        ImagePlus Maximp, Minimp;
        ImageProcessor ip = imp.getProcessor(), ipMax, ipMin;
        int c_value = 0;
        int mid_gray;
        byte object;
        byte backg;

        if (par1 != 0) {
            System.out.println("MidGrey: changed c_value from :" + c_value + "  to:" + par1);
            c_value = (int) par1;
        }

        if (doIwhite) {
            object = (byte) 0xff;
            backg = (byte) 0;
        } else {
            object = (byte) 0;
            backg = (byte) 0xff;
        }

        Maximp = duplicateImage(ip);
        ipMax = Maximp.getProcessor();
        RankFilters rf = new RankFilters();
        rf.rank(ipMax, radius, rf.MAX);// Maximum
        //Maximp.show();
        Minimp = duplicateImage(ip);
        ipMin = Minimp.getProcessor();
        rf.rank(ipMin, radius, rf.MIN); //Minimum
        //Minimp.show();
        byte[] pixels = (byte[]) ip.getPixels();
        byte[] max = (byte[]) ipMax.getPixels();
        byte[] min = (byte[]) ipMin.getPixels();

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = ((int) (pixels[i] & 0xff) > (int) (((max[i] & 0xff) + (min[i] & 0xff)) / 2) + c_value) ? object : backg;
        }
        //imp.updateAndDraw();

    }

    public void Niblack(ImagePlus imp, int radius, double par1, double par2, boolean doIwhite) {
        // Niblack recommends K_VALUE = -0.2 for images with black foreground
        // objects, and K_VALUE = +0.2 for images with white foreground objects.
        //  Niblack W. (1986) "An introduction to Digital Image processing" Prentice-Hall.
        // Ported to ImageJ plugin from E Celebi's fourier_0.8 routines
        // This version uses a circular local window, instead of a rectagular one

        ImagePlus Meanimp, Varimp;
        ImageProcessor ip = imp.getProcessor(), ipMean, ipVar;
        double k_value;
        byte object;
        byte backg;

        if (doIwhite) {
            k_value = 0.2;
            object = (byte) 0xff;
            backg = (byte) 0;
        } else {
            k_value = -0.2;
            object = (byte) 0;
            backg = (byte) 0xff;
        }

        if (par1 != 0) {
            System.out.println("Niblack: changed k_value from :" + k_value + "  to:" + par1);
            k_value = par1;
        }

        Meanimp = duplicateImage(ip);
        ImageConverter ic = new ImageConverter(Meanimp);
        ic.convertToGray32();

        ipMean = Meanimp.getProcessor();
        RankFilters rf = new RankFilters();
        rf.rank(ipMean, radius, rf.MEAN);// Mean
        //Meanimp.show();
        Varimp = duplicateImage(ip);
        ic = new ImageConverter(Varimp);
        ic.convertToGray32();
        ipVar = Varimp.getProcessor();
        rf.rank(ipVar, radius, rf.VARIANCE); //Variance
        //Varimp.show();
        byte[] pixels = (byte[]) ip.getPixels();
        float[] mean = (float[]) ipMean.getPixels();
        float[] var = (float[]) ipVar.getPixels();

        for (int i = 0; i < pixels.length; i++)
            pixels[i] = ((int) (pixels[i] & 0xff) > (int) (mean[i] + k_value * Math.sqrt(var[i]))) ? object : backg;
        //imp.updateAndDraw();

    }

    public void Sauvola(ImagePlus imp, int radius, double par1, double par2, boolean doIwhite) {
        // Sauvola recommends K_VALUE = 0.5 and R_VALUE = 128.
        // This is a modification of Niblack's thresholding method.
        // Sauvola J. and Pietaksinen M. (2000) "Adaptive Document Image Binarization"
        // Pattern Recognition, 33(2): 225-236
        // http://www.ee.oulu.fi/mvg/publications/show_pdf.php?ID=24
        // Ported to ImageJ plugin from E Celebi's fourier_0.8 routines
        // This version uses a circular local window, instead of a rectagular one

        ImagePlus Meanimp, Varimp;
        ImageProcessor ip = imp.getProcessor(), ipMean, ipVar;
        double k_value = 0.5;
        double r_value = 128;
        byte object;
        byte backg;

        if (par1 != 0) {
            System.out.println("Sauvola: changed k_value from :" + k_value + "  to:" + par1);
            k_value = par1;
        }

        if (par2 != 0) {
            System.out.println("Sauvola: changed r_value from :" + r_value + "  to:" + par2);
            r_value = par2;
        }

        if (doIwhite) {
            object = (byte) 0xff;
            backg = (byte) 0;
        } else {
            object = (byte) 0;
            backg = (byte) 0xff;
        }

        Meanimp = duplicateImage(ip);
        ImageConverter ic = new ImageConverter(Meanimp);
        ic.convertToGray32();

        ipMean = Meanimp.getProcessor();
        RankFilters rf = new RankFilters();
        rf.rank(ipMean, radius, rf.MEAN);// Mean
        //Meanimp.show();
        Varimp = duplicateImage(ip);
        ic = new ImageConverter(Varimp);
        ic.convertToGray32();
        ipVar = Varimp.getProcessor();
        rf.rank(ipVar, radius, rf.VARIANCE); //Variance
        //Varimp.show();
        byte[] pixels = (byte[]) ip.getPixels();
        float[] mean = (float[]) ipMean.getPixels();
        float[] var = (float[]) ipVar.getPixels();

        for (int i = 0; i < pixels.length; i++)
            pixels[i] = ((int) (pixels[i] & 0xff) > (int) (mean[i] * (1.0 + k_value * ((Math.sqrt(var[i]) / r_value) - 1.0)))) ? object : backg;
        //imp.updateAndDraw();

    }

    private ImagePlus duplicateImage(ImageProcessor iProcessor) {
        int w = iProcessor.getWidth();
        int h = iProcessor.getHeight();
        ImagePlus iPlus = NewImage.createByteImage("Image", w, h, 1, NewImage.FILL_BLACK);
        ImageProcessor imageProcessor = iPlus.getProcessor();
        imageProcessor.copyBits(iProcessor, 0, 0, Blitter.COPY);
        return iPlus;
    }

    public static void main(String[] args) {
        ImagePlus imagePlus = IJ.openImage("e:\\temp\\test-image\\aisha1.jpg");
        ImageConverter converter = new ImageConverter(imagePlus);
        converter.convertToGray8();
        imagePlus.getProcessor().smooth();
        imagePlus.getProcessor().findEdges();
        new AutoLocalThreshold().Bernsen(imagePlus, 15, 0, 0, true);
        imagePlus.show();

    }


}

