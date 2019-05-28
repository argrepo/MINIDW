package com.datamodel.anvizent.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.anvizent.client.scheduler.listner.ClientScheduler;
import com.anvizent.client.scheduler.util.PropertiesPlaceHolder;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.datamodel.anvizent.data.controller.PackageUploadExecutor;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.Schedule;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author mahender.alaveni
 *
 */
@RestController
@RequestMapping(Constants.AnvizentURL.MINIDW_BASE_URL + "/package/packageUploadExecutor")
@CrossOrigin
public class PackageUploadExecutorController {

	protected static final Log LOGGER = LogFactory.getLog(PackageUploadExecutorController.class);

	@Autowired
	@Qualifier("scheduleServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("scheduleServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;

	PropertiesPlaceHolder propertiesPlaceHolder = null;

	@Autowired
	ClientScheduler clientScheduler;
	@Autowired
	MetaDataFetch metadataFetch;
	
	boolean execute = false;

	Map<String, PackageUploadExecutor> executionMap = new HashMap<String, PackageUploadExecutor>();

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkPackageExecutionStarted", method = RequestMethod.POST)
	public DataResponse checkPackageExecutionStarted(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestParam(value = "packageId") Integer packageId, HttpServletRequest request, Locale locale)
			throws ParseException {
		LOGGER.info("in checkPackageExecutionStarted()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<Message>();
		Message message = new Message();
		try {
			User user = CommonUtils.getUserDetails(request, null, null);
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("packageId", packageId);

			DataResponse inProgressPackageExecutionListDs = packageRestUtilities.postRestObject(request,
					"/getInProgressPackageExecutionList", map, userId);
			if (inProgressPackageExecutionListDs != null && inProgressPackageExecutionListDs.getHasMessages()) {
				if (inProgressPackageExecutionListDs.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<?> list = (List<Map<String, Object>>) inProgressPackageExecutionListDs.getObject();
					ObjectMapper mapper = new ObjectMapper()
							.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					List<PackageExecution> packageExecutionList = mapper.convertValue(list,
							new TypeReference<List<PackageExecution>>() {
							});
					if (packageExecutionList != null && packageExecutionList.size() > 0 && executionMap.size() > 0) {
						for (PackageExecution packageExecution : packageExecutionList) {
							if (executionMap.get(user.getClientId() + "_" + packageExecution.getPackageId() + "_"
									+ packageExecution.getExecutionId()) != null) {
								dataResponse.setObject(user.getClientId() + "_" + packageExecution.getPackageId() + "_"
										+ packageExecution.getExecutionId());
								execute = true;
								break;
							}
						}
						if (execute) {
							message.setCode("SUCCESS");
							message.setText(messageSource.getMessage("anvizent.message.validation.text.alreadyRunning",
									null, locale));

							execute = false;
						} else {
							message.setCode("ERROR");
							message.setText(messageSource.getMessage("anvizent.message.validation.text.notRunning",
									null, locale));
						}
					} else {
						message.setCode("ERROR");
						message.setText(
								messageSource.getMessage("anvizent.message.validation.text.notRunning", null, locale));
					}
				} else {
					message.setCode(inProgressPackageExecutionListDs.getMessages().get(0).getCode());
					message.setText(inProgressPackageExecutionListDs.getMessages().get(0).getText());
				}
			}
		} catch (Throwable e) {
			message.setCode("ERROR");
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}

		dataResponse.setMessages(messages);
		messages.add(message);
		return dataResponse;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/initiatePackageRunning", method = RequestMethod.POST)
	public DataResponse initiatePackageRunning(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			Locale locale, @RequestParam(value = "reloadUrl", required = false) String reloadUrl,
			@RequestParam(value = "packageId") Integer packageId,
			@RequestParam(value = "timeZone", required = false) String timeZone,
			@RequestParam(value = "jobType", required = false) String jobType,
			@RequestParam(value = "packageType", required = false) String packageType,
			@RequestParam(value = "executionKey", required = false) String executionKey,
			@RequestParam(value = "schedulerType", required = false) String schedulerType,
			@RequestParam(value = "schedulerId", required = false) Integer schedulerId, HttpServletRequest request)
			throws ParseException {

		LOGGER.info("in initiatePackageRunning()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<Message>();
		Message message = new Message();
		dataResponse.setMessages(messages);
		messages.add(message);
		String uplodedStartedInfo = null;
		String started = null;
		String startDate = null;
		String endDate = null;
		Integer executionId = null;
		PackageExecution packageExecution = new PackageExecution();
		PackageUploadExecutor packageUploadExecutorThread = null;
		try {
			User user = CommonUtils.getUserDetails(request, null, null);
			Package userPackage = null;
			DataResponse userPackageDataResponse = packageRestUtilities.getRestObject(request,"/getPackagesById/{" + packageId + "}", userId, packageId);
			if (userPackageDataResponse != null && userPackageDataResponse.getHasMessages() && userPackageDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userPackageDataResponse.getObject();
				ObjectMapper packageMapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				userPackage = packageMapper.convertValue(map, new TypeReference<Package>() {
				});
			} 
			
			if (userPackage != null ) {
				if (userPackage.getIsAdvanced()) {
					message.setCode("ERROR");
					message.setText("Mappings not completed properly. Please complete");
					return dataResponse;
				}
				if (!userPackage.getIsStandard() && !userPackage.getIsMapped()) {
					message.setCode("ERROR");
					message.setText(messageSource.getMessage(
							"anvizent.message.error.text.targetTableDoesNotExistPleaseCreateATargetTableForThePackageAndSchedulePackageWillbeNotBeShownInSchedulePageWithoutATargetTable",
							null, locale).replace("?", userPackage.getPackageName()));
					return dataResponse;
				}
			} else {
				message.setCode("ERROR");
				message.setText("unable to process your request");
				return dataResponse;
			}
				
			if (!StringUtils.isBlank(executionKey)) {
				packageUploadExecutorThread = executionMap.get(executionKey);
				if (packageUploadExecutorThread.getState() == Thread.State.RUNNABLE)
					packageUploadExecutorThread.interrupt();
			}

			startDate = TimeZoneDateHelper.getFormattedDateString();
			endDate = TimeZoneDateHelper.getFormattedDateString();
			if (jobType != null && !jobType.equalsIgnoreCase(Constants.jobType.DL)) {
				uplodedStartedInfo = packageType.equals("custom") ? " Source files upload started."
						: " IL source files upload started.";
				started = Constants.ExecutionStatus.STARTED;
			} else {
				uplodedStartedInfo = Constants.ExecutionStatus.IGNORED;
				started = Constants.ExecutionStatus.IGNORED;
			}

			if (StringUtils.isBlank(timeZone)) {
				packageExecution.setTimeZone(TimeZone.getDefault().getID());
			} else {
				packageExecution.setTimeZone(timeZone);
			}

			packageExecution.setPackageId(packageId);
			packageExecution.setRunType(jobType);
			packageExecution.setUploadStatus(started);
			packageExecution.setUploadComments(uplodedStartedInfo);
			packageExecution.setUploadStartDate(startDate);
			packageExecution.setLastUploadedDate(endDate);
			packageExecution.setDlId(0);
			if (schedulerType.equals(Constants.ScheduleType.RUNNOW)) {
				packageExecution.setInitiatedFrom(Constants.ScheduleType.RUNNOW);
				packageExecution.setJobExecutionRequired(true);
			} else if (schedulerType.equals(Constants.ScheduleType.RUN_WITH_SCHEDULER)) {
				packageExecution.setInitiatedFrom(Constants.ScheduleType.RUN_WITH_SCHEDULER);
				packageExecution.setJobExecutionRequired(false);
				if (schedulerId == null || schedulerId == 0) {
					ClientData clientData = new ClientData();

					clientData.setUserPackage(userPackage);

					Schedule schedule = new Schedule();
					schedule.setTimeZone(timeZone);
					clientData.setSchedule(schedule);
					clientData.setDlInfo(new DLInfo());
					clientData.getDlInfo().setdL_id(0);
					DataResponse saveSchedulerDataResponse = restUtilities.postRestObject(request,
							"/saveRunWithSchedulerInfo", clientData, userId);
					if (saveSchedulerDataResponse != null && saveSchedulerDataResponse.getHasMessages()) {
						if (saveSchedulerDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
							
						} else {
							message.setCode("ERROR");
							message.setText(saveSchedulerDataResponse.getMessages().get(0).getText());
							return dataResponse;
						}
					} else {
						message.setCode("ERROR");
						message.setText("unable to process your request");
						return dataResponse;
					}
				}
			}

			DataResponse packageExectionDataResponse = restUtilities.postRestObject(request, "/savePackageExectionInfo",
					packageExecution, userId);
			if (packageExectionDataResponse != null && packageExectionDataResponse.getHasMessages()) {
				if (packageExectionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					executionId = (Integer) packageExectionDataResponse.getObject();
					packageExecution.setExecutionId(executionId);
					startExecution(packageExecution, user,userId, locale, request, packageId);
					message.setCode("SUCCESS");
					message.setText(
							messageSource.getMessage("anvizent.message.success.text.runNowInitiated", null, locale));
				} else {
					message.setCode(packageExectionDataResponse.getMessages().get(0).getCode());
					message.setText(packageExectionDataResponse.getMessages().get(0).getText());
					messages.add(message);
				}
			}
		} catch (Throwable e) {
			message.setCode("ERROR");
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}

		return dataResponse;
	}

	private void startExecution(PackageExecution packageExecution, User user,String userId , Locale locale, HttpServletRequest request, Integer packageId) throws Exception
	{
		String browserDetails = CommonUtils.getClientBrowserDetails(request);
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String webServiceContextUrl = (String) SessionHelper.getSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL);
		S3BucketInfo s3BucketInfo = getS3BucketInfo( request,userId );//(S3BucketInfo) SessionHelper.getSesionAttribute(request, Constants.Config.S3_BUCKET_INFO);
		FileSettings fileSettings = (FileSettings) SessionHelper.getSesionAttribute(request, Constants.Config.FILE_SETTINGS_INFO);
		String csvFolderPath = getCsvFolderPath();
		PackageUploadExecutor packageUploadExecutor = new PackageUploadExecutor(packageExecution, packageRestUtilities, LOGGER, browserDetails, deploymentType, s3BucketInfo, packageId, csvFolderPath, fileSettings, user, webServiceContextUrl, metadataFetch);
		packageUploadExecutor.start();
		String executionKey = getExecutionKey(user.getClientId(), packageExecution.getPackageId(), packageExecution.getExecutionId());
		executionMap.put(executionKey, packageUploadExecutor);
	}
	@SuppressWarnings({ "unchecked" })
	private S3BucketInfo getS3BucketInfo(HttpServletRequest request, String userId) throws Exception
	{
		S3BucketInfo s3BucketInfo = null;
		DataResponse s3BucketInfoDataResponse = packageRestUtilities.getRestObject(request, "/getS3BucketInfoFromClientId", userId);
		if( s3BucketInfoDataResponse != null && s3BucketInfoDataResponse.getHasMessages() )
		{
			if( s3BucketInfoDataResponse.getMessages().get(0).getCode().equals("SUCCESS") )
			{
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) s3BucketInfoDataResponse.getObject();
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				s3BucketInfo = mapper.convertValue(map, new TypeReference<S3BucketInfo>()
				{
				});
			}
			else
			{
				throw new Exception("s3 bucket info not found for client id:" + userId);
			}
		}
		return s3BucketInfo;
	}
	private String getExecutionKey(String clientId, long packageId, long executionId) {
		return clientId + "_" + packageId + "_" + executionId;
	}

	private String getCsvFolderPath() {
		String csvFolderPath = null;
		try {
			if (propertiesPlaceHolder == null) {
				propertiesPlaceHolder = new PropertiesPlaceHolder();
			}
			// csvFolderPath = propertiesPlaceHolder.getCSVSavePath();
		} catch (Exception e) {
			LOGGER.error("Error while getting csv save path " + e.getMessage());
		}
		if (StringUtils.isBlank(csvFolderPath)) {
			csvFolderPath = Constants.Temp.getTempFileDir();
		}
		csvFolderPath += "/";

		return csvFolderPath;
	}

	@PreDestroy
	public void cleanUp() throws Exception {
		Iterator<Entry<String, PackageUploadExecutor>> st = executionMap.entrySet().iterator();
		while (st.hasNext()) {
			Entry<String, PackageUploadExecutor> next = st.next();
			LOGGER.info("Executor Package : " + next.getKey());
			PackageUploadExecutor pkgExecutor = next.getValue();
			if (pkgExecutor != null && pkgExecutor.isAlive()) {
				LOGGER.warn("Manual interruption of Executor Package :" + next.getKey());
				// pkgExecutor.interrupt();
			}
		}
	}

}
