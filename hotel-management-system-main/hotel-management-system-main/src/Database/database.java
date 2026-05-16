package Database;

import java.sql.*;

public class database {
    public static Connection connectDB() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_management_system", "root", "your_password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public static void executeWriteQuery(String query) {
        try {
            Connection con = connectDB();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static ResultSet executeReadQuery(String query) {
        try {
            Connection con = connectDB();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            return rs;

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
