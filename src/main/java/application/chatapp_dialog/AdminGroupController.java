package application.chatapp_dialog;

import application.chatapp_dialog.dal.AdminGroupInformationDAL;
import application.chatapp_dialog.dto.AdminGroupInformation;
import application.chatapp_dialog.dto.AdminSimpleUserAccount;
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
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminGroupController implements Initializable {
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
        if (event.getSource() == dateAscending){
            orderMenu.setText(dateAscending.getText());
            groupInformations.sort(AdminGroupInformationDAL.getDateAscendingComparator());
        } else if(event.getSource() == dateDescending){
            orderMenu.setText(dateDescending.getText());
            groupInformations.sort(AdminGroupInformationDAL.getDateDescendingComparator());
        } else if (event.getSource() == boxNameAscending){
            orderMenu.setText(boxNameAscending.getText());
            groupInformations.sort(AdminGroupInformationDAL.getBoxNameAscendingComparator());
        } else {
            orderMenu.setText(boxNameDescending.getText());
            groupInformations.sort(AdminGroupInformationDAL.getBoxNameDescendingComparator());
        }
        boxTable.refresh();

    }

    public void showMemberAndAdminListOfGroup(AdminGroupInformation groupInformation){
        try {
            memberInGroups = FXCollections.observableArrayList(AdminGroupInformationDAL.getMemberOfGroup(groupInformation));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGroupName()));
        boxCreateDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCreateDate().toString()));
        boxAdmins.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdmins().toString()));
        boxMembers.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMembers().toString()));
        try {
            groupInformations = FXCollections.observableArrayList(AdminGroupInformationDAL.getGroupInformationList());
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
