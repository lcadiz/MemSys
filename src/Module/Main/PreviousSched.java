package Module.Main;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class PreviousSched extends javax.swing.JInternalFrame {
    
    static Statement stmtPreviousShed;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static String nowDate = myFunctions.getDate();
    static String nowYear = myFunctions.getYear();
    static String nowMonth = myFunctions.getMonth();
    public static Participants frmParticipants;
    
    public PreviousSched() {
        initComponents();
        cmdViewPart.setMnemonic('V');
        cmdRefresh.setMnemonic('R');
        txtMonth.setVisible(false);
        jLabel8.setVisible(false);
//        getRootPane().setDefaultButton(cmdRefresh);
    }
    
    public void showFrmParticipants() {
        frmParticipants = new Participants(this, true);
        frmParticipants.setVisible(true);
    }
    
    public void populateTBL() {
        
        Connection conn = MainDBConn.getConnection();
        String createString;
//        createString = "SELECT batchID, sched_venue, CONVERT(char(10), sched_date, 101), areacode, "
//                + "sched_address, sched_stat FROM scheduleTBL WHERE sched_date <'" + nowDate + "'" + "AND year(sched_date)=" + txtYear.getText() + "AND month(sched_date)=" + txtMonth.getText() + "ORDER BY sched_date";
//        //stmtIncomingShed
        
        createString = "SELECT batchID, sched_venue, CONVERT(char(10), sched_date, 101), areacode, "
                + "sched_address, sched_stat FROM scheduleTBL WHERE sched_date <'" + nowDate + "'" + "AND year(sched_date)=" + txtYear.getText() + " ORDER BY sched_date";
        
        try {
            stmtPreviousShed = conn.createStatement();
            ResultSet rs = stmtPreviousShed.executeQuery(createString);
            
            model = (DefaultTableModel) jTblLMPreviousSched.getModel();
            
            renderer.setHorizontalAlignment(0);
            
            jTblLMPreviousSched.setRowHeight(21);
            jTblLMPreviousSched.getColumnModel().getColumn(0).setMaxWidth(100);
            jTblLMPreviousSched.getColumnModel().getColumn(0).setCellRenderer(renderer);
            jTblLMPreviousSched.getColumnModel().getColumn(1).setMaxWidth(150);
            jTblLMPreviousSched.getColumnModel().getColumn(1).setCellRenderer(renderer);
            jTblLMPreviousSched.setColumnSelectionAllowed(false);
            
            model.setNumRows(0);
            String stat = null;
            
            
            
            while (rs.next()) {
                if (rs.getInt(6) == 0) {
                    stat = "Open";
                } else {
                    stat = "Closed";
                }
                model.addRow(new Object[]{rs.getString(1), rs.getString(3), rs.getString(2), rs.getString(5), stat});
            }
            
            stmtPreviousShed.close();
            conn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ScrollPane = new javax.swing.JScrollPane();
        jTblLMPreviousSched = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        cmdViewPart = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtMonth = new javax.swing.JTextField();
        cmdRefresh = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Previous PMES Schedule/s");
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
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Membership/Images/manageprevschedule.png"))); // NOI18N
        jToolBar1.add(jLabel4);

        jLabel7.setForeground(new java.awt.Color(255, 102, 0));
        jLabel7.setText(" Note: Here  you can view previous PMES Schedule.");
        jToolBar1.add(jLabel7);

        ScrollPane.setToolTipText("List of Previous PMES Schedule/s");
        ScrollPane.setAutoscrolls(true);

        jTblLMPreviousSched.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BATCH No.", "Date", "Venue of Seminar", "Address (Specific)", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jTblLMPreviousSched.setColumnSelectionAllowed(true);
        ScrollPane.setViewportView(jTblLMPreviousSched);
        jTblLMPreviousSched.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setText("Year:");

        txtYear.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtYear.setToolTipText("Preiod of Seminar");
        txtYear.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtYear.setSelectionEnd(10);
        txtYear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtYearMouseClicked(evt);
            }
        });
        txtYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtYearActionPerformed(evt);
            }
        });
        txtYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtYearFocusGained(evt);
            }
        });

        cmdViewPart.setText("View participants in this schedule");
        cmdViewPart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdViewPartActionPerformed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("Month:");

        txtMonth.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMonth.setToolTipText("Preiod of Seminar");
        txtMonth.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtMonth.setSelectionEnd(10);
        txtMonth.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtMonthMouseClicked(evt);
            }
        });
        txtMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMonthActionPerformed(evt);
            }
        });
        txtMonth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMonthFocusGained(evt);
            }
        });

        cmdRefresh.setText("Refresh View");
        cmdRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmdRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdViewPart, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(138, Short.MAX_VALUE))
            .addComponent(ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtYear, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdRefresh)
                    .addComponent(cmdViewPart))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE))
        );

        jTblLMPreviousSched.getColumnModel().getColumn(4).setMaxWidth(100);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        txtYear.setText(nowYear);
        txtMonth.setText(nowMonth);
        
        populateTBL();
    }//GEN-LAST:event_formInternalFrameOpened
    
    private void txtYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtYearActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtYearActionPerformed
    
    private void txtYearFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtYearFocusGained
        txtYear.selectAll();
}//GEN-LAST:event_txtYearFocusGained
    
    private void txtYearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtYearMouseClicked
        txtYear.selectAll();
    }//GEN-LAST:event_txtYearMouseClicked
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        txtMonth.selectAll();
    }//GEN-LAST:event_formMouseClicked
    
    private void cmdRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefreshActionPerformed
        populateTBL();
    }//GEN-LAST:event_cmdRefreshActionPerformed
    
    private void cmdViewPartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdViewPartActionPerformed
        int col = 0; //set column value to 1
        int col1 = 1; //set column value to 1
        int col2 = 2; //set column value to 2
        int col3 = 3; //set column value to 3
        int row = jTblLMPreviousSched.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (jTblLMPreviousSched.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = jTblLMPreviousSched.getValueAt(row, col).toString();
            String date = jTblLMPreviousSched.getValueAt(row, col1).toString();
            String venue = jTblLMPreviousSched.getValueAt(row, col2).toString();
            String address = jTblLMPreviousSched.getValueAt(row, col3).toString();
            String title = "[" + date + "]-" + venue.trim() + "-" + address.trim();
            
            Participants.bID = id;
            Participants.title = title;
            
            showFrmParticipants();
        }
        
        
        
    }//GEN-LAST:event_cmdViewPartActionPerformed
    
    private void txtMonthFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMonthFocusGained
        txtMonth.selectAll();
    }//GEN-LAST:event_txtMonthFocusGained
    
    private void txtMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMonthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMonthActionPerformed
    
    private void txtMonthMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMonthMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMonthMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JButton cmdRefresh;
    private javax.swing.JButton cmdViewPart;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTable jTblLMPreviousSched;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField txtMonth;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables
}
