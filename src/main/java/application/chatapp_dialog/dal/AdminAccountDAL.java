package application.chatapp_dialog.dal;

import java.security.PrivilegedAction;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.dto.AdminAccount;

import application.chatapp_dialog.dto.AdminUserAccount;
import application.chatapp_dialog.security.EncryptPassword;

public class AdminAccountDAL {
    private static String getSaltQuery = "select salt from admin_accounts where username = ? or email = ?";


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

    /**
     * Return a String object, "Success" or "Wrong username or password!!!" for authentication process, or Exception message
     */

    public static String authenticate(String username, String password){
        Connection conn = UtilityDAL.getConnection();
        PreparedStatement ps = null;
        String res = null;
        try {
            ps = conn.prepareStatement(getSaltQuery);
            if (username == null || password == null){
                throw new NullPointerException("Username or password is not provided");
            }
            ps.setString(1, username);
            ps.setString(2, username);
            ResultSet  rs = ps.executeQuery();
            if (rs.next()){
                String salt =  rs.getString("salt");
                String hashedPass = EncryptPassword.hashPassword(password, salt);
                ps = conn.prepareStatement("select * from admin_accounts where password = ?");
                ps.setString(1, hashedPass);
                rs = ps.executeQuery();
                if (rs.next()){
                    String id = rs.getString("id");
                    String displayName = rs.getString("displayname");
                    System.out.println(id + " " + displayName);
                    res = "Success";
                }else{
                    res = "Wrong username or password";
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            return e.getMessage();
        }
        return res;
    }


}
