package com.example.l1t2_term_project.Controller;

import com.example.l1t2_term_project.Model.Player.Player;
import com.example.l1t2_term_project.Model.Player.PlayerFilter;
import com.example.l1t2_term_project.Model.Player.Position;
import com.example.l1t2_term_project.Model.Player.Role;
import com.example.l1t2_term_project.Model.Player.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


/*
TODO list
TODO: make role field have appropriate items according to position field
TODO: Use enum in appropriate places
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
    }

    @FXML
    public void openFilter() // Filter Button
    {
        mainMenu.setDisable(true);
        filterBox.setVisible(true);
        // System.out.println("Filter");
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
        // System.out.println("Cancel");
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
        if (filter.getPosition() != null) positionField.setValue(filter.getPosition()); // TODO: Replace with enum
        else positionField.setValue(positionField.getItems().get(0));

        if (filter.getRole() != null) roleField.setValue(filter.getRole()); // TODO: Change get method to role & Replace with enum
        else roleField.setValue(roleField.getItems().get(0));

        if (filter.getNationality() != null) nationField.setValue(filter.getNationality());
        else nationField.setValue(nationField.getItems().get(0));

        if (filter.getTeam() != null) clubField.setValue(filter.getTeam());
        else roleField.setValue(roleField.getItems().get(0));

        if (filter.getStartingValue() != 0) minValueField.setText(Integer.toString(filter.getStartingValue()));
        else minValueField.setText(null);

        if (filter.getEndingValue() != 0) maxValueField.setText(Integer.toString(filter.getEndingValue()));
        else maxValueField.setText(null);

        availabilityField.setSelected(filter.isForSale());
    }
}
