package com.datamodel.anvizent.data.RestController;

import static minidwclientws.WebServiceUtils.closeAPISession;

import java.io.File;
import java.security.cert.CertificateException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.WSService;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileInfo;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.OAuth1;
import com.datamodel.anvizent.service.model.TimeZones;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceAuthenticationTypes;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.service.model.WebServiceJoin;
import com.datamodel.anvizent.service.model.WebServiceTemplateAuthRequestparams;
import com.datamodel.anvizent.service.model.WebServiceTemplateMaster;
import com.datamodel.anvizent.spring.AppProperties;
import minidwclientws.WebServiceUtils;

/**
 * 
 * @author rajesh.anthari
 *
 */
@Import(AppProperties.class)
@RestController("webServiceDataRestController")
@RequestMapping("" + Constants.AnvizentURL.COMMON_SERVICES_BASE_URL)
@CrossOrigin
public class WebServiceDataRestController
{

	protected static final Log LOGGER = LogFactory.getLog(WebServiceDataRestController.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PackageService packageService;
	@Autowired
	private WSService wSService;
	@Autowired
	FileService fileService;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	@RequestMapping(value = "/getWebserviceTempleteDetails/{templateId}", method = RequestMethod.GET)
	public DataResponse getWebserviceTempleteDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("templateId") Long wsTemplateId, HttpServletRequest request, Locale locale)
	{
		LOGGER.info("in getWebserviceTempleteDetails()");

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		messages.add(message);
		dataResponse.setMessages(messages);

		WebServiceTemplateMaster webServiceTemplateMaster = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			webServiceTemplateMaster = wSService.getWebserviceTempleteDetails(wsTemplateId, clientIdfromHeader, clientAppDbJdbcTemplate);

			if( webServiceTemplateMaster != null )
			{
				if( StringUtils.isNotEmpty(webServiceTemplateMaster.getApiSessionAuthURL()) )
				{
					JSONObject sessionAuthUrlObj = new JSONObject(webServiceTemplateMaster.getApiSessionAuthURL());
					if( sessionAuthUrlObj.length() != 0 )
					{
						webServiceTemplateMaster.setLoginUrl(sessionAuthUrlObj.getString("loginUrl"));
						webServiceTemplateMaster.setLogoutUrl(sessionAuthUrlObj.getString("logoutUrl"));
					}
				}
				dataResponse.setObject(webServiceTemplateMaster);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveWSTamplateDetails", null, locale));
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveWSTamplateDetails", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getWebServiceAuthenticationTypes", method = RequestMethod.GET)
	public DataResponse getWebServiceAuthenticationTypes(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		LOGGER.info("in getWebserviceTempleteDetails()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;

		List<WebServiceAuthenticationTypes> webServiceAuthenticationTypes = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			webServiceAuthenticationTypes = wSService.getWebServiceAuthenticationTypes(clientAppDbJdbcTemplate);
			if( webServiceAuthenticationTypes != null && webServiceAuthenticationTypes.size() > 0 )
			{
				dataResponse.setObject(webServiceAuthenticationTypes);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.listNotFound", null, locale));

			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToPackagesList", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/saveWebserviceMasterConnectionMapping", method = RequestMethod.POST)
	public DataResponse saveWebserviceMasterConnectionMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebServiceConnectionMaster webServiceConnectionMaster, HttpServletRequest request, Locale locale)
	{
		LOGGER.info(" in saveWebserviceMasterConnectionMapping()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int count = -1;
		try
		{

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			webServiceConnectionMaster.setModification(modification);

			if( webServiceConnectionMaster.getDataSourceName().equals("-1") )
			{
				ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader), webServiceConnectionMaster.getDataSourceNameOther());
				clientDataSource.setDataSourceFrom(Constants.SourceType.WEBSERVICE);
				clientDataSource.setModification(modification);
				packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
				webServiceConnectionMaster.setDataSourceName(webServiceConnectionMaster.getDataSourceNameOther());
			}

			count = wSService.saveWebserviceMasterConnectionMapping(clientId, webServiceConnectionMaster, clientAppDbJdbcTemplate);
			if( count > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.webServiceMasterConnectionSavedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.text.webserviceMasterConnectionMappingNotSavedSuccesfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.connectionNameAlreadyExist", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToPackagesList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getWebServiceConnections", method = RequestMethod.GET)
	public DataResponse getWebServiceConnections(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale)
	{
		LOGGER.info("in getWebServiceConnections()");
		List<WebServiceConnectionMaster> webServiceConnectionMasterList = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			webServiceConnectionMasterList = wSService.getWebServiceConnections(clientIdfromHeader, userId, clientAppDbJdbcTemplate);
			if( webServiceConnectionMasterList != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(webServiceConnectionMasterList);
			}

		}
		catch ( AnvizentRuntimeException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToretrieveConnectionsList", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveWebServiceTemplate", method = RequestMethod.POST)
	public DataResponse saveWebServiceTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebServiceTemplateMaster webServiceTemplateMaster, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int templateInfo = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId + "\n" + request.getHeader("Browser-Detail"));
			webServiceTemplateMaster.setModification(modification);
			if( webServiceTemplateMaster.getWebServiceAuthenticationTypes().getId() == 3 )
			{
				List<WebServiceTemplateAuthRequestparams> webServiceTemplateAuthRequestparams = new ArrayList<>();
				WebServiceTemplateAuthRequestparams userParam = new WebServiceTemplateAuthRequestparams();
				userParam.setParamName("Username");
				userParam.setMandatory(true);
				userParam.setPasswordType(false);
				webServiceTemplateAuthRequestparams.add(userParam);

				WebServiceTemplateAuthRequestparams passwordParam = new WebServiceTemplateAuthRequestparams();
				passwordParam.setParamName("Password");
				passwordParam.setMandatory(true);
				passwordParam.setPasswordType(true);
				webServiceTemplateAuthRequestparams.add(passwordParam);
				webServiceTemplateMaster.setWebServiceTemplateAuthRequestparams(webServiceTemplateAuthRequestparams);
			}
			System.out.println(webServiceTemplateMaster.getoAuth1().toString());
			templateInfo = wSService.saveWebServiceTemplate(webServiceTemplateMaster, clientAppDbJdbcTemplate);
			if( templateInfo != 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingTheWebserviceTemplate", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while saveWebServiceTemplate() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.webServiceNameAlreadyExist", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while saveWebServiceTemplate() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingTheWebserviceTemplate", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/testAuthenticationUrl", method = RequestMethod.POST)
	public DataResponse testAuthenticateWebServiceApi(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebServiceConnectionMaster webServiceConnectionMaster, HttpServletRequest request, Locale locale)
	{
		LOGGER.info("in testAuthenticationUrl()");

		ResponseEntity<Map> response = null;
		ResponseEntity<String> soapResponse = null;
		HttpStatus statusCode = null;
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		RestTemplate authenticateRestTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			if( webServiceConnectionMaster.getId() != null )
			{
				webServiceConnectionMaster = wSService.getWebServiceConnectionDetails(webServiceConnectionMaster.getId(), clientIdfromHeader, clientAppDbJdbcTemplate);
			}

			if( webServiceConnectionMaster.getWebServiceTemplateMaster().isSslDisable() )
			{
				TrustStrategy acceptingTrustStrategy = new TrustStrategy()
				{
					@Override
					public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException
					{
						return true;
					}
				};
				SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
				SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
				CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
				HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
				requestFactory.setHttpClient(httpClient);

				authenticateRestTemplate = new RestTemplate(requestFactory);
			}
			else
			{
				authenticateRestTemplate = new RestTemplate();
			}

			if( webServiceConnectionMaster.getWebServiceTemplateMaster().getWebserviceType().equals("SOAP") )
			{
				soapResponse = WebServiceUtils.testAuthenticationUrl(authenticateRestTemplate, webServiceConnectionMaster);
				if( soapResponse != null )
				{
					statusCode = soapResponse.getStatusCode();
				}

			}
			else
			{
				response = WebServiceUtils.testAuthenticationUrl(webServiceConnectionMaster, authenticateRestTemplate, clientJdbcInstance.getClientDbCredentials());
				if( response != null )
				{
					statusCode = response.getStatusCode();
				}
			}

			if( statusCode == null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else if( statusCode.equals(HttpStatus.OK) )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				message.setText(messageSource.getMessage("anvizent.message.success.text.ok", null, locale));
				dataResponse.setMessages(messages);
				if( webServiceConnectionMaster.getWebServiceTemplateMaster().getWebserviceType().equals("SOAP") )
				{
					dataResponse.setObject(soapResponse.getBody());
				}
				else
				{
					dataResponse.setObject(response.getBody());
				}
			}
			else if( statusCode.equals(HttpStatus.NO_CONTENT) )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				message.setText(messageSource.getMessage("anvizent.message.validation.text.noContent", null, locale));
				dataResponse.setMessages(messages);
				dataResponse.setObject(response.getBody());
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, locale) + ". Status Code : " + statusCode);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			response = closeAPISession(webServiceConnectionMaster, authenticateRestTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getOAuth1AuthorizationURL", method = RequestMethod.POST)
	public DataResponse getOAuth1AuthorizationURL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebServiceConnectionMaster webServiceConnectionMaster, HttpServletRequest request, Locale locale)
	{
		LOGGER.info("in getOAuth1AuthorizationURL()");

		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		try
		{
			
			OAuth1 oAuth1 = WebServiceUtils.getOAuth1RequestToken(webServiceConnectionMaster.getWebServiceTemplateMaster());
			
			if( oAuth1.getAuthURL() != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				message.setText(messageSource.getMessage("anvizent.message.success.text.ok", null, locale));
				dataResponse.setMessages(messages);
				dataResponse.setObject(oAuth1);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getWebServiceConnectionDetails/{wsConId}/{ilId}", method = RequestMethod.GET)
	public DataResponse getWebServiceConnectionDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("wsConId") Long wsConId, @PathVariable("ilId") Long ilId, HttpServletRequest request, Locale locale)
	{
		LOGGER.info("in getWebserviceTempleteDetails()");
		ResponseEntity<Map> response = null;
		ResponseEntity<String> soapResponse = null;
		HttpStatus statusCode = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		messages.add(message);
		dataResponse.setMessages(messages);

		WebServiceConnectionMaster webServiceConnectionMaster = null;
		List<WebServiceApi> webServiceApiList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			webServiceConnectionMaster = wSService.getWebServiceConnectionDetails(wsConId, clientIdfromHeader, clientAppDbJdbcTemplate);

			if( webServiceConnectionMaster != null )
			{

				Map<String, Object> map = new HashMap<>();
				webServiceApiList = wSService.getWebServiceApiDetails(wsConId, ilId, clientIdfromHeader, clientAppDbJdbcTemplate);
				map.put("conDetails", webServiceConnectionMaster);
				map.put("apiDetails", webServiceApiList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);

				try
				{
					if( webServiceConnectionMaster.getWebServiceTemplateMaster().getWebserviceType().equals("SOAP") )
					{
						soapResponse = WebServiceUtils.testAuthenticationUrl(restTemplate, webServiceConnectionMaster);
						if( soapResponse != null )
						{
							statusCode = soapResponse.getStatusCode();
							if( statusCode != null && statusCode.equals(HttpStatus.OK) )
							{
								map.put("authenticationResponse", statusCode);
							}
							else if( statusCode != null && statusCode.equals(HttpStatus.NO_CONTENT) )
							{
								map.put("authenticationResponse", "[]");
							}
							else
							{
								map.put("authenticationResponse", "Error Code " + soapResponse.getStatusCode());
							}
						}
					}
					else
					{
						response = WebServiceUtils.testAuthenticationUrl(webServiceConnectionMaster, restTemplate, clientJdbcInstance.getClientDbCredentials());
						if( response != null )
						{
							statusCode = response.getStatusCode();
							if( statusCode != null && statusCode.equals(HttpStatus.OK) )
							{
								map.put("authenticationResponse", response.getBody());
							}
							else if( statusCode != null && statusCode.equals(HttpStatus.NO_CONTENT) )
							{
								map.put("authenticationResponse", "[]");
							}
							else
							{
								map.put("authenticationResponse", "Error Code " + response.getStatusCode());
							}
						}
					}
				}
				catch ( Exception e )
				{
					LOGGER.error("error while fetching ws connection meta info", e);
				}
				dataResponse.setObject(map);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveWSTamplateDetails", null, locale));
			}
		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveWSTamplateDetails", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			response = closeAPISession(webServiceConnectionMaster, restTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getWebServiceConnectionDetailsById/{wsConId}", method = RequestMethod.GET)
	public DataResponse getWebServiceConnectionDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("wsConId") Long wsConId, HttpServletRequest request, Locale locale)
	{
		LOGGER.info("in getWebServiceConnectionDetailsById()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		WebServiceConnectionMaster webServiceConnectionMaster = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			webServiceConnectionMaster = wSService.getWebServiceConnectionDetails(wsConId, clientIdfromHeader, clientAppDbJdbcTemplate);
			if( webServiceConnectionMaster != null )
			{
				dataResponse.setObject(webServiceConnectionMaster);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveWSTamplateDetails", null, locale));
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveWSTamplateDetails", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/validateWebService", method = RequestMethod.POST)
	public DataResponse validateWebService(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebServiceApi webService, HttpServletRequest request, Locale locale)
	{
		LOGGER.info("in validateWebService()");
		LOGGER.debug("validate method started\t\t\t\t\t\t" + new Date());
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<LinkedHashMap<String, Object>> finalformattedApiResponse = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		RestTemplate connectionRestTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			if( webService != null )
			{

				if( webService.getWebServiceConnectionMaster().getId() != null )
				{
					webService.setWebServiceConnectionMaster(wSService.getWebServiceConnectionDetails(webService.getWebServiceConnectionMaster().getId(), clientIdfromHeader, clientAppDbJdbcTemplate));

					if( webService.getWebServiceConnectionMaster().getWebServiceTemplateMaster().isSslDisable() )
					{
						connectionRestTemplate = WebServiceUtils.getRestTemplate();
					}
					else
					{
						connectionRestTemplate = new RestTemplate();
					}

					finalformattedApiResponse = WebServiceUtils.validateWebService(webService, connectionRestTemplate, clientJdbcInstance.getClientDbCredentials());

				}
			}
			if( finalformattedApiResponse != null )
			{
				boolean isValidate = Boolean.FALSE;

				if( finalformattedApiResponse.size() > 0 )
				{
					isValidate = Boolean.TRUE;
				}
				else
				{
					isValidate = Boolean.FALSE;
				}

				if( isValidate )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.verifiedSuccessfully", null, locale));
					messages.add(message);
					dataResponse.setObject(finalformattedApiResponse);
					dataResponse.setMessages(messages);
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.noRecordsFound", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}

			}
			else
			{

				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.failedToConnect", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);

			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while validating ws api ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			message.setText(message.getText() + "\n" + t.getLocalizedMessage());
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			closeAPISession(webService.getWebServiceConnectionMaster(), connectionRestTemplate);
		}
		LOGGER.debug("validate method end\t\t\t\t\t\t" + new Date());
		return dataResponse;
	}

	@RequestMapping(value = "/joinAndSaveWsApi", method = RequestMethod.POST)
	public DataResponse joinAndSaveWebServiceData(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService, Locale locale, HttpServletRequest request)
	{
		LOGGER.info("in joinAndSaveWsApi()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<LinkedHashMap<String, Object>> finalformattedApiResponse = new ArrayList<>();
		List<String> originalFileheaders = null;
		WebServiceApi webServiceApi = new WebServiceApi();
		JdbcTemplate clientStagingJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		RestTemplate connectionRestTemplate = null;
		try
		{

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			User user = new User();
			user.setUserId(clientId);

			CommonUtils.deleteWebServiceIlConnectionMapping(user, clientId, null, clientAppDbJdbcTemplate, webService, packageService.getPackageDao(), fileService.getFileDao(), modification);

			if( webService.getWebServiceJoin().size() > 0 )
			{

				for (WebServiceJoin webServiceJoin : webService.getWebServiceJoin())
				{

					webService.setUrl(webServiceJoin.getWebServiceUrl());
					webService.setBaseUrl(webServiceJoin.getBaseUrl());
					webService.setSoapBodyElement(webServiceJoin.getSoapBodyElement());
					webService.setBaseUrlRequired(webServiceJoin.getBaseUrlRequired());
					webService.setResponseObjName(webServiceJoin.getResponseObjectName());
					webService.setResponseColumnObjName(webServiceJoin.getResponseColumnObjectName());
					webService.setRequestMethod(webServiceJoin.getWebServiceMethodType());
					webService.setClientId(Integer.parseInt(clientId));
					webService.setApiName(webServiceJoin.getWebServiceApiName());
					webService.setApiPathParams(webServiceJoin.getApiPathParams());
					webService.setRequestParameters(webServiceJoin.getApiRequestParams());
					webService.setApiBodyParams(webServiceJoin.getApiBodyParams());
					webService.setPaginationRequired(webServiceJoin.getPaginationRequired());
					webService.setPaginationType(webServiceJoin.getPaginationType());
					webService.setPaginationRequestParamsData(webServiceJoin.getPaginationRequestParamsData());
					webService.setIncrementalUpdate(webServiceJoin.getIncrementalUpdate());
					webService.setIncrementalUpdateparamdata(webServiceJoin.getIncrementalUpdateparamdata());
					webService.setValidateOrPreview(webServiceJoin.getValidateOrPreview());

					webServiceApi.setApiMethodType(webServiceJoin.getWebServiceMethodType());
					webServiceApi.setApiName(webServiceJoin.getWebServiceApiName());
					webServiceApi.setSoapBodyElement(webServiceJoin.getSoapBodyElement());
					webServiceApi.setApiUrl(webServiceJoin.getWebServiceUrl());
					webServiceApi.setBaseUrl(webServiceJoin.getBaseUrl());
					webServiceApi.setBaseUrlRequired(webServiceJoin.getBaseUrlRequired());
					webServiceApi.setResponseObjectName(webServiceJoin.getResponseObjectName());
					webServiceApi.setResponseColumnObjectName(webServiceJoin.getResponseColumnObjectName());
					webServiceApi.setApiPathParams(webServiceJoin.getApiPathParams());
					webServiceApi.setApiBodyParams(webServiceJoin.getApiBodyParams());
					webServiceApi.setApiRequestParams(webServiceJoin.getApiRequestParams());
					webServiceApi.setPaginationRequired(webServiceJoin.getPaginationRequired());
					webServiceApi.setPaginationType(webServiceJoin.getPaginationType());
					webServiceApi.setPaginationRequestParamsData(webServiceJoin.getPaginationRequestParamsData());
					webServiceApi.setIncrementalUpdate(webServiceJoin.getIncrementalUpdate());
					webServiceApi.setIncrementalUpdateparamdata(webServiceJoin.getIncrementalUpdateparamdata());
					webServiceApi.setValidateOrPreview(webServiceJoin.getValidateOrPreview());

					webServiceApi.setIlId((long) webService.getIlId());

					// String incrementalDate =
					// packageService.getILIncrementalUpdateDate(null,
					// webService.getIlId(),
					// "",Constants.SourceType.WEBSERVICE,0,
					// clientStagingJdbcTemplate);
					// webServiceApi.setInclUpdateDate(null);

					webServiceApi.setWebServiceConnectionMaster(wSService.getWebServiceConnectionDetails(Long.valueOf(webService.getWsConId()), clientIdfromHeader, clientAppDbJdbcTemplate));

					if( webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().isSslDisable() )
					{
						connectionRestTemplate = WebServiceUtils.getRestTemplate();
					}
					else
					{
						connectionRestTemplate = new RestTemplate();
					}
					finalformattedApiResponse = WebServiceUtils.validateWebService(webServiceApi, connectionRestTemplate, clientJdbcInstance.getClientDbCredentials());

					String filePath = CommonUtils.getFilePathForWsApi(webServiceApi, finalformattedApiResponse, null);

					if( filePath != null )
					{

						File tempFile = new File(filePath);

						originalFileheaders = fileService.getHeadersFromFile(tempFile.getAbsolutePath(), Constants.FileType.CSV, ",", null);

						if( originalFileheaders.size() > 0 )
						{

							if( originalFileheaders.size() == 1 && originalFileheaders.get(0).length() == 0 )
							{
								message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
								message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
								messages.add(message);
								dataResponse.setMessages(messages);
								if( tempFile != null )
								{
									tempFile.delete();
								}
								return dataResponse;
							}
							CommonUtils.saveIlConnectionWebServiceMapping(filePath, originalFileheaders, clientId, clientStagingJdbcTemplate, clientAppDbJdbcTemplate, webService, packageService.getPackageDao(), fileService.getFileDao(), modification);

						}
					}
					closeAPISession(webServiceApi.getWebServiceConnectionMaster(), connectionRestTemplate);
				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while saving ws api joining", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientStagingJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/joinAndSaveWsApiForJsonOrXml", method = RequestMethod.POST)
	public DataResponse joinAndSaveWsApiForJsonOrXml(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService, Locale locale, HttpServletRequest request)
	{
		LOGGER.info("in joinAndSaveWsApiForJsonOrXml()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<LinkedHashMap<String, Object>> finalformattedApiResponse = new ArrayList<>();
		WebServiceApi webServiceApi = new WebServiceApi();
		JdbcTemplate clientStagingJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String wsMappingids = "";
		try
		{

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			User user = new User();
			user.setUserId(clientId);

			FileInfo fileInfo = new FileInfo();
			fileInfo.setPackageId(webService.getPackageId());
			fileInfo.setClientid(clientId);

			fileService.getFileDao().deleteIlConnectionWebServiceMapping(fileInfo, null, modification, webService, clientAppDbJdbcTemplate);

			if( webService.getWebServiceJoin().size() > 0 )
			{

				for (WebServiceJoin webServiceJoin : webService.getWebServiceJoin())
				{

					webService.setUrl(webServiceJoin.getWebServiceUrl());
					webService.setBaseUrl(webServiceJoin.getBaseUrl());
					webService.setBaseUrlRequired(webServiceJoin.getBaseUrlRequired());
					webService.setResponseObjName(webServiceJoin.getResponseObjectName());
					webService.setResponseColumnObjName(webServiceJoin.getResponseColumnObjectName());
					webService.setRequestMethod(webServiceJoin.getWebServiceMethodType());
					webService.setClientId(Integer.parseInt(clientId));
					webService.setApiName(webServiceJoin.getWebServiceApiName());
					webService.setApiPathParams(webServiceJoin.getApiPathParams());
					webService.setRequestParameters(webServiceJoin.getApiRequestParams());
					webService.setApiBodyParams(webServiceJoin.getApiBodyParams());
					webService.setPaginationRequired(webServiceJoin.getPaginationRequired());
					webService.setPaginationType(webServiceJoin.getPaginationType());
					webService.setPaginationRequestParamsData(webServiceJoin.getPaginationRequestParamsData());
					webService.setIncrementalUpdate(webServiceJoin.getIncrementalUpdate());
					webService.setIncrementalUpdateparamdata(webServiceJoin.getIncrementalUpdateparamdata());
					webService.setValidateOrPreview(webServiceJoin.getValidateOrPreview());

					webServiceApi.setApiMethodType(webServiceJoin.getWebServiceMethodType());
					webServiceApi.setApiName(webServiceJoin.getWebServiceApiName());
					webServiceApi.setApiUrl(webServiceJoin.getWebServiceUrl());
					webServiceApi.setBaseUrl(webServiceJoin.getBaseUrl());
					webServiceApi.setBaseUrlRequired(webServiceJoin.getBaseUrlRequired());
					webServiceApi.setResponseObjectName(webServiceJoin.getResponseObjectName());
					webServiceApi.setResponseColumnObjectName(webServiceJoin.getResponseColumnObjectName());
					webServiceApi.setApiPathParams(webServiceJoin.getApiPathParams());
					webServiceApi.setApiBodyParams(webServiceJoin.getApiBodyParams());
					webServiceApi.setApiRequestParams(webServiceJoin.getApiRequestParams());
					webServiceApi.setPaginationRequired(webServiceJoin.getPaginationRequired());
					webServiceApi.setPaginationType(webServiceJoin.getPaginationType());
					webServiceApi.setPaginationRequestParamsData(webServiceJoin.getPaginationRequestParamsData());
					webServiceApi.setIncrementalUpdate(webServiceJoin.getIncrementalUpdate());
					webServiceApi.setIncrementalUpdateparamdata(webServiceJoin.getIncrementalUpdateparamdata());
					webServiceApi.setValidateOrPreview(webServiceJoin.getValidateOrPreview());
					webServiceApi.setIlId((long) webService.getIlId());

					// String incrementalDate =
					// packageService.getILIncrementalUpdateDate(null,
					// webService.getIlId(), "",Constants.SourceType.WEBSERVICE,
					// clientStagingJdbcTemplate);
					// webServiceApi.setInclUpdateDate(incrementalDate);

					webServiceApi.setWebServiceConnectionMaster(wSService.getWebServiceConnectionDetails(Long.valueOf(webService.getWsConId()), clientIdfromHeader, clientAppDbJdbcTemplate));

					finalformattedApiResponse = WebServiceUtils.validateWebService(webServiceApi, restTemplate, clientJdbcInstance.getClientDbCredentials());

					if( finalformattedApiResponse != null )
					{
						int id = fileService.getFileDao().insertIlConnectionWebServiceMappingForJsonOrXml(modification, webService, clientAppDbJdbcTemplate);
						wsMappingids += id + ",";
					}

				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(wsMappingids.substring(0, wsMappingids.length() - 1));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while saving ws api joining", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientStagingJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveWsApi", method = RequestMethod.POST)
	public DataResponse saveWebServiceData(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService, Locale locale, HttpServletRequest request)
	{
		LOGGER.info("in saveWsApi()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<LinkedHashMap<String, Object>> finalformattedApiResponse = new ArrayList<>();
		List<String> originalFileheaders = null;
		List<Column> iLcolumnNames = null;
		Set<String> iLcolumnNamesFromMapping = null;
		Map<String, Object> mappingFilesHeaders = new HashMap<>();
		WebServiceApi webServiceApi = new WebServiceApi();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		RestTemplate connectionRestTemplate = null;
		ClientJdbcInstance clientJdbcInstance = null;
		boolean isdefaultMapping = false;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			if( webService != null )
			{
				webServiceApi.setWebServiceConnectionMaster(wSService.getWebServiceConnectionDetails(Long.valueOf(webService.getWsConId()), clientIdfromHeader, clientAppDbJdbcTemplate));
			}
			JSONObject defaultMappingJSONObject = new JSONObject(webService.getDefaultMapping());
			if( defaultMappingJSONObject.length() > 0 )
			{
				isdefaultMapping = true;
				originalFileheaders = new ArrayList<>();
				iLcolumnNames = new ArrayList<Column>();
				iLcolumnNamesFromMapping = (Set<String>) defaultMappingJSONObject.keySet();
				for (String key : iLcolumnNamesFromMapping)
				{
					Column column = new Column();
					column.setColumnName(key);
					column.setDataType("TEXT");
					originalFileheaders.add((String) defaultMappingJSONObject.get(key));
					iLcolumnNames.add(column);
				}
				mappingFilesHeaders.put("originalFileheaders", originalFileheaders);
				mappingFilesHeaders.put("iLcolumnNames", iLcolumnNames);
				mappingFilesHeaders.put("isdefaultMapping", isdefaultMapping);
				if( mappingFilesHeaders != null )
				{
					dataResponse.setObject(mappingFilesHeaders);
					message.setCode(messageSource.getMessage("anvizent.message.error.success", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}

			}
			else
			{

				webServiceApi.setApiPathParams(webService.getApiPathParams());
				webServiceApi.setApiMethodType(webService.getRequestMethod());
				webServiceApi.setApiName(webService.getApiName());
				webServiceApi.setApiUrl(webService.getUrl());
				webServiceApi.setSoapBodyElement(webService.getSoapBodyElement());
				webServiceApi.setBaseUrl(webService.getBaseUrl());
				webServiceApi.setBaseUrlRequired(webService.getBaseUrlRequired());
				webServiceApi.setResponseObjectName(webService.getResponseObjName());
				webServiceApi.setResponseColumnObjectName(webService.getResponseColumnObjName());
				webServiceApi.setApiRequestParams(webService.getRequestParameters());
				webServiceApi.setApiBodyParams(webService.getApiBodyParams());
				webServiceApi.setPaginationRequired(webService.getPaginationRequired());
				webServiceApi.setPaginationType(webService.getPaginationType());
				webServiceApi.setPaginationRequestParamsData(webService.getPaginationRequestParamsData());
				webServiceApi.setIncrementalUpdate(webService.getIncrementalUpdate());
				webServiceApi.setIncrementalUpdateparamdata(webService.getIncrementalUpdateparamdata());
				webServiceApi.setValidateOrPreview(webService.getValidateOrPreview());
				if( webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().isSslDisable() )
				{
					connectionRestTemplate = WebServiceUtils.getRestTemplate();
				}
				else
				{
					connectionRestTemplate = new RestTemplate();
				}
				finalformattedApiResponse = WebServiceUtils.validateWebService(webServiceApi, connectionRestTemplate, clientJdbcInstance.getClientDbCredentials());

				String filePath = CommonUtils.getFilePathForWebServiceApi(webService, finalformattedApiResponse);

				if( filePath != null )
				{
					File tempFile = new File(filePath);
					originalFileheaders = fileService.getHeadersFromFile(tempFile.getAbsolutePath(), Constants.FileType.CSV, ",", null);
					if( originalFileheaders.size() > 0 )
					{

						if( originalFileheaders.size() == 1 && originalFileheaders.get(0).length() == 0 )
						{
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
							message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
							messages.add(message);
							dataResponse.setMessages(messages);
							if( tempFile != null )
							{
								tempFile.delete();
							}
							return dataResponse;
						}

						clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

						ILInfo iLInfo = packageService.getILById(webService.getIlId(), clientId, clientAppDbJdbcTemplate);
						if( StringUtils.isNotBlank(iLInfo.getiL_table_name()) )
						{
							clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
							iLcolumnNames = packageService.getTableStructure(null, iLInfo.getiL_table_name(), webService.getIndustryId(), clientId, clientJdbcTemplate);
						}
						mappingFilesHeaders.put("originalFileheaders", originalFileheaders);
						mappingFilesHeaders.put("iLcolumnNames", iLcolumnNames);
						mappingFilesHeaders.put("isdefaultMapping", isdefaultMapping);
						mappingFilesHeaders.put("originalFileName", tempFile.getName());

						if( mappingFilesHeaders != null )
						{
							dataResponse.setObject(mappingFilesHeaders);
							message.setCode(messageSource.getMessage("anvizent.message.error.success", null, locale));
							messages.add(message);
							dataResponse.setMessages(messages);
						}

					}
					else
					{
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
						if( tempFile != null )
						{
							tempFile.delete();
						}
						return dataResponse;
					}

				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCallWebService", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;
				}

			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while saving ws api ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			closeAPISession(webServiceApi.getWebServiceConnectionMaster(), connectionRestTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/standardWsPreview/{webserviceConId}/{il_id}/{mappingId}/{packageId}", method = RequestMethod.GET)
	public DataResponse webservicePreviewStandard(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("webserviceConId") Integer webserviceConId, @PathVariable("il_id") Integer il_id, @PathVariable("mappingId") Integer mappingId,
			@PathVariable("packageId") Integer packageId, Locale locale, HttpServletRequest request)
	{
		LOGGER.info("in webservicePreviewStandard()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		WebServiceApi webServiceApi = null;
		List<LinkedHashMap<String, Object>> finalformattedApiResponse = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		RestTemplate connectionRestTemplate = null;
		try
		{

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			if( webserviceConId != null && il_id != null )
			{
				webServiceApi = packageService.getIlConnectionWebServiceMapping(clientId, packageId, il_id, mappingId, clientAppDbJdbcTemplate);
			}

			webServiceApi.setWebServiceConnectionMaster(wSService.getWebServiceConnectionDetails(Long.valueOf(webserviceConId), clientIdfromHeader, clientAppDbJdbcTemplate));
			webServiceApi.setValidateOrPreview(true);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			String incrementalDate = packageService.getILIncrementalUpdateDate(null, il_id, webServiceApi.getId().toString(), Constants.SourceType.WEBSERVICE, mappingId, clientJdbcTemplate);
			webServiceApi.setInclUpdateDate(incrementalDate);

			if( webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().isSslDisable() )
			{
				connectionRestTemplate = WebServiceUtils.getRestTemplate();
			}
			else
			{
				connectionRestTemplate = new RestTemplate();
			}

			finalformattedApiResponse = WebServiceUtils.validateWebService(webServiceApi, connectionRestTemplate, clientJdbcInstance.getClientDbCredentials());

			if( finalformattedApiResponse != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(finalformattedApiResponse);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.apiProcessingFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			LOGGER.error("error while previwing ws api data", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			closeAPISession(webServiceApi.getWebServiceConnectionMaster(), connectionRestTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/editMappedHeadersForWsApi", method = RequestMethod.POST)
	public DataResponse editMappedHeadersForWebserviceApi(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnectionMapping iLConnectionMapping, Locale locale, HttpServletRequest request)
	{
		LOGGER.info("in editMappedHeadersForWebserviceApi()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<LinkedHashMap<String, Object>> finalformattedApiResponse = new ArrayList<>();
		WebServiceApi webServiceApi = null;
		String mappedHeadersForWebservice = null;
		String mappedJoinQuery = null;
		List<String> originalFileheaders = null;
		List<Column> iLcolumnNames = null;
		Map<String, Object> mappingFilesHeaders = new HashMap<>();
		int industryId = 0;
		String fileName = null;
		ILInfo iLInfo = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		RestTemplate connectionRestTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			iLInfo = packageService.getILById(iLConnectionMapping.getiLId(), clientId, clientAppDbJdbcTemplate);
			if( StringUtils.isNotBlank(iLInfo.getiL_table_name()) )
			{
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				iLcolumnNames = packageService.getTableStructure(null, iLInfo.getiL_table_name(), industryId, clientId, clientJdbcTemplate);
			}

			if( iLConnectionMapping.getConnectionMappingId() != null && iLConnectionMapping.getWsConId() != null )
			{
				mappedHeadersForWebservice = packageService.getMappedHeadersForWebservice(clientId, iLConnectionMapping.getiLId(), iLConnectionMapping.getConnectionMappingId(), iLConnectionMapping.getWsConId(), clientAppDbJdbcTemplate);
			}

			Map<String, String> mappedWebserviceHeaders = CommonUtils.mappedWebserviceHeaders(mappedHeadersForWebservice);

			File tempFile = null;
			if( !iLConnectionMapping.isJoinWebService() )
			{

				if( iLConnectionMapping.getWsConId() != null && iLConnectionMapping.getiLId() != null )
				{
					webServiceApi = packageService.getIlConnectionWebServiceMapping(clientId, iLConnectionMapping.getPackageId(), iLConnectionMapping.getiLId(), iLConnectionMapping.getConnectionMappingId(), clientAppDbJdbcTemplate);
				}
				webServiceApi.setWebServiceConnectionMaster(wSService.getWebServiceConnectionDetails(Long.valueOf(iLConnectionMapping.getWsConId()), clientIdfromHeader, clientAppDbJdbcTemplate));
				webServiceApi.setValidateOrPreview(true);

				if( webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().isSslDisable() )
				{
					connectionRestTemplate = WebServiceUtils.getRestTemplate();
				}
				else
				{
					connectionRestTemplate = new RestTemplate();
				}

				finalformattedApiResponse = WebServiceUtils.validateWebService(webServiceApi, connectionRestTemplate, clientJdbcInstance.getClientDbCredentials());

				String filePath = CommonUtils.getFilePathForWsApi(webServiceApi, finalformattedApiResponse, null);
				if( filePath != null )
				{
					tempFile = new File(filePath);
					originalFileheaders = fileService.getHeadersFromFile(tempFile.getAbsolutePath(), Constants.FileType.CSV, ",", null);
					fileName = tempFile.getName();
					if( originalFileheaders.size() > 0 )
					{
						if( originalFileheaders.size() == 1 && originalFileheaders.get(0).length() == 0 )
						{
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
							message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
							messages.add(message);
							dataResponse.setMessages(messages);
							if( tempFile != null )
							{
								tempFile.delete();
							}
							return dataResponse;
						}
					}
					else
					{
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
						if( tempFile != null )
						{
							tempFile.delete();
						}
						return dataResponse;
					}
				}
				closeAPISession(webServiceApi.getWebServiceConnectionMaster(), connectionRestTemplate);
			}
			else
			{
				if( iLConnectionMapping.getConnectionMappingId() != null && iLConnectionMapping.getWsConId() != null )
				{
					mappedJoinQuery = iLConnectionMapping.getiLquery();
				}
				fileName = iLInfo.getiL_name();

				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);

				List<String> columns = new ArrayList<>();
				clientJdbcTemplate.query(mappedJoinQuery, new ResultSetExtractor<Object>()
				{
					@Override
					public Object extractData(ResultSet rs) throws SQLException, DataAccessException
					{
						ResultSetMetaData rsmd = rs.getMetaData();
						int columnCount = rsmd.getColumnCount();
						for (int i = 1; i <= columnCount; i++)
						{
							columns.add(rsmd.getColumnLabel(i));
						}
						return columnCount;
					}
				});
				originalFileheaders = columns;
			}

			mappingFilesHeaders.put("originalFileheaders", originalFileheaders);
			mappingFilesHeaders.put("iLcolumnNames", iLcolumnNames);
			mappingFilesHeaders.put("originalFileName", fileName);
			mappingFilesHeaders.put("mappedWebserviceHeaders", mappedWebserviceHeaders);

			if( mappingFilesHeaders != null )
			{
				dataResponse.setObject(mappingFilesHeaders);
				message.setCode(messageSource.getMessage("anvizent.message.error.success", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			LOGGER.error("error while fetching ws api meta info", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getWebserviceTemplate", method = RequestMethod.GET)
	public DataResponse getwebServiceTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<WebServiceTemplateMaster> webServiceTempList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			webServiceTempList = wSService.getWebserviceTemplate(clientAppDbJdbcTemplate);
			if( webServiceTempList != null )
			{
				dataResponse.setObject(webServiceTempList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveWebServiceList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while webServiceTemplate() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhilegetWebServiceList", null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getWebServiceTemplateById", method = RequestMethod.POST)
	public DataResponse getWebServiceTemplateById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, @RequestBody WebServiceTemplateMaster webServiceTemplateMaster, Locale locale)
	{

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		WebServiceTemplateMaster webServiceTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			webServiceTemplate = wSService.getWebServiceTemplateById(webServiceTemplateMaster, clientAppDbJdbcTemplate);
			if( webServiceTemplate != null )
			{
				dataResponse.setObject(webServiceTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveWebServiceList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while webServiceTemplate() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhilegetWebServiceList", null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/updateWebserviceMasterConnectionMapping", method = RequestMethod.POST)
	public DataResponse updateWebserviceMasterConnectionMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebServiceConnectionMaster webServiceConnectionMaster, HttpServletRequest request, Locale locale)
	{
		LOGGER.info(" in updateWebserviceMasterConnectionMapping()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int count = -1;
		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			modification.setModifiedDateTime(new Date());
			webServiceConnectionMaster.setModification(modification);

			if( webServiceConnectionMaster.getDataSourceName().equals("-1") )
			{
				ClientDataSources clientDataSource = new ClientDataSources(Long.parseLong(clientIdfromHeader), webServiceConnectionMaster.getDataSourceNameOther());
				clientDataSource.setModification(modification);
				packageService.createDataSourceList(clientDataSource, clientAppDbJdbcTemplate);
				webServiceConnectionMaster.setDataSourceName(webServiceConnectionMaster.getDataSourceNameOther());
			}

			count = wSService.updateWebserviceMasterConnectionMapping(clientId, webServiceConnectionMaster, clientAppDbJdbcTemplate);
			if( count > 0 )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.webServiceMasterConnectionMappingUpdatedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.WebserviceMasterConnectionMappingNotUpdatedSuccesfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( AnvizentDuplicateFileNameException ae )
		{
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.validation.text.connectionNameAlreadyExist", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileUpdatingWebserviceMasterConnectionMapping", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getTimeZones", method = RequestMethod.GET)
	public DataResponse getTimeZones(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<TimeZones> timeZoneList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try
		{
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			timeZoneList = wSService.getTimeZones();
			if( timeZoneList != null )
			{
				dataResponse.setObject(timeZoneList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveWebServiceList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while timeZoneList() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhilegetWebServiceList", null, locale));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}
}
