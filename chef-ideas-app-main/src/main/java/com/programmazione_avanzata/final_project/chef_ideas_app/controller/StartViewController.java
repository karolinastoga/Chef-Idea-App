package com.programmazione_avanzata.final_project.chef_ideas_app.controller;

import com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.FXMLFileName;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.SceneController;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Error;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

import static com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest.isResponseSuccess;

public class StartViewController implements Initializable {

    @FXML
    TextField enter_recipes_number_text_field;
    @FXML
    Button initialize_database_button;
    @FXML
    Button start_application_button;
    @FXML
    Label number_of_recipes_label;
    @FXML
    Label http_status_label;

    public void onStartButton(ActionEvent actionEvent) {
        SceneController.changeScene(actionEvent, FXMLFileName.ACTION_MENU_VIEW);
    }

    public void onInitializeDatabaseButton(){
        clearLabels();
        var recipesNumber = enter_recipes_number_text_field.getText();
        if (isRecipeNumberInputCorrect(recipesNumber)) {
            displayInitializationMessage(recipesNumber);
            var response = HttpRequest.initializeDatabase(recipesNumber);
            if (response != null && isResponseSuccess(response)) {
                displaySuccessMessage();
                //start_application_button.setDisable(false);
            } else if (response != null) {
                Error.displayErrorPopup(response);
            }
        } else {
            displayErrorMessage();
        }
    }

    private void clearLabels() {
        number_of_recipes_label.setText("");
        http_status_label.setText("");
    }

    private void displayInitializationMessage(String recipesNumber) {
        var message =
                "Loading data initialization.\n" +
                "Recipes to load from CSV file to database: " + recipesNumber;
        number_of_recipes_label.setText(message);
        number_of_recipes_label.setTextFill(Color.BLACK);
    }

    private void displaySuccessMessage() {
        var message = "Successful data initialization";
        http_status_label.setText(message);
        http_status_label.setTextFill(Color.GREEN);
    }

    private void displayErrorMessage() {
        var message =
                "You have enter wrong parameter!\n" +
                "Please enter the integer number.";
        number_of_recipes_label.setText(message);
        number_of_recipes_label.setTextFill(Color.RED);
    }

    private boolean isRecipeNumberInputCorrect(String recipesNumber) {
        if (isNumeric(recipesNumber)) {
            return true;
        } else {
            displayErrorMessage();
            return false;
        }
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //start_application_button.setDisable(true);
    }
}
