package com.datamodel.anvizent.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.client.data.to.csv.path.converter.ClientDBProcessor;
import com.anvizent.client.data.to.csv.path.converter.exception.QueryParcingException;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.anvizent.minidw.service.utils.processor.ParseErrorMessage;
import com.anvizent.minidw.service.utils.processor.S3FileProcessor;
import com.anvizent.minidw.service.utils.processor.WebServiceProcessor;
import com.datamodel.anvizent.common.exception.AnvizentCorewsException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.exception.CSVConversionException;
import com.datamodel.anvizent.common.exception.ClientWebserviceRequestException;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.common.exception.UploadandExecutionFailedException;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.dao.AIServiceDao;
import com.datamodel.anvizent.service.dao.CrossReferenceDao;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.StandardPackageDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.model.BusinessModal;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientDbCredentials;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CrossReferenceLogs;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.IncrementalUpdate;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.PackageExecutorMappingInfo;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;

@Component
public class DataBaseMetaDataService implements MetaDataFetch {

	protected static final Log log = LogFactory.getLog(DataBaseMetaDataService.class);

	private @Value("${alerts.thresholds.url:}") String alertsThresholdsUrl;
	private @Value("${anvizent.corews.api.url:}") String authenticationEndPointUrl;
	private @Value("${rscript.installation.path:}") String rscriptPath;
	
	@Autowired
	UserDao userDao;
	@Autowired
	ScheduleDao scheduleDao;
	@Autowired
	MessageSource messageSource;
	@Autowired
	PackageDao packageDao;
	@Autowired
	ParseErrorMessage parseErrorMessage;
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	WebServiceDao webServiceDao;
	@Autowired
	FileDao fileDao;
	@Autowired
	private StandardPackageDao standardPackageDao;
	@Autowired
	ExecutionProcessor executionProcessor;
	@Autowired
	WebServiceProcessor webServiceProcessor;
	@Autowired
	S3FileProcessor s3FileProcessor;
	@Autowired
	CrossReferenceDao crossReferenceDao;
	@Autowired
	AIServiceDao aiServiceDao;
	
	public DataBaseMetaDataService() {
		System.out.println("DataBaseMetaDataService Constructor ....");
	}

	@Override
	public void updateUploadInfo(PackageExecution packageExecution, CustomRequest customRequest) {
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String clientId = customRequest.getClientId();
		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			packageExecution.setModification(modification);
			scheduleDao.updatePackageExecutionUploadInfo(packageExecution, clientAppDbJdbcTemplate);

		} catch (Exception ae) {
			log.error("error while updatePackageExecutionUploadInfo() ", ae);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
	}

	@Override
	public String dateForIncrementalUpdateQuery(int iLId, int connectionId, String typeOfSource,int packageSourceMappingId, CustomRequest customRequest) {
		String incrementalUpdateDate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(
					userDao.getClientDbDetails(customRequest.getClientId()));
			JdbcTemplate clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			incrementalUpdateDate = packageDao.getILIncrementalUpdateDate(null, iLId, connectionId + "", typeOfSource,packageSourceMappingId,
					clientJdbcTemplate);
			;
		} catch (Throwable t) {
			throw new PackageExecutionException(t);
		}

		return incrementalUpdateDate;
	}

	@Override
	public int saveSourceFileInfo(SourceFileInfo sourceFileInfo, CustomRequest customRequest) {
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int sourceFileInfoId = -1;
		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(
					userDao.getClientDbDetails(customRequest.getClientId()));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(customRequest.getClientId());
			sourceFileInfo.setModification(modification);
			sourceFileInfoId = packageDao.updateSourceFileInfo(sourceFileInfo, clientAppDbJdbcTemplate);
			sourceFileInfo.setSourceFileId(sourceFileInfoId);
		} catch (AnvizentRuntimeException ae) {
			log.error("error while saveSourceFileInfo() ", ae);
		} catch (Exception e) {
			// packageService.logError(e, request, clientAppDbJdbcTemplate);
			log.error("error while saveSourceFileInfo() ", e);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return sourceFileInfoId;
	}

	public DataResponse saveOrUpdateIncrementalUpdate(JdbcTemplate clientJdbcTemplate, String schemaName,
			List<Map<String, Object>> incremtalUpdateList, ScheduleDao scheduleDao, User user) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			if (incremtalUpdateList != null && incremtalUpdateList.size() > 0) {
				for (Map<String, Object> incrementalMap : incremtalUpdateList) {

					ILConnectionMapping iLConnectionMapping = new ILConnectionMapping();
					ILConnection iLConnection = new ILConnection();
					Database database = new Database();
					database.setConnector_id((Integer) incrementalMap.get("wsMappingId"));
					iLConnection.setDatabase(database);
					iLConnectionMapping.setiLConnection(iLConnection);
					iLConnectionMapping.setTypeOfCommand(Constants.SourceType.WEBSERVICE);
					iLConnectionMapping.setIncrementalDateValue(incrementalMap.get("currentTime").toString());
					iLConnectionMapping.setiLId((Integer) incrementalMap.get("iLId"));

					Modification modification = new Modification(new Date());
					modification.setCreatedBy(user.getUserName());
					iLConnectionMapping.setModification(modification);

					IncrementalUpdate incrementalUpdate = scheduleDao.getIncrementalUpdate(iLConnectionMapping,
							clientJdbcTemplate, schemaName);
					if (StringUtils.isNotBlank(incrementalUpdate.getIncDateFromSource())) {
						scheduleDao.updateIncrementalUpdate(iLConnectionMapping, clientJdbcTemplate, schemaName);
					} else {
						scheduleDao.saveIncrementalUpdate(iLConnectionMapping, clientJdbcTemplate, schemaName);
					}
				}
				message.setCode("SUCCESS");
				message.setText("Incremtal Update List successfully updated.");
			} else {
				if (incremtalUpdateList == null) {
					incremtalUpdateList = new ArrayList<>();
				}
				log.error("Incremtal Update List is empty and size is :" + incremtalUpdateList.size());
				message.setCode("ERROR");
				message.setText("Incremtal Update List is empty and size is :" + incremtalUpdateList.size());
			}
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@Override
	public void saveExecutionSourceMappingInfo(PackageExecution packageExecution, SourceFileInfo sourceFileInfo,
			CustomRequest customRequest) {
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(
					userDao.getClientDbDetails(customRequest.getClientId()));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			packageDao.updatePackageExecutorSourceMappingInfo(sourceFileInfo, clientAppDbJdbcTemplate);
		} catch (Exception e) {
			throw new UploadandExecutionFailedException(e);
		}
	}

	@Override
	public PackageExecution saveUploadInfo(PackageExecution pExecution, CustomRequest customRequest) {
		JdbcTemplate clientAppDbJdbcTemplate = null;
		int saveExecutionId = 0;
		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(
					userDao.getClientDbDetails(customRequest.getClientId()));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(customRequest.getClientId());
			pExecution.setModification(modification);
			saveExecutionId = scheduleDao.savePackageExectionInfo(pExecution, clientAppDbJdbcTemplate);
			if (saveExecutionId != 0) {
				pExecution.setExecutionId(saveExecutionId);
			} else {
				throw new UploadandExecutionFailedException("\n unable to save execution info while file uploading");
			}
		} catch (Throwable t) {
			throw new UploadandExecutionFailedException("\n unable to save execution info while file uploading");
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return pExecution;
	}

	@Override
	public void updateUploadInfo(String status, String comments, PackageExecution packExecution,
			CustomRequest customRequest) {
		updateUploadInfo(commonProcessor.getUploadStatus(packExecution.getExecutionId(), status, comments,
				packExecution.getTimeZone()), customRequest);
	}

	@Override
	public Package getPackageDetails(CustomRequest customRequest, String clientId) {
		try {
			return standardPackageDao.fetchStandardPackageInfo(customRequest.getClientId(),
					getClientAppJdbcTemplate(customRequest.getClientId()));
		} catch (AnvizentCorewsException e) {
			throw new RuntimeException("Package deta fetching failed ", e);
		}
	}

	@Override
	public List<ILConnectionMapping> getPackageSourceMappingListByIds(CustomRequest customRequest, DLInfo dlInfo,
			String clientId) {
		List<Integer> mappingsIds = new ArrayList<>();
		for (ILInfo il : dlInfo.getIlList()) {
			il.getIlSources().forEach(mappingInfo -> {
				mappingsIds.add(mappingInfo.getConnectionMappingId());
			});
		}
		List<ILConnectionMapping> ilConnectionMappings = null;
		try {
			ilConnectionMappings = standardPackageDao.getILConnectionMappingInfoByMappingId(mappingsIds,
					customRequest.getUserId(), getClientAppJdbcTemplate(customRequest.getClientId()));
			ilConnectionMappings = MinidwServiceUtil.getILConnectionMapping(ilConnectionMappings);
		} catch (Exception e) {
			throw new RuntimeException("Source mapping fetching failed ", e);
		}
		return ilConnectionMappings;
	}

	@Override
	public void reloadAnvizentTableAccessEndPoint(PackageExecution packageExecution, CustomRequest customRequest) {
		ClientDbCredentials clientDbDetails = userDao.getClientDbDetails(customRequest.getClientId());
		MiniDwJobUtil.reloadUrl(customRequest.getUserId(), packageExecution.getDerivedTablesList(),
				customRequest.getClientId(), clientDbDetails.getClientDbSchema(), null, authenticationEndPointUrl);
	}

	@Override
	public void alertsAndThreshould(PackageExecution packageExecution, CustomRequest customRequest) {
		Package userPackage = getPackageDetails(customRequest, null);
		MiniDwJobUtil.alertsAndThreshoulds(customRequest.getClientId(), packageExecution.getDerivedTablesList(),
				userPackage.getPackageName(), alertsThresholdsUrl);
	}

	@Override
	public void druidIntegration(PackageExecution packageExecution, CustomRequest customRequest) {
		// this method can call separrately from runner api as we need Druid
		// dependency here
	}

	@Override
	public void saveUpdateIncrementalDates(PackageExecution packageExecution, CustomRequest customRequest) {
		ClientDbCredentials clientDbDetails = userDao.getClientDbDetails(customRequest.getClientId());
		JdbcTemplate clientJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(clientDbDetails);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
		} catch (AnvizentCorewsException e) {
			throw new RuntimeException("", e);
		}
		if (packageExecution.getIncremtalUpdateList().size() > 0 && packageExecution.getIncremtalUpdateList() != null) {
			User user = new User();
			user.setUserId(customRequest.getUserId());
			user.setClientId(customRequest.getClientId());
			user.setUserName(customRequest.getUserId());
			MiniDwJobUtil.saveOrUpdateIncrementalUpdate(clientJdbcTemplate, clientDbDetails.getClientDbSchema(),
					packageExecution.getIncremtalUpdateList(), scheduleDao, user);
		}
	}

	@Override
	public List<PackageExecutorMappingInfo> getPackageExecutorSourceMappingInfoList(PackageExecution packageExecution,
			CustomRequest customRequest) {

		JdbcTemplate clientAppJdbcTemplate = null;
		try {
			clientAppJdbcTemplate = getClientAppJdbcTemplate(customRequest.getClientId());
		} catch (AnvizentCorewsException e) {
			log.error("Unable save exection information", e);
			throw new RuntimeException("Unable to get execution source mapping list ", e);
		}
		return packageDao.getPackageExecutorSourceMappingInfoList(packageExecution.getExecutionId(),
				clientAppJdbcTemplate);
	}

	@Override
	public void saveExecutionInfo(PackageExecution packageExecution, CustomRequest customRequest) {
		JdbcTemplate clientAppJdbcTemplate = null;
		try {
			clientAppJdbcTemplate = getClientAppJdbcTemplate(customRequest.getClientId());
		} catch (AnvizentCorewsException e) {
			log.error("Unable save exection information", e);
			throw new RuntimeException("Unable to save execution information", e);
		}
		scheduleDao.updatePackageExecutionStatus(packageExecution, clientAppJdbcTemplate);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String runIl(PackageExecutorMappingInfo packageExecutorMappingInfo, CustomRequest customRequest) {
		String userId = customRequest.getUserId();
		String clientId = customRequest.getClientId();
		ClientJdbcInstance clientjdbcInstance;
		try {
			clientjdbcInstance = getClientjdbcInstance(clientId);
		} catch (AnvizentCorewsException e1) {
			log.error("Unable get jdbc connectiion", e1);
			throw new RuntimeException("Unable get jdbc connectiion", e1);
		}

		Modification modification = new Modification(new Date());
		modification.setCreatedBy(userId);
		modification.setModifiedBy(userId);
		modification.setModifiedDateTime(new Date());
		modification.setipAddress(SessionHelper.getIpAddress());
		modification.setIsActive(true);

		String tempPath = Constants.Temp.getTempFileDir();
		tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
		tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
		// creating folder when the folder does not exits.
		String jobFilesPath = tempPath + "JobFiles/";
		CommonUtils.createDir(jobFilesPath);

		Package userPackage = getPackageDetails(customRequest, null);
		String deploymentType = customRequest.getDeploymentType();
		User user = new User();
		user.setUserId(userId);
		user.setClientId(clientId);
		user.setUserName(userId);
		try {
			DataResponse runIlResponse = executionProcessor.runIl(user, modification,
					packageExecutorMappingInfo.getPackageExecution().getTimeZone(), jobFilesPath, clientjdbcInstance,
					packageExecutorMappingInfo, deploymentType, userPackage
					);
			isValidateDataRespoince(runIlResponse);
			Map<String, Object> responseMap = (Map<String, Object>) runIlResponse.getObject();
			return (String) (responseMap.get("iLTableName"));
		} catch (TalendJobNotFoundException | CSVConversionException | InterruptedException | IOException
				| AnvizentCorewsException e) {
			throw new RuntimeException("IL exection failed", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> runDl(MultiValueMap<Object, Object> dlMap, PackageExecution packageExecution,
			CustomRequest customRequest) {
		String userId = customRequest.getUserId();
		String clientId = customRequest.getClientId();
		ClientJdbcInstance clientjdbcInstance;
		try {
			clientjdbcInstance = getClientjdbcInstance(clientId);
		} catch (AnvizentCorewsException e1) {
			log.error("Unable get jdbc connectiion", e1);
			throw new RuntimeException("Unable get jdbc connectiion", e1);
		}

		Modification modification = new Modification(new Date());
		modification.setCreatedBy(userId);
		modification.setModifiedBy(userId);
		modification.setModifiedDateTime(new Date());
		modification.setipAddress(SessionHelper.getIpAddress());
		modification.setIsActive(true);

		String tempPath = Constants.Temp.getTempFileDir();
		tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
		tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
		// creating folder when the folder does not exits.
		String jobFilesPath = tempPath + "JobFiles/";
		CommonUtils.createDir(jobFilesPath);

		Package userPackage = getPackageDetails(customRequest, null);
		String deploymentType = customRequest.getDeploymentType();
		User user = new User();
		user.setUserId(userId);
		user.setClientId(clientId);
		user.setUserName(userId);
		try {
			DataResponse runIlResponse = executionProcessor.runDl(user, modification, customRequest.getTimeZone(),
					jobFilesPath, clientjdbcInstance, packageExecution.getExecutionId(),
					deploymentType, userPackage, packageExecution.getDlId());
			isValidateDataRespoince(runIlResponse);
			return (Map<String, Object>) runIlResponse.getObject();
		} catch (TalendJobNotFoundException | CSVConversionException | InterruptedException | IOException
				| AnvizentCorewsException | ParseException e) {
			throw new RuntimeException("Il exection failed", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> runDDlayouts(Set<Integer> ddLTableSet, CustomRequest customRequest) {
		Modification modification = new Modification(new Date());
		modification.setModifiedBy(customRequest.getUserId());
		modification.setCreatedBy(customRequest.getUserId());
		User user = new User();
		user.setUserId(customRequest.getUserId());
		user.setClientId(customRequest.getClientId());
		user.setUserName(customRequest.getUserId());
		try {
			ClientJdbcInstance clientjdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			DataResponse runCustomDataSetsInStandardPackage = executionProcessor.runCustomDataSetsInStandardPackage(
					ddLTableSet, user, clientjdbcInstance.getClientJdbcTemplate(),
					clientjdbcInstance.getClientAppdbJdbcTemplate(), modification, "schedule");
			Object object = runCustomDataSetsInStandardPackage.getObject();
			if (object != null) {
				return (List<String>) object;
			} else {
				return null;
			}
		} catch (SQLException | AnvizentCorewsException e) {
			throw new RuntimeException("DDL execution failed", e);
		}
	}

	public JdbcTemplate getClientAppJdbcTemplate(String clientId) throws AnvizentCorewsException {
		return getClientjdbcInstance(clientId).getClientAppdbJdbcTemplate();
	}

	public ClientJdbcInstance getClientjdbcInstance(String clientId) throws AnvizentCorewsException {
		return new ClientJdbcInstance(userDao.getClientDbDetails(clientId));
	}

	boolean isValidateDataRespoince(DataResponse response) {
		if (response != null && response.getHasMessages()) {
			if (response.getMessages().get(0).getCode().equals("SUCCESS")) {
				return true;
			} else {
				throw new RuntimeException(response.getMessages().get(0).getText());
			}
		} else {
			throw new RuntimeException("Unable to get responce from server");
		}
	}

	@Override
	public void saveExecutionInfo(String status, String comments, PackageExecution packageExecution,
			CustomRequest customRequest) {
		saveExecutionInfo(commonProcessor.getExecutionStatus(packageExecution.getExecutionId(), status, comments,
				packageExecution.getTimeZone()), customRequest);
	}

	@Override
	public void updateExecutionInfo(String status, String comments, PackageExecution packageExecution,
			CustomRequest customRequest) {
		comments = Constants.Config.NEW_LINE + commonProcessor.getFormattedDateString() + comments;
		updateExecutionInfo(commonProcessor.getExecutionStatus(packageExecution.getExecutionId(), status, comments,
				packageExecution.getTimeZone()), customRequest);
	}

	@Override
	public void updateExecutionInfo(PackageExecution packageExecution, CustomRequest customRequest) {
		JdbcTemplate clientAppJdbcTemplate = null;
		try {
			clientAppJdbcTemplate = getClientAppJdbcTemplate(customRequest.getClientId());
		} catch (AnvizentCorewsException e) {
			log.error("Unable save exection information", e);
			throw new RuntimeException("Unable to save execution information", e);
		}
		scheduleDao.updatePackageExecutionStatusInfo(packageExecution, clientAppJdbcTemplate);
	}

	@Override
	public DLInfo getSourcesByDlId(int dlId, CustomRequest customRequest) {
		JdbcTemplate clientAppDbJdbcTemplate = null;
		DLInfo dlInfo = null;
		try {
			clientAppDbJdbcTemplate = getClientAppJdbcTemplate(customRequest.getClientId());

			 dlInfo = standardPackageDao.getIlMappingInfobyId(customRequest.getUserId(), customRequest.getClientId(), dlId,
					clientAppDbJdbcTemplate);
			if (dlInfo != null) {
				List<String> totalTablesSet = new ArrayList<String>(Arrays.asList(dlInfo.getdL_table_name()));
				List<DDLayout> dDLayoutList = packageDao.getDDlayoutList(customRequest.getClientId(), totalTablesSet, customRequest.getUserId(),
						clientAppDbJdbcTemplate);
				dlInfo.setDdlayoutList(dDLayoutList);

				for (ILInfo mappingInfo : dlInfo.getIlList()) {
					mappingInfo.setIlSources(MinidwServiceUtil.getILConnectionMapping(mappingInfo.getIlSources()));
				}
			}
		} catch (Throwable t) {
			throw new RuntimeException("Unable to get dlinfo information");
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return dlInfo;
	}

	@Override
	public WebServiceApi getIlConnectionWebServiceMapping(CustomRequest customRequest, Integer packageId, Integer ilId,
			Integer connectionMappingId) {
		JdbcTemplate clientAppDbJdbcTemplate = null;
		WebServiceApi webServiceApi = null;
		try{
			clientAppDbJdbcTemplate = getClientAppJdbcTemplate(customRequest.getClientId());
			webServiceApi = packageDao.getIlConnectionWebServiceMapping(customRequest.getUserId(),packageId, ilId, connectionMappingId, clientAppDbJdbcTemplate);
		 }catch(AnvizentCorewsException e){
			 throw new RuntimeException("Unable to get webServiceApi information", e);
		 }
		return webServiceApi;
	}

	@Override
	public WebServiceConnectionMaster getWebServiceConnectionDetails(Long webServiceConId, CustomRequest customRequest) {
		JdbcTemplate clientAppDbJdbcTemplate = null;
		WebServiceConnectionMaster webServiceConnectionMaster= null;
		try{
			clientAppDbJdbcTemplate = getClientAppJdbcTemplate(customRequest.getClientId());
			webServiceConnectionMaster = webServiceDao.getWebServiceConnectionDetails(Long.valueOf(webServiceConId), customRequest.getClientId(), clientAppDbJdbcTemplate);
		}catch(AnvizentCorewsException e){
			throw new RuntimeException("Unable to get webServiceConnectionMaster information", e);
		}
		return webServiceConnectionMaster;
	}

	@Override
	public List<Table> getTempTablesAndWebServiceJoinUrls(Integer packageId, Integer ilId, Integer connectionMappingId,
			CustomRequest customRequest) {
		  JdbcTemplate clientAppDbJdbcTemplate = null;
		  List<Table> tableList = new ArrayList<>();
		try{
			clientAppDbJdbcTemplate = getClientAppJdbcTemplate(customRequest.getClientId());
			tableList = packageDao.getTempTablesAndWebServiceJoinUrls(customRequest.getUserId(), packageId, ilId, connectionMappingId, clientAppDbJdbcTemplate);
		}catch(AnvizentCorewsException e){
			throw new RuntimeException("Unable to get webservice join urls list information", e);
		}
		return tableList;
	}

	@Override
	public boolean isTableExists(String tableName,
			CustomRequest customRequest) {
		 JdbcTemplate clientStagingJdbcTemplate = null;
		 boolean istableExists = Boolean.FALSE;
		try{
			ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			istableExists = fileDao.isTableExists(customRequest.getClientId(), null, tableName, clientStagingJdbcTemplate);
		}catch(AnvizentCorewsException e){
			throw new RuntimeException("Unable to get webservice join urls list information", e);
		}
		return istableExists;
	}

	@Override
	public List<Column> getTableStructure(String tableName, int industryId, CustomRequest customRequest) {
		JdbcTemplate clientStagingJdbcTemplate = null;
		List<Column> tblColumnList = new ArrayList<>();
		try{
			ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			tblColumnList = packageDao.getTableStructure(null,tableName,industryId, customRequest.getClientId(),clientStagingJdbcTemplate);
		}catch(AnvizentCorewsException e){
			throw new RuntimeException("Unable to get table structure information", e);
		}
		return tblColumnList;
	}

	@Override
	public void truncateTable(String tableName, String schemaType, CustomRequest customRequest) {
		 JdbcTemplate clientStagingJdbcTemplate = null;
		 JdbcTemplate clientAppDbJdbcTemplate = null;
		 try{
			 ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			 clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			 clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			 if(schemaType.equals("staging")){
				 fileDao.truncateTable(null, tableName,clientStagingJdbcTemplate);
			 }else if(schemaType.equals("clientdb")){
				 
			 }else{
				 fileDao.truncateTable(null, tableName,clientAppDbJdbcTemplate); 
			 }
		 }catch(AnvizentCorewsException e){
				throw new RuntimeException("Unable to truncate the tableName information", e);
			}
	}

	@Override
	public FileSettings getFileSettingsInfo(CustomRequest customRequest) {
		 JdbcTemplate clientAppDbJdbcTemplate = null;
		 FileSettings fileSettings = null;
		 try{
			 ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			 clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			 fileSettings = packageDao.getFileSettingsInfo(customRequest.getClientId(), clientAppDbJdbcTemplate);
		 }catch(AnvizentCorewsException e){
			 throw new RuntimeException("Unable to get filesettingsinfo information", e);
		 }
		return fileSettings;
	}

	@Override
	public JdbcTemplate getClientStagingJdbcTemplate(CustomRequest customRequest) {
		JdbcTemplate clientStagingJdbcTemplate = null;
		try{
			ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
		}catch(AnvizentCorewsException e){
			throw new RuntimeException("Unable to get clientStagingJdbcTemplate", e);
		}
		return clientStagingJdbcTemplate;
	}

	@Override
	public Map<String, Object> getClientDbDetails(CustomRequest customRequest) {
		Map<String, Object> clientDbDetails = new LinkedHashMap<>();
		try{
			ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			clientDbDetails = clientJdbcInstance.getClientDbCredentials();
		}catch(AnvizentCorewsException e){
			throw new RuntimeException("Unable to get clientDbDetails", e);
		}
		return clientDbDetails;
	}

	@Override
	public void processDataFromFileBatch(String uploadedPath, Integer webServiceConnectionId, String tableName,
			CustomRequest customRequest) {
		Map<String, Object> clientDbDetails = new LinkedHashMap<>();
		JdbcTemplate clientStagingJdbcTemplate = null;
		JdbcTemplate clientAppdbJdbcTemplate = null;
		ClientData clientData = new ClientData();
		String filePath = null;
		try {
			ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			clientDbDetails = clientJdbcInstance.getClientDbCredentials();
			clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			clientAppdbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			String clientStagingSchema = clientDbDetails.get("clientdb_staging_schema").toString();

			S3BucketInfo s3BucketInfo = userDao.getS3BucketInfoByClientId(customRequest.getClientId(),
					clientAppdbJdbcTemplate);
			FileSettings fileSettings = packageDao.getFileSettingsInfo(customRequest.getClientId(),
					clientAppdbJdbcTemplate);

			String deploymentType = customRequest.getDeploymentType();

			if (StringUtils.isBlank(deploymentType)) {
				deploymentType = Constants.Config.DEPLOYMENT_TYPE_HYBRID;
			}

			List<String> filePaths = s3FileProcessor.downloadFilesFromS3(uploadedPath, customRequest.getUserId(),
					deploymentType, false, fileSettings.getFileEncryption(), s3BucketInfo);

			clientData.setUserId(customRequest.getClientId());
			Package userPackage = new Package();
			userPackage.setPackageId(0);
			userPackage.setPackageName("StandardPackage");
			clientData.setUserPackage(userPackage);
			Table tempTable = packageDao.getTempTableByWebServiceConnectionId(webServiceConnectionId,
					clientAppdbJdbcTemplate);

			tempTable.setSchemaName(clientStagingSchema);
			List<Column> columns = packageDao.getTableStructure(null, tableName, 0, null, clientStagingJdbcTemplate);
			tempTable.setColumns(columns);

			List<String> originaCols = new ArrayList<String>();
			for (Column col : columns) {
				originaCols.add(col.getColumnName());
			}
			tempTable.setOriginalColumnNames(originaCols);

			tempTable.setSchemaName(clientStagingSchema);
			clientData.getUserPackage().setTable(tempTable);
			clientData.setUserPackage(clientData.getUserPackage());
			
			fileDao.truncateTable(null, tableName, clientStagingJdbcTemplate);
			filePath = filePaths.get(0);
			MinidwServiceUtil.processDataFromFileBatch(filePath, Constants.FileType.CSV, ",", null, clientData,
					clientStagingSchema, clientStagingJdbcTemplate);
		} catch (Exception e) {
			throw new ClientWebserviceRequestException(
					"Error occured while fetching data for IL " + clientData.getIlConnectionMapping().getiLId(), e);
		} finally {
			if (filePath != null) {
				try {
					FileUtils.forceDelete(new File(filePath));
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}

	}
	
	@Override
	public String createFileByConnection(ILConnectionMapping ilConnectionMapping, S3BucketInfo s3BucketInfo,
			FileSettings fileSettings, String deploymentType, CustomRequest customRequest)
			throws AmazonS3Exception, OnpremFileCopyException, IOException {

		
		ClientDBProcessor clientDBProcessor = new ClientDBProcessor();
		HashMap<String, Object> processedMap = null;
		try {
			processedMap = clientDBProcessor.processClientDBData(Constants.Temp.getTempFileDir(), null, ilConnectionMapping, 1, false, null);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | QueryParcingException
				| SQLException e) {
			log.error("Error while creating file ", e);
			throw new OnpremFileCopyException(e);
		}
		Path tempFileName = (Path) processedMap.get("tempFile");
		File tempFile = tempFileName.toFile();
		
		return tempFile.getAbsolutePath();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> runCustomDataSets(List<String> tableList, String runType, CustomRequest customRequest) {
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppdbJdbcTemplate = null;
		List<String> ddlsTable = new ArrayList<>();
		try{
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(customRequest.getUserId());
			modification.setCreatedBy(customRequest.getUserId());
			User user = new User();
			user.setUserId(customRequest.getUserId());
			user.setClientId(customRequest.getClientId());
			user.setUserName(customRequest.getUserId());
			
			ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientAppdbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			DataResponse response = executionProcessor.runCustomDataSetsInCustomPacakge(packageDao, tableList, user, clientJdbcTemplate, clientAppdbJdbcTemplate, fileDao, modification, runType);
			isValidateDataRespoince(response);
			return (List<String>) response.getObject();
		}catch(Exception e){
			
		}
		return ddlsTable;
	}

	@Override
	public List<CrossReferenceLogs> getCrossReferencesByIlId(int ilId, CustomRequest customRequest)
	{
		JdbcTemplate clientAppdbJdbcTemplate = null;
		List<CrossReferenceLogs> crossReferenceLogs = new ArrayList<>();
		try{
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(customRequest.getUserId());
			modification.setCreatedBy(customRequest.getUserId());
			
			User user = new User();
			user.setUserId(customRequest.getUserId());
			user.setClientId(customRequest.getClientId());
			user.setUserName(customRequest.getUserId());
			
			ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			clientAppdbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			crossReferenceLogs = crossReferenceDao.getCrossReferencesByIlId(ilId, customRequest.getClientId(), clientAppdbJdbcTemplate);
		}catch(Exception e){
			throw new RuntimeException("Unable to get crossreference by ilid" + ilId);
		}
		return crossReferenceLogs;
	}

	@Override
	public void processCrossReference(CrossReferenceLogs crossReference, CustomRequest customRequest)
	{
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null;
		try {
			Modification modification = new Modification(new Date());
			modification.setModifiedBy(customRequest.getUserId());
			modification.setCreatedBy(customRequest.getUserId());
			User user = new User();
			user.setUserId(customRequest.getUserId());
			user.setClientId(customRequest.getClientId());
			user.setUserName(customRequest.getUserId());
			
			ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			ILInfo ilInfo = packageDao.getILById(crossReference.getIlId(), customRequest.getClientId(), clientAppDbJdbcTemplate);
			List<Column> columns = packageDao.getTableStructure(null, ilInfo.getiL_table_name(), 0, customRequest.getClientId(), clientJdbcTemplate);
			ilInfo.setColumns(columns);
			crossReferenceDao.processAutoMergeCrossReference(ilInfo, user, clientJdbcInstance.getClientDbCredentials(), crossReference, clientJdbcTemplate, clientAppDbJdbcTemplate);
		}catch(Throwable t) {
			throw new RuntimeException(t);
		}finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		
	}

	@Override
	public int updateRetryPaginationAtWsMapping(int id, String retryPaginationData, CustomRequest customRequest)
	{
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String clientId = customRequest.getClientId();
		int statusId = 0;
		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			 
			statusId = webServiceDao.updateRetryPaginationAtWsMapping(id,retryPaginationData,clientAppDbJdbcTemplate);

		} catch (Exception ae) {
			log.error("error while updatePackageExecutionUploadInfo() ", ae);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return statusId;
	}

	@Override
	public String runRBM(BusinessModal businessModal, CustomRequest customRequest)
	{
		String userId = customRequest.getUserId();
		String clientId = customRequest.getClientId();
		ClientJdbcInstance clientjdbcInstance;
		DataResponse rDataResponse = null;
		String statusMessage = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try
		{
			clientjdbcInstance = getClientjdbcInstance(clientId);

			clientAppDbJdbcTemplate = clientjdbcInstance.getClientAppdbJdbcTemplate();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			modification.setModifiedBy(userId);
			modification.setModifiedDateTime(new Date());
			modification.setipAddress(SessionHelper.getIpAddress());
			modification.setIsActive(true);

			String tempPath = Constants.Temp.getTempFileDir();
			tempPath = tempPath.contains("\\") ? tempPath.replace('\\', '/') : tempPath;
			tempPath = !tempPath.endsWith("/") ? tempPath + "/" : tempPath;
			// creating folder when the folder does not exits.
			String rContextParamsFilePath = tempPath + "RContextParamsFilePath/";
			CommonUtils.createDir(rContextParamsFilePath);
 
			User user = new User();
			user.setUserId(userId);
			user.setClientId(clientId);
			user.setUserName(userId);

			businessModal = aiServiceDao.getBusinessModalInfoById(businessModal.getBmid(), clientAppDbJdbcTemplate);

			rDataResponse = executionProcessor.runRJob(user, modification, rContextParamsFilePath, clientjdbcInstance, businessModal, rscriptPath);

			statusMessage = rDataResponse.getMessages().get(0).getText();

		}
		catch ( AnvizentCorewsException e1 )
		{
			log.error("Unable get jdbc connectiion", e1);
			throw new RuntimeException("Unable get jdbc connectiion", e1);
			
		}
		catch ( Throwable ae )
		{
			throw new RuntimeException("R exection failed", ae);
		}
		return statusMessage;
	}

	@Override
	public int updateIncrementalDatePaginationAtWsMapping(int id, String incrementalPaginationData, CustomRequest customRequest)
	{
		JdbcTemplate clientAppDbJdbcTemplate = null;
		String clientId = customRequest.getClientId();
		int statusId = 0;
		try {
			ClientJdbcInstance clientJdbcInstance = new ClientJdbcInstance(userDao.getClientDbDetails(clientId));
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			 
			statusId = webServiceDao.updateIncrementalDatePaginationAtWsMapping(id,incrementalPaginationData,clientAppDbJdbcTemplate);

		} catch (Exception ae) {
			log.error("error while updateIncrementalDatePaginationAtWsMapping() ", ae);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		return statusId;
	}

	@Override
	public S3BucketInfo getS3BucketInfo(CustomRequest customRequest)
	{
		 JdbcTemplate clientAppDbJdbcTemplate = null;
		 S3BucketInfo s3BucketInfo = null;
		 try{
			 ClientJdbcInstance clientJdbcInstance = getClientjdbcInstance(customRequest.getClientId());
			 clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			 s3BucketInfo = userDao.getS3BucketInfoByClientId(customRequest.getClientId(), clientAppDbJdbcTemplate);
		 }catch(AnvizentCorewsException e){
			 throw new RuntimeException("Unable to get s3BucketInfo information", e);
		 }
		return s3BucketInfo;
	}

}