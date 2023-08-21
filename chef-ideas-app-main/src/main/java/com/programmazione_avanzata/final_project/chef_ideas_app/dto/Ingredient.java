package com.programmazione_avanzata.final_project.chef_ideas_app.dto;

public class Ingredient {

    private Long ingredientId;
    private String name;

    public Ingredient(){};

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ingredient(Long ingredientId, String ingredientName) {
        this.ingredientId = ingredientId;
        this.name = ingredientName;
    }

    public Ingredient(String ingredientName) {
        this.name = ingredientName;
    }
}
