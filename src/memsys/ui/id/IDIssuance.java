package memsys.ui.id;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.swing.table.DefaultTableCellRenderer;
import memsys.global.FTPFactory;
import memsys.global.ReportFactory;

public class IDIssuance extends javax.swing.JInternalFrame {

    public static IDPrintSearch frmAdd;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static Statement stmt;
    static String nowDate = myFunctions.getDate2();

    public IDIssuance() {
        initComponents();
    }

    public static String getPicPath() throws IOException {
        Scanner scanSTR = new Scanner(new File("picPathConfig.txt"));
        String Str = scanSTR.nextLine();
        return Str;
    }

    public void showFrmAdd() {
        frmAdd = new IDPrintSearch(this, true);
        frmAdd.setVisible(true);
    }

    public boolean CheckExistance(int cmid) {
        boolean found = false;
        int rc = jTBLID.getRowCount();
        int cntr = 0;

        //Determine the no. of record selected by the user
        while (cntr < rc) {
            String mid = jTBLID.getValueAt(cntr, 0).toString();
            //System.out.println(selTF);

            if (cmid == Integer.parseInt(mid)) {
                found = true;
            }
            cntr++;
        }
        return found;

    }

    public void RemoveTag(int lid) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM IDPrintedTBL "
                + " WHERE memberID=" + lid;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select i.memberID, acctname, ornum, date_encoded, partID, address, logID from IDPrintedTBL i "
                + " left join membersTBL m on i.memberID = m.memberID where acctname like '%" + txtsearch.getText() + "%'";
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) jTBLID1.getModel();

            renderer.setHorizontalAlignment(0);
            renderer2.setHorizontalAlignment(SwingConstants.RIGHT);

            jTBLID1.setRowHeight(21);
            jTBLID1.getColumnModel().getColumn(0).setCellRenderer(renderer);
            jTBLID1.getColumnModel().getColumn(2).setCellRenderer(renderer);
            jTBLID1.getColumnModel().getColumn(3).setCellRenderer(renderer);
            jTBLID1.getColumnModel().getColumn(4).setCellRenderer(renderer);

            model.setNumRows(0);

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getDate(4), rs.getString(6), rs.getString(5), rs.getString(3), rs.getString(6)});
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public void AddList(String memberID, String Name, String MemDate, String Address, String Type, String pid) {
        model = (DefaultTableModel) jTBLID.getModel();

        renderer.setHorizontalAlignment(0);
        renderer2.setHorizontalAlignment(SwingConstants.RIGHT);

        jTBLID.setRowHeight(21);

        jTBLID.getColumnModel().getColumn(0).setCellRenderer(renderer);
        jTBLID.getColumnModel().getColumn(2).setCellRenderer(renderer);
        jTBLID.getColumnModel().getColumn(4).setCellRenderer(renderer);
        jTBLID.getColumnModel().getColumn(5).setCellRenderer(renderer);

        //model.setNumRows(0);
        String memtype = null;
        if ("1".equals(Type)) {
            memtype = "SINGLE";
        } else if ("2".equals(Type)) {
            memtype = "JOINT";
        }

        model.addRow(new Object[]{memberID, Name, MemDate, Address, memtype, pid});
    }

    void removeRecord(int id) {
        int r = jTBLID.getRowCount();
        int c;
        c = 0;

        while (c < r + 1) {
            try {
                int mID = Integer.parseInt(jTBLID.getValueAt(c, 0).toString());

                if (mID == id) {
                    model.removeRow(c);
                }

            } catch (Exception e) {
                e.getStackTrace();
            }
            c++;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTBLID = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        cmdAdd = new javax.swing.JButton();
        cmdRemove = new javax.swing.JButton();
        cmdPrint = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTBLID1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        cmdExit1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Identification Card Issuance");

        jTBLID.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MemberID", "Name of Member", "Date of Membership", "Address", "Type of Membership", "PartipantID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTBLID.setToolTipText("");
        jTBLID.getTableHeader().setReorderingAllowed(false);
        jTBLID.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTBLIDMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jTBLID);
        if (jTBLID.getColumnModel().getColumnCount() > 0) {
            jTBLID.getColumnModel().getColumn(3).setResizable(false);
        }
        //set column width
        //jTBLConn.getColumnModel().getColumn(0).setMaxWidth(80);
        //jTBLConn.getColumnModel().getColumn(1).setMaxWidth(80);
        jTBLID.getColumnModel().getColumn(1).setPreferredWidth(240);

        jLabel2.setText("List of members to be printed. Click ADD button to select a record from the members record.");

        cmdAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdAdd.setMnemonic('A');
        cmdAdd.setText("Add");
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });

        cmdRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        cmdRemove.setMnemonic('R');
        cmdRemove.setText("Remove from list");
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });

        cmdPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/printer.png"))); // NOI18N
        cmdPrint.setMnemonic('P');
        cmdPrint.setText("Print");
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });

        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit_1.png"))); // NOI18N
        cmdExit.setMnemonic('x');
        cmdExit.setText("Exit");
        cmdExit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        jTBLID1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MemberID", "Name of Member", "Date of Membership", "Address", "PartipantID", "LogID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTBLID1.setToolTipText("");
        jTBLID1.getTableHeader().setReorderingAllowed(false);
        jTBLID1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTBLID1MouseMoved(evt);
            }
        });
        jScrollPane2.setViewportView(jTBLID1);
        //set column width
        //jTBLConn.getColumnModel().getColumn(0).setMaxWidth(80);
        //jTBLConn.getColumnModel().getColumn(1).setMaxWidth(80);
        jTBLID.getColumnModel().getColumn(1).setPreferredWidth(240);

        jLabel1.setText("Issued ID");

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
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

        cmdExit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove_1.png"))); // NOI18N
        cmdExit1.setMnemonic('x');
        cmdExit1.setText("Remove Logs");
        cmdExit1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdExit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExit1ActionPerformed(evt);
            }
        });

        jLabel3.setText("NOTE: Date Issued Format must be yyyy-MM-dd Ex: 2015-01-23");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(cmdPrint)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmdAdd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdRemove)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1078, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmdExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cmdExit1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(0, 0, Short.MAX_VALUE)))))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdAdd)
                            .addComponent(cmdRemove)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmdPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdExit1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdExit))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTBLIDMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTBLIDMouseMoved
   }//GEN-LAST:event_jTBLIDMouseMoved

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        showFrmAdd();
    }//GEN-LAST:event_cmdAddActionPerformed

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        int col = 0; //set column value to 0
        int row = jTBLID.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTBLID.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
        } else {
            String id = jTBLID.getValueAt(row, col).toString();
            int i = myFunctions.msgboxYesNo("Are you sure you want remove this current record?");
            if (i == 0) {
                removeRecord(Integer.parseInt(id));
                JOptionPane.showMessageDialog(this, "Record has been successfully deleted!");
            } else if (i == 1) {
                return; //do nothing
            } else if (i == 2) {
                this.dispose(); //exit window
                return;
            }
        }
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        boolean validatedate = ValidateDateRec();
//
//        if (validatedate == true) {

            rsDeleteAllForPrintDatas();
            addAllForPrintRec();
            rsTagPrintRecord();

            FTPFactory i = new FTPFactory();

            String picpath = i.GetFTPPicPath();
            String sigpath = i.GetFTPSigPath();

            ReportFactory.rptID(picpath, sigpath);

//        } else {
//            JOptionPane.showMessageDialog(null, "Some of the Printing Entries has an Invalid Date value. Please use this format yyyy-MM-dd Ex:2015-01-31");
//        }
    }//GEN-LAST:event_cmdPrintActionPerformed

    public static String getIDPicPath() throws IOException {
        String Str = memsys.others.paths.getPicPathConfig();
        return Str;
    }

    public static String getIDSigPath() throws IOException {
        String Str = memsys.others.paths.getSigPathConfig();
        return Str;
    }

    void addAllForPrintRec() {
        int r = jTBLID.getRowCount();
        int c;
        c = 0;

        while (c < r + 1) {
            try {

                int mID = Integer.parseInt(jTBLID.getValueAt(c, 0).toString());
                String name = jTBLID.getValueAt(c, 1).toString();
                String address = jTBLID.getValueAt(c, 3).toString();
                //String date = jTBLID.getValueAt(c, 2).toString();
                String type = jTBLID.getValueAt(c, 4).toString();
                String pid = jTBLID.getValueAt(c, 5).toString();
//                String orno = jTBLID.getValueAt(c, 6).toString();//temp
//                String dateissued = jTBLID.getValueAt(c, 7).toString();//temp

//                boolean d = myFunctions.isValidDate(dateissued);
//                if (d==false){
//                    JOptionPane.showMessageDialog(null, "Some of the Printing Entries has an Invalid Date value. Please use this format yyyy-MM-dd Ex:2015-01-31");
//                    break;
//                }else{
                rsAddForPrintRecord(mID, name, address, nowDate, type, pid, "", "");
//                }
                //System.out.println(date);
            } catch (Exception e) {
                e.getStackTrace();
            }
            c++;
        }
    }

    boolean ValidateDateRec() {
        boolean allvalid = false;
        int r = jTBLID.getRowCount();
        int c;
        c = 0;

        while (c < r + 1) {
            try {
                String dateissued = jTBLID.getValueAt(c, 7).toString();//temp

                boolean d = myFunctions.isValidDate(dateissued);
                if (d == false) {
                    allvalid = false;

                    break;
                } else {
                    allvalid = true;
                }
                //System.out.println(date);
            } catch (Exception e) {
                e.getStackTrace();
            }
            c++;
        }
        return allvalid;
    }

    void rsAddForPrintRecord(int mID, String name, String address, String ddate, String type, String pid, String orno, String dateissued) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO IDForPrintTBL (memberID, name, address, date, type, partID, orNum, dateIssued) "
                + "VALUES ('" + mID + "','" + name + "','" + address + "','" + ddate + "','" + type + "','" + pid + "', '" + orno + "', '" + dateissued + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    void rsTagPrintRecord() {

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO IDPrintedTBL (memberID, datePrinted) "
                + "SELECT memberID,'" + nowDate + "' FROM IDForPrintTBL";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
            e.printStackTrace();
        }
    }

    void rsDeleteAllForPrintDatas() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM IDForPrintTBL";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void jTBLID1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTBLID1MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTBLID1MouseMoved

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed

    }//GEN-LAST:event_txtsearchActionPerformed

    private void txtsearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsearchKeyPressed
        if (!"".equals(txtsearch.getText())) {
            int key = evt.getKeyCode();
            if (key == KeyEvent.VK_ENTER) {

                populateTBL();
                // txtsearch1.setText(txtsearch1.getText());
            }
        }
    }//GEN-LAST:event_txtsearchKeyPressed

    private void cmdExit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExit1ActionPerformed
        int col = 0;//set column value to 0
        int row = jTBLID1.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTBLID1.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the table!");
            return;
        } else {

            String Pid = jTBLID1.getValueAt(row, col).toString();
            int i = myFunctions.msgboxYesNo("Are you sure you want delete this current record?");
            if (i == 0) {
                //myDataenvi.rsDeleteParticipant(Pid);
                RemoveTag(Integer.parseInt(Pid));
                populateTBL();

                JOptionPane.showMessageDialog(this, "Record has been successfully deleted!");
            } else if (i == 1) {
            } else if (i == 2) {
                this.dispose(); //exit window
            }
        }
    }//GEN-LAST:event_cmdExit1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdExit1;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTBLID;
    private javax.swing.JTable jTBLID1;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
