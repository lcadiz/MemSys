package memsys.ui.pmes;


import memsys.global.DBConn.MainDBConn;
import memsys.global.DBConn.MainDBConn;
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import memsys.global.FunctionFactory;

public final class NewSched extends javax.swing.JInternalFrame {

    static Statement stmtSelectAllAreas;
    static int areaCode;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();

    //private final static String newline = "\n";
    public NewSched() {
        initComponents();
        setdates();
        //  cmdExit.setMnemonic('C');
        getRootPane().setDefaultButton(cmdCreate);
        setdates();

    }

   void setdates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date theDate = null;
        try {
            theDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        txtdate.setDate(theDate);
        //  txtend.setDate(theDate);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmdCreate = new javax.swing.JButton();
        cmdExit = new javax.swing.JButton();
        txtAddress = new javax.swing.JTextField();
        txtVenue = new javax.swing.JTextField();
        cmbArea = new javax.swing.JComboBox();
        txtdate = new com.toedter.calendar.JDateChooser();

        jLabel1.setText("jLabel1");

        setClosable(true);
        setIconifiable(true);
        setTitle("New PMES Schedule");
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

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setLabelFor(txtVenue);
        jLabel2.setText("Venue:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Date:");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Address:");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel6.setLabelFor(txtVenue);
        jLabel6.setText("Area:");

        cmdCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdCreate.setMnemonic('S');
        cmdCreate.setText("Create Schedule");
        cmdCreate.setToolTipText("Create new schedule");
        cmdCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCreateActionPerformed(evt);
            }
        });
        cmdCreate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmdCreateKeyPressed(evt);
            }
        });

        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdExit.setMnemonic('C');
        cmdExit.setText("Cancel");
        cmdExit.setToolTipText("Cancel and exit window");
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });

        txtAddress.setToolTipText("Specific Address: Ex. Bulod, Bindoy Negros Oriental  ");

        txtVenue.setToolTipText("Ex. Bindoy Municipal Hall");

        cmbArea.setForeground(new java.awt.Color(102, 102, 102));
        cmbArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SELECT-" }));
        cmbArea.setToolTipText("Municipal/City");
        cmbArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAreaActionPerformed(evt);
            }
        });

        txtdate.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdCreate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdExit))
                    .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                    .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVenue))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVenue, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdCreate)
                    .addComponent(cmdExit))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void add() {
        //get value of the textfields
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String eeDate = dateFormat.format(txtdate.getDate());
        int strAreaCode = areaCode;
        String strAddress = txtAddress.getText();
        String strVenue = txtVenue.getText();
 
         String strDate= dateFormat.format(txtdate.getDate());

        //check if the txtfields are not  empty
        if (txtVenue.getText().isEmpty() == true || txtAddress.getText().isEmpty() == true || cmbArea.getSelectedItem() == "-SELECT-") {
            JOptionPane.showMessageDialog(this, "Please fill-up all the required fields!");
        } else {
            //check if the date entered is valid
//            if (myFunctions.isValidDate(strDate) == false) {
//                return;
//            } else {
                int dupflg = myDataenvi.findDupSched(strDate, strAreaCode);

                if (dupflg == 0) {
                    //add the new record to the database
                    myDataenvi.rsAddSched(strVenue, eeDate, strAreaCode, strAddress);
                } else {
                    int i = myFunctions.msgboxYesNoCancel("System detected an existing schedule in this particular date and area. Would you like to proceed setting this new schedule?");
                    if (i == 0) {
                        //proceed add the new record to the database even it is already existed
                        myDataenvi.rsAddSched(strVenue, strDate, strAreaCode, strAddress);
                    } else if (i == 1) {
                        return; //do nothing
                    } else if (i == 2) {
                        this.dispose(); //exit window
                        return;
                    } else {
                        return; //do nothing
                    }

                }

//            }
            //clear the fields and set the focus to the first textfield
            txtVenue.setText("");
           setdates();
            txtAddress.setText("");
            cmbArea.setSelectedIndex(0);
            cmbArea.requestFocus();

            //inform user for succesfull process
            JOptionPane.showMessageDialog(this, "New schedule has been successfully set!");
        }

    }

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCreateActionPerformed
        add();
    }//GEN-LAST:event_cmdCreateActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

       setdates();

        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM areaTBL ORDER BY area_desc;";

        try {
            stmtSelectAllAreas = conn.createStatement();
            ResultSet rs = stmtSelectAllAreas.executeQuery(createString);

            while (rs.next()) {
                cmbArea.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            stmtSelectAllAreas.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }//GEN-LAST:event_formInternalFrameOpened

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

    private void cmbAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAreaActionPerformed
        try {
            Item item = (Item) cmbArea.getSelectedItem();
            areaCode = item.getId();
        } catch (Exception e) {
        }

    }//GEN-LAST:event_cmbAreaActionPerformed

    private void cmdCreateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmdCreateKeyPressed
        add();
    }//GEN-LAST:event_cmdCreateKeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbArea;
    private javax.swing.JButton cmdCreate;
    private javax.swing.JButton cmdExit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtVenue;
    private com.toedter.calendar.JDateChooser txtdate;
    // End of variables declaration//GEN-END:variables
}
