package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.DTO.LoginDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Utils.Utils;
import javafx.application.Platform;
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

// TODO: add alert to close and use sign out then
public class ClubController implements Refreshable {

    private Client client;
    private Refreshable currentController;
    private boolean quit;
    private Club club;

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
    public VBox transferBox;

    @FXML
    public StackPane contentPane;

    @FXML
    public VBox transferSubMenu;

    @FXML
    public Label clubBudgetLabel;

    @Override
    public void refresh()
    {
        System.out.println(Thread.currentThread().getName() + " started");
        while (true)
        {
            synchronized (client.getLock())
            {
                try {
                    client.getLock().wait();
                } catch (InterruptedException e) {
                    System.err.println("Refresh Thread Interrupted");
                }
            }
            if (quit) break;

            Platform.runLater(() -> {
                clubBudgetLabel.setText("Budget: "+"\n" + club.getBudgetAsString());
                if (currentController != null) currentController.refresh();
            });
            System.out.println(Thread.currentThread().getName() + " refreshed");
        }
        System.out.println(Thread.currentThread().getName() + " closed");
    }

    public void quitRefreshThread()
    {
        quit = true;
        synchronized (client.getLock())
        {
            client.getLock().notifyAll();
        }
    }

    public void initializeValues(Client client)
    {
        this.client = client;
        quit = false;

        // Load club details
        club = Club.readFromServer(client);

        // Load club player list
        club.loadPlayers(client);

        new Thread(this::refresh, "Refresh Thread").start();
    }

    public void OpenPlayers(ActionEvent actionEvent) {
        try {
            contentPane.getChildren().clear();
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/PlayersList.fxml"));
            Parent PlayersListView = loader.load();

            club.loadPlayers(client);
            PlayersListController playerslistController = loader.getController();
            playerslistController.initializeValues(client, club);
            currentController = (Refreshable) playerslistController;

            contentPane.getChildren().setAll(PlayersListView);

            transferBox.setVisible(false);
//            Utils.playSound("Default_Click.wav");
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

            club = Club.readFromServer(client);
            ClubDetailsController controller = loader.getController();
            controller.initializeValues(client, this.club);
            currentController = (Refreshable) controller;

            transferBox.setVisible(false);
            Utils.playSound("Default_Click.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void OpenTransfer(ActionEvent actionEvent) {
        contentPane.getChildren().clear();
        transferBox.setVisible(true);

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

        Utils.playSound("Default_Click.wav");
        try {
            contentPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/Market.fxml"));
            Parent marketView = loader.load();
            currentController = loader.getController();
            ((MarketController) currentController).initializeValues(client, club);
            contentPane.getChildren().setAll(marketView); // Load into StackPane

            // Clear other visible elements
            transferBox.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
           
        }
    }



    @FXML
    private void handleSignOut(ActionEvent event) {
        boolean valid = Utils.showConfirmationAlert("Sign Out", "Confirm Sign Out", "Do you really want to sign out?");

        if (valid) {

            try{
                quitRefreshThread();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/SignIn.fxml"));
                Parent clubView = loader.load();
                ((SignInController) loader.getController()).setClient(client);
                clubSignOutButton.getScene().setRoot(clubView);
                client.setCurrentClub(null);
                client.write(new LoginDTO(club.getName(), null, LoginDTO.Type.SignOut));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void switchToPlayerOffers(ActionEvent actionEvent) {
//        Utils.playSound("Default_Click.wav");
        try {
            contentPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/Offers.fxml"));
            Parent offerView = loader.load();
            currentController = loader.getController();
            ((OffersController) currentController).initializeValues(client,club);
            contentPane.getChildren().setAll(offerView); // Load into StackPane

            // Clear other visible elements
            transferBox.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void OpenTransferSubMenu(ActionEvent actionEvent) {
        transferSubMenu.setVisible(true);
    }
}
