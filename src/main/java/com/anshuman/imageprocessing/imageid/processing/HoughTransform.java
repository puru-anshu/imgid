package com.anshuman.imageprocessing.imageid.processing;

import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;
import ij.process.FloatProcessor;

/**
 * User: Anshuman
 * Date: Oct 10, 2010
 * Time: 4:59:58 PM
 */
public class HoughTransform extends Thread {


    public static void main(String[] args) throws Exception {
        String filename = "d:\\Lena.png";
        ImagePlus imagePlus = new ImagePlus(filename);
        ImageConverter converter = new ImageConverter(imagePlus);
        converter.convertToGray8();
        ImageProcessor processor = imagePlus.getProcessor();
        processor.setInterpolate(true);
        processor = processor.resize(256);
        ImageProcessor processor2 = processor.convertToFloat();
        new ImagePlus("input", processor2).show();
        float[] pixel = (float[]) processor2.getPixels();

        float[] ht = line(pixel, processor2.getWidth(), 180, 128);

        FloatProcessor processor1 = new FloatProcessor(180, 128, ht, null);


        new ImagePlus("ht", processor1).show();

/*
//        filename = "e:\\temp\\capture\\teri_ore_BEST_QUALITY_VIdEo--DC5Qjr7XtM232.jpg";
//        filename = "E:\\imageid\\testdata\\Teri_Ore-Singh _Is _Kinng\\frame00007.jpg";

//        filename="e:\\test1.png";
//        new ImagePlus(filename).show();
        IntSignature signature = new ImageSig().getSignature(filename);

//        filename = "e:\\Lena.png";
//        filename = "E:\\imageid\\testdata\\Gal_Meethi_Meethi_bol-Aisha\\frame00008.jpg";
//        filename="e:\\test2.png";
        filename = "E:\\imageid\\testdata\\Teri_Ore-Singh _Is _Kinng\\frame00006.jpg";
        IntSignature signature1 = new ImageSig().getSignature(filename);
        double berr = signature1.getBerr(signature);
        System.out.println("berr = " + berr);
*/
    }


    public static float[] line(float[] image, int width, int distanceBins, int thetaBins) {
        int height = image.length / width;
        float[] a = new float[thetaBins * distanceBins];
        double thetaStep = Math.PI / thetaBins;
        double rStep = Math.max(width, height) / distanceBins;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                double d = image[(y * width + x)];

                if (d <= 0.0D)
                    continue;
                double dx = x - (width / 2.0D);
                double dy = y - (height / 2.0D);
                for (int tix = 0; tix < thetaBins; ++tix) {
                    double theta = tix * thetaStep;
                    double r = dx * Math.cos(theta) + dy * Math.sin(theta);
                    if (r < 0.0D) {
                        r = -r;
                        theta += Math.PI;
                    }
                    int rix = (int) Math.round(r / rStep);
                    if (rix >= distanceBins) {
                        System.err.println("Error in line Hough rix " + rix + " theta" + theta + " ix" + tix + " x=" + x + "y=" + y);
                    }
                    a[(tix * distanceBins + rix)] += d;
                }
            }
        }

        return a;
    }


}

