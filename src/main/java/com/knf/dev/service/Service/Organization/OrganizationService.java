package com.knf.dev.service.Service.Organization;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Skill.Skill;

import java.util.List;
import java.util.Set;


//interface for organization class
public interface OrganizationService {

    List<Organization> getAllOrganization();

    void saveOrganization(Organization organization);

    Organization getOrganizationById(long OrgID);

    void deleteOrganizationById(long OrgID);
    List<Organization> getByKeyword(String keyword);

//    List<Organization> getByKeywordAndSkills(List<String> skills);

    List<Organization> getByOrganizationOrSkills(String search, List<String> skills);


}
