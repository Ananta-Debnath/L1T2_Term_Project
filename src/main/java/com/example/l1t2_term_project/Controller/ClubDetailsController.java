package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.DTO.LoginDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ClubDetailsController {

    @FXML
    public ImageView clubLogo;

    @FXML
    public Label clubNameLabel;

    @FXML
    public Label managerLabel;
    @FXML
    public Label stadiumLabel;
    @FXML
    public Label budgetLabel;
    @FXML
    public TextArea historyText;
    @FXML
    public PasswordField newPasswordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public PasswordField currentPasswordField;

    private Client client;
    private Club currentClub;

    public void initializeValues(Client client, Club c){
        this.client = client;
        this.currentClub=c;
        updateClubDetails();
    }

    private void updateClubDetails(){

        System.out.println("Updating club details for: " + (currentClub != null ? currentClub.getName() : "null"));


        if(currentClub!=null){

            clubNameLabel.setText(currentClub.getName());
            managerLabel.setText(currentClub.getManagerName());
            stadiumLabel.setText(currentClub.getStadiumName());
            budgetLabel.setText(String.valueOf(currentClub.getBudget()));


            //TODO: history

            try{

                String imagePath="/Images/Clubs/" + currentClub.getName().toLowerCase().replace(" ","_") + ".jpeg";
                Image img=new Image(getClass().getResource(imagePath).toExternalForm());
                clubLogo.setImage(img);
            }catch(Exception e){
                System.out.println("club logo not found");
            }
        }
    }



    public void handlePasswordChange(ActionEvent actionEvent) {
        if (!newPasswordField.getText().equals(confirmPasswordField.getText()))
        {
            showAlert("Password mismatch", "New Password and Confirm Password does not match");
            return;
        }

        LoginDTO loginDTO = new LoginDTO(currentClub.getName(), currentPasswordField.getText(), LoginDTO.Type.SignIn);
        client.write(loginDTO);
        Object obj = client.read();
        if (obj instanceof Boolean)
        {
            boolean valid = (boolean) obj;
            if (!valid)
            {
                showAlert("Wrong Password", "The provided current password is wrong");
                return;
            }
        }
        else {
            System.err.println("Wrong object type - " + obj.getClass());
            return;
        }

        loginDTO = new LoginDTO(currentClub.getName(), newPasswordField.getText(), LoginDTO.Type.ChangePass);
        client.write(loginDTO);
        obj = client.read();
        if (obj instanceof Boolean)
        {
            boolean valid = (boolean) obj;
            if (valid)
            {
                showAlert("Password Changed", "New password set successfully");
            }
            else
            {
                showAlert("Failed!", "User not found");
            }
        }
        else {
            System.err.println("Wrong object type - " + obj.getClass());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
