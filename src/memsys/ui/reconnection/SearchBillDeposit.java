/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.reconnection;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import memsys.global.DBConn.MainDBConn;
import memsys.ui.user.Group;

/**
 *
 * @author LESTER JP CADIZ
 */
public class SearchBillDeposit extends javax.swing.JDialog {

    public static CreateNewRequest frmParent;
    public static String acctnym;
    static Statement stmt;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer3 = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    public static boolean chk;
    protected CheckBoxHeader rendererComponent;
    protected boolean mousePressed = true;

    public SearchBillDeposit(CreateNewRequest parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        txtsearch.setText(acctnym);

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

        TableColumn tc = tbl.getColumnModel().getColumn(0);
        tc.setCellEditor(tbl.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(tbl.getDefaultRenderer(Boolean.class));
        tc.setHeaderRenderer(new CheckBoxHeader(new MyItemListener()));
        rendererComponent.setSelected(false);
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

    public void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString;
//        createString = "select   collDate, ORno, sum(Deposit) as Deposit, Payor  from	"
//                + "(SELECT CollectionMisc.AcctNo,COALESCE(AcctName, Payor) AS Payor, PayorAddress , ORNo , CollectionTrans.TransDate as collDate, 1 AS TableSource,CollectionMisc.COAID,sum(CollectionMisc.AmtPaid) as Deposit,Users.Fullname as Teller"
//                + "		  FROM CollectionTrans INNER JOIN CollectionMisc ON CollectionTrans.TransID = CollectionMisc.TransID "
//                + "								LEFT OUTER JOIN Consumer ON CollectionMisc.AcctNo = Consumer.AcctNo "
//                + "								LEFT OUTER JOIN CollectionMiscName ON CollectionTrans.TransID = CollectionMiscName.TransID "
//                + "								LEFT OUTER JOIN Users on Users.userID=CollectionTrans.userID "
//                + "		   WHERE CollectionTrans.TransDate >= '01/01/2004' "
//                //+ "		   WHERE CollectionTrans.TransDate = '" + "11/2/2011" + "'"
//                + "					AND CollectionMisc.COAID=31  "
//                + "					AND CollectionTrans.TransID NOT IN (SELECT TransID "
//                + "														FROM CollectionTransCancelled) "
//                + "		   GROUP BY CollectionMisc.AcctNo,CollectionTrans.TransID, AcctName, Payor,PayorAddress, ORNo, CollectionTrans.TransDate,CollectionMisc.COAID,Users.Fullname "
//                + "UNION "
//                + "(SELECT ORTrans.Acctno,Payor,'' as PayorAddress, ORNo, ORTransDate AS TransDate, 2 AS TableSource,ORTransDetail.COAID,sum(ORTransDetail.AmtPaid),Users.Fullname  "
//                + "		  FROM ORTrans "
//                + "				INNER JOIN ORTransDetail ON ORTrans.ORTransID = ORTransDetail.ORTransID "
//                + "				LEFT OUTER JOIN Users ON Users.userID=ORTrans.userID  "
//                + "		   WHERE ORTransDate >= '01/01/2004' "
//                //+ "		   WHERE ORTransDate = '" + "11/2/2011" + "'"
//                + "					AND ORTransDetail.COAID=31  "
//                + "					AND ORTrans.ORTransID NOT IN (SELECT ORTransID "
//                + "													FROM ORTransCancelled) "
//                + "					AND ORTrans.ORTransID NOT IN (SELECT ORTransID "
//                + "													FROM ORTrans "
//                + "													WHERE ORTransID IN (SELECT ORTransID FROM CollectionTrans)) "
//                + "		   GROUP BY ORTrans.Acctno,ORTrans.ORTransID, Payor, ORNo, ORTransDate,ORTransDetail.COAID,Users.Fullname))data "
//                + "where payor like '" + "%" + txtsearch.getText().trim() + "%" + "'"
//                + " group by Acctno, Payor, PayorAddress,ORno,collDate, teller"
//                + " ORDER BY Acctno, colldate, CAST(ORNo AS BIGINT)";
//        //+ "		   ORDER BY TransDate, CAST(ORNo AS BIGINT) ";

        
        createString = "SELECT max(CollectionMisc.TransID) as transID,CollectionMisc.AcctNo,COALESCE(AcctName, Payor) AS Payor,PayorAddress,ORNo, CollectionTrans.TransDate, CollectionMisc.COAID,sum(CollectionMisc.AmtPaid) as AmtPaid,Users.Fullname,'fromMisc' as source  " +
                        "                  FROM CollectionTrans INNER JOIN CollectionMisc ON CollectionTrans.TransID = CollectionMisc.TransID " +
                        "                                        LEFT OUTER JOIN Consumer ON CollectionMisc.AcctNo = Consumer.AcctNo " +
                        "                                        LEFT OUTER JOIN CollectionMiscName ON CollectionTrans.TransID = CollectionMiscName.TransID " +
                        "                                        LEFT OUTER JOIN Users on Users.userID=CollectionTrans.userID " +
                        "                   WHERE CollectionMiscName.Payor like '%"+txtsearch.getText()+"%' or Consumer.AcctName like '%"+txtsearch.getText()+"%' and CollectionMisc.COAID=31 " +
                        "                            AND CollectionTrans.TransID NOT IN (SELECT TransID " +
                        "                                                                FROM CollectionTransCancelled) " +
                        "            GROUP BY CollectionMisc.AcctNo,CollectionTrans.TransID, AcctName, Payor,PayorAddress, ORNo, CollectionTrans.TransDate,CollectionMisc.COAID,Users.Fullname " +
                        " UNION " +
                        " SELECT max(cb.TransID) as transID,c.acctno,c.AcctName,c.AcctAddress,cb.ORNo,ct.TransDate,cc.COAID,sum(cc.ChargeAmt) as AmtPaid,u.FullName,'AddToBill' as source  " +
                        "                    FROM CollectionCharge  cc " +
                        "                                        inner join CollectionBill cb ON cb.CollectionID=cc.CollectionID " +
                        "                                        inner join CollectionTrans ct ON ct.TransID=cb.TransID " +
                        "                                        inner join Bill b ON cb.BillNo=b.billno " +
                        "                                        inner join Consumer c ON c.acctno=b.AcctNo " +
                        "                                        inner join Users u ON u.UserID=ct.UserID " +
                        "                    where RateID=64 and c.AcctName like '%"+txtsearch.getText()+"%' and ct.TransID NOT IN (select transid from CollectionTransCancelled) " +
                        "            GROUP by c.acctno,c.AcctName,c.AcctAddress,cb.ORNo,ct.TransDate,cc.COAID,u.FullName " +
                        " UNION " +
                        " SELECT max(ORTrans.ORTransID) as transID,ORTrans.Acctno,Payor,'' as PayorAddress, ORNo, ORTransDate AS TransDate, ORTransDetail.COAID,sum(ORTransDetail.AmtPaid) as AmtPaid,Users.Fullname,'fromCashier' as source  " +
                        "                  FROM ORTrans " +
                        "                        INNER JOIN ORTransDetail ON ORTrans.ORTransID = ORTransDetail.ORTransID " +
                        "                        LEFT OUTER JOIN Users ON Users.userID=ORTrans.userID " +
                        "                   WHERE     ORTransDetail.COAID=31 " +
                        "                            AND ORTrans.Payor like '%"+txtsearch.getText()+"%' " +
                        "                            AND ORTrans.ORTransID NOT IN (SELECT ORTransID " +
                        "                                                            FROM ORTransCancelled) " +
                        "                            AND ORTrans.ORTransID NOT IN (SELECT ORTransID " +
                        "                                                            FROM ORTrans " +
                        "                                                            WHERE ORTransID IN (SELECT ORTransID FROM CollectionTrans)) " +
                        "                   GROUP BY ORTrans.Acctno,ORTrans.ORTransID, Payor, ORNo, ORTransDate,ORTransDetail.COAID,Users.Fullname ";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);
            renderer2.setHorizontalAlignment(SwingConstants.RIGHT);

            tbl.setRowHeight(29);
            //jTblList.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(4).setCellRenderer(renderer2);
//            tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
//            tbl.getColumnModel().getColumn(4).setCellRenderer(renderer);

            model.setNumRows(0);
            int cntr = 0;
            while (rs.next()) {
                cntr++;
                model.addRow(new Object[]{chk, rs.getDate(6), rs.getString(5), rs.getString(3), rs.getString(8)});
            }

            if (cntr == 0) {
                JOptionPane.showMessageDialog(this, "No result found!");
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

        jXSearchField1 = new org.jdesktop.swingx.JXSearchField();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        cmdCreate = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();

        jXSearchField1.setText("jXSearchField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Search for Bill Deposit");

        txtsearch.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.setSearchMode(org.jdesktop.swingx.JXSearchField.SearchMode.REGULAR);
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "TransDate", "OR No.", "Payor", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl);
        tbl.getColumnModel().getColumn(3).setPreferredWidth(300);
        tbl.getColumnModel().getColumn(0).setMaxWidth(20);

        cmdCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdCreate.setMnemonic('T');
        cmdCreate.setText("Tag");
        cmdCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCreateActionPerformed(evt);
            }
        });

        cmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdCancel.setMnemonic('C');
        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmdCreate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdCancel)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdCreate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmdCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void cmdCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCreateActionPerformed
        int col =2;
        int cntr = 0;
        String ors="";
        int rows = tbl.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            String or = (String) tbl.getModel().getValueAt(i, col);
             String dte = tbl.getModel().getValueAt(i, 1).toString();
            String selTF = tbl.getValueAt(i, 0).toString();
            if ("true".equals(selTF)) {
                cntr++;
                if (cntr==1){
                    ors=or + "/"+dte;
                }else{ 
                    ors= ors+ ", "+or+ "/"+dte;
                }
            }
        }

        if (cntr == 0) {
            JOptionPane.showMessageDialog(this, "No record is selected! Please select a record from the table!");
        }else{
            frmParent.TagBillDeposit(ors);
            this.dispose();
        }
    }//GEN-LAST:event_cmdCreateActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

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
            java.util.logging.Logger.getLogger(SearchBillDeposit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SearchBillDeposit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SearchBillDeposit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SearchBillDeposit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SearchBillDeposit dialog = new SearchBillDeposit(frmParent, true);
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
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdCreate;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXSearchField jXSearchField1;
    private javax.swing.JTable tbl;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
