package com.github.appreciated.vortex_crud.security.core.service;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VortexCrudUserDetailsService<ModelClass, FieldType, RepositoryType> implements UserDetailsService {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;

    public VortexCrudUserDetailsService(VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService) {
        this.configService = configService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> userManagement =
                    configService.getConfiguration().getUserManagement();

            if (userManagement == null) {
                throw new UsernameNotFoundException("User management not configured");
            }

            RepositoryType repositoryKey = userManagement.getRepositoryKey();
            if (!(repositoryKey instanceof CrudRepository)) {
                throw new IllegalStateException("Repository must be a CrudRepository");
            }

            CrudRepository<Object, ?> repository = (CrudRepository<Object, ?>) repositoryKey;

            // Find user by username using reflection to call findByUsername method
            Method findByUsernameMethod = repository.getClass().getMethod("findByUsername", String.class);
            Object userOptional = findByUsernameMethod.invoke(repository, username);

            // Check if user exists (handling Optional)
            Method isPresentMethod = userOptional.getClass().getMethod("isPresent");
            boolean isPresent = (boolean) isPresentMethod.invoke(userOptional);

            if (!isPresent) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            // Get the user from Optional
            Method getMethod = userOptional.getClass().getMethod("get");
            Object user = getMethod.invoke(userOptional);

            // Get username field
            String usernameFieldName = userManagement.getUsername().getFieldName();
            Method getUsernameMethod = user.getClass().getMethod("get" + capitalizeFirstLetter(usernameFieldName));
            String actualUsername = (String) getUsernameMethod.invoke(user);

            // Get password hash
            Method getPasswordHashMethod = user.getClass().getMethod("getPasswordHash");
            String passwordHash = (String) getPasswordHashMethod.invoke(user);

            // Get roles
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
            }

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
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
