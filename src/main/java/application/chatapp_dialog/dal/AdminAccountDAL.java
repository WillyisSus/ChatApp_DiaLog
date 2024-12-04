package application.chatapp_dialog.dal;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.dto.AdminAccount;

import application.chatapp_dialog.security.EncryptPassword;

public class AdminAccountDAL {
    public static void createNewAdmin(String username, String password, String email, String displayName){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null){
            try {
                String query = "insert into admin_accounts (username, password, email, displayname, salt, create_date) " +
                        "values (? , ?, ?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                String salt = EncryptPassword.generateRandomSalt();
                String hashedPassword = EncryptPassword.hashPassword(password,salt);
                LocalDateTime createDate = LocalDateTime.now();
                Timestamp ts = Timestamp.valueOf(createDate.withNano(0));
                ps.setString(1, username);
                ps.setString(2, hashedPassword);
                ps.setString(3, email);
                ps.setString(4, displayName);
                ps.setString(5, salt);
                ps.setTimestamp(6, ts);
                int row = ps.executeUpdate();
                System.out.println(ps.toString());
                System.out.println(row + " added");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("Cant connect to database");
        }
    }
}
