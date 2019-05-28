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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.client.data.to.csv.path.converter.constants.Constants;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.schedulers.service.ServerSlaveService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.SchedulerSlave;


@RestController("user_schedulerServerSlaveDataRestController")
@RequestMapping("/services/common")
@CrossOrigin
public class SchedulerServerSlaveDataRestController {
	protected static final Log LOGGER = LogFactory.getLog(SchedulerServerSlaveDataRestController.class);

	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ServerSlaveService serverSlaveService;

	@Autowired
	private MasterSlaveConnector masterSlaveConnector;

	@RequestMapping(value = "/addServerSlaveInfo", method = RequestMethod.POST)
	public DataResponse addServerSlaveInfo(@RequestBody SchedulerSlave schedulerServerSlave, 
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in addServerSlaveInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int save= 0;
		try {
			save = serverSlaveService.addServerSlaveInfo(schedulerServerSlave);
			if (save != 0) {
				dataResponse.setObject(save);
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Saved Successfully");
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText("Server info not saved");
			}
		} catch (Exception e) {
			LOGGER.error("error while addServerSlaveInfo() ", e);
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/updateServerSlaveInfo", method = RequestMethod.POST)
	public DataResponse updateServerSlaveInfo(@RequestBody SchedulerSlave schedulerServerSlave, 
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in updateServerMasterInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int update= 0;
		try {
			update = serverSlaveService.updateServerSlaveInfo(schedulerServerSlave);
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
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getServerSlaveInfo", method = RequestMethod.GET)
	public DataResponse getServerSlaveList(HttpServletRequest request, Locale locale) {
		LOGGER.debug("in getServerSlaveInfo");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<SchedulerSlave> serverList= null;
		try {
			serverList = serverSlaveService.getServerSlaveInfo();
			
			
			
			if (serverList != null) {
				dataResponse.setObject(serverList);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}
			
		} catch (Exception e) {
			LOGGER.error("error while getServerSlaveInfo ", e);
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getServerSlaveById", method = RequestMethod.POST)
	public DataResponse getServerSlaveById(HttpServletRequest request,@RequestBody SchedulerSlave schedulerSlave,Locale locale) {
		LOGGER.debug("in getServerSlaveById()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		SchedulerSlave slaveInfo= null;
		try {
			slaveInfo = serverSlaveService.getServerSlaveById(schedulerSlave.getId());
			if (slaveInfo != null) {
				dataResponse.setObject(slaveInfo);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}
			
		} catch (Exception e) {
			LOGGER.error("error while getServerSlaveById() ", e);
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/startSlave", method = RequestMethod.POST)
	public DataResponse startSlave(HttpServletRequest request,@RequestBody SchedulerSlave schedulerSlave,Locale locale) {
		LOGGER.debug("in startSlave()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		SchedulerSlave slaveInfo= null;
		try {
			
			slaveInfo = serverSlaveService.getServerSlaveById(schedulerSlave.getId());
			int statusCode = masterSlaveConnector.startSlave(slaveInfo.getRequestSchema(), slaveInfo);
			
			if ( statusCode == 1 ) {
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Slave started");
				dataResponse.setObject(slaveInfo);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Slave initiation failed");
			}
			
		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	

	@RequestMapping(value = "/stopSlave", method = RequestMethod.POST)
	public DataResponse stopSlave(HttpServletRequest request,@RequestBody SchedulerSlave schedulerSlave,Locale locale) {
		LOGGER.debug("in stopSlave()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		SchedulerSlave slaveInfo= null;
		try {
			
			slaveInfo = serverSlaveService.getServerSlaveById(schedulerSlave.getId());
			int statusCode = masterSlaveConnector.stopSlave(slaveInfo.getRequestSchema(), slaveInfo);
			
			if ( statusCode == 1 ) {
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Slave stopped");
				dataResponse.setObject(slaveInfo);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Slave initiation failed");
			}
			
		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/updateServerSlaveInfoBYIpAddressAndId", method = RequestMethod.POST)
	public DataResponse updateServerSlaveInfoBYIpAddressAndId(@RequestBody SchedulerSlave schedulerSlave, 
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in updateServerMasterInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int update= 0;
		try {
			update = serverSlaveService.updateServerSlaveInfoBYIpAddressAndId(schedulerSlave);
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
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	
}