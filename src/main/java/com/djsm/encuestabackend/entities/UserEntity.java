package com.djsm.encuestabackend.entities;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

@Entity(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable{


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;
    
}
