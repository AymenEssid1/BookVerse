package com.aymen.security.auth;


import com.aymen.security.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

//8th
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class RegisterRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;

}
