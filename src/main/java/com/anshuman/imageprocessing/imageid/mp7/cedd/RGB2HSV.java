package com.anshuman.imageprocessing.imageid.mp7.cedd;

public class RGB2HSV {

    public int[] ApplyFilter(int red, int green, int blue) {
        int[] Results = new int[3];
        int HSV_H = 0;
        int HSV_S;
        int HSV_V;

        double MaxHSV = (Math.max(red, Math.max(green, blue)));
        double MinHSV = (Math.min(red, Math.min(green, blue)));

        HSV_V = (int) (MaxHSV);

        HSV_S = 0;
        if (MaxHSV != 0) HSV_S = (int) (255 - 255 * (MinHSV / MaxHSV));

        if (MaxHSV != MinHSV) {

            int IntegerMaxHSV = (int) (MaxHSV);

            if (IntegerMaxHSV == red && green >= blue) {
                HSV_H = (int) (60 * (green - blue) / (MaxHSV - MinHSV));
            } else if (IntegerMaxHSV == red && green < blue) {
                HSV_H = (int) (359 + 60 * (green - blue) / (MaxHSV - MinHSV));
            } else if (IntegerMaxHSV == green) {
                HSV_H = (int) (119 + 60 * (blue - red) / (MaxHSV - MinHSV));
            } else if (IntegerMaxHSV == blue) {
                HSV_H = (int) (239 + 60 * (red - green) / (MaxHSV - MinHSV));
            }


        } else HSV_H = 0;

        Results[0] = HSV_H;
        Results[1] = HSV_S;
        Results[2] = HSV_V;

        return (Results);
    }
}
