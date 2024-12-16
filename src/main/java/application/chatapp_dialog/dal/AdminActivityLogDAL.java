package application.chatapp_dialog.dal;

import application.chatapp_dialog.dto.AdminFriendOfUser;
import application.chatapp_dialog.dto.AdminUserActivityLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminActivityLogDAL {
    public static AdminUserActivityLog createUserActivityLog(ResultSet rs) throws SQLException {
        AdminUserActivityLog log = new AdminUserActivityLog();
        log.setId(rs.getInt("userID"));
        log.setDisplayName(rs.getString("displayName"));
        log.setSessionStart(rs.getTimestamp("sessionStart"));
        log.setSessionEnd(rs.getTimestamp("sessionEnd"));
        log.setUsername(rs.getString("Username"));
        return log;
    }
    public static AdminUserActivityLog createActiveUser(ResultSet rs) throws SQLException {
        AdminUserActivityLog log = new AdminUserActivityLog();
        log.setId(rs.getInt("user_id"));
        log.setSessionStart(rs.getTimestamp("session_start"));
        return log;
    }
    public static List<AdminUserActivityLog> getAllUserActivityLog(Integer userID) throws SQLException {
        System.out.println("From DAL: " + userID);
        Connection conn = UtilityDAL.getConnection();
        String query = "select user_accounts.id as userID, user_accounts.username as Username, " +
                "user_account_info.displayname as displayName, user_activity_logs.session_start as sessionStart,  user_activity_logs.session_end as sessionEnd " +
                " from user_accounts" +
                " join user_account_info on user_accounts.id = user_account_info.account_id" +
                " join user_activity_logs on user_activity_logs.user_id = user_accounts.id" +
                (userID != null ? " where user_accounts.id = " + userID.toString():"");


        PreparedStatement ps = null;
        List<AdminUserActivityLog> activityLogs = new ArrayList<>();
        if (conn != null){
            ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                activityLogs.add(createUserActivityLog(rs));
            }
        }else throw new SQLException("Cannot connect to database");
        return activityLogs;
    }

    public static List<AdminUserActivityLog> getActiveUserInYear(int year) throws SQLException {
        Connection conn = UtilityDAL.getConnection();
        String query = "select distinct on (user_id, EXTRACT(month from session_start)) user_id, session_start from user_activity_logs where EXTRACT(year from user_activity_logs.session_start) = ?";
        PreparedStatement ps = null;
        List<AdminUserActivityLog> activityLogs = new ArrayList<>();
        if (conn != null){
            ps = conn.prepareStatement(query);
            ps.setInt(1, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                activityLogs.add(createActiveUser(rs));
            }
        }else throw new SQLException("Cannot connect to database");
        return activityLogs;
    }
    public static Comparator<AdminUserActivityLog> getAscendingComparator(){
        return new Comparator<AdminUserActivityLog>() {
            @Override
            public int compare(AdminUserActivityLog o1, AdminUserActivityLog o2) {
                Timestamp ts1 = o1.getSessionStartAsTimeStamp();
                Timestamp ts2 = o2.getSessionStartAsTimeStamp();
                return ts1.compareTo(ts2);
            }
        };
    }

    public static Comparator<AdminUserActivityLog> getDescendingComparator(){
        return new Comparator<AdminUserActivityLog>() {
            @Override
            public int compare(AdminUserActivityLog o1, AdminUserActivityLog o2) {
                Timestamp ts1 = o1.getSessionStartAsTimeStamp();
                Timestamp ts2 = o2.getSessionStartAsTimeStamp();
                return ts2.compareTo(ts1);
            }
        };
    }
}
