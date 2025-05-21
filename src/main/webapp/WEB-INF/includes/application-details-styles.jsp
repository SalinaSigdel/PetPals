<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    .application-details-container {
        max-width: 900px;
        margin: 2rem auto;
        padding: 2rem;
    }

    .page-title {
        text-align: center;
        margin-bottom: 2rem;
        color: var(--primary-color);
    }

    .application-card {
        background: white;
        border-radius: 10px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        overflow: hidden;
        margin-bottom: 2rem;
    }

    .application-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 1.5rem;
        background: #f8f9fa;
        border-bottom: 1px solid #eee;
    }

    .application-id {
        font-size: 1.1rem;
        color: #333;
        font-weight: 500;
    }

    .application-status {
        padding: 0.4rem 1rem;
        border-radius: 20px;
        font-size: 0.9rem;
        font-weight: 500;
    }

    .status-pending {
        background: #fff3cd;
        color: #856404;
    }

    .status-approved {
        background: #d4edda;
        color: #155724;
    }

    .status-rejected {
        background: #f8d7da;
        color: #721c24;
    }

    .application-content {
        padding: 1.5rem;
    }

    .application-section {
        margin-bottom: 2rem;
    }

    .section-title {
        font-size: 1.2rem;
        color: #333;
        margin-bottom: 1rem;
        padding-bottom: 0.5rem;
        border-bottom: 1px solid #eee;
    }

    .pet-info-card {
        display: flex;
        background: #f8f9fa;
        border-radius: 8px;
        padding: 1rem;
        margin-bottom: 1.5rem;
    }

    .pet-image {
        width: 150px;
        height: 150px;
        border-radius: 8px;
        object-fit: cover;
        margin-right: 1.5rem;
    }

    .pet-details {
        flex: 1;
    }

    .pet-name {
        font-size: 1.4rem;
        margin: 0 0 0.5rem 0;
        color: #333;
    }

    .pet-stats {
        display: flex;
        gap: 1.5rem;
        margin-bottom: 1rem;
    }

    .pet-stat {
        display: flex;
        align-items: center;
        color: #666;
    }

    .pet-stat i {
        margin-right: 0.5rem;
        color: var(--primary-color);
    }

    .pet-description {
        color: #666;
        line-height: 1.5;
    }

    .info-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 1.5rem;
    }

    .info-item {
        margin-bottom: 1rem;
    }

    .info-label {
        font-size: 0.9rem;
        color: #666;
        margin-bottom: 0.3rem;
    }

    .info-value {
        font-size: 1.1rem;
        color: #333;
    }

    .reason-box {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 1.5rem;
        margin-top: 1rem;
    }

    .reason-text {
        color: #333;
        line-height: 1.6;
    }

    .rejection-reason {
        background: #f8d7da;
        border-radius: 8px;
        padding: 1.5rem;
        margin-top: 1rem;
        border-left: 4px solid #dc3545;
    }

    .rejection-reason h4 {
        color: #721c24;
        margin-top: 0;
        margin-bottom: 0.5rem;
    }

    .rejection-text {
        color: #721c24;
    }

    .admin-actions {
        display: flex;
        gap: 1rem;
        margin-top: 2rem;
    }

    .btn-approve {
        padding: 0.8rem 1.5rem;
        background: #28a745;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 1rem;
        transition: background 0.3s;
    }

    .btn-approve:hover {
        background: #218838;
    }

    .btn-reject {
        padding: 0.8rem 1.5rem;
        background: #dc3545;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 1rem;
        transition: background 0.3s;
    }

    .btn-reject:hover {
        background: #c82333;
    }

    .rejection-form {
        margin-top: 1rem;
        display: none;
    }

    .rejection-form textarea {
        width: 100%;
        padding: 0.8rem;
        border: 1px solid #ddd;
        border-radius: 5px;
        margin-bottom: 1rem;
        font-family: inherit;
        resize: vertical;
    }

    .btn-back {
        display: inline-block;
        padding: 0.6rem 1.2rem;
        background: #6c757d;
        color: white;
        border-radius: 5px;
        text-decoration: none;
        margin-bottom: 1.5rem;
        transition: background 0.3s;
    }

    .btn-back:hover {
        background: #5a6268;
    }
</style>
