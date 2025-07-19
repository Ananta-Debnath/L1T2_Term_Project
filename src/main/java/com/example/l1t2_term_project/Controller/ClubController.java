package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.DTO.LoginDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Player.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClubController {
    private Client client;

    private Club club;

    @FXML
    public AnchorPane clubMenu;

    @FXML
    public Button clubPlayersbutton;

    @FXML
    public Button clubClubButton;

    @FXML
    public Button clubTransferButton;

    @FXML
    public Button clubSignOutButton;

    @FXML
    public Button transferBuy;

    @FXML
    public Button transferSell;

    @FXML
    public VBox transferButton;

    @FXML
    public BorderPane clubBorderPane;

    @FXML
    public Button clubPlayersbutton2;

    @FXML
    public Button clubSignOutButton2;

    @FXML
    public Button clubTransferButton2;

    @FXML
    public Button clubClubButton2;

    @FXML
    public StackPane contentPane;


    public void initializeValues(Client client)
    {
        this.client = client;

        // Load club details
        Object obj = client.read();
        if (obj instanceof Club) club = (Club) obj;
        else System.err.println("Wrong object type - " + obj.getClass());

        // Load club player list
        obj = client.read();
        if (obj instanceof List<?>)
        {
            List<?> list = (List<?>) obj;
            if (!list.isEmpty() && list.get(0) instanceof Player)
            {
                @SuppressWarnings("unchecked")
                List<Player> players = (List<Player>) list;
                for (Player player : players) club.addPlayer(player);
            }
            else
            {
                System.err.println("No Player or Wrong object");
            }
        }
    }

    public void OpenPlayers(ActionEvent actionEvent) {

        try {
            contentPane.getChildren().clear();
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/PlayersList.fxml"));
            Parent PlayersListView =loader.load();

            PlayersListController playerslistController=loader.getController();
            playerslistController.setClub(club);

            contentPane.getChildren().setAll(PlayersListView);

            clubMenu.setVisible(false);
            clubBorderPane.setVisible(true);
            transferButton.setVisible(false);

        } catch(IOException e){
            e.printStackTrace();

        }
    }

    public void OpenClub(ActionEvent actionEvent) {

        try {
            contentPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/Club_Detail.fxml"));
            Parent clubDetailView = loader.load();
            contentPane.getChildren().setAll(clubDetailView); // Load into StackPane


            clubMenu.setVisible(false);
            clubBorderPane.setVisible(true);
            transferButton.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void OpenTransfer(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        clubMenu.setVisible(false);
        clubBorderPane.setVisible(true);
        transferButton.setVisible(true);

    }

        /* 

        public void OpenMarket(ActionEvent actionEvent) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Market.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage marketStage = new Stage();

            marketStage.setTitle("Market");
            marketStage.setScene(scene);
            marketStage.show();

            // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        }

        */

    @FXML
    public void OpenMarket(ActionEvent event) { 
        try {
            contentPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/Market.fxml"));
            Parent marketView = loader.load();
            ((MarketController) loader.getController()).initializeValues(client);
            contentPane.getChildren().setAll(marketView); // Load into StackPane

            // Clear other visible elements
            clubMenu.setVisible(false);
            clubBorderPane.setVisible(true);
            transferButton.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
           
        }
    }



    @FXML
    private void handleSignOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Do you really want to sign out?");

        // Customize buttons
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("custom-alert");
        try {
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/Style/custom_alert.css")).toExternalForm()
            );
        } catch (NullPointerException e) {
            System.err.println("Failed to load alert stylesheet");
        }
    
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {

           // Platform.exit();

            //TODO:Log-out and load sign-in FXML
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/SignIn.fxml"));
                Parent clubView = loader.load();
                ((SignInController) loader.getController()).setClient(client);
                clubSignOutButton.getScene().setRoot(clubView);
                client.write(new LoginDTO(club.getName(), null, false));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // For immediate termination (optional)
            // System.exit(0);
        }
    }


}
