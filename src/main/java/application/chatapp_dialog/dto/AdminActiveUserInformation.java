package application.chatapp_dialog.dto;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AdminActiveUserInformation {
    private Integer id;
    private String username;
    private String email;
    private Timestamp createDate;
    private Integer logins;
    private Integer groupChats;
    private Integer privateChats;
    public AdminActiveUserInformation(){
        id = 0;
        logins = 0;
        groupChats = 0;
        privateChats = 0;
        username = "";
        email = "";
        createDate = Timestamp.valueOf(LocalDateTime.now().withNano(0));
    }

    public AdminActiveUserInformation(int id, String username, String  email, Timestamp createDate, int logins, int groupChats, int privateChats){
        this.id = id;
        this.logins = logins;
        this.groupChats = groupChats;
        this.privateChats = privateChats;
        this.username = username;
        this.email = email;
        this.createDate = createDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGroupChats(Integer groupChats) {
        this.groupChats = groupChats;
    }

    public void setLogins(Integer logins) {
        this.logins = logins;
    }

    public void setPrivateChats(Integer privateChats) {
        this.privateChats = privateChats;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public Integer getGroupChats() {
        return groupChats;
    }

    public Integer getLogins() {
        return logins;
    }

    public Integer getPrivateChats() {
        return privateChats;
    }
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AdminActiveUserInformation c)
        {
            //whatever here
            return this.getId().equals(c.getId());
        }
        return false;
    }
}
