/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.meter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;
import static memsys.global.myDataenvi.rsAddConnLog;
import memsys.ui.main.ParentWindow;

/**
 *
 * @author cadizlester
 */
public class MeterProcess {

    static Statement stmt;
    static String nowDate2 = FunctionFactory.getSystemNowDateTimeString();

    public void ChangeMeter(int acctno, String installdate, String os, String mes, int crewid, String sealdate, String oldmetersn, String newmetersn, String issuedto,
            String tm, String PCFrom, String PCTo, double prevr, double presr, double kwh, double prevd, double presd, String Remarks, String tdate, String rdate, double pr, double pd, double kwhdemand, int mpID, int m) {

        int rdngid = InsertGetValidReadingID();

        PostReadingInclusion(acctno, oldmetersn, tm, PCFrom, PCTo, prevr, presr, kwh,
                prevd, presd, kwhdemand, Remarks, nowDate2, rdngid);

        UpdateMeterSeal(acctno, os, mes, crewid, sealdate);
        UpdateConsumerMeter(acctno, newmetersn, installdate, nowDate2, ParentWindow.getUserID(), crewid, Remarks);
        InsertConsumerMeterLog(acctno, "Installed by: ", installdate, crewid, newmetersn, m);

        if (IsMeterExist(newmetersn) == false) {
            InsertMeterToMeter(newmetersn);
        }

        UpdateMeter(newmetersn, pr, pd, rdate);
        UpdateMeterPost(crewid, mpID, acctno);
        UpdatePreviousMeterPost(oldmetersn);

        if (IsAcctExist(acctno) == false) {
            AddToConnTBL(acctno);
            rsAddConnLog(acctno, "Link Consumer Record to Membership System", 11, ParentWindow.getUserID(), nowDate2, "");
        }

        rsAddConnLog(acctno, "Meter replaced from " + oldmetersn + " to " + newmetersn + " due to " + Remarks, 11, ParentWindow.getUserID(), nowDate2, "");

        InsertMeterStatusLog(acctno, "New meter issued to " + issuedto, 3, newmetersn, String.valueOf(pr), String.valueOf(pd), installdate);
        InsertMeterStatusLog(acctno, "Meter removed due to " + Remarks, 6, oldmetersn, String.valueOf(pr), String.valueOf(pd), installdate);
        InsertMeterStatusLog(acctno, "Meter replaced from " + oldmetersn + " to " + newmetersn + " due to " + Remarks, 8, newmetersn, String.valueOf(pr), String.valueOf(pd), installdate);

        // InsertMeterStatusLog(acctno, "New meter connected", 4, newmetersn, String.valueOf(pr), String.valueOf(pd), installdate);
        //  myDataenvi.rsAddParticipants(639, GetAcctName(acctno),"","","","",nowDate2,0);
    }

    private void UpdatePreviousMeterPost(String OldMeterSN) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE meterPostTBL "
                + "SET Flag=1, AcctNo='' "
                + "WHERE MeterSN='" + OldMeterSN + "'";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    String GetTotalMultiplier(String mtrsn) {
        String an = "";
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT AcctName FROM Consumer WHERE AcctNo=" + mtrsn;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                an = rs.getString(1);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return an;
    }

    public void RemoveMeter(int acctno, String installdate, String Remarks, String oldmetersn, double pr, double pd, int crewid, int mpID, String newmetersn) {
        UpdateConsumerMeter(acctno, newmetersn, installdate, nowDate2, ParentWindow.getUserID(), crewid, Remarks);
        InsertConsumerMeterLog(acctno, "REMOVED", installdate, crewid, newmetersn, 0);
        InsertMeterStatusLog(acctno, "Meter removed due to " + Remarks, 6, oldmetersn, String.valueOf(pd), String.valueOf(pd), installdate);
        UpdatePreviousMeterPost(oldmetersn);

        if (IsAcctExist(acctno) == false) {
            AddToConnTBL(acctno);
        }
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
                + "SELECT AcctNo, TownCode, RouteCode, AcctCode, RouteSeqNo, ClassCode, AcctName, AcctAddress, MembershipID, " + ParentWindow.getUserID() + ",'" + nowDate2 + "', BrgyCode, 8 "
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

    private void UpdateMeterPost(int crewid, int mpID, int acctno) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE meterPostTBL "
                + "SET Flag=2, AcctNo='" + acctno + "', CurIssuedTo='" + crewid + "', CurIssueDate='" + FunctionFactory.getSystemNowDateTimeString() + "' "
                + "WHERE mpID=" + mpID + "";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }


    String GetAcctName(int acctno) {
        String an = "";
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT AcctName FROM Consumer WHERE AcctNo=" + acctno;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                an = rs.getString(1);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return an;
    }

    int InsertGetValidReadingID() {
        int GetReadingID = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO Reading (UserID)"
                + "VALUES ("+ParentWindow.getUserID()+"); "
                + "SELECT SCOPE_IDENTITY() AS ReadingID;";

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                GetReadingID = rs.getInt(1);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        return GetReadingID;

    }

    private void PostReadingInclusion(int acctno, String metersn, String tm, String PCFrom, String PCTo, double prevr, double presr, double kwh, double prevd, double presd, double kwhdemand, String Remarks, String tdate, int rdngid) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO ReadingInclusion (AcctNo, MeterSN, TotalMultiplier, PCFrom, PCTo, PrevRdng, PresRdng, kWhUsed, PrevDemand, PresDemand, kWDemand, Remarks, UserID, TransDate, ReadingID) "
                + " VALUES(" + acctno + ", '" + metersn + "','" + tm + "','" + PCFrom + "','" + PCTo + "','" + prevr + "','" + presr + "','" + kwh + "','" + prevd + "','" + presd + "','" + kwhdemand + "','" + Remarks + "'," + ParentWindow.getUserID() + ",'" + tdate + "', " + rdngid + ")";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
            System.out.println(e);
        }
    }

    private void UpdateMeterSeal(int acctno, String outerseal, String mes, int crewid, String sealdate) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE MeterSeal SET OuterSeal='" + outerseal + "', MeterEnclosureSeal='" + mes + "', CrewID=" + crewid + ", SealDate='" + sealdate + "', UserID=" + ParentWindow.getUserID() + ", TransDate='" + nowDate2 + "' WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void UpdateConsumerMeter(int acctno, String metersn, String installdate, String transdate, int userid, int crewid, String remarks) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE ConsumerMeter SET MeterSN='" + metersn + "',"
                + "InstallDate='" + installdate + "', TransDate='" + transdate + "', UserID=" + userid + ", CrewID=" + crewid + ", Remarks='" + remarks + "' WHERE AcctNo=" + acctno;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    boolean IsMeterExist(String mtrsn) {
        boolean found = false;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT * FROM Meter WHERE MeterSN='" + mtrsn + "'";

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

    private void InsertMeterToMeter(String mtrsn) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO Meter(MeterSN, CoopMeterSN, BrandCode, Phase, TypeCode, Wire, Voltage, AmpereCode, OwnerCode, DemandType, "
                + "EnergyDigits ,DemandDigits, MSCode, Multiplier, PresRdng, PresDemand, RdngDate) "
                + "SELECT MeterSN, CoopSN, BrandID, Phase, TypeCode, Wire, Voltage, AmpereCode, OwnerCode, DemandType, "
                + "EnergyDigits ,DemandDigits, 4 ,Multiplier, InitReading, DemandReading, '" + nowDate2 + "' "
                + "FROM meterPostTBL WHERE MeterSN='" + mtrsn + "'";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void InsertReading() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO Reading(UserID) VALUES(" + ParentWindow.getUserID() + ")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void UpdateMeter(String metersn, double presr, double presd, String rdate) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE Meter SET PresRdng='" + presr + "',"
                + "PresDemand='" + presd + "', RdngDate='" + rdate + "' WHERE MeterSN='" + metersn + "'";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void InsertConsumerMeterLog(int acctno, String loginfo, String installdate, int crewid, String metersn, int m) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO ConsumerMeterLog(AcctNo, MeterSN, TotalMultiplier, CrewID, InstallDate, Remarks, UserID, TransDate) "
                + " VALUES(" + acctno + ", '" + metersn + "'," + m + ", " + crewid + ", '" + installdate + "','" + loginfo + "'," + ParentWindow.getUserID() + ",'" + nowDate2 + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.getMessage();
        }
    }

    //METER STATUS LOG
    private void InsertMeterStatusLog(int acctno, String loginfo, int mscode, String metersn, String initrdng, String drdng, String datereplaced) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO MeterStatusLog(MeterSN, AcctNo, ChangeDate, MSCode, PresRdng, PresDemand, Remarks, UserID, TransDate) "
                + "SELECT '" + metersn + "', AcctNo,'" + datereplaced + "', " + mscode + ",'" + initrdng + "','" + drdng + "','" + loginfo + "' ," + ParentWindow.getUserID() + ",'" + nowDate2 + "' "
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

    private void UpdateMeterPostTbl(int acctno, String loginfo, int mscode) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO MeterStatusLog(MeterSN, AcctNo, ChangeDate, MSCode, PresRdng, PresDemand, Remarks, UserID, TransDate) "
                + "SELECT MeterSN, AcctNo,'" + nowDate2 + "', " + mscode + ", InitReading, DemandReading, '" + loginfo + "' ," + ParentWindow.getUserID() + ",'" + nowDate2 + "' "
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

    public static void main(String[] args) {//
        MeterProcess mp = new MeterProcess();
        //mp.PostReadingInclusion(341266, "1234", "1", "2015-01-01", "2015-01-01", 0, 0, 0, 2, 0, 2, "", "2015-01-01", mp.GetValidReadingID());
        // mp.InsertMeterToMeter("2015-08134270");
        // mp.UpdateMeter("2015-08134270",40, 0,"2017-03-10");
//        int mrid = mp.GetMaxRdngID();
        int rid = mp.InsertGetValidReadingID();
       // System.out.println(mrid);
        System.out.println(rid);

    }

}
