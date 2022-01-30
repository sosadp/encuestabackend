package com.djsm.encuestabackend.controllers;

import com.djsm.encuestabackend.entities.UserEntity;
import com.djsm.encuestabackend.models.request.UserRegisterRequestModel;
import com.djsm.encuestabackend.models.responses.UserRest;
import com.djsm.encuestabackend.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserControllers {

    @Autowired
    UserService userService;

    @PostMapping()
    public UserRest createUser(@RequestBody @Valid UserRegisterRequestModel userModels){

        UserEntity user = userService.createUser(userModels);

        UserRest userRest = new UserRest();

        BeanUtils.copyProperties(user, userRest);

        return userRest;
    }

    @GetMapping
    public UserRest getUser(Authentication authentication){

        UserEntity userUsername = userService.getUser(authentication.getPrincipal().toString());

        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(userUsername,userRest);

        return userRest;
    }
    

    
}
