package application.chatapp_dialog.dal;

import application.chatapp_dialog.security.PasswordResetSender;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailDAL {
    public static String query = "select * from user_accounts where email = ?";
    public static boolean sendNewPassword(String email, String password){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null){
            try(PreparedStatement ps = conn.prepareStatement(query)){
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (!rs.isBeforeFirst()){
                    return false;
                }
                while (rs.next()){
                    Message message = new MimeMessage(PasswordResetSender.getEmailSession());
                    message.setFrom(new InternetAddress(PasswordResetSender.getAppEmail()));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    message.setSubject("[ChatApp - DiaLog] Reset password - New Password!!!");
                    message.setText("Your new password is: " + password);
                    Transport.send(message);
                }
            } catch (SQLException | MessagingException e) {
                System.out.println(e.getMessage());
            }
        }
        return true;
    }

    public static boolean sendMessageToEmail(String email, String text){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null){
            try(PreparedStatement ps = conn.prepareStatement(query)){
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (!rs.isBeforeFirst()){
                    return false;
                }
                while (rs.next()){
                    Message message = new MimeMessage(PasswordResetSender.getEmailSession());
                    message.setFrom(new InternetAddress(PasswordResetSender.getAppEmail()));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    message.setSubject("[ChatApp - DiaLog] Announcement from Admin");
                    message.setText(text);
                    Transport.send(message);
                }
            } catch (SQLException | MessagingException e) {
                System.out.println(e.getMessage());
            }
        }
        return true;
    }
}
