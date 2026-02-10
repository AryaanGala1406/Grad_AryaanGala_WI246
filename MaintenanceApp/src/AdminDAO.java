import java.util.ArrayList;

public interface AdminDAO {

    ArrayList<Site> getAllSites();
    Site getSiteBySiteId(int siteId);
    
    ArrayList<Site> getSitesByOccupiedStatus(boolean occupied);

    ArrayList<Request> getPendingRequest(String req_type);
    public boolean updateRequestStatus(int reqId, boolean approve, String reqType);


    boolean assignSiteByUserId(int siteId, int userId);
    boolean updateSiteType(int siteId, String type);
    boolean removeOwnerBySiteId(int siteId);
    
    boolean collectMaintenance(int siteId);
    ArrayList<Site> pendingMaintenance(int year);

    Request getRequestById(int reqId);
}

