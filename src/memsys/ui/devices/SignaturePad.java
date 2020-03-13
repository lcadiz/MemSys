package memsys.ui.devices;

import Module.Main.Transparent;
import static memsys.ui.devices.CapturerMain.clip;
import com.lti.civil.CaptureException;
import com.lti.civil.CaptureObserver;
import com.lti.civil.CaptureStream;
import com.lti.civil.awt.AWTImageConverter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import memsys.global.FTPFactory;

public class SignaturePad extends javax.swing.JDialog {

    public static CurrentSignature frmParent;
    public static String memID;

    public SignaturePad(CurrentSignature parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        paint();
        setLocationRelativeTo(this);
        capturer.setVisible(false);
        //System.out.println(memID);
    }

    void paint() {
        Container content = this.jPanel1;
        //Creates a new container
        content.setLayout(new BorderLayout());
        //sets the layout

        final PadDraw drawPad = new PadDraw();
        //creates a new padDraw, which is pretty much the paint program

        content.add(drawPad, BorderLayout.CENTER);
    }

    class PadDraw extends JComponent {

        Image image;
        //this is gonna be your image that you draw on
        Graphics2D graphics2D;
        //this is what we'll be using to draw on
        int currentX, currentY, oldX, oldY;
        //these are gonna hold our mouse coordinates

        //Now for the constructors
        public PadDraw() {
            setDoubleBuffered(false);
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    oldX = e.getX();
                    oldY = e.getY();
                }
            });
            //if the mouse is pressed it sets the oldX & oldY
            //coordinates as the mouses x & y coordinates
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    currentX = e.getX();
                    currentY = e.getY();
                    if (graphics2D != null) {
                        graphics2D.drawLine(oldX, oldY, currentX, currentY);
                    }
                    repaint();
                    oldX = currentX;
                    oldY = currentY;
                }

            });
            //while the mouse is dragged it sets currentX & currentY as the mouses x and y
            //then it draws a line at the coordinates
            //it repaints it and sets oldX and oldY as currentX and currentY
        }

        public void paintComponent(Graphics g) {
            if (image == null) {
                image = createImage(getSize().width, getSize().height);
                graphics2D = (Graphics2D) image.getGraphics();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                clear();

            }
            g.drawImage(image, 0, 0, null);
        }
        //this is the painting bit
        //if it has nothing on it then
        //it creates an image the size of the window
        //sets the value of Graphics as the image
        //sets the rendering
        //runs the clear() method
        //then it draws the image

        public void clear() {
            graphics2D.setPaint(Color.white);
            graphics2D.fillRect(0, 0, getSize().width, getSize().height);
            graphics2D.setPaint(Color.black);
            repaint();
        }

        public void black() {
            graphics2D.setPaint(Color.black);
            repaint();
        }

    }

    private static class BackgroundFilter extends RGBImageFilter {

        boolean setUp = false;
        int bgColor;

        @Override
        public int filterRGB(int x, int y, int rgb) {
            int colorWOAlpha = rgb & 0xFFFFFF;
            if (!setUp && x == 0 && y == 0) {
                bgColor = colorWOAlpha;
                setUp = true;
            } else if (colorWOAlpha == bgColor) {
                return colorWOAlpha;
            }
            return rgb;
        }
    }

    public void save() {
        BufferedImage bImg = new BufferedImage(jPanel1.getWidth(), jPanel1.getWidth(), BufferedImage.TYPE_INT_ARGB);
        // BufferedImage bImg = new BufferedImage(jPanel1.getWidth(), jPanel1.getWidth(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D cg = bImg.createGraphics();
//       cg.setComposite(AlphaComposite.Clear);
//        cg.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getWidth());
//        cg.setComposite(AlphaComposite.Src);
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        cg.setRenderingHints(rh);

        jPanel1.paintAll(cg);

        try {
            if (ImageIO.write(bImg, "png", new File("./map.png"))) {
                System.out.println("-- saved");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void capsave() {

    }

    public void saveImg() {
        BufferedImage bImg = new BufferedImage(jPanel1.getWidth(), jPanel1.getWidth(), BufferedImage.TYPE_INT_ARGB);
        // BufferedImage bImg = new BufferedImage(jPanel1.getWidth(), jPanel1.getWidth(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D cg = bImg.createGraphics();
//       cg.setComposite(AlphaComposite.Clear);
//        cg.fillRect(0, 0, jPanel1.getWidth(), jPanel1.getWidth());
//        cg.setComposite(AlphaComposite.Src);
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        cg.setRenderingHints(rh);

        jPanel1.paintAll(cg);

        try {
            if (ImageIO.write(bImg, "png", new File("./img/capturedsig/img.png"))) {
                //System.out.println("-- saved");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class MyCaptureObserver implements CaptureObserver {

        public void onError(CaptureStream sender, CaptureException e) {
            System.err.println("onError " + sender);
            // e.printStackTrace();
        }

        public void onNewImage(CaptureStream sender, com.lti.civil.Image image) {

            final BufferedImage bimg, x;
            try {

                //final VideoFormat format = image.getFormat();
                //System.out.println("onNewImage format=" + videoFormatToString(format) + " length=" + image.getBytes().length);
                bimg = AWTImageConverter.toBufferedImage(image);

                //x = resize(bimg, 300, 400);
            } catch (Exception e) {
                // e.printStackTrace();
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream("img/capturedsig/img.jpg");
                JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(fos);

                jpeg.encode(bimg);

                fos.close();

//                //GET THE PREVIEW FROM THE SOURCE FILE
//                File file = new File("img/previewer/img.jpg");
//                BufferedImage myImage = ImageIO.read(file);
//                preview.setIcon(new ImageIcon(myImage));
//                preview.revalidate();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

    public static BufferedImage readImage(String fileLocation) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileLocation));
            //System.out.println("Image Read. Image Dimension: " + img.getWidth()
            // + "w X " + img.getHeight() + "h");
        } catch (IOException e) {
            // e.printStackTrace();
        }
        return img;
    }

    public static void writeImage(BufferedImage img, String fileLocation,
            String extension) {
        try {
            BufferedImage bi = img;
            File outputfile = new File(fileLocation);
            ImageIO.write(bi, extension, outputfile);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public static BufferedImage cropMyImage(BufferedImage img, int cropWidth,
            int cropHeight, int cropStartX, int cropStartY) throws Exception {
        BufferedImage clipped = null;
        Dimension size = new Dimension(cropWidth, cropHeight);

        createClip(img, size, cropStartX, cropStartY);

        try {
            int w = clip.width;
            int h = clip.height;

            //System.out.println("Crop Width " + w);
            //System.out.println("Crop Height " + h);
            //System.out.println("Crop Location " + "(" + clip.x + "," + clip.y
            // + ")");
            clipped = img.getSubimage(clip.x, clip.y, w, h);

            //System.out.println("Image Cropped. New Image Dimension: "
            //+ clipped.getWidth() + "w X " + clipped.getHeight() + "h");
        } catch (RasterFormatException rfe) {
            //System.out.println("Raster format error: " + rfe.getMessage());
            return null;
        }
        return clipped;
    }

    private static void createClip(BufferedImage img, Dimension size,
            int clipX, int clipY) throws Exception {

        boolean isClipAreaAdjusted = false;

        /**
         * Checking for negative X Co-ordinate*
         */
        if (clipX < 0) {
            clipX = 0;
            isClipAreaAdjusted = true;
        }
        /**
         * Checking for negative Y Co-ordinate*
         */
        if (clipY < 0) {
            clipY = 0;
            isClipAreaAdjusted = true;
        }

        /**
         * Checking if the clip area lies outside the rectangle*
         */
        if ((size.width + clipX) <= img.getWidth()
                && (size.height + clipY) <= img.getHeight()) {

            /**
             * Setting up a clip rectangle when clip area lies within the image.
             */
            clip = new Rectangle(size);
            clip.x = clipX;
            clip.y = clipY;
        } else {

            /**
             * Checking if the width of the clip area lies outside the image. If
             * so, making the image width boundary as the clip width.
             */
            if ((size.width + clipX) > img.getWidth()) {
                size.width = img.getWidth() - clipX;
            }

            /**
             * Checking if the height of the clip area lies outside the image.
             * If so, making the image height boundary as the clip height.
             */
            if ((size.height + clipY) > img.getHeight()) {
                size.height = img.getHeight() - clipY;
            }

            /**
             * Setting up the clip are based on our clip area size adjustment*
             */
            clip = new Rectangle(size);
            clip.x = clipX;
            clip.y = clipY;

            isClipAreaAdjusted = true;

        }
        if (isClipAreaAdjusted) {
            //System.out.println("Crop Area Lied Outside The Image."
            //  + " Adjusted The Clip Rectangle\n");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        d = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        capturer = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Signature Manager");
        setResizable(false);

        d.setToolTipText("");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        capturer.setForeground(new java.awt.Color(255, 102, 0));
        capturer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/captures4.png"))); // NOI18N
        capturer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        capturer.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                capturerMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                capturerMouseMoved(evt);
            }
        });
        capturer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                capturerMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                capturerMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(capturer)
                .addContainerGap(324, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(capturer, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dLayout = new javax.swing.GroupLayout(d);
        d.setLayout(dLayout);
        dLayout.setHorizontalGroup(
            dLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        dLayout.setVerticalGroup(
            dLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        d.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/segment.png"))); // NOI18N
        jButton3.setText("     Show Capturer     ");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cancelitem.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setLabel("    Hide Capturer     ");
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);
        jToolBar1.add(jSeparator2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        jButton1.setText("      Capture & Save       ");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator3);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        jButton2.setMnemonic('C');
        jButton2.setText("      Clear     ");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(d)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1009, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(d)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            saveImg();
            crop();
            Transparent ti = new Transparent();
            ti.croppedNow();
            frmParent.showsig();
        } catch (Exception ex) {
            Logger.getLogger(SignaturePad.class.getName()).log(Level.SEVERE, null, ex);
        }

        FTPFactory i = new FTPFactory();
        i.FTPSaveImage("img/croppedsig/img.png", "img/signature/" + memID + ".png");
        frmParent.showsig();
        this.dispose();
        JOptionPane.showMessageDialog(null, "Profile Picture Successfully Saved!");
    }//GEN-LAST:event_jButton1ActionPerformed

    public static String getSigPathConfig() {
        String Str = memsys.others.paths.getSigPathConfig();
        return Str;
    }

    void crop() {
        BufferedImage originalImage = readImage("img/capturedsig/img.png");
        try {
            BufferedImage processedImage = cropMyImage(originalImage, 460, 280, capturer.getX() + 20, capturer.getY() - 20);
            writeImage(processedImage, "img/croppedsig/img.png", "PNG");
        } catch (Exception ex) {
            Logger.getLogger(SignaturePad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//PadDraw drawPad = new PadDraw();
//drawPad.paintComponent(null);

        jPanel1.removeAll();
        jPanel1.updateUI();
        paint();

//        paint()
    }//GEN-LAST:event_jButton2ActionPerformed

    private void capturerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturerMousePressed

    }//GEN-LAST:event_capturerMousePressed

    private void capturerMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturerMouseReleased
        int x = capturer.getX();
        int y = capturer.getY();

        if (x >= 480 || y >= 320 || x <= -100 || y <= -100) {
            capturer.setLocation(80, 40);
        }
    }//GEN-LAST:event_capturerMouseReleased

    private void capturerMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturerMouseDragged
        capturer.setLocation((capturer.getX() - capturer.getWidth() / 2) + evt.getX(), (capturer.getY() - capturer.getHeight() / 2) + evt.getY());
    }//GEN-LAST:event_capturerMouseDragged

    private void capturerMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturerMouseMoved
        //capturer1.setLocation(preX + evt.getX(), preY + evt.getY());
    }//GEN-LAST:event_capturerMouseMoved

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        capturer.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        capturer.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SignaturePad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SignaturePad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SignaturePad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SignaturePad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SignaturePad dialog = new SignaturePad(frmParent, true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel capturer;
    private javax.swing.JDesktopPane d;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
