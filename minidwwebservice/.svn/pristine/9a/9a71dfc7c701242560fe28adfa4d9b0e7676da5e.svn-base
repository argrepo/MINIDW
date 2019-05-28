package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.processor.MetaDataFetch;
import com.anvizent.minidw.service.utils.processor.S3FileProcessor;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ExecutionProcessor;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.spring.AppProperties;

@Import(AppProperties.class)
@RestController("user_webServiceProcessorRestController")
@RequestMapping("" + Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/package")
@CrossOrigin
public class WebServiceProcessorRestController {
	protected static final Log LOGGER = LogFactory.getLog(WebServiceProcessorRestController.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PackageService packageService;
	@Autowired
	private FileService fileService;
	@Autowired
	ExecutionProcessor executionProcessor;
	@Autowired
	S3FileProcessor s3FileProcessor;
	@Autowired
	MetaDataFetch metaDataFetch;
	
	
	@Autowired
	WebServiceDao webServiceDao;
	
		@RequestMapping(value = "/ilConnectionWebServiceMapping", method = RequestMethod.POST)
		public DataResponse getDlSourceInfoForStandardPackage(
				@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestParam("packageId") Integer packageId,
				@RequestParam("ilId") Integer ilId, @RequestParam("connectionMappingId") Integer connectionMappingId,
				HttpServletRequest request, Locale locale) {
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientAppDbJdbcTemplate = null;
			try {
				//String clientId = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				WebServiceApi webServiceApi = packageService.getIlConnectionWebServiceMapping(userId, packageId, ilId, connectionMappingId, clientAppDbJdbcTemplate);
				if (webServiceApi != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setObject(webServiceApi);
				}
			} catch (Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableTogetMappingInfo", null, locale));
				messages.add(message);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			dataResponse.addMessages(messages);
			return dataResponse;
		}
		
		@RequestMapping(value = "/getTempTablesAndWebServiceJoinUrls", method = RequestMethod.POST)
		public DataResponse getTempTablesAndWebServiceJoinUrls(
				@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,@RequestParam("packageId") Integer packageId,
				@RequestParam("ilId") Integer ilId, @RequestParam("connectionMappingId") Integer connectionMappingId,
				HttpServletRequest request, Locale locale) {
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientAppDbJdbcTemplate = null;
			try {
				//String clientId = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				List<Table> tableList = packageService.getTempTablesAndWebServiceJoinUrls(userId, packageId, ilId, connectionMappingId, clientAppDbJdbcTemplate);
				if (tableList != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setObject(tableList);
				}
			} catch (Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableTogetMappingInfo", null, locale));
				messages.add(message);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			dataResponse.addMessages(messages);
			return dataResponse;
		}
		
		@RequestMapping(value="/checkTableExists/{tableName}", method=RequestMethod.GET)
		public DataResponse CheckTableExists(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("tableName") String tableName,
				HttpServletRequest request, Locale locale){
			
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientAppDbJdbcTemplate = null;
			JdbcTemplate clientStagingJdbcTemplate = null;
			try {
				String clientId = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);

				boolean istableExists = fileService.isTableExists(clientId, null, tableName, clientStagingJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(istableExists);
			} catch (Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableTogetMappingInfo", null, locale));
				messages.add(message);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			dataResponse.addMessages(messages);
			return dataResponse;
		  }
		
		@RequestMapping(value="/truncateTable", method=RequestMethod.POST)
		public void truncateTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestParam("tableName") String tableName,
				@RequestParam("schemaType") String schemaType, HttpServletRequest request, Locale locale){
			JdbcTemplate clientAppDbJdbcTemplate = null;
			JdbcTemplate clientStagingJdbcTemplate = null;
			try{
				//String clientId = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				if(schemaType.equals("staging")){
					fileService.truncateTable(null, tableName,clientStagingJdbcTemplate);
				 }else if(schemaType.equals("clientdb")){
					 
				 }else{
					 fileService.truncateTable(null, tableName,clientAppDbJdbcTemplate); 
				 }
				
			}catch(Throwable r){
				throw new RuntimeException("Unable to truncate table"+tableName);
			}
		}
		
		@RequestMapping(value="/getFileSettings", method=RequestMethod.GET)
		public DataResponse getFileSettings(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale){
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientAppDbJdbcTemplate = null;
			try {
				String clientId = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				FileSettings fileSettings = packageService.getFileSettingsInfo(clientId, clientAppDbJdbcTemplate);
				if(fileSettings != null){
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setObject(fileSettings);
				}			
			} catch (Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableTogetMappingInfo", null, locale));
				messages.add(message);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			dataResponse.addMessages(messages);
			return dataResponse;
		}
		
		@RequestMapping(value = "/getWebServiceConnectionMaster/{webServiceConId}", method = RequestMethod.GET)
		public DataResponse getWebServiceConnectionMaster(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
				@PathVariable("webServiceConId") Long webServiceConId, HttpServletRequest request, Locale locale) {
			
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientAppDbJdbcTemplate = null;
			try {
				String clientId = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

				WebServiceConnectionMaster webServiceConnectionMaster = webServiceDao.getWebServiceConnectionDetails(Long.valueOf(webServiceConId), clientId, clientAppDbJdbcTemplate);
				if (webServiceConnectionMaster != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setObject(webServiceConnectionMaster);
				}
			} catch (Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableTogetwebservicemasterinfo", null, locale));
				messages.add(message);
			} finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			dataResponse.addMessages(messages);
			return dataResponse;
		}
		
		@RequestMapping(value = "/getTableStructure/{tableName}", method = RequestMethod.GET)
		public DataResponse getTableStructure(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
				@PathVariable("tableName") String tableName, HttpServletRequest request, Locale locale) {
			
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientStagingJdbcTemplate = null;
			try {
				String clientId = CommonUtils.getClientIDFromHeader(request);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				List<Column> tblColumnList = packageService.getTableStructure(null,tableName, 0, clientId, clientStagingJdbcTemplate);
				if (tblColumnList != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setObject(tblColumnList);
				}
			} catch (Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableTogettablestructure", null, locale));
				messages.add(message);
			} finally {
				CommonUtils.closeDataSource(clientStagingJdbcTemplate);
			}
			dataResponse.addMessages(messages);
			return dataResponse;
		}
		
		
		@RequestMapping(value = "/getClientStagingJdbcTemplate", method = RequestMethod.GET)
		public DataResponse getClientStagingJdbcTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
				HttpServletRequest request, Locale locale) {
			
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientStagingJdbcTemplate = null;
			try {
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientStagingJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
				if (clientStagingJdbcTemplate != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setObject(clientStagingJdbcTemplate);
				}
			} catch (Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableTogetJdbctemplate", null, locale));
				messages.add(message);
			} 
			dataResponse.addMessages(messages);
			return dataResponse;
		}
		
		@RequestMapping(value = "/getClientDbDetails", method = RequestMethod.GET)
		public DataResponse getClientDbDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, HttpServletRequest request, Locale locale) {
			
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			Map<String, Object>  clientDbDetails = null;
			try {
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientDbDetails = clientJdbcInstance.getClientDbCredentials();
				if (clientDbDetails != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setObject(clientDbDetails);
				}
			} catch (Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableTogetJdbctemplate", null, locale));
				messages.add(message);
			} 
			dataResponse.addMessages(messages);
			return dataResponse;
		}
		
		@RequestMapping(value="/processWebserviceDataFromFileBatch", method = RequestMethod.POST)
		public DataResponse processWebserviceDataFromFile(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, 
				@RequestParam("filepath") String filePath, @RequestParam("webServiceConnectionId") Integer webServiceConnectionId,
				@RequestParam("tableName") String tableName, HttpServletRequest request, Locale locale){
			DataResponse dataResponse = new DataResponse();
			Message message = new Message();
			dataResponse.addMessage(message);
			ClientData clientData = new ClientData();
			try{
				String clientId = CommonUtils.getClientIDFromHeader(request);
				String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
				
				CustomRequest customRequest = new CustomRequest();
				customRequest.setClientId(clientId);
				customRequest.setUserId(userId);
				customRequest.setDeploymentType(deploymentType);
				customRequest.setBrowserDetails("");
				
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				metaDataFetch.processDataFromFileBatch(filePath, webServiceConnectionId, tableName, customRequest);
				
			}catch(Throwable t){
				LOGGER.error("",t);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Error occured while fetching data for IL "+ clientData.getIlConnectionMapping().getiLId() + "; Reason: " + MinidwServiceUtil.getErrorMessageString(t));
			}
			
			return dataResponse;
		}
		
		
		@RequestMapping(value="/processWebserviceFileCreation", method = RequestMethod.POST)
		public DataResponse processWebserviceDataFromFile(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, 
				@RequestBody ILConnectionMapping ilConnectionMapping, HttpServletRequest request, Locale locale){
			DataResponse dataResponse = new DataResponse();
			Message message = new Message();
			dataResponse.addMessage(message);
			ClientData clientData = new ClientData();
			JdbcTemplate clientAppdbJdbcTemplate = null;
			String generatedPath = null;
			
			try{
				
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
				clientAppdbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				
				String clientId = CommonUtils.getClientIDFromHeader(request);
				String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
				String csvFolderPath = CommonUtils.getCsvPath(request);
				
				CustomRequest customRequest = new CustomRequest();
				customRequest.setClientId(clientId);
				customRequest.setUserId(userId);
				customRequest.setDeploymentType(deploymentType);
				customRequest.setBrowserDetails(csvFolderPath);
				
				S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientId, clientAppdbJdbcTemplate);
				FileSettings fileSettings = packageService.getFileSettingsInfo(clientId, clientAppdbJdbcTemplate);
				
				generatedPath = metaDataFetch.createFileByConnection(ilConnectionMapping, s3BucketInfo, fileSettings, deploymentType, customRequest);
				

				SourceFileInfo sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(
						s3BucketInfo, new File(generatedPath), customRequest.getUserId(), 0,
						customRequest.getUserId(), 0, deploymentType, "", false,fileSettings.getFileEncryption(), csvFolderPath);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(sourceFileInfo.getFilePath());
				
			}catch(Throwable t){
				LOGGER.error("",t);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText("Error occured while fetching data for IL "+ clientData.getIlConnectionMapping().getiLId() + "; Reason: " + MinidwServiceUtil.getErrorMessageString(t));
			}  finally {
				if (generatedPath != null) {
					String basePath = FilenameUtils.getFullPathNoEndSeparator(generatedPath);
					try {
						FileUtils.forceDelete(new File(basePath));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			return dataResponse;
		}
		
		
		

}
