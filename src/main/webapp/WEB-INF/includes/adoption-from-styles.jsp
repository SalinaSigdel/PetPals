<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
  .adoption-container {
    max-width: 600px;
    margin: 2rem auto;
    padding: 2rem;
    background: var(--white);
    border-radius: 10px;
    box-shadow: var(--shadow);
  }

  .form-title {
    color: #00a651;
    text-align: center;
    margin-bottom: 0.5rem;
  }

  .form-subtitle {
    text-align: center;
    color: #666;
    margin-bottom: 2rem;
  }

  .pet-preview {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 1rem;
    margin-bottom: 2rem;
    display: flex;
    align-items: center;
    gap: 1.5rem;
  }

  .pet-preview img {
    width: 120px;
    height: 120px;
    border-radius: 8px;
    object-fit: cover;
  }

  .pet-info h3 {
    color: #333;
    margin: 0 0 0.5rem 0;
  }

  .pet-info p {
    color: #666;
    margin: 0;
    font-size: 0.9rem;
    line-height: 1.4;
  }

  .pet-stats {
    display: flex;
    gap: 1rem;
    margin-top: 0.5rem;
    color: #666;
    font-size: 0.9rem;
  }

  .pet-description {
    margin-top: 1rem;
    color: #666;
    font-size: 0.9rem;
    line-height: 1.5;
  }

  .form-group {
    margin-bottom: 1.5rem;
  }

  .form-group label {
    display: block;
    color: #333;
    margin-bottom: 0.5rem;
  }

  .form-row {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1rem;
    margin-bottom: 1rem;
  }

  input, textarea {
    width: 100%;
    padding: 0.8rem;
    border: 1px solid #ddd;
    border-radius: 6px;
    font-size: 1rem;
  }

  input:focus, textarea:focus {
    outline: none;
    border-color: #00a651;
  }

  input:disabled, textarea:disabled {
    background-color: #f5f5f5;
    cursor: not-allowed;
    opacity: 0.7;
  }

  button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }

  textarea {
    min-height: 100px;
    resize: vertical;
  }

  .submit-btn {
    background: #00a651;
    color: white;
    width: 100%;
    padding: 1rem;
    border: none;
    border-radius: 6px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  .submit-btn:hover {
    background: #008c44;
  }

  .notification-box {
    margin-top: 1.5rem;
    padding: 1rem;
    background: #f8f9fa;
    border-left: 4px solid #00a651;
    border-radius: 4px;
  }

  .notification-box p {
    margin: 0;
    color: #666;
    font-size: 0.9rem;
  }

  .notification-box a {
    color: #00a651;
    text-decoration: none;
  }

  .notification-box a:hover {
    text-decoration: underline;
  }

  .success-message {
    background-color: rgba(0, 166, 81, 0.1);
    color: #00a651;
    padding: 15px;
    border-radius: 5px;
    margin-bottom: 20px;
    text-align: center;
  }

  .error-message {
    background-color: rgba(255, 0, 0, 0.1);
    color: #d9534f;
    padding: 15px;
    border-radius: 5px;
    margin-bottom: 20px;
    text-align: center;
  }

  @media (max-width: 600px) {
    .form-row {
      grid-template-columns: 1fr;
    }

    .pet-preview {
      flex-direction: column;
      text-align: center;
    }
  }
</style>
