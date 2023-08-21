package com.programmazione_avanzata.final_project.chef_ideas_app.dto;

import java.util.List;
import java.util.Set;

public class RecipeInputDto {

        String name;
        String link;
        Set<String> ingredients;
        List<String> methods;
        Set<String> foodEntities;

        public RecipeInputDto(String name, String link, Set<String> ingredients, List<String> methods, Set<String> foodEntities) {
                this.name = name;
                this.link = link;
                this.ingredients = ingredients;
                this.methods = methods;
                this.foodEntities = foodEntities;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getLink() {
                return link;
        }

        public void setLink(String link) {
                this.link = link;
        }

        public Set<String> getIngredients() {
                return ingredients;
        }

        public void setIngredients(Set<String> ingredients) {
                this.ingredients = ingredients;
        }

        public List<String> getMethods() {
                return methods;
        }

        public void setMethods(List<String> methods) {
                this.methods = methods;
        }

        public Set<String> getFoodEntities() {
                return foodEntities;
        }

        public void setFoodEntities(Set<String> foodEntities) {
                this.foodEntities = foodEntities;
        }
}