package memsys.ui.pmes;

import memsys.ui.devices.CurrentPhoto1;
import memsys.ui.devices.CurrentSignature;
import memsys.global.DBConn.MainDBConn;
import memsys.global.myDataenvi;
import memsys.global.myFunctions;
import memsys.global.myReports;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import memsys.global.FunctionFactory;

public final class Attendance extends javax.swing.JInternalFrame {

    static Statement stmtAttendance, stmtUpdate, stmt;
    static Statement stmtSelectSched;
    public static int BatchID, i;
    public static String myTitle;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer cellAlignRightRenderer = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2;
    public static AddNewParticipants frmAdd;
    public static CurrentSignature frmCSignature;

    public static UpdateParticipant frmUpdate;
    public static CurrentPhoto1 frmcphoto;
    public static SearchAttendance frmSA;

    public Attendance() {
        initComponents();
        i = 0;
        populateTBLSched();
        cmdAdd.setMnemonic('A');
        cmdUpdate.setMnemonic('E');
        cmdDelete.setMnemonic('R');
        cmdExit.setMnemonic('x');
        cmdPrint.setMnemonic('P');
        cmdCancelPart.setMnemonic('t');
        cmdPrint.setEnabled(false);
        cmdRePrint.setEnabled(false);
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
        setdates();
        f.setVisible(false);
        BatchID = 0;

        TableColumn idClmn = tbl1.getColumn("Printed");
        idClmn.setMaxWidth(0);
        idClmn.setMinWidth(0);
        idClmn.setPreferredWidth(0);
    }

    public void showFrmAdd() {
        frmAdd = new AddNewParticipants(this, true);
        frmAdd.setVisible(true);
    }

    public void showFrmSA() {
        frmSA = new SearchAttendance(this, true);
        frmSA.setVisible(true);
    }

    void setdates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date theDate = null;
        try {
            theDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        txtdte.setDate(theDate);
        //  txtend.setDate(theDate);
    }

    public void showFrmUpdate() {
        frmUpdate = new UpdateParticipant(this, true);
        frmUpdate.setVisible(true);
    }

    public void populateTBLSched() {

        Connection conn = MainDBConn.getConnection();
        String createString = "";

        switch (i) {
            case 0:
                createString = "SELECT batchID,sched_date, sched_address, sched_venue FROM scheduleTBL WHERE sched_stat=0 and BatchID<>413 AND BatchID<>587  AND BatchID<>656 ORDER BY sched_date DESC";
                break;
            case 1:
                if (o1.isSelected() == true) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String eeDate = dateFormat.format(txtdte.getDate());
                    System.out.println(eeDate);
                    createString = "SELECT batchID,sched_date, sched_address, sched_venue FROM scheduleTBL WHERE sched_stat=1 and BatchID<>413 AND BatchID<>587 and sched_date='" + eeDate + "' ORDER BY sched_date DESC";

                } else if (o2.isSelected() == true) {
                    createString = "SELECT batchID,sched_date, sched_address, sched_venue FROM scheduleTBL WHERE sched_stat=1 and BatchID<>413 and BatchID<>587 and sched_address like '%" + textsearch1.getText() + "%' ORDER BY sched_date DESC";
                } else if (o3.isSelected() == true) {
                    createString = "SELECT batchID,sched_date, sched_address, sched_venue FROM scheduleTBL WHERE year(sched_date)=" + yr.getYear();
                }

                break;
            case 2:
                createString = "SELECT batchID,sched_date, sched_address, sched_venue FROM scheduleTBL WHERE BatchID=413 OR BatchID=656";
                break;

            default:
        }
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model2 = (DefaultTableModel) tbl.getModel();

            cellAlignCenterRenderer.setHorizontalAlignment(0);
            cellAlignRightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

            tbl.setRowHeight(29);

            tbl.getColumnModel().getColumn(0).setCellRenderer(cellAlignCenterRenderer);
            tbl.getColumnModel().getColumn(1).setCellRenderer(cellAlignRightRenderer);
            tbl.getColumn("Open").setCellRenderer(new ButtonRenderer12(1));
            tbl.getColumn("Open").setCellEditor(new ButtonEditor12(new JCheckBox(), 1));
            tbl.setColumnSelectionAllowed(false);

            model2.setNumRows(0);
            int cnt = 0;

            while (rs.next()) {
                String img = null;

                //   img = "/img/build.png";
                DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                String nwdate = dateFormat.format(rs.getDate(2));

                //  String lbl = "<html><table border=0 cellpadding=0 cellspacing=0>"
                //        + "<tr><td>" + nwdate + "</td></th>";
//                String lbl2 = "<html><table border=0 cellpadding=0 cellspacing=0>"
//                        + "<tr><td rowspan=2><img src=" + getClass().getResource(img) + ">&nbsp</td><td><b>" + rs.getString(4) + "<b></td></tr>"
//                        + "<tr><td>" + rs.getString(3) + "</td></tr></table>";
                model2.addRow(new Object[]{rs.getInt(1), nwdate, rs.getString(3)});
                cnt++;
            }

            stmt.close();
            conn.close();

            
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void rsUpdatePartRec(int bID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.participantsTBL "
                + "SET print_count=1, date_printed='" + nowDate + "' WHERE batchID=" + bID + "";

        try {
            stmtUpdate = conn.createStatement();
            stmtUpdate.executeUpdate(createString);
            stmtUpdate.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsClosedSched(int bID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.scheduleTBL "
                + "SET sched_stat=1 WHERE batchID='" + bID + "'";

        try {
            stmtUpdate = conn.createStatement();
            stmtUpdate.executeUpdate(createString);
            stmtUpdate.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsUpdatePartStat(String pID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.participantsTBL "
                + "SET part_stat=1 WHERE partID='" + pID + "'";

        try {
            stmtUpdate = conn.createStatement();
            stmtUpdate.executeUpdate(createString);
            stmtUpdate.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsUpdatePrntCount(String pID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.participantsTBL "
                + "SET print_count=print_count+1 WHERE partID=" + pID;

        try {
            stmtUpdate = conn.createStatement();
            stmtUpdate.executeUpdate(createString);
            stmtUpdate.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT partID, part_lname, "
                + "part_fname, part_mname, part_ext, "
                + "address,  CONVERT(char(10), date_encoded, 101),"
                + "CONVERT(char(10), date_printed, 101)"
                + "FROM participantsTBL WHERE batchID=" + BatchID + "AND part_stat=0"
                + " AND part_lname+', '+part_fname LIKE '%" + txts.getText() + "%' "
                + " ORDER BY part_lname,part_fname";

        try {
            stmtAttendance = conn.createStatement();
            ResultSet rs = stmtAttendance.executeQuery(createString);

            model = (DefaultTableModel) tbl1.getModel();

            cellAlignCenterRenderer.setHorizontalAlignment(0);

            tbl1.setRowHeight(29);
            tbl1.getColumnModel().getColumn(0).setCellRenderer(cellAlignCenterRenderer);
            tbl1.getColumn("IMG").setCellRenderer(new ButtonRenderer11(1));
            tbl1.getColumn("IMG").setCellEditor(new ButtonEditor11(new JCheckBox(), 1));
            tbl1.getColumn("Sign").setCellRenderer(new ButtonRenderer11(2));
            tbl1.getColumn("Sign").setCellEditor(new ButtonEditor11(new JCheckBox(), 2));

            tbl1.setColumnSelectionAllowed(false);

            model.setNumRows(0);

            int rc = 0;

            while (rs.next()) {
                String prnflg = null;

                if (rs.getString(8) == null) {
                    prnflg = "Not Yet Printed";
                } else {
                    prnflg = rs.getString(8);
                }

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7), prnflg});
                rc++;
            }

            if (rc == 0) {
                cmdPrint.setEnabled(false);
                cmdRePrint.setEnabled(false);
            } else {
                cmdPrint.setEnabled(true);
                cmdRePrint.setEnabled(true);
            }
            lblt.setText(rc + " Record/s");
            stmtAttendance.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void capture() {
        int col = 0; //set column value to 0
        int row = tbl1.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl1.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String memID = tbl1.getValueAt(row, col).toString();
            String name = tbl1.getValueAt(row, 1).toString() + ", " + tbl1.getValueAt(row, 2).toString() + " " + tbl1.getValueAt(row, 3).toString();
            CurrentPhoto1.name = name;
            CurrentPhoto1.memID = memID;
            showFrmCPhoto();
        }
    }

    void pirma() {
        int col = 0; //set column value to 0
        int row = tbl1.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl1.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String memID = tbl1.getValueAt(row, col).toString();
            String name = tbl1.getValueAt(row, 1).toString() + ", " + tbl1.getValueAt(row, 2).toString() + " " + tbl1.getValueAt(row, 3).toString();
            CurrentSignature.name = name;
            CurrentSignature.memID = memID;
            showFrmSignaturePad();
        }
    }

    void showFrmCPhoto() {
        frmcphoto = new CurrentPhoto1(this, true);
        frmcphoto.setVisible(true);
    }

    void showFrmSignaturePad() {
        frmCSignature = new CurrentSignature(this, true);
        frmCSignature.setVisible(true);
    }

    class ButtonEditor12 extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;
        int flg;

        public ButtonEditor12(JCheckBox checkBox, int x) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            flg = x;
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
                opensched();
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

    class ButtonRenderer12 extends JButton implements TableCellRenderer {

        int iflg;

        public ButtonRenderer12(int ico) {
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
            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/muser.png"));
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

    class ButtonEditor11 extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;
        int flg;

        public ButtonEditor11(JCheckBox checkBox, int x) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            flg = x;
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
                if (flg == 1) {
                    capture();
                } else if (flg == 2) {
                    pirma();
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
    }

    class ButtonRenderer11 extends JButton implements TableCellRenderer {

        int iflg;

        public ButtonRenderer11(int ico) {
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
            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/msupplier.png"));
            Icon ico2 = new javax.swing.ImageIcon(getClass().getResource("/img/editm.png"));

            if (iflg == 1) {
                setIcon(ico1);
            } else if (iflg == 2) {
                setIcon(ico2);
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    ////////////
    public void opensched() {
        int col = 0; //set column value to 0
        int row = tbl.getSelectedRow(); //get value of selected value

        String id = tbl.getValueAt(row, col).toString();
        BatchID = Integer.parseInt(id);

        populateTBL();
        String BNo = Integer.toString(BatchID);
        txtBatchNo.setText(BNo);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl1 = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        cmdAdd = new javax.swing.JButton();
        cmdUpdate = new javax.swing.JButton();
        cmdDelete = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        cmdPrint = new javax.swing.JButton();
        cmdRePrint = new javax.swing.JButton();
        cmdCancelPart = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        cmdExit4 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        cmdExit = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        txtBatchNo = new javax.swing.JLabel();
        txts = new org.jdesktop.swingx.JXSearchField();
        lblt = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        jToolBar2 = new javax.swing.JToolBar();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        cmdAdd1 = new javax.swing.JButton();
        f = new javax.swing.JPanel();
        txtdte = new com.toedter.calendar.JDateChooser();
        textsearch1 = new org.jdesktop.swingx.JXSearchField();
        o2 = new javax.swing.JRadioButton();
        o1 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        o3 = new javax.swing.JRadioButton();
        yr = new com.toedter.calendar.JYearChooser();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("PMES Attendance");
        setAutoscrolls(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
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

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IDNo", "Last Name", "First Name", "Middle", "Ext", "Address", "Encoded", "Printed", "IMG", "Sign"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true, true
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
        jScrollPane1.setViewportView(tbl1);
        if (tbl1.getColumnModel().getColumnCount() > 0) {
            tbl1.getColumnModel().getColumn(0).setResizable(false);
        }
        //set column width
        tbl1.getColumnModel().getColumn(0).setMaxWidth(50);

        tbl1.getColumnModel().getColumn(1).setPreferredWidth(160);
        tbl1.getColumnModel().getColumn(2).setPreferredWidth(160);
        tbl1.getColumnModel().getColumn(3).setMaxWidth(100);
        tbl1.getColumnModel().getColumn(4).setMaxWidth(50);
        tbl1.getColumnModel().getColumn(6).setMaxWidth(100);
        tbl1.getColumnModel().getColumn(5).setPreferredWidth(160);
        tbl1.getColumnModel().getColumn(8).setMaxWidth(40);
        tbl1.getColumnModel().getColumn(9).setMaxWidth(40);

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator2);

        cmdAdd.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/add.png"))); // NOI18N
        cmdAdd.setMnemonic('A');
        cmdAdd.setText("      Add      ");
        cmdAdd.setFocusable(false);
        cmdAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAddActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdAdd);

        cmdUpdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        cmdUpdate.setMnemonic('E');
        cmdUpdate.setText("    Edit     ");
        cmdUpdate.setFocusable(false);
        cmdUpdate.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdUpdate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdUpdateActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdUpdate);

        cmdDelete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/remove.png"))); // NOI18N
        cmdDelete.setMnemonic('R');
        cmdDelete.setText("    Remove     ");
        cmdDelete.setFocusable(false);
        cmdDelete.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdDelete);
        jToolBar1.add(jSeparator1);

        cmdPrint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/printer.png"))); // NOI18N
        cmdPrint.setMnemonic('P');
        cmdPrint.setText("    Print     ");
        cmdPrint.setFocusable(false);
        cmdPrint.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdPrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdPrint);

        cmdRePrint.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdRePrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/segment.png"))); // NOI18N
        cmdRePrint.setMnemonic('t');
        cmdRePrint.setText("   Re-print     ");
        cmdRePrint.setFocusable(false);
        cmdRePrint.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdRePrint.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdRePrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdRePrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRePrintActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdRePrint);

        cmdCancelPart.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdCancelPart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/trash.png"))); // NOI18N
        cmdCancelPart.setMnemonic('n');
        cmdCancelPart.setText("   Cancel     ");
        cmdCancelPart.setFocusable(false);
        cmdCancelPart.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdCancelPart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdCancelPart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdCancelPart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCancelPartActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdCancelPart);
        jToolBar1.add(jSeparator3);

        cmdExit4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lockd.png"))); // NOI18N
        cmdExit4.setMnemonic('l');
        cmdExit4.setText("  Close Schedule    ");
        cmdExit4.setActionCommand(" Close Schedule   ");
        cmdExit4.setFocusable(false);
        cmdExit4.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdExit4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExit4ActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdExit4);
        jToolBar1.add(jSeparator4);

        cmdExit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit.png"))); // NOI18N
        cmdExit.setMnemonic('x');
        cmdExit.setText("    Exit     ");
        cmdExit.setFocusable(false);
        cmdExit.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdExitActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdExit);
        jToolBar1.add(jSeparator5);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Batch No.");

        txtBatchNo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtBatchNo.setForeground(new java.awt.Color(153, 0, 204));
        txtBatchNo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtBatchNo.setText("000");

        txts.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsActionPerformed(evt);
            }
        });

        lblt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblt.setText("0 Record/s");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txts, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblt, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBatchNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBatchNo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txts, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblt))
                .addGap(95, 95, 95))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BatchID", "Date", "Venue", "Open"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
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
        jScrollPane2.setViewportView(tbl);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(180);
        tbl.getColumnModel().getColumn(0).setMaxWidth(55);
        tbl.getColumnModel().getColumn(3).setMaxWidth(55);

        jToolBar2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.add(jSeparator6);

        buttonGroup1.add(jToggleButton1);
        jToggleButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jToggleButton1.setForeground(new java.awt.Color(0, 102, 153));
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/new1.png"))); // NOI18N
        jToggleButton1.setMnemonic('p');
        jToggleButton1.setSelected(true);
        jToggleButton1.setText("      Open     ");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButton1);

        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jToggleButton2.setForeground(new java.awt.Color(153, 0, 0));
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout.png"))); // NOI18N
        jToggleButton2.setMnemonic('o');
        jToggleButton2.setText("      Closed     ");
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButton2);

        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jToggleButton3.setForeground(new java.awt.Color(153, 0, 153));
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/myinquiry.png"))); // NOI18N
        jToggleButton3.setMnemonic('o');
        jToggleButton3.setText("     Backlog    ");
        jToggleButton3.setFocusable(false);
        jToggleButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });
        jToolBar2.add(jToggleButton3);
        jToolBar2.add(jSeparator8);

        cmdAdd1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        cmdAdd1.setMnemonic('S');
        cmdAdd1.setText("      Search      ");
        cmdAdd1.setToolTipText("");
        cmdAdd1.setFocusable(false);
        cmdAdd1.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        cmdAdd1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cmdAdd1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cmdAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdAdd1ActionPerformed(evt);
            }
        });
        jToolBar2.add(cmdAdd1);

        f.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtdte.setDateFormatString("yyyy-MM-dd");
        txtdte.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                txtdteAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        txtdte.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                txtdteCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtdteInputMethodTextChanged(evt);
            }
        });
        txtdte.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtdtePropertyChange(evt);
            }
        });
        txtdte.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                txtdteVetoableChange(evt);
            }
        });

        textsearch1.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        textsearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textsearch1ActionPerformed(evt);
            }
        });

        buttonGroup2.add(o2);
        o2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        o2.setMnemonic('r');
        o2.setText("Address");

        buttonGroup2.add(o1);
        o1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        o1.setMnemonic('D');
        o1.setSelected(true);
        o1.setText("Date");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Search by:");

        buttonGroup2.add(o3);
        o3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        o3.setMnemonic('r');
        o3.setText("Year");

        yr.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        yr.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                yrHierarchyChanged(evt);
            }
        });
        yr.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                yrAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                yrAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        yr.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
                yrAncestorMoved1(evt);
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
            }
        });
        yr.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                yrMouseClicked(evt);
            }
        });
        yr.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                yrCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                yrInputMethodTextChanged(evt);
            }
        });
        yr.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                yrPropertyChange(evt);
            }
        });
        yr.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                yrVetoableChange(evt);
            }
        });

        jButton1.setText("Filter");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fLayout = new javax.swing.GroupLayout(f);
        f.setLayout(fLayout);
        fLayout.setHorizontalGroup(
            fLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(o2)
                    .addComponent(o1)
                    .addComponent(o3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(fLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fLayout.createSequentialGroup()
                        .addComponent(yr, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(textsearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdte, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
            .addGroup(fLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        fLayout.setVerticalGroup(
            fLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(fLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txtdte, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(o1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(o2)
                    .addComponent(textsearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(yr, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(o3))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(f, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(f, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(109, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1270, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 557, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        //this.setTitle(myTitle);


    }//GEN-LAST:event_formInternalFrameOpened

    private void cmdExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExitActionPerformed
        this.dispose();
    }//GEN-LAST:event_cmdExitActionPerformed

    private void tbl1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl1MouseMoved
    }//GEN-LAST:event_tbl1MouseMoved

    private void cmdAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAddActionPerformed
        if (BatchID == 0) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the schedule list!");
        } else {
            showFrmAdd();
        }


    }//GEN-LAST:event_cmdAddActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        //statflg = 1;
    }//GEN-LAST:event_formInternalFrameClosed

    private void cmdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteActionPerformed
        int col = 0; //set column value to 0
        int row = tbl1.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl1.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            int col2 = 7; //set column value to 0
            String prn = tbl1.getValueAt(row, col2).toString();
            if (prn != "Not Yet Printed") {
                JOptionPane.showMessageDialog(this, "Record cannot be removed! Certificate is already printed!");
                return;
            } else {
                String Pid = tbl1.getValueAt(row, col).toString();
                int i = myFunctions.msgboxYesNo("Are you sure you want delete this current record?");
                if (i == 0) {
                    myDataenvi.rsDeleteParticipant(Pid);
                    populateTBL();
                    JOptionPane.showMessageDialog(this, "Record has been successfully deleted!");
                } else if (i == 1) {
                    return; //do nothing
                } else if (i == 2) {
                    this.dispose(); //exit window
                    return;
                }
            }
        }

    }//GEN-LAST:event_cmdDeleteActionPerformed

    private void cmdUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdUpdateActionPerformed

        int col = 0; //set column value to 0
        int row = tbl1.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl1.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String id = tbl1.getValueAt(row, col).toString();
            UpdateParticipant.partID = id;
            UpdateParticipant.lname = tbl1.getValueAt(row, 1).toString();
            UpdateParticipant.fname = tbl1.getValueAt(row, 2).toString();
            UpdateParticipant.mname = tbl1.getValueAt(row, 3).toString();
            if (tbl1.getValueAt(row, 4) == null) {
                UpdateParticipant.ext = "";
            } else {
                UpdateParticipant.ext = tbl1.getValueAt(row, 4).toString();
            }
            UpdateParticipant.paddress = tbl1.getValueAt(row, 5).toString();
            showFrmUpdate();
        }
    }//GEN-LAST:event_cmdUpdateActionPerformed

    private void cmdPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintActionPerformed
        int i = myFunctions.msgboxYesNo("System will now generate the print preview. PROCEED PRINTING?" + (char) 10
                + "Note: If you select YES you will not be able print the certificates, unless you go to Re-print Certificates.");
        if (i == 0) {
            myReports.rptCertificate(BatchID);
            // rsUpdatePartRec(BatchID);
            populateTBL();
        } else if (i == 1) {
            return; //do nothing
        } else if (i == 2) {
            this.dispose(); //exit window
            return;
        }

    }//GEN-LAST:event_cmdPrintActionPerformed

    private void cmdRePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRePrintActionPerformed
        int col = 0; //set column value to 0
        int row = tbl1.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl1.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            int col2 = 7; //set column value to 0
            String prn = tbl1.getValueAt(row, col2).toString();
            if (prn == "Not Yet Printed") {
                JOptionPane.showMessageDialog(this, "Cannot be re-printed! This Record is not yet been printed, go to print Certificates");
                return;
            } else {
                int i = myFunctions.msgboxYesNo("System will now generate the print preview. PROCEED PRINTING?");
                if (i == 0) {
                    String id = tbl1.getValueAt(row, col).toString();
                    rsUpdatePrntCount(id);
                    myReports.rptCertReprint1b1(id);
                } else if (i == 1) {
                    return; //do nothing
                } else if (i == 2) {
                    this.dispose(); //exit window
                    return;
                }

            }
        }
    }//GEN-LAST:event_cmdRePrintActionPerformed

    private void cmdCancelPartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCancelPartActionPerformed
        int col = 0; //set column value to 0
        int row = tbl1.getSelectedRow(); //get value of selected value

        //trap user incase if no row selected
        if (tbl1.isRowSelected(row) != true) {
            JOptionPane.showMessageDialog(this, "No record selected! Please a record from the list!");
            return;
        } else {
            String prn = tbl1.getValueAt(row, col).toString();
            int i = myFunctions.msgboxYesNo("Are you sure you want to cancel this participant record?");
            if (i == 0) {
                rsUpdatePartStat(prn);
                populateTBL();
                JOptionPane.showMessageDialog(this, "Record sucessfully canceled!");
            } else if (i == 1) {
                return; //do nothing
            } else if (i == 2) {
                this.dispose(); //exit window
                return;
            }
        }
    }//GEN-LAST:event_cmdCancelPartActionPerformed

    private void cmdExit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdExit4ActionPerformed
        int i = myFunctions.msgboxYesNo("Are you sure you want to close this schedule?" + (char) 10
                + "Note: If you close this schedule it will never be updatable. Do you want proceed?");
        if (i == 0) {
            rsClosedSched(BatchID);
            this.dispose();
            JOptionPane.showMessageDialog(this, "Schedule is now closed!. Contact your administrator if their are any" + (char) 10
                    + "important changes in this schedule!");
        } else if (i == 1) {
            return; //do nothing
        } else if (i == 2) {
            this.dispose(); //exit window
            return;
        }
    }//GEN-LAST:event_cmdExit4ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        i = 0;
        populateTBLSched();
        f.setVisible(false);
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        i = 1;
        populateTBLSched();
        f.setVisible(true);
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void textsearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textsearch1ActionPerformed
        try {
            populateTBLSched();
        } catch (Exception e) {

        }
    }//GEN-LAST:event_textsearch1ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        i = 2;
        populateTBLSched();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void txtdteAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_txtdteAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdteAncestorAdded

    private void txtdteInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtdteInputMethodTextChanged

    }//GEN-LAST:event_txtdteInputMethodTextChanged

    private void txtdteCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtdteCaretPositionChanged

    }//GEN-LAST:event_txtdteCaretPositionChanged

    private void txtdteVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_txtdteVetoableChange

    }//GEN-LAST:event_txtdteVetoableChange

    private void txtdtePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtdtePropertyChange
        populateTBLSched();
    }//GEN-LAST:event_txtdtePropertyChange

    private void yrHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_yrHierarchyChanged

    }//GEN-LAST:event_yrHierarchyChanged

    private void yrAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_yrAncestorMoved

    }//GEN-LAST:event_yrAncestorMoved

    private void yrAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_yrAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_yrAncestorAdded

    private void yrAncestorMoved1(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_yrAncestorMoved1

    }//GEN-LAST:event_yrAncestorMoved1

    private void yrMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_yrMouseClicked

    }//GEN-LAST:event_yrMouseClicked

    private void yrCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_yrCaretPositionChanged

    }//GEN-LAST:event_yrCaretPositionChanged

    private void yrInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_yrInputMethodTextChanged

    }//GEN-LAST:event_yrInputMethodTextChanged

    private void yrPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_yrPropertyChange

    }//GEN-LAST:event_yrPropertyChange

    private void yrVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_yrVetoableChange

    }//GEN-LAST:event_yrVetoableChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        populateTBLSched();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsActionPerformed

    private void cmdAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdAdd1ActionPerformed
        showFrmSA();
    }//GEN-LAST:event_cmdAdd1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton cmdAdd;
    private javax.swing.JButton cmdAdd1;
    private javax.swing.JButton cmdCancelPart;
    private javax.swing.JButton cmdDelete;
    private javax.swing.JButton cmdExit;
    private javax.swing.JButton cmdExit4;
    private javax.swing.JButton cmdPrint;
    private javax.swing.JButton cmdRePrint;
    private javax.swing.JButton cmdUpdate;
    private javax.swing.JPanel f;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JLabel lblt;
    private javax.swing.JRadioButton o1;
    private javax.swing.JRadioButton o2;
    private javax.swing.JRadioButton o3;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tbl1;
    private org.jdesktop.swingx.JXSearchField textsearch1;
    private javax.swing.JLabel txtBatchNo;
    private com.toedter.calendar.JDateChooser txtdte;
    private org.jdesktop.swingx.JXSearchField txts;
    private com.toedter.calendar.JYearChooser yr;
    // End of variables declaration//GEN-END:variables
}
