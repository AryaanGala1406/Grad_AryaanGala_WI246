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
                ch = Integer.parseInt(br.readLine());
                switch(ch) {
                    case 1 -> {
                        System.out.println("Enter username: ");
                        String name = br.readLine();

                        System.out.println("Enter password: ");
                        String password = br.readLine();

                        User user = AuthService.Login(name, password);
                        String role = user.getRole();
                        if(role != null) {
                            if(role == "admin") {
                                AdminMenu.start(user);
                            } else if(role == "owner") {
                                OwnerMenu.start(user);
                            } else {
                                System.out.println("Invalid role");
                            }
                            break;
                        }    
                        else {
                            System.out.println("Incorrect Username or Password");
                        }
                    }

                    case 2 -> {
                        System.out.println("Enter username: ");
                        String name = br.readLine();

                        System.out.println("Enter password: ");
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
