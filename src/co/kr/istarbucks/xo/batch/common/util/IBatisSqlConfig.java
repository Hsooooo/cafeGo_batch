package co.kr.istarbucks.xo.batch.common.util;

import java.io.Reader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import co.kr.istarbucks.xo.batch.exception.IbatisException;

public class IBatisSqlConfig {
	private static Log logger = LogFactory.getLog(IBatisSqlConfig.class); 
	private static final SqlMapClient xoSqlMap;		// 사이렌오더
	private static final SqlMapClient msrSqlMap;	// MSR
	private static final SqlMapClient homeSqlMap;
	private static final SqlMapClient scksaSqlMap;
	private static final SqlMapClient pushSqlMap;	// PUSH - maria
	
	static {
		try {
			
			// ExpressOrder DB ==========================================================
			String properties = "jdbc.properties";
			String resource = "XOSqlMapConfig.xml";
			Properties prop = Resources.getResourceAsProperties (properties);
			//  데이터베이스 비밀번호 복호화 처리..
			prop.setProperty ("xo.oracle.jdbc.password", TripleDes.decrypt (prop.getProperty ("xo.oracle.jdbc.password")));
			
			Reader reader = Resources.getResourceAsReader (resource);
			xoSqlMap = SqlMapClientBuilder.buildSqlMapClient (reader, prop);
			// ExpressOrder DB ==========================================================
			
			// MSR DB =================================================================
			properties = "jdbc.properties";
			resource = "MSRSqlMapConfig.xml";
			prop = Resources.getResourceAsProperties (properties);
			//  데이터베이스 비밀번호 복호화 처리..
			prop.setProperty ("msr.oracle.jdbc.password", TripleDes.decrypt (prop.getProperty ("msr.oracle.jdbc.password")));
			
			reader = Resources.getResourceAsReader (resource);
			msrSqlMap = SqlMapClientBuilder.buildSqlMapClient (reader, prop);
			// MSR DB =================================================================
			
			// HOME DB =================================================================
			properties = "jdbc.properties";
			resource = "HOMESqlMapConfig.xml";
			prop = Resources.getResourceAsProperties (properties);
			//  데이터베이스 비밀번호 복호화 처리..
			prop.setProperty ("homepage.oracle.jdbc.password", TripleDes.decrypt (prop.getProperty ("homepage.oracle.jdbc.password")));
			
			reader = Resources.getResourceAsReader (resource);
			homeSqlMap = SqlMapClientBuilder.buildSqlMapClient (reader, prop);
			// HOME DB =================================================================
			
			
			// SAKCA DB =================================================================
			properties = "jdbc.properties";
			resource = "SCKSASqlMapConfig.xml";
			prop = Resources.getResourceAsProperties (properties);
			//  데이터베이스 비밀번호 복호화 처리..
			prop.setProperty ("scksa.oracle.jdbc.password", TripleDes.decrypt (prop.getProperty ("scksa.oracle.jdbc.password")));
			reader = Resources.getResourceAsReader (resource);
			scksaSqlMap = SqlMapClientBuilder.buildSqlMapClient (reader, prop);
			// SAKCA DB =================================================================
			
			// PUSH DB =================================================================
			properties = "jdbc.properties";
			resource = "PushSqlMapConfig.xml";
			prop = Resources.getResourceAsProperties (properties);
			//  데이터베이스 비밀번호 복호화 처리..
			prop.setProperty ("push.mysql.jdbc.password", TripleDes.decrypt (prop.getProperty ("push.mysql.jdbc.password")));
			reader = Resources.getResourceAsReader (resource);
			pushSqlMap = SqlMapClientBuilder.buildSqlMapClient (reader, prop);
			// PUSH DB =================================================================
			
		} catch ( Exception e ) {
			logger.error(e.getMessage(), e);
			throw new IbatisException ("Error initializing MyAppSqlConfig class. Cause: " + e);
		}
	}
	
	/**
	 * XO DB
	 * @return
	 */
	public static SqlMapClient getXoSqlMapInstance () {
		return xoSqlMap;
	}
	
	/**
	 * MSR DB
	 * @return
	 */
	public static SqlMapClient getMsrSqlMapInstance () {
		return msrSqlMap;
	}
	
	/**
	 * HOMEPAGE DB
	 * @return
	 */
	public static SqlMapClient getHomeSqlMapInstance () {
		return homeSqlMap;
	}

	/**
	 * SCKSA DB
	 * @return
	 */
	public static SqlMapClient getScksasqlMapInstance() {
		return scksaSqlMap;
	}
	
	/**
	 * SCKSA DB
	 * @return
	 */
	public static SqlMapClient getPushMapInstance() {
		return pushSqlMap;
	}
}
