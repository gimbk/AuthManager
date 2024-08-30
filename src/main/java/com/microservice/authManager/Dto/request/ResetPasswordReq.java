package com.microservice.authManager.Dto.request;

public class ResetPasswordReq {
    private String url;
    private String name;
    private String email;

    public ResetPasswordReq(String url, String name, String email) {
        this.url = url;
        this.name = name;
        this.email = email;
    }

    public ResetPasswordReq(String email,String url) {
        this.url = url;
        this.name = name;
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
