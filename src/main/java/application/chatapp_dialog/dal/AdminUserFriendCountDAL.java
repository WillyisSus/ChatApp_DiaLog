package application.chatapp_dialog.dal;

import application.chatapp_dialog.dto.AdminUserAccount;
import application.chatapp_dialog.dto.AdminUserFriendCount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdminUserFriendCountDAL {
        private static final String query = "with userfriend as( " +
                " select userid, user_accounts.username, user_account_info.displayname, user_accounts.create_date, friend from ( " +
                " select distinct friendships.accept_id as userid, friendships.request_id  as friend from friendships " +
                " union " +
                " select distinct friendships.request_id as userid, friendships.accept_id  as friend from friendships " +
                " ) join user_accounts on user_accounts.id = userid " +
                "   join user_account_info on user_account_info.account_id = userid " +
                "), userindirectfriend as( " +
                " select distinct uf1.userid, uf1.username, uf1.displayname, uf1.create_date, uf1.friend as friend, uf2.friend as indirect " +
                " from userfriend as uf1 " +
                " left join userfriend as uf2 on uf2.userid = uf1.friend and uf2.friend != uf1.userid " +
                " and uf2.friend not in (select userfriend.friend from userfriend where userfriend.userid = uf1.userid) " +
                ") " +
                "select userindirectfriend.userid, userindirectfriend.username, userindirectfriend.displayname, userindirectfriend.create_date, count(distinct userindirectfriend.friend) as directs, count(distinct userindirectfriend.indirect) as indirects  " +
                "from userindirectfriend  " +
                "group by userindirectfriend.userid,  userindirectfriend.username, userindirectfriend.displayname, userindirectfriend.create_date";
        public static AdminUserFriendCount createObject(ResultSet rs) throws SQLException {
            AdminUserFriendCount obj = new AdminUserFriendCount();
            obj.setId(rs.getInt("userid"));
            obj.setUsername(rs.getString("username"));
            obj.setDisplayName(rs.getString("displayname"));
            obj.setCreateDate(rs.getTimestamp("create_date"));
            obj.setDirectFriends(rs.getInt("directs"));
            obj.setIndirectFriends(rs.getInt("indirects"));
            return obj;
        }

        public static List<AdminUserFriendCount> getUserDirectAndIndirectFriendCount() throws SQLException {
            List<AdminUserFriendCount> list = new ArrayList<>();

            Connection conn = UtilityDAL.getConnection();
            if (conn != null){
                try (PreparedStatement ps = conn.prepareStatement(query)){
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        list.add(createObject(rs));
                    }
                } catch (SQLException e) {
                    throw e;
                }
            }

            return list;
        }

        public static Comparator<AdminUserFriendCount> getCreateDateAscendingComparator(){
            return new Comparator<AdminUserFriendCount>() {
                @Override
                public int compare(AdminUserFriendCount o1, AdminUserFriendCount o2) {
                    return o1.getCreateDate().compareTo(o2.getCreateDate());
                }
            };
        }

        public static Comparator<AdminUserFriendCount> getCreateDateDescendingComparator(){
            return new Comparator<AdminUserFriendCount>() {
                @Override
                public int compare(AdminUserFriendCount o1, AdminUserFriendCount o2) {
                    return o2.getCreateDate().compareTo(o1.getCreateDate());
                }
            };
        }

        public static Comparator<AdminUserFriendCount> getUsernameAscendingComparator(){
            return new Comparator<AdminUserFriendCount>() {
                @Override
                public int compare(AdminUserFriendCount o1, AdminUserFriendCount o2) {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            };
        }

        public static Comparator<AdminUserFriendCount> getUsernameDescendingComparator(){
            return new Comparator<AdminUserFriendCount>() {
                @Override
                public int compare(AdminUserFriendCount o1, AdminUserFriendCount o2) {
                    return o2.getUsername().compareTo(o1.getUsername());
                }
            };
        }

}
