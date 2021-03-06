/**
 * 
 */
package com.datamodel.anvizent.data.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.client.data.to.csv.path.converter.ClientDBProcessor;
import com.anvizent.client.scheduler.util.PropertiesPlaceHolder;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.OAuthConstants;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CustomPackageForm;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.FileInfo;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.OAuth1;
import com.datamodel.anvizent.service.model.OAuth2;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebService;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.validator.CustomPackageFormValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import minidwclientws.WebServiceUtils;

/**
 * @author rajesh.anthari
 *
 */
@RestController("user_packageServiceDataRestController_2")
@RequestMapping("" + Constants.AnvizentURL.MINIDW_BASE_URL + "/package")
@CrossOrigin
public class PackageDataController_2 {
	@Autowired
	MessageSource messageSource;
	protected static final Log LOGGER = LogFactory.getLog(PackageDataController_2.class);
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	
	@Autowired
	@Qualifier("anvizentServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;

	@Autowired
	@Qualifier("commonServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilitiesCommon ;
	/*@Autowired
	private RestTemplate restTemplate;*/
	
	PropertiesPlaceHolder propertiesPlaceHolder = null;
	
	SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
	
	@Autowired private ServletContext servletContext;
	
	@Autowired
	CustomPackageFormValidator cpValidator;

	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value = "/sslConnectionTest", method = RequestMethod.POST)
	public DataResponse importSSLCert(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, 
			@RequestParam(value="sslClientKeyFile" ,required=false) MultipartFile sslClientKeyFile, 
			@RequestParam(value="sslClientCertFile",required=false) MultipartFile sslClientCertFile,
			@RequestParam(value="sslServerCaFile",required=false) MultipartFile sslServerCaFile,
			@RequestParam("sslEnable") Boolean sslEnable, 
			@RequestParam("databaseId") int databaseId, 
			@RequestParam("selectData") String ilConnection, 
			Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String sslClientKeyFilePath = null;
		String sslClientCertFilePath = null;
		String sslServerCaFilePath = null;
		String tempFolderName = null;
		Connection con = null;
		int conId = 0;
		try
		{
			JSONObject ilConJsonObj = new JSONObject(ilConnection);
			ObjectMapper iLconnectionMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			LinkedHashMap<String, Object> yourHashMap = new Gson().fromJson(ilConJsonObj.toString(), LinkedHashMap.class);
			ILConnection iLconnection = iLconnectionMapper.convertValue(yourHashMap, new TypeReference<ILConnection>()
			{
			});
			DataResponse dbIdDriverNameAndProtocal = restUtilities.getRestObject(request, "/getDbIdDriverNameAndProtocal/" + iLconnection.getDatabase().getId(), DataResponse.class, clientId, iLconnection.getDatabase().getId());
			if( dbIdDriverNameAndProtocal.getObject() != null )
			{
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dbIdDriverNameAndProtocal.getObject();
				Database database = mapper.convertValue(obj, new TypeReference<Database>()
				{
				});
				iLconnection.getDatabase().setConnector_id(database.getConnector_id());
				iLconnection.getDatabase().setDriverName(database.getDriverName());
				iLconnection.getDatabase().setProtocal(database.getProtocal());

				tempFolderName = Constants.TempUpload.getTempFileDir(clientId);
				conId = iLconnection.getConnectionId();
				if(conId == 0){
					
				if(sslClientKeyFile == null || sslClientCertFile == null || sslServerCaFile == null ){
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText("Please upload valid ssl authentication files.");
					messages.add(message);
					dataResponse.setMessages(messages);
					dataResponse.setObject(message);
					return dataResponse;
				}
					
				sslClientKeyFilePath = tempFolderName + sslClientKeyFile.getOriginalFilename();
				CommonUtils.createFile(tempFolderName, sslClientKeyFilePath, sslClientKeyFile);

				sslClientCertFilePath = tempFolderName + sslClientCertFile.getOriginalFilename();
				CommonUtils.createFile(tempFolderName, sslClientCertFilePath, sslClientCertFile);

				sslServerCaFilePath = tempFolderName + sslServerCaFile.getOriginalFilename();
				CommonUtils.createFile(tempFolderName, sslServerCaFilePath, sslServerCaFile);
 
				sslClientKeyFilePath= (sslClientKeyFilePath.indexOf('\\') != -1) ? sslClientKeyFilePath.replaceAll("\\\\", "/") : sslClientKeyFilePath;
				
				sslClientCertFilePath= (sslClientCertFilePath.indexOf('\\') != -1) ? sslClientCertFilePath.replaceAll("\\\\", "/") : sslClientCertFilePath;
				
				sslServerCaFilePath = (sslServerCaFilePath.indexOf('\\') != -1) ? sslServerCaFilePath.replaceAll("\\\\", "/") : sslServerCaFilePath;
				}else{
					//to get the files info
					DataResponse getSSLFilePathsInfo = restUtilities.getRestObject(request, "/getSSLFilePathsInfo/" + iLconnection.getConnectionId(), DataResponse.class, clientId, iLconnection.getConnectionId());
					if(getSSLFilePathsInfo != null && getSSLFilePathsInfo.getHasMessages() ){
						if ( getSSLFilePathsInfo.getMessages().get(0).getCode().equals("SUCCESS") ) {
							String sSLFilePathsInfo = (String)getSSLFilePathsInfo.getObject() ;
							String[] sslFilesInfo = sSLFilePathsInfo.split(",");
							tempFolderName  = sslFilesInfo[0];
							sslClientKeyFilePath =sslFilesInfo[1];
							sslClientCertFilePath =sslFilesInfo[2]; 
							sslServerCaFilePath=sslFilesInfo[3];
						}else{
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
							message.setText(messageSource.getMessage("anvizent.message.error.text.failedToConnectServer", new Object[] { iLconnection.getDatabase().getName() }, locale));
							messages.add(message);
							dataResponse.setMessages(messages);
						}
				   }
				}
				StringJoiner sslTrustKeyStoreFilePaths = new StringJoiner(",");
				sslTrustKeyStoreFilePaths.add(tempFolderName);
				sslTrustKeyStoreFilePaths.add(sslClientKeyFilePath);
				sslTrustKeyStoreFilePaths.add(sslClientCertFilePath);
				sslTrustKeyStoreFilePaths.add(sslServerCaFilePath);
				
				iLconnection.setSslTrustKeyStoreFilePaths(sslTrustKeyStoreFilePaths.toString());

				con = CommonUtils.connectDatabase(iLconnection);

				if( con != null )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyMadeTheConnection", new Object[] { iLconnection.getDatabase().getName() }, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
				else
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.error.text.failedToConnectServer", new Object[] { iLconnection.getDatabase().getName() }, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			}
		}
		catch ( InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e )
		{
			LOGGER.error("error while connectionTest()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(e.getLocalizedMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Exception e )
		{
			LOGGER.error("error while connectionTest()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(e.getLocalizedMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		finally
		{
			CommonUtils.closeConnction(con);
			if(conId == 0){
			if( tempFolderName != null )
			{
				try
				{
					FileUtils.deleteDirectory(new File(tempFolderName));
				}
				catch ( IOException e )
				{
					e.printStackTrace();
				}
			 }
			}
		}
		dataResponse.setObject(message);
		return dataResponse;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/createsSSLILConnection", method = RequestMethod.POST)
	public DataResponse createsSSLILConnection(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("sslClientKeyFile") MultipartFile sslClientKeyFile, @RequestParam("sslClientCertFile") MultipartFile sslClientCertFile,
			@RequestParam("sslServerCaFile") MultipartFile sslServerCaFile, @RequestParam("sslEnable") Boolean sslEnable, @RequestParam("databaseId") int databaseId, @RequestParam("selectData") String ilConnection, Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String sslClientKeyFilePath = null;
		String sslClientCertFilePath = null;
		String sslServerCaFilePath = null;
		String tempFolderName = null;
		Connection con = null;
		try
		{
			JSONObject ilConJsonObj = new JSONObject(ilConnection);
			ObjectMapper iLconnectionMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			LinkedHashMap<String, Object> yourHashMap = new Gson().fromJson(ilConJsonObj.toString(), LinkedHashMap.class);
			ILConnection iLconnection = iLconnectionMapper.convertValue(yourHashMap, new TypeReference<ILConnection>()
			{
			});
			DataResponse dbIdDriverNameAndProtocal = restUtilities.getRestObject(request, "/getDbIdDriverNameAndProtocal/" + iLconnection.getDatabase().getId(), DataResponse.class, clientId, iLconnection.getDatabase().getId());
			if( dbIdDriverNameAndProtocal.getObject() != null )
			{
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dbIdDriverNameAndProtocal.getObject();
				Database database = mapper.convertValue(obj, new TypeReference<Database>()
				{
				});
				iLconnection.getDatabase().setConnector_id(database.getConnector_id());
				iLconnection.getDatabase().setDriverName(database.getDriverName());
				iLconnection.getDatabase().setProtocal(database.getProtocal());

				if(sslClientKeyFile == null || sslClientCertFile == null || sslServerCaFile == null ){
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText("Please upload valid ssl authentication files.");
					messages.add(message);
					dataResponse.setMessages(messages);
					dataResponse.setObject(message);
					return dataResponse;
				}
				
				boolean isCloudAvailable = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
				iLconnection.setAvailableInCloud(isCloudAvailable);
				DataResponse createsILConnection = restUtilities.postRestObject(request, "/createsILConnection", iLconnection, clientId);
				  
				if(createsILConnection != null && createsILConnection.getHasMessages() ){
					if ( createsILConnection.getMessages().get(0).getCode().equals("SUCCESS") ) {
						
						tempFolderName = Constants.TempUpload.getTempFileDir(clientId);
						 
						sslClientKeyFilePath = tempFolderName + sslClientKeyFile.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, sslClientKeyFilePath, sslClientKeyFile);

						sslClientCertFilePath = tempFolderName + sslClientCertFile.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, sslClientCertFilePath, sslClientCertFile);

						sslServerCaFilePath = tempFolderName + sslServerCaFile.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, sslServerCaFilePath, sslServerCaFile);
						
						
						int conId = (int)createsILConnection.getObject() ;
						MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
						map.add("sslClientKeyFile", new FileSystemResource(sslClientKeyFilePath));
						map.add("sslClientCertFile", new FileSystemResource(sslClientCertFilePath));
						map.add("sslServerCaFile", new FileSystemResource(sslServerCaFilePath));
						map.add("sslEnable", sslEnable);
						map.add("databaseId", databaseId);
						map.add("conId", conId);
						return restUtilitieslb.postRestObject(request, "/insertSSLCert", map, clientId);
					}else{
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
						message.setText(createsILConnection.getMessages().get(0).getText());
						messages.add(message);
						dataResponse.setMessages(messages);
					}
				}
			}
		}
		catch ( InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e )
		{
			LOGGER.error("error while connectionTest()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(e.getLocalizedMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Exception e )
		{
			LOGGER.error("error while connectionTest()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(e.getLocalizedMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		finally
		{
			CommonUtils.closeConnction(con);
			if( tempFolderName != null )
			{
				try
				{
					FileUtils.deleteDirectory(new File(tempFolderName));
				}
				catch ( IOException e )
				{
					e.printStackTrace();
				}
			}
		}
		dataResponse.setObject(message);
		return dataResponse;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateSSLDatabaseConnection", method = RequestMethod.POST)
	public DataResponse updateSSLDatabaseConnection(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, 
			@RequestParam(value="sslClientKeyFile",required=false) MultipartFile sslClientKeyFile, 
			@RequestParam(value="sslClientCertFile",required=false) MultipartFile sslClientCertFile,
			@RequestParam(value="sslServerCaFile",required=false) MultipartFile sslServerCaFile,
			@RequestParam("sslEnable") Boolean sslEnable,
			@RequestParam("databaseId") int databaseId,
			@RequestParam("selectData") String ilConnection,
			Locale locale, HttpServletRequest request)
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String sslClientKeyFilePath = null;
		String sslClientCertFilePath = null;
		String sslServerCaFilePath = null;
		String tempFolderName = null;
		Connection con = null;
		ILConnection iLconnection=null;
		try
		{
			JSONObject ilConJsonObj = new JSONObject(ilConnection);
			ObjectMapper iLconnectionMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			LinkedHashMap<String, Object> yourHashMap = new Gson().fromJson(ilConJsonObj.toString(), LinkedHashMap.class);
			iLconnection = iLconnectionMapper.convertValue(yourHashMap, new TypeReference<ILConnection>()
			{
			});
			DataResponse dbIdDriverNameAndProtocal = restUtilities.getRestObject(request, "/getDbIdDriverNameAndProtocal/" + iLconnection.getDatabase().getId(), DataResponse.class, clientId, iLconnection.getDatabase().getId());
			if( dbIdDriverNameAndProtocal.getObject() != null )
			{
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dbIdDriverNameAndProtocal.getObject();
				Database database = mapper.convertValue(obj, new TypeReference<Database>()
				{
				});
				iLconnection.getDatabase().setConnector_id(database.getConnector_id());
				iLconnection.getDatabase().setDriverName(database.getDriverName());
				iLconnection.getDatabase().setProtocal(database.getProtocal());

				boolean isCloudAvailable = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
				iLconnection.setAvailableInCloud(isCloudAvailable);
				DataResponse updateDatabaseConnection = restUtilities.postRestObject(request, "/updateDatabaseConnection", iLconnection, clientId);
				  
				if(updateDatabaseConnection != null && updateDatabaseConnection.getHasMessages() ){
					if ( updateDatabaseConnection.getMessages().get(0).getCode().equals("SUCCESS") ) {
						
						if(sslClientKeyFile  == null && sslClientCertFile == null && sslServerCaFile == null){
							return updateDatabaseConnection;
						}else{
						
						tempFolderName = Constants.TempUpload.getTempFileDir(clientId);
						 
						sslClientKeyFilePath = tempFolderName + sslClientKeyFile.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, sslClientKeyFilePath, sslClientKeyFile);

						sslClientCertFilePath = tempFolderName + sslClientCertFile.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, sslClientCertFilePath, sslClientCertFile);

						sslServerCaFilePath = tempFolderName + sslServerCaFile.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, sslServerCaFilePath, sslServerCaFile);
						
						MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
						map.add("sslClientKeyFile", new FileSystemResource(sslClientKeyFilePath));
						map.add("sslClientCertFile", new FileSystemResource(sslClientCertFilePath));
						map.add("sslServerCaFile", new FileSystemResource(sslServerCaFilePath));
						map.add("sslEnable", sslEnable);
						map.add("databaseId", databaseId);
						map.add("conId", iLconnection.getConnectionId());
						return restUtilitieslb.postRestObject(request, "/insertSSLCert", map, clientId);
						
						}
					}else{
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
						message.setText(updateDatabaseConnection.getMessages().get(0).getText());
						messages.add(message);
						dataResponse.setMessages(messages);
					}
				}
			}
		}
		catch ( InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e )
		{
			LOGGER.error("error while connectionTest()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(e.getLocalizedMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		catch ( Exception e )
		{
			LOGGER.error("error while connectionTest()");
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(e.getLocalizedMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		finally
		{
			CommonUtils.closeConnction(con);
			if(sslClientKeyFile != null && sslClientCertFile != null && sslServerCaFile != null){
			if( tempFolderName != null )
			{
				try
				{
					FileUtils.deleteDirectory(new File(tempFolderName));
				}
				catch ( IOException e )
				{
					e.printStackTrace();
				}
			 }
			}
		}
		dataResponse.setObject(message);
		return dataResponse;
	}
	
		
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/connectionTest", method = RequestMethod.POST)
	public DataResponse connectionTest(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnection iLconnection, Locale locale,
			HttpServletRequest request) {

		DataResponse dataResponse = null;
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		if (iLconnection != null && clientId != null) {
			Connection con = null;
			try {
				dataResponse = new DataResponse();
				DataResponse dbIdDriverNameAndProtocal = restUtilities.getRestObject(request, "/getDbIdDriverNameAndProtocal/" + iLconnection.getDatabase().getId(), DataResponse.class, clientId,iLconnection.getDatabase().getId());
				if(dbIdDriverNameAndProtocal.getObject() != null){
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dbIdDriverNameAndProtocal.getObject();
					Database database = mapper.convertValue(obj, new TypeReference<Database>() {});
					iLconnection.getDatabase().setConnector_id(database.getConnector_id());
					iLconnection.getDatabase().setDriverName(database.getDriverName());
					iLconnection.getDatabase().setProtocal(database.getProtocal());  
				 
					con = CommonUtils.connectDatabase(iLconnection);
				}
				if (con != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.successfullyMadeTheConnection",
							new Object[] { iLconnection.getDatabase().getName() }, locale));	
					messages.add(message);
					dataResponse.setMessages(messages);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
					message.setText(messageSource.getMessage("anvizent.message.error.text.failedToConnectServer",
							new Object[] { iLconnection.getDatabase().getName() }, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				LOGGER.error("error while connectionTest()");
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(e.getLocalizedMessage());
				messages.add(message);
				dataResponse.setMessages(messages);
			} catch (Exception e) {
				LOGGER.error("error while connectionTest()");
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(e.getLocalizedMessage());
				messages.add(message);
				dataResponse.setMessages(messages);
			} finally {
				CommonUtils.closeConnction(con);
			}
			dataResponse.setObject(message);
		}
		return dataResponse;

	}

	@RequestMapping(value = "/getCustomTempTables/{packageId}", method = RequestMethod.GET)
	public DataResponse getCustomTempTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") String packageId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getCustomTempTables/{packageId}", clientId, packageId);
	}

	@RequestMapping(value = "/validateCustomTempTablesQuery/{industryId}/{packageId}", method = RequestMethod.POST)
	public DataResponse validateCustomTempTablesQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("industryId") String industryId, @PathVariable("packageId") String packageId, HttpServletRequest request,
			@RequestParam("queryvalue") String query, @RequestParam("tables") String tables, @RequestParam("isstaging") Boolean isStaging) {

		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("query", query);
		map.add("tables", tables);
		map.add("isstaging", isStaging);
		return restUtilities.postRestObject(request, "/validateCustomTempTablesQuery/{industryId}/{packageId}", map, clientId, industryId, packageId);

	}

	@RequestMapping(value = "/createsCustomTargetTable", method = RequestMethod.POST)
	public DataResponse createCustomTargetTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData,
			Locale locale, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/createsCustomTargetTable", clientData, clientId);

	}

	@RequestMapping(value = "/mapFileWithIL", method = RequestMethod.POST)
	public DataResponse mapFileWithIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			@RequestParam("industryId") Integer industryId, @RequestParam("packageId") Integer packageId, @RequestParam("dL_Id") Integer dL_Id,
			@RequestParam("iL_Id") Integer iL_Id, @RequestParam("flatFileType") String flatFileType, @RequestParam("delimeter") String delimeter,
			@RequestParam("isFirstRowHasColumnNames") String isFirstRowHasColumnNames, @RequestParam("file") MultipartFile file, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<Message>();
		dataResponse.addMessage(message);
		
		List<String> originalFileheaders = null;
		Map<String, Object> mappingFilesHeaders = new HashMap<>();
		File tempFile = null;
		boolean isComplated = false;
		try {
			if (file != null) {
				tempFile = CommonUtils.multipartToFile(file, Constants.Temp.getTempFileDir() + "fileMappingWithIL/");
				originalFileheaders = MinidwServiceUtil.getHeadersFromFile(tempFile.getAbsolutePath(), flatFileType, delimeter, null);
				if (originalFileheaders.size() > 0) {
					if (originalFileheaders.size() == 1 && originalFileheaders.get(0).length() == 0) {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
						if (tempFile != null) {
							tempFile.delete();
						}
						return dataResponse;
					}
					DataResponse ilTableStructureDataResponse = restUtilities.getRestObject(request, "/getIlTableStructure/{" + iL_Id + "}",clientId,iL_Id);
					if(ilTableStructureDataResponse != null && ilTableStructureDataResponse.getHasMessages() ){
						if ( ilTableStructureDataResponse.getMessages().get(0).getCode().equals("SUCCESS") ) {
							mappingFilesHeaders.put("originalFileheaders", originalFileheaders);
							mappingFilesHeaders.put("iLcolumnNames", ilTableStructureDataResponse.getObject());
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
							mappingFilesHeaders.put("originalFileName", file.getOriginalFilename());
							dataResponse.setObject(mappingFilesHeaders);
							isComplated = true;
						} else {
							message.setCode(ilTableStructureDataResponse.getMessages().get(0).getCode());
							message.setText(ilTableStructureDataResponse.getMessages().get(0).getText());
						}
					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.error.text.unableToFetchILcoloumnsList",
								null, locale));
					}

				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
				}

			}
		}  catch (IOException e) {
			LOGGER.error("",e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
		}  catch (Exception e) {
			LOGGER.error("",e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorOccurredWhileMapping", null, locale));
		} finally {
			if ( !isComplated ) {
				if (tempFile != null) {
					tempFile.delete();
				}
			}
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	@RequestMapping(value = "/processMappingFileWithIL", method = RequestMethod.POST)
	public DataResponse processMappingFileWithIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			@RequestParam("industryId") Integer industryId, @RequestParam("packageId") Integer packageId, @RequestParam("iLId") Integer iLId,
			@RequestParam("dLId") Integer dLId, @RequestParam("fileType") String fileType, @RequestParam("delimeter") String delimeter,
			@RequestParam("isFirstRowHasColoumnNames") Boolean isFirstRowHasColoumnNames, @RequestParam("originalFileName") String originalFileName,
			@RequestParam("iLColumnNames") String iLColumnNames, @RequestParam("selectedFileHeaders") String selectedFileHeaders,
			@RequestParam("dafaultValues") String dafaultValues, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		File tempFile = null;
		String sourceFilePath = null;
		try {
			sourceFilePath = Constants.Temp.getTempFileDir() + "fileMappingWithIL/" + originalFileName;
			List<String> iLColumnNamesList = CommonUtils.stringToList(iLColumnNames, ",");
			List<String> selectedFileHeadersList = CommonUtils.stringToList(selectedFileHeaders, ",");
			List<String> dafaultValuesList = CommonUtils.stringToList(dafaultValues, ",");
			String destFilePath = MinidwServiceUtil.processFileMappingWithIL(sourceFilePath, fileType, delimeter, null, iLColumnNamesList, selectedFileHeadersList,dafaultValuesList);

			if (StringUtils.isNotBlank(destFilePath)) {
				tempFile = new File(destFilePath);
				String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
                S3BucketInfo s3BucketInfo = getS3BucketInfo(request,clientId);//(S3BucketInfo) SessionHelper.getSesionAttribute(request,Constants.Config.S3_BUCKET_INFO);
                FileSettings fileSettings = (FileSettings) SessionHelper.getSesionAttribute(request,Constants.Config.FILE_SETTINGS_INFO);
				boolean isEncryptionRequired = fileSettings.getFileEncryption();
                if (s3BucketInfo == null) {
					s3BucketInfo = new S3BucketInfo();
				}
                User user = CommonUtils.getUserDetails(request, null, null);
                String userId = CommonUtils.decryptedUserId(user.getUserId());
				// upload to S3
				SourceFileInfo sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, tempFile,userId, packageId , user.getUserName(), null, deploymentType,isEncryptionRequired);
			    DataResponse uploadedFileInfoDataResponse = restUtilities.postRestObject(request, "/updateSourceFileInfo", sourceFileInfo, clientId);
				if(uploadedFileInfoDataResponse != null && uploadedFileInfoDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					dataResponse.setObject(uploadedFileInfoDataResponse.getObject());
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					message.setText(messageSource.getMessage("anvizent.message.success.text.sourceDetailsAreSaved", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				} else {
					dataResponse = uploadedFileInfoDataResponse;
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.fileUploadingFailed", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentDuplicateFileNameException ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (AmazonS3Exception ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.text.fileSavingFailedErrorDetails", null, locale) + ae.getLocalizedMessage() );
			messages.add(message);
			dataResponse.setMessages(messages);
		}catch (IOException e) {
			LOGGER.error("",e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		}catch (Exception e) {
			LOGGER.error("",e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			if (tempFile != null) {
				tempFile.delete();
			}
			if (sourceFilePath != null) {
				File file = new File(sourceFilePath);
				if (file.exists()) {
					file.delete();
				}
			}
		}
		return dataResponse;
	}
	@SuppressWarnings({ "unchecked" })
	private S3BucketInfo getS3BucketInfo(HttpServletRequest request, String clientId) throws Exception
	{
		S3BucketInfo s3BucketInfo = null;
		DataResponse s3BucketInfoDataResponse = restUtilities.getRestObject(request, "/getS3BucketInfoFromClientId", clientId);
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
				throw new Exception("s3 bucket info not found for client id:" + clientId);
			}
		}
		return s3BucketInfo;
	}
	/**
	 * 
	 * @param clientId
	 * @param industryId
	 * @return
	 */
	@RequestMapping(value = "/getDLsById/{dLId}", method = RequestMethod.GET)
	public DataResponse getDLById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("dLId") Integer dLId,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getDLsById/{dLId}", clientId, dLId);
	}

	/**
	 * 
	 * @param clientId
	 * @param iLId
	 * @return
	 */
	@RequestMapping(value = "/getILById/{iLId}", method = RequestMethod.GET)
	public ResponseEntity<ILInfo> getILById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("iLId") Integer iLId,
			HttpServletRequest request) {

		return restUtilities.getRestEntity(request, "/getILById/{iLId}", ILInfo.class, clientId, iLId);
	}

	@RequestMapping(value = "/getTargetTablesStructure/{packageId}/{industryId}", method = RequestMethod.GET)
	public DataResponse getTargetTablleStructure(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") String packageId,
			@PathVariable("industryId") String industryId, HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getTargetTablesStructure/{packageId}/{industryId}", clientId, packageId, industryId);

	}

	@RequestMapping(value = "/saveCustomTargetDerivativeTable/{industryId}/{packageId}", method = RequestMethod.POST)
	public DataResponse saveCustomTargetDerivativeTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId, HttpServletRequest request,
			@RequestParam("queryvalue") String query, @RequestParam("table") String tablename, @RequestParam("targetTable") String targetTable,
			@RequestParam("tableid") String tableid, @RequestParam("ccols") String ccols, Locale locale) {

		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("query", query);
		map.add("table", tablename);
		map.add("targetTable", targetTable);
		map.add("tableid", tableid);
		map.add("ccols", ccols);

		return restUtilities.postRestObject(request, "/saveCustomTargetDerivativeTable/{industryId}/{packageId}", map, clientId, industryId, packageId);

	}

	@RequestMapping(value = "/deleteConnectionMapping/{packageId}/{mappingId}", method = RequestMethod.POST)
	public DataResponse deleteConnectionMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") String packageId,
			@PathVariable("mappingId") String mappingId, HttpServletRequest request) {

		return restUtilities.postRestObject(request, "/deleteConnectionMapping/{packageId}/{mappingId}", new LinkedMultiValueMap<>(), clientId, packageId,
				mappingId);

	}

	@RequestMapping(value = "/showCustomPackageTablesStatus//{packageId}/{industryId}", method = RequestMethod.GET)
	public DataResponse showCustomPackageTablesStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId, HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/showCustomPackageTablesStatus/{packageId}/{industryId}", DataResponse.class, clientId, packageId,
				industryId);

	}

	/**
	 * in case of files having same columns option 'Yes'
	 * 
	 * @param clientId
	 * @param packageId
	 * @return
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getColumnHeaders/{packageId}", method = RequestMethod.GET)
	public DataResponse getFileColumns(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") Integer packageId,
			HttpServletRequest request, Locale locale) {


		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		DataResponse dataRes = restUtilities.getRestObject(request, "/getColumnHeaders/{packageId}", clientId, packageId);
		Map<String, Object> map = (Map<String, Object>) dataRes.getObject();
		ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		FileInfo fileHeaderInfo = mapper1.convertValue(map, new TypeReference<FileInfo>() {
		});

		if (fileHeaderInfo != null) {
			dataResponse.setObject(fileHeaderInfo);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			messages.add(message);
			dataResponse.setMessages(messages);
		} else {

			Map<String, Object> data = restUtilities.getRestObject(request, "/getILsConnectionMappingInfoByPackage/{" + packageId + "}", Map.class, clientId,
					packageId);
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) data.get("object");
			List<ClientData> mappingInfoList = mapper.convertValue(l, new TypeReference<List<ClientData>>() {
			});

			for (ClientData mappingInfo : mappingInfoList) {
				if (!mappingInfo.getIlConnectionMapping().getIsFlatFile()) {
					if (mappingInfo.getIlConnectionMapping().getiLConnection() != null) {
						try {
							ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
							List<String> columnHeadersList = CommonUtils.getColumnHeadersByQuery(ilConnectionMapping);
							String fileHeaders = "";
							int i = 0;
							for (String cloumnHeader : columnHeadersList) {
								fileHeaders += cloumnHeader;
								if (i < columnHeadersList.size() - 1) {
									fileHeaders += ",";
								}
								i++;
							}
							FileInfo fileInfo = new FileInfo();
							fileInfo.setFileHeaders(fileHeaders);
							dataResponse.setObject(fileInfo);
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
							messages.add(message);
							dataResponse.setMessages(messages);
						} catch (Exception e) {
							LOGGER.error("",e);
							message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
							message.setText(e.getMessage());
							messages.add(message);
							dataResponse.setMessages(messages);
						}
					}
				}
				break;
			}
		}
		return dataResponse;
	}

	// deleteIlSource file form il_connection_mapping table and amazon server
	@RequestMapping(value = "/deleteIlSource/{connectionMappingId}/{packageId}/{iLId}/{dLId}", method = RequestMethod.POST)
	public DataResponse deleteIlSource(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionMappingId") String connectionMappingId, @PathVariable("packageId") String packageId, @PathVariable("iLId") String iLId,
			@PathVariable("dLId") String dLId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/deleteIlSource/{connectionMappingId}/{packageId}/{iLId}/{dLId}", clientId, connectionMappingId, packageId,
				iLId, dLId);
	}

	@RequestMapping(value = "/updateIsActiveStatusIlSource/{connectionMappingId}/{isActiveRequired}", method = RequestMethod.POST)
	public DataResponse updateIsActiveStatusIlSource(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionMappingId") String connectionMappingId, @PathVariable("isActiveRequired") Boolean isActiveRequired, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/updateIsActiveStatusIlSource/{connectionMappingId}/{isActiveRequired}", clientId, connectionMappingId, isActiveRequired);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getCustomFileTempTableMappings/{packageId}", method = RequestMethod.GET)
	public List<Table> getCustomFileTempTableMappings(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getCustomFileTempTableMappings/{packageId}", (Class<List<Table>>) (Object) List.class, clientId,
				packageId);

	}

	// get table preview
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getTablePreview", method = RequestMethod.POST)
	public DataResponse getTablePreview(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnectionMapping ilConnectionMapping,
			Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<Object> tablePreviewList = null;
		ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if (ilConnectionMapping != null) {
			if(ilConnectionMapping.getiLConnection() == null || !(ilConnectionMapping.getiLConnection().isDdLayout() || ilConnectionMapping.getiLConnection().isApisData())){
			int packageID = ilConnectionMapping.getPackageId();
			if (ilConnectionMapping.getConnectionMappingId() != null) {
				DataResponse data1 = restUtilities.getRestObject(request, "/getILConnectionMappingInfoByMappingId/{Package_id}/{mapping_Id}",
						DataResponse.class, clientId, packageID, ilConnectionMapping.getConnectionMappingId());
				Map<String, Object> il = (Map<String, Object>) data1.getObject();

				ILConnectionMapping ilConnectionMapping2 = mapper.convertValue(il, new TypeReference<ILConnectionMapping>() {
				});
				ilConnectionMapping = ilConnectionMapping2;
			}
			}
			if (StringUtils.isNotBlank(ilConnectionMapping.getiLquery())) {
				try {
					/**
					 * incremental upload query
					 */
					if(ilConnectionMapping.getiLConnection().isDdLayout() || ilConnectionMapping.getiLConnection().isApisData()){
			             DataResponse ddLayoutDataResponse = restUtilities.getRestObject(request, "/getClientSchemaName", clientId);
						if (ddLayoutDataResponse != null && ddLayoutDataResponse.getHasMessages() && ddLayoutDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
							   LinkedHashMap<String, Object>  clientDbDetails = ( LinkedHashMap<String, Object> )ddLayoutDataResponse.getObject();
							   if(clientDbDetails != null){
							   ILConnection iLConnection = new  ILConnection();
							   Database database = new Database();
							   database.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
							   database.setId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
							   database.setProtocal(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
							   database.setDriverName(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
						       iLConnection.setServer(clientDbDetails.get("region_hostname").toString()+":"+clientDbDetails.get("region_port").toString()+"/"+clientDbDetails.get("clientdb_schema").toString());
						       iLConnection.setUsername(clientDbDetails.get("clientdb_username").toString());
						       iLConnection.setPassword(clientDbDetails.get("clientdb_password").toString());
						       iLConnection.setDatabase(database);
							   ilConnectionMapping.setiLConnection(iLConnection);
							   }
						   }
					}else{
					if (ilConnectionMapping.getConnectionMappingId() == null) {
						int connectionId = ilConnectionMapping.getiLConnection().getConnectionId();
						DataResponse dataiLConnection = restUtilities.getRestObject(request, "/getILsConnectionById/" + connectionId, clientId, connectionId);
						LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataiLConnection.getObject();
						ILConnection ilConnection = mapper.convertValue(obj, new TypeReference<ILConnection>() {
						});
						ilConnectionMapping.setiLConnection(ilConnection);
					}
				   }
					tablePreviewList = CommonUtils.getTablePreview(ilConnectionMapping);

					dataResponse.setObject(tablePreviewList);
				} catch (AnvizentRuntimeException ae) {
					LOGGER.error("",ae);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(MinidwServiceUtil.getErrorMessageString(ae));
					messages.add(message);
					dataResponse.setMessages(messages);
				} catch (Exception e) {
					LOGGER.error("",e);
					LOGGER.error("error while getTablePreview() ", e);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(MinidwServiceUtil.getErrorMessageString(e));
					messages.add(message);
					dataResponse.setMessages(messages);

				}
			}
		}
		return dataResponse;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAllTables", method = RequestMethod.GET)
	public List<String> getAllTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getAllTables", (Class<List<String>>) (Object) List.class, clientId);

	}

	@RequestMapping(value = "/getIlEpicorQuery/{iLId}/{connectionId}", method = RequestMethod.GET)
	public DataResponse getIlEpicorQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("iLId") String IlId,
			@PathVariable("connectionId") Integer connectionId, HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getIlEpicorQuery/{iLId}/{connectionId}", clientId, IlId, connectionId);

	}

	/**
	 * file uploading by windows app in case of run now
	 * 
	 */

	/**
	 * 
	 * soft deleting the package( i.e. updating isActive = 0 for package.)
	 * 
	 * 
	 */

	@RequestMapping(value = "/disablePackage", method = RequestMethod.POST)
	public DataResponse disablePackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("packageIds") List<String> packageIds,
			HttpServletRequest request) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("packageIds", String.join(",", packageIds));
		return restUtilities.postRestObject(request, "/disablePackage",map,clientId);

	}

	/**
	 * 
	 * @param clientId
	 * @return
	 */

	@RequestMapping(value = "/deleteILConnection/{connectionId}", method = RequestMethod.POST)
	public DataResponse deleteILConnection(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("connectionId") String connectionId,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/deleteILConnection/{connectionId}", new LinkedMultiValueMap<>(), clientId, connectionId);
	}

	@RequestMapping(value = "/viewErrorLogs/{batchId}", method = RequestMethod.GET)
	public DataResponse viewErrorLogs(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("batchId") String batchId,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/viewErrorLogs/{batchId}", userId, batchId);
	}

	@RequestMapping(value = "/deleteSameDatasetCustomTablesBYPackageId/{packageId}", method = RequestMethod.POST)
	public DataResponse deleteSameDatasetCustomTablesBYPackageId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, HttpServletRequest request, Locale locale) {

		return restUtilities.postRestObject(request, "/deleteSameDatasetCustomTablesBYPackageId/{packageId}", new LinkedMultiValueMap<>(), clientId, packageId);

	}

	@RequestMapping(value = "/deleteSameDatasetCustomTablesBYTableId/{packageId}/{targetTableId}/{tableId}", method = RequestMethod.POST)
	public DataResponse deleteSameDatasetCustomTablesBYTableId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("targetTableId") String targetTableId, @PathVariable("tableId") String tableId,
			HttpServletRequest request, Locale locale) {

		return restUtilities.postRestObject(request, "/deleteSameDatasetCustomTablesBYTableId/{packageId}/{targetTableId}/{tableId}",
				new LinkedMultiValueMap<>(), clientId, packageId, targetTableId, tableId);

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateIlSource", method = RequestMethod.POST)
	public DataResponse updateIlSource(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnectionMapping ilConnectionMapping,
			Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message= new Message();
		dataResponse.addMessage(message);
		
		if (ilConnectionMapping.getPackageId() != null & ilConnectionMapping.getPackageId() != 0) {

			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			if (StringUtils.isNotBlank(ilConnectionMapping.getiLquery())
					|| (ilConnectionMapping.getIsIncrementalUpdate() != null
							&& ilConnectionMapping.getIsIncrementalUpdate()
							&& StringUtils.isNotBlank(ilConnectionMapping.getMaxDateQuery()))) {
				try {
					Integer connectionId = ilConnectionMapping.getiLConnection().getConnectionId();
					if (connectionId != null) {
						DataResponse dataiLConnection = restUtilities.getRestObject(request,
								"/getILsConnectionById/" + connectionId, clientId, connectionId);
						LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataiLConnection.getObject();
						ILConnection iLConnection = mapper.convertValue(obj, new TypeReference<ILConnection>() {
						});
						ilConnectionMapping.setiLConnection(iLConnection);
					}

					List<String> headers = CommonUtils.getQueryHeaders(ilConnectionMapping);

					int packageId = ilConnectionMapping.getPackageId();
					
					StringBuilder cols = new StringBuilder();
					for (String column : headers) {
						cols.append(column.trim().replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
						cols.append(",");
					}
					cols = cols.deleteCharAt(cols.length()-1);
					// save file headers
					
					FileInfo fileInfo = new FileInfo();
					fileInfo.setPackageId(packageId);

					fileInfo.setFileHeaders(cols.toString());
					fileInfo.setHeaders(headers);
					fileInfo.setFileType(Constants.FileType.CSV);
					fileInfo.setDelimeter(",");
					SourceFileInfo sourceFileInfo = new SourceFileInfo();
					sourceFileInfo.setFileInfo(fileInfo);
					sourceFileInfo.setIlConnectionMappingId(ilConnectionMapping.getConnectionMappingId());
					
					DataResponse uploadDependancyForCustompackageDataResponse = restUtilities.postRestObject(request, "/checkUploadDependancyForCustompackage", sourceFileInfo, clientId);
					if (!MinidwServiceUtil.isValidateDataResponse(uploadDependancyForCustompackageDataResponse, false)) {
						
						if (uploadDependancyForCustompackageDataResponse.getMessages() != null && uploadDependancyForCustompackageDataResponse.getMessages().
								get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.PROCEED_FOR_MAPPING)) {
							DataResponse updateIlSourceResponse = restUtilities.postRestObject(request, "/updateIlSource", ilConnectionMapping, clientId);
							if (MinidwServiceUtil.isValidateDataResponse(updateIlSourceResponse, false)) {
								updateIlSourceResponse.getMessages().get(0).getText();
								Message message2 = uploadDependancyForCustompackageDataResponse.getMessages().get(0);
								message2.setText(updateIlSourceResponse.getMessages().get(0).getText() + "\n" + message2.getText());
								return uploadDependancyForCustompackageDataResponse;
							} else {
								return updateIlSourceResponse;
							}
						} else {
							return uploadDependancyForCustompackageDataResponse;
						}
						
					}


				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
					LOGGER.error("",e);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(e.getLocalizedMessage());
				}
			}

		}
		
		return restUtilities.postRestObject(request, "/updateIlSource", ilConnectionMapping, clientId);

	}

	@RequestMapping(value = "/getILConnectionMappingInfoByMappingId/{Package_id}/{mapping_Id}", method = RequestMethod.GET)
	public DataResponse getILConnectionMappingInfoByMappingId(@PathVariable("mapping_Id") String mapping_Id, @PathVariable("Package_id") String Package_id,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getILConnectionMappingInfoByMappingId/{Package_id}/{mapping_Id}", clientId, Package_id, mapping_Id);

	}

	@RequestMapping(value = "/getClientsDLs", method = RequestMethod.GET)
	public DataResponse getClientsDLs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getClientsDLs", clientId);

	}

	@RequestMapping(value = "/getTargetTables/{industryId}", method = RequestMethod.GET)
	public DataResponse getTargetTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("industryId") String industryId,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getTargetTables/{industryId}", clientId, industryId);

	}


	@RequestMapping(value = "/derivedTableMappingInfo/{packageId}/{industryId}", method = RequestMethod.GET)
	public DataResponse derivedtableMappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") String packageId,
			@PathVariable("industryId") String industryId, HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/derivedTableMappingInfo/{packageId}/{industryId}", clientId, packageId, industryId);

	}

	@RequestMapping(value = "/updateIsFromDerivedTables/{packageId}/{isFromDerivedTables}", method = RequestMethod.POST)
	public DataResponse updateIsFromDerivedTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request,
			@PathVariable("packageId") String packageId, @PathVariable("isFromDerivedTables") Boolean IsFromDerivedTables) {

		return restUtilities.getRestObject(request, "/updateIsFromDerivedTables/{packageId}/{isFromDerivedTables}", clientId, packageId, IsFromDerivedTables);

	}

	@RequestMapping(value = "/checkFordownloadIlTemplate/{il_id}/{templateType}", method = RequestMethod.GET)
	public DataResponse checkFordownloadIlTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("il_id") String il_id, @PathVariable("templateType") String templateType) {


		return restUtilities.getRestObject(request, "/checkFordownloadIlTemplate/{il_id}/{templateType}", clientId, il_id, templateType);

	}

	@RequestMapping(value = "/downloadIlTemplate/{il_id}/{templateType}", method = RequestMethod.GET)
	public DataResponse downloadIlTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("il_id") String il_id, @PathVariable("templateType") String templateType) {


		try {
			ResponseEntity<byte[]> templateFileResponse = restUtilities.getRestEntity(request, "/downloadIlTemplatenew/{il_id}/{templateType}", byte[].class,
					clientId, il_id, templateType);
			byte[] templateFile = templateFileResponse.getBody();
			if (templateFile != null && templateFile.length > 0) {
				String fileName = Constants.Temp.getTempFileDir() + templateFileResponse.getHeaders().get("filename").get(0);
				;
				File targetFile = new File(fileName);
				OutputStream outStream = new FileOutputStream(targetFile);
				outStream.write(templateFile);
				outStream.close();
				CommonUtils.sendFIleToStream(fileName, request, response);
			}

		} catch (Exception e) {
			LOGGER.error("",e);
		}

		return null;

	}

	@RequestMapping(value = "/getMappedModulesForStandardPackage/{packageId}", method = RequestMethod.GET)
	public DataResponse getMappedModulesForStandardPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("packageId") Integer packageId, HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getMappedModulesForStandardPackage/{packageId}", userId, packageId);

	}

	@RequestMapping(value = "/getTargetTableQuery/{packageId}", method = RequestMethod.GET)
	public DataResponse getTargetTableQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") Integer packageId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getTargetTableQuery/{packageId}", clientId, packageId);

	}

	@RequestMapping(value = "/defaultILIncrementalQuery/{il_id}/{connection_id}", method = RequestMethod.GET)
	public DataResponse getSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("il_id") Integer il_id,
			@PathVariable("connection_id") Integer connection_id, HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/defaultILIncrementalQuery/{il_id}/{connection_id}", userId, il_id, connection_id);

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getSchemaRelatedTablesAndColumns", method = RequestMethod.POST)
	public DataResponse getSchemaRelatedTablesAndColumns(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnection iLConnection, Locale locale, HttpServletRequest request) {


		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int connectionId = iLConnection.getConnectionId();
		String schemaName = iLConnection.getDatabase().getSchema();
		List<Table> tablesList = new ArrayList<>();
		try {
			
			//get table list for DDlayout
			if(iLConnection.isDdLayout() || iLConnection.isApisData()){
             DataResponse ddLayoutDataResponse = restUtilities.getRestObject(request, "/getClientSchemaName", clientId);
			if (ddLayoutDataResponse != null && ddLayoutDataResponse.getHasMessages() && ddLayoutDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				   LinkedHashMap<String, Object>  clientDbDetails = ( LinkedHashMap<String, Object> )ddLayoutDataResponse.getObject();
				   if(clientDbDetails != null){
				   Database database = new Database();
				   database.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setProtocal(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
				   database.setDriverName(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
			       iLConnection.setServer(clientDbDetails.get("region_hostname").toString()+":"+clientDbDetails.get("region_port").toString()+"/"+clientDbDetails.get("clientdb_schema").toString());
			       iLConnection.setUsername(clientDbDetails.get("clientdb_username").toString());
			       iLConnection.setPassword(clientDbDetails.get("clientdb_password").toString());
				   iLConnection.setDatabase(database);
				   }
			   }
			}else{
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				DataResponse dataiLConnection = restUtilities.getRestObject(request, "/getILsConnectionById/" + connectionId, clientId, connectionId);
				LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataiLConnection.getObject();
				iLConnection = mapper.convertValue(obj, new TypeReference<ILConnection>() {
				});
			}
			tablesList = CommonUtils.getSchemaRelatedTablesAndColumns(iLConnection, schemaName);

			if (!tablesList.isEmpty()) {
				dataResponse.setObject(tablesList);
				message.setCode("SUCCESS");
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode("ERROR");
				message.setText(messageSource.getMessage("anvizent.message.error.text.tablesNotFoundFor",
						new Object[] { schemaName }, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (AnvizentRuntimeException ae) {
			LOGGER.error("",ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.label.unableToGetSchemaRelatedTables", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			LOGGER.error("error while getSchemaRelatedTablesAndColumns()");
			LOGGER.error("",e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.label.errorWhileGettingTableDetails", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		return dataResponse;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getTableRelatedColumns", method = RequestMethod.POST)
	public DataResponse getTableRelatedColumns(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnection iLConnection,
			Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int connectionId = iLConnection.getConnectionId();
		String schemaName = iLConnection.getDatabase().getSchema();
		String tableName = iLConnection.getDatabase().getTable().getTableName();
		List<Column> columns = null;
		try {
			//get table list for DDlayout
			if(iLConnection.isDdLayout() || iLConnection.isApisData()){
             DataResponse ddLayoutDataResponse = restUtilities.getRestObject(request, "/getClientSchemaName", clientId);
			if (ddLayoutDataResponse != null && ddLayoutDataResponse.getHasMessages() && ddLayoutDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				   LinkedHashMap<String, Object>  clientDbDetails = ( LinkedHashMap<String, Object> )ddLayoutDataResponse.getObject();
				   if(clientDbDetails != null){
				   Database database = new Database();
				   database.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setProtocal(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
				   database.setDriverName(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
			       iLConnection.setServer(clientDbDetails.get("region_hostname").toString()+":"+clientDbDetails.get("region_port").toString()+"/"+clientDbDetails.get("clientdb_schema").toString());
			       iLConnection.setUsername(clientDbDetails.get("clientdb_username").toString());
			       iLConnection.setPassword(clientDbDetails.get("clientdb_password").toString());
				   iLConnection.setDatabase(database);
				   
				   }
			   }
			}else{
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataResponse dataiLConnection = restUtilities.getRestObject(request, "/getILsConnectionById/{connectionId}", clientId, connectionId);
			LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataiLConnection.getObject();
			iLConnection = mapper.convertValue(obj, new TypeReference<ILConnection>() {
			});
			}
			columns = CommonUtils.getTableRelatedColumns(iLConnection, schemaName, tableName);
			if (!columns.isEmpty()) {
				dataResponse.setObject(columns);
				message.setCode("SUCCESS");
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (AnvizentRuntimeException ae) {
			LOGGER.error("",ae);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.label.unableToGetTableRelatedColumns", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			LOGGER.error("error while getTableRelatedColumns()");
			LOGGER.error("",e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.label.errorWhileGettingTableRelatedColumns", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		return dataResponse;

	}

	@RequestMapping(value = "/purgeUserTables", method = RequestMethod.POST)
	public DataResponse purgeUserTables(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody List<Table> table, Locale locale,
			HttpServletRequest request) throws AnvizentRuntimeException {
		return restUtilities.postRestObject(request, "/purgeUserTables", table, userId);
	}

	@RequestMapping(value = "/getAuthenticationObject", method = RequestMethod.POST)
	public DataResponse getAuthenticationObject(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestParam("authUrl") String authUrl,
			@RequestParam("methodType") String methodType, HttpServletRequest request) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("authUrl", authUrl);
		map.add("methodType", methodType);

		return restUtilities.postRestObject(request, "/getAuthenticationObject", map, clientId);
	}

	@RequestMapping(value = "/deleteCustomTargetTable/{packageId}", method = RequestMethod.GET)
	public DataResponse deleteCustomTargetTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") Integer packageId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/deleteCustomTargetTable/{packageId}", clientId, packageId);
	}

	@RequestMapping(value = "/filterPackagesOnIsActive", method = RequestMethod.GET)
	public DataResponse filterPackagesOnIsActive(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/filterPackagesOnIsActive", clientId);
	}

	@RequestMapping(value = "/uploadingFilesFromClientDatabase/{packageId}", method = RequestMethod.GET)
	public @ResponseBody DataResponse uploadingFilesFromDatabseForYesOption(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("packageId") Integer packageId, Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = uploadingFilesFromDatabse(packageId, userId, -1, request, locale);

		return dataResponse;
	}

	@RequestMapping(value = "/uploadingFilesFromClientDatabaseForNoOption/{packageId}", method = RequestMethod.GET)
	public @ResponseBody DataResponse uploadingFilesFromClientDatabaseForNoOption(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("packageId") Integer packageId, Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = uploadingFilesFromDatabse(packageId, userId, 10, request, locale);

		return dataResponse;
	}

	@SuppressWarnings("unchecked")
	private DataResponse uploadingFilesFromDatabse(Integer packageId, String encryptedUserId, int limit, HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		Package userPackage = null;
		if (packageId != null) {

			try {
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", encryptedUserId, packageId);
				if (userDataResponse != null && userDataResponse.getHasMessages() && userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
					userPackage = mapper.convertValue(map, new TypeReference<Package>() {
					});
				}

				if (userPackage != null) {
					Map<String, Object> data = restUtilities.getRestObject(request, "/getILsConnectionMappingInfoByPackage/{" + packageId + "}", Map.class,
							encryptedUserId, packageId);
					List<?> l = (List<Map<String, Object>>) data.get("object");
					String userId = "";
					try {
						userId = EncryptionServiceImpl.getInstance().decrypt(encryptedUserId);
						String[] userIdAndLocale = StringUtils.split(userId, '#');
						userId = userIdAndLocale[0];
					} catch (Exception e) {
						LOGGER.error("unable to decrypt userId :: " + e.getLocalizedMessage());
					}

					List<ClientData> mappingInfoList = mapper.convertValue(l, new TypeReference<List<ClientData>>() {
					});
					for (ClientData mappingInfo : mappingInfoList) {
						if (!mappingInfo.getIlConnectionMapping().getIsFlatFile() && !mappingInfo.getIlConnectionMapping().getIsWebservice()) {
							if (mappingInfo.getIlConnectionMapping().getiLConnection() != null) {
								
								dataResponse = fileUploadByClientApp(encryptedUserId, userId, packageId, mappingInfo, limit, locale, request);
								
								if (dataResponse != null && dataResponse.getHasMessages()) {
									if (dataResponse.getMessages().get(0).getCode().equals("ERROR"))
										return dataResponse;
								}
							}
						}

					}
				}
			} catch (Exception e) {
				LOGGER.error("",e);
				message.setCode("ERROR");
				message.setText(e.getMessage());
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}

		return dataResponse;
	}

	private DataResponse fileUploadByClientApp(String encryptedUserId, String userId, Integer packageId, ClientData mappingInfo, int limit, Locale locale,
			HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);

		File tempFile = null;
		User user = CommonUtils.getUserDetails(request, null, null);
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		String directoryPath = null;
		try {
			String query = mappingInfo.getIlConnectionMapping().getiLquery();
			if (StringUtils.isBlank(query)) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.label.QueryShouldNoBeEmptyPleaseCheckTheSourceQueries", null, locale));
				dataResponse.addMessage(message);
				return dataResponse;
			}
			
			ClientDBProcessor clientDBProcessor = new ClientDBProcessor();
			boolean isIncrementalUpdate = mappingInfo.getIlConnectionMapping().getIsIncrementalUpdate();
			String incrementalUpdateDate = null;
			int iLId = mappingInfo.getIlConnectionMapping().getiLId();
			if ( isIncrementalUpdate ) {
				DataResponse data1 = restUtilities.getRestObject(request, "/dateForIncrementalUpdateQuery?ilId={iLID}&dataSourceId={dataSourceID}&typeOfSource=database&packageSourceMappingId={packageSourceMappingId}",
						DataResponse.class, encryptedUserId, iLId, mappingInfo.getIlConnectionMapping().getiLConnection().getConnectionId(),mappingInfo.getIlConnectionMapping().getConnectionMappingId());
				incrementalUpdateDate = (String) data1.getObject();
			}
			FileSettings fileSettings = (FileSettings) SessionHelper.getSesionAttribute(request,Constants.Config.FILE_SETTINGS_INFO);
			boolean isMultiPartEnabled = fileSettings.getMultiPartEnabled();
			boolean isEncryptionRequired = fileSettings.getFileEncryption();
			if ( limit != -1 ) {
				isMultiPartEnabled = false;
			}
			ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
			String s3LogicalDirPath = "datafiles_U" + user.getClientId() + "_P" + ilConnectionMapping.getPackageId() + "_M" + ilConnectionMapping.getConnectionMappingId();
			Integer ilId = ilConnectionMapping.getiLId();
			if (ilId != null && ilId != 0) {
				s3LogicalDirPath += "_IL"+ilId + "_" +"DL"+ilConnectionMapping.getdLId() ;
			}
			s3LogicalDirPath += "_" + CommonUtils.generateUniqueIdWithTimestamp();			
			HashMap<String, Object> processedMap = clientDBProcessor.processClientDBData(getCsvFolderPath(), incrementalUpdateDate, mappingInfo.getIlConnectionMapping(), mappingInfo.getIlConnectionMapping().getiLConnection().getDatabase().getConnector_id(), isIncrementalUpdate, limit == -1? null:limit, isMultiPartEnabled, fileSettings.getNoOfRecordsPerFile());
			
			Path tempDir = (Path) processedMap.get("tempDir");
			String maxDate = (String) processedMap.get("maxDate");
			mappingInfo.getIlConnectionMapping().setClientId(userId);
			
			boolean isCloudAvailable = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
			if(mappingInfo.getIlConnectionMapping().getiLConnection() != null){
				mappingInfo.getIlConnectionMapping().getiLConnection().setWebApp(isCloudAvailable);
			}
				
				File originalFile = null;
				
				if (isMultiPartEnabled) {
					originalFile = tempDir.toFile();
					directoryPath = originalFile.getAbsolutePath();
				} else {
					Path tempFileName = (Path) processedMap.get("tempFile");
					tempFile = tempFileName.toFile();
					directoryPath = FilenameUtils.getFullPath(tempFile.getAbsolutePath());
					s3LogicalDirPath += ".csv";
					originalFile = new File(directoryPath + s3LogicalDirPath);
					tempFile.renameTo(originalFile);
					
				}
				
				LOGGER.info("deploymentType - > " + deploymentType);
				S3BucketInfo s3BucketInfo = getS3BucketInfo( request,encryptedUserId );//(S3BucketInfo) SessionHelper.getSesionAttribute(request,Constants.Config.S3_BUCKET_INFO);
				if ( s3BucketInfo == null ) {
					s3BucketInfo = new S3BucketInfo();
					s3BucketInfo.setId(0);
				}
				SourceFileInfo sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, originalFile, userId, ilConnectionMapping.getPackageId() , user.getUserName(), ilConnectionMapping.getConnectionMappingId(), deploymentType,s3LogicalDirPath ,isMultiPartEnabled,isEncryptionRequired);
				// added in /fileUploadByClientsApp
				int rowCount = 0;
				if (limit == -1) {
					rowCount = clientDBProcessor.getSourceCount();
					sourceFileInfo.setRowCount(rowCount);
					sourceFileInfo.setIncrementalDateValue(maxDate);
				}
				sourceFileInfo.setMultiPartFile(isMultiPartEnabled);
				sourceFileInfo.setS3BucketInfo(s3BucketInfo);
				
				String startTime = startDateFormat.format(new Date());
				LOGGER.info("file uploading started " + startTime + " for IL Mapping Id " + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
				boolean isSourceInfoUpdated = false;
				DataResponse uploadDataResponse = restUtilities.postRestObject(request, "/updateSourceFileInfo", sourceFileInfo, encryptedUserId);
				if (uploadDataResponse != null && uploadDataResponse.getHasMessages() ) {
					 if(uploadDataResponse.getMessages().get(0).getCode().equals("SUCCESS") && uploadDataResponse.getObject() != null){
						 sourceFileInfo.setSourceFileId((Integer)uploadDataResponse.getObject());
						 isSourceInfoUpdated = true;
					 }
				}
				if (!isSourceInfoUpdated) {
					throw new Exception("Unable to update the source info mapping");
				}
				uploadDataResponse = restUtilities.postRestObject(request, "/fileUploadByClientsApp", sourceFileInfo, encryptedUserId);
				if (uploadDataResponse == null ) {
					throw new Exception("Unable to upload the file");
				}
				String endTime = startDateFormat.format(new Date());
				LOGGER.info("file uploading completed for " + endTime + " for IL Mapping Id " + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
		} catch (IOException e) {
			LOGGER.error("",e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
		} catch (Error e) {
			LOGGER.error("",e);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
		}catch (Exception e) {
			LOGGER.error("",e);
			message.setCode("ERROR");
			message.setText(e.getMessage());
		} finally {
			if (!deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
				if (StringUtils.isNotBlank(directoryPath)) {
					try {
						FileUtils.forceDelete(new File(directoryPath));
					} catch (Exception e) {
						System.out.println("Unable to delete created csv for " + e.getMessage());
					}
				}
			}
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getAuthenticationObjectForAllApi", method = RequestMethod.POST)
	public DataResponse getAuthenticationObjectForAllApi(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService,
			HttpServletRequest request) {

		return restUtilities.postRestObject(request, "/getAuthenticationObjectForAllApi", webService, clientId);

	}

	@RequestMapping(value = "/renameUserPackage", method = RequestMethod.POST)
	public DataResponse renameUserPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request,
			@RequestBody Package userPackage) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		boolean validPackageName = true;

		String packageName = userPackage.getPackageName().trim();
		int i = 0;
		switch (i) {
			case 0:
				if (StringUtils.isBlank(packageName)) {
					message.setCode("ERROR");
					message.setText(messageSource.getMessage("anvizent.package.message.packageNameShouldNotBeEmpty", null, locale));
					validPackageName = false;
					break;
				} else {
					i++;
				}

			case 1:
				if (packageName.length() < Constants.Config.PACKAGE_NAME_MIN || packageName.length() > Constants.Config.PACKAGE_NAME_MAX) {
					message.setCode("ERROR");
					message.setText(messageSource.getMessage("anvizent.package.message.packageNameShouldContainAtleast3Characters", null, locale).replace("?",
							String.valueOf(Constants.Config.PACKAGE_NAME_MAX)));
					validPackageName = false;
					break;
				} else {
					i++;
				}
			case 2:
				if (!packageName.matches(Constants.Regex.ALPHA_NUMERICS_WITH_SP_CHAR)) {
					message.setCode("ERROR");
					message.setText(messageSource.getMessage("anvizent.package.message.specialCharactersandOnlyAllowed", null, locale));
					validPackageName = false;
					break;
				} else {
					i++;
				}
			case 3:
				if (!packageName.matches(Constants.Regex.REJECT_ONLY_SP_CHAR)) {
					message.setCode("ERROR");
					message.setText(messageSource.getMessage("anvizent.package.message.addAtLleastAnAlphanumericCharacter", null, locale));
					validPackageName = false;
					break;
				}
		}
		if (validPackageName) {
			return restUtilities.postRestObject(request, "/renameUserPackage", userPackage, clientId);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAllWebServices", method = RequestMethod.GET)
	public DataResponse getAllWebServices(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getAllWebServices", clientId);
	}

	@RequestMapping(value = "/getWebServicesByClientId", method = RequestMethod.GET)
	public DataResponse getWebServicesByClientId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getWebServicesByClientId", clientId);
	}

	@RequestMapping(value = "/getDefaultILWebServiceMapping/{web_service_id}/{il_id}", method = RequestMethod.GET)
	public DataResponse getDefaultILWebServiceMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("web_service_id") String web_service_id, @PathVariable("il_id") String il_id, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getDefaultILWebServiceMapping/{web_service_id}/{il_id}", clientId, web_service_id, il_id);
	}

	@RequestMapping(value = "/getDbSchemaVaribles/{connectorId}", method = RequestMethod.GET)
	public DataResponse getDbSchemaVaribles(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("connectorId") Integer connectorId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getDbSchemaVaribles/{connectorId}", clientId, connectorId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAllSchemasFromDatabase/{connectionId}", method = RequestMethod.GET)
	public DataResponse getAllSchemasFromDatabase(HttpServletRequest request, @PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionId") Integer connectionId, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<String> schemaList = null;
		ILConnection iLConnection = null;
		if (connectionId != null) {
			try {
				User user = CommonUtils.getUserDetails(request, null, null);
				if (user != null) {
					DataResponse iLdataresponse = restUtilities.getRestObject(request, "/getILsConnectionByIdWithSchemas/{" + connectionId + "}",
							user.getUserId(), connectionId);
					LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) iLdataresponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					iLConnection = mapper.convertValue(obj, new TypeReference<ILConnection>() {
					});
					schemaList = CommonUtils.getAllSchemasFromDatabase(iLConnection);
				}

				if (schemaList != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setObject(schemaList);
					dataResponse.setMessages(messages);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.label.schemaNotFound", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}

			} catch (Exception e) {
				LOGGER.error("",e);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.label.errorWhileGettingSchema", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}

		return dataResponse;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getSchemaByDatabse/{connectionId}/{databaseName}", method = RequestMethod.GET)
	public DataResponse getSchemaByDatabse(HttpServletRequest request, @PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionId") Integer connectionId, @PathVariable("databaseName") String databaseName, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<String> schemaList = null;
		ILConnection iLConnection = null;
		if (connectionId != null) {
			try {
				User user = CommonUtils.getUserDetails(request, null, null);
				if (user != null) {
					dataResponse = restUtilities.getRestObject(request, "/getILsConnectionByIdWithSchemas/{" + connectionId + "}", user.getUserId(),
							connectionId);
					if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						LinkedHashMap<String, Object> connectionMap = (LinkedHashMap<String, Object>) dataResponse.getObject();
						ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						iLConnection = mapper.convertValue(connectionMap, new TypeReference<ILConnection>() {
						});
						schemaList = CommonUtils.getSchemaByDatabse(iLConnection, databaseName);
					}
				}

				if (schemaList != null) {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
					messages.add(message);
					dataResponse.setObject(schemaList);
					dataResponse.setMessages(messages);
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.label.schemaNotFound", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
				}

			} catch (Exception e) {
				LOGGER.error("",e);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.label.errorWhileGettingSchema", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}

		return dataResponse;
	}

	@RequestMapping(value = "/saveMappingWithILForWebService", method = RequestMethod.POST)
	public DataResponse saveMappingWithILForWebService(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			@RequestParam("packageId") Integer packageId, @RequestParam("industryId") Integer industryId, @RequestParam("iLId") Integer iLId,
			@RequestParam("dLId") Integer dLId, @RequestParam("fileType") String fileType, @RequestParam("delimeter") String delimeter,
			@RequestParam("isFirstRowHasColoumnNames") Boolean isFirstRowHasColoumnNames, @RequestParam("originalFileName") String originalFileName,
			@RequestParam("iLColumnNames") String iLColumnNames, @RequestParam("selectedFileHeaders") String selectedFileHeaders,
			@RequestParam("dafaultValues") String dafaultValues, Locale locale) {

		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("industryId", industryId);
		map.add("packageId", packageId);
		map.add("iLId", iLId);
		map.add("dLId", dLId);
		map.add("fileType", fileType);
		map.add("delimeter", delimeter);
		map.add("isFirstRowHasColoumnNames", isFirstRowHasColoumnNames);
		map.add("originalFileName", originalFileName);
		map.add("iLColumnNames", iLColumnNames);
		map.add("selectedFileHeaders", selectedFileHeaders);
		map.add("dafaultValues", dafaultValues);

		return restUtilities.postRestObject(request, "/saveMappingWithILForWebService", map, clientId);

	}

	@RequestMapping(value = "/updateMappedHeadersForWebservice", method = RequestMethod.POST)
	public DataResponse updateMappedHeadersForWebservice(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnectionMapping iLConnectionMapping, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/updateMappedHeadersForWebservice", iLConnectionMapping, clientId);
	}

	@RequestMapping(value = "/getDerivedTableQuery ", method = RequestMethod.POST)
	public DataResponse getDerivedTableQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			@RequestParam("packageId") Integer packageId, @RequestParam("derived_table_name") String derived_table_name,
			@RequestParam("targetTableId") Integer targetTableId, @RequestParam("tableId") Integer tableId, Locale locale) {

		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("packageId", packageId);
		map.add("derived_table_name", derived_table_name);
		map.add("targetTableId", targetTableId);
		map.add("tableId", tableId);

		return restUtilities.postRestObject(request, "/getDerivedTableQuery", map, clientId);

	}

	@RequestMapping(value = "/defaultILHistoricalLoadQuery/{il_id}/{connection_id}", method = RequestMethod.GET)
	public DataResponse defaultILHistoricalLoadQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("il_id") Integer il_id,
			@PathVariable("connection_id") Integer connection_id, HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/defaultILHistoricalLoadQuery/{il_id}/{connection_id}", userId, il_id, connection_id);

	}

	@RequestMapping(value = "/viewHistoricalLoadUploadStatus/{historicalLoadId}", method = RequestMethod.GET)
	public DataResponse viewHistoricalLoadUploadStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("historicalLoadId") Integer historicalLoadId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/viewHistoricalLoadUploadStatus/{historicalLoadId}", userId, historicalLoadId);

	}

	@RequestMapping(value = "/validateWebService", method = RequestMethod.POST)
	public DataResponse validateWebService(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebServiceApi webService,
			HttpServletRequest request) {
		return restUtilitiesCommon.postRestObject(request, "/validateWebService", webService, clientId);
	}

	@RequestMapping(value = "/getCustomTempTablesForWebservice/{packageId}/{ilId}", method = RequestMethod.GET)
	public DataResponse getCustomTempTablesForWebservice(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("packageId") Integer ilId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getCustomTempTablesForWebservice/{packageId}/{ilId}", clientId, packageId, ilId);
	}

	@RequestMapping(value = "/getJoinWebServiceHeadersAndIlHeaders", method = RequestMethod.POST)
	public DataResponse getJoinWebServiceHeadersAndIlHeaders(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody Table table,
			Locale locale, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getJoinWebServiceHeadersAndIlHeaders", table, clientId);
	}

	@RequestMapping(value = "/getTempTablesAndWebServiceJoinUrls/{packageId}/{ilId}/{il_connection_mapping_id}", method = RequestMethod.GET)
	public DataResponse getTempTablesAndWebServiceJoinUrls(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("ilId") Integer ilId,
			@PathVariable("il_connection_mapping_id") Integer il_connection_mapping_id, Locale locale, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getTempTablesAndWebServiceJoinUrls/{packageId}/{ilId}/{il_connection_mapping_id}", clientId, packageId,
				ilId, il_connection_mapping_id);
	}

	@RequestMapping(value = "/getWebServiceMasterById/{webServiceId}", method = RequestMethod.GET)
	public DataResponse getWebServiceMasterById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("webServiceId") Integer webServiceId, Locale locale, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getTempTablesAndWebServiceJoinUrls/{webServiceId}", clientId, webServiceId);
	}

	@RequestMapping(value = "/getWebserviceTempleteDetails/{templateId}", method = RequestMethod.GET)
	public DataResponse getWebserviceTempleteDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("templateId") Long templateId, Locale locale, HttpServletRequest request) {
		return restUtilitiesCommon.getRestObject(request, "/getWebserviceTempleteDetails/{templateId}", clientId, templateId);
	}

	@RequestMapping(value = "/saveWebserviceMasterConnectionMapping", method = RequestMethod.POST)
	public DataResponse saveWebserviceMasterConnectionMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody WebServiceConnectionMaster webServiceConnectionMaster, HttpServletRequest request) {
		return restUtilitiesCommon.postRestObject(request, "/saveWebserviceMasterConnectionMapping", webServiceConnectionMaster, clientId);
	}

	@RequestMapping(value = "/getWebServiceConnections", method = RequestMethod.GET)
	public DataResponse getWebServiceConnections(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		return restUtilitiesCommon.getRestObject(request, "/getWebServiceConnections", clientId);
	}

	@RequestMapping(value = "/deleteWebserviceConnection/{connectionId}", method = RequestMethod.GET)
	public DataResponse deleteWebserviceConnection(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionId") String connectionId, HttpServletRequest request) {
		return restUtilitiesCommon.getRestObject(request, "/deleteWebserviceConnection/{connectionId}", clientId, connectionId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/testAuthenticationUrl", method = RequestMethod.POST)
	public DataResponse testAuthenticateWebServiceApi(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody WebServiceConnectionMaster webServiceConnectionMaster, HttpServletRequest request, Locale locale) {

		if (webServiceConnectionMaster.getWebServiceTemplateMaster() != null
				&& (webServiceConnectionMaster.getWebServiceTemplateMaster().getId() == null || webServiceConnectionMaster.getWebServiceTemplateMaster().getId() == 0)) {
			if (webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId() == 5) {

				if (StringUtils.isEmpty(webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().getAuthCodeValue())) {
					OAuth2 oauth = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2();

					String oAuth2LoginUrl = "";
					DataResponse dataResponse = new DataResponse();
					Message message = new Message();
					message.setCode("SUCCESS");

					try {

						String baseUrl = "";
						if(webServiceConnectionMaster.getWebServiceTemplateMaster().isBaseUrlRequired()){
							baseUrl = webServiceConnectionMaster.getWebServiceTemplateMaster().getBaseUrl() ;
						}
						String paramSpearator = webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationUrl().contains("?") ? "&":"?";
						 
						oAuth2LoginUrl = baseUrl + webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationUrl() + paramSpearator
								+ OAuthConstants.CLIENT_ID +"=" + oauth.getClientIdentifier() + "&"+OAuthConstants.REDIRECT_URI+"="
								+ URLEncoder.encode(oauth.getRedirectUrl(), "UTF-8") + "&"+OAuthConstants.RESPONSE_TYPE+"=code";
						if(StringUtils.isNotBlank(oauth.getScope()))
						{
							oAuth2LoginUrl += "&"+OAuthConstants.SCOPE+"="+oauth.getScope();
						}
						if(StringUtils.isNotBlank(oauth.getState()))
						{
							oAuth2LoginUrl += "&"+OAuthConstants.STATE+"="+oauth.getState();
						}
						String authRequestParams = webServiceConnectionMaster.getRequestParams();
						JSONObject authRequestParamJsonObj = null;

						StringBuilder requestParams = new StringBuilder();
						if (authRequestParams != null) {
							authRequestParamJsonObj = new JSONObject(authRequestParams);
							Iterator<String> keys = authRequestParamJsonObj.keys();
							while (keys.hasNext()) {
								String key = keys.next();
								requestParams.append("&").append(key).append("=").append(authRequestParamJsonObj.getString(key));
							}
							oAuth2LoginUrl += requestParams.toString();
						}

					} catch (Exception e) {
						LOGGER.error("",e);
						message.setCode("FAILED");
						message.setText(messageSource.getMessage("anvizent.message.error.text.unableToPrepareLoginUrl",
								null, locale));

					}

					List<Message> messages = new ArrayList<>();
					messages.add(message);
					dataResponse.setMessages(messages);
					dataResponse.setObject(oAuth2LoginUrl);
					return dataResponse;
				}
			}else if (webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId() == 4) { 
				Message message = new Message();
				List<Message> messages = new ArrayList<>();
				DataResponse dataResponse = new DataResponse();
				try
				{
					String requestToken = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth1().getRequestToken();
					String requestTokenSecret = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth1().getRequestTokenSecret();
					if(StringUtils.isEmpty(requestToken) && StringUtils.isEmpty(requestTokenSecret) ){
						
					OAuth1 oAuth1 = WebServiceUtils.getOAuth1RequestToken(webServiceConnectionMaster.getWebServiceTemplateMaster());
					
					if( oAuth1.getAuthURL() != null )
					{
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						messages.add(message);
						message.setText(messageSource.getMessage("anvizent.message.success.text.ok", null, locale));
						dataResponse.setMessages(messages);
						dataResponse.setObject(oAuth1);
						return dataResponse;
					}
					else
					{
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, locale));
						messages.add(message);
						dataResponse.setMessages(messages);
						return dataResponse;
					 }
					}
				}
				catch ( Throwable t )
				{
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, locale));
					messages.add(message);
					dataResponse.setMessages(messages);
					return dataResponse;
				}
			}
		}

		return restUtilitiesCommon.postRestObject(request, "/testAuthenticationUrl", webServiceConnectionMaster, clientId);

	}
	@RequestMapping(value = "/getOAuth1AuthorizationURL", method = RequestMethod.POST)
	public DataResponse getOAuth1AuthorizationURL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody WebServiceConnectionMaster webServiceConnectionMaster, HttpServletRequest request, Locale locale) { 
		
		LOGGER.info("in getOAuth1AuthorizationURL()");

		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		try
		{
			if(webServiceConnectionMaster.getWebServiceTemplateMaster() != null){
			OAuth1 oAuth1 = WebServiceUtils.getOAuth1RequestToken(webServiceConnectionMaster.getWebServiceTemplateMaster());
			
			if( oAuth1.getAuthURL() != null )
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				message.setText(messageSource.getMessage("anvizent.message.success.text.ok", null, locale));
				dataResponse.setMessages(messages);
				dataResponse.setObject(oAuth1);
				return dataResponse;
			}
			else
			{
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				return dataResponse;
			}
			}
		}
		catch ( Throwable t )
		{
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.failedToAuthenticate", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		
		return restUtilitiesCommon.postRestObject(request, "/getOAuth1AuthorizationURL", webServiceConnectionMaster, clientId);

	}
	@RequestMapping(value = "/getWebServiceConnectionDetails/{wsConId}/{ilId}", method = RequestMethod.GET)
	public DataResponse getWebServiceConnectionDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("wsConId") Long wsConId,
			@PathVariable("ilId") Long ilId, HttpServletRequest request, Locale locale) {
		return restUtilitiesCommon.getRestObject(request, "/getWebServiceConnectionDetails/{wsConId}/{ilId}", clientId, wsConId, ilId);
	}

	@RequestMapping(value = "/joinAndSaveWsApi", method = RequestMethod.POST)
	public DataResponse joinAndSaveWsApi(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService, Locale locale,
			HttpServletRequest request) {
		return restUtilitiesCommon.postRestObject(request, "/joinAndSaveWsApi", webService, clientId);
	}
	
	@RequestMapping(value = "/joinAndSaveWsApiForJsonOrXml", method = RequestMethod.POST)
	public DataResponse joinAndSaveWsApiForJsonOrXml(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService, Locale locale,
			HttpServletRequest request) {
		return restUtilitiesCommon.postRestObject(request, "/joinAndSaveWsApiForJsonOrXml", webService, clientId);
	}
	
	@RequestMapping(value = "/saveWsApi", method = RequestMethod.POST)
	public DataResponse saveWsApi(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody WebService webService, Locale locale,
			HttpServletRequest request) {
		return restUtilitiesCommon.postRestObject(request, "/saveWsApi", webService, clientId);
	}

	@RequestMapping(value = "/standardWsPreview/{webserviceConId}/{il_id}/{mappingId}/{packageId}", method = RequestMethod.GET)
	public DataResponse standardWsPreview(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("webserviceConId") Integer webserviceConId, @PathVariable("il_id") Integer il_id, @PathVariable("mappingId") Integer mappingId,
			@PathVariable("packageId") Integer packageId, HttpServletRequest request) {
		return restUtilitiesCommon.getRestObject(request, "/standardWsPreview/{webserviceConId}/{il_id}/{mappingId}/{packageId}", clientId, webserviceConId,
				il_id, mappingId, packageId);
	}

	@RequestMapping(value = "/editMappedHeadersForWsApi", method = RequestMethod.POST)
	public DataResponse editMappedHeadersForWsApi(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnectionMapping iLConnectionMapping, HttpServletRequest request) {
		return restUtilitiesCommon.postRestObject(request, "/editMappedHeadersForWsApi", iLConnectionMapping, clientId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveModifiedSourceDetails/{packageId}/{mappingId}", method = RequestMethod.POST)
	public DataResponse saveILsConnectionMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("packageId") String packageId,
			@PathVariable("mappingId") String mappingId, Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataResponse data = restUtilities.getRestObject(request, "/getILConnectionMappingInfoByMappingId/{Package_id}/{mapping_Id}", userId, packageId,
					mappingId);
			if (data != null && data.getHasMessages() && data.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> connMappingmap = (LinkedHashMap<String, Object>) data.getObject();
				ILConnectionMapping mappingInfo = mapper.convertValue(connMappingmap, new TypeReference<ILConnectionMapping>() {
				});
				ClientData clientData = new ClientData();
				clientData.setIlConnectionMapping(mappingInfo);

				if (!mappingInfo.getIsFlatFile() && !mappingInfo.getIsWebservice()) {
					if (mappingInfo.getiLConnection() != null) {
						String user_id = "";
						try {
							user_id = EncryptionServiceImpl.getInstance().decrypt(userId);
							String[] userIdAndLocale = StringUtils.split(user_id, '#');
							user_id = userIdAndLocale[0];
						} catch (Exception e) {
							LOGGER.error("unable to decrypt userId :: " + e.getLocalizedMessage());
						}
						dataResponse = fileUploadByClientApp(userId, user_id, Integer.parseInt(packageId), clientData, -1, locale, request);
						if (dataResponse != null && dataResponse.getHasMessages()) {
							if (dataResponse.getMessages().get(0).getCode().equals("ERROR"))
								return dataResponse;
						} else {
							message.setCode("SUCCESS");
							messages.add(message);
							dataResponse.setMessages(messages);
						}
					}
				}
			} else {
				message.setCode("ERROR");
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Exception e) {
			LOGGER.error("",e);
			message.setCode("ERROR");
			message.setText(e.getMessage());
			messages.add(message);
			dataResponse.setMessages(messages);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getHistoricalLoadDetailsById/{loadId}", method = RequestMethod.POST)
	public DataResponse getHistoricalLoadDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("loadId") Long loadId,
			Locale locale, HttpServletRequest request) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("loadId", loadId);
		return restUtilities.postRestObject(request, "/getHistoricalLoadDetailsById", map, clientId); 
	}
	
	private String getCsvFolderPath() { 
		String csvFolderPath = null;
		try {
			if ( propertiesPlaceHolder == null ) {
				propertiesPlaceHolder = new PropertiesPlaceHolder(); 
			}
			//csvFolderPath = propertiesPlaceHolder.getCSVSavePath();
		} catch (Exception e) {LOGGER.error("Error while getting csv save path " + e.getMessage());}
		if ( StringUtils.isBlank(csvFolderPath) ) {
			csvFolderPath = Constants.Temp.getTempFileDir();
		}
		csvFolderPath+="/";
		
		return csvFolderPath;
	}
	
	@RequestMapping(value = "/updateDataSourceDetails", method = RequestMethod.POST)
	public DataResponse updateDataSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("mappingId") Integer mappingId, @RequestParam("dataSouceName") String dataSouceName,
			@RequestParam(value="dataSouceNameOther",required=false) String dataSouceNameOther,
			@RequestParam("packageId") Integer packageId,HttpServletRequest request) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("mappingId", mappingId);
		map.add("dataSouceName", dataSouceName);
		map.add("dataSouceNameOther", dataSouceNameOther);
		map.add("packageId", packageId);
		return restUtilities.postRestObject(request, "/updateDataSourceDetails", map, clientId);
	}
	
	@RequestMapping(value = "/updateWsDataSourceDetails", method = RequestMethod.POST)
	public DataResponse updateWsDataSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("mappingId") Integer mappingId, @RequestParam("dataSouceName") String dataSouceName,
			@RequestParam(value="dataSouceNameOther",required=false) String dataSouceNameOther,
			@RequestParam("packageId") Integer packageId,@RequestParam("wsApiUrl") String wsApiUrl,HttpServletRequest request) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("mappingId", mappingId);
		map.add("dataSouceName", dataSouceName);
		map.add("dataSouceNameOther", dataSouceNameOther);
		map.add("packageId", packageId);
		map.add("wsApiUrl", wsApiUrl);
		return restUtilities.postRestObject(request, "/updateWsDataSourceDetails", map, clientId);
	}

	@RequestMapping(value = "/updateWsJoinDataSourceDetails", method = RequestMethod.POST)
	public DataResponse updateWsJoinDataSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("mappingId") Integer mappingId, @RequestParam("dataSouceName") String dataSouceName,
			@RequestParam(value="dataSouceNameOther",required=false) String dataSouceNameOther,
			@RequestParam("packageId") Integer packageId,@RequestParam("wsJoinUrls") String wsJoinUrls,HttpServletRequest request) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("mappingId", mappingId);
		map.add("dataSouceName", dataSouceName);
		map.add("dataSouceNameOther", dataSouceNameOther);
		map.add("packageId", packageId);
		map.add("wsJoinUrls", wsJoinUrls);
		return restUtilities.postRestObject(request, "/updateWsJoinDataSourceDetails", map, clientId);
	}
	
	@RequestMapping(value = "/checkFordownloadIlCurrencyRateTemplate/{templateType}", method = RequestMethod.GET)
	public DataResponse checkFordownloadIlCurrencyRateTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("templateType") String templateType) {

		return restUtilities.getRestObject(request, "/checkFordownloadIlCurrencyRateTemplate/{templateType}", clientId, templateType);

	}

	@RequestMapping(value = "/downloadIlCurrencyRateTemplate/{templateType}", method = RequestMethod.GET)
	public DataResponse downloadIlCurrencyRateTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			HttpServletResponse response, @PathVariable("templateType") String templateType) {

		try {
			ResponseEntity<byte[]> templateFileResponse = restUtilities.getRestEntity(request, "/downloadIlCurrencyRateTemplate/{templateType}", byte[].class,
					clientId,templateType);
			byte[] templateFile = templateFileResponse.getBody();
			if (templateFile != null && templateFile.length > 0) {
				String fileName = Constants.Temp.getTempFileDir() + templateFileResponse.getHeaders().get("filename").get(0);
				File targetFile = new File(fileName);
				OutputStream outStream = new FileOutputStream(targetFile);
				outStream.write(templateFile);
				outStream.close();
				CommonUtils.sendFIleToStream(fileName, request, response);
			}

		} catch (Exception e) {
			LOGGER.error("",e);
		}

		return null;

	}
	
	@RequestMapping(value = "/viewFileErrorLogs/{batchId}", method = RequestMethod.GET)
	public DataResponse viewFileErrorLogs(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("batchId") String batchId,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/viewFileErrorLogs/{batchId}", userId, batchId);
	}
	
	@RequestMapping(value = "/createCustomPackage", method = RequestMethod.POST)
	public DataResponse createCustomPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody CustomPackageForm customPackageForm, Locale locale,
			HttpServletRequest request) throws AnvizentRuntimeException {
		Errors errors = null;
		DataResponse dataresponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		messages.add(message);
		dataresponse.setMessages(messages);
		cpValidator.validate(customPackageForm, errors);
		Package customPackage = new Package();
		if (customPackageForm.getIndustryId() != null) {
			customPackage.setIndustry(new Industry(Integer.parseInt(customPackageForm.getIndustryId())));
		}
		if (customPackageForm.getPackageName() != null) {
			customPackage.setPackageName(customPackageForm.getPackageName().trim());
		}

		if (customPackageForm.getIsFromExistingPackages()) {
			customPackage.setExistingPackageId(customPackageForm.getExistingPackageId());
		}
		customPackage.setIsStandard(customPackageForm.getIsStandard());
		ClientData clientData = new ClientData();
		clientData.setUserPackage(customPackage);
		clientData.getUserPackage().setPackageId(customPackageForm.getExistingPackageId());

		DataResponse dataResponseObj = restUtilities.postRestObject(request, "/createsPackage", clientData, userId);
		if (dataResponseObj != null && dataResponseObj.getHasMessages()) {

			if (com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS.equals(dataResponseObj.getMessages().get(0).getCode())) {

				Integer packageId = (Integer) dataResponseObj.getObject();
				customPackageForm.setPackageId(packageId.toString());
				clientData.getUserPackage().setPackageId(packageId);
				DataResponse dataResponseObj1 = restUtilities.postRestObject(request, "/importSourcesFromPackageToPackage", clientData, userId);
				

				if (dataResponseObj1 != null && dataResponseObj1.getHasMessages()) {
					if (dataResponseObj1.getMessages().get(0).getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS)) {

						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText(messageSource.getMessage("anvizent.message.success.text.successFullyCloned", null, locale));
					} else {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(dataResponseObj1.getMessages().get(0).getText());
					}
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
					
				}
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(dataResponseObj.getMessages().get(0).getText());
			}
		} else {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			
		}
		
		return dataresponse;
	}
	
	
	
	@RequestMapping(value = "/updatePackageName", method = RequestMethod.POST)
	public DataResponse updatePackageName(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestParam("packageName") String packageName,@RequestParam("packageID") Integer packageID,
			HttpServletRequest request,Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		if (StringUtils.isBlank(packageName)) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.package.message.packageNameShouldNotBeEmpty", null, locale));
			return dataResponse;
		}
		if (packageName.length() < com.datamodel.anvizent.helper.minidw.Constants.Config.PACKAGE_NAME_MIN || packageName.length() > com.datamodel.anvizent.helper.minidw.Constants.Config.PACKAGE_NAME_MAX) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.package.message.packageNameShouldContainAtleast3Characters", null, locale)
					.replace("$", String.valueOf(com.datamodel.anvizent.helper.minidw.Constants.Config.PACKAGE_NAME_MIN)).replace("?", String.valueOf(com.datamodel.anvizent.helper.minidw.Constants.Config.PACKAGE_NAME_MAX)));
			return dataResponse;
		}
		if (!packageName.matches(Constants.Regex.ALPHA_NUMERICS_WITH_SP_CHAR)) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.package.message.specialCharactersandOnlyAllowed", null, locale));
			return dataResponse;
		}
		if (!packageName.matches(Constants.Regex.REJECT_ONLY_SP_CHAR)) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.package.message.addAtLleastAnAlphanumericCharacter", null, locale));
			return dataResponse;
		}

		
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("packageName", packageName);
		map.add("packageID", packageID);
		return restUtilities.postRestObject(request, "/updatePackageName",map,userId);
	}
	
	@RequestMapping(value = "/updateTargetTableQuery", method = RequestMethod.POST)
	public DataResponse updateTargetTableQuery(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnectionMapping iLConnectionMapping, Locale locale,
			HttpServletRequest request) {
		
		return restUtilities.postRestObject(request, "/updateTargetTableQuery",iLConnectionMapping,clientId);
	}
	
	@RequestMapping(value = "/getUpdatedTargetMappedQuery", method = RequestMethod.POST)
	public DataResponse getUpdatedTargetMappedQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnectionMapping iLConnectionMapping,
			HttpServletRequest request) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("query", iLConnectionMapping.getiLquery());
		return restUtilities.postRestObject(request, "/getUpdatedTargetMappedQuery",map,clientId);

	}

	@RequestMapping(value = "/editTargetMappedQuery", method = RequestMethod.POST)
	public DataResponse editTargetMappedQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnectionMapping iLConnectionMapping,
			HttpServletRequest request) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("query", iLConnectionMapping.getiLquery());
		return restUtilities.postRestObject(request, "/editTargetMappedQuery",map,clientId);

	}
	
	@RequestMapping(value = "/getDataTypesList", method = RequestMethod.GET)
	public DataResponse getDataTypesList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			 HttpServletRequest request,Locale locale) {
		return restUtilities.getRestObject(request, "/getDataTypesList", clientId);
	}

	}
