package com.github.appreciated.vortex_crud.example.jooq.entity;

import com.github.appreciated.vortex_crud.core.service.MutableUser;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collections;
import java.util.List;

@Entity
public class User implements com.github.appreciated.vortex_crud.core.entity.User, MutableUser {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String roles;

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
    public List<String> getRoles() {
        return Collections.singletonList(roles);
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}