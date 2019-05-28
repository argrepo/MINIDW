package com.datamodel.anvizent.data.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.anvizent.minidw.service.utils.processor.CommonProcessor;
import com.anvizent.minidw.service.utils.processor.StandardPackageProcessor;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.HierarchicalService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.CustomRequest;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Hierarchical;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.User;

@RestController
@RequestMapping(Constants.AnvizentURL.ANVIZENT_SERVICES_BASE_URL + "/hierarchical")
@CrossOrigin
public class HierarchicalRestController {
	
	protected static final Log LOGGER = LogFactory.getLog(HierarchicalRestController.class);
	
	@Autowired
	MessageSource messageSource;
	@Autowired
	HierarchicalService hierarchicalService;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	private PackageService packageService;
	@Autowired 
	CommonProcessor commonProcessor;
	@Autowired
	StandardPackageProcessor standardPacakgeProcessor;
	
	@RequestMapping(method = RequestMethod.GET)
	public DataResponse getAllHierarchicals(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<Hierarchical> hierarchicalList = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			hierarchicalList = hierarchicalService.getAllHierarchicalList(clientAppDbJdbcTemplate);
			
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(hierarchicalList);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(method = RequestMethod.POST)
	public DataResponse saveHierarchical(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Hierarchical hierarchical, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null;
		try {
			String clientIdFromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			hierarchical.setModification(modification);
			long configId = hierarchicalService.saveHierarchical(hierarchical, clientAppDbJdbcTemplate);
			hierarchical.setId(configId);
			User user = new User();
			user.setClientId(clientIdFromHeader);
			user.setUserName(clientId);
			CustomRequest customRequest = standardPacakgeProcessor.getCustomRequest(request, user);
			hierarchicalService.processHierarchicalJob(hierarchical, user,clientJdbcInstance.getClientDbCredentials(), clientJdbcTemplate, clientAppDbJdbcTemplate, customRequest);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			dataResponse.setObject(hierarchical);
		} catch (Throwable t) {
			LOGGER.error("While saving " , t);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public DataResponse getHierarchical(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long configId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		Hierarchical hierarchical = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			hierarchical = hierarchicalService.getHierarchicalById(configId, clientAppDbJdbcTemplate);
			
			JSONObject jsonObj = new JSONObject(hierarchical.getHierarchicalFormData());
			JSONArray hierarchyConfig = jsonObj.getJSONArray("config");
			
			hierarchical.setHierarchicalFormData(hierarchyConfig.toString());
			hierarchical.setFinancialTemplate(jsonObj.getBoolean("financialTemplateStatus"));
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(hierarchical);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public DataResponse deleteHierarchical(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long configId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			Hierarchical hierarchicalById = hierarchicalService.deleteHierarchicalById(configId, clientAppDbJdbcTemplate);
			dataResponse.setObject(hierarchicalById);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("Deleted successfully");
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value="/saveAssociation", method = RequestMethod.POST)
	public DataResponse saveHierarchicalAssociation(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@RequestBody Hierarchical hierarchical, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null; 
		try {
			String clientIdFromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(userId);
			hierarchical.setModification(modification);
			User user = new User();
			user.setClientId(clientIdFromHeader);
			user.setUserName(userId);
			user.setUserId(userId);
			hierarchicalService.saveHierarchicalAssociation(hierarchical, clientAppDbJdbcTemplate);
			Hierarchical hierarchicaldata = hierarchicalService.getHierarchicalById(hierarchical.getId(), clientAppDbJdbcTemplate);
			hierarchical.setHierarchicalLevelData(hierarchicaldata.getHierarchicalLevelData());
			CustomRequest customRequest = standardPacakgeProcessor.getCustomRequest(request, user);
			hierarchicalService.processHierarchicalMappingJob(hierarchical, user, clientJdbcInstance.getClientDbCredentials(), clientJdbcTemplate, clientAppDbJdbcTemplate, customRequest);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			dataResponse.setObject(hierarchical);
		} catch (Throwable t) {
			LOGGER.error("While saving " , t);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
      
	@RequestMapping(value="/getHierarchicalAssociationByHierarchyId/{configId}" , method = RequestMethod.GET)
	public DataResponse getHierarchicalAssociationByHierarchyId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("configId") long configId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		Hierarchical hierarchical = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			hierarchical = hierarchicalService.getHierarchicalAssociationByConfigId(configId, clientAppDbJdbcTemplate);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(hierarchical);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value="/run/{hierarchicalId}", method = RequestMethod.GET)
	public DataResponse runHierarchicalStructure(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("hierarchicalId") long configId, HttpServletRequest request, Locale locale) {
		DataResponse  dataResponse = new DataResponse();
		Message  message = new Message();
		Hierarchical hierarchical = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		JdbcTemplate clientJdbcTemplate = null;
		try {
             String clientIdFromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(request);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			
			hierarchical =  hierarchicalService.getHierarchicalAndMappingInfoById(configId, clientAppDbJdbcTemplate);
			User user = new User();
			user.setClientId(clientIdFromHeader);
			user.setUserName(userId);
			user.setUserId(EncryptionServiceImpl.getInstance()
							.encrypt(userId + "#" + locale + "#" + new Date()));
			CustomRequest customRequest = standardPacakgeProcessor.getCustomRequest(request, user);
			hierarchicalService.processHierarchicalMappingJob(hierarchical, user,clientJdbcInstance.getClientDbCredentials(), clientJdbcTemplate, clientAppDbJdbcTemplate, customRequest);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.addMessage(message);
		}catch(Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value="/getDistinctValuesByColumnName", method = RequestMethod.POST)
	public DataResponse getDistinctColumnValueByColumnName(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("columnName") String columnName,
			@RequestParam("iLId") String ilId, Locale locale) {

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

			List<Object> distinctValue = hierarchicalService.getDistinctValues(columnName, ilInfo.getiL_table_name(), clientJdbcTemplate);
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
	
	
	@RequestMapping(value = "/hierarchicalJobResults/{hierarchicalId}/{hierarchicalName}", method = RequestMethod.GET)
	public DataResponse hierarchicalJobResults(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("hierarchicalId") Integer hierarchicalId, @PathVariable("hierarchicalName") String hierarchicalName,
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
			resultlist = hierarchicalService.getJobResultsForHierarchical(hierarchicalId, hierarchicalName, clientIdfromHeader, null,clientJdbcTemplate);
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
	
	@RequestMapping(value = "/hierarchicalJobResultsByDate/{hierarchicalId}/{hierarchicalName}/{fromDate}/{toDate}", method = RequestMethod.GET)
	public DataResponse getHierarchicalJobResultsByDate(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("hierarchicalId") Integer hierarchicalId, @PathVariable("hierarchicalName") String hierarchicalName,
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
			resultlist = hierarchicalService.getJobResultsForHierarchicalByDate(hierarchicalId, hierarchicalName, clientIdfromHeader, null,fromDate, toDate, clientJdbcTemplate);

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

	@RequestMapping(value="/getDistinctValuesinRange", method = RequestMethod.POST)
	public DataResponse getDistinctValuesinRange(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("columnName") String columnName,
			@RequestParam("iLId") String ilId,
			@RequestParam(value = "fromRange") String fromRange,
			@RequestParam(value = "toRange") String toRange, Locale locale) {

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

			List<Object> distinctValue = hierarchicalService.getDistinctValuesByRange(columnName, ilInfo.getiL_table_name(), fromRange, toRange, clientJdbcTemplate);
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
	
	@RequestMapping(value="/getIlColumnPatternValues", method = RequestMethod.POST)
	public DataResponse getIlColumnPatternValues(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestParam("columnName") String columnName,
			@RequestParam("iLId") String ilId,
			@RequestParam(value = "patternValue") String patternValue,
			@RequestParam(value = "patternRangeValue") String patternRangeValue, Locale locale) {

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
			
			List<Object> distinctValue = hierarchicalService.getDistinctValuesByPattern(columnName, ilInfo.getiL_table_name(), patternValue, patternRangeValue,  clientJdbcTemplate);
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

}
