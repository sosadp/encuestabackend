package com.djsm.encuestabackend.services;

import com.djsm.encuestabackend.entities.UserEntity;
import com.djsm.encuestabackend.models.request.UserRegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public UserEntity createUser(UserRegisterRequest userRegisterRequest);
}
