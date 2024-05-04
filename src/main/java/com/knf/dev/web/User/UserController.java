package com.knf.dev.web.User;

import com.knf.dev.model.Organization.Organization;
import com.knf.dev.model.Organization.OrganizationType;
import com.knf.dev.model.Role.UserRole;
import com.knf.dev.model.Skill.Skill;
import com.knf.dev.model.User.User;
import com.knf.dev.repository.OrganizationRepo.OrganizationRepository;
import com.knf.dev.repository.SkillRepo.SkillRepository;
import com.knf.dev.repository.UserRepo.UserRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.knf.dev.dto.UserRegistrationDto;
import com.knf.dev.service.Service.User.UserService;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/registration")
public class UserController {

    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;


    @Autowired
    private SkillRepository skillRepository;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }


    @GetMapping
    public String showRegistrationForm() {
        return "addUser";
    }

    /**
     * Register User
     *
     * @param registrationDto
     * @param role
     * @return
     */
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, UserRole role) {
        userService.save(registrationDto, role);
        return "redirect:/users";
    }

    /**
     * Save User
     *
     * @param userDto
     * @return
     */
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserRegistrationDto userDto) {
        userService.saveUser(userDto);
        return "redirect:/users";
    }


    /**
     * Update User By Id
     *
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/update/{userId}")
    public String updateUser(@PathVariable(value = "userId") long userId, Model model) {
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "updateUser";
    }


    /**
     * Delete User By Id
     *
     * @param userId
     * @return
     */
    @GetMapping("/delete/{userId}")
    public String deleteUser(@PathVariable(value = "userId") long userId) {
        this.userService.deleteUserById(userId);
        return "redirect:/users";
    }


    /**
     * Search User By name, email,role
     *
     * @param search
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String searchUsers(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("listusers", userService.getByKeyword(search));
        } else {
            model.addAttribute("listusers", userService.getAll());
        }
        return "userHome";
    }


    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }


    /**
     * Organization Search & Filter Controller
     *
     * @param cities
     * @param states
     * @param types
     * @param skills
     * @param model
     * @return
     */
    @GetMapping("/userFilter")
    public String searchAndFilterOrganizations(@RequestParam(name = "city", required = false) List<String> cities,
                                               @RequestParam(name = "state", required = false) List<String> states,
                                               @RequestParam(name = "type", required = false) List<OrganizationType> types,
                                               @RequestParam(name = "skills", required = false) List<String> skills,
                                               @RequestParam(name = "skillsSearch", required = false) String search,
                                               Model model) {
        System.out.println("inside user filter......................");
        Specification<Organization> spec = (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();
            if (search != null && !search.isEmpty()) {
                // Search based on organization name, address, and contact
                Predicate organizationSearchPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("name"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("address"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("contact"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("type").as(String.class), "%" + search + "%")
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
        return "user_home";
    }


}
