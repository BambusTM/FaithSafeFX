package dev.yorun.faithsafe.service;

public class DataObject {
    private int id;
    private String username;
    private String domain;
    private String email;
    private String password;
    private String description;

    public DataObject() {
    }

    public DataObject(int id, String username, String domain, String email, String password, String description) {
        this.id = id;
        this.username = username;
        this.domain = domain;
        this.email = email;
        this.password = password;
        this.description = description;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
