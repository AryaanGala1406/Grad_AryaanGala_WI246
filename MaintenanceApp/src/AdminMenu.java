import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdminMenu {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static void start(User admin) {
        try {
            int ch;
            do {
                System.out.print("""

                        -------- ADMIN MENU --------
                        1. View All Sites
                        2. View Pending Maintenance
                        3. Collect Maintenance
                        4. View Update Requests
                        5. Approve / Reject Request
                        6. Logout
                        ----------------------------
                        Enter choice:
                        """);
                ch = Integer.parseInt(br.readLine());
                
                switch (ch) {

                    case 1 -> AdminHelper.viewAllSites();

                    case 2 -> AdminHelper.viewPendingMaintenance();

                    case 3 -> collectMaintenance();

                    case 4 -> viewUpdateRequests();

                    case 5 -> approveOrReject();

                    case 6 -> System.out.println("Admin logged out.");

                    default -> System.out.println("Invalid option!");
                }

            } while (ch != 6);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
