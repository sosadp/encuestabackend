package com.djsm.encuestabackend.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String mail;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;
}
