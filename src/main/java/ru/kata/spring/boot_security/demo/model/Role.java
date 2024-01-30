package ru.kata.spring.boot_security.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "id")
    private Long id;

    @NotEmpty
    @NonNull
    @Column(name = "role", unique = true)
    private String role;

    @Transient
    @ManyToMany(mappedBy = "roles") // Тут пишем название поля из User, которая связан с этой сущностью
    private Set<User> users;


    public Role(String role) {
        this.role = role;
    }
    public Role(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return getRole();
    }

    @Override
    public String toString() {
        return role + ",\b";
    }
}

