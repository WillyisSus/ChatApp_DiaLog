package application.chatapp_dialog;
import application.chatapp_dialog.dal.AdminNewUserAccountDAL;
import application.chatapp_dialog.dal.AdminUserFriendCountDAL;
import application.chatapp_dialog.dto.AdminGroupInformation;
import application.chatapp_dialog.dto.AdminNewUserAccount;
import application.chatapp_dialog.dto.AdminReportInformation;
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
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AdminNewUserController implements Initializable {
    private Connection connection;
    private class MyAutoReloadNewUser implements Runnable{
        @Override
        public void run() {
            try {
                if (connection != null){
                    ObservableList<AdminNewUserAccount> temp =  FXCollections.observableArrayList(AdminNewUserAccountDAL.getAllNewAccount(connection));
                    if(comparator != null){
                        temp.sort(comparator);
                    }
                    if (newUserTable.getItems() instanceof FilteredList<AdminNewUserAccount>){
                        newUserTable.setItems(new FilteredList<>(temp, ((FilteredList<AdminNewUserAccount>) newUserTable.getItems()).getPredicate()));
                    }else {
                        newUserTable.setItems(temp);
                    }
                    newUserAccounts = temp;
                    newUserTable.refresh();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
//    Scene properties
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
//    table property
    @FXML
    private TableView<AdminNewUserAccount> newUserTable;
    @FXML
    private TableColumn<AdminNewUserAccount, String> usernameColumn;
    @FXML
    private TableColumn<AdminNewUserAccount, String> emailColumn;
    @FXML
    private TableColumn<AdminNewUserAccount, String> createDateColumn;


    private ObservableList<AdminNewUserAccount> newUserAccounts;
    private Comparator<AdminNewUserAccount> comparator;
    // Filter Properties
    @FXML
    private TextField filterValue;
    @FXML
    private Button filterButton;
    @FXML
    private Button clearFilterButton;
    @FXML
    private ChoiceBox<String> filterChoices;
    @FXML
    private DatePicker minDate;
    @FXML
    private DatePicker maxDate;
    @FXML
    private MenuButton orderMenu;
    @FXML
    private MenuItem dateAscending;
    @FXML
    private MenuItem dateDescending;
    @FXML
    private MenuItem usernameAscending;
    @FXML
    private MenuItem usernameDescending;
    @FXML
    private MenuItem emailAscending;
    @FXML
    private MenuItem emailDescending;
// Scene management
    public void switchToLogin(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-login.fxml"));
            Parent root = loader.load(getClass().getResource("admin-login.fxml"));
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
// Functionality
    public void handleSort(ActionEvent event){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (event.getSource() == usernameAscending){
                    orderMenu.setText(usernameAscending.getText());
                    comparator = AdminNewUserAccountDAL.getUsernameAscending();
                    newUserAccounts.sort(AdminNewUserAccountDAL.getUsernameAscending());
                }else if (event.getSource() == usernameDescending){
                    orderMenu.setText(usernameDescending.getText());
                    comparator = AdminNewUserAccountDAL.getUsernameDescending();
                    newUserAccounts.sort(AdminNewUserAccountDAL.getUsernameDescending());
                }else if (event.getSource() == emailAscending){
                    orderMenu.setText(emailAscending.getText());
                    comparator = AdminNewUserAccountDAL.getEmailAscending();
                    newUserAccounts.sort(AdminNewUserAccountDAL.getEmailAscending());
                } else if (event.getSource() == emailDescending){
                    orderMenu.setText(emailDescending.getText());
                    comparator = AdminNewUserAccountDAL.getEmailDescending();
                    newUserAccounts.sort(AdminNewUserAccountDAL.getEmailDescending());
                } else if (event.getSource() == dateAscending){
                    orderMenu.setText(dateAscending.getText());
                    comparator = AdminNewUserAccountDAL.getDateAscendingComparator();
                    newUserAccounts.sort(AdminNewUserAccountDAL.getDateAscendingComparator());
                } else if (event.getSource() == dateDescending){
                    orderMenu.setText(dateDescending.getText());
                    comparator = AdminNewUserAccountDAL.getDateDescendingComparator();
                    newUserAccounts.sort(AdminNewUserAccountDAL.getDateDescendingComparator());
                }
                newUserTable.refresh();
            }
        });

    }

    public void handleFilter(ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (event.getSource() == filterButton) {
                    if (filterValue.getText().isEmpty() && maxDate.getValue() == null && minDate.getValue() == null) {
                        return;
                    }
                    FilteredList<AdminNewUserAccount> filteredList = new FilteredList<>(newUserAccounts, data -> {
                        boolean filterByName = true;
                        boolean filterByMinDate = true;
                        boolean filterByMaxDate = true;
                        if (!filterValue.getText().isEmpty()) {
                            String filterMode = filterChoices.getValue();
                            if (filterMode.contains("Username")) {
                                filterByName = data.getUsername().startsWith(filterValue.getText());
                            } else if (filterMode.contains("Email")) {
                                filterByName = data.getEmail().startsWith(filterValue.getText());
                            }
                        }
                        if (minDate.getValue() != null) {
                            LocalDate date = data.getCreateDate().toLocalDateTime().toLocalDate();
                            filterByMinDate = minDate.getValue().isBefore(date) || minDate.getValue().isEqual(date);
                        }
                        if (maxDate.getValue() != null) {
                            LocalDate date = data.getCreateDate().toLocalDateTime().toLocalDate();
                            filterByMaxDate = maxDate.getValue().isAfter(date) || maxDate.getValue().isEqual(date);
                        }
                        return (filterByName && filterByMaxDate && filterByMinDate);
                    });
                    newUserTable.setItems(filteredList);
                    newUserTable.refresh();

                } else {
                    maxDate.setValue(null);
                    minDate.setValue(null);
                    newUserTable.setItems(newUserAccounts);
                    newUserTable.refresh();

                }
            }
        });

    }
    public void setConnection(Connection conn){
        connection = conn;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                toNewcomerViewButton.requestFocus();
            }
        });
        comparator = null;
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new MyAutoReloadNewUser(), 0, 1000, TimeUnit.MILLISECONDS);
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        createDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreateDate().toString()));
        try {
            newUserAccounts = FXCollections.observableArrayList(AdminNewUserAccountDAL.getAllNewAccount(connection));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        newUserTable.setItems(newUserAccounts);
        newUserTable.refresh();
        minDate.setValue(null);
        maxDate.setValue(null);
        filterChoices.getItems().addAll("Username", "Email");
        filterChoices.setValue(filterChoices.getItems().getFirst());
    }
}
