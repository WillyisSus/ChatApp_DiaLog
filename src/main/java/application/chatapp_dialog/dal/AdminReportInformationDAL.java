package application.chatapp_dialog.dal;

import application.chatapp_dialog.AdminReportController;
import application.chatapp_dialog.dto.AdminReportInformation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminReportInformationDAL {
    public static final String query = "select reports.reported_id, reports.reporter_id,  " +
            "ua1.username as reported_username, uai1.displayname as reported_displayname, " +
            "ua1.email,ua1.status,  ua2.username as reporter_username, reports.create_date " +
            "from reports " +
            "join user_accounts as ua1 on ua1.id = reports.reported_id " +
            "join user_account_info as uai1 on uai1.account_id = reports.reported_id " +
            "join user_accounts as ua2 on ua2.id = reports.reporter_id";
    public static final String lockUserQuery =  "update user_accounts set status = ? where id = ?";
    private static final String removeReportQuery = "delete from reports where reported_id = ? and reporter_id = ?";
    public static AdminReportInformation createObject(ResultSet rs) throws SQLException {
       AdminReportInformation obj = new AdminReportInformation();
       obj.setReportedID(rs.getInt("reported_id"));
       obj.setReportedEmail(rs.getString("email"));
       obj.setReportedUsername(rs.getString("reported_username"));
       obj.setCreateDate(rs.getTimestamp("create_date"));
       obj.setDisplayName(rs.getString("reported_displayname"));
       obj.setReporterID(rs.getInt("reporter_id"));
       obj.setReporterUsername(rs.getString("reporter_username"));
       obj.setReportedUserStatus(rs.getString("status"));
       return obj;
    }

    public static List<AdminReportInformation> getReportList() throws SQLException {
        List<AdminReportInformation> list = new ArrayList<>();
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

    public static boolean removeReport(AdminReportInformation report) throws SQLException {
        boolean success = true;
        Connection conn = UtilityDAL.getConnection();
        if (conn != null){
            try(PreparedStatement ps = conn.prepareStatement(removeReportQuery)){

                ps.setInt(1, report.getReportedID());
                ps.setInt(2, report.getReporterID());
                int row = ps.executeUpdate();
                if (row <= 0){
                    success = false;
                }
            }
        } else throw new SQLException("Database refuse connection.");

        return success;
    }

    public static boolean lockUser(AdminReportInformation report) throws SQLException {
        return AdminUserAccountDAL.updateUserStatus(report.getReportedID(), "locked");
    }

    public static Comparator<AdminReportInformation> getDateAscendingComparator(){
        return new Comparator<AdminReportInformation>() {
            @Override
            public int compare(AdminReportInformation o1, AdminReportInformation o2) {
                return o1.getCreateDate().compareTo(o2.getCreateDate());
            }
        };
    }
    public static Comparator<AdminReportInformation> getDateDescendingComparator(){
        return new Comparator<AdminReportInformation>() {
            @Override
            public int compare(AdminReportInformation o1, AdminReportInformation o2) {
                return o2.getCreateDate().compareTo(o1.getCreateDate());
            }
        };
    }
    public static Comparator<AdminReportInformation> getUsernameAscendingComparator(){
        return new Comparator<AdminReportInformation>() {
            @Override
            public int compare(AdminReportInformation o1, AdminReportInformation o2) {
                return o1.getReportedUsername().compareTo(o2.getReportedUsername());
            }
        };
    }
    public static Comparator<AdminReportInformation> getUsernameDescendingComparator(){
        return new Comparator<AdminReportInformation>() {
            @Override
            public int compare(AdminReportInformation o1, AdminReportInformation o2) {
                return o2.getReportedUsername().compareTo(o1.getReportedUsername());
            }
        };
    }
    public static Comparator<AdminReportInformation> getReporterUsernameAscendingComparator(){
        return new Comparator<AdminReportInformation>() {
            @Override
            public int compare(AdminReportInformation o1, AdminReportInformation o2) {
                return o1.getReporterUsername().compareTo(o2.getReporterUsername());
            }
        };
    }
    public static Comparator<AdminReportInformation> getreporterUsernameDescendingComparator(){
        return new Comparator<AdminReportInformation>() {
            @Override
            public int compare(AdminReportInformation o1, AdminReportInformation o2) {
                return o2.getReporterUsername().compareTo(o1.getReporterUsername());
            }
        };
    }
}
