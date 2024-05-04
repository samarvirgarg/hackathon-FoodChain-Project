package com.knf.dev.service.Service.User;

import java.util.List;

import com.knf.dev.model.Role.UserRole;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.knf.dev.dto.UserRegistrationDto;
import com.knf.dev.model.User.User;

//interface for user class
public interface UserService extends UserDetailsService {

    User save(UserRegistrationDto registrationDto, UserRole role);

    void saveUser(UserRegistrationDto registrationDto);

    void deleteUserById(long userId);

    User getUserById(long userId);

    List<User> getAll();

    List<User> getByKeyword(String search);

    List<User> getAllUser(Specification<User> spec);


}
