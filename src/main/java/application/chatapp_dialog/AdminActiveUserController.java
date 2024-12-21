package application.chatapp_dialog;
import application.chatapp_dialog.dal.AdminActiveUserInformationDAL;
import application.chatapp_dialog.dal.AdminReportInformationDAL;
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
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminActiveUserController implements Initializable {
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
            Parent root = FXMLLoader.load(getClass().getResource("admin-login.fxml"));
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
// Handle Filter
    public void handleFilter(ActionEvent event){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
                    usernameFilter.setText(null);
                    minDate.setValue(null);
                    maxDate.setValue(null);
                    maxLogins.setText(null);
                    minLogins.setText(null);
                    maxGroupChats.setText(null);
                    minGroupChats.setText(null);
                    maxPrivateChats.setText(null);
                    maxPrivateChats.setText(null);
                    tableView.setItems(activeUserInformations);
                    tableView.refresh();
                }
            }
        });

    }

    public void handleSort(ActionEvent event){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (event.getSource() == createDateAscending){
                    orderMenu.setText(createDateAscending.getText());
                    activeUserInformations.sort(AdminActiveUserInformationDAL.getDateAscendingComparator());
                } else if(event.getSource() == createDateDescending){
                    orderMenu.setText(createDateDescending.getText());
                    activeUserInformations.sort(AdminActiveUserInformationDAL.getDateDescendingComparator());
                } else if (event.getSource() == usernameAscending){
                    orderMenu.setText(usernameAscending.getText());
                    activeUserInformations.sort(AdminActiveUserInformationDAL.getUsernameAscendingComparator());
                } else if (event.getSource() == usernameDescending){
                    orderMenu.setText(usernameDescending.getText());
                    activeUserInformations.sort(AdminActiveUserInformationDAL.getUsernameDescendingComparator());
                }
                tableView.refresh();
            }
        });

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                toActiveUserButton.requestFocus();
            }
        });
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        createDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreateDate().toLocalDateTime().withNano(0).toString()));
        loginsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLogins().toString()));
        privateChatsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrivateChats().toString()));
        groupChatColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGroupChats().toString()));

        try {
            activeUserInformations = FXCollections.observableArrayList(AdminActiveUserInformationDAL.getAciveUserInformations());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        tableView.setItems(activeUserInformations);
        tableView.refresh();
    }
}
