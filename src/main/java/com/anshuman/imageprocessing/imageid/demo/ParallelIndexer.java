package com.anshuman.imageprocessing.imageid.demo;


import com.anshuman.imageprocessing.imageid.data.ImageSignature;
import com.anshuman.imageprocessing.imageid.fingerprint.ImageSigGeneratorFactory;
import com.anshuman.imageprocessing.imageid.fingerprint.ImageSignatureGenerator;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Anshuman
 * Date: Sep 24, 2010
 * Time: 2:45:24 PM
 */
public class ParallelIndexer implements Runnable {
    List<String> imageFiles;
    private int NUMBER_OF_SYNC_THREADS = 2;
    Hashtable<String, Boolean> indexThreads = new Hashtable<String, Boolean>(3);

    LinkedList<ImageSignature> finished = new LinkedList<ImageSignature>();
    private boolean started = false;

    public ParallelIndexer(List<String> imageFiles) {
        this.imageFiles = new LinkedList<String>();
        assert (imageFiles != null);
        this.imageFiles.addAll(imageFiles);

    }

    public void run() {
        for (int i = 1; i < NUMBER_OF_SYNC_THREADS; i++) {
            PhotoIndexer runnable = new PhotoIndexer(this);
            Thread t = new Thread(runnable);
            t.start();
            indexThreads.put(t.getName(), false);
        }
        started = true;
    }

    public void addDoc(ImageSignature doc, String photofile) {
        synchronized (finished) {
            if (doc != null) finished.add(doc);
            Thread.yield();
        }
    }

    public ImageSignature getNext() {
        if (imageFiles.size() < 1) {
            boolean fb = true;
            for (String t : indexThreads.keySet()) {
                fb = fb && indexThreads.get(t);
            }
            if (started && fb) {
                return null;
            }
        }
        while (finished.size() < 1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return finished.removeFirst();

    }

    private String getNextImage() {
        synchronized (imageFiles) {
            if (imageFiles.size() > 0) {
                return imageFiles.remove(0);

            } else return null;
        }

    }

    class PhotoIndexer implements Runnable {
        String photo;
        ParallelIndexer parent;
        private boolean hasFinished = false;

        PhotoIndexer(ParallelIndexer parent) {
            this.parent = parent;

        }

        public void run() {
            while ((photo = parent.getNextImage()) != null) {
                try {
                    ImageSignatureGenerator imageSig = ImageSigGeneratorFactory.getInstance().getImageSigGenerator();
                    ImageSignature doc = imageSig.getSignature(photo);
                    parent.addDoc(doc, photo);
                } catch (Exception e) {
//                    e.printStackTrace();
                    System.err.println(photo);
                    parent.addDoc(null, photo);
                }
            }
            parent.indexThreads.put(Thread.currentThread().getName(), true);
        }


        public boolean hasFinished() {
            return hasFinished;
        }
    }


}

