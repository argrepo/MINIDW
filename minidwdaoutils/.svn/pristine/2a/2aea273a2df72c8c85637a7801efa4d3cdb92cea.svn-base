package com.datamodel.anvizent.common.sql;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Load SQL from property files.
 * 
 * Note:  The SQL filenames are based on the DAO class they support and must
 * exist in the same package.
 * 
 * @author 
 *
 */
public class SqlHelper {
	protected final Log log = LogFactory.getLog(SqlHelper.class);
	
	private Map<String, String> sqlMap = new HashMap<String, String>();
	
	public SqlHelper(Class<?>[] classes) throws SQLException {
		for (Class<?> clas : classes) {
			loadSql(clas, true);
		}
	}
	
	public SqlHelper(Class<?> clas) throws SQLException {
		loadSql(clas, false);
	}

	/**
	 * Load the SQL from external files on the classpath.
	 * 
	 * @param filenames
	 * @throws IOException
	 */
	private void loadSql(Class<?> clas, boolean prefixWithClassname) throws SQLException {
		String filename = clas.getName().replace('.', File.separatorChar);
		try {
			Properties sql = new Properties();
			String sqlFile = String.format("%s.xml", filename);
			log.debug(String.format("Loading SQL file: %s", sqlFile));
			sql.loadFromXML(clas.getClassLoader().getResourceAsStream(sqlFile));
			String value;
			String mapKey;
			for (Enumeration<?> keys = sql.keys(); keys.hasMoreElements();) {
				value = (String) keys.nextElement();
				value = value.replaceAll("\t", "");	//remove tabs
				value = value.replaceAll("\n", "");	// remove newlines
				if (prefixWithClassname) {
					mapKey = String.format("%s.%s", clas.getSimpleName(), value);
				} else {
					mapKey = value;
				}
				sqlMap.put(mapKey, sql.getProperty(value));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(String.format("Error loading SQL file: %s", filename), ex);
		}
		

	}
	
	/**
	 * Load SQL string based on the SQL ID (filename.key).
	 * 
	 * @param sqlId
	 * @throws SqlNotFoundException
	 * @return
	 */
	public String getSql(String sqlId) throws SqlNotFoundException{
		String sql = sqlMap.get(sqlId);
		if (sql == null) throw new SqlNotFoundException(sqlId);
		return sql;
	}
}
