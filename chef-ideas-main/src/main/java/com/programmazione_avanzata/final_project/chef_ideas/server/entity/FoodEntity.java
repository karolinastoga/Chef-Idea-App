/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmazione_avanzata.final_project.chef_ideas.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
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
public class FoodEntity {

    @Getter
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "food_entity_sequence")
    @SequenceGenerator(
            name = "food_entity_sequence",
            sequenceName = "food_entity_sequence", allocationSize = 1)
    @Column(name = "food_entity_id", unique = true, nullable = false)
    private Long foodEntityId;

    @Getter
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Setter
    @Getter
    @ManyToMany(mappedBy = "foodEntities", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Recipe> recipes = new ArrayList<>();

    public FoodEntity(String name) {
        this.name = name;
    }

    public FoodEntity(String name, List<Recipe> recipes) {
        this.name = name;
        this.recipes = recipes;
    }
}