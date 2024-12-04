package application.chatapp_dialog.dummy;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.sql.Timestamp;
public class UserAccountGenerator {
    private static String charSet =  "QWERTYUIOPASDFGHJKLZXCVBNM1234567890qwertyuiopasdfghjklzxcvbnm";
    private static String passwordSet =  "QWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*1234567890qwertyuiopasdfghjklzxcvbnm";
    private static boolean[] sex = {false, true};
    public static String randomUsername(){
        StringBuilder sb = new StringBuilder();
        int cslength = charSet.length();
        Random rd = new Random();
        for (int i = 0; i < 10; i++){
            int index = rd.nextInt(cslength);
            sb.append(charSet.charAt(index));
        }
        return sb.toString();
    }
    public static String randomPassword(){
        StringBuilder sb = new StringBuilder();
        int cslength = passwordSet.length();
        Random rd = new Random();
        for (int i = 0; i < 8; i++){
            int index = rd.nextInt(cslength);
            sb.append(passwordSet.charAt(index));
        }
        return sb.toString();
    }
    public static String randomEmail(){
        StringBuilder sb = new StringBuilder();
        int cslength = charSet.length();
        Random rd = new Random();
        for (int i = 0; i < 8; i++){
            int index = rd.nextInt(cslength);
            sb.append(charSet.charAt(index));
        }
        sb.append("@example.com");
        return sb.toString();
    }

    public static String randomDisplayname(){
        StringBuilder sb = new StringBuilder();
        int cslength = charSet.length();
        Random rd = new Random();
        for (int i = 0; i < 8; i++){
            int index = rd.nextInt(cslength);
            sb.append(charSet.charAt(index));
        }
        return sb.toString();
    }
    public static String randomAddress(){
        StringBuilder sb = new StringBuilder();
        int cslength = charSet.length();
        Random rd = new Random();
        for (int i = 0; i < 10; i++){
            int index = rd.nextInt(cslength);
            sb.append(charSet.charAt(index));
        }
        return sb.toString();
    }

    public static boolean randomSex() {
        Random rd = new Random();
        return sex[rd.nextInt(2)];
    }

    public static Timestamp randomCreateDate(){
        return Timestamp.valueOf(LocalDateTime.now().withNano(0).toString());
    }

    public static Date randomCreateDOB(){
        Random rd = new Random();
        return Date.valueOf(LocalDate.of(rd.nextInt(1900, 2024),rd.nextInt(1,13), rd.nextInt(1,29)));
    }

}
