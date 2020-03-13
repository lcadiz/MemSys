package Module.Main;


import memsys.global.DBConn.MainDBConn;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;

public class Participants extends javax.swing.JDialog {

    public static PreviousSched frmParent;
    public static String title, bID;
    static Statement stmtAttendance;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;

    public Participants(PreviousSched parent, boolean modal) {
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblAttendance = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Membership/Images/participants.png"))); // NOI18N
        jToolBar1.add(jLabel4);

        jLabel7.setForeground(new java.awt.Color(255, 102, 0));
        jLabel7.setText(" List of Participants in this particular schedule.");
        jToolBar1.add(jLabel7);

        jTblAttendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID No.", "Name of Participant", "Address", "Attendance", "Encoded", "Printed"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jTblAttendance.setToolTipText("List of All Participant(s)");
        jTblAttendance.getTableHeader().setReorderingAllowed(false);
        jTblAttendance.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTblAttendanceMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(jTblAttendance);
        //set column width
        jTblAttendance.getColumnModel().getColumn(0).setMaxWidth(50);
        jTblAttendance.getColumnModel().getColumn(3).setMaxWidth(120);
        jTblAttendance.getColumnModel().getColumn(4).setMaxWidth(160);
        jTblAttendance.getColumnModel().getColumn(5).setMaxWidth(150);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 865, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void populateTBL() {


        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT partID,RTRIM(participantsTBL.part_fname)+' '+LEFT(participantsTBL.part_mname,1) + '. '+' '+RTRIM(participantsTBL.part_lname)+' '+RTRIM(participantsTBL.part_ext)"
                + ",address, part_stat,CONVERT(char(10), date_encoded, 101),CONVERT(char(10), date_printed, 101) FROM participantsTBL WHERE batchID=" + bID + "ORDER BY part_lname,part_fname";

        try {
            stmtAttendance = conn.createStatement();
            ResultSet rs = stmtAttendance.executeQuery(createString);

            model = (DefaultTableModel) jTblAttendance.getModel();


            cellAlignCenterRenderer.setHorizontalAlignment(0);

            jTblAttendance.setRowHeight(21);
            jTblAttendance.getColumnModel().getColumn(0).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.getColumnModel().getColumn(4).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.getColumnModel().getColumn(5).setCellRenderer(cellAlignCenterRenderer);
            jTblAttendance.setColumnSelectionAllowed(false);

            model.setNumRows(0);



            while (rs.next()) {
                String stat = null;

                if (rs.getInt(4) == 0) {
                    stat = "Present";
                } else {
                    stat = "Cancelled";
                }

                String prnflg = null;

                if (rs.getString(6) == null) {
                    prnflg = "Not Yet Printed";
                } else {
                    prnflg = rs.getString(6);
                }


                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), stat, rs.getString(5), prnflg});
            }

            stmtAttendance.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.setTitle(title);
        populateTBL();
        int c;
        c = model.getRowCount();
        if (c == 0) {

            JOptionPane.showMessageDialog(this, "Record Empty!");
            this.dispose();
        }
    }//GEN-LAST:event_formWindowOpened

    private void jTblAttendanceMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblAttendanceMouseMoved
}//GEN-LAST:event_jTblAttendanceMouseMoved

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                Participants dialog = new Participants(frmParent, true);
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTblAttendance;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
