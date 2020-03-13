/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package memsys.ui.oreceipt;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import static memsys.ui.process.CostingOp.chk;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LESTER
 */
public class SearchOR extends javax.swing.JInternalFrame {
    static Statement stmt;
    static String nowDate = myFunctions.getDate();
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer3 = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    public SearchOR() {
        initComponents();
        getRootPane().setDefaultButton(cmdsearch);
    }

    public void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select   Payor, PayorAddress,ORno,collDate,sum(Deposit) as Deposit,Teller,AcctNo from	"
                + "(SELECT CollectionMisc.AcctNo,COALESCE(AcctName, Payor) AS Payor,PayorAddress,ORNo, CollectionTrans.TransDate as collDate, 1 AS TableSource,CollectionMisc.COAID,sum(CollectionMisc.AmtPaid) as Deposit,Users.Fullname as Teller"
                + "		  FROM CollectionTrans INNER JOIN CollectionMisc ON CollectionTrans.TransID = CollectionMisc.TransID "
                + "								LEFT OUTER JOIN Consumer ON CollectionMisc.AcctNo = Consumer.AcctNo "
                + "								LEFT OUTER JOIN CollectionMiscName ON CollectionTrans.TransID = CollectionMiscName.TransID "
                + "								LEFT OUTER JOIN Users on Users.userID=CollectionTrans.userID "
                + "		   WHERE CollectionTrans.TransDate >= '" + txtdate.getText().trim() + "'"
                //+ "		   WHERE CollectionTrans.TransDate = '" + "11/2/2011" + "'"
                //+ "					AND CollectionMisc.COAID=31  "
                + "					AND CollectionTrans.TransID NOT IN (SELECT TransID "
                + "														FROM CollectionTransCancelled) "
                + "		   GROUP BY CollectionMisc.AcctNo,CollectionTrans.TransID, AcctName, Payor,PayorAddress, ORNo, CollectionTrans.TransDate,CollectionMisc.COAID,Users.Fullname "
                + "UNION "
                + "(SELECT ORTrans.Acctno,Payor,'' as PayorAddress, ORNo, ORTransDate AS TransDate, 2 AS TableSource,ORTransDetail.COAID,sum(ORTransDetail.AmtPaid),Users.Fullname  "
                + "		  FROM ORTrans "
                + "				INNER JOIN ORTransDetail ON ORTrans.ORTransID = ORTransDetail.ORTransID "
                + "				LEFT OUTER JOIN Users ON Users.userID=ORTrans.userID  "
                + "		   WHERE ORTransDate >= '" + txtdate.getText().trim() + "'"
                //+ "		   WHERE ORTransDate = '" + "11/2/2011" + "'"
                //+ "					AND ORTransDetail.COAID=31  "
                + "					AND ORTrans.ORTransID NOT IN (SELECT ORTransID "
                + "													FROM ORTransCancelled) "
                + "					AND ORTrans.ORTransID NOT IN (SELECT ORTransID "
                + "													FROM ORTrans "
                + "													WHERE ORTransID IN (SELECT ORTransID FROM CollectionTrans)) "
                + "		   GROUP BY ORTrans.Acctno,ORTrans.ORTransID, Payor, ORNo, ORTransDate,ORTransDetail.COAID,Users.Fullname))data "
                + "where payor like '" + "%" + txtname.getText().trim() + "%" + "'"
              
                 + " group by Acctno, Payor, PayorAddress,ORno,collDate, teller"
          + " ORDER BY Acctno, colldate, CAST(ORNo AS BIGINT)";
        //+ "		   ORDER BY TransDate, CAST(ORNo AS BIGINT) ";

        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);
            renderer2.setHorizontalAlignment(SwingConstants.RIGHT);


            tbl.setRowHeight(21);
            //jTblList.getColumnModel().getColumn(0).setCellRenderer(renderer);
//            tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
//            tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
//            tbl.getColumnModel().getColumn(4).setCellRenderer(renderer);
            

            model.setNumRows(0);

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4), rs.getString(5), rs.getString(6)});
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtname = new javax.swing.JTextField();
        txtdate = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmdsearch = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Search OR");

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Address", "OR No", "Collection Date", "Amount", "Teller"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(300);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(300);

        jLabel1.setText("Name:");

        txtname.setToolTipText("");
        txtname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnameKeyPressed(evt);
            }
        });

        txtdate.setText("01/01/2014");
        txtdate.setToolTipText("");
        txtdate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtdate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdateKeyPressed(evt);
            }
        });

        jLabel2.setText("DatePaid:");

        jLabel3.setForeground(new java.awt.Color(255, 102, 0));
        jLabel3.setText("mm/dd/yyyy");

        cmdsearch.setMnemonic('S');
        cmdsearch.setText("Search");
        cmdsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdsearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addComponent(cmdsearch))
                        .addGap(0, 211, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdsearch)
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtnameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnameKeyPressed

     
    }//GEN-LAST:event_txtnameKeyPressed

    private void txtdateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdateKeyPressed

    private void cmdsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdsearchActionPerformed
          populateTBL();
    }//GEN-LAST:event_cmdsearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdsearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl;
    private javax.swing.JTextField txtdate;
    private javax.swing.JTextField txtname;
    // End of variables declaration//GEN-END:variables
}
