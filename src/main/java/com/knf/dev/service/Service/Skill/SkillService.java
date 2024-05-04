package com.knf.dev.service.Service.Skill;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Skill.Skill;

import java.util.List;
import java.util.Optional;

// //interface for skill class
public interface SkillService {

    List<Skill> getAllSkills();

    public List<Skill> getSelectedSkillsForOrganization(Organization organization);

    Optional<Skill> getSkillById(Long id);

    void saveSkill(Skill skill);

    Skill createOrUpdateSkill(Skill user_skill);

    void deleteSkill(Long id);

    public Optional<Skill> findByName(String name);


    List<Skill> getSkillByKeyword(String search);


}
