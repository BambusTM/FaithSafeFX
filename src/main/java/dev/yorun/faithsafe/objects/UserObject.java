package dev.yorun.faithsafe.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.yorun.faithsafe.service.BasicJson;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

  @JsonIgnore
  public static String getHashedPassword(String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    byte[] keyData = password.getBytes();
    SecretKeySpec KS = new SecretKeySpec(keyData, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.ENCRYPT_MODE, KS);

    return new String(cipher.doFinal(password.getBytes()));
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
