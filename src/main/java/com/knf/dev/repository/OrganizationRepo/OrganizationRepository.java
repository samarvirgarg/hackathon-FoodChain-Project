package com.knf.dev.repository.OrganizationRepo;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Organization.OrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;

import java.util.List;

import java.util.List;

@Repository
// Database Operations for organisation table
public interface OrganizationRepository extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {


    @Query("SELECT DISTINCT o FROM Organization o " +
            "LEFT JOIN o.skills s " +
            "WHERE o.name LIKE %:search% " +
            "    OR o.email LIKE %:search% " +
            "    OR s.name LIKE %:search%")
    List<Organization> findByKeyword(@Param("search") String search);

    @Query("SELECT DISTINCT o FROM Organization o " +
            "LEFT JOIN FETCH o.skills s " +
            "WHERE (:search IS NULL OR " +
            "       o.name LIKE %:search% OR " +
            "       o.email LIKE %:search% OR " +
            "       o.address LIKE %:search% OR " +
            "       o.city LIKE %:search% OR " +
            "       o.state LIKE %:search% OR " +
            "       o.type LIKE %:search% OR " +
            "       (COALESCE(:skills) IS NULL OR s.name IN :skills))")
    List<Organization> findByOrganizationORSkills(@Param("search") String search, @Param("skills") List<String> skills);

    @Query("SELECT DISTINCT o.city FROM Organization o")
    List<String> findAllCities();

    @Query("SELECT DISTINCT o.state FROM Organization o")
    List<String> findAllStates();

}
