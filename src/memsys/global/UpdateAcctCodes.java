/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.global;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import memsys.global.DBConn.MainDBConn;



public class UpdateAcctCodes {
private Statement stmt;
private String RouteCode;
private String RouteSeqCode;

    private void SaveRouteSeqCodes() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public String getRouteCode() {
        return RouteCode;
    }

    public void setRouteCode(String RouteCode) {
        this.RouteCode = RouteCode;
    }

    public String getRouteSeqCode() {
        return RouteSeqCode;
    }

    public void setRouteSeqCode(String RouteSeqCode) {
        this.RouteSeqCode = RouteSeqCode;
    }

    private void SaveCheckMeter() {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void UpdateCodes() {
        SaveRouteSeqCodes();
        SaveCheckMeter();
    }
}
