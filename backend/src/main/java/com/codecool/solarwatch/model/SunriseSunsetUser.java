package com.codecool.solarwatch.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class SunriseSunsetUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    public SunriseSunsetUser() {
    }

    public SunriseSunsetUser(String email, String password, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>(roles);
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return Set.copyOf(roles);
    }
}
