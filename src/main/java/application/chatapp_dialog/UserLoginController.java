package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.security.EncryptPassword;
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

public class UserLoginController implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    @FXML
    private TextField loginTextUsername;
    @FXML
    private PasswordField loginPasswordPassword;
    @FXML
    private Button loginButtonLogin;
    @FXML
    private Button loginButtonForgot;
    @FXML
    private Button loginButtonCreate;

    Connection conn = UtilityDAL.getConnection();
    public int loginedUser(String username, String password){
        if (conn != null) {
            try {
                String query = "select * from user_accounts where username = ? or email = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, username);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Wrong username or password");
                    alert.showAndWait();
                    return 0;
                }
                if (rs.getString("status").equals("locked")){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Account was banned.");
                    alert.showAndWait();
                    return 0;
                }
                String op = rs.getString("password");
                String salt = rs.getString("salt");
                int id = rs.getInt("id");
                String hashedPass = EncryptPassword.hashPassword(password, salt);
                if (hashedPass.equals(op)){
                    System.out.println("logged in " + id);
                    query = "update user_accounts set status = 'online' where id = ?";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    return id;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Wrong username or password");
                    alert.showAndWait();
                    return 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    @FXML
    public void buttonLoginClicked(ActionEvent event){
        String username = loginTextUsername.getText();
        String password = loginPasswordPassword.getText();
        System.out.println(username + "\n" + password);
        if (username == null || username.isBlank() || password == null || password.isBlank()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Wrong username or password");
            alert.showAndWait();
            return;
        }
        int id = loginedUser(username, password);
        if (id > 0) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                stage = (Stage) display.getScene().getWindow();
                stage.setScene(scene);
                UserChatController controller = fxmlLoader.getController();
                controller.setdata(id, 0, stage);
                stage.show();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
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
        loginTextUsername.setText("");
        loginPasswordPassword.setText("");
        loginButtonLogin.setOnAction(this::buttonLoginClicked);
        loginButtonForgot.setOnAction(this::buttonForgotClicked);
        loginButtonCreate.setOnAction(this::buttonCreateClicked);
    }

    void setdata(Stage gstage){
        stage = gstage;
    }
}
