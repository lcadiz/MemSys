package memsys.global.DBConn;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AGMADBConn {

    static String userid, password, user;
    static String url;
    static Connection conn;
    static String connStr;

    public static Connection getConnection() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

        } catch (java.lang.ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Database Driver error. Please check the configuration!");
        }

        try {

            String filePath = System.getProperty("user.dir") + "\\config.properties";

            Properties properties = new Properties();

            try {
                properties.load(new FileInputStream(filePath));
            } catch (IOException ex) {
                Logger.getLogger(AGMADBConn.class.getName()).log(Level.SEVERE, null, ex);
            }

            String port = properties.getProperty("port");
            String host = properties.getProperty("host2");
            String database = properties.getProperty("database2");

            url = "jdbc:jtds:sqlserver://" + host + ":" + port + "/" + database;
            user = "laptopuser";
            password = "laptopuser";

            //password = "w";
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            //JOptionPane.showMessageDialog(null, "Error in Connection!");
            JOptionPane.showMessageDialog(null, e.getMessage());

        }

        return conn;
    }
}
