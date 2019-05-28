
/**
 * 
 */
package com.anvizent.schedulers.service;

import java.util.List;

import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.AwsRegions;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.datamodel.anvizent.service.model.SchedulerType;

public interface CommonService {

	int addAwsCredentialsInfo(AwsCredentials awsCredentials);

	int updateAwsCredentialsInfo(AwsCredentials awsCredentials);
	
	List<AwsCredentials> getAwsCredentialsList();

	AwsCredentials getAwsCredentialsListById(long id);
	
	int addAndDeleteSchedulerId(long id, List<SchedulerSlave> schedulerServerSlave);
	
	List<SchedulerType> getSchedulerType();
	
	List<AwsRegions> getAwsRegionsList();

	
}
