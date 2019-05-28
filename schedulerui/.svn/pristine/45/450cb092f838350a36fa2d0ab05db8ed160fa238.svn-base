
/**
 * 
 */
package com.anvizent.schedulers.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerJobDetails;
import com.datamodel.anvizent.service.model.SchedulerMaster;

public interface ServerMasterService {

	int addServerMasterInfo(SchedulerMaster schedulerServerMaster);

	int updateServerMasterInfo(SchedulerMaster schedulerServerMaster);

	List<SchedulerMaster> getServerMasterInfo();

	SchedulerMaster getSchedulerServerMasterById(long id);

	SchedulerMaster getSchedulerState(long id);

	List<QuartzSchedulerInfo> getQuartzShedulerMasterInfo(JdbcTemplate clientAppDbJdbcTemplate);

	int updateServerMasterInfoByIpAddressAndId(SchedulerMaster schedulerMaster);

	SchedulerMaster getSchedulerServerMasterBySchedulerId(long schedulerId);

	QuartzSchedulerJobInfo getSchedulerJobInfoByJobId(long jobId,String timeZone,JdbcTemplate clientAppDbJdbcTemplate);


	

}
