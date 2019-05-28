/**
 * 
 */
package com.datamodel.anvizent.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.IncrementalUpdate;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageRunNow;
import com.datamodel.anvizent.service.model.Schedule;

/**
 * @author rakesh.gajula
 *
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

	protected static final Log LOG = LogFactory.getLog(ScheduleServiceImpl.class);
	@Autowired
	private ScheduleDao scheduleDao;

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveSchedule(ClientData clientData , JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.saveSchedule(clientData , clientJdbcTemplate);

	}

	@Override
	public List<ClientData> getScheduledPackages(String clientId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getScheduledPackages(clientId, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void updateStatusOfClientScheduler(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		scheduleDao.updateStatusOfClientScheduler(clientData, clientJdbcTemplate);

	}

	@Override
	public List<ClientData> getUploadedFileInfo(String clientId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getUploadedFileInfo(clientId, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void generateNextScheduleTime(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		scheduleDao.generateNextScheduleTime(clientData, clientJdbcTemplate);

	}

	@Override
	public List<ClientData> getUploadedFileInfoByStatus(String clientSchedulerStatus, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getUploadedFileInfoByStatus(clientSchedulerStatus, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void updateStatusOfServerScheduler(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		scheduleDao.updateStatusOfServerScheduler(clientData, clientJdbcTemplate);

	}

	@Override
	public ClientData getScheduleById(int scheduleId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getScheduleById(scheduleId, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateScheduleCurrent(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updateScheduleCurrent(clientData, clientJdbcTemplate);
	}

	@Override
	public List<ClientData> getScheduleCurrent(String clientId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getScheduleCurrent(clientId, clientJdbcTemplate);
	}

	@Override
	public List<ClientData> getAllScheduledPackages(JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getAllScheduledPackages(clientJdbcTemplate);
	}

	@Override
	public String getFilePathFromScheduleCurrent(String clientId, int packageId, int ilConnectionmappingId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getFilePathFromScheduleCurrent(clientId, packageId, ilConnectionmappingId, clientJdbcTemplate);
	}

	@Override
	public String getClientSchedulerStatus(String clientId, int packageId, int ilConnectionmappingId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getClientSchedulerStatus(clientId, packageId, ilConnectionmappingId, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void deleteSchedule(int packageId, JdbcTemplate clientJdbcTemplate) {
		scheduleDao.deleteSchedule(packageId, clientJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void updateSchedule(ClientData clientData, JdbcTemplate clientJdbcTemplate) {
		scheduleDao.updateSchedule(clientData, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateHistoricalLoadExecutionStatus(Long historicalLoadId, boolean updateStatus, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updateHistoricalLoadExecutionStatus(historicalLoadId, updateStatus, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int updateHistoricalLoadRunningStatus(boolean updateStatus, Long historicalLoadId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updateHistoricalLoadRunningStatus(updateStatus, historicalLoadId, clientJdbcTemplate);
	}

	@Override
	public ILConnectionMapping getILConnectionMappingByMappingId(int mappingId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getILConnectionMappingByMappingId(mappingId, clientJdbcTemplate);
	}

	@Override
	public IncrementalUpdate getIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate jdbcTemplate, String clientStatgingSchema) {
		return scheduleDao.getIncrementalUpdate(iLConnectionMapping, jdbcTemplate, clientStatgingSchema);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void saveIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate jdbcTemplate, String clientStatgingSchema) {
		scheduleDao.saveIncrementalUpdate(iLConnectionMapping, jdbcTemplate, clientStatgingSchema);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void saveOrUpdateIncrementalUpdate(String incrementalDateValue, int iLConnectionMappingId, JdbcTemplate jdbcTemplate) {
		
		ILConnectionMapping iLConnectionMapping = scheduleDao.getILConnectionMappingByMappingId(iLConnectionMappingId , jdbcTemplate);
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

			Modification modification1 = new Modification(new Date());
			modification1.setCreatedBy("By RunNow/Schedule ");

			iLConnectionMapping.setIncrementalDateValue(incrementalDateValue);
			iLConnectionMapping.setTypeOfCommand(typeOfSource);
			iLConnectionMapping.setModification(modification1);

			IncrementalUpdate incrementalUpdate = scheduleDao.getIncrementalUpdate(iLConnectionMapping, jdbcTemplate, null);

			if (StringUtils.isNotBlank(incrementalUpdate.getIncDateFromSource())) {
				scheduleDao.updateIncrementalUpdate(iLConnectionMapping, jdbcTemplate, null);
			} else {
				scheduleDao.saveIncrementalUpdate(iLConnectionMapping, jdbcTemplate, null);
			}
		}
		
	}
	
	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void updateIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate jdbcTemplate, String clientStatgingSchema) {
		scheduleDao.updateIncrementalUpdate(iLConnectionMapping, jdbcTemplate, clientStatgingSchema);
	}

	@Override
	public List<ClientData> getILConnectionMappingS3DetailsInfoByPackage(String userId, int packageId, Boolean isStandard, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getILConnectionMappingS3DetailsInfoByPackage(userId, packageId, isStandard, clientJdbcTemplate);
	}


	@Override
	public int updateScheduleCurrentStatus(Integer scheduleCurrentId, String userId, Long packageId, Long ilConnectionMappingId, String clientSchedularStatus,
			String clientSchedularStatusDetails, String scheduleEndDateTime, Modification modification, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updateScheduleCurrentStatus(scheduleCurrentId, userId, packageId, ilConnectionMappingId, clientSchedularStatus,
				clientSchedularStatusDetails, scheduleEndDateTime, modification, clientJdbcTemplate);
	}

	@Override
	public ClientData getScheduleCurrentByScid(String clientId, String scheduleCurrentId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getScheduleCurrentByScid(clientId, scheduleCurrentId, clientJdbcTemplate);
	}

	@Override
	public Map<String, Object> getScheduleCurrentDetailsById(Integer clientId, Integer scheduleCurrentId, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getScheduleCurrentDetailsById(clientId, scheduleCurrentId, clientJdbcTemplate);
	}

	@Override
	public int updateJobExecutionDetails(JobExecutionInfo jobExecutionInfo, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updateJobExecutionDetails(jobExecutionInfo, clientJdbcTemplate);
	}

	@Override
	public List<JobResult> getJobResults(String clientId, String userId, String clientStagingSchemaName,
			JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getJobResults(clientId,userId,clientStagingSchemaName,clientJdbcTemplate);
	}

	@Override
	public List<JobResult> getJobResultsByDate(String clientId, String userId, String clientStagingSchemaName,
			String fromDate, String toDate, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.getJobResultsByDate(clientId,userId,clientStagingSchemaName,fromDate,toDate, clientJdbcTemplate);
	}


	@Override
	public int savePackageExectionInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.savePackageExectionInfo(packageExecution, clientJdbcTemplate);
	}

	@Override
	public int updatePackageExecutionUploadInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updatePackageExecutionUploadInfo(packageExecution, clientJdbcTemplate);
	}

	@Override
	public int updatePackageExecutionStatus(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updatePackageExecutionStatus(packageExecution, clientJdbcTemplate);
	}

	@Override
	public int updatePackageExecutionStatusInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientJdbcTemplate);
	}

	@Override
	public int savePackageRunNowDetails(PackageRunNow packageRunNow, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.savePackageRunNowDetails(packageRunNow, clientJdbcTemplate);
	}

	@Override
	public List<Schedule> getPackagesForSchedulingByClientIdWithCronExpression(String clientId, JdbcTemplate clientJdbcTemplate)  {
		return scheduleDao.getPackagesForSchedulingByClientIdWithCronExpression(clientId, clientJdbcTemplate);
	}

	@Override
	public int updatePackageExecutionMappingInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updatePackageExecutionMappingInfo(packageExecution, clientJdbcTemplate);
	}

	@Override
	public int updatePackageExecutionMappingInfoStatus(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updatePackageExecutionMappingInfoStatus(packageExecution, clientJdbcTemplate);
	}
	
	@Override
	public int updateDruidStartInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updateDruidStartInfo(packageExecution, clientJdbcTemplate);
	}
	
	@Override
	public int updateDruidEndInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate) {
		return scheduleDao.updateDruidEndInfo(packageExecution, clientJdbcTemplate);
	}

	@Override
	public int updatePackageSchedule(Schedule schedule, JdbcTemplate clientJdbcTemplate) {
		return  scheduleDao.updatePackageSchedule(schedule , clientJdbcTemplate);
	}

	@Override
	public List<Integer> getPackageSourceMappingListByDlId(int dlId, JdbcTemplate clientAppJdbcTemplate)
	{
		return  scheduleDao.getPackageSourceMappingListByDlId(dlId , clientAppJdbcTemplate);
	}

	@Override
	public void updateRetryPaginationToNull(List<Integer> mappingList, JdbcTemplate clientAppJdbcTemplate)
	{
		 scheduleDao.updateRetryPaginationToNull(mappingList , clientAppJdbcTemplate);
	}

}
