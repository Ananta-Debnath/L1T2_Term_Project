package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.DTO.SellPlayerDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Player.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.function.UnaryOperator;


public class PlayersListController {

    @FXML
    public TableView<Player> playersTable;
    @FXML
    public ImageView playerImage;
    @FXML
    public Label playerName;
    @FXML
    public Label playerDetails;
    @FXML
    public Label playerStats;
    @FXML
    public VBox fullPlayerDetails;
    @FXML
    public Button playerSellButton;

    @FXML
    public VBox finalSellBox;
    @FXML
    public Button finalSellButton;
    @FXML
    public SplitPane fullListDetails;
    @FXML
    public TextField sellAmountField;


    private Client client;
    private Club club;

    public void initialize(){

        playersTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection)->{
                if(newSelection!=null){

                    updatePlayerDetails(newSelection);
                }
            });

        UnaryOperator<TextFormatter.Change> filter = change -> {
            return change.getControlNewText().matches("\\d*") ? change : null;
        };
        sellAmountField.setTextFormatter(new TextFormatter<>(filter));
    }
    
    public void initializeValues(Client client, Club c){

        System.out.println("Setting club: " + (c != null ? c.getName() : "null"));
        System.out.println("Players count: " + (c != null ? c.getPlayersList().size() : "0"));

        this.client = client;
        this.club=c;
        refreshPlayersList();
    }

    private void refreshPlayersList(){
        club.loadPlayers(client);
        System.out.println("Refreshing player list");
        playersTable.getItems().clear();

        if(club!=null){
            System.out.println("Adding "+club.getPlayersList().size() +" players....");
            playersTable.getItems().addAll(club.getPlayersList());

            if(!club.getPlayersList().isEmpty()){

                playersTable.getSelectionModel().selectFirst(); 
            }
        }else{
            System.out.printf("NULL CLUB");
        }
    }


    private void updatePlayerDetails(Player player){

        try {

            String imagePath= "/Images/Players/" + player.getName().toLowerCase().replace(" ", "_")+ ".jpeg";


            System.out.println(getClass().getResource(imagePath));
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());


            playerImage.setImage(image);

        }catch(Exception e){

            System.out.println("Player image not found");

            String defaultImagePath="/Images/Players/default.jpeg";

            playerImage.setImage(new Image(getClass().getResource(defaultImagePath).toExternalForm()));
        }

        playerName.setText(player.getName() + " #"+ player.getJerseyNumber());


        playerDetails.setText(
            "Position: "+player.getPosition()+ "\n"+
            "Age: "+ player.getAge()+   " | Height: "+ String.format("%.2f", player.getHeight())+  "\n"+
            "Nationality: "+player.getNationality()+ "\n" +
            "Value: " +formatCurrency(player.getValue()) + "\n" +
            "Contract Validity: "+ player.getContractEnd()
        );


        playerStats.setText(
           "Goals: "+player.getGoals()+ " | Assists: "+player.getAssists() + "\n" +
           "Tackles: "+player.getTackles() + " | Interceptions: " + player.getInterceptions() + "\n" +
           "Matches played: "+ player.getMatchPlayed() + "\n" +
           "Form: " +player.getForm()
        );
    }

     private String formatCurrency(long value) {
        if(value>=1_000_000){

            return String.format("€%,dM", value / 1_000_000);
        }else if(value>=1_000){

            return String.format("€%,dK", value / 1_000);
        }
        return String.format("€%,d", value);
    }

    @FXML
    public void setSellMoney(ActionEvent actionEvent) {
        fullListDetails.setVisible(false);
        finalSellBox.setVisible(true);
    }

    @FXML
    public void returnToSquad(ActionEvent actionEvent) {
        finalSellBox.setVisible(false);
        fullListDetails.setVisible(true);
    }

    @FXML
    public void handleFinalSell(ActionEvent actionEvent) {

        Player selectedPlayer = playersTable.getSelectionModel().getSelectedItem();
        if(selectedPlayer==null){
            System.out.println("No player selected. try again");
            return;
        }

        long amount = selectedPlayer.getValue();
        if (!sellAmountField.getText().isEmpty()) amount = Long.parseLong(sellAmountField.getText());
        SellPlayerDTO sellPlayerDTO = new SellPlayerDTO(selectedPlayer.getId(), amount, selectedPlayer.getTeam());

        Alert confirmation=new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Sale");
        confirmation.setHeaderText("Confirm Player Sale");
        confirmation.setContentText(String.format("Do you really want to sell %s for €%s?", selectedPlayer.getName(),amount));

        ButtonType yesButton=new ButtonType("YES", ButtonBar.ButtonData.YES);
        ButtonType noButton=new ButtonType("No", ButtonBar.ButtonData.NO);
        confirmation.getButtonTypes().setAll(yesButton,noButton);

        long finalAmount = amount;
        confirmation.showAndWait().ifPresent(response->{

            if(response==yesButton){

                client.write(sellPlayerDTO);
                Object obj = client.read();
                if (obj instanceof Boolean)
                {
                    if ((boolean) obj)
                    {
                        showAlert("Success", String.format("%s has been listed for sale for €%s", selectedPlayer.getName(), finalAmount));
                    }
                    else
                    {
                        showAlert("Failed", String.format("%s could not be listed for sale", selectedPlayer.getName()));
                    }
                }
                else System.err.println("Object not Boolean");

                refreshPlayersList();
                // TODO: update player value instantly
                finalSellBox.setVisible(false);
                fullListDetails.setVisible(true);
                sellAmountField.clear();

            }else{
                showAlert("Cancelled","Sell process terminated");
            }
        });

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
