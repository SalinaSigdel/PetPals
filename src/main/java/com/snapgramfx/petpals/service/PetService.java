package com.snapgramfx.petpals.service;

import com.snapgramfx.petpals.model.Pet;
import com.snapgramfx.petpals.repository.PetRepository;

import java.util.List;

/**
 * Service class for Pet-related business logic
 */
public class PetService {
    private final PetRepository petRepository;

    public PetService() {
        this.petRepository = new PetRepository();
    }

    /**
     * Get a pet by ID
     * @param petId the pet ID
     * @return Pet object if found, null otherwise
     */
    public Pet getPetById(int petId) {
        return petRepository.findById(petId);
    }

    /**
     * Add a new pet
     * @param pet the pet to add
     * @return true if addition successful, false otherwise
     */
    public boolean addPet(Pet pet) {
        // Set default status if not specified
        if (pet.getStatus() == null || pet.getStatus().isEmpty()) {
            pet.setStatus("available");
        }

        return petRepository.save(pet);
    }

    /**
     * Update a pet's information
     * @param pet the pet with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updatePet(Pet pet) {
        // Ensure pet exists
        Pet existingPet = petRepository.findById(pet.getPetId());
        if (existingPet == null) {
            return false;
        }

        return petRepository.update(pet);
    }

    /**
     * Delete a pet
     * @param petId the ID of the pet to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deletePet(int petId) {
        return petRepository.delete(petId);
    }

    /**
     * Get all pets
     * @return List of all pets
     */
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    /**
     * Get available pets with optional filters
     * @param search Optional search term for name or breed
     * @param petType Optional pet type filter
     * @param ageGroup Optional age group filter
     * @return List of matching pets
     */
    public List<Pet> getAvailablePets(String search, String petType, String ageGroup) {
        return petRepository.findAvailablePets(search, petType, ageGroup);
    }

    /**
     * Update a pet's status
     * @param petId the ID of the pet
     * @param status the new status
     * @return true if update successful, false otherwise
     */
    public boolean updatePetStatus(int petId, String status) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            return false;
        }

        pet.setStatus(status);
        return petRepository.update(pet);
    }

    /**
     * Get total number of pets
     * @return Total number of pets
     */
    public int getTotalPetsCount() {
        return petRepository.countAllPets();
    }

    /**
     * Get number of available pets
     * @return Number of available pets
     */
    public int getAvailablePetsCount() {
        return petRepository.countAvailablePets();
    }

    /**
     * Get most recent pets
     * @param limit Maximum number of pets to return
     * @return List of recent pets
     */
    public List<Pet> getRecentPets(int limit) {
        return petRepository.findRecentPets(limit);
    }

    /**
     * Get pets with pagination and optional filters
     * @param search Optional search term for name or breed
     * @param petType Optional pet type filter
     * @param ageGroup Optional age group filter
     * @param page Page number (1-based)
     * @param recordsPerPage Number of records per page
     * @return List of matching pets for the specified page
     */
    public List<Pet> getPetsWithPagination(String search, String petType, String ageGroup, int page, int recordsPerPage) {
        return petRepository.findPetsWithPagination(search, petType, ageGroup, page, recordsPerPage);
    }

    /**
     * Count pets with optional filters
     * @param search Optional search term for name or breed
     * @param petType Optional pet type filter
     * @param ageGroup Optional age group filter
     * @return Total number of matching pets
     */
    public int countPetsWithFilters(String search, String petType, String ageGroup) {
        return petRepository.countPetsWithFilters(search, petType, ageGroup);
    }

    /**
     * Get all pets with pagination
     * @param offset Starting index
     * @param limit Maximum number of pets to return
     * @return List of pets
     */
    public List<Pet> getAllPets(int offset, int limit) {
        return petRepository.findAllWithPagination(offset, limit);
    }
}