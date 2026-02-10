import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int ch;
            do {
                System.out.println("""
                    
                        1. Login 
                        2. Signup
                        3. Exit
                        """);
                System.out.print("Enter your choice: ");
                ch = Integer.parseInt(br.readLine());
                switch(ch) {
                    case 1 -> {
                        System.out.print("Enter username: ");
                        String name = br.readLine();

                        System.out.print("Enter password: ");
                        String password = br.readLine();
                        try {
                            User user = AuthService.Login(name, password);
                            String role = user.getRole();
                            if(role != null) {
                                if(role.equalsIgnoreCase("admin")) {
                                    AdminMenu.start(user);
                                } else if(role.equalsIgnoreCase("owner")) {
                                    UserMenu.start(user);
                                } else {
                                    System.out.println("Invalid role");
                                }
                            }    
                        } catch(Exception e) {
                            System.out.println("Incorrect Username or Password");
                        }
                        break;
                    }

                    case 2 -> {
                        System.out.print("Enter username: ");
                        String name = br.readLine();

                        System.out.print("Enter password: ");
                        String password = br.readLine();

                        AuthService.createUser(name, password);
                    }

                    case 3 -> {
                        System.out.println("Exit");
                    }

                    default -> {
                        System.out.println("Wrong case");
                    }
                }
            } while(ch != 3);
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
