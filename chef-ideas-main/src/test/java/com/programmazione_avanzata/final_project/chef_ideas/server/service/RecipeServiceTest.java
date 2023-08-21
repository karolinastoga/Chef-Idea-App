package com.programmazione_avanzata.final_project.chef_ideas.server.service;

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
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RecipeServiceTest {

    @MockBean
    RecipeRepository recipeRepository;

    @MockBean
    IngredientRepository ingredientRepository;

    @MockBean
    MethodRepository methodRepository;

    @MockBean
    FoodEntityRepository foodEntityRepository;

    @Autowired
    @InjectMocks
    RecipeService recipeService;

    @Captor
    ArgumentCaptor<HashSet<Ingredient>> ingredientCaptor;

    @Captor
    ArgumentCaptor<List<Method>> methodCaptor;


    @Test
    void shouldReturnAllRecipes() {
        //given
        var recipe = new Recipe("testRecipe", "testLink");
        //when
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));
        var actualResult = recipeService.getAllRecipes();
        //then
        assertThat(actualResult.get(0)).satisfies(
                recipeInfoDto -> assertThat(recipeInfoDto.recipeName()).isEqualTo("testRecipe")
        );
    }

    @Test
    void shouldReturnEmptyListWhenRecipeNotFound() {
        //when
        when(recipeRepository.findAll()).thenReturn(Collections.emptyList());
        var actualResult = recipeService.getAllRecipes();
        //then
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void shouldReturnFoundRecipe() {
        //given
        var recipe = new Recipe("testRecipe", "testLink");
        Long recipeId = 1L;
        //when
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        var actualResult = recipeService.getRecipe(recipeId);
        //then
        assertThat(actualResult.recipeName()).isEqualTo("testRecipe");
        assertThat(actualResult.link()).isEqualTo("testLink");
        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    void shouldThrowErrorWhenRecipeNotFound() {
        //given
        Long recipeId = 1L;
        //when
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());
        var exception = assertThrows(ApiRequestException.class,
                () -> recipeService.getRecipe(recipeId));
        //then
        assertThat(exception.getMessage()).contains("There was no recipe found with given id");
    }

    @Test
    void shouldThrowErrorWhenRecipeNotFoundWhileDeleting() {
        //given
        Long recipeId = 1L;
        //when
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());
        var exception = assertThrows(ApiRequestException.class,
                () -> recipeService.deleteRecipe(recipeId));
        //then
        assertThat(exception.getMessage()).contains("There was no recipe found with given id");
    }

    @Test
    void shouldDeleteRecipe() {
        //given
        var recipe = new Recipe("testRecipe", "testLink");
        Long recipeId = 1L;
        //when
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        recipeService.deleteRecipe(recipeId);
        //then
        verify(recipeRepository, times(1)).findById(recipeId);
        verify(recipeRepository, times(1)).delete(recipe);
    }


    @Test
    void shouldCreateRecipeWithNewFoodEntity() {
        //given
        var inputRecipeDto = new RecipeInputDto("createdRecipe", "createdLink", Set.of("ingredient"), List.of("method"), Set.of("foodEntity"));
        var returnRecipe = new Recipe("createdRecipe", "createdLink");
        returnRecipe.setRecipeId(1L);
        returnRecipe.setIngredients(Set.of(new Ingredient("ingredient", returnRecipe)));
        returnRecipe.setMethods(List.of(new Method(1,"method", returnRecipe)));
        returnRecipe.setFoodEntities(Set.of(new FoodEntity("foodEntity")));
        //when
        when(foodEntityRepository.findAllByName("foodEntity")).thenReturn(List.of());
        when(recipeRepository.save(any(Recipe.class))).thenReturn(returnRecipe);
        var actualResult = recipeService.createRecipe(inputRecipeDto);
        //then
        assertThat(actualResult.recipeName()).isEqualTo("createdRecipe");
        assertThat(actualResult.link()).isEqualTo("createdLink");
        assertThat(actualResult.ingredients().iterator().next()).satisfies(
                ingredient -> assertThat(ingredient.getName()).isEqualTo("ingredient")
        );
        assertThat(actualResult.methods().get(0)).satisfies(
                method -> {
                    assertThat(method.getStepNumber()).isEqualTo(1);
                    assertThat(method.getStepDescription()).isEqualTo("method");
                }
        );
        assertThat(actualResult.foodEntities().iterator().next()).satisfies(
                foodEntity -> assertThat(foodEntity.getName()).isEqualTo("foodEntity")
        );
    }

    @Test
    void shouldCreateRecipeWithExistingFoodEntity() {
        //given
        var inputRecipeDto = new RecipeInputDto("createdRecipe", "createdLink", Set.of("ingredient"), List.of("method"), Set.of("foodEntity"));
        var returnRecipe = new Recipe("createdRecipe", "createdLink");
        returnRecipe.setRecipeId(1L);
        returnRecipe.setIngredients(Set.of(new Ingredient("ingredient", returnRecipe)));
        returnRecipe.setMethods(List.of(new Method(1,"method", returnRecipe)));
        returnRecipe.setFoodEntities(Set.of(new FoodEntity("foodEntity")));
        //when
        when(foodEntityRepository.findAllByName("foodEntity")).thenReturn(List.of(new FoodEntity("foodEntity")));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(returnRecipe);
        var actualResult = recipeService.createRecipe(inputRecipeDto);
        //then
        assertThat(actualResult.recipeName()).isEqualTo("createdRecipe");
        assertThat(actualResult.link()).isEqualTo("createdLink");
        assertThat(actualResult.ingredients().iterator().next()).satisfies(
                ingredient -> assertThat(ingredient.getName()).isEqualTo("ingredient")
        );
        assertThat(actualResult.methods().get(0)).satisfies(
                method -> {
                    assertThat(method.getStepNumber()).isEqualTo(1);
                    assertThat(method.getStepDescription()).isEqualTo("method");
                }
        );
        assertThat(actualResult.foodEntities().iterator().next()).satisfies(
                foodEntity -> assertThat(foodEntity.getName()).isEqualTo("foodEntity")
        );
    }

    @Test
    void shouldReturnAllRecipesByName() {
        //given
        String name = "pie";
        var recipe1 = new Recipe("ApplePie", "link");
        var recipe2 = new Recipe("PieBanana", "link");
        //when
        when(recipeRepository.findByNameContainingIgnoreCase(name)).thenReturn(List.of(recipe1, recipe2));
        var result = recipeService.getRecipeByName(name);
        //then
        assertEquals(2, result.size());
        assertThat(result.get(0).recipeName()).isEqualTo("ApplePie");
        assertThat(result.get(1).recipeName()).isEqualTo("PieBanana");
    }

    @Test
    void shouldReturnRecipesWithGivenFoodEntities() {
        //given
        var foodEntities = List.of("milk");
        var foodEntity = new FoodEntity("milk");
        var foodEntity2 = new FoodEntity("sugar");
        var recipe = new Recipe("recipeName", "link");
        recipe.setFoodEntities(Set.of(foodEntity));
        var recipe2 = new Recipe("recipeName2", "link2");
        recipe2.setFoodEntities(Set.of(foodEntity2));
        //when
        when(foodEntityRepository.findAllByNames(foodEntities)).thenReturn(Set.of(foodEntity));
        when(recipeRepository.findAll()).thenReturn(List.of(recipe, recipe2));
        var result = recipeService.getRecipesWithGivenFoodEntities(foodEntities);
        //then
        assertEquals(1, result.size());
        assertThat(result.get(0).recipeName()).isEqualTo("recipeName");
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingFoodEntities() {
        //given
        List<String> foodEntities = List.of("milk");
        //when
        when(foodEntityRepository.findAllByNames(foodEntities)).thenReturn(Collections.emptySet());
        var result = recipeService.getRecipesWithGivenFoodEntities(foodEntities);
        //then
        assertTrue(result.isEmpty());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    void shouldReturnErrorWhenListOfFoodEntitiesIsEmpty() {
        //given
        List<String> foodEntities = List.of();
        //when
        var exception = assertThrows(IllegalArgumentException.class,
                () -> recipeService.getRecipesWithGivenFoodEntities(foodEntities));
        //then
        assertThat(exception.getMessage()).contains("List of food entities cannot be empty or null");
        verify(recipeRepository, never()).findAll();
        verify(foodEntityRepository, never()).findAllByNames(any());
    }

    @Test
    void shouldThrowErrorWhenRecipeIdIsNull() {
        //given
        Long recipeId = null;
        //when
        var exception = assertThrows(IllegalArgumentException.class,
                () -> recipeService.updateRecipe(recipeId, null));
        //then
        assertThat(exception.getMessage()).contains("Recipe id cannot be null");
        verify(recipeRepository, never()).findById(anyLong());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void shouldThrowErrorWhenRecipeNoFound() {
        //given
        Long recipeId = 1L;
        //when
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());
        var exception = assertThrows(ApiRequestException.class,
                () -> recipeService.updateRecipe(recipeId, null));
        //then
        assertThat(exception.getMessage()).contains("There was no recipe found with given id");
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void shouldUpdateWhenThereAreAddedNewElements(){
        //given
        Long recipeId = 1L;
        var recipeInput = new RecipeInputDto("createdRecipe", "createdLink",
                Set.of("ingredient1", "ingredient2"),
                List.of("method1", "method2"),
                Set.of("foodEntity1", "foodEntity2"));
        var recipe = new Recipe("recipe", "link");
        var ingredients = Set.of(new Ingredient("ingredient1", recipe));
        var method = List.of(new Method(1,"method1", recipe));
        var foodEntity = Set.of(new FoodEntity("foodEntity"));
        recipe.setIngredients(ingredients);
        recipe.setMethods(method);
        recipe.setFoodEntities(foodEntity);
        recipe.setRecipeId(recipeId);
        //when
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        var result = recipeService.updateRecipe(recipeId, recipeInput);
        //then
        verify(ingredientRepository).deleteAll(ingredientCaptor.capture());
        var ingredientSet = ingredientCaptor.getValue();
        verify(methodRepository).deleteAll(methodCaptor.capture());
        var methodList = methodCaptor.getValue();
        assertTrue(ingredientSet.isEmpty());
        assertTrue(methodList.isEmpty());
        assertThat(result.recipeName()).isEqualTo("createdRecipe");
        assertThat(result.link()).isEqualTo("createdLink");
        assertEquals(2, result.ingredients().size());
        assertEquals(2, result.methods().size());
        assertEquals(2, result.foodEntities().size());
    }

    @Test
    void shouldUpdateWhenThereAreDeletedNewElements(){
        //given
        Long recipeId = 1L;
        var recipeInput = new RecipeInputDto("createdRecipe", "createdLink",
                Set.of("ingredient2"),
                List.of("method2"),
                Set.of("foodEntity2"));
        var recipe = new Recipe("recipe", "link");
        var ingredients = Set.of(new Ingredient("ingredient1", recipe));
        var method = List.of(new Method(1,"method1", recipe));
        var foodEntity = Set.of(new FoodEntity("foodEntity"));
        recipe.setIngredients(ingredients);
        recipe.setMethods(method);
        recipe.setFoodEntities(foodEntity);
        recipe.setRecipeId(recipeId);
        //when
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        var result = recipeService.updateRecipe(recipeId, recipeInput);
        //then
        verify(ingredientRepository).deleteAll(ingredientCaptor.capture());
        var ingredientSet = ingredientCaptor.getValue();
        verify(methodRepository).deleteAll(methodCaptor.capture());
        var methodList = methodCaptor.getValue();
        assertFalse(ingredientSet.isEmpty());
        assertFalse(methodList.isEmpty());
        assertThat(result.recipeName()).isEqualTo("createdRecipe");
        assertThat(result.link()).isEqualTo("createdLink");
        assertEquals(1, result.ingredients().size());
        assertEquals(1, result.methods().size());
        assertEquals(1, result.foodEntities().size());
    }
}