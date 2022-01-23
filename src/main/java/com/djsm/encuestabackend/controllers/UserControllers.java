package com.djsm.encuestabackend.controllers;

import com.djsm.encuestabackend.models.request.UserRegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserControllers {

    @PostMapping()
    public String createUser(@RequestBody @Valid UserRegisterRequest userModels){
        return "Create User";
    }
    

    
}
