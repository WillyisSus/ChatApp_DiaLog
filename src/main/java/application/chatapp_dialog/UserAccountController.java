package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
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

public class UserAccountController implements Initializable, Runnable {
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
    private MenuItem accountMenuitemFriends;
    @FXML
    private MenuItem accountMenuitemLogout;
    @FXML
    private ImageView accountImageBack;
    @FXML
    private TextField accountTextDisplayname;
    @FXML
    private TextField accountTextAddress;
    @FXML
    private TextField accountTextEmail;
    @FXML
    private DatePicker accountDateDob;
    @FXML
    private ChoiceBox<String> accountChoiceSex;
    private final String[] sexList = {"Female", "Male"};
    @FXML
    private Button accountButtonPassword;
    @FXML
    private Button accountButtonConfirm;

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
    synchronized public void buttonPasswordClicked(ActionEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-password-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            UserPasswordController controller = fxmlLoader.getController();
            controller.setdata(id, stage);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void buttonConfirmClicked(ActionEvent event){
        if (conn != null) {
            try {
                String email = accountTextEmail.getText();
                String displayname = accountTextDisplayname.getText();
                LocalDate dob = accountDateDob.getValue();
                Boolean sex = null;
                if ("Male".equals(accountChoiceSex.getValue())){
                    sex = true;
                } else if ("Female".equals(accountChoiceSex.getValue())) {
                    sex = false;
                }
                String address = accountTextAddress.getText();
                if (!UserRegistrationValidator.validateEmail(email)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Invalid email");
                    alert.setContentText("- 6-32 characters\n- Only alphanumerics\n- Cannot start with 'admin'");
                    alert.showAndWait();
                    return;
                }
                if (displayname == null || displayname.isBlank() || dob == null || dob.isAfter(LocalDate.now()) || sex == null || address == null || address.isBlank()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Invalid information");
                    alert.showAndWait();
                    return;
                }
                String query = "update user_accounts set email = ? where id = ?; update user_account_info set displayname = ?, dob = ?, sex = ?, address = ? where account_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, email);
                ps.setInt(2, id);
                ps.setString(3, displayname);
                ps.setDate(4, Date.valueOf(dob));
                ps.setBoolean(5, sex);
                ps.setString(6, address);
                ps.setInt(7, id);
                ps.execute();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Information Changed.");
                alert.showAndWait();
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
        if (conn != null) {
            try {
                String query = "select id, displayname, address, email, dob, sex from user_accounts join user_account_info on id = account_id where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                rs.next();
                accountTextDisplayname.setText(rs.getString("displayname"));
                accountTextAddress.setText(rs.getString("address"));
                accountTextEmail.setText(rs.getString("email"));
                accountDateDob.setValue(rs.getDate("dob").toLocalDate());
                if (rs.getBoolean("sex")) {
                    accountChoiceSex.setValue("Male");
                } else {
                    accountChoiceSex.setValue("Female");
                }
                accountChoiceSex.getItems().addAll(sexList);
                accountMenuitemFriends.setOnAction(this::menuitemFriendsClicked);
                accountMenuitemLogout.setOnAction(this::menuitemLogoutClicked);
                accountImageBack.setOnMouseClicked(this::imageBackClicked);
                accountButtonPassword.setOnAction(this::buttonPasswordClicked);
                accountButtonConfirm.setOnAction(this::buttonConfirmClicked);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        Runnable r1 = new UserAccountController();
        Thread t1 = new Thread(r1, "UserAccountController");
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
                if (!rs.next() || !(rs.getString("status").equals("online"))){
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
