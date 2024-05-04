package com.knf.dev.model.Organization;


import com.knf.dev.model.Skill.Skill;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "Organization")
// This class is used to run operations on organisation object and used to search by students. It will be linked to skills
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orgId;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;


    @Column(name = "contact")
    private String contact;

    @Column(name = "about", length = 100000)
    private String about;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private OrganizationType type;


    @ManyToMany(cascade = CascadeType.ALL)
    private List<Skill> skills = new ArrayList<>();

    @Transient
    private String skillsSearch;


    public OrganizationType getType() {
        return type;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Organization() {
        System.out.println("default cons");
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }



    public String getSkillsSearch() {
        return this.skillsSearch;
    }

    public void setSkillsSearch(String skillsSearch) {
        this.skillsSearch = skillsSearch;
    }
}
