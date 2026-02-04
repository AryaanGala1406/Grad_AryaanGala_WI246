import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class OwnerHelper {
    static SiteDAO dao = new SiteDBOperations();
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    
    static void requestSite(int siteId, User owner) {
        dao.raiseUpdateRequest(siteId, owner);
    }

    static void viewMySite(int ownerId) {
        ArrayList<Site> sites = dao.getSiteByOwnerId(ownerId);

        if (sites == null) {
            System.out.println("No site mapped to this owner.");
            return;
        }

        System.out.println("\n--- My Site Details ---");
        for(Site site : sites) {
            site.displaySite();
        }
    }

    static void requestUpdateName(String newName, User user) {
        dao.raiseUpdateNameRequest(newName, user);
    }

    static void updateUserPassword(String newPassword, User user) {
        dao.updateUserPassword(newPassword, user);
    }

    static void updateSiteType(int siteId, String type, User user) {
        dao.raiseUpdateSiteTypeRequest(siteId, type, user);
    }
}
