/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.meter;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;

/**
 *
 * @author cadizlester
 */
public class RemoveMeter extends javax.swing.JDialog {

    public static ChangeRemoveMeter frmParent;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    public static int acctno;
    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static SearchCrewOnRemove frmSearchCrewOnRemove;
    static int crewid;

    public RemoveMeter(ChangeRemoveMeter parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        populateTBL();

        tbllog.setCellSelectionEnabled(false);
        tbllog.setRowSelectionAllowed(true);
        tbllog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbllog.setSelectionBackground(new Color(153, 204, 255));
        tbllog.setSelectionForeground(Color.BLACK);

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

        populateTBLInfo();
        populateTBLDetails();
        setdates();
    }

    public void showFrmSearchCrew() {
        frmSearchCrewOnRemove = new SearchCrewOnRemove(this, true);
        frmSearchCrewOnRemove.setVisible(true);
    }

    public void LoadCrew(int cid, String cnym) {
        txtpb.setText(cnym);
        crewid = cid;
        txtremarks.requestFocus();
    }

    void setdates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date theDate = null;
        try {
            theDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        txtdateremoved.setDate(theDate);
        //  txtend.setDate(theDate);
    }

    void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        createString = "SELECT * FROM MeterStatusLog m INNER JOIN MeterStatus s ON m.MSCODE=s.MSCODE WHERE AcctNo=" + acctno + " ORDER BY TransDate DESC";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);
            model = (DefaultTableModel) tbllog.getModel();
            renderer.setHorizontalAlignment(0);
            tbllog.setRowHeight(29);
            model.setNumRows(0);

            int cnt = 0;
            while (rs.next()) {
                model.addRow(new Object[]{rs.getDate("TransDate"), rs.getDate("ChangeDate"), rs.getString("MeterSN"), rs.getString("MSDesc"), rs.getString("Remarks")});
                cnt++;
            }
            if (cnt != 0) {
                tbllog.setRowSelectionInterval(0, 0);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void populateTBLDetails() {
        Connection conn = MainDBConn.getConnection();
        String createString = "SELECT * FROM ConsumerMeter c INNER JOIN Meter m ON c.MeterSN=m.MeterSN WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                // Date lrd =rs.getDate("PresRdng");
                lblmtrsn.setText(rs.getString("MeterSN"));
                lbllrd.setText(rs.getDate("RdngDate").toString());
                lbllr.setText(rs.getString("PresRdng"));
                lblld.setText(rs.getString("PresDemand"));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void populateTBLInfo() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        createString = "SELECT * FROM Consumer WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);
            model2 = (DefaultTableModel) tbl.getModel();
            renderer.setHorizontalAlignment(0);
            tbl.setRowHeight(29);
            model2.setNumRows(0);

            while (rs.next()) {
                model2.addRow(new Object[]{"Account Number", rs.getString("AcctNo")});
                model2.addRow(new Object[]{"Area Code", rs.getString("TownCode") + "-" + rs.getString("RouteCode") + "-" + rs.getString("RouteSeqNo")});
                model2.addRow(new Object[]{"Account Name", rs.getString("AcctName")});
                model2.addRow(new Object[]{"Address", rs.getString("AcctAddress")});
                model2.addRow(new Object[]{"Class", rs.getString("ClassCode")});
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbllog = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblmtrsn = new javax.swing.JLabel();
        lbllr = new javax.swing.JLabel();
        lblld = new javax.swing.JLabel();
        lbllrd = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtdateremoved = new com.toedter.calendar.JDateChooser();
        txtpr = new javax.swing.JFormattedTextField();
        txtremarks = new javax.swing.JTextField();
        txtpb = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        cmdpost = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Remove Meter");

        tbllog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TransDate", "ChangeDate", "MeterSN", "Status", "LogDetails"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbllog);
        tbllog.getColumnModel().getColumn(0).setMaxWidth(100);
        tbllog.getColumnModel().getColumn(4).setPreferredWidth(300);

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Information"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbl);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(200);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("MeterSN:");

        jLabel2.setText("Last Reading Date:");

        jLabel3.setText("Last Reading:");

        jLabel4.setText("Last Demand:");

        lblmtrsn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblmtrsn.setForeground(new java.awt.Color(0, 102, 0));
        lblmtrsn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lbllr.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbllr.setForeground(new java.awt.Color(0, 102, 0));
        lbllr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblld.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblld.setForeground(new java.awt.Color(0, 102, 0));
        lblld.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lbllrd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbllrd.setForeground(new java.awt.Color(0, 102, 0));
        lbllrd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel5.setText("Removal Date:");

        jLabel6.setText("Pull-out Reading:");

        jLabel7.setText("Pull-out by:");

        jLabel8.setText("Remarks:");

        txtdateremoved.setDateFormatString("yyyy-MM-dd");

        txtpr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0"))));
        txtpr.setText("0.0");
        txtpr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtprFocusGained(evt);
            }
        });
        txtpr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtprMouseClicked(evt);
            }
        });

        txtremarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtremarksActionPerformed(evt);
            }
        });

        txtpb.setEnabled(false);
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

        cmdpost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdpost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        cmdpost.setMnemonic('e');
        cmdpost.setText("Remove Meter");
        cmdpost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdpostActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblld, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbllr, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbllrd, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblmtrsn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtpb, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtremarks)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtpr, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtdateremoved, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdpost, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblmtrsn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtdateremoved, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(lbllrd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtpr, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7)
                    .addComponent(txtpb, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbllr, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblld, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtremarks, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdpost)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtprFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtprFocusGained
        txtpr.selectAll();
    }//GEN-LAST:event_txtprFocusGained

    private void txtprMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtprMouseClicked
        txtpr.selectAll();
    }//GEN-LAST:event_txtprMouseClicked

    private void txtremarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtremarksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtremarksActionPerformed

    private void txtpbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpbActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        showFrmSearchCrew();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmdpostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdpostActionPerformed
        if (txtremarks.getText().isEmpty() == true) {
            JOptionPane.showMessageDialog(this, "Remarks is required!");
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(txtdateremoved.getDate());
            double presentr = Double.valueOf(txtpr.getText());
            double lastd = Double.valueOf(lblld.getText());
            double presentd = 0;

            MeterProcess mp = new MeterProcess();
            mp.RemoveMeter(acctno, strDate, txtremarks.getText(), lblmtrsn.getText(), presentr, presentd, crewid, WIDTH, nowDate);
            //RemoveMeter(int acctno, String installdate, String Remarks, String oldmetersn, double pr, double pd, int crewid, int mpID, String newmetersn)
            this.dispose();
            JOptionPane.showMessageDialog(this, "Meter Removal Successfully Done!");
        }
    }//GEN-LAST:event_cmdpostActionPerformed

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
            java.util.logging.Logger.getLogger(RemoveMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RemoveMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RemoveMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RemoveMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RemoveMeter dialog = new RemoveMeter(frmParent, true);
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
    private javax.swing.JButton cmdpost;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblld;
    private javax.swing.JLabel lbllr;
    private javax.swing.JLabel lbllrd;
    private javax.swing.JLabel lblmtrsn;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tbllog;
    private com.toedter.calendar.JDateChooser txtdateremoved;
    private javax.swing.JTextField txtpb;
    private javax.swing.JFormattedTextField txtpr;
    private javax.swing.JTextField txtremarks;
    // End of variables declaration//GEN-END:variables
}
