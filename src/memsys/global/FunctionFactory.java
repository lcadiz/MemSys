/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.global;

import memsys.global.DBConn.MainDBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LESTER
 */
public class FunctionFactory {

    static Statement stmt;

    public static String getSystemInfo() {
        String sys = null;
        boolean is64bit = false;
        if (System.getProperty("os.name").contains("Windows")) {
            is64bit = (System.getenv("ProgramFiles(x86)") != null);
        } else {
            is64bit = (System.getProperty("os.arch").indexOf("64") != -1);
        }
        //System.out.println(is64bit);

        if (is64bit == true) {
            sys = "x64-based PC";
        } else {
            sys = "x86-based PC";
        }

        return sys;
    }

    public static String MemIdFormat(String mi) {
        DecimalFormat money = new DecimalFormat("0000000000");
        double aDouble = Double.parseDouble(mi);
        String output = money.format(aDouble);
        return output;
    }

    public static String getMonthName(int month) {
        String nym = "";
        Calendar ca1 = Calendar.getInstance();

        // set(year, month, date) month 0-11
        ca1.set(1999, month - 1, 1);

        // int iMonth=ca1.get(Calendar.MONTH);
        java.util.Date d = new java.util.Date(ca1.getTimeInMillis());

        //System.out.println("Month Name :"+new SimpleDateFormat("MMMM").format(d));
        //System.out.println("Month Name :"+new SimpleDateFormat("MMM").format(d));
        nym = new SimpleDateFormat("MMMM").format(d);
        return nym;
    }

    public static Date GetSystemNowDate() {
        Date NowDate = null;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT GETDATE() AS NowDate";
        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                NowDate = rs.getDate(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return NowDate;
    }

    public static String GetSystemNowDateString() {
        String NowDate = null;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT GETDATE() AS NowDate";
        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                NowDate = rs.getString(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return NowDate;
    }

    public static String GetSystemNowYear() {
        String NowYear = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT GETDATE() AS NowDate";
        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                NowYear = rs.getString(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return NowYear;
    }

    public static Time GetSystemNowTime() {
        Time NowTime = null;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT GETDATE() AS NowDate";
        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                NowTime = rs.getTime(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return NowTime;
    }

    public static int GetSystemNowMonth() {
        int now = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT MONTH('" + GetSystemNowDate() + "')";
        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                now = rs.getInt(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return now;
    }

    public static int GetSystemNowYear2() {
        int now = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT YEAR('" + GetSystemNowDate() + "')";
        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                now = rs.getInt(1);
            }
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return now;
    }

    public static Date getSystemNowDateTime() {
        Date NowDate = null;
        String DateStr = GetSystemNowDate().toString() + " " + GetSystemNowTime().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            NowDate = dateFormat.parse(DateStr);
        } catch (ParseException ex) {
            Logger.getLogger(FunctionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return NowDate;
    }
//    
//        public static Date getSystemNowDate2() {
//        Date NowDate = null;
//        String DateStr = GetSystemNowDate().toString();
//        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//        try {
//            NowDate = dateFormat.parse(DateStr);
//        } catch (ParseException ex) {
//            Logger.getLogger(FunctionFactory.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return NowDate;
//    }

    public static String getSystemNowDateTimeString() {
        //String NowDate = null;
        String DateStr = GetSystemNowDate().toString() + " " + GetSystemNowTime().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        // NowDate = dateFormat.format(DateStr);
        return DateStr;
    }

    public static String getdateN() {

        String DateStr = GetSystemNowDate().toString() + " " + GetSystemNowTime().toString();
        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        // NowDate = dateFormat.format(DateStr);
        return DateStr;
    }

    public static String NumFormatter(int num) {
        DecimalFormat outnum = new DecimalFormat("00");
        double aDouble = num;
        String output = outnum.format(aDouble);
        return output;
    }

    public static String NumFormatter3(int num) {
        DecimalFormat outnum = new DecimalFormat("000");
        double aDouble = num;
        String output = outnum.format(aDouble);
        return output;
    }

    public static String NumFormatter4(int num) {
        DecimalFormat outnum = new DecimalFormat("0000");
        double aDouble = num;
        String output = outnum.format(aDouble);
        return output;
    }

    public static String NumFormatter5(int num) {
        DecimalFormat outnum = new DecimalFormat("00000");
        double aDouble = num;
        String output = outnum.format(aDouble);
        return output;
    }

//     public static void main(String[] args) {
//        String x= NumFormatter(8);
//        System.out.println(x);
//     }
}
