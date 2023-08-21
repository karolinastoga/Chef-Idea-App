/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmazione_avanzata.final_project.chef_ideas.server.service;

import com.programmazione_avanzata.final_project.chef_ideas.server.dto.RecipeFullInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas.server.dto.RecipeInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas.server.dto.RecipeInputDto;
import com.programmazione_avanzata.final_project.chef_ideas.server.entity.FoodEntity;
import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Ingredient;
import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Method;
import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Recipe;
import com.programmazione_avanzata.final_project.chef_ideas.server.exception.ApiRequestException;
import com.programmazione_avanzata.final_project.chef_ideas.server.repository.FoodEntityRepository;
import com.programmazione_avanzata.final_project.chef_ideas.server.repository.IngredientRepository;
import com.programmazione_avanzata.final_project.chef_ideas.server.repository.MethodRepository;
import com.programmazione_avanzata.final_project.chef_ideas.server.repository.RecipeRepository;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.programmazione_avanzata.final_project.chef_ideas.server.exception.ExceptionInfo.*;

/**
 * @author karolinastoga
 */
@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MethodRepository methodRepository;
    private final FoodEntityRepository foodEntityRepository;

    public List<RecipeInfoDto> getAllRecipes() {
        var recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(recipe -> new RecipeInfoDto(recipe.getRecipeId(), recipe.getName()))
                .toList();
    }

    public RecipeFullInfoDto getRecipe(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .map(recipe -> new RecipeFullInfoDto(recipe.getRecipeId(), recipe.getName(), recipe.getLink(), recipe.getIngredients(), recipe.getMethods(), recipe.getFoodEntities()))
                .orElseThrow(() -> new ApiRequestException(RECIPE_NOT_FOUND));
    }

    @Transactional
    public void deleteRecipe(Long recipeId) {
        var recipeToBeDeleted = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiRequestException(RECIPE_NOT_FOUND));
        recipeRepository.delete(recipeToBeDeleted);
    }

    @Transactional
    public RecipeFullInfoDto createRecipe(RecipeInputDto recipeInput) {
        var recipe = createRecipeFromDto(recipeInput);
        return createRecipeFullInfoDto(recipe);
    }

    private Recipe createRecipeFromDto(RecipeInputDto recipeInput) {
        var recipe = new Recipe(recipeInput.name(), recipeInput.link());
        var ingredients = recipeInput.ingredients().stream()
                .map(ingredient -> new Ingredient(ingredient, recipe))
                .collect(Collectors.toSet());
        AtomicInteger stepNumber = new AtomicInteger(1);
        var methods = recipeInput.methods().stream()
                .map(method -> new Method(stepNumber.getAndIncrement(), method, recipe))
                .toList();
        var foodEntities = recipeInput.foodEntities().stream()
                .map(foodEntity -> handleFoodEntity(foodEntity, recipe))
                .collect(Collectors.toSet());
        recipe.setIngredients(ingredients);
        recipe.setMethods(methods);
        recipe.setFoodEntities(foodEntities);
        foodEntityRepository.saveAll(foodEntities);
        return recipeRepository.save(recipe);
    }

    private FoodEntity handleFoodEntity(String foodEntity, Recipe recipe) {
        var existingFoodEntities = foodEntityRepository.findAllByName(foodEntity);
        if (existingFoodEntities.isEmpty()) {
            return new FoodEntity(foodEntity, List.of(recipe));
        }
        else {
            var firstFoodEntity = existingFoodEntities.get(0);
            var currentRecipes = new ArrayList<>(firstFoodEntity.getRecipes());
            currentRecipes.add(recipe);
            firstFoodEntity.setRecipes(currentRecipes);
            return firstFoodEntity;
        }
    }

    public List<RecipeInfoDto> getRecipeByName(String recipeName) {
        var recipes = recipeRepository.findByNameContainingIgnoreCase(recipeName);
        return recipes.stream()
                .map(this::createRecipeInfoDto)
                .toList();
    }

    public List<RecipeInfoDto> getRecipesWithGivenFoodEntities(List<String> foodEntities) {
        if(foodEntities == null || foodEntities.isEmpty()) {
            throw new IllegalArgumentException(ILLEGAL_FOOD_ENTITY_LIST);
        }
        var foodEntitySet = foodEntityRepository.findAllByNames(foodEntities);
        if(foodEntitySet.isEmpty()) {
            return Collections.emptyList();
        }
        var allRecipes = recipeRepository.findAll();
        var matchingRecipes = allRecipes.stream()
                .filter(recipe -> recipe.getFoodEntities().containsAll(foodEntitySet))
                .toList();
        return matchingRecipes.stream()
                .map(this::createRecipeInfoDto)
                .toList();
    }

    private RecipeFullInfoDto createRecipeFullInfoDto(Recipe recipe) {
        return new RecipeFullInfoDto(recipe.getRecipeId(), recipe.getName(), recipe.getLink(), recipe.getIngredients(),
                recipe.getMethods(), recipe.getFoodEntities());
    }

    private RecipeInfoDto createRecipeInfoDto(Recipe recipe) {
        return new RecipeInfoDto(recipe.getRecipeId(), recipe.getName());
    }

    @Transactional
    public RecipeFullInfoDto updateRecipe(Long recipeId, RecipeInputDto recipeInputDto) {
        if(recipeId == null) {
            throw new IllegalArgumentException(ILLEGAL_RECIPE_ID);
        }
        try {
            var recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ApiRequestException(RECIPE_NOT_FOUND));
            recipe.setName(recipeInputDto.name());
            recipe.setLink(recipeInputDto.link());
            recipe.setIngredients(updatedIngredients(recipeInputDto.ingredients(), recipe.getIngredients(), recipe));
            recipe.setMethods(updatedMethods(recipeInputDto.methods(), recipe.getMethods(), recipe));
            recipe.setFoodEntities(updatedFoodEntities(recipeInputDto.foodEntities(), recipe.getFoodEntities(), recipe));
            var savedRecipe = recipeRepository.save(recipe);
            return createRecipeFullInfoDto(savedRecipe);
        }
        catch (Exception e){
            throw new ApiRequestException(e.getMessage());
        }
    }

    private Set<Ingredient> updatedIngredients(Set<String> incomingIngredients, Set<Ingredient> ingredientsInDb, Recipe recipe) {
        Set<Ingredient> ingredientSet = new HashSet<>();
        Map<String, Ingredient> ingredientMap = ingredientsInDb.stream().collect(
                Collectors.toMap(Ingredient::getName, Function.identity()));

        for (String ingredient : incomingIngredients) {
            if(ingredientMap.containsKey(ingredient)) {
                ingredientSet.add(ingredientMap.get(ingredient));
            }
            else {
                ingredientSet.add(new Ingredient(ingredient, recipe));
            }
        }
        checkIfAnyIngredientsToDeletion(ingredientsInDb, ingredientSet);
        return ingredientSet;
    }

    private void checkIfAnyIngredientsToDeletion(Set<Ingredient> ingredientsInDb, Set<Ingredient> ingredientsToBeSaved) {
        var copyIngredientsInDb = new HashSet<>(ingredientsInDb);
        copyIngredientsInDb.removeAll(ingredientsToBeSaved);
        ingredientRepository.deleteAll(copyIngredientsInDb);
    }

    private void checkIfAnyMethodsToDeletion(List<Method> methodsInDb, List<Method> methodsToBeSaved) {
        var copyMethodsInDb = new ArrayList<>(methodsInDb);
        copyMethodsInDb.removeAll(methodsToBeSaved);
        methodRepository.deleteAll(copyMethodsInDb);
    }

    private List<Method> updatedMethods(List<String> incomingMethods, List<Method> methodInDb, Recipe recipe) {
        List<Method> methodList = new ArrayList<>();
        Map<String, Method> methodMap = methodInDb.stream().collect(
                Collectors.toMap(Method::getStepDescription, Function.identity()));

        int stepNumber = 0;
        for (String method : incomingMethods) {
            stepNumber++;
            if(methodMap.containsKey(method)) {
                var methodToEditStep = methodMap.get(method);
                methodToEditStep.setStepNumber(stepNumber);
                methodList.add(methodToEditStep);
            }
            else {
                methodList.add(new Method(stepNumber,method, recipe));
            }
        }
        checkIfAnyMethodsToDeletion(methodInDb, methodList);
        return methodList;
    }

    private Set<FoodEntity> updatedFoodEntities(Set<String> incomingFoodEntities, Set<FoodEntity> foodEntityInDb, Recipe recipe) {
        Set<FoodEntity> foodEntitySet = new HashSet<>();
        Map<String, FoodEntity> foodEntityMap = foodEntityInDb.stream().collect(
                Collectors.toMap(FoodEntity::getName, Function.identity()));

        for (String foodEntity : incomingFoodEntities) {
            var allFoodEntities = foodEntityRepository.findAllByName(foodEntity);
            if(foodEntityMap.containsKey(foodEntity)) {
                foodEntitySet.add(foodEntityMap.get(foodEntity));
            }
            else if (!allFoodEntities.isEmpty()) {
                foodEntitySet.add(allFoodEntities.get(0));
            }
            else {
                var newFoodEntity = new FoodEntity(foodEntity, List.of(recipe));
                foodEntitySet.add(newFoodEntity);
            }
        }
        return foodEntitySet;
    }
}
