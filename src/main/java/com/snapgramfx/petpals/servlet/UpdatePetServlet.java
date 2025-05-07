package com.snapgramfx.petpals.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This servlet is kept for backward compatibility.
 * It redirects to the PetController's edit-pet endpoint.
 */
@WebServlet("/UpdatePetServlet")
public class UpdatePetServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the pet ID from the request
        String petIdParam = request.getParameter("petId");

        // Redirect to the PetController's edit-pet endpoint
        if (petIdParam != null && !petIdParam.trim().isEmpty()) {
            // Copy all parameters from the original request to the new request
            request.setAttribute("id", petIdParam);

            // Forward to the PetController
            request.getRequestDispatcher("edit-pet").forward(request, response);
        } else {
            // No pet ID provided, redirect to admin dashboard
            response.sendRedirect("admin-dashboard");
        }
    }
}
