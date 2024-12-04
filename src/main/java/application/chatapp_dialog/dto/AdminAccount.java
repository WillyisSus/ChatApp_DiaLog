package application.chatapp_dialog.dto;

import java.util.Date;

public class AdminAccount {
    private String username;
    private String email;
    private Date createDate;
    private String displayName;
    public static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    AdminAccount(){

    }

    AdminAccount(String username, String email, String password, Date date, String displayName){
        try {
            if (username == null||email==null||password==null||date == null || displayName == null){
                throw new NullPointerException("Initialize arguments have null");
            }
            this.username = username;
            this.createDate = date;
            this.email = email;
            this.displayName = displayName;
        }catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public static String getDateFormat() {
        return dateFormat;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }
}
