package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import javafx.application.Platform;
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

public class UserCreateGroupController implements Initializable, Runnable  {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;
    static GridPane display2 = new GridPane();

    static int id = 1;
    static List<Integer> newUserList = new ArrayList<>();
    static boolean stop = false;
    @FXML
    private TextField createTextGroupname;
    static TextField createTextGroupname2 = new TextField();
    @FXML
    private TextField createTextAdduser;
    static TextField createTextAdduser2 = new TextField();
    @FXML
    private HBox createHboxUser;
    static  HBox createHboxUser2 = new HBox();
    @FXML
    private VBox createVboxSearchuser;
    static  VBox createVboxSearchuser2 = new VBox();
    @FXML
    private TextField createTextSend;
    static TextField createTextSend2 = new TextField();
    @FXML
    private ImageView createImageSend;
    static ImageView createImageSend2 = new ImageView();
    @FXML
    private MenuItem createMenuitemAccount;
    static MenuItem createMenuitemAccount2 = new MenuItem();
    @FXML
    private MenuItem createMenuitemFriends;
    static MenuItem createMenuitemFriends2 = new MenuItem();
    @FXML
    private MenuItem createMenuitemLogout;
    static MenuItem createMenuitemLogout2 = new MenuItem();
    @FXML
    private MenuButton createMenuOnline;
    static MenuButton createMenuOnline2 = new MenuButton();
    @FXML
    private TextField createTextSearchbox;
    static TextField createTextSearchbox2 = new TextField();
    @FXML
    private VBox createVboxChatbox;
    static VBox createVboxChatbox2 = new VBox();

    Connection conn = UtilityDAL.getConnection();
    synchronized public void vboxSearchuserLoaded(){
        if (conn != null) {
            try {
                createVboxSearchuser2.getChildren().clear();
                String userssearch = createTextAdduser2.getText();
                String query = "select id, username, displayname from user_account_info join user_accounts on account_id = id join friendships on ((id = request_id and accept_id = ?) or (request_id = ? and id = accept_id)) where is_accepted = true and status != 'locked'";
                if (userssearch != null && !userssearch.isBlank()) {
                    query += " and (displayname like ? or username like ?)";
                }
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                if (userssearch != null && !userssearch.isBlank()){
                    ps.setString(3, "%" + userssearch + "%");
                    ps.setString(4, "%" + userssearch + "%");
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int userid = rs.getInt(1);
                    String userusername = rs.getString(2);
                    String userdisplayname = rs.getString(3);
                    HBox newsearchbox = new HBox();
                    newsearchbox.setPadding(new Insets(0, 10, 0 ,10));
                    newsearchbox.setSpacing(10);
                    Label newsearchdisplayname = new Label(userdisplayname);
                    newsearchdisplayname.setFont(new Font("Courier New Bold", 24));
                    newsearchdisplayname.setMaxWidth(290);
                    Label newsearchusername = new Label(userusername);
                    newsearchusername.setFont(new Font("Courier New", 20));
                    newsearchusername.setMaxWidth(290);
                    Label newsearchtoggle = new Label();
                    newsearchtoggle.setId(String.valueOf(userid));
                    newsearchtoggle.setFont(new Font("Courier New", 16));
                    if (newUserList.contains(userid)){
                        newsearchtoggle.setText("Remove");
                        newsearchtoggle.setTextFill(Color.RED);
                        newsearchtoggle.setOnMouseClicked(this::textRemoveClicked);
                    } else {
                        newsearchtoggle.setText("Add");
                        newsearchtoggle.setTextFill(Color.BLUE);
                        newsearchtoggle.setOnMouseClicked(this::textAddClicked);
                    }
                    newsearchbox.getChildren().addAll(newsearchdisplayname, newsearchusername, newsearchtoggle);
                    createVboxSearchuser2.getChildren().add(newsearchbox);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void textAddClicked(MouseEvent event){
        try{
            int userid = Integer.parseInt(((Label)event.getSource()).getId());
            String query = "select displayname from user_account_info where account_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            rs.next();
            VBox newUser = new VBox();
            newUser.setId(String.valueOf(userid));
            newUser.setAlignment(Pos.CENTER_LEFT);
            newUser.setPadding(new Insets(0, 10, 0, 10));
            newUser.setSpacing(5);
            newUser.setMinWidth(Region.USE_PREF_SIZE);
            newUser.setPrefHeight(60);
            Label newUsername = new Label(rs.getString("displayname"));
            newUsername.setFont(new javafx.scene.text.Font("Courier New Bold", 16));
            Label newRemove = new Label("Remove");
            newRemove.setId(String.valueOf(userid));
            newRemove.setFont(new javafx.scene.text.Font("Courier New", 12));
            newRemove.setTextFill(Color.RED);
            newRemove.setOnMouseClicked(this::textRemoveClicked);
            newUser.getChildren().addAll(newUsername, newRemove);
            createHboxUser2.getChildren().add(newUser);
            newUserList.add(userid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void textRemoveClicked(MouseEvent event){
        int userid = Integer.parseInt(((Label)event.getSource()).getId());
        newUserList.remove((Integer) userid);
        createHboxUser2.getChildren().remove(createHboxUser2.lookup("#" + userid));
    }
    synchronized public void textSendEntered(ActionEvent event){
        if (newUserList.size() < 2){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Group must have at least 2 users.");
            alert.showAndWait();
            return;
        }
        String groupname = createTextGroupname2.getText();
        String message = createTextSend2.getText();
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
        if (conn != null) {
            try {
                String query = "insert into box_chats (box_name, is_direct) values (?, ?); select id from box_chats order by create_date desc";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, groupname);
                ps.setBoolean(2, false);
                ps.execute();
                ps.getMoreResults();
                ResultSet rs = ps.getResultSet();
                rs.next();
                int boxnid = rs.getInt(1);
                for(Integer nid : newUserList){
                    query = "insert into box_chat_members (box_id, user_id, is_admin) values (?, ?, ?)";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, boxnid);
                    ps.setInt(2, nid);
                    if (nid == id){
                        ps.setBoolean(3, true);
                    } else {
                        ps.setBoolean(3, false);
                    }
                    ps.executeUpdate();
                }
                query = "insert into messages (user_id, box_id, content) values (?, ?, ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, boxnid);
                ps.setString(3, message);
                ps.executeUpdate();
                try {
                    stop = true;
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                    scene = new Scene(fxmlLoader.load(), 1080, 720);
                    stage = (Stage) display2.getScene().getWindow();
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
    synchronized public void imageSendClicked(MouseEvent event){
        if (newUserList.size() < 2){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText("Group must have at least 2 users.");
            alert.showAndWait();
            return;
        }
        String groupname = createTextGroupname2.getText();
        String message = createTextSend2.getText();
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
        if (conn != null) {
            try {
                String query = "insert into box_chats (box_name, is_direct) values (?, ?); select id from box_chats order by create_date desc";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, groupname);
                ps.setBoolean(2, false);
                ps.execute();
                ps.getMoreResults();
                ResultSet rs = ps.getResultSet();
                rs.next();
                int boxnid = rs.getInt(1);
                for(Integer nid : newUserList){
                    query = "insert into box_chat_members (box_id, user_id, is_admin) values (?, ?, ?)";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, boxnid);
                    ps.setInt(2, nid);
                    if (nid == id){
                        ps.setBoolean(3, true);
                    } else {
                        ps.setBoolean(3, false);
                    }
                    ps.executeUpdate();
                }
                query = "insert into messages (user_id, box_id, content) values (?, ?, ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, boxnid);
                ps.setString(3, message);
                ps.executeUpdate();
                try {
                    stop = true;
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                    scene = new Scene(fxmlLoader.load(), 1080, 720);
                    stage = (Stage) display2.getScene().getWindow();
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
    synchronized public void menuitemAccountClicked(ActionEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-account-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display2.getScene().getWindow();
            stage.setScene(scene);
            UserAccountController controller = fxmlLoader.getController();
            controller.setdata(id, stage);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void menuitemFriendsClicked(ActionEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-friends-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display2.getScene().getWindow();
            stage.setScene(scene);
            UserFriendController controller = fxmlLoader.getController();
            controller.setdata(id, stage);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void menuitemLogoutClicked(ActionEvent event){
        try{
            String query = "update user_accounts set status = 'offline' where id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ? and session_end is null";
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display2.getScene().getWindow();
            stage.setScene(scene);
            UserLoginController controller = fxmlLoader.getController();
            controller.setdata(stage);
            stage.show();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void menuOnlineClicked(Event event){
        if (conn != null) {
            try {
                createMenuOnline2.getItems().clear();
                String query = "select id, username, displayname from user_account_info join user_accounts on account_id = id join friendships on ((id = request_id and accept_id = ?) or (request_id = ? and id = accept_id)) where is_accepted = true and status = 'online'";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    MenuItem newonlinefriend = new MenuItem(rs.getString("displayname") + " @" + rs.getString("username"));
                    newonlinefriend.setId(String.valueOf(rs.getInt("id")));
                    newonlinefriend.setOnAction(this::menuitemOnlineClicked);
                    createMenuOnline2.getItems().add(newonlinefriend);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void menuitemOnlineClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "select id from box_chats join box_chat_members on id = box_id where user_id = ? and is_direct = true intersect select id from box_chats join box_chat_members on id = box_id where user_id = ? and is_direct = true";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(((MenuItem)event.getSource()).getId()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    stop = true;
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                    scene = new Scene(fxmlLoader.load(), 1080, 720);
                    stage = (Stage)display2.getScene().getWindow();
                    stage.setScene(scene);
                    UserChatController controller = fxmlLoader.getController();
                    controller.setdata(id, rs.getInt("id"), stage);
                    stage.show();
                } else {
                    query = "insert into box_chats (box_name, is_direct) values ('Direct', true);\n" +
                            "select max(id) from box_chats";
                    ps = conn.prepareStatement(query);
                    ps.execute();
                    ps.getMoreResults();
                    rs = ps.getResultSet();
                    rs.next();
                    query = "insert into box_chat_members (box_id, user_id) values (?, ?), (?, ?)";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, rs.getInt(1));
                    ps.setInt(2, id);
                    ps.setInt(3, rs.getInt(1));
                    ps.setInt(4, Integer.parseInt(((MenuItem)event.getSource()).getId()));
                    ps.executeUpdate();
                    menuitemOnlineClicked(event);
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void vboxChatboxLoaded(){
        if (conn != null) {
            try {
                createVboxChatbox2.getChildren().clear();
                String boxssearch = createTextSearchbox2.getText();
                String query;
                if (boxssearch != null && !boxssearch.isBlank()){
                    query = "with directbox as (\n" +
                            "    select box_chats.id, user_account_info.displayname as box_name, messages.content as newestmessage\n" +
                            "    from box_chats\n" +
                            "    join box_chat_members \n" +
                            "    on box_chat_members.box_id = box_chats.id\n" +
                            "    join user_account_info \n" +
                            "    on user_account_info.account_id = box_chat_members.user_id and box_chat_members.user_id != ?\n" +
                            "    left join messages on messages.box_id = box_chats.id\n" +
                            "    where box_chats.is_direct and box_chats.id in (\n" +
                            "        select distinct box_chat_members.box_id from box_chat_members where box_chat_members.user_id = ?\n" +
                            "    ) and messages.content like ?\n" +
                            "    order by box_chats.id, messages.create_date desc\n" +
                            "), \n" +
                            "groupbox as (\n" +
                            "    select box_chats.id, box_chats.box_name, \n" +
                            "    case when LENGTH(messages.content) > 0  \n" +
                            "    then CONCAT(user_account_info.displayname, ': ', messages.content)\n" +
                            "    else null\n" +
                            "    end\n" +
                            "    as newestmessage \n" +
                            "    from box_chats\n" +
                            "    join box_chat_members on box_chat_members.box_id = box_chats.id\n" +
                            "    left join messages on messages.box_id = box_chats.id\n" +
                            "    left join user_account_info on user_account_info.account_id = messages.user_id\n" +
                            "    where box_chat_members.user_id = ? and not box_chats.is_direct and messages.content like ?\n" +
                            "    order by box_chats.id, messages.create_date desc\n" +
                            ")\n" +
                            "select * from groupbox\n" +
                            "union \n" +
                            "select * from directbox";
                } else {
                    query = "with directbox as ( " +
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
                            "union " +
                            "select * from directbox;";
                }
                PreparedStatement ps = conn.prepareStatement(query);
                if (boxssearch != null && !boxssearch.isBlank()){
                    ps.setInt(1, id);
                    ps.setInt(2, id);
                    ps.setString(3, "%" + boxssearch + "%");
                    ps.setInt(4, id);
                    ps.setString(5, "%" + boxssearch + "%");
                } else {
                    ps.setInt(1, id);
                    ps.setInt(2, id);
                    ps.setInt(3, id);
                }
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
                    createVboxChatbox2.getChildren().add(newhboxbox);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void hboxBoxClicked(MouseEvent event){
        try {
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display2.getScene().getWindow();
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
        stop = false;
        display2 = display;
        createTextGroupname2 = createTextGroupname;
        createTextAdduser2 = createTextAdduser;
        createHboxUser2 = createHboxUser;
        createVboxSearchuser2 = createVboxSearchuser;
        createTextSend2 = createTextSend;
        createImageSend2 = createImageSend;
        createMenuitemAccount2 = createMenuitemAccount;
        createMenuitemFriends2 = createMenuitemFriends;
        createMenuitemLogout2 = createMenuitemLogout;
        createMenuOnline2 = createMenuOnline;
        createTextSearchbox2 = createTextSearchbox;
        createVboxChatbox2 = createVboxChatbox;
        createTextGroupname.setText("");
        createTextAdduser.setText("");
        createTextSend.setText("");
        createTextSearchbox.setText("");
        createHboxUser.getChildren().clear();
        createTextSend.setOnAction(this::textSendEntered);
        createImageSend.setOnMouseClicked(this::imageSendClicked);
        createMenuitemAccount.setOnAction(this::menuitemAccountClicked);
        createMenuitemFriends.setOnAction(this::menuitemFriendsClicked);
        createMenuitemLogout.setOnAction(this::menuitemLogoutClicked);
        createMenuOnline.setOnShowing(this::menuOnlineClicked);
        vboxChatboxLoaded();
        vboxSearchuserLoaded();
        Runnable r1 = new UserCreateGroupController();
        Thread t1 = new Thread(r1, "UserCreateGroupController");
        t1.setDaemon(true);
        t1.start();
    }

    public void setdata(int gid, Stage gstage, List<Integer> glid){
        id = gid;
        stage = gstage;
        stage.setOnCloseRequest(event -> logout(stage));
        newUserList = glid;
        for (Integer xid : newUserList){
            try{
                String query = "select displayname from user_account_info where account_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, xid);
                ResultSet rs = ps.executeQuery();
                rs.next();
                VBox newUser = new VBox();
                newUser.setAlignment(Pos.CENTER_LEFT);
                newUser.setPadding(new Insets(0, 10, 0, 10));
                newUser.setSpacing(5);
                newUser.setMinWidth(Region.USE_PREF_SIZE);
                newUser.setPrefHeight(60);
                Label newUsername = new Label(rs.getString(1));
                newUsername.setFont(new javafx.scene.text.Font("Courier New Bold", 16));
                Label newRemove = new Label();
                if (xid != id){
                    newRemove.setId(String.valueOf(xid));
                    newRemove.setText("Remove");
                    newRemove.setOnMouseClicked(this::textRemoveClicked);
                }
                newRemove.setFont(new javafx.scene.text.Font("Courier New", 12));
                newRemove.setTextFill(Color.RED);
                newUser.getChildren().addAll(newUsername, newRemove);
                createHboxUser.getChildren().add(newUser);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        vboxChatboxLoaded();
        vboxSearchuserLoaded();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try{
                String query = "select * from user_accounts where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next() || !rs.getString("status").equals("online")){
                    query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ? and session_end is null";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    stop = true;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
                                scene = new Scene(fxmlLoader.load(), 1080, 720);
                                stage = (Stage)display2.getScene().getWindow();
                                stage.setScene(scene);
                                UserLoginController controller = fxmlLoader.getController();
                                controller.setdata(stage);
                                stage.show();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (stop){
                return;
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vboxChatboxLoaded();
                    vboxSearchuserLoaded();
                }
            });
        }
    }

    public void logout(Stage st){
        if (conn != null) {
            try {
                String query = "update user_accounts set status = 'offline' where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ? and session_end is null";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
