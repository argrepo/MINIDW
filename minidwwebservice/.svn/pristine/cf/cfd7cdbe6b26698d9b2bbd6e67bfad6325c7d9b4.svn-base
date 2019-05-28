package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.ApisDataInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.spring.AppProperties;

@Import(AppProperties.class)
@RestController("apisDataRestController")
@RequestMapping(Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/apisData")
@CrossOrigin
public class ApisDataRestController {
	protected static final Log LOGGER = LogFactory.getLog(ApisDataRestController.class);
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PackageService packageService;
	
	@RequestMapping(value = "/saveApisDataInfo", method = RequestMethod.POST)
	public DataResponse saveApisDataInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ApisDataInfo apisDataInfo, HttpServletRequest request,
			Locale locale) {
		LOGGER.debug("in saveApisDataInfo");
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
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			apisDataInfo.setModification(modification);
			apisDataInfo.setUserId(Integer.parseInt(clientId));

			int save = 0;
			save = packageService.saveApisDataInfo(apisDataInfo, clientAppDbJdbcTemplate);	
			if (save != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		}catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/getApistDetailsById/{id}", method = RequestMethod.GET)
	public DataResponse getApistDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@PathVariable("id") int id, Locale locale,
			HttpServletRequest request) {

		LOGGER.info("in getApistDetailsById()");
		ApisDataInfo apisDataInfo = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			apisDataInfo = packageService.getApistDetailsById(id,clientAppDbJdbcTemplate);
			if (apisDataInfo != null) {
				dataResponse.setObject(apisDataInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);

			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}  catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/getApisDetails", method = RequestMethod.GET)
	public DataResponse getApisDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<ApisDataInfo> apisInfo = null; 
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			apisInfo = packageService.getApisDetails(clientAppDbJdbcTemplate);
			if (apisInfo != null) {
				for(ApisDataInfo apisDataInfo : apisInfo){
					String url = StringUtils.substring(request.getRequestURL().toString(), 0, StringUtils.indexOf(request.getRequestURL().toString(), '/', 10)) + request.getContextPath()+"/dataapi/v1/" + EncryptionServiceImpl.getInstance().encrypt(apisDataInfo.getEndPointUrl());
					apisDataInfo.setTestLink(url);
				}
				dataResponse.setObject(apisInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToGetDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
}
