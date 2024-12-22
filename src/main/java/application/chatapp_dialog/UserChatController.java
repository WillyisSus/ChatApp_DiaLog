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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserChatController implements Initializable, Runnable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    static int id = 1;
    static int boxid = 0;
    static boolean stop  = false;
//    static List<Integer> chatBoxchatList = new ArrayList<>();
//    static List<Integer> chatBoxchatList2 = new ArrayList<>();
//    static List<Integer> chatMessagesList = new ArrayList<>();
//    static List<Integer> chatMessagesList2 = new ArrayList<>();
    @FXML
    private TextField chatTextSend;
    static TextField chatTextSend2 = new TextField();
    @FXML
    private ImageView chatImageSend;
    static ImageView chatImageSend2 = new ImageView();
    @FXML
    private VBox chatVboxChat;
    static VBox chatVboxChat2 = new VBox();
    @FXML
    private Label chatLabelChatname;
    static Label chatLabelChatname2 = new Label();
    @FXML
    private MenuButton chatMenuChat;
    static MenuButton chatMenuChat2 = new MenuButton();
    @FXML
    private MenuButton chatMenuAccount;
    static MenuButton chatMenuAccount2 = new MenuButton();
    @FXML
    private MenuItem chatMenuitemAccount;
    static MenuItem chatMenuitemAccount2 = new MenuItem();
    @FXML
    private MenuItem chatMenuitemFriends;
    static MenuItem chatMenuitemFriends2 = new MenuItem();
    @FXML
    private MenuItem chatMenuitemLogout;
    static MenuItem chatMenuitemLogout2 = new MenuItem();
    @FXML
    private MenuButton chatMenuOnline;
    static MenuButton chatMenuOnline2 = new MenuButton();
    @FXML
    private ImageView chatImageCreategroup;
    static ImageView chatImageCreategroup2 = new ImageView();
    @FXML
    private VBox chatVboxChatbox;
    static VBox chatVboxChatbox2 = new VBox();
    @FXML
    private ScrollPane chatScrollChat;
    static ScrollPane chatScrollChat2 = new ScrollPane();
    @FXML
    private ScrollPane chatScrollBoxchat;
    static ScrollPane chatScrollBoxchat2 = new ScrollPane();
    @FXML
    private TextField chatTextSearchchat;
    static TextField chatTextSearchchat2 = new TextField();
    static String searchValue = "";
    static int changable = 0;
    @FXML
    private TextField chatTextSearchbox;
    static TextField chatTextSearchbox2 = new TextField();

    Connection conn = UtilityDAL.getConnection();
    @FXML
    synchronized public void textSendEntered(ActionEvent event){
        String message = chatTextSend2.getText();
        if (message == null || message.isBlank()){
            return;
        }
        if (conn != null) {
            try {
                String query = "insert into messages (user_id, box_id, content) values (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, boxid);
                ps.setString(3, message);
                ps.executeUpdate();
                vboxChatboxLoaded();
                vboxChatLoaded();
                chatScrollChat2.applyCss();
                chatScrollChat2.layout();
                chatScrollChat2.setVvalue(1.0);
                chatTextSend2.setText("");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void imageSendClicked(MouseEvent event){
        String message = chatTextSend2.getText();
        if (message == null || message.isBlank()){
            return;
        }
        if (conn != null) {
            try {
                String query = "insert into messages (user_id, box_id, content) values (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, boxid);
                ps.setString(3, message);
                ps.executeUpdate();
                vboxChatboxLoaded();
                vboxChatLoaded();
                chatScrollChat2.applyCss();
                chatScrollChat2.layout();
                chatScrollChat2.setVvalue(1.0);
                chatTextSend2.setText("");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void vboxChatLoaded(){
        chatVboxChat2.getChildren().clear();
        if (boxid == 0){
            chatTextSend2.setText("");
            chatLabelChatname2.setText("");
            chatTextSend2.setVisible(false);
            chatImageSend2.setVisible(false);
        } else {
            chatTextSend2.setVisible(true);
            chatImageSend2.setVisible(true);
        }
        if (conn != null){
            try{
                String query = "select box_name, displayname, is_direct from box_chats join box_chat_members on id = box_id join user_account_info on user_id = account_id where id = ? and user_id != ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()){
                    return;
                }
                if (rs.getBoolean("is_direct")){
                    chatLabelChatname2.setText(rs.getString("displayname"));
                } else {
                    chatLabelChatname2.setText(rs.getString("box_name"));
                }
                query = "select id, account_id, displayname, content, create_date from messages join user_account_info on user_id = account_id where box_id = ? order by create_date, id";
                ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                rs = ps.executeQuery();
                while(rs.next()){
                    int newchatid = rs.getInt(1);
                    int newchatuserid = rs.getInt(2);
                    String newchatname = rs.getString(3);
                    String newchatmess = rs.getString(4);
                    String newchattime = rs.getTimestamp(5).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                    HBox newchatsection = new HBox();
                    newchatsection.setId(String.valueOf(newchatid));
                    newchatsection.setPadding(new Insets(0, 10, 0, 10));
                    newchatsection.setSpacing(10);
                    newchatsection.setPrefWidth(710);
                    VBox newchattextbox = new VBox();
                    HBox newchattitlebox = new HBox();
                    newchattitlebox.setAlignment(Pos.CENTER_LEFT);
                    newchattitlebox.setSpacing(10);
                    Label newchattitlename = new Label(newchatname);
                    newchattitlename.setFont(new Font("Courier New Bold", 24));
                    newchattitlename.setMinHeight(40);
                    Label newchattitletime = new Label(newchattime);
                    newchattitletime.setFont(new Font("Courier New", 16));
                    Label newchatcontent = new Label(newchatmess);
                    newchatcontent.setFont(new Font("Courier New", 20));
                    newchatcontent.setMinHeight(Region.USE_PREF_SIZE);
                    if (newchatuserid == id){
                        Label newchatdelete = new Label("Delete");
                        newchatdelete.setId(String.valueOf(newchatid));
                        newchatdelete.setFont(new Font("Courier New", 12));
                        newchatdelete.setTextFill(Color.BLUE);
                        newchatdelete.setOnMouseClicked(this::deleteMessage);
                        newchattitlebox.setAlignment(Pos.CENTER_RIGHT);
                        newchattextbox.setAlignment(Pos.TOP_RIGHT);
                        newchatsection.setAlignment(Pos.TOP_RIGHT);
                        newchattitlebox.getChildren().addAll(newchatdelete, newchattitletime, newchattitlename);
                        newchattextbox.getChildren().addAll(newchattitlebox, newchatcontent);
                        newchatsection.getChildren().add(newchattextbox);
                        chatVboxChat2.getChildren().add(newchatsection);
                    } else {
                        newchattitlebox.getChildren().addAll(newchattitlename, newchattitletime);
                        newchattextbox.getChildren().addAll(newchattitlebox, newchatcontent);
                        newchatsection.getChildren().addAll(newchattextbox);
                        chatVboxChat2.getChildren().add(newchatsection);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void deleteMessage(MouseEvent event){
        if (conn != null) {
            try {
                String query = "delete from messages where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(((Label)event.getSource()).getId()));
                ps.executeUpdate();
                vboxChatboxLoaded();
                vboxChatLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void labelChatnameClicked(MouseEvent event){
        if (boxid == 0){
            return;
        }
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
                    vboxChatLoaded();
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
            try {
                chatMenuChat2.getItems().clear();
                String query = "select is_direct from box_chats where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getBoolean(1)){
                    MenuItem newitemfriend = new MenuItem("Unfriend");
                    newitemfriend.setOnAction(this::menuitemFriendClicked);
                    MenuItem newitemreport = new MenuItem("Report");
                    newitemreport.setOnAction(this::menuitemReportClicked);
                    MenuItem newitemblock = new MenuItem("Block");
                    newitemblock.setOnAction(this::menuitemBlockClicked);
                    chatMenuChat2.getItems().addAll(newitemfriend, newitemreport, newitemblock);
                } else {
                    MenuItem newitemmember = new MenuItem("Members");
                    newitemmember.setOnAction(this::menuitemMembersClicked);
                    MenuItem newitemleave = new MenuItem("Leave");
                    newitemleave.setOnAction(this::menuitemLeaveClicked);
                    chatMenuChat2.getItems().addAll(newitemmember, newitemleave);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void menuitemFriendClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "delete from friendships where (request_id = ? and accept_id = (select user_id from box_chat_members where box_id = ? and user_id != ?))\n" +
                        "or (request_id = (select user_id from box_chat_members where box_id = ? and user_id != ?) and accept_id = ?)\n";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, boxid);
                ps.setInt(3, id);
                ps.setInt(4, boxid);
                ps.setInt(5, id);
                ps.setInt(6, id);
                ps.executeUpdate();
                query = "delete from box_chat_members where box_id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.executeUpdate();
                boxid = 0;
                vboxChatboxLoaded();
                vboxChatLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void menuitemReportClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "insert into reports (reporter_id, reported_id) values (?, (select user_id from box_chat_members where box_id = ? and user_id != ?))";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, boxid);
                ps.setInt(3, id);
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
    synchronized public void menuitemBlockClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "insert into block_lists (user_id, block_id) values (?, (select user_id from box_chat_members where box_id = ? and user_id != ?))";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, boxid);
                ps.setInt(3, id);
                ps.executeUpdate();
                menuitemFriendClicked(event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void menuitemMembersClicked(ActionEvent event){
        try{
            stop = true;
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-group-member-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            UserGroupMemberController controller = fxmlLoader.getController();
            controller.setdata(id, boxid);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    synchronized public void menuitemLeaveClicked(ActionEvent event){
        if (conn != null) {
            try {
                String query = "delete from box_chat_members where box_id = ? and user_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ps.executeUpdate();
                boxid = 0;
                vboxChatboxLoaded();
                vboxChatLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void menuitemAccountClicked(ActionEvent event){
        try{
            stop = true;
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
            stop = true;
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
            stop = true;
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
        chatMenuOnline2.getItems().clear();
        if (conn != null) {
            try {
                String query = "select id, username, displayname from user_account_info join user_accounts on account_id = id join friendships on ((id = request_id and accept_id = ?) or (request_id = ? and id = accept_id)) where is_accepted = true and status = 'online'";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    MenuItem newonlinefriend = new MenuItem(rs.getString("displayname") + " @" + rs.getString("username"));
                    newonlinefriend.setId(String.valueOf(rs.getInt("id")));
                    newonlinefriend.setOnAction(this::menuitemOnlineClicked);
                    chatMenuOnline2.getItems().add(newonlinefriend);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void menuitemOnlineClicked(ActionEvent event){
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
                boxid = rs.getInt(1);
                vboxChatLoaded();
                chatScrollChat2.applyCss();
                chatScrollChat2.layout();
                chatScrollChat2.setVvalue(1.0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void imageCreategroupClicked(MouseEvent event){
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
        chatVboxChatbox2.getChildren().clear();
        if (conn != null) {
            try {
                String query = "select * from box_chat_members where box_id = ? and user_id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()){
                    boxid = 0;
                }
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
                        "union  " +
                        "select * from directbox;";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.setInt(2, id);
                ps.setInt(3, id);
                rs = ps.executeQuery();
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
                    chatVboxChatbox2.getChildren().add(newhboxbox);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    synchronized public void hboxBoxClicked(MouseEvent event){
        boxid = Integer.parseInt(((HBox)event.getSource()).getId());
        vboxChatLoaded();
        chatScrollChat2.applyCss();
        chatScrollChat2.layout();
        chatScrollChat2.setVvalue(1.0);
    }
    synchronized public void textSearchchatEntered(ActionEvent event){
        String newsearchtext = chatTextSearchchat2.getText();
        if (newsearchtext == null || newsearchtext.isBlank() || boxid == 0){
            return;
        }
        if (conn != null) {
            try {
                String query = "select * from messages where box_id = ? and content like ? order by create_date desc";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setString(2, "%" + newsearchtext + "%");
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    if (changable == 0){
                        chatScrollChat2.applyCss();
                        chatScrollChat2.layout();
                        chatScrollChat2.setVvalue((double) chatVboxChat2.getChildren().indexOf(chatVboxChat.lookup("#" + rs.getInt("id"))) / chatVboxChat.getChildren().size());
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stop = false;
        chatTextSend2 = chatTextSend;
        chatImageSend2 = chatImageSend;
        chatVboxChat2 = chatVboxChat;
        chatLabelChatname2 = chatLabelChatname;
        chatMenuChat2 = chatMenuChat;
        chatMenuitemAccount2 = chatMenuitemAccount;
        chatMenuitemFriends2 = chatMenuitemFriends;
        chatMenuitemLogout2 = chatMenuitemLogout;
        chatMenuOnline2 = chatMenuOnline;
        chatImageCreategroup2 = chatImageCreategroup;
        chatVboxChatbox2 = chatVboxChatbox;
        chatScrollChat2 = chatScrollChat;
        chatScrollBoxchat2 = chatScrollBoxchat;
        chatTextSearchchat2 = chatTextSearchchat;
        chatTextSearchbox2 = chatTextSearchbox;
        chatLabelChatname.setText("");
        chatTextSend.setText("");
        chatTextSearchchat.setText("");
        chatTextSearchbox.setText("");
        chatTextSend.setOnAction(this::textSendEntered);
        chatImageSend.setOnMouseClicked(this::imageSendClicked);
        chatLabelChatname.setOnMouseClicked(this::labelChatnameClicked);
        chatMenuChat.setOnShowing(this::menuChatClicked);
        chatMenuitemAccount.setOnAction(this::menuitemAccountClicked);
        chatMenuitemFriends.setOnAction(this::menuitemFriendsClicked);
        chatMenuitemLogout.setOnAction(this::menuitemLogoutClicked);
        chatMenuOnline.setOnShowing(this::menuOnlineClicked);
        chatImageCreategroup.setOnMouseClicked(this::imageCreategroupClicked);
        chatTextSearchchat.setOnAction(this::textSearchchatEntered);
        vboxChatboxLoaded();
        vboxChatLoaded();
        Runnable r1 = new UserChatController();
        Thread t1 = new Thread(r1, "UserChatController");
        t1.setDaemon(true);
        t1.start();
    }

    public void setdata(int gid, int gboxid, Stage gstage){
        id = gid;
        boxid = gboxid;
        stage = gstage;
        stage.setOnCloseRequest(event -> logout(stage));
        vboxChatboxLoaded();
        vboxChatLoaded();
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
                    vboxChatboxLoaded();
                    vboxChatLoaded();
                }
            });
            if (!Objects.equals(chatTextSearchchat2.getText(), searchValue)){
                searchValue = chatTextSearchchat2.getText();
                changable = 0;
            }
        }
    }

    public void logout(Stage st){
        if (conn != null) {
            try {
                String query = "update user_accounts set status = 'offline' where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                ps.executeUpdate();;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
