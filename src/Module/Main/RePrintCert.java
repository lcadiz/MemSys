package Module.Main;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import memsys.global.myReports;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;

public class RePrintCert extends javax.swing.JInternalFrame {

    static Statement stmtAttendance, stmtUpdate;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;

    public RePrintCert() {
        initComponents();
        //cmdViewRecords.setMnemonic('V');
        getRootPane().setDefaultButton(cmdViewRecords);
        cmdReprint.setMnemonic('R');
        cmdExit.setMnemonic('x');
    }

    public void populateTBL() {


        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT partID,RTRIM(participantsTBL.part_fname)+' '+LEFT(participantsTBL.part_mname,1) + '. '+' '+RTRIM(participantsTBL.part_lname)+' '+RTRIM(participantsTBL.part_ext)"
                + ",address, part_stat,CONVERT(char(10), date_encoded, 101),CONVERT(char(10), date_printed, 101) FROM participantsTBL WHERE batchID=" + txtBatchNo.getText() + "ORDER BY part_lname,part_fname";

        try {
            stmtAttendance = conn.createStatement();
            ResultSet rs = stmtAttendance.executeQuery(createString);

            model = (DefaultTableModel) jTblAttendance.getModel();


            cellAlignCenterRenderer.setHorizontalAlignment(0);

            jTblAttendance.setRowHeight(21);
            jTblAttendance.getColumnModel().getColumn(0).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.getColumnModel().getColumn(3).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.getColumnModel().getColumn(4).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.getColumnModel().getColumn(5).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.setColumnSelectionAllowed(false);

            model.setNumRows(0);



            while (rs.next()) {
                String stat = null;

                if (rs.getInt(4) == 0) {
                    stat = "Present";
                } else {
                    stat = "Cancelled";
                }

                String prnflg = null;

                if (rs.getString(6) == null) {
                    prnflg = "Not Yet Printed";
                } else {
                    prnflg = rs.getString(6);
                }

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), stat, rs.getString(5), prnflg});
            }

            stmtAttendance.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Record Empty");
            return;
        }


    }

    public static void rsUpdatePrntCount(String pID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.participantsTBL "
                + "SET print_count=print_count+1 WHERE partID=" + pID;

        try {
            stmtUpdate = conn.createStatement();
            stmtUpdate.executeUpdate(createString);
            stmtUpdate.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtBatchNo = new javax.swing.JTextField();
        cmdViewRecords = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblAttendance = new javax.swing.JTable();
        cmdReprint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Re-Print Certificate");

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Membership/Images/print.png"))); // NOI18N
        jToolBar1.add(jLabel4);

        jLabel7.setForeground(new java.awt.Color(255, 102, 0));
        jLabel7.setText(" Note: Here  you can re-print participants certificates.");
        jToolBar1.add(jLabel7);

        jLabel1.setText("Batch No.:");

        txtBatchNo.setToolTipText("");
        txtBatchNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));

        cmdViewRecords.setText("View Records");
        cmdViewRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewRecordsActionPerformed(evt);
            }
        });

        jTblAttendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID No.", "Name of Participant", "Address", "Attendance", "Date Encoded", "Date Printed"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblAttendance.setToolTipText("List of All Participant(s)");
        jTblAttendance.getTableHeader().setReorderingAllowed(false);
        jTblAttendance.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTblAttendanceMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jTblAttendance);
        //set column width
        jTblAttendance.getColumnModel().getColumn(0).setMaxWidth(50);
        jTblAttendance.getColumnModel().getColumn(3).setPreferredWidth(120);

        cmdReprint.setText("Re-Print");
        cmdReprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReprintActionPerformed(evt);
            }
        });

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
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 879, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 731, Short.MAX_VALUE)
                        .addComponent(cmdReprint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdExit))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBatchNo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdViewRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBatchNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdViewRecords))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdReprint)
                    .addComponent(cmdExit))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTblAttendanceMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblAttendanceMouseMoved
}//GEN-LAST:event_jTblAttendanceMouseMoved

    private void cmdViewRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewRecordsActionPerformed

        populateTBL();


    }//GEN-LAST:event_cmdViewRecordsActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdReprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdReprintActionPerformed
        int col = 0; //set column value to 0
        int row = jTblAttendance.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTblAttendance.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            int col2 = 5; //set column value to 0
            String prn = jTblAttendance.getValueAt(row, col2).toString();
            if (prn == "Not Yet Printed") {
                JOptionPane.showMessageDialog(this, "Cannot be re-printed! This Record is not yet been printed, go to print Certificates");
                return;
            } else {
                int i = myFunctions.msgboxYesNo("System will now generate the print preview. PROCEED PRINTING?");
                if (i == 0) {
                    String id = jTblAttendance.getValueAt(row, col).toString();
                    rsUpdatePrntCount(id);
                    myReports.rptCertReprint1b1(id);
                } else if (i == 1) {
                    return; //do nothing
                } else if (i == 2) {
                    this.dispose(); //exit window
                    return;
                }

            }
        }
    }//GEN-LAST:event_cmdReprintActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdReprint;
    private javax.swing.JButton cmdViewRecords;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTblAttendance;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField txtBatchNo;
    // End of variables declaration//GEN-END:variables
}
