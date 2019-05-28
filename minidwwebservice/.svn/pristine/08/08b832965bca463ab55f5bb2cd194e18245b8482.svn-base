package com.datamodel.anvizent.service;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.IncrementalUpdate;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageRunNow;
import com.datamodel.anvizent.service.model.Schedule;

/**
 * @author rakesh.gajula
 *
 */
public interface ScheduleService {

	int saveSchedule(ClientData clientData , JdbcTemplate clientJdbcTemplate);
	
	int savePackageRunNowDetails(PackageRunNow packageRunNow, JdbcTemplate clientJdbcTemplate);

	List<ClientData> getScheduledPackages(String clientId, JdbcTemplate clientJdbcTemplate);

	void updateStatusOfClientScheduler(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	void updateStatusOfServerScheduler(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	List<ClientData> getUploadedFileInfo(String clientId, JdbcTemplate clientJdbcTemplate);

	List<ClientData> getUploadedFileInfoByStatus(String clientSchedulerStatus, JdbcTemplate clientJdbcTemplate);

	void generateNextScheduleTime(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	ClientData getScheduleById(int scheduleId, JdbcTemplate clientJdbcTemplate);

	int updateScheduleCurrent(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	List<ClientData> getScheduleCurrent(String clientId, JdbcTemplate clientJdbcTemplate);

	List<ClientData> getAllScheduledPackages(JdbcTemplate clientJdbcTemplate);

	String getFilePathFromScheduleCurrent(String clientId, int packageId, int ilConnectionmappingId, JdbcTemplate clientJdbcTemplate);

	String getClientSchedulerStatus(String clientId, int packageId, int ilConnectionmappingId, JdbcTemplate clientJdbcTemplate);

	void deleteSchedule(int packageId, JdbcTemplate clientJdbcTemplate);

	void updateSchedule(ClientData clientData, JdbcTemplate clientJdbcTemplate);

	int updateHistoricalLoadExecutionStatus(Long historicalLoadId, boolean updateStatus, JdbcTemplate clientJdbcTemplate);

	int updateHistoricalLoadRunningStatus(boolean updateStatus, Long historicalLoadId, JdbcTemplate clientJdbcTemplate);

	ILConnectionMapping getILConnectionMappingByMappingId(int mappingId, JdbcTemplate clientJdbcTemplate);

	IncrementalUpdate getIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate jdbcTemplate, String clientStatgingSchema);

	List<ClientData> getILConnectionMappingS3DetailsInfoByPackage(String userId, int packageId, Boolean isStandard, JdbcTemplate clientJdbcTemplate);

	void saveIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate jdbcTemplate, String clientStatgingSchema);
	
	void saveOrUpdateIncrementalUpdate(String incrementalDateValue, int iLConnectionMappingId, JdbcTemplate jdbcTemplate);

	void updateIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate jdbcTemplate, String clientStatgingSchema);


	int updateScheduleCurrentStatus(Integer scheduleCurrentId, String userId, Long packageId, Long ilConnectionMappingId, String clientSchedularStatus,
			String clientSchedularStatusDetails, String scheduleEndDateTime, Modification modification, JdbcTemplate clientJdbcTemplate);

	ClientData getScheduleCurrentByScid(String clientId, String scheduleCurrentId, JdbcTemplate clientJdbcTemplate);

	Map<String, Object> getScheduleCurrentDetailsById(Integer clientId, Integer scheduleCurrentId, JdbcTemplate clientJdbcTemplate);

	int updateJobExecutionDetails(JobExecutionInfo jobExecutionInfo, JdbcTemplate clientJdbcTemplate);
	
	List<JobResult> getJobResults(String clientId, String userId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsByDate(String clientId, String userId, String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate);
	
	int savePackageExectionInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	
	int updatePackageExecutionUploadInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	
	int updatePackageExecutionStatus(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	
	int updatePackageExecutionStatusInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	
	public List<Schedule> getPackagesForSchedulingByClientIdWithCronExpression(String clientId, JdbcTemplate clientJdbcTemplate) ;
	
	int updatePackageExecutionMappingInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	
	int updatePackageExecutionMappingInfoStatus(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	
	int updateDruidStartInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	
	int updateDruidEndInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	
	int updatePackageSchedule(Schedule Schedule , JdbcTemplate clientJdbcTemplate);
	
	List<Integer> getPackageSourceMappingListByDlId(int dlId,JdbcTemplate clientAppJdbcTemplate);
	
	void updateRetryPaginationToNull(List<Integer> mappingList,JdbcTemplate clientAppJdbcTemplate);
	
}
