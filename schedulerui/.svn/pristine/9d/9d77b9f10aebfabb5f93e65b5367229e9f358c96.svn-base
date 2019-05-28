/**
 * 
 */
package com.anvizent.schedulers.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.client.data.to.csv.path.converter.constants.Constants;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.schedulers.service.ServerMasterService;
import com.anvizent.schedulers.service.UserService;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.QuartzSchedulerInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;

@RestController("user_schedulerServerMasterDataRestController")
@RequestMapping("/services/common")
@CrossOrigin
public class SchedulerServerMasterDataRestController {
	protected static final Log LOGGER = LogFactory.getLog(SchedulerServerMasterDataRestController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ServerMasterService serverMasterService;

	@Autowired
	private UserService userService;

	@Autowired
	private MasterSlaveConnector masterSlaveConnector;

	@RequestMapping(value = "/addServerMasterInfo", method = RequestMethod.POST)
	public DataResponse addServerMasterInfo(@RequestBody SchedulerMaster schedulerServerMaster,
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in addServerMasterInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int save = 0;
		try {
			save = serverMasterService.addServerMasterInfo(schedulerServerMaster);
			if (save != 0) {
				dataResponse.setObject(save);
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Saved Successfully");
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText("Server info not saved");
			}
		} catch (Exception e) {
			LOGGER.error("error while addServerMasterInfo() ", e);
			message = MinidwServiceUtil.getErrorMessage(e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateServerMasterInfo", method = RequestMethod.POST)
	public DataResponse updateServerMasterInfo(@RequestBody SchedulerMaster schedulerServerMaster,
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in updateServerMasterInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int update = 0;
		try {
			update = serverMasterService.updateServerMasterInfo(schedulerServerMaster);
			if (update != 0) {
				dataResponse.setObject(update);
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Updated Successfully");
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Updation Failed");
			}
		} catch (Exception e) {
			LOGGER.error("error while updateServerMasterInfo() ", e);
			message = MinidwServiceUtil.getErrorMessage(e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getServerMasterInfo", method = RequestMethod.GET)
	public DataResponse getServerMasterInfo(HttpServletRequest request, Locale locale) {
		LOGGER.debug("in getServerMasterInfo");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<SchedulerMaster> masterList = null;
		try {
			masterList = serverMasterService.getServerMasterInfo();
			if (masterList != null) {
				dataResponse.setObject(masterList);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getServerMasterInfo ", e);
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getSchedulerServerMasterById", method = RequestMethod.POST)
	public DataResponse getSchedulerServerMasterById(HttpServletRequest request,
			@RequestBody SchedulerMaster schedulerServerMaster, Locale locale) {
		LOGGER.debug("in getSchedulerServerMasterById()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		SchedulerMaster serverInfo = null;
		try {
			serverInfo = serverMasterService.getSchedulerServerMasterById(schedulerServerMaster.getId());
			if (serverInfo != null) {
				dataResponse.setObject(serverInfo);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getSchedulerServerMasterById() ", e);
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getSchedulerState", method = RequestMethod.POST)
	public DataResponse getSchedulerState(HttpServletRequest request, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in getSchedulerState()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		SchedulerMaster stateInfo = null;
		try {
			stateInfo = serverMasterService.getSchedulerState(schedulerMaster.getId());
			if (stateInfo != null) {
				dataResponse.setObject(stateInfo);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getSchedulerState() ", e);
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getQuartzShedulerMasterInfo", method = RequestMethod.GET)
	public DataResponse getQuartzShedulerMasterInfo(HttpServletRequest request, Locale locale) {
		LOGGER.debug("in getQuartzShedulerMasterInfo");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<QuartzSchedulerInfo> masterList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			masterList = serverMasterService.getQuartzShedulerMasterInfo(clientAppDbJdbcTemplate);
			if (masterList != null) {
				dataResponse.setObject(masterList);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getQuartzShedulerMasterInfo ", e);
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/startMaster", method = RequestMethod.POST)
	public DataResponse startSlave(HttpServletRequest request, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in startMaster()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		SchedulerMaster masterInfo = null;
		try {

			masterInfo = serverMasterService.getSchedulerServerMasterById(schedulerMaster.getId());
			int statusCode = masterSlaveConnector.startMaster(masterInfo.getRequestSchema(), masterInfo);

			if (statusCode == 1) {
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Master started");
				dataResponse.setObject(masterInfo);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Master initiation failed");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/stopMaster", method = RequestMethod.POST)
	public DataResponse stopSlave(HttpServletRequest request, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in stopMaster()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		SchedulerMaster masterInfo = null;
		try {

			masterInfo = serverMasterService.getSchedulerServerMasterById(schedulerMaster.getId());
			int statusCode = masterSlaveConnector.stopMaster(masterInfo.getRequestSchema(), masterInfo);

			if (statusCode == 1) {
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Master stopped");
				dataResponse.setObject(masterInfo);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Master initiation failed");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateServerMasterInfoByIpAddressAndId", method = RequestMethod.POST)
	public DataResponse updateServerMasterInfoByIpAddressAndId(@RequestBody SchedulerMaster schedulerMaster,
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in updateServerMasterInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int update = 0;
		try {
			update = serverMasterService.updateServerMasterInfoByIpAddressAndId(schedulerMaster);
			if (update != 0) {
				dataResponse.setObject(update);
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Updated Successfully");
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Updation Failed");
			}
		} catch (Exception e) {
			LOGGER.error("error while updateServerMasterInfo() ", e);
			message = MinidwServiceUtil.getErrorMessage(e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

}