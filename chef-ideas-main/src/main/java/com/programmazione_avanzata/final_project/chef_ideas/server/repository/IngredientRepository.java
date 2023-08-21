package com.programmazione_avanzata.final_project.chef_ideas.server.repository;

import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
