package com.programmazione_avanzata.final_project.chef_ideas_app.utilities;

import com.programmazione_avanzata.final_project.chef_ideas_app.dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseButton;

import java.util.List;
import java.util.stream.Collectors;

public class DisplayMethods {

    /**
     * Displays found recipes in list view.
     * When user performs double-click on list row, the scene changes to detailed view of this item.
     * @param listView list view
     * @param recipeInfoDtos recipe list
     * @param parentFxmlFile parent stage fxml file
     * @param searchInput searched input
     */
    public static void displayFoundRecipesInListView(ListView<RecipeInfoDto> listView, List<RecipeInfoDto> recipeInfoDtos, String parentFxmlFile, String searchInput) {
        addRecipesToListView(listView, recipeInfoDtos);
        onCellDoubleClick(listView, parentFxmlFile, searchInput);
    }

    /**
     * Displays editable list view.
     * When user performs click on list row it enables editing the field.
     * @param listView
     * @param observableList
     */
    public static void displayEditableListView(ListView<String> listView, ObservableList<String> observableList) {
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView());
        listView.setItems(observableList);
        onCellClick(listView);
    }

    /**
     * Displays table view with recipes.
     * When user performs double-click on row, the scene changes to detailed view of this item.
     * @param recipeInfoDtos
     * @param recipes_table_view
     */
    public static void displayTableView(List<RecipeInfoDto> recipeInfoDtos, TableView<RecipeInfoDto> recipes_table_view) {
        addRecipesToTableView(recipeInfoDtos, recipes_table_view);
        onRowDoubleClick(recipes_table_view);
    }

    /**
     * Add recipes to list view
     * @param listView list view to update
     * @param recipeInfoDtos recipe to add
     */
    private static void addRecipesToListView(ListView<RecipeInfoDto> listView, List<RecipeInfoDto> recipeInfoDtos) {
        var recipeObservableList= getObservableList(recipeInfoDtos);
        listView.setCellFactory((ListView<RecipeInfoDto> param) -> new ListCell<>() {
            @Override
            protected void updateItem(RecipeInfoDto item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getRecipeName());
                } else {
                    setText("");
                }
            }
        });
        listView.setItems(recipeObservableList);
    }

    /**
     * Add recipes to table view
     * @param recipes_table_view table view
     * @param recipeInfoDtos recipe to add
     */
    private static void addRecipesToTableView(List<RecipeInfoDto> recipeInfoDtos, TableView<RecipeInfoDto> recipes_table_view){
        var recipeObservableList= getObservableList(recipeInfoDtos);
        TableColumn<RecipeInfoDto, Long> number_column = (TableColumn<RecipeInfoDto, Long>) recipes_table_view.getColumns().get(0);
        TableColumn<RecipeInfoDto, String> recipe_name_column = (TableColumn<RecipeInfoDto, String>) recipes_table_view.getColumns().get(1);
        recipe_name_column.setCellValueFactory(new PropertyValueFactory<>("recipeName"));
        number_column.setCellValueFactory(new PropertyValueFactory<>("recipeId"));
        recipes_table_view.setItems(recipeObservableList);
        recipes_table_view.getSortOrder().add(number_column);
    }

    /**
     * Deletes clicked list view item
     * @param list
     * @param listView
     */
    public static void deleteListViewItem(ObservableList<String> list, ListView<String> listView) {
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                String rowData = listView.getSelectionModel().getSelectedItem();
                list.remove(rowData);
                displayEditableListView(listView, list);
            }
        });
    }

    /**
     *
     * @param listView
     */
    private static void onCellClick(ListView<String> listView) {
        listView.setOnEditCommit(t -> listView.getItems().set(t.getIndex(), t.getNewValue()));
    }

    /**
     *
     * @param listView
     * @param parentStage
     * @param searchInput
     */
    private static void onCellDoubleClick(ListView<RecipeInfoDto> listView, String parentStage, String searchInput) {
        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                RecipeInfoDto rowData = listView.getSelectionModel().getSelectedItem();
                SceneController.goToDetailedView(event, searchInput, rowData, parentStage);
            }
        });
    }

    /**
     *
     * @param tableView
     */
    private static void onRowDoubleClick(TableView<RecipeInfoDto> tableView) {
        tableView.setRowFactory(tv -> {
            TableRow<RecipeInfoDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    RecipeInfoDto rowData = row.getItem();
                    SceneController.goToDetailedView(event, rowData, FXMLFileName.SHOW_ALL_RECIPES_VIEW);
                }
            });
            return row;
        });
    }

    /**
     *
     * @param list
     * @return
     */
    private static ObservableList<RecipeInfoDto> getObservableList(List<RecipeInfoDto> list){
        return FXCollections.observableArrayList(list);
    }

    /**
     *
     * @param recipeDetailedInfo
     * @return
     */
    public static ObservableList<String> getObservableListMethods(RecipeDetailedInfoDto recipeDetailedInfo) {
        return FXCollections.observableArrayList(recipeDetailedInfo.getMethods().stream()
                .map(Method::getStepDescription)
                .collect(Collectors.toList()));
    }

    /**
     *
     * @param recipeDetailedInfo
     * @return
     */
    public static ObservableList<String> getObservableListIngredients(RecipeDetailedInfoDto recipeDetailedInfo) {
        return FXCollections.observableArrayList(recipeDetailedInfo.getIngredients().stream()
                .map(Ingredient::getName)
                .collect(Collectors.toList()));
    }

    /**
     * Converts products list to observableList
     * @param recipeDetailedInfo
     * @return ObservableList of recipeDetailedInfo
     */
    public static ObservableList<String> getObservableListProducts(RecipeDetailedInfoDto recipeDetailedInfo) {
        return FXCollections.observableArrayList(recipeDetailedInfo.getFoodEntities().stream()
                .map(FoodEntity::getName)
                .collect(Collectors.toList()));
    }
}