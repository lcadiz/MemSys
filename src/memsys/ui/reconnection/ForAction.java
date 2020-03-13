/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.reconnection;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author engkoicadiz
 */
public class ForAction extends javax.swing.JInternalFrame {

    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static String nowDate = FunctionFactory.GetSystemNowDateString();
    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2;
    public static TagRequest frmTagRequest;
    public static int oflg;

    public ForAction() {
        initComponents();

        model2 = (DefaultTableModel) tblrequest.getModel();

        tblrequest.setCellSelectionEnabled(false);
        tblrequest.setRowSelectionAllowed(true);
        tblrequest.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblrequest.setSelectionBackground(new Color(153, 204, 255));
        tblrequest.setSelectionForeground(Color.BLACK);

        TableColumn idClmn2 = tblrequest.getColumn("type");
        idClmn2.setMaxWidth(0);
        idClmn2.setMinWidth(0);
        idClmn2.setPreferredWidth(0);

        TableColumn idClmn3 = tblrequest.getColumn("oid");
        idClmn3.setMaxWidth(0);
        idClmn3.setMinWidth(0);
        idClmn3.setPreferredWidth(0);

        TableColumn idClmn4 = tblrequest.getColumn("acctno");
        idClmn4.setMaxWidth(0);
        idClmn4.setMinWidth(0);
        idClmn4.setPreferredWidth(0);

        oflg = GetOfficeFlg(ParentWindow.getUserID());
        populateRequestTBL();

    }

    int GetOfficeFlg(int uid) {
        int flg = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT SectionID FROM discoRecoUserSetupTBL WHERE UserID=" + uid;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                flg = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return flg;
    }

    public void showFrmTagRequest() {
        frmTagRequest = new TagRequest(this, true);
        frmTagRequest.setVisible(true);
    }

    public void populateRequestTBL() {
        Connection conn = MainDBConn.getConnection();

        String createString = null;
        if (c.isSelected() == false) {
            createString = "SELECT t.DiscoRecoID, CONCAT(t.AcctNo,'/',c.TownCode,'-',c.RouteCode,'-',c.RouteSeqNo) as AcctNo, c.AcctName, c.AcctAddress, tt.DescripType, u.FullName, t.TypeID, t.OfficeFlg, t.AcctNo "
                    + "FROM discoRecoTransTBL t "
                    + "INNER JOIN Consumer c ON t.AcctNo=c.AcctNo "
                    + "INNER JOIN costingUQTypeTBL tt ON t.TypeID=tt.CTypeID "
                    + "INNER JOIN Users u ON t.UserID=u.UserID "
                    + "WHERE t.status=14 AND (t.AcctNo LIKE '%" + txtsearch.getText() + "%' OR c.AcctName LIKE '%" + txtsearch.getText() + "%') AND OfficeFlg= " + oflg
                    + " GROUP BY t.DiscoRecoID, t.AcctNo, c.AcctName, c.AcctAddress, tt.DescripType, u.FullName, c.TownCode, c.RouteCode, c.RouteSeqNo, t.TypeID, t.OfficeFlg ORDER BY c.AcctName";
        } else {
            createString = "SELECT t.DiscoRecoID, CONCAT(t.AcctNo,'/',c.TownCode,'-',c.RouteCode,'-',c.RouteSeqNo) as AcctNo, c.AcctName, c.AcctAddress, tt.DescripType, u.FullName, t.TypeID, t.OfficeFlg, t.AcctNo "
                    + "FROM discoRecoTransTBL t "
                    + "INNER JOIN Consumer c ON t.AcctNo=c.AcctNo "
                    + "INNER JOIN costingUQTypeTBL tt ON t.TypeID=tt.CTypeID "
                    + "INNER JOIN Users u ON t.UserID=u.UserID "
                    + "WHERE t.status=14 AND (t.AcctNo LIKE '%" + txtsearch.getText() + "%' OR c.AcctName LIKE '%" + txtsearch.getText() + "%') "
                    + " GROUP BY t.DiscoRecoID, t.AcctNo, c.AcctName, c.AcctAddress, tt.DescripType, u.FullName, c.TownCode, c.RouteCode, c.RouteSeqNo, t.TypeID, t.OfficeFlg ORDER BY c.AcctName";
        }
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            tblrequest.setRowHeight(29);
            tblrequest.setColumnSelectionAllowed(false);
            model2.setNumRows(0);

            while (rs.next()) {
                String office = "";
                if (rs.getInt(8) == 1) {
                    office = "MAIN OFFICE";
                } else if (rs.getInt(8) == 2) {
                    office = "GUI OFFICE";
                }
                if (rs.getInt(8) == 3) {
                    office = "BAIS OFFICE";
                }

                model2.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), office, rs.getString(8), rs.getString(9)});
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
        cmdApproved = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        cmdPreview2 = new javax.swing.JButton();
        cmdPreview3 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        cmdPreview1 = new javax.swing.JButton();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblrequest = new javax.swing.JTable();
        c = new javax.swing.JCheckBox();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Request For Action");

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jToolBar1.setInheritsPopupMenu(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(500, 63));
        jToolBar1.setMinimumSize(new java.awt.Dimension(500, 63));

        cmdApproved.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApproved.setForeground(new java.awt.Color(0, 102, 153));
        cmdApproved.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/manageemp.png"))); // NOI18N
        cmdApproved.setMnemonic('S');
        cmdApproved.setText("     Tag Request Acted    ");
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
        jToolBar1.add(jSeparator3);

        cmdPreview2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPreview2.setForeground(new java.awt.Color(255, 153, 0));
        cmdPreview2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/moveup.png"))); // NOI18N
        cmdPreview2.setMnemonic('w');
        cmdPreview2.setText("        Send Back      ");
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

        cmdPreview3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPreview3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh.png"))); // NOI18N
        cmdPreview3.setMnemonic('w');
        cmdPreview3.setText("        Refresh        ");
        cmdPreview3.setContentAreaFilled(false);
        cmdPreview3.setFocusable(false);
        cmdPreview3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdPreview3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdPreview3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdPreview3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreview3ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdPreview3);
        jToolBar1.add(jSeparator4);

        cmdPreview1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPreview1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdPreview1.setMnemonic('w');
        cmdPreview1.setText("           Exit           ");
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

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        tblrequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Request No.", "Account No", "Account Name", "Account Address", "Type of Request", "CWDO", "type", "Office", "oid", "acctno", "TransDate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblrequest.setToolTipText("");
        tblrequest.getTableHeader().setReorderingAllowed(false);
        tblrequest.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblrequestMouseMoved(evt);
            }
        });
        jScrollPane2.setViewportView(tblrequest);
        tblrequest.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblrequest.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblrequest.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblrequest.getColumnModel().getColumn(4).setPreferredWidth(180);
        tblrequest.getColumnModel().getColumn(5).setPreferredWidth(180);

        c.setText("Unfilter");
        c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cActionPerformed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(c))
                            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdApprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApprovedActionPerformed
        int col = 0; //set column value to 0
        int row = tblrequest.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblrequest.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tblrequest.getValueAt(row, col).toString();
            String acctno = tblrequest.getValueAt(row, 9).toString();
            String typedesc = tblrequest.getValueAt(row, 6).toString();
            TagRequest.typedesc = typedesc;
            TagRequest.acctno = Integer.parseInt(acctno);
            TagRequest.id = Integer.parseInt(id);
            showFrmTagRequest();
        }
    }//GEN-LAST:event_cmdApprovedActionPerformed

    private void cmdPreview2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdPreview2ActionPerformed

    private void cmdPreview3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview3ActionPerformed
        // populateTBL();
    }//GEN-LAST:event_cmdPreview3ActionPerformed

    private void cmdPreview1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdPreview1ActionPerformed

    private void tblrequestMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblrequestMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblrequestMouseMoved

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateRequestTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cActionPerformed
        populateRequestTBL();
    }//GEN-LAST:event_cActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox c;
    private javax.swing.JButton cmdApproved;
    private javax.swing.JButton cmdPreview1;
    private javax.swing.JButton cmdPreview2;
    private javax.swing.JButton cmdPreview3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tblrequest;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
