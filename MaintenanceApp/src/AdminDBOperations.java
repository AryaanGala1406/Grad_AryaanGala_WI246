import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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

        Request req = getRequestById(reqId);
        if (req == null) {
            System.out.println("Request not found.");
            return false;
        }

        try {
            con.setAutoCommit(false);

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

                int updated = ps.executeUpdate();

                if (updated == 0) {
                    con.rollback();
                    return false;
                }
            }

            if (approve && req instanceof SiteRequest siteReq) {

                Integer siteId = siteReq.getSiteId();
                int ownerId = siteReq.getUserId();

                if (siteReq.getNewOwnerName() != null && siteId != null) {

                    boolean assigned = assignSiteByUserId(siteId, ownerId);

                    if (!assigned) {
                        con.rollback();
                        return false;
                    }

                    boolean maintenanceCreated = assignMaintenance(siteId, ownerId);

                    if (!maintenanceCreated) {
                        con.rollback();
                        return false;
                    }
                }

                // If it was site type change request
                if (siteReq.getNewSiteType() != null && siteId != null) {
                    updateSiteType(siteId, siteReq.getNewSiteType());
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ignored) {
            }
            System.out.println("Error updating request: " + e.getMessage());
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (Exception ignored) {
            }
        }
    }

    // 8. Assign Site to Owner
    public boolean assignSiteByUserId(int siteId, int userId) {

        final String SQL = """
                    UPDATE sites s
                    SET user_id = ?
                    WHERE s.site_id = ?
                      AND s.user_id IS NULL
                      AND EXISTS (
                          SELECT 1
                          FROM users u
                          WHERE u.user_id = ?
                      )
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, userId);
            ps.setInt(2, siteId);
            ps.setInt(3, userId);

            int updated = ps.executeUpdate();
            return updated > 0;

        } catch (Exception e) {
            System.out.println("assignSiteByUserId error: " + e.getMessage());
            return false;
        }
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

    public boolean assignMaintenance(int siteId, int ownerId) {

        Site site = getSiteById(siteId);

        if (site == null) {
            System.out.println("Site not found. Cannot assign maintenance.");
            return false;
        }

        int area = site.getLength() * site.getWidth();

        int rate = (site.getType() == SiteType.OPEN_SITE) ? 6 : 9;
        int amount = area * rate;

        String SQL = """
                INSERT INTO maintenance (
                    site_id,
                    owner_id,
                    year,
                    total_amount,
                    balance_amount
                )
                VALUES (?, ?, EXTRACT(YEAR FROM CURRENT_DATE), ?, ?)
                ON CONFLICT (site_id, year) DO NOTHING
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, siteId);
            ps.setInt(2, ownerId);
            ps.setInt(3, amount);
            ps.setInt(4, amount);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            System.out.println("assignMaintenance error: " + e.getMessage());
            return false;
        }
    }

    // Get Pending Maintenance
    // Get Pending Maintenance (Remaining Balance)
    public int getPendingMaintenanceBySiteId(int siteId) {

        final String SQL = """
                SELECT balance_amount
                FROM maintenance
                WHERE site_id = ?
                  AND status = 'PENDING'
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, siteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("balance_amount");
                }
            }

        } catch (SQLException e) {
            System.out.println("getPendingMaintenanceBySiteId error: " + e.getMessage());
        }

        return 0; // No pending maintenance
    }


    public List<MaintenanceTransaction> getAllTransactions() {

    List<MaintenanceTransaction> list = new ArrayList<>();

    final String SQL = """
        SELECT 
            t.txn_id,
            m.site_id,
            u.name AS owner_name,
            m.year,
            t.paid_amount,
            t.paid_on,
            m.balance_amount
        FROM maintenance_transactions t
        JOIN maintenance m ON t.maintenance_id = m.maintenance_id
        JOIN users u ON t.owner_id = u.user_id
        ORDER BY t.paid_on DESC
    """;

    try (PreparedStatement ps = con.prepareStatement(SQL);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(new MaintenanceTransaction(
                    rs.getInt("txn_id"),
                    rs.getInt("site_id"),
                    rs.getString("owner_name"),
                    rs.getInt("year"),
                    rs.getInt("paid_amount"),
                    rs.getObject("paid_on", OffsetDateTime.class),
                    rs.getInt("balance_amount")
            ));
        }

    } catch (SQLException e) {
        System.out.println("getAllTransactions error: " + e.getMessage());
    }

    return list;
}

}