package com.programmazione_avanzata.final_project.chef_ideas.server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

public record RecipeInputDto(
        @NotBlank
        @Length(max = 200)
        String name,
        String link,
        @NotNull
        Set<String> ingredients,
        @NotNull
        List<String> methods,
        @NotNull
        Set<String> foodEntities) {
}
