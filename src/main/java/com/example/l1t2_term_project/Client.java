package com.example.l1t2_term_project;

import com.example.l1t2_term_project.Controller.SignInController;
import com.example.l1t2_term_project.DTO.LoginDTO;
import com.example.l1t2_term_project.Utils.ClientReadThread;
import com.example.l1t2_term_project.Utils.SocketWrapper;
import com.example.l1t2_term_project.Utils.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Client extends Application {
    private ClientReadThread readThread;
    private SocketWrapper socketWrapper;
    private String currentClub;
    private List<String> nationList;
    private List<String> clubList;

    public String getCurrentClub() {
        return currentClub;
    }

    public void setCurrentClub(String currentClub) {
        this.currentClub = currentClub;
    }

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

        // Confirmation on close
        stage.setOnCloseRequest(event -> {
            boolean exit = Utils.showConfirmationAlert("Exit", "Confirm Exit", "Do you really want to exit?");
            if (!exit)
            {
                event.consume();
            }
            else if (currentClub != null)
            {
                write(new LoginDTO(currentClub, null, LoginDTO.Type.SignOut)); // Sign-Out
            }
            try {
                socketWrapper.closeConnection();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        stage.show();



        // Connect to Server
        socketWrapper = new SocketWrapper("127.0.0.1", 12913);
        readThread = new ClientReadThread("Client Read Thread", socketWrapper);
        readThread.start();

        // Get all nations
        Object obj = read();
        if (obj instanceof List<?> && ((List<?>) obj).get(0) instanceof String)
        {
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) obj;
            setNationList(list);
        }
        else System.err.println("Wrong object type - " + obj.getClass());

        // Get All clubs
        obj = read();
        if (obj instanceof List<?> && ((List<?>) obj).get(0) instanceof String)
        {
            @SuppressWarnings("unchecked")
            List<String> list = (List<String>) obj;
            setClubList(list);
        }
        else System.err.println("Wrong object type - " + obj.getClass());
    }

    public static void main(String[] args) {
       // Main.main(args);
        launch();
    }

    public Object read()
    {
        return readThread.read();
    }

    public void write(Object obj)
    {
        try{
            socketWrapper.write(obj);
        } catch (IOException e) {
            System.err.println("Client write error");
        }
    }
}