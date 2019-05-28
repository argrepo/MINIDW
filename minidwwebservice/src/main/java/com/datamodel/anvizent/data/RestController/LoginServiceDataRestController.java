/**
 * 
 */
package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.security.AESConverter;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.ClientConfigurationSettings;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.HybridClientsGrouping;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerClientConfig;
import com.datamodel.anvizent.service.model.User;

/**
 * @author rakesh.gajula
 *
 */
@RestController("user_loginServiceDataRestController")
@RequestMapping("/loginService")
@CrossOrigin
public class LoginServiceDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(LoginServiceDataRestController.class);
	
	private @Value("${anvizent.corews.api.url:}") String authenticationEndPointUrl;
	private @Value("${elt.url:}") String eltEndPointUrl;
	
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private ETLAdminService etlAdminService;
	@Autowired
	private PackageService packageService;
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	@Autowired
	RestTemplate restTemplate;
	

	@RequestMapping(value = "/latestMinidwVesrion", method = RequestMethod.GET)
	public DataResponse latestMinidwVesrion(Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message(); 
		Map<String, Object> map = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			
			map = userService.getLatestMinidwVersion(null);
			if (map != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(map.get("versionNumber"));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("Minidw latest minidw version is not available.", null, locale));
			}

		} catch (Throwable t) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("Error while getting latest minidw version", null, locale));
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	
	@RequestMapping(value = "/getClientDetails", method = RequestMethod.POST)
	public DataResponse getClientDetails(@RequestParam("clientAccessCode") String clientAccessCode,
			HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			dataResponse.addMessage(message);
			System.out.println("After clientAccessCode  -> " + clientAccessCode);
			String clientId = AESConverter.decrypt(clientAccessCode);

			CloudClient cloudClient = userService.getClientDetails(clientId);
			if (cloudClient != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(cloudClient);
			}

		} catch (Throwable t) {
			if ( t instanceof ArrayIndexOutOfBoundsException || t instanceof IllegalBlockSizeException) {
				message.setCode("ERROR");
				message.setText("Invalid client access code");
			}else {
				MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
			}
		}
		return dataResponse;
	}
	

	@RequestMapping(value = "/getConfigSettingsInfoByID/{client_id}", method = RequestMethod.POST)
	public DataResponse getConfigSettingsInfoByID(HttpServletRequest request,
			@PathVariable("client_id") int client_id, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		ClientConfigurationSettings settingsInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(client_id+"");
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			settingsInfo = etlAdminService.getConfigSettingsInfoByID(client_id, clientAppDbJdbcTemplate);
			if (settingsInfo != null) {
				dataResponse.setObject(settingsInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveClientConfigInfo", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} /*catch (Exception ae) {
			packageService.logError(ae, request);
			LOGGER.error("error while getSettingsInfoByID() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorwhileGettingSettingsInfo", null, locale));
			messages.add(message);
		}*/ catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveclientConfigSettings", method = RequestMethod.POST)
	public DataResponse saveclientConfigSettings(
			@RequestBody ClientConfigurationSettings clientConfigurationSettings, Locale locale, HttpServletRequest request) {
		LOGGER.debug("in saveclientConfigSettings");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientConfigurationSettings.getClientId());
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(request.getHeader("Browser-Detail"));
			clientConfigurationSettings.setModification(modification);
			int save = 0;
			if (clientConfigurationSettings.getId() != 0) {
				save = etlAdminService.updateclientConfigSettings(clientConfigurationSettings, clientAppDbJdbcTemplate);
			} else {
				save = etlAdminService.saveclientConfigSettings(clientConfigurationSettings, clientAppDbJdbcTemplate);

			}
			if (save != 0) {
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.configurationSettingsSavedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingSettings", null, locale));
			}

		} /*catch (Exception ae) {
			packageService.logError(ae, request);
			LOGGER.error("error while saveclientConfigSettings() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileSavingSettings", null, locale));
		}*/ catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	

	@RequestMapping(value = "/authetincateUserCredentials", method = RequestMethod.POST)
	public DataResponse authetincatUserCredentials(
			@RequestBody User user, Locale locale, HttpServletRequest request) {
		DataResponse dataResponseM = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			if (StringUtils.isNotBlank(user.getAccessKey())) {
				HybridClientsGrouping hybridClientsGrouping = userService.getHybridClientGroupingDetailsByAccessKey(user.getAccessKey());
				if ( hybridClientsGrouping != null) {
					if (!hybridClientsGrouping.isActive()) {
						throw new Exception( "Inactive Hybrid client group");
					}else if ( !hybridClientsGrouping.getClientId().contains(user.getClientId())) {
						throw new Exception( user.getClientId() + " does not belongs to mentioned hybrid group");
					}
				} else {
					throw new Exception( "hybrid group details not found");
				}
			}
			
			String encryptedClientId=AESConverter.encrypt(user.getClientId());
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("X-Auth-Client-Token", encryptedClientId);
			String endpoint =  authenticationEndPointUrl + "/authenticateUser/{username}/{password}/{clientID}";
			HttpEntity<Object> headerParamsPost = new HttpEntity<Object>(httpHeaders);
			ResponseEntity<?> authDataResponseEntity = restTemplate.exchange(endpoint, HttpMethod.POST, headerParamsPost, Object.class, user.getUserName(), user.getPassword(), user.getClientId());
			@SuppressWarnings("unchecked")
			Map<String, Object> authDataResponse =  (Map<String, Object>)authDataResponseEntity.getBody();
			if (authDataResponse != null) {
				String status = (String) authDataResponse.get("status");
				if (status.equalsIgnoreCase("success")) {
					String roleId = (String) authDataResponse.get("roleid");
					String userid = (String) authDataResponse.get("userId");
					String clientId = (String) authDataResponse.get("client_id");
					String roleName = (String) authDataResponse.get("rolename");
					Boolean isSandBox = false;// Boolean.parseBoolean((String) authDataResponse.get("isSandBox")); 
					
					CloudClient cloudClient = userService.getClientDetails(user.getClientId());
					
					if ( !(roleId.equals(Constants.Role.SUPERADMIN+"") || roleId.equals(Constants.Role.SUPERADMIN_ETLADMIN+"")|| roleId.equals("7") || roleId.equals("1")) ) {
						throw new Exception("Application doesn't support the " + roleName + " role ");
					}
					

					User userInfo = new User();
					if ( !(roleId.equals(Constants.Role.SUPERADMIN+"") || roleId.equals(Constants.Role.SUPERADMIN_ETLADMIN+"")) ) {
						if ( authDataResponse.get("service_cluster_url") == null ||  ((String)authDataResponse.get("service_cluster_url")).equals("CLUSTER_NOT_CONFIGURED") ) {
							throw new Exception(com.datamodel.anvizent.helper.minidw.Constants.Config.DEPLOYMENT_TYPE_MESSAGE_INVALID_CONTEXT_PATH);
						}
						ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
						FileSettings fileSettings = packageService.getFileSettingsInfo(clientId, clientJdbcInstance.getClientAppdbJdbcTemplate());
						userInfo.setFileSettings(fileSettings);
						if ( !cloudClient.getDeploymentType().equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
							S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientId, null);
							if ( s3BucketInfo == null ) {
								throw new Exception("S3 details not mapped to client " + user.getClientId());
							}
							userInfo.setS3BucketInfo(s3BucketInfo);
						}
        		    // userInfo.setAccessKey((String)authDataResponse.get("service_cluster_url"));
					}
					
					userInfo.setUserName(user.getUserName());
					userInfo.setUserId(userid);
					userInfo.setClientId(clientId);
					userInfo.setRoleId(Integer.parseInt(roleId));
					userInfo.setRoleName(roleName);
					userInfo.setIsTrailUser(false);
					userInfo.setCloudClient(cloudClient);
					userInfo.setIsSandBox(isSandBox);
					
					dataResponseM.setObject(userInfo);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				} else {
					String errMessage = (String) authDataResponse.get("message");
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(errMessage);
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.text.unableToAuthenticateUserCredentials", null, locale));
			}
			
		} catch (Throwable t) {
			t.printStackTrace();
			packageService.logError(t, request, null);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponseM.setMessages(messages);
		return dataResponseM;
	}
	
	@RequestMapping(value = "/getSchedulerClientConfigInfo", method = RequestMethod.GET)
	public DataResponse getSchedulerClientConfigInfo(Locale locale,
			HttpServletRequest request) {

		LOGGER.info("in getSchedulerClientConfigInfo()");
		SchedulerClientConfig schedulerClientConfig = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			
			schedulerClientConfig = userService.getSchedulerClientConfigInfo();
			if (schedulerClientConfig != null) {
				dataResponse.setObject(schedulerClientConfig);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.versionUpgradeDetailsNotFound", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}  catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	

	@RequestMapping(value = "/getHybridClientGroupingDetailsByAccessKey", method = RequestMethod.POST)
	public DataResponse getHybridClientGroupingDetailsByAccessKey(
			@RequestParam("accessKey") String accessKey
			, Locale locale,
			HttpServletRequest request) {

		//LOGGER.info("in getHybridClientGroupingDetailsByAccessKey()");
		HybridClientsGrouping hybridClientsGrouping = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			hybridClientsGrouping = userService.getHybridClientGroupingDetailsByAccessKey(accessKey);
			if (hybridClientsGrouping != null) {
				dataResponse.setObject(hybridClientsGrouping);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.hybridClientDetailsNotFoundWithProvidedAccessKey", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}  catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/getELTUrl", method = RequestMethod.GET)
	public DataResponse getELTUrl(Locale locale,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		dataResponse.setObject(eltEndPointUrl);
		return dataResponse;
	}

}
