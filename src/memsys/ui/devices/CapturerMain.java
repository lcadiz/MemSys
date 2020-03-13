package memsys.ui.devices;

import com.lti.civil.CaptureDeviceInfo;
import com.lti.civil.CaptureException;
import com.lti.civil.CaptureObserver;
import com.lti.civil.CaptureStream;
import com.lti.civil.CaptureSystem;
import com.lti.civil.CaptureSystemFactory;
import com.lti.civil.DefaultCaptureSystemFactorySingleton;
import com.lti.civil.Image;
import com.lti.civil.VideoFormat;
import com.lti.civil.awt.AWTImageConverter;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import memsys.global.FTPFactory;
//import javax.swing.DefaultListModel;

public class CapturerMain extends javax.swing.JDialog {

    CaptureStream captureStream = null;
    int preX, preY;
    static Rectangle clip;
    Timer test;
    ActionListener taskPerformer;
    int xs = 0;
    public static CurrentPhoto1 frmParent;
    public static String memID;

    public CapturerMain(CurrentPhoto1 parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void StopCam() {
        try {
            captureStream.stop();
            captureStream.dispose();
        } catch (CaptureException ex) {
            Logger.getLogger(CapturerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gosleep() {
        try {
            Thread.sleep(1000);

        } catch (InterruptedException ex) {
            Logger.getLogger(CapturerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void TestAndStartCam() throws CaptureException {
//        CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton.instance();
//        CaptureSystem system;
//        try {
//            system = factory.createCaptureSystem();
//            system.init();
//            List list = system.getCaptureDeviceInfoList();
//            int i = 1;
//            if (i < list.size()) {
//                CaptureDeviceInfo info = (CaptureDeviceInfo) list.get(i);
//                //System.out.println((new StringBuilder()).append("Device ID ").append(i).append(": ").append(info.getDeviceID()).toString());
//                //System.out.println((new StringBuilder()).append("Description ").append(i).append(": ").append(info.getDescription()).toString());
//                captureStream = system.openCaptureDeviceStream(info.getDeviceID());
//                captureStream.setObserver(new MyCaptureObserver());
//            }
//        } catch (CaptureException ex) {
//            //ex.printStackTrace();
//        }
        CaptureSystemFactory factory = DefaultCaptureSystemFactorySingleton.instance();
        CaptureSystem system;
//        try {
            system = factory.createCaptureSystem();
            system.init();
            List list = system.getCaptureDeviceInfoList();
            int i = 0;
            if (i < list.size()) {
                CaptureDeviceInfo info = (CaptureDeviceInfo) list.get(i);
                System.out.println((new StringBuilder()).append("Device ID ").append(i).append(": ").append(info.getDeviceID()).toString());
                System.out.println((new StringBuilder()).append("Description ").append(i).append(": ").append(info.getDescription()).toString());
                captureStream = system.openCaptureDeviceStream(info.getDeviceID());
                captureStream.setObserver(new MyCaptureObserver());
            }
//        } catch (CaptureException ex) {
//            ex.printStackTrace();
//        }
    }

    public static String videoFormatToString(VideoFormat f) {
        return "Type=" + formatTypeToString(f.getFormatType()) + " Width=" + f.getWidth() + " Height=" + f.getHeight() + " FPS=" + f.getFPS();
    }

    private static String formatTypeToString(int f) {
        switch (f) {
            case VideoFormat.RGB24:
                return "RGB24";
            case VideoFormat.RGB32:
                return "RGB32";
            default:
                return "" + f + " (unknown)";
        }
    }

    class MyCaptureObserver implements CaptureObserver {

        public void onError(CaptureStream sender, CaptureException e) {
            System.err.println("onError " + sender);
            // e.printStackTrace();
        }

        public void onNewImage(CaptureStream sender, Image image) {

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
                FileOutputStream fos = new FileOutputStream("img/previewer/img.jpg");
                JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(fos);

                jpeg.encode(bimg);

                fos.close();

                //GET THE PREVIEW FROM THE SOURCE FILE
                File file = new File("img/previewer/img.jpg");
                BufferedImage myImage = ImageIO.read(file);
                preview.setIcon(new ImageIcon(myImage));
                preview.revalidate();

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

        cmdCapture = new javax.swing.JButton();
        desk = new javax.swing.JDesktopPane();
        jLabel2 = new javax.swing.JLabel();
        LBLCAPTION = new javax.swing.JLabel();
        capturer1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        preview = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        captured = new javax.swing.JLabel();
        cmdSave = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Picture Capturer");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        cmdCapture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/capturer.png"))); // NOI18N
        cmdCapture.setMnemonic('C');
        cmdCapture.setText("CAPTURE");
        cmdCapture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cmdCaptureMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cmdCaptureMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cmdCaptureMouseReleased(evt);
            }
        });
        cmdCapture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCaptureActionPerformed(evt);
            }
        });

        desk.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 3));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 8)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CAPTURE WINDOW");
        desk.add(jLabel2);
        jLabel2.setBounds(20, 20, 190, 10);

        LBLCAPTION.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        LBLCAPTION.setForeground(new java.awt.Color(255, 255, 255));
        LBLCAPTION.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        desk.add(LBLCAPTION);
        LBLCAPTION.setBounds(110, 210, 390, 50);

        capturer1.setForeground(new java.awt.Color(255, 102, 0));
        capturer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/captures2.png"))); // NOI18N
        capturer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        capturer1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                capturer1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                capturer1MouseMoved(evt);
            }
        });
        capturer1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                capturer1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                capturer1MouseReleased(evt);
            }
        });
        desk.add(capturer1);
        capturer1.setBounds(210, 110, 200, 200);
        desk.add(jLabel4);
        jLabel4.setBounds(20, 370, 120, 100);

        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        desk.add(jLabel3);
        jLabel3.setBounds(10, 10, 620, 460);
        desk.add(preview);
        preview.setBounds(0, 0, 640, 480);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("CAPTURED PREVIEW");

        captured.setForeground(new java.awt.Color(255, 102, 0));
        captured.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 0)));

        cmdSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        cmdSave.setMnemonic('S');
        cmdSave.setText("Save");
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });

        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdExit.setMnemonic('x');
        cmdExit.setText("Exit");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(desk, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(cmdSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdCapture, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(captured, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(cmdExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(desk, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(captured, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(cmdCapture)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdExit)
                        .addGap(40, 40, 40))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdCaptureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdCaptureMouseClicked
   }//GEN-LAST:event_cmdCaptureMouseClicked

    private void cmdCaptureMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdCaptureMousePressed
        captured.setIcon(null);
        LBLCAPTION.setText("PROCESSING...");

    }//GEN-LAST:event_cmdCaptureMousePressed

    private void cmdCaptureMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cmdCaptureMouseReleased
        try {
            captureStream.stop();
        } catch (CaptureException ex) {
        }

        try {
            BufferedImage img = readImage("img/previewer/img.jpg");
            writeImage(img, "img/captured/img.jpg", "jpg");

            BufferedImage originalImage = readImage("img/captured/img.jpg");
            try {
                BufferedImage processedImage = cropMyImage(originalImage, 200, 200, capturer1.getX(), capturer1.getY());
                writeImage(processedImage, "img/cropped/img.jpg", "jpg");
            } catch (Exception ex) {
                Logger.getLogger(CapturerMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            //GET THE PREVIEW FROM THE SOURCE FILE
            File file = new File("img/cropped/img.jpg");
            BufferedImage myImage = ImageIO.read(file);
            captured.setIcon(new ImageIcon(myImage));
            captured.revalidate();

            LBLCAPTION.setText(null);

        } catch (Exception e) {
            LBLCAPTION.setText(null);
            e.getStackTrace();
        }

        try {
            captureStream.start();
        } catch (CaptureException ex) {
            Logger.getLogger(CapturerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cmdCaptureMouseReleased

    private void cmdCaptureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCaptureActionPerformed

   }//GEN-LAST:event_cmdCaptureActionPerformed

    private void capturer1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturer1MousePressed
   }//GEN-LAST:event_capturer1MousePressed

    private void capturer1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturer1MouseReleased
        int x = capturer1.getX();
        int y = capturer1.getY();

        if (x >= 480 || y >= 320 || x <= -100 || y <= -100) {
            capturer1.setLocation(80, 40);
        }
    }//GEN-LAST:event_capturer1MouseReleased

    private void capturer1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturer1MouseDragged
        capturer1.setLocation((capturer1.getX() - capturer1.getWidth() / 2) + evt.getX(), (capturer1.getY() - capturer1.getHeight() / 2) + evt.getY());
    }//GEN-LAST:event_capturer1MouseDragged

    private void capturer1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturer1MouseMoved
        //capturer1.setLocation(preX + evt.getX(), preY + evt.getY());
    }//GEN-LAST:event_capturer1MouseMoved

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
//        try {
//            String path = getPicPath();
//            BufferedImage img = readImage("img/cropped/img.jpg");
//            writeImage(img, path + memID + ".jpg", "jpg");

        FTPFactory i = new FTPFactory();
        i.FTPSaveImage("img/cropped/img.jpg", "img/pic/" + memID + ".jpg");
     

        try {
            StopCam();
        } catch (NullPointerException e) {
            e.getStackTrace();
        }

        this.dispose();
        JOptionPane.showMessageDialog(null, "Profile Picture Successfully Saved!");
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Failed! Please check your network settings");
//        }
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            TestAndStartCam();
        } catch (CaptureException ex) {
            Logger.getLogger(CapturerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
        try {
            VideoFormat format = captureStream.enumVideoFormats().get(0); //SET TO 352 BY 288 DIMENSION
            captureStream.setVideoFormat(format);
            captureStream.start();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Camera Device not detected!");
            //ex.getMessage();
            this.dispose();
        }
//         Dimension[] nonStandardResolutions = new Dimension[] {
//			WebcamResolution.PAL.getSize(),
//			WebcamResolution.HD720.getSize(),
//			new Dimension(2000, 1000),
//			new Dimension(1000, 500),
//		};
//            
//            
//		Webcam webcam = Webcam.getDefault();
//                webcam.setCustomViewSizes(nonStandardResolutions);
//		webcam.setViewSize(WebcamResolution.PAL.getSize());
//		//webcam.setViewSize(WebcamResolution.VGA.getSize());
//
//		WebcamPanel panel = new WebcamPanel(webcam);
//		panel.setFPSDisplayed(true);
//		panel.setDisplayDebugInfo(true);
//		panel.setImageSizeDisplayed(true);
//		panel.setMirrored(true);
//                
//                desk.add(panel);
//               // this.pack();
    }//GEN-LAST:event_formWindowOpened

    public static String getPicPath() {
        String Str = memsys.others.paths.getPicPathConfig();
        return Str;
    }

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed

        try {
            StopCam();
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            StopCam();
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CapturerMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CapturerMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CapturerMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CapturerMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                CapturerMain dialog = new CapturerMain(frmParent, true);
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
    private javax.swing.JLabel LBLCAPTION;
    private javax.swing.JLabel captured;
    private javax.swing.JLabel capturer1;
    private javax.swing.JButton cmdCapture;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdSave;
    private javax.swing.JDesktopPane desk;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel preview;
    // End of variables declaration//GEN-END:variables
}
