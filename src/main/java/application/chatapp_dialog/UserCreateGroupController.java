package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserCreateGroupController implements Initializable  {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    int id = 1;
    @FXML
    private TextField createTextGroupname;
    @FXML
    private TextField createTextAdduser;
    @FXML
    private ImageView createImageAdduser;
    @FXML
    private HBox createHboxUser;
    @FXML
    private VBox createVboxSearchuser;
    @FXML
    private TextField createTextSend;
    @FXML
    private ImageView createImageSend;
    @FXML
    private MenuButton createMenuAccount;
    @FXML
    private MenuItem createMenuitemAccount;
    @FXML
    private MenuItem createMenuitemFriends;
    @FXML
    private MenuItem createMenuitemLogout;
    @FXML
    private MenuButton createMenuOnline;
    @FXML
    private ImageView createImageCreategroup;
    @FXML
    private VBox createVboxChatbox;

    List<Integer> newUserList = new ArrayList<>();

    @FXML
    public void textAdduserEntered(ActionEvent event){
        createVboxSearchuser.getChildren().clear();
        String name = createTextAdduser.getText();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "select id, username, displayname from user_accounts join user_account_info on id = account_id join friendships on id = request_id or id = accept_id where is_accepted = true and (request_id = ? or accept_id = ?) and id != ? and (username like ? or displayname like ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ps.setInt(3, id);
                ps.setString(4, "%" + name + "%");
                ps.setString(5, "%" + name + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int userid = rs.getInt(1);
                    String userusername = rs.getString(2);
                    String userdisplayname = rs.getString(3);
                    HBox newsearchbox = new HBox();
                    newsearchbox.setId("search" + userid);
                    newsearchbox.setPadding(new Insets(0, 10, 0 ,10));
                    newsearchbox.setSpacing(10);
                    Label newsearchdisplayname = new Label(userdisplayname);
                    newsearchdisplayname.setId("display" + userid);
                    newsearchdisplayname.setFont(new Font("Courier New Bold", 24));
                    newsearchdisplayname.setMaxWidth(290);
                    Label newsearchusername = new Label(userusername);
                    newsearchusername.setFont(new Font("Courier New", 20));
                    newsearchusername.setMaxWidth(290);
                    Label newsearchtoggle = new Label();
                    newsearchtoggle.setFont(new Font("Courier New", 16));
                    newsearchtoggle.setId(String.valueOf(userid));
                    if (newUserList.contains(rs.getInt(1))){
                        newsearchtoggle.setText("Remove");
                        newsearchtoggle.setTextFill(Color.RED);
                        newsearchtoggle.setOnMouseClicked(this::textRemoveClicked);
                    } else {
                        newsearchtoggle.setText("Add");
                        newsearchtoggle.setTextFill(Color.BLUE);
                        newsearchtoggle.setOnMouseClicked(this::textAddClicked);
                    }
                    newsearchbox.getChildren().addAll(newsearchdisplayname, newsearchusername, newsearchtoggle);
                    createVboxSearchuser.getChildren().add(newsearchbox);
                }

//                rs.next();
//                int id = rs.getInt(1);
//                System.out.println(id);
//                if(id != 0){
//                    VBox newUser = new VBox();
//                    newUser.setAlignment(Pos.CENTER_LEFT);
//                    newUser.setPadding(new Insets(0, 10, 0, 10));
//                    newUser.setSpacing(5);
//                    newUser.setMinWidth(Region.USE_PREF_SIZE);
//                    newUser.setPrefHeight(60);
//                    Label newUsername = new Label(name);
//                    newUsername.setFont(new javafx.scene.text.Font("Courier New Bold", 16));
//                    Label newRemove = new Label("Remove");
//                    newRemove.setFont(new javafx.scene.text.Font("Courier New", 12));
//                    newRemove.setTextFill(Color.RED);
//                    newRemove.setOnMouseClicked(this::textRemoveClicked);
//                    newUser.getChildren().addAll(newUsername, newRemove);
//                    createHboxUser.getChildren().add(newUser);
//                    newUserList.add(id);
//                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    public void textAddClicked(MouseEvent event){
        int userid = Integer.parseInt(((Label)event.getSource()).getId());
        newUserList.add(userid);
        VBox newUser = new VBox();
        newUser.setId(String.valueOf(userid));
        newUser.setAlignment(Pos.CENTER_LEFT);
        newUser.setPadding(new Insets(0, 10, 0, 10));
        newUser.setSpacing(5);
        newUser.setMinWidth(Region.USE_PREF_SIZE);
        newUser.setPrefHeight(60);
        Label newUsername = new Label(((Label)createVboxSearchuser.lookup("#display" + userid)).getText());
        newUsername.setFont(new javafx.scene.text.Font("Courier New Bold", 16));
        Label newRemove = new Label("Remove");
        newRemove.setId(String.valueOf(userid));
        newRemove.setFont(new javafx.scene.text.Font("Courier New", 12));
        newRemove.setTextFill(Color.RED);
        newRemove.setOnMouseClicked(this::textRemoveClicked);
        newUser.getChildren().addAll(newUsername, newRemove);
        createHboxUser.getChildren().add(newUser);
        Label oldtoggle = ((Label)event.getSource());
        oldtoggle.setText("Remove");
        oldtoggle.setTextFill(Color.RED);
        oldtoggle.setOnMouseClicked(this::textRemoveClicked);
    }
    @FXML
    public void textRemoveClicked(MouseEvent event){
        int userid = Integer.parseInt(((Label)event.getSource()).getId());
        newUserList.remove(newUserList.indexOf(userid));
        createHboxUser.getChildren().remove(createHboxUser.lookup("#" + userid));
        HBox remuser = (HBox)createVboxSearchuser.lookup("#search" + userid);
        Label remuserl = (Label)remuser.lookup("#" + userid);
        remuserl.setText("Add");
        remuserl.setTextFill(Color.BLUE);
        remuserl.setOnMouseClicked(this::textAddClicked);
    }
    @FXML
    public void textSendEntered(ActionEvent event){
        if (newUserList.size() < 2){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Group must have at least 2 users.");
            alert.showAndWait();
            return;
        }
        String groupname = createTextGroupname.getText();
        String message = createTextSend.getText();
        if (groupname.isBlank() || groupname.length() > 32) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Invalid group name.");
            alert.setContentText("Max 32 characters.");
            alert.showAndWait();
            return;
        }
        if (message.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Message cannot be empty.");
            alert.showAndWait();
            return;
        }
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "insert into box_chats (box_name, is_direct) " +
                        "values (?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, groupname);
                ps.setBoolean(2, false);
                int row = ps.executeUpdate();
                System.out.println(ps.toString());
                System.out.println(row + " added");
                query = "select id from box_chats order by create_date desc";
                ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                int boxnid = rs.getInt(1);
                for(Integer nid : newUserList){
                    query = "insert into box_chat_members (box_id, user_id, is_admin) " +
                            "values (?, ?, ?)";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, boxnid);
                    ps.setInt(2, nid);
                    if (nid == id){
                        ps.setBoolean(3, true);
                    } else {
                        ps.setBoolean(3, false);
                    }
                    row = ps.executeUpdate();
                    System.out.println(ps.toString());
                    System.out.println(row + " added");
                }
                query = "insert into messages (user_id, box_id, content) values (?, ?, ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, boxnid);
                ps.setString(3, message);
                row = ps.executeUpdate();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                    scene = new Scene(fxmlLoader.load(), 1080, 720);
                    stage = (Stage) display.getScene().getWindow();
                    stage.setScene(scene);
                    UserChatController controller = fxmlLoader.getController();
                    controller.setdata(id, boxnid, stage);
                    stage.show();
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    public void imageSendClicked(MouseEvent event){
        if (newUserList.size() < 2){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Group must have at least 2 users.");
            alert.showAndWait();
            return;
            //a
        }
        String groupname = createTextGroupname.getText();
        String message = createTextSend.getText();
        if (groupname.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Invalid group name.");
            alert.showAndWait();
            return;
        }
        if (message.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Message cannot be empty.");
            alert.showAndWait();
            return;
        }
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "insert into box_chats (box_name, is_direct) " +
                        "values (?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, groupname);
                ps.setBoolean(2, newUserList.size() <= 2);
                int row = ps.executeUpdate();
                System.out.println(ps.toString());
                System.out.println(row + " added");
                query = "select id from box_chats order by create_date desc";
                ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                rs.next();
                int boxnid = rs.getInt(1);
                for(Integer nid : newUserList){
                    query = "insert into box_chat_members (box_id, user_id) " +
                            "values (?, ?)";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, boxnid);
                    ps.setInt(2, nid);
                    row = ps.executeUpdate();
                    System.out.println(ps.toString());
                    System.out.println(row + " added");
                }
                query = "insert into messages (user_id, box_id, content) values (?, ?, ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, boxnid);
                ps.setString(3, message);
                row = ps.executeUpdate();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                    scene = new Scene(fxmlLoader.load(), 1080, 720);
                    stage = (Stage) display.getScene().getWindow();
                    stage.setScene(scene);
                    UserChatController controller = fxmlLoader.getController();
                    controller.setdata(id, boxnid, stage);
                    stage.show();
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    public void firstPersonShooter(){
        VBox newUser = new VBox();
        newUser.setAlignment(Pos.CENTER_LEFT);
        newUser.setPadding(new Insets(0, 10, 0, 10));
        newUser.setSpacing(5);
        newUser.setMinWidth(Region.USE_PREF_SIZE);
        newUser.setPrefHeight(60);
        Label newUsername = new Label("User");
        newUsername.setFont(new javafx.scene.text.Font("Courier New Bold", 16));
        Label newRemove = new Label();
        newRemove.setFont(new javafx.scene.text.Font("Courier New", 12));
        newRemove.setTextFill(Color.RED);
        newUser.getChildren().addAll(newUsername, newRemove);
        createHboxUser.getChildren().add(newUser);
        newUserList.add(id);
    }
    public void menuitemAccountClicked(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-account-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void menuitemFriendsClicked(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-friends-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void menuitemLogoutClicked(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void menuOnlineClicked(Event event){
        createMenuOnline.getItems().clear();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "select id, username, displayname from user_account_info join user_accounts on account_id = id join friendships on (id = request_id or id = accept_id) and is_accepted = true where status = 'online' and id != ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    MenuItem newonlinefriend = new MenuItem(rs.getString(3));
                    newonlinefriend.setId(String.valueOf(rs.getInt(1)));
                    newonlinefriend.setOnAction(this::menuitemOnlineClicked);
                    createMenuOnline.getItems().add(newonlinefriend);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void menuitemOnlineClicked(ActionEvent event){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "select box_id from\n" +
                        "(select box_chat_members.box_id from box_chat_members where box_chat_members.user_id = ?\n" +
                        "intersect\n" +
                        "select box_chat_members.box_id from box_chat_members where box_chat_members.user_id = ?)\n" +
                        "where box_id in (select box_chats.id from box_chats where box_chats.is_direct)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(((MenuItem)event.getSource()).getId()));
                ResultSet rs = ps.executeQuery();
                rs.next();
                int boxid = rs.getInt(1);
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                    scene = new Scene(fxmlLoader.load(), 1080, 720);
                    stage = (Stage) display.getScene().getWindow();
                    stage.setScene(scene);
                    UserChatController controller = fxmlLoader.getController();
                    controller.setdata(id, boxid, stage);
                    stage.show();
                } catch (IOException exception) {
                    throw new RuntimeException(exception);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void vboxChatboxLoaded(){
        createVboxChatbox.getChildren().clear();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "with directbox as ( " +
                        "    select distinct on (box_chats.id) box_chats.id, user_account_info.displayname as box_name, messages.content as newestmessage " +
                        "    from box_chats " +
                        "    join box_chat_members  " +
                        "    on box_chat_members.box_id = box_chats.id " +
                        "    join user_account_info  " +
                        "    on user_account_info.account_id = box_chat_members.user_id and box_chat_members.user_id != ? " +
                        "    left join messages on messages.box_id = box_chats.id " +
                        "    where box_chats.is_direct and box_chats.id in ( " +
                        "        select distinct box_chat_members.box_id from box_chat_members where box_chat_members.user_id = ? " +
                        "    ) " +
                        "    order by box_chats.id, messages.create_date desc " +
                        "),  " +
                        "groupbox as ( " +
                        "    select distinct on (box_chats.id) box_chats.id, box_chats.box_name,  " +
                        "    case when LENGTH(messages.content) > 0   " +
                        "    then CONCAT(user_account_info.displayname, ': ', messages.content) " +
                        "    else null " +
                        "    end " +
                        "    as newestmessage  " +
                        "    from box_chats " +
                        "    join box_chat_members on box_chat_members.box_id = box_chats.id " +
                        "    left join messages on messages.box_id = box_chats.id " +
                        "    left join user_account_info on user_account_info.account_id = messages.user_id " +
                        "    where box_chat_members.user_id = ? and not box_chats.is_direct " +
                        "    order by box_chats.id, messages.create_date desc " +
                        ") " +
                        "select * from groupbox " +
                        "union  " +
                        "select * from directbox;";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ps.setInt(3, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int boxrid = rs.getInt(1);
                    String boxname = rs.getString(2);
                    String boxmess = rs.getString(3);
                    HBox newhboxbox = new HBox();
                    newhboxbox.setId(String.valueOf(boxrid));
                    newhboxbox.setAlignment(Pos.CENTER);
                    newhboxbox.setPadding(new Insets(0, 5, 0, 5));
                    newhboxbox.setSpacing(10);
                    VBox newvboxdata = new VBox();
                    newvboxdata.setMinWidth(334);
                    newvboxdata.setPrefWidth(225);
                    Label newboxname = new Label(boxname);
                    newboxname.setFont(new Font("Courier New Bold", 24));
                    newboxname.setMinHeight(40);
                    newboxname.setMaxHeight(40);
                    Label newboxmess = new Label(boxmess);
                    newboxmess.setFont(new Font("Courier New", 20));
                    newboxmess.setMinHeight(24);
                    newboxmess.setMaxHeight(24);
                    newvboxdata.getChildren().addAll(newboxname, newboxmess);
                    newhboxbox.getChildren().add(newvboxdata);
                    newhboxbox.setOnMouseClicked(this::hboxBoxClicked);
                    createVboxChatbox.getChildren().add(newhboxbox);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void hboxBoxClicked(MouseEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage) display.getScene().getWindow();
            stage.setScene(scene);
            UserChatController controller = fxmlLoader.getController();
            controller.setdata(id, Integer.parseInt(((HBox)event.getSource()).getId()), stage);
            stage.show();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createHboxUser.getChildren().clear();
        createVboxSearchuser.getChildren().clear();
        createTextGroupname.setText("");
        //firstPersonShooter();
        createTextAdduser.setOnAction(this::textAdduserEntered);
        createTextSend.setOnAction(this::textSendEntered);
        createImageSend.setOnMouseClicked(this::imageSendClicked);
        createMenuitemAccount.setOnAction(this::menuitemAccountClicked);
        createMenuitemFriends.setOnAction(this::menuitemFriendsClicked);
        createMenuitemLogout.setOnAction(this::menuitemLogoutClicked);
        createMenuOnline.setOnShowing(this::menuOnlineClicked);
        vboxChatboxLoaded();
    }

    public void setdata(int gid, Stage gstage, List<Integer> glid){
        id = gid;
        stage = gstage;
        newUserList = glid;
        vboxChatboxLoaded();
    }
}
