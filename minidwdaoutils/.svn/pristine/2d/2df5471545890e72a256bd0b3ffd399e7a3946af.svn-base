/**
 * 
 */
package com.datamodel.anvizent.service.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.PlatformTransactionManager;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.service.dao.QuartzDao;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerTriggerInfo;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;
import com.datamodel.anvizent.service.model.SchedulerMaster;

public class QuartzDaoImpl extends JdbcDaoSupport implements QuartzDao {

	private SqlHelper sqlHelper;
	protected static final Log LOG = LogFactory.getLog(QuartzDaoImpl.class);
	PlatformTransactionManager transactionManager = null;

	public QuartzDaoImpl(DataSource appDatasource) {
		try {
			setDataSource(appDatasource);
			this.sqlHelper = new SqlHelper(QuartzDaoImpl.class);
			
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating QuartzDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public long startSchedulerInfo(QuartzSchedulerInfo quartzSchedulerInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		long scheduleId = -1;
		try {
			JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
			
			final QuartzSchedulerInfo quartzSchedulerObject = quartzSchedulerInfo;
			final String sql = sqlHelper.getSql("startSchedulerInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();

			requiredJdbcTemplete.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, quartzSchedulerObject.getName());
					ps.setString(2, quartzSchedulerInfo.getDescription());
					ps.setString(3, quartzSchedulerInfo.getStartTime());
					ps.setString(4, quartzSchedulerInfo.getShutdownTime());
					ps.setString(5, quartzSchedulerInfo.getTimezone());
					ps.setString(6, quartzSchedulerInfo.getIpAddress());
					ps.setLong(7, quartzSchedulerInfo.getMasterId());
					
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				scheduleId = autoIncrement.longValue();
			}

		} catch (DataAccessException ae) {
			LOG.error("error while startSchedulerInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while startSchedulerInfo()", e);
			;
		}

		return scheduleId;
	}

	@Override
	public int updateScheduledInfo(QuartzSchedulerInfo quartzSchedulerInfo, JdbcTemplate clientAppDbJdbcTemplate) {
		int updateScheduleInfo = 0;
		try {
			JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
			String sql = sqlHelper.getSql("updateScheduleInfo");
			updateScheduleInfo = requiredJdbcTemplete.update(sql,
					new Object[] { quartzSchedulerInfo.getShutdownTime(), quartzSchedulerInfo.getId() });
		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}
		return updateScheduleInfo;
	}

	@Override
	public long addSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		long jobId = -1;
		try {
			JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
			final QuartzSchedulerJobInfo quartzJobObject = quartzSchedulerJobInfo;
			final String sql = sqlHelper.getSql("addSchedulerJobInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();

			requiredJdbcTemplete.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "jobId" });
					ps.setLong(1, quartzJobObject.getQuartzSchedulerInfo().getId());
					ps.setString(2, quartzJobObject.getJobKeyName());
					ps.setString(3, quartzJobObject.getGroupName());
					ps.setString(4, quartzJobObject.getJobDescription());
					ps.setString(5, quartzJobObject.getJobStartTime());
					ps.setString(6, quartzJobObject.getJobEndTime());
					ps.setString(7, quartzJobObject.getCronExpression());
					ps.setString(8, quartzJobObject.getStatus());
					ps.setString(9, quartzJobObject.getNextFireTime());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				jobId = autoIncrement.longValue();
			}

		} catch (DataAccessException ae) {
			LOG.error("error while startSchedulerInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while startSchedulerInfo()", e);
			;
		}
		return jobId;
	}

	@Override
	public int updateSchedulerJobInfo(QuartzSchedulerJobInfo quartzSchedulerJobInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int updateJobInfo = 0;
		try {
			JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
			String sql = sqlHelper.getSql("updateSchedulerJobInfo");
			updateJobInfo = requiredJdbcTemplete.update(sql,
					new Object[] { quartzSchedulerJobInfo.getStatus(),quartzSchedulerJobInfo.getNextFireTime(), quartzSchedulerJobInfo.getJobId() });
		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}
		return updateJobInfo;
	}

	@Override
	public long addSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		long saveTriggerJobInfo = 0;
		try {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			String sql = sqlHelper.getSql("addSchedulerTriggerInfo");
			JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
			
			

			requiredJdbcTemplete.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "triggerId" });
					ps.setLong(1, quartzSchedulerTriggerInfo.getQuartzSchedulerJobInfo().getJobId());
					ps.setString(2, quartzSchedulerTriggerInfo.getDescription());
					ps.setString(3, quartzSchedulerTriggerInfo.getFireTime());
					ps.setString(4, quartzSchedulerTriggerInfo.getStartTime() );
					ps.setString(5, quartzSchedulerTriggerInfo.getEndTime() );
					ps.setString(6, quartzSchedulerTriggerInfo.getStatus() );
					return ps;
				}
			}, keyHolder);
			
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				saveTriggerJobInfo = autoIncrement.longValue();
			}
			
			
			// update job info 
			updateSchedulerJobInfoStatusWithNextFireTime(quartzSchedulerTriggerInfo.getQuartzSchedulerJobInfo(), clientAppDbJdbcTemplate );
			
		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}
		return saveTriggerJobInfo;
	}
	@Override
	public int updateSchedulerJobInfoStatusWithNextFireTime(QuartzSchedulerJobInfo quartzSchedulerJobInfo, JdbcTemplate clientAppDbJdbcTemplate) throws SqlNotFoundException {
		String sql = sqlHelper.getSql("updateSchedulerJobInfoStatusWithNextFireTime");
		JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
		requiredJdbcTemplete.update(sql, new Object[] {quartzSchedulerJobInfo.getStatus(),
				quartzSchedulerJobInfo.getNextFireTime(), quartzSchedulerJobInfo.getJobId()+"" });
		return 0;
	}

	@Override
	public int updateSchedulerTriggerInfo(QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		int updateTriggerInfo = 0;
		try {
			String sql = sqlHelper.getSql("updateSchedulerTriggerInfo");
			JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
			updateTriggerInfo = requiredJdbcTemplete.update(sql, new Object[] {quartzSchedulerTriggerInfo.getStatus(),
					quartzSchedulerTriggerInfo.getEndTime(), quartzSchedulerTriggerInfo.getTriggerId() });
			updateSchedulerJobInfoStatusWithNextFireTime(quartzSchedulerTriggerInfo.getQuartzSchedulerJobInfo(), clientAppDbJdbcTemplate);
		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}
		return updateTriggerInfo;
	}

	// @Override
	/*public List<QuartzSchedulerJobInfo> getScheduledJobsInfo(SchedulerFilterJobDetails schedulerFilterJobDetails,
			JdbcTemplate clientAppDbJdbcTemplate) {
		List<QuartzSchedulerJobInfo> scheduledJobInfo = null;

		try {
			JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
			List<Object> valueObject = new ArrayList<>();
			String sql = sqlHelper.getSql("getScheduledJobsInfo");
			
			
			if (schedulerFilterJobDetails.getSchedulerId() != null
					&& schedulerFilterJobDetails.getGroupNames().size() > 0) {
				String getParameterHolders = MinidwServiceUtil
						.getParamaterPlaceHolders(schedulerFilterJobDetails.getSchedulerId());
				sql += " and group_name in ( " + getParameterHolders + " ) ";
				valueObject.addAll(schedulerFilterJobDetails.getGroupNames());
			}
			
			
			valueObject.add(schedulerFilterJobDetails.getSchedulerId());
			if (StringUtils.isNotBlank(schedulerFilterJobDetails.getFromDate())) {
				sql += " and start_time = ? ";
				valueObject.add(schedulerFilterJobDetails.getFromDate());
			}
			if (schedulerFilterJobDetails.getGroupNames() != null
					&& schedulerFilterJobDetails.getGroupNames().size() > 0) {
				String getParameterHolders = MinidwServiceUtil
						.getParamaterPlaceHolders(schedulerFilterJobDetails.getGroupNames());
				sql += " and group_name in ( " + getParameterHolders + " ) ";
				valueObject.addAll(schedulerFilterJobDetails.getGroupNames());
			}
			if (StringUtils.isNotBlank(schedulerFilterJobDetails.getToDate())) {
				sql += " and end_time = ? ";
				valueObject.add(schedulerFilterJobDetails.getToDate());
			}

			if (schedulerFilterJobDetails.getStatus() != null && schedulerFilterJobDetails.getStatus().size() > 0) {
				String getParameterHolders = MinidwServiceUtil
						.getParamaterPlaceHolders(schedulerFilterJobDetails.getStatus());
				sql += " and status in ( " + getParameterHolders + " ) ";
				valueObject.addAll(schedulerFilterJobDetails.getStatus());
			}

			scheduledJobInfo = requiredJdbcTemplete.query(sql, valueObject.toArray(),
					new RowMapper<QuartzSchedulerJobInfo>() {

						@Override
						public QuartzSchedulerJobInfo mapRow(ResultSet rs, int i) throws SQLException {

							QuartzSchedulerJobInfo schedulerDetails = new QuartzSchedulerJobInfo();
							schedulerDetails.setJobKeyName(rs.getString("job_key_name"));
							schedulerDetails.setGroupName(rs.getString("group_name"));
							schedulerDetails.setJobStartTime(rs.getString("start_time"));
							schedulerDetails.setJobEndTime(rs.getString("end_time"));
							schedulerDetails.setStatus(rs.getString("status"));
							schedulerDetails.setNextFireTime(rs.getString("next_fire_time"));
							return schedulerDetails;

						}

					});
			//

		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}
		return scheduledJobInfo;
	}*/
	
	@Override
	public List<QuartzSchedulerJobInfo> getScheduledJobsInfo(SchedulerFilterJobDetails schedulerFilterJobDetails,
			JdbcTemplate clientAppDbJdbcTemplate) {
		List<QuartzSchedulerJobInfo> scheduledJobInfo = null;

		try {
			JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
			List<Object> valueObject = new ArrayList<>();
			String sql = sqlHelper.getSql("getScheduledJobsInfo");
			
			
			if (schedulerFilterJobDetails.getSchedulerId() != null) {
				String getParameterHolders = MinidwServiceUtil.getParamaterPlaceHolders(schedulerFilterJobDetails.getSchedulerId());
				sql += " where scheduler_id in ( " + getParameterHolders +" ) ";
				valueObject.addAll(schedulerFilterJobDetails.getSchedulerId());
			}
			
			
			if (StringUtils.isNotBlank(schedulerFilterJobDetails.getFromDate())) {
				sql += " and start_time = ? ";
				valueObject.add(schedulerFilterJobDetails.getFromDate());
			}
			/*if (schedulerFilterJobDetails.getGroupNames() != null
					&& schedulerFilterJobDetails.getGroupNames().size() > 0) {
				String getParameterHolders = MinidwServiceUtil
						.getParamaterPlaceHolders(schedulerFilterJobDetails.getGroupNames());
				sql += " and group_name in ( " + getParameterHolders + " ) ";
				valueObject.addAll(schedulerFilterJobDetails.getGroupNames());
			}*/
			if (StringUtils.isNotBlank(schedulerFilterJobDetails.getToDate())) {
				sql += " and end_time = ? ";
				valueObject.add(schedulerFilterJobDetails.getToDate());
			}

			if (schedulerFilterJobDetails.getStatus() != null && schedulerFilterJobDetails.getStatus().size() > 0) {
				String getParameterHolders = MinidwServiceUtil.getParamaterPlaceHolders(schedulerFilterJobDetails.getStatus());
				sql += " and status in ( " + getParameterHolders + " ) ";
				valueObject.addAll(schedulerFilterJobDetails.getStatus());
			}
			String timeZone = schedulerFilterJobDetails.getTimeZone();

			scheduledJobInfo = requiredJdbcTemplete.query(sql, valueObject.toArray(),
					new RowMapper<QuartzSchedulerJobInfo>() {

						@Override
						public QuartzSchedulerJobInfo mapRow(ResultSet rs, int i) throws SQLException {

							QuartzSchedulerJobInfo schedulerDetails = new QuartzSchedulerJobInfo();
							schedulerDetails.setSchedulerId(rs.getLong("scheduler_id"));
							schedulerDetails.setJobId(rs.getLong("job_id"));
							schedulerDetails.setJobKeyName(rs.getString("job_key_name"));
							schedulerDetails.setGroupName(rs.getString("group_name"));
							try {
								schedulerDetails.setJobStartTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(rs.getString("start_time"), timeZone));
								schedulerDetails.setJobEndTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(rs.getString("end_time"), timeZone));
								schedulerDetails.setNextFireTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(rs.getString("next_fire_time"), timeZone));
							} catch (ParseException e) {
								logger.error("unable parse job details jobs", e);
							}
							schedulerDetails.setStatus(rs.getString("status"));
							return schedulerDetails;

						}

					});
			//

		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}
		return scheduledJobInfo;
	}
	
	JdbcTemplate getRequiredJdbcTemplate (JdbcTemplate clientAppDbJdbcTemplate) {
		return clientAppDbJdbcTemplate != null ? clientAppDbJdbcTemplate : getJdbcTemplate();
	}

	@Override
	public List<QuartzSchedulerJobInfo> getScheduledJobsInfoById(long schedulerId,
			String timeZone,JdbcTemplate clientAppDbJdbcTemplate) {
		List<QuartzSchedulerJobInfo> quartzJobList = null;
		try {
			String sql = sqlHelper.getSql("getScheduledJobsInfoById");
			quartzJobList = clientAppDbJdbcTemplate.query(sql, new Object[] { schedulerId },new RowMapper<QuartzSchedulerJobInfo>() {
				@Override
				public QuartzSchedulerJobInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					QuartzSchedulerJobInfo quartzInfo = new QuartzSchedulerJobInfo();
					quartzInfo.setJobKeyName(rs.getString("job_key_name"));
					quartzInfo.setGroupName(rs.getString("group_name"));
					quartzInfo.setJobId(rs.getLong("job_id"));
					//quartzInfo.setJobDescription(rs.getString("job_description"));
					try {
						quartzInfo.setJobStartTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(rs.getString("start_time"), timeZone));
						quartzInfo.setJobEndTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(rs.getString("end_time"), timeZone));
						quartzInfo.setNextFireTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(rs.getString("next_fire_time"), timeZone));
					} catch (ParseException e) {
						LOG.error("",e);
					}
					quartzInfo.setStatus(rs.getString("status"));
					return quartzInfo;
				}
			});
			
		} catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("",e);;
		}
		return quartzJobList;
	}

	@Override
	public List<QuartzSchedulerJobInfo> getTriggeredInfoByID(long jobId, String timeZone,
			JdbcTemplate clientAppDbJdbcTemplate) {
		List<QuartzSchedulerJobInfo> quartzJobList = null;
		JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
		try {
			String sql = sqlHelper.getSql("getTriggeredInfoByID");
			quartzJobList = requiredJdbcTemplete.query(sql, new Object[] { jobId },new RowMapper<QuartzSchedulerJobInfo>() {
				@Override
				public QuartzSchedulerJobInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					QuartzSchedulerJobInfo quartzInfo = new QuartzSchedulerJobInfo();
					quartzInfo.setMasterId(rs.getLong("trigger_id"));
					quartzInfo.setJobDescription(rs.getString("description"));
					try {
						quartzInfo.setJobStartTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(rs.getString("start_time"), timeZone));
						quartzInfo.setJobEndTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(rs.getString("end_time"), timeZone));
						quartzInfo.setNextFireTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(rs.getString("fire_time"), timeZone));
					} catch (ParseException e) {
						LOG.error("",e);;
					}
					quartzInfo.setStatus(rs.getString("status"));
					return quartzInfo;
				}
			});
			
		} catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("",e);;
		}
		return quartzJobList;
	}
	
	@Override
	public int saveSchedulerSourceUploadQueue(SchedulerMaster master, List<PackageExecution> uploadQ)
			throws SqlNotFoundException {
		
		long uploadId = 0;
		try {
			
			final String sql = sqlHelper.getSql("saveSchedulerSourceUploadQueue");
			KeyHolder keyHolder = new GeneratedKeyHolder();

			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setLong(1, master.getId());
					ps.setLong(2, uploadQ.size());
					ps.setString(3, TimeZoneDateHelper.getFormattedDateString());
					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				uploadId = autoIncrement.longValue();
			}
			
			final String sqlfForList = sqlHelper.getSql("saveSchedulerSourceUploadQueueList");
			
			List<Object[]> batchArgs = new ArrayList<>();
			for (PackageExecution packageExecution : uploadQ) {
				batchArgs.add(new Object[] { uploadId, packageExecution.getClientId(),packageExecution.getPackageId(), packageExecution.getUploadStartDate() });
			}
			
			getJdbcTemplate().batchUpdate(sqlfForList, batchArgs, new int[] { Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.VARCHAR });
			
		} catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("",e);;
		}
		
		return 0;
	}
	
	@Override
	public List<PackageExecution> getStoredUploadQueueList(long masterId) {
		List<PackageExecution> uploadList = null;
		
		
		try {
			final String sql = sqlHelper.getSql("getStoredUploadQueueList");
			uploadList = getJdbcTemplate().query(sql, new Object[] { masterId },new RowMapper<PackageExecution>() {
				@Override
				public PackageExecution mapRow(ResultSet rs, int rowNum) throws SQLException {
					PackageExecution packageExecution = new PackageExecution();
					packageExecution.setClientId(rs.getString("client_id"));
					packageExecution.setPackageId(rs.getLong("package_id"));
					packageExecution.setUploadStartDate(rs.getString("uploadtime"));
					return packageExecution;
				}
			});
			
		} catch (DataAccessException ae) {
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("",e);;
		}
		return uploadList;
	}
	

}
