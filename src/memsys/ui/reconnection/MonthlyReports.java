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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;

/**
 *
 * @author JoKoiZidac
 */
public class MonthlyReports extends javax.swing.JInternalFrame {

    static Statement stmt;
        static String nowDate = FunctionFactory.GetSystemNowDateString();

    public MonthlyReports() {
        initComponents();
        PopulateCMBType();
        PopulateCMBStatus();
        SetORDate();
    }

    
        void SetORDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date theDate = null;
        try {
            theDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        d1.setDate(theDate);
         d2.setDate(theDate);
        //  txtend.setDate(theDate);
    }
    
    public void PopulateCMBType() {
        cmbtype.addItem("--SELECT--");
        Connection conn = MainDBConn.getConnection();
        String createString = "SELECT CTypeID, DescripType FROM costingUQTypeTBL WHERE MemberProcessTypeID=1";

        // createString = "SELECT CTypeID, DescripType FROM costingUQTypeTBL WHERE MemberProcessTypeID=1;";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbtype.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    class Item {

        private int id;
        private String description;

        public Item(int id, String description) {
            this.id = id;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String toString() {
            return description;
        }
    }
    
    public void PopulateCMBStatus() {
        cmbstatus.addItem("--SELECT--");
        Connection conn = MainDBConn.getConnection();
        String createString = "SELECT status, statdesc FROM connStatTBL WHERE status>=12 AND status<=17 AND status<>16";

        // createString = "SELECT CTypeID, DescripType FROM costingUQTypeTBL WHERE MemberProcessTypeID=1;";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbstatus.addItem(new Item2(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    class Item2 {

        private int id;
        private String description;

        public Item2(int id, String description) {
            this.id = id;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String toString() {
            return description;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        cmbtype = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        cmbstatus = new javax.swing.JComboBox();
        o1 = new javax.swing.JRadioButton();
        o2 = new javax.swing.JRadioButton();
        o3 = new javax.swing.JRadioButton();
        o4 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        d1 = new com.toedter.calendar.JDateChooser();
        Ok = new javax.swing.JButton();
        Ok1 = new javax.swing.JButton();
        d2 = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Monthly Reports");

        jLabel1.setText("Type of Transactions:");

        cmbtype.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbtype.setMaximumRowCount(15);
        cmbtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtypeActionPerformed(evt);
            }
        });

        jLabel2.setText("Status:");

        cmbstatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbstatus.setMaximumRowCount(15);
        cmbstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbstatusActionPerformed(evt);
            }
        });

        buttonGroup1.add(o1);
        o1.setSelected(true);
        o1.setText("All");

        buttonGroup1.add(o2);
        o2.setText("Filter Per/Area");

        buttonGroup1.add(o3);
        o3.setText("Filter Main Only");

        buttonGroup1.add(o4);
        o4.setText("Filter Area Only");

        jLabel3.setText("Inclusive Dates:");

        d1.setDateFormatString("yyyy-MM-dd");
        d1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        Ok.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ledger2.png"))); // NOI18N
        Ok.setMnemonic('G');
        Ok.setText("Generate");
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

        d2.setToolTipText("d2");
        d2.setDateFormatString("yyyy-MM-dd");
        d2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel4.setForeground(new java.awt.Color(255, 102, 0));
        jLabel4.setText("From");

        jLabel5.setForeground(new java.awt.Color(255, 102, 0));
        jLabel5.setText("To");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Ok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Ok1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(d1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(d2, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(o1)
                        .addGap(37, 37, 37)
                        .addComponent(o2)
                        .addGap(18, 18, 18)
                        .addComponent(o3)
                        .addGap(18, 18, 18)
                        .addComponent(o4)
                        .addContainerGap(120, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmbstatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmbtype, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(269, 269, 269)
                .addComponent(jLabel4)
                .addGap(176, 176, 176)
                .addComponent(jLabel5)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbtype, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(o1)
                    .addComponent(o2)
                    .addComponent(o3)
                    .addComponent(o4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(d1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(d2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ok)
                    .addComponent(Ok1))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtypeActionPerformed

    }//GEN-LAST:event_cmbtypeActionPerformed

    private void cmbstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbstatusActionPerformed

    private void OkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkActionPerformed

    }//GEN-LAST:event_OkActionPerformed

    private void Ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ok1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_Ok1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Ok;
    private javax.swing.JButton Ok1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbstatus;
    private javax.swing.JComboBox cmbtype;
    private com.toedter.calendar.JDateChooser d1;
    private com.toedter.calendar.JDateChooser d2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JRadioButton o1;
    private javax.swing.JRadioButton o2;
    private javax.swing.JRadioButton o3;
    private javax.swing.JRadioButton o4;
    // End of variables declaration//GEN-END:variables
}
