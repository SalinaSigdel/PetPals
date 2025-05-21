package com.snapgramfx.petpals.model;

import java.sql.Timestamp;

/**
 * Model class representing a pet shelter
 */
public class Shelter {
    private int shelterId;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String phone;
    private String email;
    private String website;
    private String description;
    private Timestamp createdAt;

    // Constructors
    public Shelter() {
    }

    public Shelter(int shelterId, String name, String address, String city, String state, String zipCode,
                   String phone, String email, String website, String description, Timestamp createdAt) {
        this.shelterId = shelterId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getShelterId() {
        return shelterId;
    }

    public void setShelterId(int shelterId) {
        this.shelterId = shelterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "shelterId=" + shelterId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
