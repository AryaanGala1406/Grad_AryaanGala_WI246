public class User {
    private int userId;
    private String name;
    private String password;
    private String role;

    public User(int userId, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    // getters and setters
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}