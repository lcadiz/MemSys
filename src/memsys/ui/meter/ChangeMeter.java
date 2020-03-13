/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.meter;

import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;

/**
 *
 * @author cadizlester
 */
public class ChangeMeter extends javax.swing.JDialog {

    public static ChangeRemoveMeter frmParent;
    public static int acctno;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static SearchCrewOnChange frmSearchCrewOnChange;
    public static SearchMeter frmSearchMeter;
    static int meterissuedtoid, installerid, newmpid;
    static String newmetersn;
    static double initreading, demandreading;

    public ChangeMeter(ChangeRemoveMeter parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);

        tbllog.setCellSelectionEnabled(false);
        tbllog.setRowSelectionAllowed(true);
        tbllog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbllog.setSelectionBackground(new Color(153, 204, 255));
        tbllog.setSelectionForeground(Color.BLACK);

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

        populateTBL();
        populateTBLInfo();
        populateTBLDetails();
        setdates();

    }

    public void showFrmSearchCrew() {
        frmSearchCrewOnChange = new SearchCrewOnChange(this, true);
        frmSearchCrewOnChange.setVisible(true);
    }

    public void showFrmSearchMeter() {
        frmSearchMeter = new SearchMeter(this, true);
        frmSearchMeter.setVisible(true);
    }

    public void LoadNewMeter(int mpid, String mtrsn, String nym, double ir, double dr) {
        txtmeterdesc.setText("METERSN:" + mtrsn + "-" + nym);
        newmpid = mpid;
        newmetersn = mtrsn;
        initreading = ir;
        demandreading = dr;
        // txtdatereplaced.requestFocus();
    }

    
    public void LoadInstaller(int cid, String cnym) {
        txtpb.setText(cnym);
        installerid = cid;
        txtdatereplaced.requestFocus();
    }

    public void LoadIssuedTo(int cid, String cnym) {
        txtissuedto.setText(cnym);
        meterissuedtoid = cid;
        // txtdatereplaced.requestFocus();
    }

    void setdates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date theDate = null;
        try {
            theDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        txtremdate.setDate(theDate);
        txtdatereplaced.setDate(theDate);
    }

    void populateTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        createString = "SELECT * FROM MeterStatusLog m INNER JOIN MeterStatus s ON m.MSCODE=s.MSCODE WHERE AcctNo=" + acctno + " ORDER BY TransDate DESC";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);
            model = (DefaultTableModel) tbllog.getModel();
            renderer.setHorizontalAlignment(0);
            tbllog.setRowHeight(29);
            model.setNumRows(0);

            int cnt = 0;
            while (rs.next()) {
                model.addRow(new Object[]{rs.getDate("TransDate"), rs.getDate("ChangeDate"), rs.getString("MeterSN"), rs.getString("MSDesc"), rs.getString("Remarks")});
                cnt++;
            }
            if (cnt != 0) {
                tbllog.setRowSelectionInterval(0, 0);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    double GetInitReading() {
        double m = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT InitReading FROM MeterPostTBL WHERE mpID=" + newmpid;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                m = rs.getInt(1);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return m;
    }

    int GetMeterMultiplier() {
        int m = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT Multiplier FROM MeterPostTBL WHERE mpID=" + newmpid;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                m = rs.getInt(1);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return m;
    }

    void populateTBLDetails() {
        Connection conn = MainDBConn.getConnection();
        String createString = "SELECT * FROM ConsumerMeter c INNER JOIN Meter m ON c.MeterSN=m.MeterSN WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                // Date lrd =rs.getDate("PresRdng");
                lblmtrsn.setText(rs.getString("MeterSN"));
                lbllrd.setText(rs.getDate("RdngDate").toString());
                lbllr.setText(rs.getString("PresRdng"));
                lblld.setText(rs.getString("PresDemand"));
                lbltm.setText(rs.getString("TotalMultiplier"));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void populateTBLInfo() {
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        createString = "SELECT * FROM Consumer WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);
            model2 = (DefaultTableModel) tbl.getModel();
            renderer.setHorizontalAlignment(0);
            tbl.setRowHeight(29);
            model2.setNumRows(0);

            while (rs.next()) {
                model2.addRow(new Object[]{"Account Number", rs.getString("AcctNo")});
                model2.addRow(new Object[]{"Area Code", rs.getString("TownCode") + "-" + rs.getString("RouteCode") + "-" + rs.getString("RouteSeqNo")});
                model2.addRow(new Object[]{"Account Name", rs.getString("AcctName")});
                model2.addRow(new Object[]{"Address", rs.getString("AcctAddress")});
                model2.addRow(new Object[]{"Class", rs.getString("ClassCode")});
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblmtrsn = new javax.swing.JLabel();
        lblld = new javax.swing.JLabel();
        lbllrd = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtremdate = new com.toedter.calendar.JDateChooser();
        txtpr = new javax.swing.JFormattedTextField();
        txtremarks = new javax.swing.JTextField();
        txtpb = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        cmdpost = new javax.swing.JButton();
        txtpd = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        txtdatereplaced = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        txtos = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtmos = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtissuedto = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtmeterdesc = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        lbltm = new javax.swing.JLabel();
        lbllr = new javax.swing.JFormattedTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbllog = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Change Meter");

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Information"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbl);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("MeterSN:");

        jLabel2.setText("Last Reading Date:");

        jLabel3.setText("Last Reading:");

        jLabel4.setText("Last Demand:");

        lblmtrsn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblmtrsn.setForeground(new java.awt.Color(0, 102, 0));
        lblmtrsn.setText("0");
        lblmtrsn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblld.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblld.setForeground(new java.awt.Color(0, 102, 0));
        lblld.setText("0");
        lblld.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lbllrd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbllrd.setForeground(new java.awt.Color(0, 102, 0));
        lbllrd.setText("0");
        lbllrd.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel5.setText("Removal Date:");

        jLabel6.setText("Pull-out Reading:");

        jLabel7.setText("Installed by:");

        jLabel8.setText("Reason of replacement:");

        txtremdate.setDateFormatString("yyyy-MM-dd");

        txtpr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0"))));
        txtpr.setText("0.0");
        txtpr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtprFocusGained(evt);
            }
        });
        txtpr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtprMouseClicked(evt);
            }
        });

        txtremarks.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtremarksActionPerformed(evt);
            }
        });

        txtpb.setEnabled(false);
        txtpb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtpbActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/msupplier.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cmdpost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdpost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/refresh.png"))); // NOI18N
        cmdpost.setMnemonic('e');
        cmdpost.setText("Replace Meter");
        cmdpost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdpostActionPerformed(evt);
            }
        });

        txtpd.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0"))));
        txtpd.setText("0.0");
        txtpd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtpdFocusGained(evt);
            }
        });
        txtpd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtpdMouseClicked(evt);
            }
        });

        jLabel9.setText("Pull-out Demand:");

        txtdatereplaced.setDateFormatString("yyyy-MM-dd");

        jLabel10.setText("Date Replaced:");

        txtos.setText("0");
        txtos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtosActionPerformed(evt);
            }
        });

        jLabel11.setText("Outer Seal:");

        jLabel12.setText("Meter Outer Seal:");

        txtmos.setText("0");
        txtmos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmosActionPerformed(evt);
            }
        });

        jLabel13.setText("Meter Issued to:");

        txtissuedto.setEnabled(false);
        txtissuedto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtissuedtoActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/msupplier.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel14.setText("New Meter:");

        txtmeterdesc.setEnabled(false);
        txtmeterdesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmeterdescActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/meter.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel15.setText("Multiplier:");

        lbltm.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbltm.setForeground(new java.awt.Color(0, 102, 0));
        lbltm.setText("0");
        lbltm.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lbllr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0"))));
        lbllr.setText("0.0");
        lbllr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                lbllrFocusGained(evt);
            }
        });
        lbllr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbllrMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbllr, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblld, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbltm, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbllrd, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblmtrsn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9)
                    .addComponent(jLabel14)
                    .addComponent(jLabel7)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel13))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtremarks)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtpd, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtpr, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtremdate, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                                .addComponent(txtmos, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(txtdatereplaced, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdpost, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtmeterdesc, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtpb, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtissuedto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblmtrsn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel2)
                            .addComponent(lbllrd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel3)
                            .addComponent(lbllr, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblld, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel13)
                            .addComponent(txtissuedto, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(123, 123, 123)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel14)
                                .addComponent(txtmeterdesc, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(122, 122, 122)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(11, 11, 11)
                            .addComponent(txtremdate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtpr, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel9)
                                .addComponent(txtpd, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.CENTER, jPanel1Layout.createSequentialGroup()
                            .addGap(160, 160, 160)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbltm, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton2, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtpb, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel10)
                    .addComponent(txtdatereplaced, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11)
                    .addComponent(txtos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel12)
                    .addComponent(txtmos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel8)
                    .addComponent(txtremarks, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdpost)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbllog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TransDate", "ChangeDate", "MeterSN", "Status", "LogDetails"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbllog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtprFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtprFocusGained
        txtpr.selectAll();
    }//GEN-LAST:event_txtprFocusGained

    private void txtprMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtprMouseClicked
        txtpr.selectAll();
    }//GEN-LAST:event_txtprMouseClicked

    private void txtremarksActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtremarksActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtremarksActionPerformed

    private void txtpbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtpbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpbActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        SearchCrewOnChange.flg = 2;
        showFrmSearchCrew();
    }//GEN-LAST:event_jButton1ActionPerformed

    boolean isInstalled(String MeterSN) {
        boolean isExist = false;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select * from ConsumerMeter where MeterSN='" + MeterSN + "'";

        int c = 0;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                c++;
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        if (c > 0) {
            isExist = true;
        }

        return isExist;

    }


    private void cmdpostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdpostActionPerformed
        if (txtremarks.getText().isEmpty() == true) {
            JOptionPane.showMessageDialog(this, "Remarks is required!");
        } else {
            if (txtpr.getText().isEmpty() == true) {
                JOptionPane.showMessageDialog(this, "Please fill-up all the required fields!");
            } else {
                boolean isIntalledAlready = isInstalled(newmetersn);
                if (isIntalledAlready == true) {
                    JOptionPane.showMessageDialog(this, "Meter is already assigned to another consumer!");
                } else if (isIntalledAlready == false) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String strDate = dateFormat.format(txtdatereplaced.getDate());

                    double lastr = Double.valueOf(lbllr.getText().replace(",", ""));
                    double presentr = Double.valueOf(txtpr.getText().replace(",", ""));
                    double lastd = Double.valueOf(lblld.getText().replace(",", ""));
                    double presentd = Double.valueOf(txtpd.getText().replace(",", ""));

                    MeterProcess mp = new MeterProcess();
                    mp.ChangeMeter(acctno, strDate, txtos.getText(), txtmos.getText(), installerid, strDate, lblmtrsn.getText(), newmetersn, txtissuedto.getText(), lbltm.getText(), lbllrd.getText(), strDate,
                            lastr,
                            presentr,
                            presentr - lastr,
                            lastd,
                            presentd, txtremarks.getText(), nowDate, nowDate, initreading, demandreading, 0, newmpid, GetMeterMultiplier());
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "Meter Replacement Successfully Done!");
                }
            }
        }
    }//GEN-LAST:event_cmdpostActionPerformed

    private void txtpdFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtpdFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpdFocusGained

    private void txtpdMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtpdMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpdMouseClicked

    private void txtosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtosActionPerformed

    private void txtmosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmosActionPerformed

    private void txtissuedtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtissuedtoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtissuedtoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        SearchCrewOnChange.flg = 1;
        showFrmSearchCrew();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtmeterdescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmeterdescActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmeterdescActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        showFrmSearchMeter();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void lbllrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lbllrFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_lbllrFocusGained

    private void lbllrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbllrMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbllrMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChangeMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChangeMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChangeMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChangeMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChangeMeter dialog = new ChangeMeter(frmParent, true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdpost;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblld;
    private javax.swing.JFormattedTextField lbllr;
    private javax.swing.JLabel lbllrd;
    private javax.swing.JLabel lblmtrsn;
    private javax.swing.JLabel lbltm;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tbllog;
    private com.toedter.calendar.JDateChooser txtdatereplaced;
    private javax.swing.JTextField txtissuedto;
    private javax.swing.JTextField txtmeterdesc;
    private javax.swing.JTextField txtmos;
    private javax.swing.JTextField txtos;
    private javax.swing.JTextField txtpb;
    private javax.swing.JFormattedTextField txtpd;
    private javax.swing.JFormattedTextField txtpr;
    private javax.swing.JTextField txtremarks;
    private com.toedter.calendar.JDateChooser txtremdate;
    // End of variables declaration//GEN-END:variables
}
