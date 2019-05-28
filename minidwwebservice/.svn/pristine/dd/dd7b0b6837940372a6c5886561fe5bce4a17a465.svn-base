package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
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
import org.springframework.web.multipart.MultipartFile;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.common.exception.AnvizentDuplicateFileNameException;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.ELTCommandConstants;
import com.datamodel.anvizent.service.ELTConfigTagsService;
import com.datamodel.anvizent.service.ELTJobTagsService;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.Message;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController("ELTConfigTagsDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ADMIN_SERVICES_BASE_URL + "/eltconfig")
@CrossOrigin
public class ELTConfigTagsDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(ELTConfigTagsDataRestController.class);

	@Autowired
	MessageSource messageSource;
	@Autowired
	ELTConfigTagsService eltConfigTagsService;
	@Autowired
	FileService fileService;
	@Autowired
	private UserDetailsService userService;
	@Autowired
	@Qualifier("getCommonJdbcTemplate")
	private JdbcTemplate commonJdbcTemplate;

	@RequestMapping(value = "/saveEltStgKeyConfig", method = RequestMethod.POST)
	public DataResponse saveEltStgKeyConfig(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody List<ELTConfigTags> eltStgKeyConfig, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		int create = 0;
		try {
			create = eltConfigTagsService.saveEltStgKeyConfig(eltStgKeyConfig, commonJdbcTemplate);
			if (create != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(
						messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getELTStgConfigInfo", method = RequestMethod.GET)
	public DataResponse getELTStgConfigInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<ELTConfigTags> eltStgKeyConfigList = null;
		try {
			eltStgKeyConfigList = eltConfigTagsService.getELTStgConfigInfo(commonJdbcTemplate);
			if (eltStgKeyConfigList != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(eltStgKeyConfigList);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveConnectorList",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/deleteEltStgKeyConfigById", method = RequestMethod.POST)
	public DataResponse deleteEltStgKeyConfigById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int update = 0;
		try {
			update = eltConfigTagsService.deleteEltStgKeyConfigById(id, commonJdbcTemplate);
			if (update != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText("Deleted Succesfully");
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveConnectorList",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	
	@RequestMapping(value = "/deleteALLEltStgKeyConfigById", method = RequestMethod.POST)
	public DataResponse deleteALLEltStgKeyConfigById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("tagId") Integer tagId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		int update = 0;
		try {
			update = eltConfigTagsService.deleteALLEltStgKeyConfigById(tagId, commonJdbcTemplate);
			if (update != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText("Deleted Succesfully");
				messages.add(message);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveConnectorList",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}


	@RequestMapping(value = "/getEltConfigTags", method = RequestMethod.GET)
	public DataResponse getEltConfigTags(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<ELTConfigTags> eltStgKeyConfigList = null;
		try {
			eltStgKeyConfigList = eltConfigTagsService.getEltConfigTags(commonJdbcTemplate);
			if (eltStgKeyConfigList != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setObject(eltStgKeyConfigList);
				dataResponse.setMessages(messages);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveConnectorList",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveEltConfigPairInfo", method = RequestMethod.POST)
	public DataResponse saveEltConfigPairInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ELTConfigTags eltilConfigs, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		long tagId = 0;
		try {
			tagId = eltConfigTagsService.saveEltConfigPairInfo(eltilConfigs, commonJdbcTemplate);
			if (tagId != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(tagId);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}

		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveEltCloneTagInfo", method = RequestMethod.POST)
	public DataResponse saveEltCloneTagInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ELTConfigTags eltilConfigs, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		long tagId = 0;
		try {
			tagId = eltConfigTagsService.saveEltCloneTagInfo(eltilConfigs, commonJdbcTemplate);
			if (tagId != 0) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				dataResponse.setObject(tagId);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(
						messageSource.getMessage("anvizent.message.error.text.unableToSaveDetails", null, locale));
			}

		} catch (AnvizentDuplicateFileNameException ae) {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText("Tag name already existed");
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		messages.add(message);
		dataResponse.setMessages(messages);
		return dataResponse;
	}

	@RequestMapping(value = "/uploadBulkConfigInfo", method = RequestMethod.POST)
	public DataResponse uploadBulkConfigInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(required = false, value = "file") MultipartFile multipartFile, HttpServletRequest request,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		Properties endPointProperties = new Properties();
		HashMap<String, String> propValues = new HashMap<String, String>();
		try {
			File file = CommonUtils.multipartToFile(multipartFile);
			endPointProperties.load(new FileInputStream(file));
			System.out.println(endPointProperties);

			Set<String> propertyNames = endPointProperties.stringPropertyNames();
			for (String property : propertyNames) {
				propValues.put(property, endPointProperties.getProperty(property));
			}
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.addMessage(message);
			dataResponse.setObject(propValues);

		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/saveELTConfigTags", method = RequestMethod.POST)
	public DataResponse jsonObject(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody String object,  HttpServletRequest request,
			Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		long maintagId = 0;
		long apptagId = 0;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		try {
			
			String clientIdfromHeader = CommonUtils.getClientIDFromHeader(request);
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientIdfromHeader);
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();
			
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, Object> map = mapper.readValue(object, new TypeReference<LinkedHashMap<String, Object>>() {});
						
			Map<String, Object> configsMap = (LinkedHashMap<String, Object>)map.get("configs");
			String tagName = (String) configsMap.get(ELTCommandConstants.NAME);
			String accessType = (String) configsMap.get(ELTCommandConstants.ACCESS);
			List<Map<String, Object>> valuesList = (List<Map<String, Object>>) configsMap.get("values");
			ELTConfigTags eltilConfigs = new ELTConfigTags();
			eltilConfigs.setActive(true);
			eltilConfigs.setTagName(tagName);
			if(accessType != null) {
				maintagId = eltConfigTagsService.saveEltConfigPairInfo(eltilConfigs, commonJdbcTemplate);
				apptagId = eltConfigTagsService.saveEltConfigPairInfo(eltilConfigs, clientAppDbJdbcTemplate);
				if(maintagId !=0 && apptagId != 0) {
						eltConfigTagsService.saveELTKeyValuePairs(maintagId,valuesList,commonJdbcTemplate);
						eltConfigTagsService.saveELTKeyValuePairs(apptagId,valuesList,clientAppDbJdbcTemplate);
						message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
						message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
				}
			}else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Invalid Access type/Client Id");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		finally {
			if(clientAppDbJdbcTemplate != null)
				CommonUtils.closeDataSource(clientAppDbJdbcTemplate);
			if(commonJdbcTemplate != null)
				CommonUtils.closeDataSource(commonJdbcTemplate);
		}
		dataResponse.addMessage(message);
		return dataResponse;
	}

	@RequestMapping(value = "/getEltConfigByTagId", method = RequestMethod.POST)
	public DataResponse getEltConfigByTagId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("tagId") Integer tagId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		List<ELTConfigTags> eltKeyPairs = null;
		try {
			eltKeyPairs = eltConfigTagsService.getEltConfigByTagId(tagId, commonJdbcTemplate);
			if (eltKeyPairs != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				messages.add(message);
				dataResponse.setMessages(messages);
				dataResponse.setObject(eltKeyPairs);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText(messageSource.getMessage("anvizent.message.error.text.unableToRetrieveConnectorList",
						null, locale));
				messages.add(message);
				dataResponse.setMessages(messages);
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	

	
	@RequestMapping(value = "/getEltConfigTagsByID/{id}", method = RequestMethod.GET)
	public DataResponse getEltConfigTagsByID(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long id, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		ELTConfigTags eltKeyConfig = null;
		try {
			eltKeyConfig = eltConfigTagsService.getEltConfigTagsByID(id, commonJdbcTemplate);
			if (eltKeyConfig != null) {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
				dataResponse.setObject(eltKeyConfig);
			} else {
				message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
				message.setText("Job tag info not found");
			}
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
}
