package id.rumahawan.belajarfisika.Object;

public class User {
    private String email;
    private String name;
    private String password;
    private String level;

    public User() {
    }

    public User(String email, String name, String password, String level) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
