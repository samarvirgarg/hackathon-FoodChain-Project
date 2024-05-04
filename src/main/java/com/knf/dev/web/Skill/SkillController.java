package com.knf.dev.web.Skill;

import com.knf.dev.model.Skill.Skill;
import com.knf.dev.service.Service.Skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/skill")
public class SkillController {

    @Autowired
    private SkillService skillService;


    /**
     * Skill Index
     *
     * @param model
     * @return
     */
    @GetMapping("/")
    public String getAllSkills(Model model) {
        List<Skill> skillList = skillService.getAllSkills();
        model.addAttribute("skills", skillList);
        return "skillHome";
    }

    /**
     * Get Skill By Id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Optional<Skill> getSkillById(@PathVariable Long id) {
        return skillService.getSkillById(id);

    }

    /**
     * Add Skill
     *
     * @param model
     * @return
     */
    @GetMapping("addskill")
    public String addSkill(Model model) {
        Skill skill = new Skill();
        model.addAttribute("skills", skill);
        return "addSkill";
    }


    /**
     * Save Skill
     *
     * @param skill
     * @param model
     * @return
     */
    @PostMapping("/saveSkill")
    public String saveSkill(@ModelAttribute("skills") Skill skill, Model model) {
        try {
            skillService.saveSkill(skill);
            return "redirect:/skill/";
        } catch (DataIntegrityViolationException ex) {
            // Handle the duplicate skill error
            System.out.println("duplicate skill: " + ex);
            model.addAttribute("error", "Duplicate entry for skill: " + skill.getName());
            return "addSkill";
        }
    }


    /**
     * Delete Skill By Id
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public void deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
    }


    /**
     * Search Skill By Name
     *
     * @param search
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String searchSkills(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("skills", skillService.getSkillByKeyword(search));
        } else {
            model.addAttribute("skills", skillService.getAllSkills());
        }
        return "skillHome";
    }

}
