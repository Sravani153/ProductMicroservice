package com.example.task.restproductapi.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.task.restproductapi.Enum.UserRole;
import com.example.task.restproductapi.entities.User;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findUserByEmail(String email);
    User findByRole(UserRole role);
}
