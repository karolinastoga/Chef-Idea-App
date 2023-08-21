package com.programmazione_avanzata.final_project.chef_ideas_app.dto;

public class FoodEntity {

    private Long foodEntityId;
    private String name;

    public FoodEntity() {};

    public FoodEntity(Long foodEntityId, String foodEntityName) {
        this.foodEntityId = foodEntityId;
        this.name = foodEntityName;
    }

    public FoodEntity(String foodEntityName) {
    }

    public Long getFoodEntityId() {
        return foodEntityId;
    }

    public void setFoodEntityId(Long foodEntityId) {
        this.foodEntityId = foodEntityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
