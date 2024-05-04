package com.knf.dev.repository.UserRepo;

import com.knf.dev.dto.UserRegistrationDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import com.knf.dev.model.User.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Database Operations for user table
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM user where email = :email", nativeQuery = true)
    User findNameByEmail(@Param("email") String email);


    @Query("SELECT u FROM User u " +
            "WHERE u.firstName LIKE %:search% " +
            "    OR u.lastName LIKE %:search% " +
            "    OR u.email LIKE %:search% ")
    List<User> findByKeyword(@Param("search") String search);

    List<User> findAll(Specification<User> spec);


}
