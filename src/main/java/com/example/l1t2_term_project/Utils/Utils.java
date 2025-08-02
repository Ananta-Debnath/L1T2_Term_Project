package com.example.l1t2_term_project.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

import java.util.Objects;
import java.util.Optional;

public final class Utils {

    private static final String SOUNDS_PATH="/Audios/";

    private Utils() {}

    public static String formatCurrency(long value) {
        if(value>=1_000_000_000){
            return String.format("€%,dB", value/1_000_000_000);
        } else if(value>=1_000_000){
            return String.format("€%,dM", value / 1_000_000);
        }else if(value>=1_000){
            return String.format("€%,dK", value / 1_000);
        }
        return String.format("€%,d", value);
    }

    public static void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        playSound("Success.wav");
        styleAlert(alert, "information");
        alert.showAndWait();


    }

    public static boolean showConfirmationAlert(String title, String header, String content)
    {
        playSound("Confirmation.wav");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Customize buttons
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("custom-alert");
        try {
            dialogPane.getStylesheets().add(
                    Objects.requireNonNull(Utils.class.getResource("/styles/confirmation_alert.css")).toExternalForm()
            );

            String imagePath="/Images/confirmation_icon.png";

            Image image=new Image(Objects.requireNonNull(Utils.class.getResource(imagePath).toExternalForm()));

            dialogPane.setGraphic(new ImageView(image));
        } catch (NullPointerException e) {
            System.err.println("Failed to load alert stylesheet");
        }
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }

    public static void showErrorAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        playSound("Error.wav");
        styleAlert(alert, "error");
        alert.showAndWait();
    }

    public static void showCancelAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        playSound("Cancel.wav");
        styleAlert(alert, "information");
        alert.showAndWait();
    }

    public static void playSound(String filename){

        try{

            String path=Objects.requireNonNull(Utils.class.getResource(SOUNDS_PATH+filename.toLowerCase())).toString();

            AudioClip sound=new AudioClip(path);
            sound.play();
        }catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    public static void styleAlert(Alert alert, String type){

        try{
            DialogPane dialogPane= alert.getDialogPane();

            dialogPane.getStylesheets().add(Objects.requireNonNull(Utils.class.getResource("/styles/custom_alert.css").toExternalForm()));
            dialogPane.getStyleClass().add("custom-alert");
            dialogPane.getStyleClass().add(type.toLowerCase()+"-alert");

            if(type.equals("information")){

                String imagePath="/Images/info_icon.png";

                Image image=new Image(Objects.requireNonNull(Utils.class.getResource(imagePath).toExternalForm()));

                dialogPane.setGraphic(new ImageView(image));
            }else if(type.equals("error")){

                String imagePath="/Images/error_icon.png";
                Image image=new Image(Objects.requireNonNull(Utils.class.getResource(imagePath).toExternalForm()));

                dialogPane.setGraphic(new ImageView(image));
            }

        }catch (Exception e){
            System.err.println("failed to load alert css"+e.getMessage());
            e.printStackTrace();
        }
    }
}
