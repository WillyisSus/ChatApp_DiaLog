package application.chatapp_dialog;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import application.chatapp_dialog.dal.AdminAccountDAL;
import java.io.IOException;
import java.util.Scanner;

public class AdminMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}