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
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.AwsRegions;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.datamodel.anvizent.service.model.SchedulerType;




@Service
public class CommonServiceImpl implements CommonService {

	protected static final Log LOG = LogFactory.getLog(CommonServiceImpl.class);
	
	@Autowired
	private CommonDao commonDao;

	/*@Autowired
	private UserDao userDao;*/

	@Override
	public int addAwsCredentialsInfo(AwsCredentials awsCredentials) {
		return commonDao.addAwsCredentialsInfo(awsCredentials);
	}

	@Override
	public int updateAwsCredentialsInfo(AwsCredentials awsCredentials) {
		return commonDao.updateAwsCredentialsInfo(awsCredentials);
	}
	
	@Override
	public List<AwsCredentials> getAwsCredentialsList() {
		return commonDao.getAwsCredentialsList();
	}

	@Override
	public AwsCredentials getAwsCredentialsListById(long id) {
		return commonDao.getAwsCredentialsListById(id);
	}

	@Override
	public int addAndDeleteSchedulerId(long id, List<SchedulerSlave> schedulerServerSlave) {
		return commonDao.addAndDeleteSchedulerMasterMapping(id,schedulerServerSlave);
	}

	@Override
	public List<SchedulerType> getSchedulerType() {
		return commonDao.getSchedulerType();
	}

	@Override
	public List<AwsRegions> getAwsRegionsList() {
		return commonDao.getAwsRegionsList();
	}

	
}
