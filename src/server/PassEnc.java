package server;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class PassEnc {

    public List<String> createPassword(String pass) {
        String saltvalue = PassBasedEnc.getSaltvalue(30);
        String encryptedpassword = PassBasedEnc.generateSecurePassword(pass, saltvalue);
        return Arrays.asList(encryptedpassword, saltvalue);
    }
}

class PassBasedEnc {
    /* Declaration of variables */
    private static final Random random = new SecureRandom();
    private static final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int iterations = 10000;
    private static final int keylength = 256;

    /* Method to generate the salt value. */
    public static String getSaltvalue(int length) {
        StringBuilder finalval = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new String(finalval);
    }

    /* Method to generate the hash value */
    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    /* Method to encrypt the password using the original password and salt value. */
    public static String generateSecurePassword(String password, String salt) {

        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

        return Base64.getEncoder().encodeToString(securePassword);
    }

    /* Method to verify if both password matches or not */

    public static boolean verifyUserPassword(String providedPassword,
                                             String securedPassword, String salt) {


        //Generate New secure password with the same salt /
        String newSecurePassword = generateSecurePassword(providedPassword, salt);
        //System.out.println("newSecurePassword: " + newSecurePassword);

        //Check if two passwords are equal /
        return newSecurePassword.equalsIgnoreCase(securedPassword);

    }

}