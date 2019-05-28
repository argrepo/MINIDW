package com.datamodel.anvizent.data.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Hierarchical;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("" + Constants.AnvizentURL.MINIDW_BASE_URL + "/hierarchical")
public class HierarchicalDataController {
	protected static final Log LOGGER = LogFactory.getLog(HierarchicalDataController.class);
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;
	
	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	

	@Autowired
	@Qualifier("crossReferenceRestTemplateUtilities")
	private RestTemplateUtilities crossRefRestUtilities;
	
	@Autowired
	@Qualifier("hierarchicalRestTemplateUtilities")
	private RestTemplateUtilities hierarchicalRefRestUtilities;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public DataResponse getAllHierarchicals(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		return hierarchicalRefRestUtilities.getRestObject(request, "", clientId);
	}
	

	@RequestMapping(method = RequestMethod.POST)
	public DataResponse saveHierarchical(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Hierarchical hierarchical, HttpServletRequest request, Locale locale) {
		return hierarchicalRefRestUtilities.postRestObject(request, "", hierarchical, clientId);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public DataResponse getHierarchical(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long configId, HttpServletRequest request, Locale locale) {
		return hierarchicalRefRestUtilities.getRestObject(request, "/{id}", clientId, configId);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public DataResponse deleteHierarchical(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long configId, HttpServletRequest request, Locale locale) {
		return hierarchicalRefRestUtilities.deleteRestObject(request, "/{id}", clientId, configId);
	}
	
	@RequestMapping(value = "/ilList", method = RequestMethod.GET)
	public DataResponse getIlList(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			HttpServletRequest request) {
		User user = CommonUtils.getUserDetails(request, null, null);
		return restUtilities.getRestObject(request, "/getExistingILsInfo", user.getUserId());
	}
	
	@RequestMapping(value = "/columnInfo", method = RequestMethod.GET)
	public DataResponse getColumnInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestParam("tablename") String tableName, HttpServletRequest request) {

		DataResponse columnsResponse = getTableStructureByTableName(request, userId, tableName);
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
				if (!(col.getIsAutoIncrement() || exceptionColumnsList.indexOf(col.getColumnName()) != -1)) {
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
	
	public DataResponse getTableStructureByTableName(HttpServletRequest request, String userId, String tableName) {
		return packageRestUtilities.getRestObject(request,
				"/getTablesStructure/{industryId}/{dLOrILName}", userId, 0, tableName);
	}
	

	@RequestMapping(value = "/getDistinctValues", method = RequestMethod.POST)
	public DataResponse getDistinctValues(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "ilId") String ilId, @RequestParam(value = "columnName") String columnName,
			@RequestParam(value = "tableName",required=false) String tableName,
			@RequestParam(value = "columnValue") String columnValue,
			Locale locale,
			HttpServletRequest request) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		if (StringUtils.isNotBlank(columnName)) {
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("iLId", ilId);
			map.add("tableName", tableName);
			map.add("columnName", columnName);
			map.add("columnValue", columnValue);
			map.add("isXrefValueNull", true);

			dataResponse = crossRefRestUtilities.postRestObject(request, "/getDistinctValues", map, user.getUserId());
		}
		return dataResponse;
	}


	@RequestMapping(value = "/getDistinctValuesByColumnName", method = RequestMethod.POST)
	public DataResponse getDistinctValues(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "ilId") String ilId, @RequestParam(value = "columnName") String columnName,
			@RequestParam(value = "tableName",required=false) String tableName,	Locale locale,
			HttpServletRequest request) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		if (StringUtils.isNotBlank(columnName)) {
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("iLId", ilId);
			map.add("tableName", tableName);
			map.add("columnName", columnName);

			dataResponse = hierarchicalRefRestUtilities.postRestObject(request, "/getDistinctValuesByColumnName", map, user.getUserId());
		}
		return dataResponse;
	}
	
	@RequestMapping(value= "/getDistinctValuesinRange", method = RequestMethod.POST)
	public DataResponse getDistinctValuesByRange(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "ilId") String ilId, @RequestParam(value = "columnName") String columnName,
			@RequestParam(value = "tableName",required=false) String tableName,
			@RequestParam(value = "fromRange") String fromRange,
			@RequestParam(value = "toRange") String toRange,
			Locale locale,
			HttpServletRequest request) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		if (StringUtils.isNotBlank(columnName)) {
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("iLId", ilId);
			map.add("tableName", tableName);
			map.add("columnName", columnName);
			map.add("fromRange", fromRange);
			map.add("toRange", toRange);

			dataResponse = hierarchicalRefRestUtilities.postRestObject(request, "/getDistinctValuesinRange", map, user.getUserId());
		}
		return dataResponse;
	}
	
	@RequestMapping(value="/getMeasures", method = RequestMethod.GET)
	public DataResponse getMeasures(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestParam("tablename") String tableName, HttpServletRequest request) {
		
		DataResponse columnsResponse = getTableStructureByTableName(request, userId, tableName);
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
				//if(Arrays.asList(Constants.DataTypeConstants.MEASURE_TYPES).contains(col.getDataType())) {
					if (!(col.getIsAutoIncrement() || exceptionColumnsList.indexOf(col.getColumnName()) != -1)) {
						columnsNames.add(col.getColumnName());
						columnSize.put(col.getColumnName(), col.getColumnSize());
						columnDataTypes.put(col.getColumnName(), col.getDataType());
					}	
				//}
			});
		}

		Map<String, Object> columnInfo = new HashMap<>();
		columnInfo.put("columnsNames", columnsNames);
		columnInfo.put("columnSize", columnSize);
		columnInfo.put("columnDataTypes", columnDataTypes);
		columnsResponse.setObject(columnInfo);
		return columnsResponse;
		
	}
     
	@RequestMapping(value="/getDimensions", method = RequestMethod.GET)
	public DataResponse getDimensions(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestParam("tablename") String tableName, HttpServletRequest request) {
		
		DataResponse columnsResponse = getTableStructureByTableName(request, userId, tableName);
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
				if(Arrays.asList(Constants.DataTypeConstants.DIMENSION_TYPES).contains(col.getDataType())) {
					if (!(col.getIsAutoIncrement() || exceptionColumnsList.indexOf(col.getColumnName()) != -1)) {
						columnsNames.add(col.getColumnName());
						columnSize.put(col.getColumnName(), col.getColumnSize());
						columnDataTypes.put(col.getColumnName(), col.getDataType());
					}	
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
	
	@RequestMapping(value="/saveAssociation", method = RequestMethod.POST)
	public DataResponse saveHierarchicalAssociation(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Hierarchical hierarchical, HttpServletRequest request, Locale locale) {
		return hierarchicalRefRestUtilities.postRestObject(request, "/saveAssociation", hierarchical, clientId);
	}
	
	@RequestMapping(value="/getHierarchicalAssociationByHierarchyId/{configId}" , method = RequestMethod.GET)
	public DataResponse getHierarchicalAssociationByHierarchyId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("configId") long configId, HttpServletRequest request, Locale locale) {
		return hierarchicalRefRestUtilities.getRestObject(request, "/getHierarchicalAssociationByHierarchyId/{configId}", clientId, configId);	
	}
	
	@RequestMapping(value="/run/{hierarchicalId}", method = RequestMethod.GET)
	public DataResponse runHierarchicalStructure(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("hierarchicalId") long hierarchicalId, HttpServletRequest request, Locale locale) {
		
		return hierarchicalRefRestUtilities.getRestObject(request, "/run/{hierarchicalId}", clientId, hierarchicalId);
	}
	
	@RequestMapping(value="/viewHierarchicalLogs/{hierarchicalId}", method = RequestMethod.GET)
	public DataResponse viewHierarchicalLogs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("hierarchicalId") long hierarchicalId, HttpServletRequest request, Locale locale) {
		return hierarchicalRefRestUtilities.getRestObject(request, "/viewHierarchicalLogs/{hierarchicalId}", clientId, hierarchicalId);
	}
	
	@RequestMapping(value= "/getIlColumnPatternValues", method = RequestMethod.POST)
	public DataResponse getIlColumnPatternValues(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "ilId") String ilId, @RequestParam(value = "columnName") String columnName,
			@RequestParam(value = "tableName",required=false) String tableName,
			@RequestParam(value = "patternValue") String patternValue,
			@RequestParam(value = "patternRangeValue") String patternRangeValue,
			Locale locale,
			HttpServletRequest request) {
           
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = new DataResponse();
		if (StringUtils.isNotBlank(columnName)) {
			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("iLId", ilId);
			map.add("tableName", tableName);
			map.add("columnName", columnName);
			map.add("patternValue", patternValue);
			map.add("patternRangeValue", patternRangeValue);

			dataResponse = hierarchicalRefRestUtilities.postRestObject(request, "/getIlColumnPatternValues", map, user.getUserId());
		}
		return dataResponse;
	  }
	
	
	
}
