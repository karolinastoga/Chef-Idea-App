/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmazione_avanzata.final_project.chef_ideas.server.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author karolinastoga
 */
@Entity
@NoArgsConstructor
@Getter
public class Recipe {

    @Setter
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recipe_sequence")
    @SequenceGenerator(
            name = "recipe_sequence",
            sequenceName = "recipe_sequence", allocationSize = 1)
    @Column(name = "recipe_id", unique = true, nullable = false)
    private Long recipeId;

    @Setter
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Setter
    @Column(name = "link")
    private String link;

    @Setter
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private Set<Ingredient> ingredients;

    @Setter
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Method> methods;

    @Setter
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(
            name = "food_entity_recognizer",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "food_enity_id"))
    @JsonManagedReference
    private Set<FoodEntity> foodEntities = new HashSet<>();

    public Recipe(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public Recipe(String name, String link, Set<Ingredient> ingredients, List<Method> methods, Set<FoodEntity> foodEntities) {
        this.name = name;
        this.link = link;
        this.ingredients = ingredients;
        this.methods = methods;
        this.foodEntities = foodEntities;
    }
}