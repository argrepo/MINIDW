
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
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.MiddleLevelManager;
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
@RequestMapping(value = "/admin/middleLevelManager")
public class MLMController {
	protected static final Log LOGGER = LogFactory.getLog(MLMController.class);

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
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView middleLevelManager(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("middleLevelManager") MiddleLevelManager middleLevelManager, Locale locale) {
		LOGGER.debug("in middleLevelManager()");
		CommonUtils.setActiveScreenName("middleLevelManager", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			DataResponse dataResponse = restUtilities.getRestObject(request, "/getMiddleLevelManagerDetailsById",user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					@SuppressWarnings("unchecked")
					LinkedHashMap<String, Object> middleObj = (LinkedHashMap<String, Object>) dataResponse.getObject();
					MiddleLevelManager middleLevelManagerInfo = mapper.convertValue(middleObj,
							new TypeReference<MiddleLevelManager>() {
							});
					middleLevelManager.setId(middleLevelManagerInfo.getId());
					middleLevelManager.setContextPath(middleLevelManagerInfo.getContextPath());
					middleLevelManager.setUploadListEndPoint(middleLevelManagerInfo.getUploadListEndPoint());
					middleLevelManager.setWriteEndPoint(middleLevelManagerInfo.getWriteEndPoint());
					middleLevelManager.setDeleteEndPoint(middleLevelManagerInfo.getDeleteEndPoint());
					middleLevelManager.setUpgradeEndPoint(middleLevelManagerInfo.getUpgradeEndPoint());
					middleLevelManager.setUserAuthToken(middleLevelManagerInfo.getUserAuthToken());
					middleLevelManager.setClientAuthToken(middleLevelManagerInfo.getClientAuthToken());
					middleLevelManager.setEncryptionPrivateKey(middleLevelManagerInfo.getEncryptionPrivateKey());
					middleLevelManager.setEncryptionIV(middleLevelManagerInfo.getEncryptionIV());
					
					mv.setViewName("tiles-anvizent-admin:middleLevelManager");
				} 
			} 

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		mv.setViewName("tiles-anvizent-admin:middleLevelManager");
		return mv;
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView addMiddleLevelManager(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("middleLevelManager") MiddleLevelManager middleLevelManager, RedirectAttributes redirectAttributes,
			Locale locale) {
		LOGGER.debug("in addVersionUpgrade ()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilities.postRestObject(request, "/saveMiddleLevelManagerInfo", middleLevelManager,user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			}
			mv.setViewName("redirect:/admin/middleLevelManager");
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}

		return mv;
	}


}
