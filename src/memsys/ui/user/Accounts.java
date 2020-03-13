package memsys.ui.user;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class Accounts extends javax.swing.JInternalFrame {

    static Statement stmt;
    public static AddUser frmAdd;
    public static EditUserInfo frmEditInfo;
    public static ResetPassword frmReset;
    public static Assignments frmAssign;

    public Accounts() {
        initComponents();
    }

    public void showFrmAssign() {
        frmAssign = new Assignments(this, true);
        frmAssign.setVisible(true);
    }

    public void showFrmAdd() {
        frmAdd = new AddUser(this, true);
        frmAdd.setVisible(true);
    }

    public void showFrmEdit() {
        frmEditInfo = new EditUserInfo(this, true);
        frmEditInfo.setVisible(true);
    }

    public void showFrmReset() {
        frmReset = new ResetPassword(this, true);
        frmReset.setVisible(true);
    }

    public static void DeleteUser(int uID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM Users WHERE UserID=" + uID;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void populateLst() {
        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT UserID, UserName, FullName FROM Users ORDER BY FullName";

        try {
            DefaultListModel model = new DefaultListModel();
            this.lstuser.setModel(model);

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);


            while (rs.next()) {
                model.addElement(new Item(rs.getInt(1), rs.getString(3) + " (" + rs.getString(2) + ")"));
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        lstuser = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        cmdnew = new javax.swing.JButton();
        cmdedit = new javax.swing.JButton();
        cmdchangepass = new javax.swing.JButton();
        cmdremove = new javax.swing.JButton();
        cmdgrpassign = new javax.swing.JButton();
        cmdexit = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("User Accounts and Role Assignments");
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

        jScrollPane1.setViewportView(lstuser);

        jLabel1.setText("List of System Authorized Users");

        cmdnew.setMnemonic('N');
        cmdnew.setText("New User");
        cmdnew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdnewActionPerformed(evt);
            }
        });

        cmdedit.setMnemonic('E');
        cmdedit.setText("Edit UserInfo");
        cmdedit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdeditActionPerformed(evt);
            }
        });

        cmdchangepass.setMnemonic('C');
        cmdchangepass.setText("Reset Password");
        cmdchangepass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdchangepassActionPerformed(evt);
            }
        });

        cmdremove.setMnemonic('R');
        cmdremove.setText("Remove");
        cmdremove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdremoveActionPerformed(evt);
            }
        });

        cmdgrpassign.setMnemonic('g');
        cmdgrpassign.setText("Assign Roles");
        cmdgrpassign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdgrpassignActionPerformed(evt);
            }
        });

        cmdexit.setMnemonic('x');
        cmdexit.setText("Exit");
        cmdexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmdnew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdedit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(cmdchangepass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(cmdremove, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(cmdgrpassign, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(cmdexit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdnew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdedit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdchangepass)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdremove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdgrpassign)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdexit)
                        .addGap(0, 261, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        populateLst();
    }//GEN-LAST:event_formInternalFrameOpened

    private void cmdexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdexitActionPerformed

    private void cmdnewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdnewActionPerformed
        showFrmAdd();
    }//GEN-LAST:event_cmdnewActionPerformed

    private void cmdeditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdeditActionPerformed

        try {
            Item item = (Item) lstuser.getSelectedValue();
            int id = item.getId();
            EditUserInfo.uid = id;
            showFrmEdit();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No User Account Selected!");
            return;
        }

    }//GEN-LAST:event_cmdeditActionPerformed

    private void cmdchangepassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdchangepassActionPerformed
        try {
            Item item = (Item) lstuser.getSelectedValue();
            int id = item.getId();
            ResetPassword.uid = id;
            showFrmReset();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No User Account Selected!");
            return;
        }
    }//GEN-LAST:event_cmdchangepassActionPerformed

    private void cmdremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdremoveActionPerformed
        try {
            int i = myFunctions.msgboxYesNo("Are you sure you want to close this schedule?" + (char) 10
                    + "Note: If you close this schedule it will never be updatable. Do you want proceed?");
            if (i == 0) {

                Item item = (Item) lstuser.getSelectedValue();
                int id = item.getId();
                DeleteUser(id);
                this.populateLst();
                JOptionPane.showMessageDialog(null, "Current user account deleted!");

            } else if (i == 1) {
                return; //do nothing
            } else if (i == 2) {
                this.dispose(); //exit window
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No User Account Selected!");
            return;
        }
    }//GEN-LAST:event_cmdremoveActionPerformed

    private void cmdgrpassignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdgrpassignActionPerformed
        try {
            Item item = (Item) lstuser.getSelectedValue();
            int id = item.getId();
            Assignments.uid = id;
            showFrmAssign();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No User Account Selected!");
            return;
        }


    }//GEN-LAST:event_cmdgrpassignActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdchangepass;
    private javax.swing.JButton cmdedit;
    private javax.swing.JButton cmdexit;
    private javax.swing.JButton cmdgrpassign;
    private javax.swing.JButton cmdnew;
    private javax.swing.JButton cmdremove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstuser;
    // End of variables declaration//GEN-END:variables
}
