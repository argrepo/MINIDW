package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.StandardPackageDao;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.WebService;

public class StandardPackageDaoImpl extends JdbcDaoSupport implements StandardPackageDao{
	
	protected static final Log LOG = LogFactory.getLog(StandardPackageDaoImpl.class);

	private SqlHelper sqlHelper;
	
	public StandardPackageDaoImpl(DataSource dataSource){
		setDataSource(dataSource);
		try{
			this.sqlHelper = new SqlHelper(StandardPackageDaoImpl.class);
		}catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating StandardPackageDaoImpl SqlHelper.", ex);
		}
	}
	
	@Override
	public Package fetchStandardPackageInfo(String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		Package userPackage = new Package();
		  try{
			  String query = sqlHelper.getSql("getStandardPackage");
			  userPackage = clientAppDbJdbcTemplate.query(query, new Object[] { true }, new ResultSetExtractor<Package>(){

				@Override
				public Package extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						
						Package package0 = new Package();
						package0.setPackageId(rs.getInt("package_id"));
						package0.setPackageName(rs.getString("package_name"));
						package0.setDescription(rs.getString("description"));
						package0.setIndustry(new Industry(rs.getInt("industryId"), rs.getString("industryName")));
						package0.setIsStandard(rs.getBoolean("isStandard"));
						package0.setIsActive(rs.getBoolean("isActive"));
						package0.setIsScheduled(rs.getBoolean("isScheduled"));
						package0.setIsAdvanced(rs.getBoolean("isAdvanced"));
						package0.setIsMapped(rs.getBoolean("isMapped"));
						package0.setScheduleStatus(rs.getString("schedule_status"));
						package0.setIsClientDbTables(rs.getBoolean("isClientDbTables"));
						package0.setTrailingMonths(rs.getInt("trailing_months"));
						package0.setFilesHavingSameColumns(rs.getBoolean("files_having_same_columns"));
						Modification modification = new Modification();
						modification.setCreatedTime(rs.getString("created_time"));
						package0.setModification(modification);
						
						return package0;
					}else{
						return null;
					}
				}
			  });
		  }catch (SqlNotFoundException | DataAccessException ae) {
				LOG.error("error while getUserPackages()", ae);
				throw new AnvizentRuntimeException(ae);
			}
		return userPackage;
	}

	@Override
	public int createStandardPackage(Package createUserPackage, JdbcTemplate clientAppDbJdbcTemplate) {
		Integer packageId = 0;
		int autoIncrementId = 0;
		try{
			String insertQuery = sqlHelper.getSql("createStandardPackage");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

					PreparedStatement ps = con.prepareStatement(insertQuery, new String[] { "package_id" });

					ps.setString(1, createUserPackage.getPackageName());
					ps.setLong(2, createUserPackage.getIndustry().getId());
					ps.setBoolean(3, createUserPackage.getIsStandard());
					ps.setString(4, createUserPackage.getDescription());
					ps.setBoolean(5, createUserPackage.getIsScheduled());
					ps.setBoolean(6, createUserPackage.getIsMapped());
					ps.setString(7, createUserPackage.getScheduleStatus());
					ps.setInt(8, createUserPackage.getTrailingMonths());
					ps.setBoolean(9, createUserPackage.getIsAdvanced());
					ps.setString(10, createUserPackage.getUserId());
					ps.setString(11, createUserPackage.getModification().getCreatedBy());
					ps.setString(12, createUserPackage.getModification().getCreatedTime());
					return ps;
				}
			}, keyHolder);
			// get package id
			
			if(keyHolder != null){
				Number num = keyHolder.getKey();
				autoIncrementId =  num.intValue();
			}
			
			String updateQuery = sqlHelper.getSql("updateAutoIncrementToDefaulSPId");
			 int result = clientAppDbJdbcTemplate.update(updateQuery, new Object[]{autoIncrementId});
			 if(result == 1){
				 packageId = createUserPackage.getPackageId();
			 }
			/*
				String sqlGetPackageIdByName = sqlHelper.getSql("getPackageIdByName");
				packageId = clientAppDbJdbcTemplate.query(sqlGetPackageIdByName,
						new Object[] { createUserPackage.getPackageName(), createUserPackage.getUserId() },
						new ResultSetExtractor<Integer>() {
							@Override
							public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
								return rs.next() ? rs.getInt("package_id") : null;
							}
						});*/
				
		}catch(SqlNotFoundException | DataAccessException ae){
			LOG.error("error while getUserPackages()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return packageId;
	}


	@Override
	public List<ILConnectionMapping> getILSourceListById(String userId, String clientId, Integer dlId, Integer ilId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ILConnectionMapping> iLMappingList = new ArrayList<>();
		List<ILConnectionMapping> iLMappingListWithDatabase = null;
		List<ILConnectionMapping> iLMappingListWithFile = null;
		List<ILConnectionMapping> iLMappingListWithWebService = null;
		try {

			String sql = sqlHelper.getSql("getILConnectionMappingInfoByDLAndIL");
			Object[] parameters = new Object[] { clientId, dlId, ilId };
			 
			iLMappingListWithDatabase = clientAppDbJdbcTemplate.query(sql, parameters, new RowMapper<ILConnectionMapping>() {
				public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {
					if(!rs.getBoolean("isActive")){
						return null;
					}
					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setFileType(rs.getString("fileType"));
					ilConnectionMapping.setFilePath(rs.getString("filePath"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
					ilConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
					ilConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
					ilConnectionMapping.setiLquery(rs.getString("IL_query") != null ? (rs.getString("IL_query")) : "");
					ilConnectionMapping.setMaxDateQuery(
							rs.getString("max_date_query") != null ? (rs.getString("max_date_query")) : "");
					ilConnectionMapping.setDatabaseName(
							rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
					ilConnectionMapping.setIsIncrementalUpdate(rs.getBoolean("isIncrementalUpdate"));
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));
					ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
					ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));

					ilConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));

					ilConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
					ilConnectionMapping.setParent_table_name(rs.getString("parent_table_name"));

					ILConnection connection = new ILConnection();
					connection.setConnectionId(rs.getInt("connection_id"));
					connection.setConnectionName(
							rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
					connection.setConnectionType(
							rs.getString("connection_type") != null ? rs.getString("connection_type") : "");
					connection.setAvailableInCloud(rs.getBoolean("available_in_cloud"));
				    connection.setSslEnable(rs.getBoolean("ssl_enable"));
					connection.setSslTrustKeyStoreFilePaths(rs.getString("ssl_trust_key_store_file_paths")); 

					Database database = new Database();
					database.setId(rs.getInt("dataBaseId"));
					database.setName(rs.getString("name") != null ? rs.getString("name") : "");
					database.setConnector_id(rs.getInt("connectorId"));
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setUrlFormat(rs.getString("url_format"));

					connection.setDatabase(database);
					connection.setServer(rs.getString("server") != null ? rs.getString("server") : "");
					connection.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
					connection.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
					ilConnectionMapping.setiLConnection(connection);
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
					ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));

					WebService webService = new WebService();
					webService.setWsConId(rs.getInt("webservice_Id"));
					ilConnectionMapping.setWebService(webService);
					ilConnectionMapping.setIsHistoricalLoad(rs.getBoolean("is_historical_load"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));

					return ilConnectionMapping;
				}
			});

			String sql2 = sqlHelper.getSql("getILConnectionMappingInfoWhenFileBYDLAndIL");
			Object[] parameters2 = new Object[] { dlId, ilId };
			iLMappingListWithFile = clientAppDbJdbcTemplate.query(sql2, parameters2, new RowMapper<ILConnectionMapping>() {
				
				public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {				
					if(!rs.getBoolean("isActive")){
						return null;
					}
					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
					ilConnectionMapping.setFileType(rs.getString("fileType"));
					ilConnectionMapping.setEncryptionRequired(rs.getBoolean("encryption_required"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setFilePath(rs.getString("filePath"));
					ilConnectionMapping.setSourceFileInfoId(rs.getLong("source_file_info_id"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
					ilConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));
					ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));
					ilConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
					ILConnection connection = new ILConnection();
					Database database = new Database();
					connection.setDatabase(database);
					ilConnectionMapping.setiLConnection(connection);
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
					ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));

					WebService webService = new WebService();
					webService.setWsConId(rs.getInt("webservice_Id"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));

					ilConnectionMapping.setWebService(webService);

					return ilConnectionMapping;
				}
			});

			
			Object[] parameters4 = new Object[] { clientId, dlId, ilId };
			String sql4 = sqlHelper.getSql("getILConnectionMappingInfoWhenWebServiceByDLAndIL");
			iLMappingListWithWebService = clientAppDbJdbcTemplate.query(sql4, parameters4, new RowMapper<ILConnectionMapping>() {
				public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {
					if(!rs.getBoolean("isActive")){
						return null;
					}
					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
					ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));
					ilConnectionMapping.setWebserviceMappingHeaders(rs.getString("webservice_mapping_headers"));
					ilConnectionMapping.setIsFlatFile(Boolean.FALSE);
					ilConnectionMapping.setIsHavingParentTable(Boolean.FALSE);
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));
					ilConnectionMapping.setJoinWebService(rs.getBoolean("is_join_web_service"));
					ilConnectionMapping.setiLquery(rs.getString("IL_query"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setWebserviceJoinUrls(rs.getString("web_service_join_urls"));

					WebService webService = new WebService();
					webService.setApiName(rs.getString("api_name"));
					webService.setUrl(rs.getString("api_url"));
					webService.setWebserviceName(rs.getString("web_service_name"));
					webService.setWsConId(rs.getInt("webservice_Id"));					
					ilConnectionMapping.setWebService(webService);

					return ilConnectionMapping;
				}

			});
			for (ILConnectionMapping ilConnectionData : iLMappingListWithDatabase) {
				if(ilConnectionData != null){
					iLMappingList.add(ilConnectionData);
				}
			}
			for (ILConnectionMapping ilConnectionData : iLMappingListWithFile) {
				if(ilConnectionData != null){
					iLMappingList.add(ilConnectionData);
				}
			}
			for (ILConnectionMapping ilConnectionData : iLMappingListWithWebService) {
				if(ilConnectionData != null){
					iLMappingList.add(ilConnectionData);
				}
			}
			
			iLMappingList.sort((s1, s2) -> s1.getConnectionMappingId() - s2.getConnectionMappingId());
			
		} catch (DataAccessException ae) {
			LOG.error("error while getSourcesList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getSourcesList()", e);
		}

		return iLMappingList;
	}

	@Override
	public DLInfo getIlMappingInfobyId(String userId, String clientId, Integer dlId, JdbcTemplate clientAppDbJdbcTemplate) {
		   DLInfo dlInfo = new DLInfo();
		   List<ILInfo> ilList = new ArrayList<ILInfo>();
		   try{
			   String query = sqlHelper.getSql("getSourcesInfo");
			   ilList = clientAppDbJdbcTemplate.query(query, new Object[] { dlId }, new RowMapper<ILInfo>() {
				   @Override
					public ILInfo mapRow(ResultSet rs, int i) throws SQLException {
					   
					    ILInfo ilData = new ILInfo();
					      ilData.setiL_id(rs.getInt("IL_id"));
					      ilData.setiL_name(rs.getString("IL_name"));
					      ilData.setIsActive(rs.getBoolean("isActive"));
					      ilData.setIlSources(getILSourceListById(userId, clientId, dlId, ilData.getiL_id(), clientAppDbJdbcTemplate));
					      
					      dlInfo.setdL_id(rs.getInt("DL_id"));
					      dlInfo.setdL_name(rs.getString("DL_name"));
					      dlInfo.setdL_table_name(rs.getString("dl_table_name"));
					      dlInfo.setJobName(rs.getString("Job_name"));
					      dlInfo.setDependencyJars(rs.getString("dependency_jars"));
					      dlInfo.setDescription(rs.getString("description"));
					      
					      Industry industry = new Industry();
					       industry.setId(rs.getInt("industry_id"));
					       dlInfo.setIndustry(industry);
					      
						return ilData;
					}
				});
			   
			   dlInfo.setIlList(ilList.toArray(new ILInfo[0]));
		   }catch(SqlNotFoundException | DataAccessException ae){
			  LOG.error("error while getUserPackages()", ae);
			  throw new AnvizentRuntimeException(ae);
		   }
		return dlInfo;
	}

	@Override
	public DLInfo getIlMappingInfobyId(String userId, String clientId, Integer dlId, Integer iLid, JdbcTemplate clientAppDbJdbcTemplate) {
		   DLInfo dlInfo = new DLInfo();
		   ILInfo ilInfo = new ILInfo();
		   try{
			   String query = sqlHelper.getSql("getSourcesInfoByIlId");
			   ilInfo = clientAppDbJdbcTemplate.query(query, new Object[] { dlId, iLid }, new ResultSetExtractor<ILInfo>() {
				@Override
				public ILInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						ILInfo ilData = new ILInfo();
					      ilData.setiL_id(rs.getInt("IL_id"));
					      ilData.setiL_name(rs.getString("IL_name"));
					      dlInfo.setdL_name(rs.getString("DL_name"));
					      ilData.setIsActive(rs.getBoolean("isActive"));
					      ilData.setIlSources(getILSourceListById(userId, clientId, dlId, iLid, clientAppDbJdbcTemplate));
					      
						return ilData;
					}else{
						return null;
					  }
				   }
				});
			   dlInfo.setdL_id(dlId);
			   dlInfo.setIlInfo(ilInfo);
		   }catch(SqlNotFoundException | DataAccessException ae){
			  LOG.error("error while getUserPackages()", ae);
			  throw new AnvizentRuntimeException(ae);
		   }
		return dlInfo;
	}
	
	@Override
	public List<ILConnectionMapping> getILConnectionMappingInfoByMappingId(List<Integer> mappingsIds, String userId,
			JdbcTemplate clientJdbcTemplate) {

		List<ILConnectionMapping> iLMappings = new ArrayList<ILConnectionMapping>();
		ILConnectionMapping iLMapping = null;
		try {
           for(Integer mappingId : mappingsIds){
        	   
        	   String sql = sqlHelper.getSql("getILConnectionMappingInfoWhenFileById");
   			Object[] parametres = new Object[] { mappingId };
   			iLMapping = clientJdbcTemplate.query(sql, parametres, new ResultSetExtractor<ILConnectionMapping>() {
   				@Override
   				public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {

   					if (rs != null && rs.next()) {
   						ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
   						try {
   							
   							ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
   							ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
   							ilConnectionMapping.setFileType(rs.getString("fileType"));
   							ilConnectionMapping.setEncryptionRequired(rs.getBoolean("encryption_required"));
   							ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
   							ilConnectionMapping.setStorageType(rs.getString("storage_type"));
   							ilConnectionMapping.setFilePath(rs.getString("filePath"));
   							ilConnectionMapping.setSourceFileInfoId(rs.getLong("source_file_info_id"));
   							ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
   							ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
   							ilConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
   							ilConnectionMapping.setiLId(rs.getInt("IL_id"));
   							ilConnectionMapping.setdLId(rs.getInt("DL_id"));
   							ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));
   							ilConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
   							ILConnection connection = new ILConnection();
   							Database database = new Database();
   							connection.setDatabase(database);
   							ilConnectionMapping.setiLConnection(connection);
   							ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
   							ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));

   							WebService webService = new WebService();
   							webService.setWsConId(rs.getInt("webservice_Id"));
   							ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));

   							ilConnectionMapping.setWebService(webService);

   						} catch (Exception e) {
   							LOG.error("error while getILConnectionMappingInfo()", e);
   						}
   						return ilConnectionMapping;
   					} else {
   						return null;
   					}
   				}
   			});
   			if (iLMapping == null) {

   				String sql2 = sqlHelper.getSql("getILConnectionMappingInfoWhenDatabaseById");
   				Object[] parameters2 = new Object[] { mappingId };
   				iLMapping = clientJdbcTemplate.query(sql2, parameters2, new ResultSetExtractor<ILConnectionMapping>() {
   					@Override
   					public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {

   						if (rs != null && rs.next()) {
   							ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
   							try {
   								ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
   								ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
   								ilConnectionMapping.setFileType(rs.getString("fileType"));
   								ilConnectionMapping.setStorageType(rs.getString("storage_type"));
   								ilConnectionMapping.setFilePath(rs.getString("filePath"));
   								ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
   								ilConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
   								ilConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
   								ilConnectionMapping.setiLquery(rs.getString("IL_query") != null ? (rs.getString("IL_query")) : "");
   								ilConnectionMapping.setMaxDateQuery(rs.getString("max_date_query") != null ? (rs.getString("max_date_query")) : "");
   								ilConnectionMapping.setDatabaseName(rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
   								ilConnectionMapping.setiLId(rs.getInt("IL_id"));
   								ilConnectionMapping.setdLId(rs.getInt("DL_id"));
   								ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
   								ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));
   								ilConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));
   								ilConnectionMapping.setIsIncrementalUpdate(rs.getBoolean("isIncrementalUpdate"));
   								ilConnectionMapping.setIsHistoricalLoad(rs.getBoolean("is_historical_load"));
   								ILConnection connection = new ILConnection();
   								connection.setConnectionId(rs.getInt("connection_id"));
   								connection.setConnectionName(rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
   								connection.setConnectionType(rs.getString("connection_type") != null ? rs.getString("connection_type") : "");
   								connection.setAvailableInCloud(rs.getBoolean("available_in_cloud"));
   								connection.setSslEnable(rs.getBoolean("ssl_enable"));
   								connection.setSslTrustKeyStoreFilePaths(rs.getString("ssl_trust_key_store_file_paths"));
   								
   								Database database = new Database();
   								database.setId(rs.getInt("dataBaseId"));
   								database.setName(rs.getString("name") != null ? rs.getString("name") : "");
   								database.setConnector_id(rs.getInt("connectorId"));
   								database.setDriverName(rs.getString("driver_name"));
   								database.setProtocal(rs.getString("protocal"));
   								database.setUrlFormat(rs.getString("url_format"));

   								connection.setDatabase(database);
   								connection.setServer(rs.getString("server") != null ? rs.getString("server") : "");
   								connection.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
   								connection.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
   								connection.setDbVariables(rs.getString("db_variables"));
   								ilConnectionMapping.setiLConnection(connection);
   								ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));

   							} catch (Exception e) {
   								LOG.error("error while getILConnectionMappingInfo()", e);
   								
   							}
   							return ilConnectionMapping;
   						} else {
   							return null;
   						}
   					}
   				});
   			}
   			if (iLMapping == null) {
   				String sql3 = sqlHelper.getSql("getILConnectionMappingInfoWhenWebServiceByMappingId");
   				Object[] parameters3 = new Object[] { mappingId };
   				iLMapping = clientJdbcTemplate.query(sql3, parameters3, new ResultSetExtractor<ILConnectionMapping>() {

   					@Override
   					public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {
   						if (rs != null && rs.next()) {
   							ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
   							
   							ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
   							ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
   							ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
   							ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
   							ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));
   							ilConnectionMapping.setStorageType(rs.getString("storage_type"));
   							ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));
   							ilConnectionMapping.setWebserviceMappingHeaders(rs.getString("webservice_mapping_headers"));
   							ilConnectionMapping.setIsFlatFile(Boolean.FALSE);
   							ilConnectionMapping.setIsHavingParentTable(Boolean.FALSE);
   							ilConnectionMapping.setiLId(rs.getInt("IL_id"));
   							ilConnectionMapping.setdLId(rs.getInt("DL_id"));
   							ilConnectionMapping.setJoinWebService(rs.getBoolean("is_join_web_service"));
   							ilConnectionMapping.setiLquery(rs.getString("IL_query"));
   							ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
   							ilConnectionMapping.setWebserviceJoinUrls(rs.getString("web_service_join_urls"));

   							WebService webService = new WebService();
   							webService.setApiName(rs.getString("api_name"));
   							webService.setUrl(rs.getString("api_url"));
   							webService.setWebserviceName(rs.getString("web_service_name"));
   							webService.setWsConId(rs.getInt("webservice_Id"));	
   							webService.setFileType(rs.getString("fileType"));
   							ilConnectionMapping.setWebService(webService);

   						return ilConnectionMapping;
   						} else {
   							return null;
   						}
   					}
   				});
   			}
   			iLMappings.add(iLMapping);
           }
		} catch (DataAccessException ae) {
			LOG.error("error while getILConnectionMappingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILConnectionMappingInfo()", e);
		}
		return iLMappings;
	}

	@Override
	public List<ILConnectionMapping> getILConnectionMappingInfoByDLId(String userId, String clientId, Integer dlId, JdbcTemplate clientAppDbJdbcTemplate,
			boolean isActiveRequired) {
		List<ILConnectionMapping> iLMappingList = new ArrayList<>();
		List<ILConnectionMapping> iLMappingListWithDatabase = null;
		List<ILConnectionMapping> iLMappingListWithFile = null;
		List<ILConnectionMapping> iLMappingListWithParentTable = null;
		List<ILConnectionMapping> iLMappingListWithWebService = null;
		try {
			String sql = sqlHelper.getSql("getILConnectionMappingInfoDataBaseByDLId");
			Object[] parameters = new Object[] { clientId, dlId };
			 
			iLMappingListWithDatabase = clientAppDbJdbcTemplate.query(sql, parameters, new RowMapper<ILConnectionMapping>() {
				public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {
					
					if(isActiveRequired && !rs.getBoolean("isActive")){
						return null;
					}

					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setFileType(rs.getString("fileType"));
					ilConnectionMapping.setFilePath(rs.getString("filePath"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
					ilConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
					ilConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
					ilConnectionMapping.setiLquery(rs.getString("IL_query") != null ? (rs.getString("IL_query")) : "");
					ilConnectionMapping.setMaxDateQuery(rs.getString("max_date_query") != null ? (rs.getString("max_date_query")) : "");
					ilConnectionMapping.setDatabaseName(rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
					ilConnectionMapping.setIsIncrementalUpdate(rs.getBoolean("isIncrementalUpdate"));
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));
					ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
					ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));

					ilConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));

					ilConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
					ilConnectionMapping.setParent_table_name(rs.getString("parent_table_name"));

					ILConnection connection = new ILConnection();
					connection.setConnectionId(rs.getInt("connection_id"));
					connection.setConnectionName(rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
					connection.setConnectionType(rs.getString("connection_type") != null ? rs.getString("connection_type") : "");
					connection.setAvailableInCloud(rs.getBoolean("available_in_cloud"));
					connection.setSslEnable(rs.getBoolean("ssl_enable"));
					connection.setSslTrustKeyStoreFilePaths(rs.getString("ssl_trust_key_store_file_paths"));
						
					Database database = new Database();
					database.setId(rs.getInt("dataBaseId"));
					database.setName(rs.getString("name") != null ? rs.getString("name") : "");
					database.setConnector_id(rs.getInt("connectorId"));
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setUrlFormat(rs.getString("url_format"));

					connection.setDatabase(database);
					connection.setServer(rs.getString("server") != null ? rs.getString("server") : "");
					connection.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
					connection.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
					ilConnectionMapping.setiLConnection(connection);
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
					ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));

					WebService webService = new WebService();
					webService.setWsConId(rs.getInt("webservice_Id"));
					ilConnectionMapping.setWebService(webService);
					ilConnectionMapping.setIsHistoricalLoad(rs.getBoolean("is_historical_load"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));

					return ilConnectionMapping;
				}
			});

			String sql2 = sqlHelper.getSql("getILConnectionMappingInfoWhenFileByDLId");
			Object[] parameters2 = new Object[] { dlId };
			iLMappingListWithFile = clientAppDbJdbcTemplate.query(sql2, parameters2, new RowMapper<ILConnectionMapping>() {
				
				public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {	
					if(isActiveRequired && !rs.getBoolean("isActive")){
						return null;
					}

					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
					ilConnectionMapping.setFileType(rs.getString("fileType"));
					ilConnectionMapping.setEncryptionRequired(rs.getBoolean("encryption_required"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setFilePath(rs.getString("filePath"));
					ilConnectionMapping.setSourceFileInfoId(rs.getLong("source_file_info_id"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setDelimeter(rs.getString("delimeter"));
					ilConnectionMapping.setIsFirstRowHasColoumnNames(rs.getBoolean("first_row_has_coloumn_names"));
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));
					ilConnectionMapping.setiLJobStatusForRunNow(rs.getString("iL_job_status_for_run_now"));
					ilConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
					ILConnection connection = new ILConnection();
					Database database = new Database();
					connection.setDatabase(database);
					ilConnectionMapping.setiLConnection(connection);
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
					ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));

					WebService webService = new WebService();
					webService.setWsConId(rs.getInt("webservice_Id"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));

					ilConnectionMapping.setWebService(webService);

					return ilConnectionMapping;
				}
			});

			String sql3 = sqlHelper.getSql("getILConnectionMappingInfoWhenParentTableByDLId");
			Object[] parameters3 = new Object[] { dlId, };
			iLMappingListWithParentTable = clientAppDbJdbcTemplate.query(sql3, parameters3, new RowMapper<ILConnectionMapping>() {
				public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {

					if(isActiveRequired && !rs.getBoolean("isActive")){
						return null;
					}
					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
					ilConnectionMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
					ilConnectionMapping.setParent_table_name(rs.getString("parent_table_name"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setFilePath(rs.getString("filePath"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));

					WebService webService = new WebService();
					webService.setWeb_service_id(rs.getString("webservice_Id"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));

					ilConnectionMapping.setWebService(webService);

					return ilConnectionMapping;
				}
			});
			
			Object[] parameters4 = new Object[] { clientId, dlId };
			String sql4 = sqlHelper.getSql("getILConnectionMappingInfoWhenWebServiceByDLId");
			iLMappingListWithWebService = clientAppDbJdbcTemplate.query(sql4, parameters4, new RowMapper<ILConnectionMapping>() {
				public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {

					if(isActiveRequired && !rs.getBoolean("isActive")){
						return null;
					}
					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					
					ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
					ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
					ilConnectionMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
					ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
					ilConnectionMapping.setWsConId(rs.getInt("webservice_Id"));
					ilConnectionMapping.setStorageType(rs.getString("storage_type"));
					ilConnectionMapping.setActiveRequired(rs.getBoolean("isActive"));
					ilConnectionMapping.setWebserviceMappingHeaders(rs.getString("webservice_mapping_headers"));
					ilConnectionMapping.setIsFlatFile(Boolean.FALSE);
					ilConnectionMapping.setIsHavingParentTable(Boolean.FALSE);
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));
					ilConnectionMapping.setJoinWebService(rs.getBoolean("is_join_web_service"));
					ilConnectionMapping.setiLquery(rs.getString("IL_query"));
					ilConnectionMapping.setIlSourceName(rs.getString("il_source_name"));
					ilConnectionMapping.setWebserviceJoinUrls(rs.getString("web_service_join_urls"));

					WebService webService = new WebService();
					webService.setApiName(rs.getString("api_name"));
					webService.setUrl(rs.getString("api_url"));
					webService.setWebserviceName(rs.getString("web_service_name"));
					webService.setWsConId(rs.getInt("webservice_Id"));					
					ilConnectionMapping.setWebService(webService);

					return ilConnectionMapping;
				}
			});
			for (ILConnectionMapping ilConnectionData : iLMappingListWithDatabase) {
				if(ilConnectionData != null){
					iLMappingList.add(ilConnectionData);
				}
			}
			for (ILConnectionMapping ilConnectionData : iLMappingListWithFile) {
				if(ilConnectionData != null){
					iLMappingList.add(ilConnectionData);
				}
			}
			for (ILConnectionMapping ilConnectionData : iLMappingListWithParentTable) {
				if(ilConnectionData != null){
					iLMappingList.add(ilConnectionData);
				}
			}
			for (ILConnectionMapping ilConnectionData : iLMappingListWithWebService) {
				if(ilConnectionData != null){
					iLMappingList.add(ilConnectionData);
				}
			}
		} catch (DataAccessException ae) {
			LOG.error("error while getSourcesList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getSourcesList()", e);
		}
		return iLMappingList;
	}

	@Override
	public List<DLInfo> getClientSPDLs(String userId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<DLInfo> dlList = null;
		try {
			String sql = sqlHelper.getSql("getClientStandardPackageDLs");
			String getKpis = sqlHelper.getSql("getDLKpiList");

			dlList = clientAppDbJdbcTemplate.query(sql, new Object[] { userId, userId }, new RowMapper<DLInfo>() {
				public DLInfo mapRow(ResultSet rs, int i) throws SQLException {
					DLInfo dLInfo = new DLInfo();
					dLInfo.setdL_id(rs.getInt("DL_id"));
					dLInfo.setdL_name(rs.getString("DL_name"));
					dLInfo.setdL_table_name(rs.getString("dl_table_name"));
					dLInfo.setDescription(rs.getString("description"));
					dLInfo.setIndustry(new Industry(rs.getInt("industry_id")));
					dLInfo.setIsStandard(rs.getBoolean("isStandard"));
					dLInfo.setJobName(rs.getString("Job_name"));
					dLInfo.setIsLocked(rs.getBoolean("isLocked"));
					Schedule schedule = new Schedule();
					
					schedule.setScheduleId(rs.getInt("schedule_id"));
					schedule.setScheduleType(rs.getString("schedule_type"));
					schedule.setRecurrencePattern(rs.getString("recurrence_pattern"));
					schedule.setScheduleStartDate(rs.getString("schedule_start_date"));
					schedule.setScheduleStartTime(rs.getString("schedule_start_time"));
					schedule.setDaysToRun(rs.getString("days_to_run"));
					schedule.setWeeksToRun(rs.getString("weeks_to_run"));
					schedule.setDayOfMonth(rs.getString("day_of_month"));
					schedule.setMonthsToRun(rs.getString("months_to_run"));
					schedule.setDayOfYear(rs.getString("day_of_year"));
					schedule.setMonthOfYear(rs.getString("month_of_year"));
					schedule.setYearsToRun(rs.getString("years_to_run"));
					schedule.setIsNoEndDate(rs.getBoolean("is_no_end_date"));
					schedule.setScheduleEndDate(rs.getString("end_date"));
					schedule.setNoOfMaxOccurences(rs.getString("no_of_max_occurences"));
					schedule.setTimeZone(rs.getString("time_zone"));
					schedule.setTypeOfHours(rs.getString("type_of_hours"));
					schedule.setHoursToRun(rs.getString("hours_to_run"));
					schedule.setMinutesToRun(rs.getInt("minutes_to_run"));
					dLInfo.setSchedule(schedule);
					dLInfo.setTrailingMonths(rs.getInt("trailing_months"));
					return dLInfo;
				}
			});
			for (DLInfo dlInfo : dlList) {
				List<String> kpiList = clientAppDbJdbcTemplate.query(getKpis, new Object[] { dlInfo.getdL_id() },
						new RowMapper<String>() {
							public String mapRow(ResultSet rs, int i) throws SQLException {
								String kpi = rs.getString("kpi_name");
								return kpi;
							}
						});
				dlInfo.setKpi(kpiList);
			}
		} catch (DataAccessException ae) {
			LOG.error("error while getClientDLs", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientDLs", e);
		}

		return dlList;
	}

	@Override
	public int updatePackageSchedule(Schedule schedule, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateDLSchedule");
			update = clientAppDbJdbcTemplate.update(sql,
					new Object[] {
							schedule.getScheduleType(),
							schedule.getScheduleId()
							});
		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageExecutionStatusInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageExecutionStatusInfo()", e);
		}
		return update;
	}

	@Override
	public List<PackageExecution> getPackageExecutionResults(Integer dlId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<PackageExecution> packageDetails = null;
		try {
			String sql = sqlHelper.getSql("getStandardPackageExecutionResults");
			packageDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { dlId },
					new RowMapper<PackageExecution>() {
						@Override
						public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
							PackageExecution packageExecution = new PackageExecution();
							packageExecution.setExecutionId(rs.getInt("execution_id"));
							packageExecution.setPackageId(rs.getInt("package_id"));
							packageExecution.setInitiatedFrom(rs.getString("initiated_from"));
							packageExecution.setRunType(rs.getString("run_type"));
							packageExecution.setUploadStatus(rs.getString("upload_status"));
							packageExecution.setExecutionStatus(rs.getString("execution_status"));
							packageExecution.setDruidStatus(rs.getString("druid_status"));
							packageExecution.setExecutionStartDate(rs.getString("exection_start_date"));
							packageExecution.setLastExecutedDate(rs.getString("last_executed_date"));
							packageExecution.setUploadStartDate(rs.getString("upload_start_date"));
							packageExecution.setLastUploadedDate(rs.getString("last_uploaded_date")); 
							packageExecution.setDruidStartDate(rs.getString("druid_start_date"));
							packageExecution.setDruidEndDate(rs.getString("druid_end_date"));
							return packageExecution;
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getPackageExecution()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageExecution()", e);
		}
		return packageDetails;

	
	}

	@Override
	public List<JobResult> getExecutionJobResults(String packageId, Integer dlId, String clientId,
			JdbcTemplate clientJdbcTemplate) {

		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_" + packageId + "\\_" + dlId + "\\_%";
			String sql = sqlHelper.getSql("getStandardPackageJobResults");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam }, new RowMapper<JobResult>() {
				public JobResult mapRow(ResultSet rs, int i) throws SQLException {
					JobResult jobresult = new JobResult();
					jobresult.setPackageId(Integer.parseInt(packageId));
					/* jobresult.setUserId(userId); */
					jobresult.setBatchId(rs.getString("BATCH_ID"));
					jobresult.setJobName(rs.getString("JOB_NAME"));
					jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
					jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
					jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
					jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
					jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
					jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));

					jobresult.setErrorRowsCount(
							rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");
					return jobresult;
				}
			});
		} catch (DataAccessException ae) {
			LOG.error("error while getViewResults()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getViewResults()", e);
		}
		return resultList;
	}

	@Override
	public int saveDLTrailingMapping(String userId, String clientId, DLInfo dlInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("saveDLTrailingMapping");
			save = clientAppDbJdbcTemplate.update(sql, new Object[]{
					dlInfo.getdL_id(),
					dlInfo.getTrailingMonths(),
					dlInfo.getModification().getCreatedBy(),
					dlInfo.getModification().getCreatedTime()
			});
		} catch (DataAccessException ae) {
			LOG.error("error while saveDLTrailingMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveDLTrailingMapping()", e);
		}
		return save;
	}

	@Override
	public int updateDLTrailingMapping(String userId, String clientId, DLInfo dlInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateDLTrailingMapping");
			update = clientAppDbJdbcTemplate.update(sql,
					new Object[] {
							dlInfo.getTrailingMonths(),
							dlInfo.getdL_id()
							});
		} catch (DataAccessException ae) {
			LOG.error("error while updateDLTrailingMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateDLTrailingMapping()", e);
		}
		return update;
	}

	@Override
	public List<JobResult> getExecutionJobResultsByDate(String packageId, String dlId, String clientId, String fromDate,
			String toDate, JdbcTemplate clientJdbcTemplate) {

		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_" + packageId + "\\_" + dlId + "\\_%";
			String sql = sqlHelper.getSql("getStandardPackageJobResultsByDate");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam, fromDate, toDate },
					new RowMapper<JobResult>() {
						public JobResult mapRow(ResultSet rs, int i) throws SQLException {
							JobResult jobresult = new JobResult();
							jobresult.setPackageId(Integer.parseInt(packageId));
							/* jobresult.setUserId(userId); */
							jobresult.setBatchId(rs.getString("BATCH_ID"));
							jobresult.setJobName(rs.getString("JOB_NAME"));
							jobresult.setStartDate(rs.getString("JOB_START_DATETIME"));
							jobresult.setEndDate(rs.getString("JOB_END_DATETIME"));
							jobresult.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
							jobresult.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
							jobresult.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
							jobresult.setRunStatus(rs.getString("JOB_RUN_STATUS"));
							jobresult.setErrorRowsCount(
									rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");

							return jobresult;
						}

					});
		} catch (DataAccessException ae) {
			LOG.error("error while getViewResults()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getViewResults()", e);
		}
		return resultList;
	}

	@Override
	public List<PackageExecution> getPackageExecutionResultsByPagination(Integer dlId, int offset, int limit, JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<PackageExecution> packageDetails = null;
		try {
			String sql = sqlHelper.getSql("getPackageExecutionResultsByPagination");
			packageDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { dlId ,limit ,offset },
					new RowMapper<PackageExecution>() {
						@Override
						public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
							PackageExecution packageExecution = new PackageExecution();
							packageExecution.setExecutionId(rs.getInt("execution_id"));
							packageExecution.setPackageId(rs.getInt("package_id"));
							packageExecution.setInitiatedFrom(rs.getString("initiated_from"));
							packageExecution.setRunType(rs.getString("run_type"));
							packageExecution.setUploadStatus(rs.getString("upload_status"));
							packageExecution.setExecutionStatus(rs.getString("execution_status"));
							packageExecution.setDruidStatus(rs.getString("druid_status"));
							packageExecution.setExecutionStartDate(rs.getString("exection_start_date"));
							packageExecution.setLastExecutedDate(rs.getString("last_executed_date"));
							packageExecution.setUploadStartDate(rs.getString("upload_start_date"));
							packageExecution.setLastUploadedDate(rs.getString("last_uploaded_date")); 
							packageExecution.setDruidStartDate(rs.getString("druid_start_date"));
							packageExecution.setDruidEndDate(rs.getString("druid_end_date"));
							return packageExecution;
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getPackageExecutionResultsByPagination()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getPackageExecutionResultsByPagination()", e);
		}
		return packageDetails;

	
	}

	@Override
	public int getStandardPackageExecutionCount(int packageId, int dlId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		int count = 0;
		try {
			String sql = sqlHelper.getSql("getStandardPackageExecutionCount");
			
			count = clientAppDbJdbcTemplate.queryForObject(sql, new Object[] { dlId },
					new RowMapper<Integer>() {
						@Override
						public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
							if (rs == null)
								return null;
							return rs.getInt(1);
						}
					});
			
		} catch (DataAccessException ae) {
			LOG.error("error while getStandardPackageExecutionCount()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getStandardPackageExecutionCount()", e);
		}
		return count;
	}
	

}
