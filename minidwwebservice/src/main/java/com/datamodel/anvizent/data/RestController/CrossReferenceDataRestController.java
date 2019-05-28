
package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.TimeZoneDateHelper;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.CrossReferenceConstants;
import com.datamodel.anvizent.service.CrossReferenceService;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CrossReferenceForm;
import com.datamodel.anvizent.service.model.CrossReferenceLogs;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.User;

/**
 * @author rajesh.anthari
 *
 */
@RestController("CrossReferenceDataRestController")
@RequestMapping(Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/crossreference")
@CrossOrigin
public class CrossReferenceDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(CrossReferenceDataRestController.class);

	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PackageService packageService;
	@Autowired
	private CrossReferenceService crossReferenceService;
	@Autowired
	FileService fileService;
	@Autowired
	MessageSource messageSource;
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;


	/**
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/getAllClientILsForXref", method = RequestMethod.GET)
	public DataResponse getAllClientILsForXref(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {

		LOGGER.info("in getAllClientILsForXref()");
		List<ILInfo> ilList = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			ilList = crossReferenceService.getAllClientILsForXref(clientIdfromHeader, clientAppDbJdbcTemplate);
			if (ilList != null) {
				List<ILInfo> finalizedIlsList = ilList.stream().filter(il -> il.getXref_il_table_name() != null).collect(Collectors.toList());
				dataResponse.setObject(finalizedIlsList);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			}
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getCrossReferences", method = RequestMethod.GET)
	public DataResponse getCrossReferences(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
            List<CrossReferenceLogs> crossReferenceLogs = crossReferenceService.getAllCrossReferences(clientIdfromHeader, clientAppDbJdbcTemplate);
            dataResponse.setObject(crossReferenceLogs);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		}catch(Throwable t) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getDistinctValues", method = RequestMethod.POST)
	public DataResponse getDistinctValues(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("columnName") String columnName,
			@RequestParam("isXrefValueNull") Boolean isXrefValueNull, @RequestParam("iLId") String ilId,
			@RequestParam(value = "columnValue", required = false) String columnValue, Locale locale) {
		LOGGER.info("in getDistinctValues()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			ILInfo ilInfo = packageService.getILById(Integer.parseInt(ilId), clientId, clientAppDbJdbcTemplate);
			List<Column> columns = packageService.getTableStructure(null, ilInfo.getiL_table_name(), 0, clientId,
					clientJdbcTemplate);
			String xRefColumnName = getXrefColumnName(columns);

			List<Object> distinctValue = crossReferenceService.getDistinctValues(columnName, ilInfo.getiL_table_name(),
					ilInfo.getXref_il_table_name(), columnValue, isXrefValueNull, xRefColumnName, clientJdbcTemplate);
			dataResponse.setObject(distinctValue);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	private String getXrefColumnName(List<Column> columns) {
		String columnName = "";
		for (Column col : columns) {
			if (col.getColumnName().startsWith("XRef_")) {
				columnName = col.getColumnName();
			}
		}
		return columnName;
	}
	
	private String getAutoIncColumnName(List<Column> columns) {
		String columnName = "";
		for (Column col : columns) {
			if (col.getIsAutoIncrement()) {
				columnName = col.getColumnName();
				break;
			}
		}
		return columnName;
	}

	@RequestMapping(value = "/getMatchedValues", method = RequestMethod.POST)
	public DataResponse getMatchedValues(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("columnName") String columnName,
			@RequestParam("isXrefValueNull") Boolean isXrefValueNull, @RequestParam("iLId") String ilId,
			@RequestParam("columnValues") List<String> columnValues, Locale locale) {
		LOGGER.info("in getMatchedValues()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			int valueIndex = columnValues.size();
			for (int i = 0; i < valueIndex; i++) {
				if (columnValues.get(i).contains("#$#$#$")) {
					String xRefValue = StringUtils.replace(columnValues.get(i), "#$#$#$", ",");
					columnValues.set(i, xRefValue);
				}
			}

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			ILInfo ilInfo = packageService.getILById(Integer.parseInt(ilId), clientId, clientAppDbJdbcTemplate);
			List<Column> columns = packageService.getTableStructure(null, ilInfo.getiL_table_name(), 0, clientId,
					clientJdbcTemplate);
			String xRefColumnName = getXrefColumnName(columns);

			Map<String, Object> matchedRecords = crossReferenceService.getMatchedRecordWithMap(columnName, columnValues,
					ilInfo.getiL_table_name(), ilInfo.getXref_il_table_name(), isXrefValueNull, xRefColumnName,
					clientJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(matchedRecords);

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/mergeCrossReferenceRecords", method = RequestMethod.POST)
	public DataResponse mergeCrossReferenceRecords(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request,
			@RequestParam("id") Integer id,
			@RequestParam("columnName") String columnName,
			@RequestParam("iLId") Integer ilId, 
			@RequestParam("crossReferenceOption") String crossReferenceOption,
			@RequestParam("ilColumnValue") String ilColumnValue,
			@RequestParam("newOrExistingXref") String newOrExistingXref,
			@RequestParam("mergeColumnValues") List<String> mergeColumnValues,
			@RequestParam("xReferenceColumnValue") String xReferenceColumnValue,
			@RequestParam("autoincrementColumnName") String autoincrementColumnName,
			@RequestParam("conditionName") String conditionName,
			@RequestParam("applicableDate") String applicableDate,
			@RequestParam("xRefKeyColumnName") String xRefKeyColumnName, 
			@RequestParam("xrefExecutionType") String xrefExecutionType,
			Locale locale) {
		LOGGER.info("in mergeCrossReferenceRecords()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String startDate = TimeZoneDateHelper.getFormattedDateString();
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();

			ILInfo ilInfo = packageService.getILById(ilId, clientId, clientAppDbJdbcTemplate);
			String ilTableName = ilInfo.getiL_table_name();
			String xRefIlTableName = ilInfo.getXref_il_table_name();
			List<Column> columns = packageService.getTableStructure(null, ilTableName, 0, clientId, clientJdbcTemplate);
			Map<String, Object> responseParams = crossReferenceService.mergeXreferenceRecords(ilTableName, xRefIlTableName,
					autoincrementColumnName, xRefKeyColumnName, columns, mergeColumnValues, xReferenceColumnValue,
					columnName, null, conditionName, clientJdbcTemplate);

			boolean mergeStatus = (boolean) responseParams.get("mergeStatus");
			long xrefKey = (long) responseParams.get("xref_key");
			String endDate = TimeZoneDateHelper.getFormattedDateString();
			CrossReferenceLogs crossReferenceLogs = new CrossReferenceLogs(id, ilId,
					CrossReferenceConstants.MERGER.toString(), CrossReferenceConstants.MANUALMERGE.toString(),
					conditionName, columnName, ilColumnValue, CrossReferenceConstants.NEW_XREF.toString(), "", "",
					applicableDate, "", startDate, endDate, modification, xrefKey);
			
				crossReferenceService.manualmergeLog(crossReferenceLogs, clientAppDbJdbcTemplate);
			
			if (mergeStatus) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.mergingComplated", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.success.text.mergingFailed", null, locale));
			}

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/unMergeSelectedCrossReferenceRecord", method = RequestMethod.POST)
	public DataResponse unMergeSelectedCrossReferenceRecord(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			@RequestParam("iLId") String ilId, @RequestParam("ilColumnValue") Integer ilColumnValue,
			@RequestParam("autoincrementColumnName") String autoincrementColumnName,
			@RequestParam("xRefKeyColumnName") String xRefKeyColumnName, Locale locale) {
		LOGGER.info("in mergeCrossReferenceRecords()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();

			ILInfo ilInfo = packageService.getILById(Integer.parseInt(ilId), clientId, clientAppDbJdbcTemplate);
			String ilTableName = ilInfo.getiL_table_name();
			int updatedCount = crossReferenceService.unMergeSelectedCrossReferenceRecord(ilTableName,
					autoincrementColumnName, xRefKeyColumnName, ilColumnValue, clientJdbcTemplate);

			if (updatedCount > 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.text.unMergedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.success.text.mergingFailed", null, locale));
			}

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/mergeCrossReferenceRecordsWithExistingXref", method = RequestMethod.POST)
	public DataResponse mergeCrossReferenceRecordsWithExistingXref(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			@RequestParam("id") Integer id,
			@RequestParam("iLId") Integer ilId, 
			@RequestParam("existingXrefKey") String existingXrefKey,
			@RequestParam("mergeColumnValues") List<String> mergeColumnValues,
			@RequestParam("xRefKeyColumnName") String xRefKeyColumnName,
			@RequestParam("conditionName") String conditionName,
			@RequestParam("applicableDate") String applicableDate, 
			@RequestParam("autoincrementColumnName") String autoincrementColumnName,
			@RequestParam("xrefExecutionType") String xrefExecutionType,
			@RequestParam("ilColumnValues") String ilColumnValues,
			@RequestParam("columnName") String columnName, Locale locale) {
		LOGGER.info("in mergeCrossReferenceRecordsWithExistingXref()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String startDate = TimeZoneDateHelper.getFormattedDateString();
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			ILInfo ilInfo = packageService.getILById(ilId, clientId, clientAppDbJdbcTemplate);
			String ilTableName = ilInfo.getiL_table_name();
			Map<String, Object> responseParams = crossReferenceService.mergeCrossReferenceRecordsWithExistingXref(ilTableName,
					existingXrefKey, mergeColumnValues, xRefKeyColumnName, autoincrementColumnName, clientJdbcTemplate);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("selectedrecords", mergeColumnValues);
			
			boolean mergeStatus = (boolean) responseParams.get("mergeStatus");
			long xrefKey = Long.parseLong((String) responseParams.get("xref_key"));
			
			String endDate = TimeZoneDateHelper.getFormattedDateString();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			CrossReferenceLogs crossReferenceLogs = new CrossReferenceLogs(id, ilId,
					CrossReferenceConstants.MERGER.toString(), CrossReferenceConstants.MANUALMERGE.toString(),
					conditionName, columnName, ilColumnValues, CrossReferenceConstants.EXISTING_XREF.toString(), "", existingXrefKey,
					applicableDate, jsonObject.toString(), startDate, endDate, modification, xrefKey);
			
				crossReferenceService.manualmergeLog(crossReferenceLogs, clientAppDbJdbcTemplate);
			
			if (mergeStatus) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.mergingComplated", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.success.text.mergingFailed", null, locale));
			}

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/splitCrossReferenceRecords", method = RequestMethod.POST)
	public DataResponse splitCrossReferenceRecords(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("columnName") String columnName,
			@RequestParam("iLId") Integer ilId, @RequestParam("splitColumnValues") List<String> mergeColumnValues,
			@RequestParam("autoincrementColumnName") String autoincrementColumnName,
			@RequestParam("xRefKeyColumnName") String xRefKeyColumnName, Locale locale) {
		LOGGER.info("in splitCrossReferenceRecords()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String startDate = TimeZoneDateHelper.getFormattedDateString();
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();

			ILInfo ilInfo = packageService.getILById(ilId, clientId, clientAppDbJdbcTemplate);
			String ilTableName = ilInfo.getiL_table_name();
			String xRefIlTableName = ilInfo.getXref_il_table_name();
			List<Column> columns = packageService.getTableStructure(null, ilTableName, 0, clientId, clientJdbcTemplate);
			boolean mergeStatus = crossReferenceService.splitXreferenceRecords(ilTableName, xRefIlTableName,
					autoincrementColumnName, xRefKeyColumnName, columns, mergeColumnValues, clientJdbcTemplate);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("selectedrecords", mergeColumnValues);
			
			String endDate = TimeZoneDateHelper.getFormattedDateString();
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			
			CrossReferenceLogs crossReferenceLogs = new CrossReferenceLogs(ilId,
					CrossReferenceConstants.SPLIT.toString(),jsonObject.toString(), startDate, endDate, modification);
			
			crossReferenceService.splitLog(crossReferenceLogs, clientAppDbJdbcTemplate);
			
			if (mergeStatus) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.splittingComplated", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.splittingFailed", null, locale));
			}

		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getExistingXrefRecord", method = RequestMethod.POST)
	public DataResponse getExistingXrefRecord(HttpServletRequest request,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("tableName") String tableName, @RequestParam("columnName") String columnName,
			@RequestParam("existingXrefValue") String existingXrefValue, Locale locale) {
		LOGGER.info("in getExistingXrefRecord()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			Map<String, Object> xrefMatchedData = crossReferenceService.getExistingXrefRecord(tableName, columnName,
					existingXrefValue, clientJdbcTemplate);
			dataResponse.setObject(xrefMatchedData);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getCrossInfo", method = RequestMethod.POST)
	public DataResponse getCrossInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("ilId") Integer ilId, Locale locale) {
		LOGGER.info("in getCrossInfo()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<CrossReferenceLogs> crossInfoList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			crossInfoList = crossReferenceService.getCrossReferenceLogsInfo(ilId, clientAppDbJdbcTemplate);
			
			dataResponse.setObject(crossInfoList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getColumnValues", method = RequestMethod.POST)
	public DataResponse getColumnValues(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("id") int id, Locale locale) {
		LOGGER.info("in getColumnValues()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		CrossReferenceLogs crossInfoList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			crossInfoList = crossReferenceService.getCrossReferenceLogsInfoById(id, clientAppDbJdbcTemplate);
			dataResponse.setObject(crossInfoList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getAutoMergeQueriesById", method = RequestMethod.POST)
	public DataResponse getAutoMergeQueriesById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,@RequestParam("id") int id,Locale locale) {
		LOGGER.info("in getAutoMergeQueriesById()");
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		String crossInfoList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			crossInfoList = crossReferenceService.getAutoMergeQueriesById(id,clientIdfromHeader,clientAppDbJdbcTemplate);
			dataResponse.setObject(crossInfoList);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (Exception ae) {
			packageService.logError(ae, request,clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(ae.getMessage());
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}


	@RequestMapping(value = "/getXrefTableRecordsWithMap", method = RequestMethod.POST)
	public DataResponse getXrefTableRecordsWithMap(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("xrefIlTableName") String xrefIlTableName, Locale locale) {
		LOGGER.info("in getXrefTableRecordsWithMap()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Map<String, Object> xrefTableRecordsWithMap = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			xrefTableRecordsWithMap = crossReferenceService.getXrefTableRecordsWithMap(xrefIlTableName,
					clientJdbcTemplate);
			dataResponse.setObject(xrefTableRecordsWithMap);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/getUnMergeRowWithColumns", method = RequestMethod.POST)
	public DataResponse getUnMergeRowWithColumns(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestBody CrossReferenceForm crossReferenceForm, Locale locale) {
		LOGGER.info("in getUnMergeRowWithColumns()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		Map<String, Object> xrefTableRecordsWithMap = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			ILInfo ilInfo = packageService.getILById(crossReferenceForm.getIlId(), clientId, clientAppDbJdbcTemplate);
			crossReferenceForm.setIlName(ilInfo.getXref_il_table_name());
			xrefTableRecordsWithMap = crossReferenceService.getUnMergeRowWithColumns(crossReferenceForm,
					clientJdbcTemplate);
			dataResponse.setObject(xrefTableRecordsWithMap);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/autoMergeCrossReference", method = RequestMethod.POST)
	public DataResponse autoMergeCrossReference(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			 @RequestBody CrossReferenceForm crossReferenceForm, Locale locale) {
		LOGGER.info("in autoMergeCrossReference()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		//Map<String,Object> xrefTableRecordsWithMap = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			User user = new User();
			user.setClientId(clientIdfromHeader);
			user.setUserName(clientId);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			ILInfo ilInfo = packageService.getILById(crossReferenceForm.getIlId(), clientId, clientAppDbJdbcTemplate);
			crossReferenceForm.setIlName(ilInfo.getXref_il_table_name());
			
			List<Column> columns = packageService.getTableStructure(null, ilInfo.getiL_table_name(), 0, clientId, clientJdbcTemplate);
			ilInfo.setColumns(columns);
			crossReferenceForm.setxRefKeyColumnName(getXrefColumnName(columns));
			crossReferenceForm.setAutoincrementColumnName(getAutoIncColumnName(columns));
			crossReferenceForm.setIlName(ilInfo.getXref_il_table_name());
			Map<String, Object> mappedData = crossReferenceService.processAutoMergeCrossReference(crossReferenceForm, ilInfo,user, clientJdbcTemplate,clientAppDbJdbcTemplate);
			
			int totalMergedRecords = 0;
			for (Entry<String, Object> entry : mappedData.entrySet()) {
				totalMergedRecords += Integer.parseInt(entry.getValue().toString());
		    }
			
			dataResponse.setObject(crossReferenceForm);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(mappedData.size()+" "+messageSource.getMessage("anvizent.package.success.xrefswith", null, locale)+" "+ totalMergedRecords + " " +messageSource.getMessage("anvizent.package.success.recordsprocessed", null, locale));
		} catch (Throwable e) {   
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			e.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveAutoMergeCrossReference", method = RequestMethod.POST)
	public DataResponse saveAutoMergeCrossReference(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			 @RequestBody CrossReferenceForm crossReferenceForm, Locale locale) {
		LOGGER.info("in autoMergeCrossReference()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		//Map<String,Object> xrefTableRecordsWithMap = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			User user = new User();
			user.setClientId(clientIdfromHeader);
			user.setUserName(clientId);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			ILInfo ilInfo = packageService.getILById(crossReferenceForm.getIlId(), clientId, clientAppDbJdbcTemplate);
			
			List<Column> columns = packageService.getTableStructure(null, ilInfo.getiL_table_name(), 0, clientId, clientJdbcTemplate);
			ilInfo.setColumns(columns);
			crossReferenceForm.setxRefKeyColumnName(getXrefColumnName(columns));
			crossReferenceForm.setAutoincrementColumnName(getAutoIncColumnName(columns));
			crossReferenceForm.setIlName(ilInfo.getXref_il_table_name());
			
			int saveStatus = crossReferenceService.saveorEditAutoMergeCrossReference(crossReferenceForm, ilInfo,user, clientJdbcTemplate,clientAppDbJdbcTemplate);
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(user.getUserName());
			String date = TimeZoneDateHelper.getFormattedDateString();
			if( saveStatus == 1 && 
					crossReferenceForm.getXrefExecutionType().equals(Constants.CrossReferenceExecutionType.EDIT) )
			{
				CrossReferenceLogs crossReferenceLogs = new CrossReferenceLogs(crossReferenceForm.getId(), crossReferenceForm.getIlId(), CrossReferenceConstants.MERGER.toString(), CrossReferenceConstants.AUTOMERGE.toString(), crossReferenceForm.getConditionName(),
						String.join(",", crossReferenceForm.getIlMergeColumns()), crossReferenceForm.getApplicableDate(), "", date, date, modification, crossReferenceForm.getConditionObject(), Constants.CrossReferenceExecutionType.EDIT);
				crossReferenceService.processAutoMergeCrossReference(ilInfo, user, clientJdbcInstance.getClientDbCredentials(), crossReferenceLogs, clientJdbcTemplate, clientAppDbJdbcTemplate);
			}
			if(saveStatus == 1) {
				 message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				 message.setText(
							messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			}
		} catch (Throwable e) {   
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			e.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/bulkCrossReference", method = RequestMethod.POST)
	public DataResponse bulkCrossReference(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, HttpServletRequest request,
			 @RequestParam("filePath") String filePath,
			 @RequestParam("iLId") Integer ilId, 
			 @RequestParam("id") Integer id,
			 @RequestParam("referenceField") String referenceField,
			 @RequestParam("xReferenceField") String xReferenceField,
			 @RequestParam("conditionName") String conditionName,
			 @RequestParam("sourceFileId") Integer fileInfoId,
			 @RequestParam("applicableDate") String applicableDate,
			 @RequestParam("xrefExecutionType") String xrefExecutionType,
			 Locale locale) {
		LOGGER.info("in bulkCrossReference()");
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			User user = new User();
			user.setClientId(clientIdfromHeader);
			user.setUserName(clientId);
			String deploymentType = request.getHeader(Constants.Config.DEPLOYMENT_TYPE);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			ILInfo ilInfo = packageService.getILById(ilId, clientId, clientAppDbJdbcTemplate);
			List<Column> columns = packageService.getTableStructure(null, ilInfo.getiL_table_name(), 0, clientId, clientJdbcTemplate);
			ilInfo.setColumns(columns);
			S3BucketInfo s3BucketInfo = userService.getS3BucketInfoByClientId(clientIdfromHeader, null);
			List<String> fileFromS3 = MinidwServiceUtil.downloadFilesFromS3(filePath, clientIdfromHeader, deploymentType, false, s3BucketInfo, false);
			crossReferenceService.processBulkMergeCrossReference(id, ilInfo, user, clientJdbcInstance.getClientDbCredentials(), fileFromS3.get(0), referenceField,
					                                            xReferenceField, fileInfoId, conditionName, xrefExecutionType, applicableDate, clientJdbcTemplate, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("Bulk merge successfully completed");
		} catch (Throwable e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			e.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(MinidwServiceUtil.getErrorMessageString(e));
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}


	@RequestMapping(value = "/jobXrefJobResults/{ilId}", method = RequestMethod.GET)
	public DataResponse getJobResultsForHistoricalLoad(@PathVariable("ilId") Integer ilId,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = packageService.getJobResultsForCrossReference(ilId, clientIdfromHeader, null,clientJdbcTemplate);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/jobXrefJobResults/{ilId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public DataResponse getJobResultsByDate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("ilId") Integer ilId,
			@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate,
			HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = packageService.getJobResultsForCrossReferenceByDate(ilId,clientIdfromHeader, null,fromDate, toDate, clientJdbcTemplate);

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}
	
	
	@RequestMapping(value = "/getCrossReferenceLogsInfo", method = RequestMethod.GET)
	public DataResponse getCrossReferenceLogsInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, 
			 @RequestParam("ilId") Integer ilId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<CrossReferenceLogs> crossReferenceLogs = null; 
		JdbcTemplate clientAppDbJdbcTemplate = null;

		try {
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			crossReferenceLogs = crossReferenceService.getCrossReferenceLogsInfo(ilId, clientAppDbJdbcTemplate);
			if (crossReferenceLogs != null) {
				dataResponse.setObject(crossReferenceLogs);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveCrossReferenceLogsInfo", null, locale));
			}

		}  catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/getCrossReferenceLogsInfo/{id}", method = RequestMethod.GET)
	public DataResponse getCrossReferenceLogsInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,@PathVariable("id") int id, Locale locale,
			HttpServletRequest request) {

		CrossReferenceLogs crossReferenceLogs = null;
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			crossReferenceLogs = crossReferenceService.getCrossReferenceLogsInfoById(id,clientAppDbJdbcTemplate);
			if (crossReferenceLogs != null) {
				dataResponse.setObject(crossReferenceLogs);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unabletoRetrieveCrossReferenceLogsInfo", null, locale));
			}
		} catch (Throwable t) {
			packageService.logError(t, request, clientAppDbJdbcTemplate);
			t.printStackTrace();
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		} finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/crossReferencesByIlId/{ilId}", method = RequestMethod.GET)
	public DataResponse getCrossReferencesByIlId(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("ilId") Integer ilId, 
			Locale locale, HttpServletRequest request) {
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		List<CrossReferenceLogs> crossReferenceLogs = null;
		try {
			String clientId = CommonUtils.getUserClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			crossReferenceLogs = crossReferenceService.getCrossReferencesByIlId(ilId, clientId, clientAppDbJdbcTemplate);
			
			dataResponse.setObject(crossReferenceLogs);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		}catch(Throwable t) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
		}finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
		
	}
	
	@RequestMapping(value = "/processCrossReference", method = RequestMethod.POST)
	public DataResponse executeCrossReference(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @RequestBody CrossReferenceLogs crossReferenceLogs,
			HttpServletRequest request, Locale locale) {
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null;
		try {
			String  clientId = CommonUtils.getUserClientIDFromHeader(request);
			User user = new User();
			user.setClientId(clientId);
			user.setUserName(userId);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			ILInfo ilInfo = packageService.getILById(crossReferenceLogs.getIlId(), clientId, clientAppDbJdbcTemplate);
			List<Column> columns = packageService.getTableStructure(null, ilInfo.getiL_table_name(), 0, clientId, clientJdbcTemplate);
			ilInfo.setColumns(columns);
			crossReferenceLogs.setXrefExecutionType(Constants.CrossReferenceExecutionType.REGULAR);
			crossReferenceService.processAutoMergeCrossReference(ilInfo, user, clientJdbcInstance.getClientDbCredentials(), crossReferenceLogs, clientJdbcTemplate, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		}catch(Throwable t) {
			throw new RuntimeException(t);
		}finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
    
	@RequestMapping(value = "/runCrossReferenceById/{id}", method = RequestMethod.GET)
	public DataResponse runCrossReferenceById(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("id") Integer conditionId,
			HttpServletRequest request, Locale locale) {
		
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientAppDbJdbcTemplate = null;
			JdbcTemplate clientJdbcTemplate = null;
			CrossReferenceLogs crossReferenceLogs = null;
			try {
				String  clientId = CommonUtils.getUserClientIDFromHeader(request);
				User user = new User();
				user.setClientId(clientId);
				user.setUserName(userId);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				crossReferenceLogs = crossReferenceService.getCrossReferenceLogsInfoById(conditionId, clientAppDbJdbcTemplate);
				ILInfo ilInfo = packageService.getILById(crossReferenceLogs.getIlId(), clientId, clientAppDbJdbcTemplate);
				List<Column> columns = packageService.getTableStructure(null, ilInfo.getiL_table_name(), 0, clientId, clientJdbcTemplate);
				ilInfo.setColumns(columns);
				crossReferenceLogs.setXrefExecutionType(Constants.CrossReferenceExecutionType.REGULAR);
				crossReferenceService.processAutoMergeCrossReference(ilInfo, user, clientJdbcInstance.getClientDbCredentials(), crossReferenceLogs, clientJdbcTemplate, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(crossReferenceLogs.getConditionName() + " executed successfully");
			}catch(Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(t.getMessage());
			}finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
	}
	
	@RequestMapping(value = "/archiveCrossReferenceById/{id}", method = RequestMethod.GET)
	public DataResponse archiveCrossReferenceById(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("id") Integer conditionId,
			HttpServletRequest request, Locale locale) {
		
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientAppDbJdbcTemplate = null;
			try {
				String  clientId = CommonUtils.getUserClientIDFromHeader(request);
				User user = new User();
				user.setClientId(clientId);
				user.setUserName(userId);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				crossReferenceService.archiveCrossReferenceById(clientId, conditionId, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText("Archived successfully");
			}catch(Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(t.getMessage());
			}finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
	}
	
	@RequestMapping(value = "/unmergeExistingXRefandDelete/{id}/{executionType}", method = RequestMethod.GET)
	public DataResponse unmergeExistingXRefandDelete(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, 
			@PathVariable("id") Integer conditionId,
			@PathVariable("executionType") String executionType,
			HttpServletRequest request, Locale locale) {
		
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientAppDbJdbcTemplate = null;
			JdbcTemplate clientJdbcTemplate = null;
			try {
				String  clientId = CommonUtils.getUserClientIDFromHeader(request);
				User user = new User();
				user.setClientId(clientId);
				user.setUserName(userId);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				if(executionType.equals(Constants.CrossReferenceExecutionType.DELETE)) {
					CrossReferenceLogs crossReferenceLogs = crossReferenceService.getCrossReferenceLogsInfoById(conditionId, clientAppDbJdbcTemplate);
				    crossReferenceLogs.setXrefExecutionType(Constants.CrossReferenceExecutionType.DELETE);
					ILInfo ilInfo = packageService.getILById(crossReferenceLogs.getIlId(), clientId, clientAppDbJdbcTemplate);
					List<Column> columns = packageService.getTableStructure(null, ilInfo.getiL_table_name(), 0, clientId, clientJdbcTemplate);
					ilInfo.setColumns(columns);
					crossReferenceService.processDeleteCrossReference(crossReferenceLogs, ilInfo, user, clientJdbcInstance.getClientDbCredentials(), clientAppDbJdbcTemplate);
					crossReferenceService.archiveCrossReferenceById(clientId, conditionId, clientAppDbJdbcTemplate);
				}else {
					crossReferenceService.archiveCrossReferenceById(clientId, conditionId, clientAppDbJdbcTemplate);
				}
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.package.label.successfullyDeleted", null, locale));
			}catch(Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(t.getMessage());
			}finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
	}
	
	@RequestMapping(value = "/activateCrossReferenceById/{id}", method = RequestMethod.GET)
	public DataResponse activateCrossReferenceById(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, @PathVariable("id") Integer conditionId,
			HttpServletRequest request, Locale locale) {
		
			DataResponse dataResponse = new DataResponse();
			List<Message> messages = new ArrayList<>();
			Message message = new Message();
			JdbcTemplate clientAppDbJdbcTemplate = null;
			try {
				String  clientId = CommonUtils.getUserClientIDFromHeader(request);
				User user = new User();
				user.setClientId(clientId);
				user.setUserName(userId);
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
				clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
				crossReferenceService.activateCrossReferenceById(clientId, conditionId, clientAppDbJdbcTemplate);
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText("Activated successfully");
			}catch(Throwable t) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(t.getMessage());
			}finally {
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			}
			messages.add(message);
			dataResponse.setMessages(messages);
			return dataResponse;
	}
	
	
	@RequestMapping(value = "/jobXrefJobResultsById/{conditionId}/{ilId}", method = RequestMethod.GET)
	public DataResponse getJobXrefJobResultsById(@PathVariable("conditionId") Integer conditionId, @PathVariable("ilId") Integer ilId,
			@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = packageService.getJobResultsForCrossReferenceByConditionId(conditionId, ilId, clientIdfromHeader, null,clientJdbcTemplate);
		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(
					messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResults", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}
	
	@RequestMapping(value = "/jobXrefJobResultsById/{conditionId}/{ilId}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public DataResponse getJobResultsByDate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("ilId") Integer ilId,
			@PathVariable("conditionId") Integer conditionId,
			@PathVariable("fromDate") String fromDate, @PathVariable("toDate") String toDate,
			HttpServletRequest request, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		List<JobResult> resultlist = null;
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {

			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate(true);
			resultlist = packageService.getJobResultsForCrossReferenceByDateById(conditionId, ilId,clientIdfromHeader, null,fromDate, toDate, clientJdbcTemplate);

		} catch (AnvizentRuntimeException ae) {
			packageService.logError(ae, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (Exception e) {
			packageService.logError(e, request, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.FAILED);
			message.setText(messageSource.getMessage("anvizent.message.error.text.errorWhileGettingJobResultsByDate",
					null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		} finally {
			CommonUtils.closeDataSource(clientJdbcTemplate);
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		dataResponse.setObject(resultlist);
		message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;

	}
	@RequestMapping(value="/autoSplitConditionById/{id}/{executionType}", method = RequestMethod.GET)
	public DataResponse autoSplitConditionById(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("id") Integer conditionId,
			@PathVariable("executionType") String executionType,
			HttpServletRequest request, Locale locale) {
		
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null;
		try {
			String  clientId = CommonUtils.getUserClientIDFromHeader(request);
			User user = new User();
			user.setClientId(clientId);
			user.setUserName(userId);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				CrossReferenceLogs crossReferenceLogs = crossReferenceService.getCrossReferenceLogsInfoById(conditionId, clientAppDbJdbcTemplate);
			    crossReferenceLogs.setXrefExecutionType(Constants.CrossReferenceExecutionType.DELETE);
				ILInfo ilInfo = packageService.getILById(crossReferenceLogs.getIlId(), clientId, clientAppDbJdbcTemplate);
				List<Column> columns = packageService.getTableStructure(null, ilInfo.getiL_table_name(), 0, clientId, clientJdbcTemplate);
				ilInfo.setColumns(columns);
				crossReferenceService.processDeleteCrossReference(crossReferenceLogs, ilInfo, user, clientJdbcInstance.getClientDbCredentials(), clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.package.label.successfullyunmerged", null, locale));
		}catch(Throwable t) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(t.getMessage());
		}finally {
			CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
}
	
}
