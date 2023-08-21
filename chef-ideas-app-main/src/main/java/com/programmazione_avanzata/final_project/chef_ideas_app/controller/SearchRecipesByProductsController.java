package com.programmazione_avanzata.final_project.chef_ideas_app.controller;

import com.programmazione_avanzata.final_project.chef_ideas_app.dto.RecipeInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.DisplayMethods;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.FXMLFileName;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.Map;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.SceneController;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Error;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.apache.http.HttpResponse;

import java.io.IOException;

public class SearchRecipesByProductsController {

    @FXML
    public Button return_button;
    @FXML
    public Button search_button;
    @FXML
    public TextField recipe_search_by_products_text_field;
    @FXML
    public ListView<RecipeInfoDto> found_recipes_list_view;

    public void onReturnButton(ActionEvent actionEvent) throws IOException {
        SceneController.changeScene(actionEvent, FXMLFileName.ACTION_MENU_VIEW);
    }

    public void onSearchButton() {
        var products = recipe_search_by_products_text_field.getText();
        if (!products.isEmpty()) {
            var foodEntitiesList = products.split(",");
            var response = HttpRequest.findRecipesByProducts(foodEntitiesList);
            if (response != null && HttpRequest.isResponseSuccess(response)) {
                displayResults(products, response);
            } else if (response != null) {
                Error.displayErrorPopup(response);
            }
        } else {
            Error.displayErrorPopUp(Message.INCORRECT_INPUT);
        }
    }

    public void setRecipeSearchByProductsTextField(String searchInput) {
        this.recipe_search_by_products_text_field.setText(searchInput);
    }

    public void displayResults(String products, HttpResponse response) {
        var recipeList = Map.getRecipeInfoDtoList(response);
        DisplayMethods.displayFoundRecipesInListView(found_recipes_list_view, recipeList, FXMLFileName.SEARCH_RECIPES_BY_PRODUCTS_VIEW, products);
    }
}
