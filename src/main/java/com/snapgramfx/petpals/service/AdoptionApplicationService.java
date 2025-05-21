package com.snapgramfx.petpals.service;

import com.snapgramfx.petpals.model.AdoptionApplication;
import com.snapgramfx.petpals.model.Pet;
import com.snapgramfx.petpals.repository.AdoptionApplicationRepository;

import java.util.List;

/**
 * Service class for AdoptionApplication-related business logic
 */
public class AdoptionApplicationService {
    private final AdoptionApplicationRepository applicationRepository;
    private final PetService petService;
    private final NotificationService notificationService;
    private final AdoptionService adoptionService;

    public AdoptionApplicationService() {
        this.applicationRepository = new AdoptionApplicationRepository();
        this.petService = new PetService();
        this.notificationService = new NotificationService();
        this.adoptionService = new AdoptionService();
    }

    /**
     * Get an application by ID
     * @param applicationId the application ID
     * @return AdoptionApplication object if found, null otherwise
     */
    public AdoptionApplication getApplicationById(int applicationId) {
        return applicationRepository.findById(applicationId);
    }

    /**
     * Submit a new adoption application
     * @param application the application to submit
     * @return true if submission successful, false otherwise
     */
    public boolean submitApplication(AdoptionApplication application) {
        // Set default status if not specified
        if (application.getStatus() == null || application.getStatus().isEmpty()) {
            application.setStatus("pending");
        }

        boolean result = applicationRepository.save(application);

        // Update pet status to pending if application is submitted successfully
        if (result) {
            petService.updatePetStatus(application.getPetId(), "pending");
        }

        return result;
    }

    /**
     * Update an application's status
     * @param applicationId the ID of the application
     * @param status the new status
     * @param rejectionReason the reason for rejection (if status is 'rejected')
     * @return true if update successful, false otherwise
     */
    public boolean updateApplicationStatus(int applicationId, String status, String rejectionReason) {
        AdoptionApplication application = applicationRepository.findById(applicationId);
        if (application == null) {
            return false;
        }

        application.setStatus(status);
        if ("rejected".equals(status) && rejectionReason != null && !rejectionReason.isEmpty()) {
            application.setRejectionReason(rejectionReason);
        }

        boolean result = applicationRepository.update(application);

        // Update pet status based on application status
        if (result) {
            Pet pet = petService.getPetById(application.getPetId());
            String petName = (pet != null) ? pet.getName() : "the pet";

            if ("approved".equals(status)) {
                petService.updatePetStatus(application.getPetId(), "adopted");

                // Create adoption record in the Adoptions table
                adoptionService.completeAdoption(applicationId, "Adoption completed through application approval");

                // Send notification to user about approved adoption
                notificationService.createApplicationStatusNotification(
                        application.getUserId(),
                        petName,
                        status,
                        "Please check your email for next steps or contact the shelter for more information."
                );
            } else if ("rejected".equals(status)) {
                petService.updatePetStatus(application.getPetId(), "available");

                // Send notification about rejection
                notificationService.createApplicationStatusNotification(
                        application.getUserId(),
                        petName,
                        status,
                        rejectionReason != null ? rejectionReason : ""
                );
            } else {
                // For other status updates
                notificationService.createApplicationStatusNotification(
                        application.getUserId(),
                        petName,
                        status,
                        ""
                );
            }
        }

        return result;
    }

    /**
     * Delete an application
     * @param applicationId the ID of the application to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteApplication(int applicationId) {
        AdoptionApplication application = applicationRepository.findById(applicationId);
        if (application == null) {
            return false;
        }

        boolean result = applicationRepository.delete(applicationId);

        // If application is deleted and it was pending, update pet status back to available
        if (result && "pending".equals(application.getStatus())) {
            petService.updatePetStatus(application.getPetId(), "available");
        }

        return result;
    }

    /**
     * Get all applications
     * @return List of all applications
     */
    public List<AdoptionApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    /**
     * Get applications by user ID
     * @param userId the user ID
     * @return List of applications for the user
     */
    public List<AdoptionApplication> getApplicationsByUserId(int userId) {
        return applicationRepository.findByUserId(userId);
    }

    /**
     * Get applications by user ID with pagination
     * @param userId User ID to filter by
     * @param offset Starting index
     * @param limit Maximum number of applications to return
     * @return List of applications for the specified user
     */
    public List<AdoptionApplication> getApplicationsByUserIdWithPagination(int userId, int offset, int limit) {
        return applicationRepository.findByUserIdWithPagination(userId, offset, limit);
    }

    /**
     * Get count of applications by user ID
     * @param userId User ID to count applications for
     * @return Number of applications for the specified user
     */
    public int getApplicationsCountByUserId(int userId) {
        return applicationRepository.countByUserId(userId);
    }

    /**
     * Get applications by status
     * @param status the status
     * @return List of applications with the given status
     */
    public List<AdoptionApplication> getApplicationsByStatus(String status) {
        return applicationRepository.findByStatus(status);
    }

    /**
     * Get pending applications with limit
     * @param limit Maximum number of applications to return
     * @return List of pending applications
     */
    public List<AdoptionApplication> getPendingApplications(int limit) {
        return applicationRepository.findPendingApplications(limit);
    }

    /**
     * Get count of pending applications
     * @return Number of pending applications
     */
    public int getPendingApplicationsCount() {
        return applicationRepository.countPendingApplications();
    }

    /**
     * Get all applications with pagination
     * @param offset Starting index
     * @param limit Maximum number of applications to return
     * @return List of applications
     */
    public List<AdoptionApplication> getAllApplications(int offset, int limit) {
        return applicationRepository.findAllWithPagination(offset, limit);
    }

    /**
     * Get total number of applications
     * @return Total number of applications
     */
    public int getTotalApplicationsCount() {
        return applicationRepository.countAll();
    }

    /**
     * Get applications by status with pagination
     * @param status Status to filter by
     * @param offset Starting index
     * @param limit Maximum number of applications to return
     * @return List of applications with the given status
     */
    public List<AdoptionApplication> getApplicationsByStatus(String status, int offset, int limit) {
        return applicationRepository.findByStatusWithPagination(status, offset, limit);
    }

    /**
     * Get count of applications by status
     * @param status Status to count
     * @return Number of applications with the given status
     */
    public int getApplicationsCountByStatus(String status) {
        return applicationRepository.countByStatus(status);
    }
}