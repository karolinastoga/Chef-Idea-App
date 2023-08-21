package com.programmazione_avanzata.final_project.chef_ideas.server.service;

import com.programmazione_avanzata.final_project.chef_ideas.server.entity.FoodEntity;
import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Ingredient;
import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Method;
import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Recipe;
import com.programmazione_avanzata.final_project.chef_ideas.server.exception.ApiRequestException;
import com.programmazione_avanzata.final_project.chef_ideas.server.repository.FoodEntityRepository;
import com.programmazione_avanzata.final_project.chef_ideas.server.repository.RecipeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.programmazione_avanzata.final_project.chef_ideas.server.exception.ExceptionInfo.*;

@Component
@RequiredArgsConstructor
public class DataInitializationService {

    private final FoodEntityRepository foodEntityRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public Boolean initializeData(int numberOfRecords) {
        Path pathToDatasetFile = Paths.get("src/main/resources/dataset/RecipeNLG_dataset-1.csv");
        try{
            List<Recipe> recipes = new ArrayList<>();
            Map<FoodEntity, List<Recipe>> foodEntityRecipes = new HashMap<>();
            BufferedReader br = Files.newBufferedReader(pathToDatasetFile, StandardCharsets.US_ASCII);
            cleanRepositories();
            mapCSVFileToRecipes(numberOfRecords, br, recipes, foodEntityRecipes);
            setRecipesToFoodEntity(foodEntityRecipes);
            saveRepositories(recipes, foodEntityRecipes);
        }catch (IOException ioe) {
            throw new ApiRequestException(ERROR_WHILE_FETCHING_DATA);
        }
        return true;
    }

    private void mapCSVFileToRecipes(int numberOfRecords, BufferedReader br, List<Recipe> recipes, Map<FoodEntity, List<Recipe>> foodEntityRecipes){
        try {
            var line = getString(br);
            int rows = 0;
            while (line != null && rows < numberOfRecords - 1) {
                int columnNumber = 0;
                var regex = "\"(?:[^\"]|\"\")*\"";
                var matcher = getMatcher(regex, line);
                var recipe = getRecipe(line, regex);
                while (matcher.find()) {
                    var matches = matcher.group();
                    setMatchedValuesToRecipe(foodEntityRecipes, columnNumber, recipe, matches);
                    columnNumber++;
                }
                recipes.add(recipe);
                line = br.readLine();
                rows++;
            }
        }
        catch (IOException ioe){
            throw new ApiRequestException(ERROR_WHILE_MAPPING);
        }
    }

    private void setRecipesToFoodEntity(Map<FoodEntity, List<Recipe>> foodEntityRecipes) {
        for (var foodEntityRecipe : foodEntityRecipes.keySet()) {
            foodEntityRecipe.setRecipes(foodEntityRecipes.get(foodEntityRecipe));
        }
    }

    private void setMatchedValuesToRecipe(Map<FoodEntity, List<Recipe>> foodEntityRecipes, int columnNumber, Recipe recipe, String matches) {
        if (columnNumber == 0) {
            var ingredients = createIngredientsSet(matches, recipe);
            recipe.setIngredients(ingredients);
        }
        if (columnNumber == 1) {
            var methods = createMethodsList(matches, recipe);
            recipe.setMethods(methods);
        }
        if (columnNumber == 2) {
            var foodEntities = createFoodEntitiesSet(matches, recipe, foodEntityRecipes);
            recipe.setFoodEntities(foodEntities);
        }
    }

    private Recipe getRecipe(String line, String regex) {
        var recipeString = line.replaceAll(regex, "");
        return createRecipe(recipeString.split(","));
    }

    public Recipe createRecipe(String[] attributes) {
        return new Recipe(attributes[1], attributes[4]);
    }

    public Set<Ingredient> createIngredientsSet(String match, Recipe recipe) {
        Set<Ingredient> ingredients = new HashSet<>();
        String regex = "\"\"[^\"]*\"\"";
        Matcher matcher = getMatcher(regex, match);
        while (matcher.find()) {
            var ingredient = matcher.group();
            ingredient = ingredient.replace("\"", "");
            ingredients.add(new Ingredient(ingredient, recipe));
        }
        return ingredients;
    }

    private List<Method> createMethodsList(String match, Recipe recipe) {
        List<Method> methods = new ArrayList<>();
        String regex = "\"\"[^\"]*\"\"";
        Matcher matcher = getMatcher(regex, match);
        int stepNumber = 1;
        while (matcher.find()) {
            var step = matcher.group();
            step = step.replace("\"", "");
            if (!step.contains(".")) {
                methods.add(new Method(stepNumber++, step, recipe));
            } else {
                var steps = step.split("\\.");
                for(var s: steps){
                    methods.add(new Method(stepNumber++, s, recipe));                }
            }
        }
        return methods;
    }

    private Set<FoodEntity> createFoodEntitiesSet(String match, Recipe recipe, Map<FoodEntity, List<Recipe>> foodEntityRecipes) {
        Set<FoodEntity> recipeFoodEntities = new HashSet<>();
        String regex = "\"\"[^\"]*\"\"";
        Matcher matcher = getMatcher(regex, match);
        while (matcher.find()) {
            var foodEntityName = matcher.group();
            foodEntityName = foodEntityName.replace("\"", "");
            var foodEntity = checkIfFoodEntityAlreadyExist(foodEntityName, foodEntityRecipes);
            boolean isNotInDatabase = foodEntityRepository.findAllByName(foodEntityName).isEmpty();
            if (foodEntityRecipes.get(foodEntity) == null && isNotInDatabase) {
                foodEntityRecipes.put(foodEntity, List.of(recipe));
            } else {
                List<Recipe> recipesList;
                if(isNotInDatabase){
                    recipesList = foodEntityRecipes.get(foodEntity);
                }
                else{
                    recipesList = foodEntity.getRecipes();
                }
                var recipesListCopy = new ArrayList<>(recipesList);
                recipesListCopy.add(recipe);
                foodEntityRecipes.put(foodEntity, recipesListCopy);
            }
            recipeFoodEntities.add(foodEntity);
        }
        return recipeFoodEntities;
    }

    private FoodEntity checkIfFoodEntityAlreadyExist(String foodEntityName, Map<FoodEntity, List<Recipe>> foodEntityRecipes) {
        for (var foodEntityRecipe : foodEntityRecipes.keySet()) {
            if (foodEntityRecipe.getName().equals(foodEntityName)) {
                return foodEntityRecipe;
            }
        }
        var existingFoodEntity = foodEntityRepository.findAllByName(foodEntityName);
        if(existingFoodEntity.isEmpty()) {
            return new FoodEntity(foodEntityName);
        }
        return existingFoodEntity.get(0);
    }

    private void saveRepositories(List<Recipe> recipes, Map<FoodEntity, List<Recipe>> foodEntityRecipes) {
        foodEntityRepository.saveAll(foodEntityRecipes.keySet());
        recipeRepository.saveAll(recipes);
    }

    private void cleanRepositories() {
        foodEntityRepository.deleteAll();
        recipeRepository.deleteAll();
    }


    private Matcher getMatcher(String regex, String line) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(line);
    }

    private String getString(BufferedReader br) throws IOException {
        br.readLine();
        return br.readLine();
    }
}
