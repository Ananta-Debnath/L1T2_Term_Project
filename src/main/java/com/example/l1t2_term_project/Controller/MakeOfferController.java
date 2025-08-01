package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Offer;
import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class MakeOfferController {
    private Client client;
    private Club fromClub;
    private Club toClub;
    private Offer offer;

    @FXML
    public Label budgetLabel;
    @FXML
    public Label fromClubNameLabel;
    @FXML
    public TextField fromClubAmountField;
    @FXML
    public ComboBox<String> fromClubPlayerBox;

    @FXML
    public Label toClubNameLabel;
    @FXML
    public TextField toClubAmountField;
    @FXML
    public ComboBox<String> toClubPlayerBox;

    @FXML
    public Button cancelButton;

    @FXML
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

        fromClubPlayerBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                setText(name == null || name.trim().isEmpty() ? "No Player" : name);
            }
        });

        fromClubPlayerBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                setText(name == null || name.trim().isEmpty() ? "No Player" : name);
            }
        });

        toClubPlayerBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                setText(name == null || name.trim().isEmpty() ? "No Player" : name);
            }
        });

        toClubPlayerBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                setText(name == null || name.trim().isEmpty() ? "No Player" : name);
            }
        });
    }

    public void initializeValues(Client client, Club from, Club to, Offer offer) {
        this.client = client;
        this.fromClub = from;
        this.toClub = to;
        this.offer = offer;
        updateOfferDetails();
    }

    public void updateOfferDetails() {
        if (offer == null) offer = new Offer(0, Offer.Status.Make);
        fromClubNameLabel.setText(fromClub.getName());
        toClubNameLabel.setText(toClub.getName());
        budgetLabel.setText("Your Budget: " + fromClub.getBudget());

        fromClub.loadPlayers(client);
        fromClubPlayerBox.getItems().clear();
        fromClubPlayerBox.getItems().add("");
        fromClubPlayerBox.getItems().addAll(fromClub.getPlayersList().stream().map(Player::getName).collect(Collectors.toList()));
        fromClubPlayerBox.setValue(offer.getFromClubPlayer());

        toClub.loadPlayers(client);
        toClubPlayerBox.getItems().clear();
        toClubPlayerBox.getItems().add("");
        toClubPlayerBox.getItems().addAll(toClub.getPlayersList().stream().map(Player::getName).collect(Collectors.toList()));
        toClubPlayerBox.setValue(offer.getToClubPlayer());

        if (offer.getAmount() > 0) fromClubAmountField.setText(String.valueOf(offer.getAmount()));
        else if (offer.getAmount() < 0) toClubAmountField.setText(String.valueOf(-offer.getAmount()));
    }

    @FXML
    public void makeOffer() {

        Utils.playSound("Default_Click.wav");
        offer.setFromClub(fromClub.getName());
        offer.setToClub(toClub.getName());
        offer.setFromClubPlayer(fromClubPlayerBox.getValue());
        offer.setToClubPlayer(toClubPlayerBox.getValue());

        long fromClubAmount = fromClubAmountField.getText().isEmpty() ? 0 : Long.parseLong(fromClubAmountField.getText());
        long toClubAmount = toClubAmountField.getText().isEmpty() ? 0 : Long.parseLong(toClubAmountField.getText());
        offer.setAmount(fromClubAmount - toClubAmount);

        boolean valid = Utils.showConfirmationAlert("Make Offer", "Confirm your offer", "Press yes to proceed");
        if (!valid) return;

        client.write(offer);
        valid = (boolean) client.read();

        if (valid) Utils.showAlert("Successful", "Offer made successfully");
        else Utils.showErrorAlert("Failure!", "Invalid Offer");

        cancelOffer();
    }

    @FXML
    public void cancelOffer() {
        Utils.playSound("Default_Click.wav");
        // TODO: the cancel button will have the stackPane assigned to it
        @SuppressWarnings("unchecked")
        List<Pane> panes = (List<Pane>) cancelButton.getUserData();

        panes.get(0).getChildren().clear();
        panes.get(0).setVisible(false);

        panes.get(1).setVisible(true);
        panes.get(1).setDisable(false);
    }
}
