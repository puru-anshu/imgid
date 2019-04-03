package com.anshuman.imageprocessing.imageid.data;

import java.io.Serializable;

/**
 * .
 * User: anshu
 * Date: Nov 19, 2010
 * Time: 12:37:14 PM
 */
public interface ImageSignature extends Serializable {

    public String getFilePath();

    public int[] getSignature();

    public double getBerr(ImageSignature other);

    public boolean isMatching(double val);


}
