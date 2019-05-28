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
import org.springframework.transaction.PlatformTransactionManager;

import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.sql.SqlHelper;
import com.datamodel.anvizent.common.sql.SqlNotFoundException;
import com.datamodel.anvizent.security.AESConverter;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.dao.util.CloudClientMapper;
import com.datamodel.anvizent.service.dao.util.SchedulerSlaveMapper;
import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.AwsRegions;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.datamodel.anvizent.service.model.SchedulerType;

public class CommonDaoImpl extends JdbcDaoSupport implements CommonDao {

	private SqlHelper sqlHelper;
	protected static final Log LOG = LogFactory.getLog(CommonDaoImpl.class);
	PlatformTransactionManager transactionManager = null;

	public CommonDaoImpl(DataSource dataSource) {
		setDataSource(dataSource);
		try {
			this.sqlHelper = new SqlHelper(CommonDaoImpl.class);
		} catch (SQLException ex) {
			throw new DataAccessResourceFailureException("Error creating AwsDaoImpl SqlHelper.", ex);
		}
	}

	@Override
	public int addAwsCredentialsInfo(AwsCredentials awsCredentials) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("addAwsCredentialsInfo");
			save = getJdbcTemplate().update(sql, new Object[] { awsCredentials.getName(), awsCredentials.getSecretKey(),
					awsCredentials.getAccessKey(), awsCredentials.getRegion() });
		} catch (Exception e) {
			LOG.error("", e);
		}
		return save;
	}

	@Override
	public int updateAwsCredentialsInfo(AwsCredentials awsCredentials) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateAwsCredentialsInfo");
			update = getJdbcTemplate()
					.update(sql,
							new Object[] { awsCredentials.getName(), awsCredentials.getSecretKey(),
									awsCredentials.getAccessKey(), awsCredentials.getRegion(),
									awsCredentials.getId() });
		} catch (Exception e) {
			LOG.error("", e);
		}
		return update;
	}

	@Override
	public List<AwsCredentials> getAwsCredentialsList() {
		List<AwsCredentials> awsCredentialsList = null;
		try {
			String sql = sqlHelper.getSql("getAwsCredentialsList");

			awsCredentialsList = getJdbcTemplate().query(sql, new RowMapper<AwsCredentials>() {
				@Override
				public AwsCredentials mapRow(ResultSet rs, int rowNum) throws SQLException {
					AwsCredentials awsCredentials = new AwsCredentials();
					awsCredentials.setId(rs.getInt("id"));
					awsCredentials.setName(rs.getString("name"));
					awsCredentials.setAccessKey(rs.getString("access_key"));
					awsCredentials.setSecretKey(rs.getString("secret_key"));
					awsCredentials.setRegion(rs.getString("region"));
					return awsCredentials;
				}
			});
		} catch (SqlNotFoundException e) {
			throw new DataAccessException(e.getMessage()) {
				private static final long serialVersionUID = 1L;
			};
		}
		return awsCredentialsList;
	}

	@Override
	public AwsCredentials getAwsCredentialsListById(long id) {
		AwsCredentials awsCredentials = null;
		try {
			String sql = sqlHelper.getSql("getAwsCredentialsListById");
			awsCredentials = getJdbcTemplate().query(sql, new Object[] { id },
					new ResultSetExtractor<AwsCredentials>() {

						@Override
						public AwsCredentials extractData(ResultSet rs) throws SQLException, DataAccessException {
							AwsCredentials awsInfo = new AwsCredentials();
							if (rs != null && rs.next()) {
								awsInfo.setId(rs.getInt("id"));
								awsInfo.setName(rs.getString("name"));
								awsInfo.setAccessKey(rs.getString("access_key"));
								awsInfo.setSecretKey(rs.getString("secret_key"));
								awsInfo.setRegion(rs.getString("region"));
								return awsInfo;
							} else {
								return null;
							}
						}

					});

		} catch (Exception e) {
			LOG.error("", e);
		}
		return awsCredentials;
	}

	@Override
	public int addServerMasterInfo(SchedulerMaster schedulerMaster) {
		long id = 0;
		try {
			String sql = sqlHelper.getSql("addSchedulerMasterInfo");
			KeyHolder keyHolder = new GeneratedKeyHolder();
			if (schedulerMaster.getType() == 1) {
				schedulerMaster.setIpAddress(null);
			} else if (schedulerMaster.getType() == 2) {
				schedulerMaster.getAws().setId(0);
				schedulerMaster.setInstanceId(null);
			}
			getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
					ps.setString(1, schedulerMaster.getName());
					ps.setInt(2, schedulerMaster.getType());
					ps.setLong(3, schedulerMaster.getAws().getId());
					ps.setString(4, schedulerMaster.getInstanceId());
					ps.setBoolean(5, schedulerMaster.isActive());
					ps.setString(6, schedulerMaster.getIpAddress());
					ps.setLong(7, schedulerMaster.getPackageUploadInterval());
					ps.setLong(8, schedulerMaster.getPackageExecutionInterval());
					ps.setLong(9, schedulerMaster.getHistoryLoadInterval());
					ps.setLong(10, schedulerMaster.getHistoryExecutionInterval());
					ps.setString(11, schedulerMaster.getRequestSchema());
					ps.setLong(12, schedulerMaster.getThreadCount());
					ps.setLong(13, schedulerMaster.getSourceUploadInterval());
					ps.setLong(14, schedulerMaster.getSourceExecutionInterval());
					ps.setLong(15, schedulerMaster.getSlaveHeartBeatInterval());
					ps.setBoolean(16, schedulerMaster.isCurrencyLoad());

					return ps;
				}
			}, keyHolder);
			if (keyHolder != null) {
				Number autoIncrement = keyHolder.getKey();
				id = autoIncrement.longValue();
			}
			addAndDeleteSchedulerMasterMapping(id, (List<SchedulerSlave>) schedulerMaster.getSchedulerSlaves());
			addAndDeleteSchedulerMasterClientMapping(id, schedulerMaster.getClientIds());

		} catch (Exception e) {
			LOG.error("", e);
		}
		return (int) id;
	}

	@Override
	public int updateServerMasterInfo(SchedulerMaster schedulerMaster) {
		int update = 0;
		try {
			if (schedulerMaster.getType() == 1) {
				schedulerMaster.setIpAddress(null);
			} else if (schedulerMaster.getType() == 2) {
				schedulerMaster.getAws().setId(0);
				schedulerMaster.setInstanceId(null);
			}
			String sql = sqlHelper.getSql("updateSchedulerMasterInfo");
			update = getJdbcTemplate().update(sql,
					new Object[] { schedulerMaster.getName(), schedulerMaster.getType(),
							schedulerMaster.getAws().getId(), schedulerMaster.getInstanceId(),
							schedulerMaster.isActive(), schedulerMaster.getIpAddress(),
							schedulerMaster.getPackageUploadInterval(),
							schedulerMaster.getPackageExecutionInterval(),
							schedulerMaster.getHistoryLoadInterval(),
							schedulerMaster.getHistoryExecutionInterval(),
							schedulerMaster.getRequestSchema(),
							schedulerMaster.getThreadCount(),
							schedulerMaster.getSourceUploadInterval(),
							schedulerMaster.getSourceExecutionInterval(),
							schedulerMaster.getSlaveHeartBeatInterval(),
							schedulerMaster.isCurrencyLoad(),
							schedulerMaster.getId()

					});
			addAndDeleteSchedulerMasterMapping(schedulerMaster.getId(), schedulerMaster.getSchedulerSlaves());
			addAndDeleteSchedulerMasterClientMapping(schedulerMaster.getId(), schedulerMaster.getClientIds());

		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}
		return update;
	}

	@Override
	public List<SchedulerMaster> getSchedulerMasterInfo() {
		List<SchedulerMaster> serverMasterList = null;
		try {
			String sql = sqlHelper.getSql("getSchedulerMasterInfo");

			serverMasterList = getJdbcTemplate().query(sql, new RowMapper<SchedulerMaster>() {
				@Override
				public SchedulerMaster mapRow(ResultSet rs, int rowNum) throws SQLException {
					SchedulerMaster masterInfo = new SchedulerMaster();
					long id = rs.getLong("id");
					masterInfo.setId(id);
					masterInfo.setName(rs.getString("name"));
					masterInfo.setType(rs.getInt("type"));
					AwsCredentials aws = new AwsCredentials();
					aws.setId(rs.getInt("aws_id"));
					aws.setName(rs.getString("aws_name"));
					aws.setSecretKey(rs.getString("secret_key"));
					aws.setAccessKey(rs.getString("access_key"));
					aws.setRegion(rs.getString("region"));
					masterInfo.setAws(aws);
					masterInfo.setInstanceId(rs.getString("instance_id"));
					masterInfo.setActive(rs.getBoolean("is_active"));
					masterInfo.setClientIds(getSchedulerMasterClientMapping(id));
					masterInfo.setSchedulerSlaves(getSchedulerMasterSlaveMapping(id));
					masterInfo.setIpAddress(rs.getString("ip_address"));
					masterInfo.setPackageUploadInterval(rs.getLong("package_upload_interval"));
					masterInfo.setPackageExecutionInterval(rs.getLong("package_execution_interval"));
					masterInfo.setHistoryLoadInterval(rs.getLong("history_upload_interval"));
					masterInfo.setHistoryExecutionInterval(rs.getLong("history_execution_interval"));
					masterInfo.setRequestSchema(rs.getString("request_schema"));
					masterInfo.setThreadCount(rs.getLong("thread_count"));
					masterInfo.setSourceUploadInterval(rs.getLong("source_upload_interval"));
					masterInfo.setSourceExecutionInterval(rs.getLong("source_execution_interval"));
					masterInfo.setSlaveHeartBeatInterval(rs.getLong("slave_heartbeat_interval"));
					return masterInfo;
				}
			});
		} catch (SqlNotFoundException e) {
			throw new DataAccessException(e.getMessage()) {
				private static final long serialVersionUID = 1L;
			};
		}
		return serverMasterList;
	}

	@Override
	public SchedulerMaster getSchedulerMasterById(long id) {
		SchedulerMaster serverMasterInfo = null;
		try {
			String sql = sqlHelper.getSql("getSchedulerMasterById");
			serverMasterInfo = getJdbcTemplate().query(sql, new Object[] { id },
					new ResultSetExtractor<SchedulerMaster>() {

						@Override
						public SchedulerMaster extractData(ResultSet rs) throws SQLException, DataAccessException {
							SchedulerMaster masterInfo = new SchedulerMaster();
							if (rs != null && rs.next()) {
								long id = rs.getLong("id");
								masterInfo.setId(id);
								masterInfo.setName(rs.getString("name"));
								masterInfo.setType(rs.getInt("type"));
								AwsCredentials aws = new AwsCredentials();
								aws.setId(rs.getInt("aws_id"));
								aws.setName(rs.getString("aws_name"));
								aws.setSecretKey(rs.getString("secret_key"));
								aws.setAccessKey(rs.getString("access_key"));
								aws.setRegion(rs.getString("region"));
								masterInfo.setAws(aws);
								masterInfo.setInstanceId(rs.getString("instance_id"));
								masterInfo.setActive(rs.getBoolean("is_active"));
								masterInfo.setSchedulerSlaves(getSchedulerMasterSlaveMapping(id));
								masterInfo.setClientIds(getSchedulerMasterClientMapping(id));
								masterInfo.setIpAddress(rs.getString("ip_address"));
								masterInfo.setPackageUploadInterval(rs.getLong("package_upload_interval"));
								masterInfo.setPackageExecutionInterval(rs.getLong("package_execution_interval"));
								masterInfo.setHistoryLoadInterval(rs.getLong("history_upload_interval"));
								masterInfo.setHistoryExecutionInterval(rs.getLong("history_execution_interval"));
								masterInfo.setRequestSchema(rs.getString("request_schema"));
								masterInfo.setThreadCount(rs.getLong("thread_count"));
								masterInfo.setSourceUploadInterval(rs.getLong("source_upload_interval"));
								masterInfo.setSourceExecutionInterval(rs.getLong("source_execution_interval"));
								masterInfo.setSlaveHeartBeatInterval(rs.getLong("slave_heartbeat_interval"));
								masterInfo.setCurrencyLoad(rs.getBoolean("is_currency_load_required")); 
								
								return masterInfo;
							} else {
								return null;
							}
						}

					});

		} catch (DataAccessException | SqlNotFoundException e) {
			throw new AnvizentRuntimeException("Master exception :" + e.getMessage());
		}
		return serverMasterInfo;
	}

	@Override
	public int addServerSlaveInfo(SchedulerSlave schedulerSlave) {
		int save = 0;

		try {
			if (schedulerSlave.getType() == 1) {
				schedulerSlave.setIpAddress(null);
			} else if (schedulerSlave.getType() == 2) {
				schedulerSlave.getAws().setId(0);
				schedulerSlave.setInstanceId(null);
			}
			String sql = sqlHelper.getSql("addSchedulerSlaveInfo");

			save = getJdbcTemplate().update(sql,
					new Object[] { schedulerSlave.getName(), schedulerSlave.getType(), schedulerSlave.getInstanceId(),
							schedulerSlave.getAws().getId(), schedulerSlave.getIpAddress(),
							schedulerSlave.getFileUploadCount(), schedulerSlave.getPackageExecutionCount(),
							schedulerSlave.getHistoryLoadCount(), schedulerSlave.getHistoryExecutionCount(),
							schedulerSlave.isActive(),
							schedulerSlave.getRequestSchema()});
		} catch (Exception e) {
			LOG.error("", e);
		}
		return save;
	}

	@Override
	public int updateServerSlaveInfo(SchedulerSlave schedulerSlave) {
		int update = 0;
		try {
			if (schedulerSlave.getType() == 1) {
				schedulerSlave.setIpAddress(null);
			} else if (schedulerSlave.getType() == 2) {
				schedulerSlave.getAws().setId(0);
				schedulerSlave.setInstanceId(null);
			}
			String sql = sqlHelper.getSql("updateSchedulerSlaveInfo");
			update = getJdbcTemplate().update(sql,
					new Object[] { schedulerSlave.getType(), schedulerSlave.getName(), schedulerSlave.getAws().getId(),
							schedulerSlave.getInstanceId(), schedulerSlave.getFileUploadCount(),
							schedulerSlave.getPackageExecutionCount(), schedulerSlave.getHistoryLoadCount(),
							schedulerSlave.getHistoryExecutionCount(), schedulerSlave.isActive(),
							schedulerSlave.getIpAddress(), 
							schedulerSlave.getRequestSchema(),
							schedulerSlave.getId() });
		} catch (Exception e) {
			LOG.error("", e);
		}
		return update;
	}

	@Override
	public List<SchedulerSlave> getSchedulerSlaveInfo() {
		List<SchedulerSlave> serverList = null;
		try {
			String sql = sqlHelper.getSql("getSchedulerSlaveInfo");
			serverList = getJdbcTemplate().query(sql, new RowMapper<SchedulerSlave>() {
				@Override
				public SchedulerSlave mapRow(ResultSet rs, int rowNum) throws SQLException {
					SchedulerSlave slaveInfo = new SchedulerSlave();
					slaveInfo.setId(rs.getLong("id"));
					slaveInfo.setName(rs.getString("name"));
					slaveInfo.setType(rs.getInt("type"));
					AwsCredentials aws = new AwsCredentials();
					aws.setId(rs.getInt("aws_id"));
					aws.setName(rs.getString("aws_name"));
					aws.setSecretKey(rs.getString("secret_key"));
					aws.setAccessKey(rs.getString("access_key"));
					aws.setRegion(rs.getString("region"));
					slaveInfo.setAws(aws);
					slaveInfo.setIpAddress(rs.getString("ip_address"));
					slaveInfo.setInstanceId(rs.getString("instance_id"));
					slaveInfo.setPackageExecutionCount(rs.getInt("no_of_package_executions"));
					slaveInfo.setFileUploadCount(rs.getInt("no_of_package_source_uploads"));
					slaveInfo.setHistoryLoadCount(rs.getInt("no_of_history_load_source_uploads"));
					slaveInfo.setHistoryExecutionCount(rs.getInt("no_of_history_load_executions"));
					slaveInfo.setActive(rs.getBoolean("is_active"));
					slaveInfo.setRequestSchema(rs.getString("request_schema"));
					return slaveInfo;
				}

			});
		} catch (Exception e) {
			LOG.error("", e);
		}
		return serverList;
	}

	@Override
	public SchedulerSlave getServerSlaveById(long id) {
		SchedulerSlave serverSlaveInfo = null;
		try {
			String sql = sqlHelper.getSql("getSchedulerSlaveById");
			serverSlaveInfo = getJdbcTemplate().query(sql, new Object[] { id },
					new ResultSetExtractor<SchedulerSlave>() {

						@Override
						public SchedulerSlave extractData(ResultSet rs) throws SQLException, DataAccessException {
							SchedulerSlave slaveInfo = new SchedulerSlave();
							if (rs != null && rs.next()) {
								slaveInfo.setId(rs.getLong("id"));
								slaveInfo.setType(rs.getInt("type"));
								slaveInfo.setName(rs.getString("name"));
								slaveInfo.setInstanceId(rs.getString("instance_id"));
								AwsCredentials aws = new AwsCredentials();
								aws.setId(rs.getInt("aws_id"));
								aws.setName(rs.getString("aws_name"));
								aws.setSecretKey(rs.getString("secret_key"));
								aws.setAccessKey(rs.getString("access_key"));
								aws.setRegion(rs.getString("region"));
								slaveInfo.setAws(aws);
								slaveInfo.setIpAddress(rs.getString("ip_address"));
								slaveInfo.setPackageExecutionCount(rs.getInt("no_of_package_executions"));
								slaveInfo.setFileUploadCount(rs.getInt("no_of_package_source_uploads"));
								slaveInfo.setHistoryLoadCount(rs.getInt("no_of_history_load_source_uploads"));
								slaveInfo.setHistoryExecutionCount(rs.getInt("no_of_history_load_executions"));
								slaveInfo.setActive(rs.getBoolean("is_active"));
								slaveInfo.setRequestSchema(rs.getString("request_schema"));
								return slaveInfo;
							} else {
								return null;
							}
						}

					});

		} catch (Exception e) {
			LOG.error("", e);
		}
		return serverSlaveInfo;
	}

	@Override
	public int addAndDeleteSchedulerMasterMapping(long id, List<SchedulerSlave> schedulerSlaves) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("deleteSchedulerMasterSlaveMapping");
			getJdbcTemplate().update(sql, new Object[] { id });

			String addSql = sqlHelper.getSql("addSchedulerMasterSlaveMapping");

			List<Object[]> batchArgs = new ArrayList<>();
			for (SchedulerSlave schedulerServerSlave : schedulerSlaves) {
				batchArgs.add(new Object[] { id, schedulerServerSlave.getId() });
			}

			getJdbcTemplate().batchUpdate(addSql, batchArgs, new int[] { Types.BIGINT, Types.BIGINT });

		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}

		return save;
	}

	@Override
	public int addAndDeleteSchedulerMasterClientMapping(long id, List<CloudClient> clientsList) {
		int save = 0;
		try {
			String sql = sqlHelper.getSql("deleteSchedulerMasterClientMapping");
			String sqlClient = sqlHelper.getSql("deleteSchedulerMasterClientMappingByClientId");
			getJdbcTemplate().update(sql, new Object[] { id });

			String addSql = sqlHelper.getSql("addSchedulerMasterClientMapping");
			List<Object[]> batchArg = new ArrayList<>();

			List<Object[]> batchArgs = new ArrayList<>();
			for (CloudClient clientId : clientsList) {
				batchArgs.add(new Object[] { id, clientId.getId() });
				batchArg.add(new Object[] { clientId.getId() });
			}
			getJdbcTemplate().batchUpdate(sqlClient, batchArg, new int[] { Types.BIGINT });
			getJdbcTemplate().batchUpdate(addSql, batchArgs, new int[] { Types.BIGINT, Types.BIGINT });

		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}

		return save;
	}

	@Override
	public List<SchedulerSlave> getSchedulerMasterSlaveMapping(long id) {
		List<SchedulerSlave> slaves = null;
		try {

			String sql = sqlHelper.getSql("getSchedulerMasterSlaveMapping");

			slaves = getJdbcTemplate().query(sql, new Object[] { id }, new RowMapper<SchedulerSlave>() {
				@Override
				public SchedulerSlave mapRow(ResultSet rs, int rowNum) throws SQLException {
					SchedulerSlave slaveInfo = new SchedulerSlave();

					slaveInfo.setId(rs.getLong("id"));
					slaveInfo.setType(rs.getInt("type"));
					slaveInfo.setName(rs.getString("name"));
					slaveInfo.setInstanceId(rs.getString("instance_id"));
					AwsCredentials aws = new AwsCredentials();
					aws.setId(rs.getInt("aws_id"));
					aws.setName(rs.getString("aws_name"));
					aws.setSecretKey(rs.getString("secret_key"));
					aws.setAccessKey(rs.getString("access_key"));
					aws.setRegion(rs.getString("region"));
					slaveInfo.setAws(aws);
					slaveInfo.setIpAddress(rs.getString("ip_address"));
					slaveInfo.setPackageExecutionCount(rs.getInt("no_of_package_executions"));
					slaveInfo.setFileUploadCount(rs.getInt("no_of_package_source_uploads"));
					slaveInfo.setHistoryLoadCount(rs.getInt("no_of_history_load_source_uploads"));
					slaveInfo.setHistoryExecutionCount(rs.getInt("no_of_history_load_executions"));
					slaveInfo.setActive(rs.getBoolean("is_active"));
					slaveInfo.setRequestSchema(rs.getString("request_schema"));
					
					return slaveInfo;
				}
			});
		} catch (DataAccessException | SqlNotFoundException e) {
			throw new AnvizentRuntimeException("Master Slave mapping :" + e.getMessage());
		}
		return slaves;
	}

	@Override
	public List<CloudClient> getSchedulerMasterClientMapping(long id) {
		List<CloudClient> clients = null;
		try {

			String sql = sqlHelper.getSql("getSchedulerMasterClientMapping");

			clients = getJdbcTemplate().query(sql, new Object[] { id }, new RowMapper<CloudClient>() {
				@Override
				public CloudClient mapRow(ResultSet rs, int rowNum) throws SQLException {
					CloudClient client = new CloudClient();
					client.setId(rs.getLong("id"));
					client.setClientName(rs.getString("clientname"));
					client.setActive(rs.getBoolean("is_active"));
					try {
						client.setDeploymentType(AESConverter.decrypt(rs.getString("deployment_type")));
					} catch (Exception e) {
					}
					return client;
				}
			});

		} catch (DataAccessException | SqlNotFoundException e) {
			throw new AnvizentRuntimeException("Master Client mapping :" + e.getMessage());
		}
		return clients;
	}

	@Override
	public List<SchedulerType> getSchedulerType() {
		List<SchedulerType> typeInfo = null;
		try {
			String sql = sqlHelper.getSql("getSchedulerType");
			typeInfo = getJdbcTemplate().query(sql, new RowMapper<SchedulerType>() {

				@Override
				public SchedulerType mapRow(ResultSet rs, int rowNum) throws SQLException {
					SchedulerType type = new SchedulerType();
					type.setId(rs.getInt("id"));
					type.setName(rs.getString("name"));
					type.setIsActive(rs.getBoolean("is_active"));
					return type;
				}

			});

		} catch (Exception e) {
			LOG.error("", e);
		}
		return typeInfo;
	}

	@Override
	public List<CloudClient> getActiveScheduleClientsList(long masterId) {
		List<CloudClient> clientsInfo = null;
		try {
			String sql = sqlHelper.getSql("getActiveScheduleClientsList");
			clientsInfo = getJdbcTemplate().query(sql, new Object[] { masterId }, new CloudClientMapper());
		} catch (Exception e) {
			LOG.error("", e);
		}
		return clientsInfo;
	}

	@Override
	public List<SchedulerSlave> getActiveSchedulerSlavesList(long masterId) {
		List<SchedulerSlave> clientsInfo = null;
		try {
			String sql = sqlHelper.getSql("getActiveScheduleSlavesList");
			clientsInfo = getJdbcTemplate().query(sql, new Object[] { masterId }, new SchedulerSlaveMapper());
		} catch (Exception e) {
			LOG.error("", e);
		}
		return clientsInfo;
	}

	@Override
	public List<AwsRegions> getAwsRegionsList() {
		List<AwsRegions> regionInfo = null;
		try {
			String sql = sqlHelper.getSql("getAwsRegionsList");
			regionInfo = getJdbcTemplate().query(sql, new RowMapper<AwsRegions>() {

				@Override
				public AwsRegions mapRow(ResultSet rs, int rowNum) throws SQLException {
					AwsRegions region = new AwsRegions();
					region.setId(rs.getInt("id"));
					region.setName(rs.getString("region_name"));
					region.setActive(rs.getBoolean("is_active"));
					return region;
				}

			});

		} catch (Exception e) {
			throw new AnvizentRuntimeException(e);
		}
		return regionInfo;
	}

	@Override
	public SchedulerMaster getSchedulerState(long id) {
		SchedulerMaster schedulerStateInfo = null;
		try {
			String sql = sqlHelper.getSql("getSchedulerState");
			schedulerStateInfo = getJdbcTemplate().query(sql, new Object[] { id },
					new ResultSetExtractor<SchedulerMaster>() {

						@Override
						public SchedulerMaster extractData(ResultSet rs) throws SQLException, DataAccessException {
							SchedulerMaster stateInfo = new SchedulerMaster();
							if (rs != null && rs.next()) {
								stateInfo.setId(rs.getInt("id"));
								stateInfo.setServerState(rs.getBoolean("server_state"));
								stateInfo.setSchdRunningState(rs.getBoolean("scheduler_running_state"));
								stateInfo.setIsCheckingCompleted(rs.getBoolean("is_checking_completed"));
								stateInfo.setStateMsg(rs.getString("state_msg"));
								return stateInfo;
							} else {
								return null;
							}
						}

					});

		} catch (Exception e) {
			LOG.error("", e);
		}
		return schedulerStateInfo;
	}

	@Override
	public List<QuartzSchedulerInfo> getQuartzShedulerMasterInfo(JdbcTemplate clientAppDbJdbcTemplate) {
		List<QuartzSchedulerInfo> quartzMasternfoList = null;
		try {
			String sql = sqlHelper.getSql("getQuartzShedulerMasterInfo");
			quartzMasternfoList = clientAppDbJdbcTemplate.query(sql, new RowMapper<QuartzSchedulerInfo>() {

				@Override
				public QuartzSchedulerInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
					QuartzSchedulerInfo schdInfo = new QuartzSchedulerInfo();
					schdInfo.setName(rs.getString("name"));
					schdInfo.setDescription(rs.getString("description"));
					schdInfo.setStartTime(rs.getString("started_time"));
					schdInfo.setShutdownTime(rs.getString("shutdown_time"));
					schdInfo.setTimezone(rs.getString("timezone"));
					schdInfo.setIpAddress(rs.getString("ip_address"));
					return schdInfo;
				}

			});
		} catch (DataAccessException ae) {
			LOG.error("error while getQuartzShedulerMasterInfo()", ae);
			throw new AnvizentRuntimeException(ae);
		} catch (SqlNotFoundException e) {
			LOG.error("error while getQuartzShedulerMasterInfo()", e);
		}

		return quartzMasternfoList;
	}

	@Override
	public int updateServerMasterInfoByIpAddressAndId(SchedulerMaster schedulerMaster) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateServerMasterInfoByIpAddressAndId");
			update = getJdbcTemplate().update(sql,
					new Object[] { schedulerMaster.getIpAddress(), schedulerMaster.getId() });
		} catch (Exception e) {
			LOG.error("", e);
		}
		return update;
	}

	@Override
	public int updateServerSlaveInfoBYIpAddressAndId(SchedulerSlave schedulerSlave) {
		int update = 0;
		try {
			String sql = sqlHelper.getSql("updateServerSlaveInfoBYIpAddressAndId");
			update = getJdbcTemplate().update(sql,
					new Object[] { schedulerSlave.getIpAddress(), schedulerSlave.getId() });
		} catch (Exception e) {
			LOG.error("", e);
		}
		return update;
	}

	@Override
	public SchedulerMaster getSchedulerServerMasterBySchedulerId(long schedulerId) {
		SchedulerMaster serverMasterInfo = null;
		try {
			String sql = sqlHelper.getSql("getSchedulerServerMasterBySchedulerId");
			serverMasterInfo = getJdbcTemplate().query(sql, new Object[] { schedulerId },new ResultSetExtractor<SchedulerMaster>() {

						@Override
						public SchedulerMaster extractData(ResultSet rs) throws SQLException, DataAccessException {
							SchedulerMaster masterInfo = new SchedulerMaster();
							if (rs != null && rs.next()) {
								long id = rs.getLong("id");
								masterInfo.setId(id);
								masterInfo.setName(rs.getString("name"));
								masterInfo.setType(rs.getInt("type"));
								AwsCredentials aws = new AwsCredentials();
								aws.setId(rs.getInt("aws_id"));
								masterInfo.setAws(aws);
								masterInfo.setInstanceId(rs.getString("instance_id"));
								masterInfo.setActive(rs.getBoolean("is_active"));
								masterInfo.setIpAddress(rs.getString("ip_address"));
								masterInfo.setRequestSchema(rs.getString("request_schema"));
								return masterInfo;
							} else {
								return null;
							}
						}

					});

		} catch (DataAccessException | SqlNotFoundException e) {
			throw new AnvizentRuntimeException("Master exception :" + e.getMessage());
		}
		return serverMasterInfo;
	}

	@Override
	public QuartzSchedulerJobInfo  getSchedulerJobInfoByJobId(long jobId,String timeZone,JdbcTemplate clientAppDbJdbcTemplate) {
		QuartzSchedulerJobInfo jobInfo = null;
		try {
			String sql = sqlHelper.getSql("getSchedulerJobInfoByJobId");
			JdbcTemplate requiredJdbcTemplete = getRequiredJdbcTemplate(clientAppDbJdbcTemplate);
			jobInfo = requiredJdbcTemplete.query(sql, new Object[]{jobId},new ResultSetExtractor<QuartzSchedulerJobInfo>(){

				@Override
				public QuartzSchedulerJobInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
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
							e.printStackTrace();
						}
						schedulerDetails.setStatus(rs.getString("status"));
						return schedulerDetails;
					} else {
						return null;
					}
				}
				
			});
		}  catch (DataAccessException | SqlNotFoundException e) {
			throw new AnvizentRuntimeException("Master exception :" + e.getMessage());
		}
		
		return jobInfo;
	}
	JdbcTemplate getRequiredJdbcTemplate (JdbcTemplate clientAppDbJdbcTemplate) {
		return clientAppDbJdbcTemplate != null ? clientAppDbJdbcTemplate : getJdbcTemplate();
	}

}
