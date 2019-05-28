
package com.datamodel.anvizent.controller.admin;

import java.util.LinkedHashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.AppDBVersionTableScripts;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Controller
@RequestMapping(value = "/admin/appDbVersionTableScripts")
public class AppDbVersionTableScriptController {
	protected static final Log LOGGER = LogFactory.getLog(AppDbVersionTableScriptController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	
	@Autowired
	@Qualifier("appDBVersionTableScriptRestTemplateUtilities")
	private RestTemplateUtilities appDBScriptsRefRestUtilities;
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView appDBVersionTableScripts(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("appDBVersionTableScripts") AppDBVersionTableScripts appDBVersionTableScripts,
			Locale locale) {
		LOGGER.debug("in appDBVersionTableScripts()");
		CommonUtils.setActiveScreenName("appDbVersionTableScripts", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			appDBVersionTableScripts.setPageMode("list");
			DataResponse dataResponse = appDBScriptsRefRestUtilities.getRestObject(request, "/getAppDbVersionTableScripts",
					user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
				mv.addObject("appDbVersionTableScriptsInfo", dataResponse.getObject());
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

		mv.setViewName("tiles-anvizent-admin:appDbVersionTableScripts");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView appDBVersionTableScriptsAdd(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("appDBVersionTableScripts") AppDBVersionTableScripts appDBVersionTableScripts,
			RedirectAttributes redirectAttributes) {
		LOGGER.debug("in appDBVersionTableScriptsAdd()");
		mv.setViewName("tiles-anvizent-admin:appDbVersionTableScripts");
		appDBVersionTableScripts.setPageMode("add");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addAppDBVersionTableScripts(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv, @ModelAttribute("appDBVersionTableScripts") AppDBVersionTableScripts appDBVersionTableScripts,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in addHybridClientsGrouping ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = appDBScriptsRefRestUtilities.postRestObject(request, "/saveAppDBVersionTableScripts",
					appDBVersionTableScripts, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			}
			mv.setViewName("redirect:/admin/appDbVersionTableScripts");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}

		return mv;
	}

	
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView getAppDbVersionTableScriptDetailsById(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv,@ModelAttribute("appDBVersionTableScripts") AppDBVersionTableScripts appDBVersionTableScripts,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in getAppDbVersionTableScriptDetailsById()");
		if (appDBVersionTableScripts.getId() == 0) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.versionIdisShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/appDbVersionTableScripts");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			mv.setViewName("redirect:/admin/appDbVersionTableScripts");
			DataResponse dataResponse = appDBScriptsRefRestUtilities.getRestObject(request, "/getAppDbVersionTableScriptDetailsById/{id}",
					user.getUserId(), appDBVersionTableScripts.getId());
			

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> appDbVersionScriptObj = (LinkedHashMap<String, Object>) dataResponse.getObject();
					AppDBVersionTableScripts appDbScriptsInfo = mapper.convertValue(appDbVersionScriptObj,
							new TypeReference<AppDBVersionTableScripts>() {
							});
					
					appDBVersionTableScripts.setVersion(appDbScriptsInfo.getVersion());
					/*appDBVersionTableScripts.setAppDbScript(appDbScriptsInfo.getAppDbScript());
					appDBVersionTableScripts.setMinidwScript(appDbScriptsInfo.getMinidwScript());*/
					appDBVersionTableScripts.setDefaultScript(appDbScriptsInfo.getDefaultScript());
					appDBVersionTableScripts.setPageMode("edit");
					mv.setViewName("tiles-anvizent-admin:appDbVersionTableScripts");
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
			e.printStackTrace();
		}
		return mv;
	}

}
