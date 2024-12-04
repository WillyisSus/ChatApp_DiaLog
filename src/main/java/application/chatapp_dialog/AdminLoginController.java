package application.chatapp_dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.net.URL;
import java.util.ResourceBundle;

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


    public void login(ActionEvent event){
        String username = adminUsername.getText();
        String password = adminPassword.getText();
        System.out.println(username + " " + password);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        adminPassword.setText("");
        adminUsername.setText("Nigger");
        message.setText("Hello bitches");
        loginButton.setOnAction(this::login);
    }
}
