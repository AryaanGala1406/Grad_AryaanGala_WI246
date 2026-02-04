import java.util.ArrayList;

public interface SiteDAO {

    ArrayList<Site> getAllSites();
    ArrayList<Site> getSiteByOwnerId(int ownerId);

    void collectMaintenance(int siteId, int year);
    ArrayList<Site> pendingMaintenance(int year);

    boolean raiseUpdateRequest(int siteId, String name);
    ArrayList<UpdateRequest> getPendingRequests();
    void processRequest(int reqId, boolean approve);

    boolean raiseUpdateRequest(int siteId, User user);
    boolean updateUserPassword(String newPassword, User user);
    boolean raiseUpdateSiteTypeRequest(int siteId, String type, User user);
    boolean raiseUpdateNameRequest(String newName, User user);
}
