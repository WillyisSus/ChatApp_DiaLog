package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import application.chatapp_dialog.security.EncryptPassword;
import application.chatapp_dialog.dal.AdminAccountDAL;


public class AdminLoginController  implements Initializable {
    @FXML
    private Button loginButton;
    @FXML
    private Button forgetpwButton;
    @FXML
    private TextField adminUsername;
    @FXML
    private PasswordField adminPassword;
    @FXML
    private Label message;

    private Scene scene;
    private Stage stage;
    private Parent root;
    @FXML
    private Button logOutButton;
    @FXML
    private Button toUserViewButton;
    @FXML
    private Button toGroupViewButton;
    @FXML
    private Button toReportViewButton;
    @FXML
    private Button toNewcomerViewButton;
    @FXML
    private Button toGraphViewButton;
    @FXML
    private Button toActiveUserButton;

    private Connection connection;
    public int authenticate(String username, String password){
        return AdminAccountDAL.authenticate(username, password, connection);
    }

    public void goToAdminView(ActionEvent event, int id){
        System.out.println("Called");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-user-listing-view.fxml"));
            root = loader.load();
            AdminUserListController ctrl = loader.getController();
            ctrl.setAdminID(id);
            ctrl.setConnection(connection);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            ctrl.setStageAndCloseHandler(stage);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void login(ActionEvent event){
        String username = adminUsername.getText();
        String password = adminPassword.getText();
        int id = authenticate(username, password);
        String res = (id == -1 ? "Wrong password or username" : "Success");
        message.setText(res);

        if (res.equals("Success")){
            goToAdminView(event, id);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connection = UtilityDAL.getConnection();
        adminPassword.setText("");
        adminUsername.setText("");
        message.setText("");
        loginButton.setOnAction(this::login);
    }

}
