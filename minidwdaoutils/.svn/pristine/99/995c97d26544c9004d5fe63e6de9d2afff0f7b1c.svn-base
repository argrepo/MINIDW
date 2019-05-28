/**
 * 
 */
package com.datamodel.anvizent.service.dao;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.IncrementalUpdate;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageRunNow;
import com.datamodel.anvizent.service.model.Schedule;

/**
 * @author rakesh.gajula
 *
 */
public interface ScheduleDao {

	int saveSchedule(ClientData clientData, JdbcTemplate clientJdbctemplate); 
	
	int savePackageRunNowDetails(PackageRunNow packageRunNow, JdbcTemplate clientJdbctemplate) ;

	List<ClientData> getScheduledPackages(String clientId, JdbcTemplate clientJdbctemplate) ;

	void updateStatusOfClientScheduler(ClientData clientData, JdbcTemplate clientJdbctemplate) ;

	void updateStatusOfServerScheduler(ClientData clientData, JdbcTemplate clientJdbctemplate) ;

	List<ClientData> getUploadedFileInfo(String clinetId, JdbcTemplate clientJdbctemplate); 

	List<ClientData> getUploadedFileInfoByStatus(String status, JdbcTemplate clientJdbctemplate); 

	void generateNextScheduleTime(ClientData clientData, JdbcTemplate clientJdbctemplate); 

	ClientData getScheduleById(int scheduleId, JdbcTemplate clientJdbctemplate); 

	int updateScheduleCurrent(ClientData clientData, JdbcTemplate clientJdbctemplate); 

	List<ClientData> getScheduleCurrent(String clientId, JdbcTemplate clientJdbctemplate); 

	List<ClientData> getAllScheduledPackages(JdbcTemplate clientJdbctemplate);

	String getFilePathFromScheduleCurrent(String clientId, int packageId, int ilConnectionmappingId, JdbcTemplate clientJdbctemplate);

	String getClientSchedulerStatus(String clientId, int packageId, int ilConnectionmappingId, JdbcTemplate clientJdbctemplate) ;

	void deleteSchedule(int packageId, JdbcTemplate clientJdbctemplate) ;

	void updateSchedule(ClientData clientData, JdbcTemplate clientJdbctemplate) ;

	int updateHistoricalLoadExecutionStatus(Long historicalLoadId, boolean updateStatus, JdbcTemplate clientJdbctemplate) ;

	int updateHistoricalLoadRunningStatus(boolean updateStatus, Long historicalLoadId, JdbcTemplate clientJdbctemplate) ;

	ILConnectionMapping getILConnectionMappingByMappingId(int mappingId, JdbcTemplate clientJdbctemplate) ;

	IncrementalUpdate getIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate jdbcTemplate, String clientStatgingSchema);

	List<ClientData> getILConnectionMappingS3DetailsInfoByPackage(String userId, int packageId, Boolean isStandard, JdbcTemplate clientJdbctemplate); 

	void saveIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate jdbcTemplate, String clientStatgingSchema);

	void updateIncrementalUpdate(ILConnectionMapping iLConnectionMapping, JdbcTemplate jdbcTemplate, String clientStatgingSchema);

	int updateScheduleCurrentStatus(Integer scheduleCurrentId, String userId, Long packageId, Long ilConnectionMappingId, String clientSchedularStatus,
			String clientSchedularStatusDetails, String scheduleEndDateTime, Modification modification, JdbcTemplate clientJdbctemplate); 

	ClientData getScheduleCurrentByScid(String clientId, String scheduleCurrentId, JdbcTemplate clientJdbctemplate); 

	Map<String, Object> getScheduleCurrentDetailsById(Integer clientId, Integer scheduleCurrentId, JdbcTemplate clientJdbctemplate); 
	
	int updateJobExecutionDetails(JobExecutionInfo jobExecutionInfo, JdbcTemplate clientJdbctemplate); 
	
	List<JobResult> getJobResults(String clientId, String userId, String clientStagingSchemaName, JdbcTemplate clientJdbcTemplate);

	List<JobResult> getJobResultsByDate(String clientId, String userId, String clientStagingSchemaName, String fromDate, String toDate, JdbcTemplate clientJdbcTemplate);
	
	public int savePackageStatus(Integer userId, long packageId, Integer scheduleId, String package_status, int countOfilsprocessed, int countOfdlsprocesed,
			Modification modification, JdbcTemplate clientJdbctemplate); 
	
	public void updatePackageStatus(int package_status_id, String package_status, int no_of_ils_processed, int no_of_dls_processed, Modification modification, JdbcTemplate clientJdbctemplate); 

	public void updateStatusOfServerSchedulerWithDetails(String currentScheduleEndTime, String serverSchedulerStatus, String serverSchedulerStatusDetails,
			String ipAddress, String modifiedBy, String modifiedTime, Integer ilConnectionMappingId, String scheduleDate, int packageId, JdbcTemplate clientJdbctemplate); 
	
	public boolean deleteCustomTablesByPackageId(Package userpackage, String userId, JdbcTemplate clientJdbcTemplate);
	
	public String getFilePathFromScheduleCurrent(String clientId, long packageId, int ilConnectionmappingId, String scheduleDate, JdbcTemplate clientJdbctemplate); 
	

	public String processFileMappingWithIL(String filePath, String fileType, String separatorChar, String stringQuoteChar, List<String> iLColumnNames,
			List<String> selectedFileHeaders, List<String> dafaultValues, JdbcTemplate clientJdbctemplate); 

	public void updateStatusOfServerScheduler(ClientData clientData, String scheduleDate ,JdbcTemplate clientJdbcTemplate);

	
	public void updatePackageErrortStatus(int package_status_id, String package_status, Modification modification, JdbcTemplate clientJdbctemplate); 
	
	public int saveDLInitiatingStatus(Integer userId, long packageId, Integer dl_Id, Integer scheduleId, String schedule_start_date_time, String DL_Job_Status,
			Modification modification, JdbcTemplate clientJdbctemplate); 
	
	public void updateDLStatus(int schedule_current_seq_id, String schedule_end_date_time, String DL_Status, String errorDetails, Modification modification, JdbcTemplate clientJdbctemplate); 

	int savePackageExectionInfo(PackageExecution packageExecution, JdbcTemplate clientJdbctemplate); 

	int updatePackageExecutionUploadInfo(PackageExecution packageExecution, JdbcTemplate clientJdbctemplate); 

	int updatePackageExecutionStatus(PackageExecution packageExecution, JdbcTemplate clientJdbctemplate); 

	int updatePackageExecutionStatusInfo(PackageExecution packageExecution, JdbcTemplate clientJdbctemplate); 

	public List<Schedule> getPackagesForSchedulingByClientIdWithCronExpression(String clientId , JdbcTemplate clientJdbctemplate) ;
	
	int updatePackageExecutionMappingInfo(PackageExecution packageExecution, JdbcTemplate clientJdbctemplate);
	
	int updatePackageExecutionMappingInfoStatus(PackageExecution packageExecution, JdbcTemplate clientJdbctemplate);

	 int updateDruidStartInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	
	 int updateDruidEndInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	 
	 void saveOrUpdateIncrementalUpdate(String incrementalDateValue, int iLConnectionMappingId, JdbcTemplate appDbJdbcTemplate, JdbcTemplate clinetStagingDbJdbcTemplate);
	 
	 void saveOrUpdateIncrementalUpdate(String incrementalDateValue, int ilId,int connectionId,JdbcTemplate appDbJdbcTemplate, JdbcTemplate clinetStagingDbJdbcTemplate);

	 int updatePackageSchedule(Schedule schedule, JdbcTemplate clientJdbcTemplate);

	 Schedule getScheduledPackageInfo(PackageExecution packageExecution, JdbcTemplate clientJdbcTemplate);
	 
	 List<Integer> getPackageSourceMappingListByDlId(int dlId,JdbcTemplate clientAppJdbcTemplate);
		
     void updateRetryPaginationToNull(List<Integer> mappingList,JdbcTemplate clientAppJdbcTemplate);
     
}
