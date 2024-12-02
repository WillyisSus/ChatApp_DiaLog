package application.chatapp_dialog.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class EncryptPassword {
    /**
     * Only use generateRandomSalt() for creating a new account
     *
     *
     */
    public static String generateRandomSalt(){
        String charSet = "QWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*1234567890qwertyuiopasdfghjklzxcvbnm";
        int cslength = charSet.length();
        StringBuilder sb = new StringBuilder();
        Random rd = new Random();
        for (int i = 0; i < 6; i++){
            int index = rd.nextInt(cslength);
            sb.append(charSet.charAt(index));
        }
        return sb.toString();

    }
    /**
     * Generate hashed password from user password
     *
     *
     */
    public static String hashPassword(String password, String salt){
        String hashedPassword = null;
        try {
            if (password == null || salt == null){
                throw new NullPointerException("Password is null or salt is null");
            }
            String saltedPassword = password + salt;
            System.out.println("Original: " + password + ", salt: " + salt);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(saltedPassword.getBytes());
            BigInteger bigInt = new BigInteger(1, hashedBytes);
            hashedPassword = bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
        return hashedPassword;
    }
}
