package application.chatapp_dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SceneController {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;
    @FXML
    protected void loginClick() throws IOException {
        System.out.println(display.getScene());
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void accountClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-account-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void createClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-create-group-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void memberClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-group-member-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void logoutClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void changepassClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-password-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void friendsClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-friends-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void searchClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-search-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void allsearchClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-allsearch-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void changeToAdmin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-user-listing-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void memberAdminClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-group-member-admin-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage) display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void forgotClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-forgot-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage) display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void newClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-new-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage) display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void nameClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-name-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage) display.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}