package memsys.global;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cadizlester
 */
public class SysUp extends javax.swing.JFrame {

    DefaultListModel model = new DefaultListModel();

    public SysUp() {
        initComponents();
        setLocationRelativeTo(this);
        try {
            getConfigParameters();
        } catch (Exception e) {

        }
        lst.setModel(model);
    }

    public void insertlog(String txt){
            model.addElement("Downloading "+ txt);
    }
    
    void closeUpdater() {
        try {
            String path = "java -jar MemSys.jar";

            Runtime rn = Runtime.getRuntime();
            rn.exec(path);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(SysUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void getConfigParameters() {

        try {
            String filePath = System.getProperty("user.dir") + "\\version.properties";
            Properties properties = new Properties();
            properties.load(new FileInputStream(filePath));
            String version = properties.getProperty("version");
            String build = properties.getProperty("build");

            

            model.addElement("Current Membership System Version " + version);
            model.addElement("Current Build " + build);
            model.addElement("---------------------------------------------------");

            FTPFactory i = new FTPFactory();
            String VersionURL = i.GetFTPVersionPath();
            URL url = new URL(VersionURL);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Properties prop = new Properties();
            prop.load(br);
            String nversion = prop.getProperty("version");
            String nbuild = prop.getProperty("build");
            model.addElement("<html><div style=color:#FFFFFF>New Membership System Version " + nversion + "</div></html>");
            model.addElement("<html><div style=color:#FFFFFF>New Build " + nbuild + "</div></html>");
            model.addElement("<html><div style=color:#FFFFFF>---------------------------------------------------</div></html>");

            if (Integer.valueOf(nbuild) > Integer.valueOf(build)) {
                model.addElement("<html><div style=color:#ff4000>NEED TO UPDATE THE PROGRAM!!!</div></html>");
                model.addElement("<html><div style=color:#ff4000>Click UPDATE Button to update....</div></html>");
            } else if (Integer.valueOf(nbuild) <= Integer.valueOf(build)) {
                model.addElement("<html><div style=color:#ffff00>PROGRAM IS UP TO DATE!!!</div></html>");
            }

        } catch (IOException ex) {
            Logger.getLogger(SysUp.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmdupdate = new javax.swing.JButton();
        cmdexit = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lst = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("System Update");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        cmdupdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/downloaded.png"))); // NOI18N
        cmdupdate.setText("Update");
        cmdupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdupdateActionPerformed(evt);
            }
        });

        cmdexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdexit.setMnemonic('x');
        cmdexit.setText("Cancel");
        cmdexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexitActionPerformed(evt);
            }
        });

        lst.setBackground(new java.awt.Color(0, 0, 0));
        lst.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lst.setForeground(new java.awt.Color(0, 255, 0));
        jScrollPane2.setViewportView(lst);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdupdate, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdexit, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdexit)
                    .addComponent(cmdupdate))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdupdateActionPerformed
        try {
            FTPFactory i = new FTPFactory();
            model.addElement("---------------------------------------------------");
            i.FTPUpdateLib();
            i.FTPUpdate();
            JOptionPane.showMessageDialog(this, "Updated Successfully.");
            closeUpdater();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_cmdupdateActionPerformed

    private void cmdexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexitActionPerformed
        closeUpdater();
    }//GEN-LAST:event_cmdexitActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeUpdater();

    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(SysUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SysUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SysUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SysUp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SysUp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdexit;
    private javax.swing.JButton cmdupdate;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> lst;
    // End of variables declaration//GEN-END:variables
}
