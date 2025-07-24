package com.example.l1t2_term_project;

import com.example.l1t2_term_project.Controller.SignInController;
import com.example.l1t2_term_project.Main.Main;
import com.example.l1t2_term_project.Utils.ActivityLogger;
import com.example.l1t2_term_project.Utils.SocketWrapper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Client extends Application {
    private SocketWrapper socketWrapper;
    private List<String> nationList;
    private List<String> clubList;

    public List<String> getNationList() {
        return nationList;
    }

    public void setNationList(List<String> nationList) {
        this.nationList = nationList;
    }

    public List<String> getClubList() {
        return clubList;
    }

    public void setClubList(List<String> clubList) {
        this.clubList = clubList;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("SignIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((SignInController) fxmlLoader.getController()).setClient(this);
        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Connect to Server
        socketWrapper = new SocketWrapper("127.0.0.1", 12913);
    }

    public static void main(String[] args) {
       // Main.main(args);
        launch();
    }

    public Object read()
    {
        try{
            return socketWrapper.read();
        } catch (IOException | ClassNotFoundException e) {
            ActivityLogger.log("Server - Client connection disrupted");
            return null;
        }
    }

    public void write(Object obj)
    {
        try{
            socketWrapper.write(obj);
        } catch (IOException e) {
            ActivityLogger.log("Server - Client connection disrupted");
        }
    }
}