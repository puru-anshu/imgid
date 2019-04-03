package com.anshuman.imageprocessing.imageid.demo;

/**
 * User: Anshuman
 * Date: Sep 24, 2010
 * Time: 1:14:18 PM
 */

import com.anshuman.imageprocessing.imageid.imageutil.ImageUtil;
import com.drew.lang.RandomAccessFileReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifReader;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchResultsTableModel extends DefaultTableModel {
    DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
    ImageSearchHits hits = null;
    private ArrayList<ImageIcon> icons;

    /**
     * Creates a new instance of SearchResultsTableModel
     */
    public SearchResultsTableModel() {
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int col) {
        if (col == 0) {
            return "File";
        } else if (col == 1) {
            return "Preview";
        } else if (col == 2) {
            return "Preview";
        }
        return "";
    }

    public Class getColumnClass(int column) {
        if (column == 0) {
            return String.class;
        } else {
            return ImageIcon.class;
        }
    }

    public int getRowCount() {
        if (hits == null) return 0;
        return hits.length();
    }

    public Object getValueAt(int row, int col) {
        if (col == 0) {
            String text = hits.getPath(row);

            return df.format(hits.score(row)) + ": " + text;
//        } else if (col == 1) {
//            return hits.doc(row).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
        } else if (col == 1) {
            return icons.get(row);
        }
        return null;
    }

    /**
     * @param hits
     * @param progress
     */
    public void setHits(ImageSearchHits hits, JProgressBar progress) {
        this.hits = hits;
        icons = new ArrayList<ImageIcon>(hits.length());
        progress.setString("Searching finished. Loading images for result list.");
        for (int i = 0; i < hits.length(); i++) {
            ImageIcon icon = null;
            try {
                BufferedImage img = null;
                String fileIdentifier = hits.getPath(i);
                if (!fileIdentifier.startsWith("http:")) {
                    img = ImageIO.read(new FileInputStream(fileIdentifier));
                } else {
                    img = ImageIO.read(new URL(fileIdentifier));
                }
                icon = new ImageIcon(ImageUtil.scaleImage(img, 128));
                progress.setValue((i * 100) / hits.length());
            } catch (Exception ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
            icons.add(icon);
        }
        progress.setValue(100);
        fireTableDataChanged();
    }

    /**
     * @return
     */
    public ImageSearchHits getHits() {
        return hits;
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

}

