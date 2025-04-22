package model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String phone;
    private String address;
    private String role;
    private Timestamp createdAt;

    // Default constructor
    public User() {
    }

    // Constructor with basic fields
    public User(String name, String email, String username, String password, String role) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Full constructor
    public User(int id, String name, String email, String username, String password, String phone, String address, String role, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
} 