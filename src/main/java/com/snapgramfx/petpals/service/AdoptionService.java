package com.snapgramfx.petpals.service;

import com.snapgramfx.petpals.model.Adoption;
import com.snapgramfx.petpals.model.AdoptionApplication;
import com.snapgramfx.petpals.repository.AdoptionRepository;
import com.snapgramfx.petpals.repository.AdoptionApplicationRepository;

import java.util.List;

/**
 * Service class for Adoption-related business logic
 */
public class AdoptionService {
    private final AdoptionRepository adoptionRepository;
    private final AdoptionApplicationRepository applicationRepository;
    private final PetService petService;

    public AdoptionService() {
        this.adoptionRepository = new AdoptionRepository();
        this.applicationRepository = new AdoptionApplicationRepository();
        this.petService = new PetService();
    }

    /**
     * Get an adoption by ID
     * @param adoptionId the adoption ID
     * @return Adoption object if found, null otherwise
     */
    public Adoption getAdoptionById(int adoptionId) {
        return adoptionRepository.findById(adoptionId);
    }

    /**
     * Complete an adoption from an approved application
     * @param applicationId the ID of the approved application
     * @param notes Optional notes about the adoption
     * @return true if successful, false otherwise
     */
    public boolean completeAdoption(int applicationId, String notes) {
        // Get the application
        AdoptionApplication application = applicationRepository.findById(applicationId);

        // Check if application exists and is approved
        if (application == null) {
            return false;
        }

        // Skip status check if called from AdoptionApplicationService
        // which has already updated the status
        if (!"approved".equals(application.getStatus())) {
            return false;
        }

        // Check if adoption record already exists for this application
        if (adoptionRepository.findByApplicationId(applicationId) != null) {
            // Adoption record already exists, no need to create a new one
            return true;
        }

        // Create adoption record
        Adoption adoption = new Adoption();
        adoption.setApplicationId(applicationId);
        adoption.setUserId(application.getUserId());
        adoption.setPetId(application.getPetId());
        adoption.setStatus("completed");
        adoption.setNotes(notes);

        boolean success = adoptionRepository.save(adoption);

        // Update pet status to adopted - only if not already done by AdoptionApplicationService
        if (success) {
            petService.updatePetStatus(application.getPetId(), "adopted");
        }

        return success;
    }

    /**
     * Record a pet return (adoption failure)
     * @param adoptionId the ID of the adoption
     * @param notes Reason for return
     * @return true if successful, false otherwise
     */
    public boolean returnPet(int adoptionId, String notes) {
        Adoption adoption = adoptionRepository.findById(adoptionId);

        if (adoption == null) {
            return false;
        }

        // Update adoption status
        adoption.setStatus("returned");
        adoption.setNotes(notes);

        boolean success = adoptionRepository.update(adoption);

        // Update pet status back to available
        if (success) {
            petService.updatePetStatus(adoption.getPetId(), "available");
        }

        return success;
    }

    /**
     * Get all adoptions
     * @return List of all adoptions
     */
    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    /**
     * Get adoptions for a specific user
     * @param userId the user ID
     * @return List of adoptions for the user
     */
    public List<Adoption> getAdoptionsByUser(int userId) {
        return adoptionRepository.findByUserId(userId);
    }

    /**
     * Get total number of completed adoptions
     * @return Total number of completed adoptions
     */
    public int getCompletedAdoptionsCount() {
        return adoptionRepository.countCompletedAdoptions();
    }

    /**
     * Get adoption success rate
     * @return Percentage of successful adoptions
     */
    public double getAdoptionSuccessRate() {
        return adoptionRepository.calculateSuccessRate();
    }

    /**
     * Get recent adoptions
     * @param limit Maximum number of adoptions to return
     * @return List of recent adoptions
     */
    public List<Adoption> getRecentAdoptions(int limit) {
        return adoptionRepository.findRecentAdoptions(limit);
    }

    /**
     * Get all adoptions with pagination
     * @param offset Starting index
     * @param limit Maximum number of adoptions to return
     * @return List of adoptions
     */
    public List<Adoption> getAllAdoptions(int offset, int limit) {
        return adoptionRepository.findAllWithPagination(offset, limit);
    }

    /**
     * Get total number of adoptions
     * @return Total number of adoptions
     */
    public int getTotalAdoptionsCount() {
        return adoptionRepository.countAll();
    }

    /**
     * Get adoptions by status with pagination
     * @param status Status to filter by
     * @param offset Starting index
     * @param limit Maximum number of adoptions to return
     * @return List of adoptions with the given status
     */
    public List<Adoption> getAdoptionsByStatus(String status, int offset, int limit) {
        return adoptionRepository.findByStatusWithPagination(status, offset, limit);
    }

    /**
     * Get count of adoptions by status
     * @param status Status to count
     * @return Number of adoptions with the given status
     */
    public int getAdoptionsCountByStatus(String status) {
        return adoptionRepository.countByStatus(status);
    }
}
