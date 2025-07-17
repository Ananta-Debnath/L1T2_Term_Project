package com.example.l1t2_term_project;

import com.example.l1t2_term_project.Main.Main;
import com.example.l1t2_term_project.Utils.SocketWrapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Client extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("SignIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Connect to Server
        SocketWrapper socketWrapper = new SocketWrapper("127.0.0.1", 12913);
        socketWrapper.write("Unnamed");
    }

    public static void main(String[] args) {
        Main.main(args);
        launch();
    }
}