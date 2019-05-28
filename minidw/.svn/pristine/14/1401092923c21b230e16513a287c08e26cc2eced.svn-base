
package com.datamodel.anvizent.data.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CrossReferenceForm;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rajesh.anthari
 *
 */
@RestController("CrossReferenceDataController")
@RequestMapping("" + Constants.AnvizentURL.MINIDW_BASE_URL + "/crossreference")

public class CrossReferenceDataController {

	protected static final Log LOGGER = LogFactory.getLog(CrossReferenceDataController.class);
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("crossReferenceRestTemplateUtilities")
	private RestTemplateUtilities crossRefRestUtilities;
	
	@Autowired
	private MessageSource messageSource;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public DataResponse getCrossReferences(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		dataResponse = crossRefRestUtilities.getRestObject(request, "/getCrossReferences", user.getUserId());
		return dataResponse;
	}

	@RequestMapping(value = "/getDistinctValues", method = RequestMethod.POST)
	public DataResponse getDistinctValues(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "ilId") String ilId, @RequestParam(value = "columnName") String columnName,
			@RequestParam(value = "tableName",required=false) String tableName,
			@RequestParam(value = "columnValue") String columnValue,
			@RequestParam(value = "isXrefValueNull") String isXrefValueNull, Locale locale,
			HttpServletRequest request) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		if (StringUtils.isNotBlank(columnName)) {
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("columnName", columnName);
			map.add("tableName", tableName);
			map.add("columnValue", columnValue);
			map.add("iLId", ilId);
			map.add("isXrefValueNull", isXrefValueNull);

			dataResponse = crossRefRestUtilities.postRestObject(request, "/getDistinctValues", map, user.getUserId());
		}
		return dataResponse;
	}

	@RequestMapping(value = "/bulkCrossReference", method = RequestMethod.POST)
	public DataResponse bulkCrossReference(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			Locale locale, HttpServletRequest request, 
			@RequestParam("filepath") String filePath, @RequestParam("id") Integer id,
			@RequestParam("flatFileType") String flatFileType, @RequestParam("delimeter") String delimeter,
			@RequestParam("selectedIlId") String ilId, @RequestParam("referenceField") String referenceField,
			@RequestParam("xReferenceField") String xReferenceField,
			@RequestParam("iLColumnNames") String iLColumnNames, @RequestParam("selectedFileHeaders") String selectedFileHeaders,
			@RequestParam("dafaultValues") String dafaultValues,
			@RequestParam("conditionName") String conditionName,
			@RequestParam("applicableDate") String applicableDate,
			@RequestParam("xrefExecutionType") String xrefExecutionType) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		String deploymentType = (String) SessionHelper.getSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE);
		S3BucketInfo s3BucketInfo = null;
		File tempFileName = null;
		try {
			s3BucketInfo = getS3BucketInfo( request,userId );//(S3BucketInfo) SessionHelper.getSesionAttribute(request, Constants.Config.S3_BUCKET_INFO);
			String plainUserId = EncryptionServiceImpl.getInstance().decrypt(userId);
			plainUserId = plainUserId.split("#")[0];
			List<String> iLColumnNamesList = CommonUtils.stringToList(iLColumnNames, ",");
			List<String> selectedFileHeadersList = CommonUtils.stringToList(selectedFileHeaders, ",");
			List<String> dafaultValuesList = CommonUtils.stringToList(dafaultValues, ",");
			String destFilePath = MinidwServiceUtil.processFileMappingWithIL(EncryptionServiceImpl.getInstance().decrypt(filePath), "csv", delimeter, null, iLColumnNamesList, selectedFileHeadersList,dafaultValuesList);
			tempFileName = new File(destFilePath);
			SourceFileInfo s3UploadedFileInfo = MinidwServiceUtil.getS3UploadedFileInfo(s3BucketInfo, tempFileName, plainUserId,
					0, null, 0, deploymentType, "crossReference", false, false);
			
			String filePathNew = s3UploadedFileInfo.getFilePath();
			long sourceFileId = 0;
			 DataResponse uploadedFileInfoDataResponse = packageRestUtilities.postRestObject(request, "/updateSourceFileInfo", s3UploadedFileInfo, userId);
				if(uploadedFileInfoDataResponse != null && uploadedFileInfoDataResponse.getMessages().get(0).getCode().equals("SUCCESS")){
					sourceFileId = (int)uploadedFileInfoDataResponse.getObject();
				} else {
					dataResponse = uploadedFileInfoDataResponse;
					return dataResponse;
				}
			map.add("filePath", filePathNew);
			map.add("sourceFileId", sourceFileId);
			map.add("conditionName", conditionName);
			map.add("applicableDate", applicableDate);
			map.add("xrefExecutionType", xrefExecutionType);
		} catch (Exception e) {
			message.setCode("ERROR");
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
			return dataResponse;
		}catch (Throwable e) {
			message.setCode("ERROR");
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
			return dataResponse;
		}
		map.add("iLId", ilId);
		map.add("referenceField", referenceField);
		map.add("xReferenceField", xReferenceField);
		map.add("id", id);
		return crossRefRestUtilities.postRestObject(request, "/bulkCrossReference", map, userId );
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
	@RequestMapping(value = "/getCrossInfo/{ilId}", method = RequestMethod.GET)
	public DataResponse getCrossInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("ilId") String ilId, HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("ilId", ilId);
		return crossRefRestUtilities.postRestObject(request, "/getCrossInfo", map, user.getUserId());
	}

	@RequestMapping(value = "/getColumnValues/{id}", method = RequestMethod.GET)
	public DataResponse getColumnValues(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("id") int id, HttpServletRequest request) {

		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		User user = CommonUtils.getUserDetails(request, null, null);
		map.add("id", id);
		return crossRefRestUtilities.postRestObject(request, "/getColumnValues", map, user.getUserId());
	}

	@RequestMapping(value = "/getAutoMergeQueriesById/{id}", method = RequestMethod.GET)
	public DataResponse getAutoMergeQueriesById(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("id") int id, HttpServletRequest request) {

		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		User user = CommonUtils.getUserDetails(request, null, null);
		map.add("id", id);
		return crossRefRestUtilities.postRestObject(request, "/getAutoMergeQueriesById", map, user.getUserId());
	}

	@RequestMapping(value = "/getUnMergeRowWithColumns", method = RequestMethod.POST)
	public DataResponse getUnMergeRowWithColumns(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody CrossReferenceForm getUnMergeRowWithColumns, HttpServletRequest request) {
		return crossRefRestUtilities.postRestObject(request, "/getUnMergeRowWithColumns", getUnMergeRowWithColumns,
				userId);
	}

	/* New version */

	@RequestMapping(value = "/ilList", method = RequestMethod.GET)
	public DataResponse getIlList(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request) {
		return crossRefRestUtilities.getRestObject(request, "/getAllClientILsForXref", userId);
	}

	@RequestMapping(value = "/columnInfo", method = RequestMethod.GET)
	public DataResponse getColumnInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestParam("tablename") String tableName, HttpServletRequest request) {

		DataResponse columnsResponse = packageRestUtilities.getRestObject(request,
				"/getTablesStructure/{industryId}/{dLOrILName}", userId, 0, tableName);
		List<Column> ilColumnsList = null;

		if (columnsResponse != null && columnsResponse.getHasMessages()
				&& columnsResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);
			ilColumnsList = mapper.convertValue(columnsResponse.getObject(), new TypeReference<List<Column>>() {
			});
		}
		List<String> exceptionColumnsList = new ArrayList<>();
		exceptionColumnsList.add("DataSource_Id");
		exceptionColumnsList.add("Added_Date");
		exceptionColumnsList.add("Added_User");
		exceptionColumnsList.add("Updated_Date");
		exceptionColumnsList.add("Updated_User");

		List<String> columnsNames = new ArrayList<>();
		Map<String, String> columnSize = new HashMap<>();
		Map<String, String> columnDataTypes = new HashMap<>();
		if (ilColumnsList != null) {
			ilColumnsList.forEach(col -> {
				if (!(col.getIsAutoIncrement() || col.getColumnName().startsWith("XRef_")
						|| exceptionColumnsList.indexOf(col.getColumnName()) != -1)) {
					columnsNames.add(col.getColumnName());
					columnSize.put(col.getColumnName(), col.getColumnSize());
					columnDataTypes.put(col.getColumnName(), col.getDataType());
				}
			});
		}

		Map<String, Object> columnInfo = new HashMap<>();
		columnInfo.put("columnsNames", columnsNames);
		columnInfo.put("columnSize", columnSize);
		columnInfo.put("columnDataTypes", columnDataTypes);
		columnsResponse.setObject(columnInfo);
		return columnsResponse;
	}

	@RequestMapping(value = "/records", method = RequestMethod.POST)
	public DataResponse getXrefILColumnrecords(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody CrossReferenceForm crossReferenceForm, Locale locale) {
		DataResponse dataResponse = null;
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getCrossReferenceOption().equals("merge")
					|| crossReferenceForm.getCrossReferenceOption().equals("split")) {
				if (crossReferenceForm.getIlColumnValue() != null && crossReferenceForm.getIlColumnValue().size() > 0) {
					
					MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
					map.add("columnName", crossReferenceForm.getIlColumnName());
					map.add("iLId", crossReferenceForm.getIlId());
					map.add("columnValues",
							String.join(",", crossReferenceForm.getIlColumnValue().toArray(new String[0])));
					map.add("isXrefValueNull", crossReferenceForm.getCrossReferenceOption().equals("merge"));
					dataResponse = crossRefRestUtilities.postRestObject(request, "/getMatchedValues", map,
							user.getUserId());

				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
			;
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/existingXref", method = RequestMethod.POST)
	public DataResponse getXrefILrecordsForExistingXref(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody CrossReferenceForm crossReferenceForm, Locale locale) {
		DataResponse dataResponse = null;
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getCrossReferenceOption().equals("merge")
					|| crossReferenceForm.getCrossReferenceOption().equals("split")) {
				if (crossReferenceForm.getIlColumnValue() != null && crossReferenceForm.getIlColumnValue().size() > 0) {
					
					MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
					map.add("columnName", crossReferenceForm.getIlColumnName());
					map.add("iLId", crossReferenceForm.getIlId());
					map.add("columnValues",
							String.join(",", crossReferenceForm.getIlColumnValue().toArray(new String[0])));
					map.add("isXrefValueNull", crossReferenceForm.getCrossReferenceOption().equals("merge"));
					dataResponse = crossRefRestUtilities.postRestObject(request, "/getMatchedValues", map,
							user.getUserId());

				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
			;
		}
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/existingXrefRecord", method = RequestMethod.POST)
	public DataResponse existingXrefRecord(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam("xrefIlTableName") String xrefIlTableName,
			@RequestBody CrossReferenceForm crossReferenceForm, Locale locale) {
		DataResponse dataResponse = null;
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getCrossReferenceOption().equals("merge")
					|| crossReferenceForm.getCrossReferenceOption().equals("split")) {
				
					MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
					map.add("tableName", xrefIlTableName);
					map.add("columnName", crossReferenceForm.getIlColumnName());
					map.add("existingXrefValue", crossReferenceForm.getExistingXrefValue());
					dataResponse = crossRefRestUtilities.postRestObject(request, "/getExistingXrefRecord", map, user.getUserId());
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/bulkCrossReferenceFileHeader", method = RequestMethod.POST)
	public DataResponse bulkCrossReferenceFileHeader(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @RequestParam("file") MultipartFile file,
			@RequestParam("flatFileType") String flatFileType, @RequestParam("delimeter") String delimeter,
			@RequestParam("selectedIlId") String ilId ) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		Map<String, Object> map = new HashMap<>();
		File tempFileName = null;
		File csvFile = null;
		try {
			tempFileName = CommonUtils.multipartToFile(file);
			if(flatFileType.equals("xls")){
				csvFile = MinidwServiceUtil.getCsvFromXLS(tempFileName);	
			}else if(flatFileType.equals("xlsx")){
				csvFile = MinidwServiceUtil.getCsvFromXLSX(tempFileName);	
			}else{
				csvFile = tempFileName;
			}
			String filePath = EncryptionServiceImpl.getInstance().encrypt(csvFile.getAbsolutePath());
			List<String> originalFileheaders = MinidwServiceUtil.getHeadersFromFile(csvFile.getAbsolutePath(), "csv", delimeter, null);
			map.put("filePath", filePath);
			map.put("columnsList", originalFileheaders);
			dataResponse.setObject(map);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (Throwable e) {
			LOGGER.error("",e);
			message.setCode("ERROR");
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
			return dataResponse;
		} finally {
			if (tempFileName == null) {
				try {
					FileUtils.forceDeleteOnExit(tempFileName);
				} catch (IOException e) {
					LOGGER.error("",e);
				}
			}
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/autoMergeCrossReference", method = RequestMethod.POST)
	public DataResponse autoMergeCrossReference(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody CrossReferenceForm crossReferenceForm, 
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			dataResponse = crossRefRestUtilities.postRestObject(request, "/autoMergeCrossReference", crossReferenceForm,
					user.getUserId());
		} catch (Exception e) {
			LOGGER.error("", e);
			message.setCode("FAILED");
			message.setText(messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveAutoMergeCrossReference", method = RequestMethod.POST)
	public DataResponse saveAutoMergeCrossReference(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody CrossReferenceForm crossReferenceForm, 
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			dataResponse = crossRefRestUtilities.postRestObject(request, "/saveAutoMergeCrossReference", crossReferenceForm,
					user.getUserId());
		} catch (Exception e) {
			LOGGER.error("", e);
			message.setCode("FAILED");
			message.setText(messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		return dataResponse;
	}
	

	@RequestMapping(value = "/mergeCrossReferenceRecords", method = RequestMethod.POST)
	public DataResponse mergeCrossReferenceRecords(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody CrossReferenceForm crossReferenceForm, 
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("id", crossReferenceForm.getId());
			map.add("columnName", crossReferenceForm.getIlColumnName());
			map.add("iLId", crossReferenceForm.getIlId());
			map.add("crossReferenceOption", crossReferenceForm.getCrossReferenceOption());
			map.add("ilColumnValue", crossReferenceForm.getIlColumnValue());
			map.add("newOrExistingXref", crossReferenceForm.getNewOrExistingXref());
			map.add("mergeColumnValues", String.join(",", crossReferenceForm.getIlMergeColumns().toArray(new String[0])));
			map.add("xReferenceColumnValue", crossReferenceForm.getIlXreferenceColumn());
			map.add("autoincrementColumnName", crossReferenceForm.getAutoincrementColumnName());
			map.add("xRefKeyColumnName", crossReferenceForm.getxRefKeyColumnName());
			map.add("ilXreferenceValue", crossReferenceForm.getIlXreferenceValue());
			map.add("conditionName", crossReferenceForm.getConditionName());
			map.add("applicableDate", crossReferenceForm.getApplicableDate());
			map.add("xrefExecutionType", crossReferenceForm.getXrefExecutionType());
			dataResponse = crossRefRestUtilities.postRestObject(request, "/mergeCrossReferenceRecords", map, clientId);
			
		} catch (Exception e) {
			LOGGER.error("", e);
			message.setCode("FAILED");
			message.setText(messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		return dataResponse;
	}
	

	@RequestMapping(value = "/mergeCrossReferenceRecordsWithExistingXref", method = RequestMethod.POST)
	public DataResponse mergeCrossReferenceRecordsWithExistingXref(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody CrossReferenceForm crossReferenceForm, 
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("id", crossReferenceForm.getId());
			map.add("iLId", crossReferenceForm.getIlId());
			map.add("existingXrefKey", crossReferenceForm.getExistingXrefKey());
			map.add("mergeColumnValues", String.join(",", crossReferenceForm.getIlMergeColumns().toArray(new String[0])));
			map.add("xRefKeyColumnName", crossReferenceForm.getxRefKeyColumnName());
			map.add("autoincrementColumnName", crossReferenceForm.getAutoincrementColumnName());
			map.add("conditionName", crossReferenceForm.getConditionName());
			map.add("applicableDate", crossReferenceForm.getApplicableDate());
			map.add("xrefExecutionType", crossReferenceForm.getXrefExecutionType());
			map.add("ilColumnValues", crossReferenceForm.getIlColumnValue());
			map.add("columnName", crossReferenceForm.getIlColumnName());
			dataResponse = crossRefRestUtilities.postRestObject(request, "/mergeCrossReferenceRecordsWithExistingXref", map, clientId);
			
		} catch (Exception e) {
			LOGGER.error("", e);
			message.setCode("FAILED");
			message.setText(messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/unMergeSelectedCrossReferenceRecord", method = RequestMethod.POST)
	public DataResponse unMergeSelectedCrossReferenceRecord(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody CrossReferenceForm crossReferenceForm, 
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			
			map.add("iLId", crossReferenceForm.getIlId());
			map.add("ilColumnValue", crossReferenceForm.getIlValue());
			map.add("autoincrementColumnName", crossReferenceForm.getAutoincrementColumnName());
			map.add("xRefKeyColumnName", crossReferenceForm.getxRefKeyColumnName());
			dataResponse = crossRefRestUtilities.postRestObject(request, "/unMergeSelectedCrossReferenceRecord", map, clientId);
			
		} catch (Exception e) {
			LOGGER.error("", e);
			message.setCode("FAILED");
			message.setText(messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		return dataResponse;
	}
	

	@RequestMapping(value = "/splitRecords", method = RequestMethod.POST)
	public DataResponse splitRecords(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response,
			@RequestBody CrossReferenceForm crossReferenceForm, 
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("columnName", crossReferenceForm.getIlColumnName());
			map.add("iLId", crossReferenceForm.getIlId());
			map.add("splitColumnValues", String.join(",", crossReferenceForm.getIlMergeColumns().toArray(new String[0])));
			map.add("autoincrementColumnName", crossReferenceForm.getAutoincrementColumnName());
			map.add("xRefKeyColumnName", crossReferenceForm.getxRefKeyColumnName());
			dataResponse = crossRefRestUtilities.postRestObject(request, "/splitCrossReferenceRecords", map, clientId);
			
		} catch (Exception e) {
			LOGGER.error("", e);
			message.setCode("FAILED");
			message.setText(messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/runCrossReferenceById/{id}", method = RequestMethod.GET)
	public DataResponse runXrefById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer id,
			Locale locale) {
		
			return crossRefRestUtilities.getRestObject(request, "/runCrossReferenceById/{id}",  clientId, id);
	}
	
	@RequestMapping(value = "/archiveCrossReferenceById/{id}", method = RequestMethod.GET)
	public DataResponse archiveCrossReferenceById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") Integer id, Locale locale, HttpServletRequest request) {
			return crossRefRestUtilities.getRestObject(request, "/archiveCrossReferenceById/{id}", clientId, id);
	}
	
	@RequestMapping(value = "/unmergeExistingXRefandDelete/{id}/{executionType}", method = RequestMethod.GET)
	public DataResponse unmergeExistingXRefandDelete(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") Integer id, @PathVariable("executionType") String executionType, Locale locale, HttpServletRequest request) {
			return crossRefRestUtilities.getRestObject(request, "/unmergeExistingXRefandDelete/{id}/{executionType}", clientId, id, executionType);
	}
	
	@RequestMapping(value = "/activateCrossReferenceById/{id}", method = RequestMethod.GET)
	public DataResponse activateCrossReferenceById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") Integer id, Locale locale, HttpServletRequest request) {
			return crossRefRestUtilities.getRestObject(request, "/activateCrossReferenceById/{id}", clientId, id);
	}
	
	@RequestMapping(value = "/getCrossReferenceLogsInfo/{id}", method = RequestMethod.GET)
	public DataResponse getCrossReferenceLogsInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer id,
			Locale locale) {
		
			return crossRefRestUtilities.getRestObject(request, "/getCrossReferenceLogsInfo/{id}",  clientId, id);
	}
	
	@RequestMapping(value = "/autoSplitConditionById/{id}/{executionType}" , method = RequestMethod.GET)
	public DataResponse autoSplitConditionById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer id,
			@PathVariable("executionType") String executionType,
			Locale locale) {
		return crossRefRestUtilities.getRestObject(request, "/autoSplitConditionById/{id}/{executionType}", clientId, id, executionType);
	}

}
