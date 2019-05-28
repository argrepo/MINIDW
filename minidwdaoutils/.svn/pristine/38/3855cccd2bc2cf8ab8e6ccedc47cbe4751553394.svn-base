/**
 * 
 */
package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateStatusUpdationException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.util.GetScheduledPackageWithCronExpressionExtracter;
import com.datamodel.anvizent.service.dao.util.GetScheduledPackageWithCronExpressionRowMapper;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientServerScheduler;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.IncrementalUpdate;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageRunNow;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;

/**
 * @author rakesh.gajula
 *
 */
@Component
public class ScheduleDaoImpl implements ScheduleDao {

	private SqlHelper sqlHelper;
	protected static final Log LOG = LogFactory.getLog(ScheduleDaoImpl.class);
	PlatformTransactionManager transactionManager = null;

	public ScheduleDaoImpl() {

		try {
			this.sqlHelper = new SqlHelper(ScheduleDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating ScheduleDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public int saveSchedule(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		int id = -1;
		try {
			final ClientData clientDataObject = clientData;
			final String sql = sqlHelper.getSql("savepackageschedule");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, clientDataObject.getUserId());
					ps.setInt(2, clientDataObject.getUserPackage().getPackageId());
					ps.setString(3, clientDataObject.getUserPackage().getScheduleType());
					ps.setString(4, clientDataObject.getSchedule().getScheduleStartDate());
					ps.setString(5, clientDataObject.getSchedule().getScheduleStartTime());
					ps.setString(6, clientDataObject.getSchedule().getRecurrencePattern());
					ps.setString(7, clientDataObject.getSchedule().getDaysToRun());
					ps.setString(8, clientDataObject.getSchedule().getWeeksToRun());
					ps.setString(9, clientDataObject.getSchedule().getDayOfMonth());
					ps.setString(10, clientDataObject.getSchedule().getMonthsToRun());
					ps.setString(11, clientDataObject.getSchedule().getDayOfYear());
					ps.setString(12, clientDataObject.getSchedule().getMonthOfYear());
					ps.setString(13, clientDataObject.getSchedule().getYearsToRun());
					ps.setBoolean(14, clientDataObject.getSchedule().getIsNoEndDate());
					ps.setString(15, clientDataObject.getSchedule().getScheduleEndDate());
					ps.setString(16, clientDataObject.getSchedule().getNoOfMaxOccurences());
					ps.setBoolean(17, clientDataObject.getModification().getIsActive());
					ps.setString(18, clientDataObject.getModification().getipAddress());
					ps.setString(19, clientDataObject.getModification().getCreatedBy());
					ps.setString(20, clientDataObject.getModification().getCreatedTime());
					ps.setString(21, clientData.getSchedule().getTimeZone());
					ps.setString(22, clientDataObject.getSchedule().getCronExpression());
					ps.setInt(23, clientDataObject.getDlInfo().getdL_id());
					ps.setInt(24, clientDataObject.getSchedule().getMinutesToRun());
					ps.setInt(25, clientDataObject.getSchedule().getPriority());
					ps.setString(26, clientDataObject.getSchedule().getTypeOfHours());
					ps.setString(27, clientDataObject.getSchedule().getHoursToRun());
					

					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}
		} catch (DataAccessException ae) {
			LOG.error("error while saveSchedule()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveSchedule()", e);
			
		}

		return id;
	}

	@Override
	public List<ClientData> getScheduledPackages(String clientId, JdbcTemplate clientJdbcTemplate) {

		List<ClientData> scheduledPackages = null;
		try {
			String sql = sqlHelper.getSql("getScheduledPackages");
			scheduledPackages = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<ClientData>() {
				public ClientData mapRow(ResultSet rs, int i) throws SQLException {
					ClientData packageSchedule = new ClientData();

					Package userPackage = new Package();
					userPackage.setPackageId(rs.getInt("package_ID"));
					packageSchedule.setUserPackage(userPackage);

					Schedule schedule = new Schedule();
					schedule.setScheduleId(rs.getInt("id"));
					schedule.setScheduleType(rs.getString("schedule_type"));
					schedule.setScheduleStartDate(rs.getString("schedule_start_date"));
					schedule.setScheduleStartTime(rs.getString("schedule_start_time"));
					schedule.setRecurrencePattern(rs.getString("recurrence_pattern"));
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
					packageSchedule.setSchedule(schedule);

					return packageSchedule;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getScheduledPackages()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getScheduledPackages()", e);
			
		}

		return scheduledPackages;
	}

	@Override
	public void updateStatusOfClientScheduler(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("updateStatusOfClientScheduler");
			int updatedCount = clientJdbcTemplate.update(sql,
					new Object[] { clientData.getSchedule().getCurrentScheduleEndTIme(),
							clientData.getClientServerScheduler().getS3BucketName(),
							clientData.getClientServerScheduler().getEncryptedFileName(),
							clientData.getClientServerScheduler().getClientSchedulerStatus(),
							clientData.getModification().getipAddress(), clientData.getModification().getModifiedBy(),
							clientData.getModification().getModifiedTime(),
							clientData.getIlConnectionMapping().getConnectionMappingId(),
							clientData.getClientServerScheduler().getId() });
			if (updatedCount == 0) {
				LOG.error("Duplicate updation found for schedue current ID "
						+ clientData.getClientServerScheduler().getId());
				throw new AnvizentDuplicateStatusUpdationException("Duplicate updation");
			}

		} catch (DataAccessException ae) {
			LOG.error("error while updateStatusOfClientScheduler()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateStatusOfClientScheduler()", e);
			
		}

	}

	@Override
	public List<ClientData> getUploadedFileInfo(String clientId, JdbcTemplate clientJdbcTemplate) {
		List<ClientData> uploadedFilesInfo = null;
		try {
			String sql = sqlHelper.getSql("getUploadedFileInfo");
			uploadedFilesInfo = clientJdbcTemplate.query(sql, new Object[] { Constants.Status.STATUS_DONE, clientId },
					new RowMapper<ClientData>() {
						public ClientData mapRow(ResultSet rs, int i) throws SQLException {
							ClientData uploadedFile = new ClientData();
							uploadedFile.setUserId(rs.getString("clientId"));

							ILConnectionMapping iLConnectionMapping = new ILConnectionMapping();
							iLConnectionMapping.setConnectionMappingId(rs.getInt("iL_connection_mapping_seq_id"));
							uploadedFile.setIlConnectionMapping(iLConnectionMapping);

							Schedule schedule = new Schedule();
							schedule.setScheduleId(rs.getInt("schedule_id"));
							uploadedFile.setSchedule(schedule);

							ClientServerScheduler scheduler = new ClientServerScheduler();
							scheduler.setS3BucketName(rs.getString("S3_bucket_name"));
							scheduler.setEncryptedFileName(rs.getString("encrypted_file_name"));
							scheduler.setClientSchedulerStatus(rs.getString("client_schedular_status"));
							scheduler.setServerSchedulerStatus(rs.getString("server_schedular_status"));
							scheduler.setRemarks(rs.getString("description"));

							uploadedFile.setClientServerScheduler(scheduler);
							return uploadedFile;
						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getUploadedFileInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getUploadedFileInfo()", e);
			
		}

		return uploadedFilesInfo;
	}

	@Override
	public void generateNextScheduleTime(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("generateNextScheduleTime");
			clientJdbcTemplate.update(sql,
					new Object[] { clientData.getUserId(), clientData.getUserPackage().getPackageId(),
							clientData.getIlConnectionMapping().getConnectionMappingId(),
							clientData.getSchedule().getScheduleId(),
							clientData.getSchedule().getCurrentScheduleStartTime(),
							clientData.getClientServerScheduler().getClientSchedulerStatus(),
							clientData.getClientServerScheduler().getServerSchedulerStatus(),
							clientData.getModification().getipAddress(), clientData.getModification().getCreatedBy(),
							clientData.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOG.error("error while generateNextScheduleTime()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while generateNextScheduleTime()", e);
			
		}
	}

	@Override
	public List<ClientData> getUploadedFileInfoByStatus(String clientSchedulerStatus, JdbcTemplate clientJdbcTemplate) {
		List<ClientData> uploadedFilesInfo = null;
		try {
			String sql = sqlHelper.getSql("getUploadedFileInfoByStatus");
			uploadedFilesInfo = clientJdbcTemplate.query(sql, new Object[] { clientSchedulerStatus },
					new RowMapper<ClientData>() {
						public ClientData mapRow(ResultSet rs, int i) throws SQLException {
							ClientData uploadedFile = new ClientData();
							uploadedFile.setUserId(rs.getString("clientId"));

							Package userPackage = new Package();
							userPackage.setPackageId(rs.getInt("package_Id"));
							uploadedFile.setUserPackage(userPackage);

							ILConnectionMapping iLConnectionMapping = new ILConnectionMapping();
							iLConnectionMapping.setConnectionMappingId(rs.getInt("iL_connection_mapping_seq_id"));
							uploadedFile.setIlConnectionMapping(iLConnectionMapping);

							Schedule schedule = new Schedule();
							schedule.setScheduleId(rs.getInt("schedule_id"));
							uploadedFile.setSchedule(schedule);

							ClientServerScheduler scheduler = new ClientServerScheduler();
							scheduler.setS3BucketName(rs.getString("S3_bucket_name"));
							scheduler.setEncryptedFileName(rs.getString("encrypted_file_name"));
							scheduler.setClientSchedulerStatus(rs.getString("client_schedular_status"));
							scheduler.setServerSchedulerStatus(rs.getString("server_schedular_status"));
							scheduler.setRemarks(rs.getString("description"));

							uploadedFile.setClientServerScheduler(scheduler);
							return uploadedFile;
						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getUploadedFileInfoForServerScheduler()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getUploadedFileInfoForServerScheduler()", e);
			
		}

		return uploadedFilesInfo;
	}

	@Override
	public void updateStatusOfServerScheduler(ClientData clientData, JdbcTemplate clientJdbcTemplate) {

		try {
			String sql = sqlHelper.getSql("updateStatusOfServerScheduler");
			clientJdbcTemplate.update(sql,
					new Object[] { clientData.getSchedule().getCurrentScheduleEndTIme(),
							clientData.getClientServerScheduler().getServerSchedulerStatus(),
							clientData.getModification().getipAddress(), clientData.getModification().getModifiedBy(),
							clientData.getModification().getModifiedTime(),
							clientData.getIlConnectionMapping().getConnectionMappingId() });
		} catch (DataAccessException ae) {
			LOG.error("error while updateStatusOfServerScheduler()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateStatusOfServerScheduler()", e);
			
		}

	}

	@Override
	public ClientData getScheduleById(int scheduleId, JdbcTemplate clientJdbcTemplate) {


		ClientData scheduleDate = null;
		try {
			String sql = sqlHelper.getSql("getScheduleById");
			scheduleDate = clientJdbcTemplate.query(sql, new Object[] { scheduleId },
					new ResultSetExtractor<ClientData>() {

						@Override
						public ClientData extractData(ResultSet rs) throws SQLException, DataAccessException {

							if (rs.next()) {
								ClientData packageSchedule = new ClientData();
								Schedule schedule = new Schedule();
								schedule.setScheduleId(rs.getInt("id"));
								schedule.setScheduleStartDate(rs.getString("schedule_start_date"));
								schedule.setScheduleStartTime(rs.getString("schedule_start_time"));
								schedule.setRecurrencePattern(rs.getString("recurrence_pattern"));
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
								packageSchedule.setSchedule(schedule);
								return packageSchedule;

							} else {
								return null;
							}

						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getScheduledPackages()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getScheduledPackages()", e);
			
		}

		return scheduleDate;
	}

	@Override
	public int updateScheduleCurrent(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		int i = -1;
		try {

			String sql = sqlHelper.getSql("updateScheduleCurrent");
			clientJdbcTemplate.update(sql,
					new Object[] { clientData.getUserId(), clientData.getIlConnectionMapping().getConnectionMappingId(),
							clientData.getSchedule().getScheduleId(),
							clientData.getSchedule().getCurrentScheduleStartTime(),
							clientData.getSchedule().getCurrentScheduleEndTIme(), null, null,
							clientData.getClientServerScheduler().getEncryptedFileName(),
							clientData.getClientServerScheduler().getClientSchedulerStatus(),
							clientData.getClientServerScheduler().getServerSchedulerStatus(), null, 1,
							clientData.getModification().getipAddress(), clientData.getModification().getCreatedBy(),
							clientData.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOG.error("error while updateScheduleCurrent()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateScheduleCurrent()", e);
			
		}
		return i;
	}

	@Override
	public List<ClientData> getScheduleCurrent(String clientId, JdbcTemplate clientJdbcTemplate) {

		List<ClientData> scheduleCurrentList = null;
		try {
			String sql = sqlHelper.getSql("getScheduleCurrent");

			scheduleCurrentList = clientJdbcTemplate.query(sql, new Object[] { clientId }, new RowMapper<ClientData>() {
				public ClientData mapRow(ResultSet rs, int i) throws SQLException {
					ClientData clientData = new ClientData();
					clientData.setUserId(clientId);

					ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
					ilConnectionMapping.setConnectionMappingId(rs.getInt("il_connection_mapping_id"));
					ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
					ilConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
					ilConnectionMapping.setiLquery(rs.getString("IL_query") != null ? (rs.getString("IL_query")) : "");
					ilConnectionMapping.setDatabaseName(
							rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
					ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
					ilConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));
					ilConnectionMapping.setiLId(rs.getInt("IL_id"));
					ilConnectionMapping.setdLId(rs.getInt("DL_id"));

					ilConnectionMapping.setIsIncrementalUpdate(rs.getBoolean("isIncrementalUpdate"));
					ilConnectionMapping.setMaxDateQuery(rs.getString("max_date_query"));

					ILConnection connection = new ILConnection();
					connection.setConnectionId(rs.getInt("connection_id"));
					connection.setConnectionName(
							rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
					connection.setConnectionType(
							rs.getString("connection_type") != null ? rs.getString("connection_type") : "");

					Database database = new Database();
					database.setId(rs.getInt("dataBaseId"));
					database.setName(rs.getString("name") != null ? rs.getString("name") : "");
					database.setDriverName(rs.getString("driver_name"));
					database.setProtocal(rs.getString("protocal"));
					database.setUrlFormat(rs.getString("url_format"));
					connection.setDatabase(database);
					connection.setServer(rs.getString("server") != null ? rs.getString("server") : "");
					connection.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
					connection.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
					ilConnectionMapping.setiLConnection(connection);
					clientData.setIlConnectionMapping(ilConnectionMapping);

					Schedule schedule = new Schedule();
					schedule.setScheduleId(rs.getInt("schedule_id"));
					schedule.setTimeZone(rs.getString("time_zone"));
					schedule.setCurrentScheduleStartTime(rs.getString("schedule_start_date_time"));
					clientData.setSchedule(schedule);

					ClientServerScheduler clientServerScheduler = new ClientServerScheduler();
					clientServerScheduler.setId(rs.getInt("id"));
					clientData.setClientServerScheduler(clientServerScheduler);

					return clientData;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getScheduleCurrent()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getScheduleCurrent()", e);
			
		}

		return scheduleCurrentList;

	}

	@Override
	public List<ClientData> getAllScheduledPackages(JdbcTemplate clientJdbcTemplate) {
		List<ClientData> schedulePackageList = null;
		try {
			String sql = sqlHelper.getSql("getAllScheduledPackages");

			schedulePackageList = clientJdbcTemplate.query(sql, new Object[] {}, new RowMapper<ClientData>() {
				public ClientData mapRow(ResultSet rs, int i) throws SQLException {
					ClientData clientData = new ClientData();
					clientData.setUserId(rs.getString("user_id"));

					Package userPackage = new Package();
					userPackage.setPackageId(rs.getInt("package_ID"));
					clientData.setUserPackage(userPackage);

					return clientData;
				}

			});

		} catch (DataAccessException ae) {
			LOG.error("error while getAllScheduledPackages()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getAllScheduledPackages()", e);
			
		}

		return schedulePackageList;
	}

	@Override
	public String getFilePathFromScheduleCurrent(String clientId, int packageId, int ilConnectionmappingId,
			JdbcTemplate clientJdbcTemplate) {

		String filePath = null;
		try {
			String sql = sqlHelper.getSql("getFilePathFromScheduleCurrent");
			filePath = clientJdbcTemplate.query(sql, new Object[] { clientId, packageId, ilConnectionmappingId },
					new ResultSetExtractor<String>() {

						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {

								String path = rs.getString("encrypted_file_name");

								return path;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getFilePathFromScheduleCurrent()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getFilePathFromScheduleCurrent()", e);
			
		}

		return filePath;
	}

	@Override
	public String getClientSchedulerStatus(String clientId, int packageId, int ilConnectionmappingId,
			JdbcTemplate clientJdbcTemplate) {
		String clientSchedulerStatus = null;
		try {
			String sql = sqlHelper.getSql("getClientSchedulerStatus");
			clientSchedulerStatus = clientJdbcTemplate.query(sql,
					new Object[] { clientId, packageId, ilConnectionmappingId }, new ResultSetExtractor<String>() {

						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {

								String status = rs.getString("client_schedular_status");

								return status;
							} else {
								return null;
							}
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getClientSchedulerStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getClientSchedulerStatus()", e);
			
		}

		return clientSchedulerStatus;
	}

	@Override
	public void deleteSchedule(int packageId, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("deleteSchedule");
			clientJdbcTemplate.update(sql, new Object[] { packageId });
		} catch (DataAccessException ae) {
			LOG.error("error while deleteSchedule()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while deleteSchedule()", e);
			
		}

	}

	@Override
	public void updateSchedule(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("updateSchedule");
			clientJdbcTemplate.update(sql,
					new Object[] { 
							clientData.getUserPackage().getScheduleType(),
							clientData.getSchedule().getScheduleStartDate(),
							clientData.getSchedule().getScheduleStartTime(),
							clientData.getSchedule().getRecurrencePattern(), clientData.getSchedule().getDaysToRun(),
							clientData.getSchedule().getWeeksToRun(), clientData.getSchedule().getDayOfMonth(),
							clientData.getSchedule().getMonthsToRun(), clientData.getSchedule().getDayOfYear(),
							clientData.getSchedule().getMonthOfYear(), clientData.getSchedule().getYearsToRun(),
							clientData.getSchedule().getIsNoEndDate(), clientData.getSchedule().getScheduleEndDate(),
							clientData.getSchedule().getNoOfMaxOccurences(),
							clientData.getModification().getipAddress(), clientData.getModification().getModifiedBy(),
							clientData.getModification().getModifiedTime(), clientData.getSchedule().getTimeZone(),
							clientData.getSchedule().getCronExpression(), 
							clientData.getSchedule().getMinutesToRun(),
							clientData.getSchedule().getPriority(),
							clientData.getSchedule().getTypeOfHours(),
							clientData.getSchedule().getHoursToRun(),
							clientData.getSchedule().getScheduleId() });
		} catch (DataAccessException ae) {
			LOG.error("error while updateSchedule()", ae);
			throw new AnvizentRuntimeException(ae.getRootCause().getMessage());
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateSchedule()", e);
			
		}
	}

	@Override
	public int updateHistoricalLoadExecutionStatus(Long historicalLoadId, boolean updateStatus,
			JdbcTemplate clientJdbcTemplate) {
		int updatedCount = 0;
		try {
			String sql = sqlHelper.getSql("updateHistoricalLoadExecutionStatus");
			updatedCount = clientJdbcTemplate.update(sql, new Object[] { updateStatus, historicalLoadId });
		} catch (DataAccessException ae) {
			LOG.error("error while updateHistoricalLoadExecutionStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateHistoricalLoadExecutionStatus()", e);
			
		}
		return updatedCount;
	}

	@Override
	public int updateHistoricalLoadRunningStatus(boolean updateStatus, Long historicalLoadId,
			JdbcTemplate clientJdbcTemplate) {
		int updatedCount = 0;
		try {
			String sql = sqlHelper.getSql("updateHistoricalLoadRunningStatus");
			updatedCount = clientJdbcTemplate.update(sql, new Object[] { updateStatus, historicalLoadId });
		} catch (DataAccessException ae) {
			LOG.error("error while deleteSchedule()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while deleteSchedule()", e);
			
		}
		return updatedCount;
	}

	@Override
	public ILConnectionMapping getILConnectionMappingByMappingId(int mappingId, JdbcTemplate clientJdbcTemplate) {

		ILConnectionMapping iLMapping = null;
		try {

			String sql = sqlHelper.getSql("getILConnectionMappingByMappingId");

			iLMapping = clientJdbcTemplate.query(sql, new Object[] { mappingId },
					new ResultSetExtractor<ILConnectionMapping>() {
						@Override
						public ILConnectionMapping extractData(ResultSet rs) throws SQLException, DataAccessException {

							if (rs != null && rs.next()) {
								ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
								try {

									ilConnectionMapping.setdLId(rs.getInt("DL_id"));
									ilConnectionMapping.setiLId(rs.getInt("IL_id"));
									ilConnectionMapping.setClientId(rs.getString("userid"));
									ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
									ilConnectionMapping.setConnectionMappingId(rs.getInt("id"));
									ilConnectionMapping.setIsWebservice(rs.getBoolean("isWebService"));
									ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));

									ILConnection iConnection = new ILConnection();

									Database database = new Database();
									database.setConnector_id(rs.getInt("connection_id"));
									iConnection.setDatabase(database);

									ilConnectionMapping.setiLConnection(iConnection);

									com.datamodel.anvizent.service.model.WebService webService = new com.datamodel.anvizent.service.model.WebService();
									webService.setWsConId(rs.getInt("webservice_Id"));

									ilConnectionMapping.setWebService(webService);

								} catch (Exception e) {
									LOG.error("error while getILConnectionMappingByMappingId()", e);
									
								}
								return ilConnectionMapping;
							} else {
								return null;
							}

						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getILConnectionMappingByMappingId()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILConnectionMappingByMappingId()", e);
			
		}

		return iLMapping;
	}

	@Override
	public IncrementalUpdate getIncrementalUpdate(ILConnectionMapping iLConnectionMapping,
			JdbcTemplate clientJdbcTemplate, String clientStagingSchema) {
		IncrementalUpdate incrementalUpdate = null;
		try {
			String sql = sqlHelper.getSql("getIncrementalUpdate");
			incrementalUpdate = clientJdbcTemplate.query(sql,
					new Object[] { iLConnectionMapping.getiLConnection().getDatabase().getConnector_id(),
							iLConnectionMapping.getiLId(), iLConnectionMapping.getTypeOfCommand(),iLConnectionMapping.getConnectionMappingId() },
					new ResultSetExtractor<IncrementalUpdate>() {

						@Override
						public IncrementalUpdate extractData(ResultSet rs) throws SQLException, DataAccessException {
							IncrementalUpdate incrementalUpdate = new IncrementalUpdate();
							while (rs.next()) {
								incrementalUpdate.setIncDateFromSource(rs.getString("inc_date_from_source"));
							}
							return incrementalUpdate;

						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getIncrementalUpdate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getIncrementalUpdate()", e);
			
		}
		return incrementalUpdate;
	}

	@Override
	public void saveIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbcTemplate,
			String clientStatgingSchema) {
		try {
			String sql = sqlHelper.getSql("saveIncrementalUpdate");
			clientJdbcTemplate.update(sql,
					new Object[] { iLConnectionMapping.getiLConnection().getDatabase().getConnector_id(),
							iLConnectionMapping.getTypeOfCommand(), 
							iLConnectionMapping.getiLId(),
							iLConnectionMapping.getConnectionMappingId(),
							iLConnectionMapping.getIncrementalDateValue(),
							iLConnectionMapping.getModification().getCreatedBy(),
							iLConnectionMapping.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOG.error("error while saveIncrementalUpdate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveIncrementalUpdate()", e);
			
		}
	}

	@Override
	public void updateIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate clientJdbcTemplate,
			String clientStatgingSchema) {
		try {
			String sql = sqlHelper.getSql("updateIncrementalUpdate");
			clientJdbcTemplate.update(sql,
					new Object[] { iLConnectionMapping.getIncrementalDateValue(),
							iLConnectionMapping.getModification().getCreatedBy(),
							iLConnectionMapping.getModification().getCreatedTime(),
							iLConnectionMapping.getiLConnection().getDatabase().getConnector_id(),
							iLConnectionMapping.getTypeOfCommand(),
							iLConnectionMapping.getiLId(),
							iLConnectionMapping.getConnectionMappingId(),
							iLConnectionMapping.getIncrementalDateValue() });
		} catch (DataAccessException ae) {
			LOG.error("error while updateIncrementalUpdate()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateIncrementalUpdate()", e);
			
		}
	}

	@Override
	public List<ClientData> getILConnectionMappingS3DetailsInfoByPackage(String userId, int packageId,
			Boolean isStandard, JdbcTemplate clientJdbcTemplate) {
		List<ClientData> clientDataList = null;
		try {
			String sql = sqlHelper.getSql("getILConnectionMappingS3DetailsInfoByPackage");
			clientDataList = clientJdbcTemplate.query(sql, new Object[] { packageId, userId },
					new RowMapper<ClientData>() {
						@Override
						public ClientData mapRow(ResultSet rs, int rowNum) throws SQLException {
							ClientData clientData = new ClientData();
							ILInfo ilInfo = new ILInfo();
							ilInfo.setiL_id(rs.getInt("IL_id"));
							// ilInfo.setiL_name(rs.getString("IL_name"));
							clientData.setIlInfo(ilInfo);
							ILConnectionMapping connMapping = new ILConnectionMapping();
							connMapping.setConnectionMappingId(rs.getInt("connection_mapping_id"));
							connMapping.setStorageType(rs.getString("storage_type"));
							connMapping.setFilePath(rs.getString("s3_file_path"));
							connMapping.setS3BucketId(rs.getLong("s3_bucket_id"));
							connMapping.setMultipartEnabled(rs.getBoolean("is_multipart_enabled"));
							connMapping.setFileSize(FileUtils.byteCountToDisplaySize(rs.getInt("file_size")));
							connMapping.setRowCount(rs.getInt("row_count"));
							connMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
							connMapping.setIsWebservice(rs.getBoolean("isWebService"));
							connMapping.setIsHavingParentTable(rs.getBoolean("isHavingParentTable"));
							Modification modification = new Modification();
							modification.setCreatedTime(rs.getString("uploaded_time"));
							connMapping.setModification(modification);
							clientData.setIlConnectionMapping(connMapping);
							return clientData;
						}
					});
		} catch (DataAccessException ae) {
			LOG.error("error while getILConnectionMappingS3DetailsInfoByPackage()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getILConnectionMappingS3DetailsInfoByPackage()", e);
			
		}
		return clientDataList;
	}


	@Override
	public int updateScheduleCurrentStatus(Integer scheduleCurrentId, String userId, Long packageId,
			Long ilConnectionMappingId, String clientSchedularStatus, String clientSchedularStatusDetails,
			String scheduleEndDateTime, Modification modification, JdbcTemplate clientJdbcTemplate) {
		int status = 0;
		try {
			String sql = sqlHelper.getSql("updateScheduleCurrentStatus");
			status = clientJdbcTemplate.update(sql,
					new Object[] { clientSchedularStatus, clientSchedularStatusDetails, scheduleEndDateTime,
							modification.getModifiedBy(), modification.getModifiedTime(), scheduleCurrentId, userId,
							packageId, ilConnectionMappingId });
		} catch (DataAccessException ae) {
			LOG.error("error while updateScheduleCurrentStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateScheduleCurrentStatus()", e);
			
		}
		return status;
	}

	@Override
	public ClientData getScheduleCurrentByScid(String clientId, String scheduleCurrentId,
			JdbcTemplate clientJdbcTemplate) {
		ClientData scheduleCurrentList = null;
		try {
			String sql = sqlHelper.getSql("getScheduleCurrentByScid");

			scheduleCurrentList = clientJdbcTemplate.query(sql, new Object[] { clientId, scheduleCurrentId },
					new ResultSetExtractor<ClientData>() {
						@Override
						public ClientData extractData(ResultSet rs) throws SQLException, DataAccessException {

							if (rs.next()) {

								ClientData clientData = new ClientData();
								clientData.setUserId(clientId);

								ILConnectionMapping ilConnectionMapping = new ILConnectionMapping();
								ilConnectionMapping.setConnectionMappingId(rs.getInt("il_connection_mapping_id"));
								ilConnectionMapping.setIsFlatFile(rs.getBoolean("isFlatFile"));
								ilConnectionMapping.setTypeOfCommand(rs.getString("type_of_command"));
								ilConnectionMapping
										.setiLquery(rs.getString("IL_query") != null ? (rs.getString("IL_query")) : "");
								ilConnectionMapping.setDatabaseName(
										rs.getString("Database_Name") != null ? (rs.getString("Database_Name")) : "");
								ilConnectionMapping.setPackageId(rs.getInt("Package_id"));
								ilConnectionMapping.setProcedureParameters(rs.getString("procedure_parameters"));
								ilConnectionMapping.setiLId(rs.getInt("IL_id"));
								ilConnectionMapping.setdLId(rs.getInt("DL_id"));

								ilConnectionMapping.setIsIncrementalUpdate(rs.getBoolean("isIncrementalUpdate"));
								ilConnectionMapping.setMaxDateQuery(rs.getString("max_date_query"));

								ILConnection connection = new ILConnection();
								connection.setConnectionId(rs.getInt("connection_id"));
								connection.setConnectionName(
										rs.getString("connection_name") != null ? rs.getString("connection_name") : "");
								connection.setConnectionType(
										rs.getString("connection_type") != null ? rs.getString("connection_type") : "");

								Database database = new Database();
								database.setId(rs.getInt("dataBaseId"));
								database.setName(rs.getString("name") != null ? rs.getString("name") : "");
								database.setDriverName(rs.getString("driver_name"));
								database.setProtocal(rs.getString("protocal"));
								database.setUrlFormat(rs.getString("url_format"));
								connection.setDatabase(database);
								connection.setServer(rs.getString("server") != null ? rs.getString("server") : "");
								connection
										.setUsername(rs.getString("username") != null ? rs.getString("username") : "");
								connection
										.setPassword(rs.getString("password") != null ? rs.getString("password") : "");
								ilConnectionMapping.setiLConnection(connection);
								clientData.setIlConnectionMapping(ilConnectionMapping);

								Schedule schedule = new Schedule();
								schedule.setScheduleId(rs.getInt("schedule_id"));
								schedule.setTimeZone(rs.getString("time_zone"));
								schedule.setCurrentScheduleStartTime(rs.getString("schedule_start_date_time"));
								clientData.setSchedule(schedule);

								ClientServerScheduler clientServerScheduler = new ClientServerScheduler();
								clientServerScheduler.setId(rs.getInt("id"));
								clientData.setClientServerScheduler(clientServerScheduler);

								return clientData;
							} else {
								return null;
							}

						}

					});

		} catch (DataAccessException ae) {
			LOG.error("error while getScheduleCurrent()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getScheduleCurrent()", e);
			
		}

		return scheduleCurrentList;

	}

	@Override
	public Map<String, Object> getScheduleCurrentDetailsById(Integer clientId, Integer scheduleCurrentId,
			JdbcTemplate clientJdbcTemplate) {

		Map<String, Object> map = null;

		try {
			String sql = sqlHelper.getSql("getScheduleCurrentDetailsById");
			map = clientJdbcTemplate.query(sql, new ResultSetExtractor<Map<String, Object>>() {

				@Override
				public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Map<String, Object> map = new HashMap<>();
					if (rs != null && rs.next()) {
						map.put("scheduleId", rs.getInt("schedule_id"));
						map.put("scheduleStartDateTime", rs.getString("schedule_start_date_time"));
					} else {
						return null;
					}
					return map;
				}

			}, scheduleCurrentId, clientId);

		} catch (DataAccessException ae) {
			LOG.error("error while getScheduleCurrentDetailsById()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getScheduleCurrentDetailsById()", e);
			
		}
		return map;
	}

	@Override
	public int updateJobExecutionDetails(JobExecutionInfo jobExecutionInfo, JdbcTemplate clientJdbcTemplate) {
		int count = -1;
		try {
			String sql = sqlHelper.getSql("updateJobExecutionDetails");
			count = clientJdbcTemplate.update(sql,
					new Object[] { jobExecutionInfo.getJobId(), jobExecutionInfo.getStatusCode(),
							jobExecutionInfo.getJobName(), jobExecutionInfo.getJobClass(),
							jobExecutionInfo.getDependencyJars(), jobExecutionInfo.getExecutionMessages(),
							jobExecutionInfo.getS3Path(), jobExecutionInfo.getModification().getCreatedBy(),
							jobExecutionInfo.getModification().getCreatedTime() });
		} catch (DataAccessException ae) {
			LOG.error("error while updateJobExecutionDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateJobExecutionDetails()", e);
			
		}
		return count;
	}

	@Override
	public List<JobResult> getJobResults(String clientId, String userId, String clientStagingSchemaName,
			JdbcTemplate clientJdbcTemplate) {
		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_%\\_IL_Currency\\_%";
			String sql = sqlHelper.getSql("getJobResults");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam }, new RowMapper<JobResult>() {
				public JobResult mapRow(ResultSet rs, int i) throws SQLException {
					JobResult jobresult = new JobResult();
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
	public int savePackageStatus(Integer userId, long packageId, Integer scheduleId, String package_status,
			int countOfilsprocessed, int countOfdlsprocesed, Modification modification,
			JdbcTemplate clientJdbcTemplate) {

		try {
			String sql = sqlHelper.getSql("savePackageStatus");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			/*
			 * clientJdbcTemplate.update(new PreparedStatementCreator() { public
			 * PreparedStatement createPreparedStatement(Connection connection)
			 * throws SQLException { PreparedStatement ps =
			 * connection.prepareStatement(sql, new String[] { "id" });
			 * 
			 * ps.setString(1, String.valueOf(userId)); ps.setLong(2,
			 * packageId); ps.setInt(3, scheduleId); ps.setString(4,
			 * package_status); ps.setInt(5, countOfilsprocessed); ps.setInt(6,
			 * countOfdlsprocesed); ps.setString(7,
			 * modification.getCreatedBy()); ps.setString(8,
			 * modification.getCreatedTime());
			 * 
			 * return ps; } }, keyHolder);
			 */

			clientJdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

					PreparedStatement ps = con.prepareStatement(sql, new String[] { "id" });

					ps.setString(1, String.valueOf(userId));
					ps.setLong(2, packageId);
					ps.setInt(3, scheduleId);
					ps.setString(4, package_status);
					ps.setInt(5, countOfilsprocessed);
					ps.setInt(6, countOfdlsprocesed);
					ps.setString(7, modification.getCreatedBy());
					ps.setString(8, modification.getCreatedTime());
					return ps;
				}
			}, keyHolder);

			if (keyHolder != null && keyHolder.getKey() != null) {
				Number autoIncrement = keyHolder.getKey();
				return autoIncrement.intValue();
			} else {
				throw new SqlNotFoundException("error while saving details");
			}
		} catch (DataAccessException ae) {
			LOG.error("error while savePackageStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while savePackageStatus()", e);
			
		}
		return -1;
	}

	@Override
	public void updatePackageStatus(int package_status_id, String package_status, int no_of_ils_processed,
			int no_of_dls_processed, Modification modification, JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("updatePackageStatus");
			clientJdbcTemplate.update(sql, new Object[] { package_status, no_of_ils_processed, no_of_dls_processed,
					modification.getModifiedBy(), modification.getModifiedTime(), package_status_id });

		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getViewResults()", e);
			
		}

	}

	@Override
	public void updateStatusOfServerSchedulerWithDetails(String currentScheduleEndTime, String serverSchedulerStatus,
			String serverSchedulerStatusDetails, String ipAddress, String modifiedBy, String modifiedTime,
			Integer ilConnectionMappingId, String scheduleDate, int packageId, JdbcTemplate clientJdbcTemplate) {

		if (serverSchedulerStatusDetails != null && serverSchedulerStatusDetails.length() > 65500) {
			serverSchedulerStatusDetails = serverSchedulerStatusDetails.substring(0, 65501);
		}

		try {
			String sql = sqlHelper.getSql("updateStatusOfServerSchedulerWithDetails");

			clientJdbcTemplate
					.update(sql,
							new Object[] { currentScheduleEndTime, serverSchedulerStatus, serverSchedulerStatusDetails,
									ipAddress, modifiedBy, modifiedTime, ilConnectionMappingId, scheduleDate,
									packageId });
		} catch (DataAccessException ae) {
			LOG.error("error while updateStatusOfServerSchedulerWithDetails()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getViewResults()", e);
			
		}

	}

	@Override
	public boolean deleteCustomTablesByPackageId(Package userpackage, String userId, JdbcTemplate clientJdbcTemplate) {
		boolean resultList = false;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		List<TableDerivative> derivedTables = userpackage.getDerivedTables();
		Table targetTable = userpackage.getTable();

		try {
			String queryForDeleteCustomPackageDerivedTableInfo = sqlHelper
					.getSql("deleteCustomPackageDerivedTableInfo");
			String queryForDeleteCustomPackageTargetTableInfo = sqlHelper.getSql("deleteCustomPackageTargetTableInfo");

			if (derivedTables != null && derivedTables.size() > 0) {
				String[] sqlDropDerivedTables = new String[derivedTables.size()];
				for (int i = 0; i < derivedTables.size(); i++) {
					sqlDropDerivedTables[i] = "drop table if exists " + derivedTables.get(i).getSchemaName() + "."
							+ derivedTables.get(i).getTableName();
				}
				clientJdbcTemplate.batchUpdate(sqlDropDerivedTables);

				clientJdbcTemplate.batchUpdate(queryForDeleteCustomPackageDerivedTableInfo,
						new BatchPreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps, int i) throws SQLException {
								TableDerivative tableDerivative = derivedTables.get(i);
								ps.setInt(1, tableDerivative.getTableId());
								ps.setInt(2, tableDerivative.getTargetTableId());
								ps.setLong(3, userpackage.getPackageId());
							}

							@Override
							public int getBatchSize() {
								return derivedTables.size();
							}
						});

			}

			if (targetTable != null && targetTable.getTableName() != null) {
				clientJdbcTemplate.update(
						"drop table if exists " + targetTable.getSchemaName() + "." + targetTable.getTableName());
				if (!userpackage.getFilesHavingSameColumns()) {
					String sqlTemp = sqlHelper.getSql("deleteCustomTempTablesQuery");
					clientJdbcTemplate.update(sqlTemp, userId, userpackage.getPackageId());
				}
				clientJdbcTemplate.update(queryForDeleteCustomPackageTargetTableInfo, targetTable.getTableId(),
						userpackage.getPackageId());
				String SqlStatus = sqlHelper.getSql("updatePackageMappingStatus");
				clientJdbcTemplate.update(SqlStatus, new Boolean(false), userpackage.getModification().getModifiedBy(),
						CommonDateHelper.formatDateAsString(new Date()), userpackage.getPackageId());
				String sqlScheduleStatus = sqlHelper.getSql("updatePackageScheduleStatus");
				clientJdbcTemplate.update(sqlScheduleStatus, new Boolean(false), Constants.Status.STATUS_PENDING,
						userpackage.getModification().getModifiedBy(), CommonDateHelper.formatDateAsString(new Date()),
						userpackage.getPackageId() );
			}
			resultList = true;
			transactionManager.commit(status);
		} catch (Exception e) {
			
			transactionManager.rollback(status);
		}
		return resultList;
	}

	@Override
	public String getFilePathFromScheduleCurrent(String clientId, long packageId, int ilConnectionmappingId,
			String scheduleDate, JdbcTemplate clientJdbcTemplate) {
		String filePath = null;
		try {
			String sql = sqlHelper.getSql("filePathFromScheduleCurrent");

			filePath = clientJdbcTemplate.query(sql,
					new Object[] { clientId, packageId, ilConnectionmappingId, scheduleDate },
					new ResultSetExtractor<String>() {
						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {

							return rs.getString("encrypted_file_name");
						}
					});

		} catch (DataAccessException ae) {
			LOG.error("error while getFilePathFromScheduleCurrent()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOG.error("error while getFilePathFromScheduleCurrent()", e);
			
		}

		return filePath;
	}

	@Override
	public void updatePackageErrortStatus(int package_status_id, String package_status, Modification modification,
			JdbcTemplate clientJdbcTemplate) {
		try {
			String sql = sqlHelper.getSql("updatePackageErrorStatus");
			clientJdbcTemplate.update(sql, new Object[] { package_status, modification.getModifiedBy(),
					modification.getModifiedTime(), package_status_id });

		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageErrotStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getViewResults()", e);
			
		}

	}

	public void updateStatusOfServerScheduler(ClientData clientData, String scheduleDate,
			JdbcTemplate clientJdbcTemplate) {

		try {
			String sql = sqlHelper.getSql("updateStatusOfServerScheduler");

			clientJdbcTemplate.update(sql,
					new Object[] { clientData.getSchedule().getCurrentScheduleEndTIme(),
							clientData.getClientServerScheduler().getServerSchedulerStatus(),
							clientData.getModification().getipAddress(), clientData.getModification().getModifiedBy(),
							clientData.getModification().getModifiedTime(),
							clientData.getIlConnectionMapping().getConnectionMappingId(), scheduleDate,
							clientData.getUserPackage().getPackageId() });
		} catch (DataAccessException ae) {
			LOG.error("error while updateStatusOfServerScheduler()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (Exception e) {
			LOG.error("error while updateStatusOfServerScheduler()", e);
			
		}

	}

	@Override
	public int saveDLInitiatingStatus(Integer userId, long packageId, Integer dl_Id, Integer scheduleId,
			String schedule_start_date_time, String DL_Job_Status, Modification modification,
			JdbcTemplate clientJdbcTemplate) {
		int id = 0;
		try {
			String sql = sqlHelper.getSql("saveDLInitiatingStatus");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			/*
			 * clientJdbcTemplate.update(new PreparedStatementCreator() { public
			 * PreparedStatement createPreparedStatement(Connection connection)
			 * throws SQLException { PreparedStatement ps =
			 * connection.prepareStatement(sql, new String[] { "id" });
			 * 
			 * ps.setString(1, String.valueOf(userId)); ps.setLong(2,
			 * packageId); ps.setInt(3, dl_Id); ps.setInt(4, scheduleId);
			 * ps.setString(5, schedule_start_date_time); ps.setString(6,
			 * DL_Job_Status); ps.setString(7, modification.getipAddress());
			 * ps.setString(8, modification.getCreatedBy()); ps.setString(9,
			 * modification.getCreatedTime());
			 * 
			 * return ps; } }, keyHolder);
			 */

			clientJdbcTemplate.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });

					ps.setString(1, String.valueOf(userId));
					ps.setLong(2, packageId);
					ps.setInt(3, dl_Id);
					ps.setInt(4, scheduleId);
					ps.setString(5, schedule_start_date_time);
					ps.setString(6, DL_Job_Status);
					ps.setString(7, modification.getipAddress());
					ps.setString(8, modification.getCreatedBy());
					ps.setString(9, modification.getCreatedTime());

					return ps;
				}
			}, keyHolder);

			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.intValue();
			}

		} catch (DataAccessException ae) {
			LOG.error("error while saveDLInitiatingStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while saveDLInitiatingStatus()", e);
			
		}
		return id;

	}

	@Override
	public void updateDLStatus(int schedule_current_seq_id, String schedule_end_date_time, String DL_Status,
			String errorDetails, Modification modification, JdbcTemplate clientJdbcTemplate) {
		if (errorDetails != null && errorDetails.length() > 65500) {
			errorDetails = errorDetails.substring(0, 65501);
		}

		try {
			String sql = sqlHelper.getSql("updateDLStatus");
			clientJdbcTemplate.update(sql, new Object[] { schedule_end_date_time, DL_Status, errorDetails,
					modification.getModifiedBy(), modification.getModifiedTime(), schedule_current_seq_id });

		} catch (DataAccessException ae) {
			LOG.error("error while updateDLStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateDLStatus()", e);
			
		}

	}

	@Override
	public int savePackageExectionInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {

		int executionId = -1;
		try {
			final PackageExecution packageExecutionObject = packageExecution;
			final String sql = sqlHelper.getSql("savePackageExectionInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			clientJdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "execution_id" });
					ps.setLong(1, packageExecutionObject.getPackageId());
					ps.setLong(2, packageExecutionObject.getScheduleId());
					ps.setString(3, packageExecutionObject.getInitiatedFrom());
					ps.setString(4, packageExecutionObject.getRunType());
					ps.setString(5, packageExecutionObject.getUploadStatus());
					ps.setString(6, packageExecutionObject.getUploadStartDate());
					ps.setString(7, packageExecutionObject.getTimeZone());
					ps.setString(8, packageExecutionObject.getModification().getCreatedBy());
					ps.setString(9, packageExecutionObject.getModification().getCreatedTime());
					ps.setString(10, packageExecutionObject.getUploadComments());
					ps.setString(11, packageExecutionObject.getLastUploadedDate());
					ps.setInt(12, packageExecutionObject.getDlId());
					ps.setString(13, packageExecutionObject.getDdlToRun());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				executionId = autoIncrement.intValue();
			}
		} catch (DuplicateKeyException ae) {
			LOG.error("error while savePackageExectionInfo()", ae);
			throw new AnvizentDuplicateFileNameException(ae);
		} catch (DataAccessException ae) {
			LOG.error("error while savePackageExectionInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while savePackageExectionInfo()", e);
			
		}

		return executionId;
	}

	@Override
	public int updatePackageExecutionUploadInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updatePackageExecutionUploadInfo");
			update = clientJdbcTemplate.update(sql,
					new Object[] { packageExecution.getUploadStatus(), "\n" + packageExecution.getUploadComments(),
							packageExecution.getLastUploadedDate(), packageExecution.getModification().getModifiedBy(),
							packageExecution.getModification().getModifiedTime(), packageExecution.getExecutionId() });

		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageExecutionUploadInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageExecutionUploadInfo()", e);
			
		}
		return update;
	}

	@Override
	public int updatePackageExecutionStatus(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updatePackageExecutionStatus");
			update = clientJdbcTemplate.update(sql,
					new Object[] { packageExecution.getExecutionStatus(), packageExecution.getExecutionComments(),
							packageExecution.getExecutionStartDate(),
							packageExecution.getModification().getModifiedBy(),
							packageExecution.getModification().getModifiedTime(), packageExecution.getExecutionId() });

		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageExecutionStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageExecutionStatus()", e);
			
		}
		return update;
	}

	@Override
	public int updatePackageExecutionStatusInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updatePackageExecutionStatusInfo");
			update = clientJdbcTemplate.update(sql,
					new Object[] { packageExecution.getExecutionStatus(),
							packageExecution.getExecutionComments() + "\n", packageExecution.getLastExecutedDate(),
							packageExecution.getModification().getModifiedBy(),
							packageExecution.getModification().getModifiedTime(), packageExecution.getExecutionId()

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
	public String processFileMappingWithIL(String filePath, String fileType, String separatorChar,
			String stringQuoteChar, List<String> iLColumnNames, List<String> selectedFileHeaders,
			List<String> dafaultValues, JdbcTemplate clientJdbcTemplate) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public List<JobResult> getJobResultsByDate(String clientId, String userId, String clientStagingSchemaName,
			String fromDate, String toDate, JdbcTemplate clientJdbcTemplate) {
		List<JobResult> resultList = null;
		try {
			String batchIdParam = clientId + "\\_%_IL_Currency\\_%";
			String sql = sqlHelper.getSql("getJobResultsByDate");
			resultList = clientJdbcTemplate.query(sql, new Object[] { batchIdParam, fromDate, toDate },
					new RowMapper<JobResult>() {
						public JobResult mapRow(ResultSet rs, int i) throws SQLException {
							JobResult jobresult = new JobResult();
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
	public int savePackageRunNowDetails(PackageRunNow packageRunNow, JdbcTemplate clientJdbcTemplate) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("savePackageRunNowDetails");

			clientJdbcTemplate.update(sql,
					new Object[] { packageRunNow.getPackageID(), packageRunNow.getStartTime(),
							packageRunNow.getEndTime(), packageRunNow.getNoOfILsProcessed(),
							packageRunNow.getNoOfDLsProcessed(), packageRunNow.getNoOfSourcesProcessed(),
							packageRunNow.getDetailedInformation(), packageRunNow.getStatus() });
		} catch (DataAccessException ae) {
			LOG.error("error while updateSchedule()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updateSchedule()", e);
			
		}

		return save;
	}

	@Override
	public List<Schedule> getPackagesForSchedulingByClientIdWithCronExpression(String clientId,
			JdbcTemplate clientJdbcTemplate) {
		List<Schedule> scheCronInfo = null;
		try {

			String sql = sqlHelper.getSql("getCronExpression");

			scheCronInfo = clientJdbcTemplate.query(sql, new GetScheduledPackageWithCronExpressionRowMapper());
		} catch (DataAccessException ae) {
			LOG.error("error while getCronExpression()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getCronExpression()", e);
			
		}
		return scheCronInfo;
	}
	
	@Override
	public int updatePackageExecutionMappingInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updatePackageExecutionMappingInfo");
			update = clientJdbcTemplate.update(sql,
					new Object[] { packageExecution.getExecutionStatus(),
							packageExecution.getExecutionComments() + "\n",
							packageExecution.getModification().getModifiedBy(),
							packageExecution.getModification().getModifiedTime(), packageExecution.getMappingId() });

		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageExecutionMappingInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageExecutionMappingInfo()", e);
			
		}
		return update;
	}

	@Override
	public int updatePackageExecutionMappingInfoStatus(PackageExecution packageExecution,
			JdbcTemplate clientJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updatePackageExecutionMappingInfoStatus");
			update = clientJdbcTemplate.update(sql,
					new Object[] { packageExecution.getExecutionStatus(),
							packageExecution.getExecutionComments() + "\n",
							packageExecution.getModification().getModifiedBy(),
							packageExecution.getModification().getModifiedTime(), packageExecution.getMappingId() });

		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageExecutionMappingInfoStatus()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageExecutionMappingInfoStatus()", e);
			
		}
		return update;
	}

	@Override
	public int updateDruidStartInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateDruidStartInfo");
			update = clientJdbcTemplate.update(sql,
					new Object[] {  packageExecution.getDruidStatus(),
						            packageExecution.getDruidComments()+ "\n", 
							        packageExecution.getDruidStartDate(),
							        packageExecution.getModification().getModifiedBy(),
							        packageExecution.getModification().getCreatedTime(),
							        packageExecution.getExecutionId()
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
	public int updateDruidEndInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateDruidEndInfo");
			update = clientJdbcTemplate.update(sql,
					new Object[] { packageExecution.getDruidStatus(),
				            packageExecution.getDruidComments() + "\n", 
					        packageExecution.getDruidEndDate(),
					        packageExecution.getModification().getModifiedBy(),
					        packageExecution.getModification().getCreatedTime(),
					        packageExecution.getExecutionId()

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
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void saveOrUpdateIncrementalUpdate(String incrementalDateValue, int iLConnectionMappingId, JdbcTemplate appDbJdbcTemplate, JdbcTemplate clientStagingjdbcTemplate) {
		
		ILConnectionMapping iLConnectionMapping = getILConnectionMappingByMappingId(iLConnectionMappingId , appDbJdbcTemplate);
		if (iLConnectionMapping != null) {

			String typeOfSource = null;
			ILConnection iLConnection = new ILConnection();
			Database database = new Database();
			if (!iLConnectionMapping.getIsFlatFile() && !iLConnectionMapping.getIsWebservice()) {
				typeOfSource = Constants.SourceType.DATABASE;

			} else {
				typeOfSource = Constants.SourceType.WEBSERVICE;
				database.setConnector_id(iLConnectionMapping.getWebService().getWsConId());
				iLConnection.setDatabase(database);
				iLConnectionMapping.setiLConnection(iLConnection);
			}

			iLConnectionMapping.setTypeOfCommand(typeOfSource);

			Modification modification = new Modification(new Date());
			modification.setCreatedBy("By RunNow/Schedule ");

			iLConnectionMapping.setIncrementalDateValue(incrementalDateValue);
			iLConnectionMapping.setTypeOfCommand(typeOfSource);
			iLConnectionMapping.setModification(modification);

			IncrementalUpdate incrementalUpdate =  getIncrementalUpdate(iLConnectionMapping, clientStagingjdbcTemplate, null);

			if (StringUtils.isNotBlank(incrementalUpdate.getIncDateFromSource())) {
				 updateIncrementalUpdate(iLConnectionMapping, clientStagingjdbcTemplate, null);
			} else {
				 saveIncrementalUpdate(iLConnectionMapping, clientStagingjdbcTemplate, null);
			}
		}
		
	}

	@Override
	public int updatePackageSchedule(Schedule schedule , JdbcTemplate clientJdbcTemplate) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updatePackageSchedule");
			update = clientJdbcTemplate.update(sql,
					new Object[] {
							schedule.getScheduleType(),
							schedule.getScheduleId(),
							schedule.getUserId(),
							schedule.getPackageId()});

		} catch (DataAccessException ae) {
			LOG.error("error while updatePackageExecutionStatusInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while updatePackageExecutionStatusInfo()", e);
			
		}
		return update;
	}
	
	
	@Override
	public Schedule getScheduledPackageInfo(PackageExecution packageExecution,
			JdbcTemplate clientJdbcTemplate) {
		Schedule scheCronInfo = null;
		try {

			String sql = sqlHelper.getSql("getScheduledPackageInfo");
			scheCronInfo = clientJdbcTemplate.query(sql, new Object[]{packageExecution.getPackageId()}, new GetScheduledPackageWithCronExpressionExtracter());
		} catch (DataAccessException ae) {
			LOG.error("error while getCronExpression()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getCronExpression()", e);
			
		}
		return scheCronInfo;
	}

	@Override
	public void saveOrUpdateIncrementalUpdate(String incrementalDateValue, int ilId, int connectionId,
			JdbcTemplate appDbJdbcTemplate, JdbcTemplate clinetStagingDbJdbcTemplate) {

		ILConnectionMapping iLConnectionMapping = new ILConnectionMapping();
		
		if (iLConnectionMapping != null) {

			String typeOfSource = null;
			ILConnection iLConnection = new ILConnection();
			Database database = new Database();
			typeOfSource = Constants.SourceType.DATABASE;
			database.setConnector_id(connectionId);
			iLConnection.setDatabase(database);
			iLConnectionMapping.setiLConnection(iLConnection);
			iLConnectionMapping.setiLId(ilId);
			iLConnectionMapping.setTypeOfCommand(typeOfSource);

			Modification modification = new Modification(new Date());
			modification.setCreatedBy("By HistoryLoad ");

			iLConnectionMapping.setIncrementalDateValue(incrementalDateValue);
			iLConnectionMapping.setTypeOfCommand(typeOfSource);
			iLConnectionMapping.setModification(modification);

			IncrementalUpdate incrementalUpdate =  getIncrementalUpdate(iLConnectionMapping, clinetStagingDbJdbcTemplate, null);

			if (StringUtils.isNotBlank(incrementalUpdate.getIncDateFromSource())) {
				 updateIncrementalUpdate(iLConnectionMapping, clinetStagingDbJdbcTemplate, null);
			} else {
				 saveIncrementalUpdate(iLConnectionMapping, clinetStagingDbJdbcTemplate, null);
			}
		}
	}

	@Override
	public List<Integer> getPackageSourceMappingListByDlId(int dlId, JdbcTemplate clientAppJdbcTemplate)
	{
		List<Integer> mappingList = null;
		try
		{
			String sql = sqlHelper.getSql("getPackageSourceMappingListByDlId");
			mappingList = clientAppJdbcTemplate.query(sql, new Object[] { dlId }, new RowMapper<Integer>()
			{
				public Integer mapRow(ResultSet rs, int i) throws SQLException
				{
					return rs.getInt("id");
				}
			});
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while getPackageSourceMappingListByDlId()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		catch ( SqlNotFoundException e )
		{
			LOG.error("error while getPackageSourceMappingListByDlId()", e);
		}
		return mappingList;
	}

	@Override
	public void updateRetryPaginationToNull(List<Integer> mappingList, JdbcTemplate clientAppJdbcTemplate)
	{
		try
		{
			StringJoiner mappingIds = new StringJoiner(",");
			mappingList.forEach(mappingId -> mappingIds.add(mappingId.toString()));
			String updateQuery = "update minidwcs_package_web_service_source_mapping set retry_pagination = NULL where il_connection_mapping_id in(" + mappingIds + ")";
			clientAppJdbcTemplate.update(updateQuery);
		}
		catch ( DataAccessException ae )
		{
			LOG.error("error while updateRetryPaginationToNull()", ae);
			throw new AnvizentRuntimeException(ae);
		}
	}
}
