package com.github.appreciated.vortex_crud.security.userstore.local.test;

import lombok.Data;
import java.util.List;

@Data
public class TestUser {
    private Integer id;
    private String username;
    private String passwordHash;
    private List<TestRole> roles;
    private String publicField;
    private String adminField;
    private String secretField;
}
