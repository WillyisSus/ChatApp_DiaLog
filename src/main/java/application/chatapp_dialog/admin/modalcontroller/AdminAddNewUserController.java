package application.chatapp_dialog.admin.modalcontroller;

import application.chatapp_dialog.dto.AdminUserAccount;
import application.chatapp_dialog.security.UserRegistrationValidator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import application.chatapp_dialog.dal.AdminUserAccountDAL;
public class AdminAddNewUserController implements Initializable {
    private Connection connection;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField email;
    @FXML
    private TextField displayName;
    @FXML
    private RadioButton isFemale;
    @FXML
    private RadioButton isMale;
    @FXML
    private TextField address;
    @FXML
    private DatePicker dob;
    @FXML
    private Label errorMessage;
    @FXML
    private CheckBox showPassword;

    public boolean validate(){
        if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty() || email.getText().trim().isEmpty() || displayName.getText().trim().isEmpty()){
            errorMessage.setText("Please fill all the fields with *");
            return false;
        } else if (!UserRegistrationValidator.validateUsername(username.getText())){
            errorMessage.setText("Error: Username start with \"admin\" or under 8 character");
            return false;
        }else if (!UserRegistrationValidator.validateEmail(email.getText())){
            errorMessage.setText("Error: Email pattern not valid, or special character is before @");
            return false;
        }else if (!UserRegistrationValidator.validatePassword(password.getText())){
            errorMessage.setText("Password length > 8, has char in [0-9], [a-z], [A-Z] and [!@#$%]");
            return false;
        }

        return true;
    }

    public boolean addNewUser(){
        if (AdminUserAccountDAL.createNewUser(username.getText(), password.getText(), email.getText(), displayName.getText(),
                (isMale.selectedProperty().get()), address.getText(), Date.valueOf(dob.getValue()), connection)){
            return true;
        }
        errorMessage.setText("Cannot add user, please try another username or email");
        return false;
    }
    public void setConnection(Connection conn){
        connection = conn;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText("");
        password.setText("");
        displayName.setText("");
        email.setText("");
        address.setText("");
        dob.setValue(null);
        isMale.selectedProperty().set(true);
        isFemale.selectedProperty().set(false);
        errorMessage.setText("");
        dob.setValue(LocalDate.now());
        isFemale.addEventHandler(ActionEvent.ACTION, event -> {
            isMale.selectedProperty().set(false);
            isFemale.selectedProperty().set(true);
        });

        isMale.addEventHandler(ActionEvent.ACTION, event -> {
            isMale.selectedProperty().set(true);
            isFemale.selectedProperty().set(false);
        });

        showPassword.setSelected(false);

    }

}
