package application.chatapp_dialog;

import application.chatapp_dialog.dal.EmailDAL;
import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.dummy.UserAccountGenerator;
import application.chatapp_dialog.security.EncryptPassword;
import application.chatapp_dialog.security.UserRegistrationValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UserPasswordController implements Initializable, Runnable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;
    static GridPane display2 = new GridPane();

    static int id = 1;
    static boolean stop = false;
    @FXML
    private MenuItem passwordMenuitemFriends;
    @FXML
    private MenuItem passwordMenuitemLogout;
    @FXML
    private ImageView passwordImageBack;
    @FXML
    private PasswordField passwordPasswordOldpw;
    @FXML
    private PasswordField passwordPasswordNewpw;
    @FXML
    private PasswordField passwordPasswordConfirmpw;
    @FXML
    private Button passwordButtonReset;
    @FXML
    private Button passwordButtonConfirm;

    Connection conn = UtilityDAL.getConnection();
    synchronized public void menuitemFriendsClicked(ActionEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-friends-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            UserFriendController controller = fxmlLoader.getController();
            controller.setdata(id, stage);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void menuitemLogoutClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "update user_accounts set status = 'offline' where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ? and session_end is null";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                stop = true;
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                stage = (Stage)display.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void imageBackClicked(MouseEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            UserChatController controller = fxmlLoader.getController();
            controller.setdata(id, 0, stage);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void buttonResetClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "select email from user_accounts where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String email = rs.getString(1);
                String newpass = UserAccountGenerator.randomPassword();
                if (EmailDAL.sendNewPassword(email, newpass)){
                    String salt = EncryptPassword.generateRandomSalt();
                    String hashedPassword = EncryptPassword.hashPassword(newpass,salt);
                    query = "update user_accounts set password = ?, salt = ? where id = ?";
                    ps = conn.prepareStatement(query);
                    ps.setString(1, hashedPassword);
                    ps.setString(2, salt);
                    ps.setInt(3, id);
                    ps.executeUpdate();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Password has been reset. Logging out.");
                    alert.show();
                    query = "update user_accounts set status = 'offline' where id = ?";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ? and session_end is null";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    stop = true;
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
                    scene = new Scene(fxmlLoader.load(), 1080, 720);
                    stage = (Stage)display.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Error when attempting send reset email.");
                    alert.showAndWait();
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void buttonConfirmClicked(ActionEvent event){
        if (conn != null) {
            try {
                String oldpass = passwordPasswordOldpw.getText();
                String newpass = passwordPasswordNewpw.getText();
                String confirmpass = passwordPasswordConfirmpw.getText();
                if (!newpass.equals(confirmpass)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Confirm password does not matched.");
                    alert.showAndWait();
                    return;
                }
                if (oldpass == null || !UserRegistrationValidator.validatePassword(oldpass) || newpass == null || !UserRegistrationValidator.validatePassword(newpass) || confirmpass == null || !UserRegistrationValidator.validatePassword(confirmpass)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Invalid password.");
                    alert.setContentText("- 8-32 characters\n- At least one character\n- At least one number\n- At least one of !@#$%.");
                    alert.showAndWait();
                    return;
                }
                String query = "select * from user_accounts where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String op = rs.getString("password");
                String salt = rs.getString("salt");
                String hashedPass = EncryptPassword.hashPassword(oldpass, salt);
                if (!hashedPass.equals(op)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Wrong password.");
                    alert.showAndWait();
                    return;
                }
                String newsalt = EncryptPassword.generateRandomSalt();
                String hashedPassword = EncryptPassword.hashPassword(newpass,newsalt);
                query = "update user_accounts set password = ?, salt = ? where id = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, hashedPassword);
                ps.setString(2, newsalt);
                ps.setInt(3, id);
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Password Changed. Logging out.");
                alert.show();
                query = "update user_accounts set status = 'offline' where id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ? and session_end is null";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                stop = true;
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                stage = (Stage)display.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        display2 = display;
        passwordPasswordOldpw.setText("");
        passwordPasswordNewpw.setText("");
        passwordPasswordConfirmpw.setText("");
        passwordMenuitemFriends.setOnAction(this::menuitemFriendsClicked);
        passwordMenuitemLogout.setOnAction(this::menuitemLogoutClicked);
        passwordImageBack.setOnMouseClicked(this::imageBackClicked);
        passwordButtonReset.setOnAction(this::buttonResetClicked);
        passwordButtonConfirm.setOnAction(this::buttonConfirmClicked);
        Runnable r1 = new UserAccountController();
        Thread t1 = new Thread(r1, "UserPasswordController");
        t1.setDaemon(true);
        t1.start();
    }

    void setdata(int logid, Stage gstage){
        id = logid;
        stage = gstage;
        stage.setOnCloseRequest(event -> logout(stage));
    }

    public void run(){
        while (true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try{
                String query = "select * from user_accounts where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next() || !rs.getString("status").equals("online")){
                    query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ? and session_end is null";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    stop = true;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
                                scene = new Scene(fxmlLoader.load(), 1080, 720);
                                stage = (Stage) display2.getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (stop){
                return;
            }
        }
    }

    public void logout(Stage st){
        if (conn != null) {
            try {
                String query = "update user_accounts set status = 'offline' where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ? and session_end is null";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}