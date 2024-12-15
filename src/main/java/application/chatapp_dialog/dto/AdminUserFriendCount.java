package application.chatapp_dialog.dto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdminUserFriendCount {
    private Integer id;
    private String username;
    private String displayName;
    private Integer directFriends;
    private Integer indirectFriends;
    private Timestamp createDate;
    public AdminUserFriendCount(){
        id = 0;
        username = "";
        displayName = "";
        directFriends = 0;
        indirectFriends = 0;
        createDate = Timestamp.valueOf(LocalDateTime.now().withNano(0));
    }
    public AdminUserFriendCount(int id, String username, String displayName, int directFriends, int indirectFriends, Timestamp createDate){
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.directFriends = directFriends;
        this.indirectFriends = indirectFriends;
        this.createDate = createDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setDirectFriends(Integer directFriends) {
        this.directFriends = directFriends;
    }

    public void setIndirectFriends(Integer indirectFriends) {
        this.indirectFriends = indirectFriends;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDirectFriends() {
        return directFriends;
    }

    public Integer getIndirectFriends() {
        return indirectFriends;
    }

}
