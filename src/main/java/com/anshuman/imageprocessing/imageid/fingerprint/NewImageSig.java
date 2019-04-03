package com.anshuman.imageprocessing.imageid.fingerprint;


import com.anshuman.imageprocessing.imageid.data.ImageSignature;
import com.anshuman.imageprocessing.imageid.data.IntSignature;
import com.anshuman.imageprocessing.imageid.dft2.Dft2d;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

/**
 * .
 * User: anshu
 * Date: Dec 16, 2010
 * Time: 11:58:58 AM
 */
public class NewImageSig implements ImageSignatureGenerator {
    private static final int SIZE = 256;

    /**
     * ----------------------------------------------------------------
     */

    public static void doRadon(double[][] pPtr, double[] pixelPtr, double[] thetaPtr, int M, int N, int xOrigin, int yOrigin, int numAngles, int rFirst) {
        int k, m, n;              /* loop counters */
        double angle;             /* radian angle value */
        double cosine, sine;      /* cosine and sine of current angle */
        double pixel;             /* current pixel value */
        double[] ySinTable, xCosTable;
        /* tables for x*cos(angle) and y*sin(angle) */
        double x, y;
        double r;
        xCosTable = new double[2 * N];
        ySinTable = new double[2 * M];
        for (k = 0; k < numAngles; k++) {
            angle = thetaPtr[k];
            cosine = Math.cos(angle);
            sine = Math.sin(angle);

            /* Radon impulse response locus:  R = X*cos(angle) + Y*sin(angle) */
            /* Fill the X*cos table and the Y*sin table.                      */
            /* x- and y-coordinates are offset from pixel locations by 0.25 */
            /* spaced by intervals of 0.5. */
            for (n = 0; n < N; n++) {
                x = n - xOrigin;
                xCosTable[2 * n] = (x - 0.25) * cosine;
                xCosTable[2 * n + 1] = (x + 0.25) * cosine;
            }
            for (m = 0; m < M; m++) {
                y = yOrigin - m;
                ySinTable[2 * m] = (y - 0.25) * sine;
                ySinTable[2 * m + 1] = (y + 0.25) * sine;
            }

            int a = 0;
//            pixelPtr = iPtr;
            for (n = 0; n < N; n++) {
                for (m = 0; m < M; m++) {
                    pixel = pixelPtr[a++];
                    if (pixel != 0.0) {
                        pixel *= 0.25;

                        r = xCosTable[2 * n] + ySinTable[2 * m] - rFirst;
                        incrementRadon(pPtr[k], pixel, r);

                        r = xCosTable[2 * n + 1] + ySinTable[2 * m] - rFirst;
                        incrementRadon(pPtr[k], pixel, r);

                        r = xCosTable[2 * n] + ySinTable[2 * m + 1] - rFirst;
                        incrementRadon(pPtr[k], pixel, r);

                        r = xCosTable[2 * n + 1] + ySinTable[2 * m + 1] - rFirst;
                        incrementRadon(pPtr[k], pixel, r);
                    }
                }
            }
        }

    }

    private static void incrementRadon(double[] pr, double pixel, double r) {
        int r1;
        double delta;

        r1 = (int) r;
        delta = r - r1;
        pr[r1] += pixel * (1.0 - delta);
        pr[r1 + 1] += pixel * delta;
    }

    public static double[][] rd(BufferedImage img) {
        int numAngles;
        double[] thetaPtr;
        double deg2rad;
        int k;                  /* loop counter */
        int M, N;               /* input image size */
        int xOrigin, yOrigin;   /* center of image */
        int temp1, temp2;       /* used in output size computation */
        int rFirst, rLast;      /* r-values for first and last row of output */
        int rSize;              /* number of rows in output */

        /* Get THETA values */
        deg2rad = 3.14159265358979 / 180.0;
        numAngles = 180;// mxGetM(THETA) * mxGetN(THETA);
        thetaPtr = new double[numAngles];
        for (k = 0; k < numAngles; k++)
            thetaPtr[k] = (k) * deg2rad;

        M = img.getWidth();
        N = img.getHeight();
        /* Where is the coordinate system's origin? */
        xOrigin = Math.max(0, (N - 1) / 2);
        yOrigin = Math.max(0, (M - 1) / 2);

        /* How big will the output be? */
        temp1 = M - 1 - yOrigin;
        temp2 = N - 1 - xOrigin;
        rLast = (int) Math.ceil(Math.sqrt((double) (temp1 * temp1 + temp2 * temp2))) + 1;
        rFirst = -rLast;
        rSize = rLast - rFirst + 1;
        double[][] doubles = ImgOperation.get2dlumin(img);
        double[] imgVals = ImgOperation.get1D(doubles);
        double[][] outVals = new double[numAngles][rSize];
        doRadon(outVals, imgVals, thetaPtr, M, N, xOrigin, yOrigin, numAngles, rFirst);
        normalize2DArray(outVals, 0, 1);

/*
        BufferedImage image = ImgOperation.createGreyImage(outVals);

        int i = image.getWidth();

        int height = image.getHeight();
        System.out.println(i + " X " + height);
        ImgOperation.drawWindow(image);*/

        return outVals;

    }
    //---------------------------------------------------------------------------------------

    private int getGrayLevel(Raster imageRaster, int x, int y) {
        int[] tmp = new int[3];
        return imageRaster.getPixel(x, y, tmp)[0];

    }

    public BufferedImage grayscale(BufferedImage source) {
        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        return op.filter(source, null);
    }

    private Rectangle run(BufferedImage ip) {
        int minX = 0;
        int minY = 0;
        int maxX = 0;
        int maxY = 0;
        BufferedImage bufferedImage = grayscale(ip);
        Raster raster = bufferedImage.getData();
        for (int y = 0; y < ip.getHeight(); y++) {
            for (int x = 0; x < ip.getWidth(); x++) {
                if (getGrayLevel(raster, x, y) > 0) {
                    maxY = y;
                    break;
                }
            }
        }
        for (int x = 0; x < ip.getWidth(); x++) {
            for (int y = 0; y < ip.getHeight(); y++) {
                if (getGrayLevel(raster, x, y) > 0) {
                    maxX = x;
                    break;
                }
            }
        }


        for (int y = ip.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < ip.getWidth(); x++) {
                if (getGrayLevel(raster, x, y) > 0) {
                    minY = y;
                    break;
                }
            }
        }


        for (int x = ip.getWidth() - 1; x >= 0; x--) {
            for (int y = 0; y < ip.getHeight(); y++) {
                if (getGrayLevel(raster, x, y) > 0) {
                    minX = x;
                    break;
                }
            }
        }
        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }


    int w;
    int h;


    public double[][] rdProject(double[][] pix, int views, int scans) {
        int i = 0;
        double[][] proj = new double[views][scans];
        double pos, val, Aleft, Aright;
        int x, y, Xcenter, Ycenter, Ileft, Iright;
        double[] sintab = new double[views];
        double[] costab = new double[views];
        int inputimgsize = pix[0].length;
        //zero all values in projections array
        blank(proj, 0);
        float phi;
        int ang1 = 0;
        int ang2 = 180;
        float stepsize = 180.0f / views;
        for (phi = ang1; phi < ang2; phi = phi + stepsize) {
            sintab[i] = Math.sin((double) phi * Math.PI / 180 - Math.PI / 2);
            costab[i] = Math.cos((double) phi * Math.PI / 180 - Math.PI / 2);
            i++;
        }

        // Project each pixel in the image
        Xcenter = pix.length / 2;
        Ycenter = inputimgsize / 2;

        i = 0;
        double scale = inputimgsize * Math.sqrt(2) / scans;

        //old method used - produces bad artifacts at 'steep' angles
        for (x = -Xcenter; x < Xcenter; x++) {
            for (y = -Ycenter; y < Ycenter; y++) {
                val = pix[x + Xcenter][y + Ycenter];
                for (phi = ang1; phi < ang2; phi = phi + stepsize) {
                    pos = (x * sintab[i] - y * costab[i]) / scale;
                    // Slower more accurate projection
                    // Calculate weighted values for both left and right projections
                    if (pos >= 0) {
                        Ileft = (int) Math.floor(pos);
                        //Ileft += scans/2;
                        Iright = Ileft + 1;
                        Aleft = Iright - pos;
                        Aright = pos - Ileft;
                    } else {
                        Iright = (int) Math.ceil(pos);
                        //Iright += scans/2;
                        Ileft = Iright - 1;
                        Aleft = Math.abs(pos) - Math.abs(Iright);
                        Aright = Math.abs(Ileft) - Math.abs(pos);
                    }

                    //adjust ileft and iright to match array index
                    Ileft += scans / 2;
                    Iright += scans / 2;

                    if (Ileft > 0 && Ileft < scans && Iright > 0 && Iright < scans) {
                        proj[i][Ileft] += (val * Aleft);
                        proj[i][Iright] += (val * Aright);
                    }
                    i++;
                }
                i = 0;
//                val = 0d;
            }
        }
        normalize2DArray(proj, 0, 1);
        return proj;
    }

    private BufferedImage convertType(BufferedImage src, int type) {
        ColorConvertOp cco = new ColorConvertOp(null);
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), type);
        cco.filter(src, dest);
        return dest;
    }

    public ImageSignature getSignature(String filePath) {
        long t = System.currentTimeMillis();
        BufferedImage img;
        try {
            img = ImgOperation.loadImage(new File(filePath));
            img = convertType(img, BufferedImage.TYPE_INT_ARGB);
            Rectangle rectangle = run(img);
            w = rectangle.width;
            h = rectangle.height;
            /*           int w1 = (int) (w * 0.25);
                       int h1 = (int) (h * 0.25);
                       System.out.println("rectangle = " + rectangle);
                       System.out.println(w1 + " X " + h1);
           //            rectangle.grow(-w1, -h1);
                       System.out.println("rectangle = " + rectangle);
            */
            img = img.getSubimage(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//            ImgOperation.drawWindow(img);
            /**
             * @todo see how  to resize the image scaling or make it square
             */
//            double factor = 256.0 / Math.max(img.getWidth(), img.getHeight());
            img = ImgOperation.resize(img, SIZE, SIZE);

            System.out.println(" image pre = " + (System.currentTimeMillis() - t));
/*
            w = img.getWidth();
            h = img.getHeight();
            System.out.println(w + " X " + h);
*/

//            img = new MedianFilter().filter(img, null);

            /**
             * @todo how to get the luminance ?????
             */
            double[][] pVals = ImgOperation.get2dlumin(img);
//            ImgOperation.drawWindow(ImgOperation.createGreyImage(pVals));
            //radon projections
            //double[][] rdVals = rd(img);
            double[][] rdVals = rTransform(pVals, SIZE);// rdProject(pVals, SIZE, SIZE);
            System.out.println(" radon = " + (System.currentTimeMillis() - t));
//            ImgOperation.drawWindow(ImgOperation.createGreyImage(rdVals));
            //auto correlation
            int i = 0;
            for (double[] x : rdVals) {
                rdVals[i++] = autocorrelate(x);
            }
            System.out.println(" correlation = " + (System.currentTimeMillis() - t));
            //log-polar transformation
            rdVals = new LogPolar(rdVals).getOutVals();//new LogPolar2(rdVals).getOutVals()
            System.out.println(" polar  = " + (System.currentTimeMillis() - t));

            float[][] xx = new float[rdVals.length][rdVals[0].length];
            for (int x = 0; x < rdVals.length; x++) {
                for (int y = 0; y < rdVals[0].length; y++) {
                    xx[x][y] = (float) rdVals[x][y];
                }
            }
            // fft
            FloatProcessor processor = new FloatProcessor(xx);
            Dft2d dft2d = new Dft2d(processor);
            int w = processor.getWidth();
            int h = processor.getHeight();
            int N = 21;
            // fingerprint generation 
            int[][] b = generateBinaryString(new FloatProcessor(w, h, dft2d.getPower(), null), N);
            int[][] b1 = generateBinaryString(new FloatProcessor(w, h, dft2d.getPhase(), null), N);
            int[] sig = new int[N - 1];
            char[] x = new char[N - 1];
            char[] x1 = new char[N - 1];
            for (int k = 0; k < b.length; k++) {

                for (int j = 0; j < b[0].length; j++) {
                    x[j] = b[k][j] == 0 ? '0' : '1';
                    x1[j] = b1[k][j] == 0 ? '0' : '1';
                }

                sig[k] = Integer.parseInt(new String(x1), 2) ^ Integer.parseInt(new String(x), 2);
            }


            System.out.println(filePath + " : " + (System.currentTimeMillis() - t) + " ms");
            return new IntSignature(filePath, sig);


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    private int[][] generateBinaryString(ImageProcessor powerSpect, int N) {
        powerSpect.setRoi(0, 0, N, N);
        ImageProcessor processor11 = powerSpect.crop();
        float[][] floatArray = processor11.getFloatArray();
        return doFiltering(floatArray);
    }


    private int[][] doFiltering(float[][] vals) {
        int w = vals.length;
        int h = vals[0].length;
        int[][] toR = new int[w - 1][h - 1];
        final int[][] filter = {{-1, 1}, {1, -1}};
        for (int v = 0; v < h - 1; v++) {
            for (int u = 0; u < w - 1; u++) {
                //compute filter result for position (u,v)
                float sum = 0;
                for (int j = 0; j <= 1; j++) {
                    for (int i = 0; i <= 1; i++) {
                        float p = vals[u + i][v + j];
                        //get the corresponding filter coefficient
                        float c = filter[j][i];
                        sum = sum + c * p;
                    }
                }

                toR[u][v] = sum >= 0 ? 1 : 0;
            }
        }

        return toR;

    }


    private static void blank(double data[][], double value) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = value;
            }
        }
    }

    private static void normalize2DArray(double data[][], double min, double max) {
        double datamax = getMax(data);
        zeronegvals2DArray(data);
        double datamin = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = (((data[i][j] - datamin) * (max)) / datamax);

            }
        }
    }

    private static void zeronegvals2DArray(double data[][]) {
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


    private static double getMin(double data[][]) {

        double min = data[0][0];
        for (double[] aData : data) {
            for (int j = 0; j < data[0].length; j++) {
                if (aData[j] < min) min = aData[j];
            }//System.out.println(min);
        }
        return min;
    }

    private static double getMax(double data[][]) {

        double max = data[0][0];
        for (double[] aData : data) {
            for (int j = 0; j < data[0].length; j++) {
                if (aData[j] > max) max = aData[j];
            } //System.out.println(max);
        }
        return max;
    }


    private double[][] logPolarMap(double[][] rdVals, int nTheta, int nRad) {
        int cx = rdVals.length / 2;
        int cy = rdVals[0].length / 2;
        double rmax = Math.sqrt(cx * cx + cy * cy);
        double theta = 0.0;
        double dt = 2 * Math.PI / nTheta;
        double maxlogr = Math.log(rmax);
        double dr = maxlogr / (nRad - 1);
        double[] cosValues = new double[nTheta];
        double[] sinValues = new double[nTheta];
        for (int i = 0; i < nTheta; ++i, theta += dt) {
            cosValues[i] = Math.cos(theta);
            sinValues[i] = Math.sin(theta);
        }
        double[][] toReturn = new double[nTheta][nRad];
        // Map the centre point
        for (int i = 0; i < nTheta; ++i) {
            toReturn[i][0] = (float) rdVals[cx][cy];

        }
        Rectangle srcRec = new Rectangle(0, 0, rdVals.length, rdVals[0].length);
        double r;
        for (int j = 0; j < nRad; ++j) {
            r = Math.exp(j * dr);
            for (int i = 0; i < nTheta; ++i) {
                int x = (int) Math.round(cx + r * cosValues[i]);
                int y = (int) Math.round(cy + r * sinValues[i]);
                if (srcRec.contains(x, y)) {
                    toReturn[i][j] = rdVals[x][y];
                } else {
                    toReturn[i][j] = 0;
                }
            }
        }
        return toReturn;

    }

    ///////////Autcorrelation

    public static double[] autocorrelate(double[] input) {
        double[] output = new double[input.length];
        double sumSqr = 0;
        for (double d : input) {
            sumSqr += d * d;
        }
        for (int i = 0; i < input.length; i++) {//lag
            for (int j = 0; j < input.length; j++) {//value being correlated
                if ((i + j) < input.length) {
                    output[i] += (input[i + j]) * (input[j]);
                } else {
                    break;
                }
            }
            output[i] /= sumSqr;
        }
        return output;
    }


    public double[][] rTransform(double[][] data, int N) {
        int image_width = data.length;
        int image_height = data[0].length;
        float xofftemp = image_width / 2.0f - 1;
        float yofftemp = image_height / 2.0f - 1;
        int xoffset = (int) Math.rint(xofftemp);
        int yoffset = (int) Math.rint(yofftemp);
        int D = (int) Math.rint(Math.sqrt(xoffset * xoffset + yoffset * yoffset));
        double[][] imRadon = new double[N][D];
        for (int k = 0; k < N; k++) {
            //only consider from PI/8 to 3PI/8 and 5PI/8 to 7PI/8
            //to avoid computational complexity of a steep angle
            if (k == 0) {
                k = N / 8;
                continue;
            } else if (k == (3 * N / 8 + 1)) {
                k = 5 * N / 8;
                continue;
            } else if (k == 7 * N / 8 + 1) {
                k = N;
                continue;
            }

            //for each rho length, determine linear equation and sum the line
            //sum is to sum the values along the line at angle k2pi/N
            //sum2 is to sum the values along the line at angle k2pi/N + N/4
            //The sum2 is performed merely by swapping the x,y axis as if the image were rotated 90 degrees.
            for (int d = 0; d < D; d++) {
                double theta = 2 * k * Math.PI / N;//calculate actual theta
                double alpha = Math.tan(theta + Math.PI / 2);//calculate the slope
                double beta_temp = -alpha * d * Math.cos(theta) + d * Math.sin(theta);//y-axis intercept for the line
                int beta = (int) Math.rint(beta_temp);
                //for each value of m along x-axis, calculate y
                //if the x,y location is within the boundary for the respective image orientations, add to the sum

                double sum1 = 0, sum2 = 0;
                int M = (image_width >= image_height) ? image_width : image_height;
                for (int m = 0; m < M; m++) {
                    //interpolate in-between values using nearest-neighbor approximation
                    //using m,n as x,y indices into image
                    double n_temp = alpha * (m - xoffset) + beta;
                    int n = (int) Math.rint(n_temp);
                    if ((m < image_width) && (n + yoffset >= 0) && (n + yoffset < image_height)) {
                        sum1 += data[m][n + yoffset];
                    }
                    n_temp = alpha * (m - yoffset) + beta;
                    n = (int) Math.rint(n_temp);
                    if ((m < image_height) && (n + xoffset >= 0) && (n + xoffset < image_width)) {
                        sum2 += data[-(n + xoffset) + image_width - 1][m];
                    }
                }
                //assign the sums into the result matrix
                imRadon[k][d] = (float) sum1;
                //assign sum2 to angle position for theta+PI/4
                imRadon[((k + N / 4) % N)][d] = (float) sum2;
            }
        }
        return imRadon;

    }

    public static void main(String[] args) {
        ImageSignature signature = new NewImageSig().getSignature("D:\\imageid\\imsearch\\resources\\images\\test1.JPG");
        int[] ints = signature.getSignature();
        for (int i : ints) {
            System.out.println(i);
        }
    }
}
