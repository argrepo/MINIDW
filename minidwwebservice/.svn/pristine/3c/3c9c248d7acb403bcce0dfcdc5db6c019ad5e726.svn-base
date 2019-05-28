package com.datamodel.anvizent.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.datamodel.anvizent.service.dao.WebServiceDao;
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
@Service
public class WSServiceImpl implements WSService {

	protected static final Log LOG = LogFactory.getLog(WSServiceImpl.class);
	
	@Autowired
	private WebServiceDao webServiceDao;

	
	public WebServiceTemplateMaster getWebserviceTempleteDetails(Long wsTemplateId, String clientId,JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.getWebserviceTempleteDetails(wsTemplateId, clientId, clientAppDbJdbcTemplate);
	}

	public List<WebServiceAuthenticationTypes> getWebServiceAuthenticationTypes(JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.getWebServiceAuthenticationTypes(clientAppDbJdbcTemplate);
	}

	@Override
	public int saveWebserviceMasterConnectionMapping(String clientId, WebServiceConnectionMaster webServiceConnectionMaster, JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.saveWebserviceMasterConnectionMapping(clientId, webServiceConnectionMaster, clientAppDbJdbcTemplate);
	}

	@Override
	public List<WebServiceConnectionMaster> getWebServiceConnections(String clientId, String userId, JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.getWebServiceConnections(clientId, userId, clientAppDbJdbcTemplate);
	}

	@Override
	public int saveWebServiceTemplate(WebServiceTemplateMaster webServiceTemplateMaster,JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.saveWebServiceTemplate(webServiceTemplateMaster,clientAppDbJdbcTemplate);
	}

	@Override
	public WebServiceConnectionMaster getWebServiceConnectionDetails(Long wsConId, String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.getWebServiceConnectionDetails(wsConId, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<WebServiceApi> getWebServiceApiDetails(Long wsConId, Long ilId, String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.getWebServiceApiDetails(wsConId, ilId, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public WebServiceApi getWebServiceApi(Long wsConId, Long ilId, String clientId, JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.getWebServiceApi(wsConId, ilId, clientId, clientAppDbJdbcTemplate);
	}

	@Override
	public List<WebServiceTemplateMaster> getWebserviceTemplate(JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.getWebserviceTemplate(clientAppDbJdbcTemplate);
	}

	@Override
	public WebServiceTemplateMaster getWebServiceTemplateById(WebServiceTemplateMaster webServiceTemplateMaster,JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.getWebServiceTemplateById(webServiceTemplateMaster,clientAppDbJdbcTemplate);
	}

	@Override
	public int updateWebserviceMasterConnectionMapping(String clientId, WebServiceConnectionMaster webServiceConnectionMaster, JdbcTemplate clientAppDbJdbcTemplate) {
		return webServiceDao.updateWebserviceMasterConnectionMapping(clientId, webServiceConnectionMaster, clientAppDbJdbcTemplate);
	}

	@Override
	public List<TimeZones> getTimeZones() {
		return webServiceDao.getTimeZones();
	}

}
