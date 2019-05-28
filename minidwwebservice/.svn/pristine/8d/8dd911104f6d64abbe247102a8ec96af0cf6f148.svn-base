package com.datamodel.anvizent.data.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.ExecutionProcessor;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.WSService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.OAuth2;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.service.model.WebServiceTemplateMaster;
import com.datamodel.anvizent.spring.AppProperties;
import minidwclientws.WebServiceUtils;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
@Import(AppProperties.class)
@RestController("webServiceTemplateAutomationDataRestController")
@RequestMapping("/wsTemplate")
@CrossOrigin

/**
 * @author mahender.alaveni
 * 
 *         Date:-18/12/2018
 * 
 *         Implemented Web Service Template Automation for creating web service
 *         connection
 * 
 */
public class WebServiceTemplateAutomationDataRestController
{

	protected static final Log LOGGER = LogFactory.getLog(WebServiceTemplateAutomationDataRestController.class);
	private @Value("${anvizent.corews.api.url:}") String authenticationEndPointUrl;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;

	@Autowired
	private PackageService packageService;

	@Autowired
	ExecutionProcessor executionProcessor;

	@Autowired
	private WSService wSService;

	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	private ETLAdminService etlAdminService;

	@RequestMapping(value = "/getUrlGeneration", method = RequestMethod.GET)
	public DataResponse getUrlGeneration(@RequestParam(value = "userId") String userId, @RequestParam(value = "clientId") String clientId, @RequestParam(value = "templateId") int templateId, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		WebServiceTemplateMaster webServiceTemplateMaster = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String exactOnlineURL = null;
		String scope = null;
		String state = null;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			webServiceTemplateMaster = wSService.getWebserviceTempleteDetails(Long.valueOf(templateId), clientId, clientAppDbJdbcTemplate);
			if( webServiceTemplateMaster != null )
			{
				if( StringUtils.isNotBlank(webServiceTemplateMaster.getoAuth2().getScope()) )
				{
					scope = webServiceTemplateMaster.getoAuth2().getScope();
				}
				else
				{
					scope = "";
				}
				if( StringUtils.isNotBlank(webServiceTemplateMaster.getoAuth2().getState()) )
				{
					state = webServiceTemplateMaster.getoAuth2().getState();
				}
				else
				{
					state = "";
				}

				StringJoiner userIdClientIdTemplateId = new StringJoiner(",");
				userIdClientIdTemplateId.add(userId);
				userIdClientIdTemplateId.add(clientId);
				userIdClientIdTemplateId.add(String.valueOf(templateId));

				exactOnlineURL = webServiceTemplateMaster.getAuthenticationUrl() + "?client_id=" + webServiceTemplateMaster.getoAuth2().getClientIdentifier() + "&redirect_uri=" + webServiceTemplateMaster.getoAuth2().getRedirectUrl() + "?userIdClientIdTemplateId=" + userIdClientIdTemplateId
						+ "&response_type=code" + "&scope=" + scope + "&state=" + state;

				dataResponse.setObject(exactOnlineURL);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveWebServiceList", null, null));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}
		catch ( Throwable t )
		{
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			LOGGER.error("error while getUrlGeneration() ", t);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhilegetWebServiceList", null, null));
			messages.add(message);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dataResponse;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/webServiceOAuth2Authenticationcallback", method = RequestMethod.GET, produces = { MediaType.TEXT_HTML_VALUE })
	public String webServiceOAuth2Authentication(@RequestParam(value = "userIdClientIdTemplateId") String userIdClientIdTemplateId, HttpServletRequest request)
	{
		JdbcTemplate clientAppDbJdbcTemplate = null;
		WebServiceConnectionMaster webServiceConnectionMaster = new WebServiceConnectionMaster();
		int count = 0;
		HttpStatus statusCode = null;
		ResponseEntity<Map> response = null;
		WebServiceTemplateMaster webServiceTemplateMaster = null;
		String userId = null;
		String clientId = null;
		String templateId = null;
		String errorOrSuccess = "";
		try
		{

			String splitUserIdClientIdTemplateId[] = userIdClientIdTemplateId.split(",");
			if( splitUserIdClientIdTemplateId.length == 3 )
			{
				userId = splitUserIdClientIdTemplateId[0];
				clientId = splitUserIdClientIdTemplateId[1];
				templateId = splitUserIdClientIdTemplateId[2];
			}
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			webServiceConnectionMaster.setModification(modification);

			OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
			String authcodeorizationCode = oar.getCode();

			webServiceTemplateMaster = wSService.getWebserviceTempleteDetails(Long.parseLong(templateId), clientId, clientAppDbJdbcTemplate);

			webServiceTemplateMaster.getoAuth2().setAuthCodeValue(authcodeorizationCode);

			String redirectUrl = webServiceTemplateMaster.getoAuth2().getRedirectUrl() + "?userIdClientIdTemplateId=" + userIdClientIdTemplateId;

			webServiceTemplateMaster.getoAuth2().setRedirectUrl(redirectUrl);

			webServiceConnectionMaster.setWebServiceTemplateMaster(webServiceTemplateMaster);

			RestTemplate authenticateRestTemplate = new RestTemplate();
			response = WebServiceUtils.testAuthenticationUrl(webServiceConnectionMaster, authenticateRestTemplate, null);
			if( response != null )
			{
				statusCode = response.getStatusCode();
			}

			if( statusCode == null )
			{
				errorOrSuccess += "<html><body ><span style='color:red'>" + messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, null) + " Please close the window.</span></body></html>";
			}
			else if( statusCode.equals(HttpStatus.OK) )
			{

				Map<String, Object> resposeObj = (Map<String, Object>) response.getBody();

				OAuth2 oAuth2 = new OAuth2();
				oAuth2.setAccessTokenValue(resposeObj.get("access_token").toString());
				oAuth2.setRefreshTokenValue(resposeObj.get("refresh_token").toString());
				oAuth2.setAccessTokenUrl(webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getAccessTokenUrl());
				oAuth2.setClientIdentifier(webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getClientIdentifier());
				oAuth2.setClientSecret(webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getClientSecret());
				oAuth2.setScope(webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getScope());
				oAuth2.setState(webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getState());

				webServiceConnectionMaster.setoAuth2(oAuth2);
				webServiceConnectionMaster.setWebServiceConName(webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceName() + "_" + RandomStringUtils.randomAlphanumeric(4) + MinidwServiceUtil.currentTime());
				webServiceConnectionMaster.setDataSourceName(webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceName() + "_" + RandomStringUtils.randomAlphanumeric(4) + MinidwServiceUtil.currentTime());
				webServiceConnectionMaster.setWsApiAuthUrl(webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationUrl());
				webServiceConnectionMaster.setBaseUrl(webServiceConnectionMaster.getWebServiceTemplateMaster().getBaseUrl());
				webServiceConnectionMaster.setRequestParams(webServiceConnectionMaster.getWebServiceTemplateMaster().getApiAuthRequestParams());
				webServiceConnectionMaster.setBodyParams(webServiceConnectionMaster.getWebServiceTemplateMaster().getApiAuthBodyParams());
				webServiceConnectionMaster.setAuthPathParams(webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationBodyParams());
				webServiceConnectionMaster.setHeaderKeyvalues(webServiceConnectionMaster.getWebServiceTemplateMaster().getApiAuthRequestHeaders());

				count = wSService.saveWebserviceMasterConnectionMapping(userId, webServiceConnectionMaster, clientAppDbJdbcTemplate);
				if( count > 0 )
				{
					errorOrSuccess += "<html><body><span style='color:green'>Sucessfully authorized. Please close the window.</span></body></html>";
				}
				else
				{
					errorOrSuccess += "<html><body onload='javascript:settimeout('self.close()',5000);'><span style='color:red'>Not authorized. Please close the window.</span> </body></html>";
				}

			}
			else if( statusCode.equals(HttpStatus.NO_CONTENT) )
			{
				errorOrSuccess += "<html><body><span style='color:red'>" + messageSource.getMessage("anvizent.message.validation.text.noContent", null, null) + " Please close the window.</span></body></html>";
			}
			else
			{
				errorOrSuccess += "<html><body><span style='color:red'>" + messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, null) + ". Status Code : " + statusCode + " Please close the window.</span> </body></html>";
			}

		}
		catch ( Throwable t )
		{
			errorOrSuccess += "<html><body><span style='color:red'>Not authorized.Please close the window.</span></body></html>";
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}

		return errorOrSuccess;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	@RequestMapping(value = "/webServiceBasicAuthentication", method = RequestMethod.GET)
	public DataResponse webServiceBasicAuthentication(@RequestParam(value = "userId") String userId, @RequestParam(value = "clientId") String clientId, @RequestParam(value = "templateId") String templateId, @RequestParam(value = "baseUrl") String baseUrl,
			@RequestParam(value = "userName") String userName, @RequestParam(value = "passWord") String passWord, @RequestParam(value = "methodType") String methodType, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		WebServiceConnectionMaster webServiceConnectionMaster = new WebServiceConnectionMaster();
		int count = 0;
		HttpStatus statusCode = null;
		ResponseEntity<Map> response = null;
		WebServiceTemplateMaster webServiceTemplateMaster = null;
		try
		{
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			webServiceConnectionMaster.setModification(modification);

			webServiceTemplateMaster = wSService.getWebserviceTempleteDetails(Long.parseLong(templateId), clientId, clientAppDbJdbcTemplate);
			webServiceConnectionMaster.setWebServiceTemplateMaster(webServiceTemplateMaster);

			JSONObject requestParams = new JSONObject();
			requestParams.put("Username", userName);
			requestParams.put("Password", passWord);

			webServiceConnectionMaster.setRequestParams(requestParams.toString());
			webServiceConnectionMaster.setBaseUrl(baseUrl);
			webServiceConnectionMaster.setBaseUrlRequired(true);

			RestTemplate authenticateRestTemplate = new RestTemplate();

			JSONObject authRequestParamJsonObj = new JSONObject(webServiceConnectionMaster.getRequestParams());
			Map<String, String> resp = new HashMap<>();
			if( authRequestParamJsonObj != null )
			{
				String authString = authRequestParamJsonObj.getString("Username") + ":" + authRequestParamJsonObj.getString("Password");
				String authStringEnc = new BASE64Encoder().encode(authString.getBytes());

				HttpHeaders headers = new HttpHeaders();
				headers.add("Authorization", "Basic " + authStringEnc);
				HttpEntity<String> headerParams = new HttpEntity<String>(headers);

				if( baseUrl != null )
				{
					URI uri = UriComponentsBuilder.fromUriString(baseUrl).build().encode().toUri();
					if( methodType.equalsIgnoreCase("post") )
					{
						try
						{
							response = authenticateRestTemplate.exchange(uri, HttpMethod.POST, headerParams, Map.class);
						}
						catch ( Exception exception )
						{
							response = new ResponseEntity<Map>(null, HttpStatus.BAD_REQUEST);
						}
					}
					else if( methodType.equalsIgnoreCase("get") )
					{
						try
						{
							response = authenticateRestTemplate.exchange(uri, HttpMethod.GET, headerParams, Map.class);
						}
						catch ( Exception exception )
						{
							response = new ResponseEntity<Map>(null, HttpStatus.BAD_REQUEST);
						}
					}
				}
				else
				{
					resp.put("Authorization", "Basic " + authStringEnc);
					response = new ResponseEntity<Map>(resp, HttpStatus.OK);
				}
			}
			else
			{
				response = new ResponseEntity<Map>(resp, HttpStatus.BAD_REQUEST);
			}

			if( response != null )
			{
				statusCode = response.getStatusCode();
			}

			if( statusCode == null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, null));
			}
			else if( statusCode.equals(HttpStatus.OK) )
			{

				OAuth2 oAuth2 = new OAuth2();
				oAuth2.setAccessTokenValue(null);
				oAuth2.setRefreshTokenValue(null);
				oAuth2.setAccessTokenUrl(null);
				oAuth2.setClientIdentifier(null);
				oAuth2.setClientSecret(null);
				oAuth2.setScope(null);
				oAuth2.setState(null);

				webServiceConnectionMaster.setoAuth2(oAuth2);
				webServiceConnectionMaster.setWebServiceConName(webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceName() + "_" + RandomStringUtils.randomAlphanumeric(4) + MinidwServiceUtil.currentTime());
				webServiceConnectionMaster.setDataSourceName(webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceName() + "_" + RandomStringUtils.randomAlphanumeric(4) + MinidwServiceUtil.currentTime());
				webServiceConnectionMaster.setWsApiAuthUrl(null);
				webServiceConnectionMaster.setBodyParams(webServiceConnectionMaster.getWebServiceTemplateMaster().getApiAuthBodyParams());
				webServiceConnectionMaster.setAuthPathParams(webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationBodyParams());
				webServiceConnectionMaster.setHeaderKeyvalues("{}");

				count = wSService.saveWebserviceMasterConnectionMapping(userId, webServiceConnectionMaster, clientAppDbJdbcTemplate);
				if( count > 0 )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText("Successfully authorized.");
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText("Not authorized.");
				}

			}
			else if( statusCode.equals(HttpStatus.NO_CONTENT) )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.validation.text.noContent", null, null));
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, null));
			}

		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText("Not authorized.");
		}
		finally
		{
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getEncryptedText", method = RequestMethod.GET)
	public DataResponse getEncryptText(@RequestParam("textToEncrypt") String textToEncrypt, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		try
		{
			if( textToEncrypt != null )
			{
				String encryptedText = EncryptionServiceImpl.getInstance().encrypt(textToEncrypt);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(encryptedText);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getDecryptedText", method = RequestMethod.GET)
	public DataResponse getUserListFromClient(@RequestParam("textToDecrypt") String textToDecrypt, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		try
		{
			if( textToDecrypt != null )
			{
				String decryptText = EncryptionServiceImpl.getInstance().decrypt(textToDecrypt);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(decryptText);
				dataResponse.setMessages(messages);
			}
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/encryptWsCredentialsForClients", method = RequestMethod.GET)
	public DataResponse encryptWsCredentialsForClients(@RequestParam("clientIds") String clientIds, @RequestParam("userId") String userId, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		try
		{
			String[] clientsArray = clientIds.split(",");
			for (String clientId : clientsArray)
			{
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
				if( clientJdbcInstance != null )
				{
					JdbcTemplate clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
					if( clientAppDbJdbcTemplate != null )
					{
						List<WebServiceConnectionMaster> webServiceConnectionMasterList = wSService.getWebServiceConnections(clientId, userId, clientAppDbJdbcTemplate);
						if( webServiceConnectionMasterList != null && webServiceConnectionMasterList.size() > 0 )
						{
							for (WebServiceConnectionMaster webServiceConnectionMaster : webServiceConnectionMasterList)
							{
								long id = webServiceConnectionMaster.getId();
								String encryptRequestParams = EncryptionServiceImpl.getInstance().encrypt(webServiceConnectionMaster.getRequestParams());
								String encryptBodyParams = EncryptionServiceImpl.getInstance().encrypt(webServiceConnectionMaster.getBodyParams());
								String encryptAuthPathParams = EncryptionServiceImpl.getInstance().encrypt(webServiceConnectionMaster.getAuthPathParams());

								StringJoiner columnInfoStringJoiner = new StringJoiner(",");
								columnInfoStringJoiner.add("`auth_request_params`=" + "'" + encryptRequestParams + "'");
								columnInfoStringJoiner.add("`auth_body_params`=" + "'" + encryptBodyParams + "'");
								columnInfoStringJoiner.add("`auth_path_params`=" + "'" + encryptAuthPathParams + "'");

								String query = "UPDATE `minidwcs_ws_connections_mst` SET " + columnInfoStringJoiner.toString() + " where id=" + id + ";";

								etlAdminService.encryptWebServiceAuthParams(clientAppDbJdbcTemplate, query);
							}
						}
					}
				}
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Throwable t )
		{
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

}
