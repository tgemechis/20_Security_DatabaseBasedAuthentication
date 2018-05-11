package com.meklit.demo;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser,Long> {
    AppUser findByUsername(String theUser);
}
