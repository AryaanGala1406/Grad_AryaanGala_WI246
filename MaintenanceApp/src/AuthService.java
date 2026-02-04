import java.sql.*;

class AuthService {
    public static User Login(String name, String password) throws Exception {
        try {
            Connection con = DBConnection.getConnection();
            final String SQL = "SELECT * FROM users WHERE name=? AND password=?";

            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, name);
            pstmt.setString(2,password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("name");
                String role = rs.getString("role");
                return new User(userId, username, role);
            }

            throw new Exception("UserNotFoundException");
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public static boolean checkUsername(String name) {
        if (name == null)
            return false;

        try {
            final String SQL = "SELECT name FROM users WHERE name = ?";

            Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQL);

            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // true if at least one row exists
            }
        } catch (Exception e) {
            System.err.println("Error checking username: " + e.getMessage());
            return false; // or rethrow, depending on your error policy
        }
    }

    public static void createUser(String name, String password) {
        try {
            Connection con = DBConnection.getConnection();
            if (checkUsername(name)) {
                System.out.println("Username already exists.");
                return;
            }
            final String SQL = "INSERT INTO users VALUES (?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, "owner");

            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error creating user: " + e);
        }
    }

}