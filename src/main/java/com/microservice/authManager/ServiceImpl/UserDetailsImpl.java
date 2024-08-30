package com.microservice.authManager.ServiceImpl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microservice.authManager.Dto.request.UserDTO;
import com.microservice.authManager.Entity.Role;
import com.microservice.authManager.Entity.User;
import com.microservice.authManager.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String loginId;
    private String name;
    private String firstname;
    private String uuid;

    @JsonIgnore
    private String password;

    @Autowired
    private static RoleRepository roleRepository;

    private Collection<? extends GrantedAuthority> authorities;
    public UserDetailsImpl(String uuid, String name, String firstname, String loginId , String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.uuid = uuid;
        this.name = name;
        this.firstname = firstname;
        this.loginId = loginId;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        //Optional<Role> role = roleRepository.findByProvide(user.getRoleID());
        String role = user.getRole();

        GrantedAuthority authority = new SimpleGrantedAuthority(role);

        List<GrantedAuthority> authorities = Collections.singletonList(authority);

        return new UserDetailsImpl(
                user.getUuid(),
                user.getName(),
                user.getFirstname(),
                user.getUsername(),
                user.getPassword(),
                authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
