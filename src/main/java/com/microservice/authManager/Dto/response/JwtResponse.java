package com.microservice.authManager.Dto.response;

public class JwtResponse {
    private String token;
    private String uuid;
    private String username;
    private String name;
    private String firstname;
    private String roles;

    public JwtResponse(String token, String uuid, String username, String name, String firstname,String roles) {
        this.token = token;
        this.uuid = uuid;
        this.username = username;
        this.name = name;
        this.firstname = firstname;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
