package com.anshuman.imageprocessing.imageid.demo;

/**
 * User: Anshuman
 * Date: Sep 24, 2010
 * Time: 2:40:54 PM
 */


import com.anshuman.imageprocessing.imageid.data.ImageSignature;

import javax.swing.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IndexingThread extends Thread {
    UDemoFrame parent;
    public static final String SIGNATURE_DB = "signature.db";


    public IndexingThread(UDemoFrame parent) {
        this.parent = parent;
    }

    public void run() {
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.setMaximumFractionDigits(0);
        df.setMinimumFractionDigits(0);
        try {
            parent.progressBarIndexing.setValue(0);
            ArrayList<String> images =
                    getAllImages(
                            new File(parent.textfieldIndexDir.getText()), true);
            if (images == null) {
                JOptionPane.showMessageDialog(parent, "Could not find any files in " + parent.textfieldIndexDir.getText(), "No files found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<ImageSignature> pairs = new ArrayList<ImageSignature>();

            int count = 0;
            long time = System.currentTimeMillis();

            ParallelIndexer indexer = new ParallelIndexer(images);
            new Thread(indexer).start();
            ImageSignature doc;
            while ((doc = indexer.getNext()) != null) {
                try {
                    pairs.add(doc);
                } catch (Exception e) {
                    System.err.println("Could not add document.");
                    e.printStackTrace();
                }
                count++;
                float percentage = (float) count / (float) images.size();
                parent.progressBarIndexing.setValue((int) Math.floor(100f * percentage));
                float msleft = (float) (System.currentTimeMillis() - time) / percentage;
                float secLeft = msleft * (1 - percentage) / 1000f;
                String toPaint = "~ " + df.format(secLeft) + " sec. left";
                if (secLeft > 90) toPaint = "~ " + Math.ceil(secLeft / 60) + " min. left";
                parent.progressBarIndexing.setString(toPaint);
            }
            writeToFile(pairs);
            long timeTaken = (System.currentTimeMillis() - time);
            float sec = ((float) timeTaken) / 1000f;

            System.out.println("Finished indexing ...");

            parent.progressBarIndexing.setString(Math.round(sec) + " sec. for " + count + " files");
            parent.buttonStartIndexing.setEnabled(true);
            parent.progressBarIndexing.setValue(100);
            //Write to the file


        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
    }

    private static void writeToFile(List<ImageSignature> sigMap) throws IOException {
        System.out.println("Writing signature ");
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(SIGNATURE_DB));
        out.writeObject(sigMap);
        out.close();
    }


    public static ArrayList<String> getAllImages(File directory, boolean descendIntoSubDirectories) throws IOException {
        ArrayList<String> resultList = new ArrayList<String>(256);
        File[] f = directory.listFiles();
        for (File file : f) {
            if (file != null && (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) && !file.getName().startsWith("tn_")) {
                resultList.add(file.getCanonicalPath());
            }
            if (descendIntoSubDirectories && file.isDirectory()) {
                ArrayList<String> tmp = getAllImages(file, true);
                if (tmp != null) {
                    resultList.addAll(tmp);
                }
            }
        }
        if (resultList.size() > 0)
            return resultList;
        else
            return null;
    }


}

