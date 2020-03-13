package memsys.ui.process;

import memsys.global.DBConn.MainDBConn;
import Module.Main.FCCLog;
import Module.Main.ViewProfileJuri;
import Module.Main.ViewProfileNJuri;
import memsys.global.myFunctions;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import memsys.global.FTPFactory;

public class ValidatorConn extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model2, model;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static ViewProfileNJuri frmView1;
    public static ViewProfileJuri frmView2;
    public static FCCLog frmlog;
    public static CreateNewConn frmCreate;
    static Double cbal;
    public static Vector err;

    public ValidatorConn() {
        initComponents();
        //getRootPane().setDefaultButton(cmdApprove);
        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

        tble.setCellSelectionEnabled(false);
        tble.setRowSelectionAllowed(true);
        tble.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tble.setSelectionBackground(new Color(153, 204, 255));
        tble.setSelectionForeground(Color.BLACK);
    }

    public void showFrmlog() {
        frmlog = new FCCLog(this, true);
        frmlog.setVisible(true);
    }

    public void showFrmCreate() {
        frmCreate = new CreateNewConn(this, true);
        frmCreate.setVisible(true);
    }

    public void showFrmView() {
        frmView1 = new ViewProfileNJuri(this, true);
        frmView1.setVisible(true);
    }

    public void showFrmView2() {
        frmView2 = new ViewProfileJuri(this, true);
        frmView2.setVisible(true);
    }

    void validatenow() {
        try {
            model.setNumRows(0);
            model2.setNumRows(0);
            lbltotal.setText("0.00");
        } catch (Exception e) {
        }
        if (txtEntry.getText().isEmpty() == false) {
            populateTBL();

            int rc = model.getRowCount();
            if (rc == 0) {
                lblPmes.setText("0 match result(s)");
                cmdApprove.setEnabled(false);
                //cmdViewConn.setEnabled(false);
                // cmdViewprofile.setEnabled(false);
            } else {
                lblPmes.setText(rc + " match result(s)");
                cmdApprove.setEnabled(true);
                //cmdViewConn.setEnabled(true);
                //  cmdViewprofile.setEnabled(true);
            }

            txtEntry.requestFocus();
        } else {
            txtEntry.requestFocus();
            lblPmes.setText("0 match result(s)");
            try {
                model.setNumRows(0);
            } catch (NullPointerException e) {
                e.getStackTrace();
            }
            //  JOptionPane.showMessageDialog(this, "Please type an entry for validation!");
        }
    }

    public void populateEConnTBL(int memid) {

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM(select c.acctno, acctname,sum(totalamt-coalesce(amtpaid,0)) as bal from bill b "
                + "inner join consumer c "
                + "on c.acctno=b.acctno "
                + "left outer join collectiondata cd  "
                + "on b.billno=cd.billno  "
                + "where c.membershipID=" + memid + "and (totalamt-coalesce(amtpaid,0)>=0) and b.billno not in (select billno from billcancelled)  "
                + "group by c.acctno,c.membershipID,acctname "
                + ")DATA "
                + "WHERE bal<>0";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model2 = (DefaultTableModel) tble.getModel();

            renderer.setHorizontalAlignment(0);

            tble.setRowHeight(23);
            tble.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tble.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tble.getColumnModel().getColumn(2).setCellRenderer(renderer);
            tble.setColumnSelectionAllowed(false);

            model2.setNumRows(0);

            while (rs.next()) {
                String amnt = myFunctions.amountFormat2(rs.getString(3));
                model2.addRow(new Object[]{rs.getString(1), rs.getString(2), amnt});
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
        int flg;

        int GetPartID(int mid) {
            int UGID = 0;

            Connection conn = MainDBConn.getConnection();
            String createString;
            createString = "select partID FROM membersTBL WHERE memberID=" + mid;

            //int rc = 0;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(createString);

                while (rs.next()) {
                    UGID = rs.getInt(1);
                }

                stmt.close();
                conn.close();

            } catch (Exception e) {
                e.getStackTrace();
            }

            return UGID;
        }

        public ButtonEditor(JCheckBox checkBox, int x) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            flg = x;

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

            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/innn.png"));
            button.setIcon(ico1);

            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                if (flg == 1) {
                    int col = 0; //set column value to 0
                    int row = tbl.getSelectedRow(); //get value of selected value
                    String id = tbl.getValueAt(row, 0).toString();
                    FTPFactory i = new FTPFactory();
                    i.FTPViewImage(i.GetFTPPicPath() + GetPartID(Integer.parseInt(id)) + ".jpg", captured);
                } else if (flg == 2) {
                }
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

        int iflg;

        public ButtonRenderer(int ico) {
            setOpaque(true);
            iflg = ico;
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

            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/positionmini.png"));
            Icon ico2 = new javax.swing.ImageIcon(getClass().getResource("/img/ledgermini.png"));

            if (iflg == 1) {
                setIcon(ico1);
            } else if (iflg == 2) {
                setIcon(ico2);
            }

            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    void getTotalBal() {
        err = new Vector();

        int r = tble.getRowCount();
        int c;
        c = 0;

        double total = 0;
        while (c < r + 1) {
            try {
                String bal = tble.getValueAt(c, 2).toString();
                String acctno = tble.getValueAt(c, 0).toString();

                err.addElement("/" + acctno + ":" + bal);
                total = total + Double.parseDouble(bal.replace(",", ""));
            } catch (Exception e) {
                e.getStackTrace();
            }
            c++;

        }
        cbal = total;
    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT memberID, acctname, m.sex  ,CONVERT(char(10), date_encoded, 101), juridical_stat FROM membersTBL m INNER JOIN memStatTBL s ON m.status=s.memStatID WHERE acctname LIKE '%" + txtEntry.getText() + "%' " + " ORDER BY acctname";
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);

            tbl.setRowHeight(29);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
            tbl.getColumn("View").setCellRenderer(new ButtonRenderer(1));
            tbl.getColumn("View").setCellEditor(new ButtonEditor(new JCheckBox(), 1));
// jTBLprofile.getColumnModel().getColumn(4).setCellRenderer(renderer);
            tbl.setColumnSelectionAllowed(false);

            model.setNumRows(0);

            while (rs.next()) {
                int x = rs.getInt(5);
                String flg = null;
                if (x == 0) {
                    flg = "NON-JURIDICAL";
                } else {
                    flg = "JURIDICAL";
                }

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(4), flg});
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

        jRadioButton1 = new javax.swing.JRadioButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        lblPmeskjjjuuj = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tble = new javax.swing.JTable();
        lblPmes = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lbltotal = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jPanel2 = new javax.swing.JPanel();
        txtEntry = new org.jdesktop.swingx.JXSearchField();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        cmdApprove = new javax.swing.JButton();
        cmdPreview1 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        captured = new javax.swing.JLabel();

        jRadioButton1.setText("jRadioButton1");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Account Validator");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MemberID", "Name of Member", "Date_Encoded", "Type", "View"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
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
        tbl.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                tblHierarchyChanged(evt);
            }
        });
        tbl.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblMouseMoved(evt);
            }
        });
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);
        //set column width
        tbl.getColumnModel().getColumn(0).setMaxWidth(80);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbl.getColumnModel().getColumn(4).setMaxWidth(50);

        lblPmeskjjjuuj.setText("List of Existing Connection");

        tble.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account No.", "Name of Account", "Balance"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tble.setToolTipText("");
        tble.getTableHeader().setReorderingAllowed(false);
        tble.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tbleMouseMoved(evt);
            }
        });
        jScrollPane2.setViewportView(tble);
        //set column width
        //TBLprofile.getColumnModel().getColumn(0).setMaxWidth(80);
        //jTBLprofile.getColumnModel().getColumn(1).setPreferredWidth(150);

        lblPmes.setForeground(new java.awt.Color(0, 102, 0));
        lblPmes.setText("0 match result(s) ");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Total Remaining Balance:");

        lbltotal.setBackground(new java.awt.Color(255, 0, 0));
        lbltotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbltotal.setForeground(new java.awt.Color(204, 0, 0));
        lbltotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltotal.setText("0.00");

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator3);

        txtEntry.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtEntry, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jToolBar1.add(jPanel2);
        jToolBar1.add(jSeparator1);

        cmdApprove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApprove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/leaveapp.png"))); // NOI18N
        cmdApprove.setMnemonic('C');
        cmdApprove.setText("     Create New      ");
        cmdApprove.setFocusable(false);
        cmdApprove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdApprove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdApproveActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdApprove);

        cmdPreview1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPreview1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdPreview1.setMnemonic('w');
        cmdPreview1.setText("       Exit        ");
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
        jToolBar1.add(jSeparator2);

        captured.setForeground(new java.awt.Color(255, 102, 0));
        captured.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nophoto.jpg"))); // NOI18N
        captured.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        captured.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                capturedMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                capturedMouseMoved(evt);
            }
        });
        captured.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                capturedMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                capturedMouseReleased(evt);
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
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addComponent(lblPmes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(lbltotal, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblPmeskjjjuuj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(327, 327, 327))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(captured, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 758, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 184, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(captured, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPmes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblPmeskjjjuuj, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lbltotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jScrollPane3.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 944, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    boolean HasPendingTrans(int id) {
        boolean found = false;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select MembershipID from connTBL where MembershipID=" + id + " and Status<" + 8;

        int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                rc++;
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        if (rc > 0) {
            found = true;
        }

        return found;
    }

    private void cmdApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApproveActionPerformed

        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, 0).toString();

            boolean trapmem = HasPendingTrans(Integer.parseInt(id));
            if (trapmem == false) {
                populateEConnTBL(Integer.parseInt(id));

                getTotalBal();
                lbltotal.setText(myFunctions.amountFormat2(String.valueOf(cbal)));

                double bal = Double.parseDouble(lbltotal.getText().replace(",", ""));

                if (bal > 0) {
                    showFrmlog();
                } else {
                    loadcreation();
                }

            } else {
                JOptionPane.showMessageDialog(this, "This member has a pending connection application! pls verify to avoid double entry!");
            }
        }
}//GEN-LAST:event_cmdApproveActionPerformed

    public void loadcreation() {
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value
        String memid = tbl.getValueAt(row, col).toString();
        String nym = tbl.getValueAt(row, 1).toString();

        CreateNewConn.nym = nym;
        CreateNewConn.memid = memid;

        showFrmCreate();
    }

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved
}//GEN-LAST:event_tblMouseMoved

    private void tbleMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbleMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tbleMouseMoved

    private void tblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseClicked
        int row = tbl.getSelectedRow();
        String id = tbl.getValueAt(row, 0).toString();
        populateEConnTBL(Integer.parseInt(id));
        getTotalBal();

        lbltotal.setText(myFunctions.amountFormat2(String.valueOf(cbal)));
    }//GEN-LAST:event_tblMouseClicked

    private void tblHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_tblHierarchyChanged
    }//GEN-LAST:event_tblHierarchyChanged

    private void txtEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEntryActionPerformed
        validatenow();
    }//GEN-LAST:event_txtEntryActionPerformed

    private void cmdPreview1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdPreview1ActionPerformed

    private void capturedMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMousePressed

    }//GEN-LAST:event_capturedMousePressed

    private void capturedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseReleased

    }//GEN-LAST:event_capturedMouseReleased

    private void capturedMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseDragged

    }//GEN-LAST:event_capturedMouseDragged

    private void capturedMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseMoved

    }//GEN-LAST:event_capturedMouseMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel captured;
    private javax.swing.JButton cmdApprove;
    private javax.swing.JButton cmdPreview1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblPmes;
    private javax.swing.JLabel lblPmeskjjjuuj;
    private javax.swing.JLabel lbltotal;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tble;
    private org.jdesktop.swingx.JXSearchField txtEntry;
    // End of variables declaration//GEN-END:variables
}
