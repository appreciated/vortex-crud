package com.github.appreciated.vortex_crud.core.auth;

import com.github.appreciated.vortex_crud.core.auth.repository.VortexCrudUserRepository;
import com.github.appreciated.vortex_crud.core.entity.VortexCrudUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    private final VortexCrudUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserManagementServiceImpl(VortexCrudUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<VortexCrudUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void register(VortexCrudUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }
}
