/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.process;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author cadizlester
 */
public class ExtendDueDate extends javax.swing.JInternalFrame {

    static Statement stmt;
    static String towncode, routecode, towncode1, routecode1;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    static int nowYear = FunctionFactory.GetSystemNowYear2();
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2, model3, model4;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

    public ExtendDueDate() {
        initComponents();
        populate_cmbtown();
        populate_cmbtown1();
        setdates();
        populate_cmbbillingmonth();
        populate_cmbbillingmonth1();

        model = (DefaultTableModel) tbl.getModel();
        model2 = (DefaultTableModel) tblsearch.getModel();
        model3 = (DefaultTableModel) tblperaccount.getModel();
        model4 = (DefaultTableModel) tbllog.getModel();

        renderer.setHorizontalAlignment(0);

        tbl.setRowHeight(29);
        // tbl.getColumnModel().getColumn(0).setCellRenderer(renderer);
        tbl.getColumnModel().getColumn(1).setCellRenderer(renderer);
        tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
        model.setNumRows(0);

        tblperaccount.setRowHeight(29);
        tblperaccount.getColumnModel().getColumn(0).setCellRenderer(renderer);
        model3.setNumRows(0);

        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);

        tblsearch.setCellSelectionEnabled(false);
        tblsearch.setRowSelectionAllowed(true);
        tblsearch.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblsearch.setSelectionBackground(new Color(153, 204, 255));
        tblsearch.setSelectionForeground(Color.BLACK);

        tblperaccount.setCellSelectionEnabled(false);
        tblperaccount.setRowSelectionAllowed(true);
        tblperaccount.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblperaccount.setSelectionBackground(new Color(153, 204, 255));
        tblperaccount.setSelectionForeground(Color.BLACK);

        tbllog.setCellSelectionEnabled(false);
        tbllog.setRowSelectionAllowed(true);
        tbllog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbllog.setSelectionBackground(new Color(153, 204, 255));
        tbllog.setSelectionForeground(Color.BLACK);

        TableColumn idClmn = tbl.getColumn("r");
        idClmn.setMaxWidth(0);
        idClmn.setMinWidth(0);
        idClmn.setPreferredWidth(0);

        TableColumn idClmn1 = tblperaccount.getColumn("t");
        idClmn1.setMaxWidth(0);
        idClmn1.setMinWidth(0);
        idClmn1.setPreferredWidth(0);

        TableColumn idClmn2 = tblperaccount.getColumn("r");
        idClmn2.setMaxWidth(0);
        idClmn2.setMinWidth(0);
        idClmn2.setPreferredWidth(0);

        tbl.setComponentPopupMenu(pop);
        //tblsearch.setComponentPopupMenu(popsearch);

    }

    class JDateChooserRenderer extends JDateChooser implements TableCellRenderer {

        public JDateChooserRenderer(JDateChooser dateChooser) {
            if (dateChooser != null) {

                //     this.setDate(dateChooser.getDate());
                this.setDateFormatString("MM/dd/yyyy");
            } else {
            }
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            if (value instanceof Date) {
                this.setDate((Date) value);
            } else if (value instanceof String) {
            }

            return this;
        }

    }

    class JDateChooserCellEditor extends AbstractCellEditor implements TableCellEditor {

        private static final long serialVersionUID = 917881575221755609L;

        private final JDateChooser sdateChooser = new JDateChooser();
        private AbstractTableModel modeldt;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, final int row, final int column) {

            try {
                modeldt = (AbstractTableModel) table.getModel();
            } catch (Exception e) {
            };
            sdateChooser.setDateFormatString("MM/dd/yyyy");
            sdateChooser.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    String pname = evt.getPropertyName();

                }
            });

            return sdateChooser;
        }

        @Override
        public Object getCellEditorValue() {
            return sdateChooser.getDate();
        }

    }

    private void AddAccount() {
        int col = 0; //set column value to 0
        int row = tblsearch.getSelectedRow(); //get value of selected value

        String acctno = tblsearch.getValueAt(row, col).toString();
        String nym = tblsearch.getValueAt(row, 1).toString();
        String duedate = tblsearch.getValueAt(row, 3).toString();
        try {
            JDateChooser dc = new JDateChooser();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date theDate = null;
            try {
                theDate = sdf.parse(nowDate);
            } catch (ParseException e) {
            }

            Date date = theDate;

            model3.addRow(new Object[]{acctno, nym, cmbbm1.getSelectedItem().toString(), duedate, date,"",towncode1, routecode1});

            tblperaccount.getColumnModel().getColumn(4).setCellRenderer(new JDateChooserRenderer(dc));
            tblperaccount.getColumnModel().getColumn(4).setCellEditor(new JDateChooserCellEditor());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Not yet billed!");
        }
    }

    private void ExtendDueDatePerRoute() {
        int r = tbl.getRowCount();
        int i = 0;
        //System.out.println(r);
        while (i < r) {
            String tcode = (String) tbl.getValueAt(i, 0);
            String rcode = (String) tbl.getValueAt(i, 1);
            String billmonth = (String) tbl.getValueAt(i, 2);
            String duedate = (String) tbl.getValueAt(i, 3);
            String extdate = (String) tbl.getValueAt(i, 4);
            String remarks = (String) tbl.getValueAt(i, 5);

            UpdateRouteDueDate(tcode, rcode, Integer.valueOf(billmonth), extdate);
            AddLog(tcode, rcode, duedate, extdate, Integer.valueOf(billmonth), remarks);

            ///System.out.println(i);
            i++;
        }
        model.setNumRows(0);
        cmbtown.setSelectedIndex(0);
        cmbroute.setSelectedIndex(0);
        cmbbm.setSelectedIndex(0);

        txtreason.setText("");
        setdates();
        JOptionPane.showMessageDialog(this, "Duedate adjustment/s successfully done!");

    }

    private void ExtendDueDatePerAccount() {
        int r = tblperaccount.getRowCount();
        int i = 0;
        //System.out.println(r);
        while (i < r) {
            String an = (String) tblperaccount.getValueAt(i, 0);
            String billmonth = (String) tblperaccount.getValueAt(i, 2);
            String duedate = (String) tblperaccount.getValueAt(i, 3);
            String extdate = (String) tblperaccount.getValueAt(i, 4);
            String remarks = (String) tblperaccount.getValueAt(i, 5);
            String tcode = (String) tblperaccount.getValueAt(i, 6);
            String rcode = (String) tblperaccount.getValueAt(i, 7);

            UpdateRouteDueDate(tcode, rcode, Integer.valueOf(billmonth), extdate);
            AddLog(tcode, rcode, duedate, extdate, Integer.valueOf(billmonth), remarks);

            ///System.out.println(i);
            i++;
        }
        model3.setNumRows(0);
        cmbtown1.setSelectedIndex(0);
        cmbroute1.setSelectedIndex(0);
        cmbbm1.setSelectedIndex(0);

        JOptionPane.showMessageDialog(this, "Duedate adjustment/s successfully done!");

    }

    public static void AddLog(String tcode, String rcode, String duedate, String extdate, int bm, String reason) {
        // MDI.UserID
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO ExtendDueDateLogTBL( "
                + "TownCode, "
                + "RouteCode, "
                + "DueDate, "
                + "ExtendDate, "
                + "BillMonth, "
                + "UserID, "
                + "AcctNo, "
                + "TransDate, "
                + "Reason) "
                + "VALUES ('" + tcode + "','" + rcode + "','" + duedate + "','" + extdate + "'," + bm + "," + ParentWindow.getUserID() + ", 0, '" + nowDate + "','" + reason + "')";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void UpdateRouteDueDate(String tcode, String rcode, int bm, String extdate) {
        try {
            Connection conn = MainDBConn.getConnection();
            String query = "sp_ExtendDueDate '" + tcode + "','" + rcode + "'," + bm + ",'" + extdate + "'";
            CallableStatement cs = conn.prepareCall(query);
            cs.execute();

        } catch (SQLException ex) {
            Logger.getLogger(ExtendDueDate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void UpdateAccountDueDate(int an, int bm, String extdate) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE Bill "
                + "SET DueDate='" + extdate + "' "
                + "WHERE AcctNo=" + an + " AND BillMonth=" + bm;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void CleanTBLSearch() {
        int rcount = tblsearch.getRowCount();
        int row = rcount;
        while (row != -1) {

            try {
                boolean issel = (Boolean) tblsearch.getValueAt(row, 0);

                if (issel == false) {
                    ((DefaultTableModel) tblsearch.getModel()).removeRow(row);
                    row = row - 1;
                } else if (issel == true) {
                    row = row - 1;
                }
            } catch (Exception e) {
                row = row - 1;
            }
        }
    }

    void scrollToVisible(int rowIndex, int vColIndex) {
        if (!(tblsearch.getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport) tblsearch.getParent();
        Rectangle rect = tblsearch.getCellRect(rowIndex, vColIndex, true);
        Point pt = viewport.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);

        viewport.scrollRectToVisible(rect);
    }

    void populateSearchTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT *, FORMAT(DueDate,'MM/dd/yyyy') as fdd FROM Consumer c INNER JOIN Bill b ON c.AcctNo=b.AcctNo WHERE TownCode='" + towncode1 + "' AND RouteCode='" + routecode1 + "' AND BillMonth=" + cmbbm1.getSelectedItem() + " AND AcctName LIKE '%" + txtsearch.getText() + "%'  ORDER BY AcctName";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            //odel2 = (DefaultTableModel) tblsearch.getModel();
            renderer.setHorizontalAlignment(0);

            tblsearch.setRowHeight(29);
            // tblsearch.getColumnModel().getColumn(0).setCellRenderer(renderer);
            tblsearch.setColumnSelectionAllowed(false);

            model2.setNumRows(0);
            //CleanTBLSearch();

            //int cnt = 0;
            while (rs.next()) {
                model2.addRow(new Object[]{rs.getString("AcctNo"), rs.getString("AcctName"),
                    rs.getString("TownCode") + "-" + rs.getString("RouteCode") + "-" + rs.getString("RouteSeqNo"), rs.getString("fdd")});
                // cnt++;
            }
            //System.out.println(cnt);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            // JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void populateLogTBL() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT CONVERT(VARCHAR, e.TransDate)+'  -  Route '+TownCode+'-'+RouteCode+' Extends duedate from '+CONVERT(VARCHAR, DueDate)+' to '+CONVERT(VARCHAR, ExtendDate) "
                + "AS edlog, AcctNo, Reason, FullName FROM ExtendDueDateLogTBL e INNER JOIN Users u ON e.UserID=u.UserID "
                + " WHERE CONVERT(VARCHAR, e.TransDate)+'  -  Route '+TownCode+'-'+RouteCode+' Extends duedate from '+CONVERT(VARCHAR, DueDate)+' to '+CONVERT(VARCHAR, ExtendDate) LIKE '%" + txtsearchlog.getText() + "%' OR FullName LIKE '%" + txtsearchlog.getText() + "%'";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            renderer.setHorizontalAlignment(0);
            tbllog.setRowHeight(29);
            tbllog.setColumnSelectionAllowed(false);

            model4.setNumRows(0);
            while (rs.next()) {
                int an = rs.getInt(2);
                String andesc = "";
                if (an == 0) {
                    andesc = "All Accounts";
                } else {
                    andesc = "AcctNo:" + String.valueOf(an);
                }

                model4.addRow(new Object[]{rs.getString(1) + " //" + andesc, rs.getString(3), rs.getString(4)});
                // cnt++;
            }
            //System.out.println(cnt);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            // JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    Date GetDueDate() {
        Date d = null;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT MIN(DueDate) FROM Consumer c INNER JOIN Bill b ON c.AcctNo=b.AcctNo WHERE TownCode='" + towncode + "' AND RouteCode='" + routecode + "' and BillMonth=" + cmbbm.getSelectedItem() ;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                d = rs.getDate(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return d;
    }

    void setdates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date theDate = null;
        try {
            theDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        txtextenddate.setDate(theDate);
        //  txtend.setDate(theDate);
    }

    public void populate_cmbbillingmonth() {
        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT BillMonth FROM BillingMonth ORDER BY BillMonth DESC";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbbm.addItem(rs.getString(1));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    public void populate_cmbbillingmonth1() {
        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT BillMonth FROM BillingMonth ORDER BY BillMonth DESC";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbbm1.addItem(rs.getString(1));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    public void populate_cmbtown1() {
        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT areacode, area_desc, tcode FROM AreaTBL";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbtown1.addItem(new Item4(rs.getString(3).trim(), rs.getString(3).trim() + " - " + rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    private void Refresh() {
        populateSearchTBL();
    }

    class Item4 {

        private String id;
        private String description;

        public Item4(String id, String description) {
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

    public void populate_cmbtown() {
        //Populate Combo Area
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT areacode, area_desc, tcode FROM AreaTBL";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbtown.addItem(new Item2(rs.getString(3).trim(), rs.getString(3).trim() + " - " + rs.getString(2)));
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

    public void populate_cmbroute() {
        //Populate Combo Area
        cmbroute.removeAllItems();
        cmbroute.addItem("--SELECT--");

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT RouteCode, RouteName FROM Route WHERE TownCode='" + towncode + "' AND BAPAFlag=1";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbroute.addItem(new Item3(rs.getString(1).trim(), rs.getString(1).trim() + " - " + rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
            e.getErrorCode();

        }
    }

    class Item3 {

        private String id;
        private String description;

        public Item3(String id, String description) {
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

    public void populate_cmbroute1() {
        //Populate Combo Area
        cmbroute1.removeAllItems();
        cmbroute1.addItem("--SELECT--");

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT RouteCode, RouteName FROM Route WHERE TownCode='" + towncode1 + "' AND BAPAFlag=1";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {

                cmbroute1.addItem(new Item5(rs.getString(1).trim(), rs.getString(1).trim() + " - " + rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
            e.getErrorCode();

        }
    }

    class Item5 {

        private String id;
        private String description;

        public Item5(String id, String description) {
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pop = new javax.swing.JPopupMenu();
        mnuview = new javax.swing.JMenuItem();
        popsearch = new javax.swing.JPopupMenu();
        mnuadd = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        cmbtown = new javax.swing.JComboBox();
        cmbroute = new javax.swing.JComboBox();
        txtextenddate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmdCreate = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        cmdexecute = new javax.swing.JButton();
        cmdexecute1 = new javax.swing.JButton();
        cmbbm = new javax.swing.JComboBox();
        txtreason = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbroute1 = new javax.swing.JComboBox();
        cmbtown1 = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblsearch = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        cmdexecute2 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        cmbbm1 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblperaccount = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cmdexecute3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbllog = new javax.swing.JTable();
        txtsearchlog = new org.jdesktop.swingx.JXSearchField();

        pop.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        pop.setInvoker(tbl);

        mnuview.setText("View Reason");
        mnuview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuviewActionPerformed(evt);
            }
        });
        pop.add(mnuview);

        popsearch.setInvoker(tblsearch);

        mnuadd.setText("Add Account");
        popsearch.add(mnuadd);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Extend Due Date");

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        cmbtown.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--SELECT--" }));
        cmbtown.setToolTipText("");
        cmbtown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtownActionPerformed(evt);
            }
        });

        cmbroute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--SELECT--" }));
        cmbroute.setToolTipText("");
        cmbroute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbrouteActionPerformed(evt);
            }
        });

        txtextenddate.setDateFormatString("MM/dd/yyyy");

        jLabel1.setText("Town");

        jLabel2.setText("Route");

        jLabel3.setText("Billing Month");

        jLabel4.setText("Extend Date");

        cmdCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdCreate.setMnemonic('r');
        cmdCreate.setText("  Add Route ");
        cmdCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCreateActionPerformed(evt);
            }
        });

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TownCode", "RouteCode", "BillMonth", "DueDate", "ExtendDate", "r"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl.getTableHeader().setReorderingAllowed(false);
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tbl);

        cmdexecute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employer.png"))); // NOI18N
        cmdexecute.setMnemonic('r');
        cmdexecute.setText("Execute Extend Due Date");
        cmdexecute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexecuteActionPerformed(evt);
            }
        });

        cmdexecute1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        cmdexecute1.setMnemonic('r');
        cmdexecute1.setText("Remove");
        cmdexecute1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexecute1ActionPerformed(evt);
            }
        });

        cmbbm.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--SELECT--" }));
        cmbbm.setToolTipText("");
        cmbbm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbbmActionPerformed(evt);
            }
        });

        jLabel5.setText("Reason");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("List of Routes to be adjust");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Add New Route");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbtown, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbbm, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbroute, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtextenddate, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdCreate)
                            .addComponent(txtreason, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cmdexecute)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdexecute1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel1)
                            .addComponent(cmbtown, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel2)
                            .addComponent(cmbroute, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel3)
                            .addComponent(cmbbm, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel4)
                            .addComponent(txtextenddate, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel5)
                            .addComponent(txtreason, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdCreate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 167, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdexecute)
                    .addComponent(cmdexecute1))
                .addGap(30, 30, 30))
        );

        jTabbedPane1.addTab("Extend Per Route", jPanel2);

        jLabel8.setText("Town");

        jLabel9.setText("Route");

        cmbroute1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--SELECT--" }));
        cmbroute1.setToolTipText("");
        cmbroute1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbroute1ActionPerformed(evt);
            }
        });

        cmbtown1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--SELECT--" }));
        cmbtown1.setToolTipText("");
        cmbtown1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtown1ActionPerformed(evt);
            }
        });

        tblsearch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AccountNo", "Name of Account", "Area Code", "DueDate"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblsearch.getTableHeader().setReorderingAllowed(false);
        tblsearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblsearchMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblsearchMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(tblsearch);
        tblsearch.getColumnModel().getColumn(1).setPreferredWidth(150);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Select Route");

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.setSearchMode(org.jdesktop.swingx.JXSearchField.SearchMode.REGULAR);
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        cmdexecute2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employer.png"))); // NOI18N
        cmdexecute2.setMnemonic('r');
        cmdexecute2.setText("Execute Extend Due Date");
        cmdexecute2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexecute2ActionPerformed(evt);
            }
        });

        jLabel11.setText("Billing Month");

        cmbbm1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "--SELECT--" }));
        cmbbm1.setToolTipText("");
        cmbbm1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbbm1ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Select Account");

        tblperaccount.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AccountNo", "Name of Account", "BillMonth", "DueDate", "ExtendDate", "Remarks", "t", "r"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblperaccount.getTableHeader().setReorderingAllowed(false);
        tblperaccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblperaccountMouseReleased(evt);
            }
        });
        jScrollPane5.setViewportView(tblperaccount);
        tblperaccount.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblperaccount.getColumnModel().getColumn(4).setPreferredWidth(100);

        tblperaccount.getColumnModel().getColumn(5).setPreferredWidth(150);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("List of accounts to be adjust");

        jLabel14.setForeground(new java.awt.Color(0, 153, 51));
        jLabel14.setText("(Double click to add)");

        cmdexecute3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        cmdexecute3.setMnemonic('r');
        cmdexecute3.setText("Remove");
        cmdexecute3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdexecute3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbroute1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbbm1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbtown1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel10))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmdexecute2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdexecute3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cmbtown1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(cmbroute1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel11)
                            .addComponent(cmbbm1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jLabel14))
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdexecute2)
                            .addComponent(cmdexecute3))
                        .addGap(18, 18, 18))))
        );

        jTabbedPane1.addTab("Extend Per Account", jPanel3);

        tbllog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Log Details", "Remarks", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbllog.getTableHeader().setReorderingAllowed(false);
        tbllog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbllogMouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tbllog);
        tbllog.getColumnModel().getColumn(0).setPreferredWidth(500);
        tbllog.getColumnModel().getColumn(1).setPreferredWidth(230);

        txtsearchlog.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearchlog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchlogActionPerformed(evt);
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
                        .addComponent(txtsearchlog, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1251, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(txtsearchlog, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Logs", jPanel4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbtownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtownActionPerformed

        try {
            Item2 item = (Item2) cmbtown.getSelectedItem();
            towncode = item.getId();

        } catch (Exception e) {
        }
        populate_cmbroute();
    }//GEN-LAST:event_cmbtownActionPerformed

    private void cmbrouteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbrouteActionPerformed
        try {
            Item3 item = (Item3) cmbroute.getSelectedItem();
            routecode = item.getId();

        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbrouteActionPerformed

    private void cmdCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCreateActionPerformed
        if ("--SELECT--".equals(cmbtown.getSelectedItem().toString()) || "--SELECT--".equals(cmbroute.getSelectedItem().toString()) || "--SELECT--".equals(cmbbm.getSelectedItem().toString()) || txtreason.getText().isEmpty() == true) {
            JOptionPane.showMessageDialog(this, "Fill-up all the required fields!");
        } else {

            int rcnt = tbl.getRowCount();
            if (rcnt == 0) {
                addExtend();
            } else {
                int i = 0;
                int cntr = 0;
                for (i = 0; i < tbl.getRowCount(); i++) {
                    if (tbl.getModel().getValueAt(i, 1).equals(routecode)) {
                        cntr++;
                    }
                }
                if (cntr > 0) {
                    JOptionPane.showMessageDialog(this, "Already Exist!");
                } else {
                    addExtend();
                }
            }
        }

    }//GEN-LAST:event_cmdCreateActionPerformed

    void addExtend() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String ExtendDate = dateFormat.format(txtextenddate.getDate());

        Date dd = null;
        try {
            dd = GetDueDate();
            String DueDate = dateFormat.format(dd);

            if (txtextenddate.getDate().compareTo(GetDueDate()) < 0) {
                JOptionPane.showMessageDialog(this, "Not yet overdue!");
            } else {
                model.addRow(new Object[]{towncode, routecode, cmbbm.getSelectedItem(), DueDate, ExtendDate, txtreason.getText()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Not yet billed!");
        }
    }


    private void cmdexecuteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexecuteActionPerformed
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No route/s added in the list!");
        } else {
            ExtendDueDatePerRoute();
        }
    }//GEN-LAST:event_cmdexecuteActionPerformed

    private void cmdexecute1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexecute1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            model.removeRow(row);
        }
    }//GEN-LAST:event_cmdexecute1ActionPerformed

    private void cmbbmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbbmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbbmActionPerformed

    private void tblMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseReleased
//        pop.setVisible(true);

    }//GEN-LAST:event_tblMouseReleased

    private void mnuviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuviewActionPerformed
        int col = 5; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        String reason = tbl.getValueAt(row, col).toString();
        JOptionPane.showMessageDialog(this, "Reason: " + reason);

    }//GEN-LAST:event_mnuviewActionPerformed

    private void cmbroute1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbroute1ActionPerformed
        try {
            Item5 item = (Item5) cmbroute1.getSelectedItem();
            routecode1 = item.getId();

        } catch (Exception e) {
        }

        Refresh();
    }//GEN-LAST:event_cmbroute1ActionPerformed

    private void cmbtown1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtown1ActionPerformed
        try {
            Item4 item = (Item4) cmbtown1.getSelectedItem();
            towncode1 = item.getId();

        } catch (Exception e) {
        }
        populate_cmbroute1();
        Refresh();
    }//GEN-LAST:event_cmbtown1ActionPerformed

    private void tblsearchMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblsearchMouseReleased
        //popsearch.setVisible(true);
    }//GEN-LAST:event_tblsearchMouseReleased

    private void tbllogMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbllogMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbllogMouseReleased

    private void cmdexecute2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexecute2ActionPerformed
                if (model3.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No account/s added in the list!");
        } else {
            ExtendDueDatePerAccount();
        }
    }//GEN-LAST:event_cmdexecute2ActionPerformed

    private void cmbbm1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbbm1ActionPerformed
        Refresh();
    }//GEN-LAST:event_cmbbm1ActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateSearchTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void tblperaccountMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblperaccountMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblperaccountMouseReleased

    private void tblsearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblsearchMouseClicked
        if (evt.getClickCount() == 2) {
            AddAccount();
        }
    }//GEN-LAST:event_tblsearchMouseClicked

    private void txtsearchlogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchlogActionPerformed
        if (txtsearchlog.getText().isEmpty() == true) {
            try {
                model4.setNumRows(0);
            } catch (Exception e) {
            }
        } else {
            populateLogTBL();
        }
    }//GEN-LAST:event_txtsearchlogActionPerformed

    private void cmdexecute3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdexecute3ActionPerformed
         int col = 0; //set column value to 0
        int row = tblperaccount.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblperaccount.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id =tblperaccount.getValueAt(row, col).toString();
            model3.removeRow(row);
        }
    }//GEN-LAST:event_cmdexecute3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbbm;
    private javax.swing.JComboBox cmbbm1;
    private javax.swing.JComboBox cmbroute;
    private javax.swing.JComboBox cmbroute1;
    private javax.swing.JComboBox cmbtown;
    private javax.swing.JComboBox cmbtown1;
    private javax.swing.JButton cmdCreate;
    private javax.swing.JButton cmdexecute;
    private javax.swing.JButton cmdexecute1;
    private javax.swing.JButton cmdexecute2;
    private javax.swing.JButton cmdexecute3;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JMenuItem mnuadd;
    private javax.swing.JMenuItem mnuview;
    private javax.swing.JPopupMenu pop;
    private javax.swing.JPopupMenu popsearch;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tbllog;
    private javax.swing.JTable tblperaccount;
    private javax.swing.JTable tblsearch;
    private com.toedter.calendar.JDateChooser txtextenddate;
    private javax.swing.JTextField txtreason;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    private org.jdesktop.swingx.JXSearchField txtsearchlog;
    // End of variables declaration//GEN-END:variables
}
