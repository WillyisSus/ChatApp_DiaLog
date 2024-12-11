package application.chatapp_dialog.dal;

import application.chatapp_dialog.dto.AdminUserAccount;
import application.chatapp_dialog.dto.AdminFriendOfUser;
import application.chatapp_dialog.security.EncryptPassword;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminUserAccountDAL {
    public static AdminUserAccount createAdminUserAccount(ResultSet rs){
        AdminUserAccount ua = new AdminUserAccount();
        try {
            ua.setAddress(rs.getString("Address"));
            ua.setDob(rs.getDate("DOB"));
            ua.setCreateDate(rs.getTimestamp("CreateDate"));
            ua.setId(rs.getInt("UserID"));
            ua.setUsername(rs.getString("Username"));
            ua.setEmail(rs.getString("Email"));
            ua.setDisplayName(rs.getString("DisplayName"));
            ua.setStatus(rs.getString("Status"));
            ua.setSex(rs.getBoolean("Sex"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ua;
    }

    public static AdminFriendOfUser createAdminFriendOfUser(ResultSet rs){
        AdminFriendOfUser friend =  new AdminFriendOfUser();
        try {
            friend.setId(rs.getInt("friendID"));
            friend.setUsername(rs.getString("username"));
            friend.setDisplayName(rs.getString("displayName"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friend;
    }
    public static List<AdminUserAccount> getAllUserAccountsWithInfomation(){
        Connection conn = UtilityDAL.getConnection();

        String query = "select user_accounts.id as UserID, user_accounts.username as Username, user_accounts.email as Email, user_accounts.status as Status, user_accounts.create_date as CreateDate," +
                "user_account_info.displayname as DisplayName, user_account_info.dob as DOB, user_account_info.sex as Sex, user_account_info.address as Address " +
                "from user_accounts " +
                "join user_account_info on user_accounts.id = user_account_info.account_id";
        List<AdminUserAccount> userAccounts = new ArrayList<>();
        if (conn != null){
            try (PreparedStatement ps = conn.prepareStatement(query)){
                ResultSet userList = ps.executeQuery();
                while(userList.next()){

                    userAccounts.add(createAdminUserAccount(userList));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return userAccounts;
    }
    public static List<AdminFriendOfUser> getFriendOfUser(int userID){
        Connection conn = UtilityDAL.getConnection();

        String query = "select friend1.accept_id as friendID, useracc1.username as username, userinfo1.displayname as displayName " +
                "from friendships as friend1 " +
                "join user_accounts as useracc1 on friend1.accept_id =  useracc1.id " +
                "join user_account_info as userinfo1 on userinfo1.account_id = friend1.accept_id " +
                "where friend1.request_id = ? " +
                "union " +
                "select friend1.request_id, useracc1.username, userinfo1.displayname " +
                "from friendships as friend1 " +
                "join user_accounts as useracc1 on friend1.request_id =  useracc1.id " +
                "join user_account_info as userinfo1 on userinfo1.account_id = friend1.request_id " +
                "where friend1.accept_id = ?";
        List<AdminFriendOfUser> userAccounts = new ArrayList<>();
        if (conn != null){
            try (PreparedStatement ps = conn.prepareStatement(query)){
                ps.setInt(1, userID);
                ps.setInt(2, userID);
                ResultSet userList = ps.executeQuery();
                while(userList.next()){
                    userAccounts.add(createAdminFriendOfUser(userList));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return userAccounts;
    }

    public static boolean createNewUser(String username, String password, String email, String displayName, Boolean sex, String address, Date dob){
        String insertQuery = "insert into user_accounts (username, password, email, salt) values (?,?,?,?)";
        String inserUserAccountInfo = "insert into user_account_info (account_id, displayname, dob, sex, address) values (?,?,?,?,?)";
        PreparedStatement ps = null;
        try(Connection conn = UtilityDAL.getConnection()){
            String salt = EncryptPassword.generateRandomSalt();
            String hashedPassword = EncryptPassword.hashPassword(password, salt);
            ps = conn.prepareStatement(insertQuery);
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.setString(4, salt);
            int row = ps.executeUpdate();
            if (row > 0){
                ps = conn.prepareStatement("select id from user_accounts where username = ?");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                int user_id = -1;
                while (rs.next()){
                    user_id = rs.getInt("id");
                }
                if (user_id != -1 ){
                    ps = conn.prepareStatement(inserUserAccountInfo);
                    ps.setInt(1, user_id);
                    ps.setString(2, displayName);
                    ps.setDate(3, dob);
                    ps.setBoolean(4, sex);
                    ps.setString(5, address);
                    row = ps.executeUpdate();
                    if (row > 0){
                        return true;
                    }
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static boolean deleteUser(int userID) throws SQLException {
        String query = "delete from user_accounts where id = ?";
        PreparedStatement ps = null;
        Connection conn = UtilityDAL.getConnection();
        ps = conn.prepareStatement(query);
        ps.setInt(1, userID);
        int row =  ps.executeUpdate();
        return row > 0;
    }

    public static boolean updateUserStatus(int userID, String status) throws SQLException{
        String query = "update user_accounts set user_accounts.status = ? where user_accounts.id = ?";
        PreparedStatement ps = null;
        Connection conn = UtilityDAL.getConnection();
        ps = conn.prepareStatement(query);
        ps.setString(1, status);
        ps.setInt(2, userID);
        int row = ps.executeUpdate();
        return row > 0;
    }

    public static boolean updatePassword(int userID, String password) throws SQLException{
        String query = "update user_accounts set password = ?, salt = ? where id = ?";
        Connection conn = UtilityDAL.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps = conn.prepareStatement(query);

        String salt = EncryptPassword.generateRandomSalt();
        String hashedPass = EncryptPassword.hashPassword(password, salt);
        ps.setString(1, hashedPass);
        ps.setString(2, salt);
        ps.setInt(3, userID);
        int row = ps.executeUpdate();
        return row > 0;
    }

    public static boolean updateUserAccount(AdminUserAccount account) throws SQLException{
        String query = "update user_accounts set username = ?, email = ? where id = ?";
        String infoQuery = "update user_account_info set displayname = ?, dob = ?, sex = ?, address = ? where account_id = ?";
        Connection conn = UtilityDAL.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getEmail());
        ps.setInt(3, Integer.parseInt(account.getId()));
        int row =  ps.executeUpdate();
        if (row > 0){
            ps = conn.prepareStatement(infoQuery);
            ps.setString(1, account.getDisplayName());
            ps.setDate(2, Date.valueOf(account.getCreateDate()));
            ps.setBoolean(3, Boolean.parseBoolean(account.getSex()));
            ps.setString(4, account.getAddress());
            ps.setInt(3, Integer.parseInt(account.getId()));
            row = ps.executeUpdate();
        }else {
            return false;
        }
        return row > 0;
    }

}