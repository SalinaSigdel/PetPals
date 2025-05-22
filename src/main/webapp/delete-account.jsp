<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.snapgramfx.petpals.service.UserService" %>
<%@ page import="com.snapgramfx.petpals.model.User" %>

<%
  // Check if user is logged in
  HttpSession userSession = request.getSession(false);
  if (userSession == null || userSession.getAttribute("userId") == null) {
    response.sendRedirect(request.getContextPath() + "/login");
    return;
  }
  
  int userId = (int) userSession.getAttribute("userId");
  
  // Delete the user account
  UserService userService = new UserService();
  boolean success = userService.deleteUser(userId);
  
  if (success) {
    // Invalidate the session
    userSession.invalidate();
    
    // Redirect to home page with success message
    response.sendRedirect(request.getContextPath() + "/login?success=Account+deleted+successfully");
  } else {
    // Redirect back to profile with error message
    response.sendRedirect(request.getContextPath() + "/userprofile?error=Failed+to+delete+account");
  }
%> 