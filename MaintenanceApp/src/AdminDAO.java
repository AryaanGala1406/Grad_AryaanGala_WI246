import java.util.ArrayList;
import java.util.List;

public interface AdminDAO {

    ArrayList<Site> getAllSites();
    Site getSiteBySiteId(int siteId);
    
    ArrayList<Site> getSitesByOccupiedStatus(boolean occupied);

    ArrayList<Request> getPendingRequest(String req_type);
    public boolean updateRequestStatus(int reqId, boolean approve, String reqType);


    boolean assignSiteByUserId(int siteId, int userId);
    boolean assignMaintenance(int siteId, int ownerId);
    boolean updateSiteType(int siteId, String type);
    boolean removeOwnerBySiteId(int siteId);
    int getPendingMaintenanceBySiteId(int siteId);
    List<MaintenanceTransaction> getAllTransactions();
    Request getRequestById(int reqId);
}

