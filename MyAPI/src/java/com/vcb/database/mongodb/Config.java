/**
 * 
 */
package com.vcb.database.mongodb;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import javolution.util.FastMap;

/**
 * 全局配置 运行时�1�7�需要加入虚拟机参数如下＄1�7 -Djava.ext.dirs=webapp/WEB-INF/lib;packages -cp webapp/WEB-INF/classes
 * 
 * @author
 */
public class Config {

	private final static Log log = LogFactory.getLog(Config.class);
	private final static String CFG = "/doc/config/config.properties";
	private static PropertiesConfiguration configProperties =null;// ResourceBundle.getBundle("config");
	//private final static CacheProvider[] cacheProvider;
	private static Map<String, DataSource> dataSources = new FastMap<String, DataSource>();
	private static boolean show_sql = false;
	private final static Map<String, ThreadLocal<Connection>> conns = new FastMap<String, ThreadLocal<Connection>>();
	private final static ThreadLocal<String> dblocalname = new ThreadLocal<String>();
	public static boolean threadRunFlag = true;
	public static boolean ipfilter = true;
	// GM配置
	public static String gmIp = "";
	public static MongoClient mongoClient = null;// new MongoClient(uri);
	public static MongoDatabase mongoDB = null;
	public static String mongoName=null;
	public static boolean isMongo=false;
	/** 角色缓存类型0redis 1mongo */
	public static int roleCacheType = 0;
	
	// private final static Properties configProperties = new Properties();
	static {
		try {
			// configProperties.load(Config.class.getResourceAsStream(CFG));
			// 1.cache
			configProperties=new PropertiesConfiguration();
			configProperties.setDelimiterParsingDisabled(true);
			configProperties.load(System.getProperty("user.dir")+CFG);
			String sURI = configProperties.getString("mongo.url");// String.format("mongodb://%s:%s@%s:%d/%s", "user", "password", "localhost", 27017, "mydb");
			if (StringUtils.isNotEmpty(sURI)) {
				mongoName=configProperties.getString("mongo.db");
				System.out.println("mongodb init .. " + sURI);
				MongoClientURI uri = new MongoClientURI(sURI);
				mongoClient = new MongoClient(uri);
				mongoDB=mongoClient.getDatabase(mongoName);
				isMongo=true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			//throw new RuntimeException("Unabled to init config.properties...");
		}

	}

	public static DataSource getDataSource(String databaseName) {
		return dataSources.get(databaseName);
	}

	public final static Connection getConnection(String dbName) throws SQLException {
		Connection conn = conns.get(dbName).get();
		if (conn == null || conn.isClosed()) {
			conn = dataSources.get(dbName).getConnection();
			conns.get(dbName).set(conn);
			dblocalname.set(dbName);
		}
		return (show_sql && !Proxy.isProxyClass(conn.getClass())) ? new _DebugConnection(conn).getConnection() : conn;
	}

	public final static void closeConnection() {
		String dbName = dblocalname.get();
		if (dbName == null)
			return;
		Connection conn = conns.get(dbName).get();
		try {

			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			log.error("Unabled to close connection!!! ", e);
		}
		conns.get(dbName).set(null);
		dblocalname.set(null);
	}

	private static Object _obj = new Object();


	public static MongoDatabase getMongoDB() {
		return mongoDB;
	}
}

/**
 * 
 * 
 * @author liudong
 */
class _DebugConnection implements InvocationHandler {

	private final static Log log = LogFactory.getLog(_DebugConnection.class);

	private Connection conn = null;

	public _DebugConnection(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Returns the conn.
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		return (Connection) Proxy.newProxyInstance(conn.getClass().getClassLoader(), conn.getClass().getInterfaces(), this);
	}

	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
		try {
			String method = m.getName();
			if ("prepareStatement".equals(method) || "createStatement".equals(method))
				if (log.isInfoEnabled())
					log.info("[SQL] >>> " + args[0]);

			return m.invoke(conn, args);
		} catch (InvocationTargetException e) {
			log.error(e.getTargetException());
			throw e.getTargetException();
		}
	}

}
