package memsys.ui.user;

import java.io.FileInputStream;
import java.io.IOException;
import memsys.ui.main.ParentWindow;
import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public final class Login extends javax.swing.JFrame {

    static Statement stmt;
    public static Config frmconfig;

    public Login() {
        initComponents();
        setLocationRelativeTo(this);
        getRootPane().setDefaultButton(cmdlogin);
        getConfigParameters();
    }

    void getConfigParameters() {
        try {
            String filePath = System.getProperty("user.dir") + "\\version.properties";
            Properties properties = new Properties();
            properties.load(new FileInputStream(filePath));
            String version = properties.getProperty("version");
            String build = properties.getProperty("build");

            lblversion.setText("Membership System Version "+version);
            lblbuild.setText("Build "+build);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public void showFrmConfig() {
        frmconfig = new Config(this, true);
        frmconfig.setVisible(true);
    }

    boolean IsValidUser(String uname, String pword) {
        boolean found = false;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM Users WHERE UserName='" + uname + "' AND Password='" + pword + "'";

        int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                rc++;
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        if (rc > 0) {
            found = true;
        }

        return found;
    }

    int GetUserGID(String uname, String pword) {
        int UGID = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT UGID FROM Users WHERE UserName='" + uname + "' AND Password='" + pword + "'";

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                UGID = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return UGID;
    }

    int GetUserID(String uname, String pword) {
        int UserID = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT UserID FROM Users WHERE UserName='" + uname + "' AND Password='" + pword + "'";

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                UserID = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return UserID;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cmdexit = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtuname = new javax.swing.JTextField();
        cmdlogin = new javax.swing.JButton();
        txtpword = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        lblversion = new javax.swing.JLabel();
        lblbuild = new javax.swing.JLabel();
        cmdexit1 = new javax.swing.JButton();
        cmdupdate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("User Login");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/arc3.png"))); // NOI18N

        cmdexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logoff2.png"))); // NOI18N
        cmdexit.setMnemonic('x');
        cmdexit.setText("Exit");
        cmdexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexitActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Password:");

        txtuname.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtuname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtunameFocusGained(evt);
            }
        });
        txtuname.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtunameMouseClicked(evt);
            }
        });

        cmdlogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/aa.png"))); // NOI18N
        cmdlogin.setText("Log-in");
        cmdlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdloginActionPerformed(evt);
            }
        });

        txtpword.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtpword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtpwordFocusGained(evt);
            }
        });
        txtpword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtpwordMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Username:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtpword)
                    .addComponent(txtuname)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmdlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdexit, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtuname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtpword, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdexit)
                    .addComponent(cmdlogin))
                .addContainerGap())
        );

        lblversion.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        lblversion.setForeground(new java.awt.Color(0, 102, 204));
        lblversion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblversion.setText("Membership System Version 11.2");

        lblbuild.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        lblbuild.setForeground(new java.awt.Color(51, 51, 51));
        lblbuild.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblbuild.setText("Build 20180312");

        cmdexit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/set.png"))); // NOI18N
        cmdexit1.setMnemonic('x');
        cmdexit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexit1ActionPerformed(evt);
            }
        });

        cmdupdate.setForeground(new java.awt.Color(102, 102, 102));
        cmdupdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/downloaded.png"))); // NOI18N
        cmdupdate.setMnemonic('x');
        cmdupdate.setText("Update");
        cmdupdate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdupdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmdexit1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdupdate, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblversion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblbuild, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblversion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblbuild)
                        .addGap(8, 8, 8))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmdexit1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdupdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdloginActionPerformed

        String uname = txtuname.getText();
        String pword = txtpword.getText();

        String md5pword = myFunctions.convertMD5(pword);

        boolean val = IsValidUser(uname, md5pword); //VALIDATE USER IF VALID

        if (val == false) {
            JOptionPane.showMessageDialog(this, "Access Denied! Unauthorized User.");
            txtuname.requestFocus();

        } else if (val == true) {

            //GET USER GROUP ID
            int gid = GetUserGID(uname, md5pword);

            //PASS VARIABLE TO MDI CLASS
            ParentWindow.UGID = gid;

            //GET USER ID
            int uid = GetUserID(uname, md5pword);

            //PASS VARIABLE TO MDI CLASS
            //MDI.UserID = uid;
            ParentWindow.userid = uid;

            this.dispose();
            ParentWindow frame = new ParentWindow();
            frame.setVisible(true);
        }
    }//GEN-LAST:event_cmdloginActionPerformed

    private void cmdexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexitActionPerformed
System.exit(0);
        
    }//GEN-LAST:event_cmdexitActionPerformed

    private void txtunameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtunameMouseClicked
        txtuname.selectAll();
    }//GEN-LAST:event_txtunameMouseClicked

    private void txtunameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtunameFocusGained
        txtuname.selectAll();
    }//GEN-LAST:event_txtunameFocusGained

    private void txtpwordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtpwordMouseClicked
        txtpword.selectAll();
    }//GEN-LAST:event_txtpwordMouseClicked

    private void txtpwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtpwordFocusGained
        txtpword.selectAll();
    }//GEN-LAST:event_txtpwordFocusGained

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        txtuname.requestFocus();
    }//GEN-LAST:event_formWindowOpened

    private void cmdexit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexit1ActionPerformed
        showFrmConfig();
    }//GEN-LAST:event_cmdexit1ActionPerformed

    private void cmdupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdupdateActionPerformed
        try {
            String path="java -jar SystemUpdater.jar";
            
            Runtime rn=Runtime.getRuntime();
            rn.exec(path);
            System.exit(0);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }



    }//GEN-LAST:event_cmdupdateActionPerformed

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Login().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdexit;
    private javax.swing.JButton cmdexit1;
    private javax.swing.JButton cmdlogin;
    private javax.swing.JButton cmdupdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblbuild;
    private javax.swing.JLabel lblversion;
    private javax.swing.JPasswordField txtpword;
    private javax.swing.JTextField txtuname;
    // End of variables declaration//GEN-END:variables
}
