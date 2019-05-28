package com.datamodel.anvizent.helper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.anvizent.jsch.util.ProcessExecutor;
import com.anvizent.jsch.util.bean.ExecResult;
import com.anvizent.jsch.util.exception.InvalidArgumentException;
import com.anvizent.jsch.util.exception.InvalidEnvironmentVariableException;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.anvizent.minidw.service.utils.processor.ParseErrorMessage;
import com.anvizent.minidw.service.utils.processor.S3FileProcessor;
import com.datamodel.anvizent.common.exception.EltJobExecutionException;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.dao.ELTJobTagsDao;
import com.datamodel.anvizent.service.dao.ELTLoadParametersDao;
import com.datamodel.anvizent.service.dao.ELTMasterConfigDao;
import com.datamodel.anvizent.service.model.ClientDbCredentials;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;
import com.datamodel.anvizent.service.model.EltJobTagInfo;
import com.datamodel.anvizent.service.model.EltLoadParameters;
import com.datamodel.anvizent.service.model.EltMasterConfiguration;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;

@Service
public class EltProcessor {
	protected static final Log log = LogFactory.getLog(EltProcessor.class);
	
	private @Value("${elt.job.spark.submit.initiate.end.point:}") String INITIATE_ENDPOINT;
	private @Value("${elt.job.spark.submit.wait.for.end.point:}") String WAIT_FOR_ENDPOINT;
	private @Value("${elt.job.spark.submit.status.end.point:}") String STATUS_ENDPOINT;
	private @Value("${elt.job.spark.submit.initiate.or.wait.for:initiate}") String INITIATE_OR_WAIT_FOR;
	private @Value("${elt.job.spark.submit.initiate.status.refresh.for.time.interval:15}") int TIME_INTERVEL_STATUS_REFRESH;
	private @Value("${elt.csv.path.ssh.host:}") String sshHost;
	private @Value("${elt.csv.path.ssh.port:0}") int sshPort;
	private @Value("${elt.csv.path.ssh.username:}") String sshUserName;
	private @Value("${elt.csv.path.ssh.password:}") String sshpassword;
	private @Value("${elt.job.spark.submit.ui.port:0}") int uiport;
	private @Value("${elt.logs:}") String eltLogsPath;
	
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	S3FileProcessor s3FileProcessor;
	@Autowired
	ParseErrorMessage parseErrorMessage;
	@Autowired
	MetaDataFetch metaDataFetch;
	@Autowired
	ELTJobTagsDao eltDao;
	@Autowired
	ELTMasterConfigDao eltMasterConfigDao;
	@Autowired
	ELTLoadParametersDao eltLoadParametersDao;
	@Autowired
	ClientDbCredentials clientDbCredentials;
	
	
	public void processEltJob(long eltJobTagId, long loadParamId, PackageExecution packageExecution,JSONObject externalDataJson,String deploymentType,Modification modification,ClientJdbcInstance clientJdbcInstance) throws Exception {
		processEltJob(eltJobTagId, loadParamId, 0, packageExecution,externalDataJson,deploymentType,modification,clientJdbcInstance);
	}
	
	public void processEltJob(long eltJobTagId, long loadParamId, long masterId,  PackageExecution packageExecution,JSONObject externalDataJson,String deploymentType,Modification modification,ClientJdbcInstance clientJdbcInstance)throws Exception {
		processEltJob(eltJobTagId, loadParamId, masterId, packageExecution, false, false, null, null,null, externalDataJson,deploymentType,modification,clientJdbcInstance);
	}
	
	public Map<String,Object> processEltJob(long eltJobTagId, long loadParamId, long masterId, PackageExecution packageExecution,boolean isFileProcessingRequired,boolean isMultipartEnable,String filePath,
            S3BucketInfo s3BucketInfo, String datbaseTypeName,JSONObject externalDataJson,String deploymentType,Modification modification, ClientJdbcInstance clientJdbcInstance) throws Exception{
		CustomRequest customRequest = getCustomRequest(packageExecution.getClientId());
		Map<String,Object> executionResponsemap = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			EltJobTagInfo eltJobTagInfo = eltDao.getEltJobTagInfoById(eltJobTagId, null);
			validate(eltJobTagInfo, eltJobTagId);
			EltMasterConfiguration eltMasterConfiguration = null;
			if (masterId == 0) {
				eltMasterConfiguration = eltMasterConfigDao.fetchDefaultMasterConfig(null);
			} else {
				eltMasterConfiguration = eltMasterConfigDao.fetchMasterConfigById(masterId, null);
				eltMasterConfiguration.setEnvironmentVariables(eltMasterConfigDao.fetchMasterConfigEnvironmentVariablesByMappingId(masterId, null));
			}
			
			validate(eltMasterConfiguration);
			
			HashMap<String, String> envVariables = getEnvironmentVariables(eltMasterConfiguration.getEnvironmentVariables());
			String basePath = eltMasterConfiguration.getSparkJobPath();
			
			if (Constants.SparkSubmitType.CLUSTER_MODE_SUBMIT_TYPE.contains(eltMasterConfiguration.getSparkSubmitMode()) && eltMasterConfiguration.getDeployMode().equals("cluster")) {
				JSONObject jobExecutionJsonTemplate = getJobExecutionJsonTemplate();
				formPrimaryJsonObject(jobExecutionJsonTemplate, eltMasterConfiguration, loadParamId,externalDataJson);
				executionResponsemap = executeRemoteJob(packageExecution, eltJobTagInfo, jobExecutionJsonTemplate, customRequest, envVariables, isFileProcessingRequired, isMultipartEnable, filePath, s3BucketInfo, datbaseTypeName,deploymentType);
				
				if(executionResponsemap != null) {
					Object jobId = ((Map) executionResponsemap.get("data")).get("id");
					clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
					eltMasterConfigDao.saveEltInitiatedInfo(packageExecution.getExecutionId(),packageExecution.getPackageId(),packageExecution.getScheduleId(),packageExecution.getDlId(),packageExecution.getIlId(),jobId,executionResponsemap.toString(),modification,clientAppDbJdbcTemplate);
				}
			} else {
				List<String> primaryCommandPart = formPrimaryCommand(eltMasterConfiguration, loadParamId);
				executeJobs(basePath, packageExecution, eltJobTagInfo, primaryCommandPart, customRequest,envVariables,isFileProcessingRequired,isMultipartEnable,filePath,s3BucketInfo, datbaseTypeName);
			}
			

		} catch (Exception e) {
			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, e.getMessage(), packageExecution,
					customRequest);
			throw e;
		}
		return executionResponsemap;
	}
	private HashMap<String, String> getEnvironmentVariables(List<ELTConfigTags> environmentVariables) {
		HashMap<String, String> envVariables = null;
		if (environmentVariables != null && environmentVariables.size() > 0 ) {
			envVariables = new HashMap<>();
			for (ELTConfigTags variable:environmentVariables) {
				envVariables.put(variable.getKey(),variable.getValue());
			}
		}
		return envVariables;
	}

	private void executeJobs(String basePath, PackageExecution packageExecution, EltJobTagInfo eltJobTagInfo,
			List<String> primaryCommandPart, CustomRequest customRequest, HashMap<String, String> envVariables, boolean isFileProcessingRequired, boolean isMultipartEnable, 
			String filePath, S3BucketInfo s3BucketInfo, String datbaseTypeName) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Long globalConfigId = eltJobTagInfo.getGlobalValues().getTagId();
		globalConfigId = (globalConfigId == null ? 0 : globalConfigId);
		String message = "%d jobs found under %s job tag id";
		message = String.format(message, eltJobTagInfo.getJobsList().size(), eltJobTagInfo.getTagName());
		metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
				customRequest);
		for (EltJobInfo eltJobInfo : eltJobTagInfo.getJobsList()) {
			if (!eltJobInfo.getActiveStatus()) {
				continue;
			}
			
			String appName = eltJobInfo.getJobName() + "_" + UUID.randomUUID(); 
			List<String> jobCommand = new ArrayList<>(primaryCommandPart);
			jobCommand.addAll(formSecondayCommand(eltJobInfo, globalConfigId, appName,isFileProcessingRequired, isMultipartEnable, filePath, s3BucketInfo, datbaseTypeName));
			
			StringBuilder sb = new StringBuilder();
			sb.append("Job %s execution started with the below command \n %s");
			if(envVariables != null){
				sb.append("\n and environment variables: %s");
			}
			message = sb.toString();
			message = String.format(message, eltJobInfo.getJobName(),
					String.join(" ", jobCommand.toArray(new String[] {})), envVariables != null ? envVariables : "");
			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);
			ProcessExecutor processExecutor = new ProcessExecutor();
			try {
				ExecResult execResult = processExecutor.executeCommand(jobCommand, new File(basePath), envVariables);
				message = "Console: %s\nError Console: %s\nExit Code: %d";
				message = String.format(message, execResult.getConsole(), execResult.getErrorConsole(), execResult.getExitCode());
				metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
						customRequest);
			} catch (InvalidArgumentException | InvalidEnvironmentVariableException | IOException
					| InterruptedException e) {
				throw new EltJobExecutionException(e);
			}
			message = "Job %s execution Completed";
			message = String.format(message, eltJobInfo.getJobName());
			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);
		}
		message = "jobs tag execution completed for %s id";
		message = String.format(message, eltJobTagInfo.getTagName());
		metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
				customRequest);

	}
	@SuppressWarnings("rawtypes")
	private Map<String,Object> executeRemoteJob(PackageExecution packageExecution, EltJobTagInfo eltJobTagInfo,
			JSONObject primaryCommandPart, CustomRequest customRequest, HashMap<String, String> envVariables, boolean isFileProcessingRequired, boolean isMultipartEnable, 
			String filePath, S3BucketInfo s3BucketInfo, String datbaseTypeName,String deploymentType) {
		Long globalConfigId = eltJobTagInfo.getGlobalValues().getTagId();
		globalConfigId = (globalConfigId == null ? 0 : globalConfigId);
		String message = "%d jobs found under %s job tag id";
		message = String.format(message, eltJobTagInfo.getJobsList().size(), eltJobTagInfo.getTagName());
		metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
				customRequest);
		Map executionResponse = null;
		for (EltJobInfo eltJobInfo : eltJobTagInfo.getJobsList()) {
			if (!eltJobInfo.getActiveStatus()) {
				continue;
			}
			
			String appName = eltJobInfo.getJobName() + "_" + UUID.randomUUID(); 
			JSONObject jobCommand = new JSONObject(primaryCommandPart.toString());
			formSecondayJsonObject(jobCommand, eltJobInfo, globalConfigId, appName,isFileProcessingRequired, isMultipartEnable, filePath, s3BucketInfo, datbaseTypeName,deploymentType);
			
			StringBuilder sb = new StringBuilder();
			sb.append("Job %s execution started with the below command \n %s");
			if(envVariables != null){
				sb.append("\n and environment variables: %s");
			}
			message = sb.toString();
			message = String.format(message, eltJobInfo.getJobName(),
					jobCommand, envVariables != null ? envVariables : "");
			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);
			try {
				String endPoint = "";
				Map statusResponseObject = null;
				if (INITIATE_OR_WAIT_FOR.equals("initiate")) {
					endPoint = INITIATE_ENDPOINT;
					log.info("initiated end point " + INITIATE_ENDPOINT);
					executionResponse = restTemplate.postForObject(INITIATE_ENDPOINT, jobCommand.toMap(), Map.class);
					log.info("initiate end point resonse  " + executionResponse);
					if ( executionResponse.get("status").equals("ACCEPTED") ) {
						Object driverId = ((Map) executionResponse.get("data")).get("driverId");
						while (true) {
							JSONObject masterObject = jobCommand.getJSONObject(ELTCommandConstants.MASTER_OBJECT);
							  HttpHeaders headers = new HttpHeaders();
							   headers.setContentType(MediaType.APPLICATION_JSON);
							   HttpEntity<Object> entity = new HttpEntity<Object>(masterObject.toString(), headers);
							statusResponseObject =  (Map) restTemplate.postForObject(STATUS_ENDPOINT+"?submissionId="+driverId.toString(), entity, Object.class);
							if (statusResponseObject.get("status").equals("OK")) {
								if (((Map)statusResponseObject.get("data")).get("driverState").equals("RUNNING")) {
									Thread.sleep(TIME_INTERVEL_STATUS_REFRESH * 1000);
								} else {
									break;
								}
							} else {
								break;
							}
						}
					}
				} else {
					endPoint = WAIT_FOR_ENDPOINT;
					executionResponse = restTemplate.postForObject(WAIT_FOR_ENDPOINT, jobCommand.toMap(), Map.class);
				}
				message = "EndPoint:- %s\nApi Response: %s\n Status Response(if applicable): %s";
				message = String.format(message, endPoint, executionResponse, statusResponseObject);
				metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
						customRequest);
			} catch (HttpClientErrorException e) {
				printErrorMessage(e, packageExecution, customRequest);
				e.printStackTrace();
				throw new EltJobExecutionException(e);
			}catch (Exception e) {
				e.printStackTrace();
				throw new EltJobExecutionException(e);
			}
			message = "Job %s execution Completed";
			message = String.format(message, eltJobInfo.getJobName());
			metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
					customRequest);
		}
		message = "jobs tag execution completed for %s id";
		message = String.format(message, eltJobTagInfo.getTagName());
		metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
				customRequest);
		return executionResponse;
	}

	private void printErrorMessage(HttpClientErrorException e, PackageExecution packageExecution, CustomRequest customRequest) {
		String message = "Http Error Code %s \n Response %s" ;
		message = String.format(message, e.getStatusCode(), e.getResponseBodyAsString());
		metaDataFetch.updateExecutionInfo(Constants.ExecutionStatus.INPROGRESS, message, packageExecution,
				customRequest);
	}

	private CustomRequest getCustomRequest(String clientId) {
		return new CustomRequest(clientId, clientId, null, null);
	}

	List<String> formPrimaryCommand(EltMasterConfiguration eltMasterConfiguration, long loadParamId) {
		
		EltLoadParameters loadParameters = eltLoadParametersDao.fetchLoadParametersById(loadParamId, null);
		validate(loadParameters, loadParamId);
		return formPrimaryCommand(eltMasterConfiguration, loadParameters);
	}

	List<String> formPrimaryCommand(EltMasterConfiguration eltMasterConfiguration, EltLoadParameters loadParameters) {
		List<String> commandList = new ArrayList<>();
		
		commandList.add(ELTCommandConstants.SPARK_BIN);
		commandList.add(ELTCommandConstants.CLASS);
		commandList.add(eltMasterConfiguration.getEltClassPath());
		commandList.add(ELTCommandConstants.MASTER);
		commandList.add(eltMasterConfiguration.getMaster());
		if (eltMasterConfiguration.getMaster().equals(ELTCommandConstants.ValueConstants.MASTER_YARN)) {
			commandList.add(ELTCommandConstants.DEPLOY_MODE);
			commandList.add(eltMasterConfiguration.getDeployMode());
		}
		commandList.add(ELTCommandConstants.NUMBER_OF_EXECUTERS);
		commandList.add(loadParameters.getNoOfExecutors() + "");
		commandList.add(ELTCommandConstants.EXECUTOR_MEMORY);
		commandList.add(loadParameters.getExecutorMemory() + loadParameters.getExecutorMemoryType());
		commandList.add(ELTCommandConstants.TOTAL_EXECUTER_CORES);
		commandList.add(loadParameters.getExecutorCores() + "");
		commandList.add(eltMasterConfiguration.getEltLibraryPath());
		commandList.add(ELTCommandConstants.SPARK_MASTER);
		commandList.add(eltMasterConfiguration.getSparkMaster());
		
		commandList.add(ELTCommandConstants.JDBC_URL);
		commandList.add(String.format(ELTCommandConstants.ValueConstants.JDBC_URL_VALUE, clientDbCredentials.getHostname(),clientDbCredentials.getPortnumber(),clientDbCredentials.getClientDbSchema()));
		commandList.add(ELTCommandConstants.USERNAME);
		commandList.add(clientDbCredentials.getClientDbUsername());
		commandList.add(ELTCommandConstants.PASSWORD);
		try {
			commandList.add(EncryptionServiceImpl.getInstance().encrypt(clientDbCredentials.getClientDbPassword()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		commandList.add(ELTCommandConstants.DRIVER);
		commandList.add(ELTCommandConstants.ValueConstants.JDBC_DRIVER_VALUE);
		commandList.add(ELTCommandConstants.ENCRYPTION_PRIVATE_KEY);
		commandList.add(ELTCommandConstants.ValueConstants.ENCRYPTION_PRIVATE_KEY_VALUE);
		commandList.add(ELTCommandConstants.ENCRYPTION_IV);
		commandList.add(ELTCommandConstants.ValueConstants.ENCRYPTION_IV_VALUE);

		
		commandList.add(ELTCommandConstants.APP_NAME);

		return commandList;
	}
	

	void formPrimaryJsonObject(JSONObject jsonObject,EltMasterConfiguration eltMasterConfiguration, long loadParamId,JSONObject externalDataJson) {

		EltLoadParameters loadParameters = eltLoadParametersDao.fetchLoadParametersById(loadParamId, null);
		validate(loadParameters, loadParamId);
		formPrimaryJsonObject(jsonObject, eltMasterConfiguration, loadParameters,externalDataJson);
	}
	
	void formPrimaryJsonObject(JSONObject jsonObject,EltMasterConfiguration eltMasterConfiguration, EltLoadParameters loadParameters,JSONObject externalDataJson) {
		
		jsonObject.put(ELTCommandConstants.CLASS_NAME, eltMasterConfiguration.getEltClassPath());
		jsonObject.getJSONObject(ELTCommandConstants.MASTER_OBJECT)
				.put("type", eltMasterConfiguration.getMaster())
				.put("host", eltMasterConfiguration.getHost())
				.put("uiPort", uiport)
				.put("restPort", eltMasterConfiguration.getPort());
		
		jsonObject.getJSONObject(ELTCommandConstants.EXECUTION_CONFIG)
				.put("executorMemory", loadParameters.getExecutorMemory() + loadParameters.getExecutorMemoryType())
				.put("totalExecutorCores", loadParameters.getNoOfExecutors());
		
		jsonObject.put(ELTCommandConstants.DEPLOY_MODE_OBJ, eltMasterConfiguration.getDeployMode());
		jsonObject.put(ELTCommandConstants.JAR, eltMasterConfiguration.getEltLibraryPath());
		jsonObject.put(ELTCommandConstants.SPARK_BASEPATH, eltMasterConfiguration.getSparkJobPath());
		
		jsonObject.getJSONObject(ELTCommandConstants.AUTHENTICATION_OBJ)
		.put("userName", eltMasterConfiguration.getUserName());
		//jsonObject.put(ELTCommandConstants.CLIENTDBDETAILS, new JSONObject(clientDbDetails));
		jsonObject.put(ELTCommandConstants.EXTERNALDATA, externalDataJson);
		if (!eltMasterConfiguration.getAuthenticationType().equals("ppkfile")) {
			try {
				jsonObject.getJSONObject(ELTCommandConstants.AUTHENTICATION_OBJ)
				.put("password", EncryptionServiceImpl.getInstance().encrypt(eltMasterConfiguration.getPassword()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			jsonObject.getJSONObject(ELTCommandConstants.AUTHENTICATION_OBJ)
				.put("ppkfile", eltMasterConfiguration.getPpkFile());
		}
		
	}

	List<String> formSecondayCommand(EltJobInfo eltJobInfo, long globalConfigId, String appName, boolean isFileProcessingRequired, boolean isMultipartEnable, 
			String filePath, S3BucketInfo s3BucketInfo, String datbaseTypeName) {
		List<String> commandList = new ArrayList<>();

		commandList.add(appName);

		for (ELTConfigTags eltDerivedConfigs : eltJobInfo.getDerivedComponent()) {
			commandList.add(ELTCommandConstants.DERIVED_COMPONENT_CONFIGS);
			commandList.add(getQuotedQuery(eltDerivedConfigs.getTagId()));
		}

		commandList.add(ELTCommandConstants.CONFIGS);
		commandList.add(getQuotedQuery(eltJobInfo.getConfigProp().getTagId()));
		commandList.add(ELTCommandConstants.VALUES);
		String valuesQuery = getQuotedValueQuery(eltJobInfo.getValuesProp().getTagId());
		if (isFileProcessingRequired) {
			if(isMultipartEnable){
				String unionQuery = " union select 'stg.source.path' as config_key,'" + filePath + "' as config_value " +
					                " union select 'stg.part.files.prefix' as config_key, '/*.csv' as config_value ";
				valuesQuery += unionQuery;
			}else{
				String unionQuery = " union select 'stg.source.path' as config_key,'" + filePath + "' as config_value " +
				                    " union select 'stg.part.files.prefix' as config_key, '' as config_value ";
				valuesQuery += unionQuery;
			}
		}
		valuesQuery+= " union select 'fields.values' as config_key,'"+datbaseTypeName+"' as config_value ";
		commandList.add(valuesQuery);

		if (eltJobInfo.getStatsProp().getTagId() != null && eltJobInfo.getStatsProp().getTagId() != 0) {
			commandList.add(ELTCommandConstants.STATS_CONFIG);
			commandList.add(getQuotedQuery(eltJobInfo.getStatsProp().getTagId()));
		}

		if (globalConfigId != 0) {
			commandList.add(ELTCommandConstants.GLOBAL_VALUES);
			commandList.add(getQuotedQuery(globalConfigId));
		}

		return commandList;
	}
	
	void formSecondayJsonObject(JSONObject jsonObject,EltJobInfo eltJobInfo, long globalConfigId, String appName, boolean isFileProcessingRequired, boolean isMultipartEnable, 
			String filePath, S3BucketInfo s3BucketInfo, String datbaseTypeName,String deploymentType) {

		
		
		
		JSONArray derivedComponent = jsonObject.getJSONObject(ELTCommandConstants.ARGS_OBJECT)
		.getJSONArray(ELTCommandConstants.DERIVED_COMPONENT_CONFIGS_OBJECT);
		for (ELTConfigTags eltDerivedConfigs : eltJobInfo.getDerivedComponent()) {
			derivedComponent.put(getQuotedQuery(eltDerivedConfigs.getTagId()));
		}
		
		String valuesQuery = getQuotedValueQuery(eltJobInfo.getValuesProp().getTagId());
		if (isFileProcessingRequired) {
			if(isMultipartEnable){
				String unionQuery = " union select 'stg.source.path' as config_key,'" + filePath + "' as config_value " +
					                " union select 'stg.part.files.prefix' as config_key, '/*.csv' as config_value ";
				valuesQuery += unionQuery;
			}else{
				String unionQuery = " union select 'stg.source.path' as config_key,'" + filePath + "' as config_value " +
				                    " union select 'stg.part.files.prefix' as config_key, '' as config_value ";
				valuesQuery += unionQuery;
			}
			if(deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
				JSONObject synchronizeObject = new JSONObject();
				
				JSONObject synchronizeAuthenticationObject = new JSONObject();
				synchronizeAuthenticationObject.put("host", sshHost);
				synchronizeAuthenticationObject.put("port", sshPort);
				synchronizeAuthenticationObject.put("userName", sshUserName);
				
				try {
					synchronizeAuthenticationObject.put("password", EncryptionServiceImpl.getInstance().encrypt(sshpassword));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				synchronizeAuthenticationObject.put("privateKey", ELTCommandConstants.ValueConstants.ENCRYPTION_PRIVATE_KEY_VALUE);
				synchronizeAuthenticationObject.put("iv", ELTCommandConstants.ValueConstants.ENCRYPTION_IV_VALUE);
				JSONArray paths = new JSONArray();
				paths.put(filePath);
				synchronizeObject.put("authentication", synchronizeAuthenticationObject);
				synchronizeObject.put("paths", paths);
				jsonObject.put(ELTCommandConstants.SYNCHRONIZE_OBJECT, synchronizeObject);
			}
			
		}
		valuesQuery+= " union select 'fields.values' as config_key,'"+datbaseTypeName+"' as config_value ";
		
		jsonObject.getJSONObject(ELTCommandConstants.ARGS_OBJECT)
		.put(ELTCommandConstants.APP_NAME_OBJ, appName)
		.put(ELTCommandConstants.CONFIGS_OBJECT, getQuotedQuery(eltJobInfo.getConfigProp().getTagId()))
		.put(ELTCommandConstants.VALUES_OBJECT, valuesQuery);
		
		if (eltJobInfo.getStatsProp().getTagId() != null && eltJobInfo.getStatsProp().getTagId() != 0) {
			jsonObject.getJSONObject(ELTCommandConstants.ARGS_OBJECT)
			.put(ELTCommandConstants.STATS_CONFIG_OBJECT, getQuotedQuery(eltJobInfo.getStatsProp().getTagId()));
		}
		
		if (globalConfigId != 0) {
			jsonObject.getJSONObject(ELTCommandConstants.ARGS_OBJECT)
			.put(ELTCommandConstants.GLOBAL_VALUES_OBJECT, getQuotedQuery(globalConfigId));
		}
	}

	String getQuotedQuery(long id) {
		return String.format(ELTCommandConstants.CONFIG_QUERY, id);
	}

	String getQuotedValueQuery(long id) {
		return String.format(ELTCommandConstants.CONFIG_VALUE_QUERY, id);
	}

	void validate(EltJobTagInfo eltJobTagInfo, long eltJobTagId) {
		if (eltJobTagInfo == null) {
			throw new EltJobExecutionException("Job Tag details not found for the job tag id " + eltJobTagId);
		}
		if (eltJobTagInfo.getJobsList() == null && eltJobTagInfo.getJobsList().size() == 0) {
			throw new EltJobExecutionException("Job details not found for the job tag id " + eltJobTagId);
		}
	}

	void validate(EltMasterConfiguration eltMasterConfiguration) {
		if (eltMasterConfiguration == null) {
			throw new EltJobExecutionException("Default configuration details not found ");
		}
	}

	void validate(EltLoadParameters eltLoadParameters, long paramId) {
		if (eltLoadParameters == null) {
			throw new EltJobExecutionException("Elt load parameters not found for the id " + paramId);
		}
	}
	
	JSONObject getJobExecutionJsonTemplate() {
		JSONObject jsonObject = new JSONObject();
		
		JSONObject masterObject = new JSONObject();
		masterObject.put("type", "");
		masterObject.put("host", "");
		masterObject.put("restPort", "");
		
		JSONObject executionConfigObject = new JSONObject();
		executionConfigObject.put("executorMemory", "");
		executionConfigObject.put("totalExecutorCores", "");
		
		JSONObject sparkConfigObject = new JSONObject();
		sparkConfigObject.put("spark.network.timeout", "660s");
		
		
		JSONObject authenticationObject = new JSONObject();
		authenticationObject.put("userName", "root");
		authenticationObject.put("password", "");
		authenticationObject.put("privateKey", ELTCommandConstants.ValueConstants.ENCRYPTION_PRIVATE_KEY_VALUE);
		authenticationObject.put("iv", ELTCommandConstants.ValueConstants.ENCRYPTION_IV_VALUE);
		
		
		
		JSONObject argsObject = new JSONObject();
		
		argsObject.put(ELTCommandConstants.APP_NAME_OBJ, "");
		argsObject.put(ELTCommandConstants.CONFIGS_OBJECT, "");
		argsObject.put(ELTCommandConstants.VALUES_OBJECT, "");
		argsObject.put(ELTCommandConstants.GLOBAL_VALUES_OBJECT, "");
		argsObject.put(ELTCommandConstants.DERIVED_COMPONENT_CONFIGS_OBJECT, new JSONArray());
		
		argsObject.put(ELTCommandConstants.JDBC_URL_OBJECT, String.format(ELTCommandConstants.ValueConstants.JDBC_URL_VALUE, clientDbCredentials.getHostname(),clientDbCredentials.getPortnumber(),clientDbCredentials.getClientDbSchema()));
		argsObject.put(ELTCommandConstants.USERNAME_OBJECT , clientDbCredentials.getClientDbUsername());
		
		
		try {
			argsObject.put(ELTCommandConstants.PASSWORD_OBJECT, EncryptionServiceImpl.getInstance().encrypt(clientDbCredentials.getClientDbPassword()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		argsObject.put(ELTCommandConstants.DRIVER_OBJECT, ELTCommandConstants.ValueConstants.JDBC_DRIVER_VALUE);
		argsObject.put(ELTCommandConstants.ENCRYPTION_PRIVATE_KEY_OBJECT, ELTCommandConstants.ValueConstants.ENCRYPTION_PRIVATE_KEY_VALUE);
		argsObject.put(ELTCommandConstants.ENCRYPTION_IV_OBJECT, ELTCommandConstants.ValueConstants.ENCRYPTION_IV_VALUE);
		
		JSONObject rdbmsObject = new JSONObject();
		rdbmsObject.put("host",clientDbCredentials.getHostname());
		rdbmsObject.put("portNumber",clientDbCredentials.getPortnumber());
		rdbmsObject.put("userName", clientDbCredentials.getClientDbUsername());
		try {
			rdbmsObject.put("password", EncryptionServiceImpl.getInstance().encrypt(clientDbCredentials.getClientDbPassword()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		rdbmsObject.put("privateKey", ELTCommandConstants.ValueConstants.ENCRYPTION_PRIVATE_KEY_VALUE);
		rdbmsObject.put("iv",ELTCommandConstants.ValueConstants.ENCRYPTION_IV_VALUE);
		
		JSONObject clientDBObject = new JSONObject();
		clientDBObject.put(ELTCommandConstants.RDBMSCONNECTION, rdbmsObject);
		clientDBObject.put("appDBName", clientDbCredentials.getClientDbSchema());
		
		jsonObject.put(ELTCommandConstants.CLASS_NAME, "com.anvizent.elt.core.spark.App");
		jsonObject.put(ELTCommandConstants.MASTER_OBJECT, masterObject);
		jsonObject.put(ELTCommandConstants.EXECUTION_CONFIG, executionConfigObject);
		jsonObject.put(ELTCommandConstants.SPARK_CONFIG, sparkConfigObject);
		jsonObject.put(ELTCommandConstants.DEPLOY_MODE_OBJ, "");
		jsonObject.put(ELTCommandConstants.JAR, "");
		jsonObject.put(ELTCommandConstants.SPARK_BASEPATH, "");
		jsonObject.put(ELTCommandConstants.AUTHENTICATION_OBJ, authenticationObject);
		jsonObject.put(ELTCommandConstants.ARGS_OBJECT, argsObject);
		jsonObject.put(ELTCommandConstants.CLIENTDBDETAILS, clientDBObject);
		
		
		return jsonObject;
	}


}
