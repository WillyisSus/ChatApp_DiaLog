package application.chatapp_dialog.security;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import javafx.beans.property.Property;

import java.util.Properties;

public class PasswordResetSender {
    private static final String APP_EMAIL = "willyonepoint2@gmail.com";
    private static final String APP_PASSWORD = "icpo gknp uolo bwah";

    public static Session getEmailSession(){
        return Session.getInstance(getEmailProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(APP_EMAIL, APP_PASSWORD);
            }
        });
    }

    public static Properties getEmailProperties(){
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return props;
    }

    public static String getAppEmail(){
        return APP_EMAIL;
    }

}
