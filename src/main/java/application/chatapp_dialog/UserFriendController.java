package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserFriendController implements Initializable, Runnable  {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    static int id = 1;
    static boolean stop  = false;
    @FXML
    private MenuItem friendMenuitemAccount;
    static MenuItem friendMenuitemAccount2 = new MenuItem();
    @FXML
    private MenuItem friendMenuitemLogout;
    static MenuItem friendMenuitemLogout2 = new MenuItem();
    @FXML
    private ImageView friendImageBack;
    static ImageView friendImageBack2 = new ImageView();
    @FXML
    private Tab friendTabFriends;
    static Tab friendTabFriends2 = new Tab();
    @FXML
    private TextField friendTextSearchfriends;
    static TextField friendTextSearchfriends2 = new TextField();
    @FXML
    private ScrollPane friendScrollFriends;
    static ScrollPane friendScrollFriends2 = new ScrollPane();
    @FXML
    private VBox friendVboxFriends;
    static VBox friendVboxFriends2 = new VBox();

    Connection conn = UtilityDAL.getConnection();
    synchronized public void menuitemAccountClicked(ActionEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-account-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            //UserAccountController controller = fxmlLoader.getController();
            //controller.setdata(id);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void menuitemLogoutClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "update user_accounts set status = 'offline' where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                stop = true;
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                stage = (Stage)display.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void imageBackClicked(MouseEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            UserChatController controller = fxmlLoader.getController();
            controller.setdata(id, 0);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void vboxFriendsLoaded(){
        if (conn != null) {
            try {
                friendVboxFriends2.getChildren().clear();
                String query = "select id, displayname, status, max(session_end) as s_end from friendships join user_accounts on ((request_id = ? and accept_id = id) or (accept_id = ? and request_id = id)) and is_accepted = true join user_account_info on id = account_id join user_activity_logs on id = user_id group by id, displayname, status";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int friendid = rs.getInt("id");
                    String friendname = rs.getString("displayname");
                    String friendstatus = rs.getString("status");
                    String friendendtime = rs.getTimestamp("s_end").toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                    HBox newhboxfriendbox = new HBox();
                    newhboxfriendbox.setId("friendboxid" + friendid);
                    newhboxfriendbox.setPadding(new Insets(15, 0, 0, 30));
                    VBox newvboxfrienddata = new VBox();
                    newvboxfrienddata.setId("frienddata" + friendid);
                    newvboxfrienddata.setSpacing(10);
                    HBox newhboxfriendinfo = new HBox();
                    newhboxfriendinfo.setId("frinedinfo" + id);
                    newhboxfriendinfo.setSpacing(80);
                    Label newlabelfriendname = new Label(friendname);
                    newlabelfriendname.setFont(new Font("Courier New Bold", 30));
                    newlabelfriendname.setMinWidth(580);
                    newlabelfriendname.setMaxWidth(580);
                    Label newlabelfriendstatus = new Label();
                    newlabelfriendstatus.setFont(new Font("Courier New", 24));
                    if(friendstatus.equals("online")){
                        newlabelfriendstatus.setText("Online");
                        newlabelfriendstatus.setTextFill(Color.GREEN);
                    } else if (friendstatus.equals("locked")) {
                        newlabelfriendstatus.setText("Locked");
                        newlabelfriendstatus.setTextFill(Color.RED);
                    } else {
                        newlabelfriendstatus.setText(friendendtime);
                    }
                    HBox newhboxfriendbar = new HBox();
                    newhboxfriendbar.setId("friendbar" + friendid);
                    newhboxfriendbar.setAlignment(Pos.BOTTOM_LEFT);
                    newhboxfriendbar.setPadding(new Insets(0, 0, 0, 45));
                    newhboxfriendbar.setSpacing(50);
                    Label newlabelfriendchat = new Label("Chat");
                    newlabelfriendchat.setId(String.valueOf(friendid));
                    newlabelfriendchat.setFont(new Font("Courier New", 18));
                    Label newlabelfriendgroup = new Label("Group");
                    newlabelfriendchat.setId(String.valueOf(friendid));
                    newlabelfriendgroup.setFont(new Font("Courier New", 18));
                    Label newlabelfriendunfriend = new Label("Unfriend");
                    newlabelfriendunfriend.setId(String.valueOf(friendid));
                    newlabelfriendunfriend.setFont(new Font("Courier New", 18));
                    newlabelfriendunfriend.setTextFill(Color.RED);
                    //newlabelfriendunfriend.setOnMouseClicked(this::labelFriendunfriendClicked);
                    Label newlabelfriendblock = new Label("Block");
                    newlabelfriendblock.setId(String.valueOf(friendid));
                    newlabelfriendblock.setFont(new Font("Courier New", 18));
                    Label newlabelfriendreport = new Label("Report");
                    newlabelfriendreport.setId(String.valueOf(friendid));
                    newlabelfriendreport.setFont(new Font("Courier New", 18));
                    if (!friendstatus.equals("locked")){
                        newlabelfriendchat.setTextFill(Color.BLUE);
                        newlabelfriendchat.setOnMouseClicked(this::labelFriendchatClicked);
                        newlabelfriendgroup.setTextFill(Color.BLUE);
                        //newlabelfriendgroup.setOnMouseClicked(this::labelFriendgroupClicked);
                        newlabelfriendblock.setTextFill(Color.RED);
                        //newlabelfriendblock.setOnMouseClicked(this::labelFriendblockClicked);
                        newlabelfriendreport.setTextFill(Color.RED);
                        //newlabelfriendreport.setOnMouseClicked(this::labelFriendreportClicked);
                    }
                    newhboxfriendinfo.getChildren().addAll(newlabelfriendname, newlabelfriendstatus);
                    newhboxfriendbar.getChildren().addAll(newlabelfriendchat, newlabelfriendgroup, newlabelfriendunfriend, newlabelfriendblock, newlabelfriendreport);
                    newvboxfrienddata.getChildren().addAll(newhboxfriendinfo, newhboxfriendbar);
                    newhboxfriendbox.getChildren().add(newvboxfrienddata);
                    friendVboxFriends2.getChildren().add(newhboxfriendbox);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void labelFriendchatClicked(MouseEvent event) {
        if (conn != null) {
            try {
                String query = "select id from box_chats join box_chat_members where id = box_id and user_id = ? and is_direct = true intersect select * from box_chats join box_chat_members where id = box_id and user_id = ? and is_direct = true";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(((Label)event.getSource()).getId()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    stop = true;
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                    scene = new Scene(fxmlLoader.load(), 1080, 720);
                    UserChatController controller = fxmlLoader.getController();
                    controller.setdata(id, rs.getInt("id"));
                    stage = (Stage)display.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else {
                    query = "insert into box_chats (box_name, is_direct) values ('Direct', true); insert into ";
                    ps = conn.prepareStatement(query);
                    ps.executeUpdate();
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //stage.setOnCloseRequest(event -> logout(stage));
        stop = false;
        friendMenuitemAccount2 = friendMenuitemAccount;
        friendMenuitemLogout2 = friendMenuitemLogout;
        friendImageBack2 = friendImageBack;
        friendTabFriends2 = friendTabFriends;
        friendTextSearchfriends2 = friendTextSearchfriends;
        friendScrollFriends2 = friendScrollFriends;
        friendVboxFriends2 = friendVboxFriends;
        friendMenuitemAccount.setOnAction(this::menuitemAccountClicked);
        friendMenuitemLogout.setOnAction(this::menuitemLogoutClicked);
        friendImageBack.setOnMouseClicked(this::imageBackClicked);
        vboxFriendsLoaded();
        Runnable r1 = new UserFriendController();
        Thread t1 = new Thread(r1, "UserFriendController");
        t1.setDaemon(true);
        t1.start();
    }

    public void setdata(int gid){
        id = gid;
        vboxFriendsLoaded();
    }

    public void run(){
        while (true){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (stop){
                return;
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vboxFriendsLoaded();
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
                query = "update user_activity_logs set session_end = CURRENT_TIMESTAMP where user_id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
