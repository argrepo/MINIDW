package com.anvizent.schedulers.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;

import com.anvizent.client.data.to.csv.path.converter.constants.Constants;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.RestTemplateUtilities;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.QuartzSchedulerJobInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;

public class MasterSlaveConnector {
	RestTemplateUtilities restTemplateUtilities = new RestTemplateUtilities();

	public long getMasterSchedulerId(String protocal, SchedulerMaster master) {
		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return -1;
		}
		String url = protocal + "://" + MinidwServiceUtil.getMasterStatusEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, new HashMap<>());
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
				return Long.parseLong(dataResponse.getObject().toString());
			} else {
				return 0;
			}
		} else {
			return -1;
		}

	}

	public int getSlaveStatus(String protocal, SchedulerSlave slave) {
		String ipAddress = MinidwServiceUtil.getIpAddress(slave);
		if (StringUtils.isBlank(ipAddress)) {
			return -1;
		}
		int statusCode = 0;
		String url = protocal + "://" + MinidwServiceUtil.getSlaveStatusEndPoint(ipAddress);
		//System.out.println("url  -- > " + url);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, new HashMap<>());
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
				boolean status = Boolean.parseBoolean(dataResponse.getObject().toString());
				if (status) {
					statusCode = 1;
				}
			}
		} else {
			statusCode = -1;
		}

		return statusCode;
	}

	public int startSlave(String protocal, SchedulerSlave slave) throws SchedulerException {
		String ipAddress = MinidwServiceUtil.getIpAddress(slave);
		if (StringUtils.isBlank(ipAddress)) {
			return -1;
		}

		String url = protocal + "://" + MinidwServiceUtil.getStartSlaveEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, slave);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
				return 1;
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Slave not reachable");
		}

	}

	public int stopSlave(String protocal, SchedulerSlave slave) throws SchedulerException {
		String ipAddress = MinidwServiceUtil.getIpAddress(slave);
		if (StringUtils.isBlank(ipAddress)) {
			return -1;
		}

		String url = protocal + "://" + MinidwServiceUtil.getStopSlaveEndPoint(ipAddress);

		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, slave);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
				return 1;
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Slave not reachable");
		}

	}

	public int startMaster(String protocal, SchedulerMaster master) throws SchedulerException {
		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return -1;
		}
		String url = protocal + "://" + MinidwServiceUtil.getStartMasterEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, master);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
				return 1;
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Slave not reachable");
		}

	}

	public int stopMaster(String protocal, SchedulerMaster master) throws SchedulerException {
		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return -1;
		}

		String url = protocal + "://" + MinidwServiceUtil.getStopMasterEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, master);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
				return 1;
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Master not reachable");
		}

	}

	@SuppressWarnings("unchecked")
	public Map<String, List<?>> getUploadQueueAndPackageExecution(String protocal,
			SchedulerMaster master) throws SchedulerException {
		Map<String, List<?>> dataResponseMap = new HashMap<>();

		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return dataResponseMap;
		}

		String url = protocal + "://" + MinidwServiceUtil.getUploadQueueAndPackageExecutionEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, master);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			return  (Map<String, List<?>>) dataResponse.getObject();
		} else {
			throw new SchedulerException("Master not reachable");
		}

	}
	
	public boolean resumeMasterSchedulerJob(String protocal, SchedulerMaster master, QuartzSchedulerJobInfo quartzSchedulerJobInfo) throws SchedulerException {
		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return false;
		}
		String url = protocal + "://" + MinidwServiceUtil.getResumeMasterSchedulerJob(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, quartzSchedulerJobInfo);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
				return true;
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Master server not reachable");
		}

	}

	public boolean pauseMasterSchedulerJob(String protocal, SchedulerMaster master, QuartzSchedulerJobInfo quartzSchedulerJobInfo)throws SchedulerException {
		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return false;
		}
		String url = protocal + "://" + MinidwServiceUtil.getPauseMasterSchedulerJob(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, quartzSchedulerJobInfo);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
				return true;
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Master server not reachable");
		}
	}
	

	@SuppressWarnings("unchecked")
	public Map<String, List<?>> getPackageUploadAndRunnerInfo(String protocal, SchedulerSlave slave,String requestType)
			throws SchedulerException {
		Map<String, List<?>> dataResponseMap = new HashMap<>();

		String ipAddress = MinidwServiceUtil.getIpAddress(slave);
		if (StringUtils.isBlank(ipAddress)) {
			return dataResponseMap;
		}

		String url = protocal + "://" + MinidwServiceUtil.getPackageUploadInfoAndRunnerInfoEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, slave,requestType);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
				return (Map<String, List<?>>) dataResponse.getObject();
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Slave not reachable");
		}
	}
	
	public void saveJobInfo(String protocal, SchedulerMaster master)
			throws SchedulerException {

		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return;
		}
		
		String url = protocal + "://" + MinidwServiceUtil.getStoreJobsEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, master);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Master not reachable");
		}
	}
	
	public void restoreJobs(String protocal, SchedulerMaster master)
			throws SchedulerException {

		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return;
		}
		
		String url = protocal + "://" + MinidwServiceUtil.getReStoreJobsEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, master);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Master not reachable");
		}
	}
	
	public void removeUploadQJobs(String protocal, SchedulerMaster master,PackageExecution packageData)
			throws SchedulerException {

		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return;
		}
		
		String url = protocal + "://" + MinidwServiceUtil.getRemoveUploadQJobsEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, packageData);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Master not reachable");
		}
	}


	public void refreshSlavesStatus(String protocal, SchedulerMaster master)
			throws SchedulerException {

		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return;
		}
		
		String url = protocal + "://" + MinidwServiceUtil.getRefreshSlavesStatus(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.postRestObject(new CustomRequest(), url, master);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Master not reachable");
		}
	}
	

	public void pushUploadQueueToSlaves(String protocal, SchedulerMaster master)
			throws SchedulerException {

		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return;
		}
		
		String url = protocal + "://" + MinidwServiceUtil.getPushUploadQueueToSlavesEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.getRestObject(new CustomRequest(), url);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Master not reachable");
		}
	}
	
	public void pushExecuteQueueToSlaves(String protocal, SchedulerMaster master)
			throws SchedulerException {

		String ipAddress = MinidwServiceUtil.getIpAddress(master);
		if (StringUtils.isBlank(ipAddress)) {
			return;
		}
		
		String url = protocal + "://" + MinidwServiceUtil.getPushExecuteQueueToSlavesEndPoint(ipAddress);
		DataResponse dataResponse = restTemplateUtilities.getRestObject(new CustomRequest(), url);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals(Constants.Config.SUCCESS)) {
			} else {
				throw new SchedulerException(dataResponse.getMessages().get(0).getText());
			}
		} else {
			throw new SchedulerException("Master not reachable");
		}
	}
	
	
}
