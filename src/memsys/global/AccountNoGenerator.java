/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.global;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import memsys.global.DBConn.MainDBConn;

/**
 *
 * @author Engkoi Zidac
 */
public class AccountNoGenerator {

    static Statement stmt;

    public int GenerateValidAcctNo() {
        int validacctno = 0;

        boolean isValid = false;

        while (isValid == false) {
            validacctno = GetValidAcctNo(GetMaxAcctNo());
           // System.out.println(validacctno);
            boolean isExist=IsAcctNoExist(validacctno);
            if(isExist==false){
                isValid=true;
            }
        }
        return validacctno;
    }

    int GetValidAcctNo(int maxAcctNo) {
        boolean found = false;
        int getacctno = 0;

        int acctno = maxAcctNo + 1;
        String reverse = new StringBuffer(String.valueOf(acctno)).reverse().toString(); //058136

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

    static int GetMaxAcctNo() {
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

    boolean IsAcctNoExist(int acctno) {
        boolean isExist = false;
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT  AcctNo FROM connTBL WHERE AcctNo=" + acctno;

        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            int cntr = 0;
            while (rs.next()) {
                cntr++;
            }

            if (cntr >= 1) {
                isExist = true;
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
        }

        return isExist;
    }
}
