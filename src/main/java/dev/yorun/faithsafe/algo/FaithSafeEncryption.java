package dev.yorun.faithsafe.algo;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class FaithSafeEncryption {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int SALT_SIZE = 16;

    // Custom exceptions for specific errors
    public static class EncryptionException extends Exception {
        public EncryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class DecryptionException extends Exception {
        public DecryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // Generate a key from the password
    private SecretKeySpec getKeyFromPassword(String password, byte[] salt) throws EncryptionException {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
            byte[] secretKey = factory.generateSecret(spec).getEncoded();
            return new SecretKeySpec(secretKey, "AES");
        } catch (Exception e) {
            throw new EncryptionException("Error generating key from password", e);
        }
    }

    // Method to encrypt byte data
    public byte[] encrypt(byte[] data, String password) throws EncryptionException {
        try {
            SecureRandom random = new SecureRandom();

            // Generate salt
            byte[] salt = new byte[SALT_SIZE];
            random.nextBytes(salt);

            // Generate IV
            byte[] iv = new byte[IV_SIZE];
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Get key from password and salt
            SecretKeySpec key = getKeyFromPassword(password, salt);

            // Encrypt the data
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] encryptedData = cipher.doFinal(data);

            // Combine salt, IV, and encrypted data
            byte[] encryptedDataWithSaltAndIv = new byte[SALT_SIZE + IV_SIZE + encryptedData.length];
            System.arraycopy(salt, 0, encryptedDataWithSaltAndIv, 0, SALT_SIZE);
            System.arraycopy(iv, 0, encryptedDataWithSaltAndIv, salt.length, IV_SIZE);
            System.arraycopy(encryptedData, 0, encryptedDataWithSaltAndIv, SALT_SIZE + IV_SIZE, encryptedData.length);

            return encryptedDataWithSaltAndIv;
        } catch (Exception e) {
            throw new EncryptionException("Error encrypting data", e);
        }
    }

    // Method to decrypt byte data
    public byte[] decrypt(byte[] encryptedDataWithSaltAndIv, String password) throws DecryptionException {
        try {
            // Extract salt and IV
            byte[] salt = new byte[SALT_SIZE];
            byte[] iv = new byte[IV_SIZE];
            byte[] encryptedData = new byte[encryptedDataWithSaltAndIv.length - SALT_SIZE - IV_SIZE];

            System.arraycopy(encryptedDataWithSaltAndIv, 0, salt, 0, SALT_SIZE);
            System.arraycopy(encryptedDataWithSaltAndIv, salt.length, iv, 0, IV_SIZE);
            System.arraycopy(encryptedDataWithSaltAndIv, SALT_SIZE + IV_SIZE, encryptedData, 0, encryptedData.length);

            // Get key from password and salt
            SecretKeySpec key = getKeyFromPassword(password, salt);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Decrypt the data
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            throw new DecryptionException("Error decrypting data", e);
        }
    }

    // Overloaded method to encrypt string data
    public String encrypt(String data, String password) throws EncryptionException {
        try {
            byte[] encryptedData = encrypt(data.getBytes(StandardCharsets.UTF_8), password);
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new EncryptionException("Error encrypting string data", e);
        }
    }

    // Overloaded method to decrypt string data
    public String decrypt(String encryptedDataWithSaltAndIv, String password) throws DecryptionException {
        try {
            byte[] decodedData = Base64.getDecoder().decode(encryptedDataWithSaltAndIv);
            byte[] decryptedData = decrypt(decodedData, password);
            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptionException("Error decrypting string data", e);
        }
    }
}
