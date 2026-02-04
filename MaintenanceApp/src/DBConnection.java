import java.sql.*;

class DBConnection {
    private static Connection con = null;

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                Class.forName("org.postgresql.Driver");
                con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "aryaan");
                return con;
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
        return null;
    }
}
