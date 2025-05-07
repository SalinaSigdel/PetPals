package com.snapgramfx.petpals.model;

import java.sql.Timestamp;

public class Pet {
    private int petId;
    private String name;
    private String type;
    private String breed;
    private String age;
    private String gender;
    private String weight;
    private String description;
    private String imageUrl;
    private String status;
    private Timestamp createdAt;
    private String badge; // Added badge field

    // Constructors
    public Pet() {
    }

    public Pet(int petId, String name, String type, String breed, String age, String gender, String weight,
               String description, String imageUrl, String status, Timestamp createdAt) {
        this.petId = petId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.badge = "";
    }

    public Pet(int petId, String name, String type, String breed, String age, String gender, String weight,
               String description, String imageUrl, String status, Timestamp createdAt, String badge) {
        this.petId = petId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.badge = badge;
    }

    // Getters and Setters
    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Get the badge
     * @return badge text
     */
    public String getBadge() {
        return badge;
    }

    /**
     * Set the badge
     * @param badge the badge to set
     */
    public void setBadge(String badge) {
        this.badge = badge;
    }

    /**
     * Get a formatted age string
     * @return formatted age string
     */
    public String getFormattedAge() {
        return age;
    }

    /**
     * Get a status badge
     * @return status badge text
     */
    public String getStatusBadge() {
        if ("available".equals(status)) {
            return "Available";
        } else if ("pending".equals(status)) {
            return "Pending";
        } else if ("adopted".equals(status)) {
            return "Adopted";
        }
        return "";
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", weight='" + weight + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}