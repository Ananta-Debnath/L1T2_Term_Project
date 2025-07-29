package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Offer;
import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.util.function.UnaryOperator;

public class MakeOfferController {
    private Client client;
    private Club fromClub;
    private Club toClub;

    @FXML
    public Label budgetLabel;
    @FXML
    public Label fromClubNameLabel;
    @FXML
    public TextField fromClubAmountField;
    @FXML
    public ComboBox<Player> fromClubPlayerBox;

    @FXML
    public Label toClubNameLabel;
    @FXML
    public TextField toClubAmountField;
    @FXML
    public ComboBox<Player> toClubPlayerBox;

    private void initialize() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            return change.getControlNewText().matches("\\d*") ? change : null;
        };
        fromClubAmountField.setTextFormatter(new TextFormatter<>(filter));
        toClubAmountField.setTextFormatter(new TextFormatter<>(filter));

        fromClubAmountField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                long amount = fromClubAmountField.getText().isEmpty() ? 0 : Long.parseLong(fromClubAmountField.getText());
                if (!fromClub.canBuy(amount)) fromClubAmountField.setText(String.valueOf(fromClub.getBudget()));
            }
        });
    }

    public void initializeValues(Client client, Club from, Club to, Offer offer) {
        this.client = client;
        this.fromClub = from;
        this.toClub = to;
        updateOfferDetails(offer);
    }

    public void updateOfferDetails(Offer offer) {
        if (offer == null) offer = new Offer(0, Offer.Status.Make);
        fromClubAmountField.setText(fromClub.getName());
        toClubAmountField.setText(toClub.getName());

        fromClub.loadPlayers(client);
        fromClubPlayerBox.getItems().clear();
        fromClubPlayerBox.getItems().add(null);
        fromClubPlayerBox.getItems().addAll(fromClub.getPlayersList());
        if (offer.getFromClubPlayerID() != null) fromClubPlayerBox.setValue(fromClub.getPlayer(offer.getFromClubPlayerID()));

        toClub.loadPlayers(client);
        toClubPlayerBox.getItems().clear();
        toClubPlayerBox.getItems().add(null);
        toClubPlayerBox.getItems().addAll(toClub.getPlayersList());
        if (offer.getToClubPlayerID() != null) toClubPlayerBox.setValue(fromClub.getPlayer(offer.getToClubPlayerID()));

        if (offer.getAmount() > 0) fromClubAmountField.setText(String.valueOf(offer.getAmount()));
        else if (offer.getAmount() < 0) toClubAmountField.setText(String.valueOf(-offer.getAmount()));
    }

    @FXML
    public void makeOffer() {
        Offer offer = new Offer(0, Offer.Status.Make);
        offer.setFromClub(fromClub.getName());
        offer.setToClub(toClub.getName());
        offer.setFromClubPlayerID(fromClubPlayerBox.getValue() == null ? null : fromClubPlayerBox.getValue().getId());
        offer.setToClubPlayerID(toClubPlayerBox.getValue() == null ? null : toClubPlayerBox.getValue().getId());

        long fromClubAmount = fromClubAmountField.getText().isEmpty() ? 0 : Long.parseLong(fromClubAmountField.getText());
        long toClubAmount = toClubAmountField.getText().isEmpty() ? 0 : Long.parseLong(toClubAmountField.getText());
        offer.setAmount(fromClubAmount - toClubAmount);

        boolean valid = Utils.showConfirmationAlert("Make Offer", "Confirm your offer", "");
        if (!valid) return;

        client.write(offer);
        valid = (boolean) client.read();

        if (valid) Utils.showAlert("Successful", "Offer made successfully");
        else Utils.showAlert("Failure!", "Invalid Offer");
    }

    @FXML
    public void cancelOffer(ActionEvent actionEvent) {
        // TODO: the cancel button will have the stackPane assigned to it
        StackPane stackPane = (StackPane) ((Button) actionEvent.getSource()).getUserData();
        stackPane.getChildren().clear();
        stackPane.setVisible(false);
    }
}
