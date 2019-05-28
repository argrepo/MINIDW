package com.datamodel.anvizent.controller.admin;

import java.util.LinkedHashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.ContextParameter;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author rajesh.anthari
 *
 */
@Controller
@RequestMapping(value = "/admin/contextparameters")
public class ContextParamController {
	protected static final Log LOGGER = LogFactory.getLog(ContextParamController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	private MessageSource messageSource;

	final String homeRedirectUrl = "redirect:/admin/contextparameters";
	final String homeTilesUrl = "tiles-anvizent-admin:contextParameters";

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView createContextParameter(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("contextParameterForm") ContextParameter contextParameterForm, ModelAndView mv, Locale locale) {
		LOGGER.debug("in getContextParameters()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getContextParameters", user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				mv.addObject("contextParams", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName(homeTilesUrl);
		contextParameterForm.setPageMode("list");
		return mv;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView getConnectorDetailsById(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("contextParameterForm") ContextParameter contextParameterForm, RedirectAttributes redirectAttributes, BindingResult result,
			Locale locale) {
		LOGGER.debug("in contextParameter()");
		if (contextParameterForm.getParamId() == null) {
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.error.text.parameterIdisShouldNotBeEmpty", null, locale));
			mv.setViewName(homeRedirectUrl);
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			contextParameterForm.setPageMode("edit");
			mv.setViewName(homeRedirectUrl);
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getContextParameterById/{id}", user.getUserId(),
					contextParameterForm.getParamId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> databaseobj = (LinkedHashMap<String, Object>) dataResponse.getObject();
					ContextParameter contextParameter = mapper.convertValue(databaseobj, new TypeReference<ContextParameter>() {
					});
					contextParameterForm.setParamId(contextParameter.getParamId());
					contextParameterForm.setParamName(contextParameter.getParamName());
					contextParameterForm.setParamval(contextParameter.getParamval());
					contextParameterForm.setIsActive(contextParameter.getIsActive());
					contextParameterForm.setDescription(contextParameter.getDescription());
					mv.setViewName(homeTilesUrl);

				} else {
					redirectAttributes.addFlashAttribute("messagecode", "FAILED");
					redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		return mv;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView connectorUpdate(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("contextParameterForm") ContextParameter contextParameterForm, RedirectAttributes redirectAttributes, BindingResult result,
			Locale locale) {
		LOGGER.debug("in contextParameter update ()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/updateContextParams", contextParameterForm, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName(homeRedirectUrl);
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView connectorAdd(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("contextParameterForm") ContextParameter contextParameterForm, RedirectAttributes redirectAttributes, BindingResult result,
			Locale locale) {
		LOGGER.debug("in contextParameter Add ()");
		contextParameterForm.setPageMode("add");
		mv.setViewName(homeTilesUrl);
		return mv;
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.GET)
	public ModelAndView redirect(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("contextParameterForm") ContextParameter contextParameterForm, RedirectAttributes redirectAttributes, BindingResult result,
			Locale locale) {
		LOGGER.debug("in contextParameter redirect ()");
		mv.setViewName(homeRedirectUrl);
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addConnector(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("contextParameterForm") ContextParameter contextParameterForm, RedirectAttributes redirectAttributes, BindingResult result,
			Locale locale) {
		LOGGER.debug("in contextParameter Add ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = restUtilities.postRestObject(request, "/createContextParams", contextParameterForm, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName(homeRedirectUrl);
		return mv;
	}

}
