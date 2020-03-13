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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;
import memsys.global.myDataenvi;
import static memsys.global.myDataenvi.rsAddConnLog;
import memsys.global.myFunctions;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author engkoicadiz
 */
public class TagRequest extends javax.swing.JDialog {

    public static ForAction frmParent;
    public static int acctno, id, crewid;
    public static String typedesc;
    static Statement stmt;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    static String nowDate2 = FunctionFactory.getSystemNowDateTimeString();
    public static ActedBySearch frmCrew;

    public TagRequest(ForAction parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        setdates();
        crewid = 0;
    }

    public void showFrmSearchCrew() {
        frmCrew = new ActedBySearch(this, true);
        frmCrew.setVisible(true);
    }

    void setdates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date theDate = null;
        try {
            theDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        txtdate.setDate(theDate);
        //  txtend.setDate(theDate);
    }

    public static void rsUpdateRequest(int id, int Stat) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE DiscoRecoTransTBL SET "
                + "status='" + Stat + "' WHERE DiscoRecoID=" + id;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void SetActedBy(int cid, String cnym) {
        txtpb.setText(cnym);
        crewid = cid;
        // txtdatereplaced.requestFocus();
    }

    private void UpdateMeterStatus(int mscode, String msn) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE Meter SET MSCode=" + mscode + " WHERE MeterSN='" + msn + "'";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

   String GetCurrentMeterSN(int acctno) {
        String sn = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT MeterSN FROM ConsumerMeter WHERE AcctNo=" + acctno;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                sn = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return sn;
    }

   String GetPreviousMeterSN(int acctno) {
       String sn = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT CurrentMeterSN FROM discoRecoTransTBL WHERE AcctNo=" + acctno;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                sn = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return sn;
    }

    String GetStatusDesc() {
        String desc = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT DescripType FROM costingUQTypeTBL WHERE CtypeId=" + typedesc;

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

    private void InsertMeterStatusLog(int acctno, String loginfo, int mscode, String metersn) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String seldate = dateFormat.format(txtdate.getDate());

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO MeterStatusLog(MeterSN, AcctNo, MSCode,  Remarks, UserID, TransDate, ChangeDate) "
                + "VALUES('" + metersn + "'," + acctno + ", " + mscode + ",'" + loginfo + "' ," + ParentWindow.getUserID() + ",'" + nowDate2 + "','" + seldate + "' )";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void Tag() {
        myDataenvi.rsUpdateConnStat(acctno, 15);
        myDataenvi.rsUpdateRequest(id, 15);
        rsAddConnLog(acctno, GetStatusDesc()+" request acted", 15, ParentWindow.getUserID(), nowDate2, "");

        if ("8".equals(typedesc)) {
            UpdateMeterStatus(4, GetCurrentMeterSN(acctno));
            InsertMeterStatusLog(acctno, GetStatusDesc() + " Acted by: " + txtpb.getText(), 4, GetCurrentMeterSN(acctno));
        } else if ("7".equals(typedesc)) {
            UpdateMeterStatus(5, GetCurrentMeterSN(acctno));
            InsertMeterStatusLog(acctno, GetStatusDesc() + " Acted by: " + txtpb.getText(), 5, GetCurrentMeterSN(acctno));
        }

        this.dispose();
        frmParent.populateRequestTBL();
        JOptionPane.showMessageDialog(this, "Record has been successfully tag!");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Ok = new javax.swing.JButton();
        Ok1 = new javax.swing.JButton();
        txtdate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtpb = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tag Request");

        Ok.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/earn.png"))); // NOI18N
        Ok.setText("Tag as Acted");
        Ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkActionPerformed(evt);
            }
        });

        Ok1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Ok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        Ok1.setText("Cancel");
        Ok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ok1ActionPerformed(evt);
            }
        });

        txtdate.setDateFormatString("yyyy/MM/dd");
        txtdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel1.setText("Date Acted:");

        jLabel7.setText("Acted by:");

        txtpb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpbActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/msupplier.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Ok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Ok1))
                    .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtpb, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(txtpb, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ok)
                    .addComponent(Ok1))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkActionPerformed
        if (crewid == 0) {
            JOptionPane.showMessageDialog(this, "Please select a maintainance crew!");
        } else {
            // JOptionPane.showMessageDialog(this, GetStatusDesc());
            int x = myFunctions.msgboxYesNo("This request will now be tag as acted!");
            switch (x) {
                case 0:
                    String PMSN = GetPreviousMeterSN(acctno);
                    String CMSN = GetCurrentMeterSN(acctno);

                    //       System.out.println(PMSN);
                    //      System.out.println(CMSN);
// System.out.println(typedesc);
                    if (PMSN.equals(CMSN)) {
                        Tag();
                    } else {
                        Tag();
                    }

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

    private void Ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ok1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_Ok1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        showFrmSearchCrew();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtpbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpbActionPerformed

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
            java.util.logging.Logger.getLogger(TagRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TagRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TagRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TagRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TagRequest dialog = new TagRequest(frmParent, true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private com.toedter.calendar.JDateChooser txtdate;
    private javax.swing.JTextField txtpb;
    // End of variables declaration//GEN-END:variables
}
