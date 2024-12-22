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
    private static String getSaltQuery = "select * from admin_accounts where username = ? or email = ?";


    public static void createNewAdmin(String username, String password, String email, String displayName, Connection conn){
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

    public static int authenticate(String username, String password , Connection conn){

        PreparedStatement ps = null;
        int res = -1;
        if (username.equals("adminghost") && password.equals("thebeliever")){
            return -1;
        }
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
                String id = rs.getString("id");
                String displayName = rs.getString("displayname");
                String hashedPass = EncryptPassword.hashPassword(password, salt);
                if (hashedPass.equals(rs.getString("password"))){
                    res = Integer.parseInt(id);
                    System.out.println(id + " " + displayName);
                }
            }
        } catch (SQLException e) {
            return -1;
        } catch (NullPointerException e){
            return -1;
        }
        return res;
    }




}
