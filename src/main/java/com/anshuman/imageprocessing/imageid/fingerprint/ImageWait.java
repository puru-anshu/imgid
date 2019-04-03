package com.anshuman.imageprocessing.imageid.fingerprint;

import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;

public class ImageWait implements ImageObserver {
    public static void waitToLoad(final Image img) throws IOException {
        final ImageWait iw = new ImageWait(img);
        iw.waitToLoad();
    }

    public static void waitToLoad(final Image img, final long timeOut) throws IOException {
        final ImageWait iw = new ImageWait(img);
        iw.waitToLoad(timeOut);
    }

    boolean m_loaded = false;
    Image m_img;
    private boolean m_error;

    public ImageWait(final Image img) {
        m_img = img;
    }

    public synchronized boolean imageUpdate(final Image img, final int infoflags, final int x,
                                            final int y, final int width, final int height) {
        if ((infoflags & ImageObserver.ERROR) > 0) {
            m_error = true;
            notifyAll();
            return false;
        }
        if ((infoflags & ImageObserver.ERROR) > 0) {
            m_error = true;
            notifyAll();
            return false;
        }
        if ((infoflags & ImageObserver.ALLBITS) > 0) {
            m_loaded = true;
            notifyAll();
            return false;
        }
        return true;
    }

    public void reset() {
        m_loaded = false;
    }

    public synchronized void waitToLoad() throws IOException {
        if (m_loaded || m_img.getWidth(this) > -1) {
            return;
        }
        if (m_error) {
            throw new IOException("could not load image");
        }
        try {
            wait();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void waitToLoad(final long waitTime) throws IOException {
        if (m_loaded || m_img.getWidth(this) > -1) {
            return;
        }
        if (m_error) {
            throw new IOException("could not load image");
        }
        try {
            wait(waitTime);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
