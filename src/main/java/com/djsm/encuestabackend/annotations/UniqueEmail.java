package com.djsm.encuestabackend.annotations;

import com.djsm.encuestabackend.validations.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


/* para la creacion de anotaciones personalizadas se usa @interfaces como parte de la construccion y las clases
* groups y payload son clases boilerplace o relleno por convension*/
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "{encuesta.constraints.email.unique.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
