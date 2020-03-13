package Module.Main;

import memsys.global.DBConn.MainDBConn;
import memsys.ui.member.AddNonJuridical;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;

public class SelectJoint extends javax.swing.JDialog {

    public static AddNonJuridical frmParent;
    static Statement stmtAttendance;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    public static String pid;

    public SelectJoint(AddNonJuridical parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
         getRootPane().setDefaultButton(cmdselect);
    }

    public void populateTBL() {


        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM"
                + "(SELECT partID, part_lname + ', ' + part_fname + ' ' + part_mname + ' ' + part_ext AS nym, "
                + "CONVERT(char(10), sched_date, 101) AS sdate, RTRIM(sched_venue) + '/'+ RTRIM(sched_address)AS sched, participantsTBL.batchID, address, mem_stat "
                + "FROM participantsTBL "
                + "INNER JOIN scheduleTBL ON participantsTBL.batchID=scheduleTBL.batchID "
                + "WHERE sched_stat=1 AND part_stat=0) TBL WHERE nym LIKE '%" + txtEntry.getText() + "%' AND partID<>" + pid + " AND partID NOT IN (SELECT partID FROM membersTBL)";

        try {
            stmtAttendance = conn.createStatement();
            ResultSet rs = stmtAttendance.executeQuery(createString);

            model = (DefaultTableModel) jTblAttendance.getModel();


            cellAlignCenterRenderer.setHorizontalAlignment(0);

            jTblAttendance.setRowHeight(22);
            jTblAttendance.getColumnModel().getColumn(0).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.getColumnModel().getColumn(2).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.getColumnModel().getColumn(4).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.setColumnSelectionAllowed(false);

            model.setNumRows(0);



            while (rs.next()) {
//                int x = rs.getInt(7);
//                String flg = null;
//
//                if (x == 0) {
//                    flg = "Not Encoded";
//                } else if (x == 1) {
//                    flg = "Encoded";
//                }

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
            }

            stmtAttendance.close();
            conn.close();
            //txtEntry.selectAll();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTblAttendance = new javax.swing.JTable();
        cmdselect = new javax.swing.JButton();
        cmdselect1 = new javax.swing.JButton();
        txtEntry = new org.jdesktop.swingx.JXSearchField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Participant ");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTblAttendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartID", "Name of Participant", "PMES Date", "PMES Venue/Address", "BatchID", "Participant Address"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblAttendance.setToolTipText("");
        jTblAttendance.getTableHeader().setReorderingAllowed(false);
        jTblAttendance.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTblAttendanceMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jTblAttendance);
        //set column width
        jTblAttendance.getColumnModel().getColumn(0).setMaxWidth(80);
        jTblAttendance.getColumnModel().getColumn(2).setMaxWidth(100);
        jTblAttendance.getColumnModel().getColumn(4).setMaxWidth(80);

        cmdselect.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdselect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employer.png"))); // NOI18N
        cmdselect.setMnemonic('S');
        cmdselect.setText("Select");
        cmdselect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdselectActionPerformed(evt);
            }
        });

        cmdselect1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdselect1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdselect1.setMnemonic('C');
        cmdselect1.setText("Cancel");
        cmdselect1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdselect1ActionPerformed(evt);
            }
        });

        txtEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 648, Short.MAX_VALUE)
                                .addComponent(cmdselect)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdselect1)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdselect)
                    .addComponent(cmdselect1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTblAttendanceMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblAttendanceMouseMoved
   }//GEN-LAST:event_jTblAttendanceMouseMoved

    private void cmdselect1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdselect1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdselect1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        populateTBL();
    }//GEN-LAST:event_formWindowOpened

    private void cmdselectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdselectActionPerformed
        int col = 0; //set column value to 0
        int row = jTblAttendance.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTblAttendance.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = jTblAttendance.getValueAt(row, col).toString();
            String name = jTblAttendance.getValueAt(row, 1).toString();
            
            frmParent.setValue(id, name);
            this.dispose();
        }
    }//GEN-LAST:event_cmdselectActionPerformed

    private void txtEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntryActionPerformed
          populateTBL();
    }//GEN-LAST:event_txtEntryActionPerformed

    

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(SelectJoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SelectJoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SelectJoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SelectJoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SelectJoint dialog = new SelectJoint(frmParent, true);
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
    private javax.swing.JButton cmdselect;
    private javax.swing.JButton cmdselect1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTblAttendance;
    private org.jdesktop.swingx.JXSearchField txtEntry;
    // End of variables declaration//GEN-END:variables
}
