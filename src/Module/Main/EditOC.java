package Module.Main;

import memsys.global.DBConn.MainDBConn;
import java.awt.event.KeyEvent;
import java.sql.*;
import javax.swing.JOptionPane;

public class EditOC extends javax.swing.JDialog {

    public static OCSetup frmParent;
    static int Code;
    static Statement stmt;
    public static String cdesc, cqty, cunit, ccost, id, ccoaid, coainfo;
    public static SelectCOACode frmSelect;

    public EditOC(OCSetup parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
    }

    public void showFrmSelect() {
        frmSelect = new SelectCOACode(this, true);
        frmSelect.setVisible(true);
    }

//    public void populatecmb() {
////Populate Combo Area
//        Connection conn = myDBConn.getConnection();
//        String createString;
//        createString = "SELECT COAID,ShortDesc, COACode FROM COA ORDER BY COADesc;";
//
//        try {
//            stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(createString);
//
//            while (rs.next()) {
//                cmbacctno.addItem(new Item(rs.getInt(1), "[" + rs.getString(3) +"]-"+ rs.getString(2)));
//            }
//
//            stmt.close();
//            conn.close();
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
//        }
//
//    }
//
//    class Item {
//
//        private int id;
//        private String description;
//
//        public Item(int id, String description) {
//            this.id = id;
//            this.description = description;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public String toString() {
//            return description;
//        }
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtdesc = new javax.swing.JTextField();
        txtqty = new javax.swing.JTextField();
        txtunit = new javax.swing.JTextField();
        txtcost = new javax.swing.JTextField();
        cmdsave = new javax.swing.JButton();
        cmdcancel = new javax.swing.JButton();
        txtcoa = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Edit Charges");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        txtdesc.setToolTipText("");
        txtdesc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));

        txtqty.setToolTipText("");
        txtqty.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));

        txtunit.setToolTipText("");
        txtunit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));

        txtcost.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtcost.setToolTipText("");
        txtcost.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));

        cmdsave.setMnemonic('A');
        cmdsave.setText("Save");
        cmdsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdsaveActionPerformed(evt);
            }
        });

        cmdcancel.setMnemonic('C');
        cmdcancel.setText("Cancel");
        cmdcancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdcancelActionPerformed(evt);
            }
        });

        txtcoa.setToolTipText("");
        txtcoa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtcoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcoaActionPerformed(evt);
            }
        });
        txtcoa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtcoaFocusGained(evt);
            }
        });
        txtcoa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcoaKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 153, 0));
        jLabel8.setText("PRESS F1 TO EDIT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtdesc, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                    .addComponent(txtqty, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmdsave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdcancel))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtcoa, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtcost, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtunit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(txtdesc, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtqty, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtunit, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtcost, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(txtcoa, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdsave)
                    .addComponent(cmdcancel))
                .addGap(11, 11, 11))
        );

        jLabel6.setText("COA AcctNo:");

        jLabel3.setText("Unit:");

        jLabel5.setText("Cost:");

        jLabel1.setText("Description:");

        jLabel2.setText("Qty:");

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Membership/Images/costing.png"))); // NOI18N
        jToolBar1.add(jLabel4);

        jLabel7.setForeground(new java.awt.Color(255, 102, 0));
        jLabel7.setText("Here you can edit setup Charges");
        jToolBar1.add(jLabel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdsaveActionPerformed
        String desc = txtdesc.getText();
        String qty = txtqty.getText();
        String unit = txtunit.getText();
        String cost = txtcost.getText();
        int coaid = Code;


        if (desc.isEmpty() == true || cost.isEmpty() == true || coaid == 0) {
            JOptionPane.showMessageDialog(null, "Please fill-up all the required fields!");
            return;
        } else {
//            rsAddCharges(desc, qty, unit, cost, coaid);
            frmParent.populatetbl();
            this.dispose();
            JOptionPane.showMessageDialog(null, "New charges successfully added!");

        }
    }//GEN-LAST:event_cmdsaveActionPerformed

    private void cmdcancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdcancelActionPerformed
        this.dispose();
}//GEN-LAST:event_cmdcancelActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
//        populatecmb();
                getdata(id);
        txtdesc.setText(cdesc);
        txtqty.setText(cqty);
        txtunit.setText(cunit);
        txtcost.setText(ccost);
        txtcoa.setText(coainfo);
    }//GEN-LAST:event_formWindowOpened

    public void resetCOACode(String id) {
               Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT COACode, ShortDesc FROM COA WHERE COAID="+id;
        //stmtIncomingShed

        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);
            while (rs.next()) {
                coainfo = "[" + rs.getString(1) + "]-" + rs.getString(2);
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
        txtcoa.setText(coainfo);
    }

    private void txtcoaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcoaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F1) {
            showFrmSelect();
        }


    }//GEN-LAST:event_txtcoaKeyPressed

    private void txtcoaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtcoaFocusGained
        txtcoa.selectAll();
    }//GEN-LAST:event_txtcoaFocusGained

    private void txtcoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcoaActionPerformed

    public void getdata(String id) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT costsetID, description, qty, unit, cost, a.COAID, a.COACode, a.ShortDesc FROM costingSetupTBL c "
                + "INNER JOIN COA a ON c.COAID=a.COAID  "
                + "WHERE costsetID=" + id;
        //stmtIncomingShed

        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);
            while (rs.next()) {
                cdesc = rs.getString(2);
                cqty = rs.getString(3);
                cunit = rs.getString(4);
                ccost = rs.getString(5);
                ccoaid = rs.getString(6);
                coainfo = "[" + rs.getString(7) + "]-" + rs.getString(8);


            }


            stmt.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
//update charges

    public static void rsUpdateSched(String desc, String qty, String unit, String cost, String COAID, String cID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.costingSetupTBL "
                + "SET description='" + desc + "', qty='" + qty + "', unit='" + unit + "', cost='" + cost + "', COAID='" + COAID + "'"
                + " WHERE costsetID='" + cID + "'";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                EditOC dialog = new EditOC(frmParent, true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdcancel;
    private javax.swing.JButton cmdsave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField txtcoa;
    private javax.swing.JTextField txtcost;
    private javax.swing.JTextField txtdesc;
    private javax.swing.JTextField txtqty;
    private javax.swing.JTextField txtunit;
    // End of variables declaration//GEN-END:variables
}
