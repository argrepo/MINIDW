package com.datamodel.anvizent.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.model.ClientDbCredentials;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.HybridClientsGrouping;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Role;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerClientConfig;
import com.datamodel.anvizent.service.model.User;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	protected static final Log LOG = LogFactory.getLog(UserDetailsServiceImpl.class);
	
	@Autowired
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int createUser(User user , JdbcTemplate clientJdbcTemplate) {
		return userDao.createUser(user , clientJdbcTemplate);
	}
	@Override
	public ClientDbCredentials getClientDbDetails(String clientId) {
		return userDao.getClientDbDetails(clientId);
	}
	

	@Override
	public ClientJdbcInstance getClientJdbcInstance(String clientId) throws AnvizentCorewsException {
		return new ClientJdbcInstance( userDao.getClientDbDetails(clientId));
	}
	@Override
	public  ClientJdbcInstance getClientJdbcInstance(HttpServletRequest request) throws AnvizentCorewsException {
		String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
		return getClientJdbcInstance(clientIdfromHeader);
	}
	@Override
	public User verifyUserAuthentication(String username, String password, JdbcTemplate clientJdbcTemplate){
		return userDao.verifyUserAuthentication(username, password, clientJdbcTemplate);
	}

	public User getUserDetailszd(String userIdOrUsername, JdbcTemplate clientJdbcTemplate){
		return userDao.getUser(userIdOrUsername, clientJdbcTemplate);
	}

	@Override
	public List<Industry> getAllIndustries(JdbcTemplate clientJdbcTemplate){
		return userDao.getAllIndustries(clientJdbcTemplate);
	}

	@Override
	public boolean getUserActivationKeyStatus(String userId, JdbcTemplate clientJdbcTemplate){
		return userDao.getUserActivationKeyStatus(userId, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public int saveConnection(String userId, ILConnection connection, JdbcTemplate clientJdbcTemplate){
		return userDao.saveConnection(userId, connection, clientJdbcTemplate);

	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void createClientSchema(String schemaName, List<DLInfo> dlListForIndustry, List<ILInfo> ilListForAllDLs,
			JdbcTemplate clientJdbcTemplate) {
		userDao.createClientSchema(schemaName, dlListForIndustry, ilListForAllDLs, clientJdbcTemplate);
	}

	@Override
	public List<DLInfo> getAllDLs(int industryId, JdbcTemplate clientJdbcTemplate){
		return userDao.getAllDLs(industryId, clientJdbcTemplate);
	}

	@Override
	public List<ILInfo> getAllILs(int dlId, JdbcTemplate clientJdbcTemplate){
		return userDao.getAllILs(dlId, clientJdbcTemplate);
	}

	@Override
	public List<ILInfo> getStagingIlsByDlId(List<Integer> ilIds, JdbcTemplate clientJdbcTemplate){
		return userDao.getStagingIlsByDlId(ilIds, clientJdbcTemplate);
	}

	@Override
	public Boolean isClientValid(String clientId, JdbcTemplate clientJdbcTemplate){
		return userDao.isClientValid(clientId, clientJdbcTemplate);
	}

	@Override
	public Industry getIndustryById(int industryId, JdbcTemplate clientJdbcTemplate){
		return userDao.getIndustryById(industryId, clientJdbcTemplate);
	}

	@Override
	public List<Role> getUserRoles(String userIdOrUsername, JdbcTemplate clientJdbcTemplate){
		return userDao.getUserRoles(userIdOrUsername, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void createAllJobTables(String schemaName, JdbcTemplate clientJdbcTemplate) {
	}

	@Override
	public boolean isClientExist(String clientId, JdbcTemplate clientJdbcTemplate){
		return userDao.isClientExist(clientId, clientJdbcTemplate);
	}

	@Override
	@Transactional(value = "user_transactionManager", readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = AnvizentRuntimeException.class)
	public void mapDLToClient(String clientId, int dlId, boolean isLocked, Modification modification, JdbcTemplate clientJdbcTemplate){
	}

	@Override
	public Map<String, Object> getLatestMinidwVersion(JdbcTemplate clientJdbcTemplate){
		return userDao.getLatestMinidwVersion(clientJdbcTemplate);
	}

	@Override
	public S3BucketInfo getS3BucketInfoByClientId(String clientId, JdbcTemplate clientJdbcTemplate){
		return userDao.getS3BucketInfoByClientId(clientId, clientJdbcTemplate);
	}


	@Override
	public S3BucketInfo getS3BucketInfoById(String clientId, long id, JdbcTemplate clientJdbcTemplate) {
		return userDao.getS3BucketInfoById(clientId,id, clientJdbcTemplate);
	}
	
	@Override
	public List<CloudClient> getActiveClientsList() {
		return userDao.getActiveClientsList();
	}
	
	@Override
	public CloudClient getClientDetails(String clientId) {
		return userDao.getClientDetails(clientId);
	}

	@Override
	public SchedulerClientConfig getSchedulerClientConfigInfo() {
		return userDao.getSchedulerClientConfigInfo();
	}

	@Override
	public HybridClientsGrouping getHybridClientGroupingDetailsByAccessKey(String accessKey) {
		// TODO Auto-generated method stub
		return userDao.getHybridClientGroupingDetailsByAccessKey(accessKey);
	}

}
