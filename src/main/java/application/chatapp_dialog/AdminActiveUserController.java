package application.chatapp_dialog;
import application.chatapp_dialog.dal.AdminActiveUserInformationDAL;
import application.chatapp_dialog.dal.AdminReportInformationDAL;
import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.dto.AdminActiveUserInformation;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AdminActiveUserController implements Initializable {
    private Connection connection;
    ScheduledExecutorService executorService;
    private class MyAutoReloadActiveUser implements Runnable{
        @Override
        public void run() {
            try {
                if (connection != null){
                    ObservableList<AdminActiveUserInformation> temp =  FXCollections.observableArrayList(AdminActiveUserInformationDAL.getAciveUserInformations(connection));
                    if (temp != null){
                        if(comparator != null){
                            temp.sort(comparator);
                        }
                        if (tableView.getItems() instanceof FilteredList<AdminActiveUserInformation>){
                            tableView.setItems(new FilteredList<>(temp, ((FilteredList<AdminActiveUserInformation>) tableView.getItems()).getPredicate()));
                        }else {
                            tableView.setItems(temp);
                        }
                        activeUserInformations = temp;
                        tableView.refresh();
                    }

                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
//    Table property
    @FXML
    private TableView<AdminActiveUserInformation> tableView;
    @FXML
    private TableColumn<AdminActiveUserInformation, String> usernameColumn;
    @FXML
    private TableColumn<AdminActiveUserInformation, String> emailColumn;
    @FXML
    private TableColumn<AdminActiveUserInformation, String> loginsColumn;
    @FXML
    private TableColumn<AdminActiveUserInformation, String> privateChatsColumn;
    @FXML
    private TableColumn<AdminActiveUserInformation, String> groupChatColumn;
    @FXML
    private TableColumn<AdminActiveUserInformation, String> createDateColumn;

    private Comparator<AdminActiveUserInformation> comparator;
    private ObservableList<AdminActiveUserInformation> activeUserInformations;
//    Filter property
    @FXML
    private DatePicker minDate;
    @FXML
    private DatePicker maxDate;
    @FXML
    private TextField  usernameFilter;
    @FXML
    private TextField  maxLogins;
    @FXML
    private TextField  minLogins;
    @FXML
    private TextField  maxGroupChats;
    @FXML
    private TextField  minGroupChats;
    @FXML
    private TextField  maxPrivateChats;
    @FXML
    private TextField  minPrivateChats;
    @FXML
    private Button filterButton;
    @FXML
    private Button clearFilterButton;

//    Button property
    @FXML
    private MenuButton orderMenu;
    @FXML
    private MenuItem usernameAscending;
    @FXML
    private MenuItem usernameDescending;
    @FXML
    private MenuItem createDateAscending;
    @FXML
    private MenuItem createDateDescending;


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
            stage.setScene(scene);
            handleCloseStage();
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
            ctrl.setStageAndCloseHandler(stage);
            handleCloseStage();
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
            ctrl.setStageAndCloseHandler(stage);
            handleCloseStage();
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
//            ctrl.setStageAndCloseHandler(stage);
            handleCloseStage();
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
            handleCloseStage();
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
            stage.setScene(scene);
            handleCloseStage();
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
            stage.setScene(scene);
            handleCloseStage();
            ctrl.setStageAndCloseHandler(stage);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
// Handle Filter
    public void handleFilter(ActionEvent event){
        if (event.getSource() ==  filterButton){
            if (minDate.getValue() == null && maxDate.getValue() == null && usernameFilter.getText().isEmpty()
                    && maxLogins.getText().isEmpty() && minLogins.getText().isEmpty() && maxPrivateChats.getText().isEmpty() && minPrivateChats.getText().isEmpty()
                    && maxGroupChats.getText().isEmpty() && minGroupChats.getText().isEmpty())
                return;
            FilteredList<AdminActiveUserInformation> filterList = new FilteredList<AdminActiveUserInformation>(activeUserInformations, data -> {
                boolean filterByDate = true;
                boolean filterByName = true;
                boolean filterByLogins = true;
                boolean filterByPrivateChats = true;
                boolean filterByGroupChats = true;
                try{
                    if (minDate.getValue()!=null){
                        filterByDate = filterByDate && (minDate.getValue().isBefore(data.getCreateDate().toLocalDateTime().toLocalDate()) || minDate.getValue().isEqual(data.getCreateDate().toLocalDateTime().toLocalDate()));
                    }
                    if (maxDate.getValue()!=null){
                        filterByDate = filterByDate && (maxDate.getValue().isAfter(data.getCreateDate().toLocalDateTime().toLocalDate()) || maxDate.getValue().isEqual(data.getCreateDate().toLocalDateTime().toLocalDate()));
                    }
                    if (!maxLogins.getText().isEmpty()){
                        filterByLogins = filterByLogins && Integer.parseInt(maxLogins.getText()) >= data.getLogins();
                    }
                    if (!minLogins.getText().isEmpty()){
                        filterByLogins = filterByLogins && Integer.parseInt(minLogins.getText()) <= data.getLogins();
                    }
                    if (!maxGroupChats.getText().isEmpty()){
                        filterByGroupChats = filterByGroupChats && Integer.parseInt(maxGroupChats.getText()) >= data.getGroupChats();
                    }
                    if (!minGroupChats.getText().isEmpty()){
                        filterByGroupChats = filterByGroupChats && Integer.parseInt(minGroupChats.getText()) <= data.getGroupChats();
                    }
                    if (!maxPrivateChats.getText().isEmpty()){
                        filterByPrivateChats = filterByPrivateChats && Integer.parseInt(maxPrivateChats.getText()) >= data.getPrivateChats();
                    }
                    if (!minPrivateChats.getText().isEmpty()){
                        filterByPrivateChats = filterByPrivateChats && Integer.parseInt(minPrivateChats.getText()) <= data.getPrivateChats();
                    }
                    if (!usernameFilter.getText().isEmpty()){
                        filterByName = data.getUsername().startsWith(usernameFilter.getText());
                    }
                } catch (NumberFormatException e){

                }
                return (filterByDate && filterByName && filterByLogins && filterByPrivateChats && filterByGroupChats);
            });
            tableView.setItems(filterList);
            tableView.refresh();
        }else {
            usernameFilter.setText("");
            minDate.setValue(null);
            maxDate.setValue(null);
            maxLogins.setText("");
            minLogins.setText("");
            maxGroupChats.setText("");
            minGroupChats.setText("");
            maxPrivateChats.setText("");
            maxPrivateChats.setText("");
            tableView.setItems(activeUserInformations);
            tableView.refresh();
        }

    }

    public void handleSort(ActionEvent event){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (event.getSource() == createDateAscending){
                    orderMenu.setText(createDateAscending.getText());
                    comparator = AdminActiveUserInformationDAL.getDateAscendingComparator();
                    activeUserInformations.sort(AdminActiveUserInformationDAL.getDateAscendingComparator());
                } else if(event.getSource() == createDateDescending){
                    orderMenu.setText(createDateDescending.getText());
                    comparator = AdminActiveUserInformationDAL.getDateDescendingComparator();
                    activeUserInformations.sort(AdminActiveUserInformationDAL.getDateDescendingComparator());
                } else if (event.getSource() == usernameAscending){
                    orderMenu.setText(usernameAscending.getText());
                    comparator = AdminActiveUserInformationDAL.getUsernameAscendingComparator();
                    activeUserInformations.sort(AdminActiveUserInformationDAL.getUsernameAscendingComparator());
                } else if (event.getSource() == usernameDescending){
                    orderMenu.setText(usernameDescending.getText());
                    comparator = AdminActiveUserInformationDAL.getUsernameDescendingComparator();
                    activeUserInformations.sort(AdminActiveUserInformationDAL.getUsernameDescendingComparator());
                }
                tableView.refresh();
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
        executorService.shutdown();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                toActiveUserButton.requestFocus();
            }
        });
//        connection = UtilityDAL.getConnection();
        comparator = null;
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new MyAutoReloadActiveUser(), 0, 1000, TimeUnit.MILLISECONDS);
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        createDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreateDate().toString()));
        loginsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLogins().toString()));
        privateChatsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrivateChats().toString()));
        groupChatColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGroupChats().toString()));


        usernameFilter.setText("");
        maxLogins.setText("");
        minLogins.setText("");
        maxGroupChats.setText("");
        minGroupChats.setText("");
        maxPrivateChats.setText("");
        minPrivateChats.setText("");
        try {
            activeUserInformations = FXCollections.observableArrayList(AdminActiveUserInformationDAL.getAciveUserInformations(connection));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        tableView.setItems(activeUserInformations);
        tableView.refresh();
    }
}
