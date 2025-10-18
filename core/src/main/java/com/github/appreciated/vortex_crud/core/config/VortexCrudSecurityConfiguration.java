package com.github.appreciated.vortex_crud.core.config;

import com.github.appreciated.vortex_crud.core.config.model.Application;
import com.github.appreciated.vortex_crud.core.config.model.UserManagement;
import com.github.appreciated.vortex_crud.core.entity.User;
import com.github.appreciated.vortex_crud.core.service.DatabaseUserProvider;
import com.github.appreciated.vortex_crud.core.service.UserProvider;
import com.github.appreciated.vortex_crud.core.service.UserService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigurationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
public class VortexCrudSecurityConfiguration<T extends User> {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public UserProvider<T> userProvider(VortexCrudConfigurationProvider vortexCrudConfigurationProvider) {
        Application app = vortexCrudConfigurationProvider.get();
        UserManagement<T> userManagement = app.getUserManagement();
        if (userManagement.getUserProvider() != null) {
            return userManagement.getUserProvider();
        } else {
            return new DatabaseUserProvider<>(userManagement.getUserClass(), userManagement.getUserSupplier());
        }
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public UserService<T> userService(UserProvider<T> userProvider) {
        return new UserService<>(userProvider);
    }
}