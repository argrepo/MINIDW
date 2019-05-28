
package com.datamodel.anvizent.data.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.anvizent.client.data.to.csv.path.converter.exception.QueryParcingException;
import com.anvizent.client.data.to.csv.path.converter.utilities.SQLUtil;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ApisDataInfo;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.DDLayout;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.FileInfo;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author rajesh.anthari
 *
 */

/*
 * @EnableAsync
 * 
 * @EnableScheduling
 */
@RestController
@RequestMapping("" + Constants.AnvizentURL.MINIDW_BASE_URL + "/package")
@CrossOrigin
public class PackageDataController_1 {

	protected static final Log LOGGER = LogFactory.getLog(PackageDataController_1.class);
	@Autowired
	private ServletContext servletContext;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	@Qualifier("apisDataRestTemplateUtilities")
	private RestTemplateUtilities apisRestUtilities;

	//ClientDBProcessor clientDBProcessor = new ClientDBProcessor(null, null, null, null);

	/**
	 * 
	 * @param clientId
	 * @param clientInfo(packageName,isStandard,industry
	 *            of Package properties are required)
	 * @return
	 */
	@RequestMapping(value = "/createsPackage", method = RequestMethod.POST)
	public DataResponse createPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData, Locale locale,
			HttpServletRequest request) {

		return restUtilities.postRestObject(request, "/createsPackage", clientData, clientId);

	}

	/**
	 * 
	 * @param clientId
	 * @param industryId
	 * @return
	 */

	@RequestMapping(value = "/getAllDLS/{industryId}", method = RequestMethod.GET)
	public DataResponse getAllDLs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("industryId") String industryId,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getAllDLS/{industryId}", clientId, industryId);

	}

	/**
	 * 
	 * @param clientId
	 * @param DL_Id
	 * @return
	 */
	@RequestMapping(value = "/getAllILS/{DL_Id}", method = RequestMethod.GET)
	public DataResponse getAllILs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("DL_Id") String DL_Id,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getAllILS/{DL_Id}", clientId, DL_Id);
	}

	@RequestMapping(value = "/getILSWithStatus/{DL_Id}/{packageId}", method = RequestMethod.GET)
	public DataResponse getILsWithStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("DL_Id") String DL_Id,
			@PathVariable("packageId") String packageId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getILSWithStatus/{DL_Id}/{packageId}", userId, DL_Id, packageId);
	}

	/**
	 * 
	 * @param clientId
	 * @return
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value="/getUserPackages", method= RequestMethod.GET)
	 * public ResponseEntity<List<Package>>
	 * getUserPackages(@PathVariable(Constants.PathVariables.CLIENT_ID) String
	 * clientId) { logger.info("in Model App getUserPackages()"); return
	 * restUtilities.getRestEntity(request, "/getUserPackages",
	 * (Class<List<Package>>)(Object)List.class , clientId); }
	 */
	/**
	 * 
	 * @param clientId
	 * @param packageId
	 * @return
	 */
	@RequestMapping(value = "/getPackagesById/{packageId}", method = RequestMethod.GET)
	public DataResponse getPackageById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") String packageId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getPackagesById/{packageId}", clientId, packageId);
	}

	/**
	 * 
	 * @param clientId
	 * @param clientData
	 * @param locale
	 * @return
	 */
	@RequestMapping(value = "/createsILConnection", method = RequestMethod.POST)
	public DataResponse createsILConnection(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnection ilConnection,
			Locale locale, HttpServletRequest request) {
		boolean isCloudAvailable = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
		ilConnection.setAvailableInCloud(isCloudAvailable);
		return restUtilities.postRestObject(request, "/createsILConnection", ilConnection, clientId);
	}

	/**
	 * 
	 * @param clientId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUserILConnections", method = RequestMethod.GET)
	public DataResponse getILConnections(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId
			, HttpServletRequest request,Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();

		boolean isWebApp = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());

		DataResponse userConnectionDataResponse = restUtilities.getRestObject(request, "/getUserILConnections", clientId);

		List<ILConnection> existingConnectionsList = new ArrayList<ILConnection>();
		if (userConnectionDataResponse != null && userConnectionDataResponse.getHasMessages()) {
			if (userConnectionDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<?> list = (List<Map<String, Object>>) userConnectionDataResponse.getObject();
				List<ILConnection> iLConnectionList = mapper.convertValue(list, new TypeReference<List<ILConnection>>() {
				});
				if (iLConnectionList != null && !iLConnectionList.isEmpty()) {
					for (ILConnection iLConnection : iLConnectionList) {
						if (iLConnection != null) {
							iLConnection.setWebApp(isWebApp);
						}
						existingConnectionsList.add(iLConnection);
					}
				}
				message.setCode("SUCCESS");
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(existingConnectionsList);
			} else {
				message.setCode("FAILED");
				message.setText(messageSource.getMessage("anvizent.message.text.unableToRetrieveConnectionList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		}

		return dataResponse;
	}

	/**
	 * 
	 * @param clientId
	 * @param ilConnectionMapping
	 * @param locale
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveILsConnectionMapping", method = RequestMethod.POST)
	public DataResponse saveILsConnectionMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnectionMapping ilConnectionMapping, Locale locale, HttpServletRequest request) {
		
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
					
					FileInfo fileInfo = new FileInfo();
					fileInfo.setPackageId(packageId);

					fileInfo.setHeaders(headers);
					SourceFileInfo sourceFileInfo = new SourceFileInfo();
					sourceFileInfo.setFileInfo(fileInfo);
					sourceFileInfo.setIlConnectionMappingId(ilConnectionMapping.getConnectionMappingId());
					DataResponse uploadDependancyForCustompackageDataResponse = restUtilities.postRestObject(request, "/checkUploadDependancyForCustompackage", sourceFileInfo, clientId);
					if (uploadDependancyForCustompackageDataResponse == null || 
							!uploadDependancyForCustompackageDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						return uploadDependancyForCustompackageDataResponse;
					}

				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
					LOGGER.error("",e);
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(e.getLocalizedMessage());
				}
			}

		}
		

		return restUtilities.postRestObject(request, "/saveILsConnectionMapping", ilConnectionMapping, clientId);
	}

	/**
	 * 
	 * @param clientId
	 * @param connectionId
	 * @return
	 */
	@RequestMapping(value = "/getILsConnectionById/{connectionId}", method = RequestMethod.GET)
	public DataResponse getILsConnectionById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("connectionId") String connectionId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getILsConnectionById/{connectionId}", clientId, connectionId);
	}

	/**
	 * 
	 * @param clientId
	 * @param industryId
	 * @param dLOrILName--is
	 *            same as client DL/IL table name
	 * @return
	 */
	@RequestMapping(value = "/getTablesStructure/{industryId}/{dLOrILName}", method = RequestMethod.GET)
	public DataResponse getTablesStructure(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("industryId") String industryId,
			@PathVariable("dLOrILName") String dLOrILName, HttpServletRequest request, Locale locale) {
		return restUtilities.getRestObject(request, "/getTablesStructure/{industryId}/{dLOrILName}", clientId, industryId, dLOrILName);
	}
	/**
	 * 
	 * @param clientId
	 * @return
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value="/getPendingILS", method= RequestMethod.GET) public
	 * DataResponse
	 * getPendingILS(@PathVariable(Constants.PathVariables.CLIENT_ID) String
	 * clientId) {
	 * 
	 * logger.info("in Model App getPendingILS()");
	 * 
	 * return restUtilities.getRestObject(request, "/getPendingILS",List.class ,
	 * clientId ); }
	 */

	/**
	 * @param Il_Id
	 * @param DL_Id
	 * @param Package_id
	 * @param clientId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getILsConnectionMappingInfo/{Il_Id}/{DL_Id}/{Package_id}", method = RequestMethod.GET)
	public DataResponse getILConnectionMappingInfo(@PathVariable("Il_Id") String Il_Id, @PathVariable("DL_Id") String DL_Id,
			@PathVariable("Package_id") String Package_id, @PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		boolean isWebApp = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
		List<ILConnectionMapping> iLsConnectionMappingInfoList = new ArrayList<>();
		DataResponse iLsConnectionMappingInfoDataResponse = restUtilities.getRestObject(request, "/getILsConnectionMappingInfo/{Il_Id}/{DL_Id}/{Package_id}",
				clientId, Il_Id, DL_Id, Package_id);
		if (iLsConnectionMappingInfoDataResponse != null && iLsConnectionMappingInfoDataResponse.getHasMessages()) {
			if (iLsConnectionMappingInfoDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<?> list = (List<Map<String, Object>>) iLsConnectionMappingInfoDataResponse.getObject();
				List<ILConnectionMapping> iLConnectionMappingList = mapper.convertValue(list, new TypeReference<List<ILConnectionMapping>>() {
				});
				for (ILConnectionMapping iLConnectionMapping : iLConnectionMappingList) {
					if (iLConnectionMapping.getiLConnection() != null) {
						ILConnection iLConnection = iLConnectionMapping.getiLConnection();
						iLConnection.setWebApp(isWebApp);
						iLConnectionMapping.setiLConnection(iLConnection);
					}
					iLsConnectionMappingInfoList.add(iLConnectionMapping);
				}
				message.setCode("SUCCESS");
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(iLsConnectionMappingInfoList);
			} else {
				message.setCode("FAILED");
				message.setText(messageSource.getMessage("anvizent.message.text.unableToRetrieveIlConnectionMappingInfoList", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		}
		return dataResponse;
	}

	/**
	 * @param Package_id
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/getILsConnectionMappingInfoByPackage/{Package_id}", method = RequestMethod.GET)
	public DataResponse getILConnectionMappingInfo(@PathVariable("Package_id") String Package_id, HttpServletRequest request,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId) {
		return restUtilities.getRestObject(request, "/getILsConnectionMappingInfoByPackage/{Package_id}", clientId, Package_id);
	}

	/**
	 * 
	 * @param clientId
	 * @param packageId
	 * @return
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping(value="/getAllTargetTableSOfCustomPackage/{packageId}",
	 * method= RequestMethod.GET) public DataResponse
	 * getAllTargetTablesOfCustomPackage(@PathVariable(Constants.PathVariables.
	 * CLIENT_ID) String clientId,
	 * 
	 * @PathVariable("packageId") String packageId) {
	 * 
	 * logger.info("in Model App getAllTargetTablesOfCustomPackage()"); return
	 * restUtilities.getRestObject(request,
	 * "/getILsConnectionMappingInfo/{Il_Id}/{DL_Id}/{Package_id}", clientId,
	 * packageId); }
	 */
	/**
	 * @param clientId
	 * @param locale
	 * @param request
	 * @param file
	 * @param packageId
	 * @param flatFileType
	 * @param delimeter
	 * @param isFirstRowHasColumnNames
	 * @return
	 */
	@RequestMapping(value = "/uploadsFileIntoS3", method = RequestMethod.POST)
	public DataResponse uploadFileIntoS3(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request,
			@RequestParam("file") MultipartFile file, @RequestParam("packageId") String packageId, @RequestParam("flatFileType") String flatFileType,
			@RequestParam("delimeter") String delimeter, @RequestParam("isFirstRowHasColumnNames") Boolean isFirstRowHasColumnNames) {

		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		File tempFile = null;
		try {
			User user = CommonUtils.getUserDetails(request, null, null);
			String userId = "";
			if (file != null) {
				userId = CommonUtils.decryptedUserId(user.getUserId());
				tempFile = CommonUtils.multipartToFile(file);
				List<String> headers = MinidwServiceUtil.getHeadersFromFile(tempFile.getAbsolutePath(), flatFileType,delimeter, null);
				
				
				if (headers != null && headers.size() > 0) {
					boolean isDataExists = MinidwServiceUtil.hasData(tempFile.getAbsolutePath(), flatFileType, delimeter, null);
					if (headers.size() == 1 && headers.get(0).length() == 0) {
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
						return dataResponse;
					}
					
					if(!isDataExists){
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
						message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
						return dataResponse;
					}
					
				} else {
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(messageSource.getMessage("anvizent.message.validation.text.fileIsEmpty", null, locale));
					return dataResponse;
				}
				List<String> columnDatatypes = MinidwServiceUtil.getColumnsDataTypeFromFile(tempFile.getAbsolutePath(),
						flatFileType, delimeter, null);

				// replacing spaces with '_' as table is not
				// accepting column name with spaces
				StringBuilder cols = new StringBuilder();
				for (String column : headers) {
					cols.append(column.trim().replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
					cols.append(",");
				}
				cols = cols.deleteCharAt(cols.length()-1);
				// save file headers
				String datatypes = String.join(",", columnDatatypes.toArray(new String[] {}));

				FileInfo fileInfo = new FileInfo();
				fileInfo.setClientid(userId);
				fileInfo.setPackageId(Integer.parseInt(packageId));

				fileInfo.setFileType(flatFileType);
				fileInfo.setDelimeter(delimeter);
				fileInfo.setIsFirstRowHasColumnNames(isFirstRowHasColumnNames);
				fileInfo.setFileHeaders(cols.toString());
				fileInfo.setFileColumnDataTypes(datatypes.toString());
				Modification modification = new Modification(new Date());
				modification.setCreatedBy(user.getUserName());
				fileInfo.setModification(modification);
				fileInfo.setHeaders(headers);
				fileInfo.setColumnDatatypes(columnDatatypes);
				SourceFileInfo sourceFileInfo = new SourceFileInfo();
				sourceFileInfo.setFileInfo(fileInfo);
				DataResponse uploadDependancyForCustompackageDataResponse = restUtilities.postRestObject(request, "/checkUploadDependancyForCustompackage", sourceFileInfo, clientId);
				if (uploadDependancyForCustompackageDataResponse != null && uploadDependancyForCustompackageDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					S3BucketInfo s3BucketInfo = getS3BucketInfo(request,clientId);//(S3BucketInfo) SessionHelper.getSesionAttribute(request,Constants.Config.S3_BUCKET_INFO);
					FileSettings fileSettings = (FileSettings) SessionHelper.getSesionAttribute(request,Constants.Config.FILE_SETTINGS_INFO);
					boolean isEncryptionRequired = fileSettings.getFileEncryption();
					if (s3BucketInfo == null) {
						s3BucketInfo = new S3BucketInfo();
					}
					sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, tempFile, userId,
							         Integer.parseInt(packageId), user.getUserName(), null, deploymentType,isEncryptionRequired);
						             fileInfo.setFilePath(sourceFileInfo.getFilePath());
					sourceFileInfo.setFileInfo(fileInfo);
					sourceFileInfo.setS3BucketInfo(s3BucketInfo);
				    dataResponse = restUtilities.postRestObject(request, "/saveSourceFileInfo", sourceFileInfo, clientId);
				} else {
					return uploadDependancyForCustompackageDataResponse ;
				}
			}
			if (dataResponse == null || !dataResponse.getHasMessages()) {
				dataResponse = new DataResponse();
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.label.fileUploadingFailed", null, locale));

			}
		} catch (IOException|OnpremFileCopyException e) {
			e.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(e.getMessage());
			return dataResponse;
		}catch (Exception e) {
			e.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.label.fileUploadingFailed", null, locale));
			return dataResponse;
		} finally {
			if (!deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
				if (tempFile != null) {
					tempFile.delete();
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
	@RequestMapping(value = "/getFilesInfoByPackage/{packageId}", method = RequestMethod.GET)
	public DataResponse getFileInfoByPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request,
			@PathVariable("packageId") String packageId) {

		return restUtilities.getRestObject(request, "/getFilesInfoByPackage/{packageId}", clientId, packageId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveMappingTablesInfo", method = RequestMethod.POST)
	public DataResponse saveMappingTableInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ClientData clientData,
			Locale locale, HttpServletRequest request) {

		List<String> originalColumns = clientData.getUserPackage().getTable().getOriginalColumnNames();
		boolean filesHavingSameColumns = false;
		int packageId = clientData.getUserPackage().getPackageId();
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		Map<String, Object> data = restUtilities.getRestObject(request, "/getILsConnectionMappingInfoByPackage/{" + packageId + "}", Map.class, clientId,
				packageId);

		ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		List<?> l = (List<Map<String, Object>>) data.get("object");

		List<ClientData> mappingInfoList = mapper.convertValue(l, new TypeReference<List<ClientData>>() {
		});
		try {
			if (mappingInfoList.size() > 1) {

				for (ClientData mappingInfo : mappingInfoList) {

					ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
					if (ilConnectionMapping.getIsFlatFile() || ilConnectionMapping.getFilePath() != null) {
						int mappingId = ilConnectionMapping.getConnectionMappingId();

						DataResponse dataResponseHeaders = restUtilities.getRestObject(request, "/getHeadersFromS3File/{packageId}/{ilConnectionMappingId}",
								clientId, packageId, mappingId);

						List<String> headers = (List<String>) dataResponseHeaders.getObject();
						for (String column : originalColumns) {
							if (!headers.contains(column)) {
								filesHavingSameColumns = false;
								break;
							} else {
								filesHavingSameColumns = true;
							}
						}

					} else {
						List<String> colmnList = CommonUtils.getColumnHeadersByQuery(ilConnectionMapping);

						for (String column : originalColumns) {
							if (!colmnList.contains(column)) {
								filesHavingSameColumns = false;
								break;
							} else {
								filesHavingSameColumns = true;
							}
						}
					}

					if (!filesHavingSameColumns)
						break;
				}

			} else {
				filesHavingSameColumns = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.label.errorOccurredWhileCreatingTable", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}
		if (!filesHavingSameColumns) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.label.filesarenothavingsamecolumns", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
		}

		return restUtilities.postRestObject(request, "/saveMappingTablesInfo", clientData, clientId);
	}

	@RequestMapping(value = "/saveMultipleTablesMappingInfo/{packageId}/{industryId}", method = RequestMethod.POST)
	public DataResponse saveMultipleTableMappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId, Locale locale, HttpServletRequest request) {
		
		return restUtilities.postRestObject(request, "/saveMultipleTablesMappingInfo/{packageId}/{industryId}", new LinkedMultiValueMap<>(), clientId,
				packageId, industryId);
	}


	@RequestMapping(value = "/processCustomPackageTableInfo/{industryId}/{packageId}", method = RequestMethod.GET)
	public DataResponse processCustomPackageTableInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request, @PathVariable("packageId") String packageId, @PathVariable("industryId") String industryId) {

		return restUtilities.getRestObject(request, "/processCustomPackageTableInfo/{industryId}/{packageId}", clientId, industryId, packageId);

	}

	@RequestMapping(value = "/updateFileHavingSameColumns/{packageId}/{filesHavingSameColumns}", method = RequestMethod.POST)
	public DataResponse updateFilesHavingSameColumns(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request, @PathVariable("packageId") String packageId, @PathVariable("filesHavingSameColumns") Boolean filesHavingSameColumns) {

		return restUtilities.getRestObject(request, "/updateFileHavingSameColumns/{packageId}/{filesHavingSameColumns}", clientId, packageId,
				filesHavingSameColumns);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checksQuerySyntax", method = RequestMethod.POST)
	public DataResponse checkQuerySyntax(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale, HttpServletRequest request,
			@RequestBody ILConnectionMapping ilConnectionMapping) {

		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		DataResponse dataResponse = new DataResponse();
		String msg = null;
		if (ilConnectionMapping != null) {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			if (StringUtils.isNotBlank(ilConnectionMapping.getiLquery()) || (ilConnectionMapping.getIsIncrementalUpdate() != null
					&& ilConnectionMapping.getIsIncrementalUpdate() && StringUtils.isNotBlank(ilConnectionMapping.getMaxDateQuery()))) {
				Connection con = null;
				try {
					if (ilConnectionMapping.getiLConnection().isDdLayout() || ilConnectionMapping.getiLConnection().isApisData()) {

						DataResponse ddLayoutDataResponse = restUtilities.getRestObject(request, "/getClientSchemaName", clientId);
						if (ddLayoutDataResponse != null && ddLayoutDataResponse.getHasMessages()
								&& ddLayoutDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
							LinkedHashMap<String, Object> clientDbDetails = (LinkedHashMap<String, Object>) ddLayoutDataResponse.getObject();
							if (clientDbDetails != null) {
								ILConnection iLConnection = new ILConnection();
								Database database = new Database();
								database.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
							    database.setId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
							    database.setProtocal(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
							    database.setDriverName(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
						       
								iLConnection.setServer(clientDbDetails.get("region_hostname").toString() + ":" + clientDbDetails.get("region_port").toString()
										+ "/" + clientDbDetails.get("clientdb_schema").toString());
								iLConnection.setUsername(clientDbDetails.get("clientdb_username").toString());
								iLConnection.setPassword(clientDbDetails.get("clientdb_password").toString());
								iLConnection.setDatabase(database);
								con = CommonUtils.connectDatabase(iLConnection);
								ilConnectionMapping.setiLConnection(iLConnection);
								String subString = "select d.* from (" + ilConnectionMapping.getiLquery() + ") d";
								ilConnectionMapping.setiLquery(subString);
								
							}
						}

					} else {
						Integer connectionId = ilConnectionMapping.getiLConnection().getConnectionId();
						if (connectionId != null) {
							DataResponse dataiLConnection = restUtilities.getRestObject(request, "/getILsConnectionById/" + connectionId, clientId,
									connectionId);
							LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataiLConnection.getObject();
							ILConnection iLConnection = mapper.convertValue(obj, new TypeReference<ILConnection>() {
							});
							con = CommonUtils.connectDatabase(iLConnection);
							ilConnectionMapping.setiLConnection(iLConnection);
						}
					}
					if (ilConnectionMapping.getIsIncrementalUpdate() != null && ilConnectionMapping.getIsIncrementalUpdate()) {
						ilConnectionMapping.setiLquery(SQLUtil.replaceDateValue(Constants.Config.DEFAULT_DATE, ilConnectionMapping.getiLquery()));
					}
					
					/*String subString = "select d.* from (" + ilConnectionMapping.getiLquery() + ") d";
					ilConnectionMapping.setiLquery(subString);
					*/
					
					
					msg = CommonUtils.checkQuerySyntax(con, ilConnectionMapping);
					boolean isValidQuery = msg.equals("Query is OK.") || msg.equals("Procedure is OK.");
					String queryType = "normal";
					if (StringUtils.isNotBlank(ilConnectionMapping.getMaxDateQuery()) && isValidQuery) {
						ilConnectionMapping
								.setiLquery(SQLUtil.replaceDateValue(Constants.Config.DEFAULT_DATE, ilConnectionMapping.getMaxDateQuery()));
						msg = CommonUtils.checkQuerySyntax(con, ilConnectionMapping);
						isValidQuery = msg.equals("Query is OK.") || msg.equals("Procedure is OK.");
						queryType = "maxdate";
					}
					message.setCode(isValidQuery ? com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS:com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR );
					//message.setText(isValidQuery ? messageSource.getMessage("anvizent.message.text.queryisOK", null, locale): messageSource.getMessage("anvizent.message.text.procedureIsOK", null, locale));
					if(message.getCode().equals("ERROR")){
						message.setText(msg);	
					}else if(msg.equals("Query is OK.")){
						message.setText(messageSource.getMessage("anvizent.message.text.queryisOK", null, locale));
					}else if(msg.equals("Procedure is OK.")){
						message.setText(messageSource.getMessage("anvizent.message.text.procedureIsOK", null, locale));
					}
					messages.add(message);
					dataResponse.setMessages(messages);
					dataResponse.setObject(queryType);

				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException | QueryParcingException e) {
					e.printStackTrace();
					message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
					message.setText(e.getLocalizedMessage());
					messages.add(message);
					dataResponse.setMessages(messages);
				} finally {
					CommonUtils.closeConnction(con);
				}
			}
		}
		return dataResponse;

	}

	/**
	 * @param clientId
	 * @param packageId
	 * @return
	 */
	@RequestMapping(value = "/activateUserPackage/{packageId}", method = RequestMethod.POST)
	public DataResponse activateUserPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") int packageId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/activateUserPackage/{packageId}", clientId, packageId);
	}
	
	/**
	 * @param clientId
	 * @param packageId
	 * @return
	 */
	@RequestMapping(value = "/deleteUserPackage/{packageId}", method = RequestMethod.POST)
	public DataResponse deleteUserPackage(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("packageId") int packageId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/deleteUserPackage/{packageId}", clientId, packageId);
	}

	/**
	 * @param clientId
	 * @param locale
	 * @param request
	 * @param userPackage
	 * @return
	 */

	@RequestMapping(value = "/getClientILsandDLs", method = RequestMethod.GET)
	public DataResponse getClientILsandDLs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request, Locale locale) {
		return restUtilities.getRestObject(request, "/getClientILsandDLs", clientId);
	}

	@RequestMapping(value = "/updateDatabaseConnection", method = RequestMethod.POST)
	public DataResponse updateDatabaseConnection(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ILConnection ilConnection,
			Locale locale, HttpServletRequest request) {
		boolean isCloudAvailable = Boolean.parseBoolean(servletContext.getAttribute("isWebApp").toString());
		ilConnection.setAvailableInCloud(isCloudAvailable);
		return restUtilities.postRestObject(request, "/updateDatabaseConnection", ilConnection, clientId);
	}

	@RequestMapping(value = "/getDDlTableView", method = RequestMethod.POST)
	public DataResponse getDDlTableView(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DDLayout dDLayout, Locale locale,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getDDlTableView", dDLayout, clientId);
	}

	@RequestMapping(value = "/viewDDlTableSelectQry", method = RequestMethod.POST)
	public DataResponse viewDDlTableSelectQry(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DDLayout dDLayout, Locale locale,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/viewDDlTableSelectQry", dDLayout, clientId);
	}

	@RequestMapping(value = "/updateDDlayoutTable", method = RequestMethod.POST)
	public DataResponse updateDDlayoutTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DDLayout dDLayout, Locale locale,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/updateDDlayoutTable", dDLayout, clientId);
	}

	@RequestMapping(value = "/deleteDDlayoutTable", method = RequestMethod.POST)
	public DataResponse deleteDDlayoutTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DDLayout dDLayout, Locale locale,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/deleteDDlayoutTable", dDLayout, clientId);
	}

	@RequestMapping(value = "/runDDlayoutTable", method = RequestMethod.POST)
	public DataResponse runDDlayoutTable(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DDLayout dDLayout, Locale locale,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/runDDlayoutTable", dDLayout, clientId);
	}

	@RequestMapping(value = "/viewDDlTableResults", method = RequestMethod.POST)
	public DataResponse viewDDlTableResults(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody DDLayout dDLayout, Locale locale,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/viewDDlTableResults", dDLayout, clientId);
	}

	@RequestMapping(value = "/getFlatFilePreview/{packageId}/{mappingId}", method = RequestMethod.GET)
	public DataResponse getFlatFilePreview(@PathVariable("mappingId") String mappingId, @PathVariable("packageId") String packageId,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getFlatFilePreview/{packageId}/{mappingId}", clientId, packageId, mappingId);
	}

	@RequestMapping(value = "/getChangeLogDetails", method = RequestMethod.GET)
	public DataResponse getReleaseNotesDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId
			, HttpServletRequest request,Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("ERROR");
		File file = new File(Constants.Config.MINIDW_CHANGE_LOG);
		if (file.exists()) {
			try {
				String releaseNotesData = FileUtils.readFileToString(file, Constants.Config.ENCODING_TYPE);
				if (StringUtils.isNotBlank(releaseNotesData)) {
					message.setCode("SUCCESS");
					dataResponse.setObject(releaseNotesData);
				} else {
					message.setText(messageSource.getMessage("anvizent.message.text.changeLogNotFound", null, locale));
				}
			} catch (Throwable e) {
				message.setText(MinidwServiceUtil.getErrorMessageString(e));
			}
		} else {
			message.setText(messageSource.getMessage("anvizent.message.text.changeLogNotFound", null, locale));
		}
		return dataResponse;
	}
	@RequestMapping(value = "/getUploadAndExecutionStatusComments", method = RequestMethod.POST)
	public DataResponse getExecutionStatus(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody PackageExecution packageExecution, Locale locale,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getUploadAndExecutionStatusComments", packageExecution, clientId);
	}
	@RequestMapping(value = "/getPackageExecutionTargetTableInfo", method = RequestMethod.POST)
	public DataResponse getPackageExecutionTargetTableInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody PackageExecution packageExecution, Locale locale,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getPackageExecutionTargetTableInfo", packageExecution, clientId);
	}
	@RequestMapping(value = "/getPackageExecutionSourceMappingInfo", method = RequestMethod.POST)
	public DataResponse getPackageExecutionSourceMappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody PackageExecution packageExecution, Locale locale,
			HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getPackageExecutionSourceMappingInfo", packageExecution, clientId);
	}
	
	
	@RequestMapping(value = "/saveApisDataInfo", method = RequestMethod.POST)
	public DataResponse saveApisDataInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody ApisDataInfo apisDataInfo, Locale locale,
			HttpServletRequest request) {
		return apisRestUtilities.postRestObject(request, "/saveApisDataInfo", apisDataInfo, clientId);
	}
	
	@RequestMapping(value = "/getApistDetailsById/{id}", method = RequestMethod.GET)
	public DataResponse getApistDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @PathVariable("id") int id, Locale locale,
			HttpServletRequest request) {
		return apisRestUtilities.getRestObject(request, "/getApistDetailsById/{id}", clientId,id);
	}
	
	@RequestMapping(value = "/getCustomPackageExecutionByPaginationId", method = RequestMethod.GET)
	public DataResponse getPackageExecutionByPaginationId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("packageId") int packageId,
			@RequestParam(value="offset") int offset,
			@RequestParam(value="limit") int limit, HttpServletRequest request,Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("packageId", packageId);
		map.add("offset", offset);
		map.add("limit", limit);
		return restUtilities.postRestObject(request, "/getCustomPackageExecutionByPaginationId",map, clientId);
	}

}
