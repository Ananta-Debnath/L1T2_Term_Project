package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.HelloApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ClubController {
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


    public void OpenPlayers(ActionEvent actionEvent) {

        clubMenu.setVisible(false);
        clubBorderPane.setVisible(true);
        transferButton.setVisible(false);
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

            // For immediate termination (optional)
            System.exit(0);
        }
    }


}
