package com.programmazione_avanzata.final_project.chef_ideas.server.dto;

import com.programmazione_avanzata.final_project.chef_ideas.server.entity.FoodEntity;
import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Ingredient;
import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Method;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

public record RecipeFullInfoDto(
        @NotNull
        Long recipeId,
        @NotBlank
        @Length(max = 200)
        String recipeName,
        String link,
        @NotNull
        Set<Ingredient> ingredients,
        @NotNull
        List<Method> methods,
        @NotNull
        Set<FoodEntity> foodEntities) {
}
