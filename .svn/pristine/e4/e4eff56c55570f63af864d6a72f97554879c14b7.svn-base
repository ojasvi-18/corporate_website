package com.zillious.corporate_website.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.zillious.commons.db.DBConnector;

public class WebsiteDBConnector extends DBConnector {
    private static final Logger       s_logger         = Logger.getLogger(WebsiteDBConnector.class);

    private static final String       DB_JNDI_SRC      = "java:comp/env/jdbc/ZilliousCorp";
    private static final String       DB_PROP_FILE     = "website_app_db.properties";
    private static final String       DB_DRIVER        = "WEBSITE_DB_ATPCO.Driver";
    private static final String       DB_URL           = "WEBSITE_DB_ATPCO.Url";
    private static final String       DB_USER          = "WEBSITE_DB_ATPCO.User";
    private static final String       DB_PASSWD        = "WEBSITE_DB_ATPCO.Password";

    private static DataSource         s_dataSource     = null;
    private static WebsiteDBConnector s_staticInstance = new WebsiteDBConnector();

    private static Properties         s_prop;

    /**
     * Initializes from the servlet containers JNDI providers.
     */
    public static void initialize() {
        try {
            s_logger.info("Initializing WebsiteDBConnector.");
            Context ctx = new InitialContext();
            s_dataSource = (DataSource) ctx.lookup(DB_JNDI_SRC);

            if (s_dataSource == null) {
                throw new RuntimeException("No Datasource.");
            }
            try {
                s_prop = new Properties();
                s_prop.load(WebsiteDBConnector.class.getClassLoader().getResourceAsStream(DB_PROP_FILE));
            } catch (IOException e) {
                s_logger.fatal("Could not load DB properties: ", e);
            }

            s_logger.info("Initialized WebsiteDBConnector.");

        } catch (Exception e) {
            s_logger.error("Failed to initialize WebsiteDBConnector.", e);
        }
    }

    /**
     * Returns the static instance of WebsiteDBConnector class.
     * 
     * @return
     */
    public static WebsiteDBConnector getStaticInstance() {
        return s_staticInstance;
    }

    @Override
    protected Connection getConnection(boolean isAutoCommitOn, boolean isReadOnly) throws SQLException {
        Connection conn = null;
        if (s_dataSource != null) {
            conn = s_dataSource.getConnection();

        } else {
            if (s_logger.isDebugEnabled()) {
                s_logger.debug("The DB Connection not fetched from DB Pool, but made directly to DB.");
            }

            try {
                if (s_prop == null) {
                    s_logger.debug("Properties not Initialized. Initializing properties now for batch insert.");
                    s_prop = new Properties();
                    s_prop.load(WebsiteDBConnector.class.getClassLoader().getResourceAsStream(DB_PROP_FILE));
                }

                try {
                    Class.forName(s_prop.getProperty(DB_DRIVER));
                } catch (ClassNotFoundException cnfe) {
                    throw new RuntimeException("Could not find database driver: " + s_prop.getProperty(DB_DRIVER));
                }

                String url = s_prop.getProperty(DB_URL);
                String user = s_prop.getProperty(DB_USER);
                String passwd = s_prop.getProperty(DB_PASSWD);
                conn = DriverManager.getConnection(url, user, passwd);

            } catch (SQLException se) {
                s_logger.fatal("Error in Connection initialization: ", se);

            } catch (IOException e) {
                s_logger.fatal("IO Error, could not load DB properties: " + DB_PROP_FILE, e);
            }
        }

        if (conn == null) {
            throw new RuntimeException("Could not get database connection.");
        } else {
            conn.setReadOnly(isReadOnly);
            conn.setAutoCommit(isAutoCommitOn);
        }
        return conn;
    }

    public Connection getConnectionForBatchInsert() throws SQLException {
        Connection conn = null;
        if (s_dataSource != null) {
            conn = s_dataSource.getConnection();

        } else {
            s_logger.debug("The DB Connection not fetched from DB Pool, but made directly to DB for batch insert.");

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException("Could not find mysql driver.");
            }
            try {
                if (s_prop == null) {
                    s_logger.debug("Properties not Initialized. Initializing properties now for batch insert.");
                    s_prop = new Properties();
                    s_prop.load(WebsiteDBConnector.class.getClassLoader().getResourceAsStream(DB_PROP_FILE));
                }
                String url = s_prop.getProperty(DB_URL);
                String user = s_prop.getProperty(DB_USER);
                String passwd = s_prop.getProperty(DB_PASSWD);

                conn = DriverManager.getConnection(url + "?rewriteBatchedStatements=true", user, passwd);

            } catch (SQLException se) {
                s_logger.fatal("Error in mysql driver initialization: ", se);
            } catch (IOException e) {
                s_logger.fatal("Could not load DB properties: ", e);
            } catch (Exception e) {
                s_logger.fatal("Error in reading connection properties: ", e);
            }
        }

        if (conn == null) {
            throw new RuntimeException("Could not get database connection.");
        } else {
            conn.setReadOnly(false);
            conn.setAutoCommit(false);
        }
        return conn;
    }

    // This method is used, so that when we are using the Single Transaction
    // Systems, and if a connection fails, it does not go back to pool, and lead
    // to data corruption error if some other transaction commits this
    // connection.
    public Connection getConnectionNotFromPool() throws Exception {
        Connection conn = null;
        if (s_logger.isDebugEnabled()) {
            s_logger.debug("The DB Connection not fetched from DB Pool, but made directly to DB.");
        }

        try {
            if (s_prop == null) {
                s_logger.debug("Properties not Initialized. Initializing properties now for batch insert.");
                s_prop = new Properties();
                s_prop.load(WebsiteDBConnector.class.getClassLoader().getResourceAsStream(DB_PROP_FILE));
            }

            try {
                Class.forName(s_prop.getProperty(DB_DRIVER));
            } catch (ClassNotFoundException cnfe) {
                throw new RuntimeException("Could not find database driver: " + s_prop.getProperty(DB_DRIVER));
            }

            String url = s_prop.getProperty(DB_URL);
            String user = s_prop.getProperty(DB_USER);
            String passwd = s_prop.getProperty(DB_PASSWD);
            conn = DriverManager.getConnection(url, user, passwd);

        } catch (SQLException se) {
            s_logger.fatal("Error in Connection initialization: ", se);

        } catch (IOException e) {
            s_logger.fatal("IO Error, could not load DB properties: " + DB_PROP_FILE, e);
        }

        if (conn == null) {
            throw new RuntimeException("Could not get database connection.");
        } else {
            conn.setReadOnly(false);
            conn.setAutoCommit(false);
        }
        return conn;
    }

    /**
     * Returns a DB connection. Assumes connection is on auto-commit true, but
     * not read-only. Transaction Isolation level for such connection will
     * always be put as READ_UNCOMMITTED for ATPCO connections. this is because,
     * we want to have access to the data, that is still being loaded onto the
     * database through the record loader
     * 
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getReadOnlyConnection() throws SQLException {
        Connection conn = getConnection(true, true);
        if (conn == null) {
            throw new RuntimeException("Could not get database connection.");
        } else {
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        }
        return conn;
    }

    private WebsiteDBConnector() {
    }

    public static void main(String[] args) {
        WebsiteDBConnector staticInstance = WebsiteDBConnector.getStaticInstance();
        Connection conn = null;
        Set<String> ignoreTables = new HashSet<String>();
        ignoreTables.add("airport_data");
        ignoreTables.add("atpco_locations");
        ignoreTables.add("file_download_status");
        ignoreTables.add("file_process_status");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = staticInstance.getConnection(false);
            stmt = conn.prepareStatement("show tables;");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString(1);
                if (!ignoreTables.contains(tableName)) {
                    System.out.println("DROP TABLE " + tableName + ";");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            staticInstance.closeAll(conn, stmt, rs, true);
        }
    }
}
