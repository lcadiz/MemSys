package memsys.ui.process;

import memsys.global.DBConn.MainDBConn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javax.swing.table.TableColumn;
import memsys.global.FunctionFactory;
import memsys.global.myFunctions;

/**
 *
 * @author LESTER
 */
public class COInfo extends javax.swing.JDialog {

    public static IssueCO frmParent;
    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2, model3;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static String nowDate = myFunctions.getDate();
    public static int ac;
    static int setEid, sd;
    public static int membrid;
    public static String cn, nym, klas, tcode, rcode, seq;
    static int bid, cseqno, rseqlow, rseqtop, cacctno, acctnomain;
    public static String ctcode;
    DefaultComboBoxModel theModel;

    public COInfo(IssueCO parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);

        GetInfo();
        txtcn.setText(cn);
        txtsearch.requestFocus();
        this.setTitle("Connect Order - Other Info: " + nym);

        model2 = (DefaultTableModel) tbl1.getModel();

        TableColumn idClmn = tbl.getColumn("id");
        idClmn.setMaxWidth(0);
        idClmn.setMinWidth(0);
        idClmn.setPreferredWidth(0);

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);
        populateTBL();

        lblklas.setVisible(false);
        cmbcm.setVisible(false);
        chk.setVisible(false);

        txttcode.setText(myFunctions.TCODEFormat(tcode));
        txtrcode.setText(myFunctions.RCODEFormat(rcode));
        txtseq.setText(seq);
        getTownNym();

        theModel = (DefaultComboBoxModel) cmbcm.getModel();
        try {
            PopulateCMBCheckMeter(txttcode.getText(), txtrcode.getText());

        } catch (Exception e) {

        }

        lbl.setVisible(false);
        cmbroute.setVisible(false);
        setTabCMandSeq();
    }

    public boolean IsBapaRoute(String routecode) {
        boolean isbapa = false;
        int flg = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT BAPAFlag FROM Route WHERE TownCode='" + FunctionFactory.NumFormatter(Integer.parseInt(tcode.trim())) + "'  AND RouteCode='" + routecode + "'";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                flg = rs.getInt(1);
            }

            stmt.close();
            conn.close();
//            
            if (flg == 0) {
                isbapa = false;
            } else if (flg == 1) {
                isbapa = true;
            }
            System.out.print(flg);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
        return isbapa;
    }

    void setTabCMandSeq() {
        JLabel lbl = new JLabel("<html><table border=0 cellpadding=0 cellspacing=0><tr><td><img src=" + getClass().getResource("/img/devices.png") + ">&nbsp</td><td>Route Sequence and Check Meter Settings<font></td></th>");
        lbl.setIconTextGap(5);
        tab.setTabComponentAt(1, lbl);
    }

    public void UpdateTownRouteSeqCodeInConnTBL(int an, String tc, String rc, String rsn, String acctcode) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE connTBL SET TownCode='" + tc + "', RouteCode='" + rc + "', RouteSeqNo='" + rsn + "', AcctCode='" + acctcode + "' WHERE AcctNo=" + an;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void UpdateTownRouteSeqCodeInConsumer(int an, String tc, String rc, String rsn, String acctcode) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE Consumer SET TownCode='" + tc + "', RouteCode='" + rc + "', RouteSeqNo='" + rsn + "',AcctCode='" + acctcode + "' WHERE AcctNo=" + an;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void AssignCheckMeter(int anmain, int an) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO ConsumerMainSub (AcctNoMain, AcctNoSub) VALUES('" + anmain + "','" + an + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void SetInfo() {
        int row = tbl.getSelectedRow();
        String eid = tbl.getValueAt(row, 0).toString();
        String nym = tbl.getValueAt(row, 1).toString();
        setEid = Integer.parseInt(eid);
        lble.setText(nym);
        // txtcn.setText(cn);
        //System.out.println(setEid);
    }

    public void PopulateCMBCheckMeter(String tcode, String rcode) {
        //Populate Combo Area

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT c.AcctNo, AcctName, MeterSN FROM Consumer c INNER JOIN ConsumerMeter cm ON c.AcctNo=cm.AcctNo WHERE ClassCode='Z' AND TownCode='" + tcode + "' AND RouteCode='" + rcode + "'";

        cmbcm.addItem("--SELECT--");
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            int cntr = 0;
            while (rs.next()) {
                cmbcm.addItem(new Item2(rs.getInt(1), rs.getString(2) + " [" + rs.getString(3) + "]"));
                cntr++;
            }

            stmt.close();
            conn.close();

            if (cntr == 1) {
                cmbcm.setSelectedIndex(1);
            } else if (cntr > 1) {
                cmbcm.setSelectedIndex(0);
            }
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    private void UpdateCodes() {

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

    void getTownNym() {

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT TownName FROM Town WHERE TownCode='" + tcode + "'";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                txttcode.setText(tcode + "-" + rs.getString(1));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }

    }

    void populateSearch() {

        // System.out.println(towncode);
        Connection conn = MainDBConn.getConnection();
        String createString;
        // System.out.println(txtsearch23.getText());
        //System.out.print(FunctionFactory.NumFormatter(Integer.parseInt(tcode)));
       // System.out.println(tcode);
       // createString = "SELECT * FROM Consumer WHERE AcctName like '%" + txtsearch23.getText() + "%' AND TownCode='" + FunctionFactory.NumFormatter(Integer.parseInt(tcode.trim())) + "'";
         createString = "SELECT * FROM Consumer WHERE AcctName like '%" + txtsearch23.getText() + "%'";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            renderer.setHorizontalAlignment(0);

            tbl1.setRowHeight(22);
            tbl1.getColumnModel().getColumn(0).setCellRenderer(renderer);
            //   tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl1.getColumnModel().getColumn(3).setCellRenderer(renderer);
            tbl1.setColumnSelectionAllowed(false);
            tbl1.getColumn("View").setCellRenderer(new ButtonRenderer22(1));
            tbl1.getColumn("View").setCellEditor(new ButtonEditor22(new JCheckBox(), 1));

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

    class ButtonEditor22 extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor22(JCheckBox checkBox, int x) {
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
                int row = tbl1.getSelectedRow(); //get value of selected value

                String acctno = tbl1.getValueAt(row, col).toString();
                String routecode = tbl1.getValueAt(row, 3).toString();
                txtrcode.setText(routecode);
                cseqno = GetSeqCode(Integer.parseInt(acctno));
                ctcode = GetTCode2(Integer.parseInt(acctno));
                rseqlow = cseqno - 50;
                rseqtop = cseqno + 50;
                cacctno = Integer.parseInt(acctno);
                //cmbtown.setSelectedIndex(Integer.parseInt(ctcode));
                populateSilingan();

                boolean isbapa = IsBapaRoute(txtrcode.getText());
                System.out.println(isbapa);
                if (isbapa == true) {
                    theModel.removeAllElements();
                    PopulateCMBCheckMeter(ctcode, routecode);
                    lblklas.setVisible(true);
                    cmbcm.setVisible(true);
                } else if (isbapa == false) {
                    lblklas.setVisible(false);
                    cmbcm.setVisible(false);
                }

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

    class ButtonRenderer22 extends JButton implements TableCellRenderer {

        int iflg;

        public ButtonRenderer22(int ico) {
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

            model3 = (DefaultTableModel) tbl2.getModel();

            renderer.setHorizontalAlignment(0);

            tbl2.setRowHeight(22);
            tbl2.getColumnModel().getColumn(0).setCellRenderer(renderer);
            //   tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
            tbl2.getColumnModel().getColumn(3).setCellRenderer(renderer);
            tbl2.getColumnModel().getColumn(4).setCellRenderer(renderer);
            tbl2.setColumnSelectionAllowed(false);
            tbl2.getColumnModel().getColumn(0).setCellRenderer(new StatusColumnCellRenderer());

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

    String GetInfo() {
        String an = "";
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM ConnCOInfoTBL c LEFT JOIN electricianTBL e ON c.eID=e.eID  WHERE AcctNo=" + ac;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                lble.setText(rs.getString("Name"));
                txtnan.setText(rs.getString("NAcctNo"));
                txtcn.setText(rs.getString("ContactNo"));
                txtsdw.setText(rs.getString("SDW"));
                txtremarks.setText(rs.getString("Remarks"));
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return an;
    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();
        String createString = "";

//        if (i == 0) {
//            createString = "SELECT AcctNo, MembershipID, AcctName, s.statdesc, CONVERT(char(10), TransDate, 101) ,t.typedesc, ClassCode, AcctAddress "
//                    + " FROM connTBL c, connTypeTBL t, connStatTBL s "
//                    + " WHERE   c.status=s.status AND c.connType=t.connType AND c.Status=6 AND (acctname LIKE '%" + txtsearch.getText() + "%' OR AcctNo like '" + txtsearch.getText() + "%') "
//                    + " ORDER BY AcctName";
//        } else if (i == 1) {
        createString = "SELECT * FROM electricianTBL e INNER JOIN AreaTBL a ON e.areacode=a.areacode WHERE Name like '%" + txtsearch.getText() + "%' OR area_desc LIKE '%" + txtsearch.getText() + "%' ORDER BY Name";
//        }

        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);

            tbl.setRowHeight(29);
            tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
            // tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);

            tbl.getColumn("Set").setCellRenderer(new ButtonRenderer(1));
            tbl.getColumn("Set").setCellEditor(new ButtonEditor(new JCheckBox(), 1));

            tbl.setColumnSelectionAllowed(false);

            model.setNumRows(0);

            while (rs.next()) {
                String paidFlag = null;
//                int paid = determineIfPaid(rs.getInt(1));
//                paidFlag = "UNPAID";
//                if (paid == 0) {
//                    paidFlag = "PAID";
//                } else {
//                }
//
//                boolean x = checkCostingIfPaid(rs.getInt(1));
//
//                if (x == false) {
//                    paidFlag = "UNPAID";
//                } else if (x == true) {
//                    paidFlag = "PAID";
//                }

                model.addRow(new Object[]{rs.getInt("eID"), rs.getString("Name"), rs.getString("area_desc"), ""});
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
                SetInfo();
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtnan = new javax.swing.JTextField();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        txtcn = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtremarks = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lble = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtsdw = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtseq = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        txttcode = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl2 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtsearch23 = new org.jdesktop.swingx.JXSearchField();
        lblklas = new javax.swing.JLabel();
        cmbcm = new javax.swing.JComboBox();
        txtrcode = new javax.swing.JLabel();
        cmbroute = new javax.swing.JComboBox();
        lbl = new javax.swing.JLabel();
        chk = new javax.swing.JCheckBox();
        lblcaption = new javax.swing.JLabel();
        Ok = new javax.swing.JButton();
        Ok1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Connect Order - Other Info ");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        txtnan.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        txtcn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Near Account Number");

        txtremarks.setColumns(20);
        txtremarks.setLineWrap(true);
        txtremarks.setRows(5);
        txtremarks.setWrapStyleWord(true);
        jScrollPane3.setViewportView(txtremarks);

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Name of Electrician", "Area", "Set"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);
        tbl.getColumnModel().getColumn(3).setMaxWidth(50);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Remarks");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("SDW");

        lble.setBackground(new java.awt.Color(255, 255, 255));
        lble.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lble.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        lble.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Electrician");

        txtsdw.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Contact No.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtsearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(txtcn, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5)
                                    .addComponent(txtsdw, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(txtnan, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                            .addComponent(lble, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 176, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lble, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnan, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtcn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtsdw, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addGap(4, 4, 4)
                        .addComponent(jScrollPane3))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 406, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tab.addTab("CO Information", jPanel1);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Route Code:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Route Seq. No.: ");

        txtseq.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0000"))));
        txtseq.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtseq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtseqFocusGained(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Town Code:");

        txttcode.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txttcode.setForeground(new java.awt.Color(0, 102, 0));
        txttcode.setText("lbltown");
        txttcode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txttcode.setPreferredSize(new java.awt.Dimension(45, 21));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbl2.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl2.setToolTipText("");
        tbl2.getTableHeader().setReorderingAllowed(false);
        tbl2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tbl2MouseMoved(evt);
            }
        });
        jScrollPane4.setViewportView(tbl2);
        //set column width

        tbl2.getColumnModel().getColumn(1).setPreferredWidth(210);
        tbl2.getColumnModel().getColumn(2).setPreferredWidth(250);

        tbl1.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl1.setToolTipText("");
        tbl1.getTableHeader().setReorderingAllowed(false);
        tbl1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tbl1MouseMoved(evt);
            }
        });
        jScrollPane2.setViewportView(tbl1);
        //set column width

        tbl1.getColumnModel().getColumn(1).setPreferredWidth(180);
        tbl1.getColumnModel().getColumn(2).setPreferredWidth(180);
        tbl1.getColumnModel().getColumn(4).setMaxWidth(50);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Near Accounts");

        txtsearch23.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch23.setSearchMode(org.jdesktop.swingx.JXSearchField.SearchMode.REGULAR);
        txtsearch23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearch23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtsearch23, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsearch23, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        lblklas.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblklas.setText("Check Meter:");

        cmbcm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbcmActionPerformed(evt);
            }
        });

        txtrcode.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtrcode.setForeground(new java.awt.Color(0, 102, 0));
        txtrcode.setText("lblroute");
        txtrcode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtrcode.setPreferredSize(new java.awt.Dimension(45, 21));

        cmbroute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbrouteActionPerformed(evt);
            }
        });

        lbl.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl.setText("Route:");

        chk.setText("Manually select the route");
        chk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActionPerformed(evt);
            }
        });

        lblcaption.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblcaption.setForeground(new java.awt.Color(0, 102, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblklas, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbroute, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtrcode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(chk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(183, 183, 183))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbcm, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtseq, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txttcode, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblcaption, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(5, 5, 5)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txttcode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtrcode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chk)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbroute, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(txtseq, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(cmbcm, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblklas, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lblcaption, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab.addTab("Route Sequence and Check Meter Settings", jPanel3);

        Ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        Ok.setText("Save");
        Ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OkActionPerformed(evt);
            }
        });

        Ok1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        Ok1.setText("Cancel");
        Ok1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Ok1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(Ok)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Ok1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tab, javax.swing.GroupLayout.PREFERRED_SIZE, 532, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ok)
                    .addComponent(Ok1))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblKeyPressed
//        int key = evt.getKeyCode();
//        if (key == KeyEvent.VK_ENTER) {
//            showFrmSelect();
//        }
    }//GEN-LAST:event_tblKeyPressed


    private void OkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkActionPerformed
        int seq = Integer.parseInt(txtseq.getText());
//        if (txtseq.getText().isEmpty() == true || txtrcode.getText().isEmpty() == true || "000".equals(txtrcode.getText()) || seq == 0) {
//            JOptionPane.showMessageDialog(null, "Either the RouteCode is equal to 000 or Empty or the Sequence No. is Empty!");
//            txtseq.requestFocus();
//            tab.setSelectedIndex(1);
//            txtseq.selectAll();
//        } else {
            RemoveInfo(ac);
            SaveInfo(txtnan.getText(), txtcn.getText(), txtsdw.getText());
            UpdateCNo(membrid);

            boolean isbapa = IsBapaRoute(txtrcode.getText());
            if (isbapa == false) {
                UpdateTownRouteSeqCodeInConnTBL(ac, FunctionFactory.NumFormatter(Integer.parseInt(tcode.trim())), txtrcode.getText(), txtseq.getText(), FunctionFactory.NumFormatter4(Integer.parseInt(txtseq.getText())));
                UpdateTownRouteSeqCodeInConsumer(ac, FunctionFactory.NumFormatter(Integer.parseInt(tcode.trim())), txtrcode.getText(), txtseq.getText(), FunctionFactory.NumFormatter4(Integer.parseInt(txtseq.getText())));
            } else {
                UpdateTownRouteSeqCodeInConnTBL(ac, FunctionFactory.NumFormatter(Integer.parseInt(tcode.trim())), txtrcode.getText(), txtseq.getText(), FunctionFactory.NumFormatter4(Integer.parseInt(txtseq.getText())));
                UpdateTownRouteSeqCodeInConsumer(ac, FunctionFactory.NumFormatter(Integer.parseInt(tcode.trim())), txtrcode.getText(), txtseq.getText(), FunctionFactory.NumFormatter4(Integer.parseInt(txtseq.getText())));

                boolean isCMAA = frmParent.IsCMAssignedAlready(ac);

                if (isCMAA == false) {
                    if ("--SELECT--".equals(cmbcm.getSelectedItem().toString())) {
                        JOptionPane.showMessageDialog(null, "Please select a check meter!");
                    } else {
                        AssignCheckMeter(acctnomain, ac);
                    }
                }
            }

            this.dispose();
            frmParent.populateTBL();
            JOptionPane.showMessageDialog(null, "Saved!");
//        }
    }//GEN-LAST:event_OkActionPerformed

    public void SaveInfo(String nan, String cn, String sdw) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO connCOInfoTBL (AcctNo, eID, Remarks, NAcctNo, ContactNo, SDW) VALUES(" + ac + "," + setEid + ",'" + txtremarks.getText() + "','" + nan + "','" + cn + "','" + sdw + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void UpdateCNo(int memid) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE membersTBL SET contactNo='" + txtcn.getText() + "' WHERE memberID=" + memid;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void RemoveInfo(int id) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM connCOInfoTBL WHERE AcctNo=" + id;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void Ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ok1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_Ok1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //System.out.println(ac);
    }//GEN-LAST:event_formWindowOpened

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void txtseqFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtseqFocusGained
        //     populateSilingan();
    }//GEN-LAST:event_txtseqFocusGained

    private void tbl1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl1MouseMoved

    }//GEN-LAST:event_tbl1MouseMoved

    private void tbl2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl2MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl2MouseMoved

    private void txtsearch23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearch23ActionPerformed
//        if (txtsearch2.getText().isEmpty() != true) {
        populateSearch();
        //}
    }//GEN-LAST:event_txtsearch23ActionPerformed

    private void chkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActionPerformed
        if (chk.isSelected() == true) {
            lbl.setVisible(true);
            cmbroute.setVisible(true);
        } else if (chk.isSelected() == false) {
            lbl.setVisible(false);
            cmbroute.setVisible(false);
        }
    }//GEN-LAST:event_chkActionPerformed

    private void cmbcmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbcmActionPerformed
        try {
            Item2 item = (Item2) cmbcm.getSelectedItem();
            acctnomain = item.getId();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbcmActionPerformed

    private void cmbrouteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbrouteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbrouteActionPerformed

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
            java.util.logging.Logger.getLogger(COInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(COInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(COInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(COInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                COInfo dialog = new COInfo(frmParent, true);
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
    private javax.swing.JButton Ok;
    private javax.swing.JButton Ok1;
    private javax.swing.JCheckBox chk;
    private javax.swing.JComboBox cmbcm;
    private javax.swing.JComboBox cmbroute;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lbl;
    private javax.swing.JLabel lblcaption;
    private javax.swing.JLabel lble;
    private javax.swing.JLabel lblklas;
    private javax.swing.JTabbedPane tab;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tbl1;
    private javax.swing.JTable tbl2;
    private javax.swing.JTextField txtcn;
    private javax.swing.JTextField txtnan;
    private javax.swing.JLabel txtrcode;
    private javax.swing.JTextArea txtremarks;
    private javax.swing.JTextField txtsdw;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    private org.jdesktop.swingx.JXSearchField txtsearch23;
    private javax.swing.JFormattedTextField txtseq;
    private javax.swing.JLabel txttcode;
    // End of variables declaration//GEN-END:variables
}
