/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.programmazione_avanzata.final_project.chef_ideas.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author karolinastoga
 */
@Entity
@NoArgsConstructor
public class Method {

    @Getter
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "method_sequence")
    @SequenceGenerator(
            name = "method_sequence",
            sequenceName = "method_sequence", allocationSize = 1)
    @Column(name = "method_id", unique = true, nullable = false)
    private Long methodId;

    @Getter
    @Setter
    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @Getter
    @Column(name = "step_description", nullable = false)
    private String stepDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public Method(Integer stepNumber, String stepDescription, Recipe recipe) {
        this.stepNumber = stepNumber;
        this.stepDescription = stepDescription;
        this.recipe = recipe;
    }
}
