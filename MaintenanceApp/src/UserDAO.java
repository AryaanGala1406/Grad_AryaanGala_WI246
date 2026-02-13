import java.util.ArrayList;
import java.util.List;

public interface UserDAO {
    // find the list of sites that the owner own using their owner id 
    ArrayList<Site> getSiteByOwnerId(int ownerId);
    
    // returns the owner if of the site 
    int getSiteOwnerid(int site_id);
    
    // users can update their password without admin request  
    boolean updateUserPassword(String newPassword, User user);

    // request sent to admin side -> update in the db table 
    boolean raiseUpdateRequest(int siteId, User user);
    boolean raiseUpdateNameRequest(String newName, User user);
    boolean raiseUpdateSiteTypeRequest(int siteId, String type, User user);

    // get the site details to print with the help of site id
    Site getSiteBySiteId(int siteId);

    int getPendingMaintenance(int siteId, int ownerId);
    int getMaintenanceId(int siteId, int ownerId);
    boolean payMaintenance(int siteId, int ownerId, int amount);
    List<MaintenanceTransaction> getMyTransactions(int ownerId);

}

