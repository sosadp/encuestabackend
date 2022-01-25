package com.djsm.encuestabackend.services;

import com.djsm.encuestabackend.entities.UserEntity;
import com.djsm.encuestabackend.models.request.UserRegisterRequest;
import com.djsm.encuestabackend.repositories.UserRepository;

public interface UserServices {


    public UserEntity createUser(UserRegisterRequest userRegisterRequest);
}
