package com.datamodel.anvizent.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.RJobUtil;
import com.anvizent.minidw.service.utils.model.ETLjobExecutionMessages;
import com.anvizent.minidw.service.utils.model.RJobExecutionMessages;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.anvizent.minidw.service.utils.processor.ETLJobProcessor;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.anvizent.minidw.service.utils.processor.ParseErrorMessage;
import com.anvizent.minidw.service.utils.processor.PreparedObjectProcessor;
import com.anvizent.minidw.service.utils.processor.S3FileProcessor;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.CSVConversionException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.dao.AIServiceDao;
import com.datamodel.anvizent.service.dao.ETLAdminDao;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.model.AIContextParameter;
import com.datamodel.anvizent.service.model.AIJobUploadInfo;
import com.datamodel.anvizent.service.model.AIModel;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.ClientCurrencyMapping;
import com.datamodel.anvizent.service.model.ClientDbCredentials;
import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.CurrencyIntegration;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.HistoricalLoadForm;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;

@Component
public class ExecutionProcessor
{
	protected static final Log log = LogFactory.getLog(ExecutionProcessor.class);

	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	S3FileProcessor s3FileProcessor;
	@Autowired
	PreparedObjectProcessor preparedObjectProcessor;
	@Autowired
	ETLJobProcessor etlJobProcessor;
	@Autowired
	ParseErrorMessage parseErrorMessage;
	@Autowired
	MetaDataFetch metaDataFetch;
	@Autowired
	EltProcessor eltProcessor;
	@Autowired
	private ClientDbCredentials clientDbCredentials;
	@Autowired
	private ETLAdminDao etlAdminDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private FileDao fileDao;
	@Autowired
	private WebServiceDao webServiceDao;
	@Autowired
	private PackageDao packageDao;
	@Autowired
	private ScheduleDao scheduleDao;
	@Autowired
	private AIServiceDao aIServiceDao;

	public DataResponse runIl(User user, Modification modification, String timeZone, String jobFilesPath, ClientJdbcInstance clientJdbcInstance, PackageExecutorMappingInfo packageExecutorMappingInfo, String deploymentType, Package userPackage)
			throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException, AnvizentCorewsException
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		ILConnectionMapping iLConnectionMapping = null;
		List<String> filePaths = null;
		JobExecutionInfo jobExecutionInfo = null;
		Map<String, Object> responseMap = new HashMap<>();
		PackageExecution packageExecution = null;
		String executionMappingInfoStatus = Constants.ExecutionStatus.ERROR;
		StringBuilder executionMappingInfoComments = new StringBuilder();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientStagingJdbcTemplate = null;
		ClientJobExecutionParameters clientJobExecutionParameters = null;
		String datbaseTypeName = "unknown";
		S3BucketInfo s3BucketInfo = null;
		try
		{
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			iLConnectionMapping = packageDao.getIlConnectionMappingByPackageExecutorSourceMappingInfo(packageExecutorMappingInfo.getExecutionId(), packageExecutorMappingInfo.getId(), clientAppDbJdbcTemplate);

			if( iLConnectionMapping != null )
			{

				String defaultSrcTimezone = null;
				ILConnectionMapping iLConnectionMappingInfobyConnectionFlags = packageDao.getILConnectionMappingInfoForPreview(iLConnectionMapping.getConnectionMappingId(), iLConnectionMapping.getPackageId(), user.getClientId(), clientAppDbJdbcTemplate);

				if( iLConnectionMappingInfobyConnectionFlags == null )
				{
					message.setCode("ERROR");
					message.setText("Mapping info not found");
					return dataResponse;
				}
				if( iLConnectionMappingInfobyConnectionFlags.getIsWebservice() != null && iLConnectionMappingInfobyConnectionFlags.getIsWebservice() )
				{
					WebServiceConnectionMaster webServiceConnectionMaster = webServiceDao.getWebServiceConnectionDetails(Long.valueOf(iLConnectionMappingInfobyConnectionFlags.getWsConId()), user.getClientId(), clientAppDbJdbcTemplate);
					defaultSrcTimezone = webServiceConnectionMaster.getWebServiceTemplateMaster().getTimeZone();
					datbaseTypeName = "W" + webServiceConnectionMaster.getId();
				}
				else
				{
					if( iLConnectionMappingInfobyConnectionFlags.getIsFlatFile() )
					{
						datbaseTypeName = "F";
					}
					else
					{
						ILConnection iLConnection = packageDao.getILConnectionById(iLConnectionMappingInfobyConnectionFlags.getiLConnection().getConnectionId(), user.getUserId(), clientAppDbJdbcTemplate);
						defaultSrcTimezone = iLConnection.getTimeZone();
						datbaseTypeName = "D" + iLConnectionMappingInfobyConnectionFlags.getiLConnection().getConnectionId();
					}
				}
				datbaseTypeName = StringUtils.isNotBlank(iLConnectionMapping.getIlSourceName()) ? iLConnectionMapping.getIlSourceName() : datbaseTypeName;

				Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
				String clientSchemaName = clientDbDetails.get("clientdb_schema").toString();
				String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();
				String databaseHost = clientDbDetails.get("region_hostname").toString();
				String databasePort = clientDbDetails.get("region_port").toString();
				String databaseUserName = clientDbDetails.get("clientdb_username").toString();
				String databasePassword = clientDbDetails.get("clientdb_password").toString();

				ILInfo iLInfo = packageDao.getILByIdWithJobName(iLConnectionMapping.getiLId(), user.getClientId(), clientAppDbJdbcTemplate);
				if( iLInfo != null )
				{

					if( deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM) )
					{
						s3BucketInfo = new S3BucketInfo();
						s3BucketInfo.setId(0);
						s3BucketInfo.setClientId(Integer.valueOf(user.getClientId()));
					}
					else
					{
						s3BucketInfo = userDao.getS3BucketInfoById(user.getClientId(), iLConnectionMapping.getS3BucketId(), null);
					}
					if( iLInfo.getJobExecutionType().equals("E") )
					{

						String messageString = "IL %s execution started for execution source mapping id %d";
						messageString = String.format(messageString, iLInfo.getiL_name(), packageExecutorMappingInfo.getId());
						metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, messageString, packageExecutorMappingInfo.getPackageExecution(), getCustomRequest(packageExecutorMappingInfo.getPackageExecution().getClientId()));

						JSONObject externalDataJSON = commonProcessor.getExternalDataJSON(packageExecutorMappingInfo.getId(), packageExecutorMappingInfo.getPackageExecution().getScheduleId(), packageExecutorMappingInfo.getPackageExecution().getExecutionId(),
								packageExecutorMappingInfo.getPackageExecution().getClientId(), packageExecutorMappingInfo.getPackageExecution().getDlId(), packageExecutorMappingInfo.getPackageExecution().getIlId());

						try
						{
							Map<String, Object> executionResponse = eltProcessor.processEltJob(iLInfo.getJobTagId(), iLInfo.getLoadParameterId(), iLInfo.getMasterParameterId(), packageExecutorMappingInfo.getPackageExecution(), true, iLConnectionMapping.isMultipartEnabled(),
									iLConnectionMapping.getFilePath(), s3BucketInfo, datbaseTypeName, externalDataJSON, deploymentType, modification, clientJdbcInstance);

							System.out.println(executionResponse.get("data"));
							messageString = "IL %s execution completed for execution source mapping id %d";
							messageString = String.format(messageString, iLInfo.getiL_name(), packageExecutorMappingInfo.getId());
							metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, messageString, packageExecutorMappingInfo.getPackageExecution(), getCustomRequest(packageExecutorMappingInfo.getPackageExecution().getClientId()));

							Map<String, String> iLDataMap = new HashMap<>();
							iLDataMap.put("iLTableName", iLInfo.getiL_table_name());
							message.setCode("SUCCESS");
							message.setText("ELT job execution completed");
							dataResponse.setObject(iLDataMap);

							// incremental update
							if( iLConnectionMapping.getIsIncrementalUpdate() && StringUtils.isNotBlank(iLConnectionMapping.getIncrementalDateValue()) )
							{
								clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
								scheduleDao.saveOrUpdateIncrementalUpdate(iLConnectionMapping.getIncrementalDateValue(), iLConnectionMapping.getConnectionMappingId(), clientAppDbJdbcTemplate, clientStagingJdbcTemplate);
							}

						}
						catch ( Exception e )
						{
							message.setCode("ERROR");
							message.setText(e.getMessage());
							messageString = "IL %s execution failed for execuion source mapping id %d.\n Reason: %s";
							messageString = String.format(messageString, iLInfo.getiL_name(), packageExecutorMappingInfo.getId(), e.getMessage());
							metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, messageString, packageExecutorMappingInfo.getPackageExecution(), getCustomRequest(packageExecutorMappingInfo.getPackageExecution().getClientId()));
						}
						return dataResponse;
					}
					if( iLConnectionMapping.getFileType() == null || iLConnectionMapping.getFileType().equals("csv") )
					{
						packageExecution = commonProcessor.packageExecutionMappingInfo(Constants.ExecutionStatus.STARTED, "IL execution started.", packageExecutorMappingInfo.getId(), modification, timeZone);
						packageExecution.setModification(modification);
						scheduleDao.updatePackageExecutionMappingInfo(packageExecution, clientAppDbJdbcTemplate);

						if( s3BucketInfo != null )
						{
							filePaths = s3FileProcessor.downloadFilesFromS3(iLConnectionMapping.getFilePath(), user.getUserId(), deploymentType, iLConnectionMapping.isMultipartEnabled(), iLConnectionMapping.isEncryptionRequired(), s3BucketInfo);

							if( filePaths != null )
							{
								Map<String, String> ilContextParams = packageDao.getIlContextParams(iLConnectionMapping.getiLId(), clientAppDbJdbcTemplate);
								clientJobExecutionParameters = packageDao.getClientJobExecutionParams(clientAppDbJdbcTemplate);

								if( clientJobExecutionParameters == null )
								{
									clientJobExecutionParameters = getDefaultJobExecutionParamters();
								}
								if( StringUtils.isNotBlank(defaultSrcTimezone) )
								{
									clientJobExecutionParameters.setSourceTimeZone(defaultSrcTimezone);
								}
								if( userPackage.getTrailingMonths() != 0 )
								{
									clientJobExecutionParameters.setInterval(userPackage.getTrailingMonths());
								}
								Map<String, String> contextParams = preparedObjectProcessor.getContextParams(ilContextParams, databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword, databaseHost, databasePort, clientSchemaName, databaseUserName, databasePassword,
										databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword, datbaseTypeName, iLInfo, user.getClientId(), userPackage.getPackageId(), jobFilesPath, packageExecutorMappingInfo.getExecutionId(), clientJobExecutionParameters,
										iLConnectionMapping.getIsIncrementalUpdate(), packageExecutorMappingInfo.getPackageExecution().getDlId());
								if( contextParams != null )
								{
									executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(getFormattedDateString()).append("\t IL job started...").append(Constants.Config.NEW_LINE).append("id : " + iLInfo.getiL_id() + "; IL Name :" + iLInfo.getiL_name());
									int ilSourceIterationCount = 1;
									int ilSourceMultiPartCount = filePaths.size();
									log.info("IL job started...id : " + iLInfo.getiL_id() + "; IL name :" + iLInfo.getiL_name() + "; IL job Class :" + iLInfo.getJobName());
									executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(getFormattedDateString()).append("\t Execution started for the partfiles ").append(ilSourceMultiPartCount).append("\n");
									executionMappingInfoComments.append("Context Parameters \n").append(getFormattedDateString()).append("\t ").append(commonProcessor.getPrintableParams(contextParams));

									for (String filePath : filePaths)
									{
										executionMappingInfoComments.append("\nFILE_SRC=" + filePath);
										String statusString = executionMappingInfoComments.toString();
										packageExecution = commonProcessor.getExecutionStatus(packageExecutorMappingInfo.getExecutionId(), Constants.ExecutionStatus.INPROGRESS, statusString, timeZone);
										packageExecution.setModification(modification);
										scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);
										executionMappingInfoComments = new StringBuilder();

										jobExecutionInfo = etlJobProcessor.runILEtlJob(contextParams, iLInfo, user.getUserId(), userPackage.getPackageId(), filePath, modification, jobFilesPath, user);

										scheduleDao.updateJobExecutionDetails(jobExecutionInfo, clientAppDbJdbcTemplate);
										executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(jobExecutionInfo.getExecutionMessages());
										executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(getFormattedDateString()).append("\t Execution completed for the partfile ").append(ilSourceIterationCount).append(" of ").append(ilSourceMultiPartCount);
										ilSourceIterationCount++;
									}

									String statusString = executionMappingInfoComments.toString();
									packageExecution = commonProcessor.getExecutionStatus(packageExecutorMappingInfo.getExecutionId(), Constants.ExecutionStatus.INPROGRESS, statusString, timeZone);
									packageExecution.setModification(modification);
									scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);

									log.info("IL job completed...id : " + iLInfo.getiL_id() + "; IL name :" + iLInfo.getiL_name());
									executionMappingInfoStatus = Constants.ExecutionStatus.COMPLETED;

									packageExecution = commonProcessor.packageExecutionMappingInfo(executionMappingInfoStatus, "Il execution completed.", packageExecutorMappingInfo.getId(), modification, timeZone);
									packageExecution.setModification(modification);
									scheduleDao.updatePackageExecutionMappingInfoStatus(packageExecution, clientAppDbJdbcTemplate);

									message.setCode("SUCCESS");
									message.setText("IL executed successfully.");
									responseMap.put("dlId", iLConnectionMapping.getdLId());
									responseMap.put("iLTableName", iLInfo.getiL_table_name());
									dataResponse.setObject(responseMap);
									// incremental update
									if( iLConnectionMapping.getIsIncrementalUpdate() && StringUtils.isNotBlank(iLConnectionMapping.getIncrementalDateValue()) )
									{
										clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
										scheduleDao.saveOrUpdateIncrementalUpdate(iLConnectionMapping.getIncrementalDateValue(), iLConnectionMapping.getConnectionMappingId(), clientAppDbJdbcTemplate, clientStagingJdbcTemplate);
									}
								}
								else
								{
									message.setCode("ERROR");
									message.setText("Client job execution parameters not found");
								}
							}
							else
							{
								message.setCode("ERROR");
								message.setText("Files not found in S3 for buket id: " + iLConnectionMapping.getS3BucketId());
							}
						}
						else
						{
							message.setCode("ERROR");
							message.setText("S3 bucket info not found for buket id: " + iLConnectionMapping.getS3BucketId());
						}
					}
					else if( iLConnectionMapping.getFileType().equals("json") )
					{
						packageExecution = commonProcessor.packageExecutionMappingInfo(Constants.ExecutionStatus.STARTED, "IL execution started.", packageExecutorMappingInfo.getId(), modification, timeZone);
						packageExecution.setModification(modification);
						scheduleDao.updatePackageExecutionMappingInfo(packageExecution, clientAppDbJdbcTemplate);

						if( s3BucketInfo != null )
						{
							filePaths = s3FileProcessor.downloadFilesFromS3(iLConnectionMapping.getFilePath(), user.getUserId(), deploymentType, true, iLConnectionMapping.isEncryptionRequired(), s3BucketInfo);

							if( filePaths != null )
							{
								String wsJsonFilesPathDir = CommonUtils.createDir(Constants.Temp.getTempFileDir() + "fileMappingWithIL/WsJsonFilesPath/" + UUID.randomUUID() + "/");
								
								for(String filePath :filePaths ){
									File file = new File(filePath);
									FileUtils.copyFileToDirectory(file, new File(wsJsonFilesPathDir));
									file.delete();
								}
								
								Map<String, String> ilContextParams = packageDao.getIlContextParams(iLConnectionMapping.getiLId(), clientAppDbJdbcTemplate);
								clientJobExecutionParameters = packageDao.getClientJobExecutionParams(clientAppDbJdbcTemplate);

								if( clientJobExecutionParameters == null )
								{
									clientJobExecutionParameters = getDefaultJobExecutionParamters();
								}
								if( StringUtils.isNotBlank(defaultSrcTimezone) )
								{
									clientJobExecutionParameters.setSourceTimeZone(defaultSrcTimezone);
								}
								if( userPackage.getTrailingMonths() != 0 )
								{
									clientJobExecutionParameters.setInterval(userPackage.getTrailingMonths());
								}
								Map<String, String> contextParams = preparedObjectProcessor.getContextParams(ilContextParams, databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword, databaseHost, databasePort, clientSchemaName, databaseUserName, databasePassword,
										databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword, datbaseTypeName, iLInfo, user.getClientId(), userPackage.getPackageId(), jobFilesPath, packageExecutorMappingInfo.getExecutionId(), clientJobExecutionParameters,
										iLConnectionMapping.getIsIncrementalUpdate(), packageExecutorMappingInfo.getPackageExecution().getDlId());
								if( contextParams != null )
								{
									executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(getFormattedDateString()).append("\t IL job started...").append(Constants.Config.NEW_LINE).append("id : " + iLInfo.getiL_id() + "; IL Name :" + iLInfo.getiL_name());
									int ilSourceMultiPartCount = filePaths.size();
									log.info("IL job started...id : " + iLInfo.getiL_id() + "; IL name :" + iLInfo.getiL_name() + "; IL job Class :" + iLInfo.getJobName());
									executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(getFormattedDateString()).append("\t Execution started for the partfiles ").append(ilSourceMultiPartCount).append("\n");
									executionMappingInfoComments.append("Context Parameters \n").append(getFormattedDateString()).append("\t ").append(commonProcessor.getPrintableParams(contextParams));

									executionMappingInfoComments.append("\nFILE_SRC=" + wsJsonFilesPathDir);
									String statusString = executionMappingInfoComments.toString();
									packageExecution = commonProcessor.getExecutionStatus(packageExecutorMappingInfo.getExecutionId(), Constants.ExecutionStatus.INPROGRESS, statusString, timeZone);
									packageExecution.setModification(modification);
									scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);
									executionMappingInfoComments = new StringBuilder();

									jobExecutionInfo = etlJobProcessor.runILEtlJobForJson(contextParams, iLInfo, user.getUserId(), userPackage.getPackageId(), wsJsonFilesPathDir, iLConnectionMapping.getFileType(), modification, jobFilesPath, user);

									scheduleDao.updateJobExecutionDetails(jobExecutionInfo, clientAppDbJdbcTemplate);
									executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(jobExecutionInfo.getExecutionMessages());
									executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(getFormattedDateString()).append("\t Execution completed for the partfile ").append(ilSourceMultiPartCount).append(" of ").append(ilSourceMultiPartCount);

									packageExecution = commonProcessor.getExecutionStatus(packageExecutorMappingInfo.getExecutionId(), Constants.ExecutionStatus.INPROGRESS, executionMappingInfoComments.toString(), timeZone);
									packageExecution.setModification(modification);
									scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppDbJdbcTemplate);

									log.info("IL job completed...id : " + iLInfo.getiL_id() + "; IL name :" + iLInfo.getiL_name());
									executionMappingInfoStatus = Constants.ExecutionStatus.COMPLETED;

									packageExecution = commonProcessor.packageExecutionMappingInfo(executionMappingInfoStatus, "Il execution completed.", packageExecutorMappingInfo.getId(), modification, timeZone);
									packageExecution.setModification(modification);
									scheduleDao.updatePackageExecutionMappingInfoStatus(packageExecution, clientAppDbJdbcTemplate);

									message.setCode("SUCCESS");
									message.setText("Il executed successfully.");
									responseMap.put("dlId", iLConnectionMapping.getdLId());
									responseMap.put("iLTableName", iLInfo.getiL_table_name());
									dataResponse.setObject(responseMap);
									// incremental update
									if( iLConnectionMapping.getIsIncrementalUpdate() && StringUtils.isNotBlank(iLConnectionMapping.getIncrementalDateValue()) )
									{
										clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
										scheduleDao.saveOrUpdateIncrementalUpdate(iLConnectionMapping.getIncrementalDateValue(), iLConnectionMapping.getConnectionMappingId(), clientAppDbJdbcTemplate, clientStagingJdbcTemplate);
									}
								}
								else
								{
									message.setCode("ERROR");
									message.setText("Client job execution parameters not found");
								}
							}
							else
							{
								message.setCode("ERROR");
								message.setText("Files not found in S3 for buket id: " + iLConnectionMapping.getS3BucketId());
							}
						}
						else
						{
							message.setCode("ERROR");
							message.setText("S3 bucket info not found for buket id: " + iLConnectionMapping.getS3BucketId());
						}
					}
				}
				else
				{
					message.setCode("ERROR");
					message.setText("IL info not found for il id:" + iLConnectionMapping.getiLId());
				}
			}
			else
			{
				message.setCode("ERROR");
				message.setText("Mapping info not found for execution id:" + packageExecutorMappingInfo.getExecutionId() + " with mapping id:" + packageExecutorMappingInfo.getId());
			}
		}
		catch ( Throwable e )
		{
			log.error("", e);
			message.setCode("ERROR");
			message.setText(executionMappingInfoComments.toString() + parseErrorMessage.getErrorMessageString(e));
		}
		return dataResponse;
	}

	public DataResponse runRJob(User user, Modification modification, String jobFilesPath, ClientJdbcInstance clientJdbcInstance, BusinessModal businessModal, String rScriptPath) throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException, AnvizentCorewsException
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		Map<String, String> rJobContextParams = new HashMap<>();
		File file = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String rJobPath = null;
		try
		{

			ClientDbCredentials clientDbCredentials = userDao.getClientDbDetails(user.getClientId());

			String appDbSchemaName = clientDbCredentials.getClientAppDbSchema();
			String appDbHost = clientDbCredentials.getHostname();
			String appDbPort = clientDbCredentials.getPortnumber();
			String appDbUserName = clientDbCredentials.getClientAppDbUserName();
			String appDbPassword = clientDbCredentials.getClientAppDbPassword();

			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			String clientSchemaName = clientDbDetails.get("clientdb_schema").toString();
			String databaseHost = clientDbDetails.get("region_hostname").toString();
			String databasePort = clientDbDetails.get("region_port").toString();
			String databaseUserName = clientDbDetails.get("clientdb_username").toString();
			String databasePassword = clientDbDetails.get("clientdb_password").toString();

			String clientStagingSchema = clientSchemaName + "_staging";

			String eol = System.getProperty("line.separator");

			String timestamp = formatDateAsTimeStamp(new Date());
			file = new File(jobFilesPath + businessModal.getBusinessProblem() + timestamp + ".csv");

			if( !file.exists() )
			{
				file.createNewFile();
			}
			String rJobLocation = (com.datamodel.anvizent.helper.Constants.Config.AI_JOBS).replaceAll("\\\\", "/") + "/";

			String rJobContextParamsPath = file.getPath();

			String rCommonJobLocation = (com.datamodel.anvizent.helper.Constants.Config.AI_COMMON_JOBS).replaceAll("\\\\", "/");
			String rlog = (com.datamodel.anvizent.helper.Constants.Config.AI_ERROR_LOGS).replaceAll("\\\\", "/");

			List<AIContextParameter> aiContextParams = aIServiceDao.getAiContextParameters(clientAppDbJdbcTemplate);

			if( aiContextParams != null && aiContextParams.size() > 0 )
			{
				for (AIContextParameter aIContextParameter : aiContextParams)
				{
					if( aIContextParameter.getParamName().equals("client") )
					{
						rJobContextParams.put("client", user.getClientId());
					}
					if( aIContextParameter.getParamName().equals("app_db_host") )
					{
						rJobContextParams.put("app_db_host", appDbHost);
					}
					if( aIContextParameter.getParamName().equals("app_db_port") )
					{
						rJobContextParams.put("app_db_port", appDbPort);
					}
					if( aIContextParameter.getParamName().equals("app_db_name") )
					{
						rJobContextParams.put("app_db_name", appDbSchemaName);
					}
					if( aIContextParameter.getParamName().equals("app_db_user_name") )
					{
						rJobContextParams.put("app_db_user_name", appDbUserName);
					}
					if( aIContextParameter.getParamName().equals("app_db_password") )
					{
						rJobContextParams.put("app_db_password", appDbPassword);
					}
					if( aIContextParameter.getParamName().equals("staging_db_host") )
					{
						rJobContextParams.put("staging_db_host", databaseHost);
					}
					if( aIContextParameter.getParamName().equals("staging_db_port") )
					{
						rJobContextParams.put("staging_db_port", databasePort);
					}
					if( aIContextParameter.getParamName().equals("staging_db_name") )
					{
						rJobContextParams.put("staging_db_name", clientStagingSchema);
					}
					if( aIContextParameter.getParamName().equals("staging_db_user_name") )
					{
						rJobContextParams.put("staging_db_user_name", databaseUserName);
					}
					if( aIContextParameter.getParamName().equals("staging_db_password") )
					{
						rJobContextParams.put("staging_db_password", databasePassword);
					}
					if( aIContextParameter.getParamName().equals("db_host") )
					{
						rJobContextParams.put("db_host", databaseHost);
					}
					if( aIContextParameter.getParamName().equals("db_port") )
					{
						rJobContextParams.put("db_port", databasePort);
					}
					if( aIContextParameter.getParamName().equals("db_name") )
					{
						rJobContextParams.put("db_name", clientSchemaName);
					}
					if( aIContextParameter.getParamName().equals("db_user_name") )
					{
						rJobContextParams.put("db_user_name", databaseUserName);
					}
					if( aIContextParameter.getParamName().equals("db_password") )
					{
						rJobContextParams.put("db_password", databasePassword);
					}
					if( aIContextParameter.getParamName().equals("log_file_path") )
					{
						rJobContextParams.put("log_file_path", rlog);
					}
					if( aIContextParameter.getParamName().equals("user_defined_functions_path") )
					{
						rJobContextParams.put("user_defined_functions_path", rCommonJobLocation);
					}
				}
			}

			AIModel aIModel = aIServiceDao.getAiModelInfoByByName(businessModal.getModelName(), clientAppDbJdbcTemplate);

			if( aIModel != null )
			{
				JSONObject contextParmas = new JSONObject(aIModel.getAiContextParameters());
				Iterator<String> keys = contextParmas.keys();
				while (keys.hasNext())
				{
					String key = keys.next();
					rJobContextParams.put(key, contextParmas.getString(key));
				}
			}

			AIJobUploadInfo aIJobUploadInfo = aIServiceDao.getAIUploadedJobFromBid(businessModal.getModelName(), clientAppDbJdbcTemplate);

			if( aIJobUploadInfo != null )
			{
				rJobPath = rJobLocation + businessModal.getModelName() + "/" + aIJobUploadInfo.getJobFileName();
			}

			try ( Writer writer = new FileWriter(file))
			{
				writer.append("PARAM").append(',').append("VALUE").append(eol);
				for (Map.Entry<String, String> entry : rJobContextParams.entrySet())
				{
					writer.append(entry.getKey()).append(',').append(entry.getValue()).append(eol);
				}
			}
			catch ( IOException ex )
			{
				ex.printStackTrace(System.err);
			}

			RJobUtil rJobUtil = new RJobUtil(rScriptPath, rJobPath);
			RJobExecutionMessages rJobExecutionMessages = rJobUtil.runRJob(rJobContextParamsPath);

			if( rJobExecutionMessages.getStatus() == 0 )
			{
				message.setCode("SUCCESS");
				message.setText(rJobExecutionMessages.getInputStreamMsg());
			}
			else
			{
				message.setCode("ERROR");
				message.setText(rJobExecutionMessages.getErrorStreamMsg());
			}
		}
		catch ( Throwable e )
		{
			log.error("", e);
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		finally
		{
			if( file.exists() )
			{
				file.delete();
			}
		}
		return dataResponse;
	}

	public String formatDateAsTimeStamp(Date date)
	{
		final String TIMESTAMP = "yyyyMMddHHmmss";
		if( date == null ) throw new IllegalArgumentException("Date is required.");
		return new SimpleDateFormat(TIMESTAMP).format(date);
	}

	private CustomRequest getCustomRequest(String clientId)
	{
		return new CustomRequest(clientId, clientId, null, null);
	}

	public ClientJobExecutionParameters getDefaultJobExecutionParamters()
	{
		ClientJobExecutionParameters clientJobExecutionParameters = new ClientJobExecutionParameters();
		clientJobExecutionParameters.setSourceTimeZone("UTC");
		clientJobExecutionParameters.setDestTimeZone("UTC");
		clientJobExecutionParameters.setInterval(12);
		clientJobExecutionParameters.setCaseSensitive(false);
		clientJobExecutionParameters.setNullReplaceValues("unknown");
		return clientJobExecutionParameters;
	}

	public DataResponse runDl(User user, Modification modification, String timeZone, String jobFilesPath, ClientJdbcInstance clientJdbcInstance, long executionId, String deploymentType, Package userPackage, Integer dLId)
			throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException, AnvizentCorewsException, ParseException
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		messages.add(message);
		dataResponse.setMessages(messages);
		JobExecutionInfo jobExecutionInfo = null;
		Map<String, Object> responseMap = new HashMap<>();
		StringBuilder executionMappingInfoComments = new StringBuilder();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String startDate = null;
		String endDate = null;
		ClientJobExecutionParameters clientJobExecutionParameters = null;
		boolean loadType = false;
		try
		{
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			String clientSchemaName = clientDbDetails.get("clientdb_schema").toString();
			String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();
			String databaseHost = clientDbDetails.get("region_hostname").toString();
			String databasePort = clientDbDetails.get("region_port").toString();
			String databaseUserName = clientDbDetails.get("clientdb_username").toString();
			String databasePassword = clientDbDetails.get("clientdb_password").toString();

			clientJobExecutionParameters = packageDao.getClientJobExecutionParams(clientAppDbJdbcTemplate);

			if( clientJobExecutionParameters == null )
			{
				clientJobExecutionParameters = getDefaultJobExecutionParamters();
			}

			loadType = packageDao.getDlLoadType(executionId, clientAppDbJdbcTemplate);

			DLInfo dLInfo = packageDao.getDLByIdWithJobName(dLId, user.getClientId(), clientAppDbJdbcTemplate);
			if( userPackage.getTrailingMonths() != 0 )
			{
				clientJobExecutionParameters.setInterval(userPackage.getTrailingMonths());
			}
			if( dLInfo != null )
			{

				if( dLInfo.getTrailingMonths() != 0 )
				{
					clientJobExecutionParameters.setInterval(dLInfo.getTrailingMonths());
				}

				PackageExecution packageExecution = new PackageExecution();
				packageExecution.setExecutionId(executionId);
				packageExecution.setClientId(user.getClientId());
				if( dLInfo.getJobExecutionType().equals("E") )
				{

					String messageString = "DL %s execution started.";
					messageString = String.format(messageString, dLInfo.getdL_name());
					metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, messageString, packageExecution, getCustomRequest(packageExecution.getClientId()));

					JSONObject externalDataJSON = commonProcessor.getExternalDataJSON(userPackage.getPackageId(), packageExecution.getScheduleId(), executionId, user.getClientId(), dLId, 0);

					try
					{
						eltProcessor.processEltJob(dLInfo.getJobTagId(), dLInfo.getLoadParameterId(), dLInfo.getMasterParameterId(), packageExecution, externalDataJSON, deploymentType, modification, clientJdbcInstance);
						messageString = "DL %s execution Completed.";
						messageString = String.format(messageString, dLInfo.getdL_name());
						metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, messageString, packageExecution, getCustomRequest(packageExecution.getClientId()));
						Map<String, String> dLDataMap = new HashMap<>();
						dLDataMap.put("dlTableName", dLInfo.getdL_table_name());
						dLDataMap.put("executionMappingInfoComments", dLInfo.getdL_table_name());
						message.setCode("SUCCESS");
						message.setText("DL (ELT) job execution completed.");
						dataResponse.setObject(dLDataMap);
					}
					catch ( Exception e )
					{
						message.setCode("ERROR");
						message.setText(e.getMessage());
						messageString = "DL %s execution failed.";
						messageString = String.format(messageString, dLInfo.getdL_name());
						metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, messageString, packageExecution, getCustomRequest(packageExecution.getClientId()));
					}

					return dataResponse;
				}

				Map<String, String> dlParamsVals = preparedObjectProcessor.getDlParamsVals(databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword, databaseHost, databasePort, clientSchemaName, databaseUserName, databasePassword, databaseHost, databasePort,
						clientStagingSchema, databaseUserName, databasePassword, user.getClientId(), userPackage, jobFilesPath, executionId, clientJobExecutionParameters, loadType, dLId);

				if( dlParamsVals != null )
				{
					// run DL Jobs

					Map<String, String> dlContextParams = packageDao.getDlContextParams(dLInfo.getdL_id(), clientAppDbJdbcTemplate);
					if( dlContextParams != null )
					{

						String dlTableName = dLInfo.getdL_table_name();
						dlParamsVals.put("tgt_table", dlTableName);
						dlParamsVals.put("job_name", dLInfo.getdL_table_name());
						String jobType = "DL";
						dlParamsVals.put("job_type", jobType);
						commonProcessor.parseContextParams(dlContextParams, dlParamsVals);
						startDate = getFormattedDateString();
						executionMappingInfoComments.append(Constants.Config.NEW_LINE).append("DL job started...").append("id : " + dLInfo.getdL_id() + "; DL name :" + dLInfo.getdL_name() + "; DL job name :" + dLInfo.getJobName()).append(" date and time: ").append(startDate);
						executionMappingInfoComments.append("\nContext Parameters \n").append(getFormattedDateString()).append("\t ").append(commonProcessor.getPrintableParams(dlContextParams));
						metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, executionMappingInfoComments.toString(), packageExecution, getCustomRequest(packageExecution.getClientId()));

						executionMappingInfoComments = new StringBuilder();
						jobExecutionInfo = etlJobProcessor.runDlEtlJob(dlContextParams, dlParamsVals, dLInfo, user.getUserId(), userPackage, modification, jobFilesPath, user);
						endDate = getFormattedDateString();

						updatePackageExecutionTargetTableInfo(dLInfo.getdL_table_name(), startDate, endDate, jobExecutionInfo, packageDao, executionId, modification, clientAppDbJdbcTemplate);
						scheduleDao.updateJobExecutionDetails(jobExecutionInfo, clientAppDbJdbcTemplate);
						executionMappingInfoComments.append(Constants.Config.NEW_LINE).append("DL job completed...").append("id : " + dLInfo.getdL_id() + "; DL name :" + dLInfo.getdL_name()).append(" date and time: ").append(startDate);
						;
						executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(jobExecutionInfo.getExecutionMessages());

						List<DLInfo> anlyDlList = packageDao.getAnalyticalDLs(dLId, clientAppDbJdbcTemplate);
						for (DLInfo anly_DL : anlyDlList)
						{

							startDate = getFormattedDateString();
							executionMappingInfoComments.append(Constants.Config.NEW_LINE).append("Started date and time: ").append(startDate).append("\t Analytical DL job started...").append(Constants.Config.NEW_LINE).append("id : " + anly_DL.getdL_id() + "; DL Name :" + anly_DL.getdL_name());
							jobExecutionInfo = etlJobProcessor.runAnalyticalDlEtlJob(dlContextParams, anly_DL, user.getUserId(), userPackage, modification, jobFilesPath, user);
							endDate = getFormattedDateString();
							updatePackageExecutionTargetTableInfo(anly_DL.getdL_table_name(), startDate, endDate, jobExecutionInfo, packageDao, executionId, modification, clientAppDbJdbcTemplate);

							scheduleDao.updateJobExecutionDetails(jobExecutionInfo, clientAppDbJdbcTemplate);
							executionMappingInfoComments.append(Constants.Config.NEW_LINE).append(jobExecutionInfo.getExecutionMessages());
							executionMappingInfoComments.append(Constants.Config.NEW_LINE).append("Completed date and time: ").append(endDate).append("\t Analytical DL job completed...").append(Constants.Config.NEW_LINE).append("id : " + anly_DL.getdL_id() + "; DL Name :" + anly_DL.getdL_name());
						}
						PackageExecution packageExecution1 = commonProcessor.getExecutionStatus(executionId, Constants.ExecutionStatus.INPROGRESS, executionMappingInfoComments.toString(), timeZone);
						packageExecution1.setModification(modification);
						scheduleDao.updatePackageExecutionStatusInfo(packageExecution1, clientAppDbJdbcTemplate);

						message.setCode("SUCCESS");
						message.setText("Dl executed successfully.");
						responseMap.put("dlTableName", dLInfo.getdL_table_name());
						responseMap.put("executionMappingInfoComments", executionMappingInfoComments.toString());
						dataResponse.setObject(responseMap);
					}
					else
					{
						message.setCode("ERROR");
						message.setText("Client job execution parameters not found");
					}
				}
				else
				{
					message.setCode("ERROR");
					message.setText("DL param values not found for dl id:" + dLId);
				}
			}
			else
			{
				message.setCode("ERROR");
				message.setText("DL info not found for dl id: " + dLId);

			}
		}
		catch ( Throwable e )
		{
			message.setCode("ERROR");
			message.setText(executionMappingInfoComments.toString() + parseErrorMessage.getErrorMessageString(e));
		}

		return dataResponse;
	}

	public int updatePackageExecutionTargetTableInfo(String targetTableName, String startDate, String endDate, JobExecutionInfo jobExecutionInfo, PackageDao packageDao, long executionId, Modification modification, JdbcTemplate clientAppDbJdbcTemplate)
	{
		// update package execution target table info
		PackageExecution tagetTableExecution = new PackageExecution();
		String jobExecutionStatus = jobExecutionInfo.getStatusCode() == 0 ? "COMPLETED" : "ERROR";
		tagetTableExecution.setExecutionId(executionId);
		tagetTableExecution.setExecutionStatus(jobExecutionStatus);
		tagetTableExecution.setExecutionComments(jobExecutionInfo.getExecutionMessages());
		tagetTableExecution.setExecutionStartDate(startDate);
		tagetTableExecution.setLastExecutedDate(endDate);
		tagetTableExecution.setTagetTableName(targetTableName);
		tagetTableExecution.setModification(modification);
		return packageDao.updatePackageExecutionTargetTableInfo(tagetTableExecution, clientAppDbJdbcTemplate);
	}

	public String getFormattedDateString()
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		return sdf.format(date).toString();
	}

	public DataResponse runCustomDataSetsInStandardPackage(Set<Integer> ddLTableSet, User user, JdbcTemplate jdbcTemplate, JdbcTemplate clientAppDbJdbcTemplate, Modification modification, String runType) throws SQLException
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<String> ddlsTable = new ArrayList<>();
		try
		{
			List<DDLayout> dDLayoutList = packageDao.getDDlayoutListByIds(user.getClientId(), ddLTableSet, user.getUserId(), clientAppDbJdbcTemplate);
			if( dDLayoutList != null && dDLayoutList.size() > 0 )
			{
				for (DDLayout ddlayout : dDLayoutList)
				{
					boolean tableExist = fileDao.isTableExists(user.getUserId(), null, ddlayout.getTableName(), jdbcTemplate);
					if( tableExist )
					{
						int count = 0;
						String errorMessage = "";
						try
						{
							count = commonProcessor.runDDlayoutTable(ddlayout, jdbcTemplate);
							ddlsTable.add(ddlayout.getTableName());
						}
						catch ( BadSqlGrammarException | SQLException e )
						{
							errorMessage = MinidwServiceUtil.getErrorMessageString(e);
						}
						ddlayout.setRunType(runType);
						ddlayout.setModification(modification);
						ddlayout.setInsertedCount(count);
						ddlayout.setErrorMessage(errorMessage);
						packageDao.updateDDlayoutTableAuditLogs(user.getUserId(), ddlayout, clientAppDbJdbcTemplate);
					}
				}
				message.setCode("SUCCESS");
				message.setText("DDlayout List successfully updated.");
			}
			else
			{
				log.error("DDL's List is empty and size is :" + dDLayoutList.size());
				message.setCode("ERROR");
				message.setText("DDL's List is empty and size is :" + dDLayoutList.size());
			}
		}
		catch ( Throwable e )
		{
			message.setCode("ERROR");
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setObject(ddlsTable);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	public DataResponse runCustomDataSetsInCustomPacakge(PackageDao packageDao, List<String> tableList, User user, JdbcTemplate jdbcTemplate, JdbcTemplate clientAppdbJdbcTemplate, FileDao fileDao, Modification modification, String runType) throws SQLException
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<String> ddlsTable = new ArrayList<>();
		try
		{
			message.setCode("SUCCESS");
			List<DDLayout> dDLayoutList = packageDao.getDDlayoutList(user.getClientId(), tableList, user.getUserId(), clientAppdbJdbcTemplate);
			if( dDLayoutList != null && dDLayoutList.size() > 0 )
			{
				for (DDLayout ddlayout : dDLayoutList)
				{
					boolean tableExist = fileDao.isTableExists(user.getUserId(), null, ddlayout.getTableName(), jdbcTemplate);
					if( tableExist )
					{
						int count = 0;
						String errorMessage = "";
						try
						{
							count = commonProcessor.runDDlayoutTable(ddlayout, jdbcTemplate);
							ddlsTable.add(ddlayout.getTableName());
						}
						catch ( BadSqlGrammarException | SQLException e )
						{
							errorMessage = MinidwServiceUtil.getErrorMessageString(e);
						}
						ddlayout.setRunType(runType);
						ddlayout.setModification(modification);
						ddlayout.setInsertedCount(count);
						ddlayout.setErrorMessage(errorMessage);
						packageDao.updateDDlayoutTableAuditLogs(user.getUserId(), ddlayout, clientAppdbJdbcTemplate);
					}
				}

				message.setText("DDlayout List successfully updated.");
			}
			else
			{
				log.error("DDL's List is empty and size is :" + dDLayoutList.size());
				message.setText("DDL's List is empty and size is :" + dDLayoutList.size()+".");
			}
		}
		catch ( Throwable e )
		{
			message.setCode("ERROR");
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setObject(ddlsTable);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	public DataResponse runHistoricalLoad(User user, String jobFilesPath, ClientJdbcInstance clientJdbcInstance, ScheduleDao scheduleDao, PackageDao packageDao, HistoricalLoadForm historicalLoadForm, String deploymentType, S3BucketInfo s3BucketInfo, ILInfo iLInfo, String filePath,
			FileSettings fileSettings) throws TalendJobNotFoundException, CSVConversionException, InterruptedException, IOException, AnvizentCorewsException, ParseException
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		List<String> filePaths = null;
		JobExecutionInfo jobExecutionInfo = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		StringBuilder executionComments = new StringBuilder();
		ClientJobExecutionParameters clientJobExecutionParameters = null;
		try
		{

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			modification.setModifiedBy(user.getUserName());
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);

			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			Map<String, Object> clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			String clientSchemaName = clientDbDetails.get("clientdb_schema").toString();
			String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();
			String databaseHost = clientDbDetails.get("region_hostname").toString();
			String databasePort = clientDbDetails.get("region_port").toString();
			String databaseUserName = clientDbDetails.get("clientdb_username").toString();
			String databasePassword = clientDbDetails.get("clientdb_password").toString();

			String datbaseTypeName = "D" + historicalLoadForm.getIlConnection().getConnectionId();
			if( StringUtils.isNotBlank(historicalLoadForm.getDataSourceName()) )
			{
				datbaseTypeName = historicalLoadForm.getDataSourceName();
			}

			filePaths = s3FileProcessor.downloadFilesFromS3(filePath, user.getUserId(), deploymentType, historicalLoadForm.isMultipartEnabled(), fileSettings.getFileEncryption(), s3BucketInfo);
			if( filePaths != null )
			{
				Map<String, String> ilContextParams = packageDao.getIlContextParams(iLInfo.getiL_id(), clientAppDbJdbcTemplate);
				clientJobExecutionParameters = packageDao.getClientJobExecutionParams(clientAppDbJdbcTemplate);

				if( clientJobExecutionParameters == null )
				{
					clientJobExecutionParameters = getDefaultJobExecutionParamters();
				}

				Map<String, String> contextParams = MinidwServiceUtil.getContextParams(ilContextParams, databaseHost, databasePort, clientStagingSchema, databaseUserName, databasePassword, databaseHost, databasePort, clientSchemaName, databaseUserName, databasePassword, databaseHost, databasePort,
						clientStagingSchema, databaseUserName, databasePassword, datbaseTypeName, iLInfo, user.getUserId(), iLInfo.getiL_id(), jobFilesPath, historicalLoadForm.getId(), clientJobExecutionParameters, false);
				if( contextParams != null )
				{
					executionComments.append(Constants.Config.NEW_LINE).append("Historical IL job started...").append(Constants.Config.NEW_LINE).append("id : " + iLInfo.getiL_id() + "; IL name :" + iLInfo.getiL_name()+".");
					int ilSourceIterationCount = 1;
					int ilSourceMultiPartCount = filePaths.size();
					executionComments.append(Constants.Config.NEW_LINE).append("Execution started for the partfiles ").append(ilSourceMultiPartCount).append(".");
					for (String path : filePaths)
					{
						jobExecutionInfo = etlJobProcessor.runILEtlJob(contextParams, iLInfo, user.getUserId(), historicalLoadForm.getId(), path, modification, jobFilesPath, user);
						executionComments.append(Constants.Config.NEW_LINE).append(jobExecutionInfo.getExecutionMessages());
						executionComments.append(Constants.Config.NEW_LINE).append("Execution completed for the partfile ").append(ilSourceIterationCount).append(" of ").append(ilSourceMultiPartCount).append(".");
						ilSourceIterationCount++;
					}
					if( jobFilesPath != null )
					{
						String clientMatch = contextParams.get("CLIENT_ID");
						String packageMatch = contextParams.get("PACKAGE_ID");
						String match = clientMatch + "_" + packageMatch + "_.*";
						etlJobProcessor.deleteFilesFromDirWithMatching(jobFilesPath, match);
					}
				}
				message.setCode("SUCCESS");
				message.setText(executionComments.toString());
			}
			else
			{
				message.setCode("ERROR");
				message.setText("files not found in s3 for buket id: " + s3BucketInfo.getId()+".");
			}

		}
		catch ( Throwable e )
		{
			message.setCode("ERROR");
			e.printStackTrace();
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	public String execureCurrencyLoad(JdbcTemplate clientAppDbJdbcTemplate, ClientJdbcInstance clientJdbcInstance, CurrencyIntegration currencyIntegration)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowDate = sdf.format(new Date());

		String appDbUsername = clientDbCredentials.getClientDbUsername();
		String appDbPassword = clientDbCredentials.getClientDbPassword();
		String databaseHost = clientDbCredentials.getHostname();
		String databasePort = clientDbCredentials.getPortnumber();
		String schemaName = clientDbCredentials.getClientDbSchema();
		Map<String, String> dlParamsVals = new HashMap<>();

		dlParamsVals.put("SRC_HOST", databaseHost);
		dlParamsVals.put("SRC_PORT", databasePort);
		dlParamsVals.put("SRC_DBNAME", schemaName);
		dlParamsVals.put("SRC_UN", appDbUsername);
		dlParamsVals.put("SRC_PW", appDbPassword);

		dlParamsVals.put("TGT_HOST", databaseHost);
		dlParamsVals.put("TGT_PORT", databasePort);
		dlParamsVals.put("TGT_DBNAME", schemaName);
		dlParamsVals.put("TGT_UN", appDbUsername);
		dlParamsVals.put("TGT_PW", appDbPassword);

		String pathName = Constants.Temp.getTempFileDir() + "/" + "JobFiles/";
		File file = new File(pathName);
		if( !file.exists() )
		{
			file.mkdirs();
		}
		dlParamsVals.put("FILE_PATH", pathName);

		dlParamsVals.put("ETL_JOBS", "ETL_Jobs");
		dlParamsVals.put("ETL_CONTROL_JOB", "ETL_CNTRL");
		dlParamsVals.put("ETL_JOB_ERROR_LOG", "ETL_JOB_ERROR_LOG");
		dlParamsVals.put("ETL_JOB_LOAD_SMRY", "ETL_JOB_LOAD_SMRY");

		dlParamsVals.put("JOB_NAME", "IL_Currency_Rate");
		dlParamsVals.put("JOB_DESCRIPTION", "unknown");
		dlParamsVals.put("JOB_TYPE", "IL_Currency_Rate");
		dlParamsVals.put("JOB_ORDER_SEQ", "1");

		dlParamsVals.put("TGT", "IL_Currency_Rate");
		dlParamsVals.put("JOB_STARTDATETIME", nowDate);

		dlParamsVals.put("BULK_PATH", pathName);

		dlParamsVals.put("CLIENT_ID", "-1");
		dlParamsVals.put("PACKAGE_ID", "minidw");
		dlParamsVals.put("TRUNCATE_FLAG", "N");
		dlParamsVals.put("DATASOURCENAME", "api");

		dlParamsVals.put("START_PERIOD", currencyIntegration.getStartDate() + " 00:00:00");
		dlParamsVals.put("END_PERIOD", currencyIntegration.getEndDate() + " 00:00:00");
		dlParamsVals.put("source", currencyIntegration.getSource());
		dlParamsVals.put("API_URL", currencyIntegration.getApiUrl());
		dlParamsVals.put("access_key", currencyIntegration.getAccessKey());

		JSONObject currencyObj = new JSONObject();
		JSONArray clientcurrencydtls = new JSONArray();
		ArrayList<String> clientIdList = new ArrayList<String>();

		List<ClientCurrencyMapping> clientCurrencyMappingList = etlAdminDao.getClientCurrencyMappingWithCurrencyIds(clientAppDbJdbcTemplate);

		for (ClientCurrencyMapping clientCurrencyMapping : clientCurrencyMappingList)
		{

			if( (!clientCurrencyMapping.getIsActive()) || (!clientCurrencyMapping.getCurrencyType().equals("minidw")) )
			{
				continue;
			}
			clientIdList.add(clientCurrencyMapping.getClientId());
			try
			{
				ClientDbCredentials clientDbCredentials = userDao.getClientDbDetails(clientCurrencyMapping.getClientId());
				List<String> curreniesList = new ArrayList<>();

				MinidwServiceUtil.getConcatinatedString(curreniesList, clientCurrencyMapping.getCurrencyName(), "'");
				MinidwServiceUtil.getConcatinatedString(curreniesList, clientCurrencyMapping.getAccountingCurrencyCode(), "'");
				MinidwServiceUtil.getConcatinatedString(curreniesList, clientCurrencyMapping.getBasecurrencyCode(), "'");

				String currencies = String.join(",", curreniesList.toArray(new String[] {}));

				String clientSchemaName = clientDbCredentials.getClientDbSchema();
				databaseHost = clientDbCredentials.getHostname();
				databasePort = clientDbCredentials.getPortnumber();
				String databaseUserName = clientDbCredentials.getClientDbUsername();
				String databasePassword = clientDbCredentials.getClientDbPassword();

				JSONObject clientcurrency = new JSONObject();

				clientcurrency.put("hostname", databaseHost);
				clientcurrency.put("port", databasePort);
				clientcurrency.put("username", databaseUserName);
				clientcurrency.put("password", databasePassword);
				clientcurrency.put("schemaname", clientSchemaName);
				clientcurrency.put("currencies", currencies);
				clientcurrency.put("CLIENT_ID", clientCurrencyMapping.getClientId());
				clientcurrencydtls.put(clientcurrency);

			}
			catch ( Exception e )
			{
				throw new TalendJobNotFoundException("Job execution failed." + e.getMessage());
			}

		}

		if( clientcurrencydtls.length() > 0 )
		{
			currencyObj.put("clientcurrencydtl", clientcurrencydtls);
			if( SystemUtils.IS_OS_WINDOWS )
			{
				dlParamsVals.put("clientcurrencydtl", StringUtils.replace(currencyObj.toString(), "\"", "\\\""));
			}
			else
			{
				dlParamsVals.put("clientcurrencydtl", currencyObj.toString());
			}
		}
		else
		{
			throw new TalendJobNotFoundException("Job execution failed", "Clients are not mapped to default currency.");
		}

		dlParamsVals.put("DateFormat",
				"yyyy-MM-dd'T'HH:mm:ss'Z'@#dd,MM,yyyy' 'HH:mm:ss@#yyyy-MM-dd@#yyyy-MM-dd'T'HH:mm.ss'Z'@#yyyy-MM-dd'T'HH:mm:ss@#yyyy-MM-dd' 'HH:mm:ss@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy/MM/dd' 'HH:mm:ss@#MM/dd/yyyy' 'HH:mm:ss@#MM-dd-yyyy' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss'Z'@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy-MM-dd' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssXXX@#yyyy-dd-MM' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssSSS@#yyyy-dd-MM' 'HH:mm:ssSSS@#MM/dd/yyyy' 'HH:MM' AM'");

		String jobName = currencyIntegration.getJobName();
		String dependencyJarsCommaSeperated = currencyIntegration.getJobfile_names();

		ETLjobExecutionMessages etlJjobExecutionMessages = new ETLjobExecutionMessages();
		String executionmessages = null;
		int ilStatus = 1;
		try
		{

			etlJjobExecutionMessages = etlJobProcessor.runETLjar(jobName, dependencyJarsCommaSeperated, dlParamsVals);
			commonProcessor.moveErrorLogFile(dlParamsVals.get("CLIENT_ID").toString(), dlParamsVals.get("PACKAGE_ID").toString(), dlParamsVals.get("JOB_NAME").toString(), dlParamsVals.get("JOB_STARTDATETIME").toString(), dlParamsVals.get("ETL_JOB_ERROR_LOG").toString(),
					dlParamsVals.get("FILE_PATH").toString());

			ilStatus = etlJjobExecutionMessages.getStatus();
			executionmessages = etlJjobExecutionMessages.getErrorStreamMsg() + etlJjobExecutionMessages.getInputStreamMsg();

		}
		catch ( InterruptedException | IOException e )
		{
			// TODO Auto-generated catch block
			executionmessages = "IL <b> IL_Currency_Rate</b> failed. ";
		}

		JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
		jobExecutionInfo.setStatusCode(ilStatus);
		jobExecutionInfo.setJobName("IL_Currency_Rate");
		jobExecutionInfo.setJobClass(currencyIntegration.getJobName());
		jobExecutionInfo.setDependencyJars(currencyIntegration.getJobfile_names());
		jobExecutionInfo.setJobId("Scheduler_IL_Currency_Rate_" + CommonUtils.currentDateTime());
		jobExecutionInfo.setExecutionMessages(executionmessages);
		jobExecutionInfo.setS3Path(clientIdList.toString());
		Modification modification = new Modification(new Date());
		modification.setCreatedBy("Schduler");
		jobExecutionInfo.setModification(modification);

		// scheduleService.updateJobExecutionDetails(jobExecutionInfo, null);
		return executionmessages;
	}

}
