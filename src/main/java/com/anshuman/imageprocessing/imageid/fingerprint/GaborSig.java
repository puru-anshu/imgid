package com.anshuman.imageprocessing.imageid.fingerprint;

import com.anshuman.imageprocessing.imageid.data.GaborSignature;
import com.anshuman.imageprocessing.imageid.data.ImageSignature;
import com.anshuman.imageprocessing.imageid.imageutil.ImageUtil;
import com.anshuman.imageprocessing.imageid.mp7.Gabor;
import ij.process.ImageProcessor;
import ij.ImagePlus;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * .
 * User: anshu
 * Date: Dec 7, 2010
 * Time: 4:38:21 PM
 */
public class GaborSig implements ImageSignatureGenerator {
    private Rectangle run(ImageProcessor ip) {

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


    public ImageProcessor getCropProcessor(ImagePlus ip) {
        ImageProcessor processor1 = ip.getProcessor().duplicate();
        processor1 = processor1.convertToShort(true);
        processor1.autoThreshold();
        Rectangle rectangle = run(processor1);
        ImageProcessor processor = ip.getProcessor();
        processor.setRoi(rectangle);
        processor = processor.crop();

        return processor;

    }

    private BufferedImage preprocessImage(String filePath) {
        ImagePlus imagePlus = new ImagePlus(filePath);
        ImageProcessor processor = getCropProcessor(imagePlus);
        processor = processor.resize(64, 64);
        BufferedImage bufferedImage = processor.getBufferedImage();
        imagePlus.close();
        return bufferedImage;
    }

    public ImageSignature getSignature(String filePath) {
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            BufferedImage bufferedImage = ImageUtil.scaleImage(image, 256);

            BufferedImage clippedImg = bufferedImage.getSubimage(40, 40, bufferedImage.getWidth() - 80, bufferedImage.getHeight() - 80);
            new ImagePlus("", clippedImg).show();
            BufferedImage image1 = ImageUtil.scaleImage(clippedImg, 64);

            Gabor gabor = new Gabor();
            double[] feature = gabor.getNormalizedFeature(gabor.grayscale(image1));
            return new GaborSignature(filePath, feature);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }



    public static void main(String[] args) {
        new GaborSig().getSignature("d:\\Photo0361.jpg");
    }
}
