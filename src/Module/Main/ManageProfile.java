package Module.Main;

import memsys.global.DBConn.MainDBConn;
import Module.Main.EditProfileJuri;
import Module.Main.EditProfileNJuri;
import java.awt.Color;
import memsys.ui.devices.CurrentPhoto;
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import memsys.global.FTPFactory;
import memsys.ui.member.Conn;
import memsys.ui.sanitize.Sanitation;


public class ManageProfile extends javax.swing.JInternalFrame {

    static Statement stmtIncomingShed;
    static String nowDate = myFunctions.getDate();
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    public static EditProfileNJuri frmUpdate;
    public static EditProfileJuri frmUpdate2;
    public static CurrentPhoto frmCPhoto;
    public static Sanitation frmSanitize;
    public static Conn frmAccount;
    static Statement stmt;

    public ManageProfile() {
        initComponents();
        jTblManageProfile.setCellSelectionEnabled(false);
        jTblManageProfile.setRowSelectionAllowed(true);
        jTblManageProfile.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTblManageProfile.setSelectionBackground(new Color(153, 204, 255));
        jTblManageProfile.setSelectionForeground(Color.BLACK);
        
//                TableColumn idClmn = jTblManageProfile.getColumn("id");
//        idClmn.setMaxWidth(0);
//        idClmn.setMinWidth(0);
//        idClmn.setPreferredWidth(0);
        
    }

    void showphoto() {
       
        int row = jTblManageProfile.getSelectedRow(); //get value of selected value
        String id = jTblManageProfile.getValueAt(row, 6).toString();
        FTPFactory i = new FTPFactory();
        i.FTPViewImage(i.GetFTPPicPath() + Integer.parseInt(id) + ".jpg", captured);
       // System.out.println(GetPartID(Integer.parseInt(id)));
      //  System.out.println(Integer.parseInt(id));
   
        
    }

//    int GetPartID(int mid) {
//        int UGID = 0;
//
//        Connection conn = MainDBConn.getConnection();
//        String createString;
//        createString = "select partID FROM membersTBL WHERE memberID=" + mid;
//
//        //int rc = 0;
//        try {
//            stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(createString);
//
//            while (rs.next()) {
//                UGID = rs.getInt(1);
//            }
//
//            stmt.close();
//            conn.close();
//
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//
//        return UGID;
//    }

    public void showFrmAccounts() {
        frmAccount = new Conn(this, true);
        frmAccount.setVisible(true);
    }

    public void showFrmCPhoto() {
        frmCPhoto = new CurrentPhoto(this, true);
        frmCPhoto.setVisible(true);
    }

    public void showFrmSanitize() {
        frmSanitize = new Sanitation(this, true);
        frmSanitize.setVisible(true);
    }

    public void showFrmUpdate() {
        frmUpdate = new EditProfileNJuri(this, true);
        frmUpdate.setVisible(true);
    }

    public void showFrmUpdate2() {
        frmUpdate2 = new EditProfileJuri(this, true);
        frmUpdate2.setVisible(true);
    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT memberID, acctname, m.address , CONVERT(char(10), m.date_encoded, 101), juridical_stat, sched_date, m.partID as id"
                + " FROM membersTBL m "
                + " LEFT JOIN participantsTBL p ON m.partID = p.partID "
                + " LEFT JOIN scheduleTBL s ON p.batchID=s.batchID "
                + " WHERE acctname LIKE '" + txtsearch.getText() + "%' "
                + " ORDER BY acctname";
        //stmtIncomingShed

        try {
            stmtIncomingShed = conn.createStatement();
            ResultSet rs = stmtIncomingShed.executeQuery(createString);

            model = (DefaultTableModel) jTblManageProfile.getModel();

            renderer.setHorizontalAlignment(0);

            jTblManageProfile.setRowHeight(29);
            jTblManageProfile.getColumnModel().getColumn(0).setCellRenderer(renderer);
            jTblManageProfile.getColumnModel().getColumn(3).setCellRenderer(renderer);
            jTblManageProfile.getColumnModel().getColumn(4).setCellRenderer(renderer);

            jTblManageProfile.setColumnSelectionAllowed(false);

            model.setNumRows(0);

            while (rs.next()) {
                int x = rs.getInt(5);
                String flg = null;
                if (x == 0) {
                    flg = "NON-JURIDICAL";
                } else {
                    flg = "JURIDICAL";
                }

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), flg, rs.getDate(6), rs.getString("id")});
            }

            stmtIncomingShed.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewOpt = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        tbl = new javax.swing.JScrollPane();
        jTblManageProfile = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        cmdUpdate = new javax.swing.JButton();
        cmdUpdate1 = new javax.swing.JButton();
        cmdUpdate2 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        cmdExit1 = new javax.swing.JButton();
        captured = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Members Record Management");
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

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        tbl.setToolTipText("");
        tbl.setAutoscrolls(true);
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblMouseReleased(evt);
            }
        });
        tbl.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                tblCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        jTblManageProfile.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MemberID", "Name of Member", "Address", "Date Encoded", "Type", "PMES Date", "id"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblManageProfile.setColumnSelectionAllowed(true);
        jTblManageProfile.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                jTblManageProfileHierarchyChanged(evt);
            }
        });
        jTblManageProfile.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                jTblManageProfileAncestorMoved1(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jTblManageProfile.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
                jTblManageProfileAncestorMoved(evt);
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
            }
        });
        jTblManageProfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblManageProfileMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTblManageProfileMouseReleased(evt);
            }
        });
        jTblManageProfile.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTblManageProfileCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        jTblManageProfile.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTblManageProfilePropertyChange(evt);
            }
        });
        jTblManageProfile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTblManageProfileKeyReleased(evt);
            }
        });
        jTblManageProfile.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                jTblManageProfileVetoableChange(evt);
            }
        });
        tbl.setViewportView(jTblManageProfile);
        jTblManageProfile.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (jTblManageProfile.getColumnModel().getColumnCount() > 0) {
            jTblManageProfile.getColumnModel().getColumn(6).setResizable(false);
        }
        jTblManageProfile.getColumnModel().getColumn(0).setMaxWidth(100);
        jTblManageProfile.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTblManageProfile.getColumnModel().getColumn(2).setPreferredWidth(350);
        jTblManageProfile.getColumnModel().getColumn(3).setPreferredWidth(90);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        cmdUpdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        cmdUpdate.setMnemonic('E');
        cmdUpdate.setText("        Edit        ");
        cmdUpdate.setFocusable(false);
        cmdUpdate.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdateActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdUpdate);

        cmdUpdate1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdUpdate1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/devices.png"))); // NOI18N
        cmdUpdate1.setMnemonic('E');
        cmdUpdate1.setText("      Accounts      ");
        cmdUpdate1.setFocusable(false);
        cmdUpdate1.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdUpdate1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdUpdate1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdUpdate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdate1ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdUpdate1);

        cmdUpdate2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdUpdate2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/paysummary.png"))); // NOI18N
        cmdUpdate2.setMnemonic('E');
        cmdUpdate2.setText("      Sanitatize     ");
        cmdUpdate2.setFocusable(false);
        cmdUpdate2.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdUpdate2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdUpdate2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdUpdate2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdate2ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdUpdate2);
        jToolBar1.add(jSeparator4);

        cmdExit1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdExit1.setMnemonic('x');
        cmdExit1.setText("       Exit        ");
        cmdExit1.setFocusable(false);
        cmdExit1.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdExit1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExit1ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdExit1);

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
                    .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(captured, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 809, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 223, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbl, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(captured, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 110, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened


    }//GEN-LAST:event_formInternalFrameOpened

    private void cmdUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdateActionPerformed
        int col = 0; //set column value to 0
        int row = jTblManageProfile.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTblManageProfile.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = jTblManageProfile.getValueAt(row, col).toString();
            String juri = jTblManageProfile.getValueAt(row, 4).toString();

            if ("JURIDICAL".equals(juri)) {
                EditProfileJuri.memid = id;
                showFrmUpdate2();
            } else {
                EditProfileNJuri.memid = id;
//                JDialogEditProfile1.cflg=0;
//                JDialogEditProfile1.title="Edit Member Profile";
                showFrmUpdate();
            }

        }
    }//GEN-LAST:event_cmdUpdateActionPerformed

    private void cmdExit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExit1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExit1ActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void cmdUpdate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdate1ActionPerformed
        int col = 0; //set column value to 0
        int row = jTblManageProfile.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTblManageProfile.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = jTblManageProfile.getValueAt(row, col).toString();
            Conn.mid = Integer.parseInt(id);
            showFrmAccounts();
        }
    }//GEN-LAST:event_cmdUpdate1ActionPerformed

    private void cmdUpdate2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdate2ActionPerformed
        int col = 0; //set column value to 0
        int row = jTblManageProfile.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTblManageProfile.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = jTblManageProfile.getValueAt(row, col).toString();
            String nym = jTblManageProfile.getValueAt(row, 1).toString();
            String address = jTblManageProfile.getValueAt(row, 2).toString();
            Sanitation.nym = nym;
            Sanitation.memid = Integer.parseInt(id);
            Sanitation.address = address;
            showFrmSanitize();

        }


    }//GEN-LAST:event_cmdUpdate2ActionPerformed

    private void capturedMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseDragged

    }//GEN-LAST:event_capturedMouseDragged

    private void capturedMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseMoved

    }//GEN-LAST:event_capturedMouseMoved

    private void capturedMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMousePressed

    }//GEN-LAST:event_capturedMousePressed

    private void capturedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseReleased

    }//GEN-LAST:event_capturedMouseReleased

    private void jTblManageProfileMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblManageProfileMouseReleased
       
    }//GEN-LAST:event_jTblManageProfileMouseReleased

    private void jTblManageProfileMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblManageProfileMouseClicked
        
   
           showphoto();
        
    }//GEN-LAST:event_jTblManageProfileMouseClicked

    private void jTblManageProfileCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTblManageProfileCaretPositionChanged
           
    }//GEN-LAST:event_jTblManageProfileCaretPositionChanged

    private void jTblManageProfileAncestorMoved(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_jTblManageProfileAncestorMoved
       
    }//GEN-LAST:event_jTblManageProfileAncestorMoved

    private void jTblManageProfilePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTblManageProfilePropertyChange
         
    }//GEN-LAST:event_jTblManageProfilePropertyChange

    private void jTblManageProfileHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_jTblManageProfileHierarchyChanged
       
    }//GEN-LAST:event_jTblManageProfileHierarchyChanged

    private void jTblManageProfileAncestorMoved1(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTblManageProfileAncestorMoved1
         
    }//GEN-LAST:event_jTblManageProfileAncestorMoved1

    private void jTblManageProfileVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jTblManageProfileVetoableChange
       
    }//GEN-LAST:event_jTblManageProfileVetoableChange

    private void jTblManageProfileKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTblManageProfileKeyReleased
        showphoto();
    }//GEN-LAST:event_jTblManageProfileKeyReleased

    private void tblCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tblCaretPositionChanged

    }//GEN-LAST:event_tblCaretPositionChanged

    private void tblMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseReleased
        //showphoto();
    }//GEN-LAST:event_tblMouseReleased

    private void tblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseClicked
showphoto();
    }//GEN-LAST:event_tblMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel captured;
    private javax.swing.JButton cmdExit1;
    private javax.swing.JButton cmdUpdate;
    private javax.swing.JButton cmdUpdate1;
    private javax.swing.JButton cmdUpdate2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JTable jTblManageProfile;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JScrollPane tbl;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    private javax.swing.ButtonGroup viewOpt;
    // End of variables declaration//GEN-END:variables
}
