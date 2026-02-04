import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdminHelper {
    static SiteDAO dao = new SiteDBOperations();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static void viewAllSites() {
        ArrayList<Site> list = dao.getAllSites();
        if (list.isEmpty()) {
            System.out.println("No sites available.");
            return;
        }
        for (Site s : list) {
            s.displaySite();
        }
    }

    static void viewPendingMaintenance() {

        ArrayList<Site> list = dao.getPendingMaintenance();

        if (list.isEmpty()) {
            System.out.println("No pending maintenance.");
            return;
        }

        for (Site s : list) {
            s.displaySite();
        }
    }

    private static void collectMaintenance() throws Exception {

        System.out.print("Enter Site ID: ");
        int siteId = Integer.parseInt(br.readLine());

        dao.collectMaintenance(siteId);
    }

    private static void viewUpdateRequests() {

        ArrayList<UpdateRequest> list = dao.getPendingRequests();

        if (list.isEmpty()) {
            System.out.println("No pending update requests.");
            return;
        }

        System.out.println("\n--- Pending Requests ---");
        for (UpdateRequest ur : list) {
            System.out.println("Request ID : " + ur.reqId);
            System.out.println("Site ID    : " + ur.siteId);
            System.out.println("New Name   : " + ur.newName);
            System.out.println("Status     : " + ur.status);
            System.out.println("------------------------");
        }
    }

    private static void approveOrReject() throws Exception {

        System.out.print("Enter Request ID: ");
        int reqId = Integer.parseInt(br.readLine());

        System.out.print("Approve request? (yes/no): ");
        String choice = br.readLine();

        boolean approve = choice.equalsIgnoreCase("yes");

        if (dao.approveRequest(reqId, approve)) {
            System.out.println("Request processed successfully.");
        } else {
            System.out.println("Failed to process request.");
        }
    }

    
}
