package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.ETLAdminService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.AppDBVersionTableScripts;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.spring.AppProperties;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Import(AppProperties.class)
@RestController("AppDbVersionTableScriptController")
@RequestMapping(Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/appDbVersionTableScripts")
@CrossOrigin
public class AppDbVersionTableScriptDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(AppDbVersionTableScriptDataRestController.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private ETLAdminService etlAdminService;
	
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	/**
	 * 
	 * @param clientId
	 * @return
	 */

	@RequestMapping(value = "/getAppDbVersionTableScripts", method = RequestMethod.GET)
	public DataResponse getHybridClientsGrouping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<AppDBVersionTableScripts> appDbVersionList = null; 
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			appDbVersionList = etlAdminService.getAppDbVersionTableScripts(clientAppDbJdbcTemplate);
			if (appDbVersionList != null) {
				dataResponse.setObject(appDbVersionList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveversionUpgradeDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} /*catch (Exception ae) {
			packageService.logError(ae, request);
			LOGGER.error("error while getVersionUpgrade() ", ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveversionUpgradeDetails", null, locale));
			messages.add(message);
		}*/ catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveAppDBVersionTableScripts", method = RequestMethod.POST)
	public DataResponse saveAppDBVersionTableScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody AppDBVersionTableScripts appDBVersionTableScripts, HttpServletRequest request,
			Locale locale) {
		LOGGER.debug("in saveAppDBVersionTableScripts");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		
		try {
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			int save = 0;
			if (appDBVersionTableScripts.getDefaultScript()) {
				etlAdminService.updateDefaultStatus(clientAppDbJdbcTemplate);
			}
			if(appDBVersionTableScripts.getId() != 0){
				save = etlAdminService.updateAppDBVersionTableScriptsInfo(appDBVersionTableScripts,clientAppDbJdbcTemplate);
			}else{
				save = etlAdminService.createAppDBVersionTableScripts(appDBVersionTableScripts, clientAppDbJdbcTemplate);	
			}
			
			if (save != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToCreateVersionUpgrade", null, locale));
			}
		}catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	@RequestMapping(value = "/getAppDbVersionTableScriptDetailsById/{id}", method = RequestMethod.GET)
	public DataResponse getAppDbVersionTableScriptDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@PathVariable("id") int id, Locale locale,
			HttpServletRequest request) {

		LOGGER.info("in getAppDbVersionTableScriptDetailsById()");
		AppDBVersionTableScripts appDbVersionTableScript = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			appDbVersionTableScript = etlAdminService.getAppDbVersionTableScriptDetailsById(id,clientAppDbJdbcTemplate);
			if (appDbVersionTableScript != null) {
				dataResponse.setObject(appDbVersionTableScript);
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
	
	@RequestMapping(value = "/getAppDBScriptById/{id}", method = RequestMethod.GET)
	public DataResponse getIlQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") Integer id, Locale locale,
			HttpServletRequest request) {

		LOGGER.info("in getAppDBScriptById()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String appDbScript = etlAdminService.getAppDBScriptById(id, clientAppDbJdbcTemplate);
			if (appDbScript != null) {
				dataResponse.setObject(appDbScript);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToRetrieveILQuery", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} /*
			 * catch (Exception e) { packageService.logError(e, request);
			 * LOGGER.error("error while getIlQueryById() ", e);
			 * message.setCode(messageSource.getMessage(
			 * "anvizent.message.error.code", null, locale));
			 * message.setText(messageSource.getMessage(
			 * "anvizent.message.error.text.unableToRetrieveILQuery", null,
			 * locale)); messages.add(message);
			 * dataResponse.setMessages(messages); }
			 */ catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
	@RequestMapping(value = "/getMinidwDBScriptById/{id}", method = RequestMethod.GET)
	public DataResponse getMinidwDBScriptById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") Integer id, Locale locale,
			HttpServletRequest request) {

		LOGGER.info("in getMinidwDBScriptById()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String appDbScript = etlAdminService.getMinidwDBScriptById(id, clientAppDbJdbcTemplate);
			if (appDbScript != null) {
				dataResponse.setObject(appDbScript);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToRetrieveILQuery", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} /*
			 * catch (Exception e) { packageService.logError(e, request);
			 * LOGGER.error("error while getIlQueryById() ", e);
			 * message.setCode(messageSource.getMessage(
			 * "anvizent.message.error.code", null, locale));
			 * message.setText(messageSource.getMessage(
			 * "anvizent.message.error.text.unableToRetrieveILQuery", null,
			 * locale)); messages.add(message);
			 * dataResponse.setMessages(messages); }
			 */ catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}

		return dataResponse;
	}
	
}
