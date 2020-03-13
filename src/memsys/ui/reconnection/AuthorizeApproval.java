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
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author engkoicadiz
 */
public class AuthorizeApproval extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2;
    public static ReturnToCreator frmReturn;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static String nowDate = FunctionFactory.GetSystemNowDateString();
    static String nowDate2 = FunctionFactory.getSystemNowDateTimeString();
    static int typeid;

    public AuthorizeApproval() {
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

        populateRequestTBL();
    }

    public void showFrmReturn() {
        frmReturn = new ReturnToCreator(this, true);
        frmReturn.setVisible(true);
    }

    public void populateRequestTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "SELECT t.DiscoRecoID, CONCAT(t.AcctNo,'/',c.TownCode,'-',c.RouteCode,'-',c.RouteSeqNo) as AcctNo, c.AcctName, c.AcctAddress, tt.DescripType, u.FullName, t.TypeID, t.OfficeFlg, t.AcctNo "
                + "FROM discoRecoTransTBL t "
                + "INNER JOIN Consumer c ON t.AcctNo=c.AcctNo "
                        + "INNER JOIN costingUQTypeTBL tt ON t.TypeID=tt.CTypeID "
                + "INNER JOIN Users u ON t.UserID=u.UserID "
                + "WHERE t.status=13 "
                + "GROUP BY t.DiscoRecoID, t.AcctNo, c.AcctName, c.AcctAddress,tt.DescripType, u.FullName, c.TownCode, c.RouteCode, c.RouteSeqNo, t.TypeID, t.OfficeFlg";

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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Authorize and Approve Disco/Reco");

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
        cmdApproved.setText("    Send to TSD for Action    ");
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

        tblrequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Request No.", "Account No", "Account Name", "Account Address", "Type of Request", "CWDO", "type", "Office", "oid", "acctno"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane2))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void rsAddConnLog(int acctno, String remarks, int statid, int uid, String nowDate, String note) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO connLogTBL (AcctNo, Remarks, StatusID, UserID, TransDate, Note) VALUES (" + acctno + ",'" + remarks + "'," + statid + "," + uid + ",'" + nowDate + "','" + note + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    String GetStatusDesc() {
        String desc = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT DescripType FROM costingUQTypeTBL WHERE CtypeId=" + typeid;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                desc = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return desc;
    }

    private void cmdApprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApprovedActionPerformed
        int col = 0; //set column value to 0
        int row = tblrequest.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblrequest.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tblrequest.getValueAt(row, col).toString();
            String acctno = tblrequest.getValueAt(row, 9).toString();
            String typeidstr = tblrequest.getValueAt(row, 6).toString();
            typeid = Integer.parseInt(typeidstr);

            int x = myFunctions.msgboxYesNo("This request will now be send to TSD for action!");
            switch (x) {
                case 0:
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(acctno), 14);
                    myDataenvi.rsUpdateRequest(Integer.parseInt(id), 14);
                    rsAddConnLog(Integer.parseInt(acctno), GetStatusDesc() + " request for TSD Action", 14, ParentWindow.getUserID(), nowDate2, "");
                    int uid = ParentWindow.getUserID();
                    //myDataenvi.rsAddConnLog(Integer.parseInt(id), "Send to TSD for Action", 14, uid, nowDate, "");
                    populateRequestTBL();
                    JOptionPane.showMessageDialog(this, "Record has been successfully sent!");
                   // this.dispose();
                    break;
                case 1:
                    break;
                case 2:
                    this.dispose(); //exit window
                    break;
                default:
            }
        }
    }//GEN-LAST:event_cmdApprovedActionPerformed

    private void cmdPreview2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview2ActionPerformed
        int col = 0; //set column value to 0
        int row = tblrequest.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblrequest.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String AcctNO = tblrequest.getValueAt(row, 9).toString();
            String id = tblrequest.getValueAt(row, col).toString();
            String typeidstr = tblrequest.getValueAt(row, 6).toString();
            typeid = Integer.parseInt(typeidstr);
            ReturnToCreator.typeid = typeid;
            ReturnToCreator.acctno = Integer.parseInt(AcctNO);
            ReturnToCreator.id = Integer.parseInt(id);
            showFrmReturn();
        }
    }//GEN-LAST:event_cmdPreview2ActionPerformed

    private void cmdPreview1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdPreview1ActionPerformed

    private void cmdPreview3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview3ActionPerformed
        // populateTBL();
    }//GEN-LAST:event_cmdPreview3ActionPerformed

    private void tblrequestMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblrequestMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblrequestMouseMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
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
