package memsys.ui.process;

import memsys.global.DBConn.MainDBConn;
import memsys.global.DBConn.MainDBConn;
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import com.lowagie.text.Font;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public final class CostingOp extends javax.swing.JDialog {

    public static CostingMain frmParent;
    public static int acctno, edflag, eid;
    public static String acctname;
    static Statement stmt;
    static String nowDate = myFunctions.getDate();
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer renderer3 = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    public static EditCharges frmEdit;
    public static String qty, cost, amountc, descrip, address;
    public static int rsel;
    public static boolean chk;
    public static int typ;
    public static SetElectricianCosting frmSetElectrician;

    public CostingOp(CostingMain parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        model = (DefaultTableModel) jTblCosting.getModel();

        populateCMBType();
        jTblCosting.setCellSelectionEnabled(false);
        jTblCosting.setRowSelectionAllowed(true);
        jTblCosting.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTblCosting.setSelectionBackground(new Color(153, 204, 255));
        jTblCosting.setSelectionForeground(Color.BLACK);
        // lblnym.setText(acctname);
        //   lblaccount.setText(String.valueOf(acctno));
        txtx.setVisible(false);
        x.setVisible(false);
        load();

    }

    public void showFrmEditElectrician() {
        frmSetElectrician = new SetElectricianCosting(this, true);
        frmSetElectrician.setVisible(true);
    }

    public void SetElectrician(int ceid, String nym) {
        eid = ceid;
        lblenym.setText(nym);
        // txtdatereplaced.requestFocus();
    }

    private void UpdateElectricianID(int id) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE connCOInfoTBL "
                + "SET eID=" + id
                + " WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void showFrmEdit() {
        frmEdit = new EditCharges(this, true);
        frmEdit.setVisible(true);
    }

    public void UpdateChanges() {
        jTblCosting.setValueAt(amountc, rsel, 7);
        jTblCosting.setValueAt(qty, rsel, 4);
        jTblCosting.setValueAt(cost, rsel, 6);
        jTblCosting.setValueAt(descrip, rsel, 3);
        refreshTotal();
    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();
        String createString;
//        createString = "SELECT costSetID, COAID,description, qty, unit, cost, cost*qty as total "
//                + "FROM costingSetupTBL ORDER BY description";
        //stmtIncomingShed

        createString = "SELECT c.COAID, COADesc, u.qty, u.unit,  amount, u.qty*amount as total, u.defid "
                + "FROM COA c INNER JOIN CostingUQSetupTBL u ON c.COAID=u.COAID WHERE CTypeID=" + typ + " order by COADesc";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) jTblCosting.getModel();

            renderer.setHorizontalAlignment(0);
            renderer2.setHorizontalAlignment(SwingConstants.RIGHT);

            jTblCosting.setRowHeight(29);
            jTblCosting.getColumnModel().getColumn(1).setCellRenderer(renderer);
            jTblCosting.getColumnModel().getColumn(2).setCellRenderer(renderer);
            jTblCosting.getColumnModel().getColumn(4).setCellRenderer(renderer);
            jTblCosting.getColumnModel().getColumn(5).setCellRenderer(renderer);
            //  jTblCosting.getColumnModel().getColumn(6).setCellRenderer(renderer2);
            //jTblCosting.getColumnModel().getColumn(7).setCellRenderer(renderer2);
            //jTblCosting.getColumnModel().getColumn(8).setCellRenderer(renderer3);
            jTblCosting.getColumn("Edit").setCellRenderer(new ButtonRenderer(1));
            jTblCosting.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), 1));
            jTblCosting.getColumn("Amount").setCellRenderer(new ColumnBold());
            jTblCosting.getColumn("Cost").setCellRenderer(new ColumnBold2());
            //jTblCosting.getColumn("Qty").setCellRenderer(new ColumnBold2());
            jTblCosting.setColumnSelectionAllowed(false);

            model.setNumRows(0);

            int cid = 1;

            while (rs.next()) {

                double amnt = Double.parseDouble(rs.getString(5));
                String amnt2 = myFunctions.amountFormat(amnt);

                double amnt1 = Double.parseDouble(rs.getString(6));
                String amnt3 = myFunctions.amountFormat(amnt1);

                int def = rs.getInt(7);

                if (def == 0) {
                    chk = false;
                } else if (def == 1) {
                    chk = true;
                }
                //   String lbl = "<html><table border=0 cellpadding=0 cellspacing=0>"
                //     + "<tr><td><img src=" + getClass().getResource("/images/acctmini.png") + ">&nbsp</td><td>" + rs.getString(2) + "</td></th>";
                model.addRow(new Object[]{chk, cid, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4).trim(), amnt2, amnt3, ""});
                cid++;

            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    int GetCType() {
        int tid = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT CTypeID  FROM connCostingTypeTBL WHERE AcctNo=" + acctno;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                tid = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return tid;
    }

    void load() {
        //caption of the form
        // this.setTitle("Costing//" + "AccountNo:" + acctno + "/" + acctname);

        //check if the acct is costed already or not.
        boolean x = myDataenvi.checkCosting(acctno);
        if (x == true) {

            chk = false;

            typ = GetCType();
            cmbtyp.setSelectedIndex(typ);
            populateTBL();
            setCheckCharges(acctno);

            int bl = determineIfBigloads(acctno);

            if (bl > 0) {
                edflag = 0;

                //setting Energy deposit variables
                String LC = setVariables(acctno, "LC");
                String AHPDU = setVariables(acctno, "AHPDU");
                String MO = setVariables(acctno, "MO");
                String KWPHR = setVariables(acctno, "KWPHR ");
                String CV = setVariables(acctno, "CV");
                String LF = setVariables(acctno, "LF");
                String CM = setVariables(acctno, "CM");

                txtlc.setText(LC);
                txtah.setText(AHPDU);
                txtmo.setText(MO);
                txtkwhr.setText(KWPHR);
                txtcv.setText(CV);
                txtlf.setText(LF);
                txtcm.setText(CM);

                populateCalculationsTBL(acctno);

            } else {
                edflag = 1;
            }
        } else {
            // System.out.println(x);
            edflag = 0;
            //chk = true;
//            rsInsertCostingType();
            //populateTBL();

        }

        refreshTotal();//end here.

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        cmdSave = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        cmdExit = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        tab = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTblCosting = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        cmbtyp = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        lbltotal = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblenym = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        a = new javax.swing.JPanel();
        edpane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtkwhr = new javax.swing.JTextField();
        txtmo = new javax.swing.JTextField();
        txtah = new javax.swing.JTextField();
        txtlc = new javax.swing.JTextField();
        txtcm = new javax.swing.JTextField();
        txtlf = new javax.swing.JTextField();
        txtcv = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        cmdCalc = new javax.swing.JButton();
        txtx = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        x = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTblED = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtremarks = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Payment Costing");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.setRollover(true);

        cmdSave.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        cmdSave.setMnemonic('S');
        cmdSave.setText("       Save       ");
        cmdSave.setFocusable(false);
        cmdSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdSave);
        jToolBar1.add(jSeparator1);

        cmdExit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdExit.setMnemonic('x');
        cmdExit.setText("        Exit        ");
        cmdExit.setFocusable(false);
        cmdExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdExit);
        jToolBar1.add(jSeparator2);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tab.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabMousePressed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTblCosting.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "CostID", "COAID", "Description", "Qty", "Unit", "Cost", "Amount", "Edit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblCosting.setToolTipText("");
        jTblCosting.getTableHeader().setReorderingAllowed(false);
        jTblCosting.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTblCostingMouseMoved(evt);
            }
        });
        jTblCosting.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblCostingMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTblCostingMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTblCostingMouseReleased(evt);
            }
        });
        jTblCosting.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTblCostingKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTblCosting);
        if (jTblCosting.getColumnModel().getColumnCount() > 0) {
            jTblCosting.getColumnModel().getColumn(0).setResizable(false);
            jTblCosting.getColumnModel().getColumn(1).setResizable(false);
            jTblCosting.getColumnModel().getColumn(2).setResizable(false);
            jTblCosting.getColumnModel().getColumn(4).setResizable(false);
            jTblCosting.getColumnModel().getColumn(5).setResizable(false);
            jTblCosting.getColumnModel().getColumn(8).setResizable(false);
        }
        //set column width
        jTblCosting.getColumnModel().getColumn(0).setMaxWidth(20);
        jTblCosting.getColumnModel().getColumn(1).setMaxWidth(50);
        jTblCosting.getColumnModel().getColumn(2).setMaxWidth(50);
        jTblCosting.getColumnModel().getColumn(4).setMaxWidth(50);
        jTblCosting.getColumnModel().getColumn(5).setMaxWidth(80);
        jTblCosting.getColumnModel().getColumn(6).setMaxWidth(80);
        jTblCosting.getColumnModel().getColumn(7).setMaxWidth(80);
        jTblCosting.getColumnModel().getColumn(8).setMaxWidth(50);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Type:");

        cmbtyp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cmbtyp.setForeground(new java.awt.Color(102, 102, 102));
        cmbtyp.setToolTipText("Municipal/City");
        cmbtyp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtypActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 153, 204));
        jLabel12.setText(" TOTAL");

        lbltotal.setBackground(new java.awt.Color(0, 153, 204));
        lbltotal.setFont(new java.awt.Font("Calibri", 1, 48)); // NOI18N
        lbltotal.setForeground(new java.awt.Color(255, 255, 255));
        lbltotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbltotal.setText("0.00");
        lbltotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 153)));
        lbltotal.setOpaque(true);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Electrician:");

        lblenym.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblenym.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/msupplier.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbtyp, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 266, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbltotal, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(lblenym, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(cmbtyp, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lbltotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 402, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addContainerGap())
                    .addComponent(lblenym, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                    .addContainerGap(79, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(43, 43, 43)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tab.addTab("Charges", jPanel3);

        edpane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Load Current:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Average Hrs./Day Usaged:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("No. of Months (Advance Load Deposit):");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Kilowatt per Hr Rate:");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Current Voltage:");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Load Factor:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Constant Multiflier:");

        txtkwhr.setToolTipText("");

        txtmo.setToolTipText("");

        txtah.setToolTipText("");

        txtlc.setToolTipText("");

        txtcm.setText("0.9");
        txtcm.setToolTipText("");
        txtcm.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        txtlf.setText("0.45");
        txtlf.setToolTipText("");
        txtlf.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        txtcv.setText("0.240");
        txtcv.setToolTipText("");
        txtcv.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        cmdCalc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calculator.png"))); // NOI18N
        cmdCalc.setMnemonic('C');
        cmdCalc.setText("Calculate");
        cmdCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCalcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(cmdCalc, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(85, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(83, Short.MAX_VALUE)
                .addComponent(cmdCalc, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );

        txtx.setText("0.0");
        txtx.setToolTipText("");
        txtx.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setForeground(new java.awt.Color(255, 102, 51));
        jRadioButton1.setText("Average Load");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setForeground(new java.awt.Color(255, 102, 51));
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Induction Load");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("KW");

        x.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        x.setText("KW");

        javax.swing.GroupLayout edpaneLayout = new javax.swing.GroupLayout(edpane);
        edpane.setLayout(edpaneLayout);
        edpaneLayout.setHorizontalGroup(
            edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(edpaneLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(edpaneLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(edpaneLayout.createSequentialGroup()
                                .addComponent(txtlc, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14))
                            .addGroup(edpaneLayout.createSequentialGroup()
                                .addComponent(jRadioButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtx, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(x))))
                    .addGroup(edpaneLayout.createSequentialGroup()
                        .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtah, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtmo, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtkwhr, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtcm, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcv, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtlf, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        edpaneLayout.setVerticalGroup(
            edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(edpaneLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtlc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(x)
                    .addComponent(txtx, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(txtah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(txtmo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtlf, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(edpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtkwhr, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcm, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
            .addGroup(edpaneLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTblED.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Description", "Unit", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTblED.setToolTipText("");
        jTblED.getTableHeader().setReorderingAllowed(false);
        jTblED.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTblEDMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTblEDMouseReleased(evt);
            }
        });
        jTblED.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTblEDMouseMoved(evt);
            }
        });
        jScrollPane2.setViewportView(jTblED);
        if (jTblED.getColumnModel().getColumnCount() > 0) {
            jTblED.getColumnModel().getColumn(1).setResizable(false);
        }
        //set column width
        //jTblCosting.getColumnModel().getColumn(1).setMaxWidth(80);
        //jTblCosting.getColumnModel().getColumn(2).setMaxWidth(100);

        jLabel10.setText("ENERGY DEPOSIT CALCULATIONS");

        jLabel13.setText("REMARKS");

        javax.swing.GroupLayout aLayout = new javax.swing.GroupLayout(a);
        a.setLayout(aLayout);
        aLayout.setHorizontalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(edpane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 968, Short.MAX_VALUE)
                    .addComponent(txtremarks)
                    .addGroup(aLayout.createSequentialGroup()
                        .addGroup(aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel13))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        aLayout.setVerticalGroup(
            aLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(edpane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtremarks, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tab.addTab("Energy Deposit", a);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(tab, javax.swing.GroupLayout.PREFERRED_SIZE, 995, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(tab, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 23, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    public static String setVariables(int acctno, String acronym) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT EDvalue FROM EDVariablesTBL WHERE AcctNo=" + acctno + " AND acronym='" + acronym + "'";
        //stmtIncomingShed 

        String outv = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                outv = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return outv;
    }

    public void setCheckCharges(int acctno) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT costsetID, description, qty, cost, total, COAID FROM costingTempTBL WHERE AcctNo=" + acctno;
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                String cid = rs.getString(1);
                String coa = rs.getString(6);
                String desc = rs.getString(2);
                String cqty = rs.getString(3);
                String ccost = myFunctions.amountFormat2(rs.getString(4));
                String total = myFunctions.amountFormat2(rs.getString(5));
                int rows = jTblCosting.getModel().getRowCount();
                for (int i = 0; i < rows; i++) {
                    String value = jTblCosting.getValueAt(i, 1).toString();
                    // System.out.println(value);
                    if (value.equals(cid)) {
//                        String lbl = "<html><table border=0 cellpadding=0 cellspacing=0>"
//                                + "<tr><td><img src=" + getClass().getResource("/images/acctmini.png") + ">&nbsp</td><td>" + desc + "</td></th>";
                        jTblCosting.setValueAt(true, i, 0);
                        jTblCosting.setValueAt(desc, i, 3);
                        jTblCosting.setValueAt(cqty, i, 4);
                        jTblCosting.setValueAt(ccost, i, 6);
                        jTblCosting.setValueAt(total, i, 7);

                    }
                }
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void jTblCostingMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblCostingMouseMoved
}//GEN-LAST:event_jTblCostingMouseMoved

    private void jTblCostingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblCostingMouseClicked
    }//GEN-LAST:event_jTblCostingMouseClicked

    private void tabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMouseClicked
    }//GEN-LAST:event_tabMouseClicked

    private void tabMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabMousePressed
        //int x=tab.getSelectedIndex();

        if (edflag == 1 && tab.getSelectedIndex() == 1) {

            JOptionPane.showMessageDialog(null, "Energy Deposit is Disabled because it has been uncheck or not selected in the charges table! ");
            tab.setSelectedIndex(0);

        }

        //JOptionPane.showMessageDialog(null,x);
    }//GEN-LAST:event_tabMousePressed

    private void jTblCostingMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblCostingMouseReleased
        int col = 0; //set column value to 0
        int row = jTblCosting.getSelectedRow(); //get value of selected value

        String selected = jTblCosting.getValueAt(row, col).toString();
        String selcoaid = jTblCosting.getValueAt(row, 2).toString();

        if ("true".equals(selected) && "54".equals(selcoaid)) {
            edflag = 0;
        } else if ("false".equals(selected) && "54".equals(selcoaid)) {
            int i = myFunctions.msgboxYesNo("Are you sure you want uncheck this charges? NOTE: All computation in Energy Deposit TAB will be deleted!");
            if (i == 0) {
                edflag = 1;
                jTblCosting.setValueAt("0.00", row, 6);
                jTblCosting.setValueAt("0.00", row, 7);
                clrED();

            } else if (i == 1) {
                jTblCosting.setValueAt(true, row, 0);
                return; //do nothing
            } else if (i == 2) {
                jTblCosting.setValueAt(true, row, 0);
                return;
            }
        }

        refreshTotal();

        //JOptionPane.showMessageDialog(null, selected+selcoaid);
    }//GEN-LAST:event_jTblCostingMouseReleased

    private void jTblEDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblEDMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTblEDMouseClicked

    private void jTblEDMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblEDMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTblEDMouseReleased

    private void jTblEDMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblEDMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTblEDMouseMoved

    public void populateCalculationsTBL(int acctno) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT description, unit, cvalue FROM EDComputationTBL WHERE AcctNo=" + acctno;
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) jTblED.getModel();

            renderer.setHorizontalAlignment(0);
            renderer2.setHorizontalAlignment(SwingConstants.RIGHT);

            jTblED.setRowHeight(23);

            jTblED.getColumnModel().getColumn(1).setCellRenderer(renderer);
            jTblED.getColumnModel().getColumn(2).setCellRenderer(renderer2);
            jTblED.getColumn("Value").setCellRenderer(new ColumnBold());

            model.setNumRows(0);

            while (rs.next()) {

                double amnt = Double.parseDouble(rs.getString(3));
                String amnt2 = myFunctions.amountFormat(amnt);

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), amnt2});

            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cmdCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCalcActionPerformed
        try {
            if (txtlc.getText().isEmpty() == true
                    || txtah.getText().isEmpty() == true
                    || txtkwhr.getText().isEmpty() == true
                    || txtmo.getText().isEmpty() == true) {
                JOptionPane.showMessageDialog(this, "Please fill-up all the required fields! ");
                return;
            } else {
                double nmo = Double.parseDouble(txtmo.getText());
                if (nmo > 12) {
                    JOptionPane.showMessageDialog(this, "No. of months input is from 1 to 12 only! ");
                    return;
                } else {
                    //INITIALIZE VARIABLES
                    double lc = 0,
                            ah = 0,
                            kwhr = 0,
                            mo = 0,
                            cv = 0,
                            lf = 0,
                            cm = 0,
                            DEMAND = 0,
                            AVELOAD = 0,
                            EKWHPERMO = 0,
                            AMOUNT = 0;

                    //SET UP VALUES
                    lc = Double.parseDouble(txtlc.getText());
                    ah = Double.parseDouble(txtah.getText());
                    kwhr = Double.parseDouble(txtkwhr.getText());
                    mo = Double.parseDouble(txtmo.getText());
                    cv = Double.parseDouble(txtcv.getText());
                    lf = Double.parseDouble(txtlf.getText());
                    cm = Double.parseDouble(txtcm.getText());

                    //CALCULATE
                    DEMAND = ((cv * lc) * cm);            //calculate demand
                    AVELOAD = DEMAND * lf;                  //calculate average load
                    EKWHPERMO = ((AVELOAD * ah) * 30);    //calculate estimated kwh/month
                    AMOUNT = kwhr * (EKWHPERMO * mo);   //calculate total energy deposit

                    //jTblED
                    model = (DefaultTableModel) jTblED.getModel();

                    renderer.setHorizontalAlignment(0);
                    renderer2.setHorizontalAlignment(SwingConstants.RIGHT);

                    jTblED.setRowHeight(21);

                    jTblED.getColumnModel().getColumn(1).setCellRenderer(renderer);
                    jTblED.getColumnModel().getColumn(2).setCellRenderer(renderer2);
                    jTblED.getColumn("Value").setCellRenderer(new ColumnBold());

                    //model.setNumRows(0);
                    String dmd = myFunctions.amountFormat(DEMAND);
                    model.addRow(new Object[]{"DEMAND", "KW", dmd});

                    String al = myFunctions.amountFormat(AVELOAD);
                    model.addRow(new Object[]{"AVERAGE LOAD", "KW", al});

                    int cntr = 1;
                    String month = null;

                    while (cntr != mo + 1) {

                        double incamnt = EKWHPERMO * cntr;

                        if (cntr == 1) {
                            month = "MONTH";
                        } else {
                            month = "MONTHS";
                        }

                        String mword = monthInWords(Double.valueOf(cntr));

                        String amnt = myFunctions.amountFormat(incamnt);
                        model.addRow(new Object[]{"ESTIMATED KW FOR (" + cntr + ") " + mword + " " + month, "KWH", amnt});
                        cntr++;
                    }

                    String total = myFunctions.amountFormat(AMOUNT);
                    model.addRow(new Object[]{"AMOUNT", "Php", total});

                    //**update EDValue                         
                    //find COAID 54 for Energy Deposit and update value**// 
                    int rows = jTblCosting.getModel().getRowCount();
                    int col = 2;
                    int srw = 0;

                    for (int i = 0; i < rows; i++) {
                        String value = (String) jTblCosting.getModel().getValueAt(i, col);
                        if (value.equals("54")) {

                            jTblCosting.setValueAt(total, srw, 6);
                            jTblCosting.setValueAt(total, srw, 7);

                        }
                        srw++;
                        refreshTotal();
                        //UpdateChanges();
                    }

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid numeric input!");
//e.getMessage();
        }


    }//GEN-LAST:event_cmdCalcActionPerformed

    private void jTblCostingMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTblCostingMousePressed
    }//GEN-LAST:event_jTblCostingMousePressed

    public void addDatas() {
        int i = myFunctions.msgboxYesNo("Save the changes of this current costing?. Press YES to continue!");
        if (i == 0) {
            deleteAllCosting(acctno); //Clear all Exist Data 
            rsUpdateCostingType(typ);
            AddCharges();         //Add Selected Charges
            rsAddEDVariables();   //Add Energy Deposit Constant and User-defined values
            AddComputations();    //Add Energy Deposit Computations Ex.: Demands

            JOptionPane.showMessageDialog(null, "Costing Successfully Save!");
        } else if (i == 1) {
            //return; //do nothing
        } else if (i == 2) {
            //return;
        }

    }

    public void AddCharges() {
        int r = jTblCosting.getRowCount();
        int c;
        c = 0;

        while (c < r + 1) {
            try {
                String selTF = jTblCosting.getValueAt(c, 0).toString();
                String setID = jTblCosting.getValueAt(c, 1).toString();
                String vcoaid = jTblCosting.getValueAt(c, 2).toString();
                String vdesc = jTblCosting.getValueAt(c, 3).toString();
                String vqty = jTblCosting.getValueAt(c, 4).toString();
                String vunit = jTblCosting.getValueAt(c, 5).toString();
                String vcost = jTblCosting.getValueAt(c, 6).toString();
                String vtotal = jTblCosting.getValueAt(c, 7).toString();

                if ("true".equals(selTF)) {
                    rsInsertCharges(acctno, vdesc, vqty, vunit, Double.parseDouble(vcost.replace(",", "")), vcoaid, Double.parseDouble(vtotal.replace(",", "")), Integer.parseInt(setID));
                    //System.out.println(selTF);
                }

            } catch (Exception e) {
                e.getStackTrace();
            }
            c++;
        }
    }

    public void AddComputations() {
        int r = jTblED.getRowCount();
        int c;
        c = 0;

        while (c < r + 1) {
            try {
                String desc = jTblED.getValueAt(c, 0).toString();
                String unit = jTblED.getValueAt(c, 1).toString();
                String value = jTblED.getValueAt(c, 2).toString();

                rsInsertEDComputations(acctno, desc, unit, Double.parseDouble(value.replace(",", "")));

            } catch (Exception e) {
                e.getStackTrace();
            }
            c++;
        }
    }

    public static void rsInsertEDComputations(int edacctno, String eddescription, String edunit, double edcvalue) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO EDcomputationTBL (AcctNo, description, unit, cvalue) "
                + "VALUES ('" + edacctno + "','" + eddescription + "','" + edunit + "','" + edcvalue + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void jTblCostingKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTblCostingKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_E) {
            edit();
        }
    }//GEN-LAST:event_jTblCostingKeyPressed

    public static int determineIfBigloads(int AcctID) {

        int t = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT COAID FROM costingTempTBL WHERE AcctNo=" + AcctID + " AND COAID=54";
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                t++;

            }

            //t= rs.getFetchSize();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return t;

    }

    public void deleteAllCosting(int acct) {
        deleteAllCharges(acct);
        deleteAllVariables(acct);
        deleteAllComputations(acct);
    }

    public void deleteAllComputations(int acct) {
        Connection conn = MainDBConn.getConnection();
        String cmdStr;
        cmdStr = "DELETE FROM EDComputationTBL WHERE AcctNo=" + acct;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(cmdStr);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void populateCMBType() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT CTypeID, DescripType FROM costingUQTypeTBL WHERE MemberProcessTypeID=0;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbtyp.addItem(new Item(rs.getInt(1), rs.getString(2)));
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

    public void deleteAllVariables(int acct) {
        Connection conn = MainDBConn.getConnection();
        String cmdStr;
        cmdStr = "DELETE FROM EDVariablesTBL WHERE AcctNo=" + acct;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(cmdStr);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void deleteAllCharges(int acct) {
        Connection conn = MainDBConn.getConnection();
        String cmdStr;
        cmdStr = "DELETE FROM costingTempTBL WHERE AcctNo=" + acct;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(cmdStr);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void cmdSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSaveActionPerformed
        if (eid == 0) {
            JOptionPane.showMessageDialog(this, "Please Select Electrician!");
        } else {
            int col = 2;
            int Acct54 = 0;
            String total = "0.00";

            int rows = jTblCosting.getModel().getRowCount();
            for (int i = 0; i < rows; i++) {
                String value = (String) jTblCosting.getModel().getValueAt(i, col);
                String selTF = jTblCosting.getValueAt(i, 0).toString();
                String selTotal = jTblCosting.getValueAt(i, 7).toString();
                if (value.equals("54") && "true".equals(selTF)) {
                    Acct54 = 1;
                    total = selTotal;
                }
            }
            System.out.println(acctno);
            if (Acct54 == 1) {

                if ("0.00".equals(total)) {
                    JOptionPane.showMessageDialog(null, "Energy Deposit requires computation!");
                } else {
                    addDatas();

                }
            } else {
                addDatas();

            }
            UpdateElectricianID(eid);
            frmParent.transferToPayment(acctno);
            this.dispose();
            JOptionPane.showMessageDialog(this, "Costing Saved!");
        }
    }//GEN-LAST:event_cmdSaveActionPerformed

    private void cmbtypActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtypActionPerformed

        try {
            Item item = (Item) cmbtyp.getSelectedItem();
            typ = item.getId();
            populateTBL();
            refreshTotal();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbtypActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        txtx.setVisible(false);
        x.setVisible(false);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        txtx.setVisible(true);
        x.setVisible(true);
        double lc = Double.parseDouble(txtlc.getText().replace(",", ""));
        //double x = Double.parseDouble(txtx.getText().replace(",", ""));

        double lcc = lc / 2;
        txtx.setText(String.valueOf(lcc));

    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        showFrmEditElectrician();
    }//GEN-LAST:event_jButton1ActionPerformed

    public void refreshTotal() {
        int r = jTblCosting.getRowCount();
        //System.out.println(r);
        int c = 0;
        double total = 0;

        while (c < r) {
            try {
                String amnt = (jTblCosting.getValueAt(c, 7).toString());
                String selected = jTblCosting.getValueAt(c, 0).toString();
                amnt = amnt.replace(",", "");
                //System.out.println(amnt);

                if ("true".equals(selected)) {
                    total = total + Double.parseDouble(amnt);

                }

            } catch (Exception e) {
            }
            c++;
        }
        String ftotal = myFunctions.amountFormat(total);
        lbltotal.setText(ftotal);
    }

    public static String monthInWords(Double m) {
        String mword = null;
        String x = String.valueOf(m);
//        switch (x) {
//            case "1":
//                mword = "ONE";
//                break;
//            case "1.5":
//                mword = "ONE and ONE HALF";
//                break;
//            case "2":
//                mword = "TWO";
//                break;
//            case "2.5":
//                mword = "TWO and ONE HALF";
//                break;
//            case "3":
//                mword = "THREE";
//                break;
//            case "3.5":
//                mword = "THREE and ONE HALF";
//                break;
//            case "4":
//                mword = "FOUR";
//                break;
//            case "4.5":
//                mword = "FOUR and ONE HALF";
//                break;
//            case "5":
//                mword = "FIVE";
//                break;
//            case "5.5":
//                mword = "FIVE and ONE HALF";
//                break;
//            case "6":
//                mword = "SIX";
//                break;
//            case "6.5":
//                mword = "SIX and ONE HALF";
//                break;
//            case "7":
//                mword = "SEVEN";
//                break;
//            case "7.5":
//                mword = "SEVEN and ONE HALF";
//                break;
//            case "8":
//                mword = "EIGHT";
//                break;
//            case "8.5":
//                mword = "EIGHT and ONE HALF";
//                break;
//            case "9":
//                mword = "NINE";
//                break;
//            case "9.5":
//                mword = "NINE and ONE HALF";
//                break;
//            case "10":
//                mword = "TEN";
//                break;
//            case "10.5":
//                mword = "TEN and ONE HALF";
//                break;
//            case "11":
//                mword = "ELEVEN";
//                break;
//            case "11.5":
//                mword = "ELEVEN and ONE HALF";
//                break;
//            case "12":
//                mword = "TWELVE";
//                break;
//            case "12.5":
//                mword = "TWELVE and ONE HALF";
//                break;
//            default:
//                System.out.println("Invalid Month Entry!");
//                System.out.println(mword);
//                 throw new IllegalArgumentException("Invalid day of the week: " + mword);
//        }

        return mword;
    }

    public void edit() {
        int col = 0; //set column value to 0
        int row = jTblCosting.getSelectedRow(); //get value of selected value

        String selected = jTblCosting.getValueAt(row, col).toString();
        String selcoaid = jTblCosting.getValueAt(row, 2).toString();
        qty = jTblCosting.getValueAt(row, 4).toString();
        cost = jTblCosting.getValueAt(row, 6).toString();
        descrip = jTblCosting.getValueAt(row, 3).toString();

        if ("54".equals(selcoaid)) {
            JOptionPane.showMessageDialog(this, "Energy Deposit cannot be change because it is auto-calculated in Energy Deposit TAB!");
            //return;
        } else if ("false".equals(selected)) {
            JOptionPane.showMessageDialog(this, "Charges is uncheck or not selected!");
            //return;
        } else {
            EditCharges.qty = qty;
            EditCharges.cost = cost;
            EditCharges.rsel = row;
            EditCharges.descrip = descrip;
            showFrmEdit();
            //JOptionPane.showMessageDialog(this, selcoaid);
        }
    }

    public static void rsInsertCharges(int vacctno, String vdesc, String vqty, String vunit, double vcost, String vcoaid, double vtotal, int setID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO costingTempTBL (AcctNo, description, qty, unit, cost, COAID, total, costsetID) "
                + "VALUES ('" + vacctno + "','" + vdesc + "','" + vqty + "','" + vunit + "','" + vcost + "','" + vcoaid + "','" + vtotal + "'," + setID + ")";

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

//    public static void rsInsertCostingType() {
//        Connection conn = DBConn.getConnection();
//        String createString;
//        createString = "INSERT INTO connCostingTypeTBL (AcctNo, CtypeID) "
//                + "VALUES (" + acctno + "," + 0 + ")";
//
//        try {
//            stmt = conn.createStatement();
//            stmt.executeUpdate(createString);
//            stmt.close();
//            conn.close();
//
//        } catch (SQLException e) {
//            //JOptionPane.showMessageDialog(null, e.getMessage());
//            //e.printStackTrace();
//        }
//    }
    public static void rsUpdateCostingType(int tid) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE connCostingTypeTBL SET CTypeID=" + tid + " WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
            //e.printStackTrace();
        }
    }

    public void rsAddEDVariables() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO EDVariablesTBL (AcctNo, EDvariable, EDvalue, acronym, unit) "
                + "SELECT '" + acctno + "','Load Current'," + txtlc.getText() + ",'LC','KW' "
                + "UNION ALL SELECT '" + acctno + "','Average Hrs./Day Usaged'," + txtah.getText() + ",'AHPDU','KW' "
                + "UNION ALL SELECT '" + acctno + "','No. of Months (Advance Load Deposit)'," + txtmo.getText() + ",'MO','Mos.' "
                + "UNION ALL SELECT '" + acctno + "','Kilowatt per Hr. Rate'," + txtkwhr.getText() + ",'KWPHR','Php' "
                + "UNION ALL SELECT '" + acctno + "','Current Voltage'," + txtcv.getText() + ",'CV', 'KW' "
                + "UNION ALL SELECT '" + acctno + "','Load Factor'," + txtlf.getText() + ",'LF', 'KW' "
                + "UNION ALL SELECT '" + acctno + "','Constant Multiflier'," + txtcm.getText() + ",'CM', 'KW' ";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    public void clrED() {
        txtlc.setText(null);
        txtah.setText(null);
        txtkwhr.setText(null);
        txtmo.setText(null);

        model = (DefaultTableModel) jTblED.getModel();

        model.setNumRows(0);
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, int x) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/innn.png"));
            button.setIcon(ico1);
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                edit();
            }
            isPushed = false;
            return new String(label);
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        int iflg;

        public ButtonRenderer(int ico) {
            setOpaque(true);
            iflg = ico;
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/editm.png"));
            Icon ico2 = new javax.swing.ImageIcon(getClass().getResource("/img/printer.png"));
            if (iflg == 1) {
                setIcon(ico1);
            } else if (iflg == 2) {
                setIcon(ico2);
            }

            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ColumnBold extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel parent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            parent.setFont(
                    parent.getFont().deriveFont(Font.BOLD));
            parent.setHorizontalAlignment(SwingConstants.RIGHT);
            parent.setForeground(new java.awt.Color(0, 102, 0));
            return parent;
        }
    }

    class ColumnBold2 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel parent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            parent.setFont(
                    parent.getFont().deriveFont(Font.BOLD));
            parent.setHorizontalAlignment(SwingConstants.RIGHT);
            parent.setForeground(new java.awt.Color(255, 102, 0));
            return parent;
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                CostingOp dialog = new CostingOp(frmParent, true);
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
    private javax.swing.JPanel a;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbtyp;
    private javax.swing.JButton cmdCalc;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdSave;
    private javax.swing.JPanel edpane;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private static javax.swing.JTable jTblCosting;
    private javax.swing.JTable jTblED;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblenym;
    private javax.swing.JLabel lbltotal;
    private javax.swing.JTabbedPane tab;
    private javax.swing.JTextField txtah;
    private javax.swing.JTextField txtcm;
    private javax.swing.JTextField txtcv;
    private javax.swing.JTextField txtkwhr;
    private javax.swing.JTextField txtlc;
    private javax.swing.JTextField txtlf;
    private javax.swing.JTextField txtmo;
    private javax.swing.JTextField txtremarks;
    private javax.swing.JTextField txtx;
    private javax.swing.JLabel x;
    // End of variables declaration//GEN-END:variables
}
