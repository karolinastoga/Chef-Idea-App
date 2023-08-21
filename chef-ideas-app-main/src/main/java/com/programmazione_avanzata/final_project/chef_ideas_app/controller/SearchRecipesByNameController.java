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

public class SearchRecipesByNameController{

    @FXML
    public Button search_button;
    @FXML
    public Button return_button;
    @FXML
    public TextField recipe_name_search_text_field;
    @FXML
    public ListView<RecipeInfoDto> found_recipes_list_view;

    public void onReturnButton(ActionEvent actionEvent) throws IOException {
        SceneController.changeScene(actionEvent, FXMLFileName.ACTION_MENU_VIEW);
    }

    public void onSearchButton() {
        var recipeName = recipe_name_search_text_field.getText();
        if (!recipeName.isEmpty()) {
            var response = HttpRequest.findRecipesByName(recipeName);
            if (response != null && HttpRequest.isResponseSuccess(response)) {
                displayResults(recipeName, response);
            } else if (response != null) {
                Error.displayErrorPopup(response);
            }
        }
        else{
            Error.displayErrorPopUp(Message.INCORRECT_INPUT);
        }
    }

    public void displayResults(String recipeName, HttpResponse response) {
        var recipes = Map.getRecipeInfoDtoList(response);
        DisplayMethods.displayFoundRecipesInListView(found_recipes_list_view, recipes, FXMLFileName.SEARCH_RECIPES_BY_NAME_VEIW, recipeName);
    }

    public void setRecipeNameSearchTextField(String message) {
        recipe_name_search_text_field.setText(message);
    }
}
