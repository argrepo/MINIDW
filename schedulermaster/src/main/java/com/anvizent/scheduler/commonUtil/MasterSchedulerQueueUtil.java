package com.anvizent.scheduler.commonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.anvizent.client.data.to.csv.path.converter.constants.Constants;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.RestTemplateUtilities;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.scheduler.service.MasterService;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MasterSchedulerQueueUtil {

	protected static final Log LOGGER = LogFactory.getLog(MasterSchedulerQueueUtil.class);
	private List<PackageExecution> uploadQueue = new ArrayList<>();
	private List<PackageExecution> executionQueue = new ArrayList<>();

	int uploadHead;
	int executionHead;

	boolean isUploadingQueueRunning = false;
	boolean isExecutionQueueRunning = false;

	private List<SchedulerSlave> schedulerSlaves;

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	MasterService masterService;
	
	RestTemplateUtilities restTemplateUtilities = new RestTemplateUtilities();

	public List<SchedulerSlave> getSchedulerSlaves() {
		return schedulerSlaves;
	}

	public void setSchedulerSlaves(List<SchedulerSlave> schedulerSlaves) {
		if (schedulerSlaves == null) {
			schedulerSlaves = new ArrayList<>();
		}
		this.schedulerSlaves = schedulerSlaves;
	}

	boolean executeQueue(List<PackageExecution> queueList, List<SchedulerSlave> schedulerSlaves, String jobType) {
		int slaveHead = 0;
		int slavesSize = schedulerSlaves.size();
		//LOGGER.info(jobType + " Q size " + queueList.size() + " slaves size " + slavesSize);
		if (queueList.size() == 0 || slavesSize == 0) {
			return false;
		}

		try {
			for (int x = 0; x < 10; x++) {
				if (queueList.size() > 0) {
					PackageExecution packageExecution = queueList.get(0);
					if (packageExecution == null) {
						LOGGER.info("queueList has null value");
						queueList.remove(0);
						continue;
					}

					boolean updateStatus = false;
					for (int i = slaveHead; i < slavesSize; i++) {

						if (schedulerSlaves.get(i).isAvailable()) {
							int slaveLimit = 0;
							if (com.datamodel.anvizent.helper.Constants.PackageQueueType.PACKAGE_EXECUTION_QUEUE
									.equalsIgnoreCase(jobType)) {
								slaveLimit = schedulerSlaves.get(i).getPackageExecutionCount();
							} else {
								slaveLimit = schedulerSlaves.get(i).getFileUploadCount();
							}

							LOGGER.info((i + 1) + " -- " + schedulerSlaves.get(i).getName() + " is available and "
									+ jobType + " limit is " + slaveLimit);
							if (slaveLimit == 0) {
								continue;
							}

							/*Checking the limit as per client config  to push jobs for upload / execute to slaves*/
							boolean status = checkClientConcurrentUploadAndExecutionLimit(packageExecution);
							if(!status) {
								int index = queueList.indexOf(packageExecution);
								queueList.remove(index);
								queueList.add(packageExecution);
								return false;
							}
							
							String url = getEndPointDetails(schedulerSlaves.get(i), jobType);

							DataResponse dataResponse = null;
							try {
								dataResponse = restTemplate.postForObject(url, packageExecution, DataResponse.class);
							} catch (Throwable e) {
								LOGGER.error("Error while connecting to slave " + e.getMessage());
							}
							if (dataResponse != null && dataResponse.getHasMessages()) {
								if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
									slaveHead = i;
									queueList.remove(0);
									updateStatus = true;
									LOGGER.info(x + " job success. Remaining packages: " + queueList.size());
									break;
								} else {
									LOGGER.warn(dataResponse.getMessages().get(0).getText());
								}
							} else {
								schedulerSlaves.get(i).setStateMsg("Slave not reachable");
								schedulerSlaves.get(i).setLastUpdatedDate(TimeZoneDateHelper.getFormattedDateString());
								schedulerSlaves.get(i).setAvailable(false);
							}
						} else {
							LOGGER.info( (i+1) + " -- "
									+ schedulerSlaves.get(i).getName() + " is not available. Reason: " + schedulerSlaves.get(i).getStateMsg());
						}

						if (slaveHead > 0 && i == slavesSize - 1) {
							slavesSize = slaveHead;
							i = -1;
							slaveHead = 0;
							LOGGER.info("Upload head resetted to 0");
						}
					}
					if (!updateStatus) {
						break;
					}

				} else {
					return false;
				}
			}
		} catch (Throwable e) {
			LOGGER.error("Error while pushing "+jobType+" to slaves", e);
		}
		return true;
	}

	private String getEndPointDetails(SchedulerSlave schedulerSlave, String jobType) {
		String url = null;
		switch (jobType) {
		case com.datamodel.anvizent.helper.Constants.PackageQueueType.PACKAGE_UPLOAD_QUEUE: {
			url = schedulerSlave.getRequestSchema() + "://"
					+ MinidwServiceUtil.getPackageUploadEndPoint(schedulerSlave.getIpAddress());
			break;
		}
		case com.datamodel.anvizent.helper.Constants.PackageQueueType.PACKAGE_EXECUTION_QUEUE: {
			url = schedulerSlave.getRequestSchema() + "://"
					+ MinidwServiceUtil.getPackageExecutionEndPoint(schedulerSlave.getIpAddress());
			break;
		}
		default:
			url = "";
		}
		return url;
	}

	public boolean checkUploadNodeAvailability() {
		return executeQueue(uploadQueue, getSchedulerSlaves(),
				com.datamodel.anvizent.helper.Constants.PackageQueueType.PACKAGE_UPLOAD_QUEUE);
	}

	public boolean checkExectionNodeAvailability() {
		return executeQueue(executionQueue, getSchedulerSlaves(),
				com.datamodel.anvizent.helper.Constants.PackageQueueType.PACKAGE_EXECUTION_QUEUE);
	}

	public boolean checkExecutionNodeAvailability(List<SchedulerSlave> schedulerSlavesList) {
		return false;
	}

	public boolean addToUploadQueue(PackageExecution packageExecution) {
		if (!uploadQueue.contains(packageExecution)) {
			uploadQueue.add(packageExecution);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkClientConcurrentUploadAndExecutionLimit(PackageExecution packageExecution) {
		try {
			Map<String, String> properties = masterService.getJobExecutionLimitByClient(packageExecution.getClientId());
			int limit = Integer.valueOf( properties.get("client.job.upload.execution.limit") );
			int clientJobUploadAndExecutionCount = 0;
			Map<String, List<PackageExecution>> uploadAndRunnerInfo = null;
			List<PackageExecution> packageUploadAndRunnerInfoList = new ArrayList<>();
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			for(SchedulerSlave slave : getSchedulerSlaves()) {
				uploadAndRunnerInfo = getJobUploadAndRunnerInfo(slave, "running");
				if(uploadAndRunnerInfo.get("packageUploadInfoList") != null) {
					List<?> l = uploadAndRunnerInfo.get("packageUploadInfoList");
					List<PackageExecution> packageUploadingList = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					packageUploadAndRunnerInfoList.addAll(packageUploadingList);
				}
				if(uploadAndRunnerInfo.get("packageRunnerInfoList") != null) {
					List<?> l = uploadAndRunnerInfo.get("packageRunnerInfoList");
					List<PackageExecution> packageRunnerList = mapper.convertValue(l, new TypeReference<List<PackageExecution>>() {
					});
					packageUploadAndRunnerInfoList.addAll(packageRunnerList);
				}
			}
			for(PackageExecution pExecution : packageUploadAndRunnerInfoList) {
				if(pExecution.getClientId().equals(packageExecution.getClientId())) {
					clientJobUploadAndExecutionCount++;
				}
			}
			if(clientJobUploadAndExecutionCount < limit) {
				return true;
			}
			else {
				return false;
			}
		}catch(Throwable e) {
			LOGGER.info("Error while getstatus about upload & runner info from all slaves", e);
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, List<PackageExecution>> getJobUploadAndRunnerInfo(SchedulerSlave slave, String requestType){
		Map<String, List<PackageExecution>> slaveRunningJobInfo = new HashMap<>(); 
		try {
			String url = slave.getRequestSchema() + "://" + MinidwServiceUtil.getPackageUploadInfoAndRunnerInfoEndPoint(slave.getIpAddress());
			DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, slave,requestType);
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
					return (Map<String, List<PackageExecution>>) dataResponse.getObject();
				} else {
					throw new SchedulerException(dataResponse.getMessages().get(0).getText());
				}
			} else {
				throw new SchedulerException("Slave not reachable");
			}
		}catch(Throwable e) {
			LOGGER.info("Error while fetching upload & runner info from slave : "+slave.getName(), e);
		}
		return slaveRunningJobInfo;
	}

	public void addToExecutionQueue(PackageExecution packageExecution) {
		if (!executionQueue.contains(packageExecution)) {
			if (packageExecution.getInitiatedFrom().equals(com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.RUN_WITH_SCHEDULER)) {
				int rowCount = 0;
				boolean isAdded = false;
				for (PackageExecution pkgExecution : executionQueue) {
					if ( pkgExecution.getInitiatedFrom().equals(com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.RUN_WITH_SCHEDULER) ) {
						rowCount++;
						continue;
					} else {
						try {
							executionQueue.add(rowCount, packageExecution);
							isAdded = true;
						} catch (Exception e) {
							LOGGER.error("unable to add package to execution Queue",e);
							executionQueue.add(packageExecution);
							isAdded = true;
						}
					}
					break;
				}
				if (!isAdded) {
					executionQueue.add(packageExecution);
				}
			} else {
				executionQueue.add(packageExecution);
			}
		}
	}

	public void cleanData() {
		this.uploadHead = 0;
		this.executionHead = 0;
		this.setSchedulerSlaves(null);
	}

	public List<PackageExecution> getUploadQueue() {
		return uploadQueue;
	}

	public void setUploadQueue(List<PackageExecution> uploadQueue) {
		this.uploadQueue = uploadQueue;
	}

	public List<PackageExecution> getExecutionQueue() {
		return executionQueue;
	}

	public void setExecutionQueue(List<PackageExecution> executionQueue) {
		this.executionQueue = executionQueue;
	}

	public void slavesHeartBeatChecker() {
		for (SchedulerSlave slave : schedulerSlaves) {
			slave.setLastUpdatedDate(TimeZoneDateHelper.getFormattedDateString());
			SchedulerSlave newSlaveInfo = masterService.getServerSlaveById(slave.getId());
			if (newSlaveInfo == null || !newSlaveInfo.isActive()) {
				slave.setAvailable(false);
				if (newSlaveInfo == null) {
					slave.setStateMsg("Slave details not found");
				} else {
					slave.setStateMsg("Inactive slave");
				}
				continue;
			}
			refreshSlaveInfo(slave, newSlaveInfo);
			String ipAddress = null;
			try {
				ipAddress = MinidwServiceUtil.getIpAddress(slave);
			} catch (Exception e) {
				slave.setAvailable(false);
				continue;
			}
			if (StringUtils.isBlank(ipAddress)) {
				slave.setAvailable(false);
				continue;
			}
			String url = slave.getRequestSchema() + "://" + MinidwServiceUtil.getSlaveStatusEndPoint(ipAddress);
			DataResponse dataResponse = null;
			try {
				dataResponse = restTemplate.postForObject(url, slave, DataResponse.class);
			} catch (Exception e) {
				LOGGER.error("Error while connecting to slave " + e.getMessage());
			}
			boolean slaveStatus = false;
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
					slaveStatus = Boolean.parseBoolean(dataResponse.getObject().toString());
					if (slaveStatus) {
						slave.setStateMsg(null);
					} else {
						slave.setStateMsg("Slave not started");
					}
				} else {
					slave.setStateMsg("Error Response from slave: " + dataResponse.getMessages().get(0).getText());
				}
			} else {
				slave.setStateMsg("Offline");
			}
			slave.setAvailable(slaveStatus);
			if (slave.getType() == 1) {
				if (slaveStatus) {
					slave.setIpAddress(ipAddress);
				} else {
					slave.setIpAddress("");
				}
			}
		}
	}

	public void refreshSlaveInfo(SchedulerSlave oldSlaveInfo, SchedulerSlave newSlaveInfo) {
		oldSlaveInfo.setName(newSlaveInfo.getName());
		oldSlaveInfo.setType(newSlaveInfo.getType());
		oldSlaveInfo.setAws(newSlaveInfo.getAws());
		oldSlaveInfo.setInstanceId(newSlaveInfo.getInstanceId());
		oldSlaveInfo.setIpAddress(newSlaveInfo.getIpAddress());
		oldSlaveInfo.setFileUploadCount(newSlaveInfo.getFileUploadCount());
		oldSlaveInfo.setPackageExecutionCount(newSlaveInfo.getPackageExecutionCount());
		oldSlaveInfo.setHistoryLoadCount(newSlaveInfo.getHistoryLoadCount());
		oldSlaveInfo.setHistoryExecutionCount(newSlaveInfo.getHistoryExecutionCount());
	}

	public static void main1(String[] args) {
		List<PackageExecution> packageExecutionList = new ArrayList<>();
		PackageExecution packageExecution = new PackageExecution();
		packageExecution.setClientId("123");
		packageExecution.setUserId("1234");
		packageExecution.setPackageId(1125);
		packageExecution.setExecutionId(12);
		packageExecutionList.add(packageExecution);

		packageExecution = new PackageExecution();
		packageExecution.setClientId("123");
		packageExecution.setUserId("1234");
		packageExecution.setPackageId(1125);
		packageExecution.setExecutionId(11);

		if (packageExecutionList.contains(packageExecution)) {
			LOGGER.info("yes");
		} else {
			LOGGER.info("no");
		}
	}

	public static void main(String[] args) {

	}

	void incrementNumber(Integer in) {
		in++;
	}

	public static void main2(String[] args) {
		MasterSchedulerQueueUtil masterSchedulerQueueUtil = new MasterSchedulerQueueUtil();
		List<SchedulerSlave> slavesList = new ArrayList<>();
		SchedulerSlave slave = null;
		for (int i = 1; i <= 10; i++) {
			slave = new SchedulerSlave();
			slave.setName("slv" + (i - 1));
			slave.setAvailable(false);
			// slave.setAvailable(masterSchedulerQueueUtil.getRandomBool());
			slavesList.add(slave);
			LOGGER.info("Slave " + i + ") " + slave.isAvailable());
		}
		slavesList.get(3).setAvailable(true);
		slavesList.get(8).setAvailable(true);
		List<PackageExecution> uploadList = new ArrayList<>();
		PackageExecution packageExecution = null;
		for (int i = 1; i <= 200; i++) {
			packageExecution = new PackageExecution();
			packageExecution.setPackageId(i);
			uploadList.add(packageExecution);
		}
		masterSchedulerQueueUtil.setSchedulerSlaves(slavesList);
		masterSchedulerQueueUtil.setUploadQueue(uploadList);

		for (int i = 1; i <= 150; i++) {
			LOGGER.info("============================================================================================");
			LOGGER.info("Iteration " + i);
			masterSchedulerQueueUtil.checkUploadNodeAvailability();
		}
	}

	public void appendData(StringBuilder detailedInfo, String... dataArray) {
		if (detailedInfo != null) {
			detailedInfo.append(com.datamodel.anvizent.helper.minidw.Constants.Config.NEW_LINE);
			for (String data : dataArray) {
				detailedInfo.append(data).append(" ");
			}
		}
	}

	boolean getRandomBool() {
		int number = getRandonNumber();
		return number % 2 == 0;
	}

	int getRandonNumber() {
		Random random = new Random();
		return random.nextInt();
	}

}
