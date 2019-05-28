package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.anvizent.minidw.client.jdbc.utils.Constants;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.security.AESConverter;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.model.ClientDbCredentials;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.HybridClientsGrouping;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Role;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerClientConfig;
import com.datamodel.anvizent.service.model.User;

/**
 * 
 * @author rakesh.gajula
 *
 */
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	protected static final Log LOG = LogFactory.getLog(UserDaoImpl.class);
	
	private SqlHelper sqlHelper;
	private String dbUserName;
	private String dbPassword;
	private String dbHostName;
	private String dbPortNumber;
	private String dbSchemaName;
	

	public UserDaoImpl(String dbUserName, String dbPassword, String dbHostName, String dbPortNumber,
			String dbSchemaName) {
		this.dbUserName = dbUserName;
		this.dbPassword = dbPassword;
		this.dbHostName = dbHostName;
		this.dbPortNumber = dbPortNumber;
		this.dbSchemaName = dbSchemaName;
	}

	public UserDaoImpl(DataSource appDbSource, String dbUserName, String dbPassword, String dbHostName, String dbPortNumber,
			String dbSchemaName) {
		this(dbUserName, dbPassword, dbHostName, dbPortNumber, dbSchemaName);
		
		try {
			setDataSource(appDbSource);
			this.sqlHelper = new SqlHelper(UserDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating UserDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public int createUser(User user, JdbcTemplate clientJdbcTemplate) {
		int updatedRows = 0;

		try {
			String sql = sqlHelper.getSql("createUser");
			updatedRows = clientJdbcTemplate.update(sql, new Object[] { user.getClientId(), user.getUserId(), user.getUserName(), user.getRoleId(),
					user.getRoleName(), user.getIsTrailUser(), user.getModification().getCreatedBy(), user.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOG.error("error while createUser", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while createUser", e);
			
		}
		return updatedRows;
	}

	@Override
	public User verifyUserAuthentication(String username, String password, JdbcTemplate clientJdbcTemplate) {
		User userInfo = null;
		try {
			
			String sql = sqlHelper.getSql("verifyUserAuthentication");
			userInfo = clientJdbcTemplate.query(sql, new Object[] { username, password }, new ResultSetExtractor<User>() {
				@Override
				public User extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						User user = new User();
						user.setUserId(rs.getString("client_id"));
						user.setUserName(rs.getString("user_name"));
						user.setPassword(rs.getString("password"));
						user.setEmailId(rs.getString("user_email_id"));
						user.setMobileNo(rs.getString("user_mobile_number"));
						user.setActive(rs.getBoolean("isActive"));
						return user;
					} else {
						return null;
					}
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while verifyUserAuthentication", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while verifyUserAuthentication", e);
			
		}

		return userInfo;
	}
	
	@Override
	public ClientDbCredentials getClientDbDetails(String clientId) {
		ClientDbCredentials clientDbCredentials = null;
		try {
			
			
			if (StringUtils.isNotBlank(clientId) && clientId.equals("-1")) {
				clientDbCredentials = new ClientDbCredentials();
				
				clientDbCredentials.setHostname(this.dbHostName);
				clientDbCredentials.setPortnumber(this.dbPortNumber);
				clientDbCredentials.setDatabaseVendor(Constants.DataBaseDriverType.MYSQL_DB_TYPE);
				
				clientDbCredentials.setClientDbSchema(this.dbSchemaName);
				clientDbCredentials.setClientStagingDbSchema(this.dbSchemaName);
				clientDbCredentials.setClientAppDbSchema(this.dbSchemaName);

				clientDbCredentials.setClientDbUsername(this.dbUserName);
				clientDbCredentials.setClientDbPassword(this.dbPassword);
				clientDbCredentials.setClientAppDbUserName(this.dbUserName);
				clientDbCredentials.setClientAppDbPassword(this.dbPassword); 
				return clientDbCredentials;
			}
			
			
			String sql = sqlHelper.getSql("getClientDbDetails");
			
			clientDbCredentials = getJdbcTemplate().query(sql, new Object[] { clientId }, new ResultSetExtractor<ClientDbCredentials>() {
				@Override
				public ClientDbCredentials extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						
						ClientDbCredentials clientDbCredentials = new ClientDbCredentials();
						
						clientDbCredentials.setHostname(rs.getString("hostname"));
						clientDbCredentials.setPortnumber(rs.getString("portnumber"));
						clientDbCredentials.setDatabaseVendor(rs.getString("databasevendor"));
						
						clientDbCredentials.setClientDbSchema(rs.getString("clientdb_schema"));
						clientDbCredentials.setClientStagingDbSchema(rs.getString("clientdb_schema")+"_staging");
						clientDbCredentials.setClientAppDbSchema(rs.getString("clientappdb_name"));

						clientDbCredentials.setClientDbUsername(rs.getString("clientdb_username"));
						clientDbCredentials.setClientDbPassword(rs.getString("clientdb_password"));
						clientDbCredentials.setClientAppDbUserName(rs.getString("clientappdb_username"));
						clientDbCredentials.setClientAppDbPassword(rs.getString("clientappdb_password")); 
						
						return clientDbCredentials;
					} else {
						return null;
					}
				}
			});

			if (clientDbCredentials == null) {
				throw new AnvizentRuntimeException("Client db credentials not found for " + clientId +" client id");
			}
		} catch (DataAccessException ae) {
			LOG.error("error while getClientDbDetails", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientDbDetails", e);
			
		}

		return clientDbCredentials;
	}

	@Override
	public User getUser(String userIdOrUsername, JdbcTemplate clientJdbcTemplate) {
		User userInfo = null;
		try {
			String sql = sqlHelper.getSql("getUser");
			JdbcTemplate requiredJdbcTemplate = clientJdbcTemplate;
			if ( requiredJdbcTemplate == null ) {
				requiredJdbcTemplate = getJdbcTemplate();
			}
			
			userInfo = requiredJdbcTemplate.query(sql, new Object[] { userIdOrUsername, userIdOrUsername }, new ResultSetExtractor<User>() {
				@Override
				public User extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						User user = new User();

						user.setClientId(rs.getString("cloudclientid"));
						user.setUserId(rs.getString("id"));
						user.setUserName(rs.getString("username"));
						user.setRoleId(rs.getInt("roleid"));
						user.setPassword(rs.getString("pswd"));
						user.setEmailId(rs.getString("email"));
						user.setMobileNo(rs.getString("mobile"));
						user.setActive(rs.getBoolean("isactive"));
						return user;
					} else {
						return null;
					}
				}
			});

			
		} catch (DataAccessException ae) {
			LOG.error("error while getUser", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getUser", e);
			
		}

		return userInfo;
	}

	@Override
	public List<Industry> getAllIndustries(JdbcTemplate clientJdbcTemplate) {
		List<Industry> industries = null;
		try {
			String sql = sqlHelper.getSql("getAllIndustries");
			industries = clientJdbcTemplate.query(sql, new Object[] {}, new RowMapper<Industry>() {
				public Industry mapRow(ResultSet rs, int i) throws SQLException {
					Industry industry = new Industry();
					industry.setId(rs.getInt("id"));
					industry.setName(rs.getString("name"));
					return industry;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getAllIndustries", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getAllIndustries", e);
			
		}

		return industries;
	}

	@Override
	public boolean getUserActivationKeyStatus(String userid, JdbcTemplate clientJdbcTemplate) {

		boolean keyStatus = false;

		try {
			String sql = sqlHelper.getSql("getUserActivationKeyStatus");

			String activationKey = clientJdbcTemplate.query(sql, new Object[] { userid }, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					return rs.next() ? rs.getString("activation_key") : null;
				}
			});
			if (activationKey != null) {
				keyStatus = true;
			}
		} catch (DataAccessException ae) {
			LOG.error("error while getUserActivationKeyStatus", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getUserActivationKeyStatus", e);
			
		}
		return keyStatus;
	}

	@Override
	public int saveConnection(String userid, ILConnection connection, JdbcTemplate clientJdbcTemplate) {

		int updatedRows = 0;
		try {
			String sql = sqlHelper.getSql("saveConnection");
			updatedRows = clientJdbcTemplate.update(sql, new Object[] { connection.getConnectionName(), userid, connection.getDatabase(), connection.getServer(),
					connection.getUsername(), connection.getPassword() });
		} catch (DataAccessException ae) {
			LOG.error("error while saveConnection", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveConnection", e);
			
		}
		return updatedRows;
	}

	@Override
	public void createClientSchema(String schemaName, List<DLInfo> dlListForIndustry, List<ILInfo> ilListForAllDLs, JdbcTemplate clientJdbcTemplate) {

		try {

			for (DLInfo dLInfo : dlListForIndustry) {
				clientJdbcTemplate.execute(dLInfo.getdL_schema().replace("schemaName", schemaName));
			}
			for (ILInfo iLInfo : ilListForAllDLs) {
				clientJdbcTemplate.execute(iLInfo.getiL_schema().replace("schemaName", schemaName));
			}

		} catch (DataAccessException ae) {
			LOG.error("error while createClientSchema", ae);
			throw new AnvizentRuntimeException(ae);
		}

	}

	@Override
	public List<DLInfo> getAllDLs(int industryId, JdbcTemplate clientJdbcTemplate) {

		List<DLInfo> dlList = null;
		try {
			String sql = sqlHelper.getSql("getAllDLs");
			dlList = clientJdbcTemplate.query(sql, new Object[] { industryId }, new RowMapper<DLInfo>() {
				public DLInfo mapRow(ResultSet rs, int i) throws SQLException {
					DLInfo dLInfo = new DLInfo();
					dLInfo.setdL_id(rs.getInt("DL_id"));
					dLInfo.setdL_name(rs.getString("DL_name"));
					dLInfo.setdL_table_name(rs.getString("dl_table_name"));
					dLInfo.setDescription(rs.getString("description"));
					dLInfo.setdL_schema(rs.getString("DL_schema"));
					dLInfo.setIndustry(new Industry(rs.getInt("industry_id")));
					dLInfo.setIsStandard(rs.getBoolean("isStandard"));
					dLInfo.setJobName(rs.getString("Job_name"));
					dLInfo.setDL_tgt_key(rs.getString("DL_tgt_key"));
					dLInfo.setIs_Active(rs.getBoolean("isActive"));
					Modification modification = new Modification();
					modification.setCreatedTime(rs.getString("created_time"));
					modification.setModifiedTime(rs.getString("modified_time"));
					dLInfo.setModification(modification);
					dLInfo.setIsDefault(rs.getBoolean("is_default"));
					dLInfo.setVersion(rs.getString("version"));
					return dLInfo;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getAllDLs", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getAllDLs", e);
			
		}

		return dlList;
	}

	@Override
	public List<ILInfo> getAllILs(int dlId, JdbcTemplate clientJdbcTemplate) {

		List<ILInfo> ilList = null;
		try {
			String sql = sqlHelper.getSql("getAllILs");
			ilList = clientJdbcTemplate.query(sql, new Object[] { dlId }, new RowMapper<ILInfo>() {
				public ILInfo mapRow(ResultSet rs, int i) throws SQLException {
					ILInfo iLInfo = new ILInfo();
					iLInfo.setiL_id(rs.getInt("IL_id"));
					iLInfo.setiL_name(rs.getString("IL_name"));
					iLInfo.setiL_table_name(rs.getString("il_table_name"));
					iLInfo.setDescription(rs.getString("description"));
					iLInfo.setiL_schema(rs.getString("IL_schema"));
					iLInfo.setSharedDLIds(rs.getString("DL_id"));
					iLInfo.setJobName(rs.getString("Job_name"));
					iLInfo.setSrcFileContextParamKey(rs.getString("src_file_context_param_key"));
					iLInfo.setTargetTableContextParamKey(rs.getString("target_table_context_param_key"));
					return iLInfo;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getAllILs", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getAllILs", e);
			
		}

		return ilList;
	}

	@Override
	public List<ILInfo> getStagingIlsByDlId(List<Integer> ilIds, JdbcTemplate clientJdbcTemplate) {

		List<ILInfo> ils = null;

		try {
			String sql = sqlHelper.getSql("getStagingIlsByDlId");

			String ids = "";

			int i = 1, len = ilIds.size();
			for (Integer ilId : ilIds) {
				ids += ilId;

				if (i < len)
					ids += ",";
				i++;
			}

			sql = sql.replaceAll("##", ids);

			ils = clientJdbcTemplate.query(sql, new RowMapper<ILInfo>() {

				public ILInfo mapRow(ResultSet rs, int i) throws SQLException {
					ILInfo iLInfo = new ILInfo();
					iLInfo.setiL_name(rs.getString("IL_name"));
					iLInfo.setiL_schema(rs.getString("IL_schema"));

					return iLInfo;
				}

			});

		} catch (Exception e) {
			LOG.error("Error while reading IL staging details by DL id", e);
		}

		return ils;
	}

	@Override
	public Boolean isClientValid(String clientId, JdbcTemplate clientJdbcTemplate) {

		Boolean isClientValid = Boolean.FALSE;
		try {
			String sql = sqlHelper.getSql("isClientValid");
			Integer i = clientJdbcTemplate.queryForObject(sql, new Object[] { clientId, clientId }, Integer.class);
			if (i != null) {
				if (i.intValue() == 1) {
					isClientValid = Boolean.TRUE;
				} else {
					isClientValid = Boolean.FALSE;
				}
			} else {
				isClientValid = Boolean.FALSE;
			}

		} catch (DataAccessException ae) {
			LOG.error("error while isClientValid", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while isClientValid", e);
			
		}

		return isClientValid;
	}

	@Override
	public Industry getIndustryById(int industryId, JdbcTemplate clientJdbcTemplate) {
		Industry industry = null;
		;
		try {
			String sql = sqlHelper.getSql("getIndustryById");

			industry = clientJdbcTemplate.query(sql, new Object[] { industryId }, new ResultSetExtractor<Industry>() {
				@Override
				public Industry extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						Industry ind = new Industry();
						ind.setId(rs.getInt("id"));
						ind.setName(rs.getString("name"));
						return ind;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getIndustryById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getIndustryById", e);
			
		}
		return industry;
	}

	@Override
	public List<Role> getUserRoles(String userIdOrUsername, JdbcTemplate clientJdbcTemplate) {
		List<Role> roles = null;
		try {
			String sql = sqlHelper.getSql("getUserRoles");
			roles = clientJdbcTemplate.query(sql, new Object[] { userIdOrUsername, userIdOrUsername }, new RowMapper<Role>() {
				public Role mapRow(ResultSet rs, int i) throws SQLException {
					Role role = new Role();
					role.setId(rs.getInt("id"));
					role.setName(rs.getString("role"));
					return role;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getUserRoles", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getUserRoles", e);
			
		}

		return roles;
	}


	@Override
	public void createAllJobTables(final String schemaName, JdbcTemplate clientJdbcTemplate) {
	}

	@Override
	public boolean isClientExist(String clientId , JdbcTemplate clientJdbcTemplate) {
		Boolean isClientExist = Boolean.FALSE;
		try {
			String sql = sqlHelper.getSql("isClientExist");
			Integer i = clientJdbcTemplate.queryForObject(sql, new Object[] { clientId }, Integer.class);
			if (i != null) {
				if (i.intValue() >= 1) {
					isClientExist = Boolean.TRUE;
				} else {
					isClientExist = Boolean.FALSE;
				}
			} else {
				isClientExist = Boolean.FALSE;
			}

		} catch (DataAccessException ae) {
			LOG.error("error while isClientExist", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while isClientExist", e);
			
		}

		return isClientExist;
	}

	@Override
	public void mapDLToClient(String clientId, int dlId, boolean isLocked, Modification modification , JdbcTemplate clientJdbcTemplate) {
	}

	@Override
	public Map<String, Object> getLatestMinidwVersion(JdbcTemplate clientJdbcTemplate) {
		Map<String, Object> map = null;
		try {
			String sql = sqlHelper.getSql("getLatestMinidwVersion");
			map = getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Object>>() {

				@Override
				public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Map<String, Object> getLatestVersion = new HashMap<>();
					if (rs != null && rs.next()) {
						getLatestVersion.put("versionId", rs.getInt("version_id"));
						getLatestVersion.put("versionNumber", rs.getString("version_number"));
						getLatestVersion.put("description", rs.getString("description"));
						getLatestVersion.put("filePath", rs.getString("file_path"));
						getLatestVersion.put("fileSize", FileUtils.byteCountToDisplaySize(rs.getInt("file_size")));
						getLatestVersion.put("numberOfDownloads", rs.getString("number_of_downloads"));
						getLatestVersion.put("lastDownloadedDate", rs.getString("last_downloaded_date"));
					}
					return getLatestVersion;
				}
			});

		} catch (DataAccessException ae) {
			LOG.error("error while getLatestMinidwVersion", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getLatestMinidwVersion", e);
			
		}
		return map;
	}

	@Override
	public S3BucketInfo getS3BucketInfoByClientId(String clientId, JdbcTemplate clientJdbcTemplate) {
		S3BucketInfo s3BucketInfo = null;
		try {
			String sql = sqlHelper.getSql("getS3BucketInfoByClientId");
			s3BucketInfo = getJdbcTemplate().query(sql, new Object[] {clientId}, new ResultSetExtractor<S3BucketInfo>() {
				@Override
				public S3BucketInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						S3BucketInfo s3BucketInfo = new S3BucketInfo();
						s3BucketInfo.setId(rs.getInt("id"));
						s3BucketInfo.setBucketName(rs.getString("aws_bucket_name"));
						try {
							s3BucketInfo.setAccessKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("aws_access_key")));
							s3BucketInfo.setSecretKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("aws_secret_key")));
						} catch (Exception e) {
							MinidwServiceUtil.getErrorMessage("ERROR", e);
						}
						return s3BucketInfo;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getS3BucketInfoByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getS3BucketInfoByClientId", e);
			
		}
		return s3BucketInfo;
	}

	@Override
	public S3BucketInfo getS3BucketInfoById(String clientId, long id, JdbcTemplate clientJdbcTemplate) {
		S3BucketInfo s3BucketInfo = null;
		try {
			String sql = sqlHelper.getSql("getS3BucketInfoById");
			s3BucketInfo = getJdbcTemplate().queryForObject(sql, new Object[] {clientId,id}, new RowMapper<S3BucketInfo>() {
				public S3BucketInfo mapRow(ResultSet rs, int i) throws SQLException {
					S3BucketInfo s3BucketInfo = new S3BucketInfo();
					s3BucketInfo.setId(rs.getInt("id"));
					s3BucketInfo.setBucketName(rs.getString("aws_bucket_name"));
					try {
						s3BucketInfo.setAccessKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("aws_access_key")));
						s3BucketInfo.setSecretKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("aws_secret_key")));
					} catch (Exception e) {
						MinidwServiceUtil.getErrorMessage("ERROR", e);
					}
					
					return s3BucketInfo;
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getS3BucketInfoByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getS3BucketInfoByClientId", e);
			
		}
		return s3BucketInfo;
	}
	@Override
	public List<CloudClient> getActiveClientsList() {
		List<CloudClient> cloudClients = null;
		try {
			String sql = sqlHelper.getSql("getActiveClientsList");
			cloudClients = getJdbcTemplate().query(sql , new RowMapper<CloudClient>() {
				public CloudClient mapRow(ResultSet rs, int i) throws SQLException {
					CloudClient cloudClient = new CloudClient();
					cloudClient.setId(rs.getLong("id"));
					cloudClient.setClientName(rs.getString("clientname"));
					try {
						cloudClient.setDeploymentType(AESConverter.decrypt(rs.getString("deployment_type")));
						
					} catch (Exception e) {
						cloudClient.setDeploymentType(com.datamodel.anvizent.helper.Constants.Config.DEPLOYMENT_TYPE_CLOUD);
						LOG.info(cloudClient);
						LOG.error(e.getMessage());
					}
					return cloudClient;
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getS3BucketInfoByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getS3BucketInfoByClientId", e);
			throw new AnvizentRuntimeException(e);
		}
		return cloudClients;
	}
	
	@Override
	public CloudClient getClientDetails(String clientId) {
		CloudClient cloudClient = null;
		try {
			String sql = sqlHelper.getSql("getClientDetails");
			cloudClient = getJdbcTemplate().queryForObject(sql, new Object[] {clientId}, new RowMapper<CloudClient>() {
				public CloudClient mapRow(ResultSet rs, int i) throws SQLException {
					CloudClient cloudClient = new CloudClient();
					cloudClient.setId(rs.getLong("id"));
					cloudClient.setClientName(rs.getString("clientname"));
					cloudClient.setClientDescription(rs.getString("clientdescription"));
					cloudClient.setLicenseId(rs.getLong("licenseid"));
					try {
						cloudClient.setDeploymentType(AESConverter.decrypt(rs.getString("deployment_type")));
					} catch (Exception e) {
						cloudClient.setDeploymentType(com.datamodel.anvizent.helper.Constants.Config.DEPLOYMENT_TYPE_CLOUD);
						logger.error(e.getMessage());}
					cloudClient.setActive(rs.getBoolean("is_active"));
					cloudClient.setDruidEnabled(rs.getBoolean("isdruid_enabled"));
					cloudClient.setRegionId(rs.getLong("region_id"));
					cloudClient.setClientDbSchema(rs.getString("clientdb_schema"));
					cloudClient.setClusterId(rs.getLong("cluster_id"));
					
					return cloudClient;
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getS3BucketInfoByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getS3BucketInfoByClientId", e);
			
		}
		return cloudClient;
	}

	@Override
	public SchedulerClientConfig getSchedulerClientConfigInfo() {
		SchedulerClientConfig schedulerClientConfigInfo = null;
		try {
			String sql = sqlHelper.getSql("getSchedulerClientConfigInfo");
			schedulerClientConfigInfo = getJdbcTemplate().query(sql,new ResultSetExtractor<SchedulerClientConfig>(){

				@Override
				public SchedulerClientConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
					SchedulerClientConfig configInfo = new SchedulerClientConfig();
					if(rs != null && rs.next()){
						configInfo.setThreadCount(rs.getInt("thread_count"));
						configInfo.setThreadPriority(rs.getString("thread_priority"));
						configInfo.setCronExpression(rs.getString("cron_expression"));
						
						return configInfo;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOG.error("error while schedulerClientConfigInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while schedulerClientConfigInfo()", e);
			
		}
		return schedulerClientConfigInfo;
	}
	

	@Override
	public HybridClientsGrouping getHybridClientGroupingDetailsByAccessKey(String accessKey) {
		HybridClientsGrouping hybridClientsGrouping = null;

		try {
			String sql = sqlHelper.getSql("getHybridClientGroupingDetailsByAccessKey");
			hybridClientsGrouping = getJdbcTemplate().query(sql, new Object[] { accessKey }, new ResultSetExtractor<HybridClientsGrouping>() {

				@Override
				public HybridClientsGrouping extractData(ResultSet rs) throws SQLException, DataAccessException {
					HybridClientsGrouping hybridInfo = new HybridClientsGrouping();
					if (rs != null && rs.next()) {
						hybridInfo.setId(rs.getLong("id"));
						hybridInfo.setName(rs.getString("name"));
						hybridInfo.setAccessKey(rs.getString("access_key"));
						hybridInfo.setClientIds(getHybridClientGroupingByIdInfo(hybridInfo.getId()));
						hybridInfo.setClientId(new ArrayList<>());
						for (CloudClient client: hybridInfo.getClientIds()) {
							hybridInfo.getClientId().add(client.getId()+"");
						}
						hybridInfo.setActive(rs.getBoolean("is_active"));
						hybridInfo.setIntervalType(rs.getString("interval_type"));
						hybridInfo.setIntervalTime(rs.getLong("interval_time"));
						hybridInfo.setPackageThreadCount(rs.getLong("thread_count"));
						return hybridInfo;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getHybridClientGroupingDetailsByAccessKey()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getHybridClientGroupingDetailsByAccessKey()", e);
			
		}
		return hybridClientsGrouping;
	}
	

	public List<CloudClient> getHybridClientGroupingByIdInfo(long id) {
		List<CloudClient> cloudClientList = null;
		try {
			
			String sql = sqlHelper.getSql("getHybridClientsGroupingByGroupId");
			cloudClientList = getJdbcTemplate().query(sql,new Object[] { id },new RowMapper<CloudClient>() {
				@Override
				public CloudClient mapRow(ResultSet rs, int rowNum) throws SQLException {
					CloudClient cloudClient = new CloudClient();
					cloudClient.setId(rs.getLong("id"));
					cloudClient.setClientName(rs.getString("clientname"));
					return cloudClient;
				}
			});
			
		} catch (DataAccessException ae) {
			LOG.error("error while getHybridClientsGrouping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getHybridClientsGrouping()", e);
			
		}
		return cloudClientList;
	}

	@Override
	public List<Integer> getClientIds(JdbcTemplate clientJdbcTemplate) {
		List<Integer> clientIds = null;
		try {
			String sql = sqlHelper.getSql("getClientIds");
			clientIds = getJdbcTemplate().query(sql, new ResultSetExtractor<List<Integer>>() {
				
				@Override
				public List<Integer> extractData(ResultSet rs) throws SQLException{
					List<Integer> ids = new ArrayList<>();
					if(rs == null)
						return null;
					while(rs.next()) {
						ids.add(rs.getInt("id"));
					}
					return ids;
				}
			});
		}catch (DataAccessException ae) {
			LOG.error("error while getHybridClientsGrouping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getHybridClientsGrouping()", e);
		}
		return clientIds;
	}

	@Override
	public boolean insertClientJobUploadandExecutionLimitProperty(Integer clientId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("insertJobLimitProperty");
			clientAppDbJdbcTemplate.update("set foreign_key_checks = 0");
			int count = clientAppDbJdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement psmt = con.prepareStatement(sql);
					psmt.setString(1, "client.job.upload.execution.limit");
					psmt.setInt(2, 4);
					psmt.setString(3, "Schedule Client Job");
					psmt.setInt(4, clientId);
					return psmt;
				}
			});
			
			clientAppDbJdbcTemplate.update("set foreign_key_checks = 1");
			if(count > 0)
				return true;
			else 
				return false;
		}catch (DataAccessException ae) {
			LOG.error("error while getHybridClientsGrouping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getHybridClientsGrouping()", e);
		}
		return false;
	}

	@Override
	public void deleteClientJobUploadandExecutionLinitProperty(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		try {
			String sql = "delete from anvizent_properties where prop_key = 'client.job.upload.execution.limit'";
			clientAppDbJdbcTemplate.update(sql);
		}catch (DataAccessException ae) {
			LOG.error("error while getHybridClientsGrouping()", ae);
			throw new AnvizentRuntimeException(ae);
		}
	}
	
}
