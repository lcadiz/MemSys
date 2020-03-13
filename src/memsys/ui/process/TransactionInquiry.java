/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.process;

import memsys.global.DBConn.MainDBConn;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import memsys.global.FTPFactory;
import memsys.global.myFunctions;
import memsys.global.myReports;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author LESTER
 */
public class TransactionInquiry extends javax.swing.JInternalFrame {

    static Statement stmt;
    static DefaultTableCellRenderer cellAlignCenterRenderer = new DefaultTableCellRenderer();
    static DefaultTableCellRenderer cellAlignCenterRight = new DefaultTableCellRenderer();
    static DefaultTableModel model, model2, model3, model4;
    static DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    static String nowDate = myFunctions.getDate();
    static int acctno, typecode;
    static String rem, cn, nan, metersn, coopsn, initreading, type, acctname, acctaddress, seqno;
    static Date issue, post, expiry;
    public static CreateLog frmCreateLog;
    public static EditElectrician frmChangeElectrician;
    public static EditTownCode frmEditTownCode;

    public TransactionInquiry() {
        initComponents();
        model2 = (DefaultTableModel) tbl1.getModel();

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

        tblai.setCellSelectionEnabled(false);
        tblai.setRowSelectionAllowed(true);
        tblai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblai.setSelectionBackground(new Color(153, 204, 255));
        tblai.setSelectionForeground(Color.BLACK);
    }

    public void showFrmPostRemarks() {
        frmCreateLog = new CreateLog(this, true);
        frmCreateLog.setVisible(true);
    }

    public void showFrmEditElectrician() {
        frmChangeElectrician = new EditElectrician(this, true);
        frmChangeElectrician.setVisible(true);
    }

    public void showFrmEditTownCode() {
        frmEditTownCode = new EditTownCode(this, true);
        frmEditTownCode.setVisible(true);
    }

    int GetStatus(int acctno) {
        int stat = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT Status FROM ConnTBL WHERE c.acctno=" + acctno;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                stat = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return stat;

    }

    public void populateTBL() {

        Connection conn = MainDBConn.getConnection();
        String createString = "";

        createString = "SELECT AcctNo, c.membershipid, c.AcctName, AcctAddress, s.statdesc , c.classcode "
                + " FROM connTBL c"
                + " LEFT JOIN connTypeTBL t ON  c.connType=t.connType "
                + " LEFT JOIN connStatTBL s ON c.status=s.status"
                + " LEFT JOIN membersTBL m ON  c.MembershipID = m.memberID"
                + " WHERE  (c.acctname LIKE '%" + txtsearch.getText() + "%' OR AcctNo like '" + txtsearch.getText() + "%') "
                + " ORDER BY  c.AcctName";

        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model = (DefaultTableModel) tbl.getModel();

            cellAlignCenterRenderer.setHorizontalAlignment(0);

            tbl.setRowHeight(25);
            tbl.getColumnModel().getColumn(0).setCellRenderer(cellAlignCenterRenderer);
            tbl.getColumnModel().getColumn(1).setCellRenderer(cellAlignCenterRenderer);
            tbl.getColumn("View").setCellRenderer(new ButtonRenderer(1));
            tbl.getColumn("View").setCellEditor(new ButtonEditor(new JCheckBox(), 1));

            tbl.setColumnSelectionAllowed(false);

            model.setNumRows(0);

            while (rs.next()) {

                model.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), ""});
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void populateTBLPD(int ano) {

        Connection conn = MainDBConn.getConnection();
        String createString = "";

        createString = "SELECT COADesc, AmtPaid, ORNo "
                + "FROM collectionmisc c LEFT JOIN COA a ON C.COAID=a.COAID WHERE c.AcctNo=" + ano;

        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model4 = (DefaultTableModel) tblpd.getModel();

            cellAlignCenterRenderer.setHorizontalAlignment(0);
            cellAlignCenterRight.setHorizontalAlignment(SwingConstants.RIGHT);

            tblpd.setRowHeight(25);
            tblpd.getColumnModel().getColumn(1).setCellRenderer(cellAlignCenterRight);
            tblpd.getColumnModel().getColumn(2).setCellRenderer(cellAlignCenterRenderer);

            tblpd.setColumnSelectionAllowed(false);

            model4.setNumRows(0);

            while (rs.next()) {

                model4.addRow(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3)});
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
//

    String GetElectrician(int acctno) {
        String nym = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select Name from electricianTBL e INNER JOIN connCOInfoTBL c ON e.eID = c.eID WHERE c.acctno=" + acctno;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                nym = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return nym;

    }

    Date GetDateInstalled(int acctno) {
        Date val = null;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select InstallDate from ConsumerMeter WHERE acctno=" + acctno;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                val = rs.getDate(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return val;
    }

    String GetMeterSpecification(int acctno) {
        String val = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select TypeDesc from MeterPostTBL m LEFT JOIN MeterType t ON m.typecode=t.typecode WHERE acctno=" + acctno;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                val = rs.getString(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return val;
    }

    void GetInfo(int acctno) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT "
                + "           c.TownCode, c.RouteCode, c.RouteSeqNo, c.AcctName, c.AcctAddress, "
                + "           ct.typeDesc, cn.expiryDate, cn.connType,"
                + "           ci.Remarks, ci.ContactNo, ci.NAcctNo,"
                + "           mp.MeterSN, mp.CurIssueDate, mp.PostDate, mp.CoopSN, mp.InitReading "
                + "     FROM "
                + "           Consumer c "
                + "     LEFT JOIN conntbl cn ON c.AcctNo=cn.AcctNo "
                + "     LEFT JOIN connTypeTBL ct ON cn.ConnType=ct.ConnType "
                + "     LEFT JOIN connCOInfoTBL ci ON c.AcctNo=ci.AcctNo "
                + "     LEFT JOIN MeterPostTBL mp ON c.AcctNo=mp.AcctNo"
                + "     "
                + "     WHERE c.AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                seqno = rs.getString(1) + "-" + rs.getString(2) + "-" + rs.getString(3);
                acctname = rs.getString(4);
                acctaddress = rs.getString(5);
                type = rs.getString(6);
                expiry = rs.getDate(7);
                typecode = rs.getInt(8);
                rem = rs.getString(9);
                cn = rs.getString(10);
                nan = rs.getString(11);
                metersn = rs.getString(12);
                issue = rs.getDate(13);
                post = rs.getDate(14);
                coopsn = rs.getString(15);
                initreading = rs.getString(16);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    public void populateTBLAI() {

        model3 = (DefaultTableModel) tblai.getModel();

        renderer.setHorizontalAlignment(0);

        tblai.setRowHeight(25);
        //  tblai.getColumnModel().getColumn(0).setCellRenderer(renderer);
        //  tbl1.getColumnModel().getColumn(1).setCellRenderer(renderer);

        tblai.setColumnSelectionAllowed(false);

        model3.setNumRows(0);
        GetInfo(acctno);
        model3.addRow(new Object[]{"Electrician", GetElectrician(acctno)});
        model3.addRow(new Object[]{"Meter SN", metersn});
        model3.addRow(new Object[]{"Coop SN", coopsn});
        model3.addRow(new Object[]{"Meter Specification", GetMeterSpecification(acctno)});
        model3.addRow(new Object[]{"Initial Reading", initreading});
        model3.addRow(new Object[]{"DateIssued", issue});
        model3.addRow(new Object[]{"DatePosted", post});
        model3.addRow(new Object[]{"DateInstalled", GetDateInstalled(acctno)});
        model3.addRow(new Object[]{"Remarks", rem});
        model3.addRow(new Object[]{"Contact No", cn});
        model3.addRow(new Object[]{"Near AcctNo/SeqNo", nan});
        model3.addRow(new Object[]{"Assigned AcctNo/SeqNo", seqno});
        String info = "";
        if (typecode == 1) {
            info = " (Expiry Date:" + expiry + ")";
        } else if (typecode == 2) {
            info = "";
        }
        model3.addRow(new Object[]{"Connection Type", type});

//           String x=GetElectrician(acctno); 
//           System.out.println(x);
        // GetConnInfo(acctno);
        // myReports.rptConnectOrderInquiry(String.valueOf(acctno), ParentWindow.getUserName(), jpco);
        //myReports.rptCostingInquiry(acctno, acctname, acctaddress, "", ParentWindow.getUserName(), "PAYMENT COSTING", jpcosting);
    }

    public void populateTBL1() {

        Connection conn = MainDBConn.getConnection();
        String createString = "select c.TransDate, Remarks, FullName, Note from connlogTBL c inner join Users u on c.UserID=u.UserID where AcctNo=" + acctno;

        try {

            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            model2 = (DefaultTableModel) tbl1.getModel();

            renderer.setHorizontalAlignment(0);

            tbl1.setRowHeight(25);
            tbl1.getColumnModel().getColumn(0).setCellRenderer(renderer);
            //  tbl1.getColumnModel().getColumn(1).setCellRenderer(renderer);

            tbl1.setColumnSelectionAllowed(false);

            model2.setNumRows(0);

            while (rs.next()) {

                model2.addRow(new Object[]{rs.getDate(1), rs.getString(2), rs.getString(4), rs.getString(3)});
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public int GetPartID(int mid) {
        int UGID = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "select partID FROM membersTBL WHERE memberID=" + mid;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                UGID = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return UGID;
    }

    class ButtonEditor extends DefaultCellEditor {

        protected JButton button;
        private String label;
        private boolean isPushed;
        int flg;

        public ButtonEditor(JCheckBox checkBox, int x) {
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

                    int col = 0; //set column value to 0
                    int row = tbl.getSelectedRow(); //get value of selected value
                    String id = tbl.getValueAt(row, 0).toString();
                    String pid = tbl.getValueAt(row, 1).toString();
//                        String Str = memsys.others.paths.getPicPathConfig();
//                        String path = Str;
                    // File file = new File("192.168.1.192/img/29754.jpg");
                    // File file = new File(path + Integer.parseInt(pid) + ".jpg");

                    try {
                        FTPFactory i = new FTPFactory();
                        i.FTPViewImage(i.GetFTPPicPath() + GetPartID(Integer.parseInt(pid)) + ".jpg", captured);
                    } catch (Exception e) {
                    }

                    try {
                        model4.setNumRows(0);
                    } catch (Exception e) {
                    }

                    populateTBLPD(Integer.parseInt(id));

                    try {
                        model2.setNumRows(0);
                    } catch (Exception e) {

                    }

                    String cid = tbl.getValueAt(row, 0).toString();
                    acctno = Integer.parseInt(cid);
                    //  System.out.println(acctno);
                    populateTBL1();
                    populateTBLAI();
                } else if (flg == 2) {
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

            Icon ico1 = new javax.swing.ImageIcon(getClass().getResource("/img/positionmini.png"));
            Icon ico2 = new javax.swing.ImageIcon(getClass().getResource("/img/ledgermini.png"));

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

        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        captured = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl = new javax.swing.JTable();
        txtsearch = new org.jdesktop.swingx.JXSearchField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblai = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblpd = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblpd1 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Transaction Inquiry");

        captured.setForeground(new java.awt.Color(255, 102, 0));
        captured.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/nophoto.jpg"))); // NOI18N
        captured.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        captured.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                capturedMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                capturedMouseMoved(evt);
            }
        });
        captured.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                capturedMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                capturedMouseReleased(evt);
            }
        });

        tbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account No", "MemberID", "Account Name", "Address", "Status", "Class", "View"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
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
        });
        jScrollPane1.setViewportView(tbl);
        //set column width
        tbl.getColumnModel().getColumn(0).setMaxWidth(80);
        tbl.getColumnModel().getColumn(1).setMaxWidth(80);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(180);
        tbl.getColumnModel().getColumn(5).setMaxWidth(120);
        tbl.getColumnModel().getColumn(6).setMaxWidth(50);

        txtsearch.setLayoutStyle(org.jdesktop.swingx.JXSearchField.LayoutStyle.MAC);
        txtsearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtsearchMouseClicked(evt);
            }
        });
        txtsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtsearchActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/reports.png"))); // NOI18N
        jButton3.setText("Post Status");
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setIconTextGap(8);
        jButton3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        tbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "DateTime", "Status Log Info", "Remarks", "User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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

        tbl1.getColumnModel().getColumn(0).setPreferredWidth(70);
        tbl1.getColumnModel().getColumn(1).setPreferredWidth(250);
        tbl1.getColumnModel().getColumn(3).setPreferredWidth(250);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1132, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        jTabbedPane1.addTab("History Logs", jPanel5);

        tblai.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account Information", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblai.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tblai);
        tblai.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblai.getColumnModel().getColumn(1).setPreferredWidth(400);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calculator.png"))); // NOI18N
        jButton1.setMnemonic('P');
        jButton1.setText("Payment Costing Preview");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/loan.png"))); // NOI18N
        jButton2.setMnemonic('C');
        jButton2.setText("Connect Order Preview");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/save.png"))); // NOI18N
        jButton4.setMnemonic('C');
        jButton4.setText("Update Electrician");
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/edit.png"))); // NOI18N
        jButton5.setMnemonic('C');
        jButton5.setText("Edit Town Code");
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(314, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Account Info", jPanel4);

        tblpd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Payment Details", "Amount", "ORNo."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblpd.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tblpd);
        if (tblpd.getColumnModel().getColumnCount() > 0) {
            tblpd.getColumnModel().getColumn(2).setHeaderValue("ORNo.");
        }
        tblpd.getColumnModel().getColumn(0).setPreferredWidth(400);

        tblpd1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Transaction Info", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblpd1.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(tblpd1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Payment Details", jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(captured, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(captured, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 723, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseMoved

    }//GEN-LAST:event_tblMouseMoved

    private void tbl1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl1MouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tbl1MouseMoved

    private void txtsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtsearchActionPerformed
        populateTBL();
    }//GEN-LAST:event_txtsearchActionPerformed

    private void capturedMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseDragged

    }//GEN-LAST:event_capturedMouseDragged

    private void capturedMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseMoved

    }//GEN-LAST:event_capturedMouseMoved

    private void capturedMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMousePressed

    }//GEN-LAST:event_capturedMousePressed

    private void capturedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_capturedMouseReleased

    }//GEN-LAST:event_capturedMouseReleased

    private void tblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMouseClicked

    }//GEN-LAST:event_tblMouseClicked

    private void txtsearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtsearchMouseClicked
        try {
            model2.setNumRows(0);
            model3.setNumRows(0);
        } catch (Exception e) {

        }
    }//GEN-LAST:event_txtsearchMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        myReports.rptCostingInquiry(acctno, acctname, acctaddress, "", ParentWindow.getUserName(), "PAYMENT COSTING");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        myReports.rptConnectOrderInquiry(String.valueOf(acctno), ParentWindow.getUserName());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        CreateLog.acctno = acctno;
        showFrmPostRemarks();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        EditElectrician.acctno = acctno;
        showFrmEditElectrician();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        EditTownCode.acctno = acctno;
        showFrmEditTownCode();
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel captured;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tbl;
    private javax.swing.JTable tbl1;
    private javax.swing.JTable tblai;
    private javax.swing.JTable tblpd;
    private javax.swing.JTable tblpd1;
    private org.jdesktop.swingx.JXSearchField txtsearch;
    // End of variables declaration//GEN-END:variables
}
