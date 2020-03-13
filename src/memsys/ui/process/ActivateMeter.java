package memsys.ui.process;

import memsys.global.DBConn.MainDBConn;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import memsys.ui.main.ParentWindow;

public class ActivateMeter extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static ActivateMeterOps frmActivate;
    public static PostStat frmPost;
    //public static int userid;
    static int areacode;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();

    public ActivateMeter() {
        initComponents();
        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);
        populate_cmbtown();
    }

    public void showFrmCosting() {
        frmActivate = new ActivateMeterOps(this, true);
        frmActivate.setVisible(true);
    }

    public void showFrmPost() {
        frmPost = new PostStat(this, true);
        frmPost.setVisible(true);
    }

    public void populate_cmbtown() {
        //Populate Combo Area
        cmbtown.addItem("--SELECT--");
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT areacode, area_desc, tcode FROM AreaTBL";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbtown.addItem(new Item2(rs.getString(1), rs.getString(3).trim() + " - " + rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    class Item2 {

        private String id;
        private String description;

        public Item2(String id, String description) {
            this.id = id;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String toString() {
            return description;
        }
    }

    void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        if (carea.isSelected() == true) {
            createString = "SELECT c.AcctNo, c.AcctName, CONVERT(char(10), TransDate, 101), ClassCode, AcctAddress, e.Name"
                    + " FROM connTBL c "
                    + " LEFT JOIN connTypeTBL t ON c.connType=t.connType"
                    + " LEFT JOIN connStatTBL s on c.status=s.status"
                    + " LEFT JOIN connCOInfoTBL cc ON c.acctno=cc.acctno"
                    + " LEFT JOIN electricianTBL e ON cc.eID=e.eID"
                    + " WHERE c.Status=4 AND c.TownCode='" + areacode + "'  AND (acctname LIKE '%" + txtsearch.getText() + "%' OR c.AcctNo like '%" + txtsearch.getText() + "%')  "
                    + " ORDER BY c.AcctName";
        } else {
            createString = "SELECT c.AcctNo, c.AcctName, CONVERT(char(10), TransDate, 101), ClassCode, AcctAddress, e.Name"
                    + " FROM connTBL c "
                    + " LEFT JOIN connTypeTBL t ON c.connType=t.connType"
                    + " LEFT JOIN connStatTBL s on c.status=s.status"
                    + " LEFT JOIN connCOInfoTBL cc ON c.acctno=cc.acctno"
                    + " LEFT JOIN electricianTBL e ON cc.eID=e.eID"
                    + " WHERE c.Status=4 AND (acctname LIKE '%" + txtsearch.getText() + "%' OR c.AcctNo like '%" + txtsearch.getText() + "%')  "
                    + " ORDER BY c.AcctName";
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
//            jTBLConn.getColumn("Activate").setCellRenderer(new ButtonRenderer());
//            jTBLConn.getColumn("Activate").setCellEditor(new ButtonEditor(new JCheckBox()));

            model.setNumRows(0);

            int cnt = 0;
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
                cnt++;
            }

            lbl.setText(cnt + " Record/s Fetched");
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void activate() {

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        jToolBar1 = new javax.swing.JToolBar();
        cmdprint = new javax.swing.JButton();
        cmdExit3 = new javax.swing.JButton();
        cmdRefresh1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        t1 = new javax.swing.JToggleButton();
        t2 = new javax.swing.JToggleButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jPanel4 = new javax.swing.JPanel();
        carea = new javax.swing.JCheckBox();
        cmbtown = new javax.swing.JComboBox();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        cmdExit2 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        lbl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Activate Consumer Meter");
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

        jToolBar2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar2.add(jToolBar1);

        cmdprint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/activatemeter.png"))); // NOI18N
        cmdprint.setMnemonic('P');
        cmdprint.setText("     Activate Meter    ");
        cmdprint.setFocusable(false);
        cmdprint.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdprint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdprint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdprintActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdprint);

        cmdExit3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        cmdExit3.setMnemonic('x');
        cmdExit3.setText("        Post Status        ");
        cmdExit3.setFocusable(false);
        cmdExit3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExit3ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdExit3);

        cmdRefresh1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdRefresh1.setForeground(new java.awt.Color(255, 153, 0));
        cmdRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ab.png"))); // NOI18N
        cmdRefresh1.setMnemonic('R');
        cmdRefresh1.setText("    Return to Meter Issuance   ");
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
        jToolBar2.add(jSeparator2);

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
        jToolBar2.add(jSeparator5);

        carea.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        carea.setText("Filter by Area");
        carea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                careaActionPerformed(evt);
            }
        });

        cmbtown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtownActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbtown, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(carea))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(carea)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbtown, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jToolBar2.add(jPanel4);
        jToolBar2.add(jSeparator8);

        cmdExit2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit_2.png"))); // NOI18N
        cmdExit2.setMnemonic('x');
        cmdExit2.setText("        Exit        ");
        cmdExit2.setFocusable(false);
        cmdExit2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExit2ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdExit2);
        jToolBar2.add(jSeparator7);

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AccountNo", "Name of Account ", "TransDate", "Class", "Address", "Electrician"
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
        tbl.setColumnSelectionAllowed(true);
        tbl.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbl);
        tbl.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl.getColumnModel().getColumn(0).setMaxWidth(100);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(250);
        tbl.getColumnModel().getColumn(2).setMaxWidth(100);
        tbl.getColumnModel().getColumn(3).setMaxWidth(100);
        tbl.getColumnModel().getColumn(4).setPreferredWidth(200);
        tbl.getColumnModel().getColumn(5).setPreferredWidth(350);

        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl.setForeground(new java.awt.Color(102, 0, 102));
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl.setText("0 Record/s Fetched");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/greenflagmini.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/flagredtmini.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 153, 0));
        jLabel4.setText("0 Record/s Fetched");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(204, 0, 0));
        jLabel5.setText("0 Record/s Fetched");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(61, 61, 61)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(16, 16, 16)
                        .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 1015, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1153, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        populateTBL();
    }//GEN-LAST:event_formInternalFrameOpened

    private void cmdprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdprintActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();

            //  String acctno = jTBLConn.getValueAt(0, 0).toString();
            ActivateMeterOps.an = id;
            showFrmCosting();
        }

    }//GEN-LAST:event_cmdprintActionPerformed

    private void t1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1ActionPerformed

    }//GEN-LAST:event_t1ActionPerformed

    private void t2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2ActionPerformed

    }//GEN-LAST:event_t2ActionPerformed

    private void cmdExit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExit2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExit2ActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void cmdExit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExit3ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();

            //  String acctno = jTBLConn.getValueAt(0, 0).toString();
            PostStat.an = Integer.parseInt(id);
            showFrmPost();
        }


    }//GEN-LAST:event_cmdExit3ActionPerformed

    private void cmbtownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtownActionPerformed
        try {
            Item2 item = (Item2) cmbtown.getSelectedItem();
            areacode = Integer.parseInt(item.getId());

        } catch (Exception e) {
        }
        System.out.println(areacode);
        populateTBL();
    }//GEN-LAST:event_cmbtownActionPerformed

    private void careaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_careaActionPerformed
        if (carea.isSelected() == false) {
            areacode = 0;
            cmbtown.setSelectedIndex(0);
        } else {
            populateTBL();
        }
    }//GEN-LAST:event_careaActionPerformed

    private void cmdRefresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefresh1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tbl.getValueAt(row, col).toString();
            int x = myFunctions.msgboxYesNo("This Record will now transfer Back to Meter Issuance section!" + "\n" + "It will not be available here in activation section unless the Meter Issuance section sends back this record ");
            switch (x) {
                case 0:
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 3);
                    int uid = ParentWindow.getUserID();
                    myDataenvi.rsAddConnLog(Integer.parseInt(id), "Send Back to Meter Issuance", 3, uid, nowDate, "");
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox carea;
    private javax.swing.JComboBox cmbtown;
    private javax.swing.JButton cmdExit2;
    private javax.swing.JButton cmdExit3;
    private javax.swing.JButton cmdRefresh1;
    private javax.swing.JButton cmdprint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lbl;
    private javax.swing.JToggleButton t1;
    private javax.swing.JToggleButton t2;
    private javax.swing.JTable tbl;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
