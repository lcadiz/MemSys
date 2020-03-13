/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.meter;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myFunctions;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class MTC extends javax.swing.JInternalFrame {

    static Statement stmt;
    static int brandid, typecode, amperecode, ownercode, demandtypeid;
    static int acctno;
    static String nowDate = myFunctions.getDate2();

    public MTC() {
        initComponents();
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
            JOptionPane.showMessageDialog(null, e.getMessage());
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
                cmbtype.addItem(new Item2(rs.getInt(1), rs.getString(2)));
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

    boolean isPosted(String MeterSN) {
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel17 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmdpost = new javax.swing.JButton();
        cmbenergydigits = new javax.swing.JComboBox();
        cmbdemandtype = new javax.swing.JComboBox();
        cmbowner = new javax.swing.JComboBox();
        cmbampere = new javax.swing.JComboBox();
        cmbvoltage = new javax.swing.JComboBox();
        cmbwire = new javax.swing.JComboBox();
        cmbtype = new javax.swing.JComboBox();
        cmbphase = new javax.swing.JComboBox();
        cmbbrand = new javax.swing.JComboBox();
        txtcoopsn = new javax.swing.JTextField();
        txtmetersn = new javax.swing.JTextField();
        txtdemandreading = new javax.swing.JFormattedTextField();
        txtinitreading = new javax.swing.JFormattedTextField();
        txtmultiplier = new javax.swing.JFormattedTextField();
        cmbdemanddigits = new javax.swing.JComboBox();
        lbl = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("New Meter");
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

        jLabel17.setText("Demand Reading:");

        jLabel12.setText("Demand Digits:");

        jLabel14.setText("Initial Reading:");

        jLabel10.setText("Demand Type:");

        jLabel11.setText("Energy Digits:");

        jLabel8.setText("Ampere:");

        jLabel9.setText("Owner:");

        jLabel7.setText("Voltage:");

        jLabel6.setText("Wire:");

        jLabel5.setText("Type:");

        jLabel4.setText("Phase:");

        jLabel3.setText("Brand:");

        jLabel13.setText("Multiplier:");

        jLabel2.setText("Coop Serial No.:");

        jLabel1.setText("Meter Serial No.:");

        cmdpost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdpost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        cmdpost.setMnemonic('P');
        cmdpost.setText("Post");
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

        cmbowner.setToolTipText("");
        cmbowner.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbowner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbownerActionPerformed(evt);
            }
        });

        cmbampere.setToolTipText("");
        cmbampere.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbampere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbampereActionPerformed(evt);
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

        txtdemandreading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
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

        txtinitreading.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
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

        txtmultiplier.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 153, 255)));
        txtmultiplier.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtmultiplier.setText("1");
        txtmultiplier.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtmultiplierFocusGained(evt);
            }
        });
        txtmultiplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtmultiplierMouseClicked(evt);
            }
        });

        cmbdemanddigits.setToolTipText("");
        cmbdemanddigits.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cmbdemanddigits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbdemanddigitsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbbrand, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbphase, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbtype, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbowner, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbdemandtype, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbenergydigits, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtcoopsn, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtmetersn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbampere, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbvoltage, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbwire, javax.swing.GroupLayout.Alignment.LEADING, 0, 66, Short.MAX_VALUE))
                    .addComponent(cmdpost, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtmultiplier, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtinitreading, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdemandreading, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbdemanddigits, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(188, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(cmbdemanddigits, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtmultiplier, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtinitreading, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtdemandreading, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbbrand, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtmetersn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtcoopsn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbphase, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbtype, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbwire, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbvoltage, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbampere, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbowner, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbdemandtype, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbenergydigits, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(cmdpost)
                .addGap(0, 28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbdemanddigitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbdemanddigitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbdemanddigitsActionPerformed

    private void txtmultiplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtmultiplierMouseClicked
        txtmultiplier.selectAll();
    }//GEN-LAST:event_txtmultiplierMouseClicked

    private void txtmultiplierFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtmultiplierFocusGained
        txtmultiplier.selectAll();
    }//GEN-LAST:event_txtmultiplierFocusGained

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
                lbl.setText("Meter Serial No. already Installed!");
                return;
            } else {
                boolean isPosted = isPosted(txtmetersn.getText());

                if (isPosted == true) {
                    lbl.setForeground(Color.red);
                    txtmetersn.selectAll();
                    txtmetersn.requestFocus();
                    txtmetersn.setBorder(javax.swing.BorderFactory.createLineBorder(Color.red));
                    lbl.setText("Meter Serial No. already Posted!");
                    return;
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
            //populatetype(typecode);
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
            //populatetype(amperecode);
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
        //String rem = "NEW METER "+ nowDate;

        if (metersn.isEmpty() == true || coopsn.isEmpty() == true || cmbbrand.getSelectedItem() == "-SELECT-" || cmbtype.getSelectedItem() == "-SELECT-") {
            JOptionPane.showMessageDialog(null, "Please fill-up all the required fields!");
            return;
        } else {
            AddHistory(metersn);
            rsAddPost(acctno, metersn, coopsn, brandid, phase, typecode, wire, voltage, amperecode, ownercode, demandtypeid, edigits, ddigits, multiplier, initread, demread);
            this.dispose();
            JOptionPane.showMessageDialog(null, "Meter Info Added Successfully!");
        }
    }//GEN-LAST:event_cmdpostActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        populatewire();
        populatebrand();
        populatephase();
        populatevoltage();
        populateamper();
        populateowner();
        populatedemandtype();
        populateenergydigits();
        populatedemanddigits();
    }//GEN-LAST:event_formInternalFrameOpened

    public static void rsAddPost(int acctno, String metersn, String coopsn, int brandid, int phase, int typecode, int wire, int voltage,
            int ampcode, int owncode, int demand, int edigits, int ddigits, int multiplier, String initread, String demread) {
        Connection conn = MainDBConn.getConnection();
        String createString;

        createString = "INSERT INTO dbo.meterPostTBL(AcctNo,MeterSN,CoopSN,BrandID,Phase, TypeCode, Wire, Voltage, AmpereCode, OwnerCode, DemandType,"
                + "EnergyDigits ,DemandDigits, Multiplier,InitReading, DemandReading,  Flag, PostDate ) "
                + "VALUES (" + acctno + ",'" + metersn + "','" + coopsn + "'," + brandid + "," + phase + "," + typecode + "," + wire + "," + voltage
                + "," + ampcode + "," + owncode + "," + demand + "," + edigits + "," + ddigits + "," + multiplier + "," + initread + "," + demread + ",1,'" + nowDate + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void AddHistory(String sn) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO dbo.meterHistoryTBL(MeterSN, HistoryLog, LogType) "
                + "VALUES (" + sn + ",'Tested and Calibrated " + nowDate + "',1)";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
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
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbl;
    private javax.swing.JTextField txtcoopsn;
    private javax.swing.JFormattedTextField txtdemandreading;
    private javax.swing.JFormattedTextField txtinitreading;
    private javax.swing.JTextField txtmetersn;
    private javax.swing.JFormattedTextField txtmultiplier;
    // End of variables declaration//GEN-END:variables
}
