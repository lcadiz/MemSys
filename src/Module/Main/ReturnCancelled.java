package Module.Main;


import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;

public class ReturnCancelled extends javax.swing.JInternalFrame {

    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static Statement stmtAttendance, stmt;

    public ReturnCancelled() {
        initComponents();
        cmdReturn.setMnemonic('R');
        cmdExit.setMnemonic('x');
        cmdView.setMnemonic('V');
    }

    public static void rsUpdatePartStat(String pID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.participantsTBL "
                + "SET part_stat=0 WHERE partID=" + pID;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static int rsSchedExistence(String bID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT batchID FROM dbo.scheduleTBL WHERE batchID=" + bID;
        int count = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);


            while (rs.next()) {
                count++;
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return count;
    }

    public void populateTBL() {


        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT partID,RTRIM(participantsTBL.part_fname)+' '+LEFT(participantsTBL.part_mname,1) + '. '+' '+RTRIM(participantsTBL.part_lname)+' '+RTRIM(participantsTBL.part_ext)"
                + ",address FROM participantsTBL WHERE batchID=" + txtBatchNo.getText() + "AND part_stat=1 ORDER BY part_lname,part_fname";

        try {
            stmtAttendance = conn.createStatement();
            ResultSet rs = stmtAttendance.executeQuery(createString);

            model = (DefaultTableModel) jTblAttendance.getModel();


            cellAlignCenterRenderer.setHorizontalAlignment(0);

            jTblAttendance.setRowHeight(21);
            jTblAttendance.getColumnModel().getColumn(0).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.setColumnSelectionAllowed(false);

            model.setNumRows(0);



            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)});
            }

            stmtAttendance.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtBatchNo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdView = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblAttendance = new javax.swing.JTable();
        cmdReturn = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();

        setClosable(true);
        setTitle("Return Cancelled Participants");

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Membership/Images/return.png"))); // NOI18N
        jToolBar1.add(jLabel4);

        jLabel7.setForeground(new java.awt.Color(255, 102, 0));
        jLabel7.setText(" Note: Please Enter the batch no of the PMES schedule");
        jToolBar1.add(jLabel7);

        txtBatchNo.setToolTipText("");
        txtBatchNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));

        jLabel1.setText("Batch No.:");

        cmdView.setText("View Cancelled Record(s)");
        cmdView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewActionPerformed(evt);
            }
        });

        jTblAttendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID No.", "Name of Participant", "Address"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblAttendance.setToolTipText("List of All Participant Present in this Scheduled Seminar");
        jTblAttendance.getTableHeader().setReorderingAllowed(false);
        jTblAttendance.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTblAttendanceMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jTblAttendance);
        //set column width
        jTblAttendance.getColumnModel().getColumn(0).setMaxWidth(50);

        cmdReturn.setText("Return");
        cmdReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReturnActionPerformed(evt);
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
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 647, Short.MAX_VALUE)
                        .addComponent(cmdReturn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdExit))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBatchNo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdView, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBatchNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdView))
                .addGap(13, 13, 13)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdReturn)
                    .addComponent(cmdExit))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTblAttendanceMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblAttendanceMouseMoved
}//GEN-LAST:event_jTblAttendanceMouseMoved

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewActionPerformed
        model = (DefaultTableModel) jTblAttendance.getModel();
        model.setNumRows(0);

        int x = rsSchedExistence(txtBatchNo.getText());
        if (x == 0) {
            JOptionPane.showMessageDialog(this, "Schedule doesn't exist!");
            return;

        } else {
            populateTBL();
            int c;
            c = model.getRowCount();
            if (c == 0) {
                JOptionPane.showMessageDialog(this, "No Cancelled Participants in this schedule!");
                return;
            }
        }
    }//GEN-LAST:event_cmdViewActionPerformed

    private void cmdReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdReturnActionPerformed
        int col = 0; //set column value to 0
        int row = jTblAttendance.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTblAttendance.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String prn = jTblAttendance.getValueAt(row, col).toString();
            int i = myFunctions.msgboxYesNo("Are you sure you want to return this record?");
            if (i == 0) {
                rsUpdatePartStat(prn);
                populateTBL();
                 JOptionPane.showMessageDialog(this, "Record sucessfully canceled!");
            } else if (i == 1) {
                return; //do nothing
            } else if (i == 2) {
                this.dispose(); //exit window
                return;
            }
        }
    }//GEN-LAST:event_cmdReturnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdReturn;
    private javax.swing.JButton cmdView;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTblAttendance;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField txtBatchNo;
    // End of variables declaration//GEN-END:variables
}
