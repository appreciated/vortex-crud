package com.github.appreciated.vortex_crud.test.jpa.ui.security;

import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "security_roles")
public class JpaSecurityRole implements VortexCrudRoleProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<JpaSecurityUser> users;

    public JpaSecurityRole() {
    }

    public JpaSecurityRole(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JpaSecurityUser> getUsers() {
        return users;
    }

    public void setUsers(List<JpaSecurityUser> users) {
        this.users = users;
    }

    @Override
    public String getRole() {
        return name;
    }
}
