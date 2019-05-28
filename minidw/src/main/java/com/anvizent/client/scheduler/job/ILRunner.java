package com.anvizent.client.scheduler.job;

import java.util.LinkedHashMap;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.MessageSource;

import com.anvizent.client.scheduler.constant.Constant;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.processor.StandardPackageProcessor;
import com.datamodel.anvizent.data.controller.PackageUploadExecutor;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ILRunner implements Job {

	protected static final Log LOGGER = LogFactory.getLog(ILRunner.class);

	private Integer packageId, scheduleId, dlId;

	private RestTemplateUtilities packageRestUtilities;

	String browserDetails, deploymentType, csvFolderPath, timeZone;

	S3BucketInfo s3BucketInfo;

	FileSettings fileSettings;

	PackageExecution packageExecution;

	MessageSource messageSource;

	User user;

	boolean jobExecutionRequired;

	CustomRequest customRequest;

	private StandardPackageProcessor standardPackageProcessor;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {

			String uplodedStartedInfo = null;
			String started = null;
			String startDate = null;
			Package userPackage = null;
			Integer executionId = null;

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();

			packageRestUtilities = (RestTemplateUtilities) dataMap.get(Constant.General.PACKAGE_REST_UTILITY);

			browserDetails = (String) dataMap.get(Constant.General.BROWSER_DETAILS);

			deploymentType = "hybrid";

			s3BucketInfo = (S3BucketInfo) dataMap.get(Constant.General.S3_BUCKET_INFO);

			fileSettings = (FileSettings) dataMap.get(Constant.General.FILE_SETTINGS);

			csvFolderPath = (String) dataMap.get(Constant.General.CSV_FOLDER_PATH);

			user = (User) dataMap.get(Constant.General.USER_INFO);

			packageId = Integer.parseInt(dataMap.get(Constant.General.PACKAGE_ID).toString());

			scheduleId = Integer.parseInt(dataMap.get(Constant.General.SCHUDULE_ID).toString());

			dlId = Integer.parseInt(dataMap.get(Constant.General.DL_ID).toString());

			timeZone = (String) dataMap.get(Constant.General.TIME_ZONE);

			String webserviceUrl = (String) dataMap.get(Constants.Config.WEBSERVICE_CONTEXT_URL);

			jobExecutionRequired = false;

			standardPackageProcessor = (StandardPackageProcessor) dataMap
					.get(Constant.General.STANDARD_PACKAGE_PROCESSOR);

			customRequest = new CustomRequest(webserviceUrl, user.getClientId(), user.getClientId(), browserDetails,
					deploymentType, null, user.getUserId());

			DataResponse userPackageDataResponse = packageRestUtilities.getRestObject(customRequest,
					"/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);

			if (userPackageDataResponse != null && userPackageDataResponse.getHasMessages()
					&& userPackageDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

				@SuppressWarnings("unchecked")
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userPackageDataResponse.getObject();

				ObjectMapper packageMapper = new ObjectMapper()
						.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

				userPackage = packageMapper.convertValue(map, new TypeReference<Package>() {
				});

				uplodedStartedInfo = !userPackage.getIsStandard() ? "Source files upload started."
						: "IL source files upload started.";

				started = Constants.ExecutionStatus.STARTED;

				PackageExecution packageExecution = new PackageExecution();

				if (StringUtils.isBlank(timeZone)) {

					packageExecution.setTimeZone(TimeZone.getDefault().getID());

					startDate = TimeZoneDateHelper.getFormattedDateString();

				} else {

					packageExecution.setTimeZone(timeZone);

					startDate = TimeZoneDateHelper.getFormattedDateString();

				}

				packageExecution.setPackageId(packageId);

				packageExecution.setInitiatedFrom(Constants.ScheduleType.SCHEDULE);

				packageExecution.setRunType(Constants.jobType.ALL);

				packageExecution.setUploadStatus(started);

				packageExecution.setScheduleId(scheduleId);

				packageExecution.setUploadComments(uplodedStartedInfo);

				packageExecution.setUploadStartDate(startDate);

				packageExecution.setLastUploadedDate(startDate);

				packageExecution.setJobExecutionRequired(jobExecutionRequired);

				packageExecution.setDlId(dlId);

				DLInfo dlInfo = null;

				if (userPackage.getIsStandard()) {

					dlInfo = standardPackageProcessor.getDlInfoWithValidation(dlId, packageExecution, customRequest);

				}

				DataResponse packageExectionDataResponse = packageRestUtilities.postRestObject(customRequest,
						"/schedule/savePackageExectionInfo", packageExecution, user.getUserId());

				if (packageExectionDataResponse != null && packageExectionDataResponse.getHasMessages()) {

					if (packageExectionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

						executionId = (Integer) packageExectionDataResponse.getObject();

						packageExecution.setExecutionId(executionId);

						if (userPackage.getIsStandard()) {

							if (!packageExecution.getRunType().equals(Constants.jobType.DL)) {

								standardPackageProcessor.processStandardPackageExecution(packageExecution, user, dlInfo,
										s3BucketInfo, fileSettings, customRequest);

							} else {

								LOGGER.info(packageExecution.getPackageId() + " package running as run only dl");

							}

						} else {

							PackageUploadExecutor packageUploadExecutor = new PackageUploadExecutor(packageExecution,
									packageRestUtilities, LOGGER, browserDetails, deploymentType, s3BucketInfo,
									packageId, csvFolderPath, fileSettings, user, null, null);

							packageUploadExecutor.start();

							packageUploadExecutor.join();
						}

					} else {

						LOGGER.info("Save Package Exection Info failed : "
								+ packageExectionDataResponse.getMessages().get(0).getText());

					}
				}

			}

		} catch (Throwable ae) {

			LOGGER.error("Source Upload failed " + context.getJobDetail().getJobDataMap(), ae);

			Message msg = MinidwServiceUtil.getErrorMessage(ae);

			updatePackageExecutionUploadInfo(Constants.ExecutionStatus.ERROR, "Unable start the job: " + msg.getText(),
					user.getUserId());

			LOGGER.error("ILRunner Job execution failed :" + MinidwServiceUtil.getErrorMessageString(ae));

		}
	}

	void updatePackageExecutionUploadInfo(String executionOrUploadStatus, String executionOrUploadComments,
			String encUserId) {

		PackageExecution packExecution = MinidwServiceUtil.getUploadStatus(packageExecution.getExecutionId(),
				executionOrUploadStatus, executionOrUploadComments, packageExecution.getTimeZone());

		packageRestUtilities.postRestObject(customRequest, "/updatePackageExecutionUploadInfo", packExecution,
				encUserId);

	}

}
