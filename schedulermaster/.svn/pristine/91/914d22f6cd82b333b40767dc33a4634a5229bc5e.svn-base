package com.anvizent.scheduler.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.client.data.to.csv.path.converter.constants.Constants;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.scheduler.commonUtil.MasterSchedulerQueueUtil;
import com.anvizent.scheduler.master.MasterQuartzScheduler;
import com.anvizent.scheduler.service.MasterService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;

@RestController
@RequestMapping(value = "/masterserver")
public class MasterSchedulerRestController {

	final Logger LOGGER = LoggerFactory.getLogger(MasterSchedulerRestController.class);
	
	@Autowired
	MasterQuartzScheduler masterQuartzScheduler;

	@Autowired
	MasterSchedulerQueueUtil schedulerSlavesUtil;

	@Autowired
	MasterService masterService;

	@RequestMapping(value = "/getSchedulerStatus", method = RequestMethod.POST)
	public DataResponse createAuthenticationToken(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		dataResponse.setObject(masterQuartzScheduler.isSchedulerRunning());
		message.setCode(Constants.Config.SUCCESS);
		return dataResponse;
	}

	@RequestMapping(value = "/startScheduler", method = RequestMethod.POST)
	public DataResponse startScheduler(@RequestBody SchedulerMaster schedulerMaster, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (!masterQuartzScheduler.isSchedulerRunning()) {
				SchedulerMaster schedulerMasterInfo = masterService
						.getSchedulerServerMasterById(schedulerMaster.getId());
				if (schedulerMasterInfo != null) {
					message.setCode(Constants.Config.SUCCESS);
					masterQuartzScheduler.setSchedulerMaster(schedulerMasterInfo);
					masterQuartzScheduler.startScheduler();
					dataResponse.setObject(masterQuartzScheduler.isSchedulerRunning());
				} else {
					message.setCode(Constants.Config.ERROR);
					message.setCode(" Server master details not found ");
				}
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler already started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/stopScheduler", method = RequestMethod.POST)
	public DataResponse stopScheduler(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				message.setCode(Constants.Config.SUCCESS);
				masterQuartzScheduler.setSchedulerMaster(null);
				masterQuartzScheduler.stopScheduler();
				schedulerSlavesUtil.cleanData();
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/getSchedulerId", method = RequestMethod.POST)
	public DataResponse getSchedulerId(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				message.setCode(Constants.Config.SUCCESS);
				dataResponse.setObject(masterQuartzScheduler.getSchedulerId());
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/getUploadedQueue", method = RequestMethod.POST)
	public DataResponse getUploadedQueue(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				message.setCode(Constants.Config.SUCCESS);
				dataResponse.setObject(schedulerSlavesUtil.getUploadQueue());
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getExecutionQueue", method = RequestMethod.POST)
	public DataResponse getExecutionQueue(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				message.setCode(Constants.Config.SUCCESS);
				dataResponse.setObject(schedulerSlavesUtil.getExecutionQueue());
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;

	}

	@RequestMapping(value = "/getUploadAndExecutionQueue", method = RequestMethod.POST)
	public DataResponse getUploadAndExecutionQueue(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		
		Map<String, List<?>> dataResponseMap = new HashMap<>();
		List<PackageExecution> uploadQueue = new ArrayList<>();
		List<PackageExecution> executionQueue = new ArrayList<>();
		try {
			uploadQueue = schedulerSlavesUtil.getUploadQueue();
			executionQueue = schedulerSlavesUtil.getExecutionQueue();
			executionQueue = schedulerSlavesUtil.getExecutionQueue();
			
			dataResponseMap.put("packageUploadedQueue", uploadQueue);
			dataResponseMap.put("packageExecutionQueue", executionQueue);
			dataResponseMap.put("slavesList", schedulerSlavesUtil.getSchedulerSlaves());
			dataResponse.setObject(dataResponseMap);
			
			if (masterQuartzScheduler.isSchedulerRunning()) {
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}
			

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}
	
	@RequestMapping(value = "/pauseJobDetails", method = RequestMethod.POST)
	public DataResponse getPauseJobDetails(@RequestBody QuartzSchedulerJobInfo quartzSchedulerJobInfo,
			HttpServletRequest request) {
		LOGGER.info("in getPauseJobDetails()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		boolean status = false;
		try {
			status = masterQuartzScheduler.pauseScheduler(quartzSchedulerJobInfo.getJobKeyName(), quartzSchedulerJobInfo.getGroupName());
			dataResponse.setObject(status);
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/resumeJobDetails", method = RequestMethod.POST)
	public DataResponse getResumeJobDetails(@RequestBody QuartzSchedulerJobInfo quartzSchedulerJobInfo,
			HttpServletRequest request) {
		LOGGER.info("in getResumeJobDetails");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("SUCCESS");
		boolean status = false;
		try {
			status = masterQuartzScheduler.resumeScheduler(quartzSchedulerJobInfo.getJobKeyName(), quartzSchedulerJobInfo.getGroupName());
			dataResponse.setObject(status);
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/storeJobs", method = RequestMethod.POST)
	public DataResponse storeJobs(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				masterService.saveSchedulerSourceUploadQueue(masterQuartzScheduler.getSchedulerMaster(), schedulerSlavesUtil.getUploadQueue());
				message.setCode(Constants.Config.SUCCESS);
				
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}
	
	@RequestMapping(value = "/restoreJobs", method = RequestMethod.POST)
	public DataResponse restoreLastJobs(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				List<PackageExecution> jobsList = masterService.getStoredUploadQueueList(masterQuartzScheduler.getSchedulerMaster().getId());
				for (PackageExecution packageExecution : jobsList) {
					schedulerSlavesUtil.addToUploadQueue(packageExecution);
				}
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Restored successfully");
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}
	
	@RequestMapping(value = "/removeUploadQJobs", method = RequestMethod.POST)
	public DataResponse removeUploadQJobs(HttpServletRequest request,@RequestBody PackageExecution selectedPackage) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				List<PackageExecution> jobsList = schedulerSlavesUtil.getUploadQueue();
				for (PackageExecution pkg : jobsList) {
					if ( selectedPackage.getClientId().equals(pkg.getClientId()) && selectedPackage.getPackageId() == pkg.getPackageId() 
							&& selectedPackage.getUploadStartDate().equals(pkg.getUploadStartDate()) ) {
						jobsList.remove(pkg);
						break;
					}
				}
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Restored successfully");
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}
	
	@RequestMapping(value = "/refreshSlavesStatus", method = RequestMethod.POST)
	public DataResponse refreshSlavesStatus(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				schedulerSlavesUtil.slavesHeartBeatChecker();
				message.setCode(Constants.Config.SUCCESS);
				
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}
	

	@RequestMapping(value = "/pushUploadQueueToSlaves", method = RequestMethod.GET)
	public DataResponse pushUploadQueueToSlaves(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				schedulerSlavesUtil.checkUploadNodeAvailability();
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}
	
	@RequestMapping(value = "/pushExecuteQueueToSlaves", method = RequestMethod.GET)
	public DataResponse pushExecuteQueueToSlaves(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try {
			if (masterQuartzScheduler.isSchedulerRunning()) {
				schedulerSlavesUtil.checkExectionNodeAvailability();
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText(" Scheduler not yet started");
			}

		} catch (Throwable e) {
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}
	

}
