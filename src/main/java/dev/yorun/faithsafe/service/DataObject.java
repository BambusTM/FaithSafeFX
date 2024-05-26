package dev.yorun.faithsafe.service;

public class DataObject {
    private String username;
    private String domain;
    private String email;
    private String password;
    private String description;

    public DataObject(String username, String domain, String email, String password, String description) {
        this.username = username;
        this.domain = domain;
        this.email = email;
        this.password = password;
        this.description = description;
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

    public String getDescription() {
        return description;
    }
}
