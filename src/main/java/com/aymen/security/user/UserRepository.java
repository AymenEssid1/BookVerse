package com.aymen.security.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//THIS IS THE 2ND STEP
public interface UserRepository extends JpaRepository<User,Integer> {

    //we need to fetch user by email
    Optional<User> findByEmail(String email);
}
