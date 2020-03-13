/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.global;


import memsys.global.DBConn.MainDBConn;
import java.sql.*;
import javax.swing.JOptionPane;
import memsys.ui.main.ParentWindow;

public class DBCommandFactory {
static String nowDate2 = myFunctions.getDate2();
    static Statement stmtAddNewShed,
            stmtFindDuplicateSched,
            stmtUpdateSched,
            stmtDeleteSched,
            stmtAddPArt,
            stmtUpdateParticipant,
            stmtDeleteParticipant, stmt;

    public static boolean checkCosting(int acctno) {
        boolean fexist = false;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT AcctNo FROM costingTempTBL WHERE AcctNo=" + acctno;
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

    //Add new schedule
    public static void rsAddSched(String venue, String sdate, int area, String address) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO dbo.scheduleTBL(sched_venue,sched_date,areacode,sched_address,sched_stat) "
                + "VALUES ('" + venue + "','" + sdate + "','" + area + "','" + address + "','0')";

        try {
            stmtAddNewShed = conn.createStatement();
            stmtAddNewShed.executeUpdate(createString);
            stmtAddNewShed.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //Find Duplicate Schedule
    public static int findDupSched(String sdate, int area) {
        Connection conn = MainDBConn.getConnection();
        CallableStatement cstmt;

        int counter = 0;
        try {
            cstmt = conn.prepareCall("{call sp_Checkdupsked ?,?,?}");  //call stored procedure   
            cstmt.setString(1, sdate);   //date param
            cstmt.setInt(2, area);       //arecode param          
            cstmt.registerOutParameter(3, java.sql.Types.INTEGER); //resgister as output                                
            cstmt.execute();  //execute sproc 
            counter = cstmt.getInt(3); //Return count(*) 
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        int result = counter;
        return result;
    }

    //update current selected schedule record in JFrameIncomingSched
    public static void rsUpdateSched(String venue, String address, String bID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.scheduleTBL "
                + "SET sched_venue='" + venue + "', sched_address='" + address + "' WHERE batchID='" + bID + "'";

        try {
            stmtUpdateSched = conn.createStatement();
            stmtUpdateSched.executeUpdate(createString);
            stmtUpdateSched.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //update current selected schedule record in JFrameIncomingSched
    public static void rsDeleteSched(String bID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM dbo.scheduleTBL WHERE batchID='" + bID + "'";

        try {
            stmtDeleteSched = conn.createStatement();
            stmtDeleteSched.executeUpdate(createString);
            stmtDeleteSched.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //Add Participant in a particular scheduled seminar
    public static void rsAddParticipants(int bID, String lname, String fname, String mname, String ext, String address, String date_encoded) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO dbo.participantsTBL(batchID,part_lname,part_fname,part_mname,part_ext,address,date_encoded) "
                + "VALUES ('" + bID + "','" + lname + "','" + fname + "','" + mname + "','" + ext + "','" + address + "','" + date_encoded + "')";

        try {
            stmtAddPArt = conn.createStatement();
            stmtAddPArt.executeUpdate(createString);
            stmtAddPArt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //delete participant/s
    public static void rsDeleteParticipant(String bID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM dbo.participantsTBL WHERE partID='" + bID + "'";

        try {
            stmtDeleteParticipant = conn.createStatement();
            stmtDeleteParticipant.executeUpdate(createString);
            stmtDeleteParticipant.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    //update participants record
    public static void rsUpdateParticipant(String lname, String fname, String mname, String ext, String address, String pID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.participantsTBL "
                + "SET part_lname='" + lname + "',"
                + "part_fname='" + fname + "',"
                + "part_mname='" + mname + "',"
                + "part_ext='" + ext + "',"
                + "address='" + address + "'"
                + "WHERE partID='" + pID + "'";

        try {
            stmtUpdateParticipant = conn.createStatement();
            stmtUpdateParticipant.executeUpdate(createString);
            stmtUpdateParticipant.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsAddProfile(String acctname, String bdate, String bplace, int sex, int civil_status, String address, String highest_edu, String occupation, String employer, String position, String monthly_income, String spouse_name, String spouse_age, String spouse_occupation, String father, String mother, String ref1_name, String ref1_position, String ref1_address, String ref2_name, String ref2_position, String ref2_address, String status, String date_encoded, int juri, String pid, int type, String contactno, String emailadd, String mcn) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO dbo.membersTBL"
                + "("
                + "acctname,"
                + "bdate,"
                + "bplace,"
                + "sex,"
                + "civil_status,"
                + "address,"
                + "highest_edu,"
                + "occupation,"
                + "employer,"
                + "position,"
                + "monthly_income,"
                + "spouse_name,"
                + "spouse_age,"
                + "spouse_occupation,"
                + "father,"
                + "mother,"
                + "ref1_name,"
                + "ref1_position,"
                + "ref1_address,"
                + "ref2_name,"
                + "ref2_position,"
                + "ref2_address,"
                + "status,"
                + "date_encoded, "
                + "juridical_stat, "
                + "partID, "
                + "type, "
                + "contactNo, "
                + "emailAdd, mcn "
                + ") "
                + "VALUES "
                + "('" + acctname + "','" + bdate + "','" + bplace + "','" + sex + "','" + civil_status + "','" + address + "','" + highest_edu + "','" + occupation + "','" + employer + "','" + position + "','" + monthly_income + "','" + spouse_name + "','" + spouse_age + "','" + spouse_occupation + "','" + father + "','" + mother + "','" + ref1_name + "','" + ref1_position + "','" + ref1_address + "','" + ref2_name + "','" + ref2_position + "','" + ref2_address + "','" + status + "','" + date_encoded + "','" + juri + "','" + pid + "','" + type + "','" + contactno + "','" + emailadd + "','" + mcn + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsUpdateProfile(String acctname, String bdate, String bplace, int sex, int civil_status, String address, String highest_edu, String occupation, String employer, String position, String monthly_income, String spouse_name, String spouse_age, String spouse_occupation, String father, String mother, String ref1_name, String ref1_position, String ref1_address, String ref2_name, String ref2_position, String ref2_address, String memid, int type, String contactno, String emailadd, String mcn) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.membersTBL SET "
                + "acctname='" + acctname + "', "
                + "bdate='" + bdate + "', "
                + "bplace='" + bplace + "', "
                + "sex=" + sex + ", "
                + "civil_status=" + civil_status + ", "
                + "address='" + address + "', "
                + "highest_edu='" + highest_edu + "', "
                + "occupation='" + occupation + "', "
                + "employer='" + employer + "', "
                + "position='" + position + "', "
                + "monthly_income='" + monthly_income + "', "
                + "spouse_name='" + spouse_name + "', "
                + "spouse_age='" + spouse_age + "', "
                + "spouse_occupation='" + spouse_occupation + "', "
                + "father='" + father + "', "
                + "mother='" + mother + "', "
                + "ref1_name='" + ref1_name + "', "
                + "ref1_position='" + ref1_position + "', "
                + "ref1_address='" + ref1_address + "', "
                + "ref2_name='" + ref2_name + "', "
                + "ref2_position='" + ref2_position + "', "
                + "ref2_address='" + ref2_address + "', "
                + "type='" + type + "', "
                + "contactNo='" + contactno + "', "
                + "emailAdd='" + emailadd + "', "
                + "mcn='" + mcn + "' "
                + "WHERE memberID=" + memid;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsAddProfile2(String acctname, String address, String date_encoded, int juri) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO dbo.membersTBL"
                + "("
                + "acctname,"
                + "address, "
                + "date_encoded, "
                + "juridical_stat "
                + ") "
                + "VALUES "
                + "('" + acctname + "','" + address + "','" + date_encoded + "','" + juri + "')";

        try {
            stmtAddPArt = conn.createStatement();
            stmtAddPArt.executeUpdate(createString);
            stmtAddPArt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsUpdateProfile2(String acctname, String address, String memid) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE dbo.membersTBL SET "
                + "acctname='" + acctname + "', "
                + "address='" + address + "' "
                + "WHERE memberID=" + memid;

        try {
            stmtAddPArt = conn.createStatement();
            stmtAddPArt.executeUpdate(createString);
            stmtAddPArt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsDeleteProfile(String pID) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "DELETE FROM membersTBL WHERE memberID='" + pID + "'";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void AddConsumer(String acctno) {
        // MDI.UserID
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO Consumer(AcctNo, "
                + "TownCode, "
                + "RouteCode, "
                + "AcctCode, "
                + "RouteSeqNo, "
                + "ClassCode, "
                + "AcctName, "
                + "AcctAddress, "
                + "MembershipID, "
                + "UserID, "
                + "TransDate) "
                + "SELECT AcctNo, TownCode, RouteCode, AcctCode, RouteSeqNo, ClassCode, AcctName, AcctAddress, MembershipID, " + ParentWindow.getUserID() + ",'" + nowDate2 + "' "
                + "FROM connTBL WHERE AcctNo='" + acctno + "'";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsAddConn(String tcode, String rcode, String rseq, String accountname, String address, String classCode, String memid, String acctcode, String add, int ctype, String transdate, String edate, int AcctNO) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO dbo.connTBL "
                + "("
                + "TownCode, RouteCode, RouteSeqNo, AcctName, ClassCode,MembershipID, AcctCode, AcctAddress, connType, TransDate, expiryDate, AcctNo"
                + ")"
                + "VALUES"
                + "("
                + "'" + tcode + "',"
                + "'" + rcode + "',"
                + "'" + rseq + "',"
                + "'" + accountname + "',"
                + "'" + classCode + "',"
                + "'" + memid + "',"
                + "'" + acctcode + "',"
                + "'" + add + "',"
                + "'" + ctype + "',"
                + "'" + transdate + "', "
                + "'" + edate + "', "
                + "'" + AcctNO + "' "
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

    public static boolean rsIsEncodedNJ(int id) {
        boolean found = false;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT partID FROM membersTBL WHERE partID=" + id;
        //stmtIncomingShed 

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

    public static void rsUpdateConnStat(int AcctNo, int Stat) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "UPDATE connTBL SET "
                + "status='" + Stat + "' WHERE AcctNo=" + AcctNo;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void rsAddAppStatLog(int AcctNo, int Status) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO appStatLogTBL (AcctNo, Status) VALUES (" + AcctNo + "," + Status + ")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

}
