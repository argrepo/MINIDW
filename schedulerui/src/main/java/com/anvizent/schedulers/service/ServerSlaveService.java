
/**
 * 
 */
package com.anvizent.schedulers.service;

import java.util.List;

import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;

public interface ServerSlaveService {

	int addServerSlaveInfo(SchedulerSlave schedulerServerSlave);

	int updateServerSlaveInfo(SchedulerSlave schedulerServerSlave);

	List<SchedulerSlave> getServerSlaveInfo();

	SchedulerSlave getServerSlaveById(long id);

	int updateServerSlaveInfoBYIpAddressAndId(SchedulerSlave schedulerSlave);


	

}
