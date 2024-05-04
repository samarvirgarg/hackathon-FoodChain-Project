package com.knf.dev.model.Skill;

import com.knf.dev.model.Organization.Organization;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "Skills")
// Class to search and filter linked organisation
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    @Column(name = "name", unique = true)
    private String name;


    @ManyToMany(mappedBy = "skills")
    private Set<Organization> organizations = new HashSet<>();


    public Skill() {
    }

    public Skill(String skillName) {

    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.valueOf(skillId);
    }

    public Set<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }
}
