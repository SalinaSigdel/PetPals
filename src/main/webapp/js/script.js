// Common JavaScript functionality for PetPals

// Form validation
document.addEventListener('DOMContentLoaded', function() {
    console.log("DOM loaded - initializing password toggles");
    
    // Remove any password strength indicators that might be created
    function removePasswordStrength() {
        const strengthIndicators = document.querySelectorAll('.password-strength');
        strengthIndicators.forEach(indicator => {
            indicator.remove();
        });
    }

    // Remove on page load
    removePasswordStrength();

    // Add password toggle functionality
    function setupPasswordToggles() {
        console.log("Setting up password toggles");
        const passwordFields = document.querySelectorAll('input[type="password"]');
        console.log("Found password fields:", passwordFields.length);
        
        passwordFields.forEach(field => {
            // Remove any password strength indicators when typing
            field.addEventListener('input', removePasswordStrength);

            // Check if this field is already set up
            if (field.dataset.toggleInitialized === 'true') {
                return;
            }
            
            // Mark as initialized
            field.dataset.toggleInitialized = 'true';

            // Create wrapper if not already wrapped
            let wrapper;
            if (!field.parentElement.classList.contains('password-toggle-wrapper')) {
                wrapper = document.createElement('div');
                wrapper.className = 'password-toggle-wrapper';
                // Insert wrapper before the field
                field.parentNode.insertBefore(wrapper, field);
                // Move field into wrapper
                wrapper.appendChild(field);
            } else {
                wrapper = field.parentElement;
            }

            // Check if toggle button already exists
            if (!wrapper.querySelector('.password-toggle')) {
                // Create toggle button
                const toggleBtn = document.createElement('button');
                toggleBtn.type = 'button';
                toggleBtn.className = 'password-toggle';
                toggleBtn.innerHTML = '<i class="fas fa-eye"></i>';
                toggleBtn.title = 'Show password';
                toggleBtn.setAttribute('aria-label', 'Show password');
                
                // Add toggle functionality
                toggleBtn.addEventListener('click', function(e) {
                    console.log("Toggle button clicked");
                    // Prevent form submission
                    e.preventDefault();
                    e.stopPropagation();

                    // Toggle password visibility
                    if (field.type === 'password') {
                        field.type = 'text';
                        this.innerHTML = '<i class="fas fa-eye-slash"></i>';
                        this.title = 'Hide password';
                        this.setAttribute('aria-label', 'Hide password');
                    } else {
                        field.type = 'password';
                        this.innerHTML = '<i class="fas fa-eye"></i>';
                        this.title = 'Show password';
                        this.setAttribute('aria-label', 'Show password');
                    }
                });
                
                // Append button to wrapper
                wrapper.appendChild(toggleBtn);
            }
        });
    }
    
    // Set up toggles initially
    setupPasswordToggles();
    
    // Also set up toggles after a short delay (helps with dynamic content)
    setTimeout(setupPasswordToggles, 500);

    // Form submission validation
    const forms = document.querySelectorAll('form');

    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            const passwordField = form.querySelector('input[name="password"]');
            const confirmPasswordField = form.querySelector('input[name="confirmPassword"]');

            if (passwordField && confirmPasswordField) {
                if (passwordField.value !== confirmPasswordField.value) {
                    event.preventDefault();
                    showError(confirmPasswordField, 'Passwords do not match');
                }
            }
        });
    });
});

// Password strength validation function has been removed

// Show error message
function showError(field, message) {
    // Remove any existing error message
    const existingError = field.parentNode.querySelector('.error-message');
    if (existingError) {
        existingError.remove();
    }

    // Create error message element
    const errorElement = document.createElement('div');
    errorElement.className = 'error-message';
    errorElement.textContent = message;

    // Insert after the field
    field.parentNode.insertBefore(errorElement, field.nextSibling);

    // Highlight the field
    field.classList.add('error-field');

    // Remove error after 3 seconds
    setTimeout(() => {
        errorElement.remove();
        field.classList.remove('error-field');
    }, 3000);
}

// Dismiss notifications
document.addEventListener('DOMContentLoaded', function() {
    const notifications = document.querySelectorAll('.notification');

    notifications.forEach(notification => {
        // Add close button
        const closeButton = document.createElement('button');
        closeButton.className = 'notification-close';
        closeButton.innerHTML = '&times;';
        notification.appendChild(closeButton);

        // Add click event to close button
        closeButton.addEventListener('click', function() {
            notification.style.opacity = '0';
            setTimeout(() => {
                notification.remove();
            }, 300);
        });

        // Auto-dismiss after 5 seconds
        setTimeout(() => {
            notification.style.opacity = '0';
            setTimeout(() => {
                notification.remove();
            }, 300);
        }, 5000);
    });
});
