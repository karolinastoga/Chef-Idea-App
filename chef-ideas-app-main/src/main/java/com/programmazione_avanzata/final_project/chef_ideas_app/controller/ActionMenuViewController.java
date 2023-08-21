package com.programmazione_avanzata.final_project.chef_ideas_app.controller;

import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.FXMLFileName;
import com.programmazione_avanzata.final_project.chef_ideas_app.utilities.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ActionMenuViewController {

    @FXML
    public Button show_all_button;
    @FXML
    public Button add_new_button;
    @FXML
    public Button search_button;
    @FXML
    public Button find_recipe_button;

    public void onShowAllButton(ActionEvent actionEvent) {
        SceneController.changeScene(actionEvent, FXMLFileName.SHOW_ALL_RECIPES_VIEW);
    }

    public void onAddNewButton(ActionEvent actionEvent) {
        SceneController.changeScene(actionEvent, FXMLFileName.ADD_NEW_RECIPE_VIEW);
    }

    public void onSearchButton(ActionEvent actionEvent) {
        SceneController.changeScene(actionEvent, FXMLFileName.SEARCH_RECIPES_BY_NAME_VEIW);
    }

    public void onFindRecipeButton(ActionEvent actionEvent) {
        SceneController.changeScene(actionEvent, FXMLFileName.SEARCH_RECIPES_BY_PRODUCTS_VIEW);
    }
}
