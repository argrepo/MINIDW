/**
 * 
 */
package com.anvizent.schedulers.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.ec2.model.AmazonEC2Exception;
import com.anvizent.client.data.to.csv.path.converter.constants.Constants;
import com.anvizent.minidw.service.utils.AwsEC2Utilities;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.schedulers.service.CommonService;
import com.anvizent.schedulers.service.MasterService;
import com.anvizent.schedulers.service.ServerMasterService;
import com.anvizent.schedulers.service.ServerSlaveService;
import com.anvizent.schedulers.service.UserService;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.minidw.MinidwJobState;
import com.datamodel.anvizent.service.model.AwsCredentials;
import com.datamodel.anvizent.service.model.AwsRegions;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.InstanceInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.SchedulerFilterJobDetails;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.datamodel.anvizent.service.model.SchedulerType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController("user_awsDataasaRestController")
@RequestMapping("/services/common")
@CrossOrigin
public class CommonDataRestController {
	protected static final Log LOGGER = LogFactory.getLog(CommonDataRestController.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private UserService userService;

	@Autowired
	private MasterService masterService;

	/*
	 * @Autowired MasterQuartzScheduler masterQuartzScheduler;
	 */

	@Autowired
	private ServerMasterService serverMasterService;

	@Autowired
	private ServerSlaveService serverSlaveService;

	@Autowired
	private MasterSlaveConnector masterSlaveConnector;

	private AwsEC2Utilities awsEC2Utilities = new AwsEC2Utilities();

	@RequestMapping(value = "/addAwsCredentialsInfo", method = RequestMethod.POST)
	public DataResponse addAwsCredentialsInfo(@RequestBody AwsCredentials awsCredentials, HttpServletRequest request,
			Locale locale) {
		LOGGER.debug("in addAwsCredentialsInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int save = 0;
		try {
			save = commonService.addAwsCredentialsInfo(awsCredentials);
			if (save != 0) {
				dataResponse.setObject(save);
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Saved Successfully");
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Server info not saved");
			}
		} catch (Exception e) {
			LOGGER.error("error while addAwsCredentialsInfo() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/updateAwsCredentialsInfo", method = RequestMethod.POST)
	public DataResponse updateAwsCredentialsInfo(@RequestBody AwsCredentials awsCredentials, HttpServletRequest request,
			Locale locale) {
		LOGGER.debug("in updateAwsCredentialsInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		int update = 0;
		try {
			update = commonService.updateAwsCredentialsInfo(awsCredentials);
			if (update != 0) {
				dataResponse.setObject(update);
				message.setCode(Constants.Config.SUCCESS);
				message.setText("Updated Successfully");
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Updation Failed");
			}
		} catch (Exception e) {
			LOGGER.error("error while updateAwsCredentialsInfo() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
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
		List<AwsCredentials> awsList = null;
		try {
			awsList = commonService.getAwsCredentialsList();
			if (awsList != null) {
				dataResponse.setObject(awsList);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while updateAwsCredentialsInfo() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAwsCredentialsListById", method = RequestMethod.POST)
	public DataResponse getAwsCredentialsListById(HttpServletRequest request,
			@RequestBody AwsCredentials awsCredentials, Locale locale) {
		LOGGER.debug("in getAwsCredentialsListById()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		AwsCredentials awsList = null;
		try {
			awsList = commonService.getAwsCredentialsListById(awsCredentials.getId());
			if (awsList != null) {
				dataResponse.setObject(awsList);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getAwsCredentialsListById() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getSchedulerType", method = RequestMethod.GET)
	public DataResponse getSchedulerType(HttpServletRequest request, Locale locale) {
		LOGGER.debug("in getSchedulerType()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<SchedulerType> typeList = null;
		try {
			typeList = commonService.getSchedulerType();
			if (typeList != null) {
				dataResponse.setObject(typeList);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getSchedulerType() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/activeClients", method = RequestMethod.GET)
	public DataResponse getActiveClientsList(HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<CloudClient> activeClientsList = null;
		try {
			activeClientsList = userService.getActiveClientsList();
			dataResponse.setObject(activeClientsList);
			message.setCode(Constants.Config.SUCCESS);
		} catch (Exception e) {
			LOGGER.error("error while getActiveClientsList() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getAwsRegionsList", method = RequestMethod.GET)
	public DataResponse getAwsRegionsList(HttpServletRequest request, Locale locale) {
		LOGGER.debug("in getAwsRegionsList()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<AwsRegions> awsRegionsList = null;
		try {
			awsRegionsList = commonService.getAwsRegionsList();
			if (awsRegionsList != null) {
				dataResponse.setObject(awsRegionsList);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getAwsRegionsList() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/validateAwsInstance", method = RequestMethod.POST)
	public DataResponse validateAwsInstance(@RequestBody InstanceInfo instanceInfo, HttpServletRequest request,
			Locale locale) {
		LOGGER.debug("in validateAwsInstance()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			AwsCredentials awsCredential = commonService.getAwsCredentialsListById(instanceInfo.getAws().getId());
			InstanceInfo insatnceInfo = awsEC2Utilities.getInstanceDetails(awsCredential, instanceInfo.getInstanceId());
			dataResponse.setObject(insatnceInfo);
			message.setCode(Constants.Config.SUCCESS);

		} catch (Throwable e) {
			LOGGER.error("error while validateAwsInstance() " + e.getMessage());
			message = MinidwServiceUtil.getErrorMessage(e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/startAwsInstance", method = RequestMethod.POST)
	public DataResponse startAwsInstance(@RequestBody InstanceInfo instanceInfo, HttpServletRequest request,
			Locale locale) {
		LOGGER.debug("in startAwsInstance()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			AwsCredentials awsCredential = commonService.getAwsCredentialsListById(instanceInfo.getAws().getId());
			InstanceInfo insatnceInfo = awsEC2Utilities.getInstanceDetails(awsCredential, instanceInfo.getInstanceId());

			/*
			 * 
			 * code:0 ; name: pending code:16 ; name: running code:32 ; name:
			 * shutting-down code:48 ; name: terminated code:64 ; name: stopping
			 * code:80 ; name: stopped
			 * 
			 */
			if (insatnceInfo.getInstanceState().equals("running")) {
				throw new AmazonEC2Exception("Instance is already in running mode.");
			}
			dataResponse.setObject(awsEC2Utilities.startInstanceStatus(awsCredential, instanceInfo.getInstanceId()));

		} catch (Throwable e) {
			LOGGER.error("error while startAwsInstance() " + e.getMessage());
			message = MinidwServiceUtil.getErrorMessage(e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/stopAwsInstance", method = RequestMethod.POST)
	public DataResponse stopAwsInstance(@RequestBody InstanceInfo instanceInfo, HttpServletRequest request,
			Locale locale) {
		LOGGER.debug("in startAwsInstance()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			AwsCredentials awsCredential = commonService.getAwsCredentialsListById(instanceInfo.getAws().getId());
			InstanceInfo insatnceInfo = awsEC2Utilities.getInstanceDetails(awsCredential, instanceInfo.getInstanceId());

			/*
			 * 
			 * code:0 ; name: pending code:16 ; name: running code:32 ; name:
			 * shutting-down code:48 ; name: terminated code:64 ; name: stopping
			 * code:80 ; name: stopped
			 * 
			 */
			if (!insatnceInfo.getInstanceState().equals("running")) {
				dataResponse
						.setObject(awsEC2Utilities.startInstanceStatus(awsCredential, instanceInfo.getInstanceId()));
			} else {
				throw new AmazonEC2Exception("Instance is not in running mode.");
			}

		} catch (Throwable e) {
			LOGGER.error("error while stopAwsInstance() " + e.getMessage());
			message = MinidwServiceUtil.getErrorMessage(e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/validateIpAdddress", method = RequestMethod.POST)
	public DataResponse validateIpAdddress(HttpServletRequest request, Locale locale) {
		LOGGER.debug("in validateIpAdddress()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		boolean ipAdress = true;
		try {
			dataResponse.setObject(ipAdress);
			message.setCode(Constants.Config.SUCCESS);

		} catch (Throwable e) {
			LOGGER.error("error while validateIpAdddress() " + e.getMessage());
			message = MinidwServiceUtil.getErrorMessage(e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
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
		SchedulerMaster serverInfo = null;
		QuartzSchedulerJobInfo schedulerJobInfo = null;
		try {
			serverInfo = serverMasterService
					.getSchedulerServerMasterBySchedulerId(quartzSchedulerJobInfo.getSchedulerId());
			status = masterSlaveConnector.pauseMasterSchedulerJob(serverInfo.getRequestSchema(), serverInfo,
					quartzSchedulerJobInfo);
			if (status) {
				JdbcTemplate jdbcTemplate = null;
				String clientId = quartzSchedulerJobInfo.getClientId();
				if (StringUtils.isNotBlank(clientId) && !clientId.equals("1")) {
					ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
					jdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				}
				String timeZone = request.getHeader("x-time-zone");
				schedulerJobInfo = serverMasterService.getSchedulerJobInfoByJobId(quartzSchedulerJobInfo.getJobId(), timeZone,  jdbcTemplate);
				dataResponse.setObject(schedulerJobInfo);
				message.setCode(Constants.Config.SUCCESS);
			}
			// dataResponse.setObject(status);
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
		SchedulerMaster serverInfo = null;
		QuartzSchedulerJobInfo schedulerJobInfo = null;
		try {

			// dao CommonDao -- input scheduler ID
			serverInfo = serverMasterService
					.getSchedulerServerMasterBySchedulerId(quartzSchedulerJobInfo.getSchedulerId());
			status = masterSlaveConnector.resumeMasterSchedulerJob(serverInfo.getRequestSchema(), serverInfo,
					quartzSchedulerJobInfo);
			if (status) {
				JdbcTemplate jdbcTemplate = null;
				String clientId = quartzSchedulerJobInfo.getClientId();
				if (StringUtils.isNotBlank(clientId) && !clientId.equals("1")) {
					ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
					jdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				}
				String timeZone = request.getHeader("x-time-zone");
				schedulerJobInfo = serverMasterService.getSchedulerJobInfoByJobId(quartzSchedulerJobInfo.getJobId(), timeZone,jdbcTemplate);
				// schedulerJobDetails -- condition -- job id
				// set schedulerJobDetails to data response with success
				dataResponse.setObject(schedulerJobInfo);
				message.setCode(Constants.Config.SUCCESS);
			}
			// dataResponse.setObject(status);
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getScheduledJobsInfo", method = RequestMethod.POST)
	public DataResponse getScheduledJobsInfo(@RequestBody SchedulerFilterJobDetails schedulerFilterJobDetails,
			HttpServletRequest request, Locale locale) {
		LOGGER.debug("in addSchedulerJobInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<QuartzSchedulerJobInfo> scheduledJobInfo = null;
		try {
			String timeZone = request.getHeader("x-time-zone");
			List<Long> scheduleIds = new ArrayList<>();

			List<SchedulerMaster> masterList = serverMasterService.getServerMasterInfo();
			scheduledJobInfo = new ArrayList<>();
			for (SchedulerMaster master : masterList) {
				if ( !master.isActive()) {
					continue;
				}
				
				long data = masterSlaveConnector.getMasterSchedulerId(master.getRequestSchema(), master);
				if (data == -1) {
					master.setServerState(false);
				} else if (data == 0) {
					master.setServerState(true);
					master.setSchdRunningState(false);
				} else {
					schedulerFilterJobDetails.setTimeZone(timeZone);
					schedulerFilterJobDetails.setSchedulerId(Arrays.asList(data));
					schedulerFilterJobDetails.setSchedulerMaster(master);
					scheduledJobInfo.addAll(masterService.getScheduledJobsInfo(schedulerFilterJobDetails,null));
					for ( CloudClient client : master.getClientIds() ) {
						scheduledJobInfo.addAll(masterService.getScheduledJobsInfo(schedulerFilterJobDetails,client.getId()+""));
					}
					scheduleIds.add(data);
				}
			}
			dataResponse.setObject(scheduledJobInfo);
			message.setCode(Constants.Config.SUCCESS);

		} catch (Exception e) {
			LOGGER.error("error while updateSchedulerTriggerInfo() " + e.getMessage());
			message.setCode(Constants.Config.ERROR);
			message.setText("Unable to retrive details");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getSchedulerJobStates", method = RequestMethod.GET)
	public DataResponse getSchedulerJobStates(HttpServletRequest request, Locale locale) {
		LOGGER.debug("in getSchedulerJobStates");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		MinidwJobState[] jobStateList = null;
		try {
			jobStateList = MinidwJobState.values();
			if (jobStateList != null) {
				dataResponse.setObject(jobStateList);
				message.setCode(Constants.Config.SUCCESS);
			} else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getSchedulerType() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getSlaveInfo", method = RequestMethod.POST)
	public DataResponse getSlaveInfo(HttpServletRequest request, @RequestBody SchedulerSlave schedulerSlave,
			Locale locale) {
		LOGGER.debug("in getSlaveInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			long data = masterSlaveConnector.getSlaveStatus(schedulerSlave.getRequestSchema(), schedulerSlave);
			if (data == -1) {
				schedulerSlave.setServerState(false);
			} else if (data == 0) {
				schedulerSlave.setServerState(true);
				schedulerSlave.setSchdRunningState(false);
			} else {
				schedulerSlave.setServerState(true);
				schedulerSlave.setSchdRunningState(true);
			}
			dataResponse.setObject(schedulerSlave);
			message.setCode(Constants.Config.SUCCESS);

		} catch (Exception e) {
			LOGGER.error("error while getSlaveInfo() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getSlaveStatusInfo", method = RequestMethod.POST)
	public DataResponse getSlaveStatusInfo(HttpServletRequest request, @RequestBody SchedulerSlave schedulerSlave,
			Locale locale) {
		LOGGER.debug("in getSlaveStatusInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		AwsCredentials awsList = null;
		try {
			if(schedulerSlave.getAws().getId() != 0){
				awsList = commonService.getAwsCredentialsListById(schedulerSlave.getAws().getId());
				schedulerSlave.setAws(awsList);
			}
			long data = masterSlaveConnector.getSlaveStatus(schedulerSlave.getRequestSchema(), schedulerSlave);
			if (data == -1) {
				throw new Exception("Server not reachable");
			} else{
				dataResponse.setObject(true);
			}
			message.setCode(Constants.Config.SUCCESS);

		} catch (Throwable e) {
			LOGGER.error("error while getSlaveStatusInfo() "  + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getMasterInfo", method = RequestMethod.POST)
	public DataResponse getMasterInfo(HttpServletRequest request, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in getMasterInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			long data = masterSlaveConnector.getMasterSchedulerId(schedulerMaster.getRequestSchema(), schedulerMaster);
			if (data == -1) {
				schedulerMaster.setServerState(false);
			} else if (data == 0) {
				schedulerMaster.setServerState(true);
				schedulerMaster.setSchdRunningState(false);
			} else {
				schedulerMaster.setServerState(true);
				schedulerMaster.setSchdRunningState(true);
			}
			dataResponse.setObject(schedulerMaster);
			message.setCode(Constants.Config.SUCCESS);

		} catch (Exception e) {
			LOGGER.error("error while getMasterInfo() "  + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getMasterStatusInfo", method = RequestMethod.POST)
	public DataResponse getMasterStatusInfo(HttpServletRequest request
			, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in getMasterStatusInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		AwsCredentials awsList = null;
		try {
			if(schedulerMaster.getAws().getId() != 0){
				awsList = commonService.getAwsCredentialsListById(schedulerMaster.getAws().getId());
				schedulerMaster.setAws(awsList);
			}
			long data = masterSlaveConnector.getMasterSchedulerId(schedulerMaster.getRequestSchema(), schedulerMaster);
			if (data == -1) {
				throw new Exception("Server not reachable");
			} else {
				dataResponse.setObject(true);
			}
			message.setCode(Constants.Config.SUCCESS);

		} catch (Exception e) {
			LOGGER.error("error while getMasterInfo() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getPackageUploadAndRunnerInfo", method = RequestMethod.POST)
	public DataResponse getPackageUploadAndRunnerInfo(HttpServletRequest request
			,@RequestParam(value="requestType",required=false,defaultValue="running") String requestType,
			@RequestBody SchedulerSlave schedulerSlave, Locale locale) {
		LOGGER.debug("in getSlaveInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			String timeZone = request.getHeader("x-time-zone");
			schedulerSlave = serverSlaveService.getServerSlaveById(schedulerSlave.getId());
			Map<String, List<?>> dataResponseMap = masterSlaveConnector
					.getPackageUploadAndRunnerInfo(schedulerSlave.getRequestSchema(), schedulerSlave,requestType);
			if (dataResponseMap != null) {
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				if ( dataResponseMap.get("packageUploadInfoList") != null ) {
					
					List<?> l = dataResponseMap.get("packageUploadInfoList");
					List<PackageExecution> packageUploadingList = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					if ( packageUploadingList != null && packageUploadingList.size() > 0) {
						for (PackageExecution packageUploading : packageUploadingList) { 
							packageUploading.setUploadStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageUploading.getUploadStartDate(), timeZone));
							packageUploading.setLastUploadedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageUploading.getLastUploadedDate(), timeZone));
						}
					}
					dataResponseMap.put("packageUploadInfoList",packageUploadingList) ;
				}
					if ( dataResponseMap.get("packageRunnerInfoList") != null ) {
					
					List<?> l = dataResponseMap.get("packageRunnerInfoList");
					List<PackageExecution> packageRunnerList = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					if ( packageRunnerList != null && packageRunnerList.size() > 0) {
						for (PackageExecution packageRunner : packageRunnerList) { 
							packageRunner.getUserPackage().setScheduleStartTime(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageRunner.getUserPackage().getScheduleStartTime(), timeZone));
							packageRunner.setExecutionStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageRunner.getExecutionStartDate(), timeZone));
							packageRunner.setLastExecutedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageRunner.getLastExecutedDate(), timeZone));
						}
					}
					dataResponseMap.put("packageRunnerInfoList",packageRunnerList); 
					
				}
				
			}
			message.setCode(Constants.Config.SUCCESS);
			dataResponse.setObject(dataResponseMap);

		} catch (Exception e) {
			LOGGER.error("error while getSlaveInfo() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUploadQueueAndPackageExecution", method = RequestMethod.POST)
	public DataResponse getUploadQueueAndPackageExecution(HttpServletRequest request,
			@RequestBody SchedulerMaster schedulerMaster, Locale locale) {
		LOGGER.debug("in getSlaveInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			String timeZone = request.getHeader("x-time-zone");
			schedulerMaster = serverMasterService.getSchedulerServerMasterById(schedulerMaster.getId());
			Map<String, List<?>> dataResponseMap = masterSlaveConnector
					.getUploadQueueAndPackageExecution(schedulerMaster.getRequestSchema(), schedulerMaster);
			if ( dataResponseMap != null ) {
				
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				if ( dataResponseMap.get("packageUploadedQueue") != null) {
					List<?> l = (List<Map<String, Object>>) dataResponseMap.get("packageUploadedQueue");
					List<PackageExecution> packageUploadedQueue = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					if ( packageUploadedQueue != null && packageUploadedQueue.size() > 0) {
						for (PackageExecution packageQueue : packageUploadedQueue) { 
							packageQueue.setUploadStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageQueue.getUploadStartDate(), timeZone));
						}
					}
					dataResponseMap.put("packageUploadedQueue", packageUploadedQueue);
				}
				if ( dataResponseMap.get("packageExecutionQueue") != null) {
					List<?> l = (List<Map<String, Object>>) dataResponseMap.get("packageExecutionQueue");
					List<PackageExecution> packageExecutionQueue = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					if ( packageExecutionQueue != null && packageExecutionQueue.size() > 0) {
						for (PackageExecution packageQueue : packageExecutionQueue) { 
							packageQueue.setUploadStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageQueue.getUploadStartDate(), timeZone));
							packageQueue.setLastUploadedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageQueue.getLastUploadedDate(), timeZone));
							packageQueue.setExecutionStartDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(packageQueue.getExecutionStartDate(), timeZone));
						}
					}
					dataResponseMap.put("packageExecutionQueue", packageExecutionQueue);
				}
				if ( dataResponseMap.get("slavesList") != null) {
					List<?> l = (List<Map<String, Object>>) dataResponseMap.get("slavesList");
					List<SchedulerSlave> packageExecutionQueue = mapper.convertValue(l, new TypeReference<List<SchedulerSlave>>() {
					});
					if ( packageExecutionQueue != null && packageExecutionQueue.size() > 0) {
						for (SchedulerSlave slave : packageExecutionQueue) { 
							slave.setLastUpdatedDate(TimeZoneDateHelper.getConvertedDateStringByTimeZone(slave.getLastUpdatedDate(), timeZone));
						}
					}
					dataResponseMap.put("slavesList", packageExecutionQueue);
				}
				
				;
			}
			message.setCode(Constants.Config.SUCCESS);
			dataResponse.setObject(dataResponseMap);

		} catch (Exception e) {
			LOGGER.error("error while getMasterInfo() "  + e.getMessage());
			MinidwServiceUtil.getErrorMessage(message, Constants.Config.FAILED, e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/getJobDetailsByJobID", method = RequestMethod.POST)
	public DataResponse getJobDetailsByJobID(HttpServletRequest request,
			@RequestBody QuartzSchedulerJobInfo quartzSchedulerJobInfo, Locale locale) {
		LOGGER.debug("in getSlaveInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			String timeZone = request.getHeader("x-time-zone");
			List<QuartzSchedulerJobInfo> jobDetailsList = masterService.getTriggeredInfoByID(quartzSchedulerJobInfo.getJobId(), timeZone, quartzSchedulerJobInfo.getClientId());
			message.setCode(Constants.Config.SUCCESS);
			dataResponse.setObject(jobDetailsList);

		} catch (Exception e) {
			LOGGER.error("error while getJobDetailsByJobID() "  + e.getMessage());
			MinidwServiceUtil.getErrorMessage(message, Constants.Config.FAILED, e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	
	@RequestMapping(value = "/getScheduledPackagesList", method = RequestMethod.POST)
	public DataResponse getScheduledPackagesList(HttpServletRequest request,@RequestBody CloudClient cloudClient, Locale locale) {
		LOGGER.debug("in getScheduledPackagesList()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<Schedule> schedulePackageList = null;
		try {
			schedulePackageList = masterService.getPackagesForSchedulingByClientIdWithCronExpression(cloudClient.getId());
			if (schedulePackageList != null) {
				dataResponse.setObject(schedulePackageList);
				message.setCode(Constants.Config.SUCCESS);
			}else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getScheduledPackagesList() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	/*
	@RequestMapping(value = "/executeScheduledPackages", method = RequestMethod.POST)
	public DataResponse getScheduledPackagesList(HttpServletRequest request,@RequestBody ManualJobUpload manualJobUpload, Locale locale) {
		LOGGER.debug("in getScheduledPackagesList()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		ManualJobUpload manualJob = null;
		try {
			//schedulePackageList = masterService.getScheduledPackages();
			if (manualJob != null) {
				dataResponse.setObject(manualJob);
				message.setCode(Constants.Config.SUCCESS);
			}else {
				message.setCode(Constants.Config.ERROR);
				message.setText("Details not found");
			}

		} catch (Exception e) {
			LOGGER.error("error while getScheduledPackagesList() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText("Failed");
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}*/


	@RequestMapping(value = "/saveJobInfo", method = RequestMethod.POST)
	public DataResponse saveJobInfo(HttpServletRequest request
			, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in saveJobInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		AwsCredentials awsList = null;
		try {
			
			schedulerMaster = masterService.getSchedulerServerMasterById(schedulerMaster.getId());
			if(schedulerMaster.getAws().getId() != 0){
				awsList = commonService.getAwsCredentialsListById(schedulerMaster.getAws().getId());
				schedulerMaster.setAws(awsList);
			}
			masterSlaveConnector.saveJobInfo(schedulerMaster.getRequestSchema(), schedulerMaster);
			message.setCode(Constants.Config.SUCCESS);
			message.setText("Jobs saved successfully ");
		} catch (Exception e) {e.printStackTrace();
			LOGGER.error("error while getMasterInfo() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/restoreJobs", method = RequestMethod.POST)
	public DataResponse restoreJobs(HttpServletRequest request
			, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in restoreJobs()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		AwsCredentials awsList = null;
		try {
			
			schedulerMaster = masterService.getSchedulerServerMasterById(schedulerMaster.getId());
			if(schedulerMaster.getAws().getId() != 0){
				awsList = commonService.getAwsCredentialsListById(schedulerMaster.getAws().getId());
				schedulerMaster.setAws(awsList);
			}
			masterSlaveConnector.restoreJobs(schedulerMaster.getRequestSchema(), schedulerMaster);
			message.setCode(Constants.Config.SUCCESS);
			message.setText("Jobs resored successfully ");
		} catch (Exception e) {e.printStackTrace();
			LOGGER.error("error while restoreJobs() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}


	@RequestMapping(value = "/removeUploadQJobs", method = RequestMethod.POST)
	public DataResponse removeUploadQJobs(HttpServletRequest request
			, @RequestBody PackageExecution packageData,@RequestParam("masterId") String masterId,
			Locale locale) {
		LOGGER.debug("in removeUploadQJobs()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		AwsCredentials awsList = null;
		try {
			
			SchedulerMaster schedulerMaster = masterService.getSchedulerServerMasterById(Long.parseLong(masterId));
			if(schedulerMaster.getAws().getId() != 0){
				awsList = commonService.getAwsCredentialsListById(schedulerMaster.getAws().getId());
				schedulerMaster.setAws(awsList);
			}
			masterSlaveConnector.removeUploadQJobs(schedulerMaster.getRequestSchema(), schedulerMaster,packageData);
			message.setCode(Constants.Config.SUCCESS);
			message.setText("Job removed from queue");
		} catch (Exception e) {e.printStackTrace();
			LOGGER.error("error while removeUploadQJobs() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	

	@RequestMapping(value = "/refreshSlavesStatus", method = RequestMethod.POST)
	public DataResponse refreshSlavesStatus(HttpServletRequest request
			, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in refreshSlavesStatus()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		AwsCredentials awsList = null;
		try {
			
			schedulerMaster = masterService.getSchedulerServerMasterById(schedulerMaster.getId());
			if(schedulerMaster.getAws().getId() != 0){
				awsList = commonService.getAwsCredentialsListById(schedulerMaster.getAws().getId());
				schedulerMaster.setAws(awsList);
			}
			masterSlaveConnector.refreshSlavesStatus(schedulerMaster.getRequestSchema(), schedulerMaster);
			message.setCode(Constants.Config.SUCCESS);
			message.setText("Slaves status Refreshed");
		} catch (Exception e) {e.printStackTrace();
			LOGGER.error("error while restoreJobs() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/pushUploadQueueToSlaves", method = RequestMethod.POST)
	public DataResponse pushUploadQueueToSlaves(HttpServletRequest request
			, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in pushUploadQueueToSlaves()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		AwsCredentials awsList = null;
		try {
			
			schedulerMaster = masterService.getSchedulerServerMasterById(schedulerMaster.getId());
			if(schedulerMaster.getAws().getId() != 0){
				awsList = commonService.getAwsCredentialsListById(schedulerMaster.getAws().getId());
				schedulerMaster.setAws(awsList);
			}
			masterSlaveConnector.pushUploadQueueToSlaves(schedulerMaster.getRequestSchema(), schedulerMaster);
			message.setCode(Constants.Config.SUCCESS);
			message.setText("Refreshed");
		} catch (Exception e) {e.printStackTrace();
			LOGGER.error("error while pushUploadQueueToSlaves() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/pushExecuteQueueToSlaves", method = RequestMethod.POST)
	public DataResponse pushExecuteQueueToSlaves(HttpServletRequest request
			, @RequestBody SchedulerMaster schedulerMaster,
			Locale locale) {
		LOGGER.debug("in pushExecuteQueueToSlaves()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		AwsCredentials awsList = null;
		try {
			
			schedulerMaster = masterService.getSchedulerServerMasterById(schedulerMaster.getId());
			if(schedulerMaster.getAws().getId() != 0){
				awsList = commonService.getAwsCredentialsListById(schedulerMaster.getAws().getId());
				schedulerMaster.setAws(awsList);
			}
			masterSlaveConnector.pushExecuteQueueToSlaves(schedulerMaster.getRequestSchema(), schedulerMaster);
			message.setCode(Constants.Config.SUCCESS);
			message.setText("Refreshed");
		} catch (Exception e) {e.printStackTrace();
			LOGGER.error("error while pushExecuteQueueToSlaves() " + e.getMessage());
			message.setCode(Constants.Config.FAILED);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}


}