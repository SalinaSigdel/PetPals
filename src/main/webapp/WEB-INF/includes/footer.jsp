    </div> <!-- End of content-wrapper -->

    <footer>
        <div class="container">
            <div class="footer-content">
                <div class="footer-logo">
                    <h2><i class="fas fa-paw"></i> PetPals</h2>
                    <p>Building happy homes for pets and people</p>
                </div>
                <div class="footer-links">
                    <div class="link-group">
                        <h4>Quick Links</h4>
                        <a href="adopt.jsp">Adopt</a>
                        <a href="about.jsp">About Us</a>
                    </div>
                    <div class="link-group">
                        <h4>Support</h4>
                        <a href="#">FAQ</a>
                        <a href="#">Contact Us</a>
                        <a href="#">Privacy Policy</a>
                    </div>
                </div>
                <div class="social-links">
                    <h4>Follow Us</h4>
                    <div class="social-icons">
                        <a href="#"><i class="fab fa-facebook"></i></a>
                        <a href="#"><i class="fab fa-twitter"></i></a>
                        <a href="#"><i class="fab fa-instagram"></i></a>
                    </div>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; <%= new java.util.Date().getYear() + 1900 %> PetPals | Built with <i class="fas fa-heart"></i> for pets and people</p>
            </div>
        </div>
    </footer>

    <!-- Common scripts -->
    <script src="js/script.js"></script>

    <!-- Additional scripts -->
    <c:if test="${not empty param.extraScripts}">
        <jsp:include page="${param.extraScripts}" />
    </c:if>
</body>
</html>
