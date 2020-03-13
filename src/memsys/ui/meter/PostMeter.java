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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicComboPopup;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author cadizlester
 */
public final class PostMeter extends javax.swing.JDialog {

    public static MeterManage frmParent;
    static Statement stmt;
    static int brandid, typecode, amperecode, ownercode, demandtypeid, catid;
    static int acctno;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    // public static int mpid;

    public PostMeter(MeterManage parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        populatewire();
        populatebrand();
        populatephase();
        populatevoltage();
        populateamper();
        populateowner();
        populatedemandtype();
        populateenergydigits();
        populatedemanddigits();
        populateCategory();
        HideMultiplierCalculator();
    }

    void HideMultiplierCalculator() {
        lbl1.setVisible(false);
        lbl2.setVisible(false);
        lblctr.setVisible(false);
        lblptr.setVisible(false);
        lblim.setVisible(false);
        txtctr.setVisible(false);
        txtctr1.setVisible(false);
        txtptr.setVisible(false);
        txtptr1.setVisible(false);
        txtim.setVisible(false);
        c1.setVisible(false);
        c2.setVisible(false);
        txtmultiplier.setText("1");

        txtctr.setText("0");
        txtctr1.setText("0");
        txtptr.setText("0");
        txtptr1.setText("0");
        txtim.setText("0");
    }

    void ShowMultiplierCalculator() {
        lbl1.setVisible(true);
        lbl2.setVisible(true);
        lblctr.setVisible(true);
        lblptr.setVisible(true);
        lblim.setVisible(true);
        txtctr.setVisible(true);
        txtctr1.setVisible(true);
        txtptr.setVisible(true);
        txtptr1.setVisible(true);
        txtim.setVisible(true);
        c1.setVisible(true);
        c2.setVisible(true);
//        CalculateTotalMultiplier();
        txtmultiplier.setText("0");
    }

    void CalculateTotalMultiplier() {
        try {

            double ctr = 0;
            double ptr = 0;
            double ctr1 = 0;
            double ptr1 = 0;
            double im = 0;

            if ("0".equals(txtim.getText())) {
                im = 1;
            } else {
                im = Double.valueOf(txtim.getText());
            }

            if ("0".equals(txtctr.getText())) {
                ctr = 0;
            } else {
                if ("0".equals(txtctr1.getText())) {
                    ctr = 0;
                } else {
                    ctr = Double.valueOf(txtctr.getText()) / Double.valueOf(txtctr1.getText());
                }
            }

            if ("0".equals(txtptr.getText())) {
                ptr = 0;
            } else {
                if ("0".equals(txtptr1.getText())) {
                    ptr = 0;
                } else {
                    ptr = Double.valueOf(txtptr.getText()) / Double.valueOf(txtptr1.getText());
                }
            }

            double totalmultiplier = (ctr * ptr) * im;
            txtmultiplier.setText(String.valueOf(totalmultiplier));
        } catch (Exception e) {
            txtmultiplier.setText("0");
        }
    }

    void populatebrand() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT BrandID, BrandName FROM MeterBrand ORDER BY BrandName;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbbrand.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getErrorCode();
        }
    }

    void populateamper() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT AmpereCode, AmpereDesc FROM MeterAmpere;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbampere.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void populateowner() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT OwnerCode, OwnerDesc FROM MeterOwner;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbowner.addItem(new Item4(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void populatedemandtype() {
        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT DemandType, DemandTypeDesc FROM MeterDemandType";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbdemandtype.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void populatetype(int bid) {
        Object child = cmbtype.getAccessibleContext().getAccessibleChild(0);
BasicComboPopup popup = (BasicComboPopup)child;
JList list = popup.getList();
list.setSelectionBackground(new Color(153, 204, 255));
        cmbtype.removeAllItems();
        cmbtype.addItem("-SELECT-");
        Connection conn = MainDBConn.getConnection();
        String createString;

        createString = "SELECT TypeCode, TypeDesc FROM MeterType WHERE BrandID=" + bid + " ORDER BY TypeDesc;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                String desc = "";

                switch (rs.getInt(1)) {
                    case 62:
                        System.out.println(62);
                        desc = "<html><table border=0 cellpadding=0 cellspacing=0><tr><td></td><td><b><font color=#FF0000>" + " (" +  rs.getString(2) + ")" + "<font></b></td></th>";
                        break;
                    case 88:
                        System.out.println(88);
                        desc = "<html><table border=0 cellpadding=0 cellspacing=0><tr><td></td><td><b><font color=#0000FF>" + " (" +  rs.getString(2) + ")" + "<font></b></td></th>";
                        break;
                    default:
                        desc = "<html><table border=0 cellpadding=0 cellspacing=0><tr><td></td><td><font color=#000000>" + " (" +  rs.getString(2) + ")" + "<font></td></th>";
                        break;
                }

                cmbtype.addItem(new Item2(rs.getInt(1), desc));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void populatephase() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT Phase FROM MeterPhase";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbphase.addItem(rs.getInt(1));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void populatewire() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT Wire FROM MeterWire";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbwire.addItem(rs.getInt(1));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void populatevoltage() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT Voltage FROM MeterVoltage";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbvoltage.addItem(rs.getInt(1));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void populateenergydigits() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT EnergyDigits FROM MeterEnergyDigits";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbenergydigits.addItem(rs.getInt(1));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void populatedemanddigits() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT DemandDigits FROM MeterDemandDigits";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbdemanddigits.addItem(rs.getInt(1));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    boolean isInstalled(String MeterSN) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select * from ConsumerMeter where MeterSN='" + MeterSN + "'";

        boolean isExist = false;
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

    boolean isInstalledMP(String MeterSN) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select * from meterPostTBL where MeterSN='" + MeterSN + "'";

        boolean isExist = false;
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

    class Item4 {

        private int id;
        private String description;

        public Item4(int id, String description) {
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

    class Item5 {

        private int id;
        private String description;

        public Item5(int id, String description) {
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

    void populateCategory() {

        cmbcat.addItem(new Item6(0, "Self-Contained"));
        cmbcat.addItem(new Item6(1, "Transformer Rated"));

    }

    class Item6 {

        private int id;
        private String description;

        public Item6(int id, String description) {
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

    public static void rsAddPost(int acctno, String metersn, String coopsn, int brandid, int phase, int typecode, int wire, int voltage,
            int ampcode, int owncode, int demand, int edigits, int ddigits, double multiplier, String initread, String demread, String Rem, int CatID, String wh) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO dbo.meterPostTBL(AcctNo,MeterSN,CoopSN,BrandID,Phase, TypeCode, Wire, Voltage, AmpereCode, OwnerCode, DemandType, "
                + "EnergyDigits ,DemandDigits,Multiplier,InitReading, DemandReading, Remarks, CategoryID, WHConstant, PostDate) "
                + "VALUES (" + acctno + ",'" + metersn + "','" + coopsn + "'," + brandid + "," + phase + "," + typecode + "," + wire + "," + voltage
                + ",'" + ampcode + "'," + owncode + "," + demand + "," + edigits + "," + ddigits + "," + multiplier + "," + initread + "," + demread + ",'" + Rem + "'," + CatID + ",'" + wh + "','" + nowDate + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    int GetMaxPostID() {
        int MaxID = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT MAX(mpID) FROM meterPostTBL";

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                MaxID = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return MaxID;
    }

    public static void rsAddInfo(int mpid, double ptr, double ptrdiv, double ctr, double ctrdiv, double im) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO MeterMultiplierInfoTBL (mpID, PTR, PTRDiv, CTR, CTRDiv, IM)"
                + " VALUES (" + mpid + ",'" + ptr + "','" + ptrdiv + "','" + ctr + "','" + ctrdiv + "','" + im + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmdpost = new javax.swing.JButton();
        cmbenergydigits = new javax.swing.JComboBox();
        cmbdemandtype = new javax.swing.JComboBox();
        cmbampere = new javax.swing.JComboBox();
        cmbowner = new javax.swing.JComboBox();
        cmbvoltage = new javax.swing.JComboBox();
        cmbwire = new javax.swing.JComboBox();
        cmbtype = new javax.swing.JComboBox();
        cmbphase = new javax.swing.JComboBox();
        cmbbrand = new javax.swing.JComboBox();
        txtcoopsn = new javax.swing.JTextField();
        txtmetersn = new javax.swing.JTextField();
        txtrem = new javax.swing.JTextField();
        txtdemandreading = new javax.swing.JFormattedTextField();
        txtinitreading = new javax.swing.JFormattedTextField();
        cmbdemanddigits = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        lbl = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cmbcat = new javax.swing.JComboBox();
        lbl1 = new javax.swing.JLabel();
        lblctr = new javax.swing.JLabel();
        txtctr = new javax.swing.JFormattedTextField();
        c1 = new javax.swing.JLabel();
        txtctr1 = new javax.swing.JFormattedTextField();
        lblptr = new javax.swing.JLabel();
        txtptr = new javax.swing.JFormattedTextField();
        c2 = new javax.swing.JLabel();
        txtptr1 = new javax.swing.JFormattedTextField();
        lbl2 = new javax.swing.JLabel();
        lblim = new javax.swing.JLabel();
        txtim = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        txtwh = new javax.swing.JFormattedTextField();
        txtmultiplier = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Meter");

        jLabel7.setText("Voltage:");

        jLabel8.setText("Ampere:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("Total Multiplier:");

        jLabel9.setText("Owner:");

        jLabel14.setText("Initial Reading:");

        jLabel10.setText("Demand Type:");

        jLabel11.setText("Energy Digits:");

        jLabel1.setText("Meter Serial No.:");

        jLabel12.setText("Demand Digits:");

        jLabel2.setText("Coop Serial No.:");

        jLabel17.setText("Demand Reading:");

        jLabel18.setText("Remarks:");

        jLabel4.setText("Phase:");

        jLabel6.setText("Wire:");

        jLabel5.setText("Type:");

        cmdpost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdpost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdpost.setMnemonic('A');
        cmdpost.setText("Add");
        cmdpost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdpostActionPerformed(evt);
            }
        });

        cmbenergydigits.setToolTipText("");
        cmbenergydigits.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbenergydigits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbenergydigitsActionPerformed(evt);
            }
        });

        cmbdemandtype.setToolTipText("");
        cmbdemandtype.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbdemandtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbdemandtypeActionPerformed(evt);
            }
        });

        cmbampere.setToolTipText("");
        cmbampere.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbampere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbampereActionPerformed(evt);
            }
        });

        cmbowner.setToolTipText("");
        cmbowner.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbowner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbownerActionPerformed(evt);
            }
        });

        cmbvoltage.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "240" }));
        cmbvoltage.setToolTipText("");
        cmbvoltage.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbvoltage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbvoltageActionPerformed(evt);
            }
        });

        cmbwire.setToolTipText("");
        cmbwire.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbwire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbwireActionPerformed(evt);
            }
        });

        cmbtype.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbtype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SELECT-" }));
        cmbtype.setToolTipText("");
        cmbtype.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtypeActionPerformed(evt);
            }
        });

        cmbphase.setToolTipText("");
        cmbphase.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbphase.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cmbphase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbphaseActionPerformed(evt);
            }
        });

        cmbbrand.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SELECT-" }));
        cmbbrand.setToolTipText("");
        cmbbrand.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbbrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbbrandActionPerformed(evt);
            }
        });

        txtmetersn.setCaretColor(new java.awt.Color(0, 102, 0));
        txtmetersn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtmetersnFocusLost(evt);
            }
        });
        txtmetersn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmetersnActionPerformed(evt);
            }
        });

        txtrem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtremActionPerformed(evt);
            }
        });
        txtrem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtremKeyPressed(evt);
            }
        });

        txtdemandreading.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtdemandreading.setText("0.00");
        txtdemandreading.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtdemandreadingFocusGained(evt);
            }
        });
        txtdemandreading.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtdemandreadingMouseClicked(evt);
            }
        });

        txtinitreading.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0"))));
        txtinitreading.setText("0.0");
        txtinitreading.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtinitreadingFocusGained(evt);
            }
        });
        txtinitreading.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtinitreadingMouseClicked(evt);
            }
        });

        cmbdemanddigits.setToolTipText("");
        cmbdemanddigits.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbdemanddigits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbdemanddigitsActionPerformed(evt);
            }
        });

        jLabel3.setText("Brand:");

        lbl.setForeground(new java.awt.Color(255, 0, 0));

        jLabel15.setText("Category:");

        cmbcat.setToolTipText("");
        cmbcat.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbcat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcatActionPerformed(evt);
            }
        });

        lbl1.setForeground(new java.awt.Color(0, 102, 0));
        lbl1.setText("External Multiplier");

        lblctr.setText("CTR:");

        txtctr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtctr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtctr.setText("0");
        txtctr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtctrFocusGained(evt);
            }
        });
        txtctr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtctrMouseClicked(evt);
            }
        });
        txtctr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtctrKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtctrKeyReleased(evt);
            }
        });

        c1.setText(":");

        txtctr1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtctr1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtctr1.setText("0");
        txtctr1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtctr1FocusGained(evt);
            }
        });
        txtctr1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtctr1MouseClicked(evt);
            }
        });
        txtctr1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtctr1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtctr1KeyReleased(evt);
            }
        });

        lblptr.setText("PTR:");

        txtptr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtptr.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtptr.setText("0");
        txtptr.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtptrFocusGained(evt);
            }
        });
        txtptr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtptrMouseClicked(evt);
            }
        });
        txtptr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtptrKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtptrKeyReleased(evt);
            }
        });

        c2.setText(":");

        txtptr1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtptr1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtptr1.setText("0");
        txtptr1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtptr1FocusGained(evt);
            }
        });
        txtptr1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtptr1MouseClicked(evt);
            }
        });
        txtptr1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtptr1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtptr1KeyReleased(evt);
            }
        });

        lbl2.setForeground(new java.awt.Color(0, 102, 0));
        lbl2.setText("Internal Multiplier");

        lblim.setText("IM Value:");

        txtim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtim.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtim.setText("0");
        txtim.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtimFocusGained(evt);
            }
        });
        txtim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtimMouseClicked(evt);
            }
        });
        txtim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtimKeyReleased(evt);
            }
        });

        jLabel16.setText("W.H Constant:");

        txtwh.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.000"))));
        txtwh.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtwh.setText("0");
        txtwh.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtwhFocusGained(evt);
            }
        });
        txtwh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtwhMouseClicked(evt);
            }
        });
        txtwh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtwhKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtwhKeyReleased(evt);
            }
        });

        txtmultiplier.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtmultiplier.setForeground(new java.awt.Color(0, 102, 0));
        txtmultiplier.setText("0.0");
        txtmultiplier.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmdpost, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbenergydigits, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbdemandtype, 0, 451, Short.MAX_VALUE)
                            .addComponent(cmbowner, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbampere, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbvoltage, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbwire, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbphase, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbbrand, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcoopsn, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbcat, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtmetersn, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbtype, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblptr)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(lblctr)
                    .addComponent(lbl1)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtptr, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtptr1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtrem, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdemandreading, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtinitreading, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtctr, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtctr1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblim)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtim, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtwh, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cmbdemanddigits, javax.swing.GroupLayout.Alignment.LEADING, 0, 88, Short.MAX_VALUE))
                    .addComponent(txtmultiplier, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbcat, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtwh, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbdemanddigits, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtmetersn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcoopsn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtinitreading, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbbrand, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdemandreading, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbphase, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtrem, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbtype, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbwire, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblctr, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtctr, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(c1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtctr1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblim, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtim, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbvoltage, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblptr, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtptr, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(c2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtptr1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbampere, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbowner, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbdemandtype, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbenergydigits, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdpost))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtmultiplier, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtmetersnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmetersnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmetersnActionPerformed

    private void txtmetersnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtmetersnFocusLost
        if (txtmetersn.getText().isEmpty() == true) {
            lbl.setForeground(Color.red);
            txtmetersn.requestFocus();
            txtmetersn.setBorder(javax.swing.BorderFactory.createLineBorder(Color.red));
            lbl.setText("Meter Serial Number is required for verification!");
            return;
        } else {
            boolean isExist = isInstalled(txtmetersn.getText());

            if (isExist == true) {
                lbl.setForeground(Color.red);
                txtmetersn.selectAll();
                txtmetersn.requestFocus();
                txtmetersn.setBorder(javax.swing.BorderFactory.createLineBorder(Color.red));
                lbl.setText("Meter Serial No. already Exist!");
                return;
            } else {
                boolean isExistMP = isInstalledMP(txtmetersn.getText());

                if (isExistMP == true) {
                    lbl.setForeground(Color.red);
                    txtmetersn.selectAll();
                    txtmetersn.requestFocus();
                    txtmetersn.setBorder(javax.swing.BorderFactory.createLineBorder(Color.red));
                    lbl.setText("Meter Serial No. already Exist!");
                } else {
                    lbl.setForeground(new java.awt.Color(0, 102, 0));
                    lbl.setText("Meter is Available!");
                    txtmetersn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
                }
            }
        }
    }//GEN-LAST:event_txtmetersnFocusLost

    private void cmbbrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbbrandActionPerformed
        try {
            Item item = (Item) cmbbrand.getSelectedItem();
            brandid = item.getId();
            populatetype(brandid);
            System.out.println(brandid);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbbrandActionPerformed

    private void cmbphaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbphaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbphaseActionPerformed

    private void cmbtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtypeActionPerformed
        try {
            Item2 item = (Item2) cmbtype.getSelectedItem();
            typecode = item.getId();
            //    populatetype(typecode);
            // System.out.println(typecode);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbtypeActionPerformed

    private void cmbwireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbwireActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbwireActionPerformed

    private void cmbvoltageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbvoltageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbvoltageActionPerformed

    private void cmbampereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbampereActionPerformed
        try {
            Item3 item = (Item3) cmbampere.getSelectedItem();
            amperecode = item.getId();
            //  populatetype(amperecode);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbampereActionPerformed

    private void cmbownerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbownerActionPerformed
        try {
            Item4 item = (Item4) cmbowner.getSelectedItem();
            ownercode = item.getId();
            //populatetype(ownercode);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbownerActionPerformed

    private void cmbdemandtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbdemandtypeActionPerformed
        try {
            Item5 item = (Item5) cmbdemandtype.getSelectedItem();
            demandtypeid = item.getId();
            //populatetype(demandtypeid);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbdemandtypeActionPerformed

    private void cmbenergydigitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbenergydigitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbenergydigitsActionPerformed

    private void cmdpostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdpostActionPerformed
        if ("0".equals(txtmultiplier.getText())) {
            JOptionPane.showMessageDialog(null, "Multiplier should not be equal to zero!");
        } else {
            String metersn = txtmetersn.getText();
            String coopsn = txtcoopsn.getText();
            int phase = Integer.parseInt(cmbphase.getSelectedItem().toString());
            int wire = Integer.parseInt(cmbwire.getSelectedItem().toString());
            int voltage = Integer.parseInt(cmbvoltage.getSelectedItem().toString());
            int edigits = Integer.parseInt(cmbenergydigits.getSelectedItem().toString());
            int ddigits = Integer.parseInt(cmbdemanddigits.getSelectedItem().toString());
            double multiplier = Double.valueOf(txtmultiplier.getText());
            String initread = txtinitreading.getText();
            String demread = txtdemandreading.getText();
            String rem = txtrem.getText().trim();

            if (metersn.isEmpty() == true || cmbbrand.getSelectedItem() == "-SELECT-" || cmbtype.getSelectedItem() == "-SELECT-") {
                JOptionPane.showMessageDialog(null, "Please fill-up all the required fields!");
                return;
            } else {
                boolean isExist = isInstalled(txtmetersn.getText());

                if (isExist == true) {
                    lbl.setForeground(Color.red);
                    txtmetersn.selectAll();
                    txtmetersn.requestFocus();
                    txtmetersn.setBorder(javax.swing.BorderFactory.createLineBorder(Color.red));
                    lbl.setText("Meter Serial No. already Exist!");
                    return;
                } else {
                    boolean isExistMP = isInstalledMP(txtmetersn.getText());

                    if (isExistMP == true) {
                        JOptionPane.showMessageDialog(null, "Meter is already encoded!");
                    } else {
                        AddHistory(metersn);
                        rsAddPost(acctno, metersn, coopsn, brandid, phase, typecode, wire, voltage, amperecode, ownercode, demandtypeid, edigits, ddigits, multiplier, initread, demread, rem, catid, txtwh.getText());
                        this.dispose();
                        JOptionPane.showMessageDialog(null, "Meter Info Added Successfully!");
                        int maxmpid = GetMaxPostID();
                        rsAddInfo(maxmpid, Double.valueOf(txtptr.getText()), Double.valueOf(txtptr1.getText()), Double.valueOf(txtctr.getText()), Double.valueOf(txtctr1.getText()), Double.valueOf(txtim.getText()));
                        int i = memsys.global.myFunctions.msgboxYesNo("Do you want to print the meter record card?");
                        switch (i) {
                            case 0:
                                memsys.global.myReports.rptMeterRecordCard(GetMaxMPID());
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            default:
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_cmdpostActionPerformed

    public static void AddHistory(String sn) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO dbo.meterHistoryTBL(MeterSN, HistoryLog, LogType, UserID, DateTime) "
                + "VALUES (" + sn + ", 'Meter Tested, Calibrated and Record Posted, ',2," + ParentWindow.getUserID() + ",'" + nowDate + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    int GetMaxMPID() {
        int MaxMPID = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT MAX(mpID) FROM MeterPostTBL";

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                MaxMPID = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return MaxMPID;
    }


    private void cmbdemanddigitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbdemanddigitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbdemanddigitsActionPerformed

    private void txtinitreadingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtinitreadingMouseClicked
        txtinitreading.selectAll();
    }//GEN-LAST:event_txtinitreadingMouseClicked

    private void txtinitreadingFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtinitreadingFocusGained
        txtinitreading.selectAll();
    }//GEN-LAST:event_txtinitreadingFocusGained

    private void txtdemandreadingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtdemandreadingMouseClicked
        txtdemandreading.selectAll();
    }//GEN-LAST:event_txtdemandreadingMouseClicked

    private void txtdemandreadingFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtdemandreadingFocusGained
        txtdemandreading.selectAll();
    }//GEN-LAST:event_txtdemandreadingFocusGained

    private void txtremActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtremActionPerformed
        // populateTBL();
    }//GEN-LAST:event_txtremActionPerformed

    private void txtremKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtremKeyPressed
        //populateTBL();
    }//GEN-LAST:event_txtremKeyPressed

    private void cmbcatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcatActionPerformed
        try {
            Item6 item = (Item6) cmbcat.getSelectedItem();
            catid = item.getId();

            if (catid == 0) {
                HideMultiplierCalculator();
            } else if (catid == 1) {
                ShowMultiplierCalculator();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbcatActionPerformed

    private void txtctrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtctrFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtctrFocusGained

    private void txtctrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtctrMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtctrMouseClicked

    private void txtctr1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtctr1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtctr1FocusGained

    private void txtctr1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtctr1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtctr1MouseClicked

    private void txtptrFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtptrFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtptrFocusGained

    private void txtptrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtptrMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtptrMouseClicked

    private void txtptr1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtptr1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtptr1FocusGained

    private void txtptr1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtptr1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtptr1MouseClicked

    private void txtimFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtimFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtimFocusGained

    private void txtimMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtimMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtimMouseClicked

    private void txtctrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtctrKeyPressed
        CalculateTotalMultiplier();
    }//GEN-LAST:event_txtctrKeyPressed

    private void txtctr1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtctr1KeyPressed
        CalculateTotalMultiplier();
    }//GEN-LAST:event_txtctr1KeyPressed

    private void txtptrKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtptrKeyPressed
        CalculateTotalMultiplier();
    }//GEN-LAST:event_txtptrKeyPressed

    private void txtptr1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtptr1KeyPressed
        CalculateTotalMultiplier();
    }//GEN-LAST:event_txtptr1KeyPressed

    private void txtctrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtctrKeyReleased
        CalculateTotalMultiplier();
    }//GEN-LAST:event_txtctrKeyReleased

    private void txtctr1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtctr1KeyReleased
        CalculateTotalMultiplier();
    }//GEN-LAST:event_txtctr1KeyReleased

    private void txtptrKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtptrKeyReleased
        CalculateTotalMultiplier();
    }//GEN-LAST:event_txtptrKeyReleased

    private void txtptr1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtptr1KeyReleased
        CalculateTotalMultiplier();
    }//GEN-LAST:event_txtptr1KeyReleased

    private void txtimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtimKeyReleased
        CalculateTotalMultiplier();
    }//GEN-LAST:event_txtimKeyReleased

    private void txtwhKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtwhKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtwhKeyReleased

    private void txtwhKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtwhKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtwhKeyPressed

    private void txtwhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtwhMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtwhMouseClicked

    private void txtwhFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtwhFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtwhFocusGained

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
            java.util.logging.Logger.getLogger(PostMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PostMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PostMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PostMeter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PostMeter dialog = new PostMeter(frmParent, true);
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
    private javax.swing.JLabel c1;
    private javax.swing.JLabel c2;
    private javax.swing.JComboBox cmbampere;
    private javax.swing.JComboBox cmbbrand;
    private javax.swing.JComboBox cmbcat;
    private javax.swing.JComboBox cmbdemanddigits;
    private javax.swing.JComboBox cmbdemandtype;
    private javax.swing.JComboBox cmbenergydigits;
    private javax.swing.JComboBox cmbowner;
    private javax.swing.JComboBox cmbphase;
    private javax.swing.JComboBox cmbtype;
    private javax.swing.JComboBox cmbvoltage;
    private javax.swing.JComboBox cmbwire;
    private javax.swing.JButton cmdpost;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lbl1;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lblctr;
    private javax.swing.JLabel lblim;
    private javax.swing.JLabel lblptr;
    private javax.swing.JTextField txtcoopsn;
    private javax.swing.JFormattedTextField txtctr;
    private javax.swing.JFormattedTextField txtctr1;
    private javax.swing.JFormattedTextField txtdemandreading;
    private javax.swing.JFormattedTextField txtim;
    private javax.swing.JFormattedTextField txtinitreading;
    private javax.swing.JTextField txtmetersn;
    private javax.swing.JLabel txtmultiplier;
    private javax.swing.JFormattedTextField txtptr;
    private javax.swing.JFormattedTextField txtptr1;
    private javax.swing.JTextField txtrem;
    private javax.swing.JFormattedTextField txtwh;
    // End of variables declaration//GEN-END:variables
}
