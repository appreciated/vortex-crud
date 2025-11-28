package com.github.appreciated.vortex_crud.test.jpa.ui.security;

import com.github.appreciated.vortex_crud.jpa.service.annoations.EmailField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.PasswordField;
import com.github.appreciated.vortex_crud.jpa.service.annoations.TextField;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "security_users")
public class JpaSecurityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @EmailField
    private String username;

    @PasswordField
    private String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "security_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<JpaSecurityRole> roles;

    // Test fields for permissions
    @TextField
    private String publicField;

    @TextField
    private String adminField;

    @TextField
    private String secretField;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String password) {
        this.passwordHash = password;
    }

    public List<JpaSecurityRole> getRoles() {
        return roles;
    }

    public void setRoles(List<JpaSecurityRole> roles) {
        this.roles = roles;
    }

    public String getPublicField() {
        return publicField;
    }

    public void setPublicField(String publicField) {
        this.publicField = publicField;
    }

    public String getAdminField() {
        return adminField;
    }

    public void setAdminField(String adminField) {
        this.adminField = adminField;
    }

    public String getSecretField() {
        return secretField;
    }

    public void setSecretField(String secretField) {
        this.secretField = secretField;
    }
}
