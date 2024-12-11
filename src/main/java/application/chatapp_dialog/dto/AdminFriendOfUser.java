package application.chatapp_dialog.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AdminFriendOfUser{
    private SimpleIntegerProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty displayName;
    public AdminFriendOfUser(){
        id = new SimpleIntegerProperty();
        username = new SimpleStringProperty();
        displayName = new SimpleStringProperty();
    }
    public AdminFriendOfUser(int id, String username, String displayName){
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.displayName = new SimpleStringProperty(displayName);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getId() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getDisplayName() {
        return displayName.get();
    }
}