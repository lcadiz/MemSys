/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.reconnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import memsys.global.DBConn.MainDBConn;

/**
 *
 * @author Engkoi Zidac
 */
public class DiscoReco {

    static Statement stmt;

    boolean IsPaid(int DiscoRecoID) {
        boolean flg = false;
        String val = "";

        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "SELECT TransID FROM costingTBL WHERE DiscoRecoID=" + DiscoRecoID + "";

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
    //    System.out.println(val);
        if (val != null) {
            flg = true;
        }

        return flg;
    }
}
