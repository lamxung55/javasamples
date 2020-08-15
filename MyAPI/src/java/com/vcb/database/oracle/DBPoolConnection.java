package com.vcb.database.oracle;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.apache.commons.lang.ClassUtils;
import org.apache.log4j.Logger;

public class DBPoolConnection {

    /*  29 */ private static final Logger log = Logger.getLogger(DBPoolConnection.class);

    /*  31 */    private static long lastChecked = System.currentTimeMillis();
    private static final long PERIOD_CHECK = 180000L;
    /*  33 */    private static BoneCP connectionPool = getDataSource();
    /*  34 */    private static String DB_URL = null;
    /*  35 */    private static String DB_USERNAME = null;
    /*  36 */    private static String DB_PASSWORD = null;

    private static BoneCP getDataSource() {
        try {
            /*  41 */ log.debug("current directory: " + System.getProperty("user.dir"));

            /*  43 */ Properties appConfig = new Properties();

            InputStream fileInputStream = DBPoolConnection.class.getClassLoader().getResourceAsStream("c3p0.properties");
            /*  45 */ appConfig.load(fileInputStream);

            try {
                /*  53 */ Class.forName("oracle.jdbc.OracleDriver");
                /*  54 */            } catch (Exception e) {

                /*  56 */ log.error(e.getMessage(), e);
                /*  57 */ System.out.println("Error....." + e.getMessage());
                /*  58 */ return null;
            }

            /*  61 */ DB_URL = appConfig.getProperty("jdbcUrl");
            /*  62 */ DB_USERNAME = appConfig.getProperty("user");
            /*  63 */ DB_PASSWORD = appConfig.getProperty("password");
            /*  64 */ int MIN_POOL_SIZE = Integer.parseInt(appConfig.getProperty("minPoolSize"));
            /*  65 */ int MAX_POOL_SIZE = Integer.parseInt(appConfig.getProperty("maxPoolSize"));
            /*  66 */ int ACQUIRE_INCREMENT = Integer.parseInt(appConfig.getProperty("acquireIncrement"));
            /*  67 */ int MAX_IDLE_TIME = Integer.parseInt(appConfig.getProperty("maxIdleTime"));

            /*  69 */ BoneCPConfig config = new BoneCPConfig();
            /*  70 */ config.setInitSQL("SELECT 1 FROM DUAL");
            /*  71 */ config.setIdleConnectionTestPeriodInMinutes(5L);
            /*  72 */ config.setConnectionTimeoutInMs(5000L);
            /*  73 */ config.setAcquireRetryAttempts(10);
            /*  74 */ config.setIdleMaxAgeInMinutes(10L);
            /*  75 */ config.setMaxConnectionAgeInSeconds(36000L);
            /*  76 */ config.setAcquireIncrement(ACQUIRE_INCREMENT);
            /*  77 */ config.setJdbcUrl(DB_URL);
            /*  78 */ config.setUsername(DB_USERNAME);
            /*  79 */ config.setPassword(DB_PASSWORD);
            /*  80 */ config.setMinConnectionsPerPartition(MIN_POOL_SIZE);
            /*  81 */ config.setMaxConnectionsPerPartition(MAX_POOL_SIZE);
            /*  82 */ config.setIdleMaxAgeInSeconds(MAX_IDLE_TIME);

            /*  85 */ config.setPartitionCount(2);
            /*  86 */ connectionPool = new BoneCP(config);

            /*  88 */ return connectionPool;
            /*  89 */        } catch (ExceptionInInitializerError ex) {
            /*  90 */ log.debug("getdatasource error: " + ex.getMessage());

            /*  92 */ log.error(ex.getMessage(), ex);
            /*  93 */        } catch (Exception ex) {
            /*  94 */ log.debug("getdatasource error: " + ex.getMessage());

            /*  96 */ log.error(ex.getMessage(), ex);
        }
        /*  98 */ return null;
    }

    public static Connection getConnection() {
        /* 102 */ Connection con = null;
        try {
            /* 104 */ if (connectionPool == null) {
                /* 105 */ getDataSource();
            }

            /* 108 */ if (connectionPool != null) {
                /* 109 */ con = connectionPool.getConnection();
            } else {
                /* 111 */ log.debug("try to get another connection by normal way: " + DB_URL);
                /* 112 */ Class.forName("oracle.jdbc.OracleDriver");
                /* 113 */ con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            }

            /* 116 */        } catch (Exception ex) {

            try {
                /* 119 */ log.debug("error pool connection: " + ex.getMessage());

                /* 121 */ log.error(ex.getMessage(), ex);

                /* 123 */ Class.forName("oracle.jdbc.OracleDriver");
                /* 124 */ con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            } /* 126 */ catch (Exception ex1) {
                /* 127 */ log.debug("Oracle issue " + ex1.getMessage(), ex1);
            }
        }
        /* 130 */ return con;
    }
}
