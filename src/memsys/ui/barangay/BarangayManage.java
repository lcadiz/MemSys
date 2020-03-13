package memsys.ui.barangay;

import memsys.global.DBConn.MainDBConn;
import java.awt.Color;
import memsys.global.myFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class BarangayManage extends javax.swing.JInternalFrame {

    static Statement stmt;
    static int areaCode;
    public static BarangayAdd frmAdd;
    public static BarangayEdit frmEdit;
    static Statement stmtPreviousShed;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;

    public BarangayManage() {
        initComponents();
        populateArea();

        TableColumn idClmn = tbl.getColumn("id");
        idClmn.setMaxWidth(0);
        idClmn.setMinWidth(0);
        idClmn.setPreferredWidth(0);
        
         tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);
    }

    public void showFrmAdd() {
        frmAdd = new BarangayAdd(this, true);
        frmAdd.setVisible(true);
    }

    public void showFrmEdit() {
        frmEdit = new BarangayEdit(this, true);
        frmEdit.setVisible(true);
    }

    void populateArea() {

        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM areaTBL ORDER BY area_desc;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbArea.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    class Item {

        private int id;
        private String description;

        public Item(int id, String description) {
            this.id = id;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String toString() {
            return description;
        }
    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();
        String createString;

        createString = "SELECT * FROM barangayTBL WHERE areacode=" + areaCode + " ORDER BY brgy_name";
        try {
            stmtPreviousShed = conn.createStatement();
            ResultSet rs = stmtPreviousShed.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);

            tbl.setRowHeight(29);
            //tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl.setColumnSelectionAllowed(false);

            model.setNumRows(0);

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString("brgy_name")});
            }

            stmtPreviousShed.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmbArea = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        cmdAdd = new javax.swing.JButton();
        cmdUpdate = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        cmdExit = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);
        setTitle("Barangay Management");

        cmbArea.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbArea.setForeground(new java.awt.Color(102, 102, 102));
        cmbArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SELECT AREA-" }));
        cmbArea.setToolTipText("Municipal/City");
        cmbArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAreaActionPerformed(evt);
            }
        });

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Barangay Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbl);
        if (tbl.getColumnModel().getColumnCount() > 0) {
            tbl.getColumnModel().getColumn(0).setResizable(false);
            tbl.getColumnModel().getColumn(1).setResizable(false);
        }

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator2);

        cmdAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdAdd.setText("       Add       ");
        cmdAdd.setFocusable(false);
        cmdAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdAdd);

        cmdUpdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        cmdUpdate.setText("       Edit       ");
        cmdUpdate.setFocusable(false);
        cmdUpdate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdateActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdUpdate);

        cmdDelete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        cmdDelete.setText("    Remove    ");
        cmdDelete.setFocusable(false);
        cmdDelete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdDelete);
        jToolBar1.add(jSeparator1);

        cmdExit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdExit.setText("       Exit       ");
        cmdExit.setFocusable(false);
        cmdExit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdExit);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                    .addComponent(cmbArea, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(85, 85, 85)))
                .addGap(55, 55, 55))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAreaActionPerformed
        try {
            Item item = (Item) cmbArea.getSelectedItem();
            areaCode = item.getId();
            populateTBL();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbAreaActionPerformed

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        if ("--SELECT--".equals(cmbArea.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            BarangayAdd.areaCode = areaCode;
            showFrmAdd();
        }
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdateActionPerformed

        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
             String nym= tbl.getValueAt(row, 1).toString();
            BarangayEdit.bid = Integer.parseInt(id);
            BarangayEdit.nym= nym;
            showFrmEdit();
        }
    }//GEN-LAST:event_cmdUpdateActionPerformed

    public static void rsDelete(int bid) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM barangayTBL WHERE brgy_id=" + bid;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            int i = myFunctions.msgboxYesNo("Are you sure you want delete this current record?");
            if (i == 0) {
                rsDelete(Integer.parseInt(id));
                populateTBL();
                JOptionPane.showMessageDialog(this, "Record has been successfully deleted!");
            } else if (i == 1) {
                return; //do nothing
            } else if (i == 2) {
                this.dispose(); //exit window
                return;
            }

        }
    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTable tbl;
    // End of variables declaration//GEN-END:variables
}
