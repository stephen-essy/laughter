package com.laughter.laughter.Entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="User")
@Entity
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="name",nullable=false)
    private String name;
    @Column(name="email",nullable=false)
    private String email;
    @Column(name="password",nullable=false)
    private String password;
    @Column(name="recovery_string")
    private String recoveryString;

    public User(String name, String email, String password,String recoveryString) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.recoveryString=recoveryString;
    }

    @Override
    public Collection <? extends GrantedAuthority> getAuthorities(){
        return List.of();
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername(){
        return email;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){return true;}
}
