package application.chatapp_dialog.dal;

import application.chatapp_dialog.dto.AdminGroupInformation;
import application.chatapp_dialog.dto.AdminNewUserAccount;
import application.chatapp_dialog.dto.AdminUserAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminNewUserAccountDAL {
    public static final String query = "select username, email, create_date from user_accounts";
    private static final String queryInYear = "select username, email, create_date from user_accounts where EXTRACT(year from user_accounts.create_date) = ?";
    public static AdminNewUserAccount createObject(ResultSet rs) throws SQLException {
        AdminNewUserAccount obj = new AdminNewUserAccount();
        obj.setUsername(rs.getString("username"));
        obj.setEmail(rs.getString("email"));
        obj.setCreateDate(rs.getTimestamp("create_date"));
        return  obj;
    }
    public static List<AdminNewUserAccount> getAllNewAccount() throws SQLException {
        List<AdminNewUserAccount> list = new ArrayList<>();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null){
            try(PreparedStatement ps = conn.prepareStatement(query)){
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    list.add(createObject(rs));
                }
            }
        } else throw new SQLException("Database refuse connection.");
        return list;
    }
    public static List<AdminNewUserAccount> getNewAccountInYear(int year) throws SQLException {
        List<AdminNewUserAccount> list = new ArrayList<>();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null){
            try(PreparedStatement ps = conn.prepareStatement(queryInYear)){
                ps.setInt(1, year);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    list.add(createObject(rs));
                }
            }
        } else throw new SQLException("Database refuse connection.");
        return list;
    }

    public static List<Integer> getDifferentYearWithNewAccount() throws SQLException {
        List<Integer> list = new ArrayList<>();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null){
            try(PreparedStatement ps = conn.prepareStatement("select distinct EXTRACT(year from user_accounts.create_date) as years from user_accounts")){

                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    list.add(rs.getInt("years"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else throw new SQLException("Database refuse connection.");
        return list;
    }
    public static Comparator<AdminNewUserAccount> getDateAscendingComparator(){
        return new Comparator<AdminNewUserAccount>() {
            @Override
            public int compare(AdminNewUserAccount o1, AdminNewUserAccount o2) {
                return o1.getCreateDate().compareTo(o2.getCreateDate());
            }
        };
    }
    public static Comparator<AdminNewUserAccount> getDateDescendingComparator(){
        return new Comparator<AdminNewUserAccount>() {
            @Override
            public int compare(AdminNewUserAccount o1, AdminNewUserAccount o2) {
                return o2.getCreateDate().compareTo(o1.getCreateDate());
            }
        };
    }
    public static Comparator<AdminNewUserAccount> getUsernameAscending(){
        return new Comparator<AdminNewUserAccount>() {
            @Override
            public int compare(AdminNewUserAccount o1, AdminNewUserAccount o2) {
                return o1.getUsername().compareTo(o2.getUsername());
            }
        };
    }
    public static Comparator<AdminNewUserAccount> getUsernameDescending(){
        return new Comparator<AdminNewUserAccount>() {
            @Override
            public int compare(AdminNewUserAccount o1, AdminNewUserAccount o2) {
                return o2.getUsername().compareTo(o1.getUsername());
            }
        };
    }
    public static Comparator<AdminNewUserAccount> getEmailAscending(){
        return new Comparator<AdminNewUserAccount>() {
            @Override
            public int compare(AdminNewUserAccount o1, AdminNewUserAccount o2) {
                return o1.getEmail().compareTo(o2.getEmail());
            }
        };
    }
    public static Comparator<AdminNewUserAccount> getEmailDescending(){
        return new Comparator<AdminNewUserAccount>() {
            @Override
            public int compare(AdminNewUserAccount o1, AdminNewUserAccount o2) {
                return o2.getEmail().compareTo(o1.getEmail());
            }
        };
    }
}
