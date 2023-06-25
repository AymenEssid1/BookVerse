package com.aymen.security.user;


import com.aymen.security.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


//THIS IS THE FIRST STEP
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user") //postgre has a table called user so we change this name
public class User implements UserDetails { //implement user details by alt+enter

    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "user")
    private List<Token> tokens;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();  //change to what role you have
    }

    @Override
    public String getPassword() {
        return password; //if u named your password attribute password you won't get this beacuse of lombok so change the name implement then return your password
    }

    @Override
    public String getUsername() {
        return email; //change to email or username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; //change to true
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; //change to true
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; //change to true
    }

    @Override
    public boolean isEnabled() {
        return true; //change to true
    }
}
