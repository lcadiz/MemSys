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
import javax.swing.table.TableColumn;

public class IssueMeter extends javax.swing.JDialog {

    public static IssueMeterMain frmParent;
    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static SelectCrew frmSelect;
    static int cnt;
    public static String acctno;

    public IssueMeter(IssueMeterMain parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        txtsearch.requestFocus();
//        getRootPane().setDefaultButton(cmdsearch);
        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);
        model = (DefaultTableModel) tbl.getModel();
        lbl.setText("Account No.: " + acctno);

        TableColumn idClmn2 = tbl.getColumn("id");
        idClmn2.setMaxWidth(0);
        idClmn2.setMinWidth(0);
        idClmn2.setPreferredWidth(0);
    }

    boolean isInstalled(String MeterSN) {
        boolean isExist = false;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select * from ConsumerMeter where MeterSN='" + MeterSN + "'";

        int c = 0;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                c++;
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        if (c > 0) {
            isExist = true;
        }

        return isExist;
    }

    public void showFrmSelect() {
        int col = 3; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value
        String id = tbl.getValueAt(row, 5).toString();
        String sn = tbl.getValueAt(row, 0).toString();

        boolean isIntalledAlready = isInstalled(sn);
        if (isIntalledAlready == true) {
            JOptionPane.showMessageDialog(this, "Meter is already assigned to another consumer!");
        } else if (isIntalledAlready == false) {

            SelectCrew.mpID = Integer.parseInt(id);
            SelectCrew.acctno = acctno;
            SelectCrew.sn = sn;

            frmSelect = new SelectCrew(this, true);
            frmSelect.setVisible(true);
        }
    }

    public void refreshTAblePArent() {
        frmParent.populateTBL();
    }

    void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT mp.MeterSN, mt.TypeDesc, mpID, mp.CoopSN "
                + "FROM meterPostTBL mp "
                + "LEFT JOIN MeterBrand mb ON mp.BrandID = mb.BrandID "
                + "LEFT JOIN MeterType mt on mp.TypeCode=mt.TypeCode "
                + "WHERE  mp.MeterSN LIKE '%" + txtsearch.getText() + "%' OR mp.mpID LIKE '%" + txtsearch.getText() + "%' OR mp.CoopSN LIKE '%" + txtsearch.getText() + "%' AND mp.Flag=1";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            renderer.setHorizontalAlignment(0);

            tbl.setRowHeight(45);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
            //tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
            tbl.getColumn("Issue Meter").setCellRenderer(new ButtonRenderer(1));
            tbl.getColumn("Issue Meter").setCellEditor(new ButtonEditor(new JCheckBox(), 1));

            model.setNumRows(0);

            cnt = 0;
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getInt(3), rs.getString(4), rs.getString(2), "", rs.getInt(3)});
                cnt++;
            }
            if (cnt != 0) {
                tbl.setRowSelectionInterval(0, 0);
            }
//            if (cnt == 0) {
//                JOptionPane.showMessageDialog(this, "Either this meter no is installed or not yet been posted!");
//            } else {
//                tbl.requestFocus();
//                tbl.setRowSelectionInterval(0, 0);
//            }

            //int r = rs.getFetchSize();
            //System.out.println(cnt);
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

        public ButtonEditor(JCheckBox checkBox, int x) {
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
            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/innn.png"));
            button.setIcon(ico1);
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                showFrmSelect();
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
            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/metere.png"));
            Icon ico2 = new javax.swing.ImageIcon(getClass().getResource("/img/printer.png"));
            if (iflg == 1) {
                setIcon(ico1);
            } else if (iflg == 2) {
                setIcon(ico2);
            }

            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        lbl = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Meter Validator and Meter Issuance");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Meter Serial No", "CoopSN", "Old CoopSN", "Meter Info", "Issue Meter", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
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
            tbl.getColumnModel().getColumn(5).setResizable(false);
        }
        tbl.getColumnModel().getColumn(3).setPreferredWidth(300);
        tbl.getColumnModel().getColumn(4).setMaxWidth(100);

        txtsearch.setToolTipText("Enter Meter Serial No. Here");
        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.setPrompt("Enter Meter Serial No. and hit ENTER Button to validate ");
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

        lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl.setText("Account No.: 000000");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtsearch, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    }//GEN-LAST:event_formWindowOpened

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
////        model.setNumRows(0);
        if (txtsearch.getText().isEmpty() == true) {
//            txtmetersn.requestFocus();
//            JOptionPane.showMessageDialog(this, "Meter Serial Number is required for verification!");
            model.setNumRows(cnt);
        } else {
            populateTBL();

        }
    }//GEN-LAST:event_txtsearchActionPerformed

    private void tblKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            if (model.getRowCount() == 0) {
                txtsearch.requestFocus();
            } else {
                showFrmSelect();
            }
        }
    }//GEN-LAST:event_tblKeyPressed

    private void txtsearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsearchKeyPressed
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            tbl.requestFocus();
        }
    }//GEN-LAST:event_txtsearchKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IssueMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IssueMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IssueMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IssueMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                IssueMeter dialog = new IssueMeter(frmParent, true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl;
    private javax.swing.JTable tbl;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
