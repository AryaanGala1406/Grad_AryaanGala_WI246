import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBHelper {
    static Connection con = DBConnection.getConnection();

    public static int getUserIdByName(String name) {
        if (name == null)
            return 0;

        final String SQL = "SELECT user_id FROM users WHERE name = ? LIMIT 1";

        try {
            PreparedStatement pstmt = con.prepareStatement(SQL); 

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                } else {
                    return 0; // no user found
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching user_id: " + e.getMessage());
            return 0; // fallback
        }
    }
}
