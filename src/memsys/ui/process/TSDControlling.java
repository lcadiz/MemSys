/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.process;

import java.awt.Color;
import memsys.global.DBConn.MainDBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import memsys.global.FunctionFactory;
import memsys.global.myDataenvi;
import static memsys.global.myDataenvi.rsAddConnLog;
import memsys.global.myFunctions;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author lestercadiz
 */
public class TSDControlling extends javax.swing.JInternalFrame {

    static String nowDate = FunctionFactory.GetSystemNowDateString();
    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

    public TSDControlling() {
        initComponents();

        tbl23.setCellSelectionEnabled(false);
        tbl23.setRowSelectionAllowed(true);
        tbl23.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl23.setSelectionBackground(new Color(153, 204, 255));
        tbl23.setSelectionForeground(Color.BLACK);
    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();

//        if (i == 0) {
//         
//
//            createString = "SELECT AcctNo, MembershipID, AcctName, s.statdesc, CONVERT(char(10), TransDate, 101) ,t.typedesc, ClassCode "
//                    + "FROM connTBL c, connTypeTBL t, connStatTBL s "
//                    + "WHERE transdate='" + nowDate + "' and c.status=s.status AND c.connType=t.connType AND s.status=1 AND (acctname LIKE '%" + txtsearch.getText() + "%' OR AcctNo like '" + txtsearch.getText() + "%') "
//                    + "ORDER BY AcctName";
//        } else if (i == 1) {
        String createString = "SELECT  c.AcctNo, MembershipID, AcctName, CONVERT(char(10), TransDate, 101) , ClassCode, AcctAddress,  MIN(orno), override, e.Name, i.Remarks"
                + "                    FROM connTBL c LEFT JOIN CollectionMisc m ON c.AcctNo=m.acctno "
                + "                                   LEFT JOIN connCOInfoTBL i ON m.acctno=i.acctno"
                + "                                   LEFT JOIN electricianTBL e ON i.eID=e.eID"
                + "                    WHERE Status=9 AND (acctname LIKE '%" + txtsearch.getText() + "%' OR c.AcctNo like '%" + txtsearch.getText() + "%')  "
                + "                    GROUP BY recID, AcctName,  c.AcctNo, MembershipID,  TransDate , ClassCode, AcctAddress,override, e.Name,i.Remarks "
                + "                    ORDER BY AcctName  ";
//        }

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl23.getModel();

            renderer.setHorizontalAlignment(0);

            tbl23.setRowHeight(29);
            tbl23.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tbl23.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl23.getColumnModel().getColumn(4).setCellRenderer(renderer);
            tbl23.getColumnModel().getColumn(6).setCellRenderer(renderer);
            tbl23.setColumnSelectionAllowed(false);

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

        jPanel5 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        cmdApproved1 = new javax.swing.JButton();
        cmdRefresh3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        cmdPreview3 = new javax.swing.JButton();
        cmdPreview4 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl23 = new javax.swing.JTable();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtremarks = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("TSD Check");

        jToolBar2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jToolBar2.setInheritsPopupMenu(true);
        jToolBar2.setMaximumSize(new java.awt.Dimension(500, 63));
        jToolBar2.setMinimumSize(new java.awt.Dimension(500, 63));

        cmdApproved1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApproved1.setForeground(new java.awt.Color(0, 102, 153));
        cmdApproved1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employer.png"))); // NOI18N
        cmdApproved1.setMnemonic('S');
        cmdApproved1.setText("      Send to Meter Issuance     ");
        cmdApproved1.setFocusable(false);
        cmdApproved1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdApproved1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdApproved1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdApproved1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdApproved1ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdApproved1);

        cmdRefresh3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdRefresh3.setForeground(new java.awt.Color(0, 51, 153));
        cmdRefresh3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        cmdRefresh3.setMnemonic('R');
        cmdRefresh3.setText("         Send Back to ISD        ");
        cmdRefresh3.setFocusable(false);
        cmdRefresh3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdRefresh3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdRefresh3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdRefresh3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefresh3ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdRefresh3);
        jToolBar2.add(jSeparator2);

        jToggleButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jToggleButton3.setForeground(new java.awt.Color(0, 102, 0));
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download.png"))); // NOI18N
        jToggleButton3.setMnemonic('y');
        jToggleButton3.setSelected(true);
        jToggleButton3.setText("   Today   ");
        jToggleButton3.setActionCommand("     Today     ");
        jToggleButton3.setEnabled(false);
        jToggleButton3.setFocusable(false);
        jToggleButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButton3);

        jToggleButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jToggleButton4.setForeground(new java.awt.Color(153, 0, 0));
        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/summary.png"))); // NOI18N
        jToggleButton4.setText("   Previous   ");
        jToggleButton4.setEnabled(false);
        jToggleButton4.setFocusable(false);
        jToggleButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButton4);
        jToolBar2.add(jSeparator4);

        cmdPreview3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPreview3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh.png"))); // NOI18N
        cmdPreview3.setMnemonic('w');
        cmdPreview3.setText("     Refresh     ");
        cmdPreview3.setContentAreaFilled(false);
        cmdPreview3.setFocusable(false);
        cmdPreview3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdPreview3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdPreview3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdPreview3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreview3ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdPreview3);

        cmdPreview4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPreview4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdPreview4.setMnemonic('w');
        cmdPreview4.setText("    Exit    ");
        cmdPreview4.setFocusable(false);
        cmdPreview4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdPreview4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdPreview4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdPreview4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPreview4ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdPreview4);

        tbl23.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account No", "MemberID", "Account Name", "Status", "AppDate", "Type", "Class", "Address"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tbl23.setToolTipText("");
        tbl23.getTableHeader().setReorderingAllowed(false);
        tbl23.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tbl23MouseMoved(evt);
            }
        });
        tbl23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbl23MouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tbl23);
        //set column width
        tbl23.getColumnModel().getColumn(0).setMaxWidth(80);
        tbl23.getColumnModel().getColumn(1).setMaxWidth(80);
        tbl23.getColumnModel().getColumn(4).setMaxWidth(100);
        tbl23.getColumnModel().getColumn(6).setMaxWidth(50);

        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        txtremarks.setColumns(20);
        txtremarks.setRows(5);
        txtremarks.setText("Released");
        jScrollPane4.setViewportView(txtremarks);

        jLabel1.setText("Remarks");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 1038, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1038, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdApproved1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApproved1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl23.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl23.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            if ("".equals(txtremarks.getText())) {
                JOptionPane.showMessageDialog(this, "Remarks is required!");
            } else {
                String id = tbl23.getValueAt(row, col).toString();

                int i = myFunctions.msgboxYesNo("This Record will now transfer to Meter Issuance Section!");
                switch (i) {
                    case 0:
                        int uid = ParentWindow.getUserID();
                        rsAddConnLog(Integer.parseInt(id), "Send to Meter Issuance", 9, uid, nowDate, txtremarks.getText());
                        myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 3);
                        populateTBL();
                        txtremarks.setText("");
                        JOptionPane.showMessageDialog(this, "Record has been successfully sent!");
                        break;
                    case 1:
                        return; //do nothing
                    case 2:
                        this.dispose(); //exit window
                    default:
                }
            }
        }
    }//GEN-LAST:event_cmdApproved1ActionPerformed


    private void cmdRefresh3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefresh3ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl23.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl23.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            if ("".equals(txtremarks.getText())) {
                JOptionPane.showMessageDialog(this, "Remarks is required!");
            } else {
                String id = tbl23.getValueAt(row, col).toString();
                int uid = ParentWindow.getUserID();
                int i = myFunctions.msgboxYesNo("Are you sure you want to send back this current application?");
                if (i == 0) {
                    rsAddConnLog(Integer.parseInt(id), "Send Back to CO Approval", 9, uid, nowDate, txtremarks.getText());
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 10);
                    populateTBL();
                    txtremarks.setText("");
                    JOptionPane.showMessageDialog(this, "Record has been successfully sent back!");
                } else if (i == 1) {
                    return; //do nothing
                } else if (i == 2) {
                    this.dispose(); //exit window
                    return;
                }
            }
        }
    }//GEN-LAST:event_cmdRefresh3ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
//        i = 0;
//        populateTBL();
//        txtsearch.requestFocus();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
//        i = 1;
//
//        txtsearch.requestFocus();
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void cmdPreview3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview3ActionPerformed
        populateTBL();
    }//GEN-LAST:event_cmdPreview3ActionPerformed

    private void cmdPreview4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPreview4ActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdPreview4ActionPerformed

    private void tbl23MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl23MouseMoved

    }//GEN-LAST:event_tbl23MouseMoved

    private void tbl23MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl23MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl23MouseReleased

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdApproved1;
    private javax.swing.JButton cmdPreview3;
    private javax.swing.JButton cmdPreview4;
    private javax.swing.JButton cmdRefresh3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTable tbl23;
    private javax.swing.JTextArea txtremarks;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
