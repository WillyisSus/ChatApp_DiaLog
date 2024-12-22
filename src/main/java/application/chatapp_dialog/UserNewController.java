package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.security.UserRegistrationValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
            String email = signupTextEmail.getText();
            String password = signupPasswordPassword.getText();
            if (!UserRegistrationValidator.validateUsername(username)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Invalid username");
                alert.setContentText("- 6-32 characters\n- Only alphanumerics\n- Cannot start with 'admin'");
                alert.showAndWait();
                return;
            }
            if (!UserRegistrationValidator.validateEmail(email)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Invalid email");
                alert.setContentText("- 6-32 characters\n- Only alphanumerics\n- Cannot start with 'admin'");
                alert.showAndWait();
                return;
            }
            if (!UserRegistrationValidator.validatePassword(password)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Invalid password");
                alert.setContentText("- 8-32 characters\n- At least one character\n- At least one number\n- At least one of !@#$%.");
                alert.showAndWait();
                return;
            }
            Connection conn = UtilityDAL.getConnection();
            if (conn != null) {
                try {
                    String query = "select count(id) from user_accounts where username = ?";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    if (rs.getInt(1) > 0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Notification");
                        alert.setHeaderText("Username are already exists");
                        alert.showAndWait();
                        return;
                    }
                    query = "select count(id) from user_accounts where email = ?";
                    ps = conn.prepareStatement(query);
                    ps.setString(1, email);
                    rs = ps.executeQuery();
                    rs.next();
                    if (rs.getInt(1) > 0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Notification");
                        alert.setHeaderText("Email are already exists");
                        alert.showAndWait();
                        return;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-new-2-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage) display.getScene().getWindow();
            stage.setScene(scene);
            UserNew2Controller controller = fxmlLoader.getController();
            controller.setdata(username, email, password, stage);
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
            UserForgotController controller = fxmlLoader.getController();
            controller.setdata(stage);
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
            UserLoginController controller = fxmlLoader.getController();
            controller.setdata(stage);
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

    void setdata(Stage gstage){
        stage = gstage;
    }
}
