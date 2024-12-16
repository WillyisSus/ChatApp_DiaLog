package application.chatapp_dialog.admin.modalcontroller;

import application.chatapp_dialog.dal.AdminActivityLogDAL;
import application.chatapp_dialog.dto.AdminUserAccount;
import application.chatapp_dialog.dto.AdminUserActivityLog;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.intellij.lang.annotations.JdkConstants;

import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminUserActivityLogController implements Initializable {
    @FXML
    private TableView activityTable;
    @FXML
    private TableColumn<String, String> dateAndTimes;
    @FXML
    private TableColumn<AdminUserActivityLog, String> index;
    @FXML
    private MenuButton orderMenu;
    @FXML
    private MenuItem ascendingItem;
    @FXML
    private MenuItem descendingItem;


    private ObservableList<AdminUserActivityLog> activityLogs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activityLogs = FXCollections.observableArrayList(new ArrayList<>());
        dateAndTimes.setCellValueFactory(new PropertyValueFactory<String, String>("sessionStart"));
        index.setCellValueFactory(data -> {
            AdminUserActivityLog item = data.getValue();
            int index = activityTable.getItems().indexOf(item);
            return new SimpleStringProperty(Integer.toString(index));
        });
        ascendingItem.setOnAction(this::sortAscending);
        descendingItem.setOnAction(this::sortDescending);
    }

    public void sortAscending(ActionEvent event){
        if (event.getEventType() == ActionEvent.ACTION){
            if (activityLogs.isEmpty()){
                return;
            }
            activityLogs.sort(AdminActivityLogDAL.getAscendingComparator());
            orderMenu.setText(ascendingItem.getText());

        }
    }


    public void sortDescending(ActionEvent event){
        if (event.getEventType() == ActionEvent.ACTION){
            if (activityLogs.isEmpty()){
                return;
            }
            activityLogs.sort(AdminActivityLogDAL.getDescendingComparator());
            orderMenu.setText(descendingItem.getText());
        }
    }

    public void setUser(int userID){
        try {
            activityLogs = FXCollections.observableArrayList(AdminActivityLogDAL.getAllUserActivityLog(userID));
            activityTable.setItems(activityLogs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        System.out.println(activityLogs.size());
    }

}
