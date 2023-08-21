package com.programmazione_avanzata.final_project.chef_ideas_app.dto;

public class RecipeInfoDto {

    private Long recipeId;
    private String recipeName;

    public RecipeInfoDto() {
        super();
    }

    public RecipeInfoDto(Long recipeId, String recipeName) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
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
}
