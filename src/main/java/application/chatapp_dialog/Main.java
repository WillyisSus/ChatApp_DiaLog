package application.chatapp_dialog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user-login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setResizable(false);
        stage.setScene(scene);
        UserLoginController controller = fxmlLoader.getController();
        controller.setdata(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}