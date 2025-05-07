<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
  .pet-list-header {
    text-align: center;
    padding: 2rem 0;
    background-color: #f8f9fa;
    margin-bottom: 2rem;
  }

  .pet-list-header h2 {
    color: var(--primary-color);
    margin-bottom: 0.5rem;
    font-size: 2rem;
  }

  .pet-list-header p {
    color: #6c757d;
    max-width: 700px;
    margin: 0 auto;
  }

  .pet-list-container {
    padding: 0 1rem 3rem 1rem;
  }

  .filter-section {
    margin-bottom: 2rem;
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
    align-items: center;
  }

  .search-bar {
    flex: 1;
    position: relative;
    min-width: 250px;
  }

  .search-bar input {
    width: 100%;
    padding: 0.8rem 1rem 0.8rem 2.5rem;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 1rem;
  }

  .search-bar i {
    position: absolute;
    left: 1rem;
    top: 50%;
    transform: translateY(-50%);
    color: #999;
  }

  .filters {
    display: flex;
    gap: 1rem;
  }

  .filter-select {
    padding: 0.8rem;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 1rem;
    min-width: 120px;
  }

  .pet-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 2rem;
  }

  .pet-card {
    background: white;
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s;
  }

  .pet-card:hover {
    transform: translateY(-5px);
  }

  .pet-image {
    position: relative;
    height: 200px;
    overflow: hidden;
  }

  .pet-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .pet-badge {
    position: absolute;
    top: 1rem;
    right: 1rem;
    background: var(--primary-color);
    color: white;
    padding: 0.25rem 0.75rem;
    border-radius: 20px;
    font-size: 0.8rem;
    font-weight: 500;
  }

  .pet-info {
    padding: 1.5rem;
  }

  .pet-info h3 {
    margin: 0 0 0.25rem 0;
    color: #333;
  }

  .pet-breed {
    color: #666;
    margin: 0 0 1rem 0;
  }

  .pet-details {
    display: flex;
    gap: 1rem;
    margin-bottom: 1rem;
    color: #666;
    font-size: 0.9rem;
  }

  .pet-description {
    color: #666;
    margin-bottom: 1.5rem;
    font-size: 0.9rem;
    line-height: 1.5;
  }

  .pet-actions {
    margin-top: auto;
  }

  .btn-adopt {
    display: inline-block;
    background-color: var(--primary-color);
    color: white;
    padding: 0.75rem 1.5rem;
    border-radius: 5px;
    text-decoration: none;
    font-weight: 500;
    transition: background-color 0.3s;
  }

  .btn-adopt:hover {
    background-color: var(--secondary-color);
  }

  .pagination {
    display: flex;
    justify-content: center;
    margin-top: 3rem;
  }

  .page-btn {
    width: 40px;
    height: 40px;
    border: 1px solid #ddd;
    background: white;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 0.25rem;
    cursor: pointer;
    border-radius: 5px;
    transition: all 0.3s;
  }

  .page-btn.active {
    background: var(--primary-color);
    color: white;
    border-color: var(--primary-color);
  }

  .page-btn:hover:not(.active) {
    background: #f5f5f5;
  }

  .no-results {
    text-align: center;
    padding: 3rem;
    background: #f8f9fa;
    border-radius: 10px;
  }

  .no-results i {
    font-size: 3rem;
    color: #ddd;
    margin-bottom: 1rem;
  }

  .no-results h3 {
    color: #333;
    margin-bottom: 0.5rem;
  }

  .no-results p {
    color: #666;
  }

  @media (max-width: 768px) {
    .filters {
      width: 100%;
      justify-content: space-between;
    }

    .pet-grid {
      grid-template-columns: 1fr;
    }
  }
</style>
