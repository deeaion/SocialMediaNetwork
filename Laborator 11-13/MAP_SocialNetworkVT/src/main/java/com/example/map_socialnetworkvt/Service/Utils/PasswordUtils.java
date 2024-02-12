package com.example.map_socialnetworkvt.Service.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    private static final int SALT_LENGTH = 16;

    // Hash a password using SHA-256 with a random salt
    public static String hashPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);

            byte[] hashedPassword = md.digest(password.getBytes());

            // Combine salt and hashed password into a single byte array
            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            // Encode the combined byte array to Base64
            return Base64.getEncoder().encodeToString(combined);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Verify a password against a hashed password
    public static boolean verifyPassword(String password, String hashedPassword) {
        try {
            if(password.equals(hashedPassword))
            {
                return true;
            }

            // Decode the combined Base64 string to a byte array
            byte[] combined = Base64.getDecoder().decode(hashedPassword);

            // Extract salt and hashed password from the combined byte array
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(combined, 0, salt, 0, SALT_LENGTH);

            byte[] storedHashedPassword = new byte[combined.length - SALT_LENGTH];
            System.arraycopy(combined, SALT_LENGTH, storedHashedPassword, 0, storedHashedPassword.length);

            // Hash the entered password with the retrieved salt
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedAttempt = md.digest(password.getBytes());

            // Compare the stored hashed password with the calculated hash
            return MessageDigest.isEqual(hashedAttempt, storedHashedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
}
