package com.djsm.encuestabackend.validations;

import com.djsm.encuestabackend.annotations.UniqueEmail;
import com.djsm.encuestabackend.entities.UserEntity;
import com.djsm.encuestabackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>{

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        UserEntity userEntity = userRepository.findByEmail(s);

        if (userEntity == null){
            return true;
        }
        return false;
    }
}
