package com.github.appreciated.vortex_crud.core.security;

import com.github.appreciated.vortex_crud.core.entity.VortexCrudUser;
import org.springframework.security.core.userdetails.UserDetails;

public interface SecurityService {

    UserDetails getAuthenticatedUser();

    void logout();
}
