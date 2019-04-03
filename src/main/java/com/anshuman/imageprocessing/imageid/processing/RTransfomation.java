package com.anshuman.imageprocessing.imageid.processing;

import com.anshuman.imageprocessing.imageid.imageutil.MathUtil;
import ij.ImagePlus;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * User: Anshuman
 * Date: Sep 30, 2010
 * Time: 10:39:45 AM
 */
public class RTransfomation {
    public static final Logger logger = Logger.getLogger(RTransfomation.class.getName());

    private static RTransfomation _rTransfomation;

    private int views = 180;


    private final float[] sintab = new float[views];
    private final float[] costab = new float[views];
    private static final float PI = (float) Math.PI;
    private static final float HALF_PI = PI / 2;
    private static final float SANG = (float) (Math.sqrt(2) / 2);


    private RTransfomation() {
        for (int phi = 0; phi < views; phi++) {
            sintab[phi] = (float) Math.sin((float) phi * PI / 180 - HALF_PI);
            costab[phi] = (float) Math.cos((float) phi * PI / 180 - HALF_PI);
        }
    }

    public static RTransfomation getInstance() {
        if (_rTransfomation == null) {
            _rTransfomation = new RTransfomation();
        }
        return _rTransfomation;
    }


    private void fillData(float data[][], float value) {
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = value;
            }
        }
    }


    private void normalize2DArray(float data[][], float max) {

        float datamax = getMax(data);
        logger.fine("Normalizing array; MAX:" + datamax);
        zeronegvals2DArray(data);
        float datamin = 0;

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                data[i][j] = (((data[i][j] - datamin) * (max)) / datamax);

            }
        }
    }

    private void zeronegvals2DArray(float data[][]) {

        float datamin = getMin(data);
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


    private float getMin(float data[][]) {

        float min = data[0][0];
        for (float[] aData : data) {
            for (int j = 0; j < data[0].length; j++) {
                if (aData[j] < min) min = aData[j];
            }
        }
        return min;
    }

    private float getMax(float data[][]) {

        float max = data[0][0];
        for (float[] aData : data) {
            for (int j = 0; j < data[0].length; j++) {
                if (aData[j] > max) max = aData[j];
            }
        }
        return max;
    }

    public float[][] filter(float[][] proj, int scans) {

//        int i, pscans = scans;
        float filter[];//, pfilter[];   //array to store filter and padded filter

        float[] rawdata;
        float[] idata;

        float[][] fproj = new float[views][scans];

        //length of array - no of 'scans', should be a power of 2
        //if scans is a power of 2 then just allocated twice this value for arrays and then pad
        //the projection (and filter data?) with zeroes before applying FFT
        rawdata = new float[scans * 2];
        idata = new float[scans * 2];

        for (int S = 0; S < scans * 2; S++) {
            idata[S] = 0;
        }

        // Initialize the filter
        filter = filter1("ramp", scans * 2, 1);

        // filter each projection
        for (int phi = 0; phi < views; phi++) {

            Arrays.fill(rawdata, 0);
            System.arraycopy(proj[phi], 0, rawdata, 0, scans);
            FFT(1, scans * 2, rawdata, idata);
            for (int S = 0; S < scans * 2; S++) {
                rawdata[S] *= filter[S];
            }
            //perform inverse fourier transform of filtered product
            FFT(0, scans * 2, rawdata, idata);
            System.arraycopy(rawdata, 0, fproj[phi], 0, scans);
            Arrays.fill(idata, 0);

        }
        return fproj;
    }

    public static void FFT(int dir, int s, float[] x, float[] y) {
        int n, i, i1, j, k, i2, l, l1, l2;
        float c1, c2, tx, ty, t1, t2, u1, u2, z;
        int m = (int) (Math.log(s) / Math.log(2));

        /* Calculate the number of points */
        n = 1;
        for (i = 0; i < m; i++)
            n *= 2;

        /* Do the bit reversal */
        i2 = n >> 1;
        j = 0;
        for (i = 0; i < n - 1; i++) {
            if (i < j) {
                tx = x[i];
                ty = y[i];
                x[i] = x[j];
                y[i] = y[j];
                x[j] = tx;
                y[j] = ty;
            }
            k = i2;
            while (k <= j) {
                j -= k;
                k >>= 1;
            }
            j += k;
        }

        /* Compute the FFT */
        c1 = -1.0f;
        c2 = 0.0f;
        l2 = 1;
        for (l = 0; l < m; l++) {
            l1 = l2;
            l2 <<= 1;
            u1 = 1.0f;
            u2 = 0.0f;
            for (j = 0; j < l1; j++) {
                for (i = j; i < n; i += l2) {
                    i1 = i + l1;
                    t1 = u1 * x[i1] - u2 * y[i1];
                    t2 = u1 * y[i1] + u2 * x[i1];
                    x[i1] = x[i] - t1;
                    y[i1] = y[i] - t2;
                    x[i] += t1;
                    y[i] += t2;
                }
                z = u1 * c1 - u2 * c2;
                u2 = u1 * c2 + u2 * c1;
                u1 = z;
            }
            c2 = (float) Math.sqrt((1.0 - c1) / 2.0);
            if (dir == 1)
                c2 = -c2;
            c1 = (float) Math.sqrt((1.0 + c1) / 2.0);
        }

        /* Scaling for forward transform */
        if (dir == 1) {
            for (i = 0; i < n; i++) {
                x[i] /= n;
                y[i] /= n;

            }

        }

    }


    public float[] filter1(String filtname, int scans, float cutoff) {
        int i;
        int Width = scans / 2;
        float tau = Width * cutoff;
        float[] filter = new float[scans];


        filter[0] = 0;
        if (filtname.equals("ramp")) {
            for (i = 1; i <= Width; i++) {
                filter[scans - i] = filter[i] = PI * i;
            }
        } else if (filtname.equals("shepplogan")) {
            for (i = 1; i <= Width; i++) {
                filter[scans - i] = filter[i] = (float) (PI * i * ((Math.sin(PI * i / Width / 2)) / (PI * i / Width / 2)));

            }
        } else if (filtname.equals("hamming")) {

            for (i = 1; i <= Width; i++) {
                if (i <= tau) {
                    filter[scans - i] = filter[i] = (float) (PI * i * (.54 + (.46 * Math.cos(PI * i / tau))));
                } else filter[scans - i] = filter[i] = 0;
            }
        } else if (filtname.equals("hann")) {
            for (i = 1; i <= Width; i++) {
                if (i <= tau) {
                    filter[scans - i] = filter[i] = (float) (PI * i * (1 + (Math.cos(PI * i / tau))));
                } else filter[scans - i] = filter[i] = 0;
            }
        } else if (filtname.equals("cosine")) {
            for (i = 1; i <= Width; i++) {
                if (i <= tau) {
                    filter[scans - i] = filter[i] = (float) (PI * i * (Math.cos(PI * i / tau / 2)));
                } else filter[scans - i] = filter[i] = 0;
            }
        } else if (filtname.equals("blackman")) {
            for (i = 1; i <= Width; i++) {
                if (i <= tau) {
                    filter[scans - i] = filter[i] = (float) (PI * i * (0.42 + (0.5 * Math.cos(PI * i / tau - 1))
                            + (0.08 * Math.cos(2 * PI * i / tau - 1))));
                } else filter[scans - i] = filter[i] = 0;
            }
        }
        //normalizeData(filter, 1);
        setRange1DArray(filter, 0, 1);
        return filter;
    }

    public float[] setRange1DArray(float data[], int min, int max) {

        float[] result = new float[data.length];
        for (int i = 0; i < result.length; i++) {
            if (data[i] > max) {
                result[i] = max;
            } else if (data[i] < min) {
                result[i] = min;
            } else {
                result[i] = data[i];
            }
        }
        return result;
    }

    public float[][] backProject(float[][] proj, int width, int height, boolean useNearest, int scans) {
        int i, a;

        float val = 0, pos;//, Aleft, Aright;
        float[][] bpimage = new float[width][height];
        int x, y, Xcenter, Ycenter;//, Ileft, Iright;
        int S = 0;

        // Initialize output image to zero
        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {
                bpimage[x][y] = 0;
            }
        }
        float[][] fprojection = filter(proj, scans);

        //Back Project each pixel in the image
        Xcenter = width / 2;
        Ycenter = height / 2;

        float scale = width * 1.42f / scans;

        for (x = -Xcenter; x < Xcenter; x++) {

            for (y = -Ycenter; y < Ycenter; y++) {

                if (Math.abs(x) <= Xcenter && Math.abs(y) <= Ycenter) {

                    for (int phi = 0; phi < views; phi++) {
                        pos = (x * sintab[phi] - y * costab[phi]);

                        if (useNearest) {
                            S = Math.round(pos / scale);
                            S = S + scans / 2;
                            if (S < scans && S > 0)
                                val = val + fprojection[phi][S];
                        }
                        //perform linear interpolation
                        else {
                            if (pos >= 0) {
                                a = (int) Math.floor(pos / scale);
                                int b = a + scans / 2;
                                if (b < scans - 1 && b > 0) {
                                    val = (val + fprojection[phi][b] + (fprojection[phi][b + 1]
                                            - fprojection[phi][b])
                                            * (pos / scale - (float) a));
                                }
                            } else if (pos < 0) {
                                a = (int) Math.floor(pos / scale);
                                int b = a + scans / 2;
                                if (b < scans - 1 && b > 0) {
                                    val = (val + fprojection[phi][b] + (fprojection[phi][b]
                                            - fprojection[phi][b + 1])
                                            * (Math.abs(pos / scale) - Math.abs(a)));
                                }
                            }
                        }

                    }
                    bpimage[x + Xcenter][y + Ycenter] = val / views;


                    val = 0;
                }
            }

        }
        normalize2DArray(bpimage, 256);

        return bpimage;

    }


    public float[][] getProjection(float[][] pix, boolean useNearestNeighbour, int scans) {

        int value = Math.min(pix.length, pix[0].length);
        float[][] proj = new float[views][scans];

        float val;
        int x, y, Xcenter, Ycenter;


        fillData(proj, 0);

        // Project each pixel in the image
        Xcenter = pix.length / 2;
        Ycenter = pix[0].length / 2;
        //if no. scans is greater than the image width, then scale will be <1
        float scale = value * 1.42f / scans;

        logger.fine("Generating projection data from image pixels.. ");

        int N;
        val = 0;
        float weight;

        for (int phi = 0; phi < views; phi++) {

            float a = -costab[phi] / sintab[phi];
            float aa = 1 / a;
            if (Math.abs(sintab[phi]) > SANG) {
                for (int S = 0; S < scans; S++) {
                    N = S - scans / 2;
                    float b = (N - costab[phi] - sintab[phi]) / sintab[phi];
                    b = b * scale;
                    for (x = -Xcenter; x < Xcenter; x++) {
                        if (useNearestNeighbour) {
                            //just use nearest neighbour interpolation
                            y = (int) Math.round(a * x + b);
                            if (y >= -Ycenter && y < Ycenter)
                                val += pix[(x + Xcenter)][(y + Ycenter)];

                        } else {
                            //linear interpolation
                            y = (int) Math.round(a * x + b);
                            weight = (float) Math.abs((a * x + b) - Math.ceil(a * x + b));

                            if (y >= -Ycenter && y + 1 < Ycenter)
                                val += (1 - weight) * pix[(x + Xcenter)][(y + Ycenter)]
                                        + weight * pix[(x + Xcenter)][(y + Ycenter + 1)];

                        }
                    }
                    proj[phi][S] = val / Math.abs(sintab[phi]);
                    val = 0;

                }
            } else {
                for (int S = 0; S < scans; S++) {
                    N = S - scans / 2;
                    float bb = (N - costab[phi] - sintab[phi]) / costab[phi];
                    bb = bb * scale;
                    //System.out.println("bb="+bb+" ");
                    for (y = -Ycenter; y < Ycenter; y++) {
                        if (useNearestNeighbour) {
                            x = Math.round(aa * y + bb);
                            if (x >= -Xcenter && x < Xcenter)
                                val += pix[x + Xcenter][y + Ycenter];
                        } else {
                            x = Math.round(aa * y + bb);
                            weight = (float) Math.abs((aa * y + bb) - Math.ceil(aa * y + bb));

                            if (x >= -Xcenter && x + 1 < Xcenter)
                                val += (1 - weight) * pix[(x + Xcenter)][(y + Ycenter)]
                                        + weight * pix[(x + Xcenter + 1)][(y + Ycenter)];

                        }
                    }
                    proj[phi][S] = val / Math.abs(costab[phi]);
                    val = 0;

                }

            }


        }//end of for all angles
//        normalize2DArray(proj, 256);
        return proj;
    }

    public static void main(String[] args) {
        String file1 = "d:\\temp\\capture\\aisha_28.jpg";
//        ImagePlus imp = new ImagePlus("e:\\Lena.PNG");
        ImagePlus imp = new ImagePlus(file1);


        ImageConverter converter = new ImageConverter(imp);
        converter.convertToGray16();
        ImageProcessor ip = imp.getProcessor();
        ip.setInterpolate(true);

//        ip.setInterpolationMethod(ImageProcessor.CENTER_JUSTIFY);
        ip = ip.resize(256, 256);

        new ImagePlus("res", ip).show();
//        ip.autoThreshold();

//        ImageProcessor processor = ip.convertToFloat();
        float[][] pix = ip.getFloatArray();
        float[][] array = RTransfomation.getInstance().getProjection(pix, false, 256);
        float[][] floats = RTransfomation.getInstance().backProject(array, 512, 512, false, 256);
        floats = MathUtil.rowsToColumns(floats);
        FloatProcessor processor1 = new FloatProcessor(floats);
//        processor1.resetMinAndMax();
        processor1.smooth();
        new ImagePlus("reconstructed", processor1).show();

        /*PrintWriter writer;

        try {

            writer = new PrintWriter(new FileWriter("proj.txt"));

            writer.println("1");
            writer.println("1.0");
            writer.println(array.length);
            writer.println(array[0].length);
            for (int y = 0; y < array[0].length; y++) {
                for (int x = 0; x < array.length; x++) {
                    writer.print(array[x][y] + " ");
                }
                writer.println();
            } //writer.println();
            writer.close();
        } catch (IOException ioe) {
            System.out.println("I/O Exception in file writing: " + ioe);
        }*/

    }

}
