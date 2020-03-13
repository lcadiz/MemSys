package Module.Main;


import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import javax.swing.JOptionPane;
import java.sql.*;

public class ReopenSched extends javax.swing.JInternalFrame {

    static Statement stmt;

    public ReopenSched() {
        initComponents();
        cmdCancel.setMnemonic('C');
        getRootPane().setDefaultButton(cmdReopen);


    }

    public static int rsSchedExistence(String bID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT batchID FROM dbo.scheduleTBL WHERE batchID=" + bID;
        int count = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);


            while (rs.next()) {
                count++;
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return count;
    }

    public static int rsSchedDetStat(String bID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT sched_stat FROM dbo.scheduleTBL WHERE batchID=" + bID;
        int count = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                count = rs.getInt(1);
            }


            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return count;
    }

    public static void rsUpdateSchedStat(String bID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.scheduleTBL "
                + "SET sched_stat=0 WHERE batchID=" + bID;

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

        txtBatchNo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmdReopen = new javax.swing.JButton();
        cmdCancel = new javax.swing.JButton();

        setClosable(true);
        setTitle("Re-Open Closed PMES Schedule");

        txtBatchNo.setToolTipText("");
        txtBatchNo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));

        jLabel1.setText("Batch No.:");

        cmdReopen.setText("Re-open");
        cmdReopen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdReopenActionPerformed(evt);
            }
        });

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
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdReopen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdCancel))
                    .addComponent(txtBatchNo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(185, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBatchNo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdReopen)
                    .addComponent(cmdCancel))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdReopenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdReopenActionPerformed
        if (txtBatchNo.getText().isEmpty() == true) {
            JOptionPane.showMessageDialog(this, "Please enter the BATCH NO. of the PMES schedule!");
            return;
        } else {
            int x = rsSchedExistence(txtBatchNo.getText());
            if (x == 0) {
                JOptionPane.showMessageDialog(this, "Schedule doesn't exist!");
                return;

            } else {
                int d = rsSchedDetStat(txtBatchNo.getText());
                if (d == 0) {
                    JOptionPane.showMessageDialog(this, "Schedule is not closed!");
                    return;
                } else {
                    int i = myFunctions.msgboxYesNo("Are you sure you wan to re-open this schedule?");
                    if (i == 0) {
                        rsUpdateSchedStat(txtBatchNo.getText());
                        this.dispose();
                        JOptionPane.showMessageDialog(this, "Schedule is now open!");
                    } else if (i == 1) {
                        return; //do nothing
                    } else if (i == 2) {
                        this.dispose(); //exit window
                        return;
                    }
                }
            }

        }
    }//GEN-LAST:event_cmdReopenActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdReopen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtBatchNo;
    // End of variables declaration//GEN-END:variables
//    public static void main(String[] args) {
//
//        int x = rsSchedDetStat("44");
//        System.out.println(x);
//    }
}
