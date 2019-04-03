/**
 * .
 * User: anshu
 * Date: Nov 19, 2010
 * Time: 1:04:30 PM
 *
 */
package com.anshuman.imageprocessing.imageid.fingerprint;

public class ImageSigGeneratorFactory {
    private static ImageSigGeneratorFactory ourInstance = new ImageSigGeneratorFactory();

    public static ImageSigGeneratorFactory getInstance() {
        return ourInstance;
    }

    private ImageSigGeneratorFactory() {
    }

    public ImageSignatureGenerator getImageSigGenerator() {
//        return new ImageHash();
//        return new EdgeSig();

        return new NewImageSig();
//        return new GaborSig();
//        return new DCTImageSig();

    }

}


