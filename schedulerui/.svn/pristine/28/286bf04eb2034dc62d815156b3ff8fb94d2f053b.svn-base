/**
 * 
 */
package com.anvizent.schedulers.service;



import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;




@Service
public class ServerSlaveServiceImpl implements ServerSlaveService {

	protected static final Log LOG = LogFactory.getLog(ServerSlaveServiceImpl.class);
	
	@Autowired
	private CommonDao commonDao;

	@Override
	public int addServerSlaveInfo(SchedulerSlave schedulerServerSlave) {
		return commonDao.addServerSlaveInfo(schedulerServerSlave); 
	}

	@Override
	public int updateServerSlaveInfo(SchedulerSlave schedulerServerSlave) {
		return commonDao.updateServerSlaveInfo(schedulerServerSlave);
	}

	@Override
	public List<SchedulerSlave> getServerSlaveInfo() {
		return commonDao.getSchedulerSlaveInfo();
	}

	@Override
	public SchedulerSlave getServerSlaveById(long id) {
		return commonDao.getServerSlaveById(id);
	}

	@Override
	public int updateServerSlaveInfoBYIpAddressAndId(SchedulerSlave schedulerSlave) {
		// TODO Auto-generated method stub
		return commonDao.updateServerSlaveInfoBYIpAddressAndId(schedulerSlave);
	}


	
}
