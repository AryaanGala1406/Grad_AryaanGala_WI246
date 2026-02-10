import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDBOperations implements AdminDAO {

    private Connection con = DBConnection.getConnection();

    // Admin Operations
    // find the request with the help of request id
    public Request getRequestById(int reqId) {

    final String SQL = """
        SELECT *
        FROM update_requests
        WHERE req_id = ?
    """;

    try (PreparedStatement ps = con.prepareStatement(SQL)) {

        ps.setInt(1, reqId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return mapRowToRequest(rs); // your existing mapper
            }
        }

    } catch (SQLException e) {
        System.out.println("Error fetching request: " + e.getMessage());
    }

    return null;
}

    // View All Sites -> view all sites that are available
    public ArrayList<Site> getAllSites() {
        ArrayList<Site> list = new ArrayList<>();
        String SQL = """
                    SELECT s.*, u.name AS owner_name
                    FROM sites s
                    LEFT JOIN users u ON s.user_id = u.user_id
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Site s = SiteFactory.create(
                        rs.getInt("site_id"),
                        rs.getInt("length"),
                        rs.getInt("width"),
                        rs.getString("type"),
                        rs.getInt("user_id"));

                // Add owner name
                s.setOwnerName(rs.getString("owner_name"));
                s.getArea();
                list.add(s);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    // Get Single Site
    public Site getSiteBySiteId(int siteId) {
        String SQL = """
                SELECT  s.*, u.name AS owner_name
                FROM sites s
                LEFT JOIN users u ON s.user_id = u.user_id
                WHERE site_id=?
                        """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Site s = SiteFactory.create(
                        rs.getInt("site_id"),
                        rs.getInt("length"),
                        rs.getInt("width"),
                        rs.getString("type"),
                        rs.getInt("user_id"));
                // Add owner name
                s.setOwnerName(rs.getString("owner_name"));
                s.getArea();
                return s;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // 2. Get All Occupied Sites
    // 3. Get All Un-Occupied Sites
    public ArrayList<Site> getSitesByOccupiedStatus(boolean occupied) {
        ArrayList<Site> list = new ArrayList<>();
        String SQL = """
                    SELECT s.*, u.name AS owner_name
                    FROM sites s
                    LEFT JOIN users u ON s.user_id = u.user_id
                    WHERE s.occupied = ?;
                """;

        try {
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setBoolean(1, occupied);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Site s = SiteFactory.create(
                        rs.getInt("site_id"),
                        rs.getInt("length"),
                        rs.getInt("width"),
                        rs.getString("type"),
                        rs.getInt("user_id"));

                // Add owner name
                s.setOwnerName(rs.getString("owner_name"));
                s.getArea();
                list.add(s);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    private Request mapRowToRequest(ResultSet rs) throws SQLException {
        return RequestFactory.create(
                rs.getInt("req_id"),
                ReqType.fromDb(rs.getString("req_type")),
                rs.getInt("user_id"),
                (Integer) rs.getObject("site_id"),
                rs.getString("new_name"),
                rs.getString("new_owner_name"),
                rs.getString("new_site_type"),
                RequestStatus.fromDb(rs.getString("status")),
                rs.getTimestamp("requested_on") == null
                        ? null
                        : rs.getTimestamp("requested_on").toInstant().atOffset(java.time.ZoneOffset.UTC));
    }

    // 4. View Pending Owner Detail Requests
    // 6. View Pending Site Requests
    // View Pending Requests as per req_type
    public ArrayList<Request> getPendingRequest(String reqType) {
        ArrayList<Request> list = new ArrayList<>();
        final String SQL = """
                    SELECT *
                    FROM update_requests
                    WHERE req_type = ? AND status = 'PENDING'
                    ORDER BY requested_on DESC
                """;

        try {
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, ReqType.fromDb(reqType).name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToRequest(rs));
            }
        } catch (Exception e) {
            System.out.println(
                    "Error fetching pending " + ReqType.fromDb(reqType).name() + " requests: " + e.getMessage());
        }
        return list;
    }

    // is request type pending or not
    public boolean isRequestPending(int reqId) {

        final String SQL = """
                    SELECT status
                    FROM update_requests
                    WHERE req_id = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, reqId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    return RequestStatus.PENDING.toString().equalsIgnoreCase(status);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error checking request status: " + e.getMessage());
        }

        return false; // not found or not pending
    }

    // 5. Approve / Reject Owner Detail Requests
    // 7. Approve / Reject Site Requests
    // Approve / Reject Requests as per req_type
    public boolean updateRequestStatus(int reqId, boolean approve, String reqType) {

        if (!isRequestPending(reqId)) {
            System.out.println("Incorrect Request id as it is not pending.");
            return false;
        }

        final String SQL = """
                    UPDATE update_requests
                    SET status = ?
                    WHERE req_id = ?
                      AND status = 'PENDING'
                      AND req_type = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setString(1, approve ? "APPROVED" : "REJECTED");
            ps.setInt(2, reqId);
            ps.setString(3, reqType);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating request: " + e.getMessage());
        }

        return false;
    }

    // 8. Assign Site to Owner
    public boolean assignSiteByUserId(int siteId, int userId) {
        final String SQL = """
                    UPDATE sites s
                    SET user_id = ?
                    WHERE s.site_id = ?
                    AND EXISTS (SELECT 1 FROM users u WHERE u.user_id = ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, userId);
            ps.setInt(2, siteId);
            ps.setInt(3, userId);
            int updated = ps.executeUpdate(); // use executeUpdate for UPDATE statements
            return updated > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // 9. Update Site Type
    public boolean updateSiteType(int siteId, String type) {
        final String SQL = """
                    UPDATE sites
                    SET type = ?
                    WHERE site_id = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, SiteType.fromDb(type).name());
            ps.setInt(2, siteId);
            int updated = ps.executeUpdate(); // use executeUpdate for UPDATE statements
            return updated > 0;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // 10. Remove Owner
    public boolean removeOwnerBySiteId(int siteId) {
        final String SQL = """
                    UPDATE sites
                    SET user_id = NULL
                    WHERE site_id = ?
                """;
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            int updated = ps.executeUpdate();
            return updated == 1; // or > 0 if multiple rows could match (they shouldn't)
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Site getSiteById(int siteId) {
        String SQL = "SELECT * FROM sites WHERE site_id = ?";

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return SiteFactory.create(
                            rs.getInt("site_id"),
                            rs.getInt("length"),
                            rs.getInt("width"),
                            rs.getString("type"),
                            rs.getInt("user_id"));
                }
            }
        } catch (Exception e) {
            System.out.println("getSiteById error: " + e.getMessage());
        }

        return null; // No site found
    }

    // 3. Collect Maintenance
    public boolean collectMaintenance(int siteId) {
        Site s = getSiteById(siteId);

        if (s == null) {
            System.out.println("Site not found");
            return false;
        }

        int amount = s.calculateMaintenance();
        System.out.println("Maintenance Amount = Rs " + amount);

        String SQL = "UPDATE sites SET status=COMPLETED WHERE site_id=?";
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            ps.executeUpdate();
            System.out.println("Maintenance collected successfully");
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // 4. Pending Maintenance
    public ArrayList<Site> pendingMaintenance(int year) {
        ArrayList<Site> list = new ArrayList<>();
        String SQL = """
                SELECT * FROM sites s
                WHERE NOT EXISTS (
                    SELECT 1 FROM maintenance m
                    WHERE m.site_id=s.site_id AND m.year=?
                )
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(SiteFactory.create(
                        rs.getInt("site_id"),
                        rs.getInt("length"),
                        rs.getInt("width"),
                        rs.getString("type"),
                        rs.getInt("owner_id")));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    // public void collectMaintenance(int siteId, int year) {
    // Site s = getSite(siteId);
    // int amount = s.calculateMaintenance();

    // try (PreparedStatement ps = con.prepareStatement(
    // "INSERT INTO maintenance(site_id,year,amount,paid_on)
    // VALUES(?,?,?,CURRENT_DATE)")) {
    // ps.setInt(1, siteId);
    // ps.setInt(2, year);
    // ps.setInt(3, amount);
    // ps.executeUpdate();
    // System.out.println("Maintenance collected: Rs " + amount);
    // } catch (Exception e) { System.out.println(e); }
    // }

    // private Site getSite(int siteId) {
    // try (PreparedStatement ps =
    // con.prepareStatement("SELECT * FROM sites WHERE site_id=?")) {
    // ps.setInt(1, siteId);
    // ResultSet rs = ps.executeQuery();
    // if (rs.next())
    // return SiteFactory.create(
    // siteId,
    // rs.getInt("length"),
    // rs.getInt("width"),
    // rs.getString("type"),
    // rs.getBoolean("occupied"),
    // rs.getInt("owner_id")
    // );
    // } catch (Exception e) {}
    // return null;
    // }

    // public ArrayList<Site> pendingMaintenance(int year) {
    // ArrayList<Site> list = new ArrayList<>();
    // String SQL = """
    // SELECT * FROM sites s
    // WHERE NOT EXISTS (
    // SELECT 1 FROM maintenance m
    // WHERE m.site_id=s.site_id AND m.year=?
    // )
    // """;

    // try (PreparedStatement ps = con.prepareStatement(SQL)) {
    // ps.setInt(1, year);
    // ResultSet rs = ps.executeQuery();
    // while (rs.next()) {
    // list.add(SiteFactory.create(
    // rs.getInt("site_id"),
    // rs.getInt("length"),
    // rs.getInt("width"),
    // rs.getString("type"),
    // rs.getBoolean("occupied"),
    // rs.getInt("owner_id")
    // ));
    // }
    // } catch (Exception e) { System.out.println(e); }
    // return list;
    // }

}