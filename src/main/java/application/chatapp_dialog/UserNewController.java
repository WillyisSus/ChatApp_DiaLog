package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.security.EncryptPassword;
import application.chatapp_dialog.security.UserRegistrationValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UserNewController implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    @FXML
    private TextField signupTextUsername;
    @FXML
    private TextField signupTextEmail;
    @FXML
    private PasswordField signupPasswordPassword;
    @FXML
    private Button signupButtonNext;
    @FXML
    private Button signupButtonForgot;
    @FXML
    private Button signupButtonLogin;

    @FXML
    public void buttonNextClicked(ActionEvent event){
        try {
            String username = signupTextUsername.getText();
            String email = signupTextUsername.getText();
            String password = signupPasswordPassword.getText();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-new-2-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            UserNew2Controller controller = fxmlLoader.getController();
            System.out.println(controller);
            controller.setUsername(username);
            controller.setEmail(email);
            controller.setPassword(password);
            stage = (Stage) display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    @FXML
    public void buttonForgotClicked(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-forgot-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage) display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    @FXML
    public void buttonLoginClicked(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage) display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signupTextUsername.setText("");
        signupTextEmail.setText("");
        signupPasswordPassword.setText("");
        signupButtonNext.setOnAction(this::buttonNextClicked);
        signupButtonForgot.setOnAction(this::buttonForgotClicked);
        signupButtonLogin.setOnAction(this::buttonLoginClicked);
    }
}
