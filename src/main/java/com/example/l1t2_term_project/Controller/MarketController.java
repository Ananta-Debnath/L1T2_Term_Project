package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Client;
import com.example.l1t2_term_project.DTO.BuyPlayerDTO;
import com.example.l1t2_term_project.Model.Club.Club;
import com.example.l1t2_term_project.Model.Offer;
import com.example.l1t2_term_project.Model.Player.*;
import com.example.l1t2_term_project.Utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;


/*
TODO list
TODO: enhance show player with details and buttons
TODO: quality improvements - mute, ...
 */


public class MarketController implements Refreshable
{

    // Non-FXML variables
    private Client client;
    private Club club;
    PlayerFilter filter;
    List<Player> players;

    @FXML
    public VBox mainMenu;
    @FXML
    public TextField searchField;
    @FXML
    public TableView<Player> playerTable;
    @FXML
    public TableColumn<Player, Boolean> forSaleCol;

    @FXML
    public VBox filterBox;
    @FXML
    public ComboBox<Position> positionField;
    @FXML
    public ComboBox<Role> roleField;
    @FXML
    public ComboBox<String> nationField;
    @FXML
    public ComboBox<String> clubField;
    @FXML
    public TextField minValueField;
    @FXML
    public TextField maxValueField;
    @FXML
    public CheckBox availabilityField;

    @FXML
    public VBox playerShowBox;
    @FXML
    public Label nameLabel;
    @FXML
    public Button buyButton;
    @FXML
    public Button offerButton;
    @FXML
    public Label positionLabel;
    @FXML
    public Label valueLabel;
    @FXML
    public Label clubLabel;
    @FXML
    public ImageView transferImage;

    @FXML
    public StackPane makeOfferPane;

    @FXML
    private void initialize()
    {
        // Need player collection class for this
        // NOTE: the first element has to indicate null value

        positionField.getItems().add(null);
        positionField.getItems().addAll(Position.values());

        setRoleField();


        // TableView logic
        playerTable.setPlaceholder(new Label("No players found"));
        playerTable.setRowFactory(tv -> {
            TableRow<Player> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) { // double click

                    Player clickedPlayer = row.getItem();
                    Utils.playSound("Default_Click.wav");
                    showPlayerDetails(clickedPlayer);
                }
            });
            return row;
        });

        forSaleCol.setCellValueFactory(new PropertyValueFactory<>("forSale"));
        forSaleCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean forSale, boolean empty) {
                super.updateItem(forSale, empty);
                if (empty || forSale == null) {
                    setText(null);
                } else {
                    setText(forSale ? "Yes" : "No");
                }
            }
        });

        // ComboBox logic
        positionField.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Position position, boolean empty) {
                super.updateItem(position, empty);
                setText(position == null || empty ? "Select Position" : position.toString());
            }
        });

        positionField.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Position position, boolean empty) {
                super.updateItem(position, empty);
                setText(position == null || empty ? "Select Position" : position.toString());
            }
        });

        roleField.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Role role, boolean empty) {
                super.updateItem(role, empty);
                setText(role == null || empty ? "Select Role" : role.toString());
            }
        });

        roleField.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Role role, boolean empty) {
                super.updateItem(role, empty);
                setText(role == null || empty ? "Select Role" : role.toString());
            }
        });

        nationField.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String nation, boolean empty) {
                super.updateItem(nation, empty);
                setText(nation == null || empty ? "Select Nation" : nation);
            }
        });

        nationField.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String nation, boolean empty) {
                super.updateItem(nation, empty);
                setText(nation == null || empty ? "Select Nation" : nation);
            }
        });

        clubField.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String club, boolean empty) {
                super.updateItem(club, empty);
                setText(club == null || empty ? "Select Club" : club);
            }
        });

        clubField.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String club, boolean empty) {
                super.updateItem(club, empty);
                setText(club == null || empty ? "Select Club" : club);
            }
        });

        UnaryOperator<TextFormatter.Change> filter = change -> {
            return change.getControlNewText().matches("\\d*") ? change : null;
        };
        minValueField.setTextFormatter(new TextFormatter<>(filter));
        maxValueField.setTextFormatter(new TextFormatter<>(filter));
        minValueField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                onMinValueChange();
            }
        });

        maxValueField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Focus lost
                onMaxValueChange();
            }
        });
    }

    public void initializeValues(Client client, Club club)
    {
        this.client = client;
        this.club = club;
        filter = new PlayerFilter(client.getCurrentClub());
        searchPlayers();

        nationField.getItems().add(null);
        nationField.getItems().addAll(client.getNationList());

        clubField.getItems().add(null);
        clubField.getItems().addAll(client.getClubList());
    }

    @Override
    public void refresh() {
        searchPlayers();
    }

    @FXML
    public void openFilter() // Filter Button
    {
        mainMenu.setDisable(true);
        filterBox.setVisible(true);
    }

    @FXML
    public void searchPlayers()
    {
        filter.setName(searchField.getText());

        client.write(filter);
        Object obj = client.read();
        if (obj instanceof List<?>)
        {
            List<?> list = (List<?>) obj;
            players = list.stream().filter(Player.class::isInstance).map(Player.class::cast).collect(Collectors.toList());
        }
        else System.err.println("Wrong object");

        playerTable.getItems().clear();
        playerTable.getItems().addAll(players);
    }

    @FXML
    public void setFilter() // Save Button
    {
        filter = getFilterFromFields();
        filterBox.setVisible(false);
        mainMenu.setDisable(false);
        searchPlayers();
    }

    @FXML
    public void resetFilter()
    {
        setFieldsInFilterBox(new PlayerFilter(client.getCurrentClub()));
    }

    @FXML
    public void cancelFilter()
    {
        filterBox.setVisible(false);
        mainMenu.setDisable(false);
        setFieldsInFilterBox(filter); // Rollback to initial value
    }

    @FXML
    public void setRoleField()
    {
        roleField.getItems().clear();
        roleField.getItems().add(null);
        if (positionField.getValue() == null) roleField.getItems().addAll(Role.values());
        else roleField.getItems().addAll(positionField.getValue().getRoles());
    }

    @FXML
    public void cancelPlayerShow()
    {
        playerShowBox.setVisible(false);
        mainMenu.setDisable(false);
    }

    @FXML
    public void buyPlayer(ActionEvent actionEvent) //TODO:trigger on action
    {
        Player player = (Player) (((Button) actionEvent.getSource()).getUserData());

        boolean valid = Utils.showConfirmationAlert("Confirm Buy", "Confirm Player Buy", String.format("Do you want to buy %s for €%s?", player.getName(), player.getValueAsString()));

        if(valid)
        {
            client.write(new BuyPlayerDTO(player.getId(), client.getCurrentClub(), player.getWeeklySalary(), player.getTeam()));
            Object obj = client.read();
            if (obj instanceof Boolean)
            {
                if ((boolean) obj)
                {
                    Utils.showAlert("Purchase Successful!", String.format("%s has been bought for €%s", player.getName(), player.getValueAsString()));
                }
                else
                {
                    Utils.showErrorAlert("Purchase Failure!", String.format("%s could not be bought", player.getName()));
                }
            }
            else System.err.println("Object not Boolean");

            playerShowBox.setVisible(false);
            mainMenu.setDisable(false);
            searchPlayers();
        }else{
            Utils.showCancelAlert("Cancelled","Buy process terminated");
        }
    }

    @FXML
    public void showOfferScene(ActionEvent actionEvent) {
        Utils.playSound("Default_Click.wav");
        Player player = (Player) (((Button) actionEvent.getSource()).getUserData());
        try {
            makeOfferPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/l1t2_term_project/MakeOffer.fxml"));
            Parent marketView = loader.load();

            Club toClub = new Club();
            toClub.setName(player.getTeam());
            MakeOfferController makeOfferController = loader.getController();
            Offer offer = new Offer(0, Offer.Status.Make);
            offer.setToClubPlayer(player.getName());

            makeOfferController.initializeValues(client, club, toClub, offer);
            makeOfferController.toClubPlayerBox.setDisable(true);
            makeOfferController.cancelButton.setUserData(Arrays.asList(makeOfferPane, playerShowBox));
            makeOfferPane.getChildren().setAll(marketView); // Load into StackPane
            makeOfferPane.setVisible(true);
            playerShowBox.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    // Non-FXML methods
    private PlayerFilter getFilterFromFields()
    {
        PlayerFilter filter = new PlayerFilter(client.getCurrentClub());

        filter.setPosition(positionField.getValue());
        filter.setRole(roleField.getValue());
        filter.setNationality(nationField.getValue());
        filter.setTeam(clubField.getValue());

        if (!minValueField.getText().isEmpty()) filter.setMinValue(Long.parseLong(minValueField.getText()));
        else filter.setMinValue(0);

        if (!maxValueField.getText().isEmpty()) filter.setMaxValue(Long.parseLong(maxValueField.getText()));
        else filter.setMaxValue(0);

        filter.setForSale(availabilityField.isSelected());
        return filter;
    }

    private void setFieldsInFilterBox(PlayerFilter filter)
    {
        positionField.setValue(filter.getPosition());
        roleField.setValue(filter.getRole());
        nationField.setValue(filter.getNationality());
        clubField.setValue(filter.getTeam());

        if (filter.getMinValue() != 0) minValueField.setText(Double.toString(filter.getMinValue()));
        else minValueField.setText("");

        if (filter.getMaxValue() != 0) maxValueField.setText(Double.toString(filter.getMaxValue()));
        else maxValueField.setText("");

        availabilityField.setSelected(filter.isForSale());
    }

    public void onMinValueChange() {
        if (!minValueField.getText().isEmpty() && !maxValueField.getText().isEmpty() && Double.parseDouble(minValueField.getText()) > Double.parseDouble(maxValueField.getText()))
        {
            maxValueField.setText(minValueField.getText());
        }
    }

    public void onMaxValueChange() {
        if (maxValueField.getText().isEmpty() || (!minValueField.getText().isEmpty() && Double.parseDouble(minValueField.getText()) > Double.parseDouble(maxValueField.getText())))
        {
            minValueField.setText(maxValueField.getText());
        }
    }

    private void showPlayerDetails(Player player)
    {
        nameLabel.setText(player.getName());
        clubLabel.setText("Club: " +player.getTeam());
        valueLabel.setText("Value: " + player.getValueAsString());
        positionLabel.setText("Position: " + player.getPosition()+ "\n");
        transferImage.setImage(player.getImage());

        buyButton.setDisable(!player.isForSale());

        buyButton.setUserData(player);
        offerButton.setUserData(player);
        playerShowBox.setVisible(true);
        mainMenu.setDisable(true);
    }
}
