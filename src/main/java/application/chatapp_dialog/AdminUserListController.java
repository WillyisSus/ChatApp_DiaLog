package application.chatapp_dialog;


import application.chatapp_dialog.admin.modalcontroller.*;
import application.chatapp_dialog.dal.AdminActivityLogDAL;
import application.chatapp_dialog.dal.AdminUserAccountDAL;
import application.chatapp_dialog.dal.UtilityDAL;
import com.almasb.fxgl.scene3d.DoorComponent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import java.net.ServerSocket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

import application.chatapp_dialog.dto.*;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.w3c.dom.html.HTMLTableElement;

import javax.swing.*;

public class AdminUserListController implements Initializable {
//    Table related
    @FXML
    private TableView<AdminUserAccount> tableview;
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

    @FXML
    private Button reloadData;
//    Filter and ordering

    @FXML
    private ChoiceBox<String> statusFilter;
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


    @FXML
    private Tab userAccounts;
    @FXML
    private Tab latestLogins;
    @FXML
    private Tab userFriends;

//    Latest login tab
// Table
    @FXML
    private TableView<AdminUserActivityLog> activityLogTableView;
    @FXML
    private TableColumn<AdminUserActivityLog, String> activityDateTime;
    @FXML
    private TableColumn<AdminUserActivityLog, String> activityUsername;
    @FXML
    private TableColumn<AdminUserActivityLog, String> activityDisplayName;
    //Order Choice
    @FXML
    private MenuButton activityOrder;
    @FXML
    private MenuItem activityDateAscending;
    @FXML
    private MenuItem activityDateDescending;
    @FXML
    private MenuItem activityUsernameAscending;
    @FXML
    private MenuItem activityUsernameDescending;
    @FXML
    private Button reloadActivityLogs;
//   Data container;
    private ObservableList<AdminUserActivityLog> activityLogs;
// End of Login Tab

//    Friendlist tabs

    @FXML
    private TableView<AdminUserFriendCount> friendCountTableView;
    @FXML
    private TableColumn<AdminUserFriendCount, String> friendCountUsername;
    @FXML
    private TableColumn<AdminUserFriendCount, String> friendCountDisplayName;
    @FXML
    private TableColumn<AdminUserFriendCount, Integer> friendCountDirectFriend;
    @FXML
    private TableColumn<AdminUserFriendCount, Integer> friendCountIndirectFriend;
    @FXML
    private TableColumn<AdminUserFriendCount, String> friendCountCreateDate;

    private ObservableList<AdminUserFriendCount> friendCounts;
//    Filter field
    @FXML
    private TextField friendCountUsernameFilter;
    @FXML
    private TextField maxDirectFriendCountFilter;
    @FXML
    private TextField minDirectFriendCountFilter;

//    End of Friend list Tabs
    //    Scene related
    private Scene scene;
    private Stage stage;
    private Parent root;

    private ObservableList<AdminUserAccount> userlist;
    private FilteredList<AdminUserAccount> filteredUserList;


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

    public void handleReloadData(){
        try {
            userlist = FXCollections.observableArrayList(AdminUserAccountDAL.getAllUserAccountsWithInfomation());
            tableview.setItems(userlist);
            tableview.refresh();
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
                    AdminUserAccount newAccount = ctrl.setNewInformation();
                    account.copyDataFromOtherAccount(newAccount);
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
                AdminUserAccount selected = tableview.getItems().get(index);
                if (AdminUserAccountDAL.deleteUser(Integer.parseInt(selected.getId()))){
                    tableview.getItems().remove(selected);
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
                        alert.showAndWait();
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
            AdminUserAccount selected = tableview.getSelectionModel().getSelectedItem();
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
            AdminUserAccount selected  = tableview.getSelectionModel().getSelectedItem();
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
            AdminUserAccount selected = tableview.getItems().get(index);
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

    public void handleFilter(ActionEvent event){
        if (event.getSource() == filterButton){

            FilteredList<AdminUserAccount> filteredList = new FilteredList<AdminUserAccount>(userlist, i->{
                boolean filteredByUsername = true;
                boolean filteredByDisplayName = true;
                boolean filteredByStatus = true;
                if (!displayNameFilter.getText().isEmpty()){
                    filteredByDisplayName =  i.getDisplayName().startsWith(displayNameFilter.getText());
                }
                if (!nameFilter.getText().isEmpty()){
                    filteredByUsername = i.getUsername().startsWith(nameFilter.getText());
                }
                String chosenStatus = statusFilter.getValue();
                if (!chosenStatus.equals(statusFilter.getItems().getFirst())){
                    filteredByStatus = i.getStatus().equals(chosenStatus.toLowerCase());
                }
                System.out.println((filteredByUsername && filteredByDisplayName));
                return (filteredByUsername && filteredByDisplayName && filteredByStatus);
            });
            tableview.setItems(filteredList);
            tableview.refresh();
      

        }
        else if (event.getSource() == clearFilterButton){
            tableview.setItems(userlist);
            nameFilter.clear();
            displayNameFilter.clear();
            statusFilter.setValue(statusFilter.getItems().getFirst());
            tableview.refresh();
        }
    }

    public void handleSortByUsernameAscending(){
        orderMenu.setText(nameAscending.getText());
        tableview.getItems().sort(AdminUserAccountDAL.getNameComparatorAscending());
        tableview.refresh();
    }

    public void handleSortByUsernameDescending(){
        orderMenu.setText(nameDescending.getText());
        tableview.getItems().sort(AdminUserAccountDAL.getNameComparatorDescending());
        tableview.refresh();
    }

    public void handleSortByCreateDateAscending(){
        orderMenu.setText(dateAscending.getText());
        tableview.getItems().sort(AdminUserAccountDAL.getCreateDateComparatorAscending());
        tableview.refresh();
    }

    public void handleSortByCreateDateDescending(){
        orderMenu.setText(dateDescending.getText());
        tableview.getItems().sort(AdminUserAccountDAL.getCreateDateComparatorDescending());
        tableview.refresh();
    }

    public void handlerReloadActivityLogs(){
        try {
            activityLogs = FXCollections.observableArrayList(AdminActivityLogDAL.getAllUserActivityLog(null));
            activityLogTableView.setItems(activityLogs);
            activityLogTableView.refresh();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
//    Latest Logins functions
    public void handleSortActivityLogs(ActionEvent event){
        if (event.getSource() == activityDateAscending){
            activityLogTableView.getItems().sort(AdminActivityLogDAL.getAscendingComparator());
            activityOrder.setText(activityDateAscending.getText());
            activityLogTableView.refresh();
        }else if (event.getSource() == activityDateDescending){
            activityLogTableView.getItems().sort(AdminActivityLogDAL.getDescendingComparator());
            activityOrder.setText(activityDateDescending.getText());
            activityLogTableView.refresh();
        } else if (event.getSource() == activityUsernameAscending){
            activityLogTableView.getItems().sort(new Comparator<AdminUserActivityLog>() {
                @Override
                public int compare(AdminUserActivityLog o1, AdminUserActivityLog o2) {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            });
            activityOrder.setText(activityUsernameAscending.getText());
            activityLogTableView.refresh();
        } else{
            activityLogTableView.getItems().sort(new Comparator<AdminUserActivityLog>() {
                @Override
                public int compare(AdminUserActivityLog o1, AdminUserActivityLog o2) {
                    return o2.getUsername().compareTo(o1.getUsername());
                }
            });
            activityOrder.setText(activityUsernameDescending.getText());
            activityLogTableView.refresh();
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
        filteredUserList = null;
        userlist = FXCollections.observableArrayList(AdminUserAccountDAL.getAllUserAccountsWithInfomation());
        updateButton.setDisable(true);
        changePasswordButton.setDisable(true);
        removeButton.setDisable(true);
        lockButton.setDisable(true);
        showFriendButton.setDisable(true);
        signInButton.setDisable(true);

        System.out.println(userlist.stream().toList().toString());
//        Asscociate table collumns;


        username.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("username"));
        displayname.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("displayName"));
        email.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("email"));
        sex.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("sex"));
        dob.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("dob"));
        address.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("address"));
        createdate.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("createDate"));
        status.setCellValueFactory(new PropertyValueFactory<AdminUserAccount, String>("status"));
        tableview.setItems(userlist);

//       Tab latest login
        activityDateTime.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSessionStart()));
        activityUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        activityDisplayName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDisplayName()));
        activityLogs = null;

        orderMenu.setText(nameAscending.getText());




        statusFilter.getItems().addAll("All status", "Offline", "Online", "Locked");
        statusFilter.setValue(statusFilter.getItems().getFirst());

        latestLogins.setOnSelectionChanged((EventHandler<Event>) t->{
            if(latestLogins.isSelected() && activityLogTableView.getItems().isEmpty()){
                System.out.println("???");

                try {
                    activityLogs = FXCollections.observableArrayList(AdminActivityLogDAL.getAllUserActivityLog(null));
                    activityLogTableView.setItems(activityLogs);
                    activityLogTableView.refresh();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database ERROR");
                    alert.setHeaderText("CANNOT GET DATA!!");
                    alert.setContentText("Something prevents client from getting data.");
                    alert.showAndWait();
                }
            }
        });



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
