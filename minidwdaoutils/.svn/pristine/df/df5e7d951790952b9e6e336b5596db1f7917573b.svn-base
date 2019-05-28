/**
 * 
 */
package com.datamodel.anvizent.service.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.AwsRegions;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerJobDetails;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.datamodel.anvizent.service.model.SchedulerType;

public interface CommonDao {

	int addAwsCredentialsInfo(AwsCredentials awsCredentials);

	int updateAwsCredentialsInfo(AwsCredentials awsCredentials);

	List<AwsCredentials> getAwsCredentialsList();

	AwsCredentials getAwsCredentialsListById(long id);
	
	int addServerMasterInfo(SchedulerMaster schedulerServerMaster);

	int updateServerMasterInfo(SchedulerMaster schedulerServerMaster);

	List<SchedulerMaster> getSchedulerMasterInfo();
	
	SchedulerMaster getSchedulerMasterById(long id);

	int addAndDeleteSchedulerMasterMapping(long id,List<SchedulerSlave> schedulerServerSlave);
	
	public int addAndDeleteSchedulerMasterClientMapping(long id, List<CloudClient> clientsList);
	
	public List<SchedulerSlave> getSchedulerMasterSlaveMapping(long id); 
	
	public List<CloudClient> getSchedulerMasterClientMapping(long id);
	
	int addServerSlaveInfo(SchedulerSlave schedulerServerSlave);

	int updateServerSlaveInfo(SchedulerSlave schedulerServerSlave);

	List<SchedulerSlave> getSchedulerSlaveInfo();

	SchedulerSlave getServerSlaveById(long id);

	List<SchedulerType> getSchedulerType();
	
	public List<CloudClient> getActiveScheduleClientsList(long masterId);
	
	public List<SchedulerSlave> getActiveSchedulerSlavesList(long masterId);

	List<AwsRegions> getAwsRegionsList();

	SchedulerMaster getSchedulerState(long id);

	List<QuartzSchedulerInfo> getQuartzShedulerMasterInfo(JdbcTemplate clientAppDbJdbcTemplate);

	int updateServerMasterInfoByIpAddressAndId(SchedulerMaster schedulerMaster);

	int updateServerSlaveInfoBYIpAddressAndId(SchedulerSlave schedulerSlave);

	SchedulerMaster getSchedulerServerMasterBySchedulerId(long schedulerId);

	QuartzSchedulerJobInfo getSchedulerJobInfoByJobId(long jobId,String timeZone,JdbcTemplate clientAppDbJdbcTemplate);

	
}
