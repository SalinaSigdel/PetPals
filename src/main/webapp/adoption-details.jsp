<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // This is a redirect file to handle the adoption-details URL pattern
    // It simply forwards to application-details.jsp
    String id = request.getParameter("id");
    if (id != null && !id.trim().isEmpty()) {
        request.getRequestDispatcher("application-details.jsp").forward(request, response);
    } else {
        response.sendRedirect("admin-dashboard");
    }
%>
