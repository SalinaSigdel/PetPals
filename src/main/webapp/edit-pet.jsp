<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/includes/admin-header.jsp">
  <jsp:param name="title" value="Edit Pet" />
  <jsp:param name="activePage" value="admin-pets" />
</jsp:include>

<style>
  .pet-preview {
    display: flex;
    align-items: center;
    gap: 1.5rem;
    padding: 1rem;
  }

  .pet-image {
    width: 100px;
    height: 100px;
    border-radius: 8px;
    object-fit: cover;
    border: 1px solid #eee;
  }

  .pet-info h3 {
    margin: 0 0 0.5rem 0;
    color: var(--primary);
    font-size: 1.2rem;
  }

  .pet-info p {
    margin: 0 0 0.5rem 0;
    color: #666;
    font-size: 0.95rem;
  }

  .pet-info p:last-child {
    margin-bottom: 0;
  }

  @media (max-width: 768px) {
    .pet-preview {
      flex-direction: column;
      text-align: center;
    }
  }
</style>

<%-- Access control is now handled by the controller --%>

<c:if test="${empty pet}">
  <div class="alert alert-error">
    <i class="fas fa-exclamation-circle"></i> Pet not found or you don't have permission to edit it.
  </div>
  <script>
    setTimeout(function() {
      window.location.href = "admin-pets";
    }, 3000);
  </script>
</c:if>

<c:if test="${not empty pet}">
  <%-- Set default image URL if none is provided --%>
  <c:if test="${empty pet.imageUrl}">
    <c:choose>
      <c:when test="${pet.type eq 'Dog'}">
        <c:set var="defaultImageUrl" value="https://images.unsplash.com/photo-1601979031925-424e53b6caaa?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60" />
      </c:when>
      <c:when test="${pet.type eq 'Cat'}">
        <c:set var="defaultImageUrl" value="https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60" />
      </c:when>
      <c:when test="${pet.type eq 'Bird'}">
        <c:set var="defaultImageUrl" value="https://images.unsplash.com/photo-1552728089-57bdde30beb3?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60" />
      </c:when>
      <c:when test="${pet.type eq 'Rabbit'}">
        <c:set var="defaultImageUrl" value="https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60" />
      </c:when>
      <c:otherwise>
        <c:set var="defaultImageUrl" value="https://images.unsplash.com/photo-1517849845537-4d257902454a?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60" />
      </c:otherwise>
    </c:choose>
  </c:if>



  <div class="admin-dashboard">
    <div class="container">
      <div class="admin-header">
        <h1><i class="fas fa-edit"></i> Edit Pet</h1>
        <p>Update pet information and availability status.</p>
      </div>

      <c:if test="${not empty successMessage}">
        <div class="alert alert-success">
          <i class="fas fa-check-circle"></i> ${successMessage}
        </div>
      </c:if>

      <c:if test="${not empty errorMessage}">
        <div class="alert alert-error">
          <i class="fas fa-exclamation-circle"></i> ${errorMessage}
        </div>
      </c:if>

      <div class="dashboard-section">
        <div class="section-header">
          <h3><i class="fas fa-paw"></i> Current Pet</h3>
        </div>
        <div class="pet-preview">
          <img src="${not empty pet.imageUrl ? pet.imageUrl : defaultImageUrl}" alt="${pet.name}" class="pet-image">
          <div class="pet-info">
            <h3>${pet.name}</h3>
            <p>${pet.breed} (${pet.type})</p>
            <p>
              <c:set var="ageValue" value="${Double.parseDouble(pet.age)}" />
              <c:choose>
                <c:when test="${ageValue < 1}">
                  <fmt:formatNumber value="${ageValue * 12}" pattern="#.#" /> months
                </c:when>
                <c:otherwise>
                  <fmt:formatNumber value="${ageValue}" pattern="#.#" /> years
                </c:otherwise>
              </c:choose>
              old, ${pet.gender}
            </p>
            <p>Status: <span class="status ${pet.status eq 'available' ? 'available' : 'adopted'}">${pet.status eq 'available' ? 'Available' : 'Adopted'}</span></p>
          </div>
        </div>
      </div>

      <form action="edit-pet" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${pet.petId}">

        <div class="admin-form-container">
          <div class="admin-form-section">
            <h3>Basic Information</h3>
            <div class="admin-form-row">
              <div class="admin-form-group">
                <label for="name">Pet Name *</label>
                <input type="text" id="name" name="name" value="${pet.name}" required>
              </div>
              <div class="admin-form-group">
                <label for="type">Pet Type *</label>
                <select id="type" name="type" required>
                  <option value="">Select Type</option>
                  <option value="Dog" ${pet.type eq 'Dog' ? 'selected' : ''}>Dog</option>
                  <option value="Cat" ${pet.type eq 'Cat' ? 'selected' : ''}>Cat</option>
                  <option value="Bird" ${pet.type eq 'Bird' ? 'selected' : ''}>Bird</option>
                  <option value="Rabbit" ${pet.type eq 'Rabbit' ? 'selected' : ''}>Rabbit</option>
                  <option value="Other" ${pet.type eq 'Other' ? 'selected' : ''}>Other</option>
                </select>
              </div>
            </div>

            <div class="admin-form-row">
              <div class="admin-form-group">
                <label for="breed">Breed *</label>
                <input type="text" id="breed" name="breed" value="${pet.breed}" required>
              </div>
              <div class="admin-form-group">
                <label for="age">Age (in years) *</label>
                <input type="number" id="age" name="age" step="0.1" min="0.1" value="${pet.age}" required>
              </div>
            </div>

            <div class="admin-form-row">
              <div class="admin-form-group">
                <label for="gender">Gender *</label>
                <select id="gender" name="gender" required>
                  <option value="">Select Gender</option>
                  <option value="Male" ${pet.gender eq 'Male' ? 'selected' : ''}>Male</option>
                  <option value="Female" ${pet.gender eq 'Female' ? 'selected' : ''}>Female</option>
                </select>
              </div>
              <div class="admin-form-group">
                <label for="weight">Weight (kg) *</label>
                <input type="number" id="weight" name="weight" step="0.1" min="0.1" value="${pet.weight}" required>
              </div>
            </div>
          </div>

          <div class="admin-form-section">
            <h3>Description & Media</h3>
            <div class="admin-form-group">
              <label for="description">Description *</label>
              <textarea id="description" name="description" required>${pet.description}</textarea>
            </div>

            <div class="admin-form-row">
              <div class="admin-form-group">
                <label for="petImage">Pet Image</label>
                <input type="file" id="petImage" name="petImage" accept="image/*">
                <small class="form-text text-muted">Upload a new image or leave empty to keep the current one</small>
                <c:if test="${not empty pet.imageUrl}">
                  <div class="current-image-info" style="margin-top: 10px;">
                    <img src="${pet.imageUrl}" alt="${pet.name}" style="max-width: 150px; max-height: 150px; border-radius: 5px; margin-bottom: 5px;">
                    <div><small>Current image: ${pet.imageUrl}</small></div>
                    <input type="hidden" name="currentImageUrl" value="${pet.imageUrl}">
                  </div>
                </c:if>
              </div>
              <div class="admin-form-group">
                <label for="badge">Badge</label>
                <select id="badge" name="badge">
                  <option value="" ${empty pet.getBadge() ? 'selected' : ''}>All Ages</option>
                  <option value="Puppy/Kitten" ${pet.getBadge() eq 'Puppy/Kitten' ? 'selected' : ''}>Puppy/Kitten</option>
                  <option value="Young" ${pet.getBadge() eq 'Young' ? 'selected' : ''}>Young</option>
                  <option value="Adult" ${pet.getBadge() eq 'Adult' ? 'selected' : ''}>Adult</option>
                  <option value="Senior" ${pet.getBadge() eq 'Senior' ? 'selected' : ''}>Senior</option>
                </select>
              </div>
            </div>
          </div>

          <div class="admin-form-section">
            <h3>Availability</h3>
            <div class="admin-form-group">
              <label for="status">Availability Status *</label>
              <select id="status" name="status" required>
                <option value="available" ${pet.status eq 'available' ? 'selected' : ''}>Available for Adoption</option>
                <option value="adopted" ${pet.status eq 'adopted' ? 'selected' : ''}>Not Available</option>
              </select>
            </div>
          </div>

          <div class="admin-btn-container">
            <a href="admin-pets" class="admin-btn-secondary">Cancel</a>
            <button type="submit" class="admin-btn-primary">Update Pet</button>
          </div>
        </div>
      </form>
    </div>
  </div>

  <script>
    document.addEventListener('DOMContentLoaded', function() {
      // Auto-dismiss alerts after 5 seconds
      const alerts = document.querySelectorAll('.alert');
      if (alerts.length > 0) {
        setTimeout(() => {
          alerts.forEach(alert => {
            alert.style.opacity = '0';
            setTimeout(() => {
              alert.style.display = 'none';
            }, 300);
          });
        }, 5000);
      }
    });
  </script>
</c:if>

<jsp:include page="/WEB-INF/includes/admin-footer.jsp" />