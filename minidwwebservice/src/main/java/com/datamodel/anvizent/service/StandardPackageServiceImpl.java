package com.datamodel.anvizent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.dao.StandardPackageDao;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.Schedule;

public class StandardPackageServiceImpl  implements StandardPackageService {

	 @Autowired
	 private StandardPackageDao standardPackageDao;
	 
	 public StandardPackageServiceImpl(StandardPackageDao standardPackageDao){
		 this.standardPackageDao = standardPackageDao;
	 }
	
	 @Override
		public List<DLInfo> getClientSPDLs(String userId, JdbcTemplate clientAppDbJdbcTemplate) {
			return standardPackageDao.getClientSPDLs(userId , clientAppDbJdbcTemplate);
		}
	 
	@Override
	public Package fetchStandardPackageInfo(String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return standardPackageDao.fetchStandardPackageInfo(clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public int createStandardPackage(Package createUserPackage, JdbcTemplate clientAppDbJdbcTemplate) {
		return standardPackageDao.createStandardPackage(createUserPackage, clientAppDbJdbcTemplate);
	}

	@Override
	public DLInfo getIlMappingInfobyId(String userId, String clientId, Integer dlId, JdbcTemplate clientAppDbJdbcTemplate) {
		return standardPackageDao.getIlMappingInfobyId(userId, clientId, dlId, clientAppDbJdbcTemplate);
	}


	@Override
	public DLInfo getIlMappingInfobyId(String userId, String clientId, Integer dlId, Integer iLid, JdbcTemplate clientAppDbJdbcTemplate) {
		return standardPackageDao.getIlMappingInfobyId(userId, clientId, dlId, iLid, clientAppDbJdbcTemplate);
	}


	@Override
	public List<ILConnectionMapping> getILConnectionMappingInfoByMappingId(List<Integer> mappingIds, String userId,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return standardPackageDao.getILConnectionMappingInfoByMappingId(mappingIds, userId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<ILConnectionMapping> getILConnectionMappingInfoByDLId(String userId, String clientId, int dlId,	JdbcTemplate clientAppDbJdbcTemplate) {
		return standardPackageDao.getILConnectionMappingInfoByDLId(userId, clientId, dlId, clientAppDbJdbcTemplate, true);
	}

	@Override
	public int updatePackageSchedule(Schedule schedule, JdbcTemplate clientAppDbJdbcTemplate) {
		return standardPackageDao.updatePackageSchedule(schedule, clientAppDbJdbcTemplate);
	}

	@Override
	public List<PackageExecution> getPackageExecutionResults(Integer dlId, JdbcTemplate clientAppDbJdbcTemplate) {
		 return standardPackageDao.getPackageExecutionResults(dlId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<JobResult> getExecutionJobResults(String packageId, Integer dlId, String clientId, JdbcTemplate clientJdbcTemplate) {
		return standardPackageDao.getExecutionJobResults(packageId, dlId, clientId, clientJdbcTemplate);
	}

	@Override
	public int saveDLTrailingMapping(String userId, String clientId, DLInfo dlInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		return standardPackageDao.saveDLTrailingMapping(userId,clientId,dlInfo,clientAppDbJdbcTemplate);
	}

	@Override
	public int updateDLTrailingMapping(String userId, String clientId, DLInfo dlInfo,
			JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return standardPackageDao.updateDLTrailingMapping(userId,clientId,dlInfo,clientAppDbJdbcTemplate);
	}

	@Override
	public List<JobResult> getExecutionJobResultsByDate(String packageId, String dlId, String clientId,
			String fromDate, String toDate, JdbcTemplate clientJdbcTemplate) {
		// TODO Auto-generated method stub
		return standardPackageDao.getExecutionJobResultsByDate(packageId, dlId, clientId, fromDate, toDate, clientJdbcTemplate);
	}

	@Override
	public List<PackageExecution> getPackageExecutionResultsByPagination(Integer dlId, int offset, int limit, JdbcTemplate clientAppDbJdbcTemplate)
	{
		return standardPackageDao.getPackageExecutionResultsByPagination(dlId,offset,limit,clientAppDbJdbcTemplate);
	}

	@Override
	public int getStandardPackageExecutionCount(int packageId, int dlId, JdbcTemplate clientJdbcTemplate)
	{
		return standardPackageDao.getStandardPackageExecutionCount(  packageId,   dlId,   clientJdbcTemplate);
	}

}
