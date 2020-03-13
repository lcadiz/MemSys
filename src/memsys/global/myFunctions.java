package memsys.global;

import java.awt.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class myFunctions {

    public static String convertMD5(String pass) {
        //        

        byte[] defaultBytes = pass.getBytes();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            //*** Testausgabe
            //System.out.println("pass " + pass + "   md5 version is " + hexString.toString());
            pass = hexString + "";
        } catch (NoSuchAlgorithmException nsae) {
        }

        return pass;

    }

    public static String GetOwnIP() {
        String ip = null;
        InetAddress ownIP = null;
        try {
            ownIP = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(myFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
        ip = ownIP.getHostAddress();
        return ip;
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$#-" + n + "s", s);
    }

    public static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "The date you provided is in an invalid date format.");
            return false;
        }
        if (!sdf.format(testDate).equals(date)) {
            JOptionPane.showMessageDialog(null, "The date that you provided is invalid.");
            return false;
        }
        return true;
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getDate2() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getYear() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static int msgOPT(String theMessage) {
        int result = JOptionPane.showConfirmDialog((Component) null, theMessage, "Alert", JOptionPane.YES_NO_CANCEL_OPTION);
        return result;
    }

    public static int msgboxYesNoCancel(String theMessage) {
        int result = JOptionPane.showConfirmDialog((Component) null, theMessage, "Alert", JOptionPane.YES_NO_CANCEL_OPTION);
        return result;
    }

    public static int msgboxYesNo(String theMessage) {
        int result = JOptionPane.showConfirmDialog((Component) null, theMessage, "Alert", JOptionPane.YES_NO_OPTION);
        return result;
    }

    public static String amountFormat2(String amnt) {
        DecimalFormat money = new DecimalFormat("#,##0.00");
        double aDouble = Double.parseDouble(amnt);
        String output = money.format(aDouble);
        return output;
    }

    public static String amountFormat(double amnt) {
        DecimalFormat money = new DecimalFormat("#,##0.00");
        double aDouble = amnt;
        String output = money.format(aDouble);
        return output;
    }

    public static String ACODEFormat(double amnt) {
        DecimalFormat money = new DecimalFormat("00");
        double aDouble = amnt;
        String output = money.format(aDouble);
        return output;
    }

    public static String TCODEFormat(String x) {
        DecimalFormat formatx = new DecimalFormat("00");
        double aDouble = Double.parseDouble(x);
        String output = formatx.format(aDouble);
        return output;
    }

    public static String RCODEFormat(String x) {
        DecimalFormat formatx = new DecimalFormat("000");
        double aDouble = Double.parseDouble(x);
        String output = formatx.format(aDouble);
        return output;
    }

    public static String dateformatter(String wat) {
        Format formatter;
        String output = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy");
        Date date;
        try {
            date = dateFormat.parse(wat);
            formatter = new SimpleDateFormat("MM/dd/yy");
            output = formatter.format(date);
        } catch (ParseException ex) {
            Logger.getLogger(myFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }

        return output;
    }

    public static void main(String[] args) {
        String x = TCODEFormat("1");
        System.out.print(x);
    }
}
