package com.anshuman.imageprocessing.imageid.fingerprint;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

public class ImgPanel extends JPanel {
    final class TransferableImplementation implements Transferable, DragSourceListener {
        File f;
        ArrayList<File> list;

        public void dragDropEnd(final DragSourceDropEvent dsde) {
            if (f != null) {
                f.delete();
                f = null;
                list = null;
            }
        }

        public void dragEnter(final DragSourceDragEvent dsde) {
            if (f == null) {
                f = new File(System.currentTimeMillis() + ".png");
                try {
                    ImgOperation.savePNG(f, ImgOperation.convertToBuffered(m_img));
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                list = new ArrayList<File>();
                list.add(f);
            }
        }

        public void dragExit(final DragSourceEvent dse) {
        }

        public void dragOver(final DragSourceDragEvent dsde) {
        }

        public void dropActionChanged(final DragSourceDragEvent dsde) {
        }

        public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException,
                IOException {
            return list;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.javaFileListFlavor};
        }

        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            return flavor.equals(DataFlavor.javaFileListFlavor);
        }
    }

    private static final long serialVersionUID = 8203961877788662289L;
    boolean data = false;
    boolean m_clear = false;
    Image m_img;
    TransferableImplementation trans = new TransferableImplementation();
    static Executor pool = new ThreadPoolExecutor(0, 10, 1000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    public ImgPanel() {
        init();
    }

    public ImgPanel(final Image img) {
        setImage(img);
        init();
    }

    public ImgPanel(final URL url) {
        init();
        pool.execute(new Runnable() {
            public void run() {
                final Image img = Toolkit.getDefaultToolkit().createImage(url);
                try {
                    ImageWait.waitToLoad(img, 10000);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        setImage(img);
                    }
                });
            }
        });
    }

    public Image getImage() {
        return m_img;
    }

    @Override
    public boolean imageUpdate(final Image img, final int infoflags, final int x, final int y,
                               final int w, final int h) {
        setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));
        m_clear = true;
        data = true;
        invalidate();
        return super.imageUpdate(img, infoflags, x, y, w, h);
    }

    void init() {
        DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this,
                TransferHandler.MOVE, new DragGestureListener() {
            public void dragGestureRecognized(final DragGestureEvent dge) {
                DragSource.getDefaultDragSource().startDrag(dge, DragSource.DefaultMoveDrop,
                        trans, trans);
            }
        });
    }

    @Override
    public void paint(final Graphics g) {
        if (m_clear) {
            g.clearRect(getX(), getY(), getWidth(), getHeight());
            m_clear = false;
        }
        if (!data) {
            g.drawString("No Image", 10, 25);
        } else {
            g.drawImage(m_img, 0, 0, this);
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        System.out.println("test");
        super.paintComponent(g);
    }

    public void setImage(final Image img) {
        m_img = img;
        if (img == null || img.getWidth(this) < 0) {
            final Dimension dim = new Dimension(75, 75);
            setPreferredSize(dim);
        } else {
            final Dimension dim = new Dimension(img.getWidth(null), img.getHeight(null));
            m_clear = true;
            data = true;
            setPreferredSize(dim);
            repaint();
        }
        invalidate();
    }
}
