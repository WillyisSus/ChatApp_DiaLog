package application.chatapp_dialog;

import javafx.event.ActionEvent;
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
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.chatapp_dialog.security.EncryptPassword;
import application.chatapp_dialog.dal.AdminAccountDAL;

import javax.swing.*;

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

    public String authenticate(String username, String password){
        return AdminAccountDAL.authenticate(username, password);
    }

    public void goToAdminView(ActionEvent event){
        System.out.println("Called");
        try {
            root = FXMLLoader.load(getClass().getResource("admin-user-listing-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void login(ActionEvent event){
        String username = adminUsername.getText();
        String password = adminPassword.getText();
        String res = authenticate(username, password);
        message.setText(res);

        if (res.equals("Success")){
            goToAdminView(event);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminPassword.setText("");
        adminUsername.setText("");
        message.setText("");
        loginButton.setOnAction(this::login);
    }

}
