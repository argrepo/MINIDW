/**
 * 
 */
package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.spring.AppProperties;


@Import(AppProperties.class) 
@RestController("user_awsDataasaRestController")
@RequestMapping("/schedulerui")
@CrossOrigin
public class AwsDataRestController {/*
	protected static final Log LOGGER = LogFactory.getLog(AwsDataRestController.class);

	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AwsService awsService;

	@RequestMapping(value = "/addAwsCredentialsInfo", method = RequestMethod.POST)
	public DataResponse addAwsCredentialsInfo(@RequestBody AwsCredentials awsCredentials, 
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in addAwsCredentialsInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int save= 0;
		try {
			save = awsService.addAwsCredentialsInfo(awsCredentials);
			if (save != 0) {
				dataResponse.setObject(save);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while addAwsCredentialsInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/updateAwsCredentialsInfo", method = RequestMethod.POST)
	public DataResponse updateAwsCredentialsInfo(@RequestBody AwsCredentials awsCredentials, 
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in updateAwsCredentialsInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int update= 0;
		try {
			update = awsService.updateAwsCredentialsInfo(awsCredentials);
			if (update != 0) {
				dataResponse.setObject(update);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while updateAwsCredentialsInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAwsCredentialsList", method = RequestMethod.GET)
	public DataResponse getAwsCredentialsList(HttpServletRequest request, Locale locale) {
		LOGGER.debug("in updateAwsCredentialsInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<AwsCredentials> awsList= null;
		try {
			awsList = awsService.getAwsCredentialsList();
			if ( awsList != null ) {
				dataResponse.setObject(awsList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
			
		} catch (Exception e) {
			LOGGER.error("error while updateAwsCredentialsInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
*/}