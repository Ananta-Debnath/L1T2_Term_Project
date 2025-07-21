package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.DTO.LoginDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class SignInController {
    private Client client;
    
    @FXML
    public TextField clubNameTextField;
    

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public Button signInButton;

    @FXML
    public void switchToClub(ActionEvent actionEvent) {
        LoginDTO loginDTO = new LoginDTO(clubNameTextField.getText(), passwordTextField.getText(), true);
        client.write(loginDTO);

        Object obj = client.read();
        if (obj instanceof Boolean)
        {
            boolean valid = (boolean) obj;
            if (valid)
            {
                client.setCurrentClub(loginDTO.getUsername());
                // Get all nations
                obj = client.read();
                if (obj instanceof List<?> && ((List<?>) obj).get(0) instanceof String)
                {
                    @SuppressWarnings("unchecked")
                    List<String> list = (List<String>) obj;
                    client.setNationList(list);
                }
                else System.err.println("Wrong object type - " + obj.getClass());

                // Get All clubs
                obj = client.read();
                if (obj instanceof List<?> && ((List<?>) obj).get(0) instanceof String)
                {
                    @SuppressWarnings("unchecked")
                    List<String> list = (List<String>) obj;
                    client.setClubList(list);
                }
                else System.err.println("Wrong object type - " + obj.getClass());

                // Change scene
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/Club.fxml"));
                    Parent clubView = loader.load();
                    ((ClubController) loader.getController()).initializeValues(client);
                    signInButton.getScene().setRoot(clubView);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                // TODO: show alert
            }
        }
        else {
            System.err.println("Wrong object type - " + obj.getClass());
        }
    }


    public void setClient(Client client)
    {
        this.client = client;
    }
}
