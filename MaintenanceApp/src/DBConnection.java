import java.sql.*;

class DBConnection {
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/managementapp", "postgres", "aryaan");
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }
}
