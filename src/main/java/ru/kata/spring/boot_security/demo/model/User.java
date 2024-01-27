package ru.kata.spring.boot_security.demo.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Column(name = "id", unique = true)
    private Long id;

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @Size(min=2, message = "Не меньше 2 знаков")
    private String password;

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


//    @ManyToMany(fetch = FetchType.EAGER)
//    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(name = "roles_username", referencedColumnName = "username")
    )
    private Set<Role> roles;

    public User(String username, String password ,String name, String surname, int age, int level, int points) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.level = level;
        this.points = points;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = this.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getUsername()));
        }
        return authorities; // возвращает список ролей
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
