/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.co;

import java.sql.Statement;
import memsys.global.FunctionFactory;

public class CONumberController {

    static String NowYear = FunctionFactory.GetSystemNowYear();
    static Statement stmt;

    public int GetNewCOControlNumber() {
        int yr = Integer.valueOf(NowYear);
        int ctrlno = 0;

        return ctrlno;
    }

//    private int GetValidateCTRLNo() {
//        int maxAN = 0;
//        Connection conn = MainDBConn.getConnection();
//        String createString;
//        createString = "SELECT COALESCE(MAX(AcctNo), 0) AS AcctNo FROM connTBL";
//
//        try {
//            stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery(createString);
//
//            while (rs.next()) {
//                maxAN = rs.getInt(1);
//            }
//
//            stmt.close();
//            conn.close();
//
//        } catch (SQLException e) {
//            JOptionPane.showMessageDialog(null, "Error RSQuery/004: Query Select All Areas!");
//        }
//
//        return maxAN;
//    }
}
