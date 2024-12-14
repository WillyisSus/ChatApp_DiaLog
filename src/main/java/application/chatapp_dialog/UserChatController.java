package application.chatapp_dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class UserChatController implements Initializable {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private GridPane display;

    @FXML
    private TextField chatTextSend;

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
            writer.write(s + "\n");
            writer.flush();

            String s2 = reader.readLine();
            System.out.println("Server message: " + s2);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chatTextSend.setOnAction(this::textSendEntered);
    }
}
