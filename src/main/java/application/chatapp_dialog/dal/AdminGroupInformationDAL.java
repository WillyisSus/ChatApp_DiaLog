package application.chatapp_dialog.dal;

import application.chatapp_dialog.dto.AdminGroupInformation;
import application.chatapp_dialog.dto.AdminSimpleUserAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminGroupInformationDAL {
    private static final String query =   "select box_chats.id, box_chats.box_name,  " +
            "count(*) filter (where box_member.is_admin) as admins,  " +
            "count(*) as members,  " +
            "box_chats.create_date " +
            "from box_chats " +
            "join box_chat_members as box_member on box_member.box_id = box_chats.id " +
            "where not box_chats.is_direct " +
            "group by box_chats.id, box_chats.box_name";
    private static final String memberQuery = "select user_accounts.username, user_account_info.displayname, box_chat_members.is_admin " +
            "from box_chat_members " +
            "join user_accounts on user_accounts.id = box_chat_members.user_id " +
            "join user_account_info on user_account_info.account_id = user_accounts.id " +
            "where box_chat_members.box_id = ?";
    public static AdminGroupInformation createObject(ResultSet rs) throws SQLException {
        AdminGroupInformation obj = new AdminGroupInformation();
        obj.setId(rs.getInt("id"));
        obj.setGroupName(rs.getString("box_name"));
        obj.setCreateDate(rs.getTimestamp("create_date"));
        obj.setMembers(rs.getInt("members"));
        obj.setAdmins(rs.getInt("admins"));
        return obj;
    }

    public static AdminSimpleUserAccount createGroupMemberObject(ResultSet rs) throws SQLException {
        AdminSimpleUserAccount obj = new AdminSimpleUserAccount();
        obj.setAdmin(rs.getBoolean("is_admin"));
        obj.setUsername(rs.getString("username"));
        obj.setDisplayName(rs.getString("displayname"));
        return obj;
    }
    public static List<AdminGroupInformation> getGroupInformationList(Connection conn) throws SQLException {
        List<AdminGroupInformation> list = new ArrayList<>();
        if (conn != null){
            try(PreparedStatement ps = conn.prepareStatement(query)){
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    list.add(createObject(rs));
                }
            } catch (SQLException e) {
                throw new SQLException("Database refuse connection.");

            }
        }

        return list;
    }
    public static List<AdminSimpleUserAccount> getMemberOfGroup(AdminGroupInformation group, Connection conn) throws SQLException {
        List<AdminSimpleUserAccount> list = new ArrayList<>();
        if (conn != null){
            try(PreparedStatement ps = conn.prepareStatement(memberQuery)){
                ps.setInt(1, group.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    list.add(createGroupMemberObject(rs));
                }
            }
        } else throw new SQLException("Database refuse connection.");

        return list;
    }
    public static Comparator<AdminGroupInformation> getDateAscendingComparator(){
        return new Comparator<AdminGroupInformation>() {
            @Override
            public int compare(AdminGroupInformation o1, AdminGroupInformation o2) {
                return o1.getCreateDate().compareTo(o2.getCreateDate());
            }
        };
    }
    public static Comparator<AdminGroupInformation> getDateDescendingComparator(){
        return new Comparator<AdminGroupInformation>() {
            @Override
            public int compare(AdminGroupInformation o1, AdminGroupInformation o2) {
                return o2.getCreateDate().compareTo(o1.getCreateDate());
            }
        };
    }
    public static Comparator<AdminGroupInformation> getBoxNameAscendingComparator(){
        return new Comparator<AdminGroupInformation>() {
            @Override
            public int compare(AdminGroupInformation o1, AdminGroupInformation o2) {
                return o1.getGroupName().compareTo(o2.getGroupName());
            }
        };
    }
    public static Comparator<AdminGroupInformation> getBoxNameDescendingComparator(){
        return new Comparator<AdminGroupInformation>() {
            @Override
            public int compare(AdminGroupInformation o1, AdminGroupInformation o2) {
                return o2.getGroupName().compareTo(o1.getGroupName());
            }
        };
    }
}
