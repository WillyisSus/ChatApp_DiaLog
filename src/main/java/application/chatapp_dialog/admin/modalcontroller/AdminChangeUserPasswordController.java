package application.chatapp_dialog.admin.modalcontroller;

import application.chatapp_dialog.security.UserRegistrationValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.event.ActionEvent;

import javax.xml.transform.Source;
import java.lang.reflect.AccessFlag;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminChangeUserPasswordController implements Initializable {
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmNewPassword;
    @FXML
    private Label errorMessage;
    @FXML
    private Button resetUserPasswordButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newPassword.setPromptText("New password");
        confirmNewPassword.setPromptText("Confirm new password");
        errorMessage.setText("");
        resetUserPasswordButton.setOnAction(this::resetUserPassword);
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


    private void resetUserPassword(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("IN PROGRESS!!! NEED EMAIL API SETUP");
        alert.showAndWait();
    }

}
