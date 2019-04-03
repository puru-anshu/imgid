package com.anshuman.imageprocessing.imageid.fingerprint;

import com.anshuman.imageprocessing.imageid.mathutil.FFTN;
import ij.ImagePlus;
import ij.plugin.ContrastEnhancer;
import ij.process.FloatProcessor;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import java.math.BigInteger;

public class LinearHT {
    ImageProcessor ip;        // reference to original image
    int uc, vc;             // x/y-coordinate of image center
    double rMax;            // maximum radius
    int nAng;                // number of steps for the angle  (a = 0 ... PI)
    int nRad;                 // number of steps for the radius (r = -r_max ... +r_max)
    int cRad;                // array index for zero radius (r = 0)
    double dAng;            // increment of angle
    double dRad;             // increment of radius
    float[][] houghArray;     // Hough accumulator array
    private FloatProcessor fp;
    double[] costTab;
    double[] sinTab;

    // --------------  public methods ------------------------

    public LinearHT(ImageProcessor ip, int nAng, int nRad) {
        this.ip = ip;
        this.uc = ip.getWidth() / 2;
        this.vc = ip.getHeight() / 2;
        this.nAng = nAng;
        this.dAng = Math.PI / nAng;
        this.nRad = nRad;
        this.cRad = nRad / 2;
        this.rMax = Math.sqrt(uc * uc + vc * vc);
        this.dRad = (2.0 * rMax) / nRad;           //
        this.houghArray = new float[nAng][nRad]; // cells are initialized to zero
        costTab = new double[nAng];
        sinTab = new double[nAng];
        for (int i = 0; i < nAng; i++) {
            costTab[i] = Math.cos(dAng * i);
            sinTab[i] = Math.sin(dAng * i);
        }
        fillHoughArray();
    }

    public FloatProcessor getAccumulatorImage() {
        if (houghArray == null)
            throw new Error("houghArray is not initialized");

        return fp;
    }


    private float[] getColumn(int x) {

        float[] toR = new float[fp.getHeight()];
        for (int i = 0; i < fp.getHeight(); i++) {
            toR[i] = fp.getPixel(i, x);

        }
        return toR;
    }


    public double[] getMaxAlongColumn() {
        double[] toR = new double[nAng];
        int i = 0;
        for (; i < nAng; i++) {
            toR[i] = getMax(getColumn(i));
        }

        return toR;

    }

    public double[] getGradientAlongColumn() {
        double[] toR = new double[nAng];
        int i = 0;
        for (; i < nAng; i++) {
            toR[i] = getSumAbs(getColumn(i));

        }
        return toR;


    }

    private float getSumAbs(float[] fls) {
        float sum = 0.0f;
        int i = 0;
        for (; i < fls.length - 1; i++) {
            sum += Math.abs(fls[i] - fls[i + 1]);
        }
        return sum;
    }


    private float getMax(float[] fls) {
        float max = Float.MIN_VALUE;
        for (float f : fls) {
            if (f > max) {
                max = f;
            }
        }
        return max;
    }

    // --------------  nonpublic methods ------------------------

    private void fillHoughArray() {
        int h = ip.getHeight();
        int w = ip.getWidth();
        for (int v = 0; v < h; v++) {
            for (int u = 0; u < w; u++) {
                float i = ip.getPixel(u, v);
//                System.out.println("i = " + i);
                if (i > 0)
                    doPixel(u, v, i);

            }
        }
        fp = new FloatProcessor(nAng, nRad);
        for (int ri = 0; ri < nRad; ri++) {
            for (int ai = 0; ai < nAng; ai++) {
                fp.setf(ai, ri, houghArray[ai][ri]);
            }
        }
        fp.resetMinAndMax();
//        new ImagePlus("_" + nRad, fp).show();
    }

    private void doPixel(int u, int v, float i) {

        int x = u - uc;
        int y = v - vc;
        for (int ai = 0; ai < nAng; ai++) {
            int ri = cRad + (int) Math.rint((x * costTab[ai] + y * sinTab[ai]) / dRad);
            if (ri >= 0 && ri < nRad) {
                houghArray[ai][ri] += i;
            }
        }
    }


    //returns real angle for angle index ai
    double realAngle(int ai) {
        return ai * dAng;
    }

    //returns real radius for radius index ri (with respect to image center <uc,vc>)
    private double realRadius(int ri) {
        return (ri - cRad) * dRad;
    }

    static final float[] kernel = {
            0.0751f, 0.1238f, 0.0751f,
            0.1238f, 0.2042f, 0.1238f,
            0.0751f, 0.1238f, 0.0751f
    };

    public static BigInteger getSig(ImagePlus imp) {
        new ContrastEnhancer().equalize(imp);
        ImageConverter conv = new ImageConverter(imp);
        conv.convertToGray16();
        ImageProcessor ipOrg = ImageSig.getCropProcessor(imp);

        ipOrg.setInterpolate(true);
        ipOrg = ipOrg.resize(256);

        ipOrg.convolve(kernel, 3, 3);

        LinearHT ht = new LinearHT(ipOrg, 256, 64);
        StringBuilder b = generateBString(ht.getGradientAlongColumn());
        b.append(generateBString(ht.getMaxAlongColumn()));
        ht = new LinearHT(ipOrg, 256, 128);


        b.append(generateBString(ht.getGradientAlongColumn()));
        b.append(generateBString(ht.getMaxAlongColumn()));

        return new BigInteger(b.toString(), 2);
    }

    private static StringBuilder generateBString(double[] grArr) {

        double[] input = new double[grArr.length];
        System.arraycopy(grArr, 0, input, 0, grArr.length);
        new FFTN(grArr.length).fft(input);
        double[] ffts = FFTN.PowerSpectrum(input);
        StringBuilder b = new StringBuilder();
        for (int i = 1; i < 64; i++) {
            if (ffts[i + 1] - ffts[i] < 0)
                b.append('0');
            else
                b.append('1');

        }
        return b;
    }

}
