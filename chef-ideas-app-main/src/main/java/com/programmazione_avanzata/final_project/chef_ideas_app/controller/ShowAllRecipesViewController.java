package com.programmazione_avanzata.final_project.chef_ideas_app.controller;

import com.programmazione_avanzata.final_project.chef_ideas_app.dto.RecipeInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.DisplayMethods;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.FXMLFileName;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.Map;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.SceneController;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Error;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.http.HttpResponse;

import java.net.URL;
import java.util.ResourceBundle;

import static com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest.getAllRecipes;
import static com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest.isResponseSuccess;

public class ShowAllRecipesViewController implements Initializable {

    @FXML
    public TableView<RecipeInfoDto> recipes_table_view;
    @FXML
    public TableColumn<RecipeInfoDto, Long> number_column;
    @FXML
    public TableColumn<RecipeInfoDto, String> recipe_name_column;

    public ShowAllRecipesViewController() {}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HttpResponse response = getAllRecipes();
        if(response != null && isResponseSuccess(response)) {
            displayRecipesInTableView(response);
        }
        else if(response != null){
            Error.displayErrorPopup(response);
        }
    }

    public void onReturnButton(ActionEvent actionEvent){
        SceneController.changeScene(actionEvent, FXMLFileName.ACTION_MENU_VIEW);
    }

    private void displayRecipesInTableView(HttpResponse response){
        var recipeList = Map.getRecipeInfoDtoList(response);
        DisplayMethods.displayTableView(recipeList, recipes_table_view);
    }

}
