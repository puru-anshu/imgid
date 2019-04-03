package com.anshuman.imageprocessing.imageid.fingerprint;

import com.anshuman.imageprocessing.imageid.data.ImageSignature;
import com.anshuman.imageprocessing.imageid.data.IntSignature;
import com.anshuman.imageprocessing.imageid.mathutil.BIJstats;
import com.anshuman.imageprocessing.imageid.mathutil.Matrix;
import ij.process.ImageProcessor;
import ij.process.ImageConverter;
import ij.process.FloatProcessor;
import ij.ImagePlus;
import ij.plugin.filter.GaussianBlur;

import java.awt.*;

import java.io.File;


/**
 * User: Anshuman
 * Date: Oct 5, 2010
 * Time: 3:17:17 PM
 */
public final class DCTImageSig implements ImageSignatureGenerator {


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
        ImageProcessor processor1 = ip.getProcessor().duplicate();
        processor1.autoThreshold();
        Rectangle rectangle = run(processor1);
        ImageProcessor processor = ip.getProcessor();
        processor.setRoi(rectangle);
        processor = processor.crop();
        return processor;

    }

    private ImageProcessor preprocessImage(String filePath) {
        ImagePlus imagePlus = new ImagePlus(filePath);
        ImageConverter iConv = new ImageConverter(imagePlus);
        iConv.convertToGray8();
        ImageProcessor processor = getCropProcessor(imagePlus);
        processor.setInterpolate(true);
        processor = processor.resize(32, 32);
        new GaussianBlur().blur(processor, 8d);
        return processor;

    }


    public ImageSignature getSignature(String filePath) {
        System.out.println("DCTImageSig.getSignature");
        int[] sig = new int[2];
        long t = System.currentTimeMillis();
        ImageProcessor ip = preprocessImage(filePath);
        int N = 32;
        float[][] dctvals = new float[N][N];
        float c1 = (float) Math.sqrt(2.0 / N);
        for (int x = 0; x < N; x++) {
            for (int y = 1; y < N; y++) {
                dctvals[x][y] = (float) (c1 * Math.cos((Math.PI / 2 / N) * y * (2 * x + 1)));
            }
        }
        Matrix dctMatrix = new Matrix(dctvals);
        Matrix transpose = Matrix.trans(dctMatrix);
        ImageProcessor processor = ip.convertToFloat();
        Matrix imageMatrix = new Matrix(processor.getFloatArray());
        dctMatrix.mul(imageMatrix);
        dctMatrix.mul(transpose);

        Matrix subMatrix = dctMatrix.getSubMatrix(1, 1, 8, 8);
        float[] data = new float[64];
        int k = 0;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                data[k++] = subMatrix.get(i, j);
            }
        }
        double median = BIJstats.avg(data);
        long one = 1;
        long hash = 0;
        for (int i = 0; i < 63; i++) {
            float current = data[i];
            if (current > median)
                hash |= one;
            one = one << 1;
        }

        byte[] bytes = toByta(hash);
        sig[0] = toInt(bytes, 0);
        sig[1] = toInt(bytes, 4);
        return new IntSignature(filePath, sig);

    }

    

    public static int toInt(byte[] data, int start) {

        return ((0xff & data[start]) << 24 | (0xff & data[start + 1]) << 16 | (0xff & data[start + 2]) << 8 | (0xff & data[start + 3]) << 0);

    }


    public static byte[] toByta(long data) {

        return new byte[]{
                (byte) ((data >> 56) & 0xff),
                (byte) ((data >> 48) & 0xff),
                (byte) ((data >> 40) & 0xff),
                (byte) ((data >> 32) & 0xff),
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) ((data >> 0) & 0xff),

        };

    }

    public static void main(String[] args) throws Exception {
        String file1 = "D:\\temp\\test-image\\aisha1.jpg";
//        new ImagePlus(file1).show();
        String file2 = "D:\\imageid\\testdata\\Gal_Meethi_Meethi_bol-Aisha\\frame00189.jpg";
        file1 = "d:\\teremastmastdonaindaban.jpg";
//        file1 = "D:\\Tere-Mast-Mast-Do-Nain.png";
//        file2 = "e:\\Lena.jpg";
        ImageSignature signature = new DCTImageSig().getSignature(file1);
        ImageSignature signature1 = new DCTImageSig().getSignature(file2);
        double berr = signature1.getBerr(signature);
        System.out.println("berr = " + berr);
        File[] files = new File("D:\\imageid\\testdata\\Tere_Mast_Mast_Do-Dabbang").listFiles();
//        File[] files = new File("E:\\temp\\capture").listFiles();
        //
        for (File f : files) {
            if (!f.getName().endsWith("jpg")) continue;
            ImageSignature sig = new DCTImageSig().getSignature(f.getPath());
            double berr1 = sig.getBerr(signature);
            System.out.println(f + " :: " + berr1);
            if (berr1 < 10) {
                String s = String.format("%s : %2.3f", f.getName(), berr1);
                ImagePlus imagePlus = new ImagePlus(f.getPath());
                imagePlus.setTitle(s);
                imagePlus.show();
            }
        }
    }


}
