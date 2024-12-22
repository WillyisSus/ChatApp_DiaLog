package application.chatapp_dialog.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AdminGroupInformation {
    private Integer id;
    private String groupName;
    private Timestamp createDate;
    private Integer admins;
    private Integer members;

    public AdminGroupInformation(){
        id = 0;
        groupName = "";
        createDate = Timestamp.valueOf(LocalDateTime.now().withNano(0));
        admins = 0;
        members = 0;
    }
    public AdminGroupInformation(int id, String groupName, Timestamp createDate, int admins, int members){
        this.id = id;
        this.groupName = groupName;
        this.createDate = createDate;
        this.admins = admins;
        this.members = members;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public Integer getAdmins() {
        return admins;
    }

    public Integer getMembers() {
        return members;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setAdmins(Integer admins) {
        this.admins = admins;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AdminGroupInformation c)
        {
            //whatever here
            return this.getId().equals(c.getId());
        }
        return false;
    }
}
