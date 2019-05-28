package com.datamodel.anvizent.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientCurrencyMapping;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.CurrencyIntegration;
import com.datamodel.anvizent.service.model.CurrencyList;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.JobResultForm;
import com.datamodel.anvizent.service.model.TimeZones;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author usharani.konda, apurva.deshmukh
 *
 */
@Controller
@RequestMapping(value = "/admin/currencyIntegration")
public class CurrencyIntegrationController {
	protected static final Log LOGGER = LogFactory.getLog(CurrencyIntegrationController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	@Qualifier("commonServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilitiesCommon;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	@Qualifier("etlAdminServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getCurrencyIntegration(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, HttpSession session, RedirectAttributes redirectAttributes,
			@ModelAttribute("currencyIntegration") CurrencyIntegration currencyIntegration, Locale locale) {
		LOGGER.debug("in getCurrencyIntegration()");
		CommonUtils.setActiveScreenName("currencyIntegration", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				// MultiValueMap<String, Object> map = new
				// LinkedMultiValueMap<>();
				// map.add("id", currencyIntegration.getId());
				DataResponse dataResponse = restUtilities.getRestObject(request, "/getCurrencyIntegration",
						user.getUserId());
				if (dataResponse != null && dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
						ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						CurrencyIntegration currencyIntTemplate = mapper.convertValue(map,
								new TypeReference<CurrencyIntegration>() {
								});
						currencyIntegration.setId(currencyIntTemplate.getId());
						currencyIntegration.setApiUrl(currencyIntTemplate.getApiUrl());
						currencyIntegration.setAccessKey(currencyIntTemplate.getAccessKey());
						currencyIntegration.setCurrencies(currencyIntTemplate.getCurrencies());
						currencyIntegration.setSource(currencyIntTemplate.getSource());
						currencyIntegration.setTime_hours(currencyIntTemplate.getTime_hours());
						currencyIntegration.setTime_minutes(currencyIntTemplate.getTime_minutes());
						currencyIntegration.setTimeZone(currencyIntTemplate.getTimeZone());
						currencyIntegration.setJobName(currencyIntTemplate.getJobName());
						currencyIntegration.setJobfile_names(currencyIntTemplate.getJobfile_names());
						currencyIntegration.setJobFileNames(currencyIntTemplate.getJobFileNames());
						currencyIntegration.setClientSpecificJobName(currencyIntTemplate.getClientSpecificJobName());
						currencyIntegration
								.setClientSpecificJobfile_names(currencyIntTemplate.getClientSpecificJobfile_names());
						currencyIntegration
								.setClientSpecificJobFileNames(currencyIntTemplate.getClientSpecificJobFileNames());
						mv.setViewName("tiles-anvizent-admin:currencyIntegration");
					}
				} /*
					 * else { mv.addObject("messagecode", "FAILED");
					 * mv.addObject("errors", messageSource.getMessage(
					 * "anvizent.package.label.unableToProcessYourRequest",
					 * null, locale)); }
					 */
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				e.printStackTrace();
			}
		}
		mv.addObject("timesZoneList", getTimesZoneList(request));
		mv.setViewName("tiles-anvizent-admin:currencyIntegration");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView saveCurrencyIntegration(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("currencyIntegration") CurrencyIntegration currencyIntegration,
			RedirectAttributes redirectAttributes, Locale locale) {

		LOGGER.info("in savecurrencyIntegration");
		User user = CommonUtils.getUserDetails(request, null, null);
		Boolean isUploaded = false;
		try {

			MultiValueMap<Object, Object> filesMap = new LinkedMultiValueMap<>();
			if (currencyIntegration.getJarFiles() != null) {
				if (currencyIntegration.getJarFiles().size() > 0) {
					String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());
					for (MultipartFile file : currencyIntegration.getJarFiles()) {
						String tempFileName = tempFolderName + file.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, tempFileName, file);
						filesMap.add("files", new FileSystemResource(tempFileName));
					}
					DataResponse uploadedFileResponse = restUtilitieslb.postRestObject(request, "/uploadIlOrDlFiles",
							filesMap, user.getUserId());
					if (uploadedFileResponse != null && uploadedFileResponse.getHasMessages()
							&& uploadedFileResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						isUploaded = true;
					}
				}
			} else {
				isUploaded = true;
			}

			MultiValueMap<Object, Object> files = new LinkedMultiValueMap<>();
			if (currencyIntegration.getClientSpecificJarFiles() != null) {
				if (currencyIntegration.getClientSpecificJarFiles().size() > 0) {
					String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());
					for (MultipartFile file : currencyIntegration.getClientSpecificJarFiles()) {
						String tempFileName = tempFolderName + file.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, tempFileName, file);
						files.add("files", new FileSystemResource(tempFileName));
					}
					DataResponse uploadedFileResponse = restUtilitieslb.postRestObject(request, "/uploadIlOrDlFiles",
							files, user.getUserId());
					if (uploadedFileResponse != null && uploadedFileResponse.getHasMessages()
							&& uploadedFileResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						isUploaded = true;
					}
				}
			} else {
				isUploaded = true;
			}

			currencyIntegration.setJarFiles(null);
			currencyIntegration.setClientSpecificJarFiles(null);
			DataResponse dataResponse = restUtilities.postRestObject(request, "/saveCurrencyIntegration",
					currencyIntegration, user.getUserId());

			if ( dataResponse == null ) {
				throw new Exception("");
			}
			if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.setViewName("redirect:/admin/currencyIntegration");
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				mv.setViewName("tiles-anvizent-admin:currencyIntegration");
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());

			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping(value = "/instantRun", method = RequestMethod.POST)
	public ModelAndView instantRun(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("currencyIntegration") CurrencyIntegration currencyIntegration,
			RedirectAttributes redirectAttributes, Locale locale) {

		LOGGER.info("in instantRun");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/getInstantRunInfo", currencyIntegration,
					user.getUserId());

			if ( dataResponse == null ) {
				throw new Exception("");
			}
			if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("redirect:/admin/currencyIntegration");
		return mv;
	}

	@ModelAttribute("hours")
	public Map<String, String> hours() {
		Map<String, String> hours = new LinkedHashMap<>();
		for (int i = 0; i < 24; i++) {
			StringBuilder hour = new StringBuilder();
			if (i <= 9) {
				hour.append("0");
			}
			hour.append(i);
			String hh = hour.toString();
			hours.put(hh, hh);
		}
		return hours;
	}

	@ModelAttribute("minutes")
	public Map<String, String> minutes() {
		Map<String, String> minutes = new LinkedHashMap<>();
		for (int i = 0; i < 60; i++) {
			StringBuilder minute = new StringBuilder();
			if (i <= 9) {
				minute.append("0");
			}
			minute.append(i);
			String mm = minute.toString();
			minutes.put(mm, mm);
		}
		return minutes;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("timeZonesList")
	public Map<Object, Object> getTimesZoneList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilitiesCommon.getRestObject(request, "/getTimeZones", user.getUserId());
		List<TimeZones> zoneNames = null;
		if (dataResponse != null && dataResponse.getHasMessages()
				&& dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

			List<LinkedHashMap<String, Object>> timeZoneResponse = (List<LinkedHashMap<String, Object>>) dataResponse
					.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			zoneNames = mapper.convertValue(timeZoneResponse, new TypeReference<List<TimeZones>>() {
			});
		} else {
			zoneNames = new ArrayList<>();
		}

		Map<Object, Object> zonesMap = new LinkedHashMap<>();
		for (TimeZones timeZone : zoneNames) {
			zonesMap.put(timeZone.getZoneName(), timeZone.getZoneNameDisplay());
		}

		return zonesMap;
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

	@RequestMapping(value = "/clientCurrencyMapping", method = RequestMethod.GET)
	public ModelAndView clientCurrencyMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("clientCurrencyMapping") ClientCurrencyMapping clientCurrencyMapping,
			Locale locale) {
		LOGGER.debug("in clientCurrencyMapping()");
		CommonUtils.setActiveScreenName("clientCurrencyMapping", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			clientCurrencyMapping.setPageMode("list");
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getClientCurrencyMapping",user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
				mv.addObject("clientCurrencyMapping1", dataResponse.getObject());
				 
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.addObject("currencyList", getCurrencyList(request));
		mv.setViewName("tiles-anvizent-admin:clientCurrencyMapping");
		return mv;
	}

	@RequestMapping(value = "/clientCurrencyMapping/add", method = RequestMethod.GET)
	public ModelAndView clientCurrencyMappingAdd(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("clientCurrencyMapping") ClientCurrencyMapping clientCurrencyMapping,
			RedirectAttributes redirectAttributes) {
		LOGGER.debug("in clientCurrencyMapping Add ()");
		mv.setViewName("tiles-anvizent-admin:clientCurrencyMapping");
		clientCurrencyMapping.setPageMode("add");
		return mv;
	}

	@RequestMapping(value = "/clientCurrencyMapping/add", method = RequestMethod.POST)
	public ModelAndView addclientCurrencyMapping(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("clientCurrencyMapping") ClientCurrencyMapping clientCurrencyMapping,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in addclientCurrencyMapping()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.postRestObject(request, "/createClientCurrencyMapping", clientCurrencyMapping, user.getUserId());
			if (dataResponse != null) {
				if (dataResponse.getHasMessages()) {
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				} else {
					redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", "Unable to add currency mapping");
			}
			
			mv.setViewName("redirect:/admin/currencyIntegration/clientCurrencyMapping");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}

		return mv;
	}

	@RequestMapping(value = { "/clientCurrencyMapping/edit" }, method = RequestMethod.GET)
	public ModelAndView getclientCurrencyMapping(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("clientCurrencyMapping") ClientCurrencyMapping clientCurrencyMapping,
			RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		mv.setViewName("redirect:/admin/currencyIntegration/clientCurrencyMapping");
		return mv;
	}

	@RequestMapping(value = "/clientCurrencyMapping/edit", method = RequestMethod.POST)
	public ModelAndView getclientCurrencyMappingDetailsById(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("clientCurrencyMapping") ClientCurrencyMapping clientCurrencyMapping,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in getclientCurrencyMappingDetailsById()");

			if (clientCurrencyMapping.getId() == 0 && StringUtils.isBlank(clientCurrencyMapping.getClientId())) {
				redirectAttributes.addFlashAttribute("errors",
						messageSource.getMessage("anvizent.message.error.text.idisShouldNotbeEmpty", null, locale));
				mv.setViewName("redirect:/admin/currencyIntegration/clientCurrencyMapping");
				return mv;
			}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			mv.setViewName("redirect:/admin/currencyIntegration/clientCurrencyMapping");

			DataResponse dataResponse = null;
			if (clientCurrencyMapping.getId() == 0) {
				dataResponse = restUtilities.getRestObject(request,
						"/getclientCurrencyMappingDetailsByClientId/{CLIENT_ID}", user.getUserId(),
						clientCurrencyMapping.getClientId());
			} else {
				dataResponse = restUtilities.getRestObject(request, "/getclientCurrencyMappingDetailsById/{id}",
						user.getUserId(), clientCurrencyMapping.getId());
			}

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> currencyObj = (LinkedHashMap<String, Object>) dataResponse
							.getObject();
					ClientCurrencyMapping currencyMap = mapper.convertValue(currencyObj,
							new TypeReference<ClientCurrencyMapping>() {
							});
					clientCurrencyMapping.setId(currencyMap.getId());
					clientCurrencyMapping.setClientId(currencyMap.getClientId());
					clientCurrencyMapping.setCurrencyType(currencyMap.getCurrencyType());
					clientCurrencyMapping.setCurrencyName(currencyMap.getCurrencyName());
					clientCurrencyMapping.setIsActive(currencyMap.getIsActive());
					clientCurrencyMapping.setBasecurrencyCode(currencyMap.getBasecurrencyCode());
					clientCurrencyMapping.setAccountingCurrencyCode(currencyMap.getAccountingCurrencyCode());
					clientCurrencyMapping.setPageMode("edit");
					mv.setViewName("tiles-anvizent-admin:clientCurrencyMapping");
				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
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

	@RequestMapping(value = "/jobResultsForDefaultCurrencyLoad", method = RequestMethod.GET)
	public ModelAndView viewResults(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("jobResultForm") JobResultForm jobResultForm, BindingResult result, Locale locale) {

		LOGGER.info("in jobResults()");

		User user = CommonUtils.getUserDetails(request, null, null);

		DataResponse dataResponse = restUtilities.getRestObject(request, "/getJobResultsForDefaultCurrencyLoad",
				user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages()
				&& dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("success")) {
			mv.addObject("viewResultList", dataResponse.getObject());
		}
		mv.setViewName("tiles-anvizent-entry:viewResults");
		jobResultForm.setPageMode("DefaultCurrencyLoad");

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/jobResultsForDefaultCurrencyLoad", method = RequestMethod.POST)
	public ModelAndView searchJobResult(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("jobResultForm") JobResultForm jobResultForm, BindingResult result, Locale locale) {

		LOGGER.info("in searchJobResult()");

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			String fromDate = jobResultForm.getFromDate();
			String toDate = jobResultForm.getToDate();

			DataResponse dataResponse = restUtilities.getRestObject(request,
					"/getJobResultsForDefaultCurrencyLoad/{fromDate}/{toDate}", user.getUserId(), fromDate, toDate);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.getObject();

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});

			mv.addObject("viewResultList", viewResultList);

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-entry:viewResults");

		return mv;
	}

}
