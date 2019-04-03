package com.anshuman.imageprocessing.imageid.processing;

import ij.ImageStack;
import ij.process.FloatProcessor;

/**
 * User: Anshuman
 * Date: Oct 14, 2010
 * Time: 4:54:38 PM
 */
public class FFT2D {
    private FloatProcessor fp;
    float[] power;
    float[] phase;
    float[] re;
    float[] imag;

    public FFT2D(FloatProcessor fp) {
        this.fp = fp;
        FHT fht = new FHT(fp);
        fht.transform();
        ImageStack imageStack = fht.getComplexTransform();
        re = (float[]) imageStack.getPixels(1);
        imag = (float[]) imageStack.getPixels(2);
        makePowerSpectrum();
        makePhaseSpectrum();

    }

    private void makePowerSpectrum() {
        //computes the power spectrum
        power = new float[re.length];

        for (int i = 0; i < re.length; i++) {
            float p = (float) Math.sqrt(re[i] * re[i] + imag[i] * imag[i]);
            power[i] = p;

        }
    }

    private void makePhaseSpectrum() {
        //computes the phase spectrum
        phase = new float[re.length];
        for (int i = 0; i < re.length; i++) {
            phase[i] = (float) Math.atan2(imag[i], re[i]);

        }
    }


    public float[] getPower() {
        return power;
    }

    public void setPower(float[] power) {
        this.power = power;
    }

    public float[] getPhase() {
        return phase;
    }

    public void setPhase(float[] phase) {
        this.phase = phase;
    }
}

