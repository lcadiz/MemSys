package Module.Main;

import memsys.global.DBConn.MainDBConn;
import memsys.ui.meter.IssueMeterMain;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class PostMeter extends javax.swing.JDialog {

    public static IssueMeterMain frmParent;
    static Statement stmt;
    static int brandid, typecode, amperecode, ownercode, demandtypeid;
    static int acctno;

    public PostMeter(IssueMeterMain parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        getRootPane().setDefaultButton(cmdpost);
    }

    ////////////////////////////////////////////////////POPULATOR///////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
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
                cmbowner.addItem(new Item(rs.getInt(1), rs.getString(2)));
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
        cmbtype.removeAllItems();
        cmbtype.addItem("-SELECT-");
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT TypeCode, TypeDesc FROM MeterType WHERE BrandID=" + bid + " ORDER BY TypeDesc;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbtype.addItem(new Item(rs.getInt(1), rs.getString(2)));
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

    ////////////////////////////////////////////////////POPULATOR///////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
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

    public static void rsAddPost(int acctno, String metersn, String coopsn, int brandid, int phase, int typecode, int wire, int voltage,
            int ampcode, int owncode, int demand, int edigits, int ddigits, int multiplier, String initread, String demread, String Rem) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO dbo.meterPostTBL(AcctNo,MeterSN,CoopSN,BrandID,Phase, TypeCode, Wire, Voltage, AmpereCode, OwnerCode, DemandType,"
                + "EnergyDigits ,DemandDigits,Multiplier,InitReading, DemandReading, Remarks) "
                + "VALUES (" + acctno + ",'" + metersn + "','" + coopsn + "'," + brandid + "," + phase + "," + typecode + "," + wire + "," + voltage
                + "," + ampcode + "," + owncode + "," + demand + "," + edigits + "," + ddigits + "," + multiplier + "," + initread + "," + demread + "," + Rem + ")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtmetersn = new javax.swing.JTextField();
        txtcoopsn = new javax.swing.JTextField();
        cmbbrand = new javax.swing.JComboBox();
        cmbphase = new javax.swing.JComboBox();
        cmbtype = new javax.swing.JComboBox();
        cmbwire = new javax.swing.JComboBox();
        cmbvoltage = new javax.swing.JComboBox();
        cmbampere = new javax.swing.JComboBox();
        cmbowner = new javax.swing.JComboBox();
        cmbdemandtype = new javax.swing.JComboBox();
        cmbenergydigits = new javax.swing.JComboBox();
        cmdpost = new javax.swing.JButton();
        lbl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cmbdemanddigits = new javax.swing.JComboBox();
        txtmultiplier = new javax.swing.JFormattedTextField();
        txtinitreading = new javax.swing.JFormattedTextField();
        txtdemandreading = new javax.swing.JFormattedTextField();
        txtrem = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Post Meter Information");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        txtmetersn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtmetersn.setCaretColor(new java.awt.Color(0, 102, 0));
        txtmetersn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmetersnActionPerformed(evt);
            }
        });
        txtmetersn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtmetersnFocusLost(evt);
            }
        });

        txtcoopsn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));

        cmbbrand.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SELECT-" }));
        cmbbrand.setToolTipText("");
        cmbbrand.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbbrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbbrandActionPerformed(evt);
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

        cmbtype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SELECT-" }));
        cmbtype.setToolTipText("");
        cmbtype.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtypeActionPerformed(evt);
            }
        });

        cmbwire.setToolTipText("");
        cmbwire.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbwire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbwireActionPerformed(evt);
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

        cmbdemandtype.setToolTipText("");
        cmbdemandtype.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbdemandtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbdemandtypeActionPerformed(evt);
            }
        });

        cmbenergydigits.setToolTipText("");
        cmbenergydigits.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbenergydigits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbenergydigitsActionPerformed(evt);
            }
        });

        cmdpost.setForeground(new java.awt.Color(0, 102, 0));
        cmdpost.setText("Post");
        cmdpost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdpostActionPerformed(evt);
            }
        });

        lbl.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbtype, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbowner, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbdemandtype, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdpost, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbphase, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cmbampere, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbvoltage, javax.swing.GroupLayout.Alignment.LEADING, 0, 66, Short.MAX_VALUE)
                                .addComponent(cmbwire, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(cmbenergydigits, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(cmbbrand, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 120, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtcoopsn, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                    .addComponent(txtmetersn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtmetersn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtcoopsn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbbrand, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbphase, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbtype, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbwire, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbvoltage, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbampere, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbowner, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbdemandtype, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbenergydigits, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(cmdpost))
        );

        jLabel1.setText("Meter Serial No.:");

        jLabel2.setText("Coop Serial No.:");

        jLabel3.setText("Brand:");

        jLabel4.setText("Phase:");

        jLabel5.setText("Type:");

        jLabel6.setText("Wire:");

        jLabel7.setText("Voltage:");

        jLabel8.setText("Ampere:");

        jLabel9.setText("Owner:");

        jLabel10.setText("Demand Type:");

        jLabel11.setText("Energy Digits:");

        jLabel12.setText("Demand Digits:");

        cmbdemanddigits.setToolTipText("");
        cmbdemanddigits.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbdemanddigits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbdemanddigitsActionPerformed(evt);
            }
        });

        txtmultiplier.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtmultiplier.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtmultiplier.setText("1");
        txtmultiplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtmultiplierMouseClicked(evt);
            }
        });
        txtmultiplier.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtmultiplierFocusGained(evt);
            }
        });

        txtinitreading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtinitreading.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0"))));
        txtinitreading.setText("0.0");
        txtinitreading.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtinitreadingMouseClicked(evt);
            }
        });
        txtinitreading.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtinitreadingFocusGained(evt);
            }
        });

        txtdemandreading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtdemandreading.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        txtdemandreading.setText("0.00");
        txtdemandreading.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtdemandreadingMouseClicked(evt);
            }
        });
        txtdemandreading.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtdemandreadingFocusGained(evt);
            }
        });

        txtrem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtmultiplier, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtinitreading, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdemandreading, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbdemanddigits, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtrem, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(cmbdemanddigits, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtmultiplier, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtinitreading, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtdemandreading, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtrem, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(188, Short.MAX_VALUE))
        );

        jLabel13.setText("Multiplier:");

        jLabel14.setText("Initial Reading:");

        jToolBar1.setRollover(true);
        jToolBar1.setEnabled(false);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Membership/Images/edist.png"))); // NOI18N
        jToolBar1.add(jLabel15);

        jLabel16.setForeground(new java.awt.Color(255, 102, 0));
        jLabel16.setText("Input Meter Information");
        jToolBar1.add(jLabel16);

        jLabel17.setText("Demand Reading:");

        jLabel18.setText("Remarks:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addGap(14, 14, 14)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbbrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbbrandActionPerformed
        try {
            Item item = (Item) cmbbrand.getSelectedItem();
            brandid = item.getId();
            populatetype(brandid);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbbrandActionPerformed

    private void cmbtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtypeActionPerformed
        try {
            Item2 item = (Item2) cmbtype.getSelectedItem();
            typecode = item.getId();
            populatetype(typecode);
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
            populatetype(amperecode);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbampereActionPerformed

    private void cmbownerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbownerActionPerformed
        try {
            Item4 item = (Item4) cmbowner.getSelectedItem();
            ownercode = item.getId();
            populatetype(ownercode);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbownerActionPerformed

    private void cmbdemandtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbdemandtypeActionPerformed
        try {
            Item5 item = (Item5) cmbdemandtype.getSelectedItem();
            demandtypeid = item.getId();
            populatetype(demandtypeid);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbdemandtypeActionPerformed

    private void cmbenergydigitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbenergydigitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbenergydigitsActionPerformed

    private void cmbdemanddigitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbdemanddigitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbdemanddigitsActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        populatewire();
        populatebrand();
        populatephase();
        populatevoltage();
        populateamper();
        populateowner();
        populatedemandtype();
        populateenergydigits();
        populatedemanddigits();
    }//GEN-LAST:event_formWindowOpened

    private void cmbphaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbphaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbphaseActionPerformed

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
                lbl.setForeground(new java.awt.Color(0, 102, 0));
                lbl.setText("Meter is Available!");
                txtmetersn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
            }
        }
    }//GEN-LAST:event_txtmetersnFocusLost

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    }//GEN-LAST:event_formWindowClosed

    private void txtmetersnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmetersnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmetersnActionPerformed

    private void txtmultiplierFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtmultiplierFocusGained
        txtmultiplier.selectAll();
    }//GEN-LAST:event_txtmultiplierFocusGained

    private void txtinitreadingFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtinitreadingFocusGained
        txtinitreading.selectAll();
    }//GEN-LAST:event_txtinitreadingFocusGained

    private void txtdemandreadingFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtdemandreadingFocusGained
        txtdemandreading.selectAll();
    }//GEN-LAST:event_txtdemandreadingFocusGained

    private void txtmultiplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtmultiplierMouseClicked
        txtmultiplier.selectAll();
    }//GEN-LAST:event_txtmultiplierMouseClicked

    private void txtinitreadingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtinitreadingMouseClicked
        txtinitreading.selectAll();
    }//GEN-LAST:event_txtinitreadingMouseClicked

    private void txtdemandreadingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtdemandreadingMouseClicked
        txtdemandreading.selectAll();
    }//GEN-LAST:event_txtdemandreadingMouseClicked

    private void cmdpostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdpostActionPerformed
        String metersn = txtmetersn.getText();
        String coopsn = txtcoopsn.getText();
        int phase = Integer.parseInt(cmbphase.getSelectedItem().toString());
        int wire = Integer.parseInt(cmbwire.getSelectedItem().toString());
        int voltage = Integer.parseInt(cmbvoltage.getSelectedItem().toString());
        int edigits = Integer.parseInt(cmbenergydigits.getSelectedItem().toString());
        int ddigits = Integer.parseInt(cmbdemanddigits.getSelectedItem().toString());
        int multiplier = Integer.parseInt(txtmultiplier.getText());
        String initread = txtinitreading.getText();
        String demread = txtdemandreading.getText();
        String rem = txtrem.getText().trim();


        if (metersn.isEmpty()==true || coopsn.isEmpty()==true || cmbbrand.getSelectedItem()=="-SELECT-" || cmbtype.getSelectedItem()=="-SELECT-" ){
            JOptionPane.showMessageDialog(null, "Please fill-up all the required fields!");
            return;
        }else{
            rsAddPost(acctno, metersn, coopsn, brandid, phase, typecode, wire, voltage, amperecode, ownercode, demandtypeid, edigits, ddigits, multiplier, initread, demread, rem );
            this.dispose();
            JOptionPane.showMessageDialog(null, "Meter Info Added Successfully!");
        }
    }//GEN-LAST:event_cmdpostActionPerformed

    private void txtremActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtremActionPerformed
       // populateTBL();
    }//GEN-LAST:event_txtremActionPerformed

    private void txtremKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtremKeyPressed
        //populateTBL();
    }//GEN-LAST:event_txtremKeyPressed

    public static void main(String args[]) {

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
    private javax.swing.JComboBox cmbampere;
    private javax.swing.JComboBox cmbbrand;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lbl;
    private javax.swing.JTextField txtcoopsn;
    private javax.swing.JFormattedTextField txtdemandreading;
    private javax.swing.JFormattedTextField txtinitreading;
    private javax.swing.JTextField txtmetersn;
    private javax.swing.JFormattedTextField txtmultiplier;
    private javax.swing.JTextField txtrem;
    // End of variables declaration//GEN-END:variables
}
