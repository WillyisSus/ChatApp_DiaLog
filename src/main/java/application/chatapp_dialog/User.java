package application.chatapp_dialog;

import javafx.beans.property.SimpleStringProperty;

public class User {
    private SimpleStringProperty id;
    private SimpleStringProperty username;
    private SimpleStringProperty displayname;
    private SimpleStringProperty email;
    private SimpleStringProperty sex;
    private SimpleStringProperty address;
    private SimpleStringProperty dob;
    private final SimpleStringProperty createdate;
    private SimpleStringProperty status;

    User(String id, String username, String displayname, String email, String sex, String address, String dob, String createDate, String status){
        this.id = new SimpleStringProperty(id);
        this.username = new SimpleStringProperty(username);
        this.displayname = new SimpleStringProperty(displayname);
        this.email = new SimpleStringProperty(email);
        this.sex = new SimpleStringProperty(sex);
        this.address = new SimpleStringProperty(address);
        this.dob = new SimpleStringProperty(dob);
        this.createdate = new SimpleStringProperty(createDate);
        this.status = new SimpleStringProperty(status);
    }

    public String getId(){
        return id.get();
    }
    public String getUsername(){
        return username.get();
    }
    public String getDisplayname(){
        return displayname.get();
    }
    public String getEmail(){
        return email.get();
    }
    public String getSex(){
        return sex.get();
    }
    public String getAddress(){
        return address.get();
    }
    public String getDob(){
        return dob.get();
    }
    public String getCreatedate(){
        return createdate.get();
    }
    public String getStatus(){
        return status.get();
    }
    public void setId(String s){
        id.set(s);
    }
    public void setDisplayname(String s){
        displayname.set(s);
    }
    public void setUsername(String s){
        username.set(s);
    }
    public void setEmail(String s){
        email.set(s);
    }
    public void setSex(String s){
        sex.set(s);
    }
    public void setDob(String s){
        dob.set(s);
    }
    public void setAddress(String s){
        address.set(s);
    }
    public void setStatus(String s){
        address.set(s);
    }


}
