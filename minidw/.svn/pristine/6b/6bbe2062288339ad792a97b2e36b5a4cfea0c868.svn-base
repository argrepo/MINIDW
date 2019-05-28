package com.datamodel.anvizent.controller.admin;

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
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.User;

@Controller
@RequestMapping(value = "/admin/elt/loadParameters")
public class ELTLoadParametersController {
	
	protected static final Log LOGGER = LogFactory.getLog(ELTLoadParametersController.class);

	@Autowired
	@Qualifier("eltRestTemplateUtilities")
	private RestTemplateUtilities eltRestUtilities;
	
	
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value = "/eltLoadParameters",method = RequestMethod.GET)
	public ModelAndView eltLoadParameters(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session,Locale locale) {
		CommonUtils.setActiveScreenName("eltLoadParameters", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = eltRestUtilities.getRestObject(request, "/getLoadParametersList", user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
				mv.addObject("eltLoadParamterList", dataResponse.getObject());
			} else {
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors",messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}
			
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("", e);
		}
		mv.setViewName("tiles-anvizent-admin:eltLoadParamters");
		return mv;
	}
	
}
