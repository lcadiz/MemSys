package Module.Main;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class OCSetup extends javax.swing.JInternalFrame {

    public static AddOC frmAdd;
    public static EditOC frmEdit;
    public static OCSetup frmParent;
    public static Statement stmt;
    public static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
    public static DefaultTableModel model;

    public OCSetup() {
        initComponents();
    }

    public void populatetbl() {

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT costsetID, description, qty, unit, cost, a.COACode FROM costingSetupTBL c INNER JOIN COA a ON c.COAID=a.COAID ORDER BY description";
        //stmtIncomingShed 
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);
            renderer2.setHorizontalAlignment(SwingConstants.RIGHT);

            tbl.setRowHeight(23);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);


            tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(4).setCellRenderer(renderer2);
            tbl.getColumnModel().getColumn(5).setCellRenderer(renderer);
            tbl.setColumnSelectionAllowed(false);

            model.setNumRows(0);


            while (rs.next()) {


                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    public void showFrmAdd() {
        frmAdd = new AddOC(this, true);
        frmAdd.setVisible(true);
    }

    public void showFrmEdit() {
        frmEdit = new EditOC(this, true);
        frmEdit.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTblManageProfile = new javax.swing.JButton();
        cmdEdit = new javax.swing.JButton();
        cmdDel = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Costing Setup - Charges ");
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

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SetupID", "Description", "Qty", "Unit", "Cost", "COA AcctNo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tbl.setToolTipText("");
        tbl.getTableHeader().setReorderingAllowed(false);
        tbl.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);
        tbl.getColumnModel().getColumn(0).setResizable(false);
        tbl.getColumnModel().getColumn(2).setResizable(false);
        tbl.getColumnModel().getColumn(3).setResizable(false);
        tbl.getColumnModel().getColumn(4).setResizable(false);
        tbl.getColumnModel().getColumn(5).setResizable(false);
        //set column width
        tbl.getColumnModel().getColumn(0).setMaxWidth(80);
        tbl.getColumnModel().getColumn(2).setMaxWidth(80);
        tbl.getColumnModel().getColumn(3).setMaxWidth(80);
        tbl.getColumnModel().getColumn(4).setMaxWidth(100);
        tbl.getColumnModel().getColumn(5).setMaxWidth(100);

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Membership/Images/costing.png"))); // NOI18N
        jToolBar1.add(jLabel4);

        jLabel7.setForeground(new java.awt.Color(255, 102, 0));
        jLabel7.setText("Here you can add, edit and delete costing setup for charges.");
        jToolBar1.add(jLabel7);

        jTblManageProfile.setMnemonic('A');
        jTblManageProfile.setText("Add");
        jTblManageProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTblManageProfileActionPerformed(evt);
            }
        });

        cmdEdit.setMnemonic('E');
        cmdEdit.setText("Edit");
        cmdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEditActionPerformed(evt);
            }
        });

        cmdDel.setMnemonic('D');
        cmdDel.setText("Delete");
        cmdDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDelActionPerformed(evt);
            }
        });

        cmdExit.setMnemonic('x');
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
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 817, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 797, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 563, Short.MAX_VALUE)
                        .addComponent(jTblManageProfile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdDel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdExit)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTblManageProfile)
                    .addComponent(cmdEdit)
                    .addComponent(cmdDel)
                    .addComponent(cmdExit))
                .addGap(13, 13, 13))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved
}//GEN-LAST:event_tblMouseMoved

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void jTblManageProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTblManageProfileActionPerformed
        showFrmAdd();
    }//GEN-LAST:event_jTblManageProfileActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        populatetbl();
    }//GEN-LAST:event_formInternalFrameOpened

    private void cmdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEditActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            //getters
            String id = tbl.getValueAt(row, col).toString();
            //setters
            EditOC.id=id;
            
            //showform;
            showFrmEdit();
        }
    }//GEN-LAST:event_cmdEditActionPerformed

    private void cmdDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDelActionPerformed
                int col = 0; //set column value to 0
        int row =tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            int i = myFunctions.msgboxYesNo("Are you sure you want delete this current record?");
            if (i == 0) {
                rsDeleteRec(id);
                populatetbl();
                JOptionPane.showMessageDialog(this, "Record has been successfully deleted!");
            } else if (i == 1) {
                return; //do nothing
            } else if (i == 2) {
                this.dispose(); //exit window
                return;
            }
        }
    }//GEN-LAST:event_cmdDelActionPerformed

        //Add new charges
    public static void rsDeleteRec(String id) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString ="DELETE FROM costingSetupTBL WHERE costsetID="+id; 
                
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdDel;
    private javax.swing.JButton cmdEdit;
    private javax.swing.JButton cmdExit;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jTblManageProfile;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tbl;
    // End of variables declaration//GEN-END:variables
}
