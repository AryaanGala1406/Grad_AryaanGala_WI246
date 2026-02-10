import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AdminMenu {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static void start(User admin) {
        try {
            int ch;
            do {
                System.out.print("""

                        -------- ADMIN MENU --------
                        1. View All Sites
                        2. View Occupied Sites
                        3. View Open Sites
                        4. View Pending Owner Detail Requests
                        5. Approve / Reject Owner Detail Requests
                        6. View Pending Site Requests
                        7. Approve / Reject Site Requests
                        8. Assign Site to Owner 
                        9. Update Site Type 
                        10. Remove Owner 
                        11. View Pending Maintenance
                        12. Collect Maintenance
                        13. Logout
                        ----------------------------
                        """);
                System.out.print("Enter choice: ");
                ch = Integer.parseInt(br.readLine());
                
                switch (ch) {
                    case 1 -> AdminHelper.viewAllSites();

                    case 2 -> AdminHelper.viewSites(true);

                    case 3 -> AdminHelper.viewSites(false);

                    case 4 -> {
                        System.out.println("List of User Requests are: ");
                        AdminHelper.viewPendingRequest(ReqType.DETAILS.name());
                    }

                    case 5 -> {
                        if(AdminHelper.updateStatus(ReqType.DETAILS.name())) {
                            System.out.println("Details Updated Successfully.");
                        } else {
                            System.out.println("Error in updating details.");
                        }
                    }

                    case 6 -> {
                        System.out.println("List of Site Requests are: ");
                        AdminHelper.viewPendingRequest(ReqType.SITE.name());
                    }

                    case 7 -> {
                        if(AdminHelper.updateStatus(ReqType.SITE.name())) {
                            System.out.println("Details Updated Successfully.");
                        } else {
                            System.out.println("Error in updating details.");
                        }
                    }

                    case 8 -> {
                        if(AdminHelper.assignSiteToUser()) {
                            System.out.println("Site assigned to owner.");
                        } else {
                            System.out.println("Error in assinging site to the owner.");
                        }
                    }

                    case 9 -> {
                         if(AdminHelper.updateSiteType()) {
                            System.out.println("Site type updated successfully.");
                        } else {
                            System.out.println("Error in updating site type.");
                        }
                    }
                    
                    case 10 -> {
                        if(AdminHelper.removeOwner()) {
                            System.out.println("Owner removed successfully.");
                        } else {
                            System.out.println("Error in removing owner.");
                        }
                    }

                    case 11 -> {}
                    case 12 -> {}

                    case 13 -> System.out.println("Logging out of the system.");
                    
                    default -> System.out.println("Invalid option!");
                }

            } while (ch != 13);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
