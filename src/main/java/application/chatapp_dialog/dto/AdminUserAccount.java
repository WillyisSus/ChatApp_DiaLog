package application.chatapp_dialog.dto;

import com.sun.jdi.StringReference;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Class to display user Info on TableView
 * */
public class AdminUserAccount {
    private SimpleStringProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty email;
    private SimpleStringProperty displayName;
    private SimpleStringProperty address;
    private SimpleStringProperty sex;
    private SimpleStringProperty dob;
    private SimpleStringProperty createDate;
    private SimpleStringProperty status;

    public AdminUserAccount(){
        id = new SimpleStringProperty();
        username =  new SimpleStringProperty();
        email =  new SimpleStringProperty();
        displayName =  new SimpleStringProperty();
        address =  new SimpleStringProperty();
        sex =  new SimpleStringProperty();
        dob =  new SimpleStringProperty();
        createDate =  new SimpleStringProperty();
        status =  new SimpleStringProperty();
    }

    public AdminUserAccount(Integer id, String username, String email, String displayName, String address, Boolean sex, Date dob, Timestamp createDate, String status){
        this.id = new SimpleStringProperty(id.toString());
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.displayName = new SimpleStringProperty(displayName);
        this.address = new SimpleStringProperty(address);
        this.sex = new SimpleStringProperty(sex.toString()) ;
        this.dob = new SimpleStringProperty(dob.toString());
        this.createDate = new SimpleStringProperty(createDate.toString());
        this.status = new SimpleStringProperty(status);
    }

    public AdminUserAccount(AdminUserAccount account){
        this.id = new SimpleStringProperty(account.getId());
        this.username = new SimpleStringProperty(account.getUsername());
        this.email = new SimpleStringProperty(account.getEmail());
        this.displayName = new SimpleStringProperty(account.getDisplayName());
        this.address = new SimpleStringProperty(account.getAddress());
        this.sex = new SimpleStringProperty(account.getSex()) ;
        this.dob = new SimpleStringProperty(account.getDob());
        this.createDate = new SimpleStringProperty(account.getCreateDate());
        this.status = new SimpleStringProperty(account.getStatus());
    }

    public void copyDataFromOtherAccount(AdminUserAccount account){
        id.setValue(account.getId());
        username.setValue(account.getUsername());
        email.setValue(account.getEmail());
        displayName.setValue(account.getDisplayName());
        address.setValue(account.getAddress());
        sex.setValue(account.getSex());
        dob.setValue(account.getDob());
        createDate.setValue(account.getCreateDate());
        status.setValue(account.getStatus());
    }

    public void setStatus(String status) {
        this.status.setValue(status);
    }

    public void setId(Integer id){
        this.id.setValue(id.toString());
    }
    public void setDisplayName(String displayName) {
        this.displayName.setValue(displayName);
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate.setValue(createDate.toString());
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public void setAddress(String address) {
        this.address.setValue(address);
    }

    public void setDob(Date dob) {
        this.dob.setValue(dob.toString());
    }

    public void setSex(Boolean sex) {
        this.sex.setValue(sex?"Male":"Female");
    }

    public String getUsername() {
        return username.get();
    }

    public String getDisplayName() {
        return displayName.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getSex() {
        return sex.get();
    }

    public String getDob() {
        return dob.get();
    }

    public String getCreateDate() {
        return createDate.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getId() {
        return id.get();
    }

    public String getStatus() {
        return status.get();
    }
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof AdminUserAccount c)
        {
            //whatever here
            return this.getId().equals(c.getId());
        }
        return false;
    }
}
