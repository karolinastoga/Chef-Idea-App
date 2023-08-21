package com.programmazione_avanzata.final_project.chef_ideas.server.repository;

import com.programmazione_avanzata.final_project.chef_ideas.server.entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author karolinastoga
 */
@Repository
public interface FoodEntityRepository extends JpaRepository<FoodEntity, Long> {

    List<FoodEntity> findAllByName(String name);

    @Query("SELECT f FROM FoodEntity f WHERE f.name IN ?1")
    Set<FoodEntity> findAllByNames(List<String> names);



}



