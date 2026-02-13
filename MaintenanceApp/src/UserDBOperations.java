import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

class UserDBOperations implements UserDAO {

    static Connection con = DBConnection.getConnection();

    // Owner Operations
    // Owner Get Site Request - to get the site of the owners
    public ArrayList<Site> getSiteByOwnerId(int ownerId) {
        String SQL = "SELECT * FROM sites WHERE user_id = ?";
        ArrayList<Site> myList = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    myList.add(SiteFactory.create(
                            rs.getInt("site_id"),
                            rs.getInt("length"),
                            rs.getInt("width"),
                            rs.getString("type"),
                            rs.getInt("user_id")));
                }
            }
        } catch (Exception e) {
            System.out.println("getSiteByOwnerId error: " + e.getMessage());
        }
        return myList; // return empty list if none
    }

    // Owner Update Request - to update the site owner request to admin
    public boolean raiseUpdateRequest(int siteId, User user) {
        String SQL = "INSERT INTO update_requests(site_id, user_id, req_type, new_owner_name, status, requested_on) " +
                "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            ps.setInt(2, user.getUserId());
            ps.setString(3, ReqType.SITE.name());
            ps.setString(4, user.getName()); // requesting to change to this name (per your model)
            ps.setString(5, RequestStatus.PENDING.name());
            ps.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
            // ps.setDate(6, new java.sql.Date(System.currentTimeMillis())); // current date
            ps.executeUpdate();
            System.out.println("Update site owner request sent to Admin");
            return true;
        } catch (Exception e) {
            System.out.println("raiseUpdateRequest error: " + e.getMessage());
        }
        return false;
    }

    // User Name Update Request - to update the user name request to admin
    public boolean raiseUpdateNameRequest(String newName, User user) {
        String SQL = "INSERT INTO update_requests(user_id, req_type, new_name, status, requested_on) " +
                "VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, user.getUserId());
            ps.setString(2, ReqType.DETAILS.name());
            ps.setString(3, newName);
            ps.setString(4, RequestStatus.PENDING.name());
            ps.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
            // ps.setDate(5, new java.sql.Date(System.currentTimeMillis())); // current date
            ps.executeUpdate();
            System.out.println("Update Name request sent to Admin");
            return true;
        } catch (Exception e) {
            System.out.println("raiseUpdateNameRequest error: " + e.getMessage());
        }
        return false;
    }

    // User update password - password of the user can be updated without approval
    public boolean updateUserPassword(String newPassword, User user) {
        String SQL = "UPDATE users SET password = ? WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, newPassword); // index 1
            ps.setInt(2, user.getUserId()); // index 2
            int updated = ps.executeUpdate();
            if (updated > 0) {
                System.out.println("Password updated successfully!");
                return true;
            }
        } catch (Exception e) {
            System.out.println("updateUserPassword error: " + e.getMessage());
        }
        return false;
    }

    public int getSiteOwnerid(int site_id) {
        String SQL = "SELECT user_id FROM sites WHERE site_id = ?";
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, site_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // Move cursor to first row
                    return rs.getInt("user_id");
                }
            }
        } catch (Exception e) {
            System.out.println("getSiteOwnerid error: " + e.getMessage());
        }
        return -1; // If no row found or on error
    }

    // Site Type Update Request - to update the site type request to admin
    public boolean raiseUpdateSiteTypeRequest(int siteId, String type, User user) {
        String SQL = "INSERT INTO update_requests(site_id, user_id, req_type, new_site_type, status, requested_on) " +
                "VALUES (?,?,?,?,?,?)";

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            ps.setInt(2, user.getUserId());
            ps.setString(3, ReqType.SITE.name()); // safer than "SITE"
            ps.setString(4, SiteType.fromDb(type).toString()); // normalized enum value
            ps.setString(5, RequestStatus.PENDING.name());
            ps.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();
            System.out.println("Update site type request sent to Admin");
            return true;

        } catch (Exception e) {
            System.out.println("raiseUpdateSiteTypeRequest error: " + e.getMessage());
        }
        return false;
    }

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

    public int getPendingMaintenance(int siteId, int ownerId) {

        String SQL = """
                    SELECT balance_amount
                    FROM maintenance
                    WHERE site_id = ?
                      AND owner_id = ?
                      AND status = 'PENDING'
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            ps.setInt(2, ownerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("balance_amount");
            }
        } catch (Exception e) {
            System.out.println("getPendingMaintenance error: " + e.getMessage());
        }

        return 0;
    }

    public int getMaintenanceId(int siteId, int ownerId) {

        String SQL = """
                    SELECT maintenance_id
                    FROM maintenance
                    WHERE site_id = ?
                      AND owner_id = ?
                      AND status = 'PENDING'
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            ps.setInt(2, ownerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("maintenance_id");
            }
        } catch (Exception e) {
            System.out.println("getMaintenanceId error: " + e.getMessage());
        }

        return -1;
    }

    public boolean payMaintenance(int siteId, int ownerId, int amount) {

        int balance = getPendingMaintenance(siteId, ownerId);

        if (balance == 0) {
            System.out.println("No pending maintenance.");
            return false;
        }

        if (amount <= 0 || amount > balance) {
            System.out.println("Invalid payment amount.");
            return false;
        }

        int maintenanceId = getMaintenanceId(siteId, ownerId);

        if (maintenanceId == -1) {
            System.out.println("Maintenance record not found.");
            return false;
        }

        String SQL = """
                    INSERT INTO maintenance_transactions
                    (maintenance_id, owner_id, paid_amount)
                    VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, maintenanceId);
            ps.setInt(2, ownerId);
            ps.setInt(3, amount);

            ps.executeUpdate();

            System.out.println("Payment successful!");
            return true;

        } catch (Exception e) {
            System.out.println("payMaintenance error: " + e.getMessage());
        }

        return false;
    }

    public List<MaintenanceTransaction> getMyTransactions(int ownerId) {

        List<MaintenanceTransaction> list = new ArrayList<>();

        String SQL = """
                    SELECT t.txn_id,
                           m.site_id,
                           m.year,
                           t.paid_amount,
                           t.paid_on,
                           m.balance_amount
                    FROM maintenance_transactions t
                    JOIN maintenance m ON t.maintenance_id = m.maintenance_id
                    WHERE t.owner_id = ?
                    ORDER BY t.paid_on DESC
                """;

        try (PreparedStatement ps = con.prepareStatement(SQL)) {

            ps.setInt(1, ownerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new MaintenanceTransaction(
                        rs.getInt("txn_id"),
                        rs.getInt("site_id"),
                        "SELF",
                        rs.getInt("year"),
                        rs.getInt("paid_amount"),
                        rs.getObject("paid_on", java.time.OffsetDateTime.class),
                        rs.getInt("balance_amount")));
            }

        } catch (Exception e) {
            System.out.println("getMyTransactions error: " + e.getMessage());
        }

        return list;
    }

}
