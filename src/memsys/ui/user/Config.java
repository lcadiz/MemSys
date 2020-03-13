/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.user;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import memsys.global.DBConn.AGMADBConn;

/**
 *
 * @author cadizlester
 */
public class Config extends javax.swing.JDialog {

    public static Login frmParent;
    private static String db, db2, ftp, dbtemp, ftpport;

    public Config(Login parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        getRootPane().setDefaultButton(cmdsave);
        setLocationRelativeTo(this);
        getConfigParameters();
    }

    void getConfigParameters() {
        String filePath = System.getProperty("user.dir") + "\\config.properties";

        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(filePath));
        } catch (IOException ex) {
            Logger.getLogger(AGMADBConn.class.getName()).log(Level.SEVERE, null, ex);
        }

        String port = properties.getProperty("port");
        String host = properties.getProperty("host");
        String host2 = properties.getProperty("host2");
        db = properties.getProperty("database");
        db2 = properties.getProperty("database2");
        dbtemp = properties.getProperty("databasetemp");
        ftp = properties.getProperty("ftphost");
        ftpport = properties.getProperty("ftpport");

        txthost.setText(host);
        txthost2.setText(host2);
        txtport.setText(port);

    }

    void setConfigParameters() {

        Properties prop = new Properties();
        OutputStream output = null;
        try {

            output = new FileOutputStream(System.getProperty("user.dir") + "\\config.properties");

            // set the properties value
            prop.setProperty("port", txtport.getText());
            prop.setProperty("host", txthost.getText());
            prop.setProperty("host2", txthost2.getText());
            prop.setProperty("database", db);
            prop.setProperty("database2", db2);
            prop.setProperty("databasetemp", dbtemp);
            prop.setProperty("ftphost", ftp);
            prop.setProperty("ftpport", ftpport);
//            prop.setProperty("picpathconfig", "\\\\192.168.1.199\\\img\\pic\\");
//            prop.setProperty("sigpathconfig", "\\\\192.168.1.199\\\img\\signature\\");
            
//            database=NORECO1DBV2
//
//database2=AGMADB
//
//databasetemp=migration
//picpathconfig=\\\\192.168.1.199\\\img\\pic\\
//
//sigpathconfig=\\\\192.168.1.199\\\img\\signature\\


            

            // save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        txthost = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txthost2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtport = new javax.swing.JTextField();
        cmdsave = new javax.swing.JButton();
        cmdlogin1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuration");

        jLabel8.setText("Server IP (Main):");

        txthost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txthost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthostActionPerformed(evt);
            }
        });

        jLabel9.setText("Server2 IP (AGMA):");

        txthost2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txthost2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthost2ActionPerformed(evt);
            }
        });

        jLabel10.setText("Port:");

        txtport.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtportActionPerformed(evt);
            }
        });

        cmdsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        cmdsave.setText("Save");
        cmdsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdsaveActionPerformed(evt);
            }
        });

        cmdlogin1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdlogin1.setText("Cancel");
        cmdlogin1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdlogin1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdsave, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdlogin1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtport, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txthost2)
                    .addComponent(txthost))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(txthost, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(txthost2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel10)
                    .addComponent(txtport, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdsave)
                    .addComponent(cmdlogin1))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txthostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthostActionPerformed

    private void txthost2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthost2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthost2ActionPerformed

    private void txtportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtportActionPerformed

    private void cmdsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdsaveActionPerformed
        setConfigParameters();

        this.dispose();
        JOptionPane.showMessageDialog(this, "Configuration Successfully Saved!");
    }//GEN-LAST:event_cmdsaveActionPerformed

    private void cmdlogin1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdlogin1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdlogin1ActionPerformed

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
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Config dialog = new Config(frmParent, true);
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
    private javax.swing.JButton cmdlogin1;
    private javax.swing.JButton cmdsave;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txthost;
    private javax.swing.JTextField txthost2;
    private javax.swing.JTextField txtport;
    // End of variables declaration//GEN-END:variables
}
