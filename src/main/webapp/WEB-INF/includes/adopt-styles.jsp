<%-- Adopt page specific styles --%>
<style>
    .adopt-hero {
        background-color: var(--light-bg);
        padding: 3rem 0;
        text-align: center;
        margin-bottom: 2rem;
    }

    .adopt-hero h2 {
        color: var(--primary-color);
        font-size: 2.5rem;
        margin-bottom: 1rem;
    }

    .adopt-hero p {
        color: var(--grey);
        font-size: 1.1rem;
        max-width: 600px;
        margin: 0 auto;
    }

    .adopt-pets {
        padding: 2rem 0;
    }

    .filter-section {
        margin-bottom: 2rem;
    }

    .search-bar {
        position: relative;
        max-width: 500px;
        margin-bottom: 1rem;
    }

    .search-bar i {
        position: absolute;
        left: 1rem;
        top: 50%;
        transform: translateY(-50%);
        color: var(--grey);
    }

    .search-bar input {
        width: 100%;
        padding: 0.8rem 1rem 0.8rem 2.5rem;
        border: 1px solid #ddd;
        border-radius: 5px;
        font-size: 1rem;
        transition: all 0.3s;
    }

    .search-bar input:focus {
        outline: none;
        border-color: var(--primary-color);
        box-shadow: 0 0 0 2px rgba(var(--primary-rgb), 0.2);
    }

    .filters {
        display: flex;
        gap: 1rem;
        flex-wrap: wrap;
    }

    .filter-select {
        padding: 0.7rem 1rem;
        border: 1px solid #ddd;
        border-radius: 5px;
        font-size: 0.9rem;
        background-color: white;
        min-width: 150px;
        cursor: pointer;
    }

    .filter-select:focus {
        outline: none;
        border-color: var(--primary-color);
    }

    .pet-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 2rem;
        margin-bottom: 2rem;
    }

    .pet-card {
        background: white;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: var(--shadow);
        transition: transform 0.3s, box-shadow 0.3s;
    }

    .pet-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
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
        transition: transform 0.5s;
    }

    .pet-card:hover .pet-image img {
        transform: scale(1.05);
    }

    .pet-badge {
        position: absolute;
        top: 1rem;
        right: 1rem;
        background-color: var(--primary-color);
        color: white;
        padding: 0.3rem 0.8rem;
        border-radius: 20px;
        font-size: 0.8rem;
        font-weight: 500;
    }

    .pet-info {
        padding: 1.5rem;
    }

    .pet-info h3 {
        margin: 0 0 0.5rem 0;
        font-size: 1.3rem;
        color: var(--dark);
    }

    .pet-breed {
        color: var(--grey);
        margin: 0 0 1rem 0;
        font-size: 0.9rem;
    }

    .pet-details {
        display: flex;
        justify-content: space-between;
        margin-bottom: 1rem;
        font-size: 0.9rem;
        color: var(--grey);
    }

    .pet-details i {
        margin-right: 0.3rem;
        color: var(--primary-color);
    }

    .pet-description {
        margin-bottom: 1.5rem;
        font-size: 0.9rem;
        color: var(--dark);
        line-height: 1.5;
    }

    .pet-description p {
        margin: 0;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }

    .pet-actions {
        display: flex;
        justify-content: center;
    }

    .btn-adopt {
        display: inline-block;
        background-color: var(--primary-color);
        color: white;
        padding: 0.7rem 1.5rem;
        border-radius: 5px;
        text-decoration: none;
        font-weight: 500;
        transition: background-color 0.3s;
        text-align: center;
    }

    .btn-adopt:hover {
        background-color: var(--primary-dark);
    }

    .pagination {
        display: flex;
        justify-content: center;
        gap: 0.5rem;
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

    .no-results {
        text-align: center;
        padding: 3rem;
        background-color: var(--light-bg);
        border-radius: 10px;
        grid-column: 1 / -1;
    }

    .no-results i {
        font-size: 3rem;
        color: var(--grey);
        margin-bottom: 1rem;
    }

    .no-results h3 {
        color: var(--dark);
        margin-bottom: 0.5rem;
    }

    .no-results p {
        color: var(--grey);
        max-width: 400px;
        margin: 0 auto;
    }

    @media (max-width: 768px) {
        .pet-grid {
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
        }

        .adopt-hero h2 {
            font-size: 2rem;
        }

        .adopt-hero p {
            font-size: 1rem;
        }
    }

    @media (max-width: 576px) {
        .pet-grid {
            grid-template-columns: 1fr;
        }

        .filters {
            flex-direction: column;
        }

        .filter-select {
            width: 100%;
        }
    }
</style>
