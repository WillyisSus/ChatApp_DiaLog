package application.chatapp_dialog.dto;

import application.chatapp_dialog.dal.AdminUserAccountDAL;
import eu.hansolo.toolbox.time.Times;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.AbstractMap;

public class AdminUserActivityLog {
    private SimpleIntegerProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty displayName;
    private SimpleStringProperty sessionStart;
    private SimpleStringProperty sessionEnd;

    public AdminUserActivityLog(){
        id = new SimpleIntegerProperty();
        username = new SimpleStringProperty();
        displayName = new SimpleStringProperty();
        sessionStart = new SimpleStringProperty();
        sessionEnd =  new SimpleStringProperty();
    }

    public AdminUserActivityLog(int id, String username, String displayName, Timestamp sessionStart, Timestamp sessionEnd){
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.displayName = new SimpleStringProperty(displayName);
        this.sessionEnd = new SimpleStringProperty(sessionEnd.toString());
        this.sessionStart = new SimpleStringProperty(sessionStart.toString());
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setSessionEnd(Timestamp sessionEnd) {
        this.sessionEnd.set(sessionEnd.toString());
    }

    public void setSessionStart(Timestamp sessionStart) {
        this.sessionStart.set(sessionStart.toString());
    }

    public int getId() {
        return id.get();
    }

    public String getDisplayName() {
        return displayName.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getSessionEnd() {
        return sessionStart.get();
    }

    public String getSessionStart() {
        return sessionStart.get();
    }

    public Timestamp getSessionStartAsTimeStamp(){
        return Timestamp.valueOf(sessionStart.get());
    }
    public Timestamp getSessionEndAsTimeStamp(){
        return Timestamp.valueOf(sessionEnd.get());
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AdminUserActivityLog c)
        {
            //whatever here
            return this.getId() == c.getId();
        }
        return false;
    }
}


