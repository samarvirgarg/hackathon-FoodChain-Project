package com.knf.dev.repository.SkillRepo;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// Database Operations for skill table
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(String name);

    @Query("SELECT DISTINCT s FROM Skill s JOIN s.organizations o WHERE o = :organization")
    List<Skill> findAllAndSelected(@Param("organization") Organization organization);


    @Query("SELECT s FROM Skill s " +
            "WHERE s.name LIKE %:search%")
    List<Skill> findByKeyword(@Param("search") String search);


    @Query(value = "SELECT * FROM skills ORDER BY skill_id Asc", nativeQuery = true)
    List<Skill> getskill();


}
