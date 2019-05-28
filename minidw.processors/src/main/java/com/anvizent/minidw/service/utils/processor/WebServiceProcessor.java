package com.anvizent.minidw.service.utils.processor;

import static minidwclientws.WebServiceUtils.getJSONObjectKey;
import static minidwclientws.WebServiceUtils.validateWebService;
import static minidwclientws.WebServiceUtils.closeAPISession;
import static com.anvizent.minidw.service.utils.helper.WebServiceJSONWriter.getFileFolderSize;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.minidw.client.jdbc.utils.ClientJDBCUtil;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.anvizent.minidw.service.utils.helper.CommonUtils;
import com.datamodel.anvizent.common.exception.ClientWebserviceRequestException;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.common.exception.PackageExecutionException;
import com.datamodel.anvizent.errorhandler.MyResponseErrorHandler;
import com.datamodel.anvizent.helper.WebServiceConstants;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebServiceApi;

import minidwclientws.WebServiceUtils;

/**
 * Modification date:- 26/11/2018
 * Modified By:mahender.alaveni
 * Modification For : Implemented Retry schedule
 */
@Component
public class WebServiceProcessor
{
	protected static final Log logger = LogFactory.getLog(WebServiceProcessor.class);

	@Autowired
	MetaDataFetch metaDataFetch;
	@Autowired
	CommonProcessor commonProcessor;
	@Autowired
	DataBaseProcessor dataBaseProcessor;

	@SuppressWarnings("unchecked")
	public void processWebservice(User user, Package userPackage, String deploymentType, PackageExecution packageExecution, S3BucketInfo s3BucketInfo, FileSettings fileSettings, ILConnectionMapping ilConnectionMapping, CustomRequest customRequest)
	{
		List<Map<String, Object>> incremtalUpdateList = new ArrayList<Map<String, Object>>();
		try
		{
			ClientData mappingInfo = new ClientData();
			mappingInfo.setUserPackage(userPackage);

			if( s3BucketInfo != null )
			{
				ilConnectionMapping.setS3BucketId(s3BucketInfo.getId());
			}
			else
			{
				ilConnectionMapping.setS3BucketId(0);
			}
			ilConnectionMapping.setPackageExecutionId(packageExecution.getExecutionId());
			ilConnectionMapping.setDeploymentType(deploymentType);
			ilConnectionMapping.setTimeZone(packageExecution.getTimeZone());

			mappingInfo.setIlConnectionMapping(ilConnectionMapping);
			String xlsxFilePath = null;
			StringBuilder tempDisplayName = new StringBuilder();
			Map<String, Object> clientDbDetails = metaDataFetch.getClientDbDetails(customRequest);
			String userId = dataBaseProcessor.decryptUserId(user.getUserId());
			String wsFileType = mappingInfo.getIlConnectionMapping().getWebService().getFileType();
			String filePath = getWsFilePath(mappingInfo, user, userId, xlsxFilePath, user.getClientId(), incremtalUpdateList, s3BucketInfo, deploymentType, clientDbDetails, tempDisplayName, customRequest, fileSettings, packageExecution);
			if( filePath != null )
			{
				DataResponse wsFilePathDataResponse = null;

				if( wsFileType.equals(WebServiceConstants.FILETYPE_CSV) )
				{
					wsFilePathDataResponse = wsUploadExecutor(mappingInfo, user, userId, userPackage, filePath, s3BucketInfo, customRequest, fileSettings, incremtalUpdateList);
				}
				else if( wsFileType.equals(WebServiceConstants.FILETYPE_JSON) )
				{
					wsFilePathDataResponse = wsUploadExecutorForJsonOrXml(mappingInfo, user, userId, userPackage, filePath, s3BucketInfo, customRequest, fileSettings, incremtalUpdateList, wsFileType);

				}
				else if( wsFileType.equals(WebServiceConstants.FILETYPE_XML) )
				{

				}
				if( wsFilePathDataResponse != null && wsFilePathDataResponse.getHasMessages() )
				{
					if( wsFilePathDataResponse.getMessages().get(0).getCode().equals("SUCCESS") && wsFilePathDataResponse.getObject() != null )
					{
						Map<String, Object> responseMap = (Map<String, Object>) wsFilePathDataResponse.getObject();
						List<Map<String, Object>> incremtalList = (List<Map<String, Object>>) responseMap.get("incremtalUpdateList");
						for (Map<String, Object> incremtalMap : incremtalList)
						{
							incremtalUpdateList.add(incremtalMap);
						}
					}
					else if( wsFilePathDataResponse.getMessages().get(0).getCode().equals("ERROR") )
					{
						throw new PackageExecutionException(wsFilePathDataResponse.getMessages().get(0).getText());
					}
				}
			}
		}
		catch ( ClientWebserviceRequestException e )
		{
			throw new PackageExecutionException(e.getMessage());
		}
		catch ( AmazonS3Exception e )
		{
			throw new PackageExecutionException(e.getMessage());
		}
		catch ( Exception e )
		{
			throw new PackageExecutionException(e.getMessage());
		}
	}

	/*
	 * Configured ConnectionRequestTimeout ,ConnectTimeout, ReadTimeout to 5
	 * minutes means 300000 milliseconds for RestTemplate Object
	 */
	private static RestTemplate createRestTemplate(int wsConnectionRequestTimeout, int wsReadTimeout, int wsConnectTimeout, boolean isSslDisable) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException
	{
		if( isSslDisable )
		{
			return WebServiceUtils.getRestTemplate();
		}
		else
		{
			CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
			HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
			httpRequestFactory.setHttpClient(httpClient);
			httpRequestFactory.setConnectionRequestTimeout(wsConnectionRequestTimeout);
			httpRequestFactory.setConnectTimeout(wsConnectTimeout);
			httpRequestFactory.setReadTimeout(wsReadTimeout);
			RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
			restTemplate.setErrorHandler(new MyResponseErrorHandler());
			return restTemplate;
		}
	}

	public String getWsFilePath(ClientData mappingInfo, User user, String userId, String csvSavePath, String clientId, List<Map<String, Object>> incremtalUpdateList, S3BucketInfo s3BucketInfo, String deploymentType, Map<String, Object> clientDbDetails, StringBuilder datbaseTypeName,
			CustomRequest customRequest, FileSettings fileSettings, PackageExecution packageExecution) throws Exception
	{
		String decryptedFilepaths = "";
		Integer webServiceConId = mappingInfo.getIlConnectionMapping().getWsConId();
		String mappedHeaders = mappingInfo.getIlConnectionMapping().getWebserviceMappingHeaders();
		Integer connectionMappingId = mappingInfo.getIlConnectionMapping().getConnectionMappingId();
		Integer ilId = mappingInfo.getIlConnectionMapping().getiLId();
		Integer packageId = mappingInfo.getIlConnectionMapping().getPackageId();
		Integer wsConnectionRequestTimeout = mappingInfo.getIlConnectionMapping().getWsConnectionRequestTimeout();
		Integer wsReadTimeout = mappingInfo.getIlConnectionMapping().getWsReadTimeout();
		Integer wsConnectTimeout = mappingInfo.getIlConnectionMapping().getWsConnectTimeout();
		Connection connection = null;
		String filePath = null;
		WebServiceApi webServiceApi = null;
		String wsFileType = mappingInfo.getIlConnectionMapping().getWebService().getFileType();

		try
		{
			if( webServiceConId != null && ilId != null )
			{
				webServiceApi = metaDataFetch.getIlConnectionWebServiceMapping(customRequest, packageId, ilId, connectionMappingId);

				if( webServiceApi != null )
				{
					String outputFileDir = null;

					if( wsFileType.equals(WebServiceConstants.FILETYPE_JSON) )
					{
						outputFileDir = CommonUtils.createDir(Constants.Temp.getTempFileDir() + "fileMappingWithIL/new/" + UUID.randomUUID() + "/");
					}
					webServiceApi.setWebServiceConnectionMaster(metaDataFetch.getWebServiceConnectionDetails(Long.valueOf(webServiceConId), customRequest));
					webServiceApi.setWsFileType(wsFileType);

					boolean isSSLDisable = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().isSslDisable();

					//int retryLimit = webServiceApi.getWebServiceConnectionMaster().getWebServiceTemplateMaster().getRetryLimit() + 1;

					RestTemplate restTemplate = createRestTemplate(wsConnectionRequestTimeout, wsReadTimeout, wsConnectTimeout, isSSLDisable);

					datbaseTypeName.append("W" + webServiceApi.getWebServiceConnectionMaster().getId());
					String incrementalDate = metaDataFetch.dateForIncrementalUpdateQuery(webServiceApi.getIlId().intValue(), webServiceConId, Constants.SourceType.WEBSERVICE, connectionMappingId, customRequest);
					boolean isInclupdate = false;

					Map<String, Object> incrementalMap = new HashMap<String, Object>();
					incrementalMap.put("currentTime", TimeZoneDateHelper.getFormattedDateString());

					if( mappingInfo.getIlConnectionMapping().isJoinWebService() )
					{
						List<Table> tableList = metaDataFetch.getTempTablesAndWebServiceJoinUrls(packageId, ilId, connectionMappingId, customRequest);

						boolean isTableExist = false;
						if( tableList.size() > 0 )
						{
							for (Table table : tableList)
							{
								webServiceApi.setApiUrl(table.getWebServiceJoin().getWebServiceUrl());
								webServiceApi.setResponseObjectName(table.getWebServiceJoin().getResponseObjectName());
								webServiceApi.setResponseColumnObjectName(table.getWebServiceJoin().getResponseColumnObjectName());
								webServiceApi.setApiMethodType(table.getWebServiceJoin().getWebServiceMethodType());
								webServiceApi.setSoapBodyElement(table.getWebServiceJoin().getSoapBodyElement());
								webServiceApi.setIncrementalUpdate(table.getWebServiceJoin().getIncrementalUpdate());
								webServiceApi.setIncrementalUpdateparamdata(table.getWebServiceJoin().getIncrementalUpdateparamdata());
								try
								{
									if( wsFileType.equals(WebServiceConstants.FILETYPE_CSV) )
									{
										isTableExist = metaDataFetch.isTableExists(table.getTableName(), customRequest);
										if( isTableExist )
										{
											List<Column> columns = metaDataFetch.getTableStructure(table.getTableName(), 0, customRequest);
											Table tempTable = getTempTableAndColumns(table, null, null, clientId, columns);

											webServiceApi.setTable(tempTable);
											webServiceApi.setInclUpdateDate(incrementalDate);
											 
											boolean checkSubUrlIncrementalUpdate = checkSubUrlIncrementalUpdate(webServiceApi);
											if( webServiceApi.getIncrementalUpdate() || checkSubUrlIncrementalUpdate)
											{
												isInclupdate = true;
											}

											if( webServiceApi.getRetryPaginationData() == null )
											{
												metaDataFetch.truncateTable(table.getTableName(), "staging", customRequest);
											}
											validateWebService(webServiceApi, restTemplate, clientDbDetails);

										}
										ILConnectionMapping ilConnectionMappingInfo = mappingInfo.getIlConnectionMapping();
										ILConnectionMapping ilConnectionMappings = getIlConnection(clientDbDetails, ilConnectionMappingInfo, true);

										filePath = metaDataFetch.createFileByConnection(ilConnectionMappings, s3BucketInfo, fileSettings, deploymentType, customRequest);
									}
									else if( wsFileType.equals(WebServiceConstants.FILETYPE_JSON) )
									{
										webServiceApi.setInclUpdateDate(incrementalDate);

										validateWebService(webServiceApi, restTemplate, filePath, clientDbDetails);

										File originalFile = new File(outputFileDir);
										
										long folderSize = getFileFolderSize(originalFile);

										if( folderSize == 0 )
										{
											updateErrorMessage(filePath, ilId, webServiceApi.getApiUrl(), packageExecution, customRequest);
											return null;
										}
									}
									else if( wsFileType.equals(WebServiceConstants.FILETYPE_XML) )
									{

									}
								}
								catch ( ClientWebserviceRequestException e )
								{
									/*
									 * update the retry_pagination info at
									 * minidwcs_package_source_mapping
									 */
									/*if( wsFileType.equals(WebServiceConstants.FILETYPE_CSV) )
									{
										if( webServiceApi.getRetryCount() == retryLimit )
										{
											if( !webServiceApi.getValidateOrPreview() )
											{
												boolean checkPagination = checkPagination(webServiceApi);
												if( checkPagination )
												{
													metaDataFetch.updateRetryPaginationAtWsMapping(webServiceApi.getId().intValue(), getRetryPaginationData(webServiceApi).toString(), customRequest);
												}
												else
												{
													metaDataFetch.truncateTable(table.getTableName(), "staging", customRequest);
													metaDataFetch.updateRetryPaginationAtWsMapping(webServiceApi.getId().intValue(), "", customRequest);
												}
											}
										}
									}*/
									throw new ClientWebserviceRequestException("Error occured while fetching data for IL id: " + ilId + " " + e.getMessage() + ".", e);
								}
								finally
								{
									/*if( webServiceApi.getRetryCount() != retryLimit )
									{
										if( wsFileType.equals(WebServiceConstants.FILETYPE_CSV) )
										{
											metaDataFetch.updateRetryPaginationAtWsMapping(webServiceApi.getId().intValue(), "", customRequest);
										}*/
										updateIncrementalDatePagination(webServiceApi, customRequest);
									//}
									closeAPISession(webServiceApi.getWebServiceConnectionMaster(), restTemplate);
								}
							}
						}
						else
						{
							throw new IOException("temp tables not found and size is : " + tableList.size());
						}
					}
					else
					{
						try
						{
							if( wsFileType.equals(WebServiceConstants.FILETYPE_CSV) )
							{
								webServiceApi.setInclUpdateDate(incrementalDate);
								boolean checkSubUrlIncrementalUpdate = checkSubUrlIncrementalUpdate(webServiceApi);
								if( webServiceApi.getIncrementalUpdate() || checkSubUrlIncrementalUpdate )
								{
									isInclupdate = true;
								}

								Table table = new Table();
								webServiceApi.setTable(table);

								webServiceApi.setMappedHeaders(mappedHeaders);
								validateWebService(webServiceApi, restTemplate, clientDbDetails);

								ILConnectionMapping ilConnectionMappingInfo = mappingInfo.getIlConnectionMapping();

								ilConnectionMappingInfo.setiLquery(getTempSelectQuery(webServiceApi));

								ILConnectionMapping ilConnectionMappings = getIlConnection(clientDbDetails, ilConnectionMappingInfo, true);

								filePath = metaDataFetch.createFileByConnection(ilConnectionMappings, s3BucketInfo, fileSettings, deploymentType, customRequest);

							}
							else if( wsFileType.equals(WebServiceConstants.FILETYPE_JSON) )
							{
								webServiceApi.setInclUpdateDate(incrementalDate);

								validateWebService(webServiceApi, restTemplate, outputFileDir, clientDbDetails);

								File originalFile = new File(outputFileDir);

								long folderSize = getFileFolderSize(originalFile);

								if( folderSize == 0 )
								{
									updateErrorMessage(filePath, ilId, webServiceApi.getApiUrl(), packageExecution, customRequest);
									return null;
								}
							}
							else if( wsFileType.equals(WebServiceConstants.FILETYPE_XML) )
							{

							}
						}
						catch ( ClientWebserviceRequestException cwre )
						{
							/*
							 * update the retry_pagination info at
							 * minidwcs_package_source_mapping
							 */
							if( wsFileType.equals(WebServiceConstants.FILETYPE_CSV) )
							{
								/*if( webServiceApi.getRetryCount() == retryLimit )
								{
									if( !webServiceApi.getValidateOrPreview() )
									{
										boolean checkPagination = checkPagination(webServiceApi);
										if( checkPagination )
										{
											metaDataFetch.updateRetryPaginationAtWsMapping(webServiceApi.getId().intValue(), getRetryPaginationData(webServiceApi).toString(), customRequest);
										}
										else
										{*/
											connection = getStagingConnection(clientDbDetails);
											dropTempTable(webServiceApi.getTable().getTableName(), connection);
											//metaDataFetch.updateRetryPaginationAtWsMapping(webServiceApi.getId().intValue(), "", customRequest);
										//}
									//}
								//}
							}
							throw new ClientWebserviceRequestException("Error occured while fetching data for IL id : " + ilId + " " + cwre.getMessage(), cwre);
						}
						finally
						{
							/*if( webServiceApi.getRetryCount() != retryLimit )
							{*/
								if( wsFileType.equals(WebServiceConstants.FILETYPE_CSV) )
								{
									connection = getStagingConnection(clientDbDetails);
									dropTempTable(webServiceApi.getTable().getTableName(), connection);
									//metaDataFetch.updateRetryPaginationAtWsMapping(webServiceApi.getId().intValue(), "", customRequest);
								}
								updateIncrementalDatePagination(webServiceApi, customRequest);
							//}
							closeAPISession(webServiceApi.getWebServiceConnectionMaster(), restTemplate);
						}
					}
					if( isInclupdate )
					{
						incremtalUpdateList.add(incrementalMap);
					}
					if( wsFileType.equals(WebServiceConstants.FILETYPE_CSV) )
					{
						List<String> iLColumnNamesList = new ArrayList<>();
						List<String> selectedFileHeadersList = new ArrayList<>();
						List<String> dafaultValuesList = new ArrayList<>();

						String[] map = StringUtils.split(mappedHeaders, "||");
						for (int i = 0; i < map.length; i++)
						{
							String s = map[i];
							String[] iLApiHeaders = s.split("=");
							String iLHeader = iLApiHeaders[0];
							iLColumnNamesList.add(iLHeader);
							String apiHeader = iLApiHeaders[1];
							if( !apiHeader.contains("{") )
							{
								selectedFileHeadersList.add(apiHeader);
							}
							else
							{
								selectedFileHeadersList.add("");
							}

							if( apiHeader.contains("{") )
							{
								String defaultValue = apiHeader.replace("{", "").replace("}", "");
								if( StringUtils.isNotBlank(defaultValue) )
								{
									dafaultValuesList.add(defaultValue);
								}
								else
								{
									dafaultValuesList.add(null);
								}
							}
							else
							{
								dafaultValuesList.add(null);
							}
						}
						decryptedFilepaths = MinidwServiceUtil.processFileMappingWithILWs(filePath, Constants.FileType.CSV, ",", null, iLColumnNamesList, selectedFileHeadersList, dafaultValuesList);
					}
					else if( wsFileType.equals(WebServiceConstants.FILETYPE_JSON) )
					{
						decryptedFilepaths = outputFileDir;
					}
					else if( wsFileType.equals(WebServiceConstants.FILETYPE_XML) )
					{

					}
				}
				else
				{
					throw new Exception("web service api not found for mapping id : " + connectionMappingId);
				}
			}
		}
		catch ( AmazonS3Exception | OnpremFileCopyException e )
		{
			throw new AmazonS3Exception("File Uploading failed for IL " + ilId + "<br /><b>Error Details:</b>" + e.getMessage());
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			throw new IOException("file uploading failed : " + e.getMessage());

		}
		finally
		{
			if( filePath != null )
			{
				new File(filePath).delete();
			}
			if( connection != null )
			{
				connection.close();
			}
		}
		return decryptedFilepaths;
	}

	boolean checkPagination(WebServiceApi webServiceApi)
	{
		boolean checkPagination = false;
		if( webServiceApi.getPaginationRequired() && StringUtils.isNotBlank(webServiceApi.getPaginationRequestParamsData()) && StringUtils.startsWith(webServiceApi.getPaginationRequestParamsData(), "[") )
		{
			checkPagination = true;
		}
		return checkPagination;
	}

	boolean checkSubUrlIncrementalUpdate(WebServiceApi webServiceApi){
	 String	pathParams = webServiceApi.getApiPathParams();
	 String apiUrl = webServiceApi.getApiUrl();
	 boolean checkSubUrlIncrementalUpdate = false;
	if( StringUtils.isNotBlank(pathParams) )
	{
		org.json.JSONObject pathParamsJObj = new  org.json.JSONObject(pathParams);
		ArrayList<String> pathParamVariables = WebServiceUtils.getParameterVariables(apiUrl, "{#");
		for (String paramKey : pathParamVariables)
		{
			org.json.JSONObject paramValueObj = pathParamsJObj.getJSONObject(paramKey);

			if( paramValueObj != null )
			{
				  if( paramValueObj.get("valueType").equals("S") )
				  {
					  checkSubUrlIncrementalUpdate = WebServiceUtils.getJSONObjectKeyAsBoolean(paramValueObj, "subUrlIncrementalUpdate");
				  }
			}
		}
	}
	return checkSubUrlIncrementalUpdate;
	}
	
	void updateIncrementalDatePagination(WebServiceApi webServiceApi, CustomRequest customRequest)
	{
		if( webServiceApi.getPaginationRequired() && StringUtils.isNotBlank(webServiceApi.getPaginationRequestParamsData()) && StringUtils.startsWith(webServiceApi.getPaginationRequestParamsData(), "[") )
		{
			/* update incremental date pagination at ws mapping */
			if( webServiceApi.getPaginationType().equals("incrementaldate") )
			{
				JSONArray paramters = new JSONArray(webServiceApi.getPaginationRequestParamsData());
				for (int i = 0; i < paramters.length(); i++)
				{
					org.json.JSONObject paramObject = paramters.getJSONObject(i);
					String paginationIncrementalStartDateParam = paramObject.getString("paginationIncrementalStartDateParam");
					String paginationIncrementalEndDateParam = paramObject.getString("paginationIncrementalEndDateParam");
					String paginationParamType = paramObject.getString("paginationParamType");
					String paginationIncrementalEndDate = paramObject.getString("paginationIncrementalEndDate");
					String paginationIncrementalDateRange = paramObject.getString("paginationIncrementalDateRange");

					String paginationIncrementalDatePageNumberRequestParamName = getJSONObjectKey(paramObject, "paginationIncrementalDatePageNumberRequestParamName");
					String paginationIncrementalDatePageNumberRequestParamValue = getJSONObjectKey(paramObject, "paginationIncrementalDatePageNumberRequestParamValue");
					String paginationIncrementalDatePageSizeRequestParamName = getJSONObjectKey(paramObject, "paginationIncrementalDatePageSizeRequestParamName");
					String paginationIncrementalDatePageSizeRequestParamValue = getJSONObjectKey(paramObject, "paginationIncrementalDatePageSizeRequestParamValue");

					String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					JSONArray modifiedParamters = new JSONArray();
					org.json.JSONObject modifiedParamObject = new org.json.JSONObject();
					modifiedParamObject.put("paginationParamType", paginationParamType);
					modifiedParamObject.put("paginationIncrementalStartDateParam", paginationIncrementalStartDateParam);
					modifiedParamObject.put("paginationIncrementalEndDateParam", paginationIncrementalEndDateParam);
					if( StringUtils.isBlank(paginationIncrementalEndDate) )
					{
						modifiedParamObject.put("paginationIncrementalStartDate", currentDate);
						modifiedParamObject.put("paginationIncrementalEndDate", paginationIncrementalEndDate);
					}
					else
					{
						modifiedParamObject.put("paginationIncrementalStartDate", paginationIncrementalEndDate);
						modifiedParamObject.put("paginationIncrementalEndDate", currentDate);
					}
					modifiedParamObject.put("paginationIncrementalDateRange", paginationIncrementalDateRange);
					modifiedParamObject.put("paginationIncrementalDatePageNumberRequestParamName", paginationIncrementalDatePageNumberRequestParamName == null ? "" : paginationIncrementalDatePageNumberRequestParamName);
					modifiedParamObject.put("paginationIncrementalDatePageNumberRequestParamValue", paginationIncrementalDatePageNumberRequestParamValue == null ? "" : paginationIncrementalDatePageNumberRequestParamValue);
					modifiedParamObject.put("paginationIncrementalDatePageSizeRequestParamName", paginationIncrementalDatePageSizeRequestParamName == null ? "" : paginationIncrementalDatePageSizeRequestParamName);
					modifiedParamObject.put("paginationIncrementalDatePageSizeRequestParamValue", paginationIncrementalDatePageSizeRequestParamValue == null ? "" : paginationIncrementalDatePageSizeRequestParamValue);

					modifiedParamters.put(modifiedParamObject);

					metaDataFetch.updateIncrementalDatePaginationAtWsMapping(webServiceApi.getId().intValue(), modifiedParamters.toString(), customRequest);

				}
			}
		}
	}

	String getTempSelectQuery(WebServiceApi webServiceApi) throws Exception
	{
		StringBuilder query = new StringBuilder();
		StringJoiner stringJoiner = new StringJoiner(",");

		List<String> columns = webServiceApi.getTable().getOriginalColumnNames();

		query.append(" SELECT ");

		if( columns != null && webServiceApi.getTable().getTableName() != null )
		{
			for (String column : columns)
			{

				if( column.length() >= 65 )
				{
					column = column.substring(0, 64);
				}

				stringJoiner.add("`" + column + "`");
			}
		}
		else
		{
			throw new Exception("No data found in results.");
		}

		query.append(stringJoiner.toString()).append(" FROM ").append(webServiceApi.getTable().getTableName()).append(" ; ");

		return query.toString();
	}

	void dropTempTable(String tempTableName, Connection connection) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		try
		{
			if( tempTableName != null )
			{
				String sql = "DROP TABLE " + tempTableName + ";";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.executeUpdate(sql);
				System.out.println("droped temp table name --> " + tempTableName);
			}
		}
		finally
		{
			if( preparedStatement != null )
			{
				preparedStatement.close();
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	JSONObject getRetryPaginationData(WebServiceApi webServiceApi)
	{
		JSONObject retryPaginationData = new JSONObject();
		retryPaginationData.put("retryCount", webServiceApi.getRetryCount());
		retryPaginationData.put("retryOffset", webServiceApi.getRetryOffset());
		retryPaginationData.put("retryPage", webServiceApi.getRetryPage());
		retryPaginationData.put("retryDatePage", webServiceApi.getRetryDatePage());
		retryPaginationData.put("retryIncrementalDatePage", webServiceApi.getRetryIncrementalDatePage());
		retryPaginationData.put("retryStartDate", webServiceApi.getRetryDate() == null ? "" : webServiceApi.getRetryDate());
		retryPaginationData.put("retryHyperMediaNextUrl", webServiceApi.getRetryHyperMediaNextUrl() == null ? "" : webServiceApi.getRetryHyperMediaNextUrl());
		retryPaginationData.put("retryTempTableName", webServiceApi.getTable().getTableName() == null ? "" : webServiceApi.getTable().getTableName());
		retryPaginationData.put("retryIncrementalStartDate", webServiceApi.getRetryIncrementalStartDate() == null ? "" : webServiceApi.getRetryIncrementalStartDate());
		retryPaginationData.put("retryIncrementalEndDate", webServiceApi.getRetryIncrementalEndDate() == null ? "" : webServiceApi.getRetryIncrementalEndDate());
		return retryPaginationData;
	}

	Connection getStagingConnection(Map<String, Object> clientDbDetails) throws SQLException, ClassNotFoundException
	{
		String host = (String) clientDbDetails.get("region_hostname");
		String port = (String) clientDbDetails.get("region_port");
		String schemaName = (String) clientDbDetails.get("clientdb_staging_schema");
		String user = (String) clientDbDetails.get("clientdb_username");
		String password = (String) clientDbDetails.get("clientdb_password");
		Class.forName(Constants.MYSQL_DRIVER_CLASS);
		return DriverManager.getConnection(Constants.MYSQL_DB_URL + host + ":" + port + "/" + schemaName + com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL_PARAMS, user, password);

	}

	void updateErrorMessage(String filePath, int ilId, String apiUrl, PackageExecution packageExecution, CustomRequest customRequest)
	{

		if( packageExecution != null )
		{
			String noOfSourcesString = TimeZoneDateHelper.getFormattedDateString() + "\tNo data found for Il " + ilId + " with api URL " + apiUrl;
			metaDataFetch.updateUploadInfo(Constants.ExecutionStatus.INPROGRESS, noOfSourcesString, packageExecution, customRequest);
		}

	}

	public DataResponse wsUploadExecutor(ClientData mappingInfo, User user, String userId, Package userPackage, String originalFile, S3BucketInfo s3BucketInfo, CustomRequest customRequest, FileSettings fileSettings, List<Map<String, Object>> incremtalUpdateList) throws IOException
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		StringBuilder uploadStatusInfo = new StringBuilder();
		SourceFileInfo sourceFileInfo = null;
		try
		{
			ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
			String s3LogicalDirPath = "datafiles_U" + userId + "_P" + userPackage.getPackageId() + "_M" + mappingInfo.getIlConnectionMapping().getConnectionMappingId();
			Integer ilId = ilConnectionMapping.getiLId();
			if( ilId != null && ilId != 0 )
			{
				s3LogicalDirPath += "_IL" + ilId + "_" + "DL" + ilConnectionMapping.getdLId();
			}

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());

			String startTime = commonProcessor.getFormattedDateString();

			uploadStatusInfo.append("\n Multipart enabled : false");

			uploadStatusInfo.append("\n\n Web service  file uploading started " + startTime + " for IL Mapping Id:" + mappingInfo.getIlConnectionMapping().getConnectionMappingId() + " to ILId: " + ilId);

			boolean isEncryptionRequired = fileSettings.getFileEncryption();

			if( originalFile != null )
			{
				sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, new File(originalFile), userId, ilConnectionMapping.getPackageId(), user.getUserName(), ilConnectionMapping.getConnectionMappingId(), ilConnectionMapping.getDeploymentType(), s3LogicalDirPath, false,
						isEncryptionRequired);
				uploadStatusInfo.append("\n Web service source  file type:" + sourceFileInfo.getFileType());
				uploadStatusInfo.append("\n Web service source  ilId:" + ilId + " S3/Local file path is:" + sourceFileInfo.getFilePath());
			}
			else
			{
				uploadStatusInfo.append("\n Web service source file is empty or null for IL mapping Id:" + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
				message.setCode("ERROR");
				message.setText("\n Web service source file is empty or null for IL mapping Id:" + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
			}

			String endTime = commonProcessor.getFormattedDateString();
			uploadStatusInfo.append("\n Web service  file uploading completed " + endTime + " for IL mapping Id:" + mappingInfo.getIlConnectionMapping().getConnectionMappingId() + " to ILId: " + ilId + "\n");

			if( incremtalUpdateList != null && incremtalUpdateList.size() > 0 )
			{
				sourceFileInfo.setIncrementalUpdate(true);
				sourceFileInfo.setIncrementalDateValue(incremtalUpdateList.get(0).get("currentTime").toString());
			}

			sourceFileInfo.setMultiPartFile(false);
			sourceFileInfo.setS3BucketInfo(s3BucketInfo);
			sourceFileInfo.setStorageType(mappingInfo.getIlConnectionMapping().getStorageType());
			sourceFileInfo.setDelimeter(Constants.FileTypeDelimiter.CSV_DELIMITER);
			int sourceFileInfoId = metaDataFetch.saveSourceFileInfo(sourceFileInfo, customRequest);
			if( sourceFileInfoId != -1 )
			{
				sourceFileInfo.setModification(modification);
				sourceFileInfo.setSourceFileId(sourceFileInfoId);
				PackageExecution packExecution = MinidwServiceUtil.getUploadStatus(ilConnectionMapping.getPackageExecutionId(), Constants.ExecutionStatus.INPROGRESS, uploadStatusInfo.toString(), ilConnectionMapping.getTimeZone());
				packExecution.setModification(modification);
				metaDataFetch.updateUploadInfo(packExecution, customRequest);
				sourceFileInfo.setPackageExecution(MinidwServiceUtil.getUploadStatus(ilConnectionMapping.getPackageExecutionId(), Constants.ExecutionStatus.COMPLETED, "Uploaded successfully.", ilConnectionMapping.getTimeZone()));
				metaDataFetch.saveExecutionSourceMappingInfo(null, sourceFileInfo, customRequest);
				message.setCode("SUCCESS");
				message.setText("Web service file upload completed.");
			}

		}
		catch ( AmazonS3Exception e )
		{
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		catch ( OnpremFileCopyException e )
		{
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		finally
		{
			if( originalFile != null )
			{
				String basePath = FilenameUtils.getFullPathNoEndSeparator(originalFile);
				FileUtils.forceDelete(new File(basePath));
			}
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	public DataResponse wsUploadExecutorForJsonOrXml(ClientData mappingInfo, User user, String userId, Package userPackage, String originalFile, S3BucketInfo s3BucketInfo, CustomRequest customRequest, FileSettings fileSettings, List<Map<String, Object>> incremtalUpdateList, String wsFileType)
			throws IOException
	{
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		StringBuilder uploadStatusInfo = new StringBuilder();
		SourceFileInfo sourceFileInfo = null;
		try
		{
			ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
			String s3LogicalDirPath = "datafiles_U" + userId + "_P" + userPackage.getPackageId() + "_M" + mappingInfo.getIlConnectionMapping().getConnectionMappingId();
			Integer ilId = ilConnectionMapping.getiLId();
			if( ilId != null && ilId != 0 )
			{
				s3LogicalDirPath += "_IL" + ilId + "_" + "DL" + ilConnectionMapping.getdLId() + "_" + CommonUtils.generateUniqueIdWithTimestamp();
			}

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());

			String startTime = commonProcessor.getFormattedDateString();

			uploadStatusInfo.append("\n Multipart enabled : false");

			uploadStatusInfo.append("\n\n Web service  file uploading started " + startTime + " for IL mapping Id:" + mappingInfo.getIlConnectionMapping().getConnectionMappingId() + " to ILId: " + ilId);

			boolean isEncryptionRequired = fileSettings.getFileEncryption();

			if( originalFile != null )
			{
				sourceFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, new File(originalFile), userId, ilConnectionMapping.getPackageId(), user.getUserName(), ilConnectionMapping.getConnectionMappingId(), ilConnectionMapping.getDeploymentType(), s3LogicalDirPath, true,
						isEncryptionRequired);
				uploadStatusInfo.append("\n Web service source  file type:" + wsFileType + ".");
				uploadStatusInfo.append("\n Web service source  ilId:" + ilId + " S3/Local file path is:" + sourceFileInfo.getFilePath());
			}
			else
			{
				uploadStatusInfo.append("\n Web service source file is empty or null for IL mapping Id:" + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
				message.setCode("ERROR");
				message.setText("\n Web service source file is empty or null for IL mapping Id:" + mappingInfo.getIlConnectionMapping().getConnectionMappingId());
			}

			String endTime = commonProcessor.getFormattedDateString();
			uploadStatusInfo.append("\n Web service  file uploading completed " + endTime + " for IL Mapping Id:" + mappingInfo.getIlConnectionMapping().getConnectionMappingId() + " to ILId: " + ilId + "\n");

			if( incremtalUpdateList != null && incremtalUpdateList.size() > 0 )
			{
				sourceFileInfo.setIncrementalUpdate(true);
				sourceFileInfo.setIncrementalDateValue(incremtalUpdateList.get(0).get("currentTime").toString());
			}

			sourceFileInfo.setMultiPartFile(false);
			sourceFileInfo.setS3BucketInfo(s3BucketInfo);
			sourceFileInfo.setStorageType(mappingInfo.getIlConnectionMapping().getStorageType());
			sourceFileInfo.setDelimeter(Constants.FileTypeDelimiter.CSV_DELIMITER);
			sourceFileInfo.setFileType(WebServiceConstants.FILETYPE_JSON);
			int sourceFileInfoId = metaDataFetch.saveSourceFileInfo(sourceFileInfo, customRequest);
			if( sourceFileInfoId != -1 )
			{
				sourceFileInfo.setModification(modification);
				sourceFileInfo.setSourceFileId(sourceFileInfoId);
				PackageExecution packExecution = MinidwServiceUtil.getUploadStatus(ilConnectionMapping.getPackageExecutionId(), Constants.ExecutionStatus.INPROGRESS, uploadStatusInfo.toString(), ilConnectionMapping.getTimeZone());
				packExecution.setModification(modification);
				metaDataFetch.updateUploadInfo(packExecution, customRequest);
				sourceFileInfo.setPackageExecution(MinidwServiceUtil.getUploadStatus(ilConnectionMapping.getPackageExecutionId(), Constants.ExecutionStatus.COMPLETED, "Uploaded successfully.", ilConnectionMapping.getTimeZone()));
				metaDataFetch.saveExecutionSourceMappingInfo(null, sourceFileInfo, customRequest);
				message.setCode("SUCCESS");
				message.setText("Web service file upload completed.");
			}

		}
		catch ( AmazonS3Exception e )
		{
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		catch ( OnpremFileCopyException e )
		{
			message.setCode("ERROR");
			message.setText(e.getMessage());
		}
		finally
		{
			if( originalFile != null )
			{
				String basePath = FilenameUtils.getFullPathNoEndSeparator(originalFile);
				FileUtils.forceDelete(new File(basePath));
			}
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	public Table getTempTableAndColumns(Table table, String clientSchemaStaging, ClientData clientData, String clientId, List<Column> columns)
	{
		table.setSchemaName(clientSchemaStaging);
		List<String> originaCols = new ArrayList<String>();
		for (Column col : columns)
		{
			originaCols.add(col.getColumnName());
		}
		table.setOriginalColumnNames(originaCols);
		table.setColumns(columns);
		return table;
	}

	public ILConnectionMapping getIlConnection(Map<String, Object> databaseDetails, ILConnectionMapping ilConnectionMappingInfo, boolean isStaging)
	{

		ILConnectionMapping ilConnectionMappings = new ILConnectionMapping();

		ILConnection ilConnections = new ILConnection();

		String databaseHost1 = databaseDetails.get("region_hostname").toString();
		String databasePort1 = databaseDetails.get("region_port").toString();
		ilConnectionMappings.setiLquery(ilConnectionMappingInfo.getiLquery());
		ilConnections.setUsername(databaseDetails.get("clientdb_username").toString());
		ilConnections.setPassword(databaseDetails.get("clientdb_password").toString());
		ilConnections.setServer(databaseHost1 + ":" + databasePort1 + "/" + (isStaging ? databaseDetails.get("clientdb_staging_schema").toString() : databaseDetails.get("clientdb_schema").toString()));
		Database db = new Database();
		db.setId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
		db.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
		db.setProtocal(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
		db.setDriverName(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
		ilConnections.setDatabase(db);
		ilConnectionMappings.setTypeOfCommand(com.datamodel.anvizent.helper.Constants.QueryType.QUERY);
		ilConnectionMappings.setiLConnection(ilConnections);

		return ilConnectionMappings;
	}

	public String createFileByConncetion(ILConnectionMapping ilConnectionMapping)
	{

		String filePath = null;
		Connection conn = null;
		FileWriterWithEncoding fw = null;
		try
		{

			conn = connectDatabase(ilConnectionMapping.getiLConnection());

			Statement stmt = null;
			CallableStatement cstmt = null;

			if( conn != null )
			{

				String typeOfCommand = ilConnectionMapping.getTypeOfCommand();

				ResultSet res = null;

				boolean isQuery = ("Query".equals(typeOfCommand));

				if( isQuery )
				{
					System.out.println("isQuery 9in if:==========>" + isQuery);
					String query = ilConnectionMapping.getiLquery();
					stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					res = stmt.executeQuery(query);
				}
				else
				{

					String procName = ilConnectionMapping.getiLquery();
					String procParams = ilConnectionMapping.getProcedureParameters();

					List<Map<String, String>> paramList = new ArrayList<>();

					if( StringUtils.isNotEmpty(procParams) )
					{
						String[] params = procParams.split("\\^");

						if( params.length > 0 )
						{
							for (String param : params)
							{
								String[] p = param.split("=");

								if( p.length == 2 )
								{
									Map<String, String> paramMap = new HashMap<>();
									paramMap.put("name", p[0]);
									paramMap.put("value", p[1]);
									paramList.add(paramMap);
								}
							}
						}
					}

					int noofparams = paramList.size();

					StringBuilder query = new StringBuilder();

					query.append("{call ").append(procName);

					if( noofparams > 0 )
					{
						query.append("(");
						for (int i = 1; i <= noofparams; i++)
						{
							query.append("?");
							if( i < noofparams )
							{
								query.append(", ");
							}
						}
						query.append(")");
					}

					query.append("}");

					cstmt = conn.prepareCall(query.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

					if( noofparams > 0 )
					{
						int index = 1;
						for (Map<String, String> paramMap : paramList)
						{
							cstmt.setObject(index++, paramMap.get("value"));
						}
					}

					res = cstmt.executeQuery();
					String fileDir = commonProcessor.createDir(Constants.Temp.getTempFileDir());
					filePath = fileDir + (procName.replaceAll("\\.", "_").replaceAll("\\W+", "")) + ".csv";

				}
				// set table name as file name
				ResultSetMetaData rsMetaData = res.getMetaData();

				if( isQuery )
				{
					String fileDir = commonProcessor.createDir(Constants.Temp.getTempFileDir());
					String tablename = rsMetaData.getTableName(1);
					String newfilename = "";

					if( StringUtils.isNotEmpty(tablename) )
					{
						newfilename = tablename;
					}
					else
					{
						newfilename = "datafile_" + commonProcessor.generateUniqueIdWithTimestamp();
					}

					filePath = fileDir + newfilename + ".csv";
				}
				logger.debug("file path : " + filePath);
				fw = new FileWriterWithEncoding(filePath, Constants.Config.ENCODING_TYPE);

				int columnCount = rsMetaData.getColumnCount();

				for (int i = 1; i <= columnCount; i++)
				{
					fw.append(res.getMetaData().getColumnLabel(i));
					if( i < columnCount ) fw.append(",");

				}
				fw.append(System.getProperty("line.separator"));
				while (res.next())
				{
					for (int i = 1; i <= columnCount; i++)
					{
						String data = res.getString(i);
						if( data != null )
						{
							fw.append(sanitizeForCsv(data));
						}
						if( i < columnCount ) fw.append(",");
					}
					fw.append(System.getProperty("line.separator"));
				}

			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if( conn != null )
				{
					conn.close();
				}
				if( fw != null )
				{
					fw.flush();
					fw.close();
				}
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
		}

		return filePath;
	}

	public String sanitizeForCsv(String cellData)
	{

		if( StringUtils.isEmpty(cellData) ) return "";
		cellData = cellData.trim();
		StringBuilder resultBuilder = new StringBuilder(cellData);
		int lastIndex = 0;
		while (resultBuilder.indexOf("\"", lastIndex) >= 0)
		{
			int quoteIndex = resultBuilder.indexOf("\"", lastIndex);
			resultBuilder.replace(quoteIndex, quoteIndex + 1, "\"\"");
			lastIndex = quoteIndex + 2;
		}

		char firstChar = cellData.charAt(0);
		char lastChar = cellData.charAt(cellData.length() - 1);

		if( cellData.contains(",") || cellData.contains("\r") || cellData.contains("\r\n") || cellData.contains("\n") || Character.isWhitespace(firstChar) || Character.isWhitespace(lastChar) )
		{
			resultBuilder.insert(0, "\"").append("\"");
		}
		return resultBuilder.toString();
	}

	public static Connection connectDatabase(ILConnection iLConnection) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{

		Connection con = null;
		if( iLConnection != null )
		{
			con = ClientJDBCUtil.getClientDataBaseConnection(iLConnection.getDatabase().getConnector_id(), iLConnection.getServer(), iLConnection.getUsername(), iLConnection.getPassword(), iLConnection.getDatabase().getDriverName(), iLConnection.getDatabase().getProtocal() ,iLConnection.isSslEnable(),iLConnection.getSslTrustKeyStoreFilePaths() );
		}
		else
		{
			throw new SQLException("Database connection details not found.");
		}
		return con;
	}
}
