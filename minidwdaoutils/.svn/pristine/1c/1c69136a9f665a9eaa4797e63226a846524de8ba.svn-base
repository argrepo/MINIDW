package com.datamodel.anvizent.service.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import com.datamodel.anvizent.service.model.TimeZones;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceAuthenticationTypes;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.service.model.WebServiceTemplateMaster;

/**
 * 
 * @author rajesh.anthari
 *
 */
public interface WebServiceDao {

	WebServiceTemplateMaster getWebserviceTempleteDetails(Long wsTemplateId, String clientId,JdbcTemplate clientAppDbJdbcTemplate);

	List<WebServiceAuthenticationTypes> getWebServiceAuthenticationTypes(JdbcTemplate clientAppDbJdbcTemplate);

	int saveWebServiceTemplate(WebServiceTemplateMaster webServiceTemplateMaster,JdbcTemplate clientAppDbJdbcTemplate);

	int saveWebserviceMasterConnectionMapping(String clientId, WebServiceConnectionMaster webServiceConnectionMaster, JdbcTemplate clientAppDbJdbcTemplate);

	List<WebServiceConnectionMaster> getWebServiceConnections(String clientId, String userId, JdbcTemplate clientAppDbJdbcTemplate);

	WebServiceConnectionMaster getWebServiceConnectionDetails(Long wsConId, String clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<WebServiceApi> getWebServiceApiDetails(Long wsConId, Long ilId, String clientId, JdbcTemplate clientAppDbJdbcTemplate);

	WebServiceApi getWebServiceApi(Long wsConId, Long ilId, String clientId, JdbcTemplate clientAppDbJdbcTemplate);

	List<WebServiceTemplateMaster> getWebserviceTemplate(JdbcTemplate clientAppDbJdbcTemplate);

	WebServiceTemplateMaster getWebServiceTemplateById(WebServiceTemplateMaster webServiceTemplateMaster,JdbcTemplate clientAppDbJdbcTemplate);

	int updateWebserviceMasterConnectionMapping(String clientId, WebServiceConnectionMaster webServiceConnectionMaster, JdbcTemplate clientAppDbJdbcTemplate);

	List<TimeZones> getTimeZones();
	
	int updateRetryPaginationAtWsMapping(int id,String retryPaginationData,JdbcTemplate clientAppDbJdbcTemplate);
	
	public int updateIncrementalDatePaginationAtWsMapping(int id , String incrementalPaginationData,JdbcTemplate clientAppDbJdbcTemplate);
	
}
