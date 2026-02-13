import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AdminHelper {
    static AdminDAO dao = new AdminDBOperations();
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static void viewAllSites() {
        ArrayList<Site> list = dao.getAllSites();
        if (list.isEmpty()) {
            System.out.println("No sites available.");
            return;
        }
        DisplayUtil.displaySites(list);
    }

    // View Sites by passing occupancy status
    static void viewSites(boolean occupied) {
        ArrayList<Site> list = dao.getSitesByOccupiedStatus(occupied);
        if (list.isEmpty()) {
            System.out.println("No sites available.");
            return;
        }

        System.out.println(occupied
                ? "List of occupied sites are:"
                : "List of non-occupied sites are:");

        DisplayUtil.displaySites(list);
    }

    static void viewPendingRequest(String reqTypeInput) {
        ReqType reqType;
        try {
            reqType = ReqType.fromDb(reqTypeInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid request type. Use DETAILS or SITE.");
            return;
        }

        ArrayList<Request> list = dao.getPendingRequest(reqType.name());

        if (list.isEmpty()) {
            System.out.println("No pending requests available.");
            return;
        }

        switch (reqType) {
            case DETAILS -> {
                ArrayList<DetailsRequest> detailsList = new ArrayList<>();
                for (Request r : list) {
                    if (r instanceof DetailsRequest d) {
                        detailsList.add(d);
                    }
                }
                DisplayUtil.displayDetailsRequests(detailsList);
            }

            case SITE -> {
                ArrayList<SiteRequest> siteList = new ArrayList<>();
                for (Request r : list) {
                    if (r instanceof SiteRequest s) {
                        siteList.add(s);
                    }
                }
                DisplayUtil.displaySiteRequests(siteList);
            }
        }
    }

    static boolean updateStatus(String reqType) {
        try {
            System.out.print("Which request id status do you want to update: ");
            int reqId = Integer.parseInt(br.readLine());

            Request r = dao.getRequestById(reqId);

            if (r == null) {
                System.out.println("Request not found.");
                return false;
            }

            if (r instanceof DetailsRequest d) {
                DisplayUtil.displayDetailsRequests(List.of(d));
            } else if (r instanceof SiteRequest s) {
                DisplayUtil.displaySiteRequests(List.of(s));
            }

            System.out.print("Do you want to approve the request (Yes/No) ");
            boolean approve = br.readLine().trim().equalsIgnoreCase("YES");

            if (dao.updateRequestStatus(reqId, approve, ReqType.fromDb(reqType).name()))
                return true;

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    static boolean assignSiteToUser() {
        try {
            System.out.print("Enter the site id: ");
            int siteId = Integer.parseInt(br.readLine());
            System.out.print("Enter the user id: ");
            int userId = Integer.parseInt(br.readLine());
            if (dao.assignSiteByUserId(siteId, userId)) {
                if (dao.assignMaintenance(siteId, userId)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    static boolean updateSiteType() {
        try {
            System.out.print("Enter the site id: ");
            int siteId = Integer.parseInt(br.readLine());
            System.out.print("Enter the site type: ");
            String type = br.readLine();
            if (dao.updateSiteType(siteId, SiteType.fromDb(type).name()))
                return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    static boolean removeOwner() {
        try {
            System.out.print("Enter the site id: ");
            int siteId = Integer.parseInt(br.readLine());
            Site s = dao.getSiteBySiteId(siteId);

            if (s == null) {
                System.out.println("Site not found");
            } else {
                DisplayUtil.displaySites(List.of(s));
            }

            System.out.print("Do you really want to remove this site owner ? (Yes/No) ");
            String res = br.readLine();
            if (res.toUpperCase().equals("YES")) {
                if (dao.removeOwnerBySiteId(siteId))
                    return true;
            } else {
                System.out.println("Owner was not removed.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    static void viewPendingMaintenance() {
        try {
            System.out.print("Enter the site id: ");
            int siteId = Integer.parseInt(br.readLine());
            int amount = dao.getPendingMaintenanceBySiteId(siteId);
            System.out.println("Maintenance Pending for " + siteId + " is " + amount);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void printTransactions() {
        List<MaintenanceTransaction> txns = dao.getAllTransactions();
        if (txns.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            DisplayUtil.displayTransactions(txns);
        }
    }
}
