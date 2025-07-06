package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class MarketController
{
    @FXML
    private VBox filterBox;

    @FXML
    ListView<Player> itemList;

    @FXML
    public Button cancelButton;

    @FXML
    private AnchorPane mainMenu;

    @FXML
    private TextField searchField;

    @FXML
    private Button filterButton;

    @FXML
    public void onFilterClick()
    {
        // mainMenu.setVisible(false);
        mainMenu.setDisable(true);
        filterBox.setVisible(true);
        System.out.println("Filter");
//        itemList.getItems().clear();
//        itemList.getItems().add(new Player());
    }

    @FXML
    public void onCancelClick()
    {
        mainMenu.setDisable(false);
        mainMenu.setVisible(true);
        filterBox.setVisible(false);
        System.out.println("Cancel");
    }

    @FXML
    public void onSearchClick(ActionEvent actionEvent) {
    }
}
