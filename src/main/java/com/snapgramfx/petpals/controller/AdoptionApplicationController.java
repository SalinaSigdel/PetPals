package com.snapgramfx.petpals.controller;

import com.snapgramfx.petpals.model.AdoptionApplication;
import com.snapgramfx.petpals.model.Pet;
import com.snapgramfx.petpals.model.User;
import com.snapgramfx.petpals.service.AdoptionApplicationService;
import com.snapgramfx.petpals.service.PetService;
import com.snapgramfx.petpals.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling adoption application-related HTTP requests
 */
@WebServlet(name = "adoptionController", urlPatterns = {
        "/adopt",
        "/adoption-form",
        "/my-applications",
        "/application-details",
        "/adoption-details",
        "/approve-application",
        "/reject-application"
})
public class AdoptionApplicationController extends HttpServlet {
    private final AdoptionApplicationService applicationService;
    private final PetService petService;
    private final UserService userService;

    public AdoptionApplicationController() {
        this.applicationService = new AdoptionApplicationService();
        this.petService = new PetService();
        this.userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/adopt":
                // Show adoption page with available pets
                handleAdoptPage(request, response);
                break;
            case "/adoption-form":
                // Show adoption application form
                handleAdoptionForm(request, response);
                break;
            case "/my-applications":
                // Show user's applications
                handleMyApplications(request, response);
                break;
            case "/application-details":
            case "/adoption-details":
                // Show application details
                handleApplicationDetails(request, response);
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
            case "/adoption-form":
                // Handle adoption form submission
                handleAdoptionFormSubmission(request, response);
                break;
            case "/approve-application":
                // Handle application approval
                handleApproveApplication(request, response);
                break;
            case "/reject-application":
                // Handle application rejection
                handleRejectApplication(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/");
                break;
        }
    }

    /**
     * Handle adoption page display
     */
    private void handleAdoptPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get filter parameters
        String search = request.getParameter("search");
        String petType = request.getParameter("petType");
        String ageGroup = request.getParameter("ageGroup");

        // Get pagination parameters
        int page = 1;
        int recordsPerPage = 6; // Number of pets per page

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                // If page parameter is invalid, default to page 1
                page = 1;
            }
        }

        // Get total number of pets matching the filters
        int totalPets = petService.countPetsWithFilters(search, petType, ageGroup);

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalPets / recordsPerPage);

        // If requested page is greater than total pages, set to last page
        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }

        // Get pets for the current page
        List<Pet> pets = petService.getPetsWithPagination(search, petType, ageGroup, page, recordsPerPage);

        // Set attributes for the view
        request.setAttribute("pets", pets);
        request.setAttribute("search", search);
        request.setAttribute("petType", petType);
        request.setAttribute("ageGroup", ageGroup);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("recordsPerPage", recordsPerPage);
        request.setAttribute("totalPets", totalPets);

        // Forward to the view
        request.getRequestDispatcher("/adopt.jsp").forward(request, response);
    }

    /**
     * Handle adoption form display
     */
    private void handleAdoptionForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String petIdParam = request.getParameter("petId");

        if (petIdParam == null || petIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/adopt");
            return;
        }

        try {
            int petId = Integer.parseInt(petIdParam);
            Pet pet = petService.getPetById(petId);

            if (pet != null && "available".equals(pet.getStatus())) {
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("/adoption-form.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/adopt");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/adopt");
        }
    }

    /**
     * Handle adoption form submission
     */
    private void handleAdoptionFormSubmission(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String petIdParam = request.getParameter("petId");

        if (petIdParam == null || petIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/adopt");
            return;
        }

        try {
            int petId = Integer.parseInt(petIdParam);
            Pet pet = petService.getPetById(petId);
            User user = userService.getUserById(userId);

            if (pet == null || !"available".equals(pet.getStatus()) || user == null) {
                response.sendRedirect(request.getContextPath() + "/adopt");
                return;
            }

            // Get form parameters
            String applicantName = request.getParameter("applicantName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String cityState = request.getParameter("cityState");
            String reasonForAdoption = request.getParameter("reasonForAdoption");

            // Validate input
            if (applicantName == null || applicantName.trim().isEmpty() ||
                    email == null || email.trim().isEmpty() ||
                    phone == null || phone.trim().isEmpty() ||
                    cityState == null || cityState.trim().isEmpty() ||
                    reasonForAdoption == null || reasonForAdoption.trim().isEmpty()) {

                request.setAttribute("errorMessage", "All fields are required");
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("/adoption-form.jsp").forward(request, response);
                return;
            }

            // Create application object
            AdoptionApplication application = new AdoptionApplication();
            application.setUserId(userId);
            application.setPetId(petId);
            application.setStatus("pending");
            application.setApplicantName(applicantName);
            application.setEmail(email);
            application.setPhone(phone);
            application.setCityState(cityState);
            application.setReasonForAdoption(reasonForAdoption);

            // Submit application
            boolean success = applicationService.submitApplication(application);

            if (success) {
                request.setAttribute("successMessage", "Your adoption application has been submitted successfully!");
                request.getRequestDispatcher("/my-applications.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Failed to submit application");
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("/adoption-form.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/adopt");
        }
    }

    /**
     * Handle my applications display
     */
    private void handleMyApplications(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        // Get pagination parameters
        int page = 1;
        int recordsPerPage = 5; // Number of applications per page

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                // If page parameter is invalid, default to page 1
                page = 1;
            }
        }

        // Get total number of applications for this user
        int totalApplications = applicationService.getApplicationsCountByUserId(userId);

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalApplications / recordsPerPage);

        // If requested page is greater than total pages, set to last page
        if (totalPages > 0 && page > totalPages) {
            page = totalPages;
        }

        // Get applications for the current page
        List<AdoptionApplication> applications = applicationService.getApplicationsByUserIdWithPagination(
                userId, (page - 1) * recordsPerPage, recordsPerPage);

        // Set attributes for the view
        request.setAttribute("applications", applications);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalApplications", totalApplications);

        // Forward to the view
        request.getRequestDispatcher("/my-applications.jsp").forward(request, response);
    }

    /**
     * Handle application details display
     */
    private void handleApplicationDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String applicationIdParam = request.getParameter("id");

        if (applicationIdParam == null || applicationIdParam.trim().isEmpty()) {
            if ("admin".equals(session.getAttribute("userRole"))) {
                response.sendRedirect(request.getContextPath() + "/admin-dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/my-applications");
            }
            return;
        }

        try {
            int applicationId = Integer.parseInt(applicationIdParam);
            AdoptionApplication application = applicationService.getApplicationById(applicationId);

            if (application != null) {
                // Check if user is authorized to view this application
                int userId = (int) session.getAttribute("userId");
                String userRole = (String) session.getAttribute("userRole");

                if (application.getUserId() == userId || "admin".equals(userRole)) {
                    // Get related pet
                    Pet pet = petService.getPetById(application.getPetId());

                    request.setAttribute("application", application);
                    request.setAttribute("pet", pet);

                    // Determine which JSP to use based on the servlet path
                    String jspPath = "/application-details.jsp";
                    if ("/adoption-details".equals(request.getServletPath())) {
                        jspPath = "/adoption-details.jsp";
                    }

                    request.getRequestDispatcher(jspPath).forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/my-applications");
                }
            } else {
                if ("admin".equals(session.getAttribute("userRole"))) {
                    response.sendRedirect(request.getContextPath() + "/admin-dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/my-applications");
                }
            }
        } catch (NumberFormatException e) {
            if ("admin".equals(session.getAttribute("userRole"))) {
                response.sendRedirect(request.getContextPath() + "/admin-dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/my-applications");
            }
        }
    }

    /**
     * Handle application approval
     */
    private void handleApproveApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String applicationIdParam = request.getParameter("id");

        if (applicationIdParam == null || applicationIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
            return;
        }

        try {
            int applicationId = Integer.parseInt(applicationIdParam);
            applicationService.updateApplicationStatus(applicationId, "approved", null);

            // Redirect to admin dashboard
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
        }
    }

    /**
     * Handle application rejection
     */
    private void handleRejectApplication(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is admin
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userRole") == null || !"admin".equals(session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String applicationIdParam = request.getParameter("id");
        String rejectionReason = request.getParameter("rejectionReason");

        if (applicationIdParam == null || applicationIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
            return;
        }

        try {
            int applicationId = Integer.parseInt(applicationIdParam);
            applicationService.updateApplicationStatus(applicationId, "rejected", rejectionReason);

            // Redirect to admin dashboard
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin-dashboard");
        }
    }
}