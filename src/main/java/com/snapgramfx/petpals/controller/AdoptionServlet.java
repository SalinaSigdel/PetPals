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

/**
 * Servlet for handling adoption form submissions
 */
@WebServlet(name = "adoptionServlet", urlPatterns = {"/AdoptionServlet"})
public class AdoptionServlet extends HttpServlet {

    private final AdoptionApplicationService applicationService;
    private final PetService petService;
    private final UserService userService;

    public AdoptionServlet() {
        this.applicationService = new AdoptionApplicationService();
        this.petService = new PetService();
        this.userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            request.setAttribute("errorMessage", "You must be logged in to submit an adoption application");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String petIdParam = request.getParameter("petId");

        if (petIdParam == null || petIdParam.trim().isEmpty()) {
            response.sendRedirect("adopt");
            return;
        }

        try {
            int petId = Integer.parseInt(petIdParam);

            // Get pet information
            Pet pet = petService.getPetById(petId);

            if (pet == null) {
                request.setAttribute("errorMessage", "Pet not found");
                request.getRequestDispatcher("adopt").forward(request, response);
                return;
            }

            // Check if pet is available
            if (!"available".equalsIgnoreCase(pet.getStatus())) {
                request.setAttribute("errorMessage", "This pet is no longer available for adoption");
                request.getRequestDispatcher("adopt").forward(request, response);
                return;
            }

            // Get form data
            String applicantName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String cityState = request.getParameter("location");
            String reasonForAdoption = request.getParameter("reason");

            // Validate input
            if (applicantName == null || applicantName.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                cityState == null || cityState.trim().isEmpty() ||
                reasonForAdoption == null || reasonForAdoption.trim().isEmpty()) {

                request.setAttribute("errorMessage", "All fields are required");
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("adoption-form.jsp?petId=" + petId).forward(request, response);
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
                // Set success message in session instead of request
                session.setAttribute("successMessage", "Your adoption application has been submitted successfully!");
                // Redirect to profile servlet instead of forwarding to JSP
                response.sendRedirect("profile");
                return;
            } else {
                request.setAttribute("errorMessage", "Failed to submit application");
                request.setAttribute("pet", pet);
                request.getRequestDispatcher("adoption-form.jsp?petId=" + petId).forward(request, response);
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("adopt");
        }
    }
}
