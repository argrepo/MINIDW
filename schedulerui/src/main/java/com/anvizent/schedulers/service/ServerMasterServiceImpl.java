/**
 * 
 */
package com.anvizent.schedulers.service;



import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;




@Service
public class ServerMasterServiceImpl implements ServerMasterService {

	protected static final Log LOG = LogFactory.getLog(ServerMasterServiceImpl.class);
	
	@Autowired
	private CommonDao commonDao;

	@Override
	public int addServerMasterInfo(SchedulerMaster schedulerServerMaster) {
		return commonDao.addServerMasterInfo(schedulerServerMaster);
	}

	@Override
	public int updateServerMasterInfo(SchedulerMaster schedulerServerMaster) {
		return commonDao.updateServerMasterInfo(schedulerServerMaster);
	}

	@Override
	public List<SchedulerMaster> getServerMasterInfo() {
		return commonDao.getSchedulerMasterInfo();
	}

	@Override
	public SchedulerMaster getSchedulerServerMasterById(long id) {
		return commonDao.getSchedulerMasterById(id);
	}

	@Override
	public SchedulerMaster getSchedulerState(long id) {
		return commonDao.getSchedulerState(id);
	}

	@Override
	public List<QuartzSchedulerInfo> getQuartzShedulerMasterInfo(JdbcTemplate clientAppDbJdbcTemplate) {
		return null;//commonDao.getQuartzShedulerMasterInfo(clientAppDbJdbcTemplate);
	}

	@Override
	public int updateServerMasterInfoByIpAddressAndId(SchedulerMaster schedulerMaster) {
		// TODO Auto-generated method stub
		return commonDao.updateServerMasterInfoByIpAddressAndId(schedulerMaster);
	}

	@Override
	public SchedulerMaster getSchedulerServerMasterBySchedulerId(long schedulerId) {
		return commonDao.getSchedulerServerMasterBySchedulerId(schedulerId);
	}

	@Override
	public QuartzSchedulerJobInfo getSchedulerJobInfoByJobId(long jobId,String timeZone,JdbcTemplate clientAppDbJdbcTemplate) {
		// TODO Auto-generated method stub
		return commonDao.getSchedulerJobInfoByJobId(jobId, timeZone,clientAppDbJdbcTemplate);
	}

	

	

	
}
