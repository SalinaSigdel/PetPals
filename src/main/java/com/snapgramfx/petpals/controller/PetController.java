package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.Pet;
import com.snapgramfx.petpals.service.PetService;
import com.snapgramfx.petpals.util.FileUploadUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling pet-related HTTP requests
 */
@WebServlet(name = "petController", urlPatterns = {
        "/pets",
        "/pet-details",
        "/add-pet",
        "/edit-pet",
        "/delete-pet",
        "/browse-pets"  // Changed from "/adopt" to avoid conflict
})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 5 * 1024 * 1024,   // 5 MB
        maxRequestSize = 10 * 1024 * 1024 // 10 MB
)
public class PetController extends HttpServlet {
    private final PetService petService;

    public PetController() {
        this.petService = new PetService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/pets":
                // Show all pets or filtered pets
                handlePetList(request, response);
                break;
            case "/pet-details":
                // Show pet details
                handlePetDetails(request, response);
                break;
            case "/add-pet":
                // Show add pet form
                handleAddPetForm(request, response);
                break;
            case "/edit-pet":
                // Show edit pet form
                handleEditPetForm(request, response);
                break;
            case "/delete-pet":
                // Handle pet deletion
                handleDeletePet(request, response);
                break;
            case "/browse-pets":
                // Show pet browsing page
                handleBrowsePetsPage(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/add-pet":
                // Handle add pet form submission
                handleAddPet(request, response);
                break;
            case "/edit-pet":
                // Handle edit pet form submission
                handleEditPet(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
                break;
        }
    }

    /**
     * Handle pet list display with optional filters and pagination
     */
    private void handlePetList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get filter parameters
        String search = request.getParameter("search");
        String petType = request.getParameter("petType");
        String ageGroup = request.getParameter("ageGroup");

        // Get pagination parameters
        int page = 1;
        int recordsPerPage = 8;

        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            if (request.getParameter("recordsPerPage") != null) {
                recordsPerPage = Integer.parseInt(request.getParameter("recordsPerPage"));
            }
        } catch (NumberFormatException e) {
            // Use default values if parameters are invalid
        }

        // Get pets based on filters and pagination
        List<Pet> pets = petService.getPetsWithPagination(search, petType, ageGroup, page, recordsPerPage);

        // Get total records count for pagination
        int totalRecords = petService.countPetsWithFilters(search, petType, ageGroup);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Set attributes for the view
        request.setAttribute("pets", pets);
        request.setAttribute("search", search);
        request.setAttribute("petType", petType);
        request.setAttribute("ageGroup", ageGroup);
        request.setAttribute("currentPage", page);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        // Forward to the view
        request.getRequestDispatcher("/pet-list.jsp").forward(request, response);
    }

    /**
     * Handle pet details display
     */
    private void handlePetDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String petIdParam = request.getParameter("id");

        if (petIdParam == null || petIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/pets");
            return;
        }

        try {
            int petId = Integer.parseInt(petIdParam);
            Pet pet = petService.getPetById(petId);

            if (pet != null) {
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("/pet-details.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/pets");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/pets");
        }
    }

    /**
     * Show add pet form
     */
    private void handleAddPetForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.getRequestDispatcher("/add-pet.jsp").forward(request, response);
    }

    /**
     * Handle add pet form submission
     */
    private void handleAddPet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get form parameters
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String breed = request.getParameter("breed");
        String age = request.getParameter("age");
        String gender = request.getParameter("gender");
        String weight = request.getParameter("weight");
        String description = request.getParameter("description");
        String badge = request.getParameter("badge");
        String isAvailableParam = request.getParameter("isAvailable");
        boolean isAvailable = isAvailableParam != null && isAvailableParam.equals("true");

        // Validate input
        if (name == null || name.trim().isEmpty() ||
                type == null || type.trim().isEmpty() ||
                gender == null || gender.trim().isEmpty()) {

            request.setAttribute("errorMessage", "Name, type, and gender are required");
            request.getRequestDispatcher("/add-pet.jsp").forward(request, response);
            return;
        }

        // Handle file upload
        String imageUrl = null;
        try {
            imageUrl = FileUploadUtil.uploadFile(request, "petImage", "pets");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Error uploading image: " + e.getMessage());
            request.getRequestDispatcher("/add-pet.jsp").forward(request, response);
            return;
        }

        // Create pet object
        Pet pet = new Pet();
        pet.setName(name);
        pet.setType(type);
        pet.setBreed(breed);
        pet.setAge(age);
        pet.setGender(gender);
        pet.setWeight(weight);
        pet.setDescription(description);
        pet.setImageUrl(imageUrl);
        pet.setStatus(isAvailable ? "available" : "adopted");
        pet.setBadge(badge);

        // Save pet
        boolean success = petService.addPet(pet);

        if (success) {
            // Redirect to admin-pets page with success message
            response.sendRedirect(request.getContextPath() + "/admin-pets?success=Pet+added+successfully");
        } else {
            // If pet save failed, delete the uploaded image
            if (imageUrl != null) {
                FileUploadUtil.deleteFile(request, imageUrl);
            }
            request.setAttribute("errorMessage", "Failed to add pet");
            request.getRequestDispatcher("/add-pet.jsp").forward(request, response);
        }
    }

    /**
     * Show edit pet form
     */
    private void handleEditPetForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String petIdParam = request.getParameter("id");

        if (petIdParam == null || petIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
            return;
        }

        try {
            int petId = Integer.parseInt(petIdParam);
            Pet pet = petService.getPetById(petId);

            if (pet != null) {
                // Set pet object as request attribute
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("/edit-pet.jsp").forward(request, response);
            } else {
                // Pet not found
                request.setAttribute("errorMessage", "Pet not found");
                request.getRequestDispatcher("/edit-pet.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
        }
    }

    /**
     * Handle edit pet form submission
     */
    private void handleEditPet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String petIdParam = request.getParameter("id");

        if (petIdParam == null || petIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
            return;
        }

        try {
            int petId = Integer.parseInt(petIdParam);
            Pet pet = petService.getPetById(petId);

            if (pet == null) {
                request.setAttribute("errorMessage", "Pet not found");
                request.getRequestDispatcher("/edit-pet.jsp").forward(request, response);
                return;
            }

            // Get form parameters
            String name = request.getParameter("name");
            String type = request.getParameter("type");
            String breed = request.getParameter("breed");
            String age = request.getParameter("age");
            String gender = request.getParameter("gender");
            String weight = request.getParameter("weight");
            String description = request.getParameter("description");
            String currentImageUrl = request.getParameter("currentImageUrl");
            String status = request.getParameter("status");
            String badge = request.getParameter("badge");

            // Validate input
            if (name == null || name.trim().isEmpty() ||
                    type == null || type.trim().isEmpty() ||
                    gender == null || gender.trim().isEmpty() ||
                    status == null || status.trim().isEmpty()) {

                request.setAttribute("errorMessage", "Name, type, gender, and status are required");
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("/edit-pet.jsp").forward(request, response);
                return;
            }

            // Handle file upload
            String imageUrl = currentImageUrl;
            try {
                String newImageUrl = FileUploadUtil.uploadFile(request, "petImage", "pets");
                if (newImageUrl != null) {
                    // If a new image was uploaded, delete the old one if it exists
                    if (currentImageUrl != null && !currentImageUrl.isEmpty() && !currentImageUrl.startsWith("http")) {
                        FileUploadUtil.deleteFile(request, currentImageUrl);
                    }
                    imageUrl = newImageUrl;
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Error uploading image: " + e.getMessage());
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("/edit-pet.jsp").forward(request, response);
                return;
            }

            // Update pet object
            pet.setName(name);
            pet.setType(type);
            pet.setBreed(breed);
            pet.setAge(age);
            pet.setGender(gender);
            pet.setWeight(weight);
            pet.setDescription(description);
            pet.setImageUrl(imageUrl);
            pet.setStatus(status);
            pet.setBadge(badge);

            // Save changes
            boolean success = petService.updatePet(pet);

            if (success) {
                // Redirect to admin-pets page with success message
                response.sendRedirect(request.getContextPath() + "/admin-pets?success=Pet+updated+successfully");
            } else {
                // If update failed and a new image was uploaded, delete it
                if (imageUrl != null && !imageUrl.equals(currentImageUrl)) {
                    FileUploadUtil.deleteFile(request, imageUrl);
                }
                request.setAttribute("errorMessage", "Failed to update pet");
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("/edit-pet.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid pet ID format");
            request.getRequestDispatcher("/edit-pet.jsp").forward(request, response);
        }
    }

    /**
     * Handle pet deletion
     */
    private void handleDeletePet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String petIdParam = request.getParameter("id");

        if (petIdParam == null || petIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
            return;
        }

        try {
            int petId = Integer.parseInt(petIdParam);
            boolean success = petService.deletePet(petId);

            // Redirect to admin-pets page with appropriate message
            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin-pets?success=Pet+deleted+successfully");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin-pets?error=Failed+to+delete+pet");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
        }
    }

    /**
     * Handle the browse pets page
     */
    private void handleBrowsePetsPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get filter parameters
        String petType = request.getParameter("petType");
        String ageGroup = request.getParameter("ageGroup");
        String search = request.getParameter("search");

        // Get pets based on filters
        List<Pet> pets = petService.getAvailablePets(search, petType, ageGroup);

        // Set pets as request attribute
        request.setAttribute("pets", pets);

        // Forward to adopt.jsp
        request.getRequestDispatcher("/adopt.jsp").forward(request, response);
    }
}