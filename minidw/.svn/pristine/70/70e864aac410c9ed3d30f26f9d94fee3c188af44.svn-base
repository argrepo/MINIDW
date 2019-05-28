
/**
 * 
 */
package com.datamodel.anvizent.data.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.client.scheduler.constant.Constant;
import com.anvizent.client.scheduler.job.HybridClientAccessKeyAdder;
import com.anvizent.client.scheduler.listner.ClientScheduler;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;

/**
 * @author rajesh.anthari
 *
 */

@RestController("schedulerDataRestController")
@RequestMapping("" + Constants.AnvizentURL.MINIDW_BASE_URL + "/package/scheduler")
@CrossOrigin
public class SchedulerDataController {
	protected static final Log LOGGER = LogFactory.getLog(SchedulerDataController.class);
	@Autowired
	@Qualifier("scheduleServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("scheduleServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;

	@Autowired
	ClientScheduler clientScheduler;

	/**
	 * get the packages of the client which have all the ILs are mapped for
	 * atleast one DL
	 * 
	 * @param clientId
	 * @return
	 */

	@RequestMapping(value = "/schedulerStatus", method = RequestMethod.POST)
	public DataResponse schedulerStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		dataResponse.setObject(clientScheduler.isSchedulerRunning());
		return dataResponse;
	}

	@RequestMapping(value = "/stopscheduler", method = RequestMethod.POST)
	public DataResponse stopscheduler(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request,HttpSession session) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		boolean status = false;
		try {
			status = clientScheduler.stopScheduler();
			dataResponse.setObject(status);
			removeClientIdFrmSession(session);
		} catch (SchedulerException e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}

	@RequestMapping(value = "/startscheduler", method = RequestMethod.POST)
	public DataResponse startscheduler(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request,HttpSession session) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		boolean status = false;
		try {
			status = clientScheduler.startScheduler();
			dataResponse.setObject(status);
			removeClientIdFrmSession(session);
		} catch (SchedulerException e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}

	private void removeClientIdFrmSession(HttpSession session) {
		session.removeAttribute(Constant.General.CLIENT_ID);
		session.removeAttribute(Constant.General.PLAIN_CLIENT_ID);
	}

	private void setClientIdToSession(HttpSession session,String clientId) {
		try {
			session.setAttribute(Constant.General.CLIENT_ID,EncryptionServiceImpl.getInstance().encrypt(clientId));
			session.setAttribute(Constant.General.PLAIN_CLIENT_ID,clientId);
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		
	}
	
	@RequestMapping(value = "/getScheduledJobDetails", method = RequestMethod.POST)
	public DataResponse getScheduledJobDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@RequestBody SchedulerFilterJobDetails schedulerFilterJobDetails,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		try {
			dataResponse.setObject(clientScheduler.getFilterScheduledJobsWithFilter(schedulerFilterJobDetails));
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getPauseJobDetails", method = RequestMethod.POST)
	public DataResponse getPauseJobDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("jobName") String jobName, @RequestParam("groupName") String groupName,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		boolean status = false;
		try {
			status = clientScheduler.pauseScheduler(jobName, groupName);
			dataResponse.setObject(status);
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getResumeJobDetails", method = RequestMethod.POST)
	public DataResponse getResumeJobDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("jobName") String jobName, @RequestParam("groupName") String groupName,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		boolean status = false;
		try {
			status = clientScheduler.resumeScheduler(jobName, groupName);
			dataResponse.setObject(status);
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getFilterGroupNames", method = RequestMethod.POST)
	public DataResponse getFilterGroupNames(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		List<String> groupNames = null;
		try {
			groupNames = clientScheduler.getGroupNames();
			dataResponse.setObject(groupNames);
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}
	

	@RequestMapping(value = "/getGroupJobDetails", method = RequestMethod.POST)
	public DataResponse getGroupJobDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody SchedulerFilterJobDetails schedulerFilterJobDetails, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		try {
			dataResponse.setObject(clientScheduler.getFilterScheduledJobs(schedulerFilterJobDetails));
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/getClientIds", method = RequestMethod.POST)
	public DataResponse getClientIds(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		List<String> clientIds = null;
		try {
			clientIds = clientScheduler.getScheduledClientIds();
			dataResponse.setObject(clientIds);
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/getScheduledInfoJob", method = RequestMethod.POST)
	public DataResponse getScheduledJobDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		try {
			String cId = (String)SessionHelper.getSesionAttribute(request, Constant.General.CLIENT_ID);
			String webServiceUrl = (String)SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
			String timeZone = (String)SessionHelper.getSesionAttribute(request, Constants.Config.TIME_ZONE);
			if ( StringUtils.isNotBlank(cId) ) {
				dataResponse.setObject(clientScheduler.getScheduledInfoJob(cId,webServiceUrl,timeZone));
			}
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveClientId/{client_id}", method = RequestMethod.POST)
	public DataResponse saveClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("client_id") String clientId,
			HttpServletRequest request,HttpSession session) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		try {
			setClientIdToSession(session, clientId);
			dataResponse.setObject(true);
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/getTriggeredInfoByID/{job_id}", method = RequestMethod.POST)
	public DataResponse getTriggeredInfoByID(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("job_id") long job_id,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		try {
			String cId = (String)SessionHelper.getSesionAttribute(request, Constant.General.CLIENT_ID);
			String webServiceUrl = (String)SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
			String timeZone = (String)SessionHelper.getSesionAttribute(request, Constants.Config.TIME_ZONE);
			dataResponse.setObject(clientScheduler.getTriggeredInfoByID(cId,job_id,webServiceUrl,timeZone));
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/manualIntervention", method = RequestMethod.GET)
	public DataResponse manualIntervention(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		try {
			if ( HybridClientAccessKeyAdder.isManualIntervention ) {
				HybridClientAccessKeyAdder.isManualIntervention = false;
			} else {
				HybridClientAccessKeyAdder.isManualIntervention = true;
			}
			message.setText(" value " + HybridClientAccessKeyAdder.isManualIntervention );
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}
	
	
	


}
