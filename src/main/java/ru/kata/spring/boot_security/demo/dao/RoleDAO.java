package ru.kata.spring.boot_security.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Optional;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE r.id = :id")
    Optional<Role> getAllById(@Param("id") Long id);
}
