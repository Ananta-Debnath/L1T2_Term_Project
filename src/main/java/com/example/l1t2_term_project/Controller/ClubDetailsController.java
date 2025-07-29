package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.DTO.LoginDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

                String imagePath="/Images/Clubs/" + currentClub.getName().toLowerCase().replace(" ","_") + ".png";
                Image img=new Image(getClass().getResource(imagePath).toExternalForm());
                clubLogo.setImage(img);
            }catch(Exception e){
                System.out.println("club logo not found");
            }
        }
    }



    public void handlePasswordChange(ActionEvent actionEvent) {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();

        if (newPassword.isEmpty() || !newPassword.equals(confirmPassword))
        {
            Utils.showAlert("Failed", "Password change unsuccessful");
            return;
        }

        LoginDTO loginDTO = new LoginDTO(currentClub.getName(), currentPassword, LoginDTO.Type.SignIn);
        client.write(loginDTO);
        Object obj = client.read();
        if (obj instanceof Boolean)
        {
            boolean valid = (boolean) obj;
            if (!valid)
            {
                Utils.showAlert("Failed", "Password change unsuccessful");
                return;
            }
        }
        else {
            System.err.println("Wrong object type - " + obj.getClass());
            return;
        }

        loginDTO = new LoginDTO(currentClub.getName(), newPassword, LoginDTO.Type.ChangePass);
        client.write(loginDTO);
        obj = client.read();
        if (obj instanceof Boolean)
        {
            boolean valid = (boolean) obj;
            if (valid)
            {
                Utils.showAlert("Password Changed", "Password Changed successfully");
            }
            else
            {
                Utils.showAlert("Failed!", "User not found");
            }
        }
        else {
            System.err.println("Wrong object type - " + obj.getClass());
        }
    }

}
