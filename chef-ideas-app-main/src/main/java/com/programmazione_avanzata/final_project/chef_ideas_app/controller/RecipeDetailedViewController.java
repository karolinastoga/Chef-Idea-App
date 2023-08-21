package com.programmazione_avanzata.final_project.chef_ideas_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmazione_avanzata.final_project.chef_ideas_app.dto.RecipeDetailedInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas_app.dto.RecipeInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas_app.request.HttpRequest;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.DisplayMethods;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.FXMLFileName;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.SceneController;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Confirmation;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Error;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Information;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.popup.Message;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RecipeDetailedViewController{

    private Stage stage;
    private String parentFxmlFile;
    private String inputSearch;
    private RecipeInfoDto recipeInfoDto;
    private RecipeDetailedInfoDto recipeDetailedInfoDto;

    @FXML
    public Label recipe_title_label;
    @FXML
    public Hyperlink hyperlink;
    @FXML
    public ListView<String> ingredients_list_view;
    @FXML
    public ListView<String> steps_list_view;
    @FXML
    public ListView<String> products_list_view;
    @FXML
    public MenuItem edit_button;
    @FXML
    public MenuItem delete_button;
    @FXML
    public Button return_button;

    public void onHyperlinkClick() throws IOException {
        Desktop.getDesktop().browse(URI.create("https://" + hyperlink.getText()));
    }

    public void onReturnButton(ActionEvent actionEvent) throws IOException, URISyntaxException {
        goBackToParentView(actionEvent);
    }

    public void onEditButton() {
        SceneController.goToEditView(stage, recipeInfoDto, recipeDetailedInfoDto);
    }

    public void onDeleteButton() {
        if (Confirmation.displayDeleteConfirmationPopup()) {
            var response = HttpRequest.deleteRecipe(recipeInfoDto.getRecipeId());
            if (response!=null && HttpRequest.isResponseSuccess(response)) {
                SceneController.changeScene(stage, FXMLFileName.SHOW_ALL_RECIPES_VIEW);
                Information.showSuccessPopUp(Message.DELETE_RECIPE_SUCCESS);
            }
            else if(response!=null){
                Error.displayErrorPopup(response);
            }
        }
    }

    public void displayRecipeDetailedInfo(){
        var recipeID = recipeInfoDto.getRecipeId();
        var response = HttpRequest.getRecipeDetailedInfo(recipeID);
        if(response!=null && HttpRequest.isResponseSuccess(response)) {
            if(getRecipeDetailedInfoDto(response)) {
                var observableListIngredients = DisplayMethods.getObservableListIngredients(recipeDetailedInfoDto);
                var observableListMethods = DisplayMethods.getObservableListMethods(recipeDetailedInfoDto);
                var observableListFoodEntities = DisplayMethods.getObservableListProducts(recipeDetailedInfoDto);
                setData(recipeDetailedInfoDto, observableListIngredients, observableListMethods, observableListFoodEntities);
            }
        }
        else if(response!=null){
            Error.displayErrorPopup(response);
        }
    }

    private void setData(RecipeDetailedInfoDto recipeDetailedInfo, ObservableList<String> observableListIngredients, ObservableList<String> observableListMethods, ObservableList<String> observableListFoodEntities) {
        recipe_title_label.setText(recipeDetailedInfo.getRecipeName());
        hyperlink.setText(recipeDetailedInfo.getLink());
        ingredients_list_view.setItems(observableListIngredients);
        steps_list_view.setItems(observableListMethods);
        products_list_view.setItems(observableListFoodEntities);
    }

    private void goBackToParentView(ActionEvent actionEvent) {
        if(parentFxmlFile.contains("search")){
            var fxmlLoader = SceneController.changeScene(actionEvent, parentFxmlFile);
            if (parentFxmlFile.contains("name")){
                SearchRecipesByNameController controller = fxmlLoader.getController();
                controller.setRecipeNameSearchTextField(inputSearch);
                controller.onSearchButton();
            }
            else {
                SearchRecipesByProductsController controller = fxmlLoader.getController();
                controller.setRecipeSearchByProductsTextField(inputSearch);
                controller.onSearchButton();
            }
        }
        else {
            SceneController.changeScene(actionEvent, parentFxmlFile);
        }
    }

    public void passInitialData(String parentFxmlFile, RecipeInfoDto recipeInfoDto) {
        this.parentFxmlFile = parentFxmlFile;
        this.recipeInfoDto = recipeInfoDto;
        this.stage = (Stage) recipe_title_label.getScene().getWindow();
    }

    public void passInitialData(String parentFxmlFile, RecipeInfoDto recipeInfoDto, String searchInput){
        this.parentFxmlFile = parentFxmlFile;
        this.recipeInfoDto = recipeInfoDto;
        this.inputSearch = searchInput;
        this.stage = (Stage) recipe_title_label.getScene().getWindow();
    }

    private boolean getRecipeDetailedInfoDto(HttpResponse response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            var entityString = EntityUtils.toString(response.getEntity(), "UTF-8");
            this.recipeDetailedInfoDto = mapper.readValue(entityString, RecipeDetailedInfoDto.class);
            return true;
        }catch (IOException ioe){
            Error.displayErrorPopup(ioe);
            return false;
        }
    }
}


