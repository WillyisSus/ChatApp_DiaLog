package application.chatapp_dialog;

import application.chatapp_dialog.dal.UtilityDAL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.awt.*;
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

    @FXML
    private TextField createTextGroupname;
    @FXML
    private TextField createTextUsername;
    @FXML
    private ImageView createImageAdduser;
    @FXML
    private HBox createHboxUser;
    @FXML
    private TextField createTextSend;
    @FXML
    private ImageView createImageSend;

    int currentUserId = 1;
    List<Integer> newUserList = new ArrayList<>();

    @FXML
    public void textUsernameEntered(ActionEvent event){
        String name = createTextUsername.getText();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "select id from user_accounts where username = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                rs.next();
                int id = rs.getInt(1);
                System.out.println(id);
                if(id != 0){
                    VBox newUser = new VBox();
                    newUser.setAlignment(Pos.CENTER_LEFT);
                    newUser.setPadding(new Insets(0, 10, 0, 10));
                    newUser.setSpacing(5);
                    newUser.setMinWidth(Region.USE_PREF_SIZE);
                    newUser.setPrefHeight(60);
                    Label newUsername = new Label(name);
                    newUsername.setFont(new javafx.scene.text.Font("Courier New Bold", 16));
                    Label newRemove = new Label("Remove");
                    newRemove.setFont(new javafx.scene.text.Font("Courier New", 12));
                    newRemove.setTextFill(Color.RED);
                    newRemove.setOnMouseClicked(this::textRemoveClicked);
                    newUser.getChildren().addAll(newUsername, newRemove);
                    createHboxUser.getChildren().add(newUser);
                    newUserList.add(id);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        createTextUsername.setText("");
    }
    @FXML
    public void imageAdduserClicked(MouseEvent event){
        String name = createTextUsername.getText();
        Connection conn = UtilityDAL.getConnection();
        if (conn != null) {
            try {
                String query = "select id from user_accounts where username = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                rs.next();
                int id = rs.getInt(1);
                System.out.println(id);
                if(id != 0){
                    VBox newUser = new VBox();
                    newUser.setAlignment(Pos.CENTER_LEFT);
                    newUser.setPadding(new Insets(0, 10, 0, 10));
                    newUser.setSpacing(5);
                    newUser.setMinWidth(Region.USE_PREF_SIZE);
                    newUser.setPrefHeight(60);
                    Label newUsername = new Label(name);
                    newUsername.setFont(new javafx.scene.text.Font("Courier New Bold", 16));
                    Label newRemove = new Label("Remove");
                    newRemove.setFont(new javafx.scene.text.Font("Courier New", 12));
                    newRemove.setTextFill(Color.RED);
                    newRemove.setOnMouseClicked(this::textRemoveClicked);
                    newUser.getChildren().addAll(newUsername, newRemove);
                    createHboxUser.getChildren().add(newUser);
                    newUserList.add(id);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        createTextUsername.setText("");
    }
    @FXML
    public void textRemoveClicked(MouseEvent event){
        createHboxUser.getChildren().remove(((Label)event.getSource()).getParent());
    }
    @FXML
    public void imageSendClicked(MouseEvent event){
        String groupname = createTextGroupname.getText();
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
                for(Integer id : newUserList){
                    query = "insert into box_chat_members (box_id, user_id) " +
                            "values (?, ?)";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, row);
                    ps.setInt(2, id);
                    row = ps.executeUpdate();
                    System.out.println(ps.toString());
                    System.out.println(row + " added");
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
        newUserList.add(currentUserId);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createHboxUser.getChildren().clear();
        firstPersonShooter();
        createTextUsername.setOnAction(this::textUsernameEntered);
        createImageAdduser.setOnMouseClicked(this::imageAdduserClicked);
        createImageSend.setOnMouseClicked(this::imageSendClicked);
    }
}
