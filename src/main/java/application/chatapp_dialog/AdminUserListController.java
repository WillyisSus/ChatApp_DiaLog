package application.chatapp_dialog;


import application.chatapp_dialog.admin.modalcontroller.*;
import application.chatapp_dialog.dal.*;
import application.chatapp_dialog.dummy.UserAccountGenerator;
import javafx.application.Platform;
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

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import application.chatapp_dialog.dto.*;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.w3c.dom.html.HTMLTableElement;


public class AdminUserListController implements Initializable {
    private int adminID = -1;
    private Connection connection;
    private Comparator<AdminUserAccount> userAccountComparator;
    private Comparator<AdminUserActivityLog> userActivityLogComparator;
    private Comparator<AdminUserFriendCount> userFriendCountComparator;
    class MyAutoReloadUserList implements Runnable{
        @Override
        public void run() {
            try {
                if (connection != null){
                    ObservableList<AdminUserAccount> temp =  FXCollections.observableArrayList(AdminUserAccountDAL.getAllUserAccountsWithInfomation(connection));
                    if(userAccountComparator != null){
                        temp.sort(userAccountComparator);
                    }
                    if (tableview.getItems() instanceof FilteredList<AdminUserAccount>){
                        tableview.setItems(new FilteredList<>(temp, ((FilteredList<AdminUserAccount>) tableview.getItems()).getPredicate()));
                    }else {
                        tableview.setItems(temp);
                    }
                    userlist = temp;
                    tableview.refresh();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    class MyAutoReloadUserActivity implements Runnable{
        @Override
        public void run() {
            try {
                if (connection != null){
                    ObservableList<AdminUserActivityLog> temp =  FXCollections.observableArrayList(AdminActivityLogDAL.getAllUserActivityLog(null, connection));
                    if(userActivityLogComparator != null){
                        temp.sort(userActivityLogComparator);
                    }
                    if (activityLogTableView.getItems() instanceof FilteredList<AdminUserActivityLog>){
                        activityLogTableView.setItems(new FilteredList<>(temp, ((FilteredList<AdminUserActivityLog>) activityLogTableView.getItems()).getPredicate()));
                    }else {
                        activityLogTableView.setItems(temp);
                    }
                    activityLogs = temp;
                    tableview.refresh();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    class MyAutoReloadUserFriendCount implements Runnable{
        @Override
        public void run() {
            try {
                if (connection != null){
                    ObservableList<AdminUserFriendCount> temp =  FXCollections.observableArrayList(AdminUserFriendCountDAL.getUserDirectAndIndirectFriendCount(connection));
                    if(userFriendCountComparator != null){
                        temp.sort(userFriendCountComparator);
                    }
                    if (friendCountTableView.getItems() instanceof FilteredList<AdminUserFriendCount>){
                        friendCountTableView.setItems(new FilteredList<>(temp, ((FilteredList<AdminUserFriendCount>) friendCountTableView.getItems()).getPredicate()));
                    }else {
                        friendCountTableView.setItems(temp);
                    }
                    friendCounts = temp;
                    tableview.refresh();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
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
    private Button resetUserPasswordButton;
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
    private TableColumn<AdminUserFriendCount, String> friendCountDirectFriend;
    @FXML
    private TableColumn<AdminUserFriendCount, String> friendCountIndirectFriend;
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
//  Button
    @FXML
    private Button filterFriendTable;
    @FXML
    private Button clearFilterFriendTable;
    @FXML
    private MenuButton friendCountOrderMenu;
    @FXML
    private MenuItem friendAscendingByDate;
    @FXML
    private MenuItem friendDescendingByDate;
    @FXML
    private MenuItem friendDescendingByUsername;
    @FXML
    private MenuItem FriendAscendingByUsername;

    @FXML
    private Button friendReloadData;
//    End of Friend list Tabs
    //    Scene related
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

//    Private attributes
    private ObservableList<AdminUserAccount> userlist;
    private FilteredList<AdminUserAccount> filteredUserList;

    public void setAdminID(int id){
        this.adminID = id;
    }
    public void switchToLogin(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-login.fxml"));
            Parent root = loader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void switchToUser(ActionEvent event){
        try{
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("admin-user-listing-view.fxml"));
            Parent root = loader.load();
            AdminUserListController ctrl = (AdminUserListController) loader.getController();
            ctrl.setConnection(connection);
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
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("admin-activeuser-view.fxml"));
            Parent root = loader.load();
            AdminActiveUserController ctrl = (AdminActiveUserController) loader.getController();
            ctrl.setConnection(connection);
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
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("admin-graph-view.fxml"));
            Parent root = loader.load();
            AdminGraphController ctrl = (AdminGraphController) loader.getController();
            ctrl.setConnection(connection);
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
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("admin-group-view.fxml"));
            Parent root = loader.load();
            AdminGroupController ctrl = (AdminGroupController) loader.getController();
            ctrl.setConnection(connection);
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
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("admin-report-view.fxml"));
            Parent root = loader.load();
            AdminReportController ctrl = (AdminReportController) loader.getController();
            ctrl.setConnection(connection);
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
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("admin-newuser-view.fxml"));
            Parent root = loader.load();
            AdminNewUserController ctrl = (AdminNewUserController) loader.getController();
            ctrl.setConnection(connection);
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
            addNewCtrl.setConnection(connection);
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    userlist = FXCollections.observableArrayList(AdminUserAccountDAL.getAllUserAccountsWithInfomation(connection));
                    tableview.setItems(userlist);
                    tableview.refresh();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });

    }

    public void handleUpdateUser(){

        try {
            FXMLLoader fxmlLoader =  new FXMLLoader(getClass().getResource("admin-user-listing-update-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            AdminEditUserController ctrl =  fxmlLoader.getController();
            ctrl.setConnection(connection);
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
                    userlist.set(index, newAccount);
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
                if (AdminUserAccountDAL.deleteUser(Integer.parseInt(selected.getId()), connection)){
                    userlist.remove(selected);
                    tableview.refresh();
                }else{
                    removeAlert = new Alert(Alert.AlertType.ERROR);
                    removeAlert.setTitle("Cannot remove this user");
                    removeAlert.setHeaderText("Removal Error!!!");
                    removeAlert.setContentText("Something prevent this user from being removed");
                }

            }
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
                    if (AdminUserAccountDAL.updateUserStatus(Integer.parseInt(selected.getId()), newStatus, connection)){
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
            ctrl.setConnection(connection);
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
            friendController.setConnection(connection);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            friendController.loadData(userID);

            Optional<ButtonType> clickedButton = dialog.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void handleResetUserPassword(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        AdminUserAccount selected = tableview.getSelectionModel().getSelectedItem();
        alert.setTitle("Action confirmation!!!");
        alert.setHeaderText("Are you sure to reset password of this account");
        Optional<ButtonType> clicked = alert.showAndWait();
        if (clicked.isPresent()){
            if (clicked.get() == ButtonType.OK){
                String newPass = UserAccountGenerator.randomPassword();
                try {
                    AdminUserAccountDAL.updatePassword(Integer.parseInt(selected.getId()), newPass, connection);
                    EmailDAL.sendNewPassword(selected.getEmail(), newPass);
                    tableview.refresh();
                } catch (SQLException e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Can not reset password of this user");
                    error.showAndWait();
                }
            }
        }
    }
    public void handleChangePassword() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-user-listing-updatepassword-dialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            AdminChangeUserPasswordController ctrl = fxmlLoader.getController();
            ctrl.setConnection(connection);
            int index = tableview.getSelectionModel().getSelectedIndex();
            AdminUserAccount selected = tableview.getItems().get(index);
            ctrl.setUserID(selected);
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
        });


    }

    public void handleSortByUsernameAscending(){
        orderMenu.setText(nameAscending.getText());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userlist.sort(AdminUserAccountDAL.getNameComparatorAscending());
                userAccountComparator = AdminUserAccountDAL.getNameComparatorAscending();
                tableview.refresh();
            }
        });

    }

    public void handleSortByUsernameDescending(){
        orderMenu.setText(nameDescending.getText());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userlist.sort(AdminUserAccountDAL.getNameComparatorDescending());
                userAccountComparator = AdminUserAccountDAL.getNameComparatorDescending();
                tableview.refresh();
            }
        });

    }

    public void handleSortByCreateDateAscending(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userlist.sort(AdminUserAccountDAL.getCreateDateComparatorAscending());
                userAccountComparator = AdminUserAccountDAL.getCreateDateComparatorAscending();

                tableview.refresh();
            }
        });
        orderMenu.setText(dateAscending.getText());

    }

    public void handleSortByCreateDateDescending(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userlist.sort(AdminUserAccountDAL.getCreateDateComparatorDescending());
                userAccountComparator = AdminUserAccountDAL.getCreateDateComparatorDescending();
                tableview.refresh();
            }
        });
        orderMenu.setText(dateDescending.getText());

    }


//    Latest Logins functions
    public void handleSortActivityLogs(ActionEvent event){
        if (event.getSource() == activityDateAscending){
            activityLogs.sort(AdminActivityLogDAL.getAscendingComparator());
            userActivityLogComparator = AdminActivityLogDAL.getAscendingComparator();
            activityOrder.setText(activityDateAscending.getText());
            activityLogTableView.refresh();
        }else if (event.getSource() == activityDateDescending){
            activityLogs.sort(AdminActivityLogDAL.getDescendingComparator());
            userActivityLogComparator = AdminActivityLogDAL.getDescendingComparator();
            activityOrder.setText(activityDateDescending.getText());
            activityLogTableView.refresh();
        } else if (event.getSource() == activityUsernameAscending){
            activityLogs.sort(new Comparator<AdminUserActivityLog>() {
                @Override
                public int compare(AdminUserActivityLog o1, AdminUserActivityLog o2) {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            });
            userActivityLogComparator = new Comparator<AdminUserActivityLog>() {
                @Override
                public int compare(AdminUserActivityLog o1, AdminUserActivityLog o2) {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            };

            activityOrder.setText(activityUsernameAscending.getText());
            activityLogTableView.refresh();
        } else{
            activityLogs.sort(new Comparator<AdminUserActivityLog>() {
                @Override
                public int compare(AdminUserActivityLog o1, AdminUserActivityLog o2) {
                    return o2.getUsername().compareTo(o1.getUsername());
                }
            });
            userActivityLogComparator =  new Comparator<AdminUserActivityLog>() {
                @Override
                public int compare(AdminUserActivityLog o1, AdminUserActivityLog o2) {
                    return o2.getUsername().compareTo(o1.getUsername());
                }
            };
            activityOrder.setText(activityUsernameDescending.getText());
            activityLogTableView.refresh();
        }
    }
    public void handlerReloadActivityLogs(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    activityLogs = FXCollections.observableArrayList(AdminActivityLogDAL.getAllUserActivityLog(null, connection));
                    activityLogTableView.setItems(activityLogs);
                    activityLogTableView.refresh();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

    }
// Friend Count Function;
    public void handleFilterUserFriendCount(ActionEvent event){
        if (event.getSource() == filterFriendTable){
            FilteredList<AdminUserFriendCount> filter = new FilteredList<AdminUserFriendCount>(friendCounts, data->{
                boolean filteredByUsername = true;
                boolean filteredByMaxDirect = true;
                boolean filteredByMinDirect = true;
                try {
                    if (!friendCountUsernameFilter.getText().isEmpty()){
                        filteredByUsername = data.getUsername().startsWith(friendCountUsernameFilter.getText());
                    }
                    if (!maxDirectFriendCountFilter.getText().isEmpty()){
                        filteredByMaxDirect = data.getDirectFriends() <= Integer.parseInt(maxDirectFriendCountFilter.getText());
                    }
                    if (!minDirectFriendCountFilter.getText().isEmpty()){
                        filteredByMinDirect = data.getDirectFriends() >= Integer.parseInt(minDirectFriendCountFilter.getText());
                    }
                } catch (NumberFormatException exception){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("INVALID INPUT");
                    alert.setContentText("Please only enter integers in Min Direct and Max Direct field.");
                    alert.setHeaderText("Wrong number format!!!");
                    alert.showAndWait();
                }
                return (filteredByMaxDirect && filteredByUsername && filteredByMinDirect);
            });

            friendCountTableView.setItems(filter);
            friendCountTableView.refresh();
        } else {
            friendCountTableView.setItems(friendCounts);
            friendCountTableView.refresh();
        }
    }

    public void handleSortUserFriendCount(ActionEvent event){
        if (event.getSource() == friendAscendingByDate){
            friendCountOrderMenu.setText(friendAscendingByDate.getText());
            userFriendCountComparator = AdminUserFriendCountDAL.getCreateDateAscendingComparator();
            friendCounts.sort(AdminUserFriendCountDAL.getCreateDateAscendingComparator());
        }else if (event.getSource() == friendDescendingByDate){
            friendCountOrderMenu.setText(friendDescendingByDate.getText());
            userFriendCountComparator = AdminUserFriendCountDAL.getCreateDateDescendingComparator();
            friendCounts.sort(AdminUserFriendCountDAL.getCreateDateDescendingComparator());
        }else if (event.getSource() == friendDescendingByUsername){
            friendCountOrderMenu.setText(friendDescendingByUsername.getText());
            userFriendCountComparator = AdminUserFriendCountDAL.getUsernameDescendingComparator();
            friendCounts.sort(AdminUserFriendCountDAL.getUsernameDescendingComparator());
        } else if (event.getSource() == FriendAscendingByUsername){
            friendCountOrderMenu.setText(FriendAscendingByUsername.getText());
            userFriendCountComparator = AdminUserFriendCountDAL.getUsernameAscendingComparator();
            friendCounts.sort(AdminUserFriendCountDAL.getUsernameAscendingComparator());
        }
        friendCountTableView.refresh();

    }

    public void handleReloadFriendCountData(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    friendCounts = FXCollections.observableArrayList(AdminUserFriendCountDAL.getUserDirectAndIndirectFriendCount(connection));
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database ERROR");
                    alert.setHeaderText("CANNOT GET DATA!!");
                    alert.setContentText("Something prevents client from getting data.");
                    alert.showAndWait();
                }
                friendCountTableView.setItems(friendCounts);
                friendCountTableView.refresh();
            }
        });

    }

//    end of Friend count function
    public void setConnection(Connection conn){
        connection = conn;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                toUserViewButton.requestFocus();
            }
        });
//        connection = UtilityDAL.getConnection();
        userAccountComparator = null;
        userActivityLogComparator = null;
        userFriendCountComparator = null;
        ScheduledExecutorService scheduledExecutorServiceUserList = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorServiceUserList.scheduleAtFixedRate(new MyAutoReloadUserList(), 0, 1000, TimeUnit.MILLISECONDS);
        filteredUserList = null;
        userlist = FXCollections.observableArrayList(AdminUserAccountDAL.getAllUserAccountsWithInfomation(connection));
        updateButton.setDisable(true);
        changePasswordButton.setDisable(true);
        removeButton.setDisable(true);
        lockButton.setDisable(true);
        showFriendButton.setDisable(true);
        signInButton.setDisable(true);
        resetUserPasswordButton.setDisable(true);

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
        tableview.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {

            if (t1 != null){
                updateButton.setDisable(false);
                changePasswordButton.setDisable(false);
                removeButton.setDisable(false);
                lockButton.setDisable(false);
                showFriendButton.setDisable(false);
                signInButton.setDisable(false);
                resetUserPasswordButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
                changePasswordButton.setDisable(true);
                removeButton.setDisable(true);
                lockButton.setDisable(true);
                showFriendButton.setDisable(true);
                signInButton.setDisable(true);
                resetUserPasswordButton.setDisable(true);
            }
        });
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

                try {
                    ScheduledExecutorService activityThread = Executors.newSingleThreadScheduledExecutor();
                    activityThread.scheduleAtFixedRate(new MyAutoReloadUserActivity(), 0, 1000, TimeUnit.MILLISECONDS);
                    activityLogs = FXCollections.observableArrayList(AdminActivityLogDAL.getAllUserActivityLog(null, connection));
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

//        Friend counts table view
        friendCountUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        friendCountDisplayName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDisplayName()));
        friendCountDirectFriend.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDirectFriends().toString()));
        friendCountIndirectFriend.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIndirectFriends().toString()));
        friendCountCreateDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreateDate().toString()));
        userFriends.setOnSelectionChanged((EventHandler<Event>) t->{
            if(userFriends.isSelected() && friendCountTableView.getItems().isEmpty()){

                try {
                    ScheduledExecutorService friendCountThread = Executors.newSingleThreadScheduledExecutor();
                    friendCountThread.scheduleAtFixedRate(new MyAutoReloadUserFriendCount(), 0, 1000, TimeUnit.MILLISECONDS);
                    friendCounts = FXCollections.observableArrayList(AdminUserFriendCountDAL.getUserDirectAndIndirectFriendCount(connection));
                    friendCountTableView.setItems(friendCounts);
                    friendCountTableView.refresh();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database ERROR");
                    alert.setHeaderText("CANNOT GET DATA!!");
                    alert.setContentText("Something prevents client from getting data.");
                    alert.showAndWait();
                }
            }
        });
//    Add list items
    }



}
