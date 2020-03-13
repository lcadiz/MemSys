package Module.Main;


import memsys.global.DBConn.MainDBConn;
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class IncomingSched extends javax.swing.JInternalFrame {

    static Statement stmtIncomingShed;
    static String nowDate = myFunctions.getDate();
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    public static UpdateIncomingSched frmUpdate;

    public IncomingSched()  {
        super();
        initComponents();
    }

    public void populateTBL() {


        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT batchID, sched_venue, CONVERT(char(10), sched_date, 101), areacode, "
                + "sched_address FROM scheduleTBL WHERE sched_date >='" + nowDate + "' ORDER BY sched_date";
        //stmtIncomingShed

        try {
            stmtIncomingShed = conn.createStatement();
            ResultSet rs = stmtIncomingShed.executeQuery(createString);

            model = (DefaultTableModel) jTblLMIncomingSched.getModel();

            renderer.setHorizontalAlignment(0);

            jTblLMIncomingSched.setRowHeight(21);
            jTblLMIncomingSched.getColumnModel().getColumn(0).setMaxWidth(100);
            jTblLMIncomingSched.getColumnModel().getColumn(0).setCellRenderer(renderer);
            jTblLMIncomingSched.getColumnModel().getColumn(1).setMaxWidth(150);
            jTblLMIncomingSched.getColumnModel().getColumn(1).setCellRenderer(renderer);
            jTblLMIncomingSched.setColumnSelectionAllowed(false);

            model.setNumRows(0);


            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(3), rs.getString(2), rs.getString(5)});
            }

            stmtIncomingShed.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void showFrmUpdate() {
        frmUpdate = new UpdateIncomingSched(this, true);
        frmUpdate.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneIncSched = new javax.swing.JScrollPane();
        jTblLMIncomingSched = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cmdUpdate = new javax.swing.JButton();
        cmdCancelSched = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Incoming PMES Schedules");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jScrollPaneIncSched.setToolTipText("List of Incoming PMES Schedule/s");
        jScrollPaneIncSched.setAutoscrolls(true);

        jTblLMIncomingSched.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BATCH No.", "Date", "Venue of Seminar", "Address (Specific)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblLMIncomingSched.setColumnSelectionAllowed(true);
        jScrollPaneIncSched.setViewportView(jTblLMIncomingSched);
        jTblLMIncomingSched.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Membership/Images/manageincomingsched.png"))); // NOI18N
        jToolBar1.add(jLabel4);

        jLabel7.setForeground(new java.awt.Color(255, 102, 0));
        jLabel7.setText(" Note: Here  you can view, update and cancel incoming PMES Schedule");
        jToolBar1.add(jLabel7);

        cmdUpdate.setText("Update");
        cmdUpdate.setToolTipText("Update Current Selected Schedule Note: Only Venue and Address are Editable due to security purposes. Otherwise cancel the schedule and create it again!");
        cmdUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdateActionPerformed(evt);
            }
        });

        cmdCancelSched.setText("Cancel Schedule");
        cmdCancelSched.setToolTipText("Cancel Current Selected Schedule");
        cmdCancelSched.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelSchedActionPerformed(evt);
            }
        });

        cmdCancel.setText("Exit");
        cmdCancel.setToolTipText("Close Window");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(471, Short.MAX_VALUE)
                .addComponent(cmdUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdCancelSched)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdCancel)
                .addContainerGap())
            .addComponent(jScrollPaneIncSched, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneIncSched, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdUpdate)
                    .addComponent(cmdCancelSched)
                    .addComponent(cmdCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
       
        populateTBL();
    }//GEN-LAST:event_formInternalFrameOpened

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void cmdUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdateActionPerformed
        int col = 0; //set column value to 0
        int row = jTblLMIncomingSched.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTblLMIncomingSched.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = jTblLMIncomingSched.getValueAt(row, col).toString();
            UpdateIncomingSched.batchID = id;
            UpdateIncomingSched.venue = jTblLMIncomingSched.getValueAt(row, 2).toString();
            UpdateIncomingSched.address = jTblLMIncomingSched.getValueAt(row, 3).toString();
            showFrmUpdate();
        }

    }//GEN-LAST:event_cmdUpdateActionPerformed

    private void cmdCancelSchedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelSchedActionPerformed
        int col = 0; //set column value to 0
        int row = jTblLMIncomingSched.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTblLMIncomingSched.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = jTblLMIncomingSched.getValueAt(row, col).toString();
            int i = myFunctions.msgboxYesNo("Are you sure you want delete this current record?");
            if (i == 0) {
            String Bid = jTblLMIncomingSched.getValueAt(row, col).toString();
            myDataenvi.rsDeleteSched(Bid);
            populateTBL();
            JOptionPane.showMessageDialog(this, "Record has been successfully deleted!");
            } else if (i == 1) {
                return; //do nothing
            } else if (i == 2) {
                this.dispose(); //exit window
                return;
            }
        }
    }//GEN-LAST:event_cmdCancelSchedActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdCancelSched;
    private javax.swing.JButton cmdUpdate;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPaneIncSched;
    private javax.swing.JTable jTblLMIncomingSched;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
