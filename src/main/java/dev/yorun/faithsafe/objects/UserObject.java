package dev.yorun.faithsafe.objects;

import dev.yorun.faithsafe.service.BasicJson;

public class UserObject extends BasicJson {
  private int id;
  private String username;
  private String password;

  // Default constructor DO NOT DELETE
  public UserObject() {
  }

  public UserObject(int id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
