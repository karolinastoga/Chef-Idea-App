package com.programmazione_avanzata.final_project.chef_ideas_app.controller;

import com.programmazione_avanzata.final_project.chef_ideas_app.dto.RecipeDetailedInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas_app.dto.RecipeInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas_app.dto.RecipeInputDto;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.DisplayMethods;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.FXMLFileName;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.SceneController;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.Validation;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Confirmation;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Error;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Information;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Message;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;

import static com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest.editRecipe;
import static com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest.isResponseSuccess;
import static com.programmazione_avanzata.final_project.chef_ideas_app.utilities.DisplayMethods.*;

public class EditRecipeViewController {

    private Stage stage;
    private RecipeInfoDto recipeInfoDto;
    private RecipeDetailedInfoDto recipeDetailedInfoDto;
    private ObservableList<String> observableListIngredients;
    private ObservableList<String> observableListMethods;
    private ObservableList<String> observableListFoodEntities;

    @FXML
    public TextField title_text_field;
    @FXML
    public TextField link_text_field;
    @FXML
    public MenuItem save_button;
    @FXML
    public MenuItem cancel_button;
    @FXML
    public ListView<String> ingredients_list_view;
    @FXML
    public ListView<String> steps_list_view;
    @FXML
    public ListView<String> products_list_view;
    @FXML
    public TextField new_ingredient_text_field;
    @FXML
    public Button delete_ingredient_button;
    @FXML
    public Button add_ingredient_button;
    @FXML
    public TextField new_step_text_field;
    @FXML
    public Button delete_step_button;
    @FXML
    public Button add_step_button;
    @FXML
    public TextField new_product_text_field;
    @FXML
    public Button delete_product_button;
    @FXML
    public Button add_product_button;
    @FXML
    public Label title_error_message_label;

    public void onCancelButton() {
        SceneController.goToDetailedView(stage, recipeInfoDto, FXMLFileName.SHOW_ALL_RECIPES_VIEW);
    }

    public void onSaveButton() {
        if (areAllInputsCorrect() && Confirmation.displaySaveConfirmationPopUp()) {
            var updatedRecipe = getRecipeInputDto();
            var response = editRecipe(recipeInfoDto.getRecipeId(), updatedRecipe);
            if (response!=null && isResponseSuccess(response)) {
                SceneController.goToDetailedView(stage, recipeInfoDto, FXMLFileName.SHOW_ALL_RECIPES_VIEW);
                Information.showSuccessPopUp(Message.UPDATE_RECIPE_SUCCESS);
            }
            else if (response!=null){
                Error.displayErrorPopup(response);
            }
        }
        else{
            Error.displayErrorPopUp(Message.INCORRECT_INPUT);
        }
    }

    public void init(RecipeInfoDto recipeInfoDto, RecipeDetailedInfoDto recipeDetailedInfoDto){
        passRecipeId(recipeInfoDto, recipeDetailedInfoDto);
        displayRecipeInfo();
    }

    public void onAddIngredientButton() {
        var newIngredient = new_ingredient_text_field.getText();
        updateListView("Ingredient", newIngredient, observableListIngredients, ingredients_list_view, new_ingredient_text_field);
    }

    public void onDeleteIngredientButton() {
        deleteListViewItem(observableListIngredients, ingredients_list_view);
    }

    public void onAddStepButton() {
        var newMethod = new_step_text_field.getText();
        updateListView("Step", newMethod, observableListMethods, steps_list_view, new_step_text_field);
    }

    public void onDeleteStepButton() {
        deleteListViewItem(observableListMethods, steps_list_view);
    }

    public void onAddProductButton() {
        var newFoodEntity = new_product_text_field.getText();
        updateListView("Product", newFoodEntity, observableListFoodEntities, products_list_view, new_product_text_field);
    }

    public void onDeleteProductButton() {
        deleteListViewItem(observableListFoodEntities, products_list_view);
    }

    private boolean checkIfElementIsOnList(String newElement, ObservableList<String> list) {
        return list.contains(newElement);
    }

    private void passRecipeId(RecipeInfoDto recipeInfoDto, RecipeDetailedInfoDto recipeDetailedInfoDto) {
        this.recipeInfoDto = recipeInfoDto;
        this.recipeDetailedInfoDto = recipeDetailedInfoDto;
        observableListIngredients = getObservableListIngredients(recipeDetailedInfoDto);
        observableListMethods = getObservableListMethods(recipeDetailedInfoDto);
        observableListFoodEntities = getObservableListProducts(recipeDetailedInfoDto);
        this.stage = (Stage) title_text_field.getScene().getWindow();
    }

    private void displayRecipeInfo() {
        title_text_field.setText(recipeDetailedInfoDto.getRecipeName());
        link_text_field.setText(recipeDetailedInfoDto.getLink());
        DisplayMethods.displayEditableListView(ingredients_list_view, observableListIngredients);
        DisplayMethods.displayEditableListView(steps_list_view, observableListMethods);
        DisplayMethods.displayEditableListView(products_list_view, observableListFoodEntities);
    }

    private void updateListView(String kind, String newElement, ObservableList<String> list, ListView<String> listView, TextField textField) {
        if (!checkIfElementIsOnList(newElement, list)) {
            list.add(newElement);
            displayEditableListView(listView, list);
            textField.setText(null);
        } else {
            textField.setText(String.format("Error! %s already exist.", kind));
            textField.setStyle("-fx-text-fill: red;");
        }
    }

    private boolean areAllInputsCorrect() {
        var name = Validation.validateInput(title_text_field, title_error_message_label);
        var ingredients = Validation.validateInput(ingredients_list_view, new_ingredient_text_field);
        var methods = Validation.validateInput(steps_list_view, new_step_text_field);
        var products = Validation.validateInput(products_list_view, new_product_text_field);
        return name && ingredients && methods && products;
    }

    private RecipeInputDto getRecipeInputDto() {
        var name = title_text_field.getText();
        var link = link_text_field.getText();
        var ingredients = new HashSet<>(ingredients_list_view.getItems());
        var methods = new ArrayList<>(steps_list_view.getItems());
        var foodEntities = new HashSet<>(products_list_view.getItems());
        return new RecipeInputDto(name, link, ingredients, methods, foodEntities);
    }
}
