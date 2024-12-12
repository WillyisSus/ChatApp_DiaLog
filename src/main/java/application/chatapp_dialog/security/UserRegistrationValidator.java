package application.chatapp_dialog.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistrationValidator {
    private static final String regexPassword = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%]).+$";
    private static final String regexEmail = "^([\\w\\-\\.]+)[a-zA-Z0-9]+@([\\w\\-]+\\.)+([a-zA-Z]{2,})$";
    private static final String regexUsername = "^(?!admin)[a-zA-Z0-9]*$";
    public static boolean validatePassword(String password){
        if (password.trim().length() < 8){
            return false;
        }
        System.out.println(password);
        Pattern pattern = Pattern.compile(regexPassword, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return  matcher.find();
    }

    public static boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(regexEmail, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        System.out.println(email);

        return  matcher.find();
    }

    public static boolean validateUsername(String username){
        Pattern pattern = Pattern.compile(regexUsername, Pattern.CASE_INSENSITIVE);
        System.out.println(username);
        Matcher matcher = pattern.matcher(username);
        return  matcher.find();
    }

    public static String getRegexEmail() {
        return regexEmail;
    }

    public static String getRegexPassword() {
        return regexPassword;
    }

    public static String getRegexUsername() {
        return regexUsername;
    }

}
