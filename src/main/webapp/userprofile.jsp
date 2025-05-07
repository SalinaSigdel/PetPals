<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.snapgramfx.petpals.model.User" %>
<%@ page import="com.snapgramfx.petpals.model.Notification" %>
<%@ page import="com.snapgramfx.petpals.model.AdoptionApplication" %>
<%@ page import="com.snapgramfx.petpals.model.Adoption" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/includes/header.jsp">
  <jsp:param name="title" value="User Profile" />
  <jsp:param name="activePage" value="profile" />
  <jsp:param name="extraHead" value="/WEB-INF/includes/profile-styles.jsp" />
</jsp:include>


  <%
  // Check if user is logged in
  Object userIdObj = session.getAttribute("userId");
  Object userRoleObj = session.getAttribute("userRole");
  String userRole = (userRoleObj != null) ? userRoleObj.toString() : null;
  boolean isLoggedIn = (userIdObj != null);
  boolean isAdmin = (userIdObj != null && "admin".equals(userRole));

  // Redirect if not logged in
  if (!isLoggedIn) {
    response.sendRedirect("login");
    return;
  }

  // Get user information from request attributes
  User user = (User) request.getAttribute("user");

  // If user is null, try to get it from the UserService
  if (user == null && userIdObj != null) {
    com.snapgramfx.petpals.service.UserService userService = new com.snapgramfx.petpals.service.UserService();
    user = userService.getUserById((Integer) userIdObj);
  }

  // If still null, redirect to login
  if (user == null) {
    session.invalidate();
    response.sendRedirect("login");
    return;
  }

  // User information
  String userName = user.getFullName();
  String userEmail = user.getEmail();
  String userPhone = user.getPhone() != null ? user.getPhone() : "";
  String userAddress = user.getAddress() != null ? user.getAddress() : "";
  String memberSince = new java.text.SimpleDateFormat("MMMM d, yyyy").format(user.getCreatedAt());

  // Get notifications from request attributes
  List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
  int notificationCount = 0;

  // If notifications is null, try to get them from the NotificationService
  if (notifications == null && userIdObj != null) {
    com.snapgramfx.petpals.service.NotificationService notificationService = new com.snapgramfx.petpals.service.NotificationService();
    notifications = notificationService.getNotificationsByUserId((Integer) userIdObj);
  }

  if (notifications != null) {
    for (Notification notification : notifications) {
      if (!notification.isRead()) {
        notificationCount++;
      }
    }
  }

  // Get adoption applications from request attributes
  List<AdoptionApplication> applications = (List<AdoptionApplication>) request.getAttribute("applications");

  // If applications is null, try to get them from the AdoptionApplicationService
  if (applications == null && userIdObj != null) {
    com.snapgramfx.petpals.service.AdoptionApplicationService applicationService = new com.snapgramfx.petpals.service.AdoptionApplicationService();
    applications = applicationService.getApplicationsByUserId((Integer) userIdObj);
  }

  // Get completed adoptions from request attributes
  List<Adoption> adoptions = (List<Adoption>) request.getAttribute("adoptions");

  // If adoptions is null, try to get them from the AdoptionService
  if (adoptions == null && userIdObj != null) {
    com.snapgramfx.petpals.service.AdoptionService adoptionService = new com.snapgramfx.petpals.service.AdoptionService();
    adoptions = adoptionService.getAdoptionsByUser((Integer) userIdObj);
  }
  %>



  <div class="profile-container">
    <% if (!isLoggedIn) { %>
    <div class="profile-card">
      <div class="profile-card-header">
        <h2>Please Log In</h2>
      </div>
      <p>You need to be logged in to view your profile. Please <a href="login">log in</a> or <a href="register">create an account</a>.</p>
    </div>
    <% } else { %>
    <%
    // Check for success message in request attributes first, then parameters
    String successMessage = (String) request.getAttribute("successMessage");
    if (successMessage == null) {
        successMessage = request.getParameter("success");
    }
    if (successMessage != null && !successMessage.isEmpty()) {
    %>
    <div class="alert alert-success">
      <i class="fas fa-check-circle"></i> <%= successMessage %>
    </div>
    <% } %>

    <div class="profile-header">
      <h1>Welcome back, <%= userName %></h1>
      <p class="member-since"><i class="fas fa-calendar-alt"></i> Member since <%= memberSince %></p>
    </div>

    <div class="profile-tabs">
      <div class="profile-tab active" data-tab="profile">
        <i class="fas fa-user-circle"></i> Profile Information
      </div>
      <div class="profile-tab" data-tab="notifications">
        <i class="fas fa-bell"></i> Notifications
        <% if (notificationCount > 0) { %>
        <span class="notification-badge"><%= notificationCount %></span>
        <% } %>
      </div>
      <div class="profile-tab" data-tab="adoptions">
        <i class="fas fa-paw"></i> My Adoptions
      </div>
    </div>

    <div class="profile-tab-content active" id="profile-tab">
      <div class="profile-card">
        <div class="profile-card-header">
          <h2><i class="fas fa-user"></i> Personal Information</h2>
          <button class="btn btn-outline" id="editProfileBtn"><i class="fas fa-edit"></i> Edit Profile</button>
        </div>

        <!-- View mode -->
        <div class="profile-info" id="profile-info-view">
          <div class="profile-info-item">
            <span class="profile-info-label"><i class="fas fa-user-tag"></i> Full Name</span>
            <span class="profile-info-value" id="view-fullName"><%= userName %></span>
          </div>
          <div class="profile-info-item">
            <span class="profile-info-label"><i class="fas fa-envelope"></i> Email Address</span>
            <span class="profile-info-value" id="view-email"><%= userEmail %></span>
          </div>
          <div class="profile-info-item">
            <span class="profile-info-label"><i class="fas fa-phone"></i> Phone Number</span>
            <span class="profile-info-value" id="view-phone"><%= userPhone.isEmpty() ? "Not provided" : userPhone %></span>
          </div>
          <div class="profile-info-item">
            <span class="profile-info-label"><i class="fas fa-map-marker-alt"></i> Address</span>
            <span class="profile-info-value" id="view-address"><%= userAddress.isEmpty() ? "Not provided" : userAddress %></span>
          </div>
        </div>

        <!-- Edit mode (initially hidden) -->
        <div class="profile-info" id="profile-info-edit" style="display: none;">
          <form id="profile-edit-form">
            <div class="profile-info-item">
              <label for="edit-fullName" class="profile-info-label"><i class="fas fa-user-tag"></i> Full Name</label>
              <input type="text" id="edit-fullName" name="fullName" class="form-control" value="<%= userName %>" required>
            </div>
            <div class="profile-info-item">
              <label for="edit-email" class="profile-info-label"><i class="fas fa-envelope"></i> Email Address</label>
              <input type="email" id="edit-email" name="email" class="form-control" value="<%= userEmail %>" required>
            </div>
            <div class="profile-info-item">
              <label for="edit-phone" class="profile-info-label"><i class="fas fa-phone"></i> Phone Number</label>
              <input type="tel" id="edit-phone" name="phone" class="form-control" value="<%= userPhone %>">
            </div>
            <div class="profile-info-item">
              <label for="edit-address" class="profile-info-label"><i class="fas fa-map-marker-alt"></i> Address</label>
              <textarea id="edit-address" name="address" class="form-control" rows="3"><%= userAddress %></textarea>
            </div>

            <div class="form-actions">
              <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Save Changes</button>
              <button type="button" id="cancel-edit" class="btn btn-outline"><i class="fas fa-times"></i> Cancel</button>
            </div>
          </form>
        </div>
      </div>

      <div class="profile-card">
        <div class="profile-card-header">
          <h2><i class="fas fa-lock"></i> Change Password</h2>
        </div>
        <div class="profile-info">
          <form id="password-change-form">
            <div class="profile-info-item">
              <label for="current-password" class="profile-info-label"><i class="fas fa-key"></i> Current Password</label>
              <input type="password" id="current-password" name="currentPassword" class="form-control" required>
            </div>
            <div class="profile-info-item">
              <label for="new-password" class="profile-info-label"><i class="fas fa-key"></i> New Password</label>
              <input type="password" id="new-password" name="newPassword" class="form-control" required>
            </div>
            <div class="profile-info-item">
              <label for="confirm-password" class="profile-info-label"><i class="fas fa-check-circle"></i> Confirm New Password</label>
              <input type="password" id="confirm-password" name="confirmPassword" class="form-control" required>
            </div>
            <div class="form-actions">
              <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Update Password</button>
            </div>
          </form>
        </div>
      </div>

      <div class="profile-card">
        <div class="profile-card-header">
          <h2 class="danger-zone"><i class="fas fa-exclamation-triangle"></i> Danger Zone</h2>
        </div>
        <p class="danger-description">Once you delete your account, there is no going back. Please be certain.</p>
        <button class="btn btn-danger" id="deleteAccountBtn"><i class="fas fa-trash-alt"></i> Delete My Account</button>
      </div>
    </div>

    <div class="profile-tab-content" id="notifications-tab">
      <div class="profile-card">
        <div class="profile-card-header">
          <h2>Your Notifications</h2>
          <button class="btn btn-outline" id="markAllReadBtn">Mark All as Read</button>
        </div>

        <div class="notifications-container">
          <%
          if (notifications != null && !notifications.isEmpty()) {
            for (Notification notification : notifications) {
              String notificationType = notification.getType();
              String notificationMessage = notification.getMessage();
              boolean isRead = notification.isRead();
              String createdAt = new java.text.SimpleDateFormat("MMM d, yyyy 'at' h:mm a").format(notification.getCreatedAt());

              // Determine notification style based on type
              String notificationClass = "notification-item";
              String iconClass = "fas fa-bell";

              if ("success".equals(notificationType)) {
                notificationClass += " notification-success";
                iconClass = "fas fa-check-circle";
              } else if ("rejected".equals(notificationType)) {
                notificationClass += " notification-rejected";
                iconClass = "fas fa-times-circle";
              } else if ("pending".equals(notificationType)) {
                notificationClass += " notification-pending";
                iconClass = "fas fa-clock";
              }
          %>
          <div class="<%= notificationClass %>" data-notification-id="<%= notification.getNotificationId() %>">
            <div class="notification-icon">
              <i class="<%= iconClass %>"></i>
            </div>
            <div class="notification-content">
              <h4><%= notification.getTitle() %></h4>
              <p><%= notificationMessage %></p>
              <span class="notification-time"><%= createdAt %></span>
            </div>
            <button class="notification-dismiss" data-notification-id="<%= notification.getNotificationId() %>">
              <i class="fas fa-times"></i>
            </button>
          </div>
          <%
            }
          } else {
          %>
          <p>You don't have any notifications yet.</p>
          <%
          }
          %>
        </div>
      </div>
    </div>

    <div class="profile-tab-content" id="adoptions-tab">
      <div class="profile-card">
        <div class="profile-card-header">
          <h2>Your Adoption Applications</h2>
        </div>

        <div class="adoption-container">
          <% if (applications != null && !applications.isEmpty()) { %>
          <ul class="adoption-list">
          <%
            for (AdoptionApplication app : applications) {
              String status = app.getStatus();
              String petName = app.getPetName();

              // Default image if none provided
              String petImage = "https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60";

              // Format application date
              String applicationDate = new java.text.SimpleDateFormat("MMM d, yyyy").format(app.getCreatedAt());

              // Status CSS class
              String statusClass = "";
              if ("pending".equalsIgnoreCase(status)) {
                statusClass = "status-pending";
              } else if ("approved".equalsIgnoreCase(status)) {
                statusClass = "status-approved";
              } else if ("rejected".equalsIgnoreCase(status)) {
                statusClass = "status-rejected";
              }
          %>
            <li class="adoption-item">
              <div class="adoption-header">
                <div class="adoption-pet-info">
                  <img src="<%= petImage %>" alt="<%= petName %>" class="adoption-pet-image">
                  <div>
                    <h4 class="adoption-pet-name"><%= petName %></h4>
                    <p class="adoption-pet-breed"></p>
                  </div>
                </div>
                <span class="application-status <%= statusClass %>"><%= status %></span>
              </div>
              <div class="adoption-details">
                <p class="adoption-date">Applied on <%= applicationDate %></p>
                <% if ("pending".equalsIgnoreCase(status)) { %>
                <p>Your application is being reviewed. We'll notify you once there's an update.</p>
                <% } else if ("approved".equalsIgnoreCase(status)) { %>
                <p>Congratulations! Your application has been approved. Please check your email for next steps.</p>
                <% } else if ("rejected".equalsIgnoreCase(status)) { %>
                <p>We're sorry, but your application wasn't approved.
                   <% if (app.getRejectionReason() != null && !app.getRejectionReason().isEmpty()) { %>
                   Reason: <%= app.getRejectionReason() %>
                   <% } else { %>
                   Please contact us for more information.
                   <% } %>
                </p>
                <% } %>
              </div>
            </li>
          <%
            }
          %>
          </ul>
          <% } else { %>
            <p>You haven't submitted any adoption applications yet. <a href="adopt">Find a pet</a> to start the adoption process.</p>
          <% } %>
        </div>
      </div>
    </div>
    <% } %>
  </div>



  <script>
    document.addEventListener('DOMContentLoaded', function() {
      // Tab functionality
      const tabs = document.querySelectorAll('.profile-tab');
      const tabContents = document.querySelectorAll('.profile-tab-content');

      tabs.forEach(tab => {
        tab.addEventListener('click', function() {
          // Remove active class from all tabs and contents
          tabs.forEach(t => t.classList.remove('active'));
          tabContents.forEach(c => c.classList.remove('active'));

          // Add active class to clicked tab and corresponding content
          this.classList.add('active');
          const tabName = this.getAttribute('data-tab');
          document.getElementById(tabName + '-tab').classList.add('active');
        });
      });

      // Edit profile button
      const editProfileBtn = document.getElementById('editProfileBtn');
      if (editProfileBtn) {
        editProfileBtn.addEventListener('click', function() {
          // Toggle between view and edit modes
          document.getElementById('profile-info-view').style.display = 'none';
          document.getElementById('profile-info-edit').style.display = 'block';
          this.style.display = 'none';
        });
      }

      // Cancel edit button
      const cancelEditBtn = document.getElementById('cancel-edit');
      if (cancelEditBtn) {
        cancelEditBtn.addEventListener('click', function() {
          // Toggle back to view mode
          document.getElementById('profile-info-view').style.display = 'block';
          document.getElementById('profile-info-edit').style.display = 'none';
          document.getElementById('editProfileBtn').style.display = 'block';

          // Reset form
          document.getElementById('profile-edit-form').reset();
        });
      }

      // Profile edit form submission
      const profileEditForm = document.getElementById('profile-edit-form');
      if (profileEditForm) {
        profileEditForm.addEventListener('submit', function(e) {
          e.preventDefault();

          // Get form data
          const formData = new FormData(this);

          // Convert FormData to URL-encoded string
          const urlEncodedData = new URLSearchParams(formData).toString();

          // Send AJAX request
          const xhr = new XMLHttpRequest();
          xhr.open('POST', 'update-profile');
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          xhr.onload = function() {
            if (xhr.status === 200) {
              try {
                const response = JSON.parse(xhr.responseText);

                if (response.success) {
                  // Update view mode with new values
                  document.getElementById('view-fullName').textContent = formData.get('fullName');
                  document.getElementById('view-email').textContent = formData.get('email');
                  document.getElementById('view-phone').textContent = formData.get('phone') || 'Not provided';
                  document.getElementById('view-address').textContent = formData.get('address') || 'Not provided';

                  // Switch back to view mode
                  document.getElementById('profile-info-view').style.display = 'block';
                  document.getElementById('profile-info-edit').style.display = 'none';
                  document.getElementById('editProfileBtn').style.display = 'block';

                  // Show success message
                  alert('Profile updated successfully');
                } else {
                  alert(response.message || 'Failed to update profile');
                }
              } catch (e) {
                alert('An error occurred while processing the response');
              }
            } else {
              alert('An error occurred while updating your profile');
            }
          };
          xhr.onerror = function() {
            alert('An error occurred while updating your profile');
          };
          xhr.send(urlEncodedData);
        });
      }

      // Password change form submission
      const passwordChangeForm = document.getElementById('password-change-form');
      if (passwordChangeForm) {
        passwordChangeForm.addEventListener('submit', function(e) {
          e.preventDefault();

          // Validate form
          const newPassword = document.getElementById('new-password').value;
          const confirmPassword = document.getElementById('confirm-password').value;

          if (newPassword !== confirmPassword) {
            alert('New password and confirmation do not match');
            return;
          }

          // Get form data
          const formData = new FormData(this);

          // Convert FormData to URL-encoded string
          const urlEncodedData = new URLSearchParams(formData).toString();

          // Send AJAX request
          const xhr = new XMLHttpRequest();
          xhr.open('POST', 'update-profile');
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          xhr.onload = function() {
            if (xhr.status === 200) {
              try {
                const response = JSON.parse(xhr.responseText);

                if (response.success) {
                  // Reset form
                  passwordChangeForm.reset();

                  // Show success message
                  alert('Password updated successfully');
                } else {
                  alert(response.message || 'Failed to update password');
                }
              } catch (e) {
                alert('An error occurred while processing the response');
              }
            } else {
              alert('An error occurred while updating your password');
            }
          };
          xhr.onerror = function() {
            alert('An error occurred while updating your password');
          };
          xhr.send(urlEncodedData);
        });
      }

      // Delete account button
      const deleteAccountBtn = document.getElementById('deleteAccountBtn');
      if (deleteAccountBtn) {
        deleteAccountBtn.addEventListener('click', function() {
          if (confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
            // Submit form to delete account
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'DeleteAccountServlet';
            document.body.appendChild(form);
            form.submit();
          }
        });
      }

      // Mark all notifications as read
      const markAllReadBtn = document.getElementById('markAllReadBtn');
      if (markAllReadBtn) {
        markAllReadBtn.addEventListener('click', function() {
          // Hide all notifications visually
          const notificationItems = document.querySelectorAll('.notification-item');
          const notificationsContainer = document.querySelector('.notifications-container');

          notificationItems.forEach(item => {
            item.style.opacity = '0';
            setTimeout(() => {
              item.style.display = 'none';
            }, 300);
          });

          // Show "no notifications" message after all are hidden
          setTimeout(() => {
            if (notificationsContainer) {
              notificationsContainer.innerHTML = '<p>You don\'t have any notifications yet.</p>';
            }
          }, 350);

          // Hide the notification badge
          const badge = document.querySelector('.notification-badge');
          if (badge) {
            badge.style.display = 'none';
          }

          // Submit form to mark all notifications as read
          const form = document.createElement('form');
          form.method = 'POST';
          form.action = 'MarkNotificationsReadServlet';
          document.body.appendChild(form);
          form.submit();
        });
      }

      // Dismiss individual notifications
      const dismissBtns = document.querySelectorAll('.notification-dismiss');
      dismissBtns.forEach(btn => {
        btn.addEventListener('click', function() {
          const notificationId = this.getAttribute('data-notification-id');
          const notificationElement = this.closest('.notification-item, [data-notification-id="' + notificationId + '"]');

          // Remove the notification visually
          if (notificationElement) {
            notificationElement.style.opacity = '0';
            setTimeout(() => {
              notificationElement.style.display = 'none';

              // Check if this was the last visible notification
              const visibleNotifications = document.querySelectorAll('.notification-item:not([style*="display: none"])');
              if (visibleNotifications.length === 0) {
                // Show "no notifications" message
                const notificationsContainer = document.querySelector('.notifications-container');
                if (notificationsContainer) {
                  notificationsContainer.innerHTML = '<p>You don\'t have any notifications yet.</p>';
                }
              }
            }, 300);

            // Update notification count badge
            const badge = document.querySelector('.notification-badge');
            if (badge) {
              const currentCount = parseInt(badge.textContent);
              if (currentCount > 1) {
                badge.textContent = currentCount - 1;
              } else {
                badge.style.display = 'none';
              }
            }
          }

          // Send request to mark notification as read
          const xhr = new XMLHttpRequest();
          xhr.open('POST', 'DismissNotificationServlet');
          xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          xhr.send('notificationId=' + notificationId);
        });
      });
    });
  </script>

<jsp:include page="/WEB-INF/includes/footer.jsp" />