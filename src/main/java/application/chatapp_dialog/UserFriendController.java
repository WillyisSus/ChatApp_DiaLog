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
import java.util.*;

public class UserFriendController implements Initializable, Runnable  {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;
    static GridPane display2 = new GridPane();

    static int id = 1;
    static boolean stop = false;
    static int tabid;
    @FXML
    private TabPane friendTabpaneFriend;
    static TabPane friendTabpaneFriend2 = new TabPane();
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
    private TextField friendTextFriends;
    static TextField friendTextFriends2 = new TextField();
    @FXML
    private TextField friendTextOnlines;
    static TextField friendTextOnlines2 = new TextField();
    @FXML
    private TextField friendTextRequests;
    static TextField friendTextRequests2 = new TextField();
    @FXML
    private TextField friendTextBlocks;
    static TextField friendTextBlocks2 = new TextField();
    @FXML
    private TextField friendTextAdds;
    static TextField friendTextAdds2 = new TextField();
    @FXML
    private VBox friendVboxFriends;
    static VBox friendVboxFriends2 = new VBox();
    @FXML
    private VBox friendVboxOnlines;
    static VBox friendVboxOnlines2 = new VBox();
    @FXML
    private VBox friendVboxRequests;
    static VBox friendVboxRequests2 = new VBox();
    @FXML
    private VBox friendVboxBlocks;
    static VBox friendVboxBlocks2 = new VBox();
    @FXML
    private VBox friendVboxAdds;
    static VBox friendVboxAdds2 = new VBox();

    Connection conn = UtilityDAL.getConnection();
    synchronized public void menuitemAccountClicked(ActionEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-account-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            //UserAccountController controller = fxmlLoader.getController();
            //controller.setdata(id);
            stage = (Stage)display2.getScene().getWindow();
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
                stage = (Stage)display2.getScene().getWindow();
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
            stage = (Stage)display2.getScene().getWindow();
            stage.setScene(scene);
            UserChatController controller = fxmlLoader.getController();
            controller.setdata(id, 0, stage);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void vboxFriendsLoaded(){
        if (conn != null) {
            try {
                friendVboxFriends2.getChildren().clear();
                String friendssearch = friendTextFriends2.getText();
                String query = "select id, username, displayname, status, max(session_end) as s_end from friendships join user_accounts on ((request_id = ? and accept_id = id) or (accept_id = ? and request_id = id)) and is_accepted = true join user_account_info on id = account_id join user_activity_logs on id = user_id group by id, displayname, status";
                if (friendssearch != null && !friendssearch.isBlank()){
                    query = query.replace(" group by id, displayname, status", " where displayname like ? or username like ? group by id, displayname, status");
                }
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                if (friendssearch != null && !friendssearch.isBlank()){
                    ps.setString(3, "%" + friendssearch + "%");
                    ps.setString(4, "%" + friendssearch + "%");
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int friendid = rs.getInt("id");
                    String friendname = rs.getString("displayname");
                    String friendusername = "@" + rs.getString("username");
                    String friendstatus = rs.getString("status");
                    String friendendtime = rs.getTimestamp("s_end").toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                    HBox newhboxfriendbox = new HBox();
                    newhboxfriendbox.setPadding(new Insets(15, 0, 0, 30));
                    VBox newvboxfrienddata = new VBox();
                    newvboxfrienddata.setSpacing(10);
                    HBox newhboxfriendinfo = new HBox();
                    newhboxfriendinfo.setAlignment(Pos.CENTER_LEFT);
                    newhboxfriendinfo.setSpacing(80);
                    VBox newvboxfriendname = new VBox();
                    Label newlabelfriendname = new Label(friendname);
                    newlabelfriendname.setFont(new Font("Courier New Bold", 30));
                    newlabelfriendname.setMinWidth(580);
                    newlabelfriendname.setMaxWidth(580);
                    Label newlabelfriendusername = new Label(friendusername);
                    newlabelfriendusername.setFont(new Font("Courier New", 20));
                    newlabelfriendusername.setMinWidth(580);
                    newlabelfriendusername.setMaxWidth(580);
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
                    newhboxfriendbar.setAlignment(Pos.BOTTOM_LEFT);
                    newhboxfriendbar.setPadding(new Insets(0, 0, 0, 45));
                    newhboxfriendbar.setSpacing(50);
                    Label newlabelfriendchat = new Label("Chat");
                    newlabelfriendchat.setId(String.valueOf(friendid));
                    newlabelfriendchat.setFont(new Font("Courier New", 18));
                    Label newlabelfriendgroup = new Label("Group");
                    newlabelfriendgroup.setId(String.valueOf(friendid));
                    newlabelfriendgroup.setFont(new Font("Courier New", 18));
                    Label newlabelfriendunfriend = new Label("Unfriend");
                    newlabelfriendunfriend.setId(String.valueOf(friendid));
                    newlabelfriendunfriend.setFont(new Font("Courier New", 18));
                    newlabelfriendunfriend.setTextFill(Color.RED);
                    newlabelfriendunfriend.setOnMouseClicked(this::labelFriendunfriendClicked);
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
                        newlabelfriendgroup.setOnMouseClicked(this::labelFriendgroupClicked);
                        newlabelfriendblock.setTextFill(Color.RED);
                        newlabelfriendblock.setOnMouseClicked(this::labelFriendblockClicked);
                        newlabelfriendreport.setTextFill(Color.RED);
                        newlabelfriendreport.setOnMouseClicked(this::labelFriendreportClicked);
                    }
                    newvboxfriendname.getChildren().addAll(newlabelfriendname, newlabelfriendusername);
                    newhboxfriendinfo.getChildren().addAll(newvboxfriendname, newlabelfriendstatus);
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
    synchronized public void vboxOnlinesLoaded(){
        if (conn != null) {
            try {
                friendVboxOnlines2.getChildren().clear();
                String onlinessearch = friendTextOnlines2.getText();
                String query = "select id, username, displayname from friendships join user_accounts on ((request_id = ? and accept_id = id) or (accept_id = ? and request_id = id)) and is_accepted = true join user_account_info on id = account_id where status = 'online'";
                if (onlinessearch != null && !onlinessearch.isBlank()){
                    query += " and displayname like ? or username like ?";
                }
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                if (onlinessearch != null && !onlinessearch.isBlank()){
                    ps.setString(3, "%" + onlinessearch + "%");
                    ps.setString(4, "%" + onlinessearch + "%");
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int onlineid = rs.getInt("id");
                    String onlinename = rs.getString("displayname");
                    String onlineusername = "@" + rs.getString("username");
                    HBox newhboxonlinebox = new HBox();
                    newhboxonlinebox.setPadding(new Insets(15, 0, 0, 30));
                    VBox newvboxonlinedata = new VBox();
                    newvboxonlinedata.setSpacing(10);
                    HBox newhboxonlineinfo = new HBox();
                    newhboxonlineinfo.setAlignment(Pos.CENTER_LEFT);
                    newhboxonlineinfo.setSpacing(80);
                    VBox newvboxonlinename = new VBox();
                    Label newlabelonlinename = new Label(onlinename);
                    newlabelonlinename.setFont(new Font("Courier New Bold", 30));
                    newlabelonlinename.setMinWidth(580);
                    newlabelonlinename.setMaxWidth(580);
                    Label newlabelonlineusername = new Label(onlineusername);
                    newlabelonlineusername.setFont(new Font("Courier New", 20));
                    newlabelonlineusername.setMinWidth(580);
                    newlabelonlineusername.setMaxWidth(580);
                    Label newlabelonlinestatus = new Label();
                    newlabelonlinestatus.setFont(new Font("Courier New", 24));
                    newlabelonlinestatus.setText("Online");
                    newlabelonlinestatus.setTextFill(Color.GREEN);
                    HBox newhboxonlinebar = new HBox();
                    newhboxonlinebar.setAlignment(Pos.BOTTOM_LEFT);
                    newhboxonlinebar.setPadding(new Insets(0, 0, 0, 45));
                    newhboxonlinebar.setSpacing(50);
                    Label newlabelonlinechat = new Label("Chat");
                    newlabelonlinechat.setId(String.valueOf(onlineid));
                    newlabelonlinechat.setFont(new Font("Courier New", 18));
                    Label newlabelonlinegroup = new Label("Group");
                    newlabelonlinegroup.setId(String.valueOf(onlineid));
                    newlabelonlinegroup.setFont(new Font("Courier New", 18));
                    Label newlabelonlineunfriend = new Label("Unfriend");
                    newlabelonlineunfriend.setId(String.valueOf(onlineid));
                    newlabelonlineunfriend.setFont(new Font("Courier New", 18));
                    newlabelonlineunfriend.setTextFill(Color.RED);
                    newlabelonlineunfriend.setOnMouseClicked(this::labelFriendunfriendClicked);
                    Label newlabelonlineblock = new Label("Block");
                    newlabelonlineblock.setId(String.valueOf(onlineid));
                    newlabelonlineblock.setFont(new Font("Courier New", 18));
                    Label newlabelonlinereport = new Label("Report");
                    newlabelonlinereport.setId(String.valueOf(onlineid));
                    newlabelonlinereport.setFont(new Font("Courier New", 18));
                    newlabelonlinechat.setTextFill(Color.BLUE);
                    newlabelonlinechat.setOnMouseClicked(this::labelFriendchatClicked);
                    newlabelonlinegroup.setTextFill(Color.BLUE);
                    newlabelonlinegroup.setOnMouseClicked(this::labelFriendgroupClicked);
                    newlabelonlineblock.setTextFill(Color.RED);
                    newlabelonlineblock.setOnMouseClicked(this::labelFriendblockClicked);
                    newlabelonlinereport.setTextFill(Color.RED);
                    newlabelonlinereport.setOnMouseClicked(this::labelFriendreportClicked);
                    newvboxonlinename.getChildren().addAll(newlabelonlinename, newlabelonlineusername);
                    newhboxonlineinfo.getChildren().addAll(newvboxonlinename, newlabelonlinestatus);
                    newhboxonlinebar.getChildren().addAll(newlabelonlinechat, newlabelonlinegroup, newlabelonlineunfriend, newlabelonlineblock, newlabelonlinereport);
                    newvboxonlinedata.getChildren().addAll(newhboxonlineinfo, newhboxonlinebar);
                    newhboxonlinebox.getChildren().add(newvboxonlinedata);
                    friendVboxOnlines2.getChildren().add(newhboxonlinebox);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void vboxRequestsLoaded(){
        if (conn != null) {
            try {
                friendVboxRequests2.getChildren().clear();
                String requestssearch = friendTextRequests2.getText();
                String query = "select id, username, displayname, status from friendships join user_accounts on (request_id = id and accept_id = ?) and is_accepted = false join user_account_info on id = account_id";
                if (requestssearch != null && !requestssearch.isBlank()){
                    query += " where displayname like ? or username like ?";
                }
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                if (requestssearch != null && !requestssearch.isBlank()){
                    ps.setString(2, "%" + requestssearch + "%");
                    ps.setString(3, "%" + requestssearch + "%");
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int requestid = rs.getInt("id");
                    String requestname = rs.getString("displayname");
                    String requestusername = "@" + rs.getString("username");
                    String requeststatus = rs.getString("status");
                    HBox newhboxrequestbox = new HBox();
                    newhboxrequestbox.setPadding(new Insets(15, 0, 0, 30));
                    VBox newvboxrequestdata = new VBox();
                    newvboxrequestdata.setSpacing(10);
                    HBox newhboxrequestinfo = new HBox();
                    newhboxrequestinfo.setAlignment(Pos.CENTER_LEFT);
                    newhboxrequestinfo.setSpacing(40);
                    VBox newvboxrequestname = new VBox();
                    Label newlabelrequestname = new Label(requestname);
                    newlabelrequestname.setFont(new Font("Courier New Bold", 30));
                    newlabelrequestname.setMinWidth(580);
                    newlabelrequestname.setMaxWidth(580);
                    Label newlabelrequestusername = new Label(requestusername);
                    newlabelrequestusername.setFont(new Font("Courier New", 24));
                    newlabelrequestusername.setMinWidth(580);
                    newlabelrequestusername.setMaxWidth(580);
                    Button newbuttonaccept = new Button("Accept");
                    newbuttonaccept.setId(String.valueOf(requestid));
                    newbuttonaccept.setFont(new Font("Courier New", 20));
                    if (requeststatus.equals("locked")){
                        newbuttonaccept.setTextFill(Color.GRAY);
                    } else {
                        newbuttonaccept.setOnAction(this::buttonAcceptClicked);
                    }
                    Button newbuttonremove = new Button("Remove");
                    newbuttonremove.setId(String.valueOf(requestid));
                    newbuttonremove.setFont(new Font("Courier New", 20));
                    newbuttonremove.setTextFill(Color.RED);
                    newbuttonremove.setOnAction(this::buttonRemoveClicked);
                    HBox newhboxrequestbar = new HBox();
                    newhboxrequestbar.setAlignment(Pos.BOTTOM_LEFT);
                    newhboxrequestbar.setPadding(new Insets(0, 0, 0, 45));
                    newhboxrequestbar.setSpacing(50);
                    Label newlabelrequestchat = new Label("Chat");
                    newlabelrequestchat.setId(String.valueOf(requestid));
                    newlabelrequestchat.setFont(new Font("Courier New", 18));
                    Label newlabelrequestblock = new Label("Block");
                    newlabelrequestblock.setId(String.valueOf(requestid));
                    newlabelrequestblock.setFont(new Font("Courier New", 18));
                    Label newlabelrequestreport = new Label("Report");
                    newlabelrequestreport.setId(String.valueOf(requestid));
                    newlabelrequestreport.setFont(new Font("Courier New", 18));
                    if (!requeststatus.equals("locked")){
                        newlabelrequestchat.setTextFill(Color.BLUE);
                        newlabelrequestchat.setOnMouseClicked(this::labelFriendchatClicked);
                        newlabelrequestblock.setTextFill(Color.RED);
                        newlabelrequestblock.setOnMouseClicked(this::labelFriendblockClicked);
                        newlabelrequestreport.setTextFill(Color.RED);
                        newlabelrequestreport.setOnMouseClicked(this::labelFriendreportClicked);
                    }
                    newvboxrequestname.getChildren().addAll(newlabelrequestname, newlabelrequestusername);
                    newhboxrequestinfo.getChildren().addAll(newvboxrequestname, newbuttonaccept, newbuttonremove);
                    newhboxrequestbar.getChildren().addAll(newlabelrequestchat, newlabelrequestblock, newlabelrequestreport);
                    newvboxrequestdata.getChildren().addAll(newhboxrequestinfo, newhboxrequestbar);
                    newhboxrequestbox.getChildren().add(newvboxrequestdata);
                    friendVboxRequests2.getChildren().add(newhboxrequestbox);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void vboxBlocksLoaded(){
        if (conn != null) {
            try {
                friendVboxBlocks2.getChildren().clear();
                String blockssearch = friendTextBlocks2.getText();
                String query = "select user_accounts.id as id, username, displayname from block_lists join user_accounts on user_id = user_accounts.id join user_account_info on user_accounts.id = account_id where block_id = ?";
                if (blockssearch != null && !blockssearch.isBlank()){
                    query += " and displayname like ? or username like ?";
                }
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                if (blockssearch != null && !blockssearch.isBlank()){
                    ps.setString(2, "%" + blockssearch + "%");
                    ps.setString(3, "%" + blockssearch + "%");
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int blockid = rs.getInt("id");
                    String blockname = rs.getString("displayname");
                    String blockusername = "@" + rs.getString("username");
                    HBox newhboxblockinfo = new HBox();
                    newhboxblockinfo.setAlignment(Pos.CENTER_LEFT);
                    newhboxblockinfo.setPadding(new Insets(15, 0, 0, 30));
                    newhboxblockinfo.setSpacing(40);
                    VBox newvboxblockname = new VBox();
                    Label newlabelblockname = new Label(blockname);
                    newlabelblockname.setFont(new Font("Courier New Bold", 30));
                    newlabelblockname.setMinWidth(580);
                    newlabelblockname.setMaxWidth(580);
                    Label newlabelblockusername = new Label(blockusername);
                    newlabelblockusername.setFont(new Font("Courier New", 24));
                    newlabelblockusername.setMinWidth(580);
                    newlabelblockusername.setMaxWidth(580);
                    Button newbuttonremove = new Button("Remove");
                    newbuttonremove.setId(String.valueOf(blockid));
                    newbuttonremove.setFont(new Font("Courier New", 20));
                    newbuttonremove.setTextFill(Color.RED);
                    newbuttonremove.setOnAction(this::buttonRemoveblockClicked);
                    newvboxblockname.getChildren().addAll(newlabelblockname, newlabelblockusername);
                    newhboxblockinfo.getChildren().addAll(newvboxblockname, newbuttonremove);
                    friendVboxBlocks2.getChildren().add(newhboxblockinfo);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void vboxAddsLoaded(){
        if (conn != null) {
            try {
                friendVboxAdds2.getChildren().clear();
                String addssearch = friendTextAdds2.getText();
                if (addssearch == null || addssearch.isBlank()){
                    return;
                }
                String query = "select id, username, displayname from user_accounts join user_account_info on id = account_id where username = ? and id != ? and status != 'locked'\n" +
                        "except\n" +
                        "select user_accounts.id as id, username, displayname from user_accounts join block_lists on user_accounts.id = block_id join user_account_info on user_accounts.id = account_id where user_id = ?\n" +
                        "except\n" +
                        "select user_accounts.id as id, username, displayname from user_accounts join block_lists on user_accounts.id = user_id join user_account_info on user_accounts.id = account_id where block_id = ?\n" +
                        "except\n" +
                        "select id, username, displayname from friendships join user_accounts on ((request_id = id and accept_id = ?) or (accept_id = id and request_id = ?)) and is_accepted = true join user_account_info on id = account_id\n" +
                        "except\n" +
                        "select id, username, displayname from friendships join user_accounts on request_id = id and accept_id = ? and is_accepted = false join user_account_info on id = account_id";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, addssearch);
                ps.setInt(2, id);
                ps.setInt(3, id);
                ps.setInt(4, id);
                ps.setInt(5, id);
                ps.setInt(6, id);
                ps.setInt(7, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    int addid = rs.getInt("id");
                    String addname = rs.getString("displayname");
                    String addusername = "@" + rs.getString("username");
                    HBox newhboxaddbox = new HBox();
                    newhboxaddbox.setPadding(new Insets(15, 0, 0, 30));
                    VBox newvboxadddata = new VBox();
                    newvboxadddata.setSpacing(10);
                    HBox newhboxaddinfo = new HBox();
                    newhboxaddinfo.setAlignment(Pos.CENTER_LEFT);
                    newhboxaddinfo.setSpacing(40);
                    VBox newvboxaddname = new VBox();
                    Label newlabeladdname = new Label(addname);
                    newlabeladdname.setFont(new Font("Courier New Bold", 30));
                    newlabeladdname.setMinWidth(580);
                    newlabeladdname.setMaxWidth(580);
                    Label newlabeladdusername = new Label(addusername);
                    newlabeladdusername.setFont(new Font("Courier New", 24));
                    newlabeladdusername.setMinWidth(580);
                    newlabeladdusername.setMaxWidth(580);
                    Button newbuttonadd = new Button("Add");
                    newbuttonadd.setId(String.valueOf(addid));
                    newbuttonadd.setFont(new Font("Courier New", 20));
                    String query2 = "select * from friendships where request_id = ? and accept_id = ? and is_accepted = false";
                    PreparedStatement ps2 = conn.prepareStatement(query2);
                    ps2.setInt(1, id);
                    ps2.setInt(2, addid);
                    ResultSet rs2 = ps2.executeQuery();
                    if (rs2.next()){
                        newbuttonadd.setText("Remove");
                        newbuttonadd.setOnAction(this::buttonRemoveaddClicked);
                    } else {
                        newbuttonadd.setOnAction(this::buttonAddClicked);
                    }
                    HBox newhboxaddbar = new HBox();
                    newhboxaddbar.setAlignment(Pos.BOTTOM_LEFT);
                    newhboxaddbar.setPadding(new Insets(0, 0, 0, 45));
                    newhboxaddbar.setSpacing(50);
                    Label newlabeladdchat = new Label("Chat");
                    newlabeladdchat.setId(String.valueOf(addid));
                    newlabeladdchat.setFont(new Font("Courier New", 18));
                    Label newlabeladdblock = new Label("Block");
                    newlabeladdblock.setId(String.valueOf(addid));
                    newlabeladdblock.setFont(new Font("Courier New", 18));
                    Label newlabeladdreport = new Label("Report");
                    newlabeladdreport.setId(String.valueOf(addid));
                    newlabeladdreport.setFont(new Font("Courier New", 18));
                    newlabeladdchat.setTextFill(Color.BLUE);
                    newlabeladdchat.setOnMouseClicked(this::labelFriendchatClicked);
                    newlabeladdblock.setTextFill(Color.RED);
                    newlabeladdblock.setOnMouseClicked(this::labelFriendblockClicked);
                    newlabeladdreport.setTextFill(Color.RED);
                    newlabeladdreport.setOnMouseClicked(this::labelFriendreportClicked);
                    newvboxaddname.getChildren().addAll(newlabeladdname, newlabeladdusername);
                    newhboxaddinfo.getChildren().addAll(newvboxaddname, newbuttonadd);
                    newhboxaddbar.getChildren().addAll(newlabeladdchat, newlabeladdblock, newlabeladdreport);
                    newvboxadddata.getChildren().addAll(newhboxaddinfo, newhboxaddbar);
                    newhboxaddbox.getChildren().add(newvboxadddata);
                    friendVboxAdds2.getChildren().add(newhboxaddbox);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void labelFriendchatClicked(MouseEvent event) {
        if (conn != null) {
            try {
                String query = "select id from box_chats join box_chat_members on id = box_id where user_id = ? and is_direct = true intersect select id from box_chats join box_chat_members on id = box_id where user_id = ? and is_direct = true";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(((Label)event.getSource()).getId()));
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
                    ps.setInt(4, Integer.parseInt(((Label)event.getSource()).getId()));
                    ps.executeUpdate();
                    labelFriendchatClicked(event);
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void labelFriendgroupClicked(MouseEvent event){
        if (conn != null) {
            try {
                List<Integer> newfriendlist = new ArrayList<>();
                newfriendlist.add(id);
                newfriendlist.add(Integer.parseInt(((Label)event.getSource()).getId()));
                stop = true;
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-create-group-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                stage = (Stage)display2.getScene().getWindow();
                stage.setScene(scene);
                UserCreateGroupController controller = fxmlLoader.getController();
                controller.setdata(id, stage, newfriendlist);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void labelFriendunfriendClicked(MouseEvent event){
        if (conn != null) {
            try {
                int unfriendid = Integer.parseInt(((Label)event.getSource()).getId());
                String query = "delete from friendships where (request_id = ? or accept_id = ?) or (request_id = ? or accept_id = ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, unfriendid);
                ps.setInt(3, unfriendid);
                ps.setInt(4, id);
                ps.executeUpdate();
                vboxFriendsLoaded();
                vboxOnlinesLoaded();
                vboxAddsLoaded();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Removed.");
                alert.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void labelFriendblockClicked(MouseEvent event){
        if (conn != null) {
            try {
                int blockid = Integer.parseInt(((Label)event.getSource()).getId());
                String query = "delete from friendships where (request_id = ? or accept_id = ?) or (request_id = ? or accept_id = ?); insert into block_lists (user_id, block_id) values (?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, blockid);
                ps.setInt(3, blockid);
                ps.setInt(4, id);
                ps.setInt(5, blockid);
                ps.setInt(6, id);
                ps.executeUpdate();
                vboxFriendsLoaded();
                vboxOnlinesLoaded();
                vboxRequestsLoaded();
                vboxBlocksLoaded();
                vboxAddsLoaded();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Blocked.");
                alert.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void labelFriendreportClicked(MouseEvent event){
        if (conn != null) {
            try {
                String query = "insert into reports (reporter_id, reported_id) values (?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(((Label)event.getSource()).getId()));
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Reported.");
                alert.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void buttonAcceptClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "update friendships set is_accepted = true where request_id = ? and accept_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(((Button)event.getSource()).getId()));
                ps.setInt(2, id);
                ps.executeUpdate();
                vboxFriendsLoaded();
                vboxOnlinesLoaded();
                vboxRequestsLoaded();
                vboxAddsLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void buttonRemoveClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "delete from friendships where request_id = ? and accept_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(((Button)event.getSource()).getId()));
                ps.setInt(2, id);
                ps.executeUpdate();
                vboxRequestsLoaded();
                vboxAddsLoaded();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Removed.");
                alert.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void buttonRemoveblockClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "delete from block_lists where user_id = ? and block_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(((Button)event.getSource()).getId()));
                ps.setInt(2, id);
                ps.executeUpdate();
                vboxBlocksLoaded();
                vboxAddsLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void buttonAddClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "insert into friendships (request_id, accept_id) values(?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(((Button)event.getSource()).getId()));
                ps.executeUpdate();
                vboxRequestsLoaded();
                vboxAddsLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void buttonRemoveaddClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "delete from friendships where request_id = ? and accept_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(((Button)event.getSource()).getId()));
                ps.executeUpdate();
                vboxRequestsLoaded();
                vboxAddsLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stop = false;
        display2 = display;
        friendTabpaneFriend2 = friendTabpaneFriend;
        friendMenuitemAccount2 = friendMenuitemAccount;
        friendMenuitemLogout2 = friendMenuitemLogout;
        friendImageBack2 = friendImageBack;
        friendTextFriends2 = friendTextFriends;
        friendTextOnlines2 = friendTextOnlines;
        friendTextRequests2 = friendTextRequests;
        friendTextBlocks2 = friendTextBlocks;
        friendTextAdds2 = friendTextAdds;
        friendVboxFriends2 = friendVboxFriends;
        friendVboxOnlines2 = friendVboxOnlines;
        friendVboxRequests2 = friendVboxRequests;
        friendVboxBlocks2 = friendVboxBlocks;
        friendVboxAdds2 = friendVboxAdds;
        friendTextFriends.setText("");
        friendMenuitemAccount.setOnAction(this::menuitemAccountClicked);
        friendMenuitemLogout.setOnAction(this::menuitemLogoutClicked);
        friendImageBack.setOnMouseClicked(this::imageBackClicked);
        vboxFriendsLoaded();
        vboxOnlinesLoaded();
        vboxRequestsLoaded();
        vboxBlocksLoaded();
        vboxAddsLoaded();
        Runnable r1 = new UserFriendController();
        Thread t1 = new Thread(r1, "UserFriendController");
        t1.setDaemon(true);
        t1.start();
    }

    public void setdata(int gid, Stage gstage){
        id = gid;
        stage = gstage;
        stage.setOnCloseRequest(event -> logout(stage));
        vboxFriendsLoaded();
        vboxOnlinesLoaded();
        vboxRequestsLoaded();
        vboxBlocksLoaded();
        vboxAddsLoaded();
    }

    public void run(){
        while (true){
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
                if (!rs.next() || rs.getString("status").equals("locked")){
                    stop = true;
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-login-view.fxml"));
                    scene = new Scene(fxmlLoader.load(), 1080, 720);
                    stage = (Stage)display2.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
            if (stop){
                return;
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tabid = friendTabpaneFriend2.getTabs().indexOf(friendTabpaneFriend2.getSelectionModel().getSelectedItem());
                    if (tabid == 0){
                        vboxFriendsLoaded();
                    } else if (tabid == 1) {
                        vboxOnlinesLoaded();
                    } else if (tabid == 2) {
                        vboxRequestsLoaded();
                    } else if (tabid == 3) {
                        vboxBlocksLoaded();
                    } else if (tabid == 4) {
                        vboxAddsLoaded();
                    }
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
