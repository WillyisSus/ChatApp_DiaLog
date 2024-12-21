package application.chatapp_dialog.admin.modalcontroller;

import application.chatapp_dialog.dal.AdminUserAccountDAL;
import application.chatapp_dialog.dto.AdminUserAccount;
import application.chatapp_dialog.security.UserRegistrationValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class AdminEditUserController implements Initializable {
    private AdminUserAccount account;

    @FXML
    private Button toUserViewButton;
    @FXML
    private Button toGroupViewButton;
    @FXML
    private Button toReportViewButton;
    @FXML
    private Button toNewcomerViewButton;
    @FXML
    private Button toGraphViewButton;
    @FXML
    private Button toActiveUserButton;
    @FXML
    private TextField username;
    @FXML
    private TextField displayName;
    @FXML
    private TextField email;
    @FXML
    private TextField address;
    @FXML
    private RadioButton isFemale;
    @FXML
    private RadioButton isMale;
    @FXML
    private DatePicker dob;
    @FXML
    private Label errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText("");
        email.setText("");
        address.setText("");
        dob.setValue(LocalDate.now());
        errorMessage.setText("");
        isFemale.setSelected(false);
        isMale.setSelected(true);

        isFemale.addEventHandler(ActionEvent.ACTION, event -> {
            isFemale.setSelected(true);
            isMale.setSelected(false);
        });
        isMale.addEventHandler(ActionEvent.ACTION, event -> {
            isFemale.setSelected(false);
            isMale.setSelected(true);
        });
    }
    public AdminUserAccount setNewInformation() {
        AdminUserAccount temp = new AdminUserAccount(account);
        temp.setDob(Date.valueOf(dob.getValue()));
        temp.setSex(isMale.isSelected());
        temp.setDisplayName(displayName.getText());
        temp.setEmail(email.getText());
        temp.setAddress(address.getText());
        temp.setUsername(username.getText());
        try {
            if (!AdminUserAccountDAL.updateUserAccount(temp))
                return account;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return temp;
    }
    public boolean validate(){
        boolean valid = true;
        if (username.getText().trim().isEmpty()  || email.getText().trim().isEmpty() || displayName.getText().trim().isEmpty()){
            errorMessage.setText("Please fill all the fields with *");
            valid = false;

        } else if (!UserRegistrationValidator.validateUsername(username.getText())){
            errorMessage.setText("Error: Username start with \"admin\" or under 8 character");
            valid = false;

        }else if (!UserRegistrationValidator.validateEmail(email.getText())){
            errorMessage.setText("Error: Email pattern not valid, or special character is before @");
            valid = false;

        }
        return valid;
    }

    public void setUser(AdminUserAccount account){
        this.account = account;
        username.setText(account.getUsername());
        displayName.setText(account.getDisplayName());
        email.setText(account.getEmail());
        address.setText(account.getAddress());
        dob.setValue(Date.valueOf(account.getDob()).toLocalDate());
        boolean sex = account.getSex().equals("Male");
        isMale.setSelected(sex);
        isFemale.setSelected(!sex);

    }
}
