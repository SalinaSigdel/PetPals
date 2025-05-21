package com.snapgramfx.petpals.service;

/**
 * Service class for retrieving platform statistics
 */
public class StatisticsService {
    private final AdoptionService adoptionService;
    private final ShelterService shelterService;

    public StatisticsService() {
        this.adoptionService = new AdoptionService();
        this.shelterService = new ShelterService();
    }

    /**
     * Get the total number of completed adoptions
     * @return Number of completed adoptions
     */
    public int getCompletedAdoptionsCount() {
        return adoptionService.getCompletedAdoptionsCount();
    }

    /**
     * Get the adoption success rate (percentage)
     * @return Adoption success rate as a percentage
     */
    public double getAdoptionSuccessRate() {
        return adoptionService.getAdoptionSuccessRate();
    }

    /**
     * Get the total number of shelters
     * @return Number of shelters
     */
    public int getShelterCount() {
        return shelterService.getShelterCount();
    }

    /**
     * Get all statistics in a single call
     * @return Object array with [adoptionsCount, successRate, shelterCount]
     */
    public Object[] getAllStatistics() {
        int adoptionsCount = getCompletedAdoptionsCount();
        double successRate = getAdoptionSuccessRate();
        int shelterCount = getShelterCount();

        // If no data yet, use default values
        if (adoptionsCount == 0) {
            adoptionsCount = 500; // Default value
        }

        if (successRate == 0) {
            successRate = 98.0; // Default value
        }

        if (shelterCount == 0) {
            shelterCount = 50; // Default value
        }

        return new Object[]{adoptionsCount, successRate, shelterCount};
    }
}
