package application.chatapp_dialog.dal;

import application.chatapp_dialog.dto.AdminUserAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
