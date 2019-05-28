package com.datamodel.anvizent.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebServiceILMapping;

/**
 * 
 * @author usharani.konda, apurva.deshmukh
 *
 */
@Controller
@RequestMapping(value = "/admin/webServiceILMapping")
public class WebserviceIlMapppingController {
	protected static final Log LOGGER = LogFactory.getLog(WebserviceIlMapppingController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getWSILDetails(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("webServiceILMapping") WebServiceILMapping webServiceILMapping, Locale locale) {
		LOGGER.debug("in getWSILMappingDetails()");
		CommonUtils.setActiveScreenName("webServiceILMapping", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				if (webServiceILMapping.getWsTemplateId() != null) {
					MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
					map.add("wsTemplateId", webServiceILMapping.getWsTemplateId());
					DataResponse dataResponse = restUtilities.postRestObject(request, "/getILsByWSMappingId", map, user.getUserId());
					if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						mv.addObject("iLInfoList", dataResponse.getObject());
						webServiceILMapping.setPageMode("list");
					}
				}
				mv.setViewName("tiles-anvizent-admin:webServiceILMapping");
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				e.printStackTrace();
			}
		}
		return mv;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView getWSApiMappingDetails(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("webServiceILMapping") WebServiceILMapping webServiceILMapping, Locale locale) {
		LOGGER.debug("in getWSApiMappingDetails()");
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
				map.add("wsTemplateId", webServiceILMapping.getWsTemplateId());
				DataResponse dataResponse = restUtilities.postRestObject(request, "/getILsByWSMappingId", map, user.getUserId());
				if (dataResponse != null && dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						mv.addObject("iLInfoList", dataResponse.getObject());
						webServiceILMapping.setPageMode("list");
					} else {
						mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
						mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					}
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				e.printStackTrace();
			}
		}
		mv.setViewName("tiles-anvizent-admin:webServiceILMapping");
		return mv;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editWSILMappingDetails(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("webServiceILMapping") WebServiceILMapping webServiceILMapping, Locale locale) {
		LOGGER.debug("in editWSILMappingDetails()");
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				if (webServiceILMapping.getWsTemplateId() != null && webServiceILMapping.getiLId() != null) {
					if (webServiceILMapping.getPageMode().equals("delete") && webServiceILMapping.getWsILMappingId() != null) {
						MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
						map.add("id", webServiceILMapping.getWsILMappingId());
						DataResponse dataResponse = restUtilities.postRestObject(request, "/deleteWSILMappingDetailsById", map, user.getUserId());
						if (dataResponse != null && dataResponse.getHasMessages()) {
							mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
							mv.addObject("errors", dataResponse.getMessages().get(0).getText());
						} else {
							mv.addObject("messagecode", "FAILED");
							mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
						}
					}

					MultiValueMap<String, Object> map1 = new LinkedMultiValueMap<>();
					map1.add("wsTemplateId", webServiceILMapping.getWsTemplateId());
					map1.add("iLId", webServiceILMapping.getiLId());
					DataResponse dataResponse = restUtilities.postRestObject(request, "/getWSILMappingDetailsById", map1, user.getUserId());
					if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						mv.addObject("mappingDetails", dataResponse.getObject());
					}
					webServiceILMapping.setWebServiceApis(new ArrayList<>());
					webServiceILMapping.setPageMode("mapping");
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				e.printStackTrace();
			}
		}
		mv.setViewName("tiles-anvizent-admin:webServiceILMapping");
		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveWebServiceILMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("webServiceILMapping") WebServiceILMapping webServiceILMapping, RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in saveWebServiceILMapping()");
		User user = CommonUtils.getUserDetails(request, null, null);
		mv.setViewName("redirect:/admin/webServiceILMapping");
		if (user != null) {
			try {
				if (webServiceILMapping.getWsTemplateId() != null && webServiceILMapping.getiLId() != null) {
					DataResponse dataResponse = restUtilities.postRestObject(request, "/saveWsILMappingDetails", webServiceILMapping, user.getUserId());
					if (dataResponse != null && dataResponse.getHasMessages()) {
						if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
							redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
							redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
							redirectAttributes.addFlashAttribute("webServiceILMapping", webServiceILMapping);
						} else {
							mv.setViewName("tiles-anvizent-admin:webServiceILMapping");
							MultiValueMap<String, Object> map1 = new LinkedMultiValueMap<>();
							map1.add("wsTemplateId", webServiceILMapping.getWsTemplateId());
							map1.add("iLId", webServiceILMapping.getiLId());
							DataResponse dataResponse1 = restUtilities.postRestObject(request, "/getWSILMappingDetailsById", map1, user.getUserId());
							if (dataResponse1 != null && dataResponse1.getMessages().get(0).getCode().equals("SUCCESS")) {
								mv.addObject("mappingDetails", dataResponse1.getObject());
							}
							webServiceILMapping.setWebServiceApis(new ArrayList<>());
							webServiceILMapping.setPageMode("mapping");

							mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
							mv.addObject("errors", dataResponse.getMessages().get(0).getText());
						}
					} else {
						webServiceILMapping.setWsTemplateId(0);
						redirectAttributes.addFlashAttribute("messagecode", "FAILED");
						redirectAttributes.addFlashAttribute("errors",
								messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
					}
				} else {
					webServiceILMapping.setWsTemplateId(0);
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				}
			} catch (Exception e) {
				webServiceILMapping.setWsTemplateId(0);
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				e.printStackTrace();
			}
		}
		return mv;
	}

	@RequestMapping(value = { "/save", "/edit" }, method = RequestMethod.GET)
	public ModelAndView redirectWSILMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		mv.setViewName("redirect:/admin/webServiceILMapping");
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


}
