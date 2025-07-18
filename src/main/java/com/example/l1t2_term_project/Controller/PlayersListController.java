package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Player.Player; 
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class PlayersListController {

    @FXML
    public ListView<Player> playersListView;

    private Club testClub;

    public void initialize(){

       playersListView.setCellFactory(lv-> new ListCell<Player>(){

            protected void updateItem(Player p, boolean empty){

                super.updateItem(p, empty);
                setText(empty? null: p.getName()+ " " +p.getAge()+" "+p.getJerseyNumber()+" "+p.getPosition());
            }
       });
        
    }

    
    public void setClub(Club c){

        System.out.println("Setting club: " + (c != null ? c.getName() : "null"));
        System.out.println("Players count: " + (c != null ? c.getPlayersList().size() : "0"));

        this.testClub=c;
        refreshPlayersList();
    }

    private void refreshPlayersList(){

        System.out.println("Refreshing player list");
        playersListView.getItems().clear();

        if(testClub!=null){
            System.out.println("Adding "+testClub.getPlayersList().size() +" players....");
            playersListView.getItems().addAll(testClub.getPlayersList());
        }else{
            System.out.printf("NULL CLUB");
        }
    }

}
