/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;
import memsys.global.ReportFactory;

/**
 *
 * @author cadizlester
 */
public final class ConnectOrder extends javax.swing.JInternalFrame {

    public static int month, towncode, stat;
    public static String monthname, townname, statdesc;
    static Statement stmt;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();

    public ConnectOrder() {
        initComponents();
        //populateComboType();
        populateCMBMonth();
        populateCMBDistrict();
        populateCMBStatus();
        setdates();
    }

    void setdates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date testDate = null;
        try {
            testDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        txtfrom.setDate(testDate);
        txtto.setDate(testDate);
    }

    public void populateCMBDistrict() {
        cmbdistrict.addItem("--SELECT--");
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT areacode, area_desc, tcode FROM AreaTBL";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbdistrict.addItem(new Item2(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
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

    public void populateCMBStatus() {
        cmbstat.addItem("--SELECT--");
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT status, statdesc FROM connStatTBL";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbstat.addItem(new Item3(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    class Item3 {

        private int id;
        private String description;

        public Item3(int id, String description) {
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

    public void populateCMBMonth() {
        //Populate Combo Area
        cmbmonth.addItem("--SELECT--");
        cmbmonth.addItem(new Item(1, "January".toUpperCase()));
        cmbmonth.addItem(new Item(2, "February".toUpperCase()));
        cmbmonth.addItem(new Item(3, "March".toUpperCase()));
        cmbmonth.addItem(new Item(4, "April".toUpperCase()));
        cmbmonth.addItem(new Item(5, "May".toUpperCase()));
        cmbmonth.addItem(new Item(6, "June".toUpperCase()));
        cmbmonth.addItem(new Item(7, "July".toUpperCase()));
        cmbmonth.addItem(new Item(8, "August".toUpperCase()));
        cmbmonth.addItem(new Item(9, "September".toUpperCase()));
        cmbmonth.addItem(new Item(10, "October".toUpperCase()));
        cmbmonth.addItem(new Item(11, "November".toUpperCase()));
        cmbmonth.addItem(new Item(12, "December".toUpperCase()));
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

//    public void populateComboType() {
//        cmbtype.addItem(new Item(1, "Per Month"));
//        cmbtype.addItem(new Item(2, "Per District"));
//        cmbtype.addItem(new Item(3, "Per Status"));
//        cmbtype.addItem(new Item(4, "Custom Date"));
//    }
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbmonth = new javax.swing.JComboBox();
        yr = new com.toedter.calendar.JYearChooser();
        generateButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        generateButton2 = new javax.swing.JButton();
        cmbdistrict = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        cmbstat = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        generateButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        generateButton = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        txtfrom = new com.toedter.calendar.JDateChooser();
        txtto = new com.toedter.calendar.JDateChooser();
        jLabel49 = new javax.swing.JLabel();
        exitButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Reports on Connect Orders");
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

        jLabel2.setText("Month:");

        cmbmonth.setMaximumRowCount(20);
        cmbmonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbmonthActionPerformed(evt);
            }
        });

        yr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        yr.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                yrHierarchyChanged(evt);
            }
        });
        yr.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                yrAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                yrAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        yr.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
                yrAncestorMoved1(evt);
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
            }
        });
        yr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                yrMouseClicked(evt);
            }
        });
        yr.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                yrCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                yrInputMethodTextChanged(evt);
            }
        });
        yr.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                yrPropertyChange(evt);
            }
        });
        yr.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                yrVetoableChange(evt);
            }
        });

        generateButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        generateButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logs.png"))); // NOI18N
        generateButton3.setMnemonic('A');
        generateButton3.setText("Generate");
        generateButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(generateButton3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmbmonth, 0, 214, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yr, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(166, 166, 166))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(cmbmonth, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yr, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generateButton3)
                .addContainerGap(94, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Per Month", jPanel1);

        generateButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        generateButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logs.png"))); // NOI18N
        generateButton2.setMnemonic('A');
        generateButton2.setText("Generate");
        generateButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButton2ActionPerformed(evt);
            }
        });

        cmbdistrict.setToolTipText("Municipal/City");
        cmbdistrict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbdistrictActionPerformed(evt);
            }
        });

        jLabel11.setText("District:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generateButton2)
                    .addComponent(cmbdistrict, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(188, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbdistrict)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generateButton2)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Per District", jPanel2);

        cmbstat.setToolTipText("Municipal/City");
        cmbstat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbstatActionPerformed(evt);
            }
        });

        jLabel10.setText("Status:");

        generateButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        generateButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logs.png"))); // NOI18N
        generateButton1.setMnemonic('A');
        generateButton1.setText("Generate");
        generateButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generateButton1)
                    .addComponent(cmbstat, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(201, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbstat)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generateButton1)
                .addContainerGap(89, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Per Status", jPanel3);

        jLabel1.setText("Date Range:");

        generateButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        generateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logs.png"))); // NOI18N
        generateButton.setMnemonic('A');
        generateButton.setText("Generate");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        jLabel41.setForeground(new java.awt.Color(255, 102, 0));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("FROM");

        txtfrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 153, 0)));
        txtfrom.setDateFormatString("yyyy-MM-dd");

        txtto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 153, 0)));
        txtto.setDateFormatString("yyyy-MM-dd");

        jLabel49.setForeground(new java.awt.Color(255, 102, 0));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("UNTIL");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generateButton)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtfrom, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(140, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(txtfrom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel49))
                .addGap(18, 18, 18)
                .addComponent(generateButton)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Custom Date", jPanel4);

        exitButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        exitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        exitButton.setMnemonic('x');
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sd = dateFormat.format(txtfrom.getDate());
        String ed = dateFormat.format(txtto.getDate());
        System.out.println(sd);
        System.out.println(ed);
        Date sDt = null;
        Date eDt = null;

        try {
            sDt = dateFormat.parse(sd);
            eDt = dateFormat.parse(ed);

            ReportFactory.rptConnectOrderPerCustomDate(sDt,eDt, "From "+sd +" to "+ ed);

        } catch (ParseException ex) {
            ex.getMessage();
        }

    }//GEN-LAST:event_generateButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void cmbstatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbstatActionPerformed
        stat = 0;
        try {
            Item3 item = (Item3) cmbstat.getSelectedItem();
            stat = item.getId();
            statdesc = item.getDescription();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbstatActionPerformed

    private void generateButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButton1ActionPerformed
        if ("--SELECT--".equals(cmbstat.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(null, "Please select a status!");
        } else {
            ReportFactory.rptConnectOrderPerStatus(stat, statdesc.trim());
        }
    }//GEN-LAST:event_generateButton1ActionPerformed

    private void generateButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButton2ActionPerformed
        if ("--SELECT--".equals(cmbdistrict.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(null, "Please select a district!");
        } else {
            ReportFactory.rptConnectOrderPerDistrict(towncode, "District of " + townname.trim());
        }
    }//GEN-LAST:event_generateButton2ActionPerformed

    private void cmbdistrictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbdistrictActionPerformed
        towncode = 0;
        try {
            Item2 item = (Item2) cmbdistrict.getSelectedItem();
            towncode = item.getId();
            townname = item.getDescription();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbdistrictActionPerformed

    private void cmbmonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbmonthActionPerformed
        month = 0;
        try {
            Item item = (Item) cmbmonth.getSelectedItem();
            month = item.getId();
            monthname = item.getDescription();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbmonthActionPerformed

    private void yrHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_yrHierarchyChanged

    }//GEN-LAST:event_yrHierarchyChanged

    private void yrAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_yrAncestorMoved

    }//GEN-LAST:event_yrAncestorMoved

    private void yrAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_yrAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_yrAncestorAdded

    private void yrAncestorMoved1(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_yrAncestorMoved1
        // setTableModels();
    }//GEN-LAST:event_yrAncestorMoved1

    private void yrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_yrMouseClicked

    }//GEN-LAST:event_yrMouseClicked

    private void yrCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_yrCaretPositionChanged

    }//GEN-LAST:event_yrCaretPositionChanged

    private void yrInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_yrInputMethodTextChanged

    }//GEN-LAST:event_yrInputMethodTextChanged

    private void yrPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_yrPropertyChange
        //        setTableModels();
    }//GEN-LAST:event_yrPropertyChange

    private void yrVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_yrVetoableChange

    }//GEN-LAST:event_yrVetoableChange

    private void generateButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButton3ActionPerformed
        if ("--SELECT--".equals(cmbmonth.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(null, "Please select a month period!");
        } else {
            ReportFactory.rptConnectOrderPerMonth(month, yr.getYear(), monthname + " - " + yr.getYear());
        }
    }//GEN-LAST:event_generateButton3ActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
    }//GEN-LAST:event_formInternalFrameOpened


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbdistrict;
    private javax.swing.JComboBox cmbmonth;
    private javax.swing.JComboBox cmbstat;
    private javax.swing.JButton exitButton;
    private javax.swing.JButton generateButton;
    private javax.swing.JButton generateButton1;
    private javax.swing.JButton generateButton2;
    private javax.swing.JButton generateButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JDateChooser txtfrom;
    private com.toedter.calendar.JDateChooser txtto;
    private com.toedter.calendar.JYearChooser yr;
    // End of variables declaration//GEN-END:variables
}
