package com.snapgramfx.petpals.model;

import java.sql.Timestamp;

/**
 * Model class representing a completed adoption
 */
public class Adoption {
    private int adoptionId;
    private int applicationId;
    private int userId;
    private int petId;
    private Timestamp adoptionDate;
    private String status;
    private String notes;

    // Additional fields for display purposes
    private String petName;
    private String userName;

    // Constructors
    public Adoption() {
    }

    public Adoption(int adoptionId, int applicationId, int userId, int petId, Timestamp adoptionDate,
                    String status, String notes) {
        this.adoptionId = adoptionId;
        this.applicationId = applicationId;
        this.userId = userId;
        this.petId = petId;
        this.adoptionDate = adoptionDate;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    public int getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(int adoptionId) {
        this.adoptionId = adoptionId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public Timestamp getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(Timestamp adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Adoption{" +
                "adoptionId=" + adoptionId +
                ", applicationId=" + applicationId +
                ", userId=" + userId +
                ", petId=" + petId +
                ", adoptionDate=" + adoptionDate +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                ", petName='" + petName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
