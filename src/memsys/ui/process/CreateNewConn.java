package memsys.ui.process;

import memsys.global.DBConn.MainDBConn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import memsys.global.myDataenvi;
import static memsys.global.myDataenvi.rsAddConnLog;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import memsys.global.AccountNoGenerator;
import memsys.global.FunctionFactory;
import memsys.global.myFunctions;
import memsys.ui.main.ParentWindow;

public final class CreateNewConn extends javax.swing.JDialog {

    public static ValidatorConn frmParent;
    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model2, model, model3;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static String classCode, towncode;
    public static String nym, memid, ctcode;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    static int bid, cseqno, rseqlow, rseqtop, cacctno;

    public CreateNewConn(ValidatorConn parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        getRootPane().setDefaultButton(cmdCreate);
        setdates();
        txtedate.setEnabled(false);
        populate_cmbtown();
        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

        tbl1.setCellSelectionEnabled(false);
        tbl1.setRowSelectionAllowed(true);
        tbl1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl1.setSelectionBackground(new Color(153, 204, 255));
        tbl1.setSelectionForeground(Color.BLACK);
        populateComboBrgy();
        //   getNewRenderedTable(tbl1);

        jPanel2.setVisible(false);
        jLabel5.setVisible(false);
        jLabel12.setVisible(false);
        txtrcode.setVisible(false);
        txtseq.setVisible(false);
    }

    void populateSearch() {

        // System.out.println(towncode);
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM Consumer WHERE AcctName like '%" + txtsearch.getText() + "%'";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model2 = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);

            tbl.setRowHeight(22);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            //   tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(3).setCellRenderer(renderer);
            tbl.setColumnSelectionAllowed(false);
            tbl.getColumn("View").setCellRenderer(new ButtonRenderer(1));
            tbl.getColumn("View").setCellEditor(new ButtonEditor(new JCheckBox(), 1));

            model2.setNumRows(0);

            while (rs.next()) {
                //String amnt = myFunctions.amountFormat2(rs.getString(3));
                model2.addRow(new Object[]{rs.getString("AcctNo"), rs.getString("AcctName"), rs.getString("AcctAddress"), rs.getString("RouteCode")});
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
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
                int col = 0; //set column value to 0
                int row = tbl.getSelectedRow(); //get value of selected value

                String acctno = tbl.getValueAt(row, col).toString();
                String routecode = tbl.getValueAt(row, 3).toString();
                txtrcode.setText(routecode);
                cseqno = GetSeqCode(Integer.parseInt(acctno));
                ctcode = GetTCode2(Integer.parseInt(acctno));
                rseqlow = cseqno - 50;
                rseqtop = cseqno + 50;
                cacctno = Integer.parseInt(acctno);
                cmbtown.setSelectedIndex(Integer.parseInt(ctcode));
                populateSilingan();
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

        String GetTCode2(int acctno) {
            String tc = "";

            Connection conn = MainDBConn.getConnection();
            String createString;
            createString = "select TownCode FROM Consumer WHERE AcctNo=" + acctno;

            //int rc = 0;
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(createString);

                while (rs.next()) {
                    tc = rs.getString(1);
                }

                stmt.close();
                conn.close();

            } catch (Exception e) {
                e.getStackTrace();
            }

            return tc;
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
            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/logsmini.png"));
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

    class ColumnBold2 extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel parent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            parent.setFont(
                    parent.getFont().deriveFont(Font.BOLD));
            parent.getForeground().getRed();
            parent.setHorizontalAlignment(SwingConstants.RIGHT);
            parent.setForeground(new java.awt.Color(255, 102, 0));
            return parent;
        }
    }

    private static JTable getNewRenderedTable(final JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String acctno = table.getValueAt(row, 0).toString();

                if (Integer.parseInt(acctno) == cacctno) {
                    setBackground(Color.BLACK);
                    setForeground(Color.WHITE);
                } else {
                }
                return this;
            }
        });
        return table;
    }

    public class StatusColumnCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

            //Cells are by default rendered as a JLabel.
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            //Get the status for the current row.
            String acctno = table.getValueAt(row, 0).toString();
            if (Integer.parseInt(acctno) == cacctno) {
                l.setBackground(Color.GREEN);
                l.setForeground(Color.BLACK);
            } else {
                l.setBackground(Color.BLACK);
                l.setForeground(Color.WHITE);
            }

            //Return the JLabel which renders the cell.
            return l;

        }
    }

    void populateSilingan() {

        // System.out.println(towncode);
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM Consumer WHERE towncode='" + ctcode + "' and routecode='" + txtrcode.getText() + "' and routeseqno>" + rseqlow + " and routeseqno<" + rseqtop + " ORDER BY routeseqno";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model3 = (DefaultTableModel) tbl1.getModel();

            renderer.setHorizontalAlignment(0);

            tbl1.setRowHeight(22);
            tbl1.getColumnModel().getColumn(0).setCellRenderer(renderer);
            //   tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl1.getColumnModel().getColumn(3).setCellRenderer(renderer);
            tbl1.getColumnModel().getColumn(4).setCellRenderer(renderer);
            tbl1.setColumnSelectionAllowed(false);
            tbl1.getColumnModel().getColumn(0).setCellRenderer(new StatusColumnCellRenderer());

            model3.setNumRows(0);

            while (rs.next()) {
                //String amnt = myFunctions.amountFormat2(rs.getString(3));
                model3.addRow(new Object[]{rs.getString("AcctNo"), rs.getString("AcctName"), rs.getString("AcctAddress"), rs.getString("RouteCode"), rs.getString("RouteSeqNo")});
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    int GetSeqCode(int acctno) {
        int SeqNo = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select routeseqno FROM consumer WHERE acctno=" + acctno;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                SeqNo = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return SeqNo;
    }

    String GetTCode(int aid) {
        String UGID = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select tcode FROM AreaTBL WHERE areacode=" + aid;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                UGID = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return UGID;
    }

    public void populate_combo() {
        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT ClassCode, ClassDesc FROM ConsumerClass ORDER BY ClassDesc;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbClass.addItem(new Item(rs.getString(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    public void populate_cmbtown() {
        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT areacode, area_desc, tcode FROM AreaTBL";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbtown.addItem(new Item2(rs.getString(1), rs.getString(3).trim() + " - " + rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    class Item2 {

        private String id;
        private String description;

        public Item2(String id, String description) {
            this.id = id;
            this.description = description;
        }

        public String getId() {
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
        createString = "SELECT * FROM barangayTBL  WHERE areacode=" + towncode + " ORDER BY brgy_name";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbbrgy.addItem(new Item3(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getStackTrace();
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

    static int getMaxAcctNo() {
        int maxAN = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT COALESCE(MAX(AcctNo), 0) AS AcctNo FROM connTBL";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                maxAN = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }

        return maxAN;
    }

    int getValidAcctNo(int maxAcctNo) {
        boolean found = false;
        int getacctno = 0;

        int acctno = maxAcctNo + 1;
        String reverse = new StringBuffer(String.valueOf(acctno)).reverse().toString(); //058136
//        System.out.println("Last Acct No." + acctno);
//        System.out.println(reverse);

        while (found != true) {
            int i = 0;
            int digitTotal = 0;

            char[] cArray = reverse.toCharArray();
            while (i < String.valueOf(reverse).length()) {
                char c = cArray[i];
                String v = String.valueOf(c);
                digitTotal += Integer.parseInt(v) * (i + 1);
                i++;
            }
            if ((digitTotal % 11) == 0) {
                found = true;

                String valid = new StringBuffer(reverse).reverse().toString();
                getacctno = Integer.parseInt(valid);
            } else {
                int num = Integer.parseInt(new StringBuffer(reverse).reverse().toString());
                num++;
                reverse = new StringBuffer(String.valueOf(num)).reverse().toString();
            }
        }

        return getacctno;

    }

    class Item {

        private String id;
        private String description;

        public Item(String id, String description) {
            this.id = id;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public String toString() {
            return description;
        }
    }

    void setdates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date theDate = null;
        try {
            theDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        txtedate.setDate(theDate);
        //  txtend.setDate(theDate);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        typeapp = new javax.swing.ButtonGroup();
        datePickerCellEditor1 = new org.jdesktop.swingx.table.DatePickerCellEditor();
        datePickerCellEditor2 = new org.jdesktop.swingx.table.DatePickerCellEditor();
        jPanel1 = new javax.swing.JPanel();
        cmdCancel = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtaddress = new javax.swing.JTextField();
        txtedate = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbClass = new javax.swing.JComboBox();
        opt1 = new javax.swing.JRadioButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        opt2 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        cmdCreate = new javax.swing.JButton();
        txtname = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cmbtown = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        cmbbrgy = new javax.swing.JComboBox();
        txtrcode = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtseq = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Connection");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        cmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdCancel.setMnemonic('C');
        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        jLabel9.setText("Address:");

        txtaddress.setToolTipText("");
        txtaddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtaddressFocusGained(evt);
            }
        });
        txtaddress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtaddressMouseClicked(evt);
            }
        });

        txtedate.setDateFormatString("yyyy-MM-dd");

        jLabel4.setText("Town Code:");

        jLabel6.setText("Account Name:");

        cmbClass.setForeground(new java.awt.Color(102, 102, 102));
        cmbClass.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SELECT-" }));
        cmbClass.setToolTipText("");
        cmbClass.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cmbClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbClassActionPerformed(evt);
            }
        });

        typeapp.add(opt1);
        opt1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        opt1.setForeground(new java.awt.Color(102, 0, 102));
        opt1.setMnemonic('P');
        opt1.setSelected(true);
        opt1.setText("Permanent");
        opt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opt1ActionPerformed(evt);
            }
        });

        jLabel14.setText("Termination Date:");

        jLabel28.setForeground(new java.awt.Color(102, 102, 102));
        jLabel28.setText(" (Applicable For Temp Conn. Only)");

        typeapp.add(opt2);
        opt2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        opt2.setForeground(new java.awt.Color(102, 0, 102));
        opt2.setMnemonic('T');
        opt2.setText("Temporary");
        opt2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                opt2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Type:");

        cmdCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdCreate.setMnemonic('r');
        cmdCreate.setText("Create");
        cmdCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCreateActionPerformed(evt);
            }
        });

        txtname.setToolTipText("");
        txtname.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtnameFocusGained(evt);
            }
        });
        txtname.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtnameMouseClicked(evt);
            }
        });

        jLabel13.setText("Class:");

        cmbtown.setForeground(new java.awt.Color(102, 102, 102));
        cmbtown.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-SELECT-" }));
        cmbtown.setToolTipText("");
        cmbtown.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        cmbtown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtownActionPerformed(evt);
            }
        });

        jLabel10.setText("Barangay:");

        cmbbrgy.setToolTipText("Municipal/City");
        cmbbrgy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbbrgyActionPerformed(evt);
            }
        });

        txtrcode.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#000"))));
        txtrcode.setText("000");
        txtrcode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtrcodeFocusLost(evt);
            }
        });
        txtrcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtrcodeActionPerformed(evt);
            }
        });
        txtrcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtrcodeKeyPressed(evt);
            }
        });

        jLabel5.setText("Route Code:");

        jLabel12.setText("Route Seq. No.: ");

        txtseq.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0000"))));
        txtseq.setText("0000");
        txtseq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtseqFocusGained(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cmdCreate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdCancel))
                    .addComponent(txtedate, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(opt1)
                        .addGap(12, 12, 12)
                        .addComponent(opt2))
                    .addComponent(cmbClass, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbbrgy, 0, 321, Short.MAX_VALUE)
                    .addComponent(txtaddress, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                    .addComponent(txtname)
                    .addComponent(cmbtown, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(37, 37, 37))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtseq, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtrcode, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cmbtown, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtaddress, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbbrgy)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbClass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(opt1)
                    .addComponent(opt2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtedate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdCreate)
                    .addComponent(cmdCancel))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtrcode, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtseq, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Near Accounts");

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account No", "Account Name", "Address", "RouteCode", "View"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl.setToolTipText("");
        tbl.getTableHeader().setReorderingAllowed(false);
        tbl.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblMouseMoved(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);
        //set column width

        tbl.getColumnModel().getColumn(1).setPreferredWidth(180);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(180);
        tbl.getColumnModel().getColumn(4).setMaxWidth(50);

        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        tbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account No", "Account Name", "Address", "RouteCode", "SeqNo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl1.setToolTipText("");
        tbl1.getTableHeader().setReorderingAllowed(false);
        tbl1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tbl1MouseMoved(evt);
            }
        });
        jScrollPane2.setViewportView(tbl1);
        //set column width

        tbl1.getColumnModel().getColumn(1).setPreferredWidth(210);
        tbl1.getColumnModel().getColumn(2).setPreferredWidth(250);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtsearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtnameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtnameFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_txtnameFocusGained

    private void txtnameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtnameMouseClicked
        // TODO add your handling code here:
}//GEN-LAST:event_txtnameMouseClicked

    private void txtaddressFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtaddressFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_txtaddressFocusGained

    private void txtaddressMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtaddressMouseClicked
        // TODO add your handling code here:
}//GEN-LAST:event_txtaddressMouseClicked

    private void cmbClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbClassActionPerformed
        try {
            Item item = (Item) cmbClass.getSelectedItem();
            classCode = item.getId();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbClassActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        populate_combo();
        txtname.setText(nym);
    }//GEN-LAST:event_formWindowOpened

    private void opt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opt1ActionPerformed
        txtedate.setEnabled(false);
    }//GEN-LAST:event_opt1ActionPerformed

    private void cmdCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCreateActionPerformed
//        int x = SetAcctNo();
//        System.out.println(x);
        String tcode = towncode;
        String rcode = txtrcode.getText();
        String seq = txtseq.getText();
        String name = txtname.getText();
        String address = txtaddress.getText();
//        String edate = txtedate.getText();
        //int maxacctno = SetAcctNo();
        //  
        if ("-SELECT-".equals(cmbtown.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(this, "Please select a town code!");
        } else {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String eeDate = dateFormat.format(txtedate.getDate());
            AccountNoGenerator ANG = new AccountNoGenerator();
            int valacctno = ANG.GenerateValidAcctNo();

            int ctype = 0;
            if (opt1.isSelected() == true) {
                ctype = 1;

                if ( //tcode.isEmpty() == true
                        //                    || rcode.isEmpty() == true
                        //                    || seq.isEmpty() == true
                        // ||
                        name.isEmpty() == true
                        || address.isEmpty()
                        || cmbClass.getSelectedItem() == "-SELECT-") {
                    JOptionPane.showMessageDialog(this, "Please fill-up all the required fields!");
                    //return;
                } else {

                    int id = ParentWindow.getUserID();
                    rsAddConnLog(valacctno, "New permanent connection created", 1, id, nowDate, "");
                    rsAddConnLog(valacctno, "Send to costing", 2, id, nowDate, "");
                    myDataenvi.rsAddConn(FunctionFactory.NumFormatter(Integer.parseInt(tcode)), rcode, seq, name, name, classCode, memid, seq, address, ctype, nowDate, eeDate, valacctno, String.valueOf(bid));

                    myDataenvi.AddConsumer(String.valueOf(valacctno));
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "New Application Successfully Created!");
                }
            } else {
                ctype = 2;

                if ( //tcode.isEmpty() == true
                        //                    || rcode.isEmpty() == true
                        //                    || seq.isEmpty() == true
                        // ||
                        name.isEmpty() == true
                        || address.isEmpty()
                        //                    || edate.isEmpty()
                        || cmbClass.getSelectedItem() == "-SELECT-") {
                    JOptionPane.showMessageDialog(this, "Please fill-up all the required fields!");
                    //return;
                } else {
                    int id = ParentWindow.getUserID();
                    rsAddConnLog(valacctno, "New temporary connection created", 1, id, nowDate, "");
                    rsAddConnLog(valacctno, "Send to costing", 2, id, nowDate, "");
                    myDataenvi.rsAddConn(tcode, rcode, seq, name, name, classCode, memid, seq, address, ctype, nowDate, eeDate, valacctno, String.valueOf(bid));

                    myDataenvi.AddConsumer(String.valueOf(valacctno));
                    this.dispose();
                    JOptionPane.showMessageDialog(this, "New Application Successfully Created!");
                }
            }
        }
        //System.out.println(ctype);

    }//GEN-LAST:event_cmdCreateActionPerformed

    void tagStatus() {
    }

    private void opt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_opt2ActionPerformed
        txtedate.setEnabled(true);
    }//GEN-LAST:event_opt2ActionPerformed

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved

    }//GEN-LAST:event_tblMouseMoved

    private void cmbtownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtownActionPerformed
        try {
            Item2 item = (Item2) cmbtown.getSelectedItem();
            towncode = myFunctions.TCODEFormat(item.getId());
            populateSilingan();
            populateComboBrgy();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbtownActionPerformed

    private void txtrcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtrcodeKeyPressed
//        populateSilingan();
    }//GEN-LAST:event_txtrcodeKeyPressed

    private void txtrcodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtrcodeFocusLost
        //     populateSilingan();
    }//GEN-LAST:event_txtrcodeFocusLost

    private void txtseqFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtseqFocusGained
        //     populateSilingan();
    }//GEN-LAST:event_txtseqFocusGained

    private void cmbbrgyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbbrgyActionPerformed
        try {
            Item3 item = (Item3) cmbbrgy.getSelectedItem();
            bid = item.getId();
        } catch (Exception e) {
        }
//        ag();
        // System.out.println(bid);
    }//GEN-LAST:event_cmbbrgyActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        if (txtsearch.getText().isEmpty() != true) {
            populateSearch();
        }
    }//GEN-LAST:event_txtsearchActionPerformed

    private void tbl1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl1MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl1MouseMoved

    private void txtrcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtrcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtrcodeActionPerformed
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                CreateNewConn dialog = new CreateNewConn(frmParent, true);
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
    private javax.swing.JComboBox cmbClass;
    private javax.swing.JComboBox cmbbrgy;
    private javax.swing.JComboBox cmbtown;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdCreate;
    private org.jdesktop.swingx.table.DatePickerCellEditor datePickerCellEditor1;
    private org.jdesktop.swingx.table.DatePickerCellEditor datePickerCellEditor2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton opt1;
    private javax.swing.JRadioButton opt2;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tbl1;
    private javax.swing.JTextField txtaddress;
    private com.toedter.calendar.JDateChooser txtedate;
    private javax.swing.JTextField txtname;
    private javax.swing.JFormattedTextField txtrcode;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    private javax.swing.JFormattedTextField txtseq;
    private javax.swing.ButtonGroup typeapp;
    // End of variables declaration//GEN-END:variables
}
