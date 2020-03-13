package memsys.ui.process;

import memsys.global.DBConn.MainDBConn;
import memsys.global.myDataenvi;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import memsys.global.FunctionFactory;
import memsys.ui.main.ParentWindow;

public class ActivateMeterOps extends javax.swing.JDialog {

    public static ActivateMeter frmParent;
    public static String an;
    static Statement stmt;
    static String nowDate = FunctionFactory.getSystemNowDateTimeString();
    static String nowDate2 = FunctionFactory.getSystemNowDateTimeString();
    public static ParentWindow frmmdi;

    public ActivateMeterOps(ActivateMeter parent, boolean modal) {
        this.frmParent = parent;
        this.setModal(modal);
        initComponents();
        setLocationRelativeTo(this);
        setdates();

    }

    void setdates() {
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CONSUMER METER LOG

    public void ConsumerMeterLog(String acctno, String installdate) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO ConsumerMeterLog(AcctNo, MeterSN, TotalMultiplier, CrewID, InstallDate, Remarks, UserID, TransDate) "
                + "SELECT AcctNo, MeterSN, Multiplier, CurIssuedTo,'" + installdate + "', 'NEW CONNECTION' ," + ParentWindow.getUserID() + ",'" + nowDate2 + "' "
                + "FROM meterPostTBL WHERE AcctNo='" + acctno + "'";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //METER STATUS LOG
    public void MeterStatusLog(String acctno) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO MeterStatusLog(MeterSN, AcctNo, ChangeDate, MSCode, PresRdng, PresDemand, Remarks, UserID, TransDate) "
                + "SELECT MeterSN, AcctNo,'" + nowDate2 + "', 4, InitReading, DemandReading, 'NEW CONNECTION' ," + ParentWindow.getUserID() + ",'" + nowDate2 + "' "
                + "FROM meterPostTBL WHERE AcctNo='" + acctno + "'";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //METER LOG
    public static void MeterLog(String acctno, String rdngdate) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO MeterLog(MeterSN, CoopMeterSN, Brandcode, Phase, TypeCode, Wire, Voltage, AmpereCode, OwnerCode, DemandType,"
                + "EnergyDigits ,DemandDigits, MSCode, Multiplier,PresRdng, PresDemand, RdngDate , Remarks, UserID, TransDate) "
                + "SELECT MeterSN, CoopSN, BrandID, Phase, TypeCode, Wire, Voltage, AmpereCode, OwnerCode, DemandType, "
                + "EnergyDigits ,DemandDigits, 4 ,Multiplier, InitReading, DemandReading, '" + rdngdate + "' , 'Meter Issued to ' + c.CrewName ," + ParentWindow.getUserID() + ",'" + nowDate2 + "' "
                + "FROM meterPostTBL mp INNER JOIN Crew c ON mp.CurIssuedTo=c.crewID WHERE AcctNo='" + acctno + "'";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //METER
    public static void Meter(String acctno, String rdngdate) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO Meter(MeterSN, CoopMeterSN, BrandCode, Phase, TypeCode, Wire, Voltage, AmpereCode, OwnerCode, DemandType, "
                + "EnergyDigits ,DemandDigits, MSCode, Multiplier, PresRdng, PresDemand, RdngDate) "
                + "SELECT MeterSN, CoopSN, BrandID, Phase, TypeCode, Wire, Voltage, AmpereCode, OwnerCode, DemandType, "
                + "EnergyDigits ,DemandDigits, 4 ,Multiplier, InitReading, DemandReading, '" + rdngdate + "' "
                + "FROM meterPostTBL WHERE AcctNo='" + acctno + "'";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void ConsumerMeter(String acctno, String intalldate) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO ConsumerMeter(AcctNo, MeterSN, TotalMultiplier, CrewID, InstallDate, UserID, TransDate) "
                + "SELECT AcctNo, MeterSN, Multiplier, CurIssuedTo,'" + intalldate + "'," + ParentWindow.getUserID() + ",'" + nowDate2 + "' "
                + "FROM meterPostTBL WHERE AcctNo='" + acctno + "'";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    public void Consumer(String acctno) {
//        // ParentWindow.getUserID
//        Connection conn = DBConn.getConnection();
//        String createString;
//        createString = "INSERT INTO Consumer(AcctNo, "
//                + "TownCode, "
//                + "RouteCode, "
//                + "AcctCode, "
//                + "RouteSeqNo, "
//                + "ClassCode, "
//                + "AcctName, "
//                + "AcctAddress, "
//                + "MembershipID, "
//                + "UserID, "
//                + "TransDate) "
//                + "SELECT AcctNo, TownCode, RouteCode, AcctCode, RouteSeqNo, ClassCode, AcctName, AcctAddress, MembershipID, " + ParentWindow.getUserID + ",'" + nowDate2 + "' "
//                + "FROM connTBL WHERE AcctNo='" + acctno + "'";
//        try {
//            stmt = conn.createStatement();
//            stmt.executeUpdate(createString);
//            stmt.close();
//            conn.close();
//
//        } catch (SQLException e) {
//            //JOptionPane.showMessageDialog(null, e.getMessage());
//        }
//    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        jLabel1 = new javax.swing.JLabel();
        Ok = new javax.swing.JButton();
        Ok1 = new javax.swing.JButton();
        txtdate = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Activate Meter");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setText("Date Installed:");

        Ok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/activatemeter.png"))); // NOI18N
        Ok.setText("Activate");
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

        txtdate.setDateFormatString("yyyy/MM/dd ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Ok)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Ok1))
                    .addComponent(txtdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ok)
                    .addComponent(Ok1))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Ok1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Ok1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_Ok1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        java.util.Date testDate = null;
        try {
            testDate = sdf.parse(nowDate);
        } catch (ParseException e) {
        }
        //txtdate.setDateFormatString(nowDate);
        //txtdate.setDate(testDate);
    }//GEN-LAST:event_formWindowOpened

    private void OkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OkActionPerformed

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String seldate = dateFormat.format(txtdate.getDate());
       // JOptionPane.showMessageDialog(null,seldate);

        //"IBATO ANG MGA DATA" PROCESS
        //Consumer(an);                       //BATO DATA TO Consumer TABLE FROM connTBL(TEMPORARY TABLE)
        ConsumerMeter(an, seldate);           //BATO DATA TO ConsumerMeter TABLE FROM meterPostTBL
        Meter(an, seldate);                   //BATO DATA TO Meter TABLE FROM meterPostTBL
        MeterLog(an, seldate);                //BATO DATA TO MeterLog TABLE FROM meterPostTBL
        MeterStatusLog(an);                   //BATO DATA TO MeterStatusLog TABLE FROM meterPostTBL
        ConsumerMeterLog(an, seldate);        //BATO DATA TO ConsumerMeterLog TABLE FROM meterPostTBL

        rsAddConnLog(Integer.parseInt(an), "Activated", 2, ParentWindow.getUserID(), nowDate, "");
        JOptionPane.showMessageDialog(null, "Meter successfully activated");
        myDataenvi.rsUpdateConnStat(Integer.parseInt(an), 8);
        frmParent.populateTBL();
        this.dispose();
    }//GEN-LAST:event_OkActionPerformed

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

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ActivateMeterOps dialog = new ActivateMeterOps(frmParent, true);
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
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private javax.swing.JLabel jLabel1;
    private com.toedter.calendar.JDateChooser txtdate;
    // End of variables declaration//GEN-END:variables
}
