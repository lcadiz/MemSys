package memsys.ui.process;

import memsys.global.DBConn.MainDBConn;

import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import memsys.global.myReports;

import memsys.ui.overide.override_payment;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import memsys.global.DBCommandFactory;
import memsys.global.FunctionFactory;
import memsys.ui.main.ParentWindow;

public final class IssueCO extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    public static override_payment frmo;
    public static COInfo frminfo;
    public static PostStatCOIssue frmpost;
    public static int i;
    static String tcode, rcode, seq;

    static DefaultTableCellRenderer cellAlignLeftRenderer = new DefaultTableCellRenderer();

    public IssueCO() {
        initComponents();
        i = 0;
        // populateTBL();
        txtsearch.requestFocus();
        tbl.setCellSelectionEnabled(false);
        tbl.setRowSelectionAllowed(true);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setSelectionBackground(new Color(153, 204, 255));
        tbl.setSelectionForeground(Color.BLACK);
        panelremarks.setVisible(false);

        TableColumn idClmn = tbl.getColumn("conum");
        idClmn.setMaxWidth(0);
        idClmn.setMinWidth(0);
        idClmn.setPreferredWidth(0);

//        TableColumn idClmn1 = tbl.getColumn("seq");
//        idClmn1.setMaxWidth(0);
//        idClmn1.setMinWidth(0);
//        idClmn1.setPreferredWidth(0);

        TableColumn idClmn2 = tbl.getColumn("route");
        idClmn2.setMaxWidth(0);
        idClmn2.setMinWidth(0);
        idClmn2.setPreferredWidth(0);

        TableColumn idClmn3 = tbl.getColumn("id");
        idClmn3.setMaxWidth(0);
        idClmn3.setMinWidth(0);
        idClmn3.setPreferredWidth(0);
    }

    public boolean IsBapaRoute(String routecode) {
        boolean isbapa = false;
        int flg = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        System.out.println(tcode);
        createString = "SELECT BAPAFlag FROM Route WHERE TownCode='" + FunctionFactory.NumFormatter(Integer.parseInt(tcode)) + "'  AND RouteCode='" + routecode + "'";

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

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
        return isbapa;
    }

    public boolean IsCMAssignedAlready(int an) {
        boolean isCMAA = false;
        int flg = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT AcctNoSub FROM ConsumerMainSub WHERE AcctNoSub='" + an + "'";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                flg++;
            }

            stmt.close();
            conn.close();

            if (flg == 0) {
                isCMAA = false;
            } else if (flg >= 1) {
                isCMAA = true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/019: Consumer Main Sub!");
        }
        return isCMAA;
    }

    void getTCodeRouteCode(int acctno) {

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT TownCode, RouteCode FROM Consumer WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                tcode = rs.getString("TownCode");
                rcode = rs.getString("RouteCode");
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }
    }

    static int getMaxCTRLNo() {
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

    public void showFrmOverride() {
        frmo = new override_payment(this, true);
        frmo.setVisible(true);
    }//

    public void showFrmPostStat() {
        frmpost = new PostStatCOIssue(this, true);
        frmpost.setVisible(true);
    }//

    public void showFrmInfo() {

        frminfo = new COInfo(this, true);
        frminfo.setVisible(true);

    }//

    public void viewAll() {
        // txtsearch.setText("");
        //   pane.setVisible(false);
        populateTBL();
    }

    public void viewCustom() {
        //   pane.setVisible(true);
        //     txtsearch.requestFocus();
    }

    int GetORNo(int ano) {
        int orno = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT ORNo FROM collectionmisc WHERE acctno=" + ano;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                orno = rs.getInt(1);
                //gender = rs.getInt(2);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return orno;
    }

    String GetRemarks(int acctno) {
        String note = "";
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT Note FROM connLogTBL WHERE acctno=" + acctno;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                note = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }

        return note;
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
        createString = "SELECT  CONCAT(c.AcctNo,'   [',FORMAT(cast(t.TownCode as INT),'00'),'-',t.RouteCode,'-',t.AcctCode,']'), c.MembershipID, c.AcctName, CONVERT(char(10), c.TransDate, 101) , c.ClassCode, c.AcctAddress,  MIN(orno), override, e.Name, i.Remarks, c.AcctNo, t.RouteCode, COALESCE(t.RouteSeqNo, 0)AS RouteSeqNo " 
                + "                    FROM connTBL c LEFT JOIN CollectionMisc m ON c.AcctNo=m.acctno "
                + "                                   LEFT JOIN connCOInfoTBL i ON m.acctno=i.acctno"
                + "                                   LEFT JOIN electricianTBL e ON i.eID=e.eID"
                + "                                   LEFT JOIN consumer t ON c.acctno=t.acctno"
                + "                    WHERE c.Status=6 AND (c.acctname LIKE '%" + txtsearch.getText() + "%' OR c.AcctNo like '%" + txtsearch.getText() + "%')  "
                + "                    GROUP BY recID, c.AcctName, t.TownCode,t.RouteCode,t.AcctCode, t.RouteSeqNo, c.AcctNo, c.MembershipID,  c.TransDate , c.ClassCode, c.AcctAddress,override, e.Name,i.Remarks "
                + "                    ORDER BY c.AcctName  ";
//        }

        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            renderer.setHorizontalAlignment(0);
            cellAlignLeftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

            tbl.setRowHeight(29);
            tbl.getColumnModel().getColumn(1).setCellRenderer(cellAlignLeftRenderer);
            tbl.getColumnModel().getColumn(2).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(4).setCellRenderer(renderer);
            tbl.getColumnModel().getColumn(5).setCellRenderer(renderer);
            tbl.getColumn("Edit").setCellRenderer(new ButtonRenderer(1));
            tbl.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), 1));

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

                model.addRow(new Object[]{rs.getString(11), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(7), rs.getString(5), rs.getString(6), rs.getString(9), rs.getString(10), rs.getString(8), "", "", rs.getString(12), rs.getString(13)});
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
                int row = tbl.getSelectedRow();
                String acctno = tbl.getValueAt(row, col).toString();
                String klas = tbl.getValueAt(row, 5).toString();
                String mi = tbl.getValueAt(row, 2).toString();
                String nym = tbl.getValueAt(row, 3).toString();
                String seq = tbl.getValueAt(row, 13).toString();
                COInfo.ac = Integer.parseInt(acctno);

                String gcn = getContactNo(Integer.parseInt(mi));
                COInfo.cn = gcn;
                COInfo.nym = nym;

                COInfo.membrid = Integer.parseInt(mi);
                COInfo.klas = klas;

                getTCodeRouteCode(Integer.parseInt(acctno));
                COInfo.tcode = tcode;
                COInfo.rcode = rcode;
                COInfo.seq = seq;
System.out.println (tcode);
                showFrmInfo();

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

    public static int determineIfPaid(int AcctID) {
        int t = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT Count(*) FROM costingTBL WHERE AcctNo=" + AcctID;
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                t = rs.getInt(1);
            }

            //t= rs.getFetchSize();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return t;
    }

    boolean determineIfSet(int AcctID) {
        boolean t = false;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT eID FROM connCOInfoTBL WHERE AcctNo=" + AcctID;
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            int c = 0;
            while (rs.next()) {
                c++;
            }

            if (c != 0) {
                t = true;
            }

            //t= rs.getFetchSize();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return t;
    }

    double getSDW(int AcctID) {

        double t = 0;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT sum(qty) FROM costingTBL WHERE COAID=243 and AcctNo=" + AcctID;
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                t = (rs.getDouble(1) - 1) + 30;
            }

            //t= rs.getFetchSize();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return t;
    }

    String getContactNo(int memid) {
        String t = "";
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT contactNo FROM membersTBL WHERE memberID=" + memid;
        //stmtIncomingShed

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                t = rs.getString(1);
            }

            //t= rs.getFetchSize();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewOpt = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jToolBar2 = new javax.swing.JToolBar();
        jToolBar1 = new javax.swing.JToolBar();
        cmdprint = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        cmdApprove = new javax.swing.JButton();
        cmdRefresh1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        cmdCosting = new javax.swing.JButton();
        cmdExit1 = new javax.swing.JButton();
        cmdpost1 = new javax.swing.JButton();
        cmdpost = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        t1 = new javax.swing.JToggleButton();
        t2 = new javax.swing.JToggleButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        cmdExit = new javax.swing.JButton();
        panelremarks = new javax.swing.JPanel();
        lbl1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lbl2 = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Connect Order Issuance");
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

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Account No", "MemberID", "Account Name", "OR No.", "Class", "Account Address", "Electrician", "Remarks", "Override", "Edit", "conum", "route", "seq"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true, false, false, false
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
        tbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblMouseReleased(evt);
            }
        });
        tbl.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblPropertyChange(evt);
            }
        });
        tbl.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                tblVetoableChange(evt);
            }
        });
        jScrollPane1.setViewportView(tbl);
        if (tbl.getColumnModel().getColumnCount() > 0) {
            tbl.getColumnModel().getColumn(0).setResizable(false);
            tbl.getColumnModel().getColumn(11).setResizable(false);
            tbl.getColumnModel().getColumn(12).setResizable(false);
            tbl.getColumnModel().getColumn(13).setResizable(false);
        }
        //set column width
        tbl.getColumnModel().getColumn(1).setPreferredWidth(80);
        tbl.getColumnModel().getColumn(2).setMaxWidth(80);
        tbl.getColumnModel().getColumn(4).setMaxWidth(130);
        tbl.getColumnModel().getColumn(5).setMaxWidth(50);
        tbl.getColumnModel().getColumn(9).setMaxWidth(80);
        tbl.getColumnModel().getColumn(10).setMaxWidth(50);

        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        jToolBar2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar2.add(jToolBar1);

        cmdprint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdprint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/printer.png"))); // NOI18N
        cmdprint.setMnemonic('P');
        cmdprint.setText("    Print    ");
        cmdprint.setFocusable(false);
        cmdprint.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdprint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdprint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdprintActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdprint);
        jToolBar2.add(jSeparator3);

        cmdApprove.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdApprove.setForeground(new java.awt.Color(0, 102, 153));
        cmdApprove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/employer.png"))); // NOI18N
        cmdApprove.setMnemonic('S');
        cmdApprove.setText("   Send to Approval    ");
        cmdApprove.setFocusable(false);
        cmdApprove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdApprove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdApproveActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdApprove);

        cmdRefresh1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdRefresh1.setForeground(new java.awt.Color(255, 153, 0));
        cmdRefresh1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ab.png"))); // NOI18N
        cmdRefresh1.setMnemonic('R');
        cmdRefresh1.setText("  Return to Costing");
        cmdRefresh1.setFocusable(false);
        cmdRefresh1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdRefresh1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdRefresh1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdRefresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRefresh1ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdRefresh1);
        jToolBar2.add(jSeparator1);

        cmdCosting.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdCosting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calculator.png"))); // NOI18N
        cmdCosting.setMnemonic('C');
        cmdCosting.setText("    View Costing    ");
        cmdCosting.setFocusable(false);
        cmdCosting.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdCosting.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdCosting.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdCosting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCostingActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdCosting);

        cmdExit1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/pos.png"))); // NOI18N
        cmdExit1.setMnemonic('O');
        cmdExit1.setText("   Override Payment       ");
        cmdExit1.setFocusable(false);
        cmdExit1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExit1ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdExit1);

        cmdpost1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdpost1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/transactions.png"))); // NOI18N
        cmdpost1.setMnemonic('P');
        cmdpost1.setText("    Bypass CM Assignment     ");
        cmdpost1.setFocusable(false);
        cmdpost1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdpost1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdpost1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdpost1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdpost1ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdpost1);

        cmdpost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdpost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        cmdpost.setMnemonic('P');
        cmdpost.setText("    Post Status     ");
        cmdpost.setFocusable(false);
        cmdpost.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cmdpost.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdpost.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdpost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdpostActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdpost);
        jToolBar2.add(jSeparator2);

        buttonGroup1.add(t1);
        t1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        t1.setForeground(new java.awt.Color(0, 102, 0));
        t1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/download.png"))); // NOI18N
        t1.setMnemonic('y');
        t1.setSelected(true);
        t1.setText("   Today   ");
        t1.setActionCommand("     Today     ");
        t1.setEnabled(false);
        t1.setFocusable(false);
        t1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        t1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        t1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1ActionPerformed(evt);
            }
        });
        jToolBar2.add(t1);

        buttonGroup1.add(t2);
        t2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        t2.setForeground(new java.awt.Color(153, 0, 0));
        t2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/summary.png"))); // NOI18N
        t2.setText("   Previous   ");
        t2.setEnabled(false);
        t2.setFocusable(false);
        t2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        t2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        t2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t2ActionPerformed(evt);
            }
        });
        jToolBar2.add(t2);
        jToolBar2.add(jSeparator4);

        cmdExit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit_2.png"))); // NOI18N
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
        jToolBar2.add(cmdExit);

        lbl1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lbl1.setForeground(new java.awt.Color(255, 0, 204));
        lbl1.setText("Remarks from ISM");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/myinquiry.png"))); // NOI18N

        lbl2.setColumns(20);
        lbl2.setRows(5);
        jScrollPane3.setViewportView(lbl2);

        javax.swing.GroupLayout panelremarksLayout = new javax.swing.GroupLayout(panelremarks);
        panelremarks.setLayout(panelremarksLayout);
        panelremarksLayout.setHorizontalGroup(
            panelremarksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelremarksLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelremarksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelremarksLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 895, Short.MAX_VALUE))
                    .addGroup(panelremarksLayout.createSequentialGroup()
                        .addComponent(lbl1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelremarksLayout.setVerticalGroup(
            panelremarksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelremarksLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelremarksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1162, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panelremarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelremarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved
   }//GEN-LAST:event_tblMouseMoved

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
//        viewAll();
        // populateTBL();
    }//GEN-LAST:event_formInternalFrameOpened

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();

    }//GEN-LAST:event_cmdExitActionPerformed

    private void cmdApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdApproveActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String acctno = tbl.getValueAt(row, col).toString();
            String memid = tbl.getValueAt(row, 1).toString();
            String rt = tbl.getValueAt(row, 12).toString();
            String seq = tbl.getValueAt(row, 13).toString();

            int rtc = Integer.parseInt(rt);
            int seqc = Integer.parseInt(seq);
            getTCodeRouteCode(Integer.parseInt(acctno));

            if (rtc == 0 || seqc == 0) {
                JOptionPane.showMessageDialog(this, "Either the Route Code or Route Sequence Number is required!");
            } else {

                boolean isbapa = IsBapaRoute(rt);

                if (isbapa == false) {
                    SendForApproval(Integer.parseInt(acctno));
                } else {
                    boolean isCMAA = IsCMAssignedAlready(Integer.parseInt(acctno));
                    if (isCMAA == false) {
                        JOptionPane.showMessageDialog(this, "Please assign check meter!");
                    } else {
                        SendForApproval(Integer.parseInt(acctno));
                    }
                }
            }
        }
    }//GEN-LAST:event_cmdApproveActionPerformed

    void SendForApproval(int acctno) {
        boolean isset = determineIfSet(acctno);
        if (isset == true) {

            boolean x = checkCostingIfPaid(acctno);

            if (x == false) {
                JOptionPane.showMessageDialog(this, "This Account is not yet paid!");
                return;

            } else if (x == true) {
                int uid = ParentWindow.getUserID();
                myDataenvi.rsAddConnLog(acctno, "Connect order issued ", 6, uid, nowDate, "");
                myDataenvi.rsAddConnLog(acctno, "Send to Approval ", 6, uid, nowDate, "");
                // myReports.rptConnectOrder(acctno, nowDate, memid);
                DBCommandFactory.rsUpdateConnStat(acctno, 10);
                populateTBL();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please set the HW Electrician and remarks!");
        }
    }

    public static boolean checkCostingIfPaid(int acctno) {
        boolean fexist = false;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT AcctNo FROM costingTBL WHERE AcctNo=" + acctno + " AND transID IS NOT NULL";
        //stmtIncomingShed

        int t = 0;
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

        if (t != 0) {
            fexist = true;
        }
        return fexist;
    }

    public static boolean checkIfSet(int acctno) {
        boolean fexist = false;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT AcctNo FROM costingTBL WHERE AcctNo=" + acctno + " AND transID IS NOT NULL";
        //stmtIncomingShed

        int t = 0;
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

        if (t != 0) {
            fexist = true;
        }
        return fexist;
    }

    private void cmdExit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExit1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
        } else {
            String id = tbl.getValueAt(row, col).toString();
            override_payment.acctno = Integer.parseInt(id);
            showFrmOverride();
        }
    }//GEN-LAST:event_cmdExit1ActionPerformed

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void cmdRefresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRefresh1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String id = tbl.getValueAt(row, col).toString();
            int x = myFunctions.msgboxYesNo("This Record will now transfer Back to Approval Section!" + "\n" + "It will not be available here in approval section unless the Approval Section sends back this record ");
            switch (x) {
                case 0:
                    myDataenvi.rsUpdateConnStat(Integer.parseInt(id), 2);
                    int uid = ParentWindow.getUserID();
                    myDataenvi.rsAddConnLog(Integer.parseInt(id), "Send back to costing", 2, uid, nowDate, "");
                    populateTBL();
                    JOptionPane.showMessageDialog(this, "Record has been successfully sent!");
                    break;
                case 1:
                    break;
                case 2:
                    this.dispose(); //exit window
                    break;
                default:
            }
        }
    }//GEN-LAST:event_cmdRefresh1ActionPerformed

    private void cmdCostingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCostingActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();
            String nym = tbl.getValueAt(row, 2).toString();
            String a = tbl.getValueAt(row, 5).toString();

            CostingOp.acctno = Integer.parseInt(id);
            CostingOp.acctname = nym;
            CostingOp.address = a;

            boolean x = myDataenvi.checkCosting(Integer.parseInt(id));

            if (x == true) {
                int dbl = determineIfBigloads(Integer.parseInt(id));
                String capt = null;
                String note = null;

                if (dbl == 1) {
                    capt = "SUBTOTAL>>";
                    note = "NOTE: The amount of Energy Deposit stated above is only refundable upon termination of contract on electric service, as per Coop Policy.";
                } else {
                    capt = "TOTAL";
                    note = "";
                }
                myReports.rptCosting2(Integer.parseInt(id), nym, a, capt, note, "PAYMENT COSTING");
            }
        }
    }//GEN-LAST:event_cmdCostingActionPerformed

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

    private void cmdprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdprintActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");

        } else {

            String acctno = tbl.getValueAt(row, col).toString();
            String memid = tbl.getValueAt(row, 1).toString();
            String rt = tbl.getValueAt(row, 12).toString();
            String seq = tbl.getValueAt(row, 13).toString();

            boolean isset = determineIfSet(Integer.parseInt(acctno));

//            int rtc = Integer.parseInt(rt);
//            int seqc = Integer.parseInt(seq);
//
//            if (rtc == 0 || seqc == 0) {
//                JOptionPane.showMessageDialog(this, "Route Code and Route Sequence Number is required!");
//            } else {
            if (isset == true) {
                boolean x = checkCostingIfPaid(Integer.parseInt(acctno));
                // double sdw = getSDW(Integer.parseInt(acctno));
//                if (x == false) {
//                    JOptionPane.showMessageDialog(this, "This Account is not yet paid!");
//                    return;

//                } else if (x == true) {
                int uid = ParentWindow.getUserID();
                myDataenvi.rsAddConnLog(Integer.parseInt(acctno), "Connect Order printed ", 6, uid, nowDate, "");
                myReports.rptConnectOrder1(acctno);
//                }
            } else {
                JOptionPane.showMessageDialog(this, "Please set the HW Electrician and remarks!");
            }
//            }
        }
    }//GEN-LAST:event_cmdprintActionPerformed

    private void t1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1ActionPerformed
        i = 0;
        populateTBL();
        txtsearch.requestFocus();
    }//GEN-LAST:event_t1ActionPerformed

    private void t2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2ActionPerformed
        i = 1;
        // populateTBL();
        txtsearch.requestFocus();
    }//GEN-LAST:event_t2ActionPerformed

    private void tblVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_tblVetoableChange

    }//GEN-LAST:event_tblVetoableChange

    private void tblPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblPropertyChange

    }//GEN-LAST:event_tblPropertyChange

    private void tblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseClicked

    }//GEN-LAST:event_tblMouseClicked

    private void tblMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseReleased
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            // JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
        } else {
            String acctno = tbl.getValueAt(row, col).toString();
            String r = GetRemarks(Integer.parseInt(acctno));
            System.out.println(r);
            try {
                if (r.equals("")) {

                    panelremarks.setVisible(false);
                } else {
                    lbl2.setText(r);
                    panelremarks.setVisible(true);
                }
            } catch (Exception e) {
            }
        }


    }//GEN-LAST:event_tblMouseReleased

    private void cmdpostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdpostActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String id = tbl.getValueAt(row, col).toString();

            //  String acctno = jTBLConn.getValueAt(0, 0).toString();
            PostStatCOIssue.an = Integer.parseInt(id);
            System.out.print(id);
            showFrmPostStat();
        }

    }//GEN-LAST:event_cmdpostActionPerformed

    private void cmdpost1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdpost1ActionPerformed
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please select a record from the list!");
            return;
        } else {
            String acctno = tbl.getValueAt(row, col).toString();
            String memid = tbl.getValueAt(row, 1).toString();
            String rt = tbl.getValueAt(row, 12).toString();
            String seq = tbl.getValueAt(row, 13).toString();

            int rtc = Integer.parseInt(rt);
            int seqc = Integer.parseInt(seq);
            getTCodeRouteCode(Integer.parseInt(acctno));

//            if (rtc == 0 || seqc == 0) {
//                JOptionPane.showMessageDialog(this, "Either the Route Code or Route Sequence Number is required!");
//            } else {
//
//                boolean isbapa = IsBapaRoute(rt);
//
//                if (isbapa == false) {
//                    SendForApproval(Integer.parseInt(acctno));
//                } else {
//                    boolean isCMAA = IsCMAssignedAlready(Integer.parseInt(acctno));
//                    if (isCMAA == false) {
//                        JOptionPane.showMessageDialog(this, "Please assign check meter!");
//                    } else {
                        SendForApproval(Integer.parseInt(acctno));
//                    }
//                }
//            }
        }
    }//GEN-LAST:event_cmdpost1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton cmdApprove;
    private javax.swing.JButton cmdCosting;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdExit1;
    private javax.swing.JButton cmdRefresh1;
    private javax.swing.JButton cmdpost;
    private javax.swing.JButton cmdpost1;
    private javax.swing.JButton cmdprint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lbl1;
    private javax.swing.JTextArea lbl2;
    private javax.swing.JPanel panelremarks;
    private javax.swing.JToggleButton t1;
    private javax.swing.JToggleButton t2;
    private javax.swing.JTable tbl;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    private javax.swing.ButtonGroup viewOpt;
    // End of variables declaration//GEN-END:variables
}
