package memsys.ui.member;

import memsys.global.DBConn.MainDBConn;

import memsys.global.myDataenvi;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import memsys.global.FTPFactory;

public class Validator extends javax.swing.JInternalFrame {

    static Statement stmt;
    static Statement stmtAttendance;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    public static AddNonJuridical frmAdd;

    public Validator() {
        initComponents();

        cmdApprove.setMnemonic('A');

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

    }

    public void showFrmAdd() {
        frmAdd = new AddNonJuridical(this, true);
        frmAdd.setVisible(true);
    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();
        String createString;
//        createString = "SELECT * FROM"
//                + "(SELECT partID, part_lname + ', ' + part_fname + ' ' + part_mname + ' ' + part_ext AS nym, "
//                + "CONVERT(char(10), sched_date, 101) AS sdate, RTRIM(sched_venue) + '/'+ RTRIM(sched_address)AS sched, participantsTBL.batchID, address, mem_stat "
//                + "FROM participantsTBL "
//                + "INNER JOIN scheduleTBL ON participantsTBL.batchID=scheduleTBL.batchID "
//                + "WHERE sched_stat=1 AND part_stat=0) TBL WHERE nym LIKE '%" + txtsearch.getText() + "%' ";

        createString = "SELECT * FROM"
                + "(SELECT partID, part_lname + ', ' + part_fname + ' ' + part_mname + ' ' + part_ext AS nym, "
                + "CONVERT(char(10), sched_date, 101) AS sdate, RTRIM(sched_venue) + '/'+ RTRIM(sched_address)AS sched, participantsTBL.batchID, address, mem_stat "
                + "FROM participantsTBL "
                + "INNER JOIN scheduleTBL ON participantsTBL.batchID=scheduleTBL.batchID "
                + "WHERE part_stat=0 and partID NOT IN (SELECT partID FROM membersTBL)) TBL WHERE nym LIKE '%" + txtsearch.getText() + "%' ";

        try {
            stmtAttendance = conn.createStatement();
            ResultSet rs = stmtAttendance.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            cellAlignCenterRenderer.setHorizontalAlignment(0);

            tbl.setRowHeight(29);
            tbl.getColumnModel().getColumn(0).setCellRenderer(cellAlignCenterRenderer);
            tbl.getColumnModel().getColumn(2).setCellRenderer(cellAlignCenterRenderer);
            tbl.getColumnModel().getColumn(4).setCellRenderer(cellAlignCenterRenderer);
            tbl.getColumn("View").setCellRenderer(new ButtonRenderer(1));
            tbl.getColumn("View").setCellEditor(new ButtonEditor(new JCheckBox(), 1));
            tbl.setColumnSelectionAllowed(false);

            model.setNumRows(0);

            while (rs.next()) {
//                int x = rs.getInt(7);
//                String flg = null;
//
//                if (x == 0) {
//                    flg = "Not Encoded";
//                } else if (x == 1) {
//                    flg = "Encoded";
//                }

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
            }

            stmtAttendance.close();
            conn.close();
            //txtEntry.selectAll();

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
                    i.FTPViewImage(i.GetFTPPicPath()+ Integer.parseInt(id) + ".jpg", captured);

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

    void valnow() {
//          if (txtEntry.getText().isEmpty() == false) {
        populateTBL();
//            int rc = model.getRowCount();
//            if (rc == 0) {
////                lblPmes.setText("0 match result(s) in PMES Attendance");
//              //  cmdApprove.setEnabled(false);
//            } else {
//         //       lblPmes.setText(rc + " match result(s) in PMES Attendance");
//              //  cmdApprove.setEnabled(true);
//            }

//        cmdApprove1.setEnabled(true);
//        cmdView.setEnabled(true);
//            txtEntry.requestFocus();
//        } else {
//            txtEntry.requestFocus();
//         //   lblPmes.setText("0 match result(s) in PMES Attendance");
//            try {
//                model.setNumRows(0);
//            } catch (NullPointerException e) {
//                e.getStackTrace();
//            }
//      //      JOptionPane.showMessageDialog(this, "Please type an entry for validation!");
//        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        cmdApprove = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        cmdExit = new javax.swing.JButton();
        captured = new javax.swing.JLabel();
        txtsearch = new org.jdesktop.swingx.JXSearchField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("New Non Juridical Member  - Validator and Profile Creation");

        jScrollPane2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane2.setEnabled(false);

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PartID", "Name of Participant", "PMES Date", "PMES Venue/Address", "BatchID", "Participant Address", "View"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
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
        tbl.getColumnModel().getColumn(2).setMaxWidth(100);
        tbl.getColumnModel().getColumn(4).setMaxWidth(80);
        tbl.getColumnModel().getColumn(6).setMaxWidth(60);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        cmdApprove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApprove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/leaveapp_1.png"))); // NOI18N
        cmdApprove.setMnemonic('C');
        cmdApprove.setText("      Create Profile      ");
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
        jToolBar1.add(jSeparator1);

        cmdExit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
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
        jToolBar1.add(cmdExit);

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

        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(captured, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(captured, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 194, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved
}//GEN-LAST:event_tblMouseMoved

    private void cmdApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApproveActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");

        } else {
            String id = tbl.getValueAt(row, 0).toString();

            boolean isfound = myDataenvi.rsIsEncodedNJ(Integer.parseInt(id));

            if (isfound == true) {
                JOptionPane.showMessageDialog(this, "This record is already encoded in the members profiles list!");

            } else if (isfound == false) {

                AddNonJuridical.pid = tbl.getValueAt(row, col).toString();
                AddNonJuridical.nym = tbl.getValueAt(row, 1).toString();
                AddNonJuridical.add = tbl.getValueAt(row, 5).toString();
                // this.dispose();
                showFrmAdd();
            }

        }
    }//GEN-LAST:event_cmdApproveActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void capturedMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMousePressed

    }//GEN-LAST:event_capturedMousePressed

    private void capturedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseReleased

    }//GEN-LAST:event_capturedMouseReleased

    private void capturedMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseDragged

    }//GEN-LAST:event_capturedMouseDragged

    private void capturedMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseMoved

    }//GEN-LAST:event_capturedMouseMoved

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel captured;
    private javax.swing.JButton cmdApprove;
    private javax.swing.JButton cmdExit;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tbl;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
