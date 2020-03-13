package memsys.ui.member;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import memsys.global.DBConn.MainDBConn;

public class ReConnectAcct extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model1;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static ReConnectTrans frmRT;

    public ReConnectAcct() {
        initComponents();

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

        tblstatus.setCellSelectionEnabled(false);
        tblstatus.setRowSelectionAllowed(true);
        tblstatus.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblstatus.setSelectionBackground(new Color(153, 204, 255));
        tblstatus.setSelectionForeground(Color.BLACK);
    }

    public void showFrmRT() {
        frmRT = new ReConnectTrans(this, true);
        frmRT.setVisible(true);
    }

    void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";
        createString = "SELECT * FROM Consumer c "
                + "LEFT JOIN ConsumerMeter cm ON c.AcctNo=cm.AcctNo "
                + "LEFT JOIN Meter m ON cm.MeterSN=m.MeterSN "
                + "LEFT JOIN MeterStatus ms ON m.MSCODE=ms.MSCODE "
                + "WHERE m.MsCode=5 AND AcctName LIKE '%" + txtsearch.getText() + "%' OR c.AcctNo LIKE '%" + txtsearch.getText() + "%'";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);

            tbl.setRowHeight(29);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            //tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
            model.setNumRows(0);

            int cnt = 0;
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("AcctNo"), rs.getString("AcctName"), rs.getString("AcctAddress"), rs.getString("MSDesc")});
                cnt++;
            }
            if (cnt != 0) {
                tbl.setRowSelectionInterval(0, 0);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void populateTBLStatus(int acctno) {
        Connection conn = MainDBConn.getConnection();
        String createString = "select * from MeterStatusLog m "
                + "left join MeterStatus ms on m.MSCode=ms.MSCode "
                + "left join Users u on m.UserID=u.UserID "
                + "WHERE acctno=" + acctno + " ORDER BY m.TransDate DESC";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model1 = (DefaultTableModel) tblstatus.getModel();

            renderer.setHorizontalAlignment(0);

            tblstatus.setRowHeight(29);
            //  tblstatus.getColumnModel().getColumn(0).setCellRenderer(renderer);
            //tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
            model1.setNumRows(0);

            int cnt = 0;
            while (rs.next()) {
                model1.addRow(new Object[]{rs.getDate("TransDate"), rs.getString("MSDesc"), rs.getString("Remarks"), rs.getString("FullName")});
                cnt++;
            }
            if (cnt != 0) {
                tblstatus.setRowSelectionInterval(0, 0);
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

        jToolBar1 = new javax.swing.JToolBar();
        cmdApprove = new javax.swing.JButton();
        cmdApprove1 = new javax.swing.JButton();
        cmdreconnect = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        cmdExit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblstatus = new javax.swing.JTable();
        lbl = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Re-Connect Account");
        setToolTipText("");

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        cmdApprove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApprove.setForeground(new java.awt.Color(102, 0, 153));
        cmdApprove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        cmdApprove.setMnemonic('C');
        cmdApprove.setText("      Change Account Name      ");
        cmdApprove.setActionCommand("      Create Profile     ");
        cmdApprove.setFocusable(false);
        cmdApprove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdApprove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdApproveActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdApprove);

        cmdApprove1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApprove1.setForeground(new java.awt.Color(0, 102, 153));
        cmdApprove1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/new1.png"))); // NOI18N
        cmdApprove1.setMnemonic('C');
        cmdApprove1.setText("          Logs          ");
        cmdApprove1.setActionCommand("      Create Profile     ");
        cmdApprove1.setFocusable(false);
        cmdApprove1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdApprove1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdApprove1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdApprove1ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdApprove1);

        cmdreconnect.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdreconnect.setForeground(new java.awt.Color(0, 153, 153));
        cmdreconnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/devices.png"))); // NOI18N
        cmdreconnect.setMnemonic('C');
        cmdreconnect.setText("    Re-Connect    ");
        cmdreconnect.setActionCommand("      Create Profile     ");
        cmdreconnect.setFocusable(false);
        cmdreconnect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdreconnect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdreconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdreconnectActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdreconnect);
        jToolBar1.add(jSeparator1);

        cmdExit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdExit.setMnemonic('x');
        cmdExit.setText("       Exit       ");
        cmdExit.setFocusable(false);
        cmdExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdExit);

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AccountNo", "Account Name", "Address", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(350);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(280);

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        tblstatus.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DateTime", "Status", "Remarks", "User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblstatus);
        tblstatus.getColumnModel().getColumn(2).setPreferredWidth(350);

        lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl.setText("No Account Selected.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 616, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApproveActionPerformed
//        int col = 0; //set column value to 0
//        int row = tbl.getSelectedRow(); //get value of selected value
//
//        //trap user incase if no row selected
//        if (tbl.isRowSelected(row) != true) {
//            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
//        } else {
//            String id = tbl.getValueAt(row, 0).toString();
//            ChangeNameDialog.acctno = Integer.parseInt(id);
//            showFrmChange();
//        }
    }//GEN-LAST:event_cmdApproveActionPerformed

    private void cmdApprove1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApprove1ActionPerformed
//        int col = 0; //set column value to 0
//        int row = tbl.getSelectedRow(); //get value of selected value
//
//        //trap user incase if no row selected
//        if (tbl.isRowSelected(row) != true) {
//            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
//        } else {
//            String id = tbl.getValueAt(row, 0).toString();
//            ChangeAcctNameLogs.acctno = Integer.parseInt(id);
//            showFrmLogs();
//        }
    }//GEN-LAST:event_cmdApprove1ActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void cmdreconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdreconnectActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
        } else {
            String id = tbl.getValueAt(row, 0).toString();
            ReConnectTrans.acctno = Integer.parseInt(id);
            showFrmRT();
        }
    }//GEN-LAST:event_cmdreconnectActionPerformed

    private void tblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseClicked
        if (evt.getClickCount() == 2) {
            int col = 0; //set column value to 0
            int row = tbl.getSelectedRow(); //get value of selected value

            String acctno = tbl.getValueAt(row, col).toString();
            String acctnym = tbl.getValueAt(row, 1).toString();
            populateTBLStatus(Integer.parseInt(acctno));
            lbl.setText("Account: " + acctnym);
        }
    }//GEN-LAST:event_tblMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdApprove;
    private javax.swing.JButton cmdApprove1;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdreconnect;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lbl;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tblstatus;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
