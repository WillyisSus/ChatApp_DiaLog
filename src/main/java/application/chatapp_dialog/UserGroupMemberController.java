package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
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

public class UserGroupMemberController implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    int id = 1;
    int boxid = 1;
    @FXML
    private Button memberButtonAddmember;
    @FXML
    private VBox memberVboxMember;
    @FXML
    private Label memberLabelGroupname;
    @FXML
    private MenuButton memberMenuChat;
    @FXML
    private MenuButton memberMenuAccount;
    @FXML
    private MenuItem memberMenuitemAccount;
    @FXML
    private MenuItem memberMenuitemFriends;
    @FXML
    private MenuItem memberMenuitemLogout;
    @FXML
    private MenuButton memberMenuOnline;
    @FXML
    private ImageView memberImageCreategroup;
    @FXML
    private VBox memberVboxChatbox;

    public void vboxMemberLoaded(){
        memberVboxMember.getChildren().clear();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null){
            try{
                String query = "select box_name, displayname, is_direct from box_chats join box_chat_members on id = box_id join user_account_info on user_id = account_id where box_id = ? and user_id != ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()){
                    return;
                }
                if (rs.getBoolean("is_direct")){
                    memberLabelGroupname.setText(rs.getString("displayname"));
                } else {
                    memberLabelGroupname.setText(rs.getString("box_name"));
                }
                query = "select user_id, displayname, username from box_chat_members join user_account_info on user_id = account_id join user_accounts on user_id = id where box_id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                rs = ps.executeQuery();
                while(rs.next()){
                    int newuserid = rs.getInt(1);
                    String newdisplayname = rs.getString(2);
                    String newusername = rs.getString(3);
                    HBox newmembersection = new HBox();
                    newmembersection.setPadding(new Insets(0, 10, 0, 10));
                    newmembersection.setSpacing(10);
                    Label newmemberdisplayname = new Label(newdisplayname);
                    newmemberdisplayname.setFont(new Font("Courier New Bold", 24));
                    newmemberdisplayname.setMaxWidth(290);
                    Label newmemberusername = new Label(newusername);
                    newmemberusername.setFont(new Font("Courier New", 20));
                    newmemberusername.setMaxWidth(290);
                    MenuButton newmemberbutton = new MenuButton();
                    newmemberbutton.setFont(new Font("Courier New", 12));
                    if (newuserid != id){
                        String query2 = "select * from block_lists where (user_id = ? and block_id = ?) or (user_id = ? and block_id = ?)";
                        PreparedStatement ps2 = conn.prepareStatement(query2);
                        ps2.setInt(1, id);
                        ps2.setInt(2, newuserid);
                        ps2.setInt(3, newuserid);
                        ps2.setInt(4, id);
                        ResultSet rs2 = ps2.executeQuery();
                        if (!rs2.next()){
                            query2 = "select * from friendships where ((request_id = ? and accept_id = ?) or (request_id = ? and accept_id = ?))";
                            ps2 = conn.prepareStatement(query2);
                            ps2.setInt(1, id);
                            ps2.setInt(2, newuserid);
                            ps2.setInt(3, newuserid);
                            ps2.setInt(4, id);
                            rs2 = ps2.executeQuery();
                            if (rs2.next()){
                                if (rs2.getBoolean("is_accepted")){
                                    MenuItem newitemchat = new MenuItem("Chat");
                                    newitemchat.setId(String.valueOf(newuserid));
                                    newitemchat.setOnAction(this::menuitemChatClicked);
                                    newmemberbutton.getItems().add(newitemchat);
                                }
                            } else {
                                MenuItem newitemadd = new MenuItem("Add friend");
                                newitemadd.setId(String.valueOf(newuserid));
                                newitemadd.setOnAction(this::menuitemAddClicked);
                                newmemberbutton.getItems().add(newitemadd);
                            }
                            MenuItem newitemblock = new MenuItem("Block");
                            newitemblock.setId(String.valueOf(newuserid));
                            newitemblock.setOnAction(this::menuitemBlockClicked);
                            newmemberbutton.getItems().add(newitemblock);
                        }
                        MenuItem newitemreport = new MenuItem("Report");
                        newitemreport.setId(String.valueOf(newuserid));
                        newitemreport.setOnAction(this::menuitemReportClicked);
                        newmemberbutton.getItems().add(newitemreport);
                        query2 = "select is_admin from box_chat_members where box_id = ? and user_id = ?";
                        ps2 = conn.prepareStatement(query2);
                        ps2.setInt(1, boxid);
                        ps2.setInt(2, id);
                        rs2 = ps2.executeQuery();
                        rs2.next();
                        if (rs2.getBoolean(1)){
                            MenuItem newitemadmin = new MenuItem("Give admin");
                            newitemadmin.setId(String.valueOf(newuserid));
                            newitemadmin.setOnAction(this::menuitemAdminClicked);
                            MenuItem newitemkick = new MenuItem("Kick");
                            newitemkick.setId(String.valueOf(newuserid));
                            newitemkick.setOnAction(this::menuitemKickClicked);
                            newmemberbutton.getItems().addAll(newitemadmin, newitemkick);
                        }
                    } else {
                        MenuItem newitemreport = new MenuItem("Report");
                        newitemreport.setId(String.valueOf(newuserid));
                        newitemreport.setOnAction(this::menuitemReportClicked);
                        MenuItem newitemleave = new MenuItem("Leave");
                        newitemleave.setOnAction(this::menuitemLeaveClicked);
                        newmemberbutton.getItems().addAll(newitemreport, newitemleave);
                    }
                    newmembersection.getChildren().addAll(newmemberdisplayname, newmemberusername, newmemberbutton);
                    memberVboxMember.getChildren().add(newmembersection);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void menuitemChatClicked(ActionEvent event){
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
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                UserChatController controller = fxmlLoader.getController();
                controller.setdata(id, rs.getInt(1));
                stage = (Stage)display.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void menuitemAddClicked(ActionEvent event){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "insert into friendships (request_id, accept_id, is_accepted) values (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(((MenuItem)event.getSource()).getId()));
                ps.setBoolean(3, false);
                ps.executeUpdate();
                vboxMemberLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void menuitemBlockClicked(ActionEvent event){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                int newuserid = Integer.parseInt(((MenuItem)event.getSource()).getId());
                String query = "insert into block_lists (user_id, block_id) values (?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, newuserid);
                ps.executeUpdate();
                query = "delete from friendships where (request_id = ? and accept_id = ?) or (request_id = ? and accept_id = ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, newuserid);
                ps.setInt(3, newuserid);
                ps.setInt(4, id);
                ps.executeUpdate();
                query = "delete from box_chat_member where box_id = (select box_id from\n" +
                        "(select box_chat_members.box_id from box_chat_members where box_chat_members.user_id = ?\n" +
                        "intersect\n" +
                        "select box_chat_members.box_id from box_chat_members where box_chat_members.user_id = ?)\n" +
                        "where box_id in (select box_chats.id from box_chats where box_chats.is_direct))";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, newuserid);
                ps.executeUpdate();
                vboxMemberLoaded();
                vboxChatboxLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void menuitemReportClicked(ActionEvent event){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "insert into reports (reporter_id, reported_id) values (?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, Integer.parseInt(((MenuItem)event.getSource()).getId()));
                ps.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText("Reported sucessfully.");
                alert.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void menuitemAdminClicked(ActionEvent event){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "update box_chat_members set is_admin = false where user_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();
                query = "update box_chat_members set is_admin = true where user_id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(((MenuItem)event.getSource()).getId()));
                ps.executeUpdate();
                vboxMemberLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void menuitemKickClicked(ActionEvent event){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "delete from box_chat_members where box_id = ? and user_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, Integer.parseInt(((MenuItem)event.getSource()).getId()));
                ps.executeUpdate();
                vboxMemberLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void labelGroupnameClicked(MouseEvent event){
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Notification");
        td.setHeaderText("Enter group name.");
        td.setContentText("Max 32 characters.");
        td.showAndWait();
        String newGroupname = td.getResult();
        if (newGroupname != null && !newGroupname.isBlank() && newGroupname.length() <= 32){
            Connection conn = UtilityDAL.getConnection();
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
    public void menuChatClicked(Event event){
        if (boxid == 0){
            return;
        }
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            memberMenuChat.getItems().clear();
            MenuItem newitemmember = new MenuItem("Members");
            newitemmember.setOnAction(this::menuitemMembersClicked);
            MenuItem newitemsearch = new MenuItem("Search");
            newitemsearch.setOnAction(this::menuitemSearchClicked);
            MenuItem newitemleave = new MenuItem("Leave");
            newitemleave.setOnAction(this::menuitemLeaveClicked);
            memberMenuChat.getItems().addAll(newitemmember, newitemsearch, newitemleave);
        }
    }
    public void menuitemSearchClicked(ActionEvent event){}
    public void menuitemMembersClicked(ActionEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            UserChatController controller = fxmlLoader.getController();
            controller.setdata(id, boxid);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void menuitemLeaveClicked(ActionEvent event){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "delete from box_chat_members where box_id = ? and user_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ps.executeUpdate();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                UserChatController controller = fxmlLoader.getController();
                controller.setdata(id, 0);
                stage = (Stage)display.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
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
        memberMenuOnline.getItems().clear();
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
                    memberMenuOnline.getItems().add(newonlinefriend);
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
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-chat-view.fxml"));
                scene = new Scene(fxmlLoader.load(), 1080, 720);
                UserChatController controller = fxmlLoader.getController();
                controller.setdata(id, rs.getInt(1));
                stage = (Stage)display.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void imageCreategroupClicked(MouseEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-create-group-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            UserCreateGroupController controller = fxmlLoader.getController();
            controller.setdata(id);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void vboxChatboxLoaded(){
        memberVboxChatbox.getChildren().clear();
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
                    memberVboxChatbox.getChildren().add(newhboxbox);
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
            UserChatController controller = fxmlLoader.getController();
            controller.setdata(id, Integer.parseInt(((HBox) event.getSource()).getId()));
            stage = (Stage) display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void buttonAddmemberClicked(ActionEvent event){
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Notification");
        td.setHeaderText("Enter username.");
        td.showAndWait();
        String newusernameadd = td.getResult();
        if (newusernameadd != null && !newusernameadd.isBlank()){
            Connection conn = UtilityDAL.getConnection();
            if (conn != null) {
                try {
                    String query = "select * from friendships join user_accounts on request_id = id where accept_id = ? and username = ?\n" +
                            "union\n" +
                            "select * from friendships join user_accounts on accept_id = id where request_id = ? and username = ?";
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
                        alert.showAndWait();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        memberLabelGroupname.setText("");
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

    }

    public void setdata(int gid, int gboxid){
        id = gid;
        boxid = gboxid;
        vboxChatboxLoaded();
        vboxMemberLoaded();
    }
}
