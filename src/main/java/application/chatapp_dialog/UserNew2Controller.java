package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.security.EncryptPassword;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserNew2Controller implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    private String username;
    private String email;
    private String password;
    @FXML
    private TextField signupTextDisplayname;
    @FXML
    private DatePicker signupDateDob;
    @FXML
    private ChoiceBox<String> signupChoiceSex;
    private String[] sexList = {"Female", "Male"};
    @FXML
    private TextField signupTextAddress;
    @FXML
    private Button signupButtonSignup;
    @FXML
    private Button signupButtonForgot;
    @FXML
    private Button signupButtonLogin;

    public int createdNewUser(String username, String email, String password, String displayname, LocalDate dob, boolean sex, String address){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "insert into user_accounts (username, password, email, salt) " +
                        "values (?, ?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                String salt = EncryptPassword.generateRandomSalt();
                String hashedPassword = EncryptPassword.hashPassword(password,salt);
                ps.setString(1, username);
                ps.setString(2, hashedPassword);
                ps.setString(3, email);
                ps.setString(4, salt);
                int row = ps.executeUpdate();
                System.out.println(ps.toString());
                System.out.println(row + " added");
                query = "insert into user_account_info (displayname, dob, sex, address)" + "values (?, ?, ?, ?)";
                ps = conn.prepareStatement(query);
                ps.setString(1, displayname);
                ps.setDate(2, Date.valueOf(dob));
                ps.setBoolean(3, sex);
                ps.setString(4, address);
                row = ps.executeUpdate();
                System.out.println(ps.toString());
                System.out.println(row + " added");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Account create successfully!");
                Optional<ButtonType> clickButton = alert.showAndWait();
                return row;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }

    @FXML
    public void buttonSignupClicked(ActionEvent event){
        String displayname = signupTextDisplayname.getText();
        LocalDate dob = signupDateDob.getValue();
        Boolean sex = null;
        if ("Male".equals(signupChoiceSex.getValue())){
            sex = true;
        } else if ("Female".equals(signupChoiceSex.getValue())) {
            sex = false;
        }
        String address = signupTextAddress.getText();

        if (displayname.isBlank() || dob == null || dob.isAfter(LocalDate.now()) || sex == null || address.isBlank()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Invalid information");
            Optional<ButtonType> clickButton = alert.showAndWait();
            return;
        }

        System.out.println(username + "\n" + email + "\n" + password + "\n" + dob + "\n" + sex + "\n" + address);

        if (createdNewUser(username, email, password, displayname, dob, sex, address) > 0) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                stage = (Stage) display.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Cannot create new account!");
            Optional<ButtonType> clickButton = alert.showAndWait();
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
        signupTextDisplayname.setText("");
        signupChoiceSex.getItems().addAll(sexList);
        signupTextAddress.setText("");
        signupButtonSignup.setOnAction(this::buttonSignupClicked);
        signupButtonForgot.setOnAction(this::buttonForgotClicked);
        signupButtonLogin.setOnAction(this::buttonLoginClicked);
    }

    void setUsername(String u){
        username = u;
    }
    void setEmail(String e){
        email = e;
    }
    void setPassword(String p){
        password = p;
    }
}
