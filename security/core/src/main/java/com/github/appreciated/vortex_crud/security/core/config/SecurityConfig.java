package com.github.appreciated.vortex_crud.security.core.config;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStore;
import com.github.appreciated.vortex_crud.core.entity.data_store.VortexCrudDataStoreFactoryRegistry;
import com.github.appreciated.vortex_crud.core.entity.reflection.ReflectionService;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import com.github.appreciated.vortex_crud.security.core.view.LoginView;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/sign-up").permitAll())
                .with(VaadinSecurityConfigurer.vaadin(), configurer -> configurer.loginView(LoginView.class))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public <ModelClass, FieldType, RepositoryType> UserDetailsService userDetailsService(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            VortexCrudDataStoreFactoryRegistry<ModelClass, FieldType, RepositoryType> dataStoreFactoryRegistry,
            ReflectionService<FieldType> reflectionService
    ) {
        return username -> {
            try {
                IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> userManagement =
                        configService.getConfiguration().getUserManagement();

                if (userManagement == null) {
                    throw new UsernameNotFoundException("User management not configured");
                }

                // Get data store for users using framework pattern
                RepositoryType repositoryKey = userManagement.getRepositoryKey();
                VortexCrudDataStore<FieldType, ModelClass> userDataStore =
                        dataStoreFactoryRegistry.getDataStore(repositoryKey);

                // Find user by username using VortexCrudDataStore
                FieldType usernameFieldType = userManagement.getUsername().getField();
                List<ModelClass> users = userDataStore.getRecordsFromTableWhereColumnEquals(
                        usernameFieldType,
                        username,
                        0,
                        1
                );

                if (users.isEmpty()) {
                    throw new UsernameNotFoundException("User not found: " + username);
                }

                ModelClass user = users.get(0);

                // Get username using ReflectionService
                String actualUsername = reflectionService.getString(user, usernameFieldType);

                // Get password hash using reflection (passwordHash field is not in config)
                String passwordHash = getPasswordHash(user);

                // Get roles
                Set<GrantedAuthority> authorities = getRoles(user);

                // If no roles found, assign a default role
                if (authorities.isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }

                return org.springframework.security.core.userdetails.User
                        .withUsername(actualUsername)
                        .password(passwordHash)
                        .authorities(authorities)
                        .build();

            } catch (UsernameNotFoundException e) {
                throw e;
            } catch (Exception e) {
                throw new UsernameNotFoundException("Failed to load user: " + e.getMessage(), e);
            }
        };
    }

    private static String getPasswordHash(Object user) throws Exception {
        try {
            Method getPasswordHashMethod = user.getClass().getMethod("getPasswordHash");
            return (String) getPasswordHashMethod.invoke(user);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("User entity must have a getPasswordHash() method");
        }
    }

    private static Set<GrantedAuthority> getRoles(Object user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        try {
            Method getRolesMethod = user.getClass().getMethod("getRoles");
            Collection<?> roles = (Collection<?>) getRolesMethod.invoke(user);

            if (roles != null) {
                for (Object role : roles) {
                    Method getRoleNameMethod = role.getClass().getMethod("getName");
                    String roleName = (String) getRoleNameMethod.invoke(role);
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));
                }
            }
        } catch (NoSuchMethodException e) {
            // No roles method, user has no roles
        } catch (Exception e) {
            // Log error but don't fail authentication
        }
        return authorities;
    }
}