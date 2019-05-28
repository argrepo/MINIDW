package com.datamodel.anvizent.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.spring.AppProperties;

/**
 * 
 * @author rajesh.anthari
 *
 */
@Controller
@Import(AppProperties.class)
@RequestMapping(value = "/sadmin")
public class ClientSchedulerController {

	protected static final Log LOGGER = LogFactory.getLog(ClientSchedulerController.class);

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userRestUtilities;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getIlList(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) {
		CommonUtils.setActiveScreenName("scheduler", session);

		
		mv.setViewName("tiles-anvizent-admin:scheduleStatus");
		return mv;
	}


}
