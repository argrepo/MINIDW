/**
 * 
 */
package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateStatusUpdationException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.dao.ETLAdminDao;
import com.datamodel.anvizent.service.model.AllMappingInfo;
import com.datamodel.anvizent.service.model.AppDBVersionTableScripts;
import com.datamodel.anvizent.service.model.ClientConfigurationSettings;
import com.datamodel.anvizent.service.model.ClientCurrencyMapping;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.ClientSpecificILDLJars;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.CommonJob;
import com.datamodel.anvizent.service.model.ContextParameter;
import com.datamodel.anvizent.service.model.CurrencyIntegration;
import com.datamodel.anvizent.service.model.CurrencyList;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.DefaultTemplates;
import com.datamodel.anvizent.service.model.DlKpiMapping;
import com.datamodel.anvizent.service.model.ERP;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.ETLJobContextParam;
import com.datamodel.anvizent.service.model.ErrorLog;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.GeneralSettings;
import com.datamodel.anvizent.service.model.HybridClientsGrouping;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.IlDlMapping;
import com.datamodel.anvizent.service.model.IlPrebuildQueriesMapping;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.Internalization;
import com.datamodel.anvizent.service.model.Kpi;
import com.datamodel.anvizent.service.model.MiddleLevelManager;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.MultiClientInsertScriptsExecution;
import com.datamodel.anvizent.service.model.OAuth2;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.ServerConfigurations;
import com.datamodel.anvizent.service.model.TableScripts;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.VersionUpgrade;
import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceAuthenticationTypes;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.service.model.WebServiceILMapping;
import com.datamodel.anvizent.service.model.WebServiceTemplateMaster;
import com.datamodel.anvizent.service.model.WsTemplateAuthRequestParams;

/**
 * @author rakesh.gajula
 *
 */
public class ETLAdminDaoImpl extends JdbcDaoSupport implements ETLAdminDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(ETLAdminDaoImpl.class);

	private SqlHelper sqlHelper;

	public ETLAdminDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
		try {
			this.sqlHelper = new SqlHelper(ETLAdminDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating ETLAdminDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public int getExistContextParameter(String parameterName , JdbcTemplate clientAppDbJdbcTemplate) {
		int paramNameCount = 0;
		try {
			String sql = sqlHelper.getSql("existContextParameter");
			paramNameCount = clientAppDbJdbcTemplate.queryForObject(sql, new Object[] { parameterName }, new RowMapper<Integer>() {
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs == null)
						return null;
					return rs.getInt("paramcount");
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getExistContextParameter()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getExistContextParameter()", e);
			
		}
		return paramNameCount;
	}

	@Override
	public int createContextParameter(ETLAdmin eTLAdmin, String parameterName, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String sqlQuery = sqlHelper.getSql("createContextParameter");
			count = clientAppDbJdbcTemplate.update(sqlQuery,
					new Object[] { parameterName, eTLAdmin.getModification().getCreatedBy(), eTLAdmin.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while createContextParameter()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createContextParameter()", e);
			
		}
		return count;
	}

	@Override
	public List<ContextParameter> getContextParameters(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ContextParameter> contextParameterList = null;
		try {
			String sql = sqlHelper.getSql("getContextParameters");
			contextParameterList = clientAppDbJdbcTemplate.query(sql, new RowMapper<ContextParameter>() {
				@Override
				public ContextParameter mapRow(ResultSet rs, int rowNum) throws SQLException {
					ContextParameter contextParameter = new ContextParameter();
					contextParameter.setParamId(rs.getInt("param_id"));
					contextParameter.setParamName(rs.getString("param_name"));
					contextParameter.setParamval(rs.getString("paramval"));
					contextParameter.setDescription(rs.getString("description"));
					contextParameter.setIsActive(rs.getBoolean("IsActive"));
					return contextParameter;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getContextParameters()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getContextParameters()", e);
			
		}
		return contextParameterList;
	}

	@Override
	public ContextParameter getContextParametersById(int paramId, JdbcTemplate clientAppDbJdbcTemplate) {
		ContextParameter contextParameterList = null;
		try {
			String sql = sqlHelper.getSql("getContextParametersById");
			contextParameterList = clientAppDbJdbcTemplate.query(sql, new Object[] { paramId }, new ResultSetExtractor<ContextParameter>() {

				@Override
				public ContextParameter extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						ContextParameter contextParameter = new ContextParameter();
						contextParameter.setParamId(rs.getInt("param_id"));
						contextParameter.setParamName(rs.getString("param_name"));
						contextParameter.setParamval(rs.getString("paramval"));
						contextParameter.setDescription(rs.getString("description"));
						contextParameter.setIsActive(rs.getBoolean("IsActive"));
						return contextParameter;
					} else {
						return null;
					}

				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getContextParameters()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getContextParameters()", e);
			
		}
		return contextParameterList;
	}

	@Override
	public List<Map<String, Object>> getContextParamValue(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<Map<String, Object>> contextParameterList = null;
		try {
			String sql = sqlHelper.getSql("getContextParamValue");
			contextParameterList = clientAppDbJdbcTemplate.queryForList(sql);

		} catch (DataAccessException ae) {
			LOGGER.error("error while getContextParamValue()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getContextParamValue()", e);
			
		}
		return contextParameterList;
	}

	@Override
	public int saveEtlDlIlMapping(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ILInfo> iLInfoList = eTLAdmin.getiLInfo();
		int[] count = null;
		try {
			String saveIlsSql = sqlHelper.getSql("saveEtlDlIlMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(saveIlsSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ILInfo iLInfo = iLInfoList.get(i);
					ps.setInt(1, eTLAdmin.getDlId());
					ps.setInt(2, iLInfo.getiL_id());
					ps.setString(3, iLInfo.getiL_name());
					ps.setString(4, eTLAdmin.getModification().getCreatedBy());
					ps.setString(5, eTLAdmin.getModification().getCreatedTime());

				}

				@Override
				public int getBatchSize() {
					return iLInfoList.size();
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while saveEtlDlIlMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveEtlDlIlMapping", e);
			
		}
		return count != null?count.length:0;
	}

	@Override
	public int saveIlInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		int ilId = 0;
		try {
			final String sql = sqlHelper.getSql("createIl");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "IL_id" });
					ps.setString(1, eTLAdmin.getIlInfo().getiL_name());
					ps.setString(2, eTLAdmin.getIlInfo().getiLType());
					ps.setString(3, eTLAdmin.getIlInfo().getiL_table_name());
					ps.setString(4, eTLAdmin.getIlInfo().getXref_il_table_name());
					ps.setString(5, eTLAdmin.getIlInfo().getDescription());
					ps.setString(6, eTLAdmin.getIlInfo().getJobName());
					ps.setString(7, eTLAdmin.getIlInfo().getJobFileNames());
					ps.setString(8, eTLAdmin.getIlInfo().getPurgeScript());
					ps.setBoolean(9, eTLAdmin.getIlInfo().getIsActive());
					ps.setString(10, eTLAdmin.getModification().getCreatedBy());
					ps.setString(11, eTLAdmin.getModification().getCreatedTime());
					ps.setString(12, eTLAdmin.getIlInfo().getVersion());
					ps.setString(13, eTLAdmin.getIlInfo().getJobExecutionType());
					ps.setLong(14, eTLAdmin.getIlInfo().getJobTagId());
					ps.setLong(15, eTLAdmin.getIlInfo().getLoadParameterId());
					ps.setLong(16, eTLAdmin.getIlInfo().getMasterParameterId());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				ilId = autoIncrement.intValue();
			}
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while saveIlInfo()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveIlInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveIlInfo", e);
			
		}
		return ilId;
	}

	@Override
	public int saveDlInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		int dlId = 0;
		try {
			final String sql = sqlHelper.getSql("createDl");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "DL_id" });
					ps.setString(1, eTLAdmin.getDlInfo().getdL_name());
					ps.setString(2, eTLAdmin.getDlInfo().getdL_table_name());
					ps.setString(3, eTLAdmin.getDlInfo().getDescription());
					ps.setString(4, eTLAdmin.getDlInfo().getJobName());
					ps.setString(5, eTLAdmin.getModification().getCreatedBy());
					ps.setString(6, eTLAdmin.getModification().getCreatedTime());
					ps.setBoolean(7, eTLAdmin.getDlInfo().isIs_Active());
					ps.setInt(8, eTLAdmin.getDlInfo().getIndustry().getId());
					ps.setString(9, eTLAdmin.getDlInfo().getJobFileNames());
					ps.setString(10, eTLAdmin.getDlInfo().getVersion());
					ps.setString(11, eTLAdmin.getDlInfo().getJobExecutionType());
					ps.setLong(12, eTLAdmin.getDlInfo().getJobTagId());
					ps.setLong(13, eTLAdmin.getDlInfo().getLoadParameterId());
					ps.setLong(14, eTLAdmin.getDlInfo().getMasterParameterId());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				dlId = autoIncrement.intValue();
			}
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveDlInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveDlInfo", e);
			
		}
		return dlId;
	}

	@Override
	public int saveEtlDlContextParams(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] savedlContextParametersCount = null;
		List<ETLJobContextParam> idlParamsList = eTLAdmin.geteTLJobContextParamList();
		try {
			String savedlContextParametersSql = sqlHelper.getSql("saveEtldlContextParameters");
			savedlContextParametersCount = clientAppDbJdbcTemplate.batchUpdate(savedlContextParametersSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ETLJobContextParam dlParams = idlParamsList.get(i);
					ps.setInt(1, dlParams.getParamId());
					ps.setInt(2, eTLAdmin.getDlInfo().getdL_id());
					ps.setString(3, dlParams.getParamValue());
					ps.setString(4, eTLAdmin.getModification().getCreatedBy());
					ps.setString(5, eTLAdmin.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return idlParamsList.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveEtlDlContextParams", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveEtlDlContextParams", e);
			
		}

		return savedlContextParametersCount != null ? savedlContextParametersCount.length : 0;

	}

	@Override
	public int saveEtlDlJobsmapping(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {

		int[] saveEtlDlJobsMappingCount = null;
		List<String> fileNamesList = eTLAdmin.getFileNames();
		try {
			String saveEtlDlJobsMappingSql = sqlHelper.getSql("saveEtDIlJobsMapping");
			saveEtlDlJobsMappingCount = clientAppDbJdbcTemplate.batchUpdate(saveEtlDlJobsMappingSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					String fileNames = fileNamesList.get(i);
					int index = fileNames.lastIndexOf("\\");
					String fileName = fileNames.substring(index + 1);
					ps.setInt(1, eTLAdmin.getDlInfo().getdL_id());
					ps.setString(2, fileName);
					ps.setString(3, eTLAdmin.getModification().getCreatedBy());
					ps.setString(4, eTLAdmin.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return fileNamesList.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveEtlDlJobsmapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveEtlDlJobsmapping", e);
			
		}

		return saveEtlDlJobsMappingCount != null ? saveEtlDlJobsMappingCount.length : 0;

	}

	@Override
	public int saveDlClientidMapping(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		List<DLInfo> dLInfoList = eTLAdmin.getdLInfo();
		int[] count = null;
		try {
			int clientId = Integer.parseInt(eTLAdmin.getClientId());
			String saveClientidMappingSql = sqlHelper.getSql("saveDlClientidMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(saveClientidMappingSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					DLInfo dLInfo = dLInfoList.get(i);
					ps.setInt(1, clientId);
					ps.setInt(2, dLInfo.getdL_id());
					ps.setString(3, eTLAdmin.getModification().getCreatedBy());
					ps.setString(4, eTLAdmin.getModification().getCreatedTime());
					ps.setBoolean(5, dLInfo.getIsLocked());
				}

				@Override
				public int getBatchSize() {
					return dLInfoList.size();
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while saveDlClientidMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveDlClientidMapping", e);
			
		}

		return count != null?count.length:0;

	}

	@Override
	public List<DLInfo> getDlClientidMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<DLInfo> dlList = null;
		try {
			String sql = sqlHelper.getSql("getDlClientidMapping");
			dlList = clientAppDbJdbcTemplate.query(sql, new Object[] { clientId, clientId }, new RowMapper<DLInfo>() {
				public DLInfo mapRow(ResultSet rs, int i) throws SQLException {
					DLInfo dLInfo = new DLInfo();
					dLInfo.setdL_id(rs.getInt("DL_id"));
					dLInfo.setdL_name(rs.getString("DL_name"));
					dLInfo.setdL_table_name(rs.getString("dl_table_name"));
					dLInfo.setIsLocked(rs.getBoolean("isLocked"));
					return dLInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getDlClientidMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDlClientidMapping", e);
			
		}
		return dlList;
	}

	@Override
	public List<DLInfo> getVerticalMappedDLsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<DLInfo> dlList = null;
		try {
			String sql = sqlHelper.getSql("getVerticalMappedDLsByClientId");
			dlList = clientAppDbJdbcTemplate.query(sql, new RowMapper<DLInfo>() {
				public DLInfo mapRow(ResultSet rs, int i) throws SQLException {
					DLInfo dLInfo = new DLInfo();
					dLInfo.setdL_id(rs.getInt("DL_id"));
					dLInfo.setdL_name(rs.getString("DL_name"));
					return dLInfo;
				}
			}, clientId);

		} catch (DataAccessException ae) {
			LOGGER.error("error while getVerticalMappedDLsByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getVerticalMappedDLsByClientId", e);
			
		}
		return dlList;
	}

	@Override
	public ETLAdmin getClientSourceDetails(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		ETLAdmin etlAdmin = null;
		try {
			String sql = sqlHelper.getSql("getClientSourceDetails");
			etlAdmin = clientAppDbJdbcTemplate.query(sql, new Object[] { clientId }, new ResultSetExtractor<ETLAdmin>() {
				@Override
				public ETLAdmin extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						ETLAdmin etlAdmin = new ETLAdmin();
						etlAdmin.setIs_flat_file_locked(rs.getBoolean("is_flat_file_locked"));
						etlAdmin.setIs_database_locked(rs.getBoolean("is_database_locked"));
						return etlAdmin;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDlClientidMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDlClientidMapping", e);
			
		}

		return etlAdmin;
	}

	@Override
	public void deleteDlClientidMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		try {
			String dlClientidMappingSql = sqlHelper.getSql("deleteDlClientidMapping");
			clientAppDbJdbcTemplate.update(dlClientidMappingSql, new Object[] { clientId });

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteDlClientidMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteDlClientidMapping", e);
			
		}

	}

	@Override
	public void deleteEtlDlIlMappingByDlId(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		try {
			String deleteDlsSql = sqlHelper.getSql("deleteEtlDlIlMappingByDlId");
			clientAppDbJdbcTemplate.update(deleteDlsSql, new Object[] { eTLAdmin.getDlId() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteEtlDlIlMappingByDlId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteEtlDlIlMappingByDlId", e);
			
		}

	}

	@Override
	public List<DLInfo> getDLInfoById(int dLID, JdbcTemplate clientAppDbJdbcTemplate) {
		List<DLInfo> dlData = null;
		try {
			String sql = sqlHelper.getSql("getDLInfoByDLId");
			dlData = clientAppDbJdbcTemplate.query(sql, new Object[] { dLID }, new RowMapper<DLInfo>() {
				public DLInfo mapRow(ResultSet rs, int i) throws SQLException {
					DLInfo dLInfo = new DLInfo();
					Industry industry = new Industry();
					industry.setId(rs.getInt("industry_id"));
					dLInfo.setIndustry(industry);
					dLInfo.setdL_name(rs.getString("DL_name"));
					dLInfo.setdL_table_name(rs.getString("dl_table_name"));
					dLInfo.setDescription(rs.getString("description"));
					dLInfo.setJobName(rs.getString("Job_name"));
					dLInfo.setIs_Active(rs.getBoolean("isActive"));
					dLInfo.setJobFileNames(rs.getString("dependency_jars"));
					dLInfo.setVersion(rs.getString("version"));
					dLInfo.setJobExecutionType(rs.getString("job_execution_type"));
					dLInfo.setJobTagId(rs.getLong("elt_job_tag"));
					dLInfo.setLoadParameterId(rs.getLong("elt_load_parameter"));
					dLInfo.setMasterParameterId(rs.getLong("elt_master_id"));
					return dLInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getDLData", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getDLData", e);
			
		}

		return dlData;
	}

	@Override
	public List<String> getFilesByDLId(int dLID, JdbcTemplate clientAppDbJdbcTemplate) {
		List<String> filesList = null;
		try {
			String sql = "SELECT * FROM admin_dl_jobs_mapping where dl_id =" + dLID;
			filesList = clientAppDbJdbcTemplate.query(sql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("file_name");
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getFilesByILId", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return filesList;
	}

	@Override
	public List<ETLJobContextParam> getParamsByDLId(int dLID, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ETLJobContextParam> paramData = null;
		try {
			String sql = sqlHelper.getSql("getParamInfoByDLId");

			paramData = clientAppDbJdbcTemplate.query(sql, new Object[] { dLID }, new RowMapper<ETLJobContextParam>() {
				public ETLJobContextParam mapRow(ResultSet rs, int i) throws SQLException {
					ETLJobContextParam paramInfo = new ETLJobContextParam();
					paramInfo.setParamId(rs.getInt("param_id"));
					paramInfo.setParamValue(rs.getString("param_value"));
					return paramInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getParamsByILId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getParamsByILId", e);
			
		}

		return paramData;
	}

	@Override
	public int updateDLInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		int updateStatus = 0;
		int dLId = eTLAdmin.getDlInfo().getdL_id();
		try {
			final String sql = sqlHelper.getSql("updateDLInfo");
			updateStatus = clientAppDbJdbcTemplate.update(sql,
					new Object[] { eTLAdmin.getDlInfo().getdL_name(), eTLAdmin.getDlInfo().getVersion(), eTLAdmin.getDlInfo().getdL_table_name(),
							eTLAdmin.getDlInfo().getDescription(), eTLAdmin.getDlInfo().getJobName(), 
							eTLAdmin.getDlInfo().getJobExecutionType(),eTLAdmin.getDlInfo().getJobTagId(),
							eTLAdmin.getDlInfo().getLoadParameterId(),
							eTLAdmin.getDlInfo().getMasterParameterId(),
							eTLAdmin.getModification().getModifiedBy(),
							eTLAdmin.getModification().getModifiedTime(), eTLAdmin.getDlInfo().isIs_Active(), eTLAdmin.getDlInfo().getIndustry().getId(),
							eTLAdmin.getDlInfo().getJobFileNames(), dLId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateDLInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateDLInfo", e);
			
		}
		return updateStatus;
	}

	@Override
	public int deleteDLFileInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		int deleteFile = 0;
		try {
			String deleteDLFileInfo = sqlHelper.getSql("deleteDLFileInfo");
			deleteFile = clientAppDbJdbcTemplate.update(deleteDLFileInfo, new Object[] { eTLAdmin.getDlInfo().getdL_id() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteDLFileInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteDLFileInfo", e);
			
		}

		return deleteFile;
	}

	@Override
	public List<ILConnectionMapping> getDatabaseDetailsByILId(int iLID, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ILConnectionMapping> queryDetails = null;
		try {
			String sql = sqlHelper.getSql("getDBDetailsByILId");

			queryDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { iLID }, new RowMapper<ILConnectionMapping>() {
				public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {
					ILConnectionMapping queryInfo = new ILConnectionMapping();
					Database dbInfo = new Database();
					ILConnection ilInfo = new ILConnection();
					queryInfo.setiLquery(rs.getString("il_query"));
					queryInfo.setConnectionMappingId(rs.getInt("mapping_id"));
					dbInfo.setId(rs.getInt("database_type_id"));
					dbInfo.setName(rs.getString("name"));
					ilInfo.setDatabase(dbInfo);
					queryInfo.setiLConnection(ilInfo);
					return queryInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getDatabaseDetailsByILId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getDatabaseDetailsByILId", e);
			
		}

		return queryDetails;
	}

	@Override
	public int updateILQuery(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		int deleteCount = 0, updateCount = 0;
		try {
			String deleteILQuery = sqlHelper.getSql("deleteILQueryByIlId");
			deleteCount = clientAppDbJdbcTemplate.update(deleteILQuery, new Object[] { eTLAdmin.getIlInfo().getiL_id() });

			if (deleteCount > 0) {
				String updateILQuery = sqlHelper.getSql("updateILQueryByIlId");
				int length = eTLAdmin.getIlConnectionMapping().getConnectionMappingIds().size();
				List<Integer> connectionMappings = eTLAdmin.getIlConnectionMapping().getConnectionMappingIds();
				List<Integer> databaseIds = eTLAdmin.getIlConnectionMapping().getiLConnection().getDatabase().getIds();
				List<String> iLQueries = eTLAdmin.getIlConnectionMapping().getIlquery();
				for (int i = 0; i < length; i++) {
					clientAppDbJdbcTemplate.update(updateILQuery,
							new Object[] { connectionMappings.get(i), eTLAdmin.getIlInfo().getiL_id(), databaseIds.get(i), iLQueries.get(i) });
					updateCount++;
				}

			}
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateILQuery", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateILQuery", e);
			
		}

		return updateCount;

	}

	@Override
	public void deleteClientSourceDetails(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		try {
			// add
			String deleteSourceDetails = sqlHelper.getSql("deleteSourceDetailsByClientId");
			clientAppDbJdbcTemplate.update(deleteSourceDetails, new Object[] { clientId });

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteClientSourceDetails", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteClientSourceDetails", e);
			
		}
	}

	@Override
	public int getMaxFileSize(JdbcTemplate clientAppDbJdbcTemplate) {

		int maxFileSize = 0;
		try {

			String getMaxFileSize = sqlHelper.getSql("getMaxFileSize");
			maxFileSize = clientAppDbJdbcTemplate.queryForObject(getMaxFileSize, Integer.class);

		} catch (DataAccessException ae) {
			LOGGER.error("error while getMaxFileSize", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getMaxFileSize", e);
			
		}

		return maxFileSize;

	}

	@Override
	public int updateFileSettings(FileSettings fileSettings, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
            if(fileSettings.getMultiPartEnabled() == false){
			String updateFileSettings = sqlHelper.getSql("updateFileSettingsWithoutNoOfRecords");
			count = clientAppDbJdbcTemplate.update(updateFileSettings,
					new Object[] { fileSettings.getMaxFileSizeInMb(),fileSettings.getMultiPartEnabled(),
							fileSettings.getFileEncryption(),
							fileSettings.getModification().getModifiedBy(), fileSettings.getModification().getModifiedTime()});
            }else{
            	String updateFileSettings = sqlHelper.getSql("updateFileSettings");
    			count = clientAppDbJdbcTemplate.update(updateFileSettings,
    					new Object[] { fileSettings.getMaxFileSizeInMb(),fileSettings.getMultiPartEnabled(),
    							fileSettings.getNoOfRecordsPerFile(),fileSettings.getFileEncryption(),
    							fileSettings.getModification().getModifiedBy(), fileSettings.getModification().getModifiedTime()});
            }

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateFileSettings", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateFileSettings", e);
			
		}

		return count;

	}

	@Override
	public int deleteILById(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String deleteILById = sqlHelper.getSql("deleteILById");
			count = clientAppDbJdbcTemplate.update(deleteILById, new Object[] { iLId });

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteILById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteILById", e);
			
		}

		return count;
	}

	@Override
	public int deleteDLById(int dLId, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String deleteDLById = sqlHelper.getSql("deleteDLById");
			count = clientAppDbJdbcTemplate.update(deleteDLById, new Object[] { dLId });

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteDLById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteDLById", e);
			
		}

		return count;
	}

	@Override
	public List<Map<String, String>> getTemplate( JdbcTemplate clientAppDbJdbcTemplate) {
		List<Map<String, String>> ilTemp = null;
		try {
			String sql = sqlHelper.getSql("getTemplate");
			ilTemp = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<List<Map<String, String>>>() {
				@Override
				public List<Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs == null)
						return null;
					List<Map<String, String>> ilList = new ArrayList<>();
					Map<String, String> ilMap = null;
					while (rs.next()) {
						ilMap = new LinkedHashMap<>();
						ilMap.put("il_id", rs.getString("il_id"));
						ilMap.put("IL_name", rs.getString("IL_name"));
						ilMap.put("filename", rs.getString("filename"));
						ilMap.put("xref_filename", rs.getString("xref_filename"));
						ilList.add(ilMap);
					}
					return ilList;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getTemplate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getTemplate()", e);
			
		}
		return ilTemp;
	}

	@Override
	public int addNewILAndXrefTemplateInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {

		int addNewILAndXrefTemplate = 0;
		try {
			String sql = sqlHelper.getSql("addNewILAndXrefTemplateInfo");

			addNewILAndXrefTemplate = clientAppDbJdbcTemplate.update(sql, new Object[] { clientData.getIlInfo().getiL_id(), clientData.getIlInfo().getiL_name(),
					clientData.getIlInfo().getxRef_IL_name(), clientData.getModification().getCreatedBy(), clientData.getModification().getCreatedTime() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while addNewILAndXrefTemplateInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addNewILAndXrefTemplateInfo()", e);
			
		}
		return addNewILAndXrefTemplate;
	}

	@Override
	public int updateILAndXrefTemplateInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {

		int updateILAndXrefTemplate = 0;
		try {
			String sql = sqlHelper.getSql("updateILAndXrefTemplateInfo");

			updateILAndXrefTemplate = clientAppDbJdbcTemplate
					.update(sql,
							new Object[] { clientData.getIlInfo().getiL_name(), clientData.getIlInfo().getxRef_IL_name(),
									clientData.getModification().getModifiedBy(), clientData.getModification().getModifiedTime(),
									clientData.getIlInfo().getiL_id() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateILAndXrefTemplateInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateILAndXrefTemplateInfo()", e);
			
		}
		return updateILAndXrefTemplate;
	}

	public List<ErrorLog> getTopErrorLog(JdbcTemplate clientAppDbJdbcTemplate) {
		List<ErrorLog> errorLogs = null;
		try {
			String sql = sqlHelper.getSql("getTopErrorLog");
			errorLogs = clientAppDbJdbcTemplate.query(sql, new RowMapper<ErrorLog>() {
				public ErrorLog mapRow(ResultSet rs, int i) throws SQLException {
					ErrorLog errorLog = new ErrorLog();
					errorLog.setId(rs.getString("id"));
					errorLog.setErrorCode(rs.getString("error_code"));
					errorLog.setErrorMessage(rs.getString("error_message"));
					errorLog.setErrorDatetime(rs.getString("error_datetime"));
					errorLog.setRequestedService(rs.getString("requested_service"));
					errorLog.setReceivedParameters(rs.getString("received_parameters"));
					errorLog.setUserId(rs.getString("user_id"));
					errorLog.setClientDetails(rs.getString("client_details"));
					return errorLog;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getTopErrorLog()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getTopErrorLog()", e);
			
		}
		return errorLogs;
	}

	public ErrorLog getClientErrorLogById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		ErrorLog errorLogById = null;
		try {
			String sql = sqlHelper.getSql("getClientErrorLogById");

			errorLogById = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<ErrorLog>() {
				@Override
				public ErrorLog extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						ErrorLog errorLog = new ErrorLog();
						errorLog.setId(rs.getString("id"));
						errorLog.setErrorCode(rs.getString("error_code"));
						errorLog.setErrorMessage(rs.getString("error_message"));
						errorLog.setErrorBody(rs.getString("error_body"));
						errorLog.setErrorDatetime(rs.getString("error_datetime"));
						errorLog.setRequestedService(rs.getString("requested_service"));
						errorLog.setReceivedParameters(rs.getString("received_parameters"));
						errorLog.setUserId(rs.getString("user_id"));
						errorLog.setClientDetails(rs.getString("client_details"));
						return errorLog;
					} else {
						return null;
					}
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getClientErrorLogById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getClientErrorLogById()", e);
			
		}

		return errorLogById;
	}

	@Override
	public List<ErrorLog> searchClientErrorLog(ErrorLog errorLog, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ErrorLog> errorLogs = null;
		try {
			String sql = sqlHelper.getSql("searchClientErrorLog");
			errorLogs = clientAppDbJdbcTemplate.query(sql, new Object[] { errorLog.getErrorCode(), errorLog.getReceivedParameters(), errorLog.getClientDetails() },
					new RowMapper<ErrorLog>() {
						public ErrorLog mapRow(ResultSet rs, int i) throws SQLException {
							ErrorLog errorLog = new ErrorLog();
							errorLog.setId(rs.getString("id"));
							errorLog.setErrorCode(rs.getString("error_code"));
							errorLog.setErrorMessage(rs.getString("error_message"));
							errorLog.setErrorBody(rs.getString("error_body"));
							errorLog.setErrorDatetime(rs.getString("error_datetime"));
							errorLog.setRequestedService(rs.getString("requested_service"));
							errorLog.setReceivedParameters(rs.getString("received_parameters"));
							errorLog.setClientDetails(rs.getString("client_details"));
							return errorLog;
						}
					});

		} catch (DataAccessException ae) {
			LOGGER.error("error while searchClientErrorLog()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while searchClientErrorLog()", e);
			
		}
		return errorLogs;
	}

	@Override
	public int createWebService(WebService webservice, JdbcTemplate clientAppDbJdbcTemplate) {

		int createWebService = 0;
		try {
			String sql = sqlHelper.getSql("createWebService");

			createWebService = clientAppDbJdbcTemplate.update(sql,
					new Object[] { webservice.getWebserviceName(), webservice.getAuthenticationUrl(), webservice.getAuthentication_Method_Type(),
							webservice.getAuthentication_Request_Params(), webservice.getModification().getCreatedBy(),
							webservice.getModification().getCreatedTime() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while createWebService()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createWebService()", e);
			
		}
		return createWebService;
	}

	@Override
	public int createNewVertical(Industry industry, JdbcTemplate clientAppDbJdbcTemplate) {
		int create = 0;

		try {
			String sql = sqlHelper.getSql("createNewVertical");
			create = clientAppDbJdbcTemplate.update(sql, new Object[] { industry.getName(), industry.getDescription(), industry.getModification().getCreatedBy(),
					industry.getModification().getCreatedTime() });
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while createNewVertical()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while createNewVertical()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createNewVertical()", e);
			
		}
		return create;
	}

	@Override
	public List<Industry> getExistingVerticals( JdbcTemplate clientAppDbJdbcTemplate) {
		List<Industry> industries = null;

		try {
			String sql = sqlHelper.getSql("getExistingVerticals");
			industries = clientAppDbJdbcTemplate.query(sql, new RowMapper<Industry>() {

				@Override
				public Industry mapRow(ResultSet rs, int arg1) throws SQLException {
					Industry industry = new Industry();
					industry.setId(rs.getInt("id"));
					industry.setName(rs.getString("name"));
					industry.setDescription(rs.getString("description"));
					industry.setIsActive(rs.getBoolean("isActive"));
					industry.setIsDefault(rs.getBoolean("is_default"));
					return industry;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getExistingVerticals()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getExistingVerticals()", e);
			
			throw new AnvizentRuntimeException(e);
		}
		return industries;
	}

	@Override
	public Industry getVerticalDetailsById(int industryId, JdbcTemplate clientAppDbJdbcTemplate) {
		Industry industry = null;

		try {
			String sql = sqlHelper.getSql("getVerticalDetailsById");
			industry = clientAppDbJdbcTemplate.query(sql, new Object[] { industryId }, new ResultSetExtractor<Industry>() {
				@Override
				public Industry extractData(ResultSet rs) throws SQLException, DataAccessException {
					Industry industry = new Industry();
					if (rs == null)
						return null;
					while (rs.next()) {
						industry.setId(rs.getInt("id"));
						industry.setName(rs.getString("name"));
						industry.setDescription(rs.getString("description"));
						industry.setIsActive(rs.getBoolean("isActive"));
					}
					return industry;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getVerticalDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getVerticalDetailsById()", e);
			
		}
		return industry;
	}

	@Override
	public int updateVerticalById(Industry industry, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;

		try {
			String sql = sqlHelper.getSql("updateVerticalById");
			update = clientAppDbJdbcTemplate.update(sql, new Object[] { industry.getName(), industry.getDescription(), industry.getIsActive(),
					industry.getModification().getModifiedBy(), industry.getModification().getModifiedTime(), industry.getId() });
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while updateVerticalById()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateVerticalById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateVerticalById()", e);
			
		}
		return update;
	}

	@Override
	public List<Kpi> getExistingkpis( JdbcTemplate clientAppDbJdbcTemplate) {
		List<Kpi> kpis = null;

		try {
			String sql = sqlHelper.getSql("getExistingkpis");
			kpis = clientAppDbJdbcTemplate.query(sql, new RowMapper<Kpi>() {

				@Override
				public Kpi mapRow(ResultSet rs, int arg1) throws SQLException {
					Kpi kpi = new Kpi();
					kpi.setKpiId(rs.getInt("id"));
					kpi.setKpiName(rs.getString("kpi_name"));
					kpi.setKpiDescription(rs.getString("kpi_description"));
					kpi.setIsActive(rs.getBoolean("isActive"));
					return kpi;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getExistingkpis()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getExistingkpis()", e);
			
		}
		return kpis;
	}

	@Override
	public int createNewKpi(Kpi kpi, JdbcTemplate clientAppDbJdbcTemplate) {
		int create = 0;

		try {
			String sql = sqlHelper.getSql("createNewKpi");
			create = clientAppDbJdbcTemplate.update(sql,
					new Object[] { kpi.getKpiName(), kpi.getKpiDescription(), kpi.getModification().getCreatedBy(), kpi.getModification().getCreatedTime() });
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while createNewKpi()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while createNewKpi()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createNewKpi()", e);
			
		}
		return create;
	}

	@Override
	public Kpi getKpiDetailsById(int kpiId, JdbcTemplate clientAppDbJdbcTemplate) {
		Kpi kpi = null;

		try {
			String sql = sqlHelper.getSql("getKpiDetailsById");
			kpi = clientAppDbJdbcTemplate.query(sql, new Object[] { kpiId }, new ResultSetExtractor<Kpi>() {
				@Override
				public Kpi extractData(ResultSet rs) throws SQLException, DataAccessException {
					Kpi kpiDetails = new Kpi();
					if (rs == null)
						return null;
					while (rs.next()) {
						kpiDetails.setKpiId(rs.getInt("id"));
						kpiDetails.setKpiName(rs.getString("kpi_name"));
						kpiDetails.setKpiDescription(rs.getString("kpi_description"));
						kpiDetails.setIsActive(rs.getBoolean("isActive"));
					}
					return kpiDetails;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getKpiDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getKpiDetailsById()", e);
			
		}
		return kpi;
	}

	@Override
	public int updateKpiById(Kpi kpi, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;

		try {
			String sql = sqlHelper.getSql("updateKpiById");
			update = clientAppDbJdbcTemplate.update(sql, new Object[] { kpi.getKpiName(), kpi.getKpiDescription(), kpi.getIsActive(),
					kpi.getModification().getModifiedBy(), kpi.getModification().getModifiedTime(), kpi.getKpiId() });
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while updateKpiById()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateKpiById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateKpiById()", e);
			
		}
		return update;
	}

	@Override
	public List<ILInfo> getExistingILsInfo( JdbcTemplate clientAppDbJdbcTemplate) {
		List<ILInfo> ilList = null;
		try {
			String sql = sqlHelper.getSql("getExistingILsInfo");
			ilList = clientAppDbJdbcTemplate.query(sql, new RowMapper<ILInfo>() {
				public ILInfo mapRow(ResultSet rs, int i) throws SQLException {
					ILInfo iLInfo = new ILInfo();
					iLInfo.setiL_id(rs.getInt("IL_id"));
					iLInfo.setiL_name(rs.getString("IL_name"));
					iLInfo.setiLType(rs.getString("il_type"));
					iLInfo.setiL_table_name(rs.getString("il_table_name"));
					iLInfo.setXref_il_table_name(rs.getString("xref_il_table_name"));
					iLInfo.setDescription(rs.getString("description"));
					iLInfo.setJobName(rs.getString("Job_name"));
					iLInfo.setPurgeScript(rs.getString("purge_script"));
					iLInfo.setIsActive(rs.getBoolean("isActive"));
					Modification modification = new Modification();
					modification.setCreatedTime(rs.getString("created_time"));
					modification.setModifiedTime(rs.getString("modified_time"));
					iLInfo.setModification(modification);
					iLInfo.setVersion(rs.getString("version"));
					return iLInfo;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getExistingILsInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getExistingILsInfo", e);
			
		}
		return ilList;
	}

	@Override
	public ILInfo getILInfoById(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		ILInfo ilInfo = null;
		try {
			String sql = sqlHelper.getSql("getILInfoById");
			ilInfo = clientAppDbJdbcTemplate.query(sql, new Object[] { iLId }, new ResultSetExtractor<ILInfo>() {
				@Override
				public ILInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					ILInfo iLInfo = new ILInfo();
					if (rs != null && rs.next()) {
						iLInfo.setiL_id(rs.getInt("IL_id"));
						iLInfo.setiL_name(rs.getString("IL_name"));
						iLInfo.setiLType(rs.getString("il_type"));
						iLInfo.setiL_table_name(rs.getString("il_table_name"));
						iLInfo.setXref_il_table_name(rs.getString("xref_il_table_name"));
						iLInfo.setDescription(rs.getString("description"));
						iLInfo.setJobName(rs.getString("Job_name"));
						iLInfo.setIsActive(rs.getBoolean("isActive"));
						iLInfo.setVersion(rs.getString("version"));
						iLInfo.setJobExecutionType(rs.getString("job_execution_type"));
						iLInfo.setJobTagId(rs.getLong("elt_job_tag"));
						iLInfo.setLoadParameterId(rs.getLong("elt_load_parameter"));
						iLInfo.setMasterParameterId(rs.getLong("elt_master_id"));
						
						List<String> purgeScripts = new ArrayList<>();
						String ps = rs.getString("purge_script");
						iLInfo.setPurgeScript(ps);
						purgeScripts.add(ps);
						iLInfo.setPurgeScripts(purgeScripts);
					}
					return iLInfo;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getILInfoById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getILInfoById", e);
			
		}
		return ilInfo;
	}

	@Override
	public ILInfo getClientSpecificILInfoById(int iLId, int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		ILInfo ilInfo = null;
		try {
			String sql = sqlHelper.getSql("getClientSpecificILInfoById");
			ilInfo = clientAppDbJdbcTemplate.query(sql, new Object[] { iLId, clientId }, new ResultSetExtractor<ILInfo>() {
				@Override
				public ILInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					ILInfo iLInfo = new ILInfo();
					if (rs != null && rs.next()) {
						iLInfo.setJobName(rs.getString("Job_name"));
						iLInfo.setJobFileNames(rs.getString("dependency_jars"));
						iLInfo.setIsDefaultIL(rs.getBoolean("is_default"));
						iLInfo.setVersion(rs.getString("job_version"));
					}
					return iLInfo;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getClientSpecificILInfoById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getClientSpecificILInfoById", e);
			
		}
		return ilInfo;
	}

	@Override
	public List<ETLJobContextParam> getContextParamsByILId(int iLID, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ETLJobContextParam> paramData = null;
		try {
			String sql = sqlHelper.getSql("getContextParamsByILId");

			paramData = clientAppDbJdbcTemplate.query(sql, new Object[] { iLID }, new RowMapper<ETLJobContextParam>() {
				public ETLJobContextParam mapRow(ResultSet rs, int i) throws SQLException {
					ETLJobContextParam paramInfo = new ETLJobContextParam();
					paramInfo.setParamId(rs.getInt("param_id"));
					paramInfo.setParamName(rs.getString("param_name"));
					paramInfo.setParamValue(rs.getString("paramval"));
					return paramInfo;
				}
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getContextParamsByILId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getContextParamsByILId", e);
			
		}
		return paramData;
	}

	@Override
	public String getJobFilesByILId(int iLID, JdbcTemplate clientAppDbJdbcTemplate) {
		String filesNames = null;
		try {
			String sql = sqlHelper.getSql("getJobFilesByILId");
			filesNames = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("dependency_jars");
					} else {
						return null;
					}
				}

			}, iLID);

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getJobFilesByILId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getJobFilesByILId", e);
			
		}
		return filesNames;
	}

	@Override
	public int updateILDetailsById(ILInfo iLInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		int updateStatus = 0;
		try {
			String sql = sqlHelper.getSql("updateILDetailsById");
			updateStatus = clientAppDbJdbcTemplate.update(sql,
					new Object[] { iLInfo.getiL_name(), iLInfo.getVersion(), iLInfo.getiLType(), iLInfo.getiL_table_name(), iLInfo.getXref_il_table_name(),
							iLInfo.getDescription(), iLInfo.getJobName(), iLInfo.getJobFileNames(), iLInfo.getPurgeScript(), iLInfo.getIsActive(),
							iLInfo.getJobExecutionType(),iLInfo.getJobTagId(),iLInfo.getLoadParameterId(),
							iLInfo.getMasterParameterId(),
							iLInfo.getModification().getModifiedBy(), iLInfo.getModification().getModifiedTime(), iLInfo.getiL_id() });
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while updateILDetailsById()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateILDetailsById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateILDetailsById", e);
			
		}
		return updateStatus;
	}

	@Override
	public int deleteILJobFileInfo(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		int deleteFile = 0;
		try {
			String sql = sqlHelper.getSql("deleteILJobFileInfo");
			deleteFile = clientAppDbJdbcTemplate.update(sql, new Object[] { iLId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteILJobFileInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteILJobFileInfo", e);
			
		}
		return deleteFile;
	}

	@Override
	public int deleteILContextParams(int iLId, JdbcTemplate clientAppDbJdbcTemplate) {
		int delete = 0;
		try {
			String sql = sqlHelper.getSql("deleteILContextParams");
			delete = clientAppDbJdbcTemplate.update(sql, new Object[] { iLId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteILContextParams", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteILContextParams", e);
			
		}

		return delete;
	}

	@Override
	public int saveEtlIlContextParams(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		List<ETLJobContextParam> ilParamsList = eTLAdmin.geteTLJobContextParamList();
		try {
			String sql = sqlHelper.getSql("saveEtlilContextParameters");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ETLJobContextParam ilParams = ilParamsList.get(i);
					ps.setInt(1, ilParams.getParamId());
					ps.setInt(2, eTLAdmin.getIlInfo().getiL_id());
					ps.setString(3, ilParams.getParamValue());
					ps.setString(4, eTLAdmin.getModification().getCreatedBy());
					ps.setString(5, eTLAdmin.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return ilParamsList.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveEtlIlContextParams", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveEtlIlContextParams", e);
			
		}

		return count != null?count.length:0;

	}

	@Override
	public int saveILJobFileInfo(ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		List<String> fileNamesList = eTLAdmin.getFileNames();
		try {
			String sql = sqlHelper.getSql("saveILJobFileInfo");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					String fileNames = fileNamesList.get(i);
					int index = fileNames.lastIndexOf("\\");
					String fileName = fileNames.substring(index + 1);
					ps.setInt(1, eTLAdmin.getIlInfo().getiL_id());
					ps.setString(2, fileName);
					ps.setString(3, eTLAdmin.getModification().getCreatedBy());
					ps.setString(4, eTLAdmin.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return fileNamesList.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveILJobFileInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveILJobFileInfo", e);
			
		}

		return count != null?count.length:0;
	}

	@Override
	public List<Industry> getVerticalsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<Industry> verticals = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getVerticalsByClientId");
			verticals = clientAppDbJdbcTemplate.query(sql, new Object[]{clientId},new RowMapper<Industry>() {
				@Override
				public Industry mapRow(ResultSet rs, int rowNum) throws SQLException {
					Industry vertical = new Industry();
					vertical.setId(rs.getInt("id"));
					vertical.setName(rs.getString("name"));
					vertical.setDescription(rs.getString("description"));
					return vertical;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getVerticalsByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getVerticalsByClientId", e);
			
		}
		return verticals;
	}

	@Override
	public int deleteClientVerticalMappingById(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		int delete = 0;
		try {
			String sql = sqlHelper.getSql("deleteClientVerticalMappingById");
			delete = clientAppDbJdbcTemplate.update(sql, new Object[] { clientId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteClientVerticalMappingById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteClientVerticalMappingById", e);
			
		}
		return delete;
	}

	@Override
	public int saveClientVerticalMapping(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		List<Industry> verticals = clientData.getIndustries();
		try {
			String sql = sqlHelper.getSql("saveClientVerticalMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Industry vertical = verticals.get(i);
					ps.setInt(1, Integer.parseInt(clientData.getUserId()));
					ps.setInt(2, vertical.getId());
					ps.setString(3, clientData.getModification().getCreatedBy());
					ps.setString(4, clientData.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return verticals.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveClientVerticalMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveClientVerticalMapping", e);
			
		}
		return count == null ? 0 : count.length;
	}

	@Override
	public int deleteClientVerticalMappedDLsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		int delete = 0;
		try {
			String sql = sqlHelper.getSql("deleteClientVerticalMappedDLsByClientId");
			delete = clientAppDbJdbcTemplate.update(sql, new Object[] { clientId, clientId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteClientVerticalMappedDLsByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteClientVerticalMappedDLsByClientId", e);
			
		}
		return delete;
	}

	@Override
	public List<Database> getAllDatabases( JdbcTemplate clientAppDbJdbcTemplate) {
		List<Database> databases = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getAllDatabases");
			databases = clientAppDbJdbcTemplate.query(sql, new RowMapper<Database>() {

				@Override
				public Database mapRow(ResultSet rs, int rowNum) throws SQLException {
					Database database = new Database();
					database.setId(rs.getInt("id"));
					database.setName(rs.getString("name"));
                    database.setDriverName(rs.getString("driver_name"));
                    database.setProtocal(rs.getString("protocal"));
                    database.setUrlFormat(rs.getString("url_format"));
					return database;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAllDatabases", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAllDatabases", e);
			
		}

		return databases;
	}

	@Override
	public List<Database> getDatabasesByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<Database> databases = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getDatabasesByClientId");
			databases = clientAppDbJdbcTemplate.query(sql, new RowMapper<Database>() {

				@Override
				public Database mapRow(ResultSet rs, int rowNum) throws SQLException {
					Database database = new Database();
					database.setId(rs.getInt("database_id"));
					database.setName(rs.getString("name"));
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setUrlFormat(rs.getString("url_format"));
					return database;
				}
			}, clientId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDatabasesByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDatabasesByClientId", e);
			
		}

		return databases;
	}

	@Override
	public List<Database> getConnectorsByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<Database> databases = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getConnectorsByClientId");
			databases = clientAppDbJdbcTemplate.query(sql, new RowMapper<Database>() {

				@Override
				public Database mapRow(ResultSet rs, int rowNum) throws SQLException {
					Database database = new Database();
					database.setId(rs.getInt("dbid"));
					database.setName(rs.getString("databasename"));
					database.setConnector_id(rs.getInt("connectorid"));
					database.setConnectorName(rs.getString("connectorname"));
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setUrlFormat(rs.getString("url_format"));
					return database;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getConnectorsByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getConnectorsByClientId", e);
			
		}

		return databases;
	}

	@Override
	public List<Integer> getClientsByDatabaseId(int databaseId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<Integer> clients = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getClientsByDatabaseId");
			clients = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<List<Integer>>() {
				@Override
				public List<Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Integer> clientList = new ArrayList<>();
					if (rs == null)
						return clientList;

					while (rs.next()) {
						int clientId = rs.getInt("client_id");
						clientList.add(clientId);
					}
					return clientList;
				}
			}, databaseId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getClientsByDatabaseId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getClientsByDatabaseId", e);
			
		}
		return clients;
	}

	@Override
	public int deleteClientDatabaseMappingById(int clientId, String columnName, JdbcTemplate clientAppDbJdbcTemplate) {
		int delete = 0;
		try {
			String sql = sqlHelper.getSql("deleteClientDatabaseMappingById");
			sql = sql.replace("placeholder", columnName);
			delete = clientAppDbJdbcTemplate.update(sql, new Object[] { clientId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteClientDatabaseMappingById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteClientDatabaseMappingById", e);
			
		}
		return delete;
	}

	@Override
	public int saveClientDatabaseMapping(int clientId, List<String> databases, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		try {
			String sql = sqlHelper.getSql("saveClientDatabaseMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, clientId);
					ps.setInt(2, Integer.parseInt(databases.get(i)));
					ps.setString(3, modification.getCreatedBy());
					ps.setString(4, modification.getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return databases.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveClientDatabaseMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveClientDatabaseMapping", e);
			
		}
		return count != null?count.length:0;
	}

	@Override
	public int deleteClientConnectorMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		int delete = 0;
		try {
			String sql = sqlHelper.getSql("deleteClientConnectorMapping");
			delete = clientAppDbJdbcTemplate.update(sql, new Object[] { clientId, clientId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteClientConnectorMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteClientConnectorMapping", e);
			
		}
		return delete;
	}

	@Override
	public int saveDatabaseClientMapping(int databaseId, List<String> clients, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		try {
			String sql = sqlHelper.getSql("saveDatabaseClientMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, databaseId);
					ps.setInt(2, Integer.parseInt(clients.get(i)));
					ps.setString(3, modification.getCreatedBy());
					ps.setString(4, modification.getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return clients.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveDatabaseClientMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveDatabaseClientMapping", e);
			
		}
		return count != null?count.length:0;
	}

	@Override
	public List<Database> getDatabaseMappedConnectors(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<Database> databases = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getDatabaseMappedConnectors");
			databases = clientAppDbJdbcTemplate.query(sql, new RowMapper<Database>() {

				@Override
				public Database mapRow(ResultSet rs, int rowNum) throws SQLException {
					Database database = new Database();
					database.setConnector_id(rs.getInt("connector_id"));
					return database;
				}
			}, clientId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDatabaseMappedConnectors", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDatabaseMappedConnectors", e);
			
		}

		return databases;
	}

	@Override
	public int deleteClientConnectorMappingById(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		int delete = 0;
		try {
			String sql = sqlHelper.getSql("deleteClientConnectorMappingById");
			delete = clientAppDbJdbcTemplate.update(sql, new Object[] { clientId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteClientConnectorMappingById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteClientConnectorMappingById", e);
			
		}
		return delete;
	}

	@Override
	public int saveClientConnectorMapping(int clientId, List<String> connectorIds, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		try {
			String sql = sqlHelper.getSql("saveClientConnectorMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, clientId);
					ps.setInt(2, Integer.parseInt(connectorIds.get(i)));
					ps.setString(3, modification.getCreatedBy());
					ps.setString(4, modification.getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return connectorIds.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveClientConnectorMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveClientConnectorMapping", e);
			
		}
		return count != null ?count.length:0;
	}

	@Override
	public List<ILInfo> getILInfoByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ILInfo> ilInfo = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getILInfoByClientId");
			ilInfo = clientAppDbJdbcTemplate.query(sql, new RowMapper<ILInfo>() {

				@Override
				public ILInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					ILInfo ilInfo = new ILInfo();
					ilInfo.setiL_id(rs.getInt("il_id"));
					ilInfo.setiL_name(rs.getString("il_name"));
					return ilInfo;
				}
			}, clientId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getILInfoByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getILInfoByClientId", e);
			
		}
		return ilInfo;
	}

	@Override
	public int saveClientSpecificIL(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String sql = sqlHelper.getSql("saveclientSpecificIL");
			count = clientAppDbJdbcTemplate.update(sql,
					new Object[] { Integer.parseInt(clientData.getUserId()), clientData.getIlInfo().getiL_id(), clientData.getIlInfo().getVersion(),
							clientData.getIlInfo().getJobName(), clientData.getIlInfo().getJobFileNames(), clientData.getModification().getCreatedBy(),
							clientData.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveClientSpecificIL", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveClientSpecificIL", e);
			
		}
		return count;
	}

	@Override
	public int updateClientSpecificIL(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String sql = sqlHelper.getSql("updateClientSpecificIL");
			count = clientAppDbJdbcTemplate.update(sql,
					new Object[] { clientData.getIlInfo().getVersion(), clientData.getIlInfo().getJobName(), clientData.getIlInfo().getJobFileNames(),
							clientData.getModification().getModifiedBy(), clientData.getModification().getModifiedTime(),
							Integer.parseInt(clientData.getUserId()), clientData.getIlInfo().getiL_id() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateClientSpecificIL", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateClientSpecificIL", e);
			
		}
		return count;
	}

	@Override
	public int updateClientSpecificILToDefault(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String sql = sqlHelper.getSql("updateClientSpecificILToDefault");
			count = clientAppDbJdbcTemplate.update(sql, new Object[] { clientData.getModification().getModifiedBy(), clientData.getModification().getModifiedTime(),
					Integer.parseInt(clientData.getUserId()), clientData.getIlInfo().getiL_id() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateClientSpecificILToDefault", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateClientSpecificILToDefault", e);
			
		}
		return count;
	}

	@Override
	public List<DLInfo> getDLInfoByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<DLInfo> dlInfo = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getDLInfoByClientId");
			dlInfo = clientAppDbJdbcTemplate.query(sql, new RowMapper<DLInfo>() {

				@Override
				public DLInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					DLInfo dlInfo = new DLInfo();
					dlInfo.setdL_id(rs.getInt("DL_id"));
					dlInfo.setdL_name(rs.getString("DL_name"));
					return dlInfo;
				}
			}, clientId, clientId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDLInfoByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDLInfoByClientId", e);
			
		}
		return dlInfo;
	}

	@Override
	public DLInfo getClientSpecificDLInfoById(int dLId, int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		DLInfo dlInfo = null;
		try {
			String sql = sqlHelper.getSql("getClientSpecificDLInfoById");
			dlInfo = clientAppDbJdbcTemplate.query(sql, new Object[] { dLId, clientId }, new ResultSetExtractor<DLInfo>() {
				@Override
				public DLInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					DLInfo dlInfo = new DLInfo();
					if (rs != null && rs.next()) {
						dlInfo.setJobName(rs.getString("Job_name"));
						dlInfo.setJobFileNames(rs.getString("dependency_jars"));
						dlInfo.setIsDefaultDL(rs.getBoolean("is_default"));
						dlInfo.setVersion(rs.getString("job_version"));
					}
					return dlInfo;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getClientSpecificDLInfoById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getClientSpecificDLInfoById", e);
			
		}
		return dlInfo;
	}

	@Override
	public int saveClientSpecificDLInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String sql = sqlHelper.getSql("saveClientSpecificDLInfo");
			count = clientAppDbJdbcTemplate.update(sql,
					new Object[] { Integer.parseInt(clientData.getUserId()), clientData.getDlInfo().getdL_id(), clientData.getDlInfo().getVersion(),
							clientData.getDlInfo().getJobName(), clientData.getDlInfo().getJobFileNames(), clientData.getModification().getCreatedBy(),
							clientData.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveClientSpecificDLInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveClientSpecificDLInfo", e);
			
		}
		return count;
	}

	@Override
	public int updateClientSpecificDLInfo(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String sql = sqlHelper.getSql("updateClientSpecificDLInfo");
			count = clientAppDbJdbcTemplate.update(sql,
					new Object[] { clientData.getDlInfo().getVersion(), clientData.getDlInfo().getJobName(), clientData.getDlInfo().getJobFileNames(),
							clientData.getModification().getModifiedBy(), clientData.getModification().getModifiedTime(),
							Integer.parseInt(clientData.getUserId()), clientData.getDlInfo().getdL_id() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateClientSpecificDLInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateClientSpecificDLInfo", e);
			
		}
		return count;
	}

	@Override
	public int updateClientSpecificDLToDefault(ClientData clientData, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String sql = sqlHelper.getSql("updateClientSpecificDLToDefault");
			count = clientAppDbJdbcTemplate.update(sql, new Object[] { clientData.getModification().getModifiedBy(), clientData.getModification().getModifiedTime(),
					Integer.parseInt(clientData.getUserId()), clientData.getDlInfo().getdL_id() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateClientSpecificDLToDefault", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateClientSpecificDLToDefault", e);
			
		}
		return count;
	}

	@Override
	public AllMappingInfo getAllmappingInfoById(int clientId, JdbcTemplate clientAppDbJdbcTemplate, JdbcTemplate commonDbJdbcTemplate) {
		AllMappingInfo allMappingInfo = null;
		try {
			String sql = sqlHelper.getSql("getAllmappingInfoById");
			String sqlS3Mapping = sqlHelper.getSql("getTableScriptsandS3MappingInfoById");
			allMappingInfo = clientAppDbJdbcTemplate.query(sql,new Object[]{clientId, clientId, clientId, clientId, clientId}, new ResultSetExtractor<AllMappingInfo>() {

				@Override
				public AllMappingInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					AllMappingInfo allMappingInfo = new AllMappingInfo();
					if (rs != null && rs.next()) {
						allMappingInfo.setNumberOfVerticals(rs.getInt("number_of_verticals"));
						allMappingInfo.setNumberOfConnectors(rs.getInt("number_of_connectors"));
						allMappingInfo.setNumberOfDLs(rs.getInt("number_of_dls"));
						allMappingInfo.setNumberOfWebServices(rs.getInt("number_of_ws"));
						
					}
					return allMappingInfo;

				}
			});
			
			AllMappingInfo allS3MappingInfo = commonDbJdbcTemplate.query(sqlS3Mapping,new Object[]{clientId, clientId, clientId, clientId},new ResultSetExtractor<AllMappingInfo>() {

				@Override
				public AllMappingInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					AllMappingInfo allMappingInfo = new AllMappingInfo();
					if (rs != null && rs.next()) {
						allMappingInfo.setNumberOfTableScripts(rs.getInt("number_of_table_scripts"));
						allMappingInfo.setNumberOfS3BuckeMappings(rs.getInt("number_of_s3_mappings"));
						allMappingInfo.setNumberOfCurrencies(rs.getInt("number_of_currencies"));
						allMappingInfo.setNumberOfSchedulers(rs.getInt("number_of_schedulers"));
					}
				return allMappingInfo;

				}
			});
			allMappingInfo.setNumberOfTableScripts(allS3MappingInfo.getNumberOfTableScripts());
			allMappingInfo.setNumberOfS3BuckeMappings(allS3MappingInfo.getNumberOfS3BuckeMappings());
			allMappingInfo.setNumberOfCurrencies(allS3MappingInfo.getNumberOfCurrencies());
			allMappingInfo.setNumberOfSchedulers(allS3MappingInfo.getNumberOfSchedulers());
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAllmappingInfoById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAllmappingInfoById", e);
			
		}
		return allMappingInfo;
	}

	@Override
	public List<CommonJob> getCommonJobInfo( JdbcTemplate clientAppDbJdbcTemplate) {
		List<CommonJob> commonJobs = null;
		try {
			String sql = sqlHelper.getSql("getCommonJobInfo");

			commonJobs = clientAppDbJdbcTemplate.query(sql, new RowMapper<CommonJob>() {

				@Override
				public CommonJob mapRow(ResultSet rs, int rowNum) throws SQLException {
					CommonJob commonJob = new CommonJob();
					commonJob.setId(rs.getInt("id"));
					commonJob.setJobType(rs.getString("job_type"));
					commonJob.setJobFileName(rs.getString("job_file"));
					commonJob.setIsActive(rs.getBoolean("is_active"));
					return commonJob;
				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getCommonJobInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getCommonJobInfo", e);
			
		}
		return commonJobs;
	}

	@Override
	public int saveCommonJobInfo(CommonJob commonJob, JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("saveCommonJobInfo");

			save = clientAppDbJdbcTemplate.update(sql, new Object[] { commonJob.getJobType(), commonJob.getJobFileName(), commonJob.getIsActive(),
					commonJob.getModification().getCreatedBy(), commonJob.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveCommonJobInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveCommonJobInfo", e);
			
		}
		return save;
	}

	@Override
	public CommonJob getCommonJobInfoById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		CommonJob commonJob = null;
		try {
			String sql = sqlHelper.getSql("getCommonJobInfoById");

			commonJob = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<CommonJob>() {

				@Override
				public CommonJob extractData(ResultSet rs) throws SQLException, DataAccessException {
					CommonJob commonJob = new CommonJob();
					if (rs != null && rs.next()) {
						commonJob.setId(rs.getInt("id"));
						commonJob.setJobType(rs.getString("job_type"));
						commonJob.setJobFileName(rs.getString("job_file"));
						commonJob.setIsActive(rs.getBoolean("is_active"));
					}
					return commonJob;
				}

			}, id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getCommonJobInfoById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getCommonJobInfoById", e);
			
		}
		return commonJob;
	}

	@Override
	public int updateCommonJobInfo(CommonJob commonJob, JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("updateCommonJobInfo");

			save = clientAppDbJdbcTemplate.update(sql, new Object[] { commonJob.getJobType(), commonJob.getIsActive(), commonJob.getModification().getModifiedBy(),
					commonJob.getModification().getModifiedTime(), commonJob.getId() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateCommonJobInfo", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateCommonJobInfo", e);
			
		}
		return save;
	}

	@Override
	public Map<Integer, String> getAllWebServices( JdbcTemplate clientAppDbJdbcTemplate) {
		Map<Integer, String> webServices = null;

		try {

			String sql = sqlHelper.getSql("getAllWebServices");

			webServices = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<Map<Integer, String>>() {

				@Override
				public Map<Integer, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs == null)
						return null;

					Map<Integer, String> webService = new LinkedHashMap<>();

					while (rs.next()) {
						webService.put(rs.getInt(1), rs.getString(2));
					}

					return webService;
				}
			});

		} catch (Exception e) {
			LOGGER.error("Error while reading getAllWebServices ", e);
		}

		return webServices;
	}

	@Override
	public WebService getWebServiceAuthDetailsById(int webServiceId, JdbcTemplate clientAppDbJdbcTemplate) {
		WebService wsAuthDetails = null;
		try {
			String sql = sqlHelper.getSql("getWebServiceAuthDetailsById");
			wsAuthDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { webServiceId }, new ResultSetExtractor<WebService>() {
				public WebService extractData(ResultSet rs) throws SQLException, DataAccessException {
					WebService webService = new WebService();

					if (rs == null)
						return null;
					while (rs.next()) {
						webService.setWebserviceName(rs.getString("web_service_name"));
						webService.setAuthenticationUrl(rs.getString("authentication_url"));
						webService.setAuthentication_Method_Type(rs.getString("authentication_method_type"));
						webService.setAuthentication_Request_Params(rs.getString("Authentication_Request_Params"));
					}

					return webService;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getWebServiceAuthDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getWebServiceAuthDetailsById()", e);
			
		}
		return wsAuthDetails;
	}

	@Override
	public int saveWsILMappingDetails(WebServiceILMapping webServiceILMapping, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] save = null;
		try {
			String sql = sqlHelper.getSql("saveWsILMappingDetails");
			save = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, webServiceILMapping.getWsTemplateId());
					ps.setInt(2, webServiceILMapping.getiLId());
					ps.setString(3, webServiceILMapping.getWebServiceApis().get(i).getApiName());
					ps.setString(4, webServiceILMapping.getWebServiceApis().get(i).getApiUrl());
					ps.setBoolean(5, webServiceILMapping.getWebServiceApis().get(i).getBaseUrlRequired());
					ps.setString(6, webServiceILMapping.getWebServiceApis().get(i).getApiMethodType());
					ps.setString(7, webServiceILMapping.getWebServiceApis().get(i).getApiPathParams());
					ps.setString(8, webServiceILMapping.getWebServiceApis().get(i).getApiRequestParams());
					ps.setString(9, webServiceILMapping.getWebServiceApis().get(i).getResponseObjectName());
					ps.setString(10, webServiceILMapping.getModification().getCreatedBy());
					ps.setString(11, webServiceILMapping.getModification().getCreatedTime());
					ps.setString(12, webServiceILMapping.getWebServiceApis().get(i).getApiBodyParams());
					ps.setBoolean(13, webServiceILMapping.getWebServiceApis().get(i).getIncrementalUpdate());
					ps.setString(14, webServiceILMapping.getWebServiceApis().get(i).getResponseColumnObjectName());
					ps.setString(15, webServiceILMapping.getWebServiceApis().get(i).getIncrementalUpdateparamdata());
					ps.setBoolean(16, webServiceILMapping.getWebServiceApis().get(i).getPaginationRequired());
					ps.setString(17, webServiceILMapping.getWebServiceApis().get(i).getPaginationType());
					ps.setString(18, webServiceILMapping.getWebServiceApis().get(i).getPaginationRequestParamsData());
					ps.setString(19, webServiceILMapping.getWebServiceApis().get(i).getSoapBodyElement());
					ps.setString(20, webServiceILMapping.getWebServiceApis().get(i).getDefaultMapping());
					
					
				}

				@Override
				public int getBatchSize() {
					return webServiceILMapping.getWebServiceApis().size();
				}
			});
		} catch (DuplicateKeyException d) {
			LOGGER.error("error while saveWsILMappingDetails()", d);
			throw new AnvizentDuplicateFileNameException(d);
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveWsILMappingDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveWsILMappingDetails()", e);
			
		}

		return save != null ? save.length:0;
	}

	@Override
	public int updateWebServicesById(WebService webservice, JdbcTemplate clientAppDbJdbcTemplate) {
		int updateWebServicesById = 0;
		try {
			String sql = sqlHelper.getSql("updateWebServicesById");

			updateWebServicesById = clientAppDbJdbcTemplate.update(sql,
					new Object[] { webservice.getWebserviceName(), webservice.getAuthenticationUrl(), webservice.getAuthentication_Method_Type(),
							webservice.getAuthentication_Request_Params(), webservice.getModification().getModifiedBy(),
							webservice.getModification().getModifiedTime(), webservice.getWeb_service_id() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateWebServicesById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateWebServicesById()", e);
			
		}
		return updateWebServicesById;
	}

	@Override
	public List<WebServiceApi> getWSILMappingDetailsById(int wsTemplateId, int iLId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<WebServiceApi> mappingDetails = null;
		try {
			String sql = sqlHelper.getSql("getWSILMappingDetailsById");
			mappingDetails = clientAppDbJdbcTemplate.query(sql, new RowMapper<WebServiceApi>() {
				@Override
				public WebServiceApi mapRow(ResultSet rs, int rowNum) throws SQLException {
					WebServiceApi ws = new WebServiceApi();
					ws.setId(rs.getLong("id"));
					ws.setApiName(rs.getString("api_name"));
					ws.setBaseUrl(rs.getString("base_url"));
					ws.setBaseUrlRequired(rs.getBoolean("base_url_required"));
					ws.setPaginationType(rs.getString("pagination_type"));
					ws.setApiUrl(rs.getString("api_url"));
					ws.setApiMethodType(rs.getString("api_method_type"));
					ws.setApiPathParams(rs.getString("api_path_params"));
					ws.setApiRequestParams(rs.getString("api_request_params"));
					ws.setApiBodyParams(rs.getString("api_body_params"));
					ws.setIncrementalUpdate(rs.getBoolean("incremental_update"));
					ws.setIncrementalUpdateparamdata(rs.getString("incremental_update_params"));
					ws.setResponseObjectName(rs.getString("response_object_name"));
					ws.setResponseColumnObjectName(rs.getString("response_column_object_name"));
					ws.setPaginationRequired(rs.getBoolean("pagination_required"));
					ws.setPaginationType(rs.getString("pagination_type"));
					ws.setPaginationRequestParamsData(rs.getString("pagination_request_params"));
					ws.setSoapBodyElement(rs.getString("soap_body_element"));
					ws.setDefaultMapping(rs.getString("default_mapping"));
					return ws;
				}

			}, wsTemplateId, iLId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getWSILMappingDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getWSILMappingDetailsById()", e);
			
		}
		return mappingDetails;
	}

	@Override
	public List<String> getWebserviceClients(int webServiceId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<String> getWebserviceClients = null;

		try {
			String getWebserviceClientsSql = sqlHelper.getSql("getWebserviceClients");
			getWebserviceClients = clientAppDbJdbcTemplate.query(getWebserviceClientsSql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs != null) {
						return rs.getString(1);
					} else {
						return null;
					}
				}
			}, webServiceId);

		} catch (DataAccessException ae) {
			LOGGER.error("error while getWebserviceClients()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getWebserviceClients()", e);
			
		}
		return getWebserviceClients;

	}

	@Override
	public int saveWebserviceClientMapping(int webserviceId, String clients, ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {
		List<String> clientList = new ArrayList<String>(Arrays.asList(clients.split(",")));
		int[] count = null;
		try {

			String saveClientidMappingSql = sqlHelper.getSql("saveWebserviceClientMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(saveClientidMappingSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, webserviceId);
					ps.setString(2, clientList.get(i));
					ps.setString(3, eTLAdmin.getModification().getCreatedBy());
					ps.setString(4, eTLAdmin.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return clientList.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveWebserviceClientMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveWebserviceClientMapping", e);
			
		}

		return count != null?count.length:0;

	}

	@Override
	public int deleteWebserviceClientMapping(int webserviceId, JdbcTemplate clientAppDbJdbcTemplate) {

		int count = -1;
		try {
			String deleteWebserviceClientMapping = sqlHelper.getSql("deleteWebserviceClientMapping");
			count = clientAppDbJdbcTemplate.update(deleteWebserviceClientMapping, new Object[] { webserviceId });

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteWebserviceClientMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteWebserviceClientMapping", e);
			
		}

		return count;

	}

	@Override
	public List<WebService> getWebserviceByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<WebService> webservices = new ArrayList<>();
		try {
			String sql = sqlHelper.getSql("getWebserviceByClientId");
			webservices = clientAppDbJdbcTemplate.query(sql, new RowMapper<WebService>() {

				@Override
				public WebService mapRow(ResultSet rs, int rowNum) throws SQLException {
					WebService webService = new WebService();
					webService.setWeb_service_id(rs.getString("ws_template_id"));
					return webService;
				}
			}, clientId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getWebserviceByClientId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getWebserviceByClientId", e);
			
		}
		return webservices;
	}

	@Override
	public Map<Integer, String> getAllKpis( JdbcTemplate clientAppDbJdbcTemplate) {

		Map<Integer, String> getAllKpis = null;

		try {

			String sql = sqlHelper.getSql("getAllKpis");

			getAllKpis = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<Map<Integer, String>>() {

				@Override
				public Map<Integer, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs == null)
						return null;

					Map<Integer, String> webService = new LinkedHashMap<>();

					while (rs.next()) {
						webService.put(rs.getInt(1), rs.getString(2));
					}

					return webService;
				}
			});

		} catch (Exception e) {
			LOGGER.error("Error while reading getAllKpis ", e);
		}

		return getAllKpis;

	}

	@Override
	public int saveClientWebserviceMapping(int clientId, List<Integer> webServiceIds, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		try {

			String sql = sqlHelper.getSql("saveClientWebserviceMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, clientId);
					ps.setInt(2, webServiceIds.get(i));
					ps.setString(3, modification.getCreatedBy());
					ps.setString(4, modification.getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return webServiceIds.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveClientWebserviceMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveClientWebserviceMapping", e);
			
		}

		return count != null?count.length:0;

	}

	@Override
	public List<String> getKpiListByDlId(int dlId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<String> getKpiListByDlId = null;

		try {
			String getKpiListByDlIdSql = sqlHelper.getSql("getKpiListByDlId");
			getKpiListByDlId = clientAppDbJdbcTemplate.query(getKpiListByDlIdSql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs != null) {
						return rs.getString(1);
					} else {
						return null;
					}
				}
			}, dlId);

		} catch (DataAccessException ae) {
			LOGGER.error("error while getKpiListByDlId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getKpiListByDlId()", e);
			
		}
		return getKpiListByDlId;

	}

	@Override
	public int deleteDlKpiMapping(int dlId, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = -1;
		try {
			String deleteDlKpiMapping = sqlHelper.getSql("deleteDlKpiMapping");
			count = clientAppDbJdbcTemplate.update(deleteDlKpiMapping, new Object[] { dlId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteDlKpiMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteDlKpiMapping", e);
			
		}

		return count;

	}

	@Override
	public int saveDlKpiMapping(int dlId, String kpis, ETLAdmin eTLAdmin, JdbcTemplate clientAppDbJdbcTemplate) {


		List<Kpi> kpiInfoList = eTLAdmin.getKpiInfo();

		int[] count = null;
		try {

			String saveClientidMappingSql = sqlHelper.getSql("saveDlKpiMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(saveClientidMappingSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Kpi kpiInfo = kpiInfoList.get(i);
					ps.setInt(1, dlId);
					ps.setInt(2, kpiInfo.getKpiId());
					ps.setString(3, eTLAdmin.getModification().getCreatedBy());
					ps.setString(4, eTLAdmin.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return kpiInfoList.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveDlKpiMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveDlKpiMapping", e);
			
		}

		return count != null?count.length:0;
	}

	@Override
	public List<TableScriptsForm> getTableScripts( JdbcTemplate clientAppDbJdbcTemplate) {
		List<TableScriptsForm> tableScriptsList = null;
		try {
			String sql = sqlHelper.getSql("getTableScripts");
			tableScriptsList = clientAppDbJdbcTemplate.query(sql, new RowMapper<TableScriptsForm>() {
				public TableScriptsForm mapRow(ResultSet rs, int i) throws SQLException {
					TableScriptsForm tableScripts = new TableScriptsForm();
					tableScripts.setId(rs.getInt("id"));
					tableScripts.setClientId(rs.getInt("client_id"));
					tableScripts.setScriptTypeName(rs.getString("script_type"));
					tableScripts.setSchemaName(rs.getString("target_schema"));
					tableScripts.setScriptName(rs.getString("script_name"));
					tableScripts.setSqlScript("");
					tableScripts.setIs_Active(rs.getBoolean("isActive"));
					tableScripts.setScriptDescription(rs.getString("script_description"));
					tableScripts.setCreated_Date(rs.getString("created_time"));
					tableScripts.setModified_Date(rs.getString("modified_time"));
					tableScripts.setIsDefault(rs.getBoolean("is_default"));
					tableScripts.setVersion(rs.getString("version"));
					return tableScripts;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getTableScripts", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getTableScripts", e);
			
		}

		return tableScriptsList;

	}

	@Override
	public int addTableScripts(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate) {

		int count = -1;
		try {
			String addTableScripts = sqlHelper.getSql("addTableScripts");
			count = clientAppDbJdbcTemplate.update(addTableScripts, new Object[] { tableScripts.getPriority(), tableScripts.getScriptName(),
					tableScripts.getSqlScript(), tableScripts.getIs_Active(), tableScripts.getModification().getCreatedBy() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while addTableScripts", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addTableScripts", e);
			
		}

		return count;

	}

	@Override
	public int updateTableScripts(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate) {

		int count = -1;
		try {
			String updateTableScriptsSql = sqlHelper.getSql("updateTableScripts");
			count = clientAppDbJdbcTemplate.update(updateTableScriptsSql,
					new Object[] { tableScripts.getPriority(), tableScripts.getScriptName(), tableScripts.getSqlScript(),
							tableScripts.getModification().getCreatedBy(), tableScripts.getModification().getCreatedTime(), tableScripts.getIs_Active(),
							tableScripts.getId() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateTableScripts", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateTableScripts", e);
			
		}

		return count;
	}

	public int updateScriptHistoryTable(int tableScriptId, JdbcTemplate clientAppDbJdbcTemplate) {

		int count = -1;
		try {
			String updateScriptHistoryTbl = sqlHelper.getSql("updateScriptHistoryTable");
			count = clientAppDbJdbcTemplate.update(updateScriptHistoryTbl, tableScriptId);

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateScriptHistoryTable", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateScriptHistoryTable", e);
			
		}

		return count;
	}

	@Override
	public List<ILConnectionMapping> getDefaultQuery(int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<ILConnectionMapping> query = null;
		;
		try {
			String sql = sqlHelper.getSql("getDefaultQuery");

			query = clientAppDbJdbcTemplate.query(sql, new Object[] { databaseTypeId }, new RowMapper<ILConnectionMapping>() {
				@Override
				public ILConnectionMapping mapRow(ResultSet rs, int i) throws SQLException {
					ILConnectionMapping ilconnectionMapping = new ILConnectionMapping();
					ilconnectionMapping.setConnectionMappingId(rs.getInt("mapping_id"));
					ilconnectionMapping.setiLId(rs.getInt("il_id"));
					Database database = new Database();
					database.setId(rs.getInt("database_type_id"));
					ILConnection ilconnection = new ILConnection();
					ilconnection.setDatabase(database);
					ilconnectionMapping.setiLConnection(ilconnection);
					ilconnectionMapping.setIlSourceName(rs.getString("IL_name"));
					ilconnectionMapping.setiLquery(rs.getString("il_query"));
					ilconnectionMapping.setiLIncrementalUpdateQuery(rs.getString("il_incremental_update_query"));
					ilconnectionMapping.setHistoricalLoad(rs.getString("historical_load"));
					ilconnectionMapping.setMaxDateQuery(rs.getString("max_date_query"));
					database.setIsActive(rs.getBoolean("isActive"));
					ilconnection.setDatabase(database);
					ilconnectionMapping.setiLConnection(ilconnection);

					return ilconnectionMapping;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDefaultQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDefaultQuery()", e);
			
		}
		return query;
	}

	@Override
	public String getIlQueryById(int ilId, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		String ilQuery = null;
		try {
			String sql = sqlHelper.getSql("getIlQueryById");

			ilQuery = clientAppDbJdbcTemplate.query(sql, new Object[] { ilId, databaseTypeId }, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("il_query");
					} else {
						return null;
					}

				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getIlQueryById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getIlQueryById()", e);
			
		}
		return ilQuery;
	}

	@Override
	public String getIlincrementalQueryById(int ilId, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		String ilincrQuery = null;
		try {
			String sql = sqlHelper.getSql("getIlincrementalQueryById");

			ilincrQuery = clientAppDbJdbcTemplate.query(sql, new Object[] { ilId, databaseTypeId }, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("il_incremental_update_query");
					} else {
						return null;
					}

				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getIlincrementalQueryById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getIlincrementalQueryById()", e);
			
		}
		return ilincrQuery;
	}

	@Override
	public String gethistoryLoadById(int ilId, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		String ilQuery = null;
		try {
			String sql = sqlHelper.getSql("gethistoryLoadById");

			ilQuery = clientAppDbJdbcTemplate.query(sql, new Object[] { ilId, databaseTypeId }, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("historical_load");
					} else {
						return null;
					}

				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while gethistoryLoadById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while gethistoryLoadById()", e);
			
		}
		return ilQuery;
	}

	@Override
	public String geMaxDateQueryById(Integer ilId, Integer databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		String maxDateQuery = null;
		try {
			String sql = sqlHelper.getSql("geMaxDateQueryById");

			maxDateQuery = clientAppDbJdbcTemplate.query(sql, new Object[] { ilId, databaseTypeId }, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("max_date_query");
					} else {
						return null;
					}
				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while geMaxDateQueryById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while geMaxDateQueryById()", e);
			
		}
		return maxDateQuery;
	}

	@Override
	public List<ILInfo> getAllILs(int dlId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<ILInfo> ilList = null;
		try {
			String sql = sqlHelper.getSql("getAllILs");
			ilList = clientAppDbJdbcTemplate.query(sql, new Object[] { dlId }, new RowMapper<ILInfo>() {
				public ILInfo mapRow(ResultSet rs, int i) throws SQLException {
					ILInfo iLInfo = new ILInfo();
					iLInfo.setiL_id(rs.getInt("il_id"));
					iLInfo.setiL_name(rs.getString("il_name"));
					iLInfo.setSharedDLIds(rs.getString("dl_id"));
					return iLInfo;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getAllILs", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAllILs", e);
			
		}

		return ilList;
	}

	@Override
	public Map<Integer, String> getNotMappedILsByDBTypeId(int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {

		Map<Integer, String> notmappedILs = null;
		try {
			String sql = sqlHelper.getSql("getNotMappedILsByDBTypeId");
			notmappedILs = clientAppDbJdbcTemplate.query(sql, new Object[] { databaseTypeId }, new ResultSetExtractor<Map<Integer, String>>() {
				@Override
				public Map<Integer, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Map<Integer, String> notMappedIL = new LinkedHashMap<>();
					if (rs == null) {
						return null;
					}

					while (rs.next()) {
						notMappedIL.put(rs.getInt("il_id"), rs.getString("IL_name"));
					}
					return notMappedIL;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getNotMappedILsByDBTypeId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getNotMappedILsByDBTypeId()", e);
			
		}
		return notmappedILs;
	}

	@Override
	public int saveILDefaultQuery(ILConnectionMapping ilconnectionMapping, JdbcTemplate clientAppDbJdbcTemplate) {

		int saveILQuery = 0;
		try {
			String sql = sqlHelper.getSql("saveILDefaultQuery");
			saveILQuery = clientAppDbJdbcTemplate.update(sql,
					new Object[] { ilconnectionMapping.getiLId(), ilconnectionMapping.getiLConnection().getDatabase().getId(), ilconnectionMapping.getiLquery(),
							ilconnectionMapping.getiLIncrementalUpdateQuery(), ilconnectionMapping.getHistoricalLoad(), ilconnectionMapping.getMaxDateQuery(),
							ilconnectionMapping.getModification().getCreatedBy(), ilconnectionMapping.getModification().getCreatedTime()

					});
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while saveILDefaultQuery()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveILDefaultQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveILDefaultQuery()", e);
			
		}
		return saveILQuery;
	}

	@Override
	public ILConnectionMapping editILDefaultQuery(int ilid, int databaseTypeId, JdbcTemplate clientAppDbJdbcTemplate) {
		ILConnectionMapping defaultQueryDetails = null;
		try {
			String sql = sqlHelper.getSql("editILDefaultQuery");
			defaultQueryDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { ilid, databaseTypeId }, new ResultSetExtractor<ILConnectionMapping>() {
				public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {
					ILConnectionMapping ilconnectionMapping = new ILConnectionMapping();
					if (rs == null)
						return null;
					while (rs.next()) {
						ilconnectionMapping.setIlSourceName(rs.getString("IL_name"));
						ilconnectionMapping.setiLquery(rs.getString("il_query"));
						ilconnectionMapping.setiLIncrementalUpdateQuery(rs.getString("il_incremental_update_query"));
						ilconnectionMapping.setHistoricalLoad(rs.getString("historical_load"));
						ilconnectionMapping.setMaxDateQuery(rs.getString("max_date_query"));
					}
					return ilconnectionMapping;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while editILDefaultQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while editILDefaultQuery()", e);
			
		}
		return defaultQueryDetails;
	}

	@Override
	public List<Kpi> getAllKpi(int dlId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<Kpi> kpiList = null;
		try {
			String sql = sqlHelper.getSql("getAllKpisByDlId");
			kpiList = clientAppDbJdbcTemplate.query(sql, new Object[] { dlId }, new RowMapper<Kpi>() {
				public Kpi mapRow(ResultSet rs, int i) throws SQLException {
					Kpi kpiInfo = new Kpi();
					kpiInfo.setKpiName(rs.getString("kpi_name"));
					kpiInfo.setKpiId(rs.getInt("id"));
					return kpiInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getAllKpi", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAllKpi", e);
			
		}

		return kpiList;
	}

	@Override
	public int updateILDefaultQuery(ILConnectionMapping ilconnectionMapping, JdbcTemplate clientAppDbJdbcTemplate) {

		int updateILDefaultQuery = 0;
		try {
			String sql = sqlHelper.getSql("updateILDefaultQuery");
			updateILDefaultQuery = clientAppDbJdbcTemplate.update(sql,
					new Object[] { ilconnectionMapping.getiLquery(), ilconnectionMapping.getiLIncrementalUpdateQuery(), ilconnectionMapping.getHistoricalLoad(),
							ilconnectionMapping.getMaxDateQuery(), ilconnectionMapping.getModification().getModifiedBy(),
							ilconnectionMapping.getModification().getModifiedTime(), ilconnectionMapping.getiLId(),
							ilconnectionMapping.getiLConnection().getDatabase().getId() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateILDefaultQuery()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateILDefaultQuery()", e);
			
		}
		return updateILDefaultQuery;
	}

	@Override
	public int deleteEtlDlJobsmapping(int dlId, String fileName, JdbcTemplate clientAppDbJdbcTemplate) {

		int count = -1;
		try {
			String deleteEtlDlJobsmapping = sqlHelper.getSql("deleteEtlDlJobsmapping");
			count = clientAppDbJdbcTemplate.update(deleteEtlDlJobsmapping, new Object[] { dlId, fileName });

		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteEtlDlJobsmapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteEtlDlJobsmapping", e);
			
		}

		return count;
	}

	@Override
	public List<Database> getDatabase( JdbcTemplate clientAppDbJdbcTemplate) {
		List<Database> dbList = null;
		try {
			String sql = sqlHelper.getSql("getDatabase");
			dbList = clientAppDbJdbcTemplate.query(sql, new RowMapper<Database>() {
				public Database mapRow(ResultSet rs, int i) throws SQLException {
					Database database = new Database();
					database.setId(rs.getInt("id"));
					database.setName(rs.getString("name"));
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setConnectionStringParams(rs.getString("connection_string_params"));
					database.setUrlFormat(rs.getString("url_format"));
					database.setIsActive(rs.getBoolean("isActive"));
					return database;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getDatabase", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDatabase", e);
			
		}
		return dbList;
	}

	@Override
	public Database getDBDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {

		Database databaseDetails = null;

		try {
			String sql = sqlHelper.getSql("getDBDetailsById");
			databaseDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<Database>() {

				@Override
				public Database extractData(ResultSet rs) throws SQLException, DataAccessException {
					Database database = new Database();
					if (rs != null && rs.next()) {
						database.setName(rs.getString("name"));
						database.setDriverName(rs.getString("driver_name"));
						database.setProtocal(rs.getString("protocal"));
						database.setConnectionStringParams(rs.getString("connection_string_params"));
						database.setUrlFormat(rs.getString("url_format"));
						if(rs.getString("connector_jars") != null){
						String [] connectorJars = rs.getString("connector_jars").split(",");
						ArrayList<String> connectorJarsList = new ArrayList<>();
						for(String cjar : connectorJars){
							connectorJarsList.add(cjar);
						}
						database.setConJars(connectorJarsList);
						}
						database.setIsActive(rs.getBoolean("isActive"));
						database.setId(rs.getInt("id"));
						return database;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDBDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDBDetailsById()", e);
			
		}
		return databaseDetails;
	}

	@Override
	public int updateDatabase(Database database, JdbcTemplate clientAppDbJdbcTemplate) {

		int updateDB = 0;
		try {
			String sql = sqlHelper.getSql("updateDatabase");
			updateDB = clientAppDbJdbcTemplate.update(sql, new Object[] {
					database.getName(),
					database.getDriverName(),
					database.getProtocal(),
					database.getConnectionStringParams(),
					database.getUrlFormat(),
					database.getConnectorJars(),
					database.getIsActive(),
					database.getModification().getModifiedBy(),
					database.getModification().getModifiedTime(), database.getId() });

		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while updateDatabase()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateDatabase()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateDatabase()", e);
			
		}
		return updateDB;
	}

	@Override
	public int createDB(Database database, JdbcTemplate clientAppDbJdbcTemplate) {

		int saveDB = 0;
		try {
			final String sql = sqlHelper.getSql("createDB");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });

					ps.setObject(1, database.getName());
					ps.setObject(2, database.getDriverName());
					ps.setObject(3, database.getProtocal());
					ps.setObject(4, database.getUrlFormat());
					ps.setObject(5, database.getConnectorJars());
					ps.setObject(6, database.getIsActive());
					ps.setObject(7, database.getModification().getCreatedBy());
					ps.setObject(8, database.getModification().getCreatedTime());

					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				saveDB = autoIncrement.intValue();
			}
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while createDB()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while createDB()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createDB()", e);

		}
		return saveDB;
	}

	@Override
	public List<Database> getconnector( JdbcTemplate clientAppDbJdbcTemplate) {
		List<Database> connectorList = null;
		try {
			String sql = sqlHelper.getSql("getconnector");
			connectorList = clientAppDbJdbcTemplate.query(sql, new RowMapper<Database>() {
				public Database mapRow(ResultSet rs, int i) throws SQLException {
					Database database = new Database();
					database.setConnector_id(rs.getInt("connector_id"));
					database.setConnectorName(rs.getString("connector_name"));
					database.setName(rs.getString("database_name"));
					database.setIsActive(rs.getBoolean("isActive"));
					database.setIsDefault(rs.getBoolean("is_default"));
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setUrlFormat(rs.getString("url_format"));
					return database;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getconnector", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getconnector", e);
			
		}
		return connectorList;
	}

	@Override
	public Database getConnectorDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {

		Database databaseDetails = null;

		try {
			String sql = sqlHelper.getSql("getConnectorDetailsById");
			databaseDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<Database>() {

				@Override
				public Database extractData(ResultSet rs) throws SQLException, DataAccessException {
					Database database = new Database();
					if (rs != null && rs.next()) {
						database.setConnector_id(rs.getInt("connector_id"));
						database.setConnectorName(rs.getString("name"));
						database.setIsActive(rs.getBoolean("isActive"));
						database.setId(rs.getInt("id"));
						return database;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getConnectorDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getConnectorDetailsById()", e);
			
		}
		return databaseDetails;
	}

	@Override
	public int updateConnector(Database database, JdbcTemplate clientAppDbJdbcTemplate) {

		int updateConnector = 0;
		try {
			String sql = sqlHelper.getSql("updateConnector");
			updateConnector = clientAppDbJdbcTemplate.update(sql, new Object[] { database.getConnectorName(), database.getConnector_id(), database.getIsActive(),
					database.getModification().getModifiedBy(), database.getModification().getModifiedTime(), database.getId() });

		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while updateConnector()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateConnector()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateConnector()", e);
			
		}
		return updateConnector;
	}

	@Override
	public int createConnector(Database database, JdbcTemplate clientAppDbJdbcTemplate) {

		int saveConnector = 0;
		try {
			String sql = sqlHelper.getSql("createConnector");
			saveConnector = clientAppDbJdbcTemplate.update(sql, new Object[] { database.getConnectorName(), database.getConnector_id(), database.getIsActive(),
					database.getModification().getCreatedBy(), database.getModification().getCreatedTime()

			});
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while createNewConnector()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while createConnector()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createConnector()", e);
			
		}
		return saveConnector;
	}

	@Override
	public int createContextParams(ContextParameter contextParameter, JdbcTemplate clientAppDbJdbcTemplate) {

		int saveConnector = 0;
		try {
			String sql = sqlHelper.getSql("createContextParams");
			saveConnector = clientAppDbJdbcTemplate.update(sql,
					new Object[] { contextParameter.getParamName(), contextParameter.getParamval(), contextParameter.getDescription(),
							contextParameter.getIsActive(), contextParameter.getModification().getCreatedBy(),
							contextParameter.getModification().getCreatedTime()

					});
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while createContextParams()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while createContextParams()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createContextParams()", e);
			
		}
		return saveConnector;
	}

	@Override
	public int updateContextParams(ContextParameter contextParameter, JdbcTemplate clientAppDbJdbcTemplate) {

		int saveConnector = 0;
		try {
			String sql = sqlHelper.getSql("updateContextParams");
			saveConnector = clientAppDbJdbcTemplate.update(sql,
					new Object[] { contextParameter.getParamName(), contextParameter.getParamval(), contextParameter.getDescription(),
							contextParameter.getIsActive(), contextParameter.getModification().getModifiedBy(),
							contextParameter.getModification().getModifiedTime(), contextParameter.getParamId()

					});
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while updateContextParams()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateContextParams()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateContextParams()", e);
			
		}
		return saveConnector;
	}

	@Override
	public int deleteDLContextParams(int dLId, JdbcTemplate clientAppDbJdbcTemplate) {
		int delete = 0;
		try {
			String sql = sqlHelper.getSql("deleteDLContextParams");
			delete = clientAppDbJdbcTemplate.update(sql, new Object[] { dLId });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteDLContextParams", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteDLContextParams", e);
			
		}

		return delete;
	}

	@Override
	public List<TableScripts> getTableScriptsByClient(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<TableScripts> tableScriptsList = null;
		try {
			String sql = sqlHelper.getSql("getTableScriptsByClient");
			tableScriptsList = clientAppDbJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<TableScripts>() {
				public TableScripts mapRow(ResultSet rs, int i) throws SQLException {
					TableScripts tableScripts = new TableScripts();
					tableScripts.setId(rs.getInt("id"));
					tableScripts.setScriptName(rs.getString("script_name"));
					tableScripts.setSqlScript("");
					tableScripts.setScriptType(rs.getString("script_type"));
					tableScripts.setVersion(rs.getString("version"));
					tableScripts.setCreated_Date(rs.getString("created_time"));
					tableScripts.setModified_Date(rs.getString("modified_time"));
					return tableScripts;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getTableScriptsByClient", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getTableScriptsByClient", e);
			
		}
		return tableScriptsList;
	}

	@Override
	public int addTableScriptsMapping(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate) {

		int count = -1;
		try {
			String addTableScripts = sqlHelper.getSql("addTableScriptsMapping");
			count = clientAppDbJdbcTemplate.update(addTableScripts,
					new Object[] { tableScripts.getClientId(), tableScripts.getScriptTypeName(), tableScripts.getSchemaName(), tableScripts.getScriptName(),
							tableScripts.getVersion(), tableScripts.getScriptDescription(), tableScripts.getSqlScript(), tableScripts.getIs_Active(),
							tableScripts.getModification().getCreatedBy() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while addTableScriptsMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while addTableScriptsMapping", e);
			
		}

		return count;

	}

	@Override
	public TableScriptsForm getTableScriptsMappingById(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate) {
		TableScriptsForm tableScript = null;
		try {
			String sql = sqlHelper.getSql("getTableScriptsMappingById");
			tableScript = clientAppDbJdbcTemplate.query(sql, new Object[] { tableScripts.getId() }, new ResultSetExtractor<TableScriptsForm>() {
				@Override
				public TableScriptsForm extractData(ResultSet rs) throws SQLException {
					if (rs.next()) {
						TableScriptsForm tableScript = new TableScriptsForm();
						tableScript.setId(rs.getInt("id"));
						tableScript.setClientId(rs.getInt("client_id"));
						tableScript.setScriptTypeName(rs.getString("script_type"));
						tableScript.setSchemaName(rs.getString("target_schema"));
						tableScript.setIs_Active(rs.getBoolean("isActive"));
						tableScript.setScriptName(rs.getString("script_name"));
						tableScript.setSqlScript(rs.getString("sql_script"));
						tableScript.setSchemaName(rs.getString("target_schema"));
						tableScript.setScriptDescription(rs.getString("script_description"));
						tableScript.setVersion(rs.getString("version"));
						return tableScript;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDlClientidMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDlClientidMapping", e);
			
		}
		return tableScript;
	}

	@Override
	public int updateTableScriptsMapping(TableScriptsForm tableScripts, JdbcTemplate clientAppDbJdbcTemplate) {

		int count = -1;
		try {
			String updateTableScriptsMappingSql = sqlHelper.getSql("updateTableScriptsMapping");
			count = clientAppDbJdbcTemplate.update(updateTableScriptsMappingSql,
					new Object[] { tableScripts.getScriptTypeName(), tableScripts.getScriptName(), tableScripts.getVersion(),
							tableScripts.getScriptDescription(), tableScripts.getSqlScript(), tableScripts.getModification().getCreatedBy(),
							tableScripts.getModification().getCreatedTime(), tableScripts.getIs_Active(), tableScripts.getClientId(),
							tableScripts.getSchemaName(), tableScripts.getId() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateTableScripts", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateTableScripts", e);
			
		}

		return count;

	}

	@Override
	public int saveClientTableScriptsMapping(TableScriptsForm tableScriptsForm, JdbcTemplate clientAppDbJdbcTemplate,JdbcTemplate commonJdbcTemplate) {

		List<TableScripts> tableScriptModifiedList = new ArrayList<>();
		List<TableScripts> tableScriptList = tableScriptsForm.getTableScriptList();
		for (TableScripts tableScript : tableScriptList) {
			if (tableScript.isCheckedScript()) {
				tableScriptModifiedList.add(tableScript);
			}
		}

		int[] count = null;
		try {
			String saveIlsSql = sqlHelper.getSql("saveClientTableScriptsMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(saveIlsSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					TableScripts tableScript = tableScriptModifiedList.get(i);
					ps.setInt(1, tableScriptsForm.getClientId());
					ps.setInt(2, tableScript.getId());
					ps.setInt(3, tableScript.getPriority());
					ps.setBoolean(4, tableScript.isCheckedScript());
					ps.setString(5, tableScriptsForm.getModification().getCreatedBy());
					ps.setString(6, tableScriptsForm.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return tableScriptModifiedList.size();
				}
			});
			count = commonJdbcTemplate.batchUpdate(saveIlsSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					TableScripts tableScript = tableScriptModifiedList.get(i);
					ps.setInt(1, tableScriptsForm.getClientId());
					ps.setInt(2, tableScript.getId());
					ps.setInt(3, tableScript.getPriority());
					ps.setBoolean(4, tableScript.isCheckedScript());
					ps.setString(5, tableScriptsForm.getModification().getCreatedBy());
					ps.setString(6, tableScriptsForm.getModification().getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return tableScriptModifiedList.size();
				}
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveEtlDlIlMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveEtlDlIlMapping", e);
			
		}
		return count != null?count.length:0;

	}

	@Override
	public int deleteClientTableScriptsMapping(int clientId, JdbcTemplate clientAppDbJdbcTemplate,JdbcTemplate commonJdbcTemplate) {

		int count = -1;
		String deleteClientTableScriptsMappingSql = null;
		try {
		    deleteClientTableScriptsMappingSql = sqlHelper.getSql("deleteClientTableScriptsMapping");
			count = clientAppDbJdbcTemplate.update(deleteClientTableScriptsMappingSql, new Object[] { clientId });
		    if(count != -1){
			 count = commonJdbcTemplate.update(deleteClientTableScriptsMappingSql, new Object[] { clientId });
			}
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteClientTableScriptsMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteClientTableScriptsMapping", e);
			
		}
		return count;
	}

	@Override
	public List<TableScripts> getTableScriptsMappingByClient(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<TableScripts> tableScriptsList = null;
		try {
			String sql = sqlHelper.getSql("getTableScriptsMappingByClient");
			tableScriptsList = clientAppDbJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<TableScripts>() {
				public TableScripts mapRow(ResultSet rs, int i) throws SQLException {
					TableScripts tableScripts = new TableScripts();
					tableScripts.setId(rs.getInt("script_id"));
					tableScripts.setPriority(rs.getInt("priority"));
					tableScripts.setCheckedScript(rs.getBoolean("isChecked"));
					tableScripts.setExecuted(rs.getBoolean("isExecuted"));
					tableScripts.setScriptName(rs.getString("script_name"));
					tableScripts.setSqlScript("");
					tableScripts.setError(rs.getBoolean("isError"));
					tableScripts.setTargetSchema(rs.getString("target_schema"));
					tableScripts.setScriptType(rs.getString("script_type"));
					tableScripts.setCreated_Date(rs.getString("created_time"));
					tableScripts.setModified_Date(rs.getString("modified_time"));

					return tableScripts;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getTableScriptsByClient", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getTableScriptsByClient", e);
			
		}

		return tableScriptsList;

	}

	@Override
	public List<TableScripts> getSqlScriptByScriptIds(int clientId, List<Integer> scriptIds, JdbcTemplate clientAppDbJdbcTemplate) {

		List<TableScripts> tableScriptsList = null;

		StringBuilder builder = new StringBuilder();

		scriptIds.forEach(value -> {
			builder.append("?,");
		});

		scriptIds.add(0, clientId);

		try {

			String sql = StringUtils.replace(sqlHelper.getSql("getSqlScriptByScriptIds"), "{scriptIds}", builder.deleteCharAt(builder.length() - 1).toString());

			tableScriptsList = clientAppDbJdbcTemplate.query(sql, scriptIds.toArray(), new RowMapper<TableScripts>() {
				public TableScripts mapRow(ResultSet rs, int i) throws SQLException {
					TableScripts tableScripts = new TableScripts();
					tableScripts.setId(rs.getInt("id"));
					tableScripts.setPriority(rs.getInt("priority"));
					tableScripts.setExecuted(rs.getBoolean("isExecuted"));
					tableScripts.setScriptName(rs.getString("script_name"));
					tableScripts.setSqlScript(rs.getString("sql_script"));
					tableScripts.setTargetSchema(rs.getString("target_schema"));
					tableScripts.setScriptType(rs.getString("script_type"));
					return tableScripts;
				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getSqlScriptByScriptIds()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getSqlScriptByScriptIds()", e);
			
		}
		return tableScriptsList;
	}

	@Override
	public int updateTableScriptsMappingIsExecuted(int clientId, int scriptId, boolean isExecuted, Modification modification, JdbcTemplate clientAppDbJdbcTemplate,JdbcTemplate commonJdbcTemplate) {
		int count = -1;
		try {
			String updateTableScriptsMappingSql = sqlHelper.getSql("updateTableScriptsMappingIsExecuted");
			count = clientAppDbJdbcTemplate.update(updateTableScriptsMappingSql,
					new Object[] { isExecuted, modification.getCreatedBy(), modification.getCreatedTime(), scriptId, clientId });

			if(count != -1){
			count = commonJdbcTemplate.update(updateTableScriptsMappingSql,
				    new Object[] { isExecuted, modification.getCreatedBy(), modification.getCreatedTime(), scriptId, clientId });
			}
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateTableScriptsMappingIsExecuted", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateTableScriptsMappingIsExecuted", e);
			
		}

		return count;
	}

	@Override
	public String getJobFilesByDLId(int dLId, JdbcTemplate clientAppDbJdbcTemplate) {

		String jobfileNames = null;
		try {
			String sql = sqlHelper.getSql("getJobFilesByDLId");

			jobfileNames = clientAppDbJdbcTemplate.query(sql, new Object[] { dLId }, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("dependency_jars");
					} else {
						return null;
					}

				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getJobFilesByDLId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getJobFilesByDLId()", e);
			
		}
		return jobfileNames;

	}

	@Override
	public int getExecutedtableScriptCount(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		int executedCount = 0;
		try {
			String sql = sqlHelper.getSql("getExecutedtableScriptCount");
			executedCount = clientAppDbJdbcTemplate.queryForObject(sql, new Object[] { scriptId }, new RowMapper<Integer>() {
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs == null)
						return null;
					return rs.getInt(1);
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getExistContextParameter()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getExistContextParameter()", e);
			
		}
		return executedCount;
	}

	@Override
	public int getMappedTableScriptCount(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		int mappedCount = 0;
		try {
			String sql = sqlHelper.getSql("getMappedTableScriptCount");
			mappedCount = clientAppDbJdbcTemplate.queryForObject(sql, new Object[] { scriptId }, new RowMapper<Integer>() {
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					if (rs == null)
						return null;
					return rs.getInt(1);
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getMappedTableScriptCount()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getMappedTableScriptCount()", e);
			
		}
		return mappedCount;
	}

	@Override
	public void updateTableScriptsMappingIsNotExecutedErrorMsg(int clientId, int scriptId, String errorMsg, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		try {
			String updateTableScriptsMappingSql = sqlHelper.getSql("updateTableScriptsMappingIsNotExecutedErrorMsg");
			clientAppDbJdbcTemplate.update(updateTableScriptsMappingSql,
					new Object[] { clientId, scriptId, errorMsg, modification.getCreatedBy(), modification.getCreatedTime() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateTableScriptsMappingIsNotExecutedErrorMsg", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateTableScriptsMappingIsNotExecutedErrorMsg", e);
			
		}

	}

	@Override
	public String getTableScriptsMappingIsNotExecutedErrorMsg(int clientId, int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {

		String errorMsg = null;
		try {
			String sql = sqlHelper.getSql("getTableScriptsMappingIsNotExecutedErrorMsg");

			errorMsg = clientAppDbJdbcTemplate.query(sql, new Object[] { clientId, scriptId }, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("error_msg");
					} else {
						return null;
					}

				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getTableScriptsMappingIsNotExecutedErrorMsg()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getTableScriptsMappingIsNotExecutedErrorMsg()", e);
			
		}
		return errorMsg;

	}

	@Override
	public int updateTableScriptsMappingIsError(int clientId, int scriptId, boolean isError, Modification modification, JdbcTemplate clientAppDbJdbcTemplate,JdbcTemplate commonJdbcTemplate) {
		int count = -1;
		try {
			String updateTableScriptsMappingIsErrorSql = sqlHelper.getSql("updateTableScriptsMappingIsError");
			count = clientAppDbJdbcTemplate.update(updateTableScriptsMappingIsErrorSql,
					new Object[] { isError, modification.getCreatedBy(), modification.getCreatedTime(), scriptId, clientId });
            if(count != -1){
            count = commonJdbcTemplate.update(updateTableScriptsMappingIsErrorSql,
    				new Object[] { isError, modification.getCreatedBy(), modification.getCreatedTime(), scriptId, clientId });
            }
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateTableScriptsMappingIsError", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateTableScriptsMappingIsError", e);
			
		}

		return count;
	}

	@Override
	public int saveDefaultTemplateMasterMappingData(int templateId, String mappingType, List<Integer> masterIds, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		try {
			String updateSql = sqlHelper.getSql("saveDefaultTemplateMasterMappingData");
			count = clientAppDbJdbcTemplate.batchUpdate(updateSql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					int masterId = masterIds.get(i);
					ps.setInt(1, templateId);
					ps.setString(2, mappingType);
					ps.setInt(3, masterId);
					ps.setString(4, modification.getCreatedBy());
					ps.setString(5, modification.getCreatedTime());
				}

				@Override
				public int getBatchSize() {
					return masterIds.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveDefaultTemplateMasterMappingData()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveDefaultTemplateMasterMappingData()", e);
			
		}
		return count != null?count.length:0;
	}

	@Override
	public void updateInstantTableScriptExecution(TableScriptsForm tableScriptsForm, String status, String statusMessage, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {

		try {
			String updateTableScriptsMappingSql = sqlHelper.getSql("updateInstantTableScriptExecution");
			clientAppDbJdbcTemplate.update(updateTableScriptsMappingSql,
					new Object[] { tableScriptsForm.getClientId(), tableScriptsForm.getSchemaName(), tableScriptsForm.getScriptDescription(),
							tableScriptsForm.getSqlScript(), status.equalsIgnoreCase("success") ? true : false, statusMessage, modification.getCreatedBy(),
							modification.getCreatedTime() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while updateInstantTableScriptExecution", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateInstantTableScriptExecution", e);
			
		}

	}

	@Override
	public List<TableScriptsForm> getPreviousExecutedScripts(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<TableScriptsForm> prevoiusExcutetedScriptList = null;
		try {
			String sql = sqlHelper.getSql("getPreviousExecutedScripts");
			prevoiusExcutetedScriptList = clientAppDbJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<TableScriptsForm>() {
				public TableScriptsForm mapRow(ResultSet rs, int i) throws SQLException {
					TableScriptsForm tableScripts = new TableScriptsForm();
					Modification modification = new Modification();
					tableScripts.setId(rs.getInt("id"));
					tableScripts.setClientId(rs.getInt("client_id"));
					tableScripts.setScriptDescription(rs.getString("script_description"));
					tableScripts.setSqlScript(rs.getString("sql_script"));
					tableScripts.setExecution_status(rs.getBoolean("execution_status"));
					tableScripts.setExecution_status_msg(rs.getString("execution_status_msg"));
					modification.setCreatedBy(rs.getString("created_by"));
					modification.setCreatedTime(rs.getString("created_time"));
					tableScripts.setModification(modification);
					return tableScripts;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getPreviousExecutedScripts", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getPreviousExecutedScripts", e);
			
		}

		return prevoiusExcutetedScriptList;
	}

	@Override
	public String getInstantTableScriptsIsNotExecutedErrorMsg(int id, int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		String errorMsg = null;
		try {
			String sql = sqlHelper.getSql("getInstantTableScriptsIsNotExecutedErrorMsg");

			errorMsg = clientAppDbJdbcTemplate.query(sql, new Object[] { id, clientId }, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("execution_status_msg");
					} else {
						return null;
					}

				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getInstantTableScriptsIsNotExecutedErrorMsg()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getInstantTableScriptsIsNotExecutedErrorMsg()", e);
			
		}
		return errorMsg;
	}

	@Override
	public String getPreviousTableScriptView(int id, int clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		String errorMsg = null;
		try {
			String sql = sqlHelper.getSql("getPreviousTableScriptView");

			errorMsg = clientAppDbJdbcTemplate.query(sql, new Object[] { id, clientId }, new ResultSetExtractor<String>() {

				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("sql_script");
					} else {
						return null;
					}

				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getPreviousTableScriptView()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getPreviousTableScriptView()", e);
			
		}
		return errorMsg;
	}

	@Override
	public List<DefaultTemplates> getAllDefaultTemplatesInfo( JdbcTemplate clientAppDbJdbcTemplate) {
		List<DefaultTemplates> defaultTemplates = null;
		try {
			String sql = sqlHelper.getSql("getAllDefaultTemplatesInfo");
			defaultTemplates = clientAppDbJdbcTemplate.query(sql, new RowMapper<DefaultTemplates>() {

				@Override
				public DefaultTemplates mapRow(ResultSet rs, int rowNum) throws SQLException {
					DefaultTemplates defaultTemplates = new DefaultTemplates();
					defaultTemplates.setTemplateId(rs.getInt("id"));
					defaultTemplates.setTemplateName(rs.getString("template_name"));
					defaultTemplates.setDescription(rs.getString("description"));
					defaultTemplates.setActive(rs.getBoolean("is_active"));
					return defaultTemplates;
				}
			});

		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAllDefaultTemplatesInfo()", e);
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAllDefaultTemplatesInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		}

		return defaultTemplates;
	}

	@Override
	public int createDefaultTemplate(DefaultTemplates defaultTemplates, JdbcTemplate clientAppDbJdbcTemplate) {
		int templateId = -1;
		try {
			String sql = sqlHelper.getSql("createDefaultTemplate");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, defaultTemplates.getTemplateName());
					ps.setString(2, defaultTemplates.getDescription());
					ps.setBoolean(3, defaultTemplates.isActive());
					ps.setBoolean(4, defaultTemplates.isTrialTemplate());
					ps.setString(5, defaultTemplates.getModification().getCreatedBy());
					ps.setString(6, defaultTemplates.getModification().getCreatedTime());
					return ps;
				}
			}, keyHolder);

			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				templateId = autoIncrement.intValue();
			}
		} catch (DuplicateKeyException ae) {
			LOGGER.error("error while createDefaultTemplate()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOGGER.error("error while createDefaultTemplate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createDefaultTemplate()", e);
			
		}
		return templateId;
	}

	@Override
	public DataResponse getDefaultTemplateMasterMappedData(int templateId, String mappingType, JdbcTemplate clientAppDbJdbcTemplate) {
		DataResponse dataResponse = new DataResponse();
		try {
			String sql = null;
			if (mappingType.equals("vertical")) {
				sql = sqlHelper.getSql("getDefaultTemplateVerticalMappedData");
				List<Industry> verticals = clientAppDbJdbcTemplate.query(sql, new RowMapper<Industry>() {
					@Override
					public Industry mapRow(ResultSet rs, int arg1) throws SQLException {
						Industry industry = new Industry();
						industry.setId(rs.getInt("id"));
						industry.setName(rs.getString("name"));
						industry.setDescription(rs.getString("description"));
						industry.setIsDefault(rs.getBoolean("default"));
						return industry;
					}
				}, templateId, mappingType);
				dataResponse.setObject(verticals);
			} else if (mappingType.equals("connector")) {
				sql = sqlHelper.getSql("getDefaultTemplateConnectorMappedData");
				List<Database> connectors = clientAppDbJdbcTemplate.query(sql, new RowMapper<Database>() {
					@Override
					public Database mapRow(ResultSet rs, int i) throws SQLException {
						Database database = new Database();
						database.setConnector_id(rs.getInt("connector_id"));
						database.setConnectorName(rs.getString("connector_name"));
						database.setName(rs.getString("database_name"));
						database.setIsDefault(rs.getBoolean("default"));
						database.setDriverName(rs.getString("driver_name"));
						database.setProtocal(rs.getString("protocal"));
						database.setUrlFormat(rs.getString("url_format"));
						return database;
					}
				}, templateId, mappingType);
				dataResponse.setObject(connectors);
			} else if (mappingType.equals("dl")) {
				sql = sqlHelper.getSql("getDefaultTemplateDLsMappedData");
				List<DLInfo> dlList = clientAppDbJdbcTemplate.query(sql, new RowMapper<DLInfo>() {
					public DLInfo mapRow(ResultSet rs, int i) throws SQLException {
						DLInfo dLInfo = new DLInfo();
						dLInfo.setdL_id(rs.getInt("DL_id"));
						dLInfo.setdL_name(rs.getString("DL_name"));
						dLInfo.setdL_table_name(rs.getString("dl_table_name"));
						dLInfo.setDescription(rs.getString("description"));
						dLInfo.setIsDefault(rs.getBoolean("default"));
						dLInfo.setIndustry(new Industry(rs.getInt("industry_id")));
						return dLInfo;
					}
				}, templateId, mappingType);
				dataResponse.setObject(dlList);
			} else if (mappingType.equals("tablescript")) {
				sql = sqlHelper.getSql("getDefaultTemplateTblScriptsMappedData");
				List<TableScriptsForm> tableScriptsList = clientAppDbJdbcTemplate.query(sql, new RowMapper<TableScriptsForm>() {
					public TableScriptsForm mapRow(ResultSet rs, int i) throws SQLException {
						TableScriptsForm tableScripts = new TableScriptsForm();
						tableScripts.setId(rs.getInt("id"));
						tableScripts.setScriptTypeName(rs.getString("script_type"));
						tableScripts.setSchemaName(rs.getString("target_schema"));
						tableScripts.setScriptName(rs.getString("script_name"));
						tableScripts.setScriptDescription(rs.getString("script_description"));
						tableScripts.setVersion(rs.getString("version"));
						tableScripts.setIsDefault(rs.getBoolean("default"));
						return tableScripts;
					}
				}, templateId, mappingType);
				dataResponse.setObject(tableScriptsList);
			} else if(mappingType.equals("webservice")){
				sql = sqlHelper.getSql("getDefaultTemplateWebServicesMappedData");
				List<WebServiceTemplateMaster> webServiceList = clientAppDbJdbcTemplate.query(sql, new RowMapper<WebServiceTemplateMaster>() {
					@Override
					public WebServiceTemplateMaster mapRow(ResultSet rs, int rowNum) throws SQLException {
						WebServiceTemplateMaster ws = new WebServiceTemplateMaster();
						ws.setId(rs.getLong("id"));
						ws.setWebServiceName(rs.getString("web_service_name"));
						ws.setIsDefault(rs.getBoolean("default"));
						WebServiceAuthenticationTypes auth = new WebServiceAuthenticationTypes();
						auth.setAuthenticationType(rs.getString("authentication_type"));
						ws.setWebServiceAuthenticationTypes(auth);
						return ws;
					}
				}, templateId, mappingType);
				dataResponse.setObject(webServiceList);
			} else if(mappingType.equals("currencies")){
				sql = sqlHelper.getSql("getDefaultTemplateCurrenciesMappedData");
				List<ClientCurrencyMapping> currencyList = clientAppDbJdbcTemplate.query(sql, new RowMapper<ClientCurrencyMapping>() {
					@Override
					public ClientCurrencyMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
						ClientCurrencyMapping currencyMap = new ClientCurrencyMapping();
						
						currencyMap.setCurrencyType(rs.getString("currency_type"));
						currencyMap.setCurrencyName(rs.getString("dashboard_currency"));
						currencyMap.setBasecurrencyCode(rs.getString("accountCurrencyCode"));
						currencyMap.setAccountingCurrencyCode(rs.getString("otherCurrencyCode"));
						
						return currencyMap;
					}
				}, templateId);
				dataResponse.setObject(currencyList);
			}else if(mappingType.equals("s3Bucket")){
				sql = sqlHelper.getSql("getDefaultTemplateS3BucketMappedData");
				int bucketId = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<Integer>() {
					@Override
					public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
						if(rs.next()){
							return rs.getInt("master_id");
						}
						return 0;
					}			
				}, templateId, mappingType);
				dataResponse.setObject(bucketId);
				
			} 
			else if(mappingType.equals("schedularMaster")){
				sql = sqlHelper.getSql("getDefaultTemplateSchedularMasterData");
				List<Integer> schedularMasterIds = clientAppDbJdbcTemplate.query(sql, new RowMapper<Integer>() {
					@Override
					public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getInt("master_id");
					}
				}, templateId, mappingType);
				dataResponse.setObject(schedularMasterIds);
			}
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDefaultTemplateMasterMappedData", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDefaultTemplateMasterMappedData", e);
			
		}
		return dataResponse;
	}

	@Override
	public void deleteDefaultTemplateMasterMappedData(int templateId, String mappingType, JdbcTemplate clientAppDbJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteDefaultTemplateMasterMappedData");
			clientAppDbJdbcTemplate.update(sql, new Object[] { templateId, mappingType });
		} catch (DataAccessException ae) {
			LOGGER.error("error while deleteDefaultTemplateMasterMappedData", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteDefaultTemplateMasterMappedData", e);
			
		}

	}

	@Override
	public DefaultTemplates getDefaultTemplateInfoById(int templateId, JdbcTemplate clientAppDbJdbcTemplate) {
		DefaultTemplates defaultTemplates = null;
		try {
			String sql = sqlHelper.getSql("getDefaultTemplateInfoById");
			defaultTemplates = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<DefaultTemplates>() {

				@Override
				public DefaultTemplates extractData(ResultSet rs) throws SQLException, DataAccessException {
					DefaultTemplates defaultTemplate = new DefaultTemplates();
					if (rs == null) {
						return null;
					}
					while (rs.next()) {
						defaultTemplate.setTemplateId(rs.getInt("id"));
						defaultTemplate.setTemplateName(rs.getString("template_name"));
						defaultTemplate.setDescription(rs.getString("description"));
						defaultTemplate.setActive(rs.getBoolean("is_active"));
						defaultTemplate.setTrialTemplate(rs.getBoolean("trial_template"));
					}
					return defaultTemplate;
				}

			}, templateId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getDefaultTemplateInfoById", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDefaultTemplateInfoById", e);
			
		}
		return defaultTemplates;
	}

	@Override
	public void deleteClientWSMappings(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteClientWSMappings");
			clientAppDbJdbcTemplate.update(sql, new Object[] { clientId });
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while deleteClientWSMappings", e);
			
		}
	}

	@Override
	public int updateWsILMappingDetails(WebServiceILMapping webServiceILMapping, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] update = null;
		try {
			String sql = sqlHelper.getSql("updateWsILMappingDetails");
			update = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, webServiceILMapping.getWebServiceApis().get(i).getApiName());
					ps.setString(2, webServiceILMapping.getWebServiceApis().get(i).getApiUrl());
					ps.setBoolean(3, webServiceILMapping.getWebServiceApis().get(i).getBaseUrlRequired());
					ps.setString(4, webServiceILMapping.getWebServiceApis().get(i).getApiMethodType());
					ps.setString(5, webServiceILMapping.getWebServiceApis().get(i).getApiPathParams());
					ps.setString(6, webServiceILMapping.getWebServiceApis().get(i).getApiRequestParams());
					ps.setString(7, webServiceILMapping.getWebServiceApis().get(i).getResponseObjectName());
					ps.setString(8, webServiceILMapping.getModification().getModifiedBy());
					ps.setString(9, webServiceILMapping.getModification().getModifiedTime());
					ps.setString(10, webServiceILMapping.getWebServiceApis().get(i).getApiBodyParams());
					ps.setBoolean(11, webServiceILMapping.getWebServiceApis().get(i).getIncrementalUpdate());
					ps.setString(12, webServiceILMapping.getWebServiceApis().get(i).getIncrementalUpdateparamdata());
					ps.setString(13, webServiceILMapping.getWebServiceApis().get(i).getResponseColumnObjectName());
					ps.setBoolean(14, webServiceILMapping.getWebServiceApis().get(i).getPaginationRequired());
					ps.setString(15, webServiceILMapping.getWebServiceApis().get(i).getPaginationType());
					ps.setString(16, webServiceILMapping.getWebServiceApis().get(i).getPaginationRequestParamsData());
					ps.setString(17, webServiceILMapping.getWebServiceApis().get(i).getSoapBodyElement());
					ps.setString(18, webServiceILMapping.getWebServiceApis().get(i).getDefaultMapping());
					ps.setLong(19, webServiceILMapping.getWebServiceApis().get(i).getId());
					
				}

				@Override
				public int getBatchSize() {
					return webServiceILMapping.getWebServiceApis().size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("in updateWsILMappingDetails()",ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("in updateWsILMappingDetails()", e);
		}
		return update != null  ? update.length:0;
	}

	@Override
	public int deleteWSILMappingDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		int delete = 0;
		try {
			String sql = sqlHelper.getSql("deleteWSILMappingDetailsById");
			delete = clientAppDbJdbcTemplate.update(sql, new Object[] { id });
		} catch (DataAccessException ae) {
			LOGGER.error("in deleteWSILMappingDetailsById()",ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("in deleteWSILMappingDetailsById()", e);
		}
		return delete;
	}

	@Override
	public List<ILInfo> getILsByWSMappingId(int wsTemplateId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ILInfo> ilList = null;
		try {
			String sql = sqlHelper.getSql("getILsByWSMappingId");
			ilList = clientAppDbJdbcTemplate.query(sql, new RowMapper<ILInfo>() {
				public ILInfo mapRow(ResultSet rs, int i) throws SQLException {
					ILInfo iLInfo = new ILInfo();
					iLInfo.setiL_id(rs.getInt("IL_id"));
					iLInfo.setiL_name(rs.getString("IL_name"));
					iLInfo.setiLType(rs.getString("il_type"));
					iLInfo.setiL_table_name(rs.getString("il_table_name"));
					iLInfo.setXref_il_table_name(rs.getString("xref_il_table_name"));
					iLInfo.setDescription(rs.getString("description"));
					iLInfo.setVersion(rs.getString("version"));
					iLInfo.setWsApiCount(rs.getInt("api_count"));
					return iLInfo;
				}
			}, wsTemplateId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getILsByWSMappingId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getILsByWSMappingId", e);
		}
		return ilList;
	}

	@Override
	public int updateDefaultTemplate(DefaultTemplates defaultTemplates, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = -1;
		try {
			String updateDefaultTemp = sqlHelper.getSql("updateDefaultTemplate");
			count = clientAppDbJdbcTemplate.update(updateDefaultTemp,
					new Object[] { defaultTemplates.getTemplateName(), defaultTemplates.getDescription(), defaultTemplates.isActive(),
							defaultTemplates.isTrialTemplate(), defaultTemplates.getModification().getModifiedBy(),
							defaultTemplates.getModification().getModifiedTime(), defaultTemplates.getTemplateId()

					});
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateDefaultTemplate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateDefaultTemplate()", e);
		}
		return count;
	}

	@Override
	public int saveGeneralSettings(GeneralSettings generalSettings, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String tempGeneralSettings = sqlHelper.getSql("saveGeneralSettings");
			count = clientAppDbJdbcTemplate.update(tempGeneralSettings,
					new Object[] { generalSettings.getClientId(), generalSettings.getFlatFile(), generalSettings.getDatabase(), generalSettings.getWebService(),
							generalSettings.getFileSize(), generalSettings.getModification().getCreatedBy(),
							generalSettings.getModification().getCreatedTime() });

		} catch (DataAccessException ae) {
			LOGGER.error("error while saveGeneralSettings", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveGeneralSettings", e);
			
		}

		return count;
	}

	@Override
	public GeneralSettings getSettingsInfoByID(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		GeneralSettings generalInfo = null;
		try {
			String sql = sqlHelper.getSql("getSettingsInfoByID");
			generalInfo = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<GeneralSettings>() {

				@Override
				public GeneralSettings extractData(ResultSet rs) throws SQLException, DataAccessException {

					if (rs.next()) {
						GeneralSettings generalSettingsInfo = new GeneralSettings();
						generalSettingsInfo.setId(rs.getInt("id"));
						generalSettingsInfo.setClientId(rs.getInt("clientId"));
						generalSettingsInfo.setFlatFile(rs.getString("flat_file"));
						generalSettingsInfo.setDatabase(rs.getString("database_settings"));
						generalSettingsInfo.setWebService(rs.getString("webservice"));
						generalSettingsInfo.setFileSize(rs.getLong("file_size"));
						return generalSettingsInfo;
					} else {
						return null;
					}
				}

			}, clientId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getSettingsInfoByID", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getSettingsInfoByID", e);
			
		}
		return generalInfo;
	}

	@Override
	public int updateGeneralSettings(GeneralSettings generalSettings, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String updateGenSettings = sqlHelper.getSql("updateGeneralSettings");
			count = clientAppDbJdbcTemplate.update(updateGenSettings,
					new Object[] { generalSettings.getFlatFile(), generalSettings.getDatabase(), generalSettings.getWebService(), generalSettings.getFileSize(),
							generalSettings.getModification().getModifiedBy(), generalSettings.getModification().getModifiedTime(),
							generalSettings.getClientId(), generalSettings.getId() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateDefaultTemplate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateDefaultTemplate()", e);
			
		}
		return count;
	}

	@Override
	public int saveclientConfigSettings(ClientConfigurationSettings clientConfigurationSettings, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String clientConfigSettings = sqlHelper.getSql("saveclientConfigSettings");
			count = clientAppDbJdbcTemplate.update(clientConfigSettings,
					new Object[] { clientConfigurationSettings.getClientId(), clientConfigurationSettings.getFrom(), clientConfigurationSettings.getPassword(),
							clientConfigurationSettings.getTo(), clientConfigurationSettings.getCc(), clientConfigurationSettings.getBcc(),
							clientConfigurationSettings.getReplyTo(), clientConfigurationSettings.getSmtpHost(),
							clientConfigurationSettings.getSmtpFactoryPort(), clientConfigurationSettings.getSmtpPort(),
							clientConfigurationSettings.getModification().getCreatedBy(), clientConfigurationSettings.getModification().getCreatedTime(),
							clientConfigurationSettings.getMacAddress() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveclientConfigSettings()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveclientConfigSettings()", e);
			
		}
		return count;
	}

	@Override
	public ClientConfigurationSettings getConfigSettingsInfoByID(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		ClientConfigurationSettings generalInfo = null;
		try {
			String sql = sqlHelper.getSql("getConfigSettingsInfoByID");
			generalInfo = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<ClientConfigurationSettings>() {

				@Override
				public ClientConfigurationSettings extractData(ResultSet rs) throws SQLException, DataAccessException {

					if (rs.next()) {
						ClientConfigurationSettings clientConfigSettingsInfo = new ClientConfigurationSettings();
						clientConfigSettingsInfo.setId(rs.getInt("id"));
						clientConfigSettingsInfo.setFrom(rs.getString("mail_from"));
						clientConfigSettingsInfo.setPassword(rs.getString("password"));
						clientConfigSettingsInfo.setTo(rs.getString("mail_to"));
						clientConfigSettingsInfo.setCc(rs.getString("cc"));
						clientConfigSettingsInfo.setBcc(rs.getString("bcc"));
						clientConfigSettingsInfo.setReplyTo(rs.getString("reply_to"));
						clientConfigSettingsInfo.setSmtpHost(rs.getString("smtp_host"));
						clientConfigSettingsInfo.setSmtpFactoryPort(rs.getInt("socket_factory_port"));
						clientConfigSettingsInfo.setSmtpPort(rs.getInt("smtp_port"));
						clientConfigSettingsInfo.setMacAddress(rs.getString("mac_address"));
						return clientConfigSettingsInfo;
					} else {
						return null;
					}
				}

			}, clientId);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getConfigSettingsInfoByID", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getConfigSettingsInfoByID", e);
			
		}
		return generalInfo;
	}

	@Override
	public int updateclientConfigSettings(ClientConfigurationSettings clientConfigurationSettings, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String updateGenSettings = sqlHelper.getSql("updateclientConfigSettings");
			count = clientAppDbJdbcTemplate.update(updateGenSettings,
					new Object[] { clientConfigurationSettings.getFrom(), clientConfigurationSettings.getPassword(), clientConfigurationSettings.getTo(),
							clientConfigurationSettings.getCc(), clientConfigurationSettings.getBcc(), clientConfigurationSettings.getReplyTo(),
							clientConfigurationSettings.getSmtpHost(), clientConfigurationSettings.getSmtpFactoryPort(),
							clientConfigurationSettings.getSmtpPort(), clientConfigurationSettings.getModification().getModifiedBy(),
							clientConfigurationSettings.getModification().getModifiedTime(), clientConfigurationSettings.getClientId(),
							clientConfigurationSettings.getMacAddress(), clientConfigurationSettings.getId() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateclientConfigSettings()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateclientConfigSettings()", e);
			
		}
		return count;
	}

	@Override
	public List<ServerConfigurations> getAllServerConfigurations( JdbcTemplate clientAppDbJdbcTemplate) {

		List<ServerConfigurations> serverConfigList = null;

		try {
			String sql = sqlHelper.getSql("getAllServerConfigurations");

			serverConfigList = clientAppDbJdbcTemplate.query(sql, new RowMapper<ServerConfigurations>() {

				@Override
				public ServerConfigurations mapRow(ResultSet rs, int rowNum) throws SQLException {
					ServerConfigurations sc = new ServerConfigurations();
					sc.setId(rs.getInt("id"));
					sc.setServerName(rs.getString("name"));
					sc.setShortName(rs.getString("short_name"));
					sc.setDescription(rs.getString("description"));
					sc.setIpAddress(rs.getString("ip_address"));
					sc.setPortNumber(rs.getString("port_number"));
					sc.setMinidwSchemaName(rs.getString("minidw_schema_name"));
					sc.setAnvizentSchemaName(rs.getString("anvizent_schema_name"));
					sc.setUserName(rs.getString("user_name"));
					sc.setServerPassword(rs.getString("password"));
					sc.setActiveStatus(rs.getBoolean("is_active"));
					sc.setClientDbDetailsEndPoint(rs.getString("client_db_details_end_point"));
					return sc;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getAllServerConfigurations()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAllServerConfigurations()", e);
			
		}

		return serverConfigList;
	}

	@Override
	public int saveServerConfigurationDetails(ServerConfigurations serverConfigurations, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {

		int save = 0;
		try {
			String sql = sqlHelper.getSql("saveServerConfigurationDetails");
			save = clientAppDbJdbcTemplate.update(sql,
					new Object[] { serverConfigurations.getServerName(), serverConfigurations.getShortName(), serverConfigurations.getDescription(),
							serverConfigurations.getIpAddress(), serverConfigurations.getPortNumber(), serverConfigurations.getMinidwSchemaName(),
							serverConfigurations.getAnvizentSchemaName(), serverConfigurations.getUserName(), serverConfigurations.getServerPassword(),
							serverConfigurations.isActiveStatus(),serverConfigurations.getClientDbDetailsEndPoint() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveServerConfigurationDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveServerConfigurationDetails()", e);
			
		}
		return save;
	}

	@Override
	public int updateServerConfigurationDetails(ServerConfigurations serverConfigurations, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {

		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateServerConfigurationDetails");
			update = clientAppDbJdbcTemplate.update(sql,
					new Object[] { serverConfigurations.getServerName(), serverConfigurations.getShortName(), serverConfigurations.getDescription(),
							serverConfigurations.getIpAddress(), serverConfigurations.getPortNumber(), serverConfigurations.getMinidwSchemaName(),
							serverConfigurations.getAnvizentSchemaName(), serverConfigurations.getUserName(), serverConfigurations.getServerPassword(),
							serverConfigurations.isActiveStatus(),serverConfigurations.getClientDbDetailsEndPoint(), serverConfigurations.getId() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateServerConfigurationDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateServerConfigurationDetails()", e);
			
		}
		return update;
	}

	@Override
	public ServerConfigurations getServerConfigurationDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {

		ServerConfigurations sc = null;
		try {
			String sql = sqlHelper.getSql("getServerConfigurationDetailsById");
			sc = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<ServerConfigurations>() {

				@Override
				public ServerConfigurations extractData(ResultSet rs) throws SQLException, DataAccessException {
					ServerConfigurations sc = new ServerConfigurations();
					if (rs == null)
						return null;

					while (rs.next()) {
						sc.setId(rs.getInt("id"));
						sc.setServerName(rs.getString("name"));
						sc.setShortName(rs.getString("short_name"));
						sc.setDescription(rs.getString("description"));
						sc.setIpAddress(rs.getString("ip_address"));
						sc.setPortNumber(rs.getString("port_number"));
						sc.setMinidwSchemaName(rs.getString("minidw_schema_name"));
						sc.setAnvizentSchemaName(rs.getString("anvizent_schema_name"));
						sc.setUserName(rs.getString("user_name"));
						sc.setServerPassword(rs.getString("password"));
						sc.setActiveStatus(rs.getBoolean("is_active"));
						sc.setClientDbDetailsEndPoint(rs.getString("client_db_details_end_point"));
					}
					return sc;
				}

			}, id);
		} catch (DataAccessException ae) {
			LOGGER.error("error while getServerConfigurationDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getServerConfigurationDetailsById()", e);
			
		}

		return sc;
	}

	@Override
	public Map<Integer, String> getUsersByClientId(int clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		Map<Integer, String> users = null;

		try {

			String sql = sqlHelper.getSql("getUsersByClientId");

			users = clientAppDbJdbcTemplate.query(sql, new Object[] { clientId }, new ResultSetExtractor<Map<Integer, String>>() {

				@Override
				public Map<Integer, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs == null)
						return null;

					Map<Integer, String> users = new LinkedHashMap<>();

					while (rs.next()) {
						users.put(rs.getInt(1), rs.getString(2));
					}

					return users;
				}
			});

		} catch (Exception e) {
			LOGGER.error("Error while reading getAllWebServices ", e);
		}

		return users;

	}

	@Override
	public List<Package> userPackageList(int clientUserId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<Package> packageList = null;
		try {
			String sql = sqlHelper.getSql("getAllUserPackages");
			packageList = clientAppDbJdbcTemplate.query(sql, new Object[] { clientUserId }, new RowMapper<Package>() {
				public Package mapRow(ResultSet rs, int i) throws SQLException {
					Package userPackage = new Package();
					userPackage.setPackageId(rs.getInt("package_id"));
					userPackage.setPackageName(rs.getString("package_name"));
					userPackage.setDescription(rs.getString("description"));
					userPackage.setIndustry(new Industry(rs.getInt("industryId"), rs.getString("industryName")));
					userPackage.setIsStandard(rs.getBoolean("isStandard"));
					userPackage.setIsActive(rs.getBoolean("isActive"));
					userPackage.setIsScheduled(rs.getBoolean("isScheduled"));
					userPackage.setIsAdvanced(rs.getBoolean("isAdvanced"));
					userPackage.setIsMapped(rs.getBoolean("isMapped"));
					userPackage.setScheduleStatus(rs.getString("schedule_status"));
					userPackage.setIsClientDbTables(rs.getBoolean("isClientDbTables"));
					userPackage.setTrailingMonths(rs.getInt("trailing_months"));
					userPackage.setFilesHavingSameColumns(rs.getBoolean("files_having_same_columns"));
					Modification modification = new Modification();
					modification.setCreatedTime(rs.getString("created_time"));
					userPackage.setModification(modification);
					return userPackage;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getUserPackages()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getUserPackages()", e);
			
		}

		return packageList;
	}
	
	@Override
	public int saveCurrencyIntegration(CurrencyIntegration currencyIntegration, JdbcTemplate clientAppDbJdbcTemplate) {
		int count = 0;
		try {
			String tempCurrencyIntegration = sqlHelper.getSql("saveCurrencyIntegration");
			count = clientAppDbJdbcTemplate.update(tempCurrencyIntegration,
					new Object[] { 
									currencyIntegration.getApiUrl(),
									currencyIntegration.getAccessKey(),
									currencyIntegration.getCurrencies(),
									currencyIntegration.getSource(),
									currencyIntegration.getTime_hours(),
									currencyIntegration.getTime_minutes(),
									currencyIntegration.getModification().getCreatedBy(),
									currencyIntegration.getModification().getCreatedTime(),
									currencyIntegration.getTimeZone(),
									currencyIntegration.getJobName(),
									currencyIntegration.getJobfile_names(),
									currencyIntegration.getClientSpecificJobName(),
									currencyIntegration.getClientSpecificJobfile_names()
								});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveCurrencyIntegration", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveCurrencyIntegration", e);
			
		}

		return count;
	}
	
	@Override
	public CurrencyIntegration getCurrencyIntegration( JdbcTemplate clientAppDbJdbcTemplate) {

		CurrencyIntegration currencyIntegrationList = null;

		try {
			String sql = sqlHelper.getSql("getCurrencyIntegration");

			currencyIntegrationList = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<CurrencyIntegration>()  {

				@Override
				public CurrencyIntegration extractData(ResultSet rs) throws SQLException, DataAccessException {
					CurrencyIntegration currencyInt = new CurrencyIntegration();
					if (rs != null && rs.next()) {
					currencyInt.setId(rs.getInt("id"));
					currencyInt.setApiUrl(rs.getString("api_url"));
					currencyInt.setAccessKey(rs.getString("access_key"));
					currencyInt.setCurrencies(rs.getString("currencies"));
					currencyInt.setSource(rs.getString("source"));
					currencyInt.setTime_hours(rs.getString("time_hours"));
					currencyInt.setTime_minutes(rs.getString("time_minutes"));
					currencyInt.setTimeZone(rs.getString("time_zone"));
					currencyInt.setJobName(rs.getString("Job_name"));
					currencyInt.setJobfile_names(rs.getString("job_file_names"));
					currencyInt.setClientSpecificJobName(rs.getString("client_specific_Job_name"));
					currencyInt.setClientSpecificJobfile_names(rs.getString("client_specific_job_file_names"));
					return currencyInt;
					} else {
						return null;
					}
			}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getCurrencyIntegration()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getCurrencyIntegration()", e);
			
		}

		return currencyIntegrationList;
	}
	
	@Override
	public int updateCurrencyIntegration(CurrencyIntegration currencyIntegration, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateCurrencyIntegration");
			update = clientAppDbJdbcTemplate.update(sql,new Object[] {
					currencyIntegration.getApiUrl(),
					currencyIntegration.getAccessKey(),
					currencyIntegration.getCurrencies(),
					currencyIntegration.getSource(),
					currencyIntegration.getTime_hours(),
					currencyIntegration.getTime_minutes(),
					currencyIntegration.getModification().getModifiedBy(),
					currencyIntegration.getModification().getModifiedTime(),
					currencyIntegration.getTimeZone(),
					currencyIntegration.getJobName(),
					currencyIntegration.getJobfile_names(),
					currencyIntegration.getClientSpecificJobName(),
					currencyIntegration.getClientSpecificJobfile_names(),
					currencyIntegration.getId()
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getCurrencyIntegration()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getCurrencyIntegration()", e);
			
		}
		return update;
	}

	@Override
	public List<VersionUpgrade> getVersionUpgrade( JdbcTemplate clientAppDbJdbcTemplate) {
		List<VersionUpgrade> versionUpgradeList = null;
		try {
			String sql = sqlHelper.getSql("getVersionUpgrade");
			versionUpgradeList = clientAppDbJdbcTemplate.query(sql,new RowMapper<VersionUpgrade>() {
				@Override
				public VersionUpgrade mapRow(ResultSet rs, int rowNum) throws SQLException {
					VersionUpgrade versionUpgrade = new VersionUpgrade();
					versionUpgrade.setVersionId(rs.getInt("version_id"));
					versionUpgrade.setVersionNumber(rs.getString("version_number"));
					versionUpgrade.setDescription(rs.getString("description"));
					versionUpgrade.setFilePath(rs.getString("file_path"));
					versionUpgrade.setLatestVersion(rs.getBoolean("is_latest_version"));
					return versionUpgrade;
				}
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getVersionUpgrade()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getVersionUpgrade()", e);
			
		}
		return versionUpgradeList;
	}
	
	@Override
	public int createVersionUpgrade(VersionUpgrade versionUpgrade, JdbcTemplate clientAppDbJdbcTemplate) {

		int saveVersionUpgrade = 0;
		try {
			String sql = sqlHelper.getSql("createVersionUpgrade");
			saveVersionUpgrade = clientAppDbJdbcTemplate.update(sql, new Object[] { 
					versionUpgrade.getVersionId(),
					versionUpgrade.getVersionNumber(),
					versionUpgrade.getDescription(),
					versionUpgrade.getFilePath(),
					versionUpgrade.getModification().getCreatedBy(),
					versionUpgrade.getModification().getCreatedTime(),
					versionUpgrade.isLatestVersion()

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while createVersionUpgrade()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createVersionUpgrade()", e);
			
		}
		return saveVersionUpgrade;
	}
	
	@Override
	public VersionUpgrade getVersionUpgradeDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {

		VersionUpgrade versionUpgradeDetails = null;

		try {
			String sql = sqlHelper.getSql("getVersionUpgradeDetailsById");
			versionUpgradeDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<VersionUpgrade>() {

				@Override
				public VersionUpgrade extractData(ResultSet rs) throws SQLException, DataAccessException {
					VersionUpgrade versionUpgrades = new VersionUpgrade();
					if (rs != null && rs.next()) {
						versionUpgrades.setVersionNumber(rs.getString("version_number"));
						versionUpgrades.setDescription(rs.getString("description"));
						versionUpgrades.setFilePath(rs.getString("file_path"));
						versionUpgrades.setVersionId(rs.getInt("version_id"));
						versionUpgrades.setLatestVersion(rs.getBoolean("is_latest_version"));
						return versionUpgrades;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getVersionUpgradeDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getVersionUpgradeDetailsById()", e);
			
		}
		return versionUpgradeDetails;
	}
	@Override
	public VersionUpgrade getVersionUpgradeDetailsByVersionNumber(String versionNumber, JdbcTemplate clientAppDbJdbcTemplate) {

		VersionUpgrade versionUpgradeDetails = null;

		try {
			String sql = sqlHelper.getSql("getVersionUpgradeDetailsByVersionNumber");
			versionUpgradeDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { versionNumber }, new ResultSetExtractor<VersionUpgrade>() {

				@Override
				public VersionUpgrade extractData(ResultSet rs) throws SQLException, DataAccessException {
					VersionUpgrade versionUpgrades = new VersionUpgrade();
					if (rs != null && rs.next()) {
						versionUpgrades.setVersionNumber(rs.getString("version_number"));
						versionUpgrades.setDescription(rs.getString("description"));
						versionUpgrades.setFilePath(rs.getString("file_path"));
						versionUpgrades.setVersionId(rs.getInt("version_id"));
						versionUpgrades.setLatestVersion(rs.getBoolean("is_latest_version"));
						return versionUpgrades;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getVersionUpgradeDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getVersionUpgradeDetailsById()", e);
			
		}
		return versionUpgradeDetails;
	}

	@Override
	public int updateVersionUpgrade(VersionUpgrade versionUpgrade, JdbcTemplate clientAppDbJdbcTemplate) {
		int versionUpdate = 0;
		try {
			String sql = sqlHelper.getSql("updateVersionUpgrade");
			versionUpdate = clientAppDbJdbcTemplate.update(sql,new Object[]{
					versionUpgrade.getVersionNumber(),
					versionUpgrade.getDescription(),
					versionUpgrade.getFilePath(),
					versionUpgrade.isLatestVersion(),
					versionUpgrade.getModification().getCreatedTime(),
					versionUpgrade.getVersionId()
			});
			
		}  catch (DataAccessException ae) {
			LOGGER.error("error while getVersionUpgradeDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getVersionUpgradeDetailsById()", e);
			
		}
		return versionUpdate;
	}


	@Override
	public List<String> getAllClientsByServer(JdbcTemplate jdbctemplate, JdbcTemplate clientAppDbJdbcTemplate) {
			List<String> clientList = null;
			try {
				String getAllClientsByServerSql = sqlHelper.getSql("getAllClientsByServer");
				clientList = jdbctemplate.query(getAllClientsByServerSql, new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						if (rs != null) {
							return rs.getString("client_id");
						} else {
							return null;
						}
					}
				});

			} catch (DataAccessException ae) {
				LOGGER.error("error while getAllClientsByServer()", ae);
				throw new AnvizentRuntimeException(ae);
			} catch (SqlNotFoundException e) {
				LOGGER.error("error while getWebserviceClients()", e);
				 
			}
			return clientList;

	}

	@Override
	public List<Package> getPackageList(JdbcTemplate jdbctemplate, String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<Package> packageList = null;
		try {
			String sql = sqlHelper.getSql("getPackageList");
			packageList = jdbctemplate.query(sql,new Object[]{clientId},new RowMapper<Package>() {
				@Override
				public Package mapRow(ResultSet rs, int rowNum) throws SQLException {
					Package packageFromServer = new Package();
					packageFromServer.setPackageId(rs.getInt("package_id"));
					packageFromServer.setPackageName(rs.getString("package_name"));
					packageFromServer.setIsStandard(rs.getBoolean("isStandard"));
					Modification modification = new Modification();
					modification.setCreatedBy(rs.getString("created_by")); 
					packageFromServer.setModification(modification);
					return packageFromServer;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getPackageList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getPackageList()", e);
			
		}
		return packageList;
	}
	@Override
	public List<User> getUsersList(JdbcTemplate jdbctemplate, String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<User> userList = null;
		try {
			String sql = sqlHelper.getSql("getUsersList");
			userList = jdbctemplate.query(sql,new Object[]{Integer.parseInt(clientId)},new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User userDetails = new User();
					userDetails.setUserId(rs.getString("userId"));
					userDetails.setUserName(rs.getString("user_name"));
					return userDetails;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getUsersList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getUsersList()", e);
			
		}
		return userList;
	}

	@Override
	public List<ILConnection> getDatabaseConnectionList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<ILConnection> iLConnectionList = null;
		try {
			String sql = sqlHelper.getSql("getDatabaseConnectionList");
			iLConnectionList = jdbctemplate.query(sql,new Object[]{clientId},new RowMapper<ILConnection>() {
				@Override
				public ILConnection mapRow(ResultSet rs, int rowNum) throws SQLException {
					ILConnection connection = new ILConnection();
					
					connection.setConnectionId(rs.getInt("connection_id"));
					connection.setConnectionName(rs.getString("connection_name"));

					Database dataBase = new Database();
					dataBase.setId(rs.getInt("database_Id"));
					dataBase.setName(rs.getString("database_Name"));
					dataBase.setConnector_id(rs.getInt("connectorId"));
					dataBase.setDriverName(rs.getString("driver_name"));
					dataBase.setProtocal(rs.getString("protocal"));
					dataBase.setUrlFormat(rs.getString("url_format"));
					connection.setDatabase(dataBase);
					connection.setDateFormat(rs.getString("date_format"));
					connection.setTimeZone(rs.getString("time_zone"));
					// decrypt client connection details
					try {
						connection.setConnectionType(EncryptionServiceImpl.getInstance().decrypt(rs.getString("connection_type")));
						connection.setUsername(EncryptionServiceImpl.getInstance().decrypt(rs.getString("username")));
						connection.setPassword(EncryptionServiceImpl.getInstance().decrypt(rs.getString("password")));
						connection.setServer(EncryptionServiceImpl.getInstance().decrypt(rs.getString("server")));
					} catch (Exception e) {
						LOGGER.error("", e);
					}
					return connection;
					
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getDatabaseConnectionList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getDatabaseConnectionList()", e);
			
		}
		return iLConnectionList;
	}

	@Override
	public List<WebServiceConnectionMaster> getWsConnectionList(JdbcTemplate jdbctemplate, String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<WebServiceConnectionMaster> wsConnectionList = null;
		try {
			String sql = sqlHelper.getSql("getWsConnectionList");
			wsConnectionList = jdbctemplate.query(sql,new Object[]{clientId},new RowMapper<WebServiceConnectionMaster>() {
				@Override
				public WebServiceConnectionMaster mapRow(ResultSet rs, int rowNum) throws SQLException {

					WebServiceConnectionMaster webServiceConnectionMaster = new WebServiceConnectionMaster();

					webServiceConnectionMaster.setId(rs.getLong("ws_con_id"));
					webServiceConnectionMaster.setWebServiceConName(rs.getString("web_service_con_name"));
					webServiceConnectionMaster.setRequestParams(rs.getString("auth_request_params"));
					webServiceConnectionMaster.setBodyParams(rs.getString("auth_body_params"));

					webServiceConnectionMaster.setAuthPathParams(rs.getString("auth_path_params"));
					webServiceConnectionMaster.setHeaderKeyvalues(rs.getString("header_key_values"));
					webServiceConnectionMaster.setAuthenticationToken(rs.getString("authentication_token"));
					webServiceConnectionMaster.setAuthenticationRefreshToken(rs.getString("authentication_refresh_token"));
					webServiceConnectionMaster.setActive(rs.getBoolean("wc_con_active_status"));
					WebServiceTemplateMaster webServiceTemplateMaster = new WebServiceTemplateMaster();

					webServiceTemplateMaster.setId(rs.getLong("ws_template_id"));
					webServiceTemplateMaster.setWebServiceName(rs.getString("ws_template_name"));

					WebServiceAuthenticationTypes webServiceAuthenticationTypes = new WebServiceAuthenticationTypes();
					webServiceAuthenticationTypes.setId(rs.getLong("authentication_type_id"));
					webServiceAuthenticationTypes.setAuthenticationType(rs.getString("authentication_type_name"));

					webServiceTemplateMaster.setWebServiceAuthenticationTypes(webServiceAuthenticationTypes);
					webServiceTemplateMaster.setAuthenticationUrl(rs.getString("authentication_url"));
					webServiceTemplateMaster.setAuthenticationMethodType(rs.getString("authentication_method_type"));
					webServiceTemplateMaster.setApiAuthRequestParams(rs.getString("api_auth_request_params"));
					webServiceTemplateMaster.setApiAuthRequestHeaders(rs.getString("api_auth_request_headers"));
					webServiceTemplateMaster.setAuthenticationBodyParams(rs.getString("api_auth_request_body_params"));

					webServiceTemplateMaster.setDateFormat(rs.getString("date_format"));
					webServiceTemplateMaster.setTimeZone(rs.getString("time_zone"));

					OAuth2 oauth = new OAuth2();
					oauth.setRedirectUrl(rs.getString("callback_url"));
					oauth.setAccessTokenUrl(rs.getString("access_token_url"));
					oauth.setResponseType(rs.getString("response_type"));
					oauth.setClientIdentifier(rs.getString("clientid"));
					oauth.setClientSecret(rs.getString("client_secret"));
					oauth.setGrantType(rs.getString("grant_type"));
					webServiceTemplateMaster.setoAuth2(oauth);
					webServiceConnectionMaster.setWebServiceTemplateMaster(webServiceTemplateMaster);
					return webServiceConnectionMaster;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getWsConnectionList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getWsConnectionList()", e);
			
		}
		return wsConnectionList;
	}
	
	 
	@Override
	public List<ClientSpecificILDLJars> getClientSpecificILJarsList(JdbcTemplate jdbctemplate, String clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<ClientSpecificILDLJars> clientSpecificILDLJarsList = null;
		try {
			String sql = sqlHelper.getSql("getClientSpecificILJarsList");
			clientSpecificILDLJarsList = jdbctemplate.query(sql,new Object[]{clientId},new RowMapper<ClientSpecificILDLJars>() {
				@Override
				public ClientSpecificILDLJars mapRow(ResultSet rs, int rowNum) throws SQLException {
					ClientSpecificILDLJars clientSpecificILDLJars = new ClientSpecificILDLJars();
					clientSpecificILDLJars.setiLId(rs.getInt("il_id"));
					clientSpecificILDLJars.setClientSpecificJobName(rs.getString("Job_name"));
					clientSpecificILDLJars.setClientSpecificJobVersion(rs.getString("job_version"));
					return clientSpecificILDLJars;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getClientSpecificILJarsList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getClientSpecificILJarsList()", e);
			
		}
		return clientSpecificILDLJarsList;
	
	}

	@Override
	public List<ClientSpecificILDLJars> getClientSpecificDLJarsList(JdbcTemplate jdbctemplate,  String clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<ClientSpecificILDLJars> clientSpecificILDLJarsList = null;
		try {
			String sql = sqlHelper.getSql("getClientSpecificDLJarsList");
			clientSpecificILDLJarsList = jdbctemplate.query(sql,new Object[]{clientId},new RowMapper<ClientSpecificILDLJars>() {
				@Override
				public ClientSpecificILDLJars mapRow(ResultSet rs, int rowNum) throws SQLException {
					ClientSpecificILDLJars clientSpecificILDLJars = new ClientSpecificILDLJars();
					clientSpecificILDLJars.setdLId(rs.getInt("dl_id"));
					clientSpecificILDLJars.setClientSpecificJobName(rs.getString("Job_name"));
					clientSpecificILDLJars.setClientSpecificJobVersion(rs.getString("job_version"));
					return clientSpecificILDLJars;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getClientSpecificDLJarsList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getClientSpecificDLJarsList()", e);
			
		}
		return clientSpecificILDLJarsList;
	}
	 
	@Override
	public List<Industry> getTemplatesList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		List<Industry> templatesList = null;
		try {
			String sql = sqlHelper.getSql("getTemplatesList"); 
			templatesList = jdbctemplate.query(sql,new Object[]{clientId},new RowMapper<Industry>() {
				@Override
				public Industry mapRow(ResultSet rs, int rowNum) throws SQLException {
					Industry industry = new Industry();
					industry.setId(rs.getInt("id"));
					industry.setName(rs.getString("name"));
					return industry;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getTemplatesList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getTemplatesList()", e);
			
		}
		return templatesList;
	
	}
 
	@Override
	public List<TableScripts> getTableScriptsList(JdbcTemplate jdbctemplate,String clientId, JdbcTemplate clientAppDbJdbcTemplate) {

		List<TableScripts> tableScriptsList = null;
		try {
			String sql = sqlHelper.getSql("getTableScriptsList");
			tableScriptsList = jdbctemplate.query(sql,new Object[]{clientId},new RowMapper<TableScripts>() {
				@Override
				public TableScripts mapRow(ResultSet rs, int rowNum) throws SQLException {
					TableScripts tableScripts = new TableScripts();
					tableScripts.setId(rs.getInt("id"));
					tableScripts.setScriptName(rs.getString("script_name"));
					tableScripts.setScriptType(rs.getString("script_type"));
					return tableScripts;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getTableScriptsList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getTableScriptsList()", e);
			
		}
		return tableScriptsList;
	
	}

	@Override
	public List<ClientCurrencyMapping> getClientCurrencyMapping( JdbcTemplate clientAppDbJdbcTemplate) {
		List<ClientCurrencyMapping> clientCurrencyMappingList = null;
		try {
			String sql = sqlHelper.getSql("getClientCurrencyMapping");
			clientCurrencyMappingList = clientAppDbJdbcTemplate.query(sql,new RowMapper<ClientCurrencyMapping>() {
				@Override
				public ClientCurrencyMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
					ClientCurrencyMapping clientCurrencyMap = new ClientCurrencyMapping();
					clientCurrencyMap.setId(rs.getInt("id"));
					clientCurrencyMap.setClientId(rs.getString("clientId"));
					clientCurrencyMap.setCurrencyType(rs.getString("currency_type"));
					clientCurrencyMap.setIsActive(rs.getBoolean("isActive"));
					clientCurrencyMap.setCurrencyName(rs.getString("dashboard_currency"));
					clientCurrencyMap.setBasecurrencyCode(rs.getString("accountCurrencyCode"));
					clientCurrencyMap.setAccountingCurrencyCode(rs.getString("otherCurrencyCode"));
					
					return clientCurrencyMap;
				}
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getClientCurrencyMappingList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getClientCurrencyMappingList()", e);
			
		}
		return clientCurrencyMappingList;
	}
	

	@Override
	public List<ClientCurrencyMapping> getClientCurrencyMappingWithCurrencyIds( JdbcTemplate clientAppDbJdbcTemplate) {
		List<ClientCurrencyMapping> clientCurrencyMappingList = null;
		try {
			String sql = sqlHelper.getSql("getClientCurrencyMappingWithCurrencyIds");
			clientCurrencyMappingList = clientAppDbJdbcTemplate.query(sql,new RowMapper<ClientCurrencyMapping>() {
				@Override
				public ClientCurrencyMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
					ClientCurrencyMapping clientCurrencyMap = new ClientCurrencyMapping();
					clientCurrencyMap.setId(rs.getInt("id"));
					clientCurrencyMap.setClientId(rs.getString("clientId"));
					clientCurrencyMap.setCurrencyType(rs.getString("currency_type"));
					clientCurrencyMap.setIsActive(rs.getBoolean("isActive"));
					clientCurrencyMap.setCurrencyName(rs.getString("dashboard_currency"));
					clientCurrencyMap.setBasecurrencyCode(rs.getString("accountCurrencyCode"));
					clientCurrencyMap.setAccountingCurrencyCode(rs.getString("otherCurrencyCode"));
					
					return clientCurrencyMap;
				}
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getClientCurrencyMappingList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getClientCurrencyMappingList()", e);
			
		}
		return clientCurrencyMappingList;
	}
	
	

	@Override
	public int createClientCurrencyMapping(ClientCurrencyMapping clientCurrencyMapping, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate) {
		int saveCurrency = 0;
		try {
			
			if(clientCurrencyMapping.getId() == 0){
			String sql = sqlHelper.getSql("createClientCurrencyMapping");
			saveCurrency = clientAppDbJdbcTemplate.update(sql, new Object[] { 
					
					clientCurrencyMapping.getClientId(),
					clientCurrencyMapping.getCurrencyType(),
					clientCurrencyMapping.getCurrencyName(),
					clientCurrencyMapping.getIsActive(),
					clientCurrencyMapping.getBasecurrencyCode(),
					clientCurrencyMapping.getAccountingCurrencyCode(),
					clientCurrencyMapping.getModification().getCreatedBy(),
					clientCurrencyMapping.getModification().getCreatedTime()

			});
			}else{
				String queryUpdate = sqlHelper.getSql("updateClientCurrencyMapping");
				saveCurrency = clientAppDbJdbcTemplate.update(queryUpdate,new Object[]{
						
						clientCurrencyMapping.getClientId(),
						clientCurrencyMapping.getCurrencyType(),
						clientCurrencyMapping.getCurrencyName(),
						clientCurrencyMapping.getIsActive(),
						clientCurrencyMapping.getBasecurrencyCode(),
						clientCurrencyMapping.getAccountingCurrencyCode(),
						clientCurrencyMapping.getModification().getModifiedBy(),
						clientCurrencyMapping.getModification().getModifiedTime(),
						clientCurrencyMapping.getId()
						
				});
			}
			
			String sqlCount = sqlHelper.getSql("checkCurrencyRecordExistance");
			int count = clientJdbcTemplate.queryForObject(sqlCount,Integer.class,clientCurrencyMapping.getClientId());
			if(count == 0){
				String sqlSave = sqlHelper.getSql("saveClientTable");
				clientJdbcTemplate.update(sqlSave, 
						clientCurrencyMapping.getClientId(),
						clientCurrencyMapping.getCurrencyType(),
						clientCurrencyMapping.getCurrencyName(),
						clientCurrencyMapping.getBasecurrencyCode(),
						clientCurrencyMapping.getAccountingCurrencyCode(),
						true,
						clientCurrencyMapping.getModification().getCreatedBy(),
						clientCurrencyMapping.getModification().getCreatedTime()
				);
			}else{
				String sqlUpdate = sqlHelper.getSql("updateClientTable");
				clientJdbcTemplate.update(sqlUpdate,  
						clientCurrencyMapping.getCurrencyType(),
						clientCurrencyMapping.getCurrencyName(),
						clientCurrencyMapping.getBasecurrencyCode(),
						clientCurrencyMapping.getAccountingCurrencyCode(),
						clientCurrencyMapping.getModification().getModifiedBy(),
						clientCurrencyMapping.getModification().getModifiedTime(),
						true,
						clientCurrencyMapping.getClientId()
				);
			}
			
		} catch (DataAccessException ae) {
			
			LOGGER.error("error while createClientCurrencyMapping()", ae);
			if (ae instanceof DuplicateKeyException) {
				throw new AnvizentDuplicateStatusUpdationException("Currency already mapped to " + clientCurrencyMapping.getClientId());
			} if (ae instanceof BadSqlGrammarException) {
				if (saveCurrency == 1) {
					throw new AnvizentDuplicateStatusUpdationException("Currency mapping failed at client schema level.<br /><b>Reason:</b>" + ae.getMostSpecificCause().getMessage());
				} else {
					throw new AnvizentDuplicateStatusUpdationException("Currency mapping failed.<br /><b>Reason:</b>" + ae.getMostSpecificCause().getMessage());
				}
				
				
			} else {
				throw new AnvizentRuntimeException(ae);
			}
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createClientCurrencyMapping()", e);
			
		}
		return saveCurrency;
	}

	@Override
	public ClientCurrencyMapping getclientCurrencyMappingDetailsById(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		ClientCurrencyMapping clientCurrencyDetails = null;

		try {
			String sql = sqlHelper.getSql("getclientCurrencyMappingDetailsById");
			clientCurrencyDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<ClientCurrencyMapping>() {

				@Override
				public ClientCurrencyMapping extractData(ResultSet rs) throws SQLException, DataAccessException {
					ClientCurrencyMapping clientCurrencyMap = new ClientCurrencyMapping();
					if (rs != null && rs.next()) {
						clientCurrencyMap.setId(rs.getInt("id"));
						clientCurrencyMap.setClientId(rs.getString("clientId"));
						clientCurrencyMap.setCurrencyType(rs.getString("currency_type"));
						clientCurrencyMap.setIsActive(rs.getBoolean("isActive"));
						clientCurrencyMap.setCurrencyName(rs.getString("dashboard_currency"));
						clientCurrencyMap.setBasecurrencyCode(rs.getString("accountCurrencyCode"));
						clientCurrencyMap.setAccountingCurrencyCode(rs.getString("otherCurrencyCode"));
						return clientCurrencyMap;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getclientCurrencyMappingDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getclientCurrencyMappingDetailsById()", e);
			
		}
		return clientCurrencyDetails;
	}

	@Override
	public int createCurrencyMapping(ClientCurrencyMapping clientCurrencyMapping, JdbcTemplate clientAppDbJdbcTemplate) {
		int currencyMapping = 0;
		try {
			String sql = sqlHelper.getSql("createCurrencyMapping");
			currencyMapping = clientAppDbJdbcTemplate.update(sql,new Object[]{
					clientCurrencyMapping.getId(),
					clientCurrencyMapping.getCurrencyType(),
					clientCurrencyMapping.getCurrencyName(),
					clientCurrencyMapping.getBasecurrencyCode(),
					clientCurrencyMapping.getAccountingCurrencyCode(),
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while createCurrencyMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createCurrencyMapping()", e);
			
		}
		return currencyMapping;
	}

	@Override
	public ClientCurrencyMapping getDefaultCurrencyInfoById(int templateId, JdbcTemplate clientAppDbJdbcTemplate) {
		ClientCurrencyMapping clientCurrencyDetails = null;
		try {
			String sql = sqlHelper.getSql("getDefaultCurrencyInfoById");
			clientCurrencyDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { templateId }, new ResultSetExtractor<ClientCurrencyMapping>() {

				@Override
				public ClientCurrencyMapping extractData(ResultSet rs) throws SQLException, DataAccessException {
					ClientCurrencyMapping clientCurrencyMap = new ClientCurrencyMapping();
					if (rs != null && rs.next()) {
						clientCurrencyMap.setId(rs.getInt("template_id"));
						clientCurrencyMap.setCurrencyType(rs.getString("currency_type"));
						clientCurrencyMap.setCurrencyName(rs.getString("dashboard_currency"));
						clientCurrencyMap.setBasecurrencyCode(rs.getString("accountCurrencyCode"));
						clientCurrencyMap.setAccountingCurrencyCode(rs.getString("otherCurrencyCode"));
						return clientCurrencyMap;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getclientCurrencyMappingDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getclientCurrencyMappingDetailsById()", e);
			
		}
		return clientCurrencyDetails;
	}

	@Override
	public int updateCurrencyMapping(ClientCurrencyMapping clientCurrencyMapping, JdbcTemplate clientAppDbJdbcTemplate) {
		int currencyUpdate = 0;
		try {
			
			String sqlCount = sqlHelper.getSql("checkCurrencyRecordExist");
			int count = clientAppDbJdbcTemplate.queryForObject(sqlCount,Integer.class,clientCurrencyMapping.getId());
			if(count == 0){
				String sql = sqlHelper.getSql("createCurrencyMapping");
				currencyUpdate = clientAppDbJdbcTemplate.update(sql,new Object[]{
						clientCurrencyMapping.getId(),
						clientCurrencyMapping.getCurrencyType(),
						clientCurrencyMapping.getCurrencyName(),
						clientCurrencyMapping.getBasecurrencyCode(),
						clientCurrencyMapping.getAccountingCurrencyCode(),
				});
			}else{
				String sql = sqlHelper.getSql("updateCurrencyMapping");
				currencyUpdate = clientAppDbJdbcTemplate.update(sql,new Object[]{
						
						clientCurrencyMapping.getCurrencyType(),
						clientCurrencyMapping.getCurrencyName(),
						clientCurrencyMapping.getBasecurrencyCode(),
						clientCurrencyMapping.getAccountingCurrencyCode(),
						clientCurrencyMapping.getId()
						
				});
			}
			
			
			
		}  catch (DataAccessException ae) {
			LOGGER.error("error while updateCurrencyMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateCurrencyMapping()", e);
			
		}
		return currencyUpdate;
	}

	@Override
	public List<CurrencyList> getCurrencyList( JdbcTemplate clientAppDbJdbcTemplate) {
		List<CurrencyList> currencyList = null;
		try {

			String sql = sqlHelper.getSql("getCurrencyList");
			currencyList = clientAppDbJdbcTemplate.query(sql, new RowMapper<CurrencyList>() {
				public CurrencyList mapRow(ResultSet rs, int i) throws SQLException {
					CurrencyList currencyList = new CurrencyList();
					currencyList.setId(rs.getInt("id"));
					currencyList.setCurrencyCode(rs.getString("currency_code"));
					currencyList.setCurrencyName(rs.getString("currency_name"));
					return currencyList;
				}

			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getCurrencyList", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getCurrencyList", e);
			
		}

		return currencyList;
	}

	 
	@Override
	public int updateAllVersionsToOld( JdbcTemplate clientAppDbJdbcTemplate) {
		int count = -1;
		try {
			String saveIlsSql = sqlHelper.getSql("updateAllVersionsToOld");
			count = clientAppDbJdbcTemplate.update(saveIlsSql,new Object[]{false});
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateAllVersionsToOld", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateAllVersionsToOld", e);
			
		}
		return count;
	}

	@Override
	public ClientCurrencyMapping getclientCurrencyMappingDetailsByClientId(int id, JdbcTemplate clientAppDbJdbcTemplate) {
		ClientCurrencyMapping clientCurrencyDetails = null;

		try {
			String sql = sqlHelper.getSql("getclientCurrencyMappingDetailsByClientId");
			clientCurrencyDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<ClientCurrencyMapping>() {

				@Override
				public ClientCurrencyMapping extractData(ResultSet rs) throws SQLException, DataAccessException {
					ClientCurrencyMapping clientCurrencyMap = new ClientCurrencyMapping();
					if (rs != null && rs.next()) {
						clientCurrencyMap.setId(rs.getInt("id"));
						clientCurrencyMap.setClientId(rs.getString("clientId"));
						clientCurrencyMap.setCurrencyType(rs.getString("currency_type"));
						clientCurrencyMap.setIsActive(rs.getBoolean("isActive"));
						clientCurrencyMap.setCurrencyName(rs.getString("dashboard_currency"));
						clientCurrencyMap.setBasecurrencyCode(rs.getString("accountCurrencyCode"));
						clientCurrencyMap.setAccountingCurrencyCode(rs.getString("otherCurrencyCode"));
						return clientCurrencyMap;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getclientCurrencyMappingDetailsByClientId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getclientCurrencyMappingDetailsByClientId()", e);
			
		}
		return clientCurrencyDetails;
	}

	@Override
	public int savePropertiesKeyValuePairs(Internalization internalization, JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			
			if(internalization.getId() == 0){
				String sql = sqlHelper.getSql("savePropertiesKeyValuePairs");
				save = clientAppDbJdbcTemplate.update(sql,new Object[]{
						internalization.getLocale(),
						internalization.getLocaleName(),
						internalization.getProperties()
				});	
			}else{
				String sql = sqlHelper.getSql("updatePropertiesKeyValuePairs");
				save = clientAppDbJdbcTemplate.update(sql,new Object[]{
						internalization.getLocale(),
						internalization.getLocaleName(),
						internalization.getProperties(),
						internalization.getId()
				});	
			}
			
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while savePropertiesKeyValuePairs()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while savePropertiesKeyValuePairs()", e);
			
		}
		return save;
	}

	@Override
	public List<Internalization> getPropertiesList( JdbcTemplate clientAppDbJdbcTemplate) {
		List<Internalization> propertiesList= null;
		try {
			String sql = sqlHelper.getSql("getPropertiesList");
			propertiesList = clientAppDbJdbcTemplate.query(sql, new RowMapper<Internalization>(){

				@Override
				public Internalization mapRow(ResultSet rs, int rowNum) throws SQLException {
					Internalization proList = new Internalization();
					proList.setId(rs.getLong("id"));
					proList.setLocale(rs.getString("locale_id"));
					proList.setLocaleName(rs.getString("locale_name"));
					return proList;
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getPropertiesList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getPropertiesList()", e);
			
		}
		
		return propertiesList;
	}

	@Override
	public Internalization getpropertiesKeyValuePairsById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		Internalization propertiesList = null;
		try {
			String sql = sqlHelper.getSql("getpropertiesKeyValuePairsById");
			propertiesList = clientAppDbJdbcTemplate.query(sql, new Object[] {id},new ResultSetExtractor<Internalization>(){

				@Override
				public Internalization extractData(ResultSet rs) throws SQLException, DataAccessException {
					Internalization propInfo = new Internalization();
					if(rs != null && rs.next()){
						propInfo.setLocale(rs.getString("locale_id"));
						propInfo.setLocaleName(rs.getString("locale_name"));
						propInfo.setProperties(rs.getString("properties"));
						return propInfo;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getpropertiesKeyValuePairsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getpropertiesKeyValuePairsById()", e);
			
		}
		return propertiesList;
	}

	@Override
	public List<S3BucketInfo> getS3InfoList( JdbcTemplate clientAppDbJdbcTemplate) {
		List<S3BucketInfo> s3infoList= null;
		try {
			String sql = sqlHelper.getSql("getS3InfoList");
			s3infoList = clientAppDbJdbcTemplate.query(sql, new RowMapper<S3BucketInfo>(){

				@Override
				public S3BucketInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					S3BucketInfo s3List = new S3BucketInfo();
					s3List.setId(rs.getInt("id"));
					s3List.setBucketName(rs.getString("aws_bucket_name"));
					try {
						s3List.setAccessKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("aws_access_key")));
						s3List.setSecretKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("aws_secret_key")));
					} catch (Exception e) {
						MinidwServiceUtil.getErrorMessage("ERROR", e);
					}
					
					s3List.setIsActive(rs.getBoolean("isActive"));
					return s3List;
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getS3InfoList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getS3InfoList()", e);
			
		}
		
		return s3infoList;
	}

	@Override
	public int saveS3BucketInfo(S3BucketInfo s3BucketInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			
			if(s3BucketInfo.getId() == 0){
				String sql = sqlHelper.getSql("saveS3BucketInfo");
				try {
					save = clientAppDbJdbcTemplate.update(sql,new Object[]{
							
							s3BucketInfo.getBucketName(),
							EncryptionServiceImpl.getInstance().encrypt(s3BucketInfo.getSecretKey()),
							EncryptionServiceImpl.getInstance().encrypt(s3BucketInfo.getAccessKey()),
							s3BucketInfo.getIsActive(),
							s3BucketInfo.getModification().getCreatedBy(),
							s3BucketInfo.getModification().getCreatedTime(),
							
					});
				} catch (DuplicateKeyException ae) {
					throw new AnvizentDuplicateFileNameException(ae);
				} catch (Exception e) {
					MinidwServiceUtil.getErrorMessage("ERROR", e);
					throw new AnvizentRuntimeException(e);
				}	
			}else{
				String sql = sqlHelper.getSql("updateS3BucketInfo");
				try {
					save = clientAppDbJdbcTemplate.update(sql,new Object[]{
							s3BucketInfo.getBucketName(),
							EncryptionServiceImpl.getInstance().encrypt(s3BucketInfo.getSecretKey()),
							EncryptionServiceImpl.getInstance().encrypt(s3BucketInfo.getAccessKey()),
							s3BucketInfo.getIsActive(),
							s3BucketInfo.getModification().getModifiedBy(),
							s3BucketInfo.getModification().getModifiedTime(),
							s3BucketInfo.getId()
					});
				} catch (DuplicateKeyException ae) {
					throw new AnvizentDuplicateFileNameException(ae);
				}catch (Exception e) {
					MinidwServiceUtil.getErrorMessage("ERROR", e);
					throw new AnvizentRuntimeException(e);
				}	
			}
			
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveS3BucketInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveS3BucketInfo()", e);
			
		}
		return save;
	}

	@Override
	public S3BucketInfo getS3BucketInfoById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		S3BucketInfo s3Info = null;
		try {
			String sql = sqlHelper.getSql("getS3BucketInfoById");
			s3Info = clientAppDbJdbcTemplate.query(sql, new Object[] {id},new ResultSetExtractor<S3BucketInfo>(){

				@Override
				public S3BucketInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					S3BucketInfo s3BucketInfo = new S3BucketInfo();
					if(rs != null && rs.next()){
						s3BucketInfo.setId(rs.getInt("id"));
						s3BucketInfo.setBucketName(rs.getString("aws_bucket_name"));
						try {
							s3BucketInfo.setSecretKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("aws_secret_key")));
							s3BucketInfo.setAccessKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("aws_access_key")));
						} catch (Exception e) {
							MinidwServiceUtil.getErrorMessage("ERROR", e);
						}
						s3BucketInfo.setIsActive(rs.getBoolean("isActive"));
						return s3BucketInfo;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getS3BucketInfoById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getS3BucketInfoById()", e);
			
		}
		return s3Info;
	}

	@Override
	public Map<Integer, String> getBucketList( JdbcTemplate clientAppDbJdbcTemplate) { 
		Map<Integer, String> bucketinfoMap = null;
		try {

			String sql = sqlHelper.getSql("getBucketList");

			bucketinfoMap = clientAppDbJdbcTemplate.query(sql, 
					new ResultSetExtractor<Map<Integer, String>>() {

						@Override
						public Map<Integer, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs == null)
								return null;

							Map<Integer, String> bucketInfo = new LinkedHashMap<>();

							while (rs.next()) {
								bucketInfo.put(rs.getInt(1), rs.getString(2));
							}

							return bucketInfo;
						}
					});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getS3BucketInfoById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getS3BucketInfoById()", e);
			
		}
		return bucketinfoMap;
	
	}
	
	@Override
	public List<SchedulerMaster> getScheduledMasterInfoList( JdbcTemplate clientAppDbJdbcTemplate) {
		List<SchedulerMaster> schedulerMaster= null;
		try {
			String sql = sqlHelper.getSql("getScheduledMasterInfoList");
			schedulerMaster = clientAppDbJdbcTemplate.query(sql, new RowMapper<SchedulerMaster>(){

				@Override
				public SchedulerMaster mapRow(ResultSet rs, int rowNum) throws SQLException {
					SchedulerMaster schedulerMaster = new SchedulerMaster();
					schedulerMaster.setId(rs.getInt("id"));
					schedulerMaster.setName(rs.getString("name"));
					schedulerMaster.setIpAddress(rs.getString("ip_address"));
					schedulerMaster.setType(rs.getInt("type"));
					return schedulerMaster;
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getScheduledMasterInfoList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getScheduledMasterInfoList()", e);
			
		}
		
		return schedulerMaster;
	}
	@Override
	public List<FileSettings> getFileSettingsList( JdbcTemplate clientAppDbJdbcTemplate) {
		List<FileSettings> fileSettings= null;
		try {
			String sql = sqlHelper.getSql("getFileSettingsList");
			fileSettings = clientAppDbJdbcTemplate.query(sql, new RowMapper<FileSettings>(){

				@Override
				public FileSettings mapRow(ResultSet rs, int rowNum) throws SQLException {
					FileSettings fileSetting = new FileSettings();
					fileSetting.setClientId(rs.getString("id"));
					fileSetting.setMaxFileSizeInMb(rs.getInt("max_file_size_in_mb"));;
					fileSetting.setMultiPartEnabled(rs.getBoolean("multipart_file_enabled"));
					fileSetting.setNoOfRecordsPerFile(rs.getInt("no_of_records_per_file"));
					return fileSetting;
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getFileSettingsList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getFileSettingsList()", e);
			
		}
		
		return fileSettings;
	}

	@Override
	public int saveClientMapping(S3BucketInfo s3BucketInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			String delteSql = sqlHelper.getSql("deleteClientMapping");
			clientAppDbJdbcTemplate.update(delteSql,new Object[]{s3BucketInfo.getClientId()});
			
			String sql = sqlHelper.getSql("saveClientMapping");
			save = clientAppDbJdbcTemplate.update(sql,new Object[]{
					s3BucketInfo.getClientId(),
					s3BucketInfo.getId(),
					s3BucketInfo.getModification().getCreatedBy(),
					s3BucketInfo.getModification().getCreatedTime(),
					
			});	
		} catch (DataAccessException ae) {
			LOGGER.error("error while getS3BucketInfoById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getS3BucketInfoById()", e);
			
		}
		
		return save;
	}

	@Override
	public List<MiddleLevelManager> getMiddleLevelManager(JdbcTemplate clientAppDbJdbcTemplate) {
		List<MiddleLevelManager> middleInfoList= null;
		try {
			String sql = sqlHelper.getSql("getMiddleLevelManager");
			middleInfoList = clientAppDbJdbcTemplate.query(sql, new RowMapper<MiddleLevelManager>(){

				@Override
				public MiddleLevelManager mapRow(ResultSet rs, int rowNum) throws SQLException {
					MiddleLevelManager middleLevelList = new MiddleLevelManager();
					middleLevelList.setId(rs.getInt("id"));
					middleLevelList.setContextPath(rs.getString("context_path"));
					middleLevelList.setUploadListEndPoint(rs.getString("upload_list_end_point"));
					middleLevelList.setWriteEndPoint(rs.getString("write_end_point"));
					middleLevelList.setDeleteEndPoint(rs.getString("delete_end_point"));
					middleLevelList.setUpgradeEndPoint(rs.getString("upgrade_end_point"));
					middleLevelList.setUserAuthToken(rs.getString("user_auth_token"));
					middleLevelList.setClientAuthToken(rs.getString("client_auth_token"));
					try {
						middleLevelList.setEncryptionPrivateKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("encryption_private_key")));
						middleLevelList.setEncryptionIV(EncryptionServiceImpl.getInstance().decrypt(rs.getString("encryption_IV")));
					} catch (Exception e) {
						MinidwServiceUtil.getErrorMessage("ERROR", e);
					}
					
					return middleLevelList;
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getMiddleLevelManager()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getMiddleLevelManager()", e);
			
		}
		
		return middleInfoList;
	}

	@Override
	public int updateMiddleLevelManager(MiddleLevelManager middleLevelManager, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateMiddleLevelManager");
			try {
				update = clientAppDbJdbcTemplate.update(sql,new Object[]{
						middleLevelManager.getContextPath(),
						middleLevelManager.getUploadListEndPoint(),
						middleLevelManager.getWriteEndPoint(),
						middleLevelManager.getDeleteEndPoint(),
						middleLevelManager.getUpgradeEndPoint(),
						middleLevelManager.getUserAuthToken(),
						middleLevelManager.getClientAuthToken(),
						EncryptionServiceImpl.getInstance().encrypt(middleLevelManager.getEncryptionPrivateKey()),
						EncryptionServiceImpl.getInstance().encrypt(middleLevelManager.getEncryptionIV()),
						middleLevelManager.getId()
				});
			} catch (Exception e) {
				MinidwServiceUtil.getErrorMessage("ERROR", e);
			}	
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateMiddleLevelManager()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateMiddleLevelManager()", e);
			
		}
		return update;
	}

	@Override
	public int createMiddleLevelManager(MiddleLevelManager middleLevelManager, JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("createMiddleLevelManager");
			try {
				save = clientAppDbJdbcTemplate.update(sql,new Object[]{
						
						middleLevelManager.getContextPath(),
						middleLevelManager.getUploadListEndPoint(),
						middleLevelManager.getWriteEndPoint(),
						middleLevelManager.getDeleteEndPoint(),
						middleLevelManager.getUpgradeEndPoint(),
						middleLevelManager.getUserAuthToken(),
						middleLevelManager.getClientAuthToken(),
						EncryptionServiceImpl.getInstance().encrypt(middleLevelManager.getEncryptionPrivateKey()),
						EncryptionServiceImpl.getInstance().encrypt(middleLevelManager.getEncryptionIV())
				});
			} catch (Exception e) {
				MinidwServiceUtil.getErrorMessage("ERROR", e);
			}	
		} catch (DataAccessException ae) {
			LOGGER.error("error while middleLevelManager()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while middleLevelManager()", e);
			
		}
		return save;
	}

	@Override
	public MiddleLevelManager getMiddleLevelManagerDetailsById(JdbcTemplate clientAppDbJdbcTemplate) {
		MiddleLevelManager middleLevelInfo = null;
		try {
			String sql = sqlHelper.getSql("getMiddleLevelManagerDetailsById");
			middleLevelInfo = clientAppDbJdbcTemplate.query(sql,new ResultSetExtractor<MiddleLevelManager>(){

				@Override
				public MiddleLevelManager extractData(ResultSet rs) throws SQLException, DataAccessException {
					MiddleLevelManager middleLevelMManageInfo = new MiddleLevelManager();
					if(rs != null && rs.next()){
						middleLevelMManageInfo.setId(rs.getInt("id"));
						middleLevelMManageInfo.setContextPath(rs.getString("context_path"));
						middleLevelMManageInfo.setUploadListEndPoint(rs.getString("upload_list_end_point"));
						middleLevelMManageInfo.setWriteEndPoint(rs.getString("write_end_point"));
						middleLevelMManageInfo.setDeleteEndPoint(rs.getString("delete_end_point"));
						middleLevelMManageInfo.setUpgradeEndPoint(rs.getString("upgrade_end_point"));
						middleLevelMManageInfo.setUserAuthToken(rs.getString("user_auth_token"));
						middleLevelMManageInfo.setClientAuthToken(rs.getString("client_auth_token"));
						try {
							middleLevelMManageInfo.setEncryptionPrivateKey(EncryptionServiceImpl.getInstance().decrypt(rs.getString("encryption_private_key")));
							middleLevelMManageInfo.setEncryptionIV(EncryptionServiceImpl.getInstance().decrypt(rs.getString("encryption_IV")));
						} catch (Exception e) {
							MinidwServiceUtil.getErrorMessage("ERROR", e);
						}
						
						return middleLevelMManageInfo;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getMiddleLevelManagerDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getMiddleLevelManagerDetailsById()", e);
			
		}
		return middleLevelInfo;
	}
	

	@Override
	public List<AppDBVersionTableScripts> getAppDbVersionTableScripts(JdbcTemplate clientAppDbJdbcTemplate) {
		List<AppDBVersionTableScripts> appDbVersionList = null;
		try {
			String sql = sqlHelper.getSql("getAppDbVersionTableScripts");
			appDbVersionList = clientAppDbJdbcTemplate.query(sql,new RowMapper<AppDBVersionTableScripts>() {
				@Override
				public AppDBVersionTableScripts mapRow(ResultSet rs, int rowNum) throws SQLException {
					AppDBVersionTableScripts appDbTableScriptInfo = new AppDBVersionTableScripts();
					
					appDbTableScriptInfo.setId(rs.getInt("id"));
					appDbTableScriptInfo.setVersion(rs.getString("version"));
					//appDbTableScriptInfo.setAppDbScript(rs.getString("anviz_appdbscript"));
					//appDbTableScriptInfo.setMinidwScript(rs.getString("minidw_appdbscript"));
					appDbTableScriptInfo.setDefaultScript(rs.getBoolean("default_db"));
					return appDbTableScriptInfo;
				}
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAppDbVersionTableScripts()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAppDbVersionTableScripts()", e);
			
		}
		return appDbVersionList;
	}

	@Override
	public int createAppDBVersionTableScripts(AppDBVersionTableScripts appDBVersionTableScripts,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int saveAppDbVersionTableScripts = 0;
		try {
			String sql = sqlHelper.getSql("createAppDBVersionTableScripts");
			saveAppDbVersionTableScripts = clientAppDbJdbcTemplate.update(sql, new Object[] { 
					
					appDBVersionTableScripts.getVersion(),
					appDBVersionTableScripts.getAppDbScript(),
					appDBVersionTableScripts.getMinidwScript(),
					appDBVersionTableScripts.getDefaultScript()

			});
		}catch (DuplicateKeyException ae) {
			LOGGER.error("error while createAppDBVersionTableScripts()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		}  catch (DataAccessException ae) {
			LOGGER.error("error while saveAppDbVersionTableScripts()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveAppDbVersionTableScripts()", e);
			
		}
		return saveAppDbVersionTableScripts;
	}

	@Override
	public int updateAppDBVersionTableScriptsInfo(AppDBVersionTableScripts appDBVersionTableScripts,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int appDbVersion = 0;
		try {
			String sql = sqlHelper.getSql("updateAppDBVersionTableScriptsInfo");
			appDbVersion  = clientAppDbJdbcTemplate.update(sql,new Object[]{
					appDBVersionTableScripts.getVersion(),
					appDBVersionTableScripts.getAppDbScript(),
					appDBVersionTableScripts.getMinidwScript(),
					appDBVersionTableScripts.getDefaultScript(),
					appDBVersionTableScripts.getId()
			});
			
		}catch (DuplicateKeyException ae) {
			LOGGER.error("error while updateAppDBVersionTableScriptsInfo()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		}   catch (DataAccessException ae) {
			LOGGER.error("error while updateAppDBVersionTableScriptsInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateAppDBVersionTableScriptsInfo()", e);
			
		}
		return appDbVersion ;
	}

	@Override
	public AppDBVersionTableScripts getAppDbVersionTableScriptDetailsById(int id,
			JdbcTemplate clientAppDbJdbcTemplate) {
		AppDBVersionTableScripts appDbVersionTableScriptsDetails = null;

		try {
			String sql = sqlHelper.getSql("getAppDbVersionTableScriptDetailsById");
			appDbVersionTableScriptsDetails = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<AppDBVersionTableScripts>() {

				@Override
				public AppDBVersionTableScripts extractData(ResultSet rs) throws SQLException, DataAccessException {
					AppDBVersionTableScripts appDbTableScriptInfo = new AppDBVersionTableScripts();
					if (rs != null && rs.next()) {
						appDbTableScriptInfo.setVersion(rs.getString("version"));
						appDbTableScriptInfo.setAppDbScript(rs.getString("anviz_appdbscript"));
						appDbTableScriptInfo.setMinidwScript(rs.getString("minidw_appdbscript"));
						appDbTableScriptInfo.setDefaultScript(rs.getBoolean("default_db"));
						return appDbTableScriptInfo;
					} else {
						return null;
					}

				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAppDbVersionTableScriptDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAppDbVersionTableScriptDetailsById()", e);
			
		}
		return appDbVersionTableScriptsDetails;
	}

	@Override
	public String getAppDBScriptById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		String appDBScript = null;
		try {
			String sql = sqlHelper.getSql("getAppDBScriptById");
			appDBScript = clientAppDbJdbcTemplate.query(sql, new Object[] {id}, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("anviz_appdbscript");
					} else {
						return null;
					}
				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getAppDBScriptById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getAppDBScriptById()", e);
			
		}
		return appDBScript;
	}

	@Override
	public String getMinidwDBScriptById(Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		String minidwDBScript = null;
		try {
			String sql = sqlHelper.getSql("getMinidwDBScriptById");
			minidwDBScript = clientAppDbJdbcTemplate.query(sql, new Object[] {id}, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("minidw_appdbscript");
					} else {
						return null;
					}
				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getMinidwDBScriptById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getMinidwDBScriptById()", e);
			
		}
		return minidwDBScript;
	}

	@Override
	public int updateDefaultStatus(JdbcTemplate clientAppDbJdbcTemplate) {
		int count = -1;
		try {
			String saveSql = sqlHelper.getSql("updateDefaultStatus");
			count = clientAppDbJdbcTemplate.update(saveSql,new Object[]{false});
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateDefaultStatus", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateDefaultStatus", e);
			
		}
		return count;
	}

	

	@Override
	public List<HybridClientsGrouping> getHybridClientsGrouping(JdbcTemplate clientAppDbJdbcTemplate) {
		List<HybridClientsGrouping> hybridList = null;
		try {
			String sql = sqlHelper.getSql("getHybridClientsGrouping");
			hybridList = clientAppDbJdbcTemplate.query(sql,new RowMapper<HybridClientsGrouping>() {
				@Override
				public HybridClientsGrouping mapRow(ResultSet rs, int rowNum) throws SQLException {
					HybridClientsGrouping hybridInfo = new HybridClientsGrouping();
					hybridInfo.setId(rs.getLong("id"));
					hybridInfo.setName(rs.getString("name"));
					hybridInfo.setAccessKey(rs.getString("access_key"));
					hybridInfo.setActive(rs.getBoolean("is_active"));
					hybridInfo.setLastModifiedDate(rs.getString("last_modfied_date"));
					hybridInfo.setClientIds(getHybridClientGroupingByIdInfo(hybridInfo.getId(),clientAppDbJdbcTemplate));;
					return hybridInfo;
				}
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getHybridClientsGrouping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getHybridClientsGrouping()", e);
			
		}
		return hybridList;
	}

	@Override
	public int createHybridClientsGroupingInfo(HybridClientsGrouping hybridClientsGrouping,
			JdbcTemplate clientAppDbJdbcTemplate) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("createHybridClientsGroupingInfo");
			
			
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientAppDbJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, hybridClientsGrouping.getName());
					ps.setString(2, MinidwServiceUtil.getSaltString());
					ps.setBoolean(3, hybridClientsGrouping.isActive());
					ps.setString(4, hybridClientsGrouping.getDescription());
					ps.setString(5, hybridClientsGrouping.getIntervalType());
					ps.setLong(6, hybridClientsGrouping.getIntervalTime());
					ps.setLong(7, hybridClientsGrouping.getPackageThreadCount());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.longValue();
			}
			addHybridClientsGrouping(id,hybridClientsGrouping.getClientId(), clientAppDbJdbcTemplate);
		} catch (DataAccessException ae) {
			LOGGER.error("error while createHybridClientsGroupingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createHybridClientsGroupingInfo()", e);
			
		}
		return (int) id;
	}
	
	public List<CloudClient> getHybridClientGroupingByIdInfo(long id,JdbcTemplate clientAppDbJdbcTemplate) {
		List<CloudClient> cloudClientList = null;
		try {
			
			String sql = sqlHelper.getSql("getHybridClientsGroupingByGroupId");
			cloudClientList = clientAppDbJdbcTemplate.query(sql,new Object[] { id },new RowMapper<CloudClient>() {
				@Override
				public CloudClient mapRow(ResultSet rs, int rowNum) throws SQLException {
					CloudClient cloudClient = new CloudClient();
					cloudClient.setId(rs.getLong("id"));
					cloudClient.setClientName(rs.getString("clientname"));
					return cloudClient;
				}
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getHybridClientsGrouping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getHybridClientsGrouping()", e);
			
		}
		return cloudClientList;
	}

	public int addHybridClientsGrouping(long id, List<String> clientIds,JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			String addSql = sqlHelper.getSql("addHybridClientsGrouping");
			String deleteHybridGroupMappingSql = sqlHelper.getSql("deleteHybridGroupMapping");
			
			clientAppDbJdbcTemplate.update(deleteHybridGroupMappingSql, new Object[]{id}, new int[] { Types.BIGINT });
			
			String deleteHybridClientMappingSql = sqlHelper.getSql("deleteHybridClientMapping");
			
			List<Object[]> batchArgs = new ArrayList<>();
			List<Object[]> batchArgs1 = new ArrayList<>();
			for (String clients : clientIds) {
				batchArgs.add(new Object[] { id, clients});
				batchArgs1.add(new Object[] { clients});
			}

			clientAppDbJdbcTemplate.batchUpdate(deleteHybridClientMappingSql, batchArgs1, new int[] { Types.BIGINT });
			clientAppDbJdbcTemplate.batchUpdate(addSql, batchArgs, new int[] { Types.BIGINT, Types.BIGINT });

		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}

		return save;
	}

	@Override
	public HybridClientsGrouping getHybridClientGroupingDetailsById(long id, JdbcTemplate clientAppDbJdbcTemplate) {

		HybridClientsGrouping hybridClientsGrouping = null;

		try {
			String sql = sqlHelper.getSql("getHybridClientGroupingDetailsById");
			hybridClientsGrouping = clientAppDbJdbcTemplate.query(sql, new Object[] { id }, new ResultSetExtractor<HybridClientsGrouping>() {

				@Override
				public HybridClientsGrouping extractData(ResultSet rs) throws SQLException, DataAccessException {
					HybridClientsGrouping hybridInfo = new HybridClientsGrouping();
					if (rs != null && rs.next()) {
						
						hybridInfo.setName(rs.getString("name"));
						hybridInfo.setAccessKey(rs.getString("access_key"));
						hybridInfo.setDescription(rs.getString("description"));
						hybridInfo.setClientIds(getHybridClientGroupingByIdInfo(id,clientAppDbJdbcTemplate));
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
			LOGGER.error("error while hybridClientsGrouping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while hybridClientsGrouping()", e);
			
		}
		return hybridClientsGrouping;
	}

	@Override
	public int updateHybridClientsGroupingInfo(HybridClientsGrouping hybridClientsGrouping,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateHybridClientsGroupingInfo");
			update = clientAppDbJdbcTemplate.update(sql,new Object[]{
					hybridClientsGrouping.getName(),
					hybridClientsGrouping.isActive(),
					hybridClientsGrouping.getDescription(),
					hybridClientsGrouping.getIntervalType(),
					hybridClientsGrouping.getIntervalTime(),
					hybridClientsGrouping.getPackageThreadCount(),
					hybridClientsGrouping.getId()
			});	
			addHybridClientsGrouping(hybridClientsGrouping.getId(),hybridClientsGrouping.getClientId(), clientAppDbJdbcTemplate);
		} catch (DataAccessException ae) {
			LOGGER.error("error while hybridClientsGrouping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while hybridClientsGrouping()", e);
			
		}
		return update;
	}

	@Override
	public int saveClientsInstantScriptExecution(TableScriptsForm tableScriptForm, JdbcTemplate jdbcTemplate) {
		
		int clientinstantId = 0;
		try {
			
			final String sql = sqlHelper.getSql("saveClientsInstantScriptExecution");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "clients_instant_script_id" });
					ps.setString(1, tableScriptForm.getClientIdList().toString());
					ps.setString(2, tableScriptForm.getSqlScript());
					ps.setString(3, tableScriptForm.getModification().getCreatedBy());
					ps.setString(4,tableScriptForm.getModification().getCreatedTime());
					ps.setString(5, tableScriptForm.getExecutionType());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				clientinstantId = autoIncrement.intValue();
			}
			
		} catch (DataAccessException ae) {
			LOGGER.error("error in saveClientsInstantScriptExecution()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveClientsInsatntScriptExecution()", e);
			e.getCause().getMessage();

		}
		return clientinstantId;
	}

	@Override
	public int saveInstantScriptExecutionOfClient(TableScriptsForm tableScriptForm, JdbcTemplate jdbcTemplate) {
		int saveCount = 0;
		try {
			String sql = sqlHelper.getSql("saveInstantScriptExecutionOfClient");
			saveCount = jdbcTemplate.update(sql,
					new Object[] { 
							tableScriptForm.getClientInstantExecutionId(),
							tableScriptForm.getClientId(),
							tableScriptForm.getSqlScript(),
							tableScriptForm.getExecution_status(), 
							tableScriptForm.getExecution_status_msg(),
						    tableScriptForm.getModification().getCreatedBy(),
							tableScriptForm.getModification().getCreatedTime()
							 });

		} catch (DataAccessException ae) {
			LOGGER.error("error in saveInstantScriptExecutionOfClient()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveInstantScriptExecutionOfClient()", e);
			e.getCause().getMessage();

		}
		return saveCount;
	}
	
	@Override
	public List<TableScriptsForm> getClientsInstantScriptExecution(JdbcTemplate clientAppDbJdbcTemplate) {
		List<TableScriptsForm> tableScriptsFormList = null;
		try {
			String sql = sqlHelper.getSql("getClientsInstantScriptExecution");
			tableScriptsFormList = clientAppDbJdbcTemplate.query(sql,new RowMapper<TableScriptsForm>() {
				@Override
				public TableScriptsForm mapRow(ResultSet rs, int rowNum) throws SQLException {
					TableScriptsForm tablescriptForm = new TableScriptsForm();
					tablescriptForm.setClientsInstantScriptId(rs.getInt("clients_instant_script_id"));
					tablescriptForm.setSqlScript(rs.getString("sqlscript"));
					tablescriptForm.setClientIds(rs.getString("clientIds"));
					return tablescriptForm;
				}
			});
		
		} catch (DataAccessException ae) {
			LOGGER.error("error while getClientsInstantScriptExecution()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getClientsInstantScriptExecution()", e);
			e.getMessage();
		}
		return tableScriptsFormList;
	}

	@Override
	public List<TableScriptsForm> getInstantScriptExecutionOfClient(int client_instant_execution_id,JdbcTemplate clientAppDbJdbcTemplate) {
		List<TableScriptsForm> tableScriptsFormList = null;
		try {
			String sql = sqlHelper.getSql("getInstantScriptExecutionOfClient");
			
			tableScriptsFormList = clientAppDbJdbcTemplate.query(sql,new Object[] { client_instant_execution_id },new RowMapper<TableScriptsForm>() {
				@Override
				public TableScriptsForm mapRow(ResultSet rs, int rowNum) throws SQLException {
					TableScriptsForm tableScriptForm = new TableScriptsForm();
					tableScriptForm.setClientInstantExecutionId(rs.getInt("client_instant_execution_id"));
					tableScriptForm.setSqlScript(rs.getString("sqlscript"));
					tableScriptForm.setClientId(rs.getInt("client_id"));
					tableScriptForm.setExecution_status(rs.getBoolean("execution_status"));
					tableScriptForm.setExecution_status_msg(rs.getString("execution_status_msg"));
					return tableScriptForm;
				}
			});
			
		} catch (DataAccessException ae) {
			LOGGER.error("error while getInstantScriptExecutionOfClient()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getInstantScriptExecutionOfClient()", e);
			e.getMessage();
		}
		return tableScriptsFormList;
	}

	 

	@Override
	public String getSqlScriptByExecutionId(int scriptId, JdbcTemplate clientAppDbJdbcTemplate) {
		String sqlScript = null;
		try {
			String sql = sqlHelper.getSql("getSqlScriptByExecutionId");
			sqlScript = clientAppDbJdbcTemplate.query(sql, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getString("sqlscript");
					} else {
						return null;
					}
				}

			}, scriptId);

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getSqlScriptByExecutionId", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getSqlScriptByExecutionId", e);
			
		}
		return sqlScript;
	}

	@Override
	public ClientJobExecutionParameters getclientJobExecutionParametersDetailsById(
			JdbcTemplate clientAppDbJdbcTemplate) {
		ClientJobExecutionParameters clientJobParams= null;
		try {
			String sql = sqlHelper.getSql("getclientJobExecutionParametersDetailsById");
			clientJobParams = clientAppDbJdbcTemplate.query(sql,new ResultSetExtractor<ClientJobExecutionParameters>(){

				@Override
				public ClientJobExecutionParameters extractData(ResultSet rs) throws SQLException, DataAccessException {
					ClientJobExecutionParameters  clientJobParamsInfo = new ClientJobExecutionParameters();
					if(rs != null && rs.next()){
						clientJobParamsInfo.setId(rs.getInt("id"));
						clientJobParamsInfo.setSourceTimeZone(rs.getString("source_time_zone"));
						clientJobParamsInfo.setDestTimeZone(rs.getString("dest_time_zone"));
						clientJobParamsInfo.setNullReplaceValues(rs.getString("null_raplace_value"));
						clientJobParamsInfo.setCaseSensitive(rs.getBoolean("case_sensitive"));
						clientJobParamsInfo.setInterval(rs.getLong("trial_interval"));
						
						return clientJobParamsInfo;
					}else{
						return null;
					}
				}
				
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getclientJobExecutionParametersDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getclientJobExecutionParametersDetailsById()", e);
			
		}
		return clientJobParams;
	}

	@Override
	public int createClientJobExecutionParams(ClientJobExecutionParameters clientJobExecutionParameters,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("createClientJobExecutionParams");
			try {
				save = clientAppDbJdbcTemplate.update(sql,new Object[]{
						clientJobExecutionParameters.getSourceTimeZone(),
						clientJobExecutionParameters.getDestTimeZone(),
						clientJobExecutionParameters.getNullReplaceValues(),
						clientJobExecutionParameters.getCaseSensitive(),
						clientJobExecutionParameters.getInterval()
						
				});
			} catch (Exception e) {
				MinidwServiceUtil.getErrorMessage("ERROR", e);
			}	
		} catch (DataAccessException ae) {
			LOGGER.error("error while createClientJobExecutionParams()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while createClientJobExecutionParams()", e);
			
		}
		return save;
	}

	@Override
	public int updateClientJobExecutionParams(ClientJobExecutionParameters clientJobExecutionParameters,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateClientJobExecutionParams");
				update = clientAppDbJdbcTemplate.update(sql,new Object[]{
						clientJobExecutionParameters.getSourceTimeZone(),
						clientJobExecutionParameters.getDestTimeZone(),
						clientJobExecutionParameters.getNullReplaceValues(),
						clientJobExecutionParameters.getCaseSensitive(),
						clientJobExecutionParameters.getInterval(),
						clientJobExecutionParameters.getId()
						
				});
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateClientJobExecutionParams()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateClientJobExecutionParams()", e);
			
		}
		return update;
	}

	@Override
	public int executeInsertScripts(MultiClientInsertScriptsExecution multiClientInsertScriptsExecution,
			JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getDDlTableCreateScripts(String query, JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<String> ddlCreateScript = null;
		try {
			ddlCreateScript = clientAppDbJdbcTemplate.query(query, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("ddl_script");
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getDDlTableCreateScripts", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return ddlCreateScript;
	
	}
	
	@Override
	public int saveSchedularMasterClientMapping(int clientId, List<Integer> schedularMasterIds, Modification modification, JdbcTemplate clientAppDbJdbcTemplate) {
		int[] count = null;
		try {

			String sql = sqlHelper.getSql("saveClientSchedularMasterMapping");
			count = clientAppDbJdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, clientId);
					ps.setInt(2, schedularMasterIds.get(i));
				
				}

				@Override
				public int getBatchSize() {
					return schedularMasterIds.size();
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while saveClientSchedularMasterMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while saveClientWebserviceMapping", e);
			
		}

		return count != null?count.length:0;

	}

	@Override
	public int updateFilesetting( Integer id, JdbcTemplate clientAppDbJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateFilesetting");
				update = clientAppDbJdbcTemplate.update(sql,new Object[]{
						id,
						id
						
				});
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateClientJobExecutionParams()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateClientJobExecutionParams()", e);
			
		}
		return update;
	}

	@Override
	public List<Map<String, Object>> getWishList(JdbcTemplate clientAppDbJdbcTemplate) {
		List<Map<String, Object>> wishListMap = new ArrayList<Map<String, Object>>();
		try {
			if( clientAppDbJdbcTemplate == null ) {
				clientAppDbJdbcTemplate = getJdbcTemplate();
			}
			String sql = sqlHelper.getSql("getWishList");
			wishListMap = clientAppDbJdbcTemplate.query(sql, new RowMapper<Map<String, Object>>(){
				public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
					Map<String, Object> wishMap = new HashMap<String, Object>();
					ResultSetMetaData rm = rs.getMetaData();
					int totalColumns = rm.getColumnCount();
					for(int j = 1; j <= totalColumns; j++) {
						wishMap.put(rm.getColumnLabel(j), rs.getObject(rm.getColumnLabel(j)));
					}
					return wishMap;
				}
			});
		}catch (DataAccessException ae) {
			LOGGER.error("error while getWishList()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getWishList()", e);
		}
		return wishListMap;
	}

	@Override
	public Map<String, Integer> transeferDataERPtoClient(String clientId,String referenceClientName, Integer erpId, String referenceUserId, List<String> erptableList, 
			JdbcTemplate clientAppDbJdbcTemplate) {
		Map<String, Integer> erpInsertStatus = new HashMap<>();
		
		try {
			clientAppDbJdbcTemplate.execute(new ConnectionCallback<Integer>() {
				int noOfCon = 0;
				List<String> columns = null;
				PreparedStatement pstmt = null;
				Statement stmt = null;
				DatabaseMetaData dbm = clientAppDbJdbcTemplate.getDataSource().getConnection().getMetaData();
				public Integer doInConnection(Connection con) throws SQLException {
					try {
						Map<String, String> erpTables = getERPTables();
						con.setAutoCommit(false);
						for(String tbl : erpTables.keySet()) {
							int insertCount = 0;
							List<Map<String, Object>> dataListMap = tableResultSet(erpId, tbl, null);
						     if( dataListMap.size() > 0 ) {
						    	  
						    	 columns = getColumnsByTable(tbl, dbm);
						     
								String sql = buildInsertScriptWithPlaceHolders(tbl, columns);
								
								stmt = con.createStatement();
								stmt.addBatch("SET FOREIGN_KEY_CHECKS=0");
								stmt.addBatch("truncate "+tbl);
								stmt.addBatch("SET FOREIGN_KEY_CHECKS=1");
								stmt.executeBatch();
								stmt.close();
								
								pstmt = con.prepareStatement(sql);
								pstmt.addBatch("SET FOREIGN_KEY_CHECKS=0");
								for(int i = 0; i < dataListMap.size(); i++) {
									int k = 1;
									for(String column : columns) {
										Object arg = dataListMap.get(i).get(column);
										if(column.equalsIgnoreCase("user_id") || column.equalsIgnoreCase("userid")) {
											arg = referenceUserId;
										 }
										if(column.equalsIgnoreCase("cloudclientid")) {
											arg = clientId;
										 }
										if(column.equalsIgnoreCase("cloudclientname")) {
											arg = referenceClientName;
										 }
										pstmt.setObject(k, arg);
										/* if (arg instanceof Date) {
											 	pstmt.setTimestamp(k, new Timestamp(((Date) arg).getTime()));
										    } else if (arg instanceof Integer) {
										    	pstmt.setInt(k, (Integer) arg);
										    } else if (arg instanceof Long) {
										    	pstmt.setLong(k, (Long) arg);
										    } else if (arg instanceof Double) {
										    	pstmt.setDouble(k, (Double) arg);
										    } else if (arg instanceof Float) {
										    	pstmt.setFloat(k, (Float) arg);
										    } else if (arg instanceof Boolean) {
										    	pstmt.setBoolean(k, (Boolean) arg);
										    } else if(arg instanceof Timestamp){
										    	pstmt.setTimestamp(k, (Timestamp) arg);
										    } else if(arg instanceof Time){
										    	pstmt.setTime(k, (Time) arg);
										    } else {
										    	pstmt.setString(k, (String) arg);
										    }*/
										 k++;
									}
									pstmt.addBatch();
								}
								pstmt.addBatch("SET FOREIGN_KEY_CHECKS=1");
								insertCount = pstmt.executeBatch().length;
								pstmt.close();
						       }
							erpInsertStatus.put(tbl, insertCount);
							LOGGER.info(tbl+ " Total records:::: " + insertCount);
						}
						con.commit();
					}catch( Throwable t) {
						t.printStackTrace();
						con.rollback();
						throw new RuntimeException(t.getMessage());
					}finally {
						pstmt.close();
						stmt.close();
						con.close();
					}
					return noOfCon++;
				}
			});
		}catch( Throwable t) {
			throw new AnvizentRuntimeException(t);
		}
		return erpInsertStatus;
	}
	
	private List<String> getColumnsByTable(String tbl, DatabaseMetaData dbm) {
		List<String> columns = new ArrayList<>();
		try {
			ResultSet rs = dbm.getColumns(null, null, tbl, null);
			while(rs.next()) {
				columns.add(rs.getString("COLUMN_NAME"));
			}
		}catch( Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return columns;
	 }

	public Map<String, String> getERPTables() {
		 Map<String, String> erpTables = new HashMap<>();
		try {
			String query = "select * from minidw_erp_tables where reference_table_scope in('cm','cs')";
			 getJdbcTemplate().query(query, new RowMapper<Map<String, String>>() {

				public Map<String, String> mapRow(ResultSet rs, int i) throws SQLException {
					 ResultSetMetaData rm = rs.getMetaData();
						int totalColumns = rm.getColumnCount();
						for(int j = 1; j <= totalColumns; j++) {
							erpTables.put(rs.getString("reference_table"), rs.getString("reference_table_scope"));
						}
						return null;
				}
			});
		}catch( Throwable t) {
			throw new RuntimeException(t.getMessage());
		}
		return erpTables;
	}

	private List<Map<String, Object>> tableResultSet(Integer erpId, String tbl, JdbcTemplate jdbcTemplate) {
		List<Map<String, Object>> dataMap = null;
		String selectQuery = "select * from erp_" + tbl + " where erp_id = " + erpId;
		try {
			if(jdbcTemplate == null) {
				jdbcTemplate = getJdbcTemplate();
			}
			dataMap = jdbcTemplate.query(selectQuery, new RowMapper<Map<String, Object>>(){
				
				public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException{
				   Map<String, Object> record = new  HashMap<>();
				   ResultSetMetaData rm = rs.getMetaData();
					int totalColumns = rm.getColumnCount();
					for(int j = 1; j <= totalColumns; j++) {
						record.put(rm.getColumnLabel(j), rs.getObject(rm.getColumnLabel(j)));
					}
				   return record;
				}
			});
		}catch( Throwable t ) {
			throw new RuntimeException(t.getMessage());
		}
		return dataMap;
	}
	
	private String buildInsertScriptWithPlaceHolders(String tbl, List<String> columns) {
		StringBuilder script = new  StringBuilder();
		try {
				script.append("INSERT INTO ").append(tbl);
				script.append(" (`" + StringUtils.join(columns, "`,`") + "`) VALUES (");
				
				for(int i = 0; i < columns.size(); i++) {
					script.append("?");
					if( i < columns.size()-1  ) {
						script.append(",");
					}else {
						script.append(")");
					}
				}
		}catch( Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return script.toString();
	}

	@Override
	public List<ERP> getERPList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<ERP> erpList = null;
		try {
			String sql = sqlHelper.getSql("getERPList");
			erpList = clientAppDbJdbcTemplate.query(sql, new RowMapper<ERP>() {
				public ERP mapRow(ResultSet rs, int i) throws SQLException {
					ERP erp = new ERP();
					erp.setId(rs.getInt("id"));
					erp.setName(rs.getString("erp_name"));
					erp.setVersion(rs.getString("erp_version"));
					erp.setDescription(rs.getString("description"));
					erp.setIsActive(rs.getBoolean("isactive"));
					return erp;
				}
			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getERPList", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getERPList", e);
			
		}
		return erpList;
	}

	@Override
	public List<DLInfo> getDlInfoList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<DLInfo> dLInfoList = null;
		try {
			String sql = sqlHelper.getSql("getDlInfoList");
			dLInfoList = clientAppDbJdbcTemplate.query(sql, new RowMapper<DLInfo>() {
				public DLInfo mapRow(ResultSet rs, int i) throws SQLException {
					DLInfo dLInfo = new DLInfo();
					dLInfo.setdL_id(rs.getInt("DL_id"));
					dLInfo.setdL_name(rs.getString("DL_name"));
					dLInfo.setdL_table_name(rs.getString("dl_table_name"));
					dLInfo.setJobName(rs.getString("Job_name"));
					dLInfo.setDescription(rs.getString("description"));
					dLInfo.setVersion(rs.getString("version"));
					dLInfo.setIs_Active(rs.getBoolean("isActive"));
					return dLInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getDlInfoList", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getDlInfoList", e);
		}
		return dLInfoList;
	}

	@Override
	public List<IlPrebuildQueriesMapping> getIlPrebuildQueriesMapping(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<IlPrebuildQueriesMapping> ilPrebuildQueriesMappingList = null;
		try {
			String sql = sqlHelper.getSql("getIlPrebuildQueriesMapping");
			ilPrebuildQueriesMappingList = clientAppDbJdbcTemplate.query(sql, new RowMapper<IlPrebuildQueriesMapping>() {
				public IlPrebuildQueriesMapping mapRow(ResultSet rs, int i) throws SQLException {
					IlPrebuildQueriesMapping ilPrebuildQueriesMapping = new IlPrebuildQueriesMapping();
					ilPrebuildQueriesMapping.setMappingId(rs.getInt("mapping_id"));
					ilPrebuildQueriesMapping.setDbTypeId(rs.getInt("database_type_id"));
					ilPrebuildQueriesMapping.setIlId(rs.getInt("il_id"));
					ilPrebuildQueriesMapping.setIlQuery(rs.getString("il_query"));
					ilPrebuildQueriesMapping.setHostoricalQuery(rs.getString("historical_load"));
					ilPrebuildQueriesMapping.setIncrementalUpdateQuery(rs.getString("il_incremental_update_query"));
					ilPrebuildQueriesMapping.setMaxDateQuery(rs.getString("max_date_query"));
					ilPrebuildQueriesMapping.setActive(rs.getBoolean("isActive"));
					return ilPrebuildQueriesMapping;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getIlPrebuildQueriesMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getIlPrebuildQueriesMapping", e);
		}
		return ilPrebuildQueriesMappingList;
	}

	@Override
	public List<IlDlMapping> getIlDlMapping(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<IlDlMapping> ilDlMappingMappingList = null;
		try {
			String sql = sqlHelper.getSql("getIlDlMapping");
			ilDlMappingMappingList = clientAppDbJdbcTemplate.query(sql, new RowMapper<IlDlMapping>() {
				public IlDlMapping mapRow(ResultSet rs, int i) throws SQLException {
					IlDlMapping ilDlMapping = new IlDlMapping();
					ilDlMapping.setId(rs.getInt("id"));
					ilDlMapping.setIlId(rs.getInt("il_id"));
					ilDlMapping.setDlId(rs.getInt("dl_id"));
					ilDlMapping.setActive(rs.getBoolean("isActive"));
					return ilDlMapping;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getIlDlMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getIlDlMapping", e);
		}
		return ilDlMappingMappingList;
	}

	@Override
	public List<DlKpiMapping> getDlKpiMapping(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<DlKpiMapping> dlKpiMappingList = null;
		try {
			String sql = sqlHelper.getSql("getDlKpiMapping");
			dlKpiMappingList = clientAppDbJdbcTemplate.query(sql, new RowMapper<DlKpiMapping>() {
				public DlKpiMapping mapRow(ResultSet rs, int i) throws SQLException {
					DlKpiMapping ilDlMapping = new DlKpiMapping();
					ilDlMapping.setId(rs.getInt("id"));
					ilDlMapping.setKpiId(rs.getInt("kpi_id"));
					ilDlMapping.setDlId(rs.getInt("dl_id"));
					ilDlMapping.setActive(rs.getBoolean("isActive"));
					return ilDlMapping;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getDlKpiMapping", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getDlKpiMapping", e);
		}
		return dlKpiMappingList;
	}

	@Override
	public List<WsTemplateAuthRequestParams> getWsTemplateAuthRequestParams(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<WsTemplateAuthRequestParams> wsTemplateAuthRequestParamsList = null;
		try {
			String sql = sqlHelper.getSql("getWsTemplateAuthRequestParams");
			wsTemplateAuthRequestParamsList = clientAppDbJdbcTemplate.query(sql, new RowMapper<WsTemplateAuthRequestParams>() {
				public WsTemplateAuthRequestParams mapRow(ResultSet rs, int i) throws SQLException {
					WsTemplateAuthRequestParams wsTemplateAuthRequestParams = new WsTemplateAuthRequestParams();
					wsTemplateAuthRequestParams.setId(rs.getInt("id"));
					wsTemplateAuthRequestParams.setParamName(rs.getString("param_name"));
					wsTemplateAuthRequestParams.setPasswordType(rs.getBoolean("is_passwordtype"));
					wsTemplateAuthRequestParams.setWsTemplateId(rs.getInt("ws_template_id"));
					wsTemplateAuthRequestParams.setMandatory(rs.getBoolean("is_mandatory"));
					return wsTemplateAuthRequestParams;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getWsTemplateAuthRequestParams", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getWsTemplateAuthRequestParams", e);
		}
		return wsTemplateAuthRequestParamsList;
	}

	@Override
	public List<WebServiceApi> getWsMappingList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<WebServiceApi> webServiceApiList = null;
		try {
			String sql = sqlHelper.getSql("getWsMappingList");
			webServiceApiList = clientAppDbJdbcTemplate.query(sql, new RowMapper<WebServiceApi>() {
				public WebServiceApi mapRow(ResultSet rs, int i) throws SQLException {
					WebServiceApi ws = new WebServiceApi();
					ws.setId(rs.getLong("id"));
					ws.setIlId(rs.getLong("il_id"));
					ws.setApiName(rs.getString("api_name"));
					ws.setBaseUrlRequired(rs.getBoolean("base_url_required"));
					ws.setPaginationType(rs.getString("pagination_type"));
					ws.setApiUrl(rs.getString("api_url"));
					ws.setApiMethodType(rs.getString("api_method_type"));
					ws.setApiPathParams(rs.getString("api_path_params"));
					ws.setApiRequestParams(rs.getString("api_request_params"));
					ws.setApiBodyParams(rs.getString("api_body_params"));
					ws.setIncrementalUpdate(rs.getBoolean("incremental_update"));
					ws.setIncrementalUpdateparamdata(rs.getString("incremental_update_params"));
					ws.setResponseObjectName(rs.getString("response_object_name"));
					ws.setResponseColumnObjectName(rs.getString("response_column_object_name"));
					ws.setPaginationRequired(rs.getBoolean("pagination_required"));
					ws.setPaginationType(rs.getString("pagination_type"));
					ws.setPaginationRequestParamsData(rs.getString("pagination_request_params"));
					ws.setSoapBodyElement(rs.getString("soap_body_element"));
					ws.setDefaultMapping(rs.getString("default_mapping"));
					return ws;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getWsMappingList", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getWsMappingList", e);
		}
		return webServiceApiList;
	}

	@Override
	public List<String> getErpTableList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<String> erpTableList = null;
		try {
			String sql = sqlHelper.getSql("getErpTableList");
			erpTableList = clientAppDbJdbcTemplate.query(sql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("table_name");
				}
			});

		}  catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getErpTableList", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getErpTableList", e);
		}
		return erpTableList;
	
	}

	@Override
	public List<String> getCmAndCstableList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<String> cmAndCstableList = null;
		try {
			String sql = sqlHelper.getSql("getErpTableList");
			cmAndCstableList = clientAppDbJdbcTemplate.query(sql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("reference_table");
				}
			});

		}  catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getCmAndCstableList", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getCmAndCstableList", e);
		}
		return cmAndCstableList;
	
	}

	@Override
	public List<User> getUserListFromClient(JdbcTemplate clientAppDbJdbcTemplate, int clientId)
	{
		List<User> userList = null;
		try {
			String sql = sqlHelper.getSql("getUserListFromClient");
			userList = clientAppDbJdbcTemplate.query(sql,new Object[] { clientId }, new RowMapper<User>() {
				public User mapRow(ResultSet rs, int i) throws SQLException {
					User user = new User();
					user.setUserId(rs.getString("id"));
					user.setUserName(rs.getString("username"));
					return user;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getUserListFromClient", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getUserListFromClient", e);
		}
		return userList;
	}

	@Override
	public int updateErpToClientMigrationMapping(JdbcTemplate clientAppDbJdbcTemplate, int clientId, int erpId,int userId,ETLAdmin eTLAdmin)
	{
		int count = 0;
		try {
			String sqlQuery = sqlHelper.getSql("updateErpToClientMigrationMapping");
			count = clientAppDbJdbcTemplate.update(sqlQuery,
					new Object[] { erpId, clientId,eTLAdmin.getModification().getCreatedBy(), eTLAdmin.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOGGER.error("error while updateErpToClientMigrationMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while updateErpToClientMigrationMapping()", e);
			
		}
		return count;
	}

	@Override
	public int wishListToWishListAccessOnlyMapping(String[] clientInfoArray, JdbcTemplate clientAppDbJdbcTemplate)
	{
		int insertCount = 0;
		try
		{
			clientAppDbJdbcTemplate.execute(" truncate table wishlistaccessonly ;");
			String selectInsertQuery = " INSERT INTO wishlistaccessonly (wishlistid,  userid,  viewdashboard,  cloudclientid,  "
					+ " cloudclientname, clientstatus ) SELECT  id, " + Integer.parseInt(clientInfoArray[0]) + "," +"'"+ clientInfoArray[1] +"'"+ "," +
					Integer.parseInt(clientInfoArray[2]) + "," +"'"+ clientInfoArray[3] + "'"+","
					+ "'"+clientInfoArray[4]+"'"+ " FROM  wishlist";
			insertCount = clientAppDbJdbcTemplate.update(selectInsertQuery);
		}
		catch ( DataAccessException ae )
		{
			LOGGER.error("error while wishListToWishListAccessOnlyMapping()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return insertCount;
	}

	@Override
	public List<String> getMasterTablesList(JdbcTemplate clientAppDbJdbcTemplate)
	{
		List<String> masterTablesList = null;
		try {
			String sql = sqlHelper.getSql("getMasterTablesList");
			masterTablesList = clientAppDbJdbcTemplate.query(sql, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("table_name");
				}
			});

		}  catch (DataAccessException ae) {
			LOGGER.error("error while Etladmin getMasterTablesList", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while Etladmin getMasterTablesList", e);
		}
		return masterTablesList;
	
	}

	@Override
	public Map<String, Integer> transeferDataERPtoClientForMasterTables(List<String> tablesList, int erpId,String clientId, JdbcTemplate clientAppDbJdbcTemplate)
	{
		Map<String, Integer> reslutMap = new HashMap<String, Integer>();
		try
		{
			for (String tableName : tablesList)
			{
				String selectQuery = "select count(*) from " + tableName + " where erp_id=" + erpId;
				int selectQueryCount = clientAppDbJdbcTemplate.queryForObject(selectQuery, Integer.class);
				if( selectQueryCount > 0 )
				{
					DatabaseMetaData  databaseMetaData = clientAppDbJdbcTemplate.getDataSource().getConnection().getMetaData();
					List<String> columnList = getColumnsByTable(tableName.replace("erp_", ""),databaseMetaData);
					StringJoiner columns = new StringJoiner(",");
					
					List<String> result = columnList.stream()                // convert list to stream
			                .filter(column -> !"id".equals(column))     // we dont like mkyong
			                .collect(Collectors.toList()); 
					
					result.forEach(column -> columns.add(column));
					String queryClomns = null;
					if(tableName.equals("erp_minidwm_client_currency_mapping"))
					{
						queryClomns = columns.toString().replace("clientId", "'"+clientId+"'");
						clientAppDbJdbcTemplate.execute("delete from " + tableName.replace("erp_", "") + " where clientId="+"'"+clientId+"'");
					}
					else
					{
						queryClomns = columns.toString().replace("client_id",clientId);
						clientAppDbJdbcTemplate.execute("delete from " + tableName.replace("erp_", "") + " where client_id="+clientId);
					}
					clientAppDbJdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
					String insertQry = "INSERT INTO " + tableName.replace("erp_", "") + "(" + columns.toString() + ")"  + " select " + queryClomns + "   from " + tableName + " where erp_id =" + erpId ;
					int count = clientAppDbJdbcTemplate.update(insertQry);
					System.out.println(count +" Records inserted into table " +tableName.replace("erp_", ""));
					reslutMap.put(tableName, count);
					clientAppDbJdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
				}
			}
		}
		catch ( DataAccessException ae )
		{
			LOGGER.error("error while Etladmin transeferDataERPtoClientForMasterTables", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SQLException e )
		{
			LOGGER.error("error while Etladmin transeferDataERPtoClientForMasterTables", e);
			throw new AnvizentRuntimeException(e);
		}
		return reslutMap;
	}

	@Override
	public int getErpIdFromNameAndVersion(JdbcTemplate clientAppDbJdbcTemplate, String name, String version)
	{
		int id = 0;
		try {
			String sql = sqlHelper.getSql("getErpIdFromNameAndVersion");
			id = clientAppDbJdbcTemplate.query(sql, new Object[] {name,version}, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs != null && rs.next()) {
						return rs.getInt("id");
					} else {
						return null;
					}
				}

			});
		} catch (DataAccessException ae) {
			LOGGER.error("error while getErpIdFromNameAndVersion()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOGGER.error("error while getErpIdFromNameAndVersion()", e);
			
		}
		return id;
	}

	@Override
	public int encryptWebServiceAuthParams(JdbcTemplate clientAppDbJdbcTemplate, String query)
	{
		int id = 0;
		try {
			id = clientAppDbJdbcTemplate.update(query);
		} catch (DataAccessException ae) {
			LOGGER.error("error while encryptWebServiceAuthParams()", ae);
			throw new AnvizentRuntimeException(ae);
		}  
		return id;
	}

}