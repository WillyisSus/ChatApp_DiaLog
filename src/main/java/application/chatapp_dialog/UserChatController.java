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
import kotlin.collections.ArrayDeque;

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

public class UserChatController implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    int id = 1;
    int boxid = 1;
    @FXML
    private TextField chatTextSend;
    @FXML
    private ImageView chatImageSend;
    @FXML
    private VBox chatVboxChat;
    @FXML
    private ImageView chatImageChat;
    @FXML
    private Label chatLabelChatname;
    @FXML
    private MenuButton chatMenuChat;
    @FXML
    private MenuButton chatMenuAccount;
    @FXML
    private MenuItem chatMenuitemAccount;
    @FXML
    private MenuItem chatMenuitemFriends;
    @FXML
    private MenuItem chatMenuitemLogout;
    @FXML
    private MenuButton chatMenuOnline;
    @FXML
    private ImageView chatImageCreategroup;
    @FXML
    private VBox chatVboxChatbox;

    @FXML
    public void textSendEntered(ActionEvent event){
        String serverIP = "localhost";
        int serverPort = 1234;
        try (Socket socket = new Socket(serverIP, serverPort)) {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

            String s = chatTextSend.getText();
            System.out.println(s);
            writer.write(s + " ");
            writer.flush();

            String s2 = reader.readLine();
            System.out.println("Server message: " + s2);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void imageSendClicked(MouseEvent event){}
    public void vboxChatLoaded(){
        chatVboxChat.getChildren().clear();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null){
            try{
                String query = "select box_name, displayname, is_direct from box_chats join box_chat_members on id = box_id join user_account_info on user_id = account_id where id = ? and user_id != ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                ps.setInt(2, id);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getBoolean("is_direct")){
                    chatLabelChatname.setText(rs.getString("displayname"));
                } else {
                    chatLabelChatname.setText(rs.getString("box_name"));
                }
                query = "select id, account_id, displayname, content, create_date from messages join user_account_info on user_id = account_id where box_id = ? and visible_to_owner = true order by create_date, id";
                ps = conn.prepareStatement(query);
                ps.setInt(1, boxid);
                rs = ps.executeQuery();
                while(rs.next()){
                    int newchatid = rs.getInt(1);
                    int newchatuserid = rs.getInt(2);
                    String newchatname = rs.getString(3);
                    String newchatmess = rs.getString(4);
                    String newchattime = LocalDateTime.parse(rs.getString(5), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")).format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
                    HBox newchatsection = new HBox();
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
                        MenuButton newchatbutton = new MenuButton();
                        MenuItem newchatitem = new MenuItem("Delete");
                        newchatitem.setId(String.valueOf(newchatid));
                        newchatitem.setOnAction(this::deleteMessage);
                        newchatbutton.getItems().add(newchatitem);
                        newchattitlebox.setAlignment(Pos.CENTER_RIGHT);
                        newchattextbox.setAlignment(Pos.TOP_RIGHT);
                        newchatsection.setAlignment(Pos.TOP_RIGHT);
                        newchattitlebox.getChildren().addAll(newchattitletime, newchattitlename);
                        newchattextbox.getChildren().addAll(newchattitlebox, newchatcontent);
                        newchatsection.getChildren().addAll(newchatbutton, newchattextbox);
                        chatVboxChat.getChildren().add(newchatsection);
                    } else {
                        newchattitlebox.getChildren().addAll(newchattitlename, newchattitletime);
                        newchattextbox.getChildren().addAll(newchattitlebox, newchatcontent);
                        newchatsection.getChildren().addAll(newchattextbox);
                        chatVboxChat.getChildren().add(newchatsection);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void deleteMessage(ActionEvent event){
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "update messages set visible_to_owner = false where id = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(((MenuItem)event.getSource()).getId()));
                ps.executeUpdate();
                vboxChatLoaded();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void labelChatnameClicked(MouseEvent event){
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Notification");
        td.setHeaderText("Enter group name");
        td.showAndWait();
        String newGroupname = td.getResult();
        if (newGroupname != null && !newGroupname.isBlank()){
            Connection conn = UtilityDAL.getConnection();
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
    public void menuChatClicked(Event event){}
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
    public void menuOnlineClicked(Event event){}
    public void imageCreategroupClicked(MouseEvent event){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("user-create-group-view.fxml"));
            scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage = (Stage)display.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void vboxChatboxLoaded(){
        chatVboxChatbox.getChildren().clear();
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
                    int boxid = rs.getInt(1);
                    String boxname = rs.getString(2);
                    String boxmess = rs.getString(3);
                    HBox newhboxbox = new HBox();
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
                    chatVboxChatbox.getChildren().add(newhboxbox);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatTextSend.setText("");
        chatTextSend.setOnAction(this::textSendEntered);
        chatImageSend.setOnMouseClicked(this::imageSendClicked);
        chatLabelChatname.setOnMouseClicked(this::labelChatnameClicked);
        chatMenuChat.setOnShowing(this::menuChatClicked);
        chatMenuitemAccount.setOnAction(this::menuitemAccountClicked);
        chatMenuitemFriends.setOnAction(this::menuitemFriendsClicked);
        chatMenuitemLogout.setOnAction(this::menuitemLogoutClicked);
        chatMenuOnline.setOnShowing(this::menuOnlineClicked);
        chatImageCreategroup.setOnMouseClicked(this::imageCreategroupClicked);
        vboxChatLoaded();
        vboxChatboxLoaded();
    }

    public void setdata(int gid){
        id = gid;
        vboxChatLoaded();
        vboxChatboxLoaded();
    }
}
