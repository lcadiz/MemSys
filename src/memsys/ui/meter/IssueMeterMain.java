package memsys.ui.meter;

import memsys.global.DBConn.MainDBConn;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import memsys.global.FunctionFactory;
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import memsys.global.myReports;
import memsys.ui.main.ParentWindow;

public class IssueMeterMain extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    //public static JFramePostMeter frmPost;
    public static IssueMeter frmAssignMeter;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    public static int i;

    public IssueMeterMain() {
        initComponents();
        i = 0;
        // populateTBL();

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

    }

//    public void showFrmCPhoto() {
//        frmPost = new JFramePostMeter(this, true);
//        frmPost.setVisible(true);
//    }
    public void showFrmIssue() {
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String AcctNO = tbl.getValueAt(row, col).toString();
            IssueMeter.acctno = AcctNO;

            frmAssignMeter = new IssueMeter(this, true);
            frmAssignMeter.setVisible(true);
        }
    }

    void Edit() {
        showFrmIssue();
    }

    void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        if (i == 0) {
            createString = "SELECT c.AcctNo, AcctName, CONVERT(char(10), TransDate, 101), ClassCode, AcctAddress, MeterSN"
                    + " FROM connTBL c "
                    + " LEFT JOIN connTypeTBL t ON c.connType=t.connType "
                    + " LEFT JOIN connStatTBL s ON c.status=s.status "
                    + " LEFT JOIN meterPostTBL p ON c.acctno=p.acctno"
                    + " WHERE c.Status=3 AND (c.acctname LIKE '%" + txtsearch.getText() + "%' OR c.AcctNo like '" + txtsearch.getText() + "%') "
                    + " ORDER BY AcctName";
        } else if (i == 1) {
            createString = "SELECT AcctNo, AcctName, CONVERT(char(10), TransDate, 101), ClassCode, AcctAddress"
                    + " FROM connTBL c, connTypeTBL t, connStatTBL s "
                    + " WHERE c.status=s.status AND c.connType=t.connType AND c.Status=3 AND (c.acctname LIKE '%" + txtsearch.getText() + "%' OR c.AcctNo like '" + txtsearch.getText() + "%') "
                    + " ORDER BY AcctName";
        }
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);

            tbl.setRowHeight(29);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
//            tbl.getColumn("Issue").setCellRenderer(new ButtonRenderer());
//            tbl.getColumn("Issue").setCellEditor(new ButtonEditor(new JCheckBox()));

            model.setNumRows(0);

            int cnt = 0;
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
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

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                Edit();
            }
            isPushed = false;
            return new String(label);
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }

            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewOpt = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jToolBar2 = new javax.swing.JToolBar();
        jToolBar1 = new javax.swing.JToolBar();
        cmdRefresh2 = new javax.swing.JButton();
        cmdprint = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        cmdApproved = new javax.swing.JButton();
        cmdRefresh1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        t1 = new javax.swing.JToggleButton();
        t2 = new javax.swing.JToggleButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        cmdExit = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Meter Issuance");
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

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.setRecentSearchesSaveKey("");
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });
        txtsearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtsearchKeyPressed(evt);
            }
        });

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AccountNo", "Name of Account ", "AppDate", "Class", "Address", "Meter No"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tbl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);
        if (tbl.getColumnModel().getColumnCount() > 0) {
            tbl.getColumnModel().getColumn(0).setResizable(false);
        }
        tbl.getColumnModel().getColumn(1).setPreferredWidth(300);
        tbl.getColumnModel().getColumn(4).setPreferredWidth(450);

        jToolBar2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar2.add(jToolBar1);

        cmdRefresh2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdRefresh2.setForeground(new java.awt.Color(51, 51, 0));
        cmdRefresh2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/metere.png"))); // NOI18N
        cmdRefresh2.setMnemonic('u');
        cmdRefresh2.setText("    Issue Meter    ");
        cmdRefresh2.setFocusable(false);
        cmdRefresh2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdRefresh2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdRefresh2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdRefresh2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefresh2ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdRefresh2);

        cmdprint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdprint.setForeground(new java.awt.Color(0, 153, 255));
        cmdprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/printer.png"))); // NOI18N
        cmdprint.setMnemonic('x');
        cmdprint.setText("       Print      ");
        cmdprint.setFocusable(false);
        cmdprint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdprint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdprintActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdprint);
        jToolBar2.add(jSeparator2);

        cmdApproved.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApproved.setForeground(new java.awt.Color(0, 102, 153));
        cmdApproved.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employer.png"))); // NOI18N
        cmdApproved.setMnemonic('S');
        cmdApproved.setText("    Send to Activation    ");
        cmdApproved.setFocusable(false);
        cmdApproved.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdApproved.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdApproved.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdApproved.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdApprovedActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdApproved);

        cmdRefresh1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdRefresh1.setForeground(new java.awt.Color(255, 153, 0));
        cmdRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ab.png"))); // NOI18N
        cmdRefresh1.setMnemonic('R');
        cmdRefresh1.setText("       Return to TSD Check      ");
        cmdRefresh1.setFocusable(false);
        cmdRefresh1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdRefresh1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdRefresh1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdRefresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefresh1ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdRefresh1);
        jToolBar2.add(jSeparator1);

        buttonGroup1.add(t1);
        t1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        t1.setForeground(new java.awt.Color(0, 102, 0));
        t1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download.png"))); // NOI18N
        t1.setMnemonic('y');
        t1.setSelected(true);
        t1.setText("   Today   ");
        t1.setActionCommand("     Today     ");
        t1.setEnabled(false);
        t1.setFocusable(false);
        t1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        t1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        t1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1ActionPerformed(evt);
            }
        });
        jToolBar2.add(t1);

        buttonGroup1.add(t2);
        t2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        t2.setForeground(new java.awt.Color(153, 0, 0));
        t2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/summary.png"))); // NOI18N
        t2.setText("   Previous   ");
        t2.setEnabled(false);
        t2.setFocusable(false);
        t2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        t2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        t2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t2ActionPerformed(evt);
            }
        });
        jToolBar2.add(t2);
        jToolBar2.add(jSeparator4);

        cmdExit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit_2.png"))); // NOI18N
        cmdExit.setMnemonic('x');
        cmdExit.setText("        Exit        ");
        cmdExit.setFocusable(false);
        cmdExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdExit);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        txtsearch.requestFocus();
    }//GEN-LAST:event_formInternalFrameOpened

    private void cmdRefresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefresh1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tbl.getValueAt(row, col).toString();
            int x = myFunctions.msgboxYesNo("This Record will now transfer Back to TSD Checking section!" + "\n" + "It will not be available here in approval section unless the TSD Checking Section sends back this record ");
            switch (x) {
                case 0:
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 9);
                    int uid = ParentWindow.getUserID();
                    myDataenvi.rsAddConnLog(Integer.parseInt(id), "Send Back to TSD Check", 9, uid, nowDate, "");
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

    private void t1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1ActionPerformed
        i = 0;
        populateTBL();
        txtsearch.requestFocus();
    }//GEN-LAST:event_t1ActionPerformed

    private void t2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2ActionPerformed
        i = 1;
        // populateTBL();
        txtsearch.requestFocus();
    }//GEN-LAST:event_t2ActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void cmdRefresh2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefresh2ActionPerformed
        Edit();
    }//GEN-LAST:event_cmdRefresh2ActionPerformed

    private void tblKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblKeyPressed
        populateTBL();
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            if (model.getRowCount() == 0) {
            } else {
                Edit();
            }
        }
    }//GEN-LAST:event_tblKeyPressed

    private void txtsearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsearchKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {

            Edit();
        }
    }//GEN-LAST:event_txtsearchKeyPressed

    private void cmdprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdprintActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String acctno = tbl.getValueAt(row, col).toString();
             myReports.rptConnectOrderWH(acctno, ParentWindow.getUserName());
        }
    }//GEN-LAST:event_cmdprintActionPerformed

    private void cmdApprovedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApprovedActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tbl.getValueAt(row, col).toString();
             String mtr="";
            try{
              mtr = tbl.getValueAt(row, 5).toString();
            }catch( Exception e){
                mtr="";
            }
             if (mtr.isEmpty()==true){
                 JOptionPane.showMessageDialog(this, "Not yet issued!");
             }else{
                
                int x = myFunctions.msgboxYesNo("This Record will now be send to TSD for Releasing and Activation!");
                switch (x) {
                    case 0:
                        myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 4);
                        int uid = ParentWindow.getUserID();
                        myDataenvi.rsAddConnLog(Integer.parseInt(id), "Send to TSD for releasing and activation", 4, uid, nowDate, "");
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
        }
    }//GEN-LAST:event_cmdApprovedActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cmdApproved;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdRefresh1;
    private javax.swing.JButton cmdRefresh2;
    private javax.swing.JButton cmdprint;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToggleButton t1;
    private javax.swing.JToggleButton t2;
    private javax.swing.JTable tbl;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    private javax.swing.ButtonGroup viewOpt;
    // End of variables declaration//GEN-END:variables
}
