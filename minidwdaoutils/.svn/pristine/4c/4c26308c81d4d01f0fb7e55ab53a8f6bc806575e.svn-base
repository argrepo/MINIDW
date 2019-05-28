package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.DataValidationDao;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataValidation;
import com.datamodel.anvizent.service.model.DataValidationIntegration;
import com.datamodel.anvizent.service.model.DataValidationType;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Modification;


public class DataValidationDaoImpl implements DataValidationDao {

	protected final Logger logger = LoggerFactory.getLogger(DataValidationDaoImpl.class);
	
	private SqlHelper sqlHelper;
	
	public DataValidationDaoImpl() {
		try {
			this.sqlHelper = new SqlHelper(DataValidationDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating ETLAdminDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public List<DataValidation> getValidationInfo(Integer validationTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<DataValidation> validationInfo = new ArrayList<DataValidation>();
		try{
			String sql = sqlHelper.getSql("getValidationsById");
			System.out.println(sql);
			// select * from data_validation_script where validation_id = ?;/
			validationInfo = clientAppDbJdbcTemplate.query(sql, new Object[] {validationTypeId}, new RowMapper<DataValidation>(){
				public DataValidation mapRow(ResultSet rs, int i) throws SQLException {
					DataValidation dataValidation = new DataValidation();
					dataValidation.setScriptId(rs.getInt("script_id"));
					dataValidation.setValidationId(rs.getInt("validation_id"));
					dataValidation.setValidationName(rs.getString("script_name"));
					dataValidation.setPreparedStatement(rs.getBoolean("prepare_stmt"));
					dataValidation.setDatabaseConnectorId(rs.getInt("database_connector_id"));
					dataValidation.setDatabaseConnectorName(rs.getString("connector_name"));
					dataValidation.setActive(rs.getBoolean("active"));
					dataValidation.setDlNames(rs.getString("dl_names"));
					dataValidation.setDlIds(rs.getString("dl_ids"));
					dataValidation.setIlId(rs.getInt("il_id"));
					dataValidation.setIlname(rs.getString("IL_name"));
					dataValidation.setValidationTypeId(rs.getInt("validation_type_id"));
					dataValidation.setValidationTypeName(rs.getString("validationTypeName"));
					
					Modification modification = new Modification();
					modification.setCreatedBy(rs.getString("created_by"));
					modification.setCreatedTime(rs.getString("created_date"));
					modification.setModifiedBy(rs.getString("modified_by"));
					modification.setModifiedTime(rs.getString("modified_date"));
					
					dataValidation.setModification(modification);
					
					return dataValidation;
					
				}
			});
			
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return validationInfo;
	}

	@Override
	public Integer saveDataValidation(DataValidation dataValidation, JdbcTemplate clientAppDbJdbcTemplate) {
		Integer createdDataValidationId = 0;
		try{
			String sql = sqlHelper.getSql("insertDataValidation");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator(){
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps =con.prepareStatement(sql, new String[] {"script_id"});
					ps.setString(1, dataValidation.getValidationName());
                    ps.setString(2, dataValidation.getValidationScripts());
                    ps.setInt(3, dataValidation.getValidationId());
                    ps.setBoolean(4, dataValidation.isActive());
                    ps.setString(5,  dataValidation.getModification().getCreatedBy());
                    ps.setString(6, dataValidation.getModification().getCreatedTime());
                    ps.setString(7,  dataValidation.getModification().getModifiedTime());
                    ps.setString(8, dataValidation.getModification().getModifiedBy());
                    ps.setInt(9, dataValidation.getDatabaseConnectorId());
                    ps.setInt(10, dataValidation.getValidationTypeId());
                    ps.setBoolean(11, dataValidation.isPreparedStatement());
					return ps;
				}
			}, keyHolder);
			
			if(keyHolder !=null){
				Number autoInc = keyHolder.getKey();
				createdDataValidationId = autoInc.intValue();
			}
			
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return createdDataValidationId;
	}

	@Override
	public int saveScriptDlMapping(int scriptId, DataValidation dataValidation, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		List<DLInfo> dlInfoList =dataValidation.getDlInfoList();
		try{
			String sql = sqlHelper.getSql("saveScriptDlMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					DLInfo dlInfo = dlInfoList.get(i);
					ps.setInt(1, scriptId);
					ps.setInt(2, dlInfo.getdL_id());
				}
				@Override
				public int getBatchSize() {
					
					return dlInfoList.size();
				}
			});
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return count != null?count.length:0;
	}

	@Override
	public DataValidation getValidationScriptDataById(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		    DataValidation validation = new DataValidation();
		    List<DLInfo> dlInfoList = new ArrayList<DLInfo>();
		  try{
			  String sql = sqlHelper.getSql("getValidationScriptById");
			  validation = clientAppDbJdbcTemplate.query(sql,  new Object[] {scriptId}, new ResultSetExtractor<DataValidation>(){
				@Override
				public DataValidation extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						DataValidation dataValidation = new DataValidation();
						dataValidation.setScriptId(rs.getInt("script_id"));
						dataValidation.setValidationScripts(rs.getString("validation_script"));
						dataValidation.setValidationId(rs.getInt("validation_id"));
						dataValidation.setValidationName(rs.getString("script_name"));
						dataValidation.setPreparedStatement(rs.getBoolean("prepare_stmt"));
						dataValidation.setDatabaseConnectorId(rs.getInt("database_connector_id"));
						dataValidation.setActive(rs.getBoolean("active"));
						dataValidation.setValidationTypeId(rs.getInt("validation_type_id"));
						
						Modification modification = new Modification();
						modification.setCreatedBy(rs.getString("created_by"));
						modification.setCreatedTime(rs.getString("created_date"));
						modification.setModifiedBy(rs.getString("modified_by"));
						modification.setModifiedTime(rs.getString("modified_date"));
						dataValidation.setModification(modification);
						
						return dataValidation;
					}else{
						return null;
					}
				}
			  });
			  
			  String dlSql = sqlHelper.getSql("getDLInfoByValidationScriptId");
			  dlInfoList = clientAppDbJdbcTemplate.query(dlSql,  new Object[] {scriptId}, new RowMapper<DLInfo>(){
				  public DLInfo mapRow(ResultSet rs, int i)throws SQLException, DataAccessException{
					  DLInfo dlInfo = new DLInfo();
					  dlInfo.setdL_id(rs.getInt("dl_id"));
					return dlInfo;
				  }
			  });
			  validation.setDlInfoList(dlInfoList);
			  
			  String ilSql = sqlHelper.getSql("getIlInfoByValidationScriptId");
			  validation.setIlId(clientAppDbJdbcTemplate.query(ilSql, new Object[] {scriptId}, new ResultSetExtractor<Integer>(){
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						 int ilId = rs.getInt("il_id");
						 return ilId;
					}else{
						return null;
					}
				}
			  }));
			  
		   }catch (DataAccessException ae) {
				logger.error("error while getValidationScriptDataById()", ae);
				throw new AnvizentRuntimeException(ae);
		   }catch(SqlNotFoundException s){
			  s.printStackTrace();
		  }
		return validation;
	}
	
	@Override
	public List<String> getContextParamsByValidationId(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<String> contextParams =  new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("contextParamDataForDataValidations");
			contextParams = clientAppDbJdbcTemplate.query(sql, new Object[] {scriptId}, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int i) throws SQLException, DataAccessException{
					String paramId = String.valueOf(rs.getString("param_id"));
					return paramId;
				}
			});
			
		}catch (DataAccessException ae) {
			logger.error("error while getValidationScriptDataById()", ae);
			throw new AnvizentRuntimeException(ae);
	    }catch(SqlNotFoundException s){
		  s.printStackTrace();
	    }
		return contextParams;
	}

	@Override
	public List<DLInfo> getDLInfoByValidationScriptId(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		  List<DLInfo> dlInfoList = new ArrayList<DLInfo>();
		  try{
			  String sql = sqlHelper.getSql("getDLInfoByValidationScriptId");
			  dlInfoList = clientAppDbJdbcTemplate.query(sql,  new Object[] {scriptId}, new RowMapper<DLInfo>(){
				  public DLInfo mapRow(ResultSet rs, int i)throws SQLException, DataAccessException{
					  DLInfo dlInfo = new DLInfo();
					  dlInfo.setdL_id(rs.getInt("dl_id"));
					 // dlInfo.setdL_name(dL_name);
					return dlInfo;
				  }
			  });
		  }catch (DataAccessException ae) {
				logger.error("error while getValidationScriptDataById()", ae);
				throw new AnvizentRuntimeException(ae);
		   }catch(SqlNotFoundException s){
			  s.printStackTrace();
		  }
		return dlInfoList;
	}

	@Override
	public int updateDataValidationScript(DataValidation dataValidation, JdbcTemplate clientAppDbJdbcTemplate) {
		logger.info("in updateDataValidationScript()");
		int updateStatus = 0;
		int scriptId = dataValidation.getScriptId();
		try{
			String sql = sqlHelper.getSql("updateDataValidation");
			updateStatus = clientAppDbJdbcTemplate.update(sql, 
					new Object[] {dataValidation.getValidationName(),dataValidation.getValidationScripts(), dataValidation.isActive(),
							 dataValidation.getModification().getModifiedBy(), dataValidation.getModification().getModifiedTime(), dataValidation.getDatabaseConnectorId(), 
							 dataValidation.getValidationTypeId(),dataValidation.isPreparedStatement(),scriptId});
		}catch(DataAccessException ae){
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return updateStatus;
	}

	@Override
	public int  deleteScriptDlMapping(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = -1;
		try{
			String sql = sqlHelper.getSql("deleteScriptDlILMapping");
			count = clientAppDbJdbcTemplate.update(sql, new Object[] {scriptId});
		}catch(DataAccessException ae){
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return count;
	}

	@Override
	public DataValidationIntegration getDataValidationIntegration(JdbcTemplate commonJdbcTemplate) {
		DataValidationIntegration dataIntegration = new DataValidationIntegration();
		try{
			String sql = sqlHelper.getSql("getdvIntegration");
			dataIntegration = commonJdbcTemplate.query(sql, new ResultSetExtractor<DataValidationIntegration>() {

				@Override
				public DataValidationIntegration extractData(ResultSet rs) throws SQLException, DataAccessException {
					DataValidationIntegration integration = new DataValidationIntegration();
					if(rs != null && rs.next()){
						
						integration.setId(rs.getInt("id"));
						integration.setTimezone(rs.getString("time_zone"));
						integration.setJobName(rs.getString("Job_name"));
						integration.setJobFileNames(rs.getString("job_file_names"));
						integration.setClientSpecificJobName(rs.getString("client_specific_Job_name"));
						integration.setClientSpecificJobfileNames(rs.getString("client_specific_job_file_names"));
						integration.setStartDate(rs.getString("start_date"));
						integration.setEndDate(rs.getString("end_date"));
						
						return integration;
					}else{
						return null;
					}
				}
			});
		}catch (DataAccessException ae) {
			logger.error("error while getCurrencyIntegration()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while getCurrencyIntegration()", e);
			e.printStackTrace();
		}
		return dataIntegration;
	}

	@Override
	public List<JobResult> getJobExecutionResults(String clientId, Integer scriptId, JdbcTemplate clientJdbcTemplate) {
		List<JobResult> jobresults = new  ArrayList<JobResult>();
		try{
			String sql = sqlHelper.getSql("getdvJobResutls");
			String batchParam = "%" + clientId + "_" + "Data_Validation_Framework" + "_" + scriptId + "_%";
			jobresults = clientJdbcTemplate.query(sql, new Object[] {batchParam}, new RowMapper<JobResult>() {
				@Override
				public JobResult mapRow(ResultSet rs, int rowNum) throws SQLException {
					JobResult result = new JobResult();
					
					result.setScriptId(scriptId);
					result.setBatchId(rs.getString("BATCH_ID"));
					result.setJobName(rs.getString("JOB_NAME"));
					result.setStartDate(rs.getString("JOB_START_DATETIME"));
					result.setEndDate(rs.getString("JOB_END_DATETIME"));
					result.setInsertedRecords(rs.getInt("TGT_INSERT_COUNT"));
					result.setUpdatedRecords(rs.getInt("TGT_UPDATE_COUNT"));
					result.setTotalRecordsFromSource(rs.getInt("SRC_COUNT"));
					result.setRunStatus(rs.getString("JOB_RUN_STATUS"));
					result.setErrorRowsCount(rs.getString("ERROR_ROWS_COUNT") != null ? rs.getString("ERROR_ROWS_COUNT") : "0");
					
					return result;
				}
			});
		}catch(DataAccessException ae){
			logger.error("error while getCurrencyIntegration()", ae);
			throw new AnvizentRuntimeException(ae);
		}catch (SqlNotFoundException e) {
			logger.error("error while getCurrencyIntegration()", e);
			e.printStackTrace();
		}
		
		return jobresults;
	}

	@Override
	public List<JobResult> getValidationScriptErrorLogs(String batchId, JdbcTemplate clientJdbcTemplate) {
		List<JobResult> errorlogs = new  ArrayList<JobResult>();
		try{
		 String sql = sqlHelper.getSql("getdvErrorlogs");
		 errorlogs = clientJdbcTemplate.query(sql, new Object[] { batchId }, new RowMapper<JobResult>() {
			@Override
			public JobResult mapRow(ResultSet rs, int rowNum) throws SQLException {
				JobResult result = new JobResult();
				
				result.setErrorId(rs.getInt("ERROR_ID"));
				result.setBatchId(rs.getString("BATCH_ID"));
				result.setErrorCode(rs.getString("ERROR_CODE"));
				result.setErrorType(rs.getString("ERROR_TYPE"));
				result.setErrorMessage(rs.getString("ERROR_MSG"));
				result.setErrorSyntax(rs.getString("ERROR_SYNTAX"));
				result.setDataSetValue(rs.getString("DATA_VALUE_SET"));
				result.setStartDate(rs.getString("ADDED_DATETIME"));
				result.setEndDate(rs.getString("UPDATED_DATETIME"));			
				
				return result;
			}
		});
	}catch(DataAccessException ae){
		logger.error("error while getCurrencyIntegration()", ae);
		throw new AnvizentRuntimeException(ae);
	}catch (SqlNotFoundException e) {
		logger.error("error while getCurrencyIntegration()", e);
		e.printStackTrace();
	}
	
	return errorlogs;
	}

	@Override
	public int saveScriptIlMapping(int createValdationId, DataValidation dataValidation,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try{
			String sql = sqlHelper.getSql("saveScriptIlMapping");
			count = clientAppDbJdbcTemplate.update(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ILInfo ilInfo = dataValidation.getIlInfo();
					ps.setInt(1, createValdationId);
					ps.setInt(2, ilInfo.getiL_id());
				}
				
			});
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return count;
	}

	@Override
	public int deleteScriptilMapping(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = -1;
		try{
			String sql = sqlHelper.getSql("deleteScriptDlILMapping");
			count = clientAppDbJdbcTemplate.update(sql, new Object[] {scriptId});
		}catch(DataAccessException ae){
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return count;
	}

	@Override
	public List<DataValidation> getPreloadValidationInfoByConnectionId(Integer validationTypeId, Integer connectionId,JdbcTemplate clientAppDbJdbcTemplate) {
		    List<DataValidation> validationInfo = new ArrayList<DataValidation>();
			try{
				String sql = sqlHelper.getSql("getValidationsByConnectionId");
				validationInfo = clientAppDbJdbcTemplate.query(sql, new Object[] {connectionId, validationTypeId}, new RowMapper<DataValidation>(){
					
					public DataValidation mapRow(ResultSet rs, int i) throws SQLException {
						DataValidation dataValidation = new DataValidation();
						dataValidation.setScriptId(rs.getInt("script_id"));
						dataValidation.setValidationId(rs.getInt("validation_id"));
						dataValidation.setValidationName(rs.getString("script_name"));
						dataValidation.setPreparedStatement(rs.getBoolean("prepare_stmt"));
						dataValidation.setDatabaseConnectorId(rs.getInt("database_connector_id"));
						dataValidation.setDatabaseConnectorName(rs.getString("name"));
						dataValidation.setActive(rs.getBoolean("active"));
						dataValidation.setIlId(rs.getInt("il_id"));
						dataValidation.setIlname(rs.getString("IL_name"));
						dataValidation.setValidationTypeId(rs.getInt("validation_type_id"));
						
						DataValidationType dataValidationType = new DataValidationType();
						
						dataValidationType.setJobName(rs.getString("dvtjobname"));
						dataValidationType.setDependencyJars(rs.getString("dvtdependencyjars"));
						dataValidation.setDataValidationType(dataValidationType);
						
						ILConnection connection = new ILConnection();
						connection.setConnectionId(rs.getInt("connection_id"));
						connection.setConnectionName(rs.getString("connection_name"));
						connection.setAvailableInCloud(rs.getBoolean("available_in_cloud"));
						
						Database database = new Database();
						database.setUrlFormat(rs.getString("url_format"));
						database.setId(rs.getInt("id"));
						database.setName(rs.getString("name"));
						
						connection.setDatabase(database);
						
						dataValidation.setIlConnection(connection);
						
						Modification modification = new Modification();
						modification.setCreatedBy(rs.getString("created_by"));
						modification.setCreatedTime(rs.getString("created_date"));
						modification.setModifiedBy(rs.getString("modified_by"));
						modification.setModifiedTime(rs.getString("modified_date"));
						
						dataValidation.setModification(modification);
						
						return dataValidation;
						
					}
				});
			}catch (DataAccessException ae) {
				throw new AnvizentRuntimeException(ae);
			}catch(SqlNotFoundException s){
				s.printStackTrace();
			}
		return validationInfo;
	}

	@Override
	public DataValidation getValidationScriptByScriptId(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		DataValidation validation = new DataValidation();
		try{
			String sql = sqlHelper.getSql("getValidationScriptById");
			  validation = clientAppDbJdbcTemplate.query(sql,  new Object[] {scriptId}, new ResultSetExtractor<DataValidation>(){
				@Override
				public DataValidation extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						DataValidation dataValidation = new DataValidation();
						dataValidation.setScriptId(rs.getInt("script_id"));
						dataValidation.setValidationScripts(rs.getString("validation_script"));
						dataValidation.setValidationId(rs.getInt("validation_id"));
						dataValidation.setValidationName(rs.getString("script_name"));
						dataValidation.setPreparedStatement(rs.getBoolean("prepare_stmt"));
						dataValidation.setDatabaseConnectorId(rs.getInt("database_connector_id"));
						dataValidation.setActive(rs.getBoolean("active"));
						dataValidation.setValidationTypeId(rs.getInt("validation_type_id"));
						
						DataValidationType dvt = new DataValidationType();
						dvt.setJobName(rs.getString("job_name"));
						dvt.setDependencyJars(rs.getString("dependency_jars"));
						dataValidation.setDataValidationType(dvt);
						
						Modification modification = new Modification();
						modification.setCreatedBy(rs.getString("created_by"));
						modification.setCreatedTime(rs.getString("created_date"));
						modification.setModifiedBy(rs.getString("modified_by"));
						modification.setModifiedTime(rs.getString("modified_date"));
						
						dataValidation.setModification(modification);
						return dataValidation;
					}else{
						return null;
					}
				}
			  });
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return validation;
	}

	@Override
	public List<DataValidation> getValidationScriptByScriptId(String scriptIds,Integer connectionId,JdbcTemplate clientAppDbJdbcTemplate) {
		List<DataValidation> validationInfo = new ArrayList<>();
		try{
			
			String sql = sqlHelper.getSql("validationScriptInfoByIds");
			validationInfo = clientAppDbJdbcTemplate.query(sql, new Object[] {connectionId,scriptIds}, new RowMapper<DataValidation>(){
				public DataValidation mapRow(ResultSet rs, int i) throws SQLException {
					DataValidation dataValidation = new DataValidation();
					dataValidation.setScriptId(rs.getInt("script_id"));
					dataValidation.setValidationId(rs.getInt("validation_id"));
					dataValidation.setValidationScripts(rs.getString("validation_script"));
					dataValidation.setValidationName(rs.getString("script_name"));
					dataValidation.setPreparedStatement(rs.getBoolean("prepare_stmt"));
					dataValidation.setDatabaseConnectorId(rs.getInt("database_connector_id"));
					dataValidation.setDatabaseConnectorName(rs.getString("connector_name"));
					dataValidation.setActive(rs.getBoolean("active"));
					dataValidation.setIlId(rs.getInt("il_id"));
					dataValidation.setIlname(rs.getString("IL_name"));
					dataValidation.setValidationTypeId(rs.getInt("validation_type_id"));
					
					DataValidationType dataValidationType = new DataValidationType();
					dataValidationType.setValidationTypeId(rs.getInt("validation_type_id"));
					dataValidationType.setJobName(rs.getString("dvjobname"));
					dataValidationType.setDependencyJars(rs.getString("dvdependencyjars"));
					dataValidation.setDataValidationType(dataValidationType);
					
					ILConnection connection = new ILConnection();
					connection.setConnectionId(rs.getInt("connection_id"));
					connection.setConnectionName(rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
					connection.setConnectionType(rs.getString("connection_type") != null ? rs.getString("connection_type") : "");
					connection.setAvailableInCloud(rs.getBoolean("available_in_cloud"));
					
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
					
					dataValidation.setIlConnection(connection);
					
					Modification modification = new Modification();
					modification.setCreatedBy(rs.getString("created_by"));
					modification.setCreatedTime(rs.getString("created_date"));
					modification.setModifiedBy(rs.getString("modified_by"));
					modification.setModifiedTime(rs.getString("modified_date"));
					
					dataValidation.setModification(modification);
					
					return dataValidation;
					
				}
			});
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return validationInfo;
	}

	@Override
	public int saveDataValidationContextParams(DataValidationType dataValidation, JdbcTemplate clientAppDbJdbcTemplate) {

		int[] count = null;
		try {
			String sql = sqlHelper.getSql("saveDataValidationContextParameters");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					int paramId = Integer.valueOf(dataValidation.getContextParamList().get(i));
					ps.setInt(1, paramId);
					ps.setInt(2, dataValidation.getValidationTypeId());
					ps.setString(3, dataValidation.getModification().getCreatedBy());
					ps.setString(4, dataValidation.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return dataValidation.getContextParamList().size();
				}
			});
		} catch (DataAccessException ae) {
			logger.error("error while saveDataValidationContextParams", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while saveDataValidationContextParams", e);
			
		}

		return count != null?count.length:0;
	
	}

	@Override
	public int deleteValidationContextParams(int dataValidationTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		int delete = 0;
		try {
			String sql = sqlHelper.getSql("deleteDataValidationContextParams");
			delete = clientAppDbJdbcTemplate.update(sql, new Object[] { dataValidationTypeId });
		} catch (DataAccessException ae) {
			logger.error("error while deleteValidationContextParams", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while deleteValidationContextParams", e);
			
		}
		return delete;
	}

	@Override
	public Map<String, String> getDataValidationContextParams(Integer scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		Map<String, String> contextParams = null;
		try {
			String sql = sqlHelper.getSql("getDataValidationContextParams");
			contextParams = clientAppDbJdbcTemplate.query(sql, new Object[] { clientAppDbJdbcTemplate },
					new ResultSetExtractor<Map<String, String>>() {
						@Override
						public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs == null)
								return null;
							Map<String, String> contextParams = new LinkedHashMap<>();
							while (rs.next()) {
								contextParams.put(rs.getString(1), rs.getString(2));
							}
							return contextParams;
						}
					});
		} catch (Exception e) {
			logger.error("Error while reading il context params ", e);
		}
		return contextParams;
	}
/*
	@Override
	public List<DataValidationType> getAllValidationTypes(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<DataValidationType> dataValidationTypeList = new ArrayList<DataValidationType>();
		  try{
			  String sql = sqlHelper.getSql("getDataValidationType");
			  dataValidationTypeList = clientAppDbJdbcTemplate.query(sql, new RowMapper<DataValidationType>(){
				  public DataValidationType mapRow(ResultSet rs, int i)throws SQLException, DataAccessException{
					  DataValidationType dataValidationType = new DataValidationType();
					  dataValidationType.setValidationTypeId(rs.getInt("validation_type_id"));
					  dataValidationType.setValidationTypeName(rs.getString("validation_type_name"));
					return dataValidationType;
				  }
			  });
		  }catch (DataAccessException ae) {
				logger.error("error while getDataValidationType", ae);
				throw new AnvizentRuntimeException(ae);
		   }catch(SqlNotFoundException s){
			  s.printStackTrace();
		  }
		return dataValidationTypeList;
	}
*/
	@Override
	public DataValidationType getValidationTypeNameById(Integer validationTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		DataValidationType validation = new DataValidationType();
		try{
			String sql = sqlHelper.getSql("getDataValidationTypeNameById");
			  validation = clientAppDbJdbcTemplate.query(sql,  new Object[] {validationTypeId}, new ResultSetExtractor<DataValidationType>(){
				@Override
				public DataValidationType extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						DataValidationType dataValidationType = new DataValidationType();
							dataValidationType.setValidationTypeId(rs.getInt("validation_type_id"));
							dataValidationType.setValidationTypeName(rs.getString("validation_type_name"));
							dataValidationType.setValidationTypeDesc(rs.getString("validation_type_desc"));
							dataValidationType.setActive(rs.getBoolean("isActive"));
							dataValidationType.setValidationName(rs.getString("validationname"));
							dataValidationType.setValidationId(rs.getInt("validation_id"));
							dataValidationType.setDependencyJars(rs.getString("dependency_jars"));
							dataValidationType.setJobName(rs.getString("job_name"));
							
							String contextParamByIdSql = sqlHelper.getSql("getDataValidationTypeContextParamsById");
							List<String> contextParamList = clientAppDbJdbcTemplate.query(contextParamByIdSql, new Object[] { validationTypeId },
									new RowMapper<String>() {
										public String mapRow(ResultSet rs, int i) throws SQLException {
											String contextParam = rs.getString("param_id");
											return contextParam;
										}
									});
						dataValidationType.setContextParamList(contextParamList);
						return dataValidationType;
					}else{
						return null;
					}
				}
			  });
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return validation;
	}

	@Override
	public DataValidationType getDataValidationTypeJobsandDependencyJars(Integer validationTypeId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		DataValidationType dataValidationType = new DataValidationType();
		try{
			String sql = sqlHelper.getSql("getDataValidationTypeJobsandDependencyJars");
			  dataValidationType = clientAppDbJdbcTemplate.query(sql,  new Object[] {validationTypeId}, new ResultSetExtractor<DataValidationType>(){
				@Override
				public DataValidationType extractData(ResultSet rs) throws SQLException, DataAccessException {
					if(rs.next()){
						DataValidationType dvType = new DataValidationType();
						dvType.setValidationTypeId(rs.getInt("validation_type_id"));
							dvType.setJobName(rs.getString("job_name"));
							dvType.setDependencyJars(rs.getString("dependency_jars"));
							
							/*String contextParamByIdSql = sqlHelper.getSql("getDataValidationTypeContextParamsById");
							List<String> contextParamList = clientAppDbJdbcTemplate.query(contextParamByIdSql, new Object[] { validationTypeId },
									new RowMapper<String>() {
										public String mapRow(ResultSet rs, int i) throws SQLException {
											String contextParam = rs.getString("param_id");
											return contextParam;
										}
									});
						dvType.setContextParamList(contextParamList);*/
							
						return dvType;
					}else{
						return null;
					}
				}
			  });
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return dataValidationType;
	}

	@Override
	public List<DataValidationType> getAllDataValidationTypes(String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<DataValidationType> dataValidationTypeList = null;
		  try {
			  String sql = sqlHelper.getSql("getAllDataValidationTypes");
			  dataValidationTypeList = clientAppDbJdbcTemplate.query(sql,
					  new RowMapper<DataValidationType>() {
				@Override
				public DataValidationType mapRow(ResultSet rs, int rowNum) throws SQLException {
					DataValidationType dataValidationType = new DataValidationType();
					dataValidationType.setValidationTypeId(rs.getInt("validation_type_id"));
					dataValidationType.setValidationTypeName(rs.getString("validation_type_name"));
					dataValidationType.setValidationTypeDesc(rs.getString("validation_type_desc"));
					dataValidationType.setActive(rs.getBoolean("isActive"));
					dataValidationType.setValidationName(rs.getString("validationname"));
					return dataValidationType;
				}
			  });
		  }catch(SqlNotFoundException e) {
			  logger.error("error while getAllDataValidationTypes()", e);
		  }
		return dataValidationTypeList;
	}

	@Override
	public int saveDataValidationTypes(DataValidationType dataValidationType,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int createdValidationTypeJobId = 0;
		try{
			String sql = sqlHelper.getSql("insertDataValidationType");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator(){
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps =con.prepareStatement(sql, new String[] {"validation_type_id"});
                    ps.setString(1, dataValidationType.getValidationTypeName());
                    ps.setString(2, dataValidationType.getValidationTypeDesc());
                    ps.setBoolean(3, dataValidationType.isActive());
                    ps.setInt(4, dataValidationType.getValidationId());
                    ps.setString(5, dataValidationType.getDependencyJars());
                    ps.setString(6, dataValidationType.getJobName());
					return ps;
				}
			}, keyHolder);
			
			if(keyHolder !=null){
				Number autoInc = keyHolder.getKey();
				createdValidationTypeJobId = autoInc.intValue();
			}
			
		}catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		}catch(SqlNotFoundException s){
			s.printStackTrace();
		}
		return createdValidationTypeJobId;
	}

	@Override
	public int updateDataValidationTypes(DataValidationType dataValidationType, JdbcTemplate clientAppDbJdbcTemplate) {
		int updateStatus = 0;
		Integer validationTypeid = dataValidationType.getValidationTypeId();

		try {
			String sql = sqlHelper.getSql("updateDataValidationType");

			updateStatus = clientAppDbJdbcTemplate.update(sql, new Object[] { dataValidationType.getValidationTypeName(),
					dataValidationType.getValidationTypeDesc(), dataValidationType.isActive(),dataValidationType.getValidationId(),dataValidationType.getDependencyJars(),dataValidationType.getJobName(), validationTypeid });

		} catch (DataAccessException ae) {
			logger.error("error while updateDataValidationType()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while updateDataValidationType()", e);
		}
		return updateStatus;
	}

	@Override
	public List<Map<String, String>> getDataValidation(JdbcTemplate clientAppDbJdbcTemplate) {
		List<Map<String, String>> contextParams = null;
		try {
			String sql = sqlHelper.getSql("getDataValidation");
			contextParams = clientAppDbJdbcTemplate.query(sql,
					new ResultSetExtractor <List<Map<String, String>>>() {
						
				public List<Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs == null)
						return null;
					List<Map<String, String>> dataValidationList = new ArrayList<>();
					Map<String, String> dataValidationMap = null;
					while (rs.next()) {
						dataValidationMap = new LinkedHashMap<>();
						dataValidationMap.put(String.valueOf(rs.getLong("id")), rs.getString("type_name"));
						dataValidationList.add(dataValidationMap);
					}
					return dataValidationList;
				}
			});

		} catch (Exception e) {
			logger.error("Error while reading DataValidationTypes ", e);
		}
		return contextParams;
	}

	@Override
	public List<DataValidationType> getDataValidationTypesByvalidationId(String clientId, Integer validationId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		List<DataValidationType> dataValidationTypeList = null;
		  try {
			  String sql = sqlHelper.getSql("getDataValidationType");
			  dataValidationTypeList = clientAppDbJdbcTemplate.query(sql,new Object[] {validationId},
					  new RowMapper<DataValidationType>() {
				@Override
				public DataValidationType mapRow(ResultSet rs, int rowNum) throws SQLException {
					DataValidationType dataValidationType = new DataValidationType();
					dataValidationType.setValidationTypeId(rs.getInt("validation_type_id"));
					dataValidationType.setValidationTypeName(rs.getString("validation_type_name"));
					return dataValidationType;
				}
			  });
		  }catch(SqlNotFoundException e) {
			  logger.error("error while getAllDataValidationTypes()", e);
		  }
		return dataValidationTypeList;	
		}
	
	@Override
	public List<Map<String, String>> getContextParamNameAndValueById(int validationTypeId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		List<Map<String, String>> contextParameterList = null;
		
		try {
			String sql = sqlHelper.getSql("getContextParamNameAndValueById");
			contextParameterList = clientAppDbJdbcTemplate.query(sql,new Object[]{validationTypeId},
					new ResultSetExtractor <List<Map<String, String>>>() {
						
				public List<Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs == null)
						return null;
					List<Map<String, String>> contextParamList = new ArrayList<>();
					Map<String, String> contextParamMap = null;
					while (rs.next()) {
						contextParamMap = new LinkedHashMap<>();
						contextParamMap.put(String.valueOf(rs.getString("param_name")), rs.getString("paramval"));
						contextParamList.add(contextParamMap);
					}
					return contextParamList;
				}
			});

		} catch (DataAccessException ae) {
			logger.error("error while getContextParamValue()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			logger.error("error while getContextParamValue()", e);
			
		}
		return contextParameterList;
	}

	
}
