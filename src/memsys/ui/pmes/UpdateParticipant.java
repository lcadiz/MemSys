package memsys.ui.pmes;

import memsys.global.DBConn.MainDBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import memsys.global.FunctionFactory;
import memsys.global.myDataenvi;
import static memsys.ui.pmes.AddNewParticipants.aid;
import static memsys.ui.pmes.Attendance.stmt;

public class UpdateParticipant extends javax.swing.JDialog {

    public static Attendance frmParent;
    public static String lname, fname, mname, ext, paddress, partID;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    public static int bid;
    //   public static String pID;

    public UpdateParticipant(Attendance parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        
        populateComboArea();
        cmbbrgy.addItem("--SELECT--");
        getRootPane().setDefaultButton(cmdsave);
        cmdCancel.setMnemonic('C');
        bid=GetBID();
    }
        int GetBID() {
        int UGID = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT brgyID FROM participantsTBL WHERE partID="+partID;


        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                UGID = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return UGID;
    }
    void ag() {

        AddressGenerator ng = new AddressGenerator();
        String sitio = "";
        String brgy = "";
        String area = "";

        try {
            if (txtsitio.getText().isEmpty() == true) {
                sitio = "";
            } else {
                sitio = txtsitio.getText();
            }
        } catch (Exception e) {
        }

        try {
            if ("--SELECT--".equals(cmbbrgy.getSelectedItem().toString())) {
                brgy = "";
            } else {
                brgy = cmbbrgy.getSelectedItem().toString();
            }
        } catch (Exception e) {
        }

        try {
            if ("--SELECT--".equals(cmbarea.getSelectedItem().toString())) {
                area = "";
            } else {
                area = cmbarea.getSelectedItem().toString();
            }
        } catch (Exception e) {
        }

        lbl.setText(sitio.toUpperCase().trim().replace("'", " ") + ", " + brgy.toUpperCase().toUpperCase().trim().replace("'", " ") + ", " + area.toUpperCase().toUpperCase().trim().replace("'", " ") + ", NEGROS ORIENTAL");

    }

    public final void populateComboArea() {
        //Populate Combo Area
        cmbarea.addItem("--SELECT--");

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM areaTBL ORDER BY area_desc;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbarea.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getStackTrace();
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

    public final void populateComboBrgy() {
        //Populate Combo Area

        DefaultComboBoxModel model = (DefaultComboBoxModel) cmbbrgy.getModel();

        model.removeAllElements();
        cmbbrgy.addItem("--SELECT--");
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM barangayTBL  WHERE areacode=" + aid + " ORDER BY brgy_name";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbbrgy.addItem(new Item2(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    class Item2 {

        private int id;
        private String description;

        public Item2(int id, String description) {
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

    private void saveRec() {
        String clname = txtLastName.getText().toUpperCase().trim().replace("'", " ");
        String cfname = txtFirstName.getText().toUpperCase().trim().replace("'", " ");
        String cmname = txtMiddleName.getText().toUpperCase().trim().replace("'", " ");
        String cext = txtExt.getText().toUpperCase().trim().replace("'", " ");
        String address = lbl.getText().toUpperCase().trim().replace("'", " ");
        //  int batchID = Attendance.BatchID;
        myDataenvi.rsUpdateParticipant(clname, cfname, cmname, cext, address, partID, bid);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel11 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        lbl = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbarea = new javax.swing.JComboBox();
        cmbbrgy = new javax.swing.JComboBox();
        txtsitio = new javax.swing.JTextField();
        cmdsave = new javax.swing.JButton();
        txtMiddleName = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cmdCancel = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtExt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Update Current Participant Record");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel11.setText("Address:");

        txtLastName.setToolTipText("Ex. De la cruz");

        lbl.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Last Name");

        cmbarea.setToolTipText("Municipal/City");
        cmbarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbareaActionPerformed(evt);
            }
        });

        cmbbrgy.setToolTipText("Municipal/City");
        cmbbrgy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbbrgyActionPerformed(evt);
            }
        });

        txtsitio.setToolTipText("Ex. 094, Poblacion, Binsoy Negros Or.");
        txtsitio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtsitioKeyPressed(evt);
            }
        });

        cmdsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        cmdsave.setText("Save");
        cmdsave.setToolTipText("Add Participant");
        cmdsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdsaveActionPerformed(evt);
            }
        });

        txtMiddleName.setToolTipText("Mother's Family Name");

        jLabel8.setText("Sitio (Optional):");

        txtFirstName.setToolTipText("Ex. Juan");

        jLabel9.setText("Barangay:");

        cmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdCancel.setText("Close");
        cmdCancel.setToolTipText("Cancel Adding Participant");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("First Name");

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Middle Name");

        jLabel1.setText("Participants:");

        jLabel10.setText("Town:");

        txtExt.setToolTipText("Ex. Jr.,Sr.,II,III,IV");

        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Ext.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel8)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbarea, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbbrgy, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtsitio, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdsave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdCancel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtLastName, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMiddleName, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                            .addComponent(txtExt))))
                .addGap(26, 26, 26))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtExt, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMiddleName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cmbarea, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbbrgy, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtsitio, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdsave)
                    .addComponent(cmdCancel))
                .addGap(48, 48, 48))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        txtLastName.setText(lname.trim());
        txtFirstName.setText(fname.trim());
        txtMiddleName.setText(mname.trim());
        txtExt.setText(ext.trim());
        lbl.setText(paddress.trim());
    }//GEN-LAST:event_formWindowOpened

    private void cmbareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbareaActionPerformed
        try {
            Item item = (Item) cmbarea.getSelectedItem();
            aid = item.getId();
            populateComboBrgy();
        } catch (Exception e) {
        }
        ag();
    }//GEN-LAST:event_cmbareaActionPerformed

    private void cmbbrgyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbbrgyActionPerformed
        try {
            Item2 item = (Item2) cmbbrgy.getSelectedItem();
            bid = item.getId();
        } catch (Exception e) {
        }
        ag();
    }//GEN-LAST:event_cmbbrgyActionPerformed

    private void cmdsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdsaveActionPerformed
        if (txtLastName.getText().isEmpty() == true
                || txtFirstName.getText().isEmpty() == true
                || txtsitio.getText().isEmpty() == true
                || "--SELECT--".equals(cmbarea.getSelectedItem().toString())
                || "--SELECT--".equals(cmbbrgy.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(this, "Please fill-up all the required fields");
        } else {
            //add the new record to the database
            saveRec();
            //  clearFields();
            frmParent.populateTBL();
            this.dispose();
            JOptionPane.showMessageDialog(this, "Record sucessfully added");
        }
    }//GEN-LAST:event_cmdsaveActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void txtsitioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtsitioKeyPressed
         ag();
    }//GEN-LAST:event_txtsitioKeyPressed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                UpdateParticipant dialog = new UpdateParticipant(frmParent, true);
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
    private javax.swing.JComboBox cmbarea;
    private javax.swing.JComboBox cmbbrgy;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdsave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbl;
    private javax.swing.JTextField txtExt;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtMiddleName;
    private javax.swing.JTextField txtsitio;
    // End of variables declaration//GEN-END:variables
}
