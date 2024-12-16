package application.chatapp_dialog;
import application.chatapp_dialog.dal.AdminNewUserAccountDAL;
import application.chatapp_dialog.dal.AdminUserFriendCountDAL;
import application.chatapp_dialog.dto.AdminGroupInformation;
import application.chatapp_dialog.dto.AdminNewUserAccount;
import application.chatapp_dialog.dto.AdminReportInformation;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminNewUserController implements Initializable {
//    Scene properties
    private Scene scene;
    private Stage stage;
    private Parent root;
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
// Functionality
    public void handleSort(ActionEvent event){
        if (event.getSource() == usernameAscending){
            orderMenu.setText(usernameAscending.getText());
            newUserAccounts.sort(AdminNewUserAccountDAL.getUsernameAscending());
        }else if (event.getSource() == usernameDescending){
            orderMenu.setText(usernameDescending.getText());
            newUserAccounts.sort(AdminNewUserAccountDAL.getUsernameDescending());
        }else if (event.getSource() == emailAscending){
            orderMenu.setText(emailAscending.getText());
            newUserAccounts.sort(AdminNewUserAccountDAL.getEmailAscending());
        } else if (event.getSource() == emailDescending){
            orderMenu.setText(emailDescending.getText());
            newUserAccounts.sort(AdminNewUserAccountDAL.getEmailDescending());
        } else if (event.getSource() == dateAscending){
            orderMenu.setText(dateAscending.getText());
            newUserAccounts.sort(AdminNewUserAccountDAL.getDateAscendingComparator());
        } else if (event.getSource() == dateDescending){
            orderMenu.setText(dateDescending.getText());
            newUserAccounts.sort(AdminNewUserAccountDAL.getDateDescendingComparator());
        }
        newUserTable.refresh();
    }

    public void handleFilter(ActionEvent event) {
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        createDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreateDate().toString()));
        try {
            newUserAccounts = FXCollections.observableArrayList(AdminNewUserAccountDAL.getAllNewAccount());
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
