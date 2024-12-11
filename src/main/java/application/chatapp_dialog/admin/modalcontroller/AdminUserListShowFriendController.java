package application.chatapp_dialog.admin.modalcontroller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.chatapp_dialog.dal.AdminUserAccountDAL;
import application.chatapp_dialog.dto.AdminFriendOfUser;
import javafx.scene.control.cell.PropertyValueFactory;


public class AdminUserListShowFriendController implements Initializable {
    @FXML
    private TableView friendListTable;
    @FXML
    private TableColumn<Integer, Integer> id;
    @FXML
    private TableColumn<String, String> username;
    @FXML
    private TableColumn<String, String> displayName;


    private ObservableList<AdminFriendOfUser> friendList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        friendList = null;
        id.setCellValueFactory(new PropertyValueFactory<Integer, Integer>("id"));
        username.setCellValueFactory(new PropertyValueFactory<String, String>("username"));
        displayName.setCellValueFactory(new PropertyValueFactory<String, String>("displayName"));
    }

    public void loadData(int userID){
        List<AdminFriendOfUser> friends = AdminUserAccountDAL.getFriendOfUser(userID);
        if (friends.size() > 0){
            friendList = FXCollections.observableArrayList(friends);
            friendListTable.setItems(friendList);
        }
    }
}
