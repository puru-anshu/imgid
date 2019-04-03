package com.anshuman.imageprocessing.imageid.demo;


import com.anshuman.imageprocessing.imageid.data.ImageSignature;
import com.anshuman.imageprocessing.imageid.fingerprint.ImageSigGeneratorFactory;
import com.anshuman.imageprocessing.imageid.fingerprint.ImageSignatureGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * User: Anshuman
 * Date: Sep 24, 2010
 * Time: 3:21:48 PM
 */
public class ImageSearcher {
    static List<ImageSignature> list = loadFromFile();

    static int gcd(int a, int b) {
        return (b == 0) ? a : gcd(b, a % b);
    }

    public ImageSearchHits search(String path) throws Exception {
        ImageSearchHits hits = new ImageSearchHits(5);
        ImageSearchHits hitAll = new ImageSearchHits(5);

        list = loadFromFile();
        ImageSignatureGenerator imageSig = ImageSigGeneratorFactory.getInstance().getImageSigGenerator();

        ImageSignature sigPair = imageSig.getSignature(path);
        for (ImageSignature basePair : list) {
            double berr = basePair.getBerr(sigPair);
            if (basePair.isMatching(berr))
                hits.RankThis(new Result(basePair.getFilePath(), berr));
            hitAll.RankThis(new Result(basePair.getFilePath(), berr));

        }
        System.out.println("hits.lastNullIndex = " + hits.lastNullIndex);
  /*      if (hits.lastNullIndex <= 0) {
            for (int i = 1; i < 6; i++) {
                try {
                    BufferedImage image = ImageIO.read(new File(path));
                    image = ImageUtil.scaleImage(image, 256);
                    Rectangle rect = new Rectangle(image.getWidth(), image.getHeight());
                    int i1 = -(i * 10);
                    rect.grow(i1, i1);
                    System.out.println(rect);
                    image = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
                    image = ImageUtil.scaleImage(image, 256);
                    File output = new File(System.getProperty("java.io.tmpdir") + File.separator + System.currentTimeMillis() + ".jpg");
                    ImageIO.write(image, "JPG", output);
                    sigPair = imageSig.getSignature(output.getPath());
                    for (ImageSignature basePair : list) {
                        double berr = basePair.getBerr(sigPair);
                        if (basePair.isMatching(berr))
                            hits.RankThis(new Result(basePair.getFilePath(), berr));
                        hitAll.RankThis(new Result(basePair.getFilePath(), berr));
                    }
                    output.delete();
                    if (hits.lastNullIndex > 0) {
                        break;
                    }
                } catch (Exception e) {

                }
            }
        }
*/
        return hits.lastNullIndex > 0 ? hits : hitAll;
    }


    private static List<ImageSignature> loadFromFile() {

        try {
            File file = new File(IndexingThread.SIGNATURE_DB);
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            List<ImageSignature> sig = (List<ImageSignature>) in.readObject();
            in.close();
            return sig;
        } catch (Exception e) {

            System.out.println("e.getMessage() = " + e.getMessage());

        }
        return null;
    }
}
