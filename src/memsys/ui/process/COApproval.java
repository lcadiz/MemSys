/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.process;

import java.awt.Color;
import memsys.global.DBConn.MainDBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import memsys.global.myDataenvi;
import static memsys.global.myDataenvi.rsAddConnLog;
import memsys.global.myFunctions;
import memsys.ui.main.ParentWindow;
import static memsys.ui.process.ConnApproval.i;
import static memsys.ui.process.ConnApproval.nowDate;
import static memsys.ui.process.CostingOp.model;
import static memsys.ui.process.CostingOp.renderer;
import static memsys.ui.process.CostingOp.stmt;

/**
 *
 * @author lestercadiz
 */
public class COApproval extends javax.swing.JInternalFrame {

    /**
     * Creates new form NewJInternalFrame
     */
    public COApproval() {
        initComponents();
         tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);
    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();

//        if (i == 0) {
//         
//
//            createString = "SELECT AcctNo, MembershipID, AcctName, s.statdesc, CONVERT(char(10), TransDate, 101) ,t.typedesc, ClassCode "
//                    + "FROM connTBL c, connTypeTBL t, connStatTBL s "
//                    + "WHERE transdate='" + nowDate + "' and c.status=s.status AND c.connType=t.connType AND s.status=1 AND (acctname LIKE '%" + txtsearch.getText() + "%' OR AcctNo like '" + txtsearch.getText() + "%') "
//                    + "ORDER BY AcctName";
//        } else if (i == 1) {
        String createString = "SELECT AcctNo, MembershipID, AcctName, s.statdesc, CONVERT(char(10), TransDate, 101) ,t.typedesc, ClassCode, AcctAddress "
                + "FROM connTBL c, connTypeTBL t, connStatTBL s "
                + "WHERE  c.status=s.status AND c.connType=t.connType AND s.status=10 AND (acctname LIKE '%" + txtsearch.getText() + "%' OR AcctNo like '" + txtsearch.getText() + "%') "
                    + "ORDER BY AcctName";
//        }

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);

            tbl.setRowHeight(29);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(4).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(6).setCellRenderer(renderer);
            tbl.setColumnSelectionAllowed(false);

            model.setNumRows(0);

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)});
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

        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        cmdApproved = new javax.swing.JButton();
        cmdRefresh2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        cmdPreview2 = new javax.swing.JButton();
        cmdPreview1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtremarks = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("CO Approval");

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jToolBar1.setInheritsPopupMenu(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(500, 63));
        jToolBar1.setMinimumSize(new java.awt.Dimension(500, 63));

        cmdApproved.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApproved.setForeground(new java.awt.Color(0, 102, 153));
        cmdApproved.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employer.png"))); // NOI18N
        cmdApproved.setMnemonic('S');
        cmdApproved.setText("     Approved and Send to TSD     ");
        cmdApproved.setFocusable(false);
        cmdApproved.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdApproved.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdApproved.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdApproved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdApprovedActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdApproved);

        cmdRefresh2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdRefresh2.setForeground(new java.awt.Color(0, 51, 153));
        cmdRefresh2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        cmdRefresh2.setMnemonic('R');
        cmdRefresh2.setText("     Send Back to CO Issuance      ");
        cmdRefresh2.setFocusable(false);
        cmdRefresh2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdRefresh2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdRefresh2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdRefresh2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefresh2ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdRefresh2);
        jToolBar1.add(jSeparator1);

        jToggleButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jToggleButton1.setForeground(new java.awt.Color(0, 102, 0));
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download.png"))); // NOI18N
        jToggleButton1.setMnemonic('y');
        jToggleButton1.setSelected(true);
        jToggleButton1.setText("   Today   ");
        jToggleButton1.setActionCommand("     Today     ");
        jToggleButton1.setEnabled(false);
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        jToggleButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jToggleButton2.setForeground(new java.awt.Color(153, 0, 0));
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/summary.png"))); // NOI18N
        jToggleButton2.setText("   Previous   ");
        jToggleButton2.setEnabled(false);
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton2);
        jToolBar1.add(jSeparator3);

        cmdPreview2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPreview2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh.png"))); // NOI18N
        cmdPreview2.setMnemonic('w');
        cmdPreview2.setText("     Refresh     ");
        cmdPreview2.setContentAreaFilled(false);
        cmdPreview2.setFocusable(false);
        cmdPreview2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdPreview2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdPreview2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdPreview2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreview2ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdPreview2);

        cmdPreview1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPreview1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdPreview1.setMnemonic('w');
        cmdPreview1.setText("    Exit    ");
        cmdPreview1.setFocusable(false);
        cmdPreview1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdPreview1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdPreview1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdPreview1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreview1ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdPreview1);

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account No", "MemberID", "Account Name", "Status", "AppDate", "Type", "Class", "Address", "CO No."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);
        //set column width
        tbl.getColumnModel().getColumn(0).setMaxWidth(80);
        tbl.getColumnModel().getColumn(1).setMaxWidth(80);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(240);
        tbl.getColumnModel().getColumn(7).setPreferredWidth(240);
        tbl.getColumnModel().getColumn(4).setMaxWidth(100);
        tbl.getColumnModel().getColumn(6).setMaxWidth(50);

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        txtremarks.setColumns(20);
        txtremarks.setRows(5);
        txtremarks.setText("Approved");
        jScrollPane2.setViewportView(txtremarks);

        jLabel1.setText("Remarks");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1073, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdApprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApprovedActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "Please fill-up the remarks!");
            return;
        } else {
            if ("".equals(txtremarks.getText())) {
                JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            } else {
                String id = tbl.getValueAt(row, col).toString();

                int i = myFunctions.msgboxYesNo("This Record will now transfer to TSD Section for checking!" );
                switch (i) {
                    case 0:
                        int uid = ParentWindow.getUserID();
                        rsAddConnLog(Integer.parseInt(id), "Approved and Send to TSD for Checking", 10, uid, nowDate, txtremarks.getText());
                        myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 9);
                        txtremarks.setText("");
                        populateTBL();
                        JOptionPane.showMessageDialog(this, "Record has been successfully sent!");
                        break;
                    case 1:
                        return; //do nothing
                    case 2:
                        this.dispose(); //exit window
                    default:
                }
            }
        }
    }//GEN-LAST:event_cmdApprovedActionPerformed

    private void cmdRefresh2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefresh2ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            if ("".equals(txtremarks.getText())) {
                JOptionPane.showMessageDialog(this, "Remarks is required!");
            } else {
                String id = tbl.getValueAt(row, col).toString();
                int uid = ParentWindow.getUserID();
                int i = myFunctions.msgboxYesNo("Are you sure you want to send back this current application?");
                if (i == 0) {
                    rsAddConnLog(Integer.parseInt(id), "CO Send Back to CO Issuance Section", 9, uid, nowDate, txtremarks.getText());
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 6);
                    txtremarks.setText("");
                    populateTBL();
                    JOptionPane.showMessageDialog(this, "Record has been successfully sent back!");
                } else if (i == 1) {
                    return; //do nothing
                } else if (i == 2) {
                    this.dispose(); //exit window
                    return;
                }
            }
        }
    }//GEN-LAST:event_cmdRefresh2ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        i = 0;
        populateTBL();
        txtsearch.requestFocus();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        i = 1;

        txtsearch.requestFocus();
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void cmdPreview2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview2ActionPerformed
        populateTBL();
    }//GEN-LAST:event_cmdPreview2ActionPerformed

    private void cmdPreview1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdPreview1ActionPerformed

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved

    }//GEN-LAST:event_tblMouseMoved

    private void tblMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblMouseReleased

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdApproved;
    private javax.swing.JButton cmdPreview1;
    private javax.swing.JButton cmdPreview2;
    private javax.swing.JButton cmdRefresh2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tbl;
    private javax.swing.JTextArea txtremarks;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
