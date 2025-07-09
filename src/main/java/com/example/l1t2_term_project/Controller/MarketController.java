package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Model.Player.PlayerFilter;
import com.example.l1t2_term_project.Model.Player.Position;
import com.example.l1t2_term_project.Model.Player.Role;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


/*
TODO list
TODO: Design fxml for player list
TODO: get player lists (applyFilter)
TODO: show player
 */


public class MarketController
{
    // Non-FXML variables
    PlayerFilter filter = new PlayerFilter();

    @FXML
    public AnchorPane mainMenu;
    @FXML
    public TextField searchField;

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
    private void initialize()
    {
        // TODO: Set the items in the ComboBox
        // Need player collection class for this
        // NOTE: the first element has to indicate null value

        positionField.getItems().add(null);
        positionField.getItems().addAll(Position.values());

        setRoleField();

        nationField.getItems().add(null);
        // TODO: get nations

        clubField.getItems().add(null);
        // TODO: get clubs


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
                setText(club == null || empty ? "Select Nation" : club);
            }
        });

        clubField.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String club, boolean empty) {
                super.updateItem(club, empty);
                setText(club == null || empty ? "Select Nation" : club);
            }
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
        // TODO: get Players from Player Collection
        filter.setName(searchField.getText());
        System.out.println(filter);
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


    // Non-FXML methods
    private PlayerFilter getFilterFromFields()
    {
        PlayerFilter filter = new PlayerFilter();

        filter.setPosition(positionField.getValue());
        filter.setRole(roleField.getValue());
        filter.setNationality(nationField.getValue());
        filter.setTeam(clubField.getValue());

        if (!minValueField.getText().isEmpty()) filter.setStartingValue(Integer.parseInt(minValueField.getText()));
        else filter.setStartingValue(0);

        if (!maxValueField.getText().isEmpty()) filter.setEndingValue(Integer.parseInt(maxValueField.getText()));
        else filter.setEndingValue(0);

        filter.setForSale(availabilityField.isSelected());
        return filter;
    }

    private void setFieldsInFilterBox(PlayerFilter filter)
    {
        positionField.setValue(filter.getPosition());
        roleField.setValue(filter.getRole());
        nationField.setValue(filter.getNationality());
        clubField.setValue(filter.getTeam());

        if (filter.getStartingValue() != 0) minValueField.setText(Integer.toString(filter.getStartingValue()));
        else minValueField.setText("");

        if (filter.getEndingValue() != 0) maxValueField.setText(Integer.toString(filter.getEndingValue()));
        else maxValueField.setText("");

        availabilityField.setSelected(filter.isForSale());
    }
}
