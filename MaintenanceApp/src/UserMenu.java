import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserMenu {
    public static void start(User user) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            UserDAO dao = new UserDBOperations();
            int ch;
            do {
                System.out.println("""

                        -------- SITE OWNER MENU --------
                        1. Request Site Ownership
                        2. View My Site Details
                        3. Request Update (Owner Name)
                        4. Update Password (Owner Password)
                        5. Request Site Type Update
                        6. Pay Maintenance
                        7. View My Transactions
                        8. Logout
                        --------------------------------
                        """);
                System.out.print("Enter choice: ");
                ch = Integer.parseInt(br.readLine());

                switch (ch) {
                    case 1 -> {
                        System.out.print("Enter site id: ");
                        int siteId = Integer.parseInt(br.readLine());

                        Site s = dao.getSiteBySiteId(siteId);
                        if (s == null) {
                            System.out.println("Site not found");
                        } else {
                            DisplayUtil.displaySites(List.of(s));
                        }

                        System.out.print("Do you really want to request this site ? (Yes/No) ");
                        String res = br.readLine();

                        if (res.toUpperCase().equals("YES")) {
                            dao.raiseUpdateRequest(siteId, user);
                        } else {
                            System.out.println("Error in sending the site ownership request.");
                        }
                    }

                    case 2 -> {
                        ArrayList<Site> sites = dao.getSiteByOwnerId(user.getUserId());

                        if (sites == null || sites.isEmpty()) {
                            System.out.println("No site mapped to this owner.");
                            break;
                        }

                        System.out.println("\n--- My Site Details ---");
                        DisplayUtil.displaySites(sites);
                    }

                    case 3 -> {
                        System.out.print("Enter the updated name: ");
                        String newName = br.readLine();
                        dao.raiseUpdateNameRequest(newName, user);
                    }

                    case 4 -> {
                        System.out.print("Enter the updated password: ");
                        String newPassword = br.readLine();
                        dao.updateUserPassword(newPassword, user);
                    }

                    case 5 -> {
                        System.out.print("Enter Site id: ");
                        int siteId = Integer.parseInt(br.readLine());

                        if (dao.getSiteOwnerid(siteId) != user.getUserId()) {
                            System.out.println("Unauthorized request: site not owned by user");
                            break;
                        }
                        System.out.print("Enter the updated site type: ");
                        String type = br.readLine();

                        Site s = dao.getSiteBySiteId(siteId);
                        if (s == null) {
                            System.out.println("Site not found");
                        } else {
                            DisplayUtil.displaySites(List.of(s));
                        }

                        System.out.print("Do you really want to update this site ? (Yes/No) ");
                        String res = br.readLine();

                        if (res.toUpperCase().equals("YES")) {
                            if (dao.raiseUpdateSiteTypeRequest(siteId, SiteType.fromDb(type).toString(), user))
                                System.out.println("");
                        } else {
                            System.out.println("Error in sending the update request.");
                        }
                    }

                    case 6 -> {
                        System.out.print("Enter Site ID: ");
                        int siteId = Integer.parseInt(br.readLine());

                        if (dao.getSiteOwnerid(siteId) != user.getUserId()) {
                            System.out.println("Unauthorized access.");
                            break;
                        }

                        int balance = dao.getPendingMaintenance(siteId, user.getUserId());

                        if (balance == 0) {
                            System.out.println("No pending maintenance.");
                            break;
                        }

                        System.out.println("Remaining Amount: " + balance);

                        System.out.print("Enter amount to pay: ");
                        int amount = Integer.parseInt(br.readLine());

                        dao.payMaintenance(siteId, user.getUserId(), amount);
                    }
                    case 7 -> {
                        List<MaintenanceTransaction> list = dao.getMyTransactions(user.getUserId());

                        if (list.isEmpty()) {
                            System.out.println("No transactions found.");
                        } else {
                            DisplayUtil.displayTransactions(list);
                        }
                    }

                    case 8 -> System.out.println("Owner logged out.");

                    default -> System.out.println("Invalid option!");
                }

            } while (ch != 8);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
