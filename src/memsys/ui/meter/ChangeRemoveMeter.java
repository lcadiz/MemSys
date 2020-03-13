/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.meter;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import memsys.global.DBConn.MainDBConn;

/**
 *
 * @author cadizlester
 */
public class ChangeRemoveMeter extends javax.swing.JDialog {

    public static MeterManage frmParent;
    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static ChangeMeter frmChangeMeter;
    public static RemoveMeter frmRemoveMeter;

    public ChangeRemoveMeter(MeterManage parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);
    }

    public void showFrmRemoveMeter() {
        frmRemoveMeter = new RemoveMeter(this, true);
        frmRemoveMeter.setVisible(true);
    }

    public void showFrmChangeMeter() {
        frmChangeMeter = new ChangeMeter(this, true);
        frmChangeMeter.setVisible(true);
    }

    void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        createString = "SELECT * FROM Consumer c INNER JOIN ConsumerMeter cm ON c.AcctNo=cm.AcctNo WHERE AcctName LIKE '%" + txtsearch.getText() + "%' OR MeterSN LIKE '%"+txtsearch.getText() +"%' OR c.AcctNo LIKE '%"+txtsearch.getText() +"%'";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);

            tbl.setRowHeight(29);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            //tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
            model.setNumRows(0);

            int cnt = 0;
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("AcctNo"), rs.getString("AcctName"), rs.getString("AcctAddress"), rs.getString("MeterSN")});
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        cmdchange = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        cmdadd1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Change/Remove Meter");

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AccountNo", "Account Name", "Address", "MeterSN"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl);
        tbl.getColumnModel().getColumn(0).setMaxWidth(100);

        cmdchange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/metere.png"))); // NOI18N
        cmdchange.setMnemonic('C');
        cmdchange.setText("Change");
        cmdchange.setToolTipText("Create new schedule");
        cmdchange.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdchange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdchangeActionPerformed(evt);
            }
        });
        cmdchange.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdchangeKeyPressed(evt);
            }
        });

        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdExit.setMnemonic('C');
        cmdExit.setText("Exit");
        cmdExit.setToolTipText("Cancel and exit window");
        cmdExit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        cmdadd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove_1.png"))); // NOI18N
        cmdadd1.setMnemonic('R');
        cmdadd1.setText("Remove");
        cmdadd1.setToolTipText("Create new schedule");
        cmdadd1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdadd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdadd1ActionPerformed(evt);
            }
        });
        cmdadd1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdadd1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmdExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdchange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdadd1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdchange)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdadd1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdExit)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdchangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdchangeActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            ChangeMeter.acctno = Integer.parseInt(id);
            showFrmChangeMeter();
        }
    }//GEN-LAST:event_cmdchangeActionPerformed

    private void cmdchangeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdchangeKeyPressed
        //     add();
    }//GEN-LAST:event_cmdchangeKeyPressed

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdadd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdadd1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            RemoveMeter.acctno = Integer.parseInt(id);
            showFrmRemoveMeter();
        }
    }//GEN-LAST:event_cmdadd1ActionPerformed

    private void cmdadd1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdadd1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmdadd1KeyPressed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChangeRemoveMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangeRemoveMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangeRemoveMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangeRemoveMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChangeRemoveMeter dialog = new ChangeRemoveMeter(frmParent, true);
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
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdadd1;
    private javax.swing.JButton cmdchange;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
