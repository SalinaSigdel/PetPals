<%-- Profile page specific styles --%>
<style>
    /* Define missing variables */
    :root {
        --grey: #6c757d;
        --dark: #343a40;
        --primary-dark: #3d8b40;
        --light-bg: #f8f9fa;
        --border-color: #dee2e6;
        --card-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
        --transition-speed: 0.3s;
    }

    /* Alert styles */
    .alert {
        padding: 1rem;
        border-radius: 8px;
        margin-bottom: 1.5rem;
        display: flex;
        align-items: center;
    }

    .alert i {
        margin-right: 0.75rem;
        font-size: 1.2rem;
    }

    .alert-success {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }

    .alert-danger {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }

    /* Form styles */
    .form-control {
        width: 100%;
        padding: 0.75rem 1rem;
        border: 1px solid var(--border-color);
        border-radius: 8px;
        font-size: 1rem;
        transition: all var(--transition-speed);
        background-color: white;
    }

    .form-control:focus {
        outline: none;
        border-color: var(--primary-color);
        box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.15);
    }

    .form-actions {
        display: flex;
        gap: 1rem;
        margin-top: 2rem;
    }

    /* Button styles */
    .btn {
        padding: 0.6rem 1.2rem;
        border-radius: 8px;
        font-size: 0.95rem;
        font-weight: 500;
        cursor: pointer;
        border: none;
        transition: all var(--transition-speed);
        display: inline-flex;
        align-items: center;
        justify-content: center;
    }

    .btn i {
        margin-right: 0.5rem;
    }

    .btn-primary {
        background-color: var(--primary-color);
        color: white;
    }

    .btn-primary:hover {
        background-color: var(--primary-dark);
        transform: translateY(-1px);
    }

    .btn-outline {
        background: transparent;
        border: 1px solid var(--border-color);
        color: var(--dark);
    }

    .btn-outline:hover {
        background: var(--light-bg);
        border-color: var(--grey);
    }

    .btn-danger {
        background: #e74c3c;
        color: white;
    }

    .btn-danger:hover {
        background: #c0392b;
        transform: translateY(-1px);
    }

    /* Password section styles */
    .profile-card h2 {
        margin-top: 0;
        margin-bottom: 1.5rem;
        color: var(--text-color);
        font-weight: 600;
    }

    #password-change-form {
        max-width: 500px;
    }

    /* Profile container */
    .profile-container {
        max-width: 800px;
        margin: 2rem auto;
        padding: 0 1.5rem;
    }

    /* Profile header */
    .profile-header {
        margin-bottom: 2rem;
        text-align: center;
    }

    .profile-header h1 {
        color: var(--primary-color);
        margin-bottom: 0.5rem;
        font-size: 2.2rem;
        font-weight: 600;
    }

    .profile-header p {
        color: var(--grey);
        margin: 0;
    }

    /* Profile cards */
    .profile-card {
        background: var(--white);
        border-radius: 12px;
        padding: 2rem;
        margin-bottom: 2rem;
        box-shadow: var(--card-shadow);
        border: 1px solid var(--border-color);
        transition: box-shadow var(--transition-speed);
    }

    .profile-card:hover {
        box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
    }

    .profile-card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1.5rem;
        padding-bottom: 1rem;
        border-bottom: 1px solid var(--border-color);
    }

    .profile-card-header h2 {
        margin: 0;
        font-size: 1.5rem;
        color: var(--dark);
        font-weight: 600;
    }

    /* Profile info items */
    .profile-info-item {
        margin-bottom: 1.5rem;
        padding-bottom: 1.5rem;
        border-bottom: 1px solid var(--border-color);
    }

    .profile-info-item:last-child {
        margin-bottom: 0;
        padding-bottom: 0;
        border-bottom: none;
    }

    .profile-info-label {
        display: block;
        font-size: 0.9rem;
        color: var(--grey);
        margin-bottom: 0.5rem;
        font-weight: 500;
    }

    .profile-info-value {
        font-size: 1.1rem;
        color: var(--dark);
    }

    .member-since {
        font-size: 1rem;
        color: var(--grey);
        margin-bottom: 1.5rem;
    }

    /* Danger zone */
    .danger-zone {
        color: #e74c3c;
    }

    .danger-description {
        margin: 1rem 0;
        font-size: 0.95rem;
        color: var(--grey);
        line-height: 1.5;
    }

    /* Notification Styles */
    .notification-badge {
        background-color: var(--primary-color);
        color: white;
        border-radius: 50%;
        width: 24px;
        height: 24px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 0.8rem;
        font-weight: bold;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .notifications-container {
        margin-bottom: 2rem;
    }

    .notification-item {
        display: flex;
        padding: 1.25rem;
        border-radius: 10px;
        margin-bottom: 1rem;
        position: relative;
        background-color: var(--light-bg);
        border-left: 4px solid #ddd;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
        transition: transform var(--transition-speed), box-shadow var(--transition-speed);
    }

    .notification-item:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    .notification-success {
        border-left-color: #28a745;
        background-color: #f0fff4;
    }

    .notification-pending {
        border-left-color: #ffc107;
        background-color: #fffbeb;
    }

    .notification-rejected {
        border-left-color: #dc3545;
        background-color: #fff5f5;
    }

    .notification-icon {
        margin-right: 1rem;
        font-size: 1.5rem;
        color: #aaa;
    }

    .notification-success .notification-icon {
        color: #28a745;
    }

    .notification-pending .notification-icon {
        color: #ffc107;
    }

    .notification-rejected .notification-icon {
        color: #dc3545;
    }

    .notification-content {
        flex: 1;
    }

    .notification-content h4 {
        margin: 0 0 0.5rem 0;
        font-size: 1.1rem;
        font-weight: 600;
    }

    .notification-content p {
        margin: 0;
        color: #666;
        line-height: 1.5;
    }

    .notification-time {
        display: block;
        font-size: 0.8rem;
        color: #999;
        margin-top: 0.75rem;
    }

    .notification-dismiss {
        background: none;
        border: none;
        cursor: pointer;
        position: absolute;
        top: 0.75rem;
        right: 0.75rem;
        font-size: 1rem;
        color: #aaa;
        transition: color var(--transition-speed);
        width: 30px;
        height: 30px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
    }

    .notification-dismiss:hover {
        color: #666;
        background-color: rgba(0, 0, 0, 0.05);
    }

    /* Profile tabs */
    .profile-tabs {
        display: flex;
        border-bottom: 1px solid var(--border-color);
        margin-bottom: 2rem;
        overflow-x: auto;
        scrollbar-width: none; /* For Firefox */
        -ms-overflow-style: none; /* For Internet Explorer and Edge */
    }

    .profile-tabs::-webkit-scrollbar {
        display: none; /* For Chrome, Safari, and Opera */
    }

    .profile-tab {
        padding: 1rem 1.5rem;
        cursor: pointer;
        border-bottom: 3px solid transparent;
        font-weight: 500;
        white-space: nowrap;
        transition: all var(--transition-speed);
        color: var(--grey);
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .profile-tab:hover {
        color: var(--primary-color);
        background-color: rgba(76, 175, 80, 0.05);
    }

    .profile-tab.active {
        border-bottom-color: var(--primary-color);
        color: var(--primary-color);
        font-weight: 600;
    }

    .profile-tab-content {
        display: none;
    }

    .profile-tab-content.active {
        display: block;
        animation: fadeIn 0.3s ease-in-out;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0); }
    }

    /* Adoption Application Styles */
    .application-status {
        display: inline-block;
        padding: 0.35rem 0.75rem;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .status-pending {
        background-color: #fff3cd;
        color: #856404;
    }

    .status-approved {
        background-color: #d4edda;
        color: #155724;
    }

    .status-rejected {
        background-color: #f8d7da;
        color: #721c24;
    }

    .adoption-list {
        list-style: none;
        padding: 0;
    }

    .adoption-item {
        border: 1px solid var(--border-color);
        border-radius: 12px;
        padding: 1.5rem;
        margin-bottom: 1.5rem;
        position: relative;
        transition: all var(--transition-speed);
        box-shadow: var(--card-shadow);
        background-color: white;
    }

    .adoption-item:hover {
        box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.1);
        transform: translateY(-2px);
    }

    .adoption-item:last-child {
        margin-bottom: 0;
    }

    .adoption-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1.25rem;
        padding-bottom: 1rem;
        border-bottom: 1px solid var(--border-color);
    }

    .adoption-pet-info {
        display: flex;
        align-items: center;
        gap: 1.25rem;
    }

    .adoption-pet-image {
        width: 70px;
        height: 70px;
        border-radius: 8px;
        object-fit: cover;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }

    .adoption-pet-name {
        font-weight: 600;
        color: var(--dark);
        margin: 0 0 0.25rem 0;
        font-size: 1.1rem;
    }

    .adoption-pet-breed {
        font-size: 0.9rem;
        color: var(--grey);
        margin: 0;
    }

    .adoption-date {
        font-size: 0.85rem;
        color: var(--grey);
        margin-top: 0.75rem;
        display: flex;
        align-items: center;
    }

    .adoption-date i {
        margin-right: 0.5rem;
        color: var(--primary-color);
    }

    /* Responsive styles */
    @media (max-width: 768px) {
        .profile-container {
            padding: 0 1rem;
        }

        .profile-card {
            padding: 1.5rem;
        }

        .profile-card-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 1rem;
        }

        .profile-card-header button {
            width: 100%;
        }

        .adoption-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 1rem;
        }

        .form-actions {
            flex-direction: column;
        }

        .form-actions button {
            width: 100%;
        }
    }
</style>
