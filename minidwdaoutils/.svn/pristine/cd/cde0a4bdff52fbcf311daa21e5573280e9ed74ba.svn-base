package com.datamodel.anvizent.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.anvizent.minidw.client.jdbc.utils.Constants;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.service.model.ClientDbCredentials;

public class ClientJdbcInstance  {
	protected static final Log LOGGER = LogFactory.getLog(ClientJdbcInstance.class);

	private String url;
	private String username;
	private String password;
	private int maxActive;
	private boolean isStaging = false;
	private String regionPort;
	private String regionHostName;
	private String clientdbUsername;
	private String clientdbPassword;
	private String clientdbSchema;
	private String clientdbStagingSchema;
	private String appDbSchema;
	private String clientAppDbUsername;
	private String clientAppDbPassword;
	private ClientDbCredentials clientDbCredentials;
	private static final String TEST_QUERY = "SELECT 1";

	public ClientJdbcInstance(ClientDbCredentials clientDbCredentials) throws AnvizentCorewsException {
		this(clientDbCredentials, false);
	}

	public ClientJdbcInstance(ClientDbCredentials clientDbCredentials, boolean isStaging) throws AnvizentCorewsException {
		this.isStaging = isStaging;
		this.clientDbCredentials = clientDbCredentials;
		setDbProperties();
	}

	private void setDbProperties() throws AnvizentCorewsException {
		if (clientDbCredentials != null) {
			try {
				this.regionPort = clientDbCredentials.getPortnumber();
				this.regionHostName = clientDbCredentials.getHostname();

				this.clientdbUsername = clientDbCredentials.getClientDbUsername();
				this.clientdbPassword = clientDbCredentials.getClientDbPassword();
				this.clientAppDbUsername = clientDbCredentials.getClientAppDbUserName();
				this.clientAppDbPassword = clientDbCredentials.getClientAppDbPassword();

				this.clientdbSchema = clientDbCredentials.getClientDbSchema();
				this.clientdbStagingSchema = clientDbCredentials.getClientStagingDbSchema();
				this.appDbSchema = clientDbCredentials.getClientAppDbSchema();

			} catch (Exception e) {
				throw new AnvizentCorewsException(e.getMessage(), e);
			}

		} else {
			throw new AnvizentCorewsException("Database details not found");
		}
	}

	public JdbcTemplate getClientJdbcTemplate(boolean isStaging) throws AnvizentCorewsException {
		return getClientJdbcTemplate(isStaging, false);
	}
	
	public JdbcTemplate getClientJdbcTemplate(boolean isStaging, boolean isAppDb) throws AnvizentCorewsException {

		if (StringUtils.isNotBlank(regionPort) && StringUtils.isNotBlank(regionHostName)
				&& StringUtils.isNotBlank(clientdbPassword) && StringUtils.isNotBlank(clientdbUsername)) {
			String dbParameters = Constants.DataBaseDriverURL.MYSQL_DB_URL_PARAMS;
			this.maxActive = 20;
			if (isAppDb) {
				this.url = Constants.DataBaseDriverURL.MYSQL_DB_URL + regionHostName.trim() + ":" + regionPort.trim() + "/" + appDbSchema.trim();
				this.username = clientAppDbUsername.trim();
				this.password = clientAppDbPassword.trim();
			} else if (isStaging) {
				this.url = Constants.DataBaseDriverURL.MYSQL_DB_URL + regionHostName.trim() + ":" + regionPort.trim() + "/" + clientdbStagingSchema.trim();
				this.username = clientdbUsername.trim();
				this.password = clientdbPassword.trim();
			} else {
				this.url = Constants.DataBaseDriverURL.MYSQL_DB_URL + regionHostName.trim() + ":" + regionPort.trim() + "/" + clientdbSchema.trim();
				this.username = clientdbUsername.trim();
				this.password = clientdbPassword.trim();
			}
			this.url += dbParameters;
			BasicDataSource dataSource = null;
			dataSource = createDataSource(url, username, password);
			int i = 0;
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			try {
				i = jdbcTemplate.queryForObject(TEST_QUERY, Integer.class);
			} catch (CannotGetJdbcConnectionException e) {
				throw new AnvizentCorewsException("Access denied for the client database connection", e);
			}
			if (i == 1) {
				//LOGGER.info("Connection established.");
				//setJdbcTemplate(new JdbcTemplate(dataSource));
				return jdbcTemplate;
			} else {
				//LOGGER.error("Connection not established.");
				throw new AnvizentCorewsException("Client Connection not established.");
			}
		}
		return null;
	}

	
	public JdbcTemplate getClientJdbcTemplate() throws AnvizentCorewsException {
		return getClientJdbcTemplate(isStaging);
	}

	public JdbcTemplate getClientAppdbJdbcTemplate() throws AnvizentCorewsException {
		return getClientJdbcTemplate(false,true);
	}

	public BasicDataSource createDataSource(String url, String username, String password) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
		dataSource.setUrl(url);
		dataSource.setInitialSize(1);
		dataSource.setMaxActive(maxActive);
		dataSource.setMinEvictableIdleTimeMillis(10);
		dataSource.setTestOnBorrow(true);
		dataSource.setTimeBetweenEvictionRunsMillis(10);
		dataSource.setMinIdle(0);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	public Map<String, Object> getClientDbCredentials() throws AnvizentCorewsException  {

		Map<String, Object> clientDbcredentilas = null;
		if (StringUtils.isNotBlank(regionPort) && StringUtils.isNotBlank(regionHostName) && StringUtils.isNotBlank(clientdbPassword)
				&& StringUtils.isNotBlank(clientdbUsername)) {
			clientDbcredentilas = new HashMap<>();
			clientDbcredentilas.put("region_hostname", regionHostName);
			clientDbcredentilas.put("region_port", regionPort);
			clientDbcredentilas.put("clientdb_username", clientdbUsername);
			clientDbcredentilas.put("clientdb_password", clientdbPassword);
			clientDbcredentilas.put("clientdb_schema", clientdbSchema);
			clientDbcredentilas.put("clientdb_staging_schema", clientdbStagingSchema);
			clientDbcredentilas.put("clientappdb_name", appDbSchema);
			clientDbcredentilas.put("clientappdb_username", clientAppDbUsername);
			clientDbcredentilas.put("clientappdb_password", clientAppDbPassword);
		} else {
			throw new AnvizentCorewsException("Client schema details not found");
		}
		return clientDbcredentilas;
	}
}
