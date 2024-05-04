package com.knf.dev.web.Organization;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Organization.OrganizationType;
import com.knf.dev.model.Skill.Skill;
import com.knf.dev.repository.OrganizationRepo.OrganizationRepository;
import com.knf.dev.repository.SkillRepo.SkillRepository;
import com.knf.dev.service.Service.Organization.OrganizationService;
import com.knf.dev.service.Service.Skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import java.util.*;

@Controller
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private SkillRepository skillRepository;


    @Autowired
    private SkillService skillService;


    /**
     * Organization Search & Filter Controller
     *
     * @param cities
     * @param states
     * @param types
     * @param skills
     * @param skillsSearch
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/")
    public String searchAndFilterOrganizations(@RequestParam(name = "city", required = false) List<String> cities,
                                               @RequestParam(name = "state", required = false) List<String> states,
                                               @RequestParam(name = "type", required = false) List<OrganizationType> types,
                                               @RequestParam(name = "skills", required = false) List<String> skills,
                                               @RequestParam(name = "skillsSearch", required = false) String skillsSearch,
                                               Model model,
                                               Authentication authentication) {
        Specification<Organization> spec = (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();
            if (skillsSearch != null && !skillsSearch.isEmpty()) {
                // Search based on organization name, address, and contact
                Predicate organizationSearchPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("name"), "%" + skillsSearch + "%"),
                        criteriaBuilder.like(root.get("address"), "%" + skillsSearch + "%"),
                        criteriaBuilder.like(root.get("contact"), "%" + skillsSearch + "%"),
                        criteriaBuilder.like(root.get("type").as(String.class), "%" + skillsSearch + "%")
                );
                predicate = criteriaBuilder.and(predicate, organizationSearchPredicate);
            }
            if (cities != null && !cities.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("city").in(cities));
            }
            if (states != null && !states.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("state").in(states));
            }
            if (types != null && !types.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("type").in(types));
            }
            if (skills != null && !skills.isEmpty()) {
                Join<Organization, Skill> skillJoin = root.join("skills");
                List<Predicate> skillPredicates = new ArrayList<>();
                for (String skill : skills) {
                    skillPredicates.add(criteriaBuilder.equal(skillJoin.get("name"), skill));
                }
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(skillPredicates.toArray(new Predicate[0])));
            }
            return predicate;
        };
        List<Organization> organizations = organizationRepository.findAll(spec);
        model.addAttribute("organizations", organizations);
        model.addAttribute("cities", organizationRepository.findAllCities());
        model.addAttribute("states", organizationRepository.findAllStates());
        model.addAttribute("types", Arrays.asList(OrganizationType.values()));
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute("selectedCities", cities != null ? cities : new ArrayList<String>());
        model.addAttribute("selectedStates", states != null ? states : new ArrayList<String>());
        model.addAttribute("selectedTypes", types != null ? types : new ArrayList<OrganizationType>());
        model.addAttribute("selectedSkills", skills != null ? skills : new ArrayList<String>());
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
                return "organizationHome";
            }
        }
        return "userHome";
    }


    /**
     * Create Organization Controller
     *
     * @param model
     * @return
     */
    @GetMapping("/showNewOrganizationForm")
    public String createOrganization(Model model) {
        List<Skill> skills = skillService.getAllSkills();
        model.addAttribute("organization", new Organization());
        model.addAttribute("allSkills", skillService.getAllSkills());
        return "addOrganization";
    }


    /**
     * Save Organization Controller
     * Method to create new organization in database
     * @param organization
     * @param model
     * @return
     */
    @PostMapping("/saveOrganization")
    public String saveOrganization(@ModelAttribute("organization") Organization organization, Model model) {
            organizationService.saveOrganization(organization);
            return "redirect:/organization/";
    }


    /**
     * Update Organization Controller
     *
     * @param OrgID
     * @param skilId
     * @param model
     * @return
     */
    @GetMapping("/showFormForUpdate/{OrgID}")
    public String updateOrganization(@PathVariable(value = "OrgID") long OrgID,
                                     @RequestParam(name = "skilId", required = false) Long skilId,
                                     Model model) {
        Organization organization = organizationService.getOrganizationById(OrgID);
        List<Skill> selectedSkills = skillService.getSelectedSkillsForOrganization(organization);
        List<Skill> skills = skillService.getAllSkills();
        model.addAttribute("organization", organization);
        model.addAttribute("skills", skills);
        model.addAttribute("selectedSkillIds", selectedSkills);
        model.addAttribute("skilId", skilId);
        return "updateOrganization";
    }


    /**
     * Delete Organization Controller
     *
     * @param OrgID
     * @return
     */
    @GetMapping("/deleteOrganization/{OrgID}")
    public String deleteOrganization(@PathVariable(value = "OrgID") long OrgID) {
        this.organizationService.deleteOrganizationById(OrgID);
        return "redirect:/organization/";
    }

    @ModelAttribute("organizationTypes")
    public OrganizationType[] organizationTypes() {
        return OrganizationType.values();
    }


    /**
     * Organization Search By Skills OR organization Controller
     *
     * @param organization
     * @param model
     * @param authentication
     * @return
     */
    @RequestMapping(path = {"/Search-org"})
    public String searchByOrganizationOrSkills(@ModelAttribute("organizationSearch") Organization organization,
                                               Model model,
                                               Authentication authentication) {
        String skillsSearch = organization.getSkillsSearch();
        if (skillsSearch != null) {
            model.addAttribute("listOrganization", organizationService.getByOrganizationOrSkills(skillsSearch, Arrays.asList(skillsSearch.split(","))));
        } else {
            model.addAttribute("userlist", organizationService.getAllOrganization());
        }

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
                return "org_home";
            }
        }
        return "user_home";
    }

    /**
     * Organization Filter Controller
     *
     * @param cities
     * @param states
     * @param types
     * @param skills
     * @param model
     * @return
     */
    @GetMapping("/filter")
    public String getOrganizations(@RequestParam(name = "city", required = false) List<String> cities,
                                   @RequestParam(name = "state", required = false) List<String> states,
                                   @RequestParam(name = "type", required = false) List<OrganizationType> types,
                                   @RequestParam(name = "skills", required = false) List<String> skills,
                                   Model model) {
        Specification<Organization> spec = (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();
            if (cities != null && !cities.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("city").in(cities));
            }
            if (states != null && !states.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("state").in(states));
            }
            if (types != null && !types.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("type").in(types));
            }
            if (skills != null && !skills.isEmpty()) {
                Join<Organization, Skill> skillJoin = root.join("skills");
                List<Predicate> skillPredicates = new ArrayList<>();
                for (String skill : skills) {
                    skillPredicates.add(criteriaBuilder.equal(skillJoin.get("name"), skill));
                }
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(skillPredicates.toArray(new Predicate[0])));
            }
            return predicate;
        };
        List<Organization> organizations = organizationRepository.findAll(spec);
        model.addAttribute("organizations", organizations);
        model.addAttribute("cities", organizationRepository.findAllCities());
        model.addAttribute("states", organizationRepository.findAllStates());
        model.addAttribute("types", Arrays.asList(OrganizationType.values()));
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute("selectedCities", cities != null ? cities : new ArrayList<String>());
        model.addAttribute("selectedStates", states != null ? states : new ArrayList<String>());
        model.addAttribute("selectedTypes", types != null ? types : new ArrayList<OrganizationType>());
        model.addAttribute("selectedSkills", skills != null ? skills : new ArrayList<String>());
        return "organizations_filter";
    }


}
