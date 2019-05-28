package com.datamodel.anvizent.controller.admin;

import java.util.HashMap;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Kpi;
import com.datamodel.anvizent.service.model.User;

/**
 * 
 * @author apurva.deshmukh
 *
 */
@Controller
@RequestMapping(value = "/admin/kpi")
public class KpiController {
	protected static final Log LOGGER = LogFactory.getLog(KpiController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getExistingkpis(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale) {
		LOGGER.debug("in getExistingkpis()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			CommonUtils.setActiveScreenName("kpi", session);
			DataResponse dataResponse = restUtilities.getRestObject(request, "/getExistingkpis", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("kpis", dataResponse.getObject());
				} else {
					mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
					mv.addObject("kpis", dataResponse.getObject());
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
		mv.setViewName("tiles-anvizent-admin:kpi");
		return mv;
	}

	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public ModelAndView createNewKpi(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @ModelAttribute("Kpi") Kpi kpi,
			BindingResult result, Locale locale, RedirectAttributes redirectAttributes) {
		LOGGER.debug("in createNewKpi()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			/* Default status if exception occurs */
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));

			DataResponse dataResponse = restUtilities.postRestObject(request, "/createNewKpi", kpi, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/admin/kpi");
		return mv;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView createNewKpi(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		LOGGER.debug("in createNewKpi()");
		mv.setViewName("redirect:/admin/kpi");
		return mv;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editkpi(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @ModelAttribute("Kpi") Kpi kpi, BindingResult result,
			Locale locale, RedirectAttributes redirectAttributes) {
		LOGGER.debug("in updateKpi()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			/* Default status if exception occurs */
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));

			DataResponse dataResponse = restUtilities.postRestObject(request, "/updateKpiById", kpi, user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.setViewName("redirect:/admin/kpi");
		return mv;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editkpi(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		LOGGER.debug("in updateKpi()");
		mv.setViewName("redirect:/admin/kpi");
		return mv;
	}

	@RequestMapping(value = "/dlMapping", method = RequestMethod.GET)
	public ModelAndView dlkPIMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, Locale locale) {
		LOGGER.info("in dlkPIMapping()");
		User user = CommonUtils.getUserDetails(request, null, null);
		if (user != null) {
			try {
				Map<String, Object> modelObject = new HashMap<String, Object>();

				DataResponse dataResponse = restUtilities.getRestObject(request, "/getDlInfo", user.getUserId());
				if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					modelObject.put("dLList", dataResponse.getObject());
				}

				DataResponse dataResponse1 = restUtilities.getRestObject(request, "/getAllKpis", user.getUserId());
				if (dataResponse1 != null && dataResponse1.getHasMessages() && dataResponse1.getMessages().get(0).getCode().equals("SUCCESS")) {
					modelObject.put("KpiList", dataResponse1.getObject());
				}

				mv.addObject("modelObject", modelObject);

			} catch (Exception e) {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
				e.printStackTrace();
			}
		}
		mv.setViewName("tiles-anvizent-admin:dlKpiMapping");
		return mv;
	}

}
