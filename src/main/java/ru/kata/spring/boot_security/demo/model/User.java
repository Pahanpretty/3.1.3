package ru.kata.spring.boot_security.demo.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;


@Entity(name = "users")
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "age")
    private int age;

    @Column(name = "level")
    private int level;

    @Column(name = "points")
    private int points;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @Size(min=2, message = "Не меньше 5 знаков")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(String username, String password ,String name, String surname, int age, int level, int points) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.level = level;
        this.points = points;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
