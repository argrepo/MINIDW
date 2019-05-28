
package com.datamodel.anvizent.controller.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.AllMappingInfo;
import com.datamodel.anvizent.service.model.AllMappingInfoForm;
import com.datamodel.anvizent.service.model.ClientConfigurationSettings;
import com.datamodel.anvizent.service.model.ClientConnectorMappingForm;
import com.datamodel.anvizent.service.model.ClientWebserviceMappingForm;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.CurrencyList;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.DefaultTemplates;
import com.datamodel.anvizent.service.model.DefaultTemplatesForm;
import com.datamodel.anvizent.service.model.ErrorLog;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.GeneralSettings;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.TemplateMigration;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.VersionUpgrade;
import com.datamodel.anvizent.service.model.WebService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Controller
@RequestMapping(value = "/admin")
public class ETLAdminController {
	protected static final Log LOGGER = LogFactory.getLog(ETLAdminController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/ETLAdmin", method = RequestMethod.GET)
	public ModelAndView etlAdmin(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) {
		LOGGER.debug("in ETLAdmin()");
		CommonUtils.setActiveScreenName("ETLAdmin", session);
		mv.setViewName("tiles-anvizent-admin:ETLAdmin");
		return mv;
	}
	@RequestMapping(value = "/ETLAdminClient", method = RequestMethod.GET)
	public ModelAndView etlAdminClient(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) {
		LOGGER.debug("in ETLAdminClient()");
		CommonUtils.setActiveScreenName("ETLAdminClient", session);
		mv.setViewName("tiles-anvizent-admin:ETLAdminClient");
		return mv;
	}


	@RequestMapping(value = "/clientchange", method = RequestMethod.POST)
	public ModelAndView clientchangeRequest(HttpServletRequest request,
			@ModelAttribute("clientConnectorMappingForm") ClientConnectorMappingForm clientConnectorMappingForm,
			HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale,
			RedirectAttributes redirectAttributes) {
		LOGGER.debug("in clientchange()");
		SessionHelper.setSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID,
				clientConnectorMappingForm.getClientId() + "");
		redirectAttributes.addFlashAttribute("messagecode", "SUCCESS");
		redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.text.successfullyChanged", null, locale));
		mv.setViewName("redirect:/admin/clientchange");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/createIL", method = RequestMethod.GET)
	public ModelAndView createIL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			Locale locale) {
		LOGGER.debug("in createIL()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			Object object = restUtilities.getRestObject(request, "/getContextParameters", Object.class,
					user.getUserId());
			Map<String, Object> dataResponse = (Map<String, Object>) object;
			List<Map<String, Object>> contextParams = (List<Map<String, Object>>) dataResponse.get("object");
			mv.addObject("contextParams", contextParams);

			Object paramValueObject = restUtilities.getRestObject(request, "/getContextParamValue", Object.class,
					user.getUserId());
			Map<String, Object> paramValueDataResponse = (Map<String, Object>) paramValueObject;
			List<Map<String, Object>> paramValue = (List<Map<String, Object>>) paramValueDataResponse.get("object");
			mv.addObject("contextParamValues", paramValue);

			Map<String, Object> modelObject = new HashMap<String, Object>();
			modelObject.put("contextParams", contextParams);
			modelObject.put("paramValue", paramValue);

			mv.addObject("modelObject", modelObject);

			mv.setViewName("tiles-anvizent-admin:createIL");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/createDL", method = RequestMethod.GET)
	public ModelAndView createDL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			Locale locale) {
		LOGGER.debug("in createDL()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			Object object = restUtilities.getRestObject(request, "/getContextParameters", Object.class,
					user.getUserId());
			Map<String, Object> dataResponse = (Map<String, Object>) object;
			List<Map<String, Object>> contextParams = (List<Map<String, Object>>) dataResponse.get("object");
			mv.addObject("contextParams", contextParams);

			Object paramValueObject = restUtilities.getRestObject(request, "/getContextParamValue", Object.class,
					user.getUserId());
			Map<String, Object> paramValueDataResponse = (Map<String, Object>) paramValueObject;
			List<Map<String, Object>> paramValue = (List<Map<String, Object>>) paramValueDataResponse.get("object");
			mv.addObject("contextParamValues", paramValue);

			Map<String, Object> modelObject = new HashMap<String, Object>();
			modelObject.put("contextParams", contextParams);
			modelObject.put("paramValue", paramValue);

			mv.addObject("modelObject", modelObject);
			mv.setViewName("tiles-anvizent-admin:createDL");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/iLDLMapping", method = RequestMethod.GET)
	public ModelAndView iLDLMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			Locale locale) {
		LOGGER.debug("in iLDLMapping()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			Map object = restUtilities.getRestObject(request, "/getDlInfo", Map.class, user.getUserId());
			List<LinkedHashMap<String, Object>> dLs = (List<LinkedHashMap<String, Object>>) object.get("object");
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<DLInfo> dLList = mapper.convertValue(dLs, new TypeReference<List<DLInfo>>() {
			});

			Map ilObject = restUtilities.getRestObject(request, "/getAllIlInfo", Map.class, user.getUserId());
			List<LinkedHashMap<String, Object>> ils = (List<LinkedHashMap<String, Object>>) ilObject.get("object");
			List<ILInfo> iLList = mapper.convertValue(ils, new TypeReference<List<ILInfo>>() {
			});

			Map<String, Object> modelObject = new HashMap<String, Object>();
			modelObject.put("dLList", dLList);
			modelObject.put("iLList", iLList);
			mv.addObject("modelObject", modelObject);

			mv.setViewName("tiles-anvizent-admin:iLDLMapping");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}

	@RequestMapping(value = "/viewILDL", method = RequestMethod.GET)
	public ModelAndView viewILDL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		LOGGER.debug("in viewILDL()");
		mv.setViewName("tiles-anvizent-admin:viewILDL");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fileSettings", method = RequestMethod.GET)
	public ModelAndView fileSettings(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) {
		LOGGER.debug("in fileSettings()");
		CommonUtils.setActiveScreenName("fileSettings", session);
		User user = CommonUtils.getUserDetails(request, null, null);

		try {
			DataResponse fileSettingsDataResponse = packageRestUtilities.getRestObject(request, "/getFileSettingsInfo",
					user.getUserId());
			if (fileSettingsDataResponse != null && fileSettingsDataResponse.getHasMessages()) {
				if (fileSettingsDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) fileSettingsDataResponse
							.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					FileSettings fileSettings = mapper.convertValue(map, new TypeReference<FileSettings>() {
					});
					mv.addObject("fileSettingsInfo", fileSettings);
				}
			}
			mv.setViewName("tiles-anvizent-admin:fileSettings");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public ModelAndView getTemplate(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			Locale locale) {
		LOGGER.info("in getTemplate()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getTemplate", user.getUserId());
			List<Map<String, String>> template = (List<Map<String, String>>) dataResponse.getObject();
			mv.addObject("template", template);
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("tiles-anvizent-admin:templates");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/errorLogs", method = RequestMethod.GET)
	public ModelAndView getTopErrorLog(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) {
		LOGGER.info("in errorLogs()");
		CommonUtils.setActiveScreenName("errorLogs", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				DataResponse dataResponse = restUtilities.getRestObject(request, "/getTopErrorLog", user.getUserId());
				List<ErrorLog> errorLogs = (List<ErrorLog>) dataResponse.getObject();

				mv.addObject("errorLogs", errorLogs);
				mv.setViewName("tiles-anvizent-admin:clientErrorLogs");
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
		}

		return mv;
	}

	@RequestMapping(value = "/addWebService", method = RequestMethod.GET)
	public ModelAndView addWebService(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			Locale locale) {
		LOGGER.debug("in addWebService()");
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllWebServices",
						user.getUserId());
				mv.addObject("webservicelist", dataResponse.getObject());
				mv.setViewName("tiles-anvizent-admin:addWebService");
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
		}
		return mv;
	}

	@SuppressWarnings({ "unchecked" })
	@ModelAttribute("allClients")
	public Map<Object, Object> getAllClientInfo(HttpServletRequest request) {

		// TODO
		long clientId = Long
				.valueOf((String) SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID));
		User user = CommonUtils.getUserDetails(request, null, null);

		String activeClientsUrl = "/activeClients";
		DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, activeClientsUrl,
				new LinkedMultiValueMap<>(), DataResponse.class, user.getUserId());
		Map<Object, Object> clientMap = new LinkedHashMap<>();
		if (dataResponse != null) {
			List<Map<String, Object>> clientList = (List<Map<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<CloudClient> cloudClients = mapper.convertValue(clientList, new TypeReference<List<CloudClient>>() {
			});

			if (cloudClients == null || cloudClients.size() == 0) {
				return new HashMap<>();
			}

			for (CloudClient cloudClient : cloudClients) {
				// TODO Add this condition
				if (clientId == cloudClient.getId()) {
					clientMap.put(cloudClient.getId(), cloudClient.getId() + " - " + cloudClient.getClientName());
				}
			}
		}
		return clientMap;
	}

	@SuppressWarnings({ "unchecked" })
	@ModelAttribute("allClientsForChangeRequest")
	public Map<Object, Object> getAllClientInfoForChangeRequest(HttpServletRequest request) {

		String activeClientsUrl = "/activeClients";
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, activeClientsUrl,
				new LinkedMultiValueMap<>(), DataResponse.class, user.getUserId());

		Map<Object, Object> clientMap = new LinkedHashMap<>();
		if (dataResponse != null) {
			List<Map<String, Object>> clientList = (List<Map<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<CloudClient> cloudClients = mapper.convertValue(clientList, new TypeReference<List<CloudClient>>() {
			});

			if (cloudClients == null || cloudClients.size() == 0) {
				return new HashMap<>();
			}

			for (CloudClient cloudClient : cloudClients) {
				clientMap.put(cloudClient.getId(), cloudClient.getId() + " - " + cloudClient.getClientName());
			}
		}
		return clientMap;
	}

	@RequestMapping(value = "/allMappingInfo", method = RequestMethod.GET)
	public ModelAndView allMappingInfo(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("allMappingInfoForm") AllMappingInfoForm allMappingInfoForm,
			Locale locale) {
		LOGGER.debug("in allMappingInfo()");
		CommonUtils.setActiveScreenName("allMappingInfo", session);
		mv.setViewName("tiles-anvizent-admin:allMappingInfo");
		return mv;
	}

	@RequestMapping(value = "/defaultTemplates", method = RequestMethod.GET)
	public ModelAndView getdefaultTemplates(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("defaultTemplatesForm") DefaultTemplatesForm defaultTemplatesForm,
			Locale locale) {
		LOGGER.debug("in defaultTemplates()");
		CommonUtils.setActiveScreenName("defaultTemplates", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllDefaultTemplatesInfo",
					user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("defaultTemplates", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		defaultTemplatesForm.setPageMode("list");
		mv.setViewName("tiles-anvizent-admin:defaultMappingConfiguration");
		return mv;
	}

	@RequestMapping(value = "/defaultTemplates/add", method = RequestMethod.GET)
	public ModelAndView addDefaultTemplate(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("defaultTemplatesForm") DefaultTemplatesForm defaultTemplatesForm, Locale locale) {
		LOGGER.debug("in addDefaultTemplate()");
		defaultTemplatesForm.setPageMode("add");
		mv.setViewName("tiles-anvizent-admin:defaultMappingConfiguration");
		return mv;
	}

	@RequestMapping(value = "/defaultTemplates/add", method = RequestMethod.POST)
	public ModelAndView createDefaultTemplate(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("defaultTemplatesForm") DefaultTemplatesForm defaultTemplatesForm,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in createDefaultTemplate()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			defaultTemplatesForm.setPageMode("add");
			mv.setViewName("tiles-anvizent-admin:defaultMappingConfiguration");

			DefaultTemplates defaultTemplates = new DefaultTemplates();
			defaultTemplates.setTemplateId(defaultTemplatesForm.getTemplateId());
			defaultTemplates.setTemplateName(defaultTemplatesForm.getTemplateName());
			defaultTemplates.setDescription(defaultTemplatesForm.getDescription());
			defaultTemplates.setActive(defaultTemplatesForm.isActive());
			defaultTemplates.setTrialTemplate(defaultTemplatesForm.isTrialTemplate());
			DataResponse dataResponse = null;
			if (defaultTemplatesForm.getTemplateId() != 0) {
				dataResponse = restUtilities.postRestObject(request, "/updateDefaultTemplate", defaultTemplates,
						user.getUserId());

			} else {
				dataResponse = restUtilities.postRestObject(request, "/createDefaultTemplate", defaultTemplates,
						user.getUserId());
			}
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.setViewName("redirect:/admin/defaultTemplates/edit/" + dataResponse.getObject());
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/defaultTemplates/edit/{templateId}", method = RequestMethod.GET)
	public ModelAndView editDefaultTemplate(@PathVariable("templateId") int templateId, HttpServletRequest request,
			HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("defaultTemplatesForm") DefaultTemplatesForm defaultTemplatesForm, Locale locale) {
		LOGGER.debug("in editDefaultTemplate()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse templateDetails = restUtilities.getRestObject(request,
					"/getDefaultTemplateInfoById/{templateId}", user.getUserId(), templateId);
			if (templateDetails != null && templateDetails.getHasMessages()) {
				if (templateDetails.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> maps = (LinkedHashMap<String, Object>) templateDetails.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					DefaultTemplatesForm defaultTemplates = mapper.convertValue(maps,
							new TypeReference<DefaultTemplatesForm>() {
							});
					defaultTemplatesForm.setTemplateId(defaultTemplates.getTemplateId());
					defaultTemplatesForm.setTemplateName(defaultTemplates.getTemplateName());
					defaultTemplatesForm.setDescription(defaultTemplates.getDescription());
					defaultTemplatesForm.setActive(defaultTemplates.isActive());
					defaultTemplatesForm.setTrialTemplate(defaultTemplates.isTrialTemplate());
					mv.addObject("template", templateDetails.getObject());
				}
			}
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("templateId", templateId);
			map.add("mappingType", "connector");
			DataResponse dataResponseConnector = restUtilities.postRestObject(request,
					"/getDefaultTemplateMasterMappedData", map, user.getUserId());
			if (dataResponseConnector != null && dataResponseConnector.getHasMessages()) {
				if (dataResponseConnector.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("connector", dataResponseConnector.getObject());
				}
			}

			MultiValueMap<String, Object> map1 = new LinkedMultiValueMap<>();
			map1.add("templateId", templateId);
			map1.add("mappingType", "vertical");
			DataResponse dataResponseVertical = restUtilities.postRestObject(request,
					"/getDefaultTemplateMasterMappedData", map1, user.getUserId());
			if (dataResponseVertical != null && dataResponseVertical.getHasMessages()) {
				if (dataResponseVertical.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("verticals", dataResponseVertical.getObject());
				}
			}

			MultiValueMap<String, Object> map2 = new LinkedMultiValueMap<>();
			map2.add("templateId", templateId);
			map2.add("mappingType", "dl");
			DataResponse dataResponseDls = restUtilities.postRestObject(request, "/getDefaultTemplateMasterMappedData",
					map2, user.getUserId());
			if (dataResponseDls != null && dataResponseDls.getHasMessages()) {
				if (dataResponseDls.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("dlInfo", dataResponseDls.getObject());
				}
			}

			MultiValueMap<String, Object> map3 = new LinkedMultiValueMap<>();
			map3.add("templateId", templateId);
			map3.add("mappingType", "tablescript");
			DataResponse dataResponseTableScripts = restUtilities.postRestObject(request,
					"/getDefaultTemplateMasterMappedData", map3, user.getUserId());
			if (dataResponseTableScripts != null && dataResponseTableScripts.getHasMessages()) {
				if (dataResponseTableScripts.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("tableScriptsList", dataResponseTableScripts.getObject());
				}
			}

			MultiValueMap<String, Object> map4 = new LinkedMultiValueMap<>();
			map4.add("templateId", templateId);
			map4.add("mappingType", "webservice");
			DataResponse dataResponseWebService = restUtilities.postRestObject(request,
					"/getDefaultTemplateMasterMappedData", map4, user.getUserId());
			if (dataResponseWebService != null && dataResponseWebService.getHasMessages()) {
				if (dataResponseWebService.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("webServices", dataResponseWebService.getObject());
				}
			}

			DataResponse templateCurrrencyDetails = restUtilities.getRestObject(request,
					"/getDefaultCurrencyInfoById/{templateId}", user.getUserId(), templateId);
			if (templateCurrrencyDetails != null && templateCurrrencyDetails.getHasMessages()) {
				if (templateCurrrencyDetails.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("currencyDetails", templateCurrrencyDetails.getObject());
				}
			}
			
			MultiValueMap<String, Object> map5 = new LinkedMultiValueMap<>();
			map5.add("templateId", templateId);
			map5.add("mappingType", "s3Bucket");
			DataResponse dataResponseS3Bucket = restUtilities.postRestObject(request,
					"/getDefaultTemplateMasterMappedData", map5, user.getUserId());
			if (dataResponseS3Bucket != null && dataResponseS3Bucket.getHasMessages()) {
				if (dataResponseS3Bucket.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("bucketId", dataResponseS3Bucket.getObject());
				}
			}
			
			/*MultiValueMap<String, Object> map6 = new LinkedMultiValueMap<>();
			map4.add("templateId", templateId);
			map4.add("mappingType", "schedularMaster");
			DataResponse dataResponseschedularMaster = restUtilities.postRestObject(request,
					"/getDefaultTemplateMasterMappedData", map6, user.getUserId());
			if (dataResponseschedularMaster != null && dataResponseschedularMaster.getHasMessages()) {
				if (dataResponseschedularMaster.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("schedularMasters", dataResponseschedularMaster.getObject());
				}
			}
			
			MultiValueMap<String, Object> map7 = new LinkedMultiValueMap<>();
			map4.add("templateId", templateId);
			map4.add("mappingType", "fileMultiPart");
			DataResponse dataResponsefileMultiPart = restUtilities.postRestObject(request,
					"/getDefaultTemplateMasterMappedData", map7, user.getUserId());
			if (dataResponsefileMultiPart != null && dataResponsefileMultiPart.getHasMessages()) {
				if (dataResponsefileMultiPart.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("fileMultiParts", dataResponsefileMultiPart.getObject());
				}
			}*/

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		defaultTemplatesForm.setPageMode("edit");
		mv.setViewName("tiles-anvizent-admin:defaultMappingConfiguration");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/allMappingInfo", method = RequestMethod.POST)
	public ModelAndView allMappingInfo(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("allMappingInfoForm") AllMappingInfoForm allMappingInfoForm,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in allMappingInfo()");

		if (allMappingInfoForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/allMappingInfo");
			return mv;
		}
		try {
			User user = CommonUtils.getUserDetails(request, null, null);
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllmappingInfoById/{client_Id}",
					user.getUserId(), allMappingInfoForm.getClientId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataResponse.getObject();
					AllMappingInfo allMappingInfo = mapper.convertValue(obj, new TypeReference<AllMappingInfo>() {
					});
					allMappingInfoForm.setNumberOfVerticals(allMappingInfo.getNumberOfVerticals());
					allMappingInfoForm.setNumberOfTableScripts(allMappingInfo.getNumberOfTableScripts());
					allMappingInfoForm.setNumberOfDatabases(allMappingInfo.getNumberOfDatabases());
					allMappingInfoForm.setNumberOfConnectors(allMappingInfo.getNumberOfConnectors());
					allMappingInfoForm.setNumberOfDLs(allMappingInfo.getNumberOfDLs());
					allMappingInfoForm.setNumberOfWebServices(allMappingInfo.getNumberOfWebServices());
					allMappingInfoForm.setNumberOfCurrencies(allMappingInfo.getNumberOfCurrencies());
					allMappingInfoForm.setNumberOfTableScripts(allMappingInfo.getNumberOfTableScripts());
					allMappingInfoForm.setNumberOfS3BuckeMappings(allMappingInfo.getNumberOfS3BuckeMappings());
				} else {
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-admin:allMappingInfo");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/webServiceClientMapping", method = RequestMethod.GET)
	public ModelAndView webserviceClientMapping(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, Locale locale) {
		LOGGER.info("in webServiceClientMapping()");
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				Long clientId = Long
						.valueOf((String) SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID));
				String activeClientsUrl = "/activeClients";
				DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, activeClientsUrl,
						new LinkedMultiValueMap<>(), DataResponse.class);
				Map<String, Object> modelObject = new HashMap<String, Object>();
				if (dataResponse != null) {
					List<CloudClient> clientListNew = new ArrayList<>();
					List<Map<String, Object>> clientList = (List<Map<String, Object>>) dataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					List<CloudClient> cloudClients = mapper.convertValue(clientList,
							new TypeReference<List<CloudClient>>() {
							});
					for (CloudClient cloudClient : cloudClients) {
						if (clientId == cloudClient.getId()) {
							clientListNew.add(cloudClient);
						}
					}
					modelObject.put("clientList", clientListNew);
				}

				DataResponse dataResponse1 = restUtilities.getRestObject(request, "/getAllWebServices",
						user.getUserId());
				Map<Integer, String> webserviceList = (Map<Integer, String>) dataResponse1.getObject();

				modelObject.put("webserviceList", webserviceList);
				mv.addObject("modelObject", modelObject);

				mv.setViewName("tiles-anvizent-admin:webServiceClientMapping");
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
		}

		return mv;
	}

	@RequestMapping(value = "/defaultQueries", method = RequestMethod.GET)
	public ModelAndView defaultQueries(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			ModelAndView mv) {
		LOGGER.debug("in defaultQueries()");
		CommonUtils.setActiveScreenName("defaultQueries", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			mv.setViewName("tiles-anvizent-admin:defaultQueries");
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("databseList")
	public List<Database> getDatabaseTypes(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<Database> databseList = null;

		databseList = new ArrayList<>();
		DataResponse databseTypesDataResponse = restUtilities.getRestObject(request, "/getDatabasesTypes",
				user.getUserId());
		List<LinkedHashMap<String, Object>> databseTypes = null;
		if (databseTypesDataResponse != null && databseTypesDataResponse.getHasMessages()
				&& databseTypesDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			databseTypes = (List<LinkedHashMap<String, Object>>) databseTypesDataResponse.getObject();
		} else {
			databseTypes = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : databseTypes) {
			Database databse = new Database();
			int id = Integer.parseInt(map.get("id").toString());
			int connectorId = Integer.parseInt(map.get("connector_id").toString());
			databse.setId(id);
			databse.setName(map.get("name").toString());
			databse.setConnector_id(connectorId);
			databseList.add(databse);
		}

		return databseList;
	}

	@RequestMapping(value = "/saveILDefaultQuery", method = RequestMethod.POST)
	public ModelAndView saveILDefaultQuery(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@RequestParam("iLId") Integer iLId, @RequestParam("id") Integer id, @RequestParam("iLquery") String iLquery,
			@RequestParam("iLIncrementalUpdateQuery") String iLIncrementalUpdateQuery,
			@RequestParam("historicalLoad") String historicalLoad, @RequestParam("maxDateQuery") String maxDateQuery,
			BindingResult result, Locale locale) {
		LOGGER.debug("in saveILDefaultQuery()");

		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("iLId", iLId);
		map.add("id", id);
		map.add("iLquery", iLquery);
		map.add("iLIncrementalUpdateQuery", iLIncrementalUpdateQuery);
		map.add("historicalLoad", historicalLoad);
		map.add("maxDateQuery", maxDateQuery);

		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				DataResponse dataResponse = restUtilities.postRestObject(request, "/saveILDefaultQuery", map,
						user.getUserId());
				List<Message> messages = dataResponse.getMessages();
				Message message = messages.get(0);
				mv.addObject("errors", message.getText());
				mv.addObject("messagecode", message.getCode());
				mv.setViewName("tiles-anvizent-admin:defaultQueries");
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				LOGGER.error("", e);
			}
		}
		return mv;
	}

	@RequestMapping(value = "/saveILDefaultQuery", method = RequestMethod.GET)
	public ModelAndView saveILDefaultQuery(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		LOGGER.debug("in saveILDefaultQuery()");
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			mv.setViewName("redirect:/admin/defaultQueries");
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getTableScripts", method = RequestMethod.GET)
	public ModelAndView getTableScripts(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			Locale locale) {
		LOGGER.debug("in tableScripts()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			Map<?, ?> object = restUtilities.getRestObject(request, "/getTableScripts", Map.class, user.getUserId());
			List<LinkedHashMap<String, Object>> dLs = (List<LinkedHashMap<String, Object>>) object.get("object");
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<TableScriptsForm> tableScripts = mapper.convertValue(dLs, new TypeReference<List<TableScriptsForm>>() {
			});
			mv.addObject("tableScripts", tableScripts);
			mv.setViewName("tiles-anvizent-admin:tableScripts");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("getClientList")
	public Map<Object, Object> getClientList(HttpServletRequest request) {

		// TODO
		long clientId = Long
				.valueOf((String) SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID));
		User user = CommonUtils.getUserDetails(request, null, null);

		String activeClientsUrl = "/activeClients";
		DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, activeClientsUrl,
				new LinkedMultiValueMap<>(), DataResponse.class, user.getUserId());
		Map<Object, Object> clientMap = new LinkedHashMap<>();
		if (dataResponse != null) {
			List<Map<String, Object>> clientList = (List<Map<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<CloudClient> cloudClients = mapper.convertValue(clientList, new TypeReference<List<CloudClient>>() {
			});

			if (cloudClients == null || cloudClients.size() == 0) {
				return new HashMap<>();
			}

			for (CloudClient cloudClient : cloudClients) {
				// TODO Add this condition
				if (clientId == cloudClient.getId()) {
					clientMap.put(cloudClient.getId(), cloudClient.getId() + " - " + cloudClient.getClientName());
				}
			}
		}
		return clientMap;
	}

	@RequestMapping(value = "/clientWebserviceMapping", method = RequestMethod.GET)
	public ModelAndView getClients(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session,
			@ModelAttribute("clientWebserviceMappingForm") ClientWebserviceMappingForm clientWebserviceMappingForm,
			BindingResult result, Locale locale) {
		LOGGER.info("in clientWebserviceMapping()");
		CommonUtils.setActiveScreenName("clientWebserviceMapping", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			mv.setViewName("tiles-anvizent-admin:clientWebserviceMapping");
		} else {
			mv.setViewName("redirect:/admin/clientWebserviceMapping");
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("getAllWebServices")
	public Map<Object, Object> getAllWebServices(HttpServletRequest request) {
		Map<Object, Object> webServiceMap = new LinkedHashMap<>();
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllWebServices", user.getUserId());
		if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			Map<Integer, String> map = (Map<Integer, String>) dataResponse.getObject();
			for (Map.Entry<Integer, String> entry : map.entrySet()) {
				webServiceMap.put(entry.getKey(), entry.getValue());
			}
		} else {
			return new HashMap<>();
		}
		return webServiceMap;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/clientWebserviceMapping", method = RequestMethod.POST)
	public ModelAndView clientWebserviceMapping(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv,
			@ModelAttribute("clientWebserviceMappingForm") ClientWebserviceMappingForm clientWebserviceMappingForm,
			RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		LOGGER.info("in clientWebserviceMapping()");

		if (clientWebserviceMappingForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.pleaseSelectClient", null, locale));
			mv.setViewName("tiles-anvizent-admin:clientWebserviceMapping");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getWebserviceByClientId/{clientId}",
					user.getUserId(), clientWebserviceMappingForm.getClientId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				List<LinkedHashMap<String, Object>> data = (List<LinkedHashMap<String, Object>>) dataResponse
						.getObject();
				List<WebService> webservices = mapper.convertValue(data, new TypeReference<List<WebService>>() {
				});
				clientWebserviceMappingForm.setWebservices(new ArrayList<>());
				List<String> selectedwebserviceList = clientWebserviceMappingForm.getWebservices();
				webservices.forEach(webservice -> {
					selectedwebserviceList.add(webservice.getWeb_service_id() + "");
				});

			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-admin:clientWebserviceMapping");
		return mv;
	}

	@RequestMapping(value = "/clientWebserviceMapping/save", method = RequestMethod.POST)
	public ModelAndView saveclientWebserviceMapping(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv,
			@ModelAttribute("clientWebserviceMappingForm") ClientWebserviceMappingForm clientWebserviceMappingForm,
			RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {

		LOGGER.info("in saveClientWebserviceMapping()");

		if (clientWebserviceMappingForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.pleaseSelectClient", null, locale));
			mv.setViewName("redirect:/admin/clientWebserviceMapping");
			return mv;
		}

		mv.setViewName("redirect:/admin/clientWebserviceMapping");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			map.add("client_Id", clientWebserviceMappingForm.getClientId());

			if (clientWebserviceMappingForm.getWebservices() != null
					&& clientWebserviceMappingForm.getWebservices().size() > 0) {
				for (String webserviceId : clientWebserviceMappingForm.getWebservices()) {
					map.add("webServiceIds", webserviceId);
				}
			}

			DataResponse dataResponse = restUtilities.postRestObject(request, "/saveClientWebserviceMapping", map,
					user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				clientWebserviceMappingForm.setClientId(0);
			} else {
				mv.addObject("messagecode", "ERROR");
				mv.addObject("errors", messageSource.getMessage(
						"anvizent.message.error.text.errorWhileSavingClientWebserviceMapping", null, locale));
				mv.setViewName("tiles-anvizent-admin:clientWebserviceMapping");
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;
	}

	@RequestMapping(value = "/generalSettings", method = RequestMethod.GET)
	public ModelAndView generalSettings(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("generalSettings") GeneralSettings generalSettings, Locale locale) {
		LOGGER.debug("in generalSettings()");
		CommonUtils.setActiveScreenName("generalSettings", session);
		mv.setViewName("tiles-anvizent-admin:generalSettings");
		return mv;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/generalSettings", method = RequestMethod.POST)
	public ModelAndView addGeneralSettings(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("generalSettings") GeneralSettings generalSettings, Locale locale) {
		LOGGER.debug("in generalSettings()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/getSettingsInfoByID/{client_id}",
					generalSettings, user.getUserId(), generalSettings.getClientId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					GeneralSettings getInfo = mapper.convertValue(map, new TypeReference<GeneralSettings>() {
					});
					generalSettings.setId(getInfo.getId());
					generalSettings.setFlatFile(getInfo.getFlatFile());
					generalSettings.setDatabase(getInfo.getDatabase());
					generalSettings.setWebService(getInfo.getWebService());
					generalSettings.setFileSize(getInfo.getFileSize());

				}
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-admin:generalSettings");
		return mv;

	}

	@RequestMapping(value = "/generalSettings/save", method = RequestMethod.POST)
	public ModelAndView saveGeneralSettings(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("generalSettings") GeneralSettings generalSettings, RedirectAttributes redirectAttributes,
			Locale locale) {

		LOGGER.info("in saveGeneralSettings()");
		User user = CommonUtils.getUserDetails(request, null, null);

		try {
			mv.setViewName("redirect:/admin/generalSettings");
			DataResponse dataResponse = null;
			if (generalSettings.getId() != 0) {
				dataResponse = restUtilities.postRestObject(request, "/updateGeneralSettings", generalSettings,
						user.getUserId());
			} else {
				dataResponse = restUtilities.postRestObject(request, "/saveGeneralSettings", generalSettings,
						user.getUserId());
			}

			if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				mv.setViewName("redirect:/admin/generalSettings");
			} else {
				mv.setViewName("tiles-anvizent-admin:generalSettings");
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());

			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		return mv;
	}

	@RequestMapping(value = "/existingJars", method = RequestMethod.GET)
	public ModelAndView existedJars(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) {
		LOGGER.debug("in existedJars()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			CommonUtils.setActiveScreenName("existingJars", session);
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getAvailableJarsList", user.getUserId());
			mv.addObject("jarFilesList", dataResponse.getObject());
			mv.setViewName("tiles-anvizent-admin:existingJars");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;

	}

	@RequestMapping(value = "/downloadJar/{jarName}", method = RequestMethod.GET)
	public ModelAndView downloadJar(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @PathVariable("jarName") String jarName, Locale locale) {
		LOGGER.debug("in downloadJar()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ResponseEntity<byte[]> templateFileResponse = restUtilities.getRestEntity(request, "/downloadJar/{jarName}",
					byte[].class, user.getUserId(), jarName);
			byte[] templateFile = templateFileResponse.getBody();
			if (templateFile != null && templateFile.length > 0) {
				String fileName = Constants.Temp.getTempFileDir()
						+ templateFileResponse.getHeaders().get("filename").get(0);
				;
				File targetFile = new File(fileName);
				OutputStream outStream = new FileOutputStream(targetFile);
				outStream.write(templateFile);
				outStream.close();
				CommonUtils.sendFIleToStream(fileName, request, response);
				return null;
			}

			mv.setViewName("tiles-anvizent-admin:existingJars");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		return mv;

	}

	@RequestMapping(value = "/clientConfigurationSettings", method = RequestMethod.GET)
	public ModelAndView clientConfigurationSettings(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, HttpSession session,
			@ModelAttribute("clientConfigurationSettings") ClientConfigurationSettings clientConfigurationSettings,
			Locale locale) {
		LOGGER.debug("in clientConfigurationSettings()");
		CommonUtils.setActiveScreenName("clientConfigurationSettings", session);
		mv.setViewName("tiles-anvizent-admin:clientConfigurationSettings");
		return mv;

	}

	@RequestMapping(value = "/clientConfigurationSettings/save", method = RequestMethod.POST)
	public ModelAndView saveClientConfigurationSettings(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv,
			@ModelAttribute("clientConfigurationSettings") ClientConfigurationSettings clientConfigurationSettings,
			RedirectAttributes redirectAttributes, Locale locale) {

		LOGGER.info("in saveClientConfigurationSettings()");
		User user = CommonUtils.getUserDetails(request, null, null);

		try {
			mv.setViewName("redirect:/admin/clientConfigurationSettings");
			DataResponse dataResponse = null;
			if (clientConfigurationSettings.getId() != 0) {
				dataResponse = restUtilities.postRestObject(request, "/updateclientConfigSettings",
						clientConfigurationSettings, user.getUserId());
			} else {
				dataResponse = restUtilities.postRestObject(request, "/saveclientConfigSettings",
						clientConfigurationSettings, user.getUserId());
			}

			if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				mv.setViewName("redirect:/admin/clientConfigurationSettings");
			} else {
				mv.setViewName("tiles-anvizent-admin:clientConfigurationSettings");
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());

			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/clientConfigurationSettings", method = RequestMethod.POST)
	public ModelAndView clientConfigurationSettings(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv,
			@ModelAttribute("clientConfigurationSettings") ClientConfigurationSettings clientConfigurationSettings,
			Locale locale) {
		LOGGER.debug("in clientConfigurationSettings()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/getConfigSettingsInfoByID/{client_id}",
					clientConfigurationSettings, user.getUserId(), clientConfigurationSettings.getClientId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					ClientConfigurationSettings getInfo = mapper.convertValue(map,
							new TypeReference<ClientConfigurationSettings>() {
							});
					clientConfigurationSettings.setId(getInfo.getId());
					clientConfigurationSettings.setFrom(getInfo.getFrom());
					clientConfigurationSettings.setPassword(getInfo.getPassword());
					clientConfigurationSettings.setTo(getInfo.getTo());
					clientConfigurationSettings.setCc(getInfo.getCc());
					clientConfigurationSettings.setBcc(getInfo.getBcc());
					clientConfigurationSettings.setReplyTo(getInfo.getReplyTo());
					clientConfigurationSettings.setSmtpHost(getInfo.getSmtpHost());
					clientConfigurationSettings.setSmtpFactoryPort(getInfo.getSmtpFactoryPort());
					clientConfigurationSettings.setSmtpPort(getInfo.getSmtpPort());

				}
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-admin:clientConfigurationSettings");
		return mv;

	}

	@RequestMapping(value = "/templateMigration", method = RequestMethod.GET)
	public ModelAndView templateMigration(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("templateMigration") TemplateMigration templateMigration,
			Locale locale) {
		LOGGER.debug("in templateMigration()");
		CommonUtils.setActiveScreenName("templateMigration", session);
		mv.setViewName("tiles-anvizent-admin:templateMigration");
		return mv;

	}

	@RequestMapping(value = "/templateMigration", method = RequestMethod.POST)
	public ModelAndView templateMigrationEdit(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("templateMigration") TemplateMigration templateMigration,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in templateMigration()");

		if (templateMigration.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/templateMigration");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getUsersByClientId/{client_Id}",
					user.getUserId(), templateMigration.getClientId());
			if (dataResponse != null && dataResponse.getHasMessages()
					&& dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("getAllUsers", dataResponse.getObject());
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("tiles-anvizent-admin:templateMigration");
		return mv;

	}

	@RequestMapping(value = "/templateMigration/list", method = RequestMethod.POST)
	public ModelAndView templateMigrationList(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("templateMigration") TemplateMigration templateMigration,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in templateMigrationList()");

		if (templateMigration.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/templateMigration");
			return mv;
		}
		try {
			User user = CommonUtils.getUserDetails(request, null, null);
			DataResponse packageListDataResponse = restUtilities.getRestObject(request,
					"/getUserPackageList/{clientUserId}", user.getUserId(), templateMigration.getUserId());

			if (packageListDataResponse != null && packageListDataResponse.getHasMessages()
					&& packageListDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<Package> userPackageList = mapper.convertValue(packageListDataResponse.getObject(),
						new TypeReference<List<Package>>() {
						});
				mv.addObject("userPackageList", userPackageList);
				templateMigration.setPageMode("list");
			}

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getUsersByClientId/{client_Id}",
					user.getUserId(), templateMigration.getClientId());
			if (dataResponse != null && dataResponse.getHasMessages()
					&& dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("getAllUsers", dataResponse.getObject());
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("tiles-anvizent-admin:templateMigration");
		return mv;

	}

	@RequestMapping(value = "/migrateTemplate", method = RequestMethod.GET)
	public ModelAndView migrateTemplate(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) {
		LOGGER.debug("in migrateTemplate()");
		CommonUtils.setActiveScreenName("migrateTemplate", session);
		mv.setViewName("tiles-anvizent-admin:migrateTemplate");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/templateMigration/save", method = RequestMethod.POST)
	public ModelAndView templateMigrationSave(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("templateMigration") TemplateMigration templateMigration,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in templateMigrationSave()");

		if (templateMigration.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/templateMigration");
			return mv;
		}
		try {
			User user = CommonUtils.getUserDetails(request, null, null);

			StringBuffer packageIds = new StringBuffer();
			if (templateMigration.getPackageList() != null && !templateMigration.getPackageList().isEmpty()) {

				for (Package packageInfo : templateMigration.getPackageList()) {
					if (packageInfo.getIsMapped() && packageInfo.getIsMapped() != null) {
						packageIds.append(packageInfo.getPackageId()).append(",");
					}
				}
				String modifiedPackageIds = packageIds.toString();

				DataResponse getPackagetablesData = restUtilities.getRestObject(request,
						"/migrateUserToUser/{packageIds}/{clientUserId}", user.getUserId(),
						modifiedPackageIds.substring(0, modifiedPackageIds.length() - 1),
						templateMigration.getUserId());

				if (getPackagetablesData != null && getPackagetablesData.getHasMessages()
						&& getPackagetablesData.getMessages().get(0).getCode().equals("SUCCESS")) {
					StringBuffer packageIdForFormJsonFile = new StringBuffer();
					for (Package packageInfo : templateMigration.getPackageList()) {
						if (packageInfo.getIsMapped() && packageInfo.getIsMapped() != null) {
							packageIdForFormJsonFile.append(packageInfo.getPackageId()).append("_");
						}
					}
					String fileName = Constants.Temp.getTempFileDir() + packageIdForFormJsonFile.toString()
							+ "JsonFile.json";
					try (FileWriter file = new FileWriter(fileName)) {

						LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) getPackagetablesData
								.getObject();

						JSONObject json = new JSONObject(map);
						file.write(json.toString());
						file.flush();

						CommonUtils.sendFIleToStream(fileName, request, response);

						mv.addObject("messagecode", "SUCCESS");
						mv.addObject("errors", "Successfully getting table data.");

					} catch (IOException e) {
						LOGGER.error("", e);
					}
				}

			}

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getUsersByClientId/{client_Id}",
					user.getUserId(), templateMigration.getClientId());
			if (dataResponse != null && dataResponse.getHasMessages()
					&& dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("getAllUsers", dataResponse.getObject());
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("tiles-anvizent-admin:templateMigration");
		return mv;

	}

	@RequestMapping(value = "/versionUpgrade", method = RequestMethod.GET)
	public ModelAndView versionUpgrade(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("versionUpgrade") VersionUpgrade versionUpgrade, Locale locale) {
		LOGGER.debug("in versionUpgrade()");
		CommonUtils.setActiveScreenName("versionUpgrade", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			versionUpgrade.setPageMode("list");
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getVersionUpgrade", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
				mv.addObject("versionUpgradeModel", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}

		mv.setViewName("tiles-anvizent-admin:versionUpgrade");
		return mv;
	}

	@RequestMapping(value = "/versionUpgrade/add", method = RequestMethod.GET)
	public ModelAndView versionUpgradeAdd(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("versionUpgrade") VersionUpgrade versionUpgrade, RedirectAttributes redirectAttributes) {
		LOGGER.debug("in versionUpgrade Add ()");
		mv.setViewName("tiles-anvizent-admin:versionUpgrade");
		versionUpgrade.setPageMode("add");
		return mv;
	}

	@RequestMapping(value = "/versionUpgrade/add", method = RequestMethod.POST)
	public ModelAndView addVersionUpgrade(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("versionUpgrade") VersionUpgrade versionUpgrade, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.debug("in addVersionUpgrade ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.postRestObject(request, "/createVersionUpgrade", versionUpgrade,
					user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			}
			mv.setViewName("redirect:/admin/versionUpgrade");
		} catch (Exception e) {
			LOGGER.error("", e);
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}

		return mv;
	}

	@RequestMapping(value = { "/versionUpgrade/edit" }, method = RequestMethod.GET)
	public ModelAndView versionUpgrades(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("versionUpgrade") VersionUpgrade versionUpgrade, RedirectAttributes redirectAttributes,
			BindingResult result, Locale locale) {
		mv.setViewName("redirect:/admin/versionUpgrade");
		return mv;
	}

	@RequestMapping(value = "/versionUpgrade/edit", method = RequestMethod.POST)
	public ModelAndView getVersionUpgradeDetailsById(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("versionUpgrade") VersionUpgrade versionUpgrade,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in getVersionUpgradeDetailsById()");
		if (versionUpgrade.getVersionId() == 0) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.versionIdisShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/versionUpgrade");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			mv.setViewName("redirect:/admin/versionUpgrade");
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getVersionUpgradeDetailsById/{id}",
					user.getUserId(), versionUpgrade.getVersionId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> versionObj = (LinkedHashMap<String, Object>) dataResponse.getObject();
					VersionUpgrade versionUpgrades = mapper.convertValue(versionObj,
							new TypeReference<VersionUpgrade>() {
							});
					versionUpgrade.setVersionNumber(versionUpgrades.getVersionNumber());
					versionUpgrade.setDescription(versionUpgrades.getDescription());
					versionUpgrade.setFilePath(versionUpgrades.getFilePath());
					versionUpgrade.setVersionId(versionUpgrades.getVersionId());
					versionUpgrade.setLatestVersion(versionUpgrades.isLatestVersion());
					versionUpgrade.setPageMode("edit");
					;
					mv.setViewName("tiles-anvizent-admin:versionUpgrade");
				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("currencyList")
	public Map<Object, Object> getCurrencyList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getCurrencyList", user.getUserId());
		List<CurrencyList> currencyNames = null;
		if (dataResponse != null && dataResponse.getHasMessages()
				&& dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

			List<LinkedHashMap<String, Object>> CurrencyResponse = (List<LinkedHashMap<String, Object>>) dataResponse
					.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			currencyNames = mapper.convertValue(CurrencyResponse, new TypeReference<List<CurrencyList>>() {
			});
		} else {
			currencyNames = new ArrayList<>();
		}

		Map<Object, Object> currencyMap = new LinkedHashMap<>();
		for (CurrencyList currency : currencyNames) {
			currencyMap.put(currency.getCurrencyCode(), currency.getCurrencyName());
		}

		return currencyMap;
	}

	@RequestMapping(value = "/s3BucketInfo", method = RequestMethod.GET)
	public ModelAndView s3BucketInfo(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) throws IOException {
		LOGGER.debug("in s3BucketInfo()");
		CommonUtils.setActiveScreenName("s3BucketInfo", session);
		mv.setViewName("tiles-anvizent-admin:s3BucketInfo");
		return mv;
	}

	@RequestMapping(value = "/clientS3Mapping", method = RequestMethod.GET)
	public ModelAndView clientS3Mapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) throws IOException {
		LOGGER.debug("in clientS3Mapping()");
		CommonUtils.setActiveScreenName("clientS3Mapping", session);
		mv.setViewName("tiles-anvizent-admin:s3ClientMapping");
		return mv;
	}
	
	@RequestMapping(value = "/webServiceAuthParamsEncryption", method = RequestMethod.GET)
	public ModelAndView webserviceAuthParamsEncyption(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) throws IOException {
		LOGGER.debug("in webserviceAuthParamsEncyption()");
		CommonUtils.setActiveScreenName("webServiceAuthParamsEncryption", session);
		mv.setViewName("tiles-anvizent-admin:webServiceAuthParamsEncryption");
		return mv;
	}

}
