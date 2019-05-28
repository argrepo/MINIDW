package com.datamodel.anvizent.service.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.Schedule;

public interface StandardPackageDao {
	
   Package fetchStandardPackageInfo(String clientId, JdbcTemplate clientAppDbJdbcTemplate);

   int createStandardPackage(Package createUserPackage, JdbcTemplate clientAppDbJdbcTemplate);

   DLInfo getIlMappingInfobyId(String userId, String clientId, Integer dlId, JdbcTemplate clientAppDbJdbcTemplate);

   List<ILConnectionMapping> getILSourceListById(String userId, String clientId, Integer dlId, Integer ilId, JdbcTemplate clientAppDbJdbcTemplate);

   DLInfo getIlMappingInfobyId(String userId, String clientId, Integer dlId, Integer iLid,	JdbcTemplate clientAppDbJdbcTemplate);

   List<ILConnectionMapping> getILConnectionMappingInfoByMappingId(List<Integer> mappingIds, String userId, JdbcTemplate clientAppDbJdbcTemplate);

   List<ILConnectionMapping> getILConnectionMappingInfoByDLId(String userId, String clientId, Integer dlId, JdbcTemplate clientAppDbJdbcTemplate, boolean isActiveRequired);

   List<DLInfo> getClientSPDLs(String userId, JdbcTemplate clientAppDbJdbcTemplate);

   int updatePackageSchedule(Schedule schedule, JdbcTemplate clientAppDbJdbcTemplate);

   List<PackageExecution> getPackageExecutionResults(Integer dlId, JdbcTemplate clientAppDbJdbcTemplate);
   
   List<PackageExecution> getPackageExecutionResultsByPagination(Integer dlId, int offset, int limit, JdbcTemplate clientAppDbJdbcTemplate);

   List<JobResult> getExecutionJobResults(String packageId, Integer dlId, String clientId, JdbcTemplate clientJdbcTemplate);

   int saveDLTrailingMapping(String userId, String clientId,DLInfo dlInfo,JdbcTemplate clientAppDbJdbcTemplate);

   int updateDLTrailingMapping(String userId, String clientId, DLInfo dlInfo, JdbcTemplate clientAppDbJdbcTemplate);

   List<JobResult> getExecutionJobResultsByDate(String packageId, String dlId, String clientId, String fromDate,
		String toDate, JdbcTemplate clientJdbcTemplate);
   
   int getStandardPackageExecutionCount(int packageId, int dlId, JdbcTemplate clientJdbcTemplate);

}	
