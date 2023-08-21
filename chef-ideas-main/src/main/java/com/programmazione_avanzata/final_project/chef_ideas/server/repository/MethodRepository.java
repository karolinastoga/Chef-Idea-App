package com.programmazione_avanzata.final_project.chef_ideas.server.repository;

import com.programmazione_avanzata.final_project.chef_ideas.server.entity.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodRepository extends JpaRepository<Method, Long> {
}
