package application.chatapp_dialog.admin.modalcontroller;

import application.chatapp_dialog.dal.AdminUserAccountDAL;
import application.chatapp_dialog.dal.EmailDAL;
import application.chatapp_dialog.dto.AdminUserAccount;
import application.chatapp_dialog.security.UserRegistrationValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import javax.xml.transform.Source;
import java.lang.reflect.AccessFlag;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminChangeUserPasswordController implements Initializable {
    private AdminUserAccount userID;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmNewPassword;
    @FXML
    private Label errorMessage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newPassword.setPromptText("New password");
        confirmNewPassword.setPromptText("Confirm new password");
        errorMessage.setText("");
    }

    public boolean validate(){
        boolean valid = true;
        if (newPassword.getText().isEmpty() || confirmNewPassword.getText().isEmpty()){
            errorMessage.setText("Please fill in all field!!!");
            valid = false;
        } else if (!newPassword.getText().equals(confirmNewPassword.getText())) {
            errorMessage.setText("Password and confirm password not match!!!");
            valid = false;
        } else if (!UserRegistrationValidator.validatePassword(newPassword.getText())){
            errorMessage.setText("Password length must be > 8, has at least a digit, and a character in [!@#$]");
            valid = false;
        }
        return valid;
    };

    public boolean changeUserPassword(){
        try {
            AdminUserAccountDAL.updatePassword(Integer.parseInt(userID.getId()), newPassword.getText());
            EmailDAL.sendNewPassword(userID.getEmail(), newPassword.getText());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database ERROR !!!");
            alert.setHeaderText("Change user password failed!");
            alert.setContentText("Database-related problems occur.");
            return false;
        }
        return true;
    }



    public void setUserID(AdminUserAccount selected){
        this.userID = selected;
    }

}
