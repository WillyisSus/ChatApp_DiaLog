package application.chatapp_dialog;

import application.chatapp_dialog.dal.EmailDAL;
import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.dummy.UserAccountGenerator;
import application.chatapp_dialog.security.EncryptPassword;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserForgotController implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    @FXML
    private TextField forgotTextUsername;
    @FXML
    private TextField forgotTextEmail;
    @FXML
    private Button forgotButtonReset;
    @FXML
    private Button forgotButtonLogin;
    @FXML
    private Button forgotButtonCreate;

    Connection conn = UtilityDAL.getConnection();
    public void buttonResetClicked(ActionEvent event){
        if (conn != null) {
            try {
                String username = forgotTextUsername.getText();
                String email = forgotTextEmail.getText();
                if (username == null || username.isBlank() || email == null || email.isBlank()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Invalid username or email.");
                    alert.showAndWait();
                    return;
                }
                String query = "select * from user_accounts where username = ? and email = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    String newpass = UserAccountGenerator.randomPassword();
                    if (EmailDAL.sendNewPassword(email, newpass)){
                        String salt = EncryptPassword.generateRandomSalt();
                        String hashedPassword = EncryptPassword.hashPassword(newpass,salt);
                        query = "update user_accounts set password = ?, salt = ? where username = ? and email = ?";
                        ps = conn.prepareStatement(query);
                        ps.setString(1, hashedPassword);
                        ps.setString(2, salt);
                        ps.setString(3, username);
                        ps.setString(4, email);
                        ps.executeUpdate();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Notification");
                        alert.setHeaderText("Password has been reset.");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Notification");
                        alert.setHeaderText("Error when attempting send an email.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Cannot find your account.");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void buttonLoginClicked(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage) display.getScene().getWindow();
            stage.setScene(scene);
            UserLoginController controller = fxmlLoader.getController();
            controller.setdata(stage);
            stage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    public void buttonCreateClicked(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-new-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage) display.getScene().getWindow();
            stage.setScene(scene);
            UserNewController controller = fxmlLoader.getController();
            controller.setdata(stage);
            stage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        forgotTextUsername.setText("");
        forgotTextEmail.setText("");
        forgotButtonReset.setOnAction(this::buttonResetClicked);
        forgotButtonLogin.setOnAction(this::buttonLoginClicked);
        forgotButtonCreate.setOnAction(this::buttonCreateClicked);
    }

    void setdata(Stage gstage){
        stage = gstage;
    }
}
