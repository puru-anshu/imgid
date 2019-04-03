package com.anshuman.imageprocessing.imageid.imageutil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * User: Anshuman
 * Date: Sep 13, 2010
 * Time: 3:57:34 PM
 */
public class ImageUtil {


    public BufferedImage fillBorder(BufferedImage src, int leftBorder, int rightBorder, int topBorder, int bottomBorder) {
        Paint borderPaint = Color.black;
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage dst = new BufferedImage(width + leftBorder + rightBorder, height + topBorder + bottomBorder, src.getType());
        Graphics2D g = dst.createGraphics();
        if (borderPaint != null) {
            g.setPaint(borderPaint);
            if (leftBorder > 0)
                g.fillRect(0, 0, leftBorder, height);
            if (rightBorder > 0)
                g.fillRect(width - rightBorder, 0, rightBorder, height);
            if (topBorder > 0)
                g.fillRect(leftBorder, 0, width - leftBorder - rightBorder, topBorder);
            if (bottomBorder > 0)
                g.fillRect(leftBorder, height - bottomBorder, width - leftBorder - rightBorder, bottomBorder);
        }
        g.drawRenderedImage(src, AffineTransform.getTranslateInstance(leftBorder, rightBorder));
        g.dispose();
        return dst;
    }


    public static BufferedImage scaleImage(BufferedImage image, int maxSideLength) {
        assert (maxSideLength > 0);
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();
        double scaleFactor = 0.0;
        if (originalWidth > originalHeight) {
            scaleFactor = ((double) maxSideLength / originalWidth);
        } else {
            scaleFactor = ((double) maxSideLength / originalHeight);
        }
        // create smaller image
        BufferedImage img = new BufferedImage((int) (originalWidth * scaleFactor), (int) (originalHeight * scaleFactor), BufferedImage.TYPE_INT_RGB);
        // fast scale (Java 1.4 & 1.5)
        Graphics g = img.getGraphics();
        g.drawImage(image, 0, 0, img.getWidth(), img.getHeight(), null);
        return img;
    }


    public static BufferedImage removeBorder(BufferedImage imp) {
        int w = imp.getWidth();
        int h = imp.getHeight();
        int borderH = 0;
        int borderW = 0;
        outer:
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (!isBlack(imp, x, y, 140)) {
                    borderH = y;
                    break outer;
                }
            }
        }

        outer:
        for (int y = 0; y < h; y++) {

            for (int x = 0; x < w; x++) {

                if (!isBlack(imp, x, y, 140)) {
                    borderW = x;
                    break outer;
                }
            }
        }

        if (borderH > 0) {
            try {
                imp = imp.getSubimage(borderW, borderH, w - (2 * borderW), h - (2 * borderH));
            } catch (Exception e) {
                e.printStackTrace();
//                new ImagePlus("failed", imp).show();
//                return null;
            }
        }
        return imp;
    }

    public static BufferedImage reScale(BufferedImage bufferedImage, int maxWidth) {

        BufferedImage image = scaleImage(bufferedImage, maxWidth);
        int w = image.getWidth();
        int h = image.getHeight();
        int cw = w / 2;
        int ch = h / 2;


        int r;
        int g;
        int b;
        int minX = -1;
        int maxX = 0;
        double luminance;
        for (int x = 0; x < w; x++) {
            int pixelRGBValue = image.getRGB(x, ch);
            r = (pixelRGBValue >> 16) & 0xff;
            g = (pixelRGBValue >> 8) & 0xff;
            b = (pixelRGBValue) & 0xff;
//            luminance = (r * 0.299) + (g * 0.587) + (b * 0.114);
            if (r == g && r == b) {
                if (minX == -1) {
                    minX = x;
                }
                maxX = x;

            }

        }

        int minY = -1;
        int maxY = 0;
        for (int y = 0; y < h; y++) {
            int pixelRGBValue = image.getRGB(cw, y);
            r = (pixelRGBValue >> 16) & 0xff;
            g = (pixelRGBValue >> 8) & 0xff;
            b = (pixelRGBValue) & 0xff;
//            luminance = (r * 0.299) + (g * 0.587) + (b * 0.114);
            if (r == g && r == b) {
                if (minY == -1) {
                    minY = y;
                }
                maxY = y;

            }

        }
        minX = minX == -1 ? 0 : minX;
        minY = minY == -1 ? 0 : minY;

        int nh = maxY - minY;
        int nw = maxX - minX;
        if (nh > 0 && nw > 0)
            image = image.getSubimage(minX, minY, nw, nh);

        image = scaleImage(image, maxWidth);
        return image;
    }

    private static boolean isBlack(BufferedImage image, int x, int y,
                                   int luminanceCutOff) {
        int pixelRGBValue;
        int r;
        int g;
        int b;
        double luminance = 0.0;

        // return white on areas outside of image boundaries
        if (x < 0 || y < 0 || x > image.getWidth() || y > image.getHeight()) {
            return false;
        }

        try {
            pixelRGBValue = image.getRGB(x, y);
            r = (pixelRGBValue >> 16) & 0xff;
            g = (pixelRGBValue >> 8) & 0xff;
            b = (pixelRGBValue >> 0) & 0xff;
            luminance = (r * 0.299) + (g * 0.587) + (b * 0.114);
        } catch (Exception e) {
            // ignore.
        }

        return (luminance < luminanceCutOff);
    }

}
