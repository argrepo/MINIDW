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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.QuartzService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.QuartzSchedulerTriggerInfo;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;
import com.datamodel.anvizent.spring.AppProperties;

@Import(AppProperties.class)
@RestController("user_quartzServiceDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/package/schedule")
@CrossOrigin
public class QuartzServiceDataRestController {
	protected static final Log LOGGER = LogFactory.getLog(QuartzServiceDataRestController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserDetailsService userService;

	@Autowired
	private QuartzService quartzService;

	@RequestMapping(value = "/startSchedulerInfo", method = RequestMethod.POST)
	public DataResponse startSchedulerInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody QuartzSchedulerInfo quartzSchedulerInfo, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		long saveScheduledInfo = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			saveScheduledInfo = quartzService.startSchedulerInfo(quartzSchedulerInfo, clientAppDbJdbcTemplate);
			if (saveScheduledInfo != 0) {
				dataResponse.setObject(saveScheduledInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while createScheduledMasterInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateSchedulerInfo", method = RequestMethod.POST)
	public DataResponse addSchedulerJobInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody QuartzSchedulerInfo quartzSchedulerInfo, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int updateScheduledInfo = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			updateScheduledInfo = quartzService.updateSchedulerInfo(quartzSchedulerInfo, clientAppDbJdbcTemplate);
			if (updateScheduledInfo != 0) {
				dataResponse.setObject(updateScheduledInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while updateSchedulerInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/addSchedulerJobInfo", method = RequestMethod.POST)
	public DataResponse addSchedulerJobInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody QuartzSchedulerJobInfo quartzSchedulerJobInfo, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		long saveJobInfo = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			saveJobInfo = quartzService.addSchedulerJobInfo(quartzSchedulerJobInfo, clientAppDbJdbcTemplate);
			if (saveJobInfo != 0) {
				dataResponse.setObject(saveJobInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while addSchedulerJobInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateSchedulerJobInfo", method = RequestMethod.POST)
	public DataResponse updateSchedulerJobInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody QuartzSchedulerJobInfo quartzSchedulerJobInfo, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int updateScheduledInfo = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			updateScheduledInfo = quartzService.updateSchedulerJobInfo(quartzSchedulerJobInfo, clientAppDbJdbcTemplate);
			if (updateScheduledInfo != 0) {
				dataResponse.setObject(updateScheduledInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while updateSchedulerJobInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/addSchedulerTriggerInfo", method = RequestMethod.POST)
	public DataResponse addSchedulerTriggerInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo, HttpServletRequest request,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		long savetriggerInfo = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			savetriggerInfo = quartzService.addSchedulerTriggerInfo(quartzSchedulerTriggerInfo,
					clientAppDbJdbcTemplate);
			if (savetriggerInfo != 0) {
				dataResponse.setObject(savetriggerInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while addSchedulerTriggerInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateSchedulerTriggerInfo", method = RequestMethod.POST)
	public DataResponse updateSchedulerTriggerInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody QuartzSchedulerTriggerInfo quartzSchedulerTriggerInfo, HttpServletRequest request,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int updateTriggerInfo = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			updateTriggerInfo = quartzService.updateSchedulerTriggerInfo(quartzSchedulerTriggerInfo,
					clientAppDbJdbcTemplate);
			if (updateTriggerInfo != 0) {
				dataResponse.setObject(updateTriggerInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while updateSchedulerTriggerInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getScheduledJobsInfo", method = RequestMethod.POST)
	public DataResponse getScheduledJobsInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody SchedulerFilterJobDetails schedulerFilterJobDetails, HttpServletRequest request,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<QuartzSchedulerJobInfo> scheduledJobInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			scheduledJobInfo = quartzService.getScheduledJobsInfo(schedulerFilterJobDetails, clientAppDbJdbcTemplate);
			if (scheduledJobInfo != null) {
				dataResponse.setObject(scheduledJobInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while updateSchedulerTriggerInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getScheduledJobsInfoById", method = RequestMethod.POST)
	public DataResponse getScheduledJobsInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody QuartzSchedulerJobInfo quartzSchedulerJobInfo, HttpServletRequest request,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<QuartzSchedulerJobInfo> scheduledJobInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			String timeZone = CommonUtils.getTimeZoneFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			scheduledJobInfo = quartzService.getScheduledJobsInfoById(quartzSchedulerJobInfo.getSchedulerId(), timeZone, clientAppDbJdbcTemplate);
			if (scheduledJobInfo != null) {
				dataResponse.setObject(scheduledJobInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while updateSchedulerTriggerInfo() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getTriggeredInfoByID", method = RequestMethod.POST)
	public DataResponse getTriggeredInfoByID(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody QuartzSchedulerJobInfo quartzSchedulerJobInfo, HttpServletRequest request,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<QuartzSchedulerJobInfo> scheduledTriggerJobInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			String timeZone = CommonUtils.getTimeZoneFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			scheduledTriggerJobInfo = quartzService.getTriggeredInfoByID(quartzSchedulerJobInfo.getJobId(), timeZone, clientAppDbJdbcTemplate);
			if (scheduledTriggerJobInfo != null) {
				dataResponse.setObject(scheduledTriggerJobInfo);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}
		} catch (Exception e) {
			LOGGER.error("error while getTriggeredInfoByID() ", e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

}