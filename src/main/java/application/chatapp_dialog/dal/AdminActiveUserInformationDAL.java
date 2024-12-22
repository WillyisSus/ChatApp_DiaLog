package application.chatapp_dialog.dal;

import application.chatapp_dialog.dto.AdminActiveUserInformation;
import application.chatapp_dialog.dto. AdminActiveUserInformation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminActiveUserInformationDAL {
    public static String query = "select distinct user_accounts.id, user_accounts.username, user_accounts.email, user_accounts.create_date,  " +
            "count(distinct logs.session_start) as logins,  " +
            "count(distinct messages.create_date) filter (where box_chats.is_direct) as privates, " +
            "count(distinct messages.create_date) filter (where not box_chats.is_direct) as groupchats " +
            "from user_accounts " +
            "left join user_activity_logs as logs on logs.user_id = user_accounts.id " +
            "left join messages on messages.user_id = user_accounts.id " +
            "left join box_chats on box_chats.id  = messages.box_id " +
            "group by user_accounts.id, user_accounts.username, user_accounts.email, user_accounts.create_date";
    public static AdminActiveUserInformation createObject(ResultSet rs) throws SQLException {
        AdminActiveUserInformation obj =  new AdminActiveUserInformation();
        obj.setId(rs.getInt("id"));
        obj.setUsername(rs.getString("username"));
        obj.setEmail(rs.getString("email"));
        obj.setLogins(rs.getInt("logins"));
        obj.setGroupChats(rs.getInt("groupchats"));
        obj.setPrivateChats(rs.getInt("privates"));
        obj.setCreateDate(rs.getTimestamp("create_date"));
        return obj;
    }
    public static List<AdminActiveUserInformation> getAciveUserInformations(Connection conn) throws SQLException {
        List<AdminActiveUserInformation> list = new ArrayList<>();

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
    public static Comparator<AdminActiveUserInformation> getDateAscendingComparator(){
        return new Comparator<AdminActiveUserInformation>() {
            @Override
            public int compare(AdminActiveUserInformation o1, AdminActiveUserInformation o2) {
                return o1.getCreateDate().compareTo(o2.getCreateDate());
            }
        };
    }
    public static Comparator<AdminActiveUserInformation> getDateDescendingComparator(){
        return new Comparator< AdminActiveUserInformation>() {
            @Override
            public int compare( AdminActiveUserInformation o1,  AdminActiveUserInformation o2) {
                return o2.getCreateDate().compareTo(o1.getCreateDate());
            }
        };
    }
    public static Comparator< AdminActiveUserInformation> getUsernameAscendingComparator(){
        return new Comparator< AdminActiveUserInformation>() {
            @Override
            public int compare( AdminActiveUserInformation o1,  AdminActiveUserInformation o2) {
                return o1.getUsername().compareTo(o2.getUsername());
            }
        };
    }
    public static Comparator< AdminActiveUserInformation> getUsernameDescendingComparator(){
        return new Comparator< AdminActiveUserInformation>() {
            @Override
            public int compare( AdminActiveUserInformation o1,  AdminActiveUserInformation o2) {
                return o2.getUsername().compareTo(o1.getUsername());
            }
        };
    }
}
