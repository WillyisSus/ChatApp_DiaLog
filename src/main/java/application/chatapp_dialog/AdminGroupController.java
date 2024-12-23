package application.chatapp_dialog;

import application.chatapp_dialog.dal.AdminGroupInformationDAL;
import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.dto.AdminGroupInformation;
import application.chatapp_dialog.dto.AdminSimpleUserAccount;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AdminGroupController implements Initializable {
    private Connection connection;
    ScheduledExecutorService scheduledExecutorService;
    private class MyAutoReloadGroupInformation implements Runnable {
        @Override
        public void run() {
            try {
                if (connection != null){
                    ObservableList<AdminGroupInformation> temp =  FXCollections.observableArrayList(AdminGroupInformationDAL.getGroupInformationList(connection));
                    if (temp !=  null){
                        if(comparator != null){
                            temp.sort(comparator);
                        }
                        if (boxTable.getItems() instanceof FilteredList<AdminGroupInformation>){
                            boxTable.setItems(new FilteredList<>(temp, ((FilteredList<AdminGroupInformation>) boxTable.getItems()).getPredicate()));
                        }else {
                            boxTable.setItems(temp);
                        }
                        groupInformations = temp;
                        boxTable.refresh();
                    }

                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @FXML
    private TableView<AdminGroupInformation> boxTable;
    @FXML
    private TableColumn<AdminGroupInformation, String> boxName;
    @FXML
    private TableColumn<AdminGroupInformation, String> boxCreateDate;
    @FXML
    private TableColumn<AdminGroupInformation, String> boxAdmins;
    @FXML
    private TableColumn<AdminGroupInformation, String> boxMembers;

    private Comparator<AdminGroupInformation> comparator;
    private ObservableList<AdminGroupInformation> groupInformations;
    private ObservableList<AdminSimpleUserAccount> memberInGroups;
//    Admin table
    @FXML
    private TableView<AdminSimpleUserAccount> adminTableView;
    @FXML
    private TableColumn<AdminSimpleUserAccount, String> adminUsername;
    @FXML
    private TableColumn<AdminSimpleUserAccount, String> adminDisplayname;
//    Member table
    @FXML
    private TableView<AdminSimpleUserAccount> memberTableView;
    @FXML
    private TableColumn<AdminSimpleUserAccount, String> memberUsername;
    @FXML
    private TableColumn<AdminSimpleUserAccount, String> memberDisplayname;

//    Ordering and filter
    @FXML
    private TextField groupNameFilter;
    @FXML
    private Button filterGroupName;
    @FXML
    private Button clearFilter;
    @FXML
    private MenuButton orderMenu;
    @FXML
    private MenuItem dateAscending;
    @FXML
    private MenuItem dateDescending;
    @FXML
    private MenuItem boxNameAscending;
    @FXML
    private MenuItem boxNameDescending;

    @FXML
    private TabPane mytabpane;

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
    public void switchToLogin(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-login.fxml"));
            Parent root = loader.load();
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            handleCloseStage();
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
            handleCloseStage();
            ctrl.setStageAndCloseHandler(stage);
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
            handleCloseStage();
            stage.setScene(scene);
            ctrl.setStageAndCloseHandler(stage);
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
            handleCloseStage();
            stage.setScene(scene);
//            ctrl.setStageAndCloseHandler(stage);
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
            handleCloseStage();
            stage.setScene(scene);
            ctrl.setStageAndCloseHandler(stage);
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
            handleCloseStage();
            stage.setScene(scene);
            ctrl.setStageAndCloseHandler(stage);
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
            handleCloseStage();
            stage.setScene(scene);
            ctrl.setStageAndCloseHandler(stage);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void handleFilter(ActionEvent event){
        if (event.getSource() ==  filterGroupName){
            FilteredList<AdminGroupInformation>  filteredList = new FilteredList<AdminGroupInformation>(groupInformations, data->{
                boolean filtered = true;
                if (!groupNameFilter.getText().isEmpty()){
                    filtered = data.getGroupName().startsWith(groupNameFilter.getText());
                }
                return filtered;
            });
            boxTable.setItems(filteredList);
            boxTable.refresh();
        }else {
            boxTable.setItems(groupInformations);
            boxTable.refresh();
        }


    }

    public void handleSortBoxTable(ActionEvent event){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (event.getSource() == dateAscending){
                    orderMenu.setText(dateAscending.getText());
                    comparator = AdminGroupInformationDAL.getDateAscendingComparator();
                    groupInformations.sort(AdminGroupInformationDAL.getDateAscendingComparator());
                } else if(event.getSource() == dateDescending){
                    orderMenu.setText(dateDescending.getText());
                    comparator = AdminGroupInformationDAL.getDateDescendingComparator();
                    groupInformations.sort(AdminGroupInformationDAL.getDateDescendingComparator());
                } else if (event.getSource() == boxNameAscending){
                    orderMenu.setText(boxNameAscending.getText());
                    comparator = AdminGroupInformationDAL.getBoxNameAscendingComparator();
                    groupInformations.sort(AdminGroupInformationDAL.getBoxNameAscendingComparator());
                } else {
                    orderMenu.setText(boxNameDescending.getText());
                    comparator = AdminGroupInformationDAL.getBoxNameDescendingComparator();
                    groupInformations.sort(AdminGroupInformationDAL.getBoxNameDescendingComparator());
                }
                boxTable.refresh();
            }
        });

    }

    public void showMemberAndAdminListOfGroup(AdminGroupInformation groupInformation){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    memberInGroups = FXCollections.observableArrayList(AdminGroupInformationDAL.getMemberOfGroup(groupInformation, connection));
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Database error");
                    alert.setHeaderText("Cannot get group member data!!!");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
                memberTableView.setItems(memberInGroups);
                memberTableView.refresh();

                adminTableView.setItems(new FilteredList<AdminSimpleUserAccount>(memberInGroups, AdminSimpleUserAccount::getAdmin));
                adminTableView.refresh();
            }
        });

    }
    public void setConnection(Connection conn){
        connection = conn;
    }
    public void setStageAndCloseHandler(Stage stage){
        this.stage = stage;
        stage.setOnCloseRequest(windowEvent -> this.handleCloseStage());
    }
    public void handleCloseStage(){
        scheduledExecutorService.shutdown();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                toGroupViewButton.requestFocus();
            }
        });
//        connection = UtilityDAL.getConnection();
        comparator = null;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new MyAutoReloadGroupInformation(), 0, 1000, TimeUnit.MILLISECONDS);
        boxName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGroupName()));
        boxCreateDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreateDate().toString()));
        boxAdmins.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdmins().toString()));
        boxMembers.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMembers().toString()));
        try {
            groupInformations = FXCollections.observableArrayList(AdminGroupInformationDAL.getGroupInformationList(connection));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        boxTable.setItems(groupInformations);

        adminUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        adminDisplayname.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDisplayName()));

        memberUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        memberDisplayname.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDisplayName()));

        boxTable.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            if (t1 != null){
                showMemberAndAdminListOfGroup(t1);
            } else {
                memberInGroups.clear();
                adminTableView.getItems().clear();
                memberTableView.getItems().clear();
            }
        });
    }
}
