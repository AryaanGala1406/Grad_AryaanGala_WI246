import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class SiteDBOperations implements SiteDAO {

    private Connection con = DBConnection.getConnection();

    // 1. View All Sites
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
                        rs.getInt("user_id")
                );

                // Add owner name
                s.setOwnerName(rs.getString("owner_name"));

                list.add(s);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    // 2. Get Single Site
    public Site getSiteBySiteId(int siteId) {
        String SQL = "SELECT * FROM sites WHERE site_id=?";

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return SiteFactory.create(
                        rs.getInt("site_id"),
                        rs.getInt("length"),
                        rs.getInt("width"),
                        rs.getString("type"),
                        rs.getInt("owner_id")
                );
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
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

        String SQL = "UPDATE sites SET maintenance_paid=true WHERE site_id=?";
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
                        rs.getInt("owner_id")
                ));
            }
        } catch (Exception e) { System.out.println(e); }
        return list;
    }

   
    // 6. Admin View Requests
    public ArrayList<UpdateRequest> getPendingRequests() {
        ArrayList<UpdateRequest> list = new ArrayList<>();
        String SQL = "SELECT * FROM update_requests WHERE status='PENDING'";

        try (PreparedStatement ps = con.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UpdateRequest ur = new UpdateRequest();
                ur.reqId = rs.getInt("req_id");
                ur.siteId = rs.getInt("site_id");
                ur.newName = rs.getString("new_owner_name");
                ur.status = rs.getString("status");
                list.add(ur);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    // 7. Approve / Reject
    public boolean approveRequest(int reqId, boolean approve) {
        String status = approve ? "APPROVED" : "REJECTED";

        String SQL = "UPDATE update_requests SET status=? WHERE req_id=?";
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, status);
            ps.setInt(2, reqId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // Owner Operations
    // Owner Get Site Request - to get the site of the owners 
    public ArrayList<Site> getSiteByOwnerId(int ownerId) {
        String SQL = "SELECT * FROM sites WHERE owner_id=?";
        ArrayList<Site> myList = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                myList.add(SiteFactory.create(
                        rs.getInt("site_id"),
                        rs.getInt("length"),
                        rs.getInt("width"),
                        rs.getString("type"),
                        rs.getInt("owner_id")
                ));
            }
            return myList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // Owner Update Request - to update the site owner request to admin
    public boolean raiseUpdateRequest(int siteId, User user) {
        String SQL = "INSERT INTO update_requests(site_id, user_id, new_owner_name, status, requested_on) VALUES(?,?,?,?,?)";

        int userId = user.getUserId();
        String name = user.getName();

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            ps.setInt(2, userId);
            ps.setString(3, name);
            ps.setString(4, "PENDING");
            ps.setDate(5, new java.sql.Date(System.currentTimeMillis()));  // current date

            ps.executeUpdate();
            System.out.println("Update site owner request sent to Admin");
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // User Name Update Request - to update the user name request to admin
    public boolean raiseUpdateNameRequest(String newName, User user) {
        String SQL = "INSERT INTO update_details_request(user_id, new_name, status, requested_on) VALUES(?,?,?,?)";

        int userId = user.getUserId();

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(2, userId);
            ps.setString(3, newName);
            ps.setString(4, "PENDING");
            ps.setDate(5, new java.sql.Date(System.currentTimeMillis()));  // current date

            ps.executeUpdate();
            System.out.println("Update Name request sent to Admin");
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // User update password - password of the user can be updated without approval 
    public boolean updateUserPassword(String newPassword, User user) {
        String SQL = "UPDATE user SET password=? WHERE user_id=?";

        int userId = user.getUserId();

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(3, newPassword);
            ps.setInt(2, userId);
    
            ps.executeUpdate();
            System.out.println("Password updated successfully!");
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // Site Type Update Request - to update the site type request to admin
    public boolean raiseUpdateSiteTypeRequest(int siteId, String type, User user) {
        String SQL = "INSERT INTO update_requests(site_id, user_id, type, status, requested_on) VALUES(?,?,?,?,?)";

        int userId = user.getUserId();

        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setInt(1, siteId);
            ps.setInt(2, userId);
            ps.setString(3, type);
            ps.setString(4, "PENDING");
            ps.setDate(5, new java.sql.Date(System.currentTimeMillis()));  // current date

            ps.executeUpdate();
            System.out.println("Update site type request sent to Admin");
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
