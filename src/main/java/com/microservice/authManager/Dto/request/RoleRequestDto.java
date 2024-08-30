package com.microservice.authManager.Dto.request;

public class RoleRequestDto {
    private String keyapp;
    private Long title;

    public RoleRequestDto(String keyapp, Long title) {
        this.keyapp = keyapp;
        this.title = title;
    }

    public String getKeyapp() {
        return keyapp;
    }

    public void setKeyapp(String keyapp) {
        this.keyapp = keyapp;
    }

    public Long getTitle() {
        return title;
    }

    public void setTitle(Long title) {
        this.title = title;
    }
}
