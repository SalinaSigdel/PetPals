<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    .applications-container {
        max-width: 1000px;
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
        margin-bottom: 1.5rem;
        overflow: hidden;
    }

    .application-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 1rem 1.5rem;
        background: #f8f9fa;
        border-bottom: 1px solid #eee;
    }

    .application-id {
        font-size: 0.9rem;
        color: #666;
    }

    .application-status {
        padding: 0.3rem 0.8rem;
        border-radius: 20px;
        font-size: 0.8rem;
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

    .application-body {
        display: flex;
        padding: 1.5rem;
    }

    .pet-image {
        width: 120px;
        height: 120px;
        border-radius: 8px;
        object-fit: cover;
        margin-right: 1.5rem;
    }

    .application-details {
        flex: 1;
    }

    .pet-name {
        font-size: 1.3rem;
        margin: 0 0 0.5rem 0;
        color: #333;
    }

    .application-date {
        font-size: 0.9rem;
        color: #666;
        margin-bottom: 1rem;
    }

    .application-actions {
        margin-top: 1rem;
    }

    .btn-view-details {
        display: inline-block;
        padding: 0.5rem 1rem;
        background: var(--primary-color);
        color: white;
        border-radius: 5px;
        text-decoration: none;
        font-size: 0.9rem;
        transition: background 0.3s;
    }

    .btn-view-details:hover {
        background: var(--primary-dark);
    }

    .no-applications {
        text-align: center;
        padding: 3rem;
        background: white;
        border-radius: 10px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }

    .no-applications i {
        font-size: 3rem;
        color: #ddd;
        margin-bottom: 1rem;
    }

    .no-applications h3 {
        color: #666;
        margin-bottom: 1rem;
    }

    .btn-browse-pets {
        display: inline-block;
        padding: 0.8rem 1.5rem;
        background: var(--primary-color);
        color: white;
        border-radius: 5px;
        text-decoration: none;
        transition: background 0.3s;
    }

    .btn-browse-pets:hover {
        background: var(--primary-dark);
    }

    /* Pagination Styles */
    .pagination {
        display: flex;
        justify-content: center;
        gap: 0.5rem;
        margin-top: 2rem;
    }

    .page-btn {
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px solid #ddd;
        background: white;
        border-radius: 5px;
        cursor: pointer;
        transition: all 0.3s;
        text-decoration: none;
        color: inherit;
        font-size: 0.9rem;
    }

    .page-btn:hover {
        border-color: var(--primary-color);
        color: var(--primary-color);
    }

    .page-btn.active {
        background-color: var(--primary-color);
        border-color: var(--primary-color);
        color: white;
    }

    .page-btn.disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }

    .page-btn.disabled:hover {
        border-color: #ddd;
        color: inherit;
    }

    .page-ellipsis {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 40px;
        height: 40px;
        color: var(--grey);
    }
</style>
