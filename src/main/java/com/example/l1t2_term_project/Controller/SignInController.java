package com.example.l1t2_term_project.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignInController {
    
    
    @FXML
    public TextField clubNameTextField;
    

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public Button signInButton;


    public void switchToClub(ActionEvent actionEvent) {

        try{

            Parent clubView=  FXMLLoader.load(getClass().getResource("/com/example/l1t2_term_project/Club.fxml"));
            signInButton.getScene().setRoot(clubView);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
