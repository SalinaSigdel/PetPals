package com.snapgramfx.petpals.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Utility class for database operations with connection pooling
 */
public class DatabaseUtil {
    private static final String PROPERTIES_FILE = "/database.properties";
    private static String JDBC_URL;
    private static String JDBC_USER;
    private static String JDBC_PASSWORD;
    private static String JDBC_DRIVER;

    // Connection pool settings
    private static int POOL_SIZE = 10;
    private static BlockingQueue<Connection> connectionPool;
    private static boolean poolInitialized = false;

    static {
        try {
            // Load properties
            loadProperties();

            // Load JDBC driver
            Class.forName(JDBC_DRIVER);

            // Initialize connection pool
            initConnectionPool();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load database properties from file
     * @throws IOException if properties file cannot be read
     */
    private static void loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = DatabaseUtil.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.err.println("Unable to find " + PROPERTIES_FILE);
                // Fallback to hardcoded values
                JDBC_URL = "jdbc:mysql://localhost:3306/petpals_db";
                JDBC_USER = "root";
                JDBC_PASSWORD = "";
                JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
                System.out.println("Using hardcoded database connection values");
                return;
            }

            props.load(input);
            JDBC_URL = props.getProperty("db.url");
            JDBC_USER = props.getProperty("db.user");
            JDBC_PASSWORD = props.getProperty("db.password");
            JDBC_DRIVER = props.getProperty("db.driver");

            // Get pool size if specified
            String poolSize = props.getProperty("db.pool.maxSize");
            if (poolSize != null && !poolSize.isEmpty()) {
                try {
                    POOL_SIZE = Integer.parseInt(poolSize);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid pool size in properties, using default: " + POOL_SIZE);
                }
            }
        }
    }

    /**
     * Initialize the connection pool
     */
    private static synchronized void initConnectionPool() {
        if (poolInitialized) {
            return;
        }

        connectionPool = new ArrayBlockingQueue<>(POOL_SIZE);

        // Create initial connections
        for (int i = 0; i < POOL_SIZE / 2; i++) {
            try {
                Connection conn = createConnection();
                connectionPool.offer(conn);
            } catch (SQLException e) {
                System.err.println("Error initializing connection pool: " + e.getMessage());
            }
        }

        poolInitialized = true;
    }

    /**
     * Create a new database connection
     * @return a new Connection object
     * @throws SQLException if connection fails
     */
    private static Connection createConnection() throws SQLException {
        try {
            System.out.println("Creating database connection to: " + JDBC_URL + " with user: " + JDBC_USER);
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("Database connection successful");
            return conn;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get a database connection from the pool
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (!poolInitialized) {
            initConnectionPool();
        }

        Connection connection = connectionPool.poll();

        // If no connection available in pool, create a new one
        if (connection == null) {
            connection = createConnection();
        } else {
            // Validate connection
            try {
                if (connection.isClosed() || !connection.isValid(1)) {
                    connection = createConnection();
                }
            } catch (SQLException e) {
                connection = createConnection();
            }
        }

        return connection;
    }

    /**
     * Return a connection to the pool
     * @param connection Connection to return
     */
    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed() && connection.isValid(1)) {
                    connectionPool.offer(connection);
                }
            } catch (SQLException e) {
                closeConnection(connection);
            }
        }
    }

    /**
     * Close a database connection safely
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}