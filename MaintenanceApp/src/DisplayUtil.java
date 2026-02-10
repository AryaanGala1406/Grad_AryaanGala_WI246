import java.time.format.DateTimeFormatter;
import java.util.List;

public final class DisplayUtil {

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private DisplayUtil() {}

    // ------------------ SITES ------------------
    public static void displaySites(List<Site> sites) {

        System.out.println(
            "+---------+-------------------+---------+---------+---------+-----------+-----------------+---------------+");
        System.out.printf("| %-7s | %-17s | %-7s | %-7s | %-7s | %-9s | %-15s | %-13s |\n",
                "Site ID", "Type", "Length", "Width", "Area",
                "Owner ID", "Owner Name", "Maintenance");
        System.out.println(
            "+---------+-------------------+---------+---------+---------+-----------+-----------------+---------------+");

        for (Site s : sites) {
            System.out.printf("| %-7d | %-17s | %-7d | %-7d | %-7d | %-9d | %-15s | %-13d |\n",
                    s.getSiteId(),
                    s.getType(),
                    s.getLength(),
                    s.getWidth(),
                    s.getArea(),
                    s.getOwnerId(),
                    s.getOwnerName() == null ? "N/A" : s.getOwnerName(),
                    s.calculateMaintenance());
        }

        System.out.println(
            "+---------+-------------------+---------+---------+---------+-----------+-----------------+---------------+");
    }

    // ------------------ DETAILS REQUESTS ------------------
    public static void displayDetailsRequests(List<DetailsRequest> requests) {

        System.out.println(
            "+---------+-------------+---------+-------------+----------------------+----------------------+");
        System.out.printf("| %-7s | %-11s | %-7s | %-11s | %-20s | %-20s |\n",
                "Req ID", "Req Type", "User ID", "Status",
                "New Name", "Requested On");
        System.out.println(
            "+---------+-------------+---------+-------------+----------------------+----------------------+");

        for (DetailsRequest r : requests) {
            String time = r.getRequestedOn() == null
                    ? "N/A"
                    : r.getRequestedOn().format(DT_FMT);

            System.out.printf("| %-7d | %-11s | %-7d | %-11s | %-20s | %-20s |\n",
                    r.getReqId(),
                    r.getReqType(),
                    r.getUserId(),
                    r.getStatus(),
                    r.getNewName(),
                    time);
        }

        System.out.println(
            "+---------+-------------+---------+-------------+----------------------+----------------------+");
    }

    // ------------------ SITE REQUESTS ------------------
    public static void displaySiteRequests(List<SiteRequest> requests) {

        System.out.println(
            "+---------+-------------+---------+---------+-------------+-----------------+-----------------+----------------------+");
        System.out.printf("| %-7s | %-11s | %-7s | %-7s | %-11s | %-15s | %-15s | %-20s |\n",
                "Req ID", "Req Type", "User ID", "Site ID", "Status",
                "New Owner", "New Site Type", "Requested On");
        System.out.println(
            "+---------+-------------+---------+---------+-------------+-----------------+-----------------+----------------------+");

        for (SiteRequest r : requests) {
            String time = r.getRequestedOn() == null
                    ? "N/A"
                    : r.getRequestedOn().format(DT_FMT);

            System.out.printf("| %-7d | %-11s | %-7d | %-7s | %-11s | %-15s | %-15s | %-20s |\n",
                    r.getReqId(),
                    r.getReqType(),
                    r.getUserId(),
                    r.getSiteId() == null ? "N/A" : r.getSiteId(),
                    r.getStatus(),
                    r.getNewOwnerName(),
                    r.getNewSiteType(),
                    time);
        }

        System.out.println(
            "+---------+-------------+---------+---------+-------------+-----------------+-----------------+----------------------+");
    }
}
