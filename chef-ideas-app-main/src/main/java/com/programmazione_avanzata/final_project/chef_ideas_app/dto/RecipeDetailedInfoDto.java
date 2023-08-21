package com.programmazione_avanzata.final_project.chef_ideas_app.dto;

import java.util.List;
import java.util.Set;

public class RecipeDetailedInfoDto {

    private Long recipeId;
    private String recipeName;
    private String link;
    private Set<Ingredient> ingredients;
    private List<Method> methods;
    private Set<FoodEntity> foodEntities;

    public Set<FoodEntity> getFoodEntities() {
        return foodEntities;
    }

    public void setFoodEntities(Set<FoodEntity> foodEntities) {
        this.foodEntities = foodEntities;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public Set<FoodEntity> getFoodEntity() {
        return foodEntity;
    }

    public void setFoodEntity(Set<FoodEntity> foodEntity) {
        this.foodEntity = foodEntity;
    }

    public RecipeDetailedInfoDto() {
    }

    public RecipeDetailedInfoDto(Long recipeId, String recipeName, String link, Set<Ingredient> ingredients, List<Method> methods, Set<FoodEntity> foodEntity) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.link = link;
        this.ingredients = ingredients;
        this.methods = methods;
        this.foodEntity = foodEntity;
    }

    public RecipeDetailedInfoDto(String recipeName, String link, Set<Ingredient> ingredients, List<Method> methods, Set<FoodEntity> foodEntity) {
        this.recipeName = recipeName;
        this.link = link;
        this.ingredients = ingredients;
        this.methods = methods;
        this.foodEntity = foodEntity;
    }

    private Set<FoodEntity> foodEntity;
}
