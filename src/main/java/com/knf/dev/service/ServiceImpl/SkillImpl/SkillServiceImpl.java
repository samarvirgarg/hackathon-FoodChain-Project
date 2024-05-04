package com.knf.dev.service.ServiceImpl.SkillImpl;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Skill.Skill;
import com.knf.dev.repository.SkillRepo.SkillRepository;
import com.knf.dev.service.Service.Skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class SkillServiceImpl implements SkillService {


    @Autowired
    private SkillRepository skillRepository;

    // Method to get all skills from the database
    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.getskill();
    }


    // Method to get selected skill for organisation
    @Override
    public List<Skill> getSelectedSkillsForOrganization(Organization organization) {
        return skillRepository.findAllAndSelected(organization);
    }


    // Method to get a particular skill by its ID
    @Override
    public Optional<Skill> getSkillById(Long id) {
        return skillRepository.findById(id);
    }


    // Method to save skill into the database
    @Override
    public void saveSkill(Skill skill) {
        this.skillRepository.save(skill);
    }


    // Method to update skill in the database
    @Override
    public Skill createOrUpdateSkill(Skill user_skill) {
        return skillRepository.save(user_skill);
    }


    // Method to delete skill from the database
    @Override
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }


    // Method to find skill from the database during search by its name
    @Override
    public Optional<Skill> findByName(String name) {
        return skillRepository.findByName(name);
    }


    // Method to get skill by keyword during search
    @Override
    public List<Skill> getSkillByKeyword(String search) {
        return skillRepository.findByKeyword(search);
    }
}
