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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientVerticalMappingForm;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author apurva.deshmukh
 *
 */
@Controller
@RequestMapping(value = "/admin/vertical")
public class VerticalController {
	protected static final Log LOGGER = LogFactory.getLog(VerticalController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView vertical(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) {
		LOGGER.debug("in vertical()");
		CommonUtils.setActiveScreenName("vertical", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getExistingVerticals", user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("verticals", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("verticals", dataResponse.getObject());
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
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin:vertical");
		return mv;
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public ModelAndView createNewVertical(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("industry") Industry industry, BindingResult result, Locale locale,
			RedirectAttributes redirectAttributes) {
		LOGGER.debug("in createVertical()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			/* Default status if exception occurs */
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));

			DataResponse dataResponse = restUtilities.postRestObject(request, "/createNewVertical", industry,
					user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}

		mv.setViewName("redirect:/admin/vertical");
		return mv;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView createNewVertical(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		LOGGER.debug("in createVertical()");
		mv.setViewName("redirect:/admin/vertical");
		return mv;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editVertical(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("industry") Industry industry, BindingResult result, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.debug("in updateVertical()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			/* Default status if exception occurs */
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));

			DataResponse dataResponse = restUtilities.postRestObject(request, "/updateVerticalById", industry,
					user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			}

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/admin/vertical");
		return mv;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editVertical(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		LOGGER.debug("in updateVertical()");
		mv.setViewName("redirect:/admin/vertical");
		return mv;
	}

	@RequestMapping(value = "/clientMapping", method = RequestMethod.GET)
	public ModelAndView getClients(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session,
			@ModelAttribute("clientVerticalMappingForm") ClientVerticalMappingForm clientVerticalMappingForm,
			BindingResult result, Locale locale) {
		LOGGER.info("in clientVerticalMapping()");
		CommonUtils.setActiveScreenName("verticalclientMapping", session);
		try {
			mv.addObject("getExistingVerticals", getExistingVerticals(request));
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin:clientVerticalMapping");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/clientMapping", method = RequestMethod.POST)
	public ModelAndView getVerticals(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientVerticalMappingForm") ClientVerticalMappingForm clientVerticalMappingForm,
			RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {
		LOGGER.info("in clientVerticalMapping()");

		if (clientVerticalMappingForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.pleaseSelectClient", null, locale));
			mv.setViewName("redirect:/admin/vertical/clientMapping");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mv.addObject("getExistingVerticals", getExistingVerticals(request));
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getVerticalsByClientId/{clientId}",
					user.getUserId(), clientVerticalMappingForm.getClientId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				List<LinkedHashMap<String, Object>> data = (List<LinkedHashMap<String, Object>>) dataResponse
						.getObject();
				List<Industry> verticals = mapper.convertValue(data, new TypeReference<List<Industry>>() {
				});
				clientVerticalMappingForm.setVerticals(new ArrayList<>());
				List<String> selectedVerticalList = clientVerticalMappingForm.getVerticals();

				verticals.forEach(vertical -> {
					selectedVerticalList.add(vertical.getId() + "");
				});
				mv.addObject("verticaldetails", verticals);
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin:clientVerticalMapping");
		return mv;
	}

	@RequestMapping(value = "/clientMapping/save", method = RequestMethod.POST)
	public ModelAndView saveClientVerticalMapping(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv,
			@ModelAttribute("clientVerticalMappingForm") ClientVerticalMappingForm clientVerticalMappingForm,
			RedirectAttributes redirectAttributes, BindingResult result, Locale locale) {

		LOGGER.info("in saveClientVerticalMapping()");

		if (clientVerticalMappingForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.pleaseSelectClient", null, locale));
			mv.setViewName("redirect:/admin/vertical/clientMapping");
			return mv;
		}

		mv.setViewName("redirect:/admin/vertical/clientMapping");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ClientData clientData = new ClientData();
			clientData.setUserId(clientVerticalMappingForm.getClientId().toString());

			if (clientVerticalMappingForm.getVerticals() != null
					&& clientVerticalMappingForm.getVerticals().size() > 0) {
				List<Industry> industries = new ArrayList<>();
				clientVerticalMappingForm.getVerticals().forEach(verticalId -> {
					industries.add(new Industry(Integer.parseInt(verticalId), null));
				});
				clientData.setIndustries(industries);
			}

			DataResponse dataResponse = restUtilities.postRestObject(request, "/saveClientVerticalMapping", clientData,
					user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				clientVerticalMappingForm.setClientId(0);
			} else {
				mv.addObject("getExistingVerticals", getExistingVerticals(request));
				mv.addObject("messagecode", "ERROR");
				mv.addObject("errors", messageSource
						.getMessage("anvizent.message.error.text.errorWhileSavingClientVerticalMapping", null, locale));
				mv.setViewName("tiles-anvizent-admin:clientVerticalMapping");
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
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

	@SuppressWarnings("unchecked")
	public Map<Object, Object> getExistingVerticals(HttpServletRequest request) {
		List<Industry> vertical = null;
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getExistingVerticals", user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages()
				&& dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			List<LinkedHashMap<String, Object>> varticalResponse = (List<LinkedHashMap<String, Object>>) dataResponse
					.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			vertical = mapper.convertValue(varticalResponse, new TypeReference<List<Industry>>() {
			});
		} else {
			return new HashMap<>();
		}

		Map<Object, Object> verticalList = new LinkedHashMap<>();
		for (Industry industry : vertical) {
			if (industry.getIsActive()) {
				verticalList.put(industry.getId(), industry.getName());
			}

		}
		return verticalList;

	}

	@RequestMapping(value = "/clientMapping/save", method = RequestMethod.GET)
	public ModelAndView handleGetRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		mv.setViewName("redirect:/admin/vertical/clientMapping");
		return mv;
	}

}
