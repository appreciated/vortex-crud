package com.github.appreciated.vortex_crud.security.core.service;

import com.github.appreciated.vortex_crud.core.config.model.IdentityAndAccessManagement;
import com.github.appreciated.vortex_crud.core.service.VortexCrudConfigService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class UserRegistrationService<ModelClass, FieldType, RepositoryType> {

    private final VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(
            VortexCrudConfigService<ModelClass, FieldType, RepositoryType> configService,
            PasswordEncoder passwordEncoder
    ) {
        this.configService = configService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user by encoding the password and saving to the repository.
     *
     * @param user The user entity to register
     * @return The saved user entity
     * @throws RuntimeException if registration fails
     */
    @SuppressWarnings("unchecked")
    public Object registerUser(Object user) {
        try {
            IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> userManagement =
                    configService.getConfiguration().getUserManagement();

            // Get the password field name from configuration
            String passwordFieldName = userManagement.getPassword().getFieldName();

            // Get the password from the user entity
            Method getPasswordMethod = user.getClass().getMethod("get" +
                    capitalizeFirstLetter(passwordFieldName));
            String rawPassword = (String) getPasswordMethod.invoke(user);

            if (rawPassword == null || rawPassword.isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }

            // Encode the password
            String encodedPassword = passwordEncoder.encode(rawPassword);

            // Set the encoded password to passwordHash field
            Method setPasswordHashMethod = user.getClass().getMethod("setPasswordHash", String.class);
            setPasswordHashMethod.invoke(user, encodedPassword);

            // Get the repository and save the user
            RepositoryType repositoryKey = userManagement.getRepositoryKey();
            if (repositoryKey instanceof CrudRepository) {
                CrudRepository<Object, ?> repository = (CrudRepository<Object, ?>) repositoryKey;
                return repository.save(user);
            } else {
                throw new IllegalStateException("Repository must be a CrudRepository");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to register user: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a username already exists in the repository.
     *
     * @param username The username to check
     * @return true if the username exists, false otherwise
     */
    @SuppressWarnings("unchecked")
    public boolean usernameExists(String username) {
        try {
            IdentityAndAccessManagement<ModelClass, FieldType, RepositoryType> userManagement =
                    configService.getConfiguration().getUserManagement();

            RepositoryType repositoryKey = userManagement.getRepositoryKey();
            if (repositoryKey instanceof CrudRepository) {
                CrudRepository<Object, ?> repository = (CrudRepository<Object, ?>) repositoryKey;

                // Get all users and check if username exists
                Iterable<?> allUsers = repository.findAll();
                String usernameFieldName = userManagement.getUsername().getFieldName();

                for (Object user : allUsers) {
                    Method getUsernameMethod = user.getClass().getMethod("get" +
                            capitalizeFirstLetter(usernameFieldName));
                    String existingUsername = (String) getUsernameMethod.invoke(user);
                    if (username.equals(existingUsername)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to check username existence: " + e.getMessage(), e);
        }
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
