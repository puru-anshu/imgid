package com.anshuman.imageprocessing.imageid.fingerprint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ImgWindow extends JFrame {
   public static interface DropNotify {
      void handle(final File f);
      void handle(final URL u);
   }
   private static final long serialVersionUID = -3358642818124188182L;
   ImgPanel m_panel;
   public ImgWindow() {
      m_panel = new ImgPanel();
      init();
   }
   public ImgWindow(final Image img) {
      m_panel = new ImgPanel(img);
      init();
   }
   public ImgWindow(final URL url) {
      m_panel = new ImgPanel();
      init();
      final Image img = Toolkit.getDefaultToolkit().createImage(url);
      SwingUtilities.invokeLater(new Runnable(){
         public void run() {
            setImage(img);
         }
      });
   }
   public ImgPanel getPanel() {
      return m_panel;
   }
   @Override
   public boolean imageUpdate(final Image img, final int infoflags, final int x, final int y,
         final int w, final int h) {
      if ((infoflags & SOMEBITS) > 0) {
         int xsize = img.getWidth(null);
         int ysize = img.getHeight(null);
         final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
         xsize += 15;
         ysize += 45;
         setSize(xsize < dim.width ? xsize : dim.width, ysize < dim.height ? ysize : dim.height);
      }
      return super.imageUpdate(img, infoflags, x, y, w, h);
   }
   void init() {
      setLayout(new BorderLayout());
      add(m_panel);
      sizeToFit();
      final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      int xpos = dim.width / 2 - 200 / 2;
      int ypos = dim.height / 2 - 200 / 2 - 100;
      if (xpos < 0) {
         xpos = 0;
      }
      if (ypos < 0) {
         ypos = 0;
      }
      setLocation(xpos, ypos);
   }
   public void setImage(final Image img) {
      m_panel.setImage(img);
      sizeToFit();
   }
   public void sizeToFit() {
      int xsize = 75;
      int ysize = 50;
      if (m_panel.getImage() != null && m_panel.getImage().getHeight(this) > 0) {
         xsize = m_panel.getImage().getWidth(null);
         ysize = m_panel.getImage().getHeight(null);
      }
      xsize += 12;
      ysize += 44;
      setSize(xsize, ysize);
   }
}
