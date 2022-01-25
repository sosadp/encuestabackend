package com.djsm.encuestabackend.repositories;

import com.djsm.encuestabackend.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {


}
