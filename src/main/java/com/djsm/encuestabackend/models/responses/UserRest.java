package com.djsm.encuestabackend.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRest {

    private long id;

    private String name;

    private String email;

}
