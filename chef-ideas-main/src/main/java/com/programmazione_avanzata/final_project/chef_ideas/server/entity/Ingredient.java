/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmazione_avanzata.final_project.chef_ideas.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author karolinastoga
 */
@Entity
@NoArgsConstructor
public class Ingredient {

    @Getter
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ingredient_sequence")
    @SequenceGenerator(
            name = "ingredient_sequence",
            sequenceName = "ingredient_sequence", allocationSize = 1)
    @Column(name = "ingredient_id", unique = true, nullable = false)
    private Long ingredientId;

    @Getter
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public Ingredient(String name, Recipe recipe) {
        this.name = name;
        this.recipe = recipe;
    }
}