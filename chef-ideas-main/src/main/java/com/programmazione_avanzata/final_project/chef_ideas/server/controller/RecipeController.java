/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmazione_avanzata.final_project.chef_ideas.server.controller;

import com.programmazione_avanzata.final_project.chef_ideas.server.dto.RecipeFullInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas.server.dto.RecipeInfoDto;
import com.programmazione_avanzata.final_project.chef_ideas.server.dto.RecipeInputDto;
import com.programmazione_avanzata.final_project.chef_ideas.server.service.DataInitializationService;
import com.programmazione_avanzata.final_project.chef_ideas.server.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author karolinastoga
 */
@Controller
@RequestMapping("api/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final DataInitializationService dataInitializationService;

    @PostMapping("/{numberOfRecords}")
    public ResponseEntity<Boolean> initializeData(@PathVariable int numberOfRecords) {
        var result = dataInitializationService.initializeData(numberOfRecords);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecipeInfoDto>> getAllRecipes() {
        var result = recipeService.getAllRecipes();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeFullInfoDto> getRecipe(@PathVariable Long recipeId) {
        var result = recipeService.getRecipe(recipeId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{recipeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
    }

    @PostMapping
    public ResponseEntity<RecipeFullInfoDto> createRecipe(@Valid @RequestBody RecipeInputDto recipeInput) {
        var result = recipeService.createRecipe(recipeInput);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/name")
    public ResponseEntity<List<RecipeInfoDto>> getRecipesWithGivenName(@RequestParam String recipeName) {
        var result = recipeService.getRecipeByName(recipeName);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/food-entity")
    public ResponseEntity<List<RecipeInfoDto>> getRecipesWithGivenFoodEntities(@RequestBody List<String> foodEntities) {
        var result = recipeService.getRecipesWithGivenFoodEntities(foodEntities);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<RecipeFullInfoDto> updateRecipe(@RequestParam Long recipeId, @Valid @RequestBody RecipeInputDto recipeInputDto ) {
        var result = recipeService.updateRecipe(recipeId, recipeInputDto);
        return ResponseEntity.ok(result);
    }




}