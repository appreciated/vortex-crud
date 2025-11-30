package com.github.appreciated.vortex_crud.security.userstore.local.test;

import com.github.appreciated.vortex_crud.security.core.config.VortexCrudRoleProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestRole implements VortexCrudRoleProvider {
    private Integer id;
    private String name;

    @Override
    public String getRole() {
        return name;
    }
}
