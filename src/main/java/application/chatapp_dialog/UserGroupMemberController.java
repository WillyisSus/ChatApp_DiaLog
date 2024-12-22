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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserGroupMemberController implements Initializable, Runnable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;
    static GridPane display2 = new GridPane();

    static int id = 1;
    static int boxid = 1;
    static boolean stop = false;
    @FXML
    private Button memberButtonAddmember;
    static Button memberButtonAddmember2 = new Button();
    @FXML
    private VBox memberVboxMember;
    static VBox memberVboxMember2 = new VBox();
    @FXML
    private Label memberLabelGroupname;
    static Label memberLabelGroupname2 = new Label();
    @FXML
    private MenuButton memberMenuChat;
    static MenuButton memberMenuChat2 = new MenuButton();
    @FXML
    private MenuItem memberMenuitemAccount;
    static MenuItem memberMenuitemAccount2 = new MenuItem();
    @FXML
    private MenuItem memberMenuitemFriends;
    static MenuItem memberMenuitemFriends2 = new MenuItem();
    @FXML
    private MenuItem memberMenuitemLogout;
    static MenuItem memberMenuitemLogout2 = new MenuItem();
    @FXML
    private MenuButton memberMenuOnline;
    static MenuButton memberMenuOnline2 = new MenuButton();
    @FXML
    private ImageView memberImageCreategroup;
    static ImageView memberImageCreategroup2 = new ImageView();
    @FXML
    private VBox memberVboxChatbox;
    static VBox memberVboxChatbox2 = new VBox();
    @FXML
    private TextField memberTextSearchbox;
    static TextField memberTextSearchbox2 = new TextField();

    Connection conn = UtilityDAL.getConnection();
    synchronized public void vboxMemberLoaded(){
        if (conn != null){
            try{
                memberVboxMember2.getChildren().clear();
                String query = "select box_name, displayname, is_direct from box_chats join box_chat_members on id = box_id join user_account_info on user_id = account_id where box_id = ? and user_id != ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()){
                    return;
                }
                if (rs.getBoolean("is_direct")){
                    memberLabelGroupname2.setText(rs.getString("displayname"));
                } else {
                    memberLabelGroupname2.setText(rs.getString("box_name"));
                }
                query = "select user_id, displayname, username, is_admin, status from box_chat_members join user_account_info on user_id = account_id join user_accounts on user_id = id where box_id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                rs = ps.executeQuery();
                while(rs.next()){
                    int newuserid = rs.getInt(1);
                    String newdisplayname = rs.getString(2);
                    String newusername = "@" + rs.getString(3);
                    boolean newuseradmin = rs.getBoolean("is_admin");
                    String newuserstatus = rs.getString("status");
                    HBox newmembersection = new HBox();
                    newmembersection.setPadding(new Insets(0, 10, 0, 10));
                    VBox newmemberjs = new VBox();
                    newmemberjs.setSpacing(10);
                    HBox newmemberhbn = new HBox();
                    newmemberhbn.setAlignment(Pos.CENTER_LEFT);
                    newmemberhbn.setSpacing(10);
                    Label newmemberdisplayname = new Label(newdisplayname);
                    newmemberdisplayname.setFont(new Font("Courier New Bold", 24));
                    newmemberdisplayname.setMaxWidth(290);
                    Label newmemberusername = new Label(newusername);
                    newmemberusername.setFont(new Font("Courier New", 20));
                    newmemberusername.setMaxWidth(290);
                    HBox newmemberhbl = new HBox();
                    newmemberhbl.setAlignment(Pos.BOTTOM_LEFT);
                    newmemberhbl.setPadding(new Insets(0, 0, 0, 30));
                    newmemberhbl.setSpacing(40);
                    if (newuserid != id){
                        if (!newuserstatus.equals("locked")){
                            String query2 = "select * from block_lists where (user_id = ? and block_id = ?) or (user_id = ? and block_id = ?)";
                            PreparedStatement ps2 = conn.prepareStatement(query2);
                            ps2.setInt(1, id);
                            ps2.setInt(2, newuserid);
                            ps2.setInt(3, newuserid);
                            ps2.setInt(4, id);
                            ResultSet rs2 = ps2.executeQuery();
                            if (!rs2.next()){
                                Label newitemchat = new Label("Chat");
                                newitemchat.setId(String.valueOf(newuserid));
                                newitemchat.setFont(new Font("Courier New", 16));
                                newitemchat.setOnMouseClicked(this::menuitemChatClicked);
                                newmemberhbl.getChildren().add(newitemchat);
                                Label newitemblock = new Label("Block");
                                newitemblock.setId(String.valueOf(newuserid));
                                newitemblock.setFont(new Font("Courier New", 16));
                                newitemblock.setOnMouseClicked(this::menuitemBlockClicked);
                                newmemberhbl.getChildren().add(newitemblock);
                            }
                            Label newitemreport = new Label("Report");
                            newitemreport.setId(String.valueOf(newuserid));
                            newitemreport.setFont(new Font("Courier New", 16));
                            newitemreport.setOnMouseClicked(this::menuitemReportClicked);
                            newmemberhbl.getChildren().add(newitemreport);
                            query2 = "select is_admin from box_chat_members where box_id = ? and user_id = ?";
                            ps2 = conn.prepareStatement(query2);
                            ps2.setInt(1, boxid);
                            ps2.setInt(2, id);
                            rs2 = ps2.executeQuery();
                            rs2.next();
                            if (rs2.getBoolean(1)){
                                Label newitemadmin = new Label();
                                newitemadmin.setId(String.valueOf(newuserid));
                                newitemadmin.setFont(new Font("Courier New", 16));
                                if (newuseradmin){
                                    newitemadmin.setText("Remove admin");
                                    newitemadmin.setOnMouseClicked(this::menuitemAdminClicked);
                                } else {
                                    newitemadmin.setText("Give admin");
                                    newitemadmin.setOnMouseClicked(this::menuitemAdminClicked);
                                }
                                Label newitemkick = new Label("Kick");
                                newitemkick.setId(String.valueOf(newuserid));
                                newitemkick.setFont(new Font("Courier New", 16));
                                newitemkick.setOnMouseClicked(this::menuitemKickClicked);
                                newmemberhbl.getChildren().addAll(newitemadmin, newitemkick);
                            }
                        } else {
                            String query2 = "select is_admin from box_chat_members where box_id = ? and user_id = ?";
                            PreparedStatement ps2 = conn.prepareStatement(query2);
                            ps2.setInt(1, boxid);
                            ps2.setInt(2, id);
                            ResultSet rs2 = ps2.executeQuery();
                            rs2.next();
                            if (rs2.getBoolean(1)){
                                Label newitemkick = new Label("Kick");
                                newitemkick.setId(String.valueOf(newuserid));
                                newitemkick.setFont(new Font("Courier New", 16));
                                newitemkick.setOnMouseClicked(this::menuitemKickClicked);
                                newmemberhbl.getChildren().add(newitemkick);
                            }
                        }
                    } else {
                        Label newitemreport = new Label("Report");
                        newitemreport.setId(String.valueOf(newuserid));
                        newitemreport.setFont(new Font("Courier New", 16));
                        newitemreport.setOnMouseClicked(this::menuitemReportClicked);
                        Label newitemleave = new Label("Leave");
                        newitemleave.setId(String.valueOf(newuserid));
                        newitemleave.setFont(new Font("Courier New", 16));
                        newitemleave.setOnMouseClicked(this::labelLeaveClicked);
                        newmemberhbl.getChildren().addAll(newitemreport, newitemleave);
                    }
                    newmemberhbn.getChildren().addAll(newmemberdisplayname, newmemberusername);
                    newmemberjs.getChildren().addAll(newmemberhbn, newmemberhbl);
                    newmembersection.getChildren().add(newmemberjs);
                    memberVboxMember2.getChildren().add(newmembersection);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void menuitemChatClicked(MouseEvent event){
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
                    menuitemChatClicked(event);
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void menuitemBlockClicked(MouseEvent event){
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
                vboxMemberLoaded();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Blocked.");
                alert.show();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void menuitemReportClicked(MouseEvent event){
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
                alert.show();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void menuitemAdminClicked(MouseEvent event){
        if (conn != null) {
            try {
                String query = "update box_chat_members set is_admin = !is_admin where user_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(((Label)event.getSource()).getId()));
                ps.executeUpdate();
                vboxMemberLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void menuitemKickClicked(MouseEvent event){
        if (conn != null) {
            try {
                String query = "delete from box_chat_members where box_id = ? and user_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, Integer.parseInt(((Label)event.getSource()).getId()));
                ps.executeUpdate();
                vboxMemberLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void labelLeaveClicked(MouseEvent event){
        if (conn != null) {
            try {
                String query = "select is_admin from box_chat_members where box_id = ? and user_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getBoolean("is_admin")){
                    query = "select user_id from box_chat_members where box_id = ? and user_id != ? and is_admin = true";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, boxid);
                    ps.setInt(2, id);
                    rs = ps.executeQuery();
                    if (!rs.next()){
                        query = "update box_chat_members set is_admin = true where box_id = ? and user_id = (select user_id from box_chat_members where box_id = ? and user_id != ? limit 1)";
                        ps = conn.prepareStatement(query);
                        ps.setInt(1, boxid);
                        ps.setInt(2, boxid);
                        ps.setInt(3, id);
                        ps.executeUpdate();
                    }
                }
                query = "delete from box_chat_members where box_id = ? and user_id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ps.executeUpdate();
                stop = true;
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                stage = (Stage)display.getScene().getWindow();
                stage.setScene(scene);
                UserChatController controller = fxmlLoader.getController();
                controller.setdata(id, 0, stage);
                stage.show();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void labelGroupnameClicked(MouseEvent event){
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Notification");
        td.setHeaderText("Enter group name.");
        td.setContentText("Max 32 characters.");
        td.showAndWait();
        String newGroupname = td.getResult();
        if (newGroupname != null && !newGroupname.isBlank() && newGroupname.length() <= 32){
            if (conn != null) {
                try {
                    String query = "update box_chats set box_name = ? where id = ?";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, newGroupname);
                    ps.setInt(2, boxid);
                    ps.executeUpdate();
                    vboxChatboxLoaded();
                    vboxMemberLoaded();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    synchronized public void menuChatClicked(Event event){
        if (boxid == 0){
            return;
        }
        if (conn != null) {
            memberMenuChat2.getItems().clear();
            MenuItem newitemmember = new MenuItem("Members");
            newitemmember.setOnAction(this::menuitemMembersClicked);
            MenuItem newitemleave = new MenuItem("Leave");
            newitemleave.setOnAction(this::menuitemLeaveClicked);
            memberMenuChat2.getItems().addAll(newitemmember, newitemleave);
        }
    }
    synchronized public void menuitemMembersClicked(ActionEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display2.getScene().getWindow();
            stage.setScene(scene);
            UserChatController controller = fxmlLoader.getController();
            controller.setdata(id, boxid, stage);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void menuitemLeaveClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "select is_admin from box_chat_members where box_id = ? and user_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getBoolean("is_admin")){
                    query = "select user_id from box_chat_members where box_id = ? and user_id != ? and is_admin = true";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, boxid);
                    ps.setInt(2, id);
                    rs = ps.executeQuery();
                    if (!rs.next()){
                        query = "update box_chat_members set is_admin = true where box_id = ? and user_id = (select user_id from box_chat_members where box_id = ? and user_id != ? limit 1)";
                        ps = conn.prepareStatement(query);
                        ps.setInt(1, boxid);
                        ps.setInt(2, boxid);
                        ps.setInt(3, id);
                        ps.executeUpdate();
                    }
                }
                query = "delete from box_chat_members where box_id = ? and user_id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ps.executeUpdate();
                stop = true;
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                stage = (Stage)display.getScene().getWindow();
                stage.setScene(scene);
                UserChatController controller = fxmlLoader.getController();
                controller.setdata(id, 0, stage);
                stage.show();
            } catch (SQLException | IOException e) {
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
                memberMenuOnline2.getItems().clear();
                String query = "select id, username, displayname from user_account_info join user_accounts on account_id = id join friendships on ((id = request_id and accept_id = ?) or (request_id = ? and id = accept_id)) where is_accepted = true and status = 'online'";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    MenuItem newonlinefriend = new MenuItem(rs.getString("displayname") + " @" + rs.getString("username"));
                    newonlinefriend.setId(String.valueOf(rs.getInt("id")));
                    newonlinefriend.setOnAction(this::menuitemOnlineClicked);
                    memberMenuOnline2.getItems().add(newonlinefriend);
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
    synchronized public void imageCreategroupClicked(MouseEvent event){
        try{
            List<Integer> newuserlist = new ArrayList<>();
            newuserlist.add(id);
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-create-group-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            UserCreateGroupController controller = fxmlLoader.getController();
            controller.setdata(id, stage, newuserlist);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void vboxChatboxLoaded(){
        if (conn != null) {
            try {
                memberVboxChatbox2.getChildren().clear();
                String boxssearch = memberTextSearchbox2.getText();
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
                    memberVboxChatbox2.getChildren().add(newhboxbox);
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
    synchronized public void buttonAddmemberClicked(ActionEvent event){
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Notification");
        td.setHeaderText("Enter username.");
        td.showAndWait();
        String newusernameadd = td.getResult();
        if (newusernameadd != null && !newusernameadd.isBlank()){
            Connection conn = UtilityDAL.getConnection();
            if (conn != null) {
                try {
                    String query = "select * from friendships join user_accounts on request_id = id where accept_id = ? and is_accepted = true and username = ? and status != 'locked'\n" +
                            "union\n" +
                            "select * from friendships join user_accounts on accept_id = id where request_id = ? and is_accepted = true and username = ? and status != 'locked'";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setInt(1, id);
                    ps.setString(2, newusernameadd);
                    ps.setInt(3, id);
                    ps.setString(4, newusernameadd);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        query = "insert into box_chat_members (box_id, user_id) values (?, ?)";
                        ps = conn.prepareStatement(query);
                        ps.setInt(1, boxid);
                        ps.setInt(2, rs.getInt("id"));
                        ps.executeUpdate();
                        vboxMemberLoaded();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Notification");
                        alert.setHeaderText("This user must be your friend first.");
                        alert.show();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stop = false;
        display2 = display;
        memberButtonAddmember2 = memberButtonAddmember;
        memberVboxMember2 = memberVboxMember;
        memberLabelGroupname2 = memberLabelGroupname;
        memberMenuChat2 = memberMenuChat;
        memberMenuitemAccount2 = memberMenuitemAccount;
        memberMenuitemFriends2 = memberMenuitemFriends;
        memberMenuitemLogout2 = memberMenuitemLogout;
        memberMenuOnline2 = memberMenuOnline;
        memberImageCreategroup2 = memberImageCreategroup;
        memberVboxChatbox2 = memberVboxChatbox;
        memberTextSearchbox2 = memberTextSearchbox;
        memberLabelGroupname.setText("");
        memberTextSearchbox.setText("");
        memberLabelGroupname.setOnMouseClicked(this::labelGroupnameClicked);
        memberMenuChat.setOnShowing(this::menuChatClicked);
        memberMenuitemAccount.setOnAction(this::menuitemAccountClicked);
        memberMenuitemFriends.setOnAction(this::menuitemFriendsClicked);
        memberMenuitemLogout.setOnAction(this::menuitemLogoutClicked);
        memberMenuOnline.setOnShowing(this::menuOnlineClicked);
        memberImageCreategroup.setOnMouseClicked(this::imageCreategroupClicked);
        memberButtonAddmember.setOnAction(this::buttonAddmemberClicked);
        vboxChatboxLoaded();
        vboxMemberLoaded();
        Runnable r1 = new UserGroupMemberController();
        Thread t1 = new Thread(r1, "UserGroupMemberController");
        t1.setDaemon(true);
        t1.start();
    }

    public void setdata(int gid, int gboxid, Stage gstage){
        id = gid;
        boxid = gboxid;
        stage = gstage;
        stage.setOnCloseRequest(event -> logout(stage));
        vboxChatboxLoaded();
        vboxMemberLoaded();
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
                    vboxMemberLoaded();
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
