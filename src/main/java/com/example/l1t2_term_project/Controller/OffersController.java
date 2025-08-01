package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Offer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.example.l1t2_term_project.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Arrays;

public class OffersController implements Refreshable {

    @FXML
    public TableView <Offer>incomingOffersTable;
    @FXML
    public TableView <Offer> outgoingOffersTable;
    @FXML
    public VBox offerActions;
    @FXML
    public Label selectedOfferLabel;

    @FXML
    public AnchorPane tabAnchorPane;
    @FXML
    public StackPane makeOfferPane;

    private Club userClub;
    private Client client;


    @FXML
    public void initialize(){
        setTable();
        setSelectionListener();
    }

    public void initializeValues(Client client, Club club) {
        System.out.println("Setting club: " + (club != null ? club.getName() : "null"));
        this.client = client;
        this.userClub = club;
        refresh();
    }

    private void setTable(){

        incomingOffersTable.getColumns().forEach(offerTableColumn -> {

            String colName=offerTableColumn.getText();

            if(colName.equals("Player")){
                offerTableColumn.setCellValueFactory(new PropertyValueFactory<>("toClubPlayer"));
            }else if(colName.equals("From Club")){
                offerTableColumn.setCellValueFactory(new PropertyValueFactory<>("fromClub"));
            }else if(colName.equals("Offer Amount")){
                offerTableColumn.setCellValueFactory(new PropertyValueFactory<>("amountAsString"));
            }else if(colName.equals("Swap Offer")){
                @SuppressWarnings("unchecked")
                TableColumn<Offer, String> swapOfferCol = (TableColumn<Offer, String>) offerTableColumn;
                swapOfferCol.setCellValueFactory(new PropertyValueFactory<>("fromClubPlayer"));
                swapOfferCol.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && (item == null || item.trim().isEmpty())) {
                            setText("N/A");  // your fallback text
                        } else {
                            setText(item);
                        }
                    }
                });

            }
        });

        outgoingOffersTable.getColumns().forEach(offerTableColumn -> {

            String colName=offerTableColumn.getText();

            if (colName.equals("Player")) {
                offerTableColumn.setCellValueFactory(new PropertyValueFactory<>("toClubPlayer"));
            } else if (colName.equals("To Club")) {
                offerTableColumn.setCellValueFactory(new PropertyValueFactory<>("toClub"));
            } else if (colName.equals("Offer Amount")) {
                offerTableColumn.setCellValueFactory(new PropertyValueFactory<>("amountAsString"));
            }else if(colName.equals("Swap Offer")){
                @SuppressWarnings("unchecked")
                TableColumn<Offer, String> swapOfferCol = (TableColumn<Offer, String>) offerTableColumn;
                swapOfferCol.setCellValueFactory(new PropertyValueFactory<>("fromClubPlayer"));
                swapOfferCol.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty && (item == null || item.trim().isEmpty())) {
                            setText("N/A");  // your fallback text
                        } else {
                            setText(item);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void refresh(){
        userClub.loadOffers(client);

        incomingOffersTable.getItems().clear();
        incomingOffersTable.getItems().addAll(userClub.getIncomingOffersList());

        outgoingOffersTable.getItems().clear();
        outgoingOffersTable.getItems().addAll(userClub.getOutgoingOffersList());

        if (!incomingOffersTable.getItems().isEmpty()) {
            incomingOffersTable.getSelectionModel().selectFirst();
        }else {
        System.out.println("NULL CLUB - Cannot load offers");
        }
    }

    private void setSelectionListener(){
        incomingOffersTable.getSelectionModel().selectedItemProperty().addListener(

                (obs, oldSelection, newSelection)->{
                    Utils.playSound("Default_Click.wav");
                    offerActions.setVisible(newSelection!=null);

                    if(newSelection!=null){
                        selectedOfferLabel.setText("Selected Offer: "+ newSelection.getFromClubPlayer()+" from "+newSelection.getFromClub());
                    }
                }
        );
    }



    //public


    @FXML
    public void acceptOffer(ActionEvent actionEvent) {
        Offer offer = incomingOffersTable.getSelectionModel().getSelectedItem();
        boolean valid = Utils.showConfirmationAlert("Accept Offer", "Confirm", "Do you want to accept this offer?");
        if (!valid) return;

        offer.setStatus(Offer.Status.Accept);
        client.write(offer);
        valid = (boolean) client.read();

        if (valid) Utils.showAlert("Successful", "Offer accepted");
        else Utils.showAlert("Failure!", "Invalid Offer");

        refresh();
    }

    @FXML
    public void rejectOffer(ActionEvent actionEvent) {
        Offer offer = incomingOffersTable.getSelectionModel().getSelectedItem();
        boolean valid = Utils.showConfirmationAlert("Reject Offer", "Confirm", "Do you want to reject this offer?");
        if (!valid) return;

        offer.setStatus(Offer.Status.Reject);
        client.write(offer);
        valid = (boolean) client.read();

        if (valid) Utils.showAlert("Successful", "Offer rejected");
        else Utils.showAlert("Failure!", "Invalid Offer");

        refresh();
    }

    @FXML
    public void showOfferScene(ActionEvent actionEvent) {
        Utils.playSound("Default_Click.wav");
        Offer offer = incomingOffersTable.getSelectionModel().getSelectedItem().counter();
        System.out.println(offer.toCSVLine());
        try {
            makeOfferPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/MakeOffer.fxml"));
            Parent marketView = loader.load();

            Club toClub = new Club();
            toClub.setName(offer.getToClub());
            MakeOfferController makeOfferController = loader.getController();

            makeOfferController.initializeValues(client, userClub, toClub, offer);
            makeOfferController.toClubPlayerBox.setDisable(false);
            makeOfferController.cancelButton.setUserData(Arrays.asList(makeOfferPane, tabAnchorPane));
            makeOfferPane.getChildren().setAll(marketView); // Load into StackPane
            makeOfferPane.setVisible(true);
            tabAnchorPane.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client getClient() {
        return client;
    }
}
