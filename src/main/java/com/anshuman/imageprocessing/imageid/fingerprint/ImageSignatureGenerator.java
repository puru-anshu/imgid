package com.anshuman.imageprocessing.imageid.fingerprint;
import com.anshuman.imageprocessing.imageid.data.ImageSignature;

/**
 * .
 * User: anshu
 * Date: Nov 19, 2010
 * Time: 12:59:15 PM
 */
public interface ImageSignatureGenerator {
    public ImageSignature getSignature(String filePath);

}
