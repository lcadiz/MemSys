package memsys.ui.process;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import memsys.global.myReports;
import static memsys.global.myDataenvi.rsAddConnLog;
import java.awt.Color;

import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import memsys.global.FunctionFactory;
import memsys.ui.main.ParentWindow;

public class CostingMain extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static CostingOp frmCosting;
    public static CancelCosting frmRemoveCosting;
    //static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    static String nowDate = FunctionFactory.GetSystemNowDateString();
    static String fDate = FunctionFactory.GetSystemNowDateString();
    public static int i;

    public CostingMain() {
        initComponents();
        i = 0;
        System.out.println(nowDate);
        txtsearch.requestFocus();
        getRootPane().setDefaultButton(cmdCosting);

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);
        //  populateTBL();
        // populateTBL();
    }

    public void showFrmCosting() {
        frmCosting = new CostingOp(this, true);
        frmCosting.setVisible(true);
    }

    public void showFrmRemoveCosting() {
        frmRemoveCosting = new CancelCosting(this, true);
        frmRemoveCosting.setVisible(true);
    }

    public void viewAll() {
        txtsearch.setText("");
        //   pane.setVisible(false);
        populateTBL();
    }

    public void viewCustom() {
        //  pane.setVisible(true);
        txtsearch.requestFocus();
    }

    public void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        if (i == 0) {
            createString = "SELECT AcctNo, MembershipID, AcctName, s.statdesc, CONVERT(char(10), TransDate, 101) ,t.typedesc, ClassCode , c.AcctAddress "
                    + "FROM connTBL c, connTypeTBL t, connStatTBL s "
                    + "WHERE   c.status=s.status AND c.connType=t.connType AND s.status=2 AND (acctname LIKE '%" + txtsearch.getText() + "%' OR AcctNo like '" + txtsearch.getText() + "%') "
                    + "ORDER BY AcctName";
        } else if (i == 1) {
            createString = "SELECT AcctNo, MembershipID, AcctName, s.statdesc, CONVERT(char(10), TransDate, 101) ,t.typedesc, ClassCode , c.AcctAddress "
                    + "FROM connTBL c, connTypeTBL t, connStatTBL s "
                    + "WHERE  c.status=s.status AND c.connType=t.connType AND s.status=2 AND (acctname LIKE '%" + txtsearch.getText() + "%' OR AcctNo like '" + txtsearch.getText() + "%') "
                    + "ORDER BY AcctName";
        }

        //stmtIncomingShed
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);
            model = (DefaultTableModel) tbl.getModel();
            renderer.setHorizontalAlignment(0);
            tbl.setRowHeight(29);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(4).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(5).setCellRenderer(renderer);
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

        viewOpt = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        cmdCosting = new javax.swing.JButton();
        cmdPreview = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        cmdApproved = new javax.swing.JButton();
        cmdRefresh1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        cmdApproved1 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        cmdPreview1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Costing");
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

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account No", "MemberID", "Account Name", "Status", "AppDate", "Type", "Class", "Address"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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
        //set column width
        tbl.getColumnModel().getColumn(0).setMaxWidth(80);
        tbl.getColumnModel().getColumn(1).setMaxWidth(80);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(180);
        tbl.getColumnModel().getColumn(4).setMaxWidth(100);
        tbl.getColumnModel().getColumn(6).setMaxWidth(50);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jToolBar1.setInheritsPopupMenu(true);
        jToolBar1.setMaximumSize(new java.awt.Dimension(500, 63));
        jToolBar1.setMinimumSize(new java.awt.Dimension(500, 63));

        cmdCosting.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdCosting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calculator.png"))); // NOI18N
        cmdCosting.setMnemonic('C');
        cmdCosting.setText("    Costing    ");
        cmdCosting.setFocusable(false);
        cmdCosting.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdCosting.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdCosting.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdCosting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCostingActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdCosting);

        cmdPreview.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/printer.png"))); // NOI18N
        cmdPreview.setMnemonic('P');
        cmdPreview.setText("    Print    ");
        cmdPreview.setFocusable(false);
        cmdPreview.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdPreview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdPreview.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreviewActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdPreview);
        jToolBar1.add(jSeparator2);

        cmdApproved.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApproved.setForeground(new java.awt.Color(0, 102, 153));
        cmdApproved.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employer.png"))); // NOI18N
        cmdApproved.setMnemonic('S');
        cmdApproved.setText("  Send to Payment  ");
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

        cmdRefresh1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdRefresh1.setForeground(new java.awt.Color(204, 0, 0));
        cmdRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        cmdRefresh1.setMnemonic('R');
        cmdRefresh1.setText("Cancel Application");
        cmdRefresh1.setEnabled(false);
        cmdRefresh1.setFocusable(false);
        cmdRefresh1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdRefresh1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdRefresh1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdRefresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefresh1ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdRefresh1);
        jToolBar1.add(jSeparator1);

        buttonGroup3.add(jToggleButton1);
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

        buttonGroup3.add(jToggleButton2);
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
        jToolBar1.add(jSeparator4);

        cmdApproved1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApproved1.setForeground(new java.awt.Color(0, 153, 0));
        cmdApproved1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/secset.png"))); // NOI18N
        cmdApproved1.setMnemonic('S');
        cmdApproved1.setText("    Remove Costing    ");
        cmdApproved1.setFocusable(false);
        cmdApproved1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdApproved1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdApproved1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdApproved1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdApproved1ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdApproved1);
        jToolBar1.add(jSeparator3);

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 1253, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved
}//GEN-LAST:event_tblMouseMoved

    private void cmdRefresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefresh1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tbl.getValueAt(row, col).toString();
            int x = myFunctions.msgboxYesNo("This Record will now transfer Back to Approval Section!" + "\n" + "It will not be available here in approval section unless the Approval Section sends back this record ");
            switch (x) {
                case 0:
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 1);
                    int uid = ParentWindow.getUserID();
                    myDataenvi.rsAddConnLog(Integer.parseInt(id), "Send Back to Connection Approval", 1, uid, nowDate,"");
                    populateTBL();
                    JOptionPane.showMessageDialog(this, "Record has been successfully sent!");
                    break;
                case 1:
                    break;
                case 2:
                    this.dispose(); //exit window
                    break;
                default:
            }
        }
}//GEN-LAST:event_cmdRefresh1ActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
//        viewAll();
//        populateTBL();
    }//GEN-LAST:event_formInternalFrameOpened

    public static void RemoveCosting(int id) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM costingTBL WHERE AcctNo=" + id;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void RemoveTempCosting(int id) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM costingtempTBL WHERE AcctNo=" + id;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }


    private void cmdCostingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCostingActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            String nym = tbl.getValueAt(row, 2).toString();
            String a = tbl.getValueAt(row, 7).toString();
            boolean x = myDataenvi.checkCosting(Integer.parseInt(id));

            if (x == true) {
                JOptionPane.showMessageDialog(this, "Account is already costed!, please cancel the previous costing to re-cost this account!");
            } else {
                RemoveCosting(Integer.parseInt(id));
                RemoveTempCosting(Integer.parseInt(id));
                CostingOp.acctno = Integer.parseInt(id);
                CostingOp.acctname = nym;
                CostingOp.address = a;
                showFrmCosting();
            }
        }
    }//GEN-LAST:event_cmdCostingActionPerformed

    public void transferToPayment(int acctno) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO costingTBL (AcctNo, description, qty, unit, cost, COAID, total) "
                + " SELECT AcctNo, description, qty, unit, cost, COAID, total FROM costingTempTBL WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void cmdApprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApprovedActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            String nym = tbl.getValueAt(row, 2).toString();
            boolean x = myDataenvi.checkCosting(Integer.parseInt(id));

            if (x == true) {
                int i = myFunctions.msgboxYesNo("This Record will now transfer to payment!");
                if (i == 0) {

                    int dbl = determineIfBigloads(Integer.parseInt(id));
                    String capt = null;
                    String note = null;

                    if (dbl == 1) {
                        capt = "SUBTOTAL>>";
                        note = "NOTE: The amount of Energy Deposit stated above is only refundable upon termination of contract on electric service, as per Coop Policy.";
                    } else {
                        capt = "TOTAL";
                        note = "";
                    }
//                    try {
//                        myReports.rptCosting(Integer.parseInt(id), nym, "HANGYAD, BAIS CITY", capt, note, "PAYMENT COSTING");
//                    } catch (FileNotFoundException ex) {
//                        Logger.getLogger(CostingMain.class.getName()).log(Level.SEVERE, null, ex);
//                    } catch (IOException ex) {
//                        Logger.getLogger(CostingMain.class.getName()).log(Level.SEVERE, null, ex);
//                    }

                    int uid = ParentWindow.getUserID();
                    rsAddConnLog(Integer.parseInt(id), "Costing saved and approved ", 3, uid, nowDate,"");
                    rsAddConnLog(Integer.parseInt(id), "Send to payment ", 6 , uid, nowDate,"");
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 6);
                    // transferToPayment(Integer.parseInt(id));
                    populateTBL();

                    JOptionPane.showMessageDialog(this, "Record has been successfully sent!");

                } else if (i == 1) {
                    return; //do nothing
                } else if (i == 2) {
                    this.dispose(); //exit window
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Account is not yet been costed!");
            }
        }
}//GEN-LAST:event_cmdApprovedActionPerformed

    private void cmdPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreviewActionPerformed

        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            String nym = tbl.getValueAt(row, 2).toString();
            String a = tbl.getValueAt(row, 7).toString();

            boolean x = myDataenvi.checkCosting(Integer.parseInt(id));

            if (x == true) {
                int dbl = determineIfBigloads(Integer.parseInt(id));
                String capt = null;
                String note = null;

                if (dbl == 1) {
                    capt = "SUBTOTAL>>";
                    note = "NOTE: The amount of Energy Deposit stated above is only refundable upon termination of contract on electric service, as per Coop Policy.";
                } else {
                    capt = "TOTAL";
                    note = "";
                }
                myReports.rptCosting(Integer.parseInt(id), nym, a, capt, note, "PAYMENT COSTING",ParentWindow.getUserName());
            } else {
                JOptionPane.showMessageDialog(null, "Not yet costed!", "Administrator", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }


    }//GEN-LAST:event_cmdPreviewActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
    if(txtsearch.getText().isEmpty()==false){
        populateTBL();
    }
        
    }//GEN-LAST:event_txtsearchActionPerformed

    private void cmdPreview1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdPreview1ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        i = 0;
        populateTBL();
        txtsearch.requestFocus();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        i = 1;
        //populateTBL();
        txtsearch.requestFocus();
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void cmdApproved1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApproved1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            // String nym = tbl.getValueAt(row, 2).toString();
            boolean x = myDataenvi.checkCosting(Integer.parseInt(id));

            if (x == true) {
                CancelCosting.id = Integer.parseInt(id);
                showFrmRemoveCosting();
            } else {
                JOptionPane.showMessageDialog(this, "Account is not yet costed!");
            }

        }
    }//GEN-LAST:event_cmdApproved1ActionPerformed

    public static int determineIfBigloads(int AcctID) {

        int t = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT COAID FROM costingTempTBL WHERE AcctNo=" + AcctID + " AND COAID=54";
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                t++;

            }

            //t= rs.getFetchSize();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return t;

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JButton cmdApproved;
    private javax.swing.JButton cmdApproved1;
    private javax.swing.JButton cmdCosting;
    private javax.swing.JButton cmdPreview;
    private javax.swing.JButton cmdPreview1;
    private javax.swing.JButton cmdRefresh1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tbl;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    private javax.swing.ButtonGroup viewOpt;
    // End of variables declaration//GEN-END:variables
}
