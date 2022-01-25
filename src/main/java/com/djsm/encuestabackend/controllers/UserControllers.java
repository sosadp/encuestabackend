package com.djsm.encuestabackend.controllers;

import com.djsm.encuestabackend.entities.UserEntity;
import com.djsm.encuestabackend.models.request.UserRegisterRequest;
import com.djsm.encuestabackend.models.responses.UserRest;
import com.djsm.encuestabackend.services.UserServices;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserControllers {

    @Autowired
    UserServices userServices;

    @PostMapping()
    public UserRest createUser(@RequestBody @Valid UserRegisterRequest userModels){

        UserEntity user = userServices.createUser(userModels);

        UserRest userRest = new UserRest();

        BeanUtils.copyProperties(user, userRest);

        return userRest;
    }
    

    
}
