package application.chatapp_dialog;

import application.chatapp_dialog.dal.AdminGroupInformationDAL;
import application.chatapp_dialog.dal.AdminReportInformationDAL;
import application.chatapp_dialog.dal.AdminUserAccountDAL;
import application.chatapp_dialog.dal.UtilityDAL;
import application.chatapp_dialog.dto.AdminReportInformation;
import application.chatapp_dialog.dto.AdminUserAccount;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.net.ConnectException;
import java.net.URL;
import java.security.AllPermission;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AdminReportController implements Initializable {
    private Connection connection;
    ScheduledExecutorService scheduledExecutorService;
    private class MyAutoReloadReport implements Runnable{

        @Override
        public void run() {
            try {
                if (connection != null){
                    ObservableList<AdminReportInformation> temp =  FXCollections.observableArrayList(AdminReportInformationDAL.getReportList(connection));
                    if(comparator != null){
                        temp.sort(comparator);
                    }
                    if (reportTable.getItems() instanceof FilteredList<AdminReportInformation>){
                        reportTable.setItems(new FilteredList<>(temp, ((FilteredList<AdminReportInformation>) reportTable.getItems()).getPredicate()));
                    }else {
                        reportTable.setItems(temp);
                    }

                    reportList = temp;
                    reportTable.refresh();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
// scene controller
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
// Table Properties
    @FXML
    private TableView<AdminReportInformation> reportTable;
    @FXML
    private TableColumn<AdminReportInformation, String> reportedUsername;
    @FXML
    private TableColumn<AdminReportInformation, String> reportedEmail;
    @FXML
    private TableColumn<AdminReportInformation, String> reportedDisplayName;
    @FXML
    private TableColumn<AdminReportInformation, String> reporterUsername;
    @FXML
    private TableColumn<AdminReportInformation, String> createDate;
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
    private MenuItem reportedNameAscending;
    @FXML
    private MenuItem reportedNameDescending;
    @FXML
    private MenuItem reporterAscending;
    @FXML
    private MenuItem reporterDescending;
//  Action Button
    @FXML
    private Button removeReport;
    @FXML
    private Button lockUser;
//  Table data properties;
    private Comparator<AdminReportInformation> comparator;
    private ObservableList<AdminReportInformation> reportList;
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
            handleCloseStage();
            stage.setScene(scene);
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

    public void handleRemoveReport(){
        try {
            AdminReportInformation selected = reportTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove report confirmation.");
            alert.setHeaderText("Are you sure to remove report from: " + selected.getReporterUsername());
            alert.setContentText("Click OK to continue.");
            Optional<ButtonType> clicked = alert.showAndWait();
            if (clicked.isPresent() && clicked.get() == ButtonType.OK){
                if (!AdminReportInformationDAL.removeReport(selected, connection)){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Remove ERROR");
                    alert.setHeaderText("Cannot remove report from: " + selected.getReporterUsername());
                    alert.setContentText("Click OK to continue.");
                    alert.showAndWait();
                }else{

                    reportList.remove(selected);
                    reportTable.refresh();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleLockUser(){
        try {
            AdminReportInformation selected = reportTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Lock user confirmation.");
            alert.setHeaderText("Are you sure to lock user: " + selected.getReportedUsername());
            alert.setContentText("Click OK to continue.");
            Optional<ButtonType> clicked = alert.showAndWait();
            if (clicked.isPresent() && clicked.get() == ButtonType.OK){
                if (!AdminReportInformationDAL.lockUser(selected, connection)){
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Lock user ERROR");
                    alert.setHeaderText("Cannot locked user: " + selected.getReportedUsername());
                    alert.setContentText("Click OK to continue.");
                    alert.showAndWait();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//  Sort and filter functions

    public void handleSort(ActionEvent event){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (event.getSource() == dateAscending){
                    orderMenu.setText(dateAscending.getText());
                    comparator = AdminReportInformationDAL.getDateAscendingComparator();
                    reportList.sort(AdminReportInformationDAL.getDateAscendingComparator());
                } else if(event.getSource() == dateDescending){
                    orderMenu.setText(dateDescending.getText());
                    comparator = AdminReportInformationDAL.getDateDescendingComparator();
                    reportList.sort(AdminReportInformationDAL.getDateDescendingComparator());
                } else if (event.getSource() == reporterAscending){
                    orderMenu.setText(reporterAscending.getText());
                    comparator = AdminReportInformationDAL.getReporterUsernameAscendingComparator();
                    reportList.sort(AdminReportInformationDAL.getReporterUsernameAscendingComparator());
                } else if (event.getSource() == reporterDescending){
                    orderMenu.setText(reporterDescending.getText());
                    comparator = AdminReportInformationDAL.getreporterUsernameDescendingComparator();
                    reportList.sort(AdminReportInformationDAL.getreporterUsernameDescendingComparator());
                } else if (event.getSource() == reportedNameAscending){
                    orderMenu.setText(reportedNameAscending.getText());
                    comparator = AdminReportInformationDAL.getUsernameAscendingComparator();
                    reportList.sort(AdminReportInformationDAL.getUsernameAscendingComparator());
                } else {
                    orderMenu.setText(reportedNameDescending.getText());
                    comparator = AdminReportInformationDAL.getUsernameDescendingComparator();
                    reportList.sort(AdminReportInformationDAL.getUsernameDescendingComparator());
                }
                reportTable.refresh();
            }
        });

    }

    public void handleFilter(ActionEvent event){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (event.getSource() == filterButton){
                    if (filterValue.getText().isEmpty() && maxDate.getValue() == null && minDate.getValue() == null){
                        return;
                    }
                    FilteredList<AdminReportInformation> filteredList = new FilteredList<>(reportList, data->{
                        boolean filterByName = true;
                        boolean filterByMinDate = true;
                        boolean filterByMaxDate = true;
                        if (!filterValue.getText().isEmpty()){
                            String filterMode = filterChoices.getValue();
                            if (filterMode.contains("Reported")){
                                filterByName = data.getReportedUsername().startsWith(filterValue.getText());
                            } else if (filterMode.contains("Email")){
                                filterByName = data.getReportedEmail().startsWith(filterValue.getText());
                            } else {
                                filterByName = data.getReporterUsername().startsWith(filterValue.getText());
                            }
                        }
                        if (minDate.getValue() != null){
                            LocalDate date = data.getCreateDate().toLocalDateTime().toLocalDate();
                            filterByMinDate = minDate.getValue().isBefore(date) || minDate.getValue().isEqual(date);
                        }
                        if (maxDate.getValue() != null){
                            LocalDate date = data.getCreateDate().toLocalDateTime().toLocalDate();
                            filterByMaxDate = maxDate.getValue().isAfter(date) || maxDate.getValue().isEqual(date);
                        }
                        return (filterByName && filterByMaxDate && filterByMinDate);
                    });
                    reportTable.setItems(filteredList);
                    reportTable.refresh();

                }else {
                    maxDate.setValue(null);
                    minDate.setValue(null);
                    reportTable.setItems(reportList);
                    reportTable.refresh();

                }
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
                toReportViewButton.requestFocus();
            }
        });
//        connection = UtilityDAL.getConnection();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new MyAutoReloadReport(), 0, 1000, TimeUnit.MILLISECONDS);
        comparator = null;
        reportedUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReportedUsername()));
        reportedEmail.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getReportedEmail()));
        reportedDisplayName.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getDisplayName()));
        reporterUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReporterUsername()));
        createDate.setCellValueFactory(data->new SimpleStringProperty(data.getValue().getCreateDate().toString()));

        try {
            reportList = FXCollections.observableList(AdminReportInformationDAL.getReportList(connection));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        removeReport.setDisable(true);
        lockUser.setDisable(true);
        maxDate.setValue(null);
        minDate.setValue(null);
        filterChoices.getItems().addAll("Reported Username", "Reported Email", "Reporter Username");
        filterChoices.setValue(filterChoices.getItems().getFirst());
        reportTable.setItems(reportList);
        reportTable.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            if (t1 != null){
                removeReport.setDisable(false);
                lockUser.setDisable(false);

            } else {
                removeReport.setDisable(true);
                lockUser.setDisable(true);
            }
        });
    }
}
