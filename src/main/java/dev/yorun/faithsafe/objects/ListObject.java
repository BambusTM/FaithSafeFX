package dev.yorun.faithsafe.objects;

public class ListObject {
    private int id;
    private String username;
    private String domain;
    private String email;
    private String password;

    public ListObject(int id, String username, String domain, String email, String password) {
        this.id = id;
        this.username = username;
        this.domain = domain;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDomain() {
        return domain;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username + " - " + domain + " - " + email;
    }
}
