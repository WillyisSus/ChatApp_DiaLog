package application.chatapp_dialog.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistrationValidator {
    private static final String regexUsername = "^(?!admin)[a-zA-Z0-9]{6,32}$";
    private static final String regexEmail = "^(?!.*[\\.]{2,})(?!.*\\.@)[\\w\\-][\\w\\-\\.]{0,63}@((?!\\-)[a-zA-Z0-9\\-]{0,62}[a-zA-Z0-9][\\.]){0,3}((?=.*[a-zA-z\\-])(?!\\-)[a-zA-Z0-9\\-]{0,62}[a-zA-Z0-9])$";
    private static final String regexPassword = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%\\.])[a-zA-Z0-9!@#$%\\.]{8,32}$";

    public static boolean validateUsername(String username){
        Pattern pattern = Pattern.compile(regexUsername, Pattern.CASE_INSENSITIVE);
        System.out.println(username);
        Matcher matcher = pattern.matcher(username);
        return  matcher.find();
    }
    public static boolean validateEmail(String email){
        Pattern pattern = Pattern.compile(regexEmail, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        System.out.println(email);
        return  matcher.find();
    }
    public static boolean validatePassword(String password){
        System.out.println(password);
        Pattern pattern = Pattern.compile(regexPassword, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        return  matcher.find();
    }

    public static String getRegexUsername() {
        return regexUsername;
    }
    public static String getRegexPassword() {
        return regexPassword;
    }
    public static String getRegexEmail() {
        return regexEmail;
    }

    public static void main(String[] args) {
        System.out.println(validateEmail("-___@1-2"));
    }
}
