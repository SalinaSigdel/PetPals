package com.snapgramfx.petpals.service;

import com.snapgramfx.petpals.model.Shelter;
import com.snapgramfx.petpals.repository.ShelterRepository;

import java.util.List;

/**
 * Service class for Shelter-related business logic
 */
public class ShelterService {
    private final ShelterRepository shelterRepository;
    
    public ShelterService() {
        this.shelterRepository = new ShelterRepository();
    }
    
    /**
     * Get a shelter by ID
     * @param shelterId the shelter ID
     * @return Shelter object if found, null otherwise
     */
    public Shelter getShelterById(int shelterId) {
        return shelterRepository.findById(shelterId);
    }
    
    /**
     * Add a new shelter
     * @param shelter the shelter to add
     * @return true if addition successful, false otherwise
     */
    public boolean addShelter(Shelter shelter) {
        return shelterRepository.save(shelter);
    }
    
    /**
     * Update a shelter's information
     * @param shelter the shelter with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateShelter(Shelter shelter) {
        // Ensure shelter exists
        Shelter existingShelter = shelterRepository.findById(shelter.getShelterId());
        if (existingShelter == null) {
            return false;
        }
        
        return shelterRepository.update(shelter);
    }
    
    /**
     * Delete a shelter
     * @param shelterId the ID of the shelter to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteShelter(int shelterId) {
        return shelterRepository.delete(shelterId);
    }
    
    /**
     * Get all shelters
     * @return List of all shelters
     */
    public List<Shelter> getAllShelters() {
        return shelterRepository.findAll();
    }
    
    /**
     * Get shelters by state
     * @param state The state to filter by
     * @return List of shelters in the specified state
     */
    public List<Shelter> getSheltersByState(String state) {
        return shelterRepository.findByState(state);
    }
    
    /**
     * Get total number of shelters
     * @return Total number of shelters
     */
    public int getShelterCount() {
        return shelterRepository.countShelters();
    }
}
