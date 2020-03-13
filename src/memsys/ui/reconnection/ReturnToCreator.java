/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.reconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author Engkoi Zidac
 */
public class ReturnToCreator extends javax.swing.JDialog {

    public static int acctno, id;
    static String nowDate2 = FunctionFactory.getSystemNowDateTimeString();
    static Statement stmt;
    public static AuthorizeApproval frmParent;
    public static int typeid;

    public ReturnToCreator(AuthorizeApproval parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
    }

    String GetStatusDesc() {
        String desc = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT DescripType FROM costingUQTypeTBL WHERE CtypeId=" + typeid;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                desc = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return desc;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Ok1 = new javax.swing.JButton();
        Ok = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtremarks = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Remarks");

        Ok1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Ok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        Ok1.setText("Cancel");
        Ok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ok1ActionPerformed(evt);
            }
        });

        Ok.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employer.png"))); // NOI18N
        Ok.setText("Send Back");
        Ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkActionPerformed(evt);
            }
        });

        txtremarks.setColumns(20);
        txtremarks.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        txtremarks.setRows(5);
        jScrollPane1.setViewportView(txtremarks);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 367, Short.MAX_VALUE)
                        .addComponent(Ok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Ok1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ok)
                    .addComponent(Ok1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ok1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_Ok1ActionPerformed

    public static void rsAddConnLog(int acctno, String remarks, int statid, int uid, String nowDate, String note) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO connLogTBL (AcctNo, Remarks, StatusID, UserID, TransDate, Note) VALUES (" + acctno + ",'" + remarks + "'," + statid + "," + uid + ",'" + nowDate + "','" + note +  "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void OkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkActionPerformed
        if (txtremarks.getText().isEmpty() == true) {
            JOptionPane.showMessageDialog(this, "Remarks is required!");
        } else {
            int x = myFunctions.msgboxYesNo("This request will now be send back to CWDO!");
            switch (x) {
                case 0:
                    myDataenvi.rsUpdateConnStat(acctno, 12);
                    myDataenvi.rsUpdateRequest(id, 12);
                    rsAddConnLog(acctno, GetStatusDesc()+" request send back to CWDO", 12, ParentWindow.getUserID(), nowDate2, txtremarks.getText());
                    int uid = ParentWindow.getUserID();
                    frmParent.populateRequestTBL();
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Record has been successfully sent back!");
                    break;
                case 1:
                    break;
                case 2:
                    this.dispose(); //exit window
                    break;
                default:
            }
        }
    }//GEN-LAST:event_OkActionPerformed

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
            java.util.logging.Logger.getLogger(ReturnToCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReturnToCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReturnToCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReturnToCreator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ReturnToCreator dialog = new ReturnToCreator(frmParent, true);
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
    private javax.swing.JButton Ok;
    private javax.swing.JButton Ok1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtremarks;
    // End of variables declaration//GEN-END:variables
}
