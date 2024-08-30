package com.microservice.authManager.Dto.request;

public class UserRequestDto {
    private String username;

    public UserRequestDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
