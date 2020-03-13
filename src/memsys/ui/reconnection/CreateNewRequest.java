/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.reconnection;

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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;
import memsys.global.ORValidator;
import memsys.global.myFunctions;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author LESTER JP CADIZ
 */
public class CreateNewRequest extends javax.swing.JDialog {

    public static NewDiscoReco frmParent;
    public static AddPaymentCharges frmAdd;
    public static EditPaymentCharges frmEdit;
    public static String AcctNo, Nym, Stat, OrderBy, TownCode, RouteCode, NearAcctNos;
    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer cellAlignRightRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2, model3, model4;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    public static CreateNewRequest frmCreateNewRequest;
    static int typeid, areaid, RouteSeqNo;
    public static SearchBillDeposit frmSearchBillDeposit;
    static String nowDate = FunctionFactory.GetSystemNowDateString();
    static String nowDate2 = FunctionFactory.getSystemNowDateTimeString();

    public CreateNewRequest(NewDiscoReco parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);

        model = (DefaultTableModel) tblpayments.getModel();

        PopulateCMBType();
        GetOldAcctCode();
        this.setTitle("Create New Request: " + Nym + " Account No.:" + AcctNo + "/" + TownCode + "-" + RouteCode + "-" + RouteSeqNo + " - " + Stat);
        jPanel1.setVisible(false);
        lblpayments.setVisible(false);
        jscrollpayments.setVisible(false);
        lblbd.setVisible(false);
        txtbd.setVisible(false);
        cmdbd.setVisible(false);
        cmdadditem.setVisible(false);
        cmdremoveitem.setVisible(false);
        cmdeditamount.setVisible(false);
        txtrecommendation.setVisible(false);
        lblreccomendation.setVisible(false);
        lbltotal.setVisible(false);
        txttotal.setVisible(false);

        TableColumn idClmn2 = tblpayments.getColumn("id");
        idClmn2.setMaxWidth(0);
        idClmn2.setMinWidth(0);
        idClmn2.setPreferredWidth(0);

        TableColumn idClmn4 = tblpayments.getColumn("coaid");
        idClmn4.setMaxWidth(0);
        idClmn4.setMinWidth(0);
        idClmn4.setPreferredWidth(0);

        tblpayments.setCellSelectionEnabled(false);
        tblpayments.setRowSelectionAllowed(true);
        tblpayments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblpayments.setSelectionBackground(new Color(153, 204, 255));
        tblpayments.setSelectionForeground(Color.BLACK);

        tblMonth.setCellSelectionEnabled(false);
        tblMonth.setRowSelectionAllowed(true);
        tblMonth.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMonth.setSelectionBackground(new Color(153, 204, 255));
        tblMonth.setSelectionForeground(Color.BLACK);

        txtremarks.setVisible(false);

        PopulateCmbOffice();

        populateTBLNearaAcctNo();
        ORValidator valdtr = new ORValidator();
        double Balance = valdtr.GetCurrentBalance(AcctNo);
        lblunpaid.setText(myFunctions.amountFormat(Balance));
        SetORDate();
    }

    void SetORDate() {
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

    public void showFrmAdd() {
        frmAdd = new AddPaymentCharges(this, true);
        frmAdd.setVisible(true);
    }

    public void showFrmEdit() {
        frmEdit = new EditPaymentCharges(this, true);
        frmEdit.setVisible(true);
    }

    public static void rsAddConnLog(int acctno, String remarks, int statid, int uid, String nowDate, String note) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO connLogTBL (AcctNo, Remarks, StatusID, UserID, TransDate, Note) VALUES (" + acctno + ",'" + remarks + "'," + statid + "," + uid + ",'" + nowDate + "','" + note + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void GetOldAcctCode() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT TownCode, RouteCode, RouteSeqNo FROM Consumer WHERE AcctNo=" + AcctNo;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                TownCode = rs.getString(1);
                RouteCode = rs.getString(2);
                RouteSeqNo = rs.getInt(3);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void editPayments(int row, String unit, String qty, String amnt) {
        double total = Double.parseDouble(amnt.replace(",", "")) * Double.parseDouble(qty.replace(",", ""));
        tblpayments.getModel().setValueAt(unit, row, 2);
        tblpayments.getModel().setValueAt(myFunctions.amountFormat2(qty), row, 3);
        tblpayments.getModel().setValueAt(myFunctions.amountFormat2(amnt), row, 4);
        tblpayments.getModel().setValueAt(myFunctions.amountFormat2(String.valueOf(total)), row, 5);
        CalculateTotalPayments();
    }

    public void insertPayments(int id, String title, String unit, String qty, String amnt, String coaid) {
        double total = Double.valueOf(amnt) * Double.valueOf(qty);
        ((DefaultTableModel) tblpayments.getModel()).addRow(new Object[]{id, title, unit, qty, myFunctions.amountFormat2(amnt), myFunctions.amountFormat2(String.valueOf(total)), coaid});
        CalculateTotalPayments();
    }

    public void populateTBLNearaAcctNo() {
        Connection conn = MainDBConn.getConnection();
        String createString;

        int tseq = RouteSeqNo + 50;
        int bseq = RouteSeqNo - 50;

        createString = "SELECT CONCAT(AcctNo,'/',TownCode, '-', RouteCode,'-', RouteSeqNo) as acctno, AcctName, AcctAddress FROM Consumer WHERE TownCode='" + TownCode + "' AND RouteCode='" + RouteCode + "' AND RouteSeqNo>" + bseq + " AND RouteSeqNo<" + tseq + "";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);
            model4 = (DefaultTableModel) tblMonth.getModel();

            cellAlignCenterRenderer.setHorizontalAlignment(0);
            cellAlignRightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

            tblMonth.setRowHeight(21);

            // tblMonth.getColumnModel().getColumn(2).setCellRenderer(cellAlignRightRenderer);
            tblMonth.setColumnSelectionAllowed(false);
            model4.setNumRows(0);

            while (rs.next()) {
                model4.addRow(new Object[]{false, rs.getString(1), rs.getString(2), rs.getString(3)});

            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void PopulateCmbOffice() {
        cmboffice.addItem(new Item2(1, "Main Office"));
        cmboffice.addItem(new Item2(2, "Guihulngan Office"));
        cmboffice.addItem(new Item2(3, "Bais Office"));
        cmboffice.addItem(new Item2(4, "Mabinay Office"));
        cmboffice.addItem(new Item2(5, "BAPA Section"));
    }

    private void AddNewRequest() {
        AddRequest();
        AddPayments();

        if (IsAcctExist(Integer.parseInt(AcctNo)) == false) {
            AddToConnTBL(Integer.parseInt(AcctNo));
        }

        rsAddConnLog(Integer.parseInt(AcctNo), cmbtype.getSelectedItem().toString() + " request created", 12, ParentWindow.getUserID(), nowDate2, "");

    }

    private void Transact() {

        int col = 1;
        int cntr = 0;
        String nas = "";
        int rows = tblMonth.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            String na = (String) tblMonth.getModel().getValueAt(i, col);
            String nan = (String) tblMonth.getModel().getValueAt(i, 2);
            String selTF = tblMonth.getValueAt(i, 0).toString();
            if ("true".equals(selTF)) {
                cntr++;
                if (cntr == 1) {
                    nas = na + ":" + nan;
                } else {
                    nas = nas + ", " + na + ":" + nan;
                }
            }
        }
        if (cntr == 0) {
            JOptionPane.showMessageDialog(this, "No record is selected! Please select a record from the table!");
        } else {
            NearAcctNos = nas;
            if (o4.isSelected() == true) {
                if (txtremarks.getText().isEmpty() == true) {
                    JOptionPane.showMessageDialog(this, "Please fill-up the remarks");
                } else {
                    switch (typeid) {
                        case 7:
                            if (txtDM.getText().isEmpty() == true) {
                                JOptionPane.showMessageDialog(this, "Please fill-up the required fields");
                            } else {
                                cmdCreate.setEnabled(false);
                                AddNewRequest();
                                frmParent.populateRequestTBL();
                                this.dispose();
                                JOptionPane.showMessageDialog(this, "Request Successfully Created!");
                            }
                        case 8:
                            if (txtDM.getText().isEmpty() == true || txtDOR.getText().isEmpty() == true) {
                                JOptionPane.showMessageDialog(this, "Please fill-up the required fields");
                            } else {
                                cmdCreate.setEnabled(false);
                                AddNewRequest();
                                frmParent.populateRequestTBL();
                                this.dispose();
                                JOptionPane.showMessageDialog(this, "Request Successfully Created!");
                            }
                            break;
                        default:
                            break;
                    }
                }
            } else {
                switch (typeid) {
                    case 7:
                        if (txtDM.getText().isEmpty() == true) {
                            JOptionPane.showMessageDialog(this, "Please fill-up the required fields");
                        } else {
                            cmdCreate.setEnabled(false);
                            AddNewRequest();
                            frmParent.populateRequestTBL();
                            this.dispose();
                            JOptionPane.showMessageDialog(this, "Request Successfully Created!");
                        }

                    case 8:
                        if (txtDM.getText().isEmpty() == true || txtDOR.getText().isEmpty() == true) {
                            JOptionPane.showMessageDialog(this, "Please fill-up the required fields");
                        } else {
                            cmdCreate.setEnabled(false);
                            AddNewRequest();
                            frmParent.populateRequestTBL();
                            this.dispose();
                            JOptionPane.showMessageDialog(this, "Request Successfully Created!");
                        }
                        break;
                    default:
                        break;
                }
            }
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

    public void showFrmSearchBillDeposit() {
        frmSearchBillDeposit = new SearchBillDeposit(this, true);
        frmSearchBillDeposit.setVisible(true);
    }

    public void TagBillDeposit(String billdeposit) {
        txtbd.setText(billdeposit);

        int rows = tblpayments.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {
            String coaid = (String) tblpayments.getModel().getValueAt(i, 6);
            int id = Integer.parseInt(coaid);
            if (id == 31 || id == 117) {
                ((DefaultTableModel) tblpayments.getModel()).removeRow(i);
            }
        }
        CalculateTotalPayments();
        //31 117
    }

    public static int GetCurrentMeterSN(int AcctNo) {
        int mtrsn = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT MeterSN FROM ConsumerMeter WHERE AcctNo=" + AcctNo;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                mtrsn = rs.getInt(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return mtrsn;
    }

    private static int GetMaxID() {
        int max = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT MAX(DiscoRecoID) FROM discoRecoTransTBL";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                max = rs.getInt(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return max;
    }

    private void AddPayments() {
        int rows = tblpayments.getModel().getRowCount();
        for (int i = 0; i < rows; i++) {;
            String desc = (String) tblpayments.getModel().getValueAt(i, 1);
            String unit = (String) tblpayments.getModel().getValueAt(i, 2);
            String qty = (String) tblpayments.getModel().getValueAt(i, 3);
            String amnt = (String) tblpayments.getModel().getValueAt(i, 4);
            String totalamnt = (String) tblpayments.getModel().getValueAt(i, 5);
            String coaid = (String) tblpayments.getModel().getValueAt(i, 6);

            if (!"0.00".equals(totalamnt)) {
                // AddPaymentItem(desc, unit, 1, 1, 1, 1);
                System.out.println(Double.parseDouble(qty.replace(",", "")));
                System.out.println(Double.parseDouble(amnt.replace(",", "")));
                System.out.println(Double.parseDouble(totalamnt.replace(",", "")));
                System.out.println(Integer.parseInt(coaid));
                AddPaymentItem(desc, unit, Double.parseDouble(qty.replace(",", "")), Double.parseDouble(amnt.replace(",", "")), Double.parseDouble(totalamnt.replace(",", "")), coaid);
            }
        }
    }

    private void AddPaymentItem(String desc, String unit, double qty, double amnt, double totalamnt, String coaid) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO costingTBL (AcctNo, description, unit, qty, cost, total, COAID, MemberProcessTypeID, DiscoRecoID) "
                + "VALUES ('" + AcctNo + "','" + desc + "','" + unit + "'," + qty + ",'" + amnt + "','" + totalamnt + "'," + coaid + ",1," + GetMaxID() + ")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void AddRequest() {
        String rem = "";

        if (o1.isSelected() == true) {
            rem = rem + "- Needs Inpection\n";
        }
        if (o2.isSelected() == true) {
            rem = "- Ready for Re-connection\n";
        }
        if (o3.isSelected() == true) {
            rem = "- For Change Meter\n";
        }
        if (o4.isSelected() == true) {
            rem = "- " + txtremarks.getText() + "\n";
        }
        if (o5.isSelected() == true) {
            rem = "- Ready for Disconnection\n";
        }

        int opt = 1;
        if (p1.isSelected() == true) {
            opt = 1;
        } else if (p2.isSelected() == true) {
            opt = 2;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String seldate = dateFormat.format(txtdate.getDate());

        ORValidator valdtr = new ORValidator();
        double AmntPaid = valdtr.GetAmountPaid(txtDOR.getText().trim(), opt, seldate);
        double Balance = valdtr.GetCurrentBalance(AcctNo) + AmntPaid;

        String remarksfinal = rem + txtDM1.getText();

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO discoRecoTransTBL ("
                + "AcctNo, "
                + "TypeID, "
                + "TransDate, "
                + "status, "
                + "CurrentMeterSN, "
                + "BillDeposit, "
                + "Remarks, "
                + "Recommendation, "
                + "UserID, "
                + "ContactNo, "
                + "DeliquentOR, "
                + "NearAcctNo, "
                + "DeliquentMonths, "
                + "OrderForAction, OfficeFlg, "
                + "DeliquentBalance, DeliquentAmountPaid) "
                + "VALUES ('"
                + AcctNo + "',"
                + typeid + ",'"
                + nowDate + "',"
                + 12 + ","
                + GetCurrentMeterSN(Integer.parseInt(AcctNo)) + ",'"
                + txtbd.getText() + "','"
                + remarksfinal + "','"
                + txtrecommendation.getText() + "', "
                + ParentWindow.getUserID() + ", '"
                + txtcn.getText() + "', '"
                + txtDOR.getText() + "', '"
                + NearAcctNos + "', '"
                + txtDM.getText() + "', '"
                + lblorder.getText() + "', " + areaid + ", '" + Balance + "','" + AmntPaid + "'"
                + ")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void CalculateTotalPayments() {
        int r = tblpayments.getRowCount();
        int c;
        c = 0;
        double totalAmnt = 0;
        while (c < r + 1) {
            try {
                String amntStr = tblpayments.getValueAt(c, 5).toString().replace(",", "");
                double amnt = Double.parseDouble(amntStr);

                totalAmnt = totalAmnt + amnt;

            } catch (Exception e) {
                e.getStackTrace();
            }
            c++;
        }
        txttotal.setText(myFunctions.amountFormat(totalAmnt));
    }

    public void PopulateCMBType() {
        cmbtype.addItem("--SELECT--");
        Connection conn = MainDBConn.getConnection();
        String createString = "";

        if (null != Stat) {
            switch (Stat) {
                case "Connected":
                    createString = "SELECT CTypeID, DescripType FROM costingUQTypeTBL WHERE MemberProcessTypeID=1";
                    break;
                case "Disconnected":
                    createString = "SELECT CTypeID, DescripType FROM costingUQTypeTBL WHERE MemberProcessTypeID=1";
                    break;
            }
        }

        // createString = "SELECT CTypeID, DescripType FROM costingUQTypeTBL WHERE MemberProcessTypeID=1;";
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                cmbtype.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    boolean IsAcctExist(int acctno) {
        boolean found = false;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM connTBL WHERE AcctNo='" + acctno + "'";

        int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                rc++;
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        if (rc > 0) {
            found = true;
        }

        return found;
    }

    public static void AddToConnTBL(int acctno) {
        // MDI.UserID
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO connTBL(AcctNo, "
                + "TownCode, "
                + "RouteCode, "
                + "AcctCode, "
                + "RouteSeqNo, "
                + "ClassCode, "
                + "AcctName, "
                + "AcctAddress, "
                + "MembershipID, "
                + "UserID, "
                + "TransDate, BrgyCode, Status) "
                + "SELECT AcctNo, TownCode, RouteCode, AcctCode, RouteSeqNo, ClassCode, AcctName, AcctAddress, MembershipID, " + ParentWindow.getUserID() + ",'" + nowDate2 + "', BrgyCode, 12 "
                + "FROM Consumer WHERE AcctNo='" + acctno + "'";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void LoadPayments() {

        populateTBLPayments();
        CalculateTotalPayments();

    }

    public void populateTBLPayments() {
        Connection conn = MainDBConn.getConnection();
        String createString;

        // createString = "SELECT t.PaymentTypeID, t.PaymentDesc, d.Amount FROM discoRecoPaymentDefaultTBL d"
        //       + " INNER JOIN discoRecoPaymentTypeTBL t ON d.PaymentTypeID=t.PaymentTypeID"
        //     + " WHERE d.TypeID=" + typeid;
        createString = "SELECT setupID, COADesc, unit, qty, amount, qty*amount, c.COAID FROM costingUQSetupTBL c INNER JOIN COA a ON c.COAID=a.COAID WHERE CTypeID=" + typeid;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            cellAlignCenterRenderer.setHorizontalAlignment(0);
            cellAlignRightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

            tblpayments.setRowHeight(29);

            tblpayments.getColumnModel().getColumn(2).setCellRenderer(cellAlignCenterRenderer);
            tblpayments.getColumnModel().getColumn(3).setCellRenderer(cellAlignCenterRenderer);
            tblpayments.getColumnModel().getColumn(4).setCellRenderer(cellAlignRightRenderer);
            tblpayments.getColumnModel().getColumn(5).setCellRenderer(cellAlignRightRenderer);
            tblpayments.setColumnSelectionAllowed(false);
            model.setNumRows(0);

            int cnt = 0;
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), myFunctions.amountFormat2(rs.getString(5)), myFunctions.amountFormat2(rs.getString(6)), rs.getString(7)});
                cnt++;
            }
            if (cnt != 0) {
                tblpayments.setRowSelectionInterval(0, 0);
            }
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        o2 = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMonth = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtcn = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtremarks = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        txtDM = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmboffice = new javax.swing.JComboBox();
        lblorder = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtDM1 = new javax.swing.JTextField();
        o5 = new javax.swing.JRadioButton();
        o1 = new javax.swing.JCheckBox();
        o3 = new javax.swing.JCheckBox();
        o4 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtDOR = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        p1 = new javax.swing.JRadioButton();
        txtdate = new com.toedter.calendar.JDateChooser();
        lblunpaid = new javax.swing.JLabel();
        p2 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        cmbtype = new javax.swing.JComboBox();
        lblpayments = new javax.swing.JLabel();
        lblbd = new javax.swing.JLabel();
        cmdbd = new javax.swing.JButton();
        txtbd = new javax.swing.JTextField();
        jscrollpayments = new javax.swing.JScrollPane();
        tblpayments = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        cmdCancel = new javax.swing.JButton();
        cmdCreate = new javax.swing.JButton();
        lblreccomendation = new javax.swing.JLabel();
        txtrecommendation = new javax.swing.JTextField();
        txttotal = new javax.swing.JLabel();
        lbltotal = new javax.swing.JLabel();
        cmdadditem = new javax.swing.JButton();
        cmdremoveitem = new javax.swing.JButton();
        cmdeditamount = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Create New Request");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Recommendation");

        buttonGroup1.add(o2);
        o2.setSelected(true);
        o2.setText("Ready for Reconnection");
        o2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o2ActionPerformed(evt);
            }
        });

        tblMonth.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Account No.", "Account Name", "Address"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblMonth);
        if (tblMonth.getColumnModel().getColumnCount() > 0) {
            tblMonth.getColumnModel().getColumn(2).setHeaderValue("Account Name");
            tblMonth.getColumnModel().getColumn(3).setHeaderValue("Address");
        }
        tblMonth.getColumnModel().getColumn(0).setMaxWidth(20);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Nearest Neighbor Consumer Account");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Contact No.");

        txtremarks.setColumns(20);
        txtremarks.setRows(5);
        jScrollPane3.setViewportView(txtremarks);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Deliquent Period");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Ordered for action by");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setText("Office where consumer applied");

        cmboffice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmboffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbofficeActionPerformed(evt);
            }
        });

        lblorder.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblorder.setForeground(new java.awt.Color(51, 102, 0));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Landmark");

        buttonGroup1.add(o5);
        o5.setText("Ready for Disconnection");
        o5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o5ActionPerformed(evt);
            }
        });

        o1.setText("Need Inspection");

        o3.setText("Change Meter");

        o4.setText("Others");
        o4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o4ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 0));
        jLabel8.setText("Paid Under O.R. No. ");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 102, 0));
        jLabel13.setText("O.R. Date");

        txtDOR.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtDOR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDORFocusLost(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("Unpaid Balance:");

        buttonGroup2.add(p1);
        p1.setSelected(true);
        p1.setText("Paid on Teller");

        txtdate.setDateFormatString("yyyy-MM-dd");
        txtdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        lblunpaid.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblunpaid.setForeground(new java.awt.Color(255, 0, 0));
        lblunpaid.setText("0.00");
        lblunpaid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));

        buttonGroup2.add(p2);
        p2.setText("Paid on Cashier");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtDOR, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(p1))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(p2)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblunpaid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtDOR, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(p1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(p2))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblunpaid, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(o4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(o1)
                                    .addComponent(o3))
                                .addGap(59, 59, 59)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(o5)
                                    .addComponent(o2))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtcn, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmboffice, 0, 326, Short.MAX_VALUE)
                                    .addComponent(lblorder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtDM, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDM1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(104, 104, 104)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(o2)
                    .addComponent(o1))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(o5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(o3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(o4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDM, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDM1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cmboffice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel9)
                    .addComponent(lblorder, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        cmbtype.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbtype.setMaximumRowCount(15);
        cmbtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbtypeActionPerformed(evt);
            }
        });

        lblpayments.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblpayments.setText("Payment Details");

        lblbd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblbd.setText("Bill Deposit ");

        cmdbd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        cmdbd.setMnemonic('k');
        cmdbd.setText("Verify");
        cmdbd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdbdActionPerformed(evt);
            }
        });

        txtbd.setEnabled(false);

        tblpayments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Payment Item", "Unit", "QTY", "Amount", "Total", "coaid"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jscrollpayments.setViewportView(tblpayments);
        if (tblpayments.getColumnModel().getColumnCount() > 0) {
            tblpayments.getColumnModel().getColumn(0).setResizable(false);
        }
        tblpayments.getColumnModel().getColumn(1).setPreferredWidth(250);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Type of Request");

        cmdCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdCancel.setMnemonic('C');
        cmdCancel.setText("Cancel");
        cmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelActionPerformed(evt);
            }
        });

        cmdCreate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdCreate.setMnemonic('r');
        cmdCreate.setText("Create");
        cmdCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCreateActionPerformed(evt);
            }
        });

        lblreccomendation.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblreccomendation.setText("Remarks");

        txttotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txttotal.setForeground(new java.awt.Color(0, 102, 0));
        txttotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txttotal.setText("0.00");
        txttotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbltotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbltotal.setForeground(new java.awt.Color(0, 102, 0));
        lbltotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbltotal.setText("Total");
        lbltotal.setToolTipText("");

        cmdadditem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdadditem.setMnemonic('r');
        cmdadditem.setText("Add Item");
        cmdadditem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdadditemActionPerformed(evt);
            }
        });

        cmdremoveitem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        cmdremoveitem.setMnemonic('e');
        cmdremoveitem.setText("Remove Item");
        cmdremoveitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdremoveitemActionPerformed(evt);
            }
        });

        cmdeditamount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        cmdeditamount.setMnemonic('i');
        cmdeditamount.setText("Edit Item");
        cmdeditamount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdeditamountActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblreccomendation)
                    .addComponent(lblbd)
                    .addComponent(lblpayments, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cmdadditem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdeditamount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdremoveitem, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jscrollpayments)
                    .addComponent(cmbtype, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtbd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cmdCreate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdCancel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                .addComponent(lbltotal, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(11, 11, 11)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdbd, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txtrecommendation))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(cmbtype, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jscrollpayments, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(lblpayments)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdadditem)
                    .addComponent(cmdremoveitem)
                    .addComponent(cmdeditamount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(lblbd)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtbd, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmdbd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtrecommendation, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblreccomendation))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbltotal))
                        .addComponent(txttotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(cmdCreate)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCreateActionPerformed
        int opt = 1;
        if (p1.isSelected() == true) {
            opt = 1;
        } else if (p2.isSelected() == true) {
            opt = 2;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String seldate = dateFormat.format(txtdate.getDate());
        ORValidator valdtr = new ORValidator();
        boolean payval = valdtr.ValidatePayments(AcctNo, txtDOR.getText().trim(), opt, seldate);

        double AmntPaid = valdtr.GetAmountPaid(txtDOR.getText().trim(), opt, seldate);
        double Balance = valdtr.GetCurrentBalance(AcctNo);// + AmntPaid;
        double Percentage = AmntPaid / Balance * 100;

        switch (typeid) {
            case 7:
                if (Balance == 0) {
                    Transact();
                } else {
                    if (payval == false) {
                        JOptionPane.showMessageDialog(this, " Unable to create reconnection request! \r\n Payment must reach 50% of the total payment! \r\n  \r\n Details: "
                                + "\r\n Total Balance: " + myFunctions.amountFormat(Balance) + ""
                                + "\r\n Amount Paid:   " + myFunctions.amountFormat(AmntPaid) + ""
                                + "\r\n Percentage:     " + myFunctions.amountFormat(Percentage) + "%"
                                + "");
                    } else if (payval == true) {
                        Transact();
                    }
                }
                break;
            case 8:
                if (Balance == 0) {
                    Transact();
                } else {
//                    if (payval == false) {
//                        JOptionPane.showMessageDialog(this, " Unable to create reconnection request! \r\n Payment must reach 50% of the total payment! \r\n  \r\n Details: "
//                                + "\r\n Total Balance: " + myFunctions.amountFormat(Balance) + ""
//                                + "\r\n Amount Paid:   " + myFunctions.amountFormat(AmntPaid) + ""
//                                + "\r\n Percentage:     " + myFunctions.amountFormat(Percentage) + "%"
//                                + "");
//                    } else if (payval == true) {
                        Transact();
//                    }
                    break;
                }
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            default:
                break;
        }


    }//GEN-LAST:event_cmdCreateActionPerformed

    private void cmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdCancelActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

    }//GEN-LAST:event_formWindowOpened

    private void cmbtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbtypeActionPerformed
        try {
            Item item = (Item) cmbtype.getSelectedItem();
            typeid = item.getId();

            switch (typeid) {
                case 7:
                    jPanel1.setVisible(true);
                    lblpayments.setVisible(true);
                    jscrollpayments.setVisible(true);
                    lblbd.setVisible(false);
                    txtbd.setVisible(false);
                    cmdbd.setVisible(false);
                    cmdadditem.setVisible(true);
                    cmdremoveitem.setVisible(true);
                    cmdeditamount.setVisible(true);
                    txtrecommendation.setVisible(true);
                    lblreccomendation.setVisible(true);
                    lbltotal.setVisible(true);
                    txttotal.setVisible(true);
                    o5.setSelected(true);
                    //  jPanel3.setVisible(false);
                    break;
                case 8:
                    jPanel1.setVisible(true);
                    lblpayments.setVisible(true);
                    jscrollpayments.setVisible(true);
                    lblbd.setVisible(true);
                    txtbd.setVisible(true);
                    cmdbd.setVisible(true);
                    cmdadditem.setVisible(true);
                    cmdremoveitem.setVisible(true);
                    cmdeditamount.setVisible(true);
                    txtrecommendation.setVisible(true);
                    lblreccomendation.setVisible(true);
                    lbltotal.setVisible(true);
                    txttotal.setVisible(true);
                    o2.setSelected(true);
                    //  jPanel3.setVisible(true);

                    break;
                case 9:
                    jPanel1.setVisible(true);
                    lblpayments.setVisible(true);
                    jscrollpayments.setVisible(true);
                    lblbd.setVisible(true);
                    txtbd.setVisible(true);
                    cmdbd.setVisible(true);
                    cmdadditem.setVisible(true);
                    cmdremoveitem.setVisible(true);
                    cmdeditamount.setVisible(true);
                    txtrecommendation.setVisible(true);
                    lblreccomendation.setVisible(true);
                    lbltotal.setVisible(true);
                    txttotal.setVisible(true);
                    o5.setSelected(true);
                    break;
                case 10:
                    jPanel1.setVisible(true);
                    lblpayments.setVisible(true);
                    jscrollpayments.setVisible(true);
                    lblbd.setVisible(false);
                    txtbd.setVisible(false);
                    cmdbd.setVisible(false);
                    cmdadditem.setVisible(true);
                    cmdremoveitem.setVisible(true);
                    cmdeditamount.setVisible(true);
                    txtrecommendation.setVisible(true);
                    lblreccomendation.setVisible(true);
                    lbltotal.setVisible(true);
                    txttotal.setVisible(true);
                    o5.setSelected(true);
                    break;
                case 11:
                    jPanel1.setVisible(false);
                    lblpayments.setVisible(true);
                    jscrollpayments.setVisible(true);
                    lblbd.setVisible(true);
                    txtbd.setVisible(true);
                    cmdbd.setVisible(true);
                    cmdadditem.setVisible(false);
                    txtrecommendation.setVisible(true);
                    lblreccomendation.setVisible(true);
                    lbltotal.setVisible(true);
                    txttotal.setVisible(true);
                    break;
                case 12:
                    jPanel1.setVisible(false);
                    lblpayments.setVisible(true);
                    jscrollpayments.setVisible(true);
                    lblbd.setVisible(true);
                    txtbd.setVisible(true);
                    cmdbd.setVisible(true);
                    cmdadditem.setVisible(false);
                    txtrecommendation.setVisible(true);
                    lblreccomendation.setVisible(true);
                    lbltotal.setVisible(true);
                    txttotal.setVisible(true);
                    break;
                case 13:
                    jPanel1.setVisible(false);
                    lblpayments.setVisible(true);
                    jscrollpayments.setVisible(true);
                    lblbd.setVisible(true);
                    txtbd.setVisible(true);
                    cmdbd.setVisible(true);
                    cmdadditem.setVisible(false);
                    txtrecommendation.setVisible(true);
                    lblreccomendation.setVisible(true);
                    lbltotal.setVisible(true);
                    txttotal.setVisible(true);
                    break;
                case 14:
                    jPanel1.setVisible(false);
                    lblpayments.setVisible(true);
                    jscrollpayments.setVisible(true);
                    lblbd.setVisible(true);
                    txtbd.setVisible(true);
                    cmdbd.setVisible(true);
                    cmdadditem.setVisible(false);
                    txtrecommendation.setVisible(true);
                    lblreccomendation.setVisible(true);
                    lbltotal.setVisible(true);
                    txttotal.setVisible(true);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
        }
        LoadPayments();
    }//GEN-LAST:event_cmbtypeActionPerformed

    private void cmdadditemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdadditemActionPerformed
        showFrmAdd();
    }//GEN-LAST:event_cmdadditemActionPerformed

    private void cmdbdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdbdActionPerformed
        SearchBillDeposit.acctnym = Nym;
        showFrmSearchBillDeposit();
    }//GEN-LAST:event_cmdbdActionPerformed

    private void o2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o2ActionPerformed
        txtremarks.setVisible(false);
    }//GEN-LAST:event_o2ActionPerformed

    private void cmbofficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbofficeActionPerformed
        OrderBy = "";
        System.out.println(areaid);
        try {
            Item2 item = (Item2) cmboffice.getSelectedItem();
            areaid = item.getId();

            switch (areaid) {
                case 1:
                   
                    OrderBy = " ENGR. XENO REUBEN GENE ZERNA";
                    break;
                case 2:
                    OrderBy = "ENGR. SILVERIO P. ACASIO";
                    break;
                case 3:
                    OrderBy = "ENGR. ROSSWARD INFANTE";
                    break;
                case 4:
                    OrderBy = "ENGR. JAY BRIAN RUBIO";
                    break;
                case 5:
                    OrderBy = "CHARLIE OLPOS";
                    break;
                default:
                    break;
            }

            lblorder.setText(OrderBy);
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbofficeActionPerformed

    private void o5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o5ActionPerformed
        int col = 0; //set column value to 0
        int row = tblpayments.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblpayments.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = tblpayments.getValueAt(row, col).toString();
            int i = myFunctions.msgboxYesNo("Are you sure you want delete this current record?");
            switch (i) {
                case 0:
//                    rsDelete(Integer.parseInt(id));
//                    populateTBL();
                    JOptionPane.showMessageDialog(this, "Record has been successfully deleted!");
                    break;
                case 1:
                    return; //do nothing
                case 2:
                    this.dispose(); //exit window
                    return;
                default:
                    break;
            }

        }
    }//GEN-LAST:event_o5ActionPerformed

    private void cmdremoveitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdremoveitemActionPerformed
//int col = 0; //set column value to 0
        int row = tblpayments.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblpayments.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a item from the list!");
        } else {
            int i = myFunctions.msgboxYesNo("Are you sure you want delete this current item?");
            switch (i) {
                case 0:
                    ((DefaultTableModel) tblpayments.getModel()).removeRow(row);
                    CalculateTotalPayments();
                    JOptionPane.showMessageDialog(this, "Item has been successfully deleted!");
                    break;
                case 1:
                    return; //do nothing
                case 2:
                    this.dispose(); //exit window
                    return;
                default:
                    break;
            }
        }
    }//GEN-LAST:event_cmdremoveitemActionPerformed

    private void cmdeditamountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdeditamountActionPerformed
        int row = tblpayments.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tblpayments.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
        } else {
            String item = tblpayments.getValueAt(row, 1).toString();
            String unit = tblpayments.getValueAt(row, 2).toString();
            String qty = tblpayments.getValueAt(row, 3).toString();
            String amnt = tblpayments.getValueAt(row, 4).toString();

            EditPaymentCharges.row = row;
            EditPaymentCharges.caption = item;
            EditPaymentCharges.unit = unit;
            EditPaymentCharges.qty = qty;
            EditPaymentCharges.amnt = amnt;
            showFrmEdit();
        }
    }//GEN-LAST:event_cmdeditamountActionPerformed

    private void o4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o4ActionPerformed
        if (o4.isSelected() == true) {
            txtremarks.setVisible(true);
            txtremarks.requestFocus();
        } else {
            txtremarks.setVisible(false);

        }
    }//GEN-LAST:event_o4ActionPerformed

    private void txtDORFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDORFocusLost

    }//GEN-LAST:event_txtDORFocusLost

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
            java.util.logging.Logger.getLogger(CreateNewRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateNewRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateNewRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateNewRequest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CreateNewRequest dialog = new CreateNewRequest(frmParent, true);
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmboffice;
    private javax.swing.JComboBox cmbtype;
    private javax.swing.JButton cmdCancel;
    private javax.swing.JButton cmdCreate;
    private javax.swing.JButton cmdadditem;
    private javax.swing.JButton cmdbd;
    private javax.swing.JButton cmdeditamount;
    private javax.swing.JButton cmdremoveitem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jscrollpayments;
    private javax.swing.JLabel lblbd;
    private javax.swing.JLabel lblorder;
    private javax.swing.JLabel lblpayments;
    private javax.swing.JLabel lblreccomendation;
    private javax.swing.JLabel lbltotal;
    private javax.swing.JLabel lblunpaid;
    private javax.swing.JCheckBox o1;
    private javax.swing.JRadioButton o2;
    private javax.swing.JCheckBox o3;
    private javax.swing.JCheckBox o4;
    private javax.swing.JRadioButton o5;
    private javax.swing.JRadioButton p1;
    private javax.swing.JRadioButton p2;
    private javax.swing.JTable tblMonth;
    private javax.swing.JTable tblpayments;
    private javax.swing.JTextField txtDM;
    private javax.swing.JTextField txtDM1;
    private javax.swing.JFormattedTextField txtDOR;
    private javax.swing.JTextField txtbd;
    private javax.swing.JTextField txtcn;
    private com.toedter.calendar.JDateChooser txtdate;
    private javax.swing.JTextField txtrecommendation;
    private javax.swing.JTextArea txtremarks;
    private javax.swing.JLabel txttotal;
    // End of variables declaration//GEN-END:variables
}
