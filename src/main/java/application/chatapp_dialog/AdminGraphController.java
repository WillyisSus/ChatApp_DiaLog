package application.chatapp_dialog;
import application.chatapp_dialog.dal.AdminActivityLogDAL;
import application.chatapp_dialog.dal.AdminNewUserAccountDAL;
import application.chatapp_dialog.dto.AdminNewUserAccount;
import application.chatapp_dialog.dto.AdminUserActivityLog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.net.URL;
import java.nio.channels.NotYetBoundException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class AdminGraphController implements Initializable {
//    Chart properties
    @FXML
    private LineChart<String , Integer> newUsersChart;
    @FXML
    private LineChart<String, Integer> activeUsersChart;
    @FXML
    private CategoryAxis newUsersX;
    @FXML
    private NumberAxis newUsersY;
    @FXML
    private CategoryAxis activeUsersX;
    @FXML
    private NumberAxis activeUsersY;

    @FXML
    private MenuButton yearButton;
    ObservableList<String> monthList = FXCollections.observableArrayList("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10", "M11", "M12");
    XYChart.Series<String, Integer> activeUserSeries;
    XYChart.Series<String, Integer> newUserSeries;
    private List<Integer> newUsersInMonth;
    private List<Integer> activeUsersInMonth;
    //
    private Scene scene;
    private Stage stage;
    private Parent root;
    public void switchToUser(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-user-listing-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void switchToActiveUser(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-activeuser-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void switchToGraph(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-graph-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void switchToGroup(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-group-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void switchToReport(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-report-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void switchToNewUser(ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("admin-newuser-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void loadDataOfYear(int year){
        activeUserSeries.getData().clear();
        newUserSeries.getData().clear();
        newUsersInMonth = new ArrayList<>(List.of(0,0,0,0,0,0,0,0,0,0,0,0));
        activeUsersInMonth = new ArrayList<>(List.of(0,0,0,0,0,0,0,0,0,0,0,0));
        for(int i = 0; i < monthList.size(); i++){
            activeUserSeries.getData().add(new XYChart.Data<>(monthList.get(i), activeUsersInMonth.get(i)));
        }

        for(int i = 0; i < monthList.size(); i++){
            newUserSeries.getData().add(new XYChart.Data<>(monthList.get(i), newUsersInMonth.get(i)));
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                try {
                    List<AdminNewUserAccount> newUserList = AdminNewUserAccountDAL.getNewAccountInYear(year);
                    newUserList.forEach(data -> {
                        int monthValue = data.getCreateDate().toLocalDateTime().getMonthValue();
                        int newValue = newUsersInMonth.get(monthValue - 1) + 1;
                        newUsersInMonth.set(monthValue - 1, newValue);
                    });
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    List<AdminUserActivityLog> newUserList = AdminActivityLogDAL.getActiveUserInYear(year);
                    newUserList.forEach(data -> {
                        int monthValue = data.getSessionStartAsTimeStamp().toLocalDateTime().getMonthValue();
                        int newValue = activeUsersInMonth.get(monthValue - 1) + 1;
                        activeUsersInMonth.set(monthValue - 1, newValue);
                    });
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                for(int i = 0; i < activeUsersInMonth.size(); i++){
                    activeUserSeries.getData().get(i).setYValue(activeUsersInMonth.get(i));
                }

                for(int i = 0; i < newUsersInMonth.size(); i++){
                    newUserSeries.getData().get(i).setYValue(newUsersInMonth.get(i));

                }

            }
        });
    }
    public void fetchYearData(ActionEvent event){
        MenuItem clicked = (MenuItem) event.getSource();
        yearButton.setText( clicked.getText());
        int year = Integer.parseInt(clicked.getText());
        System.out.println(year);
        loadDataOfYear(year);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newUsersInMonth = new ArrayList<>(List.of(0,0,0,0,0,0,0,0,0,0,0,0));
        activeUsersInMonth = new ArrayList<>(List.of(0,0,0,0,0,0,0,0,0,0,0,0));
        activeUsersX = new CategoryAxis(monthList);
        activeUsersY = new NumberAxis();
        activeUserSeries = new XYChart.Series<>();
        newUserSeries = new XYChart.Series<>();
        activeUserSeries.setName("Active Users");
        newUserSeries.setName("New Users");
        activeUsersChart.getData().add(activeUserSeries);
        newUsersChart.getData().add(newUserSeries);
        newUsersX = new CategoryAxis(monthList);
        newUsersY = new NumberAxis();
        try {
            List<Integer> years = AdminNewUserAccountDAL.getDifferentYearWithNewAccount();
            years.forEach(year -> {
                MenuItem newItem = new MenuItem(year.toString());
                newItem.setOnAction(this::fetchYearData);
                yearButton.getItems().addLast(newItem);

            });

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        loadDataOfYear(LocalDate.now().getYear());

    }
}
