package application.chatapp_dialog;


import application.chatapp_dialog.admin.modalcontroller.*;
import application.chatapp_dialog.dal.AdminUserAccountDAL;
import application.chatapp_dialog.dal.UtilityDAL;
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
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import application.chatapp_dialog.dto.*;

import javax.swing.*;

public class AdminUserListController implements Initializable {
//    Table related
    @FXML
    private TableView tableview;
    @FXML
    private TableColumn<AdminUserAccount, String> username;
    @FXML
    private TableColumn<AdminUserAccount, String> displayname;
    @FXML
    private TableColumn<AdminUserAccount, String> email;
    @FXML
    private TableColumn<AdminUserAccount, String> sex;
    @FXML
    private TableColumn<AdminUserAccount, String> address;
    @FXML
    private TableColumn<AdminUserAccount, String> dob;
    @FXML
    private TableColumn<AdminUserAccount, String> createdate;
    @FXML
    private TableColumn<AdminUserAccount, String> status;

    @FXML
    private ListView<String> orderlist;

//    Functional button
    @FXML
    private Button updateButton;
    @FXML
    private Button changePasswordButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button lockButton;
    @FXML
    private Button addNewButton;
    @FXML
    private Button showFriendButton;
    @FXML
    private Button signInButton;

//    Filter and ordering

    @FXML
    private MenuButton statusMenu;
    @FXML
    private MenuItem offlineItem;
    @FXML
    private MenuItem onlineItem;
    @FXML
    private MenuItem lockItem;
    @FXML
    private MenuItem allItem;
    @FXML
    private MenuButton orderMenu;
    @FXML
    private MenuItem nameAscending;
    @FXML
    private MenuItem nameDescending;
    @FXML
    private MenuItem dateAscending;
    @FXML
    private MenuItem dateDescending;
    @FXML
    private TextField nameFilter;
    @FXML
    private TextField displayNameFilter;
    @FXML
    private Button filterButton;
    @FXML
    private Button clearFilterButton;

//    Scene related
    private Scene scene;
    private Stage stage;
    private Parent root;

    private ObservableList<AdminUserAccount> userlist;



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
            AdminAddNewUserController addNewCtrl = fxmlLoader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            final Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            ok.addEventFilter(
                    ActionEvent.ACTION,
                    event -> {
                        if (!addNewCtrl.validate()){
                            event.consume();
                        }else {
                            if (!addNewCtrl.addNewUser()){
                                event.consume();
                            }
                            tableview.refresh();
                        }
                    }
            );
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
            AdminEditUserController ctrl =  fxmlLoader.getController();
            int index = tableview.getSelectionModel().getSelectedIndex();
            AdminUserAccount account = userlist.get(index);
            ctrl.setUser(account);
            dialog.setDialogPane(dialogPane);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.addEventFilter(ActionEvent.ACTION, event -> {
                if (!ctrl.validate()){
                    System.out.println("validate is false");
                    event.consume();
                } else {
                    userlist.set(index, ctrl.setNewInformation());
                    tableview.refresh();

                }
            });
            Optional<ButtonType> clickedButton = dialog.showAndWait();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleRemoveUser(){

        try {
            Alert removeAlert = new Alert(Alert.AlertType.CONFIRMATION);

            removeAlert.setTitle("Remove a user");
            removeAlert.setHeaderText("Removal Confirmation");
            removeAlert.setContentText("Are you sure to remove this user!!!");
            Optional<ButtonType> clickButton = removeAlert.showAndWait();
            if (clickButton.get() == ButtonType.OK){
                int index = tableview.getSelectionModel().getSelectedIndex();
                AdminUserAccount selected = userlist.get(index);
                if (AdminUserAccountDAL.deleteUser(Integer.parseInt(selected.getId()))){
                    userlist.remove(selected);
                }else{
                    removeAlert = new Alert(Alert.AlertType.ERROR);
                    removeAlert.setTitle("Cannot remove this user");
                    removeAlert.setHeaderText("Removal Error!!!");
                    removeAlert.setContentText("Something prevent this user from being removed");
                }

            }
            System.out.println(clickButton);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleLockUser(){

        try {
            int index = tableview.getSelectionModel().getSelectedIndex();
            AdminUserAccount selected = userlist.get(index);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unlock/lock a user");
            alert.setHeaderText("Action confirmation");
            alert.setContentText((selected.getStatus().equals("locked")?"Unlock ": "Lock ") + selected.getUsername());
            Optional<ButtonType> clickedButton = alert.showAndWait();
            if (clickedButton.isPresent()){
                if (clickedButton.get() ==  ButtonType.OK){
                    String newStatus = (selected.getStatus().equals("locked") ? "offline" : "locked");
                    if (AdminUserAccountDAL.updateUserStatus(Integer.parseInt(selected.getId()), newStatus)){
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Result for action - Success");
                        alert.setHeaderText(newStatus.equals("locked") ? "Lock user successfully!!!" : "Unlock user successfully!!!" );
                        selected.setStatus(newStatus);
                        tableview.refresh();
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleShowLogin(){
        try {
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("admin-user-listing-signin-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            AdminUserActivityLogController ctrl = fxmlLoader.getController();
            AdminUserAccount selected = (AdminUserAccount) tableview.getSelectionModel().getSelectedItem();
            ctrl.setUser(Integer.parseInt(selected.getId()));
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
            AdminUserAccount selected  = (AdminUserAccount) tableview.getSelectionModel().getSelectedItem();
            int userID = Integer.parseInt(selected.getId());
            DialogPane dialogPane = fxmlLoader.load();

            AdminUserListShowFriendController friendController = fxmlLoader.getController();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            friendController.loadData(userID);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleChangePassword() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-user-listing-updatepassword-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            AdminChangeUserPasswordController ctrl = fxmlLoader.getController();
            int index = tableview.getSelectionModel().getSelectedIndex();
            AdminUserAccount selected = userlist.get(index);
            ctrl.setUserID(Integer.parseInt(selected.getId()));
            dialog.setDialogPane(dialogPane);
            Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
            okButton.addEventFilter(
                    ActionEvent.ACTION,
                    event -> {
                        if (!ctrl.validate()){
                            event.consume();
                        }else if (!ctrl.changeUserPassword()) {
                            event.consume();
                        }
                    }
            );
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
        userlist = FXCollections.observableArrayList(AdminUserAccountDAL.getAllUserAccountsWithInfomation());
        System.out.println(userlist.stream().toList().toString());
//        Asscociate table collumns;
        updateButton.setDisable(true);
        changePasswordButton.setDisable(true);
        removeButton.setDisable(true);
        lockButton.setDisable(true);
        showFriendButton.setDisable(true);
        signInButton.setDisable(true);

        username.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("username"));
        displayname.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("displayName"));
        email.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("email"));
        sex.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("sex"));
        dob.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("dob"));
        address.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("address"));
        createdate.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("createDate"));
        status.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("status"));
        tableview.setItems(userlist);

        tableview.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            System.out.println(o);
            System.out.println(t1);
            if (t1 != null){
                updateButton.setDisable(false);
                changePasswordButton.setDisable(false);
                removeButton.setDisable(false);
                lockButton.setDisable(false);
                showFriendButton.setDisable(false);
                signInButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
                changePasswordButton.setDisable(true);
                removeButton.setDisable(true);
                lockButton.setDisable(true);
                showFriendButton.setDisable(true);
                signInButton.setDisable(true);
            }
        });
//    Add list items
    }



}
