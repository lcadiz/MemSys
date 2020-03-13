/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import memsys.global.DBConn.AGMADBConn;
import memsys.global.DBConn.MainDBConn;
import memsys.global.FunctionFactory;

/**
 *
 * @author LESTER
 */
public class AGMAEntry {

    static Statement stmt;

//    public static void main(String[] args) {
//AGMAEntry x =new AGMAEntry();
//        x.PostEntry("","",10);
//    }

    public void PostEntry(String name, String address, int pid) {
        int bcode = GetBarangayCode(pid);
        //  System.out.println("bcode:"+bcode);
        int towncode = GetTownCode(bcode);
        //  System.out.println("tcode:"+towncode);
        String tc = FunctionFactory.NumFormatter(towncode);
      //  System.out.println(tc);
        Insert(name, address, tc);
    }

    private void Insert(String name, String address, String tcode) {
        Connection conn = AGMADBConn.getConnection();
        String createString;
        createString = "INSERT INTO Membership(name, address, stat, towncode) "
                + "VALUES ('" + name + "','" + address + "','S','" + tcode + "')";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    int GetTownCode(int bid) {
        int tcode = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT areacode FROM BarangayTBL WHERE bID=" + bid;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                tcode = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return tcode;
    }

    int GetBarangayCode(int pid) {
        int bcode = 0;

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT brgyID FROM ParticipantsTBL WHERE partID=" + pid;

        //int rc = 0;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(createString);

            while (rs.next()) {
                bcode = rs.getInt(1);
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.getStackTrace();
        }

        return bcode;
    }

}
