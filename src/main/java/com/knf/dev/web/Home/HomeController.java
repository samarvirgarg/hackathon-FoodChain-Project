package com.knf.dev.web.Home;

import com.knf.dev.model.Role.UserRole;
import com.knf.dev.model.Skill.Skill;
import com.knf.dev.model.User.User;
import com.knf.dev.repository.UserRepo.UserRepository;
import com.knf.dev.service.Service.Skill.SkillService;
import com.knf.dev.service.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    SkillService skillService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Redirect User & Admin
     *
     * @param authentication
     * @return
     */
    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/organization/";
            }
        }
        return "user_home";
    }

    /**
     * Default Get All Skills
     *
     * @return
     */
    @GetMapping("/all")
    @ResponseBody
    @CrossOrigin
    public List<String> getAllSkills() {
        List<Skill> skillList = skillService.getAllSkills();
        return skillList.stream().map(Skill::getName).collect(Collectors.toList());
    }

    /**
     * Get User Login Details
     *
     * @param model
     * @return
     */
    @ResponseBody
    @GetMapping("/profile")
    @CrossOrigin
    public ResponseEntity<User> showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findNameByEmail(username);
        System.out.println("user email: " + user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    /**
     * User Search & Filter By name & Role
     *
     * @param search
     * @param roles
     * @param model
     * @return
     */
    @GetMapping("/users")
    public String searchUsers(@RequestParam(required = false) String search,
                              @RequestParam(name = "role", required = false) List<UserRole> roles,
                              Model model) {
        List<User> users;
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Predicate predicate = criteriaBuilder.conjunction();
            if (search != null && !search.isEmpty()) {
                System.out.println("inside user search: " + search);
                Predicate userSearchPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("firstName"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("lastName"), "%" + search + "%"),
                        criteriaBuilder.like(root.get("email"), "%" + search + "%")
                );
                predicate = criteriaBuilder.and(predicate, userSearchPredicate);
            }
            if (roles != null && !roles.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("role").in(roles));
            }
            return predicate;
        };
        if (spec != null) {
            users = userService.getAllUser(spec);
        } else {
            users = userService.getAll();
        }
        model.addAttribute("listusers", users);
        model.addAttribute("roles", Arrays.asList(UserRole.values()));
        model.addAttribute("selectedRoles", roles != null ? roles : new ArrayList<UserRole>());
        return "userHome";
    }
}
