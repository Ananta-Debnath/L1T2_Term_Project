package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Model.Player.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.UnaryOperator;


/*
TODO list
TODO: enhance show player with details and buttons
TODO: implement networking
TODO: add return button to club home page
TODO: quality improvements - mute, ...
 */


public class MarketController
{
    // Non-FXML variables
    PlayerFilter filter = new PlayerFilter();
    List<Player> players;

    @FXML
    public AnchorPane mainMenu;
    @FXML
    public TextField searchField;
    @FXML
    public TableView<Player> playerTable;

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
    public HBox playerShowBox;
    @FXML
    public Label nameLabel;


    @FXML
    private void initialize()
    {
        // TODO: Set the items in the ComboBox
        // Need player collection class for this
        // NOTE: the first element has to indicate null value

        positionField.getItems().add(null);
        positionField.getItems().addAll(Position.values());

        setRoleField();

        nationField.getItems().add(null);
        nationField.getItems().addAll(PlayerCollection.getAllNationalities()); // TODO: Use networking

        clubField.getItems().add(null);
        clubField.getItems().addAll(PlayerCollection.getAllTeams()); // TODO: use networking

        playerTable.getItems().addAll(PlayerCollection.getFilteredPlayers(filter)); // TODO: use networking


        // TableView logic
        playerTable.setPlaceholder(new Label("No players found"));
        playerTable.setRowFactory(tv -> {
            TableRow<Player> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) { // double click
                    Player clickedPlayer = row.getItem();
                    showPlayerDetails(clickedPlayer);
                }
            });
            return row;
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
        // NOTE: change listener to focus change??
        minValueField.textProperty().addListener((obs, oldText, newText) -> {
            onMinValueChange();
        });

        maxValueField.textProperty().addListener((obs, oldText, newText) -> {
            onMaxValueChange();
        });
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
        // TODO: get Players from server
        filter.setName(searchField.getText());
        players = PlayerCollection.getFilteredPlayers(filter);
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
        setFieldsInFilterBox(new PlayerFilter());
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


    // Non-FXML methods
    private PlayerFilter getFilterFromFields()
    {
        PlayerFilter filter = new PlayerFilter();

        filter.setPosition(positionField.getValue());
        filter.setRole(roleField.getValue());
        filter.setNationality(nationField.getValue());
        filter.setTeam(clubField.getValue());

        if (!minValueField.getText().isEmpty()) filter.setMinValue(Double.parseDouble(minValueField.getText()));
        else filter.setMinValue(0);

        if (!maxValueField.getText().isEmpty()) filter.setMaxValue(Double.parseDouble(maxValueField.getText()));
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
        // TODO: include more details
        nameLabel.setText(player.getName());
        playerShowBox.setVisible(true);
        mainMenu.setDisable(true);
    }
}
