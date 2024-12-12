package application.chatapp_dialog.dal;

import application.chatapp_dialog.dto.AdminFriendOfUser;
import application.chatapp_dialog.dto.AdminUserActivityLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class AdminActivityLogDAL {
    public static AdminUserActivityLog createUserActivityLog(ResultSet rs){
        AdminUserActivityLog log = null;
        return null;
    }
    public static List<AdminUserActivityLog> getAllUserActivityLog() throws SQLException {
        Connection conn = UtilityDAL.getConnection();
        String query = "";
        PreparedStatement ps = null;
        List<AdminUserActivityLog> activityLogs = null;
        if (conn != null){

        }else throw new SQLException("Cannot connect to database");

        return null;
    }
}
