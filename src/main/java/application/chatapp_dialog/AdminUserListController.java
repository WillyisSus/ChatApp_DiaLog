package application.chatapp_dialog;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminUserListController implements Initializable {
    @FXML
    private TableView tableview;
    @FXML
    private TableColumn<User, String> username;
    @FXML
    private TableColumn<User, String> displayname;
    @FXML
    private TableColumn<User, String> email;
    @FXML
    private TableColumn<User, String> sex;
    @FXML
    private TableColumn<User, String> address;
    @FXML
    private TableColumn<User, String> dob;
    @FXML
    private TableColumn<User, String> createdate;
    @FXML
    private TableColumn<User, String> status;

    @FXML
    private ListView<String> orderlist;


    private Scene scene;
    private Stage stage;
    private Parent root;
    private int currentMax = 1;
    private String[] orders = {"Ascending Creation Date", "Descending Creation Date", "Ascending by name (A-Z)",  "Descending by name (Z-A)"};
    private ObservableList<User> userlist;



    public void switchToUser(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-user-listing-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void switchToActiveUser(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-activeuser-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void switchToGraph(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-graph-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void switchToGroup(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-group-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void switchToReport(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-report-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void switchToNewUser(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-newuser-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    public void handleAddNewUser(){

        try {
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("admin-user-listing-add-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleUpdateUser(){

        try {
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("admin-user-listing-update-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleRemoveUser(){

        try {
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("admin-user-listing-remove-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleLockUser(){

        try {
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("admin-user-listing-lock-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleShowLogin(){
        try {
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("admin-user-listing-signin-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleShowFriendList(){
        try {
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("admin-user-listing-show-friend.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleChangePassword(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-user-listing-updatepassword-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            Optional<ButtonType> clickedButton = dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void changeToUser(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
        scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userlist = FXCollections.observableArrayList(
                new User("001", "user001", "User 001", "user001@gmail.com", "Male", "user001.address", "01/01/2001", "01/01/2001", "online"),
                new User("002", "user002", "User 002", "user002@gmail.com", "Female", "user002.address", "02/02/2001", "02/02/2001", "online"),
                new User("003", "user003", "User 003", "user003@gmail.com", "Male", "user001.address", "01/01/2001", "01/01/2001", "online"),
                new User("004", "user004", "User 004", "user004@gmail.com", "Female", "user001.address", "01/01/2001", "01/01/2001", "online"),
                new User("005", "user005", "User 005", "user005@gmail.com", "Female", "user001.address", "01/01/2001", "01/01/2001", "offline"),
                new User("006", "user006", "User 006", "user006@gmail.com", "Male", "user001.address", "01/01/2001", "01/01/2001", "locked")
        );
        System.out.println(userlist.stream().toList().toString());
//        Asscociate table collumns;

        username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        displayname.setCellValueFactory(new PropertyValueFactory<User, String>("displayname"));
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        sex.setCellValueFactory(new PropertyValueFactory<User, String>("sex"));
        dob.setCellValueFactory(new PropertyValueFactory<User, String>("dob"));
        address.setCellValueFactory(new PropertyValueFactory<User, String>("address"));
        createdate.setCellValueFactory(new PropertyValueFactory<User, String>("createdate"));
        status.setCellValueFactory(new PropertyValueFactory<User, String>("status"));
        tableview.setItems(userlist);

//    Add list items
        orderlist.getItems().addAll(orders);
    }



}
