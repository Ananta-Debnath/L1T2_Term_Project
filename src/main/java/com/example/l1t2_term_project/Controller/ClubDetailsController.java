package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Model.Club.Club;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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

    private Club currentClub;

    public void setClub(Club c){
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
    }
}
