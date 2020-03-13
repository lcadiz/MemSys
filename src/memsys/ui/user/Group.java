package memsys.ui.user;

import memsys.global.DBConn.MainDBConn;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.*;

public class Group extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer3 = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    public static boolean chk;
    protected CheckBoxHeader rendererComponent;
    protected boolean mousePressed = true;
    public static AddGroup frmAddGroup;
    public static RenameGroup frmRenameGroup;

    public Group() {
        initComponents();

        TableColumn tc = tbl.getColumnModel().getColumn(0);
        tc.setCellEditor(tbl.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(tbl.getDefaultRenderer(Boolean.class));
        tc.setHeaderRenderer(new CheckBoxHeader(new MyItemListener()));
        rendererComponent.setSelected(false);
    }

    public void showFrmAddGroup() {
        frmAddGroup = new AddGroup(this, true);
        frmAddGroup.setVisible(true);
    }

    public void showFrmRenameGroup() {
        frmRenameGroup = new RenameGroup(this, true);
        frmRenameGroup.setVisible(true);
    }

    class MyItemListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            Object source = e.getSource();
            if (source instanceof AbstractButton == false) {
                return;
            }
            boolean checked = e.getStateChange() == ItemEvent.SELECTED;
            for (int x = 0, y = tbl.getRowCount(); x < y; x++) {
                tbl.setValueAt(new Boolean(checked), x, 0);
            }
        }
    }

    ////////////////////////////////////////
    class CheckBoxHeader extends JCheckBox
            implements TableCellRenderer, MouseListener {

        protected int column;

        public CheckBoxHeader(ItemListener itemListener) {
            rendererComponent = this;
            rendererComponent.addItemListener(itemListener);
        }

        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (table != null) {
                JTableHeader header = table.getTableHeader();
                if (header != null) {
                    rendererComponent.setForeground(header.getForeground());
                    rendererComponent.setBackground(header.getBackground());
                    rendererComponent.setFont(header.getFont());


                    header.addMouseListener(rendererComponent);
                }
            }
            setColumn(column);
            rendererComponent.setText("Check All");
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            return rendererComponent;
        }

        protected void setColumn(int column) {
            this.column = column;
        }

        public int getColumn() {
            return column;
        }

        protected void handleClickEvent(MouseEvent e) {

            if (mousePressed) {
                mousePressed = false;
                JTableHeader header = (JTableHeader) (e.getSource());
                JTable tableView = header.getTable();
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);

                if (viewColumn == this.column && e.getClickCount() == 1 && column != -1) {
                    doClick();
                }
            }
        }

        public void mouseClicked(MouseEvent e) {
            handleClickEvent(e);
            ((Component) e.getSource()).repaint();
        }

        public void mousePressed(MouseEvent e) {
            mousePressed = true;
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    ////////////////////////////////////////
    public void populateTBL(String wat) {


        Connection conn = MainDBConn.getConnection();
        String createString;

        createString = "SELECT p.prevID, prevDesc, mh.Caption, mh.MHID "
                + "FROM menuItemTBL mi, menuHeaderTBL mh, privilegesTBL p "
                + "WHERE mi.MHID=mh.MHID AND p.prevID=mi.prevID AND mh.Caption LIKE '%" + wat + "%' "
                + "GROUP BY  mh.MHID,p.prevID, prevDesc, mh.Caption "
                + "ORDER BY mh.Caption, p.prevID";

//        createString = "SELECT up.prevID, prevDesc, mh.Caption "
//                + "FROM userGroupsAssignTBL ua,userGroupPrevTBL up, menuItemTBL mi, menuHeaderTBL mh, privilegesTBL p "
//                + "WHERE ua.UGID=up.UGID AND up.prevID=mi.prevID AND mi.MHID=mh.MHID AND p.prevID=up.prevID AND mh.Caption LIKE '%" + wat + "%' "
//                + " "
//                + "GROUP BY up.prevID, prevDesc, mh.Caption "
//                + "ORDER BY mh.Caption, prevDesc";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);
            renderer2.setHorizontalAlignment(SwingConstants.RIGHT);


            tbl.setRowHeight(21);
            //jTblList.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);

            model.setNumRows(0);

            while (rs.next()) {
                model.addRow(new Object[]{chk, rs.getString(1), rs.getString(2), rs.getString(3)});
            }



            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void populatecombo() {
        //Populate Combo Area
        cmbArea.addItem("ALL AREAS");
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT MHID, Caption FROM menuHeaderTBL";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbArea.addItem(new Item2(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    void populateLst() {
        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT UGID, groupName FROM userGroupTBL ORDER BY groupName";

        try {
            DefaultListModel model = new DefaultListModel();
            this.lstgroup.setModel(model);

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);


            while (rs.next()) {
                //String desc = null;
                //desc = "[" + rs.getString(2) + "]" + "-" + rs.getString(4).trim() + "/" + rs.getString(3);

                model.addElement(new Item(rs.getInt(1), rs.getString(2)));


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

    class Item2 {

        private int id;
        private String description;

        public Item2(int id, String description) {
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

    public void setPrevModules(int ugid) {
        int col = 1;



        int rows = tbl.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            String value = (String) tbl.getModel().getValueAt(i, col);
            //String selTF = tbl.getValueAt(i, 0).toString();

            boolean isAllowed = isAllowed(ugid, Integer.parseInt(value));

            if (isAllowed == true) {
                tbl.setValueAt(true, i, 0);
            } else if (isAllowed == false) {
                tbl.setValueAt(false, i, 0);
            }
        }
    }

    public void saveChanges(int ugid) {
        int col = 1;



        int rows = tbl.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            String value = (String) tbl.getModel().getValueAt(i, col);
            String selTF = tbl.getValueAt(i, 0).toString();

            boolean isAllowed = isAllowed(ugid, Integer.parseInt(value));

            if (isAllowed == true) {
                if ("false".equals(selTF)) {
                    DeletePrev(ugid, Integer.parseInt(value));
                }
            } else if (isAllowed == false) {
                if ("true".equals(selTF)) {
                    AddPrev(ugid, Integer.parseInt(value));
                }
            }
        }
    }

    boolean isAllowed(int ugid, int previd) {
        boolean isallow = false;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT prevID FROM userGroupPrevTBL WHERE UGID=" + ugid + " AND prevID=" + previd;
        //stmtIncomingShed

        int rfound = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                rfound++;
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }


        if (rfound > 0) {
            isallow = true;
        }

        return isallow;
    }

    public static void AddPrev(int ugid, int previd) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO userGroupPrevTBL(UGID, prevID) "
                + "VALUES (" + ugid + "," + previd + ")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void DeletePrev(int ugid, int previd) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM userGroupPrevTBL WHERE UGID=" + ugid + " AND prevID=" + previd;


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        lstgroup = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        cmdexit = new javax.swing.JButton();
        cmbArea = new javax.swing.JComboBox();
        cmdsave = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("User Groups and Module Privileges");
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

        lstgroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstgroupMouseClicked(evt);
            }
        });
        lstgroup.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstgroupValueChanged(evt);
            }
        });
        lstgroup.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                lstgroupHierarchyChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstgroup);

        jLabel1.setForeground(new java.awt.Color(255, 102, 0));
        jLabel1.setText("List of User Groups");

        jLabel2.setText("Group Module Privileges Setup");

        jButton1.setMnemonic('A');
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setMnemonic('R');
        jButton2.setText("Rename");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setMnemonic('o');
        jButton3.setText("Remove");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "ModuleID", "Module Privilege Description", "Area"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl.setToolTipText("");
        tbl.setEnabled(false);
        tbl.getTableHeader().setReorderingAllowed(false);
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblMouseReleased(evt);
            }
        });
        tbl.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblMouseMoved(evt);
            }
        });
        tbl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);
        //set column width
        tbl.getColumnModel().getColumn(0).setMaxWidth(18);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(300);
        tbl.getColumnModel().getColumn(1).setMaxWidth(80);
        tbl.getColumnModel().getColumn(3).setPreferredWidth(120);

        cmdexit.setMnemonic('x');
        cmdexit.setText("Exit");
        cmdexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexitActionPerformed(evt);
            }
        });

        cmbArea.setEnabled(false);
        cmbArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAreaActionPerformed(evt);
            }
        });

        cmdsave.setForeground(new java.awt.Color(0, 153, 0));
        cmdsave.setMnemonic('S');
        cmdsave.setText("Save Changes");
        cmdsave.setEnabled(false);
        cmdsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdsaveActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(0, 51, 153));
        jLabel3.setText("Note: Check items are the modules that selected user is allowed to access");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmdsave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdexit)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(cmdexit)
                    .addComponent(cmdsave))
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        populateLst();
        populatecombo();

        if (cmbArea.getSelectedItem() == "ALL AREAS") {
            populateTBL("");
        } else {
            populateTBL(String.valueOf(cmbArea.getSelectedItem()));
        }
        //chk = true;
    }//GEN-LAST:event_formInternalFrameOpened

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseClicked
   }//GEN-LAST:event_tblMouseClicked

    private void tblMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMousePressed
   }//GEN-LAST:event_tblMousePressed

    private void tblMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseReleased
//        int col = 0; //set column value to 0
//        int row = jTblCosting.getSelectedRow(); //get value of selected value
//
//        String selected = jTblCosting.getValueAt(row, col).toString();
//        String selcoaid = jTblCosting.getValueAt(row, 2).toString();
//
//
//
//
//        if ("true".equals(selected) && "54".equals(selcoaid)) {
//            edflag = 0;
//        } else if ("false".equals(selected) && "54".equals(selcoaid)) {
//            int i = myFunctions.msgboxYesNo("Are you sure you want uncheck this charges? NOTE: All computation in Energy Deposit TAB will be deleted!");
//            if (i == 0) {
//                edflag = 1;
//                jTblCosting.setValueAt("0.00", row, 6);
//                jTblCosting.setValueAt("0.00", row, 7);
//                clrED();
//
//            } else if (i == 1) {
//                jTblCosting.setValueAt(true, row, 0);
//                return; //do nothing
//            } else if (i == 2) {
//                jTblCosting.setValueAt(true, row, 0);
//                return;
//            }
//        }
//
//        refreshTotal();
        //JOptionPane.showMessageDialog(null, selected+selcoaid);
    }//GEN-LAST:event_tblMouseReleased

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved
   }//GEN-LAST:event_tblMouseMoved

    private void tblKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_E) {
//            edit();
//        }
    }//GEN-LAST:event_tblKeyPressed

    private void cmdexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdexitActionPerformed

    private void cmbAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAreaActionPerformed
        if (cmbArea.getSelectedItem() == "ALL AREAS") {
            populateTBL("");
        } else {
            populateTBL(String.valueOf(cmbArea.getSelectedItem()));
        }
        LoadAllowedModules();
    }//GEN-LAST:event_cmbAreaActionPerformed

    private void lstgroupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstgroupMouseClicked
        tbl.setEnabled(true);
        cmbArea.setEnabled(true);
        cmdsave.setEnabled(true);
        LoadAllowedModules();
    }//GEN-LAST:event_lstgroupMouseClicked

    private void lstgroupHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_lstgroupHierarchyChanged
    }//GEN-LAST:event_lstgroupHierarchyChanged

    private void lstgroupValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstgroupValueChanged
        LoadAllowedModules();
    }//GEN-LAST:event_lstgroupValueChanged

    private void cmdsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdsaveActionPerformed


        try {
            Item item = (Item) lstgroup.getSelectedValue();
            int id = item.getId();
            //JOptionPane.showMessageDialog(null, id);
            saveChanges(id);
            JOptionPane.showMessageDialog(null, "Changes has been successfully saved!");

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "No User Selected!");
            // return;
        }
    }//GEN-LAST:event_cmdsaveActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        showFrmAddGroup();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            Item item = (Item) lstgroup.getSelectedValue();
            int id = item.getId();
            RenameGroup.gid=id;
            showFrmRenameGroup();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No User Group Selected!");
            return;
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    void LoadAllowedModules() {
        try {
            Item item = (Item) lstgroup.getSelectedValue();
            int id = item.getId();
            //JOptionPane.showMessageDialog(null, id);
            setPrevModules(id);

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "No User Selected!");
            // return;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JButton cmdexit;
    private javax.swing.JButton cmdsave;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList lstgroup;
    private static javax.swing.JTable tbl;
    // End of variables declaration//GEN-END:variables
}
