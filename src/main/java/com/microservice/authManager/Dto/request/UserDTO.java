package com.microservice.authManager.Dto.request;

import java.time.LocalDateTime;

public class UserDTO {
    public Long id;
    public String uuid;
    public String firstname;
    public String name;
    public String dateOfBirth;
    public String placeOfBirth;
    public String CountryOfBirth;
    public String cityOfBirth;
    public String CountryResidence;
    public String cityResidence;
    public String civility;
    public String phone;
    public String email;
    public String areaCode;
    public String typePiece;
    public String idPiece;
    public String dateIssue;
    public String dateExpiration;
    public String signature;
    public String photo;
    public String keyapp;
    public String username;
    public String password;
    public String createdAt;
    public String updatedAt;
    public Long roleID;
    public int numberConnect;
    public LocalDateTime lastConnect;
    public boolean isConnected;
    public boolean isActived;
    public boolean isDeleted;
    public LocalDateTime deletedAt;
    public int statusCode;
    public int role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getCountryOfBirth() {
        return CountryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        CountryOfBirth = countryOfBirth;
    }

    public String getCityOfBirth() {
        return cityOfBirth;
    }

    public void setCityOfBirth(String cityOfBirth) {
        this.cityOfBirth = cityOfBirth;
    }

    public String getCountryResidence() {
        return CountryResidence;
    }

    public void setCountryResidence(String countryResidence) {
        CountryResidence = countryResidence;
    }

    public String getCityResidence() {
        return cityResidence;
    }

    public void setCityResidence(String cityResidence) {
        this.cityResidence = cityResidence;
    }

    public String getCivility() {
        return civility;
    }

    public void setCivility(String civility) {
        this.civility = civility;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getTypePiece() {
        return typePiece;
    }

    public void setTypePiece(String typePiece) {
        this.typePiece = typePiece;
    }

    public String getIdPiece() {
        return idPiece;
    }

    public void setIdPiece(String idPiece) {
        this.idPiece = idPiece;
    }

    public String getDateIssue() {
        return dateIssue;
    }

    public void setDateIssue(String dateIssue) {
        this.dateIssue = dateIssue;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getKeyapp() {
        return keyapp;
    }

    public void setKeyapp(String keyapp) {
        this.keyapp = keyapp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getRoleID() {
        return roleID;
    }

    public void setRoleID(Long roleID) {
        this.roleID = roleID;
    }

    public int getNumberConnect() {
        return numberConnect;
    }

    public void setNumberConnect(int numberConnect) {
        this.numberConnect = numberConnect;
    }

    public LocalDateTime getLastConnect() {
        return lastConnect;
    }

    public void setLastConnect(LocalDateTime lastConnect) {
        this.lastConnect = lastConnect;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public boolean isActived() {
        return isActived;
    }

    public void setActived(boolean actived) {
        isActived = actived;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
