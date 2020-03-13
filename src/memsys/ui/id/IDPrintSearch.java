package memsys.ui.id;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.*;

public class IDPrintSearch extends javax.swing.JDialog {

    public static IDIssuance frmParent;
    static Statement stmt;
    static String nowDate = myFunctions.getDate();
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer3 = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    public static boolean chk;
    protected CheckBoxHeader rendererComponent;
    protected boolean mousePressed = true;
    static String Name, MemDate, MemType, Address;

    public IDPrintSearch(IDIssuance parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        getRootPane().setDefaultButton(cmdSelect);

        TableColumn tc = jTblList.getColumnModel().getColumn(0);
        tc.setCellEditor(jTblList.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(jTblList.getDefaultRenderer(Boolean.class));
        tc.setHeaderRenderer(new CheckBoxHeader(new MyItemListener()));
        rendererComponent.setSelected(true);

    }

    class MyItemListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            Object source = e.getSource();
            if (source instanceof AbstractButton == false) {
                return;
            }
            boolean checked = e.getStateChange() == ItemEvent.SELECTED;
            for (int x = 0, y = jTblList.getRowCount(); x < y; x++) {
                jTblList.setValueAt(new Boolean(checked), x, 0);
            }
        }
    }

    public void populateTBL() {


        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT m.memberID, m.acctname, p.batchID, p.partID "
                + "FROM membersTBL m INNER JOIN participantsTBL p ON m.partID=p.partID "
                + "WHERE (p.batchID LIKE'%" + txtsearch.getText() + "%' OR m.acctname LIKE '%" + txtsearch.getText() + "%') AND memberID NOT IN (SELECT memberID FROM IDPrintedTBL)"
                + "ORDER BY m.memberID";
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) jTblList.getModel();

            renderer.setHorizontalAlignment(0);
            renderer2.setHorizontalAlignment(SwingConstants.RIGHT);


            jTblList.setRowHeight(21);
            //jTblList.getColumnModel().getColumn(0).setCellRenderer(renderer);
            jTblList.getColumnModel().getColumn(1).setCellRenderer(renderer);
            jTblList.getColumnModel().getColumn(3).setCellRenderer(renderer);
            jTblList.getColumnModel().getColumn(4).setCellRenderer(renderer);
            

            model.setNumRows(0);

            while (rs.next()) {
                model.addRow(new Object[]{chk, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
            }



            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtsearch = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmdSelect = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblList = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Member Search");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        txtsearch.setToolTipText("");
        txtsearch.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
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

        jLabel1.setText("List of Coop Members");

        jLabel2.setDisplayedMnemonic('S');
        jLabel2.setText("Search Here:");

        cmdSelect.setMnemonic('A');
        cmdSelect.setText("ADD TO LIST");
        cmdSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelectActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTblList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "MemberID", "Name of Member", "Batch No.", "ParticipantID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblList.setToolTipText("");
        jTblList.getTableHeader().setReorderingAllowed(false);
        jTblList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTblListMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTblListMouseReleased(evt);
            }
        });
        jTblList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTblListMouseMoved(evt);
            }
        });
        jTblList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTblListKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTblList);
        if (jTblList.getColumnModel().getColumnCount() > 0) {
            jTblList.getColumnModel().getColumn(1).setResizable(false);
            jTblList.getColumnModel().getColumn(3).setResizable(false);
        }
        //set column width
        jTblList.getColumnModel().getColumn(0).setMaxWidth(18);
        jTblList.getColumnModel().getColumn(1).setMaxWidth(80);
        jTblList.getColumnModel().getColumn(3).setMaxWidth(80);
        jTblList.getColumnModel().getColumn(4).setMaxWidth(80);

        jLabel3.setDisplayedMnemonic('S');
        jLabel3.setForeground(new java.awt.Color(255, 153, 0));
        jLabel3.setText("(Note: Member's Name OR PMES Batch No.)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtsearch, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                    .addComponent(cmdSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(372, 372, 372)
                .addComponent(cmdSelect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(8, 8, 8)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(42, 42, 42)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(102, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //get Data 
    void getData(int mID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT CONVERT(char(10), date_encoded, 101), address, type  FROM membersTBL WHERE memberID=" + mID;
        //stmtIncomingShed 

        
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                MemDate= rs.getString(1);
                Address= rs.getString(2);
                MemType= rs.getString(3);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    
    
    

    private void cmdSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSelectActionPerformed
        int rc = jTblList.getRowCount();
        int cntr = 0;
        int finalrc = 0;

        //Determine the no. of record selected by the user
        while (cntr < rc) {
            String selTF = jTblList.getValueAt(cntr, 0).toString();
            //System.out.println(selTF);

            if ("true".equals(selTF)) {
                finalrc++;
                //System.out.println(finalrc);
            }
            cntr++;
        }
        //System.out.println(finalrc);

        //trap if the user select no record
        if (finalrc == 0) {
            JOptionPane.showMessageDialog(null, "No Record Selected!");
        } else {
            AddToList();
        }

        this.dispose();

    }//GEN-LAST:event_cmdSelectActionPerformed

    
    
    
    
    
    void AddToList() {
        int rc = jTblList.getRowCount();
        int cntr = 0;

        Vector err = new Vector();

        while (cntr < rc) {
            String selTF = jTblList.getValueAt(cntr, 0).toString();
            String mID = jTblList.getValueAt(cntr, 1).toString();
            String nym = jTblList.getValueAt(cntr, 2).toString();
            String pid = jTblList.getValueAt(cntr, 4).toString();

            if ("true".equals(selTF)) {
                boolean found = frmParent.CheckExistance(Integer.parseInt(mID));
                
                getData(Integer.parseInt(mID));
                if (found == false) {
                    frmParent.AddList(mID, nym, MemDate, Address, MemType, pid);
                }else{
                    err.addElement(mID);
                }
            }

            cntr++;
        }

        Enumeration vEnum = err.elements();
        String memsid="";
        while (vEnum.hasMoreElements()) {
                memsid = memsid + vEnum.nextElement() + ", ";
            }
        
        if (err.size() > 0) {
            
            JOptionPane.showMessageDialog(null, "Cannot add the following MembershipID/s "+ memsid + " because it is already in the list! ");
        }

        //System.out.println(err.size());

    }


    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened




        txtsearch.requestFocus();
        chk = true;
        populateTBL();

    }//GEN-LAST:event_formWindowOpened

    private void jTblListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblListMouseClicked
   }//GEN-LAST:event_jTblListMouseClicked

    private void jTblListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblListMousePressed
   }//GEN-LAST:event_jTblListMousePressed

    private void jTblListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblListMouseReleased
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
    }//GEN-LAST:event_jTblListMouseReleased

    private void jTblListMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblListMouseMoved
   }//GEN-LAST:event_jTblListMouseMoved

    private void jTblListKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTblListKeyPressed
//        if (evt.getKeyCode() == KeyEvent.VK_E) {
//            edit();
//        }
    }//GEN-LAST:event_jTblListKeyPressed

    private void txtsearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsearchKeyPressed

        populateTBL();
    }//GEN-LAST:event_txtsearchKeyPressed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtsearchActionPerformed

    public static void main(String args[]) {

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
            java.util.logging.Logger.getLogger(IDPrintSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IDPrintSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IDPrintSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IDPrintSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                IDPrintSearch dialog = new IDPrintSearch(frmParent, true);
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
    private javax.swing.JButton cmdSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTable jTblList;
    private javax.swing.JTextField txtsearch;
    // End of variables declaration//GEN-END:variables
}
