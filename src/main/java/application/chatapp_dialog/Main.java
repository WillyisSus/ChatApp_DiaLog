package application.chatapp_dialog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user-friends-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setResizable(false);
        stage.setScene(scene);
        UserFriendController controller = fxmlLoader.getController();
        controller.setdata(1, stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}