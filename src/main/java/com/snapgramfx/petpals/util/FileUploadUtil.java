package com.snapgramfx.petpals.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Utility class for handling file uploads
 */
public class FileUploadUtil {

    private static final Logger LOGGER = Logger.getLogger(FileUploadUtil.class.getName());
    private static final String UPLOAD_DIRECTORY = "uploads";
    private static final String WEBAPP_PATH = "/src/main/webapp";

    /**
     * Upload a file to the server
     * @param request The HttpServletRequest containing the file
     * @param partName The name of the file part in the request
     * @param subDirectory The subdirectory to store the file in (e.g., "pets")
     * @return The path to the uploaded file, or null if no file was uploaded
     * @throws IOException If an I/O error occurs
     * @throws ServletException If a servlet error occurs
     */
    public static String uploadFile(HttpServletRequest request, String partName, String subDirectory)
            throws IOException, ServletException {
        Part filePart = request.getPart(partName);

        // Check if a file was uploaded
        if (filePart == null || filePart.getSize() <= 0 || filePart.getSubmittedFileName() == null
                || filePart.getSubmittedFileName().isEmpty()) {
            return null;
        }

        // Get the file name and extension
        String fileName = filePart.getSubmittedFileName();
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        // Generate a unique file name
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Get the real path to the webapp directory
        String contextPath = request.getServletContext().getRealPath("");

        // Create paths for both the temporary and permanent locations
        String tempUploadPath = contextPath + File.separator + UPLOAD_DIRECTORY + File.separator + subDirectory;

        // Get the project root directory (going up from the webapp directory)
        String projectRoot = new File(contextPath).getParentFile().getParentFile().getPath();
        String permanentUploadPath = projectRoot + WEBAPP_PATH + File.separator + UPLOAD_DIRECTORY + File.separator + subDirectory;

        // Create both directories if they don't exist
        File tempUploadDir = new File(tempUploadPath);
        File permanentUploadDir = new File(permanentUploadPath);

        if (!tempUploadDir.exists()) {
            tempUploadDir.mkdirs();
        }

        if (!permanentUploadDir.exists()) {
            permanentUploadDir.mkdirs();
        }

        // Log the paths for debugging
        LOGGER.info("Temporary upload path: " + tempUploadPath);
        LOGGER.info("Permanent upload path: " + permanentUploadPath);

        // Save the file to both locations
        Path tempFilePath = Paths.get(tempUploadPath, uniqueFileName);
        Path permanentFilePath = Paths.get(permanentUploadPath, uniqueFileName);

        try (InputStream inputStream = filePart.getInputStream()) {
            // Save to temporary location (for current session)
            Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
        }

        try (InputStream inputStream = filePart.getInputStream()) {
            // Save to permanent location (for persistence)
            Files.copy(inputStream, permanentFilePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Return the relative path to the file
        return UPLOAD_DIRECTORY + "/" + subDirectory + "/" + uniqueFileName;
    }

    /**
     * Delete a file from the server
     * @param request The HttpServletRequest
     * @param filePath The path to the file to delete
     * @return true if the file was deleted, false otherwise
     */
    public static boolean deleteFile(HttpServletRequest request, String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        // Get paths for both temporary and permanent locations
        String contextPath = request.getServletContext().getRealPath("");
        String tempFullPath = contextPath + File.separator + filePath;

        // Get the project root directory
        String projectRoot = new File(contextPath).getParentFile().getParentFile().getPath();
        String permanentFullPath = projectRoot + WEBAPP_PATH + File.separator + filePath;

        // Log the paths for debugging
        LOGGER.info("Deleting temporary file: " + tempFullPath);
        LOGGER.info("Deleting permanent file: " + permanentFullPath);

        // Delete from both locations
        File tempFile = new File(tempFullPath);
        File permanentFile = new File(permanentFullPath);

        boolean tempDeleted = !tempFile.exists() || tempFile.delete();
        boolean permanentDeleted = !permanentFile.exists() || permanentFile.delete();

        return tempDeleted && permanentDeleted;
    }

    /**
     * Check if a file is an image
     * @param fileName The name of the file
     * @return true if the file is an image, false otherwise
     */
    public static boolean isImageFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") ||
               extension.equals("png") || extension.equals("gif") ||
               extension.equals("bmp") || extension.equals("webp");
    }
}
