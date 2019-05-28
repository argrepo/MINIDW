package com.datamodel.anvizent.controller;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;

@Controller
@RequestMapping(value = "/adt/currencyLoad")
public class CurrencyLoadController {

	protected static final Log LOGGER = LogFactory.getLog(CurrencyLoadController.class);

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	
	@Autowired
	private MessageSource messageSource;
 
	@RequestMapping( method = RequestMethod.GET)
	public ModelAndView standardPackage(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			ModelAndView mv, Locale locale) {
		try {
			CommonUtils.setActiveScreenName("currency", session);
			mv.addObject("isStandard", true);
			mv.setViewName("tiles-anvizent-entry:currencyLoad");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		return mv;
	}
	
	
	
	
	
}
