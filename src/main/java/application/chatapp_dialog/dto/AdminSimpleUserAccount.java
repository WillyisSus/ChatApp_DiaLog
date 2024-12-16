package application.chatapp_dialog.dto;

public class AdminSimpleUserAccount {
    private String username;
    private String displayName;
    private Boolean isAdmin;
    public AdminSimpleUserAccount(){
        username = "";
        displayName = "";
        isAdmin = false;

    }
    public AdminSimpleUserAccount(String username, String displayName, Boolean isAdmin){
        this.username = username;
        this.displayName = displayName;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
