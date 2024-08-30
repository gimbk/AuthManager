package com.microservice.authManager.Entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    public int statusCode;
    public String role;
    public Long provide;
    public String keyapp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getKeyapp() {
        return keyapp;
    }

    public void setKeyapp(String keyapp) {
        this.keyapp = keyapp;
    }

    public Long getProvide() {
        return provide;
    }

    public void setProvide(Long provide) {
        this.provide = provide;
    }
}
