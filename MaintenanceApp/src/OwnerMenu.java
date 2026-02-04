import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OwnerMenu {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void start(User owner) {
        try {
            int ch;
            int userId = owner.getUserId();
            do {
                System.out.println("""
                        
                        -------- SITE OWNER MENU --------
                        1. Request Site Ownership
                        2. View My Site Details
                        3. Request Update (Owner Name)
                        4. Update Password (Owner Password)
                        5. Request Site Type Update
                        6. Logout
                        --------------------------------
                        Enter choice:
                        """);

                ch = Integer.parseInt(br.readLine());

                switch (ch) {

                    case 1 -> {
                        System.out.println("Enter Site id: ");
                        int siteId = Integer.parseInt(br.readLine());
                        OwnerHelper.requestSite(siteId, owner);
                    }

                    case 2 -> OwnerHelper.viewMySite(userId);

                    case 3 -> {
                        System.out.println("Enter the updated name: ");
                        String newName = br.readLine();
                        OwnerHelper.requestUpdateName(newName, owner);
                    }

                    case 4 -> {
                        System.out.println("Enter the updated password: ");
                        String newPassword = br.readLine();
                        OwnerHelper.updateUserPassword(newPassword, owner);
                    }

                    case 5 -> {
                        System.out.println("Enter the updated site type: ");
                        String type = br.readLine();
                        System.out.println("Enter Site id: ");
                        int siteId = Integer.parseInt(br.readLine());
                        OwnerHelper.updateSiteType(siteId, type, owner);
                    }

                    case 6 -> System.out.println("Owner logged out.");

                    default -> System.out.println("Invalid option!");
                }

            } while (ch != 5);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
