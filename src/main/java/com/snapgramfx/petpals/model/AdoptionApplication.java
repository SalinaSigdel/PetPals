package com.snapgramfx.petpals.model;

import java.sql.Timestamp;

public class AdoptionApplication {
    private int applicationId;
    private int userId;
    private int petId;
    private String status;
    private String applicantName;
    private String email;
    private String phone;
    private String cityState;
    private String reasonForAdoption;
    private String rejectionReason;
    private Timestamp createdAt;

    // For displaying related information
    private String petName;
    private String userName;

    // Constructors
    public AdoptionApplication() {
    }

    public AdoptionApplication(int applicationId, int userId, int petId, String status, String applicantName,
                               String email, String phone, String cityState, String reasonForAdoption,
                               String rejectionReason, Timestamp createdAt) {
        this.applicationId = applicationId;
        this.userId = userId;
        this.petId = petId;
        this.status = status;
        this.applicantName = applicantName;
        this.email = email;
        this.phone = phone;
        this.cityState = cityState;
        this.reasonForAdoption = reasonForAdoption;
        this.rejectionReason = rejectionReason;
        this.createdAt = createdAt;
    }

    // Getters and Setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getReasonForAdoption() {
        return reasonForAdoption;
    }

    public void setReasonForAdoption(String reasonForAdoption) {
        this.reasonForAdoption = reasonForAdoption;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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
        return "AdoptionApplication{" +
                "applicationId=" + applicationId +
                ", userId=" + userId +
                ", petId=" + petId +
                ", status='" + status + '\'' +
                ", applicantName='" + applicantName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", cityState='" + cityState + '\'' +
                ", reasonForAdoption='" + reasonForAdoption + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}