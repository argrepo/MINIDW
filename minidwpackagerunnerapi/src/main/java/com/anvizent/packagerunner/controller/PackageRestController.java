package com.anvizent.packagerunner.controller;

import static com.datamodel.anvizent.helper.MiniDwJobUtil.cpPackageUploadExecutor;
import static com.datamodel.anvizent.helper.MiniDwJobUtil.reloadUrl;
import static com.datamodel.anvizent.helper.MiniDwJobUtil.alertsAndThreshoulds;
import static com.datamodel.anvizent.helper.MiniDwJobUtil.runAllSources;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.annotation.PreDestroy;
import javax.naming.LimitExceededException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.anvizent.minidw.service.utils.EmailSender;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.processor.DLProcessor;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.anvizent.minidw.service.utils.processor.StandardPackageProcessor;
import com.anvizent.minidw_druid__integration.DruidIntegration;
import com.anvizent.packagerunner.exceptions.SlaveNotStartedException;
import com.anvizent.packagerunner.service.PackageService;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.minidw.MinidwJobState;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.dao.ETLAdminDao;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.StandardPackageDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.MiddleLevelManager;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.datamodel.anvizent.service.model.User;

@RestController
public class PackageRestController
{

	private @Value("${alerts.thresholds.url:}") String alertsThresholdsUrl;
	private @Value("${anvizent.corews.api.url:}") String authenticationEndPointUrl;
	private @Value("${minidw.package.upload.execution.list.limit:500}") int uploadExecutionLimit;
	private @Value("${webservice.connection.request.timeout:0}") int wsConnectionRequestTimeout;
	private @Value("${webservice.connect.timeout:0}") int wsConnectTimeout;
	private @Value("${minidw.package.upload.execution.alert:prod}") String uploadExecutionAlert;
	private @Value("${webservice.uploade.timeout:0}") int wsReadTimeout;

	@Autowired
	PackageService packageService;
	@Autowired
	PackageDao packageDao;
	@Autowired
	FileDao fileDao;
	@Autowired
	ScheduleDao scheduleDao;
	@Autowired
	WebServiceDao webServiceDao;
	@Autowired
	UserDao userDao;
	@Autowired
	CommonDao commonDao;
	@Autowired
	ETLAdminDao eTLAdminDao;
	@Autowired
	StandardPackageProcessor standardPackageProcessor;
	@Autowired
	private StandardPackageDao standardPackageDao;
	@Autowired
	DLProcessor dlProcessor;
	@Autowired
	MetaDataFetch metadataFetch;
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	@Autowired
	SchedulerSlave slave;

	Map<String, PackageExecution> sourcePackageUploadInfo = new LinkedHashMap<>();
	Map<String, PackageExecution> packageExecutionInfo = new LinkedHashMap<>();

	Map<String, PackageExecution> totalSourcePackageUploadInfo = new LinkedHashMap<>();
	Map<String, PackageExecution> totalPackageExecutionInfo = new LinkedHashMap<>();

	Map<String, SourceUploadRunner> packageUploadThreads = new HashMap<>();
	Map<String, PackageExecuter> packageExecutionThreads = new HashMap<>();

	private static Log log = LogFactory.getLog(PackageRestController.class);

	// -------------------Retrieve All
	// Users--------------------------------------------------------

	@RequestMapping(value = "/executePackage", method = RequestMethod.POST)
	public DataResponse executePackage(@RequestBody PackageExecution packageExecution, HttpServletRequest request, Locale locale)
	{
		log.debug("in packageUploadExecutor() " + packageExecution);
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Package userPackage = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{

			CloudClient cloudClient = userDao.getClientDetails(packageExecution.getClientId());
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(packageExecution.getClientId()));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			// log.error("PackageExecution info " + packageExecution);
			userPackage = packageDao.getPackageById((int) packageExecution.getPackageId(), packageExecution.getUserId(), clientAppDbJdbcTemplate);
			userPackage.setUserId(packageExecution.getClientId());
			// log.error("UserPackage info " + userPackage);
			userPackage.setScheduleStatus(MinidwJobState.RUNNING.toString());
			packageExecution.setUserPackage(userPackage);
			packageExecution.setExecutionStartDate(TimeZoneDateHelper.getFormattedDateString());
			addToRunnerInfo(userPackage.getIsStandard(), packageExecution.getClientId(), packageExecution.getPackageId(), packageExecution.getDlId(), packageExecution.getExecutionId(), packageExecution);

			String deploymentType = cloudClient.getDeploymentType();
			PackageExecuter executor = new PackageExecuter(userPackage, clientJdbcTemplate, clientAppDbJdbcTemplate, packageExecution, deploymentType, clientJdbcInstance);
			addToExecutionThreadsList(userPackage.getIsStandard(), packageExecution.getClientId(), packageExecution.getPackageId(), packageExecution.getDlId(), packageExecution.getExecutionId(), executor);
			executor.start();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		}
		catch ( Throwable e )
		{
			if( e instanceof LimitExceededException || e instanceof SlaveNotStartedException )
			{
				// log.info("Message: " +
				// MinidwServiceUtil.getErrorMessageString(e));
			}
			else
			{
				log.error("error while packageUploadExecutor() " + MinidwServiceUtil.getErrorMessageString(e), e);
			}
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/uploadPackageSources", method = RequestMethod.POST)
	public DataResponse uploadPackageSources(@RequestBody PackageExecution packageExecution, HttpServletRequest request, Locale locale)
	{
		log.debug("in packageUploadFiles()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Package userPackage = null;
		try
		{

			CloudClient cloudClient = userDao.getClientDetails(packageExecution.getClientId());
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(packageExecution.getClientId()));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			userPackage = packageDao.getPackageById((int) packageExecution.getPackageId(), packageExecution.getUserId(), clientAppDbJdbcTemplate);
			userPackage.setUserId(packageExecution.getClientId());
			// userPackage.setScheduleStartTime(TimeZoneDateHelper.getFormattedDateString());
			userPackage.setScheduleStatus(MinidwJobState.RUNNING.toString());
			packageExecution.setUserPackage(userPackage);
			packageExecution.setUploadStartDate(TimeZoneDateHelper.getFormattedDateString());
			addToUploadInfo(userPackage.getIsStandard(), packageExecution.getClientId(), packageExecution.getPackageId(), packageExecution.getDlId(), packageExecution);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);

			String deploymentType = cloudClient.getDeploymentType();
			SourceUploadRunner process = new SourceUploadRunner(clientAppDbJdbcTemplate, userPackage, packageExecution, deploymentType);
			addToSourceUploadThreadsList(userPackage.getIsStandard(), packageExecution.getClientId(), packageExecution.getPackageId(), packageExecution.getDlId(), process);
			process.start();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		}
		catch ( Throwable e )
		{
			if( e instanceof LimitExceededException || e instanceof SlaveNotStartedException )
			{
				log.info("Message:" + e.getMessage());
			}
			else
			{
				log.error("error while packageUploadExecutor() " + e.getMessage());
			}
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	class PackageExecuter extends Thread
	{

		Package userPackage = null;
		DataResponse spCpPackageUploadExecutorDs = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		StringBuilder uploadStatus = new StringBuilder();
		String status = null;
		Package packageSchedule = null;
		Modification modification = null;
		PackageExecution packageExecution = null;
		String deploymentType = null;
		ClientJdbcInstance clientJdbcInstance = null;

		String clientId;
		String packageId;
		String executionId;
		Integer dlId;
        String dlName;
        String userEmail;
		public PackageExecuter(Package userPackage, JdbcTemplate clientJdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate, PackageExecution packageExecution, String deploymentType, ClientJdbcInstance clientJdbcInstance)
		{
			this.userPackage = userPackage;
			this.clientJdbcTemplate = clientJdbcTemplate;
			this.clientAppDbJdbcTemplate = clientAppDbJdbcTemplate;
			this.packageExecution = packageExecution;
			this.deploymentType = deploymentType;
			this.clientJdbcInstance = clientJdbcInstance;
			this.packageId = this.packageExecution.getPackageId() + "";
			this.clientId = this.packageExecution.getClientId();
			this.executionId = this.packageExecution.getExecutionId() + "";
			this.dlId = this.packageExecution.getDlId();
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run()
		{
			try
			{
				deploymentType = StringUtils.isBlank(deploymentType) ? "cloud" : deploymentType;
				User user = userDao.getUser(packageExecution.getUserId(), clientAppDbJdbcTemplate);
				if( user == null )
				{
					user = new User();
					user.setClientId(packageExecution.getClientId());
					user.setUserId(packageExecution.getUserId());
					user.setUserName(packageExecution.getClientId());
				}
				userEmail = user.getEmailId();
				modification = new Modification(new Date());
				modification.setCreatedBy(user.getUserName());
				modification.setModifiedBy(user.getUserName());

				// for time zone
				packageSchedule = packageDao.getPackageSchedule(packageExecution.getUserId(), (int) packageExecution.getPackageId(), (int) packageExecution.getScheduleId(), clientAppDbJdbcTemplate);

				if( userPackage.getIsStandard() )
				{
					DLInfo dlInfo = packageDao.getDLById(packageExecution.getDlId(), packageExecution.getClientId(), clientAppDbJdbcTemplate);
					dlName =dlInfo.getdL_name();
					List<String> totalTablesSet = new ArrayList<String>(Arrays.asList(dlInfo.getdL_table_name()));
					List<DDLayout> dDLayoutList = packageDao.getDDlayoutList(clientId, totalTablesSet, packageExecution.getUserId(), clientAppDbJdbcTemplate);

					if( packageExecution.getInitiatedFrom() != null && packageExecution.getInitiatedFrom().equals(com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.RUN_WITH_SCHEDULER) )
					{
						if( StringUtils.isNotBlank(packageExecution.getDdlToRun()) )
						{
							List<String> selectedDlList = Arrays.asList(StringUtils.split(packageExecution.getDdlToRun(), ","));
							dDLayoutList = dDLayoutList.stream().filter(ddlLayout -> selectedDlList.contains(ddlLayout.getId().toString())).collect(Collectors.toList());
						}
						else
						{
							dDLayoutList.clear();
						}
					}

					dlInfo.setDdlayoutList(dDLayoutList);

					packageExecution.setJobExecutionRequired(true);
					dlInfo.setDlExecutionRequired(true);
					List<String> dlTablesList = standardPackageProcessor.processILDLExecution(packageExecution, dlInfo, getCustomRequest(deploymentType, user.getUserId(), clientId));
					if( dlTablesList != null && dlTablesList.size() > 0 )
					{

						druidIntegration(user.getClientId(), user.getUserId(), dlTablesList, clientAppDbJdbcTemplate, packageExecution.getExecutionId(), packageSchedule.getTimeZone(), scheduleDao, userDao, eTLAdminDao, commonJdbcTemplate);
					}

					spCpPackageUploadExecutorDs = null;
					status = Constants.ExecutionStatus.COMPLETED;
				}
				else
				{
					dlName = userPackage.getPackageName();
					spCpPackageUploadExecutorDs =  cpPackageUploadExecutor(packageExecution.getExecutionId(), userPackage, fileDao, scheduleDao, packageDao, user, clientJdbcInstance, deploymentType, packageSchedule.getTimeZone(), userDao, slave);
					uploadStatus.append("\n Execution completed for custom package.\n Source files execution completed.");
				}

				if( spCpPackageUploadExecutorDs != null && spCpPackageUploadExecutorDs.getHasMessages() )
				{
					if( spCpPackageUploadExecutorDs.getMessages().get(0).getCode().equals("SUCCESS") && spCpPackageUploadExecutorDs.getObject() != null )
					{
						Map<String, Object> responseMap = (Map<String, Object>) spCpPackageUploadExecutorDs.getObject();
						Set<String> tableSet = (HashSet<String>) responseMap.get("totalTablesSet");
						List<String> tableList = new ArrayList<String>(tableSet);
						String runType = com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.SCHEDULE;
						// runCustomDataSets
						List<String> ddlsList = metadataFetch.runCustomDataSets(tableList, runType, getCustomRequest(deploymentType, user.getUserId(), clientId));
						String ddlTablesList = "";
						if( ddlsList != null && ddlsList.size() > 0 )
						{
							for (String ddlTable : ddlsList)
							{
								tableList.add(ddlTable);
							}
							ddlTablesList = String.join(",", ddlsList.toArray(new String[] {}));
						}
						uploadStatus.append("\n ddl ran successfully. Processed DDL's " + ddlTablesList);

						clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
						Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
						String schemaName = clientDbDetails.get("clientdb_schema").toString();

						reloadUrl(user.getUserId(), tableList, user.getClientId(), schemaName, clientAppDbJdbcTemplate, authenticationEndPointUrl);
						uploadStatus.append("\n reload Url updated successfully.");

						druidIntegration(user.getClientId(), user.getUserId(), tableList, clientAppDbJdbcTemplate, packageExecution.getExecutionId(), packageSchedule.getTimeZone(), scheduleDao, userDao, eTLAdminDao, commonJdbcTemplate);
						uploadStatus.append("\n druid Integration   updated successfully.");

						if( StringUtils.isNotBlank(alertsThresholdsUrl) )
						{
							alertsAndThreshoulds(user.getClientId(), tableList, packageExecution.getPackageId() + "", alertsThresholdsUrl);
							uploadStatus.append("\n alerts And Threshoulds updated successfully.");
						}

						status = Constants.ExecutionStatus.COMPLETED;
					}
					else
					{
						status = Constants.ExecutionStatus.FAILED;
					}
				}

			}
			catch ( Throwable e )
			{
				log.error("Error occured while executing the job ", e);
				Message msg = MinidwServiceUtil.getErrorMessage(e);
				uploadStatus.append(msg.getText());
				status = Constants.ExecutionStatus.FAILED;
			}
			finally
			{
				String keyObj = clientId + "_" + packageId + "_" + dlId + "_" + executionId;
				packageExecutionInfo.remove(keyObj);
				packageExecutionThreads.remove(keyObj);
				if( totalPackageExecutionInfo.get(keyObj) != null )
				{
					totalPackageExecutionInfo.get(keyObj).getUserPackage().setScheduleStatus(status);
					totalPackageExecutionInfo.get(keyObj).setLastExecutedDate(TimeZoneDateHelper.getFormattedDateString());
				}
			}

			packageExecution = MinidwServiceUtil.getExecutionStatus(packageExecution.getExecutionId(), status, uploadStatus.toString(), packageExecution.getTimeZone());
			packageExecution.setModification(modification);
			scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);

			if( uploadExecutionAlert.equals("prod") )
			{
				if( status.equals(Constants.ExecutionStatus.FAILED) )
				{
					emailSendForFailedUploadOrExecution(userEmail,dlName,clientId, userPackage.getPackageId(), dlId, (int) packageExecution.getExecutionId(), "execution", clientAppDbJdbcTemplate);
				}
			}
		}
	}

	class SourceUploadRunner extends Thread
	{

		String startDate = null;
		Integer executionId = null;

		String clientId;
		String packageId;
		Integer dlId;
		Modification modification = null;
		String timeZone = null;
		StringBuilder uploadStatus = new StringBuilder();
		String status = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Package userPackage = null;
		PackageExecution packageExecution = null;
		String deploymentType = null;
		String dlName;
        String userEmail;
		public SourceUploadRunner(JdbcTemplate clientAppDbJdbcTemplate, Package userPackage, PackageExecution packageExecution, String deploymentType)
		{
			super();
			this.clientAppDbJdbcTemplate = clientAppDbJdbcTemplate;
			this.userPackage = userPackage;
			this.packageExecution = packageExecution;
			this.packageId = this.packageExecution.getPackageId() + "";
			this.clientId = this.packageExecution.getClientId();
			this.deploymentType = deploymentType;
			this.dlId = this.packageExecution.getDlId();
		}

		@Override
		public void run()
		{
			try
			{
				ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(packageExecution.getClientId()));
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				userPackage = packageDao.getPackageById((int) packageExecution.getPackageId(), packageExecution.getUserId(), clientAppDbJdbcTemplate);
				deploymentType = StringUtils.isBlank(deploymentType) ? "cloud" : deploymentType;

				User user = userDao.getUser(packageExecution.getUserId(), clientAppDbJdbcTemplate);
				modification = new Modification(new Date());
				modification.setCreatedBy(user.getUserName());
				modification.setModifiedBy(user.getUserName());
				userEmail = user.getEmailId();
				Package packageSchedule = packageDao.getPackageSchedule(user.getUserId(), (int) packageExecution.getPackageId(), (int) packageExecution.getScheduleId(), clientAppDbJdbcTemplate);
				S3BucketInfo s3BucketInfo = userDao.getS3BucketInfoByClientId(user.getClientId(), clientAppDbJdbcTemplate);
				if( s3BucketInfo == null )
				{
					s3BucketInfo = new S3BucketInfo();
					s3BucketInfo.setId(0);
				}
				timeZone = StringUtils.isBlank(packageSchedule.getTimeZone()) ? TimeZone.getDefault().getID() : packageSchedule.getTimeZone();

				startDate = TimeZoneDateHelper.getFormattedDateString();

				if( StringUtils.isBlank(packageSchedule.getTimeZone()) )
				{
					packageExecution.setTimeZone(TimeZone.getDefault().getID());
				}
				else
				{
					packageExecution.setTimeZone(packageSchedule.getTimeZone());
				}
				packageExecution.setInitiatedFrom(com.datamodel.anvizent.helper.minidw.Constants.ScheduleType.SCHEDULE);
				packageExecution.setRunType(com.datamodel.anvizent.helper.minidw.Constants.jobType.ALL);
				packageExecution.setUploadStatus(Constants.ExecutionStatus.STARTED);
				packageExecution.setUploadComments(" Source files upload started at " + slave.getId() + "-" + slave.getName()+".");
				packageExecution.setUploadStartDate(startDate);
				packageExecution.setLastUploadedDate(startDate);
				packageExecution.setJobExecutionRequired(false);
				packageExecution.setModification(modification);

				DLInfo dlInfo = null;
				if( userPackage.getIsStandard() )
				{
					dlInfo = standardPackageDao.getIlMappingInfobyId(packageExecution.getUserId(), clientId, dlId, clientAppDbJdbcTemplate);
					dlName = dlInfo.getdL_name();
					standardPackageProcessor.determinePackageExecution(dlInfo, packageExecution);
				}

				executionId = scheduleDao.savePackageExectionInfo(packageExecution, clientAppDbJdbcTemplate);
				if( executionId != 0 )
				{
					packageExecution.setExecutionId(executionId);

					if( userPackage.getIsStandard() )
					{
						if( !packageExecution.getRunType().equals(com.datamodel.anvizent.helper.minidw.Constants.jobType.DL) )
						{
							if( dlInfo != null )
							{
								List<String> totalTablesSet = new ArrayList<String>(Arrays.asList(dlInfo.getdL_table_name()));
								List<DDLayout> dDLayoutList = packageDao.getDDlayoutList(clientId, totalTablesSet, packageExecution.getUserId(), clientAppDbJdbcTemplate);
								dlInfo.setDdlayoutList(dDLayoutList);

								for (ILInfo mappingInfo : dlInfo.getIlList())
								{
									mappingInfo.setIlSources(MinidwServiceUtil.getILConnectionMapping(mappingInfo.getIlSources()));
								}
							}

							FileSettings fileSettings = packageDao.getFileSettingsInfo(user.getClientId(), clientAppDbJdbcTemplate);

							dlInfo.setWsConnectionRequestTimeout(wsConnectionRequestTimeout);
							dlInfo.setWsReadTimeout(wsReadTimeout);
							dlInfo.setWsConnectTimeout(wsConnectTimeout);

							standardPackageProcessor.processStandardPackageExecution(packageExecution, user, dlInfo, s3BucketInfo, fileSettings, getCustomRequest(deploymentType, packageExecution.getUserId(), clientId));

						}
						else
						{
							status = Constants.ExecutionStatus.IGNORED;
							return;
						}
					}
					else
					{
						dlName = userPackage.getPackageName();
						runAllSources((long) executionId, userPackage, fileDao, scheduleDao, packageDao, user, clientJdbcInstance, deploymentType, packageSchedule.getTimeZone(), webServiceDao, s3BucketInfo, commonJdbcTemplate, packageExecution);
					}
				}
				status = Constants.ExecutionStatus.COMPLETED;

			}
			catch ( PackageExecutionException pe )
			{
				log.error("error while packageUploadExecutor() ", pe);
				uploadStatus.append(MinidwServiceUtil.getErrorMessageString(pe));
				status = Constants.ExecutionStatus.FAILED;
			}
			catch ( Throwable e )
			{
				log.error("error while packageUploadExecutor() ", e);
				uploadStatus.append(MinidwServiceUtil.getErrorMessageString(e));
				status = Constants.ExecutionStatus.FAILED;
			}
			finally
			{
				String key = clientId + "_" + packageId + "_" + dlId;
				sourcePackageUploadInfo.remove(key);
				packageUploadThreads.remove(key);
				if( totalSourcePackageUploadInfo.get(key) != null )
				{
					totalSourcePackageUploadInfo.get(key).getUserPackage().setScheduleStatus(status);
					totalSourcePackageUploadInfo.get(key).setLastUploadedDate(TimeZoneDateHelper.getFormattedDateString());
				}
			}
			packageExecution = MinidwServiceUtil.getUploadStatus(executionId, status, "\n" + uploadStatus.toString() + "\n Source files upload completed.", timeZone);
			packageExecution.setModification(modification);
			scheduleDao.updatePackageExecutionUploadInfo(packageExecution, clientAppDbJdbcTemplate);
			if( uploadExecutionAlert.equals("prod") )
			{
				 if( status.equals(Constants.ExecutionStatus.FAILED) )
				 {
					emailSendForFailedUploadOrExecution(userEmail, dlName,clientId, userPackage.getPackageId(), dlId, executionId, "upload", clientAppDbJdbcTemplate);
				 }
			}
		}

	}

	void emailSendForFailedUploadOrExecution(String userEmail,String dlName ,String clientId, int packageId, int dlId, int executionId, String uploadOrExecution, JdbcTemplate clientAppDbJdbcTemplate)
	{
		FileReader fr = null;
		try
		{
			String uploadComments = packageDao.getUploadAndExecutionStatusComments(executionId, uploadOrExecution, clientAppDbJdbcTemplate);
			fr = new FileReader(new ClassPathResource("mail.properties").getFile());
			Properties mailProperties = new Properties();
			mailProperties.load(fr);
			String toMails =  mailProperties.getProperty("anvizent.mail.to");
			mailProperties.put("anvizent.mail.to", toMails+","+userEmail);
			URL aURL = new URL(authenticationEndPointUrl);
		    String enviromentURL = aURL.getProtocol()+"://"+aURL.getAuthority();
			EmailSender emailSender = new EmailSender(mailProperties);
			if( uploadOrExecution.equals("upload") )
			{
				emailSender.sendUploadErrorDetails(dlName,uploadComments, clientId, executionId, packageId, dlId,enviromentURL);
			}
			else
			{
				emailSender.sendExecutionErrorDetails(dlName,uploadComments, clientId, executionId, packageId, dlId,enviromentURL);
			}

		}
		catch ( IOException e )
		{
			e.printStackTrace();
			log.error("Error occured while sending mail for upload info:", e);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			log.error("Error occured while sending mail for upload info:", e);
		}
		finally
		{
			if( fr != null )
			{
				try
				{
					fr.close();
				}
				catch ( IOException e )
				{
					e.printStackTrace();
					log.error("Error occured while sending mail for upload info:", e);
				}
			}
		}
	}

	private synchronized void addToUploadInfo(boolean isStandard, String clientId, long packageId, long dlId, PackageExecution packageExecution) throws Exception
	{
		if( checkForUploadLimit() ) 
		{
			sourcePackageUploadInfo.put(clientId + "_" + packageId + "_" + dlId, packageExecution);
			totalSourcePackageUploadInfo.put(clientId + "_" + packageId + "_" + dlId, packageExecution);
		}
	}

	private synchronized void addToRunnerInfo(boolean isStandard, String clientId, long packageId, long dlId, long executionId, PackageExecution packageExecution) throws Exception
	{
		if( checkForRunnerLimit() )
		{
			packageExecutionInfo.put(clientId + "_" + packageId + "_" + dlId + "_" + executionId, packageExecution);
			totalPackageExecutionInfo.put(clientId + "_" + packageId + "_" + dlId + "_" + executionId, packageExecution);
		}
	}

	private synchronized void addToSourceUploadThreadsList(boolean isStandard, String clientId, long packageId, long dlId, SourceUploadRunner userPackage) throws Exception
	{
		packageUploadThreads.put(clientId + "_" + packageId + "_" + dlId, userPackage);
	}

	private synchronized void addToExecutionThreadsList(boolean isStandard, String clientId, long packageId, long dlId, long executionId, PackageExecuter userPackage) throws Exception
	{
		packageExecutionThreads.put(clientId + "_" + packageId + "_" + dlId + "_" + executionId, userPackage);
	}

	synchronized boolean checkForUploadLimit() throws Exception
	{
		if( slave != null )
		{
			if( sourcePackageUploadInfo.size() < slave.getFileUploadCount() )
			{
				return true;
			}
			else
			{
				throw new LimitExceededException("Source upload limit reached");
			}
		}
		else
		{
			throw new SlaveNotStartedException("Slave not yet started");
		}
	}

	synchronized boolean checkForRunnerLimit() throws Exception
	{
		if( slave != null )
		{
			if( packageExecutionInfo.size() < slave.getPackageExecutionCount() )
			{
				return true;
			}
			else
			{
				throw new LimitExceededException("package execution limit reached");
			}
		}
		else
		{
			throw new SlaveNotStartedException("Slave not yet started");
		}
	}

	@RequestMapping(value = "/startSlave", method = RequestMethod.POST)
	public DataResponse startSlave(@RequestBody SchedulerSlave schedulerSlave, HttpServletRequest request, Locale locale)
	{
		log.debug("in startSlave()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		try
		{
			schedulerSlave = commonDao.getServerSlaveById(schedulerSlave.getId());

			if( schedulerSlave != null )
			{
				slave = schedulerSlave;
			}
			else
			{
				throw new Exception("slave details not found.");
			}

			message.setCode("SUCCESS");
			message.setText("Slave started ");

		}
		catch ( Throwable e )
		{
			log.error("error while startSlave() ", e);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
		}

		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/slaveStatus", method = RequestMethod.POST)
	public DataResponse slaveStatus(HttpServletRequest request, Locale locale)
	{
		log.debug("in slaveStatus()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		dataResponse.setObject(false);
		try
		{
			message.setCode("SUCCESS");
			if( slave != null )
			{
				message.setText("slave details available with id:" + slave.getId());
			}
			dataResponse.setObject(this.slave != null);
		}
		catch ( Throwable e )
		{
			log.error("error while startSlave() ", e);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/stopSlave", method = RequestMethod.POST)
	public DataResponse stopSlave(HttpServletRequest request, Locale locale)
	{
		log.debug("in stopSlave()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try
		{
			if( slave != null )
			{
				slave = null;
			}
			else
			{
				throw new SlaveNotStartedException("slave  not yet started.");
			}
			message.setCode("SUCCESS");
		}
		catch ( Throwable e )
		{
			log.error("error while stopSlave() ", e);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	public static void druidIntegration(String clientIdfromHeader, String userId, List<String> totalTablesList, JdbcTemplate clientAppDbJdbcTemplate, long executionId, String timeZone, ScheduleDao scheduleDao, UserDao userDao, ETLAdminDao eTLAdminDao, JdbcTemplate commonJdbcTemplate)
	{

		Modification modification = null;
		MiddleLevelManager middleLevelManager = null;

		try
		{

			CloudClient cloudClient = userDao.getClientDetails(clientIdfromHeader);
			timeZone = StringUtils.isBlank(timeZone) ? TimeZone.getDefault().getID() : timeZone;

			modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			modification.setModifiedBy(userId);

			if( cloudClient.isDruidEnabled() )
			{

				middleLevelManager = eTLAdminDao.getMiddleLevelManagerDetailsById(commonJdbcTemplate);
				DruidIntegration druidIntegration = new DruidIntegration(totalTablesList, userId, middleLevelManager, clientIdfromHeader, scheduleDao, modification, executionId, timeZone, clientAppDbJdbcTemplate);
				druidIntegration.start();

			}
			else
			{
				PackageExecution packExecutionAfter = MinidwServiceUtil.getDruidStatusAndComments(executionId, Constants.ExecutionStatus.IGNORED, "Druid is not not enabled this client id:" + clientIdfromHeader, timeZone);
				packExecutionAfter.setModification(modification);
				scheduleDao.updateDruidStartInfo(packExecutionAfter, clientAppDbJdbcTemplate);
			}
		}
		catch ( Throwable t )
		{
			PackageExecution packExecutionAfter = MinidwServiceUtil.getDruidStatusAndComments(executionId, Constants.ExecutionStatus.FAILED, "\n" + t.getMessage(), timeZone);
			packExecutionAfter.setModification(modification);
			scheduleDao.updateDruidEndInfo(packExecutionAfter, clientAppDbJdbcTemplate);
		}

	}

	@RequestMapping(value = "/getPackageUploadAndRunnerInfo", method = RequestMethod.POST)
	public DataResponse getPackageUploadAndRunnerInfo(HttpServletRequest request, @RequestParam(value = "requestType", required = false, defaultValue = "running") String requestType, Locale locale)
	{
		log.debug("in getPackageUploadAndRunnerInfo()");

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		try
		{

			removeExcessAboveLimit(totalSourcePackageUploadInfo, "upload");
			removeExcessAboveLimit(totalPackageExecutionInfo, "execution");
			Map<String, List<PackageExecution>> dataResponseList = new HashMap<>();

			List<PackageExecution> packageUploadInfoList = new ArrayList<>();
			List<PackageExecution> packageRunnerInfoList = new ArrayList<>();

			Map<String, PackageExecution> sourceInfo = requestType.equals("all") ? totalSourcePackageUploadInfo : sourcePackageUploadInfo;
			Map<String, PackageExecution> executionInfo = requestType.equals("all") ? totalPackageExecutionInfo : packageExecutionInfo;

			for (Entry<String, PackageExecution> entry : sourceInfo.entrySet())
			{
				packageUploadInfoList.add(entry.getValue());
			}

			for (Entry<String, PackageExecution> entry : executionInfo.entrySet())
			{
				packageRunnerInfoList.add(entry.getValue());
			}

			dataResponseList.put("packageUploadInfoList", packageUploadInfoList);
			dataResponseList.put("packageRunnerInfoList", packageRunnerInfoList);

			dataResponse.setObject(dataResponseList);
			message.setCode("SUCCESS");

		}
		catch ( Throwable e )
		{
			log.error("error while getPackageUploadAndRunnerInfo() ", e);
			MinidwServiceUtil.addErrorMessage(message, e);
		}
		messages.add(message);
		dataResponse.addMessages(messages);
		return dataResponse;
	}

	private void removeExcessAboveLimit(Map<String, PackageExecution> list, String type)
	{

		if( list.size() > uploadExecutionLimit )
		{
			int toBeRemovedCount = list.size() - uploadExecutionLimit;
			Iterator<Entry<String, PackageExecution>> st = list.entrySet().iterator();
			int iterationCnt = 0;
			while (st.hasNext())
			{
				Entry<String, PackageExecution> next = st.next();
				log.info("Removed excess package execution from Total " + type + " list : " + next.getValue());
				st.remove();
				iterationCnt++;
				if( iterationCnt >= toBeRemovedCount )
				{
					break;
				}
			}
		}
	}

	@PreDestroy
	public void cleanUp() throws Exception
	{
		log.info("Before destroying Controller");
		log.info("All total Upload list info:" + totalSourcePackageUploadInfo);
		log.info("All total Execution list info:" + totalPackageExecutionInfo);
		log.info("Current running upload list info:" + sourcePackageUploadInfo);
		log.info("Current running Execution list info:" + packageExecutionInfo);
	}

	public CustomRequest getCustomRequest(String deploymentType, String userId, String clientId)
	{
		CustomRequest customRequest = new CustomRequest(null, clientId, clientId, null, deploymentType, null, userId);
		return customRequest;
	}

}