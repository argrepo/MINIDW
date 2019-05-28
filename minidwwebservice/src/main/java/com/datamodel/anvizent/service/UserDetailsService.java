package com.datamodel.anvizent.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;

import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
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
public interface UserDetailsService {

	int createUser(User user, JdbcTemplate clientJdbcTemplate);

	User verifyUserAuthentication(String username, String password, JdbcTemplate clientJdbcTemplate);
	
	ClientDbCredentials getClientDbDetails(String clientId);
	
	ClientJdbcInstance getClientJdbcInstance(String clientId) throws AnvizentCorewsException;
	
	ClientJdbcInstance getClientJdbcInstance(HttpServletRequest request) throws AnvizentCorewsException;
	
	User getUserDetailszd(String userIdOrUsername, JdbcTemplate clientJdbcTemplate);

	Boolean isClientValid(String client_Id, JdbcTemplate clientJdbcTemplate);

	List<Industry> getAllIndustries(JdbcTemplate clientJdbcTemplate);

	boolean getUserActivationKeyStatus(String userid, JdbcTemplate clientJdbcTemplate);

	int saveConnection(String userId, ILConnection connection, JdbcTemplate clientJdbcTemplate);

	void createClientSchema(String schemaName, List<DLInfo> dlListForIndustry, List<ILInfo> ilListForAllDLs, JdbcTemplate clientJdbcTemplate);

	List<DLInfo> getAllDLs(int industryId, JdbcTemplate clientJdbcTemplate);

	List<ILInfo> getAllILs(int dlId, JdbcTemplate clientJdbcTemplate);

	Industry getIndustryById(int industryId, JdbcTemplate clientJdbcTemplate);

	List<Role> getUserRoles(String userIdOrUsername, JdbcTemplate clientJdbcTemplate);

	List<ILInfo> getStagingIlsByDlId(List<Integer> ilIds, JdbcTemplate clientJdbcTemplate);

	void createAllJobTables(String schemaName, JdbcTemplate clientJdbcTemplate);

	boolean isClientExist(String clientId, JdbcTemplate clientJdbcTemplate);

	void mapDLToClient(String clientId, int dl_Id, boolean isLocked, Modification modification, JdbcTemplate clientJdbcTemplate);

	Map<String, Object> getLatestMinidwVersion(JdbcTemplate clientJdbcTemplate);

	S3BucketInfo getS3BucketInfoByClientId(String clientId, JdbcTemplate clientJdbcTemplate);

	S3BucketInfo getS3BucketInfoById(String clientId,long id, JdbcTemplate clientJdbcTemplate);
	
	List<CloudClient> getActiveClientsList();
	
	public CloudClient getClientDetails(String clientId);

	SchedulerClientConfig getSchedulerClientConfigInfo();
	
	HybridClientsGrouping getHybridClientGroupingDetailsByAccessKey(String accessKey);
}
