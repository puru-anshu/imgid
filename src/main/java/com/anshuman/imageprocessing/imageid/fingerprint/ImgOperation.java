package com.anshuman.imageprocessing.imageid.fingerprint;


import jnt.FFT.ComplexDouble2DFFT;
import jnt.FFT.ComplexDoubleFFT;
import jnt.FFT.ComplexDoubleFFT_Mixed;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class ImgOperation {

    private static double getBrightness(int red, int green, int blue) {
        return Math.sqrt(
                red * red * .241 +
                        green * green * .691 +
                        blue * green * .068);
    }


    public static Image convert(final Image img, final ImageFilter filter) {
        return Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(img.getSource(), filter));
    }

     static BufferedImage convertToBuffered(final Image img) throws IOException {

        final BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB_PRE);
        bi.getGraphics().drawImage(img, 0, 0, null);
        return bi;
    }

    public static BufferedImage resize(final Image img, final double scale) throws IOException {
        ImageWait.waitToLoad(img);
        final int xsrc = img.getWidth(null);
        final int ysrc = img.getHeight(null);
        final int width = (int) (xsrc * scale);
        final int heigth = (int) (ysrc * scale);
        final BufferedImage buf = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, width, heigth, null);
        return buf;
    }

    public static BufferedImage resize(final Image img, final int width, final int heigth)
            throws IOException {
        ImageWait.waitToLoad(img);
        final BufferedImage buf = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = buf.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, width, heigth, null);
        return buf;
    }


     static double[][] convolve(final double img[][],
                                      final double kernel[][],
                                      final double factor) {
        final int w = img.length;
        final int h = img[0].length;
        final int kw = kernel.length;
        final int kh = kernel[0].length;
        final double r[][] = new double[w - kw][h - kh];
        final int mkw = kw / 2;
        final int mkh = kh / 2;
        for (int x = mkw; x < w - mkw - 1; ++x) {
            final double rcol[] = r[x - mkw];
            for (int y = mkh; y < h - mkh - 1; ++y) {
                int v = 0;
                for (int i = 0; i < kw; ++i) {
                    final double col[] = img[x + i - mkw];
                    final double kcol[] = kernel[i];
                    for (int j = 0; j < kh; ++j) {
                        v += col[y + j - mkh] * kcol[j];
                    }
                }
                v /= factor;
                rcol[y - mkh] = v;
            }
        }
        return r;
    }

    public static int[][] convolve(final int img[][], final int kernel[][], final int factor) {
        final int w = img.length;
        final int h = img[0].length;
        final int kw = kernel.length;
        final int kh = kernel[0].length;
        final int r[][] = new int[w - kw][h - kh];
        final int mkw = kw / 2;
        final int mkh = kh / 2;
        for (int x = mkw; x < w - mkw - 1; ++x) {
            final int rcol[] = r[x - mkw];
            for (int y = mkh; y < h - mkh - 1; ++y) {
                int v = 0;
                for (int i = 0; i < kw; ++i) {
                    final int col[] = img[x + i - mkw];
                    final int kcol[] = kernel[i];
                    for (int j = 0; j < kh; ++j) {
                        v += col[y + j - mkh] * kcol[j];
                    }
                }
                v /= factor;
                rcol[y - mkh] = v;
            }
        }
        return r;
    }

    public static int[][] convolveRGB(final int img[][], final int kernel[][], final int factor) {
        final int w = img.length;
        final int h = img[0].length;
        final int kw = kernel.length;
        final int kh = kernel[0].length;
        final int r[][] = new int[w - kw][h - kh];
        final int mkw = kw / 2;
        final int mkh = kh / 2;
        for (int x = mkw; x < w - mkw - 1; ++x) {
            final int rcol[] = r[x - mkw];
            for (int y = mkh; y < h - mkh - 1; ++y) {
                int v = 0;
                for (int i = 0; i < kw; ++i) {
                    final int col[] = img[x + i - mkw];
                    final int kcol[] = kernel[i];
                    for (int j = 0; j < kh; ++j) {
                        final int c = col[y + j - mkh];
                        final int red = c << 16 & 0xFFFF;
                        final int green = c << 8 & 0xFFFF;
                        final int blue = c & 0xFFFF;
                        v += kcol[j] * (green + blue + red);
                    }
                }
                v /= factor;
                rcol[y - mkh] += v;
            }
        }
        return r;
    }

    public static BufferedImage createARGBImage(final int darr[][]) {
        final int w = darr.length;
        final int h = darr[0].length;
        final int arr[] = new int[w * h];
        for (int i = 0; i < w; ++i) {
            final int col[] = darr[i];
            for (int j = 0; j < h; ++j) {
                arr[j * w + i] = col[j];
            }
        }
        return createARGBImage(arr, w, h);
    }

    public static BufferedImage createARGBImage(final int w, final int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }

    public static BufferedImage createARGBImage(final int arr[], final int w, final int h) {
        final BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bi.setRGB(0, 0, w, h, arr, 0, w);
        return bi;
    }

    public static BufferedImage createGreyImage(final double darr[][]) {
        final int w = darr.length;
        final int h = darr[0].length;
        final int arr[] = new int[w * h];
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < w; ++i) {
            final double col[] = darr[i];
            for (int j = 0; j < h; ++j) {
                if (col[j] > max) {
                    max = col[j];
                }
                if (col[j] < min) {
                    min = col[j];
                }
            }
        }
        final double r = max - min;
        for (int i = 0; i < w; ++i) {
            final double col[] = darr[i];
            for (int j = 0; j < h; ++j) {
                arr[j * w + i] = (int) ((col[j] - min) * 0xffff / r);
            }
        }
        return createGreyImage(arr, w, h);
    }

    public static BufferedImage createGreyImage(final int darr[][]) {
        final int w = darr.length;
        final int h = darr[0].length;
        final int arr[] = new int[w * h];
        for (int i = 0; i < w; ++i) {
            final int col[] = darr[i];
            for (int j = 0; j < h; ++j) {
                arr[j * w + i] = col[j];
            }
        }
        return createGreyImage(arr, w, h);
    }

    public static BufferedImage createGreyImage(final int arr[], final int w, final int h) {
        final BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_USHORT_GRAY);
        final WritableRaster wr = bi.getRaster();
        final int length = w * h;
        final int r[] = new int[length];
        System.arraycopy(arr, 0, r, 0, length);
        wr.setSamples(0, 0, w, h, 0, r);
        return bi;
    }

    public static byte[] createJPG(final BufferedImage img) throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/jpeg");
        final ImageWriter w = iter.next();
        final ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        w.setOutput(ios);
        w.write(img);
        w.dispose();
        return os.toByteArray();
    }

    public static byte[] createPNG(final BufferedImage img) throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/png");
        final ImageWriter w = iter.next();
        final ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        w.setOutput(ios);
        w.write(img);
        w.dispose();
        return os.toByteArray();
    }


    public static BufferedImage createRGBImage(final int darr[][]) {
        final int w = darr.length;
        final int h = darr[0].length;
        final int arr[] = new int[w * h];
        for (int i = 0; i < w; ++i) {
            final int col[] = darr[i];
            for (int j = 0; j < h; ++j) {
                arr[j * w + i] = col[j];
            }
        }
        return createRGBImage(arr, w, h);
    }

    public static BufferedImage createRGBImage(final int arr[][][]) {
        final int w = arr[0].length;
        final int h = arr[0][0].length;
        final int a[] = new int[w * h];
        final int[] sa = new int[]{16, 8, 0};
        for (int k = 0; k < arr.length; ++k) {
            final int shift = sa[k];
            final int[][] b = arr[k];
            for (int i = 0; i < w; ++i) {
                final int col[] = b[i];
                for (int j = 0; j < h; ++j) {
                    a[j * w + i] |= col[j] << shift;
                }
            }
        }
        return createRGBImage(a, w, h);
    }

    public static BufferedImage createRGBImage(final int w, final int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    }

    public static BufferedImage createRGBImage(final int arr[], final int w, final int h) {
        final BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        bi.setRGB(0, 0, w, h, arr, 0, w);
        return bi;
    }


    public static double[][] dx(final double arr[][]) {
        return convolve(arr, new double[][]{{-1, 0, 1}, {-2, 0, +2},
                {-1, 0, 1}}, 8);
    }

    public static double[][] dy(final double arr[][]) {
        return convolve(arr,
                new double[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}}, 8);
    }

    public static BufferedImage edgeRGB(BufferedImage img) {
        final double[][][] arr = getRGBDArray(img);
        double[][][] doubles = edgeRGB(arr);
        return createRGBImage(doubles);
    }

    public static BufferedImage createRGBImage(final double darr[][][]) {
        Arr.normalize(darr, 255);
        return createRGBImage(Arr.casti(darr));
    }

    public static double[][][] edgeRGB(final double rgb[][][]) {
        int w = rgb[0].length;
        int h = rgb[0][0].length;
        final double r[][][] = new double[rgb.length][w][h];
        for (int k = 0; k < rgb.length; ++k) {
            final double dx[][] = convolve(rgb[k], new double[][]{{-1, 0, 1}, {-2, 0, +2},
                    {-1, 0, 1}}, 8);
            final double dy[][] = convolve(rgb[k], new double[][]{{-1, -2, -1}, {0, 0, 0},
                    {1, 2, 1}}, 8);
            w = dx.length;
            h = dx[0].length;
            for (int i = 0; i < w - 1; ++i) {
                final double cdx[] = dx[i];
                final double cdy[] = dy[i];
                for (int j = 0; j < h - 1; ++j) {
                    final double x = cdx[j];
                    final double y = cdy[j];
                    final double v = Math.sqrt(x * x + y * y);
                    r[k][i][j] = v;
                }
            }
        }
        return r;
    }

    public static int[][][] edgeRGB(final int rgb[][][]) {
        int w = rgb[0].length;
        int h = rgb[0][0].length;
        final int r[][][] = new int[rgb.length][w][h];
        for (int k = 0; k < rgb.length; ++k) {
            final int shift = k * 8;
            final int[][] a = r[k];
            final int dx[][] = convolve(rgb[k], new int[][]{{-1, 0, 1}, {-2, 0, +2},
                    {-1, 0, 1}}, 8);
            final int dy[][] = convolve(rgb[k],
                    new int[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}}, 8);
            w = dx.length;
            h = dx[0].length;
            for (int i = 0; i < w - 1; ++i) {
                final int rcol[] = a[i];
                final int cdx[] = dx[i];
                final int cdy[] = dy[i];
                for (int j = 0; j < h - 1; ++j) {
                    final int x = cdx[j];
                    final int y = cdy[j];
                    final int v = (int) Math.sqrt(x * x + y * y);
                    rcol[j] |= v << shift;
                }
            }
        }
        return r;
    }

    public static double[][] edgeRGB(final int rgb[][]) {
        int w = rgb.length;
        int h = rgb[0].length;
        final double r[][] = new double[w][h];
        final int dx[][] = convolveRGB(rgb,
                new int[][]{{-1, 0, 1}, {-2, 0, +2}, {-1, 0, 1}}, 8);
        final int dy[][] = convolveRGB(rgb, new int[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}},
                8);
        w = dx.length;
        h = dx[0].length;
        for (int i = 0; i < w - 1; ++i) {
            final double rcol[] = r[i];
            final int cdx[] = dx[i];
            final int cdy[] = dy[i];
            for (int j = 0; j < h - 1; ++j) {
                final double x = cdx[j];
                final double y = cdy[j];
                rcol[j] = Math.sqrt(x * x + y * y);
            }
        }
        return r;
    }


    public static BufferedImage equalize(final BufferedImage img) {
        final Random ran = new Random();
        final int arr[] = getArray(img);
        final double h[] = new double[arr.length];
        for (int j = 0; j < arr.length; ++j) {
            final int c = arr[j];
            final int r = c >> 16 & 0xff;
            final int g = c >> 8 & 0xff;
            final int b = c & 0xff;
            h[j] = r + g + b + (ran.nextDouble() - 0.5) / 2;
        }
        Arrays.sort(h);
        for (int j = 0; j < arr.length; ++j) {
            final int c = arr[j];
            final int r = c >> 16 & 0xff;
            final int g = c >> 8 & 0xff;
            final int b = c & 0xff;
            final double v = r + g + b + (ran.nextDouble() - 0.5) / 2;
            int ind = Arrays.binarySearch(h, v);
            if (ind < 0) {
                ind = -(ind + 1);
            }
            double f = (double) ind / h.length * 765. / v;
            final int max = r > b ? (r > g ? r : g) : b > g ? b : g;
            if (f * max > 255) {
                f = 255. / max;
            }
            final int nr = (int) (f * r);
            final int ng = (int) (f * g);
            final int nb = (int) (f * b);
            arr[j] = nr << 16 | ng << 8 | nb;
        }
        return createRGBImage(arr, img.getWidth(), img.getHeight());
    }

    public static void equalize(final double arr[][]) {
        final int w = arr.length;
        final int h = arr[0].length;
        final double length = w * h;
        final double r[] = new double[w * h];
        for (int i = 0; i < w; ++i) {
            final int t = i * h;
            final double col[] = arr[i];
            System.arraycopy(col, 0, r, t, h);
        }
        Arrays.sort(r);
        for (int i = 0; i < w; ++i) {
            final double col[] = arr[i];
            for (int j = 0; j < h; ++j) {
                col[j] = Arrays.binarySearch(r, col[j]) / length;
            }
        }
    }

    public static void equalize(final double arr[][][]) {
        final int nc = arr.length;
        final int w = arr[0].length;
        final int h = arr[0][0].length;
        final double length = nc * w * h;
        final double r[] = new double[nc * w * h];
        for (int k = 0; k < nc; ++k) {
            for (int i = 0; i < w; ++i) {
                System.arraycopy(arr[k][i], 0, r, k * w * h + i * h, h);
            }
        }
        Arrays.sort(r);
        for (int k = 0; k < nc; ++k) {
            for (int i = 0; i < w; ++i) {
                for (int j = 0; j < h; ++j) {
                    arr[k][i][j] = Arrays.binarySearch(r, arr[k][i][j]) / length;
                }
            }
        }
    }


    public static void expm1(double[][] r) {
        for (int i = 0; i < r.length; ++i) {
            for (int j = 0; j < r[0].length; ++j) {
                final double v = r[i][j];
                if (v < 0) {
                    r[i][j] = -Math.expm1(-v);
                } else {
                    r[i][j] = Math.expm1(v);
                }
            }
        }
    }

    public static void fct(double r[], boolean forward) {
        final double x[] = new double[2 * r.length];
        for (int i = 0; i < r.length; ++i) {
            x[i] = r[i];
            if (i > 0) {
                x[x.length - i] = r[i];
            }
        }
        final double[] im = new double[x.length];
        fft(x, im, forward);
        System.arraycopy(Arr.subset(x, 0, r.length), 0, r, 0, r.length);
    }

    public static void fct(double r[][], boolean forward) {
        for (double[] aR : r) {
            fct(aR, forward);
        }
        for (int i = 0; i < r[0].length; ++i) {
            final double[] c = Arr.getColumn(r, i);
            fct(c, forward);
            for (int j = 0; j < c.length; ++j) {
                r[j][i] = c[j];
            }
        }
    }

    public static void fct(double r[][], int scale, boolean forward) {
        for (double[] aR : r) {
            final double x[] = new double[aR.length * scale];
            System.arraycopy(aR, 0, x, 0, aR.length);
            fct(x, forward);
            System.arraycopy(x, 0, aR, 0, aR.length);
        }
        for (int i = 0; i < r[0].length; ++i) {
            final double[] c = Arr.getColumn(r, i);
            final double x[] = new double[r.length * scale];
            System.arraycopy(c, 0, x, 0, c.length);
            fct(x, forward);
            for (int j = 0; j < c.length; ++j) {
                r[j][i] = x[j];
            }
        }
    }

     static void fft(final double r[], final double im[], boolean forward) {
        final ComplexDoubleFFT cdf = new ComplexDoubleFFT_Mixed(r.length);
        final double c[] = new double[r.length * 2];
        for (int i = 0; i < r.length; ++i) {
            c[2 * i] = r[i];
            c[2 * i + 1] = im[i];
        }
        if (forward) {
            cdf.transform(c);
        } else {
            cdf.inverse(c);
        }
        for (int i = 0; i < r.length; ++i) {
            r[i] = c[2 * i];
            im[i] = c[2 * i + 1];
        }
    }

    public static void fft(final double r[][], final double im[][], final boolean forward) {
        final int w = r.length, h = r[0].length;
        final double[] rd = get1D(r);
        final double[] id = get1D(im);
        fft2D(rd, id, w, h, forward);
        for (int i = 0; i < h; ++i) {
            final int t = i * w;
            for (int j = 0; j < w; ++j) {
                r[j][i] = rd[t + j];
                im[j][i] = id[t + j];
            }
        }
    }

    public static void fft(final double r[], final double im[], int scale, boolean forward) {
        final ComplexDoubleFFT cdf = new ComplexDoubleFFT_Mixed(r.length * scale);
        final double c[] = new double[r.length * 2 * scale];
        for (int i = 0; i < r.length; ++i) {
            c[2 * i] = r[i];
            c[2 * i + 1] = im[i];
        }
        if (forward) {
            cdf.transform(c);
        } else {
            cdf.inverse(c);
        }
        for (int i = 0; i < r.length; ++i) {
            r[i] = c[2 * i];
            im[i] = c[2 * i + 1];
        }
    }

    public static void fft(final double r[][], final double im[][], int scale, final boolean forward) {
        for (int i = 0; i < r.length; ++i) {
            fft(r[i], im[i], scale, forward);
        }
        for (int i = 0; i < r[0].length; ++i) {
            final double[] cr = Arr.getColumn(r, i);
            final double[] ci = Arr.getColumn(im, i);
            fft(cr, ci, scale, forward);
            for (int j = 0; j < cr.length; ++j) {
                r[j][i] = cr[j];
                im[j][i] = ci[j];
            }
        }
    }

    public static void fft2D(final double r[], final double im[], final int w, final int h,
                             final boolean forward) {
        final ComplexDouble2DFFT cdf = new ComplexDouble2DFFT(h, w);
        final double c[] = new double[r.length * 2];
        for (int i = 0; i < r.length; ++i) {
            c[2 * i] = r[i];
            c[2 * i + 1] = im[i];
        }
        if (forward) {
            cdf.transform(c, w * 2);
        } else {
            cdf.backtransform(c, w * 2);
        }

        for (int i = 0; i < r.length; ++i) {
            r[i] = c[2 * i];
            im[i] = c[2 * i + 1];
        }
    }

    public static double[] get1D(final double arr[][]) {
        final int w = arr.length;
        final int h = arr[0].length;
        final double r[] = new double[w * h];
        for (int j = 0; j < h; ++j) {
            final int t = j * w;
            for (int i = 0; i < w; ++i) {
                r[t + i] = arr[i][j];
            }
        }
        return r;
    }

    public static int[] get1D(final int arr[][]) {
        final int w = arr.length;
        final int h = arr[0].length;
        final int r[] = new int[w * h];
        for (int j = 0; j < h; ++j) {
            final int t = j * w;
            for (int i = 0; i < w; ++i) {
                r[t + i] = arr[i][j];
            }
        }
        return r;
    }

    public static int[][] get2dArray(final BufferedImage img) {
        final int w = img.getWidth();
        final int h = img.getHeight();
        final int arr[][] = new int[w][h];
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                arr[j][i] = img.getRGB(j, i);
            }
        }
        return arr;
    }

    public static double[][] get2dArray(final double img[], final int w, final int h) {
        final double arr[][] = new double[w][h];
        for (int i = 0; i < h; ++i) {
            final int t = i * w;
            for (int j = 0; j < w; ++j) {
                arr[j][i] = img[t + j];
            }
        }
        return arr;
    }

    public static int[][] get2dArray(final int img[], final int w, final int h) {
        final int arr[][] = new int[w][h];
        for (int i = 0; i < h; ++i) {
            final int t = i * w;
            for (int j = 0; j < w; ++j) {
                arr[j][i] = img[t + j];
            }
        }
        return arr;
    }

    public static double luminance(float r, float g, float b) {
//        return 0.299f * r + 0.587f * g + 0.114f * b;
        return 0.2126 * r + 0.7152f * g + 0.0722 * b;
    }


    public static double[][] readLuminanceFromImage(BufferedImage sourceImage) {
        int type = sourceImage.getType();

        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int picsize = width * height;
        double[] data = new double[picsize];

        if (type == BufferedImage.TYPE_INT_RGB || type == BufferedImage.TYPE_INT_ARGB) {
            int[] pixels = (int[]) sourceImage.getData().getDataElements(0, 0, width, height, null);
            for (int i = 0; i < picsize; i++) {
                int p = pixels[i];
                int r = (p & 0xff0000) >> 16;
                int g = (p & 0xff00) >> 8;
                int b = p & 0xff;
                data[i] = luminance(r, g, b);
            }
        } else if (type == BufferedImage.TYPE_BYTE_GRAY || type == BufferedImage.TYPE_CUSTOM) {
            byte[] pixels = (byte[]) sourceImage.getData().getDataElements(0, 0, width, height, null);
            for (int i = 0; i < picsize; i++) {
                data[i] = (pixels[i] & 0xff);
            }
        } else if (type == BufferedImage.TYPE_USHORT_GRAY) {
            short[] pixels = (short[]) sourceImage.getData().getDataElements(0, 0, width, height, null);
            for (int i = 0; i < picsize; i++) {
                data[i] = (pixels[i] & 0xffff) / 256;
            }
        } else if (type == BufferedImage.TYPE_3BYTE_BGR) {
            byte[] pixels = (byte[]) sourceImage.getData().getDataElements(0, 0, width, height, null);
            int offset = 0;
            for (int i = 0; i < picsize; i++) {
                int b = pixels[offset++] & 0xff;
                int g = pixels[offset++] & 0xff;
                int r = pixels[offset++] & 0xff;
                data[i] = luminance(r, g, b);
            }
        } else {
            throw new IllegalArgumentException("Unsupported image type: " + type);
        }
        return get2dArray(data, width, height);
    }


    public static BufferedImage getGrayBufferedImage(BufferedImage image) {
        /* PlanarImage planarImage = new RenderedImageAdapter(image);
     BufferedImage sourceImage = planarImage.getAsBufferedImage();*/

        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        BufferedImage sourceImgGray = op.filter(image, null);

        return sourceImgGray;
    }

    public static double[][] get2dGrey(final BufferedImage img) {
        final int w = img.getWidth(null);
        final int h = img.getHeight(null);
        final double arr[][] = new double[w][h];
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                final int c = img.getRGB(i, j);
                final int v = (c >> 16 & 0xff) + (c >> 8 & 0xff) + (c & 0xff);
                arr[i][j] = (double) v * 0xffff / 768.;
            }
        }
        return arr;
    }


    public static double[][] get2dlumin(final BufferedImage img) {
        final int w = img.getWidth(null);
        final int h = img.getHeight(null);
        final double arr[][] = new double[w][h];
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                final int c = img.getRGB(i, j);
                arr[i][j] = Color.RGBtoHSB(c >> 16 & 0xff, (c >> 8 & 0xff), (c & 0xff), null)[2];

            }
        }
        return arr;
    }

    public static int[][] get2dGrey(final int img[], final int w, final int h) {
        final int arr[][] = get2dArray(img, w, h);
        for (int i = 0; i < h; ++i) {
            for (int j = 0; j < w; ++j) {
                final int c = arr[j][i];
                arr[j][i] = (c & 0xff0000) >> 16 | (c & 0xff00) >> 8 | c & 0xff;
            }
        }
        return arr;
    }

    public static int[] getARGB(final int pixel) {
        return new int[]{(pixel >> 16 & 0xff), (pixel >> 8 & 0xff), (pixel & 0xff)};
    }

    public static int[] getArray(final BufferedImage img) {
        final int w = img.getWidth();
        final int h = img.getHeight();
        final int arr[] = new int[w * h];
        for (int i = 0; i < h; ++i) {
            final int t = i * w;
            for (int j = 0; j < w; ++j) {
                arr[t + j] = img.getRGB(j, i);
            }
        }
        return arr;
    }

    public static double[] getDouble(final int arr[]) {
        double max = Double.MIN_VALUE;
        final double d[] = new double[arr.length];
        for (int anArr : arr) {
            if (anArr > max) {
                max = anArr;
            }
        }
        for (int i = 0; i < arr.length; ++i) {
            d[i] = arr[i] / max;
        }
        return d;
    }

    public static double[][] getDouble(final int arr[][]) {
        final int w = arr.length;
        final int h = arr[0].length;
        double max = Double.MIN_VALUE;
        final double d[][] = new double[w][h];
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                if (arr[i][j] > max) {
                    max = arr[i][j];
                }
            }
        }
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                d[i][j] = arr[i][j] / max;
            }
        }
        return d;
    }

    public static int[] getGreenGradient(final int length) {
        final int r[] = new int[length];
        final int b[] = new int[length];
        final int g[] = new int[length];
        for (int i = 0; i < length; ++i) {
            r[i] = (int) (scurve(3, (i - length / 2.) / length) * 255)
                    + (int) (128. * i / length);
        }
        for (int i = 0; i < length; ++i) {
            g[i] = (int) (400. * (i + length / 10.) / length);
            if (g[i] > 255) {
                g[i] = 255;
            }
        }
        for (int i = 0; i < length; ++i) {
            b[i] = (int) (scurve(3, (i - length / 2.) / length) * 255)
                    + (int) (128. * i / length);
        }
        final int c[] = new int[length];
        for (int i = 0; i < length; ++i) {
            c[i] = setRGB(r[i], g[i], b[i]);
        }
        return c;
    }

    public static int[] getGrey(final BufferedImage img) {
        final int w = img.getWidth(null);
        final int h = img.getHeight(null);
        final int arr[] = new int[w * h];
        for (int i = 0; i < h; ++i) {
            final int t = i * w;
            for (int j = 0; j < w; ++j) {
                final int c = img.getRGB(j, i);
                arr[t + j] = (c & 0xff0000) >> 16 | (c & 0xff00) >> 8 | c & 0xff;
            }
        }
        return arr;
    }

    public static int[] getGrey(final int img[]) {
        final int arr[] = new int[img.length];
        for (int i = 0; i < img.length; ++i) {
            final int c = img[i];
            final int v = (c >> 16 & 0xff) + (c >> 8 & 0xff) + (c & 0xff);
            arr[i] = (int) ((double) v * 0xffff / 768.);
        }
        return arr;
    }

    public static double[][][] getHSV(double rgb[][][]) {
        final int w = rgb[0].length;
        final int h = rgb[0][0].length;
        final double arr[][][] = new double[3][w][h];
        final double a[] = new double[3];
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                RGBtoHSB(rgb[0][i][j], rgb[1][i][j], rgb[2][i][j], a);
                for (int k = 0; k < 3; ++k) {
                    arr[k][i][j] = a[k];
                }
            }
        }
        return arr;
    }

    public static double[][][] getHSV(int rgb[][][]) {
        final int w = rgb[0].length;
        final int h = rgb[0][0].length;
        final double arr[][][] = new double[3][w][h];
        final float a[] = new float[3];
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                Color.RGBtoHSB(rgb[0][i][j], rgb[1][i][j], rgb[2][i][j], a);
                for (int k = 0; k < 3; ++k) {
                    arr[k][i][j] = a[k];
                }
            }
        }
        return arr;
    }

    public static double[][][] getRGB(double hsv[][][]) {
        final int w = hsv[0].length;
        final int h = hsv[0][0].length;
        final double arr[][][] = new double[3][w][h];
        final double a[] = new double[3];
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                HSBtoRGB(hsv[0][i][j], hsv[1][i][j], hsv[2][i][j], a);
                for (int k = 0; k < 3; ++k) {
                    arr[k][i][j] = a[k];
                }
            }
        }
        return arr;
    }

    public static int[] getRGB(final int pixel) {
        return new int[]{(pixel >> 16 & 0xff), (pixel >> 8 & 0xff), (pixel & 0xff)};
    }

    public static int[][][] getRGBArray(BufferedImage img) {
        final int w = img.getWidth();
        final int h = img.getHeight();
        final int arr[] = getArray(img);
        return getRGBArray(arr, w, h);
    }

    public static int[][][] getRGBArray(final int arr[], final int w, final int h) {
        final int r[][][] = new int[3][w][h];
        final int[] sa = new int[]{16, 8, 0};
        for (int i = 0; i < 3; ++i) {
            final int img[][] = r[i];
            final int shift = sa[i];
            for (int j = 0; j < h; ++j) {
                final int t = j * w;
                for (int k = 0; k < w; ++k) {
                    img[k][j] = arr[t + k] >> shift & 0xFF;
                }
            }
        }
        return r;
    }

    public static double[][][] getRGBDArray(BufferedImage img) {
        final int arr[] = getArray(img);
        final int w = img.getWidth();
        final int h = img.getHeight();
        final double r[][][] = new double[3][w][h];
        final int[] sa = new int[]{16, 8, 0};
        for (int i = 0; i < 3; ++i) {
            final int shift = sa[i];
            for (int j = 0; j < h; ++j) {
                final int t = j * w;
                for (int k = 0; k < w; ++k) {
                    r[i][k][j] = arr[t + k] >> shift & 0xFF;
                }
            }
        }
        return r;
    }

    public static double[][][] getRGBDArray(final int arr[], final int w, final int h) {
        final double r[][][] = new double[3][w][h];
        final int[] sa = new int[]{16, 8, 0};
        for (int i = 0; i < 3; ++i) {
            final int shift = sa[i];
            for (int j = 0; j < h; ++j) {
                final int t = j * w;
                for (int k = 0; k < w; ++k) {
                    r[i][k][j] = arr[t + k] >> shift & 0xFF;
                }
            }
        }
        return r;
    }

    public static int[] getTemperatureColorScale(final int length) {
        final int r[] = new int[length];
        final int b[] = new int[length];
        final int g[] = new int[length];
        for (int i = 0; i < length; ++i) {
            r[i] = (int) (scurve(6, (i + 0.3 * length) / length) * 255) + (int) (3. * i / length);
        }
        for (int i = 0; i < length; ++i) {
            g[i] = (int) (scurve(6, (i - .1 * length) / length) * 255) + (int) (22. * i / length);
        }
        for (int i = 0; i < length; ++i) {
            b[i] = (int) (scurve(12, 1. * (i - length / 3) / length) * 255) + (int) (31. * i / length);
        }
        final int c[] = new int[length];
        for (int i = 0; i < length; ++i) {
            c[i] = setRGB(r[i], g[i], b[i]);
        }
        return c;
    }

    public static int[] getTemperatureColorScale2(final int length) {
        final double r[] = new double[length];
        final double b[] = new double[length];
        final double g[] = new double[length];
        for (int i = 0; i < length; ++i) {
            r[i] = scurve(12, (double) i / length + 0.45);
        }
        r[0] = 0;
        Arr.normalize(r);
        for (int i = 0; i < length; ++i) {
            g[i] = (double) i / length;
        }
        Arr.normalize(g);
        for (int i = 0; i < length; ++i) {
            b[i] = scurve(12, (double) i / length - 0.45);
        }
        Arr.normalize(b);
        final int c[] = new int[length];
        for (int i = 0; i < length; ++i) {
            c[i] = setRGB((int) (255 * r[i]), (int) (255 * g[i]), (int) (255 * b[i]));
        }
        return c;
    }

    private static int[] getTemperatureGreyScale(int length) {
        final int r[] = new int[length];
        final int b[] = new int[length];
        final int g[] = new int[length];
        for (int i = 0; i < length; ++i) {
            // r[i] = i;
            g[i] = i;
            // b[i] = i;
        }
        final int c[] = new int[length];
        for (int i = 0; i < length; ++i) {
            c[i] = setRGB(r[i], g[i], b[i]);
        }
        return c;
    }

    public static void greyEqualize(final double arr[][][]) {
        final int w = arr[0].length;
        final int h = arr[0][0].length;
        final double r[] = new double[w * h];
        int count = 0;
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                r[count++] = arr[0][i][j] + arr[1][i][j] + arr[2][i][j];
            }
        }
        Arrays.sort(r);
        for (int i = 0; i < w; ++i) {
            for (int j = 0; j < h; ++j) {
                final double v = arr[0][i][j] + arr[1][i][j] + arr[2][i][j];
                final double scale = 255. * Arrays.binarySearch(r, v) / count;
                arr[0][i][j] = scale;
                arr[1][i][j] = scale;
                arr[2][i][j] = scale;
            }
        }
    }

    public static void HSBtoRGB(double hue, double saturation, double brightness, double rgb[]) {
        double r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = brightness;
        } else {
            final double h = (hue - Math.floor(hue)) * 6.0;
            final double f = h - Math.floor(h);
            final double p = brightness * (1.0 - saturation);
            final double q = brightness * (1.0 - saturation * f);
            final double t = brightness * (1.0 - saturation * (1.0 - f));
            switch ((int) h) {
                case 0:
                    r = brightness;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = brightness;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = brightness;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = brightness;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = brightness;
                    break;
                case 5:
                    r = brightness;
                    g = p;
                    b = q;
                    break;
            }
        }
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
    }

    public static double[][] integrateX(final double dx[][]) {
        final double arr[][] = new double[dx.length][dx[0].length];
        for (int i = 0; i < dx.length; ++i) {
            for (int j = 1; j < dx[0].length; ++j) {
                arr[i][j] = arr[i][j - 1] + dx[i][j];
            }
        }
        return arr;
    }

    public static double[][][] integrateX(final double dx[][][]) {
        final double arr[][][] = new double[dx.length][dx[0].length][dx[0][0].length];
        for (int i = 0; i < dx.length; ++i) {
            arr[i] = integrateX(dx[i]);
        }
        return arr;
    }

    public static double[][] integrateY(final double dy[][]) {
        final double arr[][] = new double[dy.length][dy[0].length];
        for (int i = 1; i < dy.length; ++i) {
            for (int j = 0; j < dy[0].length; ++j) {
                arr[i][j] = arr[i - 1][j] + dy[i][j];
            }
        }
        return arr;
    }

    public static double[][][] integrateY(final double dy[][][]) {
        final double arr[][][] = new double[dy.length][dy[0].length][dy[0][0].length];
        for (int i = 0; i < dy.length; ++i) {
            arr[i] = integrateY(dy[i]);
        }
        return arr;
    }

    public static BufferedImage invertImage(final BufferedImage img) {
        final int[] arr = getArray(img);
        for (int i = 0; i < arr.length; ++i) {
            final int[] rgb = getRGB(arr[i]);
            arr[i] = setRGB(255 - rgb[0], 255 - rgb[1], 255 - rgb[2]);
        }
        return createRGBImage(arr, img.getWidth(), img.getHeight());
    }


    public static double[][] laplace(final double arr[][]) {
        int w = arr.length;
        int h = arr[0].length;
        final double r[][] = new double[w][h];
        final double dx[][] = convolve(arr, new double[][]{{-1, 0, 1}, {-2, 0, +2},
                {-1, 0, 1}}, 8);
        final double dy[][] = convolve(arr,
                new double[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}}, 8);
        w = dx.length;
        h = dx[0].length;
        for (int i = 0; i < w - 1; ++i) {
            final double rcol[] = r[i];
            final double cdx[] = dx[i];
            final double cdy[] = dy[i];
            for (int j = 0; j < h - 1; ++j) {
                final double x = cdx[j];
                final double y = cdy[j];
                rcol[j] = Math.sqrt(x * x + y * y);
            }
        }
        return r;
    }

    public static BufferedImage loadImage(final byte b[]) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(b));
    }

    public static BufferedImage loadImage(final File f) throws IOException {
        return ImageIO.read(f);
    }

    public static BufferedImage loadImage(final InputStream is) throws IOException {
        return ImageIO.read(is);
    }


    public static BufferedImage loadImage(final ReadableByteChannel bc) throws IOException {
        return loadImage(Channels.newInputStream(bc));
    }

    public static Image loadImage(final URL u) throws IOException {
        return ImageIO.read(u);
    }

    public static void logp1(double[][] r) {
        for (int i = 0; i < r.length; ++i) {
            for (int j = 0; j < r[0].length; ++j) {
                final double v = r[i][j];
                if (v < 0) {
                    r[i][j] = -Math.log1p(-v);
                } else {
                    r[i][j] = Math.log1p(v);
                }
            }
        }
    }

    public static int[] map2Color(final double d[]) {
        final int c[] = new int[d.length];
        final double max = Arr.max(d);
        final double min = Arr.min(d);
        final double r = max - min + 1;
        final int[] scale = getTemperatureColorScale2(32768);
        for (int i = 0; i < d.length; ++i) {
            c[i] = scale[(int) ((d[i] - min) / r * 32768.)];
        }
        return c;
    }

    public static int[][] map2Color(final double[][] a) {
        final int c[][] = new int[a.length][a[0].length];
        final double max = Arr.max(a);
        final double min = Arr.min(a);
        final double r = max - min + 1;
        final int[] scale = getTemperatureColorScale(32768);
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[0].length; ++j) {
                c[i][j] = scale[(int) ((a[i][j] - min) / r * 32768.)];
            }
        }
        return c;
    }

    public static int[] map2Color(final int d[]) {
        final int c[] = new int[d.length];
        final int max = Arr.max(d);
        final int min = Arr.min(d);
        final int r = max - min + 1;
        final int[] scale = getTemperatureColorScale2(32768);
        for (int i = 0; i < d.length; ++i) {
            c[i] = scale[(d[i] - min) * 32768 / r];
        }
        return c;
    }

    public static int[][] map2Grey(double[][] a) {
        final int c[][] = new int[a.length][a[0].length];
        final double max = Arr.max(a);
        final double min = Arr.min(a);
        final double r = max - min + 1;
        final int[] scale = getTemperatureGreyScale(256);
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[0].length; ++j) {
                c[i][j] = scale[(int) ((a[i][j] - min) / r * 256.)];
            }
        }
        return c;
    }

    public static void plot(final BufferedImage bi,
                            final Graph graph,
                            final double y[]) {
        plot(bi, graph, Arr.gendIndex(y.length), y);
    }

    public static void plot(final BufferedImage bi, final Graph graph,
                            final double x[],
                            final double y[]) {
        int h = bi.getHeight();
        int w = bi.getWidth();
        double xmin = Double.MAX_VALUE, xmax = -Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE, ymax = -Double.MAX_VALUE;
        for (int i = 0; i < x.length; ++i) {
            if (x[i] > xmax) {
                xmax = x[i];
            }
            if (x[i] < xmin) {
                xmin = x[i];
            }
            if (y[i] > ymax) {
                ymax = y[i];
            }
            if (y[i] < ymin) {
                ymin = y[i];
            }
        }
        final double xr = xmax - xmin;
        final double yr = ymax - ymin;
        int xi, yi;
        final int c = graph.plotColor.getRGB();
        w = w - graph.left - graph.right;
        h = h - graph.top - graph.bottom;
        if (graph.point == null) {
            for (int i = 0; i < x.length; ++i) {
                xi = (int) ((x[i] - xmin) * (w - 1) / xr) + graph.left;
                yi = h - (int) ((y[i] - ymin) * (h - 1) / yr) + graph.bottom;
                bi.setRGB(xi, yi, c);
            }
        } else {
            for (int i = 0; i < x.length; ++i) {
                xi = (int) ((x[i] - xmin) * (w - 1) / xr) + graph.left;
                yi = h - (int) ((y[i] - ymin) * (h - 1) / yr) + graph.bottom;
                for (int a = 0; a < graph.point.length; ++a) {
                    for (int b = 0; b < graph.point[0].length; ++b) {
                        if (xi - 1 >= 0 && yi >= 0 && xi < w && yi < h && graph.point[a][b] > 0) {
                            bi.setRGB(xi + a, yi + b, graph.point[a][b]);
                        }
                    }
                }
            }
        }
    }

    public static void plot(final BufferedImage bi, final Graph graph,
                            final double x[],
                            final double y[],
                            final double c[]) {
        int h = bi.getHeight();
        int w = bi.getWidth();
        double xmin = Double.MAX_VALUE, xmax = -Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE, ymax = -Double.MAX_VALUE;
        double cmin = Double.MAX_VALUE, cmax = -Double.MAX_VALUE;
        for (int i = 0; i < x.length; ++i) {
            if (x[i] > xmax) {
                xmax = x[i];
            }
            if (x[i] < xmin) {
                xmin = x[i];
            }
            if (y[i] > ymax) {
                ymax = y[i];
            }
            if (y[i] < ymin) {
                ymin = y[i];
            }
            if (c[i] > cmax) {
                cmax = c[i];
            }
            if (c[i] < cmin) {
                cmin = c[i];
            }
        }
        final double xr = xmax - xmin;
        final double yr = ymax - ymin;
        int xi, yi;
        w = w - graph.left - graph.right;
        h = h - graph.top - graph.bottom;
        for (int i = 0; i < x.length; ++i) {
            xi = (int) ((x[i] - xmin) * (w - 1) / xr) + graph.left;
            yi = h - (int) ((y[i] - ymin) * (h - 1) / yr) + graph.bottom;
            bi.setRGB(xi, yi, (int) ((c[i] - cmin) * (cmax - cmin) * 255));
        }
    }

    public static void plotXAxis(final BufferedImage bi, final Graph g) {
        final int h = bi.getHeight();
        final int w = bi.getWidth();
        final int c = g.textColor.getRGB();
        final int y = h - g.bottom;
        for (int i = g.left; i < w - g.right; ++i) {
            bi.setRGB(i, y, c);
        }
    }

    public static void plotYAxis(final BufferedImage bi, final Graph g) {
        final int h = bi.getHeight();
        final int c = g.textColor.getRGB();
        final int x = g.left;
        for (int i = g.top; i < h - g.bottom; ++i) {
            bi.setRGB(x, i, c);
        }
    }

    public static BufferedImage quantize(final BufferedImage img, int x) {
        final int[] arr = getArray(img);
        final int level[] = new int[]{1, 128, 85, 64, 51, 48, 36, 32, 24, 16};
        final int n = level[x];
        for (int i = 0; i < arr.length; ++i) {
            final int[] rgb = getRGB(arr[i]);
            arr[i] = setRGB(rgb[0] / n * n, rgb[1] / n * n, rgb[2] / n * n);
        }
        return createRGBImage(arr, img.getWidth(), img.getHeight());
    }


    public static float[] rgb2hsv(int rgb) {
        final int r = rgb >> 16 & 0xff;
        final int g = rgb >> 8 & 0xff;
        final int b = rgb & 0xff;
        int min;
        int max;
        int delMax;
        if (r > g) {
            min = g;
            max = r;
        } else {
            min = r;
            max = g;
        }
        if (b > max) {
            max = b;
        }
        if (b < min) {
            min = b;
        }
        delMax = max - min;
        float H, S;
        final float V = max;
        if (delMax == 0) {
            H = 0;
            S = 0;
        } else {
            S = delMax / 255f;
            if (r == max) {
                H = (g - b) / (float) delMax / 3f;
            } else if (g == max) {
                H = (1 + (b - r) / (float) delMax) / 3f;
            } else
                H = (2 + (r - g) / (float) delMax) / 3f;
        }
        return new float[]{H, S, V};
    }


    public static double[] RGBtoHSB(double r, double g, double b, double[] hsbvals) {
        double hue, saturation, brightness;
        if (hsbvals == null) {
            hsbvals = new double[3];
        }
        double cmax = r > g ? r : g;
        if (b > cmax) {
            cmax = b;
        }
        double cmin = r < g ? r : g;
        if (b < cmin) {
            cmin = b;
        }
        brightness = cmax;
        if (cmax != 0) {
            saturation = (cmax - cmin) / (float) cmax;
        } else {
            saturation = 0;
        }
        if (saturation == 0) {
            hue = 0;
        } else {
            final double redc = (cmax - r) / (cmax - cmin);
            final double greenc = (cmax - g) / (cmax - cmin);
            final double bluec = (cmax - b) / (cmax - cmin);
            if (r == cmax) {
                hue = bluec - greenc;
            } else if (g == cmax) {
                hue = 2.0f + redc - bluec;
            } else {
                hue = 4.0f + greenc - redc;
            }
            hue = hue / 6.0f;
            if (hue < 0) {
                hue = hue + 1.0f;
            }
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public static void saveBMP(final File file, final BufferedImage img) throws Exception {
        final Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/bmp");
        final ImageWriter w = iter.next();
        final ImageOutputStream ios = ImageIO.createImageOutputStream(file);
        w.setOutput(ios);
        w.write(img);
        ios.close();
    }

    public static void saveJPEG(final File file, final BufferedImage img) throws Exception {
        ImageWriter w = null;
        ImageOutputStream ios = null;
        try {
            final Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/jpeg");
            w = iter.next();
            ios = ImageIO.createImageOutputStream(file);
            w.setOutput(ios);
            int[] arr = getArray(img);
            w.write(createRGBImage(arr, img.getWidth(), img.getHeight()));
        } finally {
            if (w != null) {
                w.dispose();
            }
            if (ios != null) {
                ios.close();
            }
        }
    }

    public static void savePNG(final File file, final BufferedImage img) throws Exception {
        final Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/png");
        final ImageWriter w = iter.next();
        final ImageOutputStream ios = ImageIO.createImageOutputStream(file);
        w.setOutput(ios);
        w.write(img);
        w.dispose();
        ios.close();
    }

    public static double scurve(final double r, final double x) {
        return 1 / (1 + Math.exp(-(r * (x - 0.5))));
    }

    public static int setARGB(final int rgb[]) {
        return rgb[0] << 24 | rgb[1] << 16 | rgb[2] << 8 | rgb[3];
    }

    public static int setARGB(final int a, final int r, final int g, final int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int setRGB(final int rgb[]) {
        return rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }

    public static int setRGB(final int r, final int g, final int b) {
        return r << 16 | g << 8 | b;
    }

    public static double[] spectrum(double x[]) {
        final double[] im = new double[x.length];
        fft(x, im, true);
        return Arr.abs(x, im);
    }

    public static byte[] toBMP(final BufferedImage img) throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/bmp");
        final ImageWriter w = iter.next();
        final ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        w.setOutput(ios);
        w.write(img);
        ios.close();
        return out.toByteArray();
    }

    public static byte[] toJPEG(final BufferedImage img) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/jpeg");
        final ImageWriter w = iter.next();
        final ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
        w.setOutput(ios);
        int[] arr = getArray(img);
        w.write(createRGBImage(arr, img.getWidth(), img.getHeight()));
        w.dispose();
        ios.close();
        return baos.toByteArray();
    }

    public static byte[] toPNG(final BufferedImage img) throws Exception {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/png");
        final ImageWriter w = iter.next();
        final ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        w.setOutput(ios);
        w.write(img);
        w.dispose();
        ios.close();
        return out.toByteArray();
    }

    public static double[][] getSpectrum(BufferedImage img, int bins) {
        double arr[][][] = getHSV(getRGBArray(img));
        double h[] = get1D(arr[0]);
        double s[] = get1D(arr[1]);
        double v[] = get1D(arr[2]);
        h = Arr.histogram(h, bins, Arr.min(h), Arr.max(h));
        s = Arr.histogram(s, bins, Arr.min(s), Arr.max(s));
        v = Arr.histogram(v, bins, Arr.min(v), Arr.max(v));
        return new double[][]{h, s, v};
    }

    public static double[][] getBrightness(BufferedImage img) {
        double arr[][][] = getHSV(getRGBArray(img));
        return arr[2];
    }

    public static double[] getSaturationSpectrum(BufferedImage img, int bins) {
        double arr[][][] = getHSV(getRGBArray(img));
        double r[] = get1D(arr[1]);
        return Arr.histogram(r, bins, Arr.min(r), Arr.max(r));
    }


    public static void drawWindow(final Image img) {
        drawWindow(img, false);
    }

    public static void drawWindow(final Image img, final boolean app) {
        final ImgWindow frame = new ImgWindow(img);
        if (app) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
        frame.setVisible(true);
    }


    public static void main(String[] args) throws Exception {
        BufferedImage img = loadImage(new File("d:\\Photo0412.jpg"));
        img = resize(img, 128, 128);

        double[][] imgData = get2dGrey(img);
        equalize(imgData);
        double[][] imagData = new double[imgData.length][imgData[0].length];
        fft(imgData, imagData, true);

        BufferedImage gImg = createGreyImage(imgData);
//        gImg = edgeRGB(gImg);
/*

        Graph g = new Graph();
        g.point = new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 1, 0}};
        BufferedImage buf = ImgOperation.createRGBImage(800, 600);
        buf.getGraphics().setColor(Color.white);
        buf.getGraphics().fillRect(0, 0, 800, 600);
        double[][] s = getSpectrum(img, 100);
        plot(buf, g, s[0]);
*/
        // plot(buf, g, s[1]);
        // plot(buf, g, s[2]);
        drawWindow(gImg);
    }
}

