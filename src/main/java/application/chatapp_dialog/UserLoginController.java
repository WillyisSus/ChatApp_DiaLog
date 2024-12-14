package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.security.EncryptPassword;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
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

    public boolean loginedUser(String username, String password){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "select salt from user_accounts where username = ? or email = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                if (username == null || password == null){
                    throw new NullPointerException("Username or password is not provided");
                }
                ps.setString(1, username);
                ps.setString(2, username);
                ResultSet  rs = ps.executeQuery();
                rs.next();
                String salt =  rs.getString("salt");
                String hashedPass = EncryptPassword.hashPassword(password, salt);
                ps = conn.prepareStatement("select * from user_accounts where password = ?");
                ps.setString(1, hashedPass);
                rs = ps.executeQuery();
                rs.next();
                int id = rs.getInt("id");
                System.out.println("logged in " + id);
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @FXML
    public void buttonLoginClicked(ActionEvent event){
        String username = loginTextUsername.getText();
        String password = loginPasswordPassword.getText();
        System.out.println(username + "\n" + password);

        if (loginedUser(username, password)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                stage = (Stage) display.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        } else {
            System.out.println("cant login");
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
    public void buttonCreateClicked(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-new-view.fxml"));
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
        loginTextUsername.setText("");
        loginPasswordPassword.setText("");
        loginButtonLogin.setOnAction(this::buttonLoginClicked);
        loginButtonForgot.setOnAction(this::buttonForgotClicked);
        loginButtonCreate.setOnAction(this::buttonCreateClicked);
    }
}
