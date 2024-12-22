package application.chatapp_dialog.dto;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AdminNewUserAccount {
    private String username;
    private String email;
    private Timestamp createDate;
    public AdminNewUserAccount(){
        username = "";
        email = "";
        createDate = Timestamp.valueOf(LocalDateTime.now().withNano(0));
    }
    public AdminNewUserAccount(String username, String email, Timestamp createDate){
        this.username = username;
        this.email = email;
        this.createDate = createDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AdminNewUserAccount c)
        {
            //whatever here
            return this.getUsername().equals(c.getUsername());
        }
        return false;
    }
}
