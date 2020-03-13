package memsys.ui.sanitize;

import memsys.global.DBConn.MainDBConn;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SanitizeLog {

    static Statement stmt;

    public void CreateLog(String dtl, String td, int userID, String remarks, int mid) {
        Connection conn = MainDBConn.getConnection();
        String createString;
        createString = "INSERT INTO sanitizeLogTBL(LogDetails, TransDate, UserID, Remarks, MemberID) VALUES('" + dtl + "','" + td + "'," + userID + ",'" + remarks + "',"+mid+")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(createString);
            stmt.close();
            conn.close();

        } catch (SQLException e) {
        }
    }
}
