package memsys.ui.process;

import memsys.ui.process.CostingOp;
import memsys.global.myFunctions;
import javax.swing.JOptionPane;

public class EditCharges extends javax.swing.JDialog {

        public static CostingOp frmParent;
        public static String qty, cost, descrip;
        public static int rsel;

        public EditCharges(CostingOp parent, boolean modal) {
                this.frmParent = parent;
                this.setModal(modal);
                initComponents();
                setLocationRelativeTo(this);
                getRootPane().setDefaultButton(cmdSave);
        }

        @SuppressWarnings("unchecked")
          // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
          private void initComponents() {

                    jButton1 = new javax.swing.JButton();
                    jLabel1 = new javax.swing.JLabel();
                    jLabel2 = new javax.swing.JLabel();
                    jLabel3 = new javax.swing.JLabel();
                    cmdcancel = new javax.swing.JButton();
                    cmdSave = new javax.swing.JButton();
                    txtcost = new javax.swing.JFormattedTextField();
                    txtqty = new javax.swing.JFormattedTextField();
                    txtdesc = new javax.swing.JTextField();

                    jButton1.setText("jButton1");

                    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                    setTitle("Edit Charges");
                    addWindowListener(new java.awt.event.WindowAdapter() {
                              public void windowOpened(java.awt.event.WindowEvent evt) {
                                        formWindowOpened(evt);
                              }
                    });

                    jLabel1.setText("QTY:");

                    jLabel2.setText("Cost:");

                    jLabel3.setText("Desciption:");

                    cmdcancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit_2.png"))); // NOI18N
                    cmdcancel.setMnemonic('C');
                    cmdcancel.setText("Cancel");
                    cmdcancel.addActionListener(new java.awt.event.ActionListener() {
                              public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        cmdcancelActionPerformed(evt);
                              }
                    });

                    cmdSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
                    cmdSave.setMnemonic('S');
                    cmdSave.setText("Save");
                    cmdSave.addActionListener(new java.awt.event.ActionListener() {
                              public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        cmdSaveActionPerformed(evt);
                              }
                    });

                    txtcost.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
                    txtcost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
                    txtcost.addFocusListener(new java.awt.event.FocusAdapter() {
                              public void focusGained(java.awt.event.FocusEvent evt) {
                                        txtcostFocusGained(evt);
                              }
                    });

                    txtqty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
                    txtqty.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
                    txtqty.addFocusListener(new java.awt.event.FocusAdapter() {
                              public void focusGained(java.awt.event.FocusEvent evt) {
                                        txtqtyFocusGained(evt);
                              }
                    });

                    txtdesc.setToolTipText("");

                    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                    getContentPane().setLayout(layout);
                    layout.setHorizontalGroup(
                              layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addGroup(layout.createSequentialGroup()
                                        .addGap(48, 48, 48)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                  .addComponent(jLabel2)
                                                  .addComponent(jLabel1)
                                                  .addComponent(jLabel3))
                                        .addGap(33, 33, 33)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                  .addComponent(txtdesc, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                                                  .addGroup(layout.createSequentialGroup()
                                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                      .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(cmdSave)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(cmdcancel))
                                                                      .addComponent(txtqty, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                      .addComponent(txtcost, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGap(0, 0, Short.MAX_VALUE)))
                                        .addGap(48, 48, 48))
                    );
                    layout.setVerticalGroup(
                              layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                              .addGroup(layout.createSequentialGroup()
                                        .addGap(45, 45, 45)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                  .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                  .addComponent(txtdesc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(9, 9, 9)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                  .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                  .addComponent(txtqty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                                  .addComponent(jLabel2)
                                                  .addComponent(txtcost, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                  .addComponent(cmdSave)
                                                  .addComponent(cmdcancel))
                                        .addContainerGap(49, Short.MAX_VALUE))
                    );

                    txtcost.getAccessibleContext().setAccessibleDescription("30");
                    txtqty.getAccessibleContext().setAccessibleDescription("30");
                    txtdesc.getAccessibleContext().setAccessibleDescription("30");

                    pack();
          }// </editor-fold>//GEN-END:initComponents

    private void cmdcancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdcancelActionPerformed
            this.dispose();
    }//GEN-LAST:event_cmdcancelActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
            txtqty.setText(qty);
            txtcost.setText(cost);
            txtdesc.setText(descrip);
    }//GEN-LAST:event_formWindowOpened

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
            if (txtqty.getText().isEmpty() == true
                    || txtcost.getText().isEmpty() == true
                    || txtdesc.getText().isEmpty() == true) {
                    JOptionPane.showMessageDialog(this, "Please fill-up all the required fields!");
                    return;
            } else {

                    CostingOp.qty = txtqty.getText().replace(",", "");
                    CostingOp.cost = myFunctions.amountFormat(Double.parseDouble(txtcost.getText().replace(",", "")));
                    CostingOp.rsel = rsel;
                    CostingOp.descrip = txtdesc.getText();

                    Double calctotal = Double.parseDouble(txtqty.getText()) * Double.parseDouble(txtcost.getText());
                    String finaltotal = myFunctions.amountFormat(calctotal);

                    CostingOp.amountc = finaltotal;
                    //System.out.println(finaltotal);

                    frmParent.UpdateChanges();

                    this.dispose();
            }
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void txtcostFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtcostFocusGained
            txtcost.selectAll();
    }//GEN-LAST:event_txtcostFocusGained

    private void txtqtyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtqtyFocusGained
            txtqty.selectAll();
    }//GEN-LAST:event_txtqtyFocusGained

        public static void main(String arg[]) {
                java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                                EditCharges dialog = new EditCharges(frmParent, true);
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
          private javax.swing.JButton cmdSave;
          private javax.swing.JButton cmdcancel;
          private javax.swing.JButton jButton1;
          private javax.swing.JLabel jLabel1;
          private javax.swing.JLabel jLabel2;
          private javax.swing.JLabel jLabel3;
          private javax.swing.JFormattedTextField txtcost;
          private javax.swing.JTextField txtdesc;
          private javax.swing.JFormattedTextField txtqty;
          // End of variables declaration//GEN-END:variables
}
