package com.programmazione_avanzata.final_project.chef_ideas_app.controller;

import com.programmazione_avanzata.final_project.chef_ideas_app.dto.RecipeInputDto;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.*;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Confirmation;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Error;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Information;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest.addNewRecipe;
import static com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest.isResponseSuccess;

public class AddNewRecipeViewController {

    @FXML
    public Label process_message_label;
    @FXML
    public TextField name_text_field;
    @FXML
    public TextField link_text_field;
    @FXML
    public TextArea ingredients_text_area;
    @FXML
    public TextArea steps_text_area;
    @FXML
    public TextArea products_text_area;
    @FXML
    public Button submit_button;
    @FXML
    public Button return_button;
    @FXML
    public Label name_error_input;

    public void onReturnButton(ActionEvent actionEvent) {
        SceneController.changeScene(actionEvent, FXMLFileName.ACTION_MENU_VIEW);
    }

    public void onSubmitButton(ActionEvent actionEvent) {
        if (areAllInputsCorrect() && Confirmation.displaySaveConfirmationPopUp()) {
            var newRecipe = getRecipeInputDto();
            var response = addNewRecipe(newRecipe);
            if (response!=null && isResponseSuccess(response)) {
                SceneController.changeScene(actionEvent, FXMLFileName.SHOW_ALL_RECIPES_VIEW);
                Information.showSuccessPopUp(Message.ADD_NEW_RECIPE_SUCCESS);
            } else if(response!=null){
                Error.displayErrorPopup(response);
            }
        } else if (!areAllInputsCorrect()) {
            Error.displayErrorPopUp(Message.INCORRECT_INPUT);
        }
    }

    private boolean areAllInputsCorrect() {
        var name = Validation.validateInput(name_text_field, name_error_input);
        var ingredients = Validation.validateInput(ingredients_text_area);
        var methods = Validation.validateInput(steps_text_area);
        var products = Validation.validateInput(products_text_area);
        return name && ingredients && methods && products;
    }

    private RecipeInputDto getRecipeInputDto() {
        var name = name_text_field.getText();
        var link = link_text_field.getText();
        var ingredients = Arrays.stream(ingredients_text_area.getText().split("\n")).collect(Collectors.toSet());
        var methods = Arrays.stream(steps_text_area.getText().split("\n")).collect(Collectors.toList());
        var foodEntities = Arrays.stream(products_text_area.getText().split("\n")).collect(Collectors.toSet());
        return new RecipeInputDto(name, link, ingredients, methods, foodEntities);
    }
}
