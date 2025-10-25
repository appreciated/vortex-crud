package com.github.appreciated.vortex_crud.example.jpa.entity;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudUser;
import com.github.appreciated.vortex_crud.jpa.service.annoations.EmailField;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class User implements VortexCrudUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @EmailField()
    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
