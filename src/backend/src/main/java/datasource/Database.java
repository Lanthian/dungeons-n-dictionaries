// java/datasource/Database.java
package datasource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import config.Env;
import datasource.exceptions.PersistenceException;

/**
 * A class to manage the formation of database connections, connection pool
 * management, and database initialisation as necessary.
 *
 * <p> Public methods supplied via this class:
 * <ul>
 *   <li> {@link #getConnection()} - retrieve a database connection
 *   <li> {@link #releaseConnection(Connection)} - manually release a connection
 *   <li> {@link #shutdown()} - close all remaining connections and clear pool
 * </ul>
 *
 * <p> Developed in https://github.com/SWEN90007-2025-sem2/High-Integrity in
 * collaboration with:
 * <ul>
 *   <li> Yuk Hang Cheng : {@link https://github.com/SKYYKS-9998}
 *   <li> Adam Mantello : {@link https://github.com/adammantello-melbuni}
 *   <li> Andrew Walton : {@link https://github.com/AndrewWalton002}
 * </ul>
 */
public class Database {

    // --- Constants ---
    private static final int TRANSACTION_ISOLATION = Connection.TRANSACTION_READ_COMMITTED;
    private static final int POOL_SIZE = 10;
    private static final String SCHEMA_FILE = "database/setup.sql";
    private static final String POPULATE_FILE = "database/load.sql";
    // Database .env settings
    private static final String URI = Env.get("JDBC_URI");
    private static final String USER = Env.get("JDBC_USERNAME");
    private static final String PASSWORD = Env.get("JDBC_PASSWORD");
    private static final boolean RESET_DB = Boolean.parseBoolean(Env.get("RESET_DATABASE"));
    private static final boolean POPULATE_DB = Boolean.parseBoolean(Env.get("POPULATE_DATABASE"));

    // --- Attributes ---
    private static final ArrayBlockingQueue<Connection> pool = new ArrayBlockingQueue<>(POOL_SIZE);

    /* ======================================================================
     * --------------------------- Database Setup ---------------------------
     * ====================================================================== */

    static {
        // Validate required environment variables
        if (URI == null || URI.trim().isEmpty()) {
            throw new RuntimeException("JDBC_URI environment variable is required but not set");
        }
        if (USER == null || USER.trim().isEmpty()) {
            throw new RuntimeException("JDBC_USERNAME environment variable is required but not set");
        }
        if (PASSWORD == null) {
            throw new RuntimeException("JDBC_PASSWORD environment variable is required but not set");
        }

        // Verify postgresql driver is present
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find postgresql driver");
        }

        // Create connection pool
        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection conn = DriverManager.getConnection(URI, USER, PASSWORD);
                // Explicitly define transaction isolation level
                conn.setTransactionIsolation(TRANSACTION_ISOLATION);
                conn.setAutoCommit(true);
                pool.add(conn);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create initial connection pool", e);
        }

        // Initialise schema
        try (Connection conn = DriverManager.getConnection(URI, USER, PASSWORD);
            Statement stmt = conn.createStatement()) {

            // Setup schema
            runSQL(SCHEMA_FILE, stmt);

            // Initialize test data - do not shut down on failure
            if (POPULATE_DB) try { runSQL(POPULATE_FILE, stmt); }
            catch (IOException | SQLException ignored) {}

        } catch (IOException | SQLException e) {
            System.err.println("Failed to initialize database schema: " + e.getMessage());
            shutdown();
            throw new RuntimeException("Failed to initialize database schema", e);
        }
    }

    /* -------------------------- Utility  Methods -------------------------- */

    /**
     * Private utility method to execute all SQL statements found within a
     * particular {@code resources/} file.
     *
     * @param resourceFile a particular SQL file read and executed
     * @param stmt an active, SQL {@link Statement} object for sending
     *         statements to the database
     * @throws IOException on failure to read from specified file
     * @throws SQLException on failure to execute SQL statement
     */
    private static void runSQL(String resourceFile, Statement stmt) throws IOException, SQLException {
        System.out.println("Executing " + resourceFile + "...");
        InputStream in = Database.class.getClassLoader().getResourceAsStream(resourceFile);
        // Throw a specific error if database file cannot be located
        if (in == null) {
            System.err.println("Could not find database schema file: resources/" + resourceFile);
            throw new RuntimeException("Database schema file is missing");
        }

        // Execute SQL statements one by one
        String sql = readInputStream(in);
        String[] statements = sanitiseSQL(sql);
        for (String statement : statements) {
            // Skip table dropping statements if RESET flag isn't true
            if (!RESET_DB && statement.toUpperCase().startsWith("DROP")) continue;
            stmt.execute(statement);
        }
        System.out.println("Execution finished.");
    }

    /**
     * GPT generated method to replace Java 9 InputStream.readAllBytes() method
     * and StandardCharsets constant. Reads in and returns sanitised input from
     * a files InputStream.
     */
    private static String readInputStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * Private utility method to sanitise and separate a full SQL code String
     * into individual statements, removing all empty statements present.
     *
     * @param sqlString Unsanitised SQl code
     * @return an array of String statements, separated on the delim {@code ;}
     */
    private static String[] sanitiseSQL(String sqlString) {
        return Arrays.stream(sqlString
                // Remove "/* block */" and "-- inline" comments, then split
                .replaceAll("(?s)/\\*.*?\\*/", "")
                .replaceAll("(?m)--.*?$", "")
                .split(";"))
            // Trim blankspace and drop empty statements
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toArray(String[]::new);
    }

    /* ======================================================================
     * -------------------------- Connection  Pool --------------------------
     * ====================================================================== */

    /**
     * Request a database connection from current connection pool. Waits if all
     * connections are busy. Auto-releases connection on method closing so
     * {@link #releaseConnection(Connection)} does not have to be called.
     *
     * @return Connection to database
     * @throws PersistenceException if interrupted while waiting for a connection
     */
    public static Connection getConnection() {
        try { return autoReleaseConnection(pool.take()); }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PersistenceException("Interrupted while waiting for a database connection", e);
        }
    }

    /**
     * GPT generated utility method to wrap a Connection so that it
     * automatically releases instead on closing. This way, methods creating
     * connections don't have to worry about returning them properly.
     *
     * @param conn Connection to wrap with auto-releasing functionality
     * @return Connection proxy with auto-release functionality on "close"
     */
    private static Connection autoReleaseConnection(Connection conn) {
        // Protect against nested proxies
        if (Proxy.isProxyClass(conn.getClass())) return conn;

        // Return a proxy that auto-releases when close() is called
        Connection proxy = (Connection) Proxy.newProxyInstance(
            Connection.class.getClassLoader(),
            new Class[]{Connection.class},
            (proxyObj, method, args) -> {
                if ("close".equals(method.getName())) {
                    releaseConnection(conn);
                    return null;
                }
                return method.invoke(conn, args);
            }
        );
        return proxy;
    }

    /**
     * Release a database Connection {@code conn} back into connection pool. On
     * failure of requeuing this connection, close connection to avoid memory
     * leaks.
     *
     * @param conn Database Connection to be released
     */
    public static void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    // Reset autocommit state in case forgotten to be restored
                    conn.setAutoCommit(true);
                    // On connection return failure, warn of connection lost
                    if (!pool.offer(conn)) {
                        conn.close();
                        System.err.println("Database connection pool full — closed an extra connection.");
                    }
                }
            } catch (SQLException e) {
                // Ensure connections close on failure
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    /**
     * Close all database connections gracefully on shutdown.
     */
    public static void shutdown() {
        for (Connection conn : pool) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
        pool.clear();
        System.out.println("Database connection pool closed.");
    }
}
