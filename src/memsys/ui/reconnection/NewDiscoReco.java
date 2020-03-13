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
import memsys.global.ReportFactory;
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author LESTER JP CADIZ
 */
public class NewDiscoReco extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static CreateNewRequest frmCreateNewRequest;

    public static OverridePayment frmOverridePayment;
    public static ViewLog frmLogs;
    static String nowDate = FunctionFactory.GetSystemNowDateString();
    static String nowDate2 = FunctionFactory.getSystemNowDateTimeString();
    public static int oflg;
    static String areacode;

    public NewDiscoReco() {
        initComponents();
        model = (DefaultTableModel) tbl.getModel();
        model2 = (DefaultTableModel) tblrequest.getModel();

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

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

        // oflg = GetOfficeFlg(ParentWindow.getUserID());
        areacode = GetAreaCode(ParentWindow.getUserID());
        populateRequestTBL();

        // jButton7.setVisible(false);
        //c.setVisible(false);
    }

    public void showFrmNewRequest() {
        frmCreateNewRequest = new CreateNewRequest(this, true);
        frmCreateNewRequest.setVisible(true);
    }

    public void showFrmOverridePayment() {
        frmOverridePayment = new OverridePayment(this, true);
        frmOverridePayment.setVisible(true);
    }

    public void showFrmLogs() {
        frmLogs = new ViewLog(this, true);
        frmLogs.setVisible(true);
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

    String GetAreaCode(int uid) {
        String flg = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT AreaCode FROM Users WHERE UserID=" + uid;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                flg = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return flg;
    }

    String GetBillDeposit(int DRID) {
        String bd = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT BillDeposit FROM discoRecoTransTBL WHERE DiscoRecoID=" + DRID;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                bd = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return bd;
    }

    public void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        createString = "SELECT c.AcctNo, AcctName, AcctAddress ,MSCode "
                + "FROM Consumer c INNER JOIN ConsumerMeter cm ON c.AcctNo=cm.AcctNo "
                + "INNER JOIN Meter m ON cm.MeterSN=m.MeterSN "
                + "WHERE c.acctname LIKE '%" + txtsearch.getText() + "%' OR c.AcctNo like '" + txtsearch.getText() + "%' "
                + "ORDER BY AcctName ";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            //renderer.setHorizontalAlignment(0);
            tbl.setRowHeight(24);
            tbl.setColumnSelectionAllowed(false);
            model.setNumRows(0);

            int cnt = 0;
            while (rs.next()) {
                String Stat = "";
                if (rs.getInt(4) == 4) {
                    Stat = "Connected";
                } else if (rs.getInt(4) == 5) {
                    Stat = "Disconnected";
                }
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), Stat});
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

    public void populateRequestTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        if (c.isSelected() == false) {
            createString = "SELECT t.DiscoRecoID, CONCAT(t.AcctNo,'  :  ',c.TownCode,'-',c.RouteCode,'-',c.RouteSeqNo) as AcctNo, c.AcctName, c.AcctAddress, tt.DescripType, u.FullName, t.TypeID, t.OfficeFlg, t.AcctNo "
                    + "FROM discoRecoTransTBL t "
                    + "INNER JOIN Consumer c ON t.AcctNo=c.AcctNo "
                    + "INNER JOIN costingUQTypeTBL tt ON t.TypeID=tt.CTypeID "
                    + "INNER JOIN Users u ON t.UserID=u.UserID "
                    + "WHERE t.status=12 AND t.UserId=" + ParentWindow.getUserID()
                    + " GROUP BY t.DiscoRecoID, t.AcctNo, c.AcctName, c.AcctAddress, tt.DescripType, u.FullName, c.TownCode, c.RouteCode, c.RouteSeqNo, t.TypeID, t.OfficeFlg ORDER BY t.DiscoRecoId DESC";
        } else {
            createString = "SELECT t.DiscoRecoID, CONCAT(t.AcctNo,'  :  ',c.TownCode,'-',c.RouteCode,'-',c.RouteSeqNo) as AcctNo, c.AcctName, c.AcctAddress, tt.DescripType, u.FullName, t.TypeID, t.OfficeFlg, t.AcctNo "
                    + "FROM discoRecoTransTBL t "
                    + "INNER JOIN Consumer c ON t.AcctNo=c.AcctNo "
                    + "INNER JOIN costingUQTypeTBL tt ON t.TypeID=tt.CTypeID "
                    + "INNER JOIN Users u ON t.UserID=u.UserID "
                    + "WHERE t.status=12 "
                    + " GROUP BY t.DiscoRecoID, t.AcctNo, c.AcctName, c.AcctAddress, tt.DescripType, u.FullName, c.TownCode, c.RouteCode, c.RouteSeqNo, t.TypeID, t.OfficeFlg ORDER BY t.DiscoRecoId DESC";
        }

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            tblrequest.setRowHeight(24);
            tblrequest.setColumnSelectionAllowed(false);
            model2.setNumRows(0);

            while (rs.next()) {
                String office = "";
                if (rs.getInt(8) == 1) {
                    office = "MAIN OFFICE";
                } else if (rs.getInt(8) == 2) {
                    office = "GUI OFFICE";
                } else if (rs.getInt(8) == 3) {
                    office = "BAIS OFFICE";
                } else if (rs.getInt(8) == 4) {
                    office = "MABINAY OFFICE";
                } else if (rs.getInt(8) == 5) {
                    office = "BAPA";
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

        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblrequest = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        c = new javax.swing.JCheckBox();
        jButton9 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("New Request for Disco/Reco/Others");
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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Pending/Returned Request");

        tblrequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Request No.", "Account No", "Account Name", "Account Address", "Type of Request", "CWDO", "type", "Office", "oid", "acctno", "Bill Payment(%)", "DR Payment Overide"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblrequest.setToolTipText("");
        tblrequest.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblrequest.getTableHeader().setReorderingAllowed(false);
        tblrequest.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblrequestMouseMoved(evt);
            }
        });
        jScrollPane2.setViewportView(tblrequest);
        tblrequest.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblrequest.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblrequest.getColumnModel().getColumn(1).setPreferredWidth(220);
        tblrequest.getColumnModel().getColumn(4).setPreferredWidth(220);
        tblrequest.getColumnModel().getColumn(5).setPreferredWidth(180);
        tblrequest.getColumnModel().getColumn(6).setPreferredWidth(180);
        tblrequest.getColumnModel().getColumn(7).setPreferredWidth(180);
        tblrequest.getColumnModel().getColumn(10).setPreferredWidth(180);
        tblrequest.getColumnModel().getColumn(11).setPreferredWidth(180);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        jButton2.setToolTipText("Cancel Transaction");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logs.png"))); // NOI18N
        jButton4.setToolTipText("Logs");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/printer.png"))); // NOI18N
        jButton5.setToolTipText("Print");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/moveup.png"))); // NOI18N
        jButton6.setToolTipText("Send");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/leaveapp.png"))); // NOI18N
        jButton7.setToolTipText("Override Payment");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        c.setText("Unfilter");
        c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh.png"))); // NOI18N
        jButton9.setToolTipText("Send");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1054, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton2)
                                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton5)
                                    .addComponent(jButton6)
                                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addComponent(jButton9, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(c)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(c)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(8, 8, 8)
                        .addComponent(jButton4))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE))
                .addContainerGap())
        );

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account No", "Account Name", "Account Address", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

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
        //set column width
        tbl.getColumnModel().getColumn(0).setMaxWidth(80);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(300);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(300);

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        jButton3.setToolTipText("Create New");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 0, 102));
        jLabel2.setText("Search Consumer Account");

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        jButton8.setToolTipText("Hide Search View");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1054, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton3)
                                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(26, 26, 26))
        );

        jScrollPane3.setViewportView(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1177, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved

    }//GEN-LAST:event_tblMouseMoved

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

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

    private void tblrequestMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblrequestMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblrequestMouseMoved

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int col = 0; //set column value to 0
        int row = tblrequest.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblrequest.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tblrequest.getValueAt(row, col).toString();
            String acctno = tblrequest.getValueAt(row, 9).toString();
            String type = tblrequest.getValueAt(row, 4).toString();

            int x = myFunctions.msgboxYesNo("Are you sure you want to cancel this transaction!");
            switch (x) {
                case 0:
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(acctno), 17);
                    myDataenvi.rsUpdateRequest(Integer.parseInt(id), 17);
                    rsAddConnLog(Integer.parseInt(acctno), type + " request cancelled", 17, ParentWindow.getUserID(), nowDate2, "");

                    int uid = ParentWindow.getUserID();
                    populateRequestTBL();
                    JOptionPane.showMessageDialog(this, "Record has been successfully cancelled!");
                    break;
                case 1:
                    break;
                case 2:
                    this.dispose(); //exit window
                    break;
                default:
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String AcctNO = tbl.getValueAt(row, col).toString();
            String AcctNym = tbl.getValueAt(row, 1).toString();
            String Stat = tbl.getValueAt(row, 3).toString();
            CreateNewRequest.AcctNo = AcctNO;
            CreateNewRequest.Nym = AcctNym;
            CreateNewRequest.Stat = Stat;
            showFrmNewRequest();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int col = 9; //set column value to 0
        int row = tblrequest.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblrequest.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {

            String an = tblrequest.getValueAt(row, col).toString();
            ViewLog.acctno = Integer.parseInt(an);
            showFrmLogs();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int col = 0; //set column value to 0
        int row = tblrequest.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblrequest.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tblrequest.getValueAt(row, col).toString();
            String tid = tblrequest.getValueAt(row, 6).toString();
            String ofis = tblrequest.getValueAt(row, 7).toString();
            String oid = tblrequest.getValueAt(row, 8).toString();
            String type = tblrequest.getValueAt(row, 4).toString();

            String bd = GetBillDeposit(Integer.parseInt(id));
            System.out.print(bd);
            String lbl = "";
            if (bd.isEmpty() == true) {
                lbl = "";
            } else {
                lbl = " - Bill Deposit under O.R. No.: ";
            }

            // int officeID = Integer.parseInt(oid);
            String manager = "";

            String OrderBy = "";
            if ("01".equals(areacode)) {
                OrderBy = "Area Engineer";
                manager = "JONAH M. MAXINO";

            } else {
                OrderBy = "Area Engineer";
                manager = "ERLYN D. ZAMORA";
            }

            ReportFactory.rptReconnection(Integer.parseInt(id), lbl, ofis, OrderBy, type + " ORDER", manager);

        }
    }//GEN-LAST:event_jButton5ActionPerformed

    public void Send() {
        int col = 0; //set column value to 0
        int row = tblrequest.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblrequest.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tblrequest.getValueAt(row, col).toString();
            String acctno = tblrequest.getValueAt(row, 9).toString();
            String type = tblrequest.getValueAt(row, 4).toString();

            DiscoReco dr = new DiscoReco();
            boolean IsPd = dr.IsPaid(Integer.parseInt(id));

            if (IsPd == true) {

                int x = myFunctions.msgboxYesNo("This request will now be send to ISM for approval!");
                switch (x) {
                    case 0:
                        myDataenvi.rsUpdateConnStat(Integer.parseInt(acctno), 13);
                        myDataenvi.rsUpdateRequest(Integer.parseInt(id), 13);
                        rsAddConnLog(Integer.parseInt(acctno), type + " request send for approval", 13, ParentWindow.getUserID(), nowDate2, "");
                        int uid = ParentWindow.getUserID();
                        myDataenvi.rsAddConnLog(Integer.parseInt(id), "Send for approval", 13, uid, nowDate, "");
                        populateRequestTBL();
                        JOptionPane.showMessageDialog(this, "Record has been successfully sent!");
                        break;
                    case 1:
                        break;
                    case 2:
                        this.dispose(); //exit window
                        break;
                    default:
                }

            } else {
                JOptionPane.showMessageDialog(this, "Transaction is not yet paid!");
            }
        }
    }

    public void Override() {
        int col = 0; //set column value to 0
        int row = tblrequest.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblrequest.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tblrequest.getValueAt(row, col).toString();
            String acctno = tblrequest.getValueAt(row, 9).toString();
            String type = tblrequest.getValueAt(row, 4).toString();

            DiscoReco dr = new DiscoReco();
            //    boolean IsPd = dr.IsPaid(Integer.parseInt(id));

            //   if (IsPd == true) {
            int x = myFunctions.msgboxYesNo("This request will now be send to ISM for approval!");
            switch (x) {
                case 0:
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(acctno), 13);
                    myDataenvi.rsUpdateRequest(Integer.parseInt(id), 13);
                    rsAddConnLog(Integer.parseInt(acctno), type + " request send for approval", 13, ParentWindow.getUserID(), nowDate2, "");
                    int uid = ParentWindow.getUserID();
                    myDataenvi.rsAddConnLog(Integer.parseInt(id), "Send for approval", 13, uid, nowDate, "");
                    populateRequestTBL();
                    JOptionPane.showMessageDialog(this, "Record has been successfully sent!");
                    break;
                case 1:
                    break;
                case 2:
                    this.dispose(); //exit window
                    break;
                default:
            }

            // } else {
            //     JOptionPane.showMessageDialog(this, "Transaction is not yet paid!");
            // }
        }
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        Send();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        int col = 0; //set column value to 0
        int row = tblrequest.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblrequest.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tblrequest.getValueAt(row, col).toString();

            showFrmOverridePayment();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
// +-   *
    }//GEN-LAST:event_formInternalFrameOpened

    private void cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cActionPerformed
        populateRequestTBL();
    }//GEN-LAST:event_cActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox c;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tblrequest;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
