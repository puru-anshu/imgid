package com.anshuman.imageprocessing.imageid.fingerprint;

//import Jama.Matrix;

import com.anshuman.imageprocessing.imageid.data.HashSignature;
import com.anshuman.imageprocessing.imageid.data.ImageSignature;
import ij.ImagePlus;
import ij.plugin.filter.GaussianBlur;
import ij.plugin.filter.BackgroundSubtracter;
import ij.process.*;

import java.awt.*;

/**
 * User: Anshuman
 * Date: Oct 5, 2010
 * Time: 3:17:17 PM
 */
public final class ImageHash implements ImageSignatureGenerator {


    private static Rectangle run(ImageProcessor ip) {
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        for (int y = 0; y < ip.getHeight(); y++) {
            for (int x = 0; x < ip.getWidth(); x++) {
                if (ip.get(x, y) == 255) {
                    maxY = y;
                    break;
                }
            }
        }
        for (int x = 0; x < ip.getWidth(); x++) {
            for (int y = 0; y < ip.getHeight(); y++) {
                if (ip.get(x, y) == 255) {
                    maxX = x;
                    break;
                }
            }
        }

        for (int y = ip.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < ip.getWidth(); x++) {
                if (ip.get(x, y) == 255) {
                    minY = y;
                    break;
                }
            }
        }


        for (int x = ip.getWidth() - 1; x >= 0; x--) {
            for (int y = 0; y < ip.getHeight(); y++) {
                if (ip.get(x, y) == 255) {
                    minX = x;
                    break;
                }
            }
        }
        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }


    public static ImageProcessor getCropProcessor(ImagePlus ip) {
//        ip.show();
        ImageProcessor processor1 = ip.getProcessor().duplicate();
        processor1.autoThreshold();
        Rectangle rectangle = run(processor1);
        ImageProcessor processor = ip.getProcessor();

        processor.setRoi(rectangle);
        processor = processor.crop();


        return processor;
    }

    public ImageProcessor preprocessImage(String filePath) {
        ImagePlus imagePlus = new ImagePlus(filePath);
        ImageConverter iConv = new ImageConverter(imagePlus);
        iConv.convertToGray8();
        ImageProcessor processor = getCropProcessor(imagePlus);
        processor.setInterpolate(true);
        processor = processor.resize(64, 64);
//        new GaussianBlur().blurGaussian(processor, 2.5, 2.5, 0.002);
        processor.gamma(1.0);

//        new ImagePlus("xx", processor).show();
        return processor;
    }


    public double[] radonProjections(ImageProcessor imp, int N) {
        imp = imp.convertToFloat();
        int width = imp.getWidth();
        int height = imp.getHeight();
        int D = (width > height) ? width : height;
        int x_off = width / 2;
        int y_off = height / 2;
        int[] nbrPtPerline = new int[N];
        FloatProcessor img = new FloatProcessor(N, D);
        for (int k = 0; k < N / 4 + 1; k++) {
            double theta = k * Math.PI / N;
            double alpha = Math.tan(theta);
            for (int x = 0; x < D; x++) {
                double y = alpha * (x - x_off);
                int yd = (int) Math.round(y);
                if ((yd + y_off >= 0) && (yd + y_off < height) && (x < width)) {
                    img.setf(k, x, imp.getf(x, yd + y_off));
                    nbrPtPerline[k] += 1;
                }
                if ((yd + x_off >= 0) && (yd + x_off < width) && (k != N / 4) && (x < height)) {
                    img.setf(N / 2 - k, x, imp.getf(yd + x_off, x));
                    nbrPtPerline[N / 2 - k] += 1;
                }
            }
        }
        int j = 0;
        for (int k = 3 * N / 4; k < N; k++) {
            double theta = k * Math.PI / N;
            double alpha = Math.tan(theta);
            for (int x = 0; x < D; x++) {
                double y = alpha * (x - x_off);
                int yd = (int) Math.round(y);
                if ((yd + y_off >= 0) && (yd + y_off < height) && (x < width)) {
                    img.setf(k, x, imp.getf(x, yd + y_off));
                    nbrPtPerline[k] += 1;
                }
                if ((y_off - yd >= 0) && (y_off - yd < width) && (2 * y_off - x >= 0) && (2 * y_off - x < height) && (k != 3 * N / 4)) {
                    img.setf(k - j, x, imp.getf(-yd + y_off, -(x - y_off) + y_off));
                    nbrPtPerline[k - j] += 1;
                }
            }
            j += 2;
        }
        return featureVector(img.getFloatArray(), nbrPtPerline);

    }

    public int[] generateDigest(String filePath) {
        ImageProcessor ip = preprocessImage(filePath);
        double[] fv = radonProjections(ip, 180);
        return dct(fv);
    }


    private int[] dct(double[] fv) {
        double SQRT_TWO = Math.sqrt(2);
        int N = fv.length;
        int nb_coeffs = 40;
        int[] digest = new int[40];
        double[] dTemp = new double[nb_coeffs];
        double max = 0.0;
        double min = 0.0;
        for (int k = 0; k < nb_coeffs; k++) {
            double sum = 0.0;
            for (int n = 0; n < N; n++) {
                sum += fv[n] * Math.cos((Math.PI * (2 * n + 1) * k) / (2 * N));
            }
            if (k == 0)
                dTemp[k] = sum / Math.sqrt((double) N);
            else
                dTemp[k] = sum * SQRT_TWO / Math.sqrt((double) N);
            if (dTemp[k] > max)
                max = dTemp[k];
            if (dTemp[k] < min)
                min = dTemp[k];
        }
        for (int i = 0; i < nb_coeffs; i++) {
            digest[i] = (short) (255 * (dTemp[i] - min) / (max - min));
        }
        return digest;
    }

    private double[] featureVector(float[][] projections, int[] ptsPerLine) {

        int N = projections.length;
        int D = projections[0].length;
        double[] featureVectors = new double[projections.length];
        double sum = 0.0f;
        double sum_sqd = 0.0f;
        for (int k = 0; k < N; k++) {
            double line_sum = 0.0f;
            double line_sum_sqd = 0.0f;
            int nb_pixels = ptsPerLine[k];
            for (int i = 0; i < D; i++) {
                line_sum += projections[k][i];
                line_sum_sqd += projections[k][i] * projections[k][i];
            }
            featureVectors[k] = (line_sum_sqd / nb_pixels) - (line_sum * line_sum) / (nb_pixels * nb_pixels);
            sum += featureVectors[k];
            sum_sqd += featureVectors[k] * featureVectors[k];
        }
        double mean = sum / N;
        double var = (float) Math.sqrt((sum_sqd / N) - (sum * sum) / (N * N));
        for (int i = 0; i < N; i++) {
            featureVectors[i] = (featureVectors[i] - mean) / var;
        }
        return featureVectors;

    }

    public static double crossCorrelate(int[] x, int[] y) {
        int N = y.length;
        double[] r = new double[N];
        double sumx = 0.0;
        double sumy = 0.0;
        for (int i = 0; i < N; i++) {
            sumx += x[i];
            sumy += y[i];
        }
        double meanx = sumx / N;
        double meany = sumy / N;
        double max = 0;
        for (int d = 0; d < N; d++) {
            double num = 0.0;
            double denx = 0.0;
            double deny = 0.0;
            for (int i = 0; i < N; i++) {
                num += (x[i] - meanx) * (y[(N + i - d) % N] - meany);
                denx += Math.pow((x[i] - meanx), 2);
                deny += Math.pow((y[(N + i - d) % N] - meany), 2);
            }
            r[d] = num / Math.sqrt(denx * deny);
            if (r[d] > max)
                max = r[d];
        }
        return max;
    }


    public static void main(String[] args) {

        long t = System.currentTimeMillis();
        int[] l = new ImageHash().generateDigest("D:\\dabang.jpg");
        int[] l1 = new ImageHash().generateDigest("D:\\imageid\\testdata\\Tere_Mast_Mast_Do-Dabbang\\frame00377.jpg");
        double v = crossCorrelate(l, l1);
        System.out.println("Time taken " + (System.currentTimeMillis() - t));
        System.out.println("Similarity = " + v);

    }

    public ImageSignature getSignature(String filePath) {
        long t = System.currentTimeMillis();
        int[] sigs = generateDigest(filePath);
        t = System.currentTimeMillis() - t;
        System.out.println("ImageHash.getSignature " + filePath + " " + t + " ms");
        return new HashSignature(filePath, sigs);

    }
}
