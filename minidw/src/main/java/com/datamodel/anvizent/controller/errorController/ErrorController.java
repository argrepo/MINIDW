package com.datamodel.anvizent.controller.errorController;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.datamodel.anvizent.helper.RequestHelper;
import com.datamodel.anvizent.helper.SessionHelper;

/**
 * Controller for /error requests.
 * 
 * @author
 *
 */
@Controller
@RequestMapping("/errors")
public class ErrorController {
	protected static final Log log = LogFactory.getLog(ErrorController.class);
	private static final String ERROR = "javax.servlet.error.exception";

	@RequestMapping(value = "/500", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ModelAndView error500(Device device, HttpServletRequest request, ModelAndView mv) {
		RequestHelper.logAttributes(request);
		SessionHelper.logAttributes(request);
		Exception error = (Exception) request.getAttribute(ERROR);
		if (null != error) {
			log.error("Uncaught Exception: " + error.getMessage(), error);
			try {
				// send email alert to admin
			} catch (Exception ignored) {
			}
		}
		mv.setViewName("errors/500");
		return mv;
	}

	@RequestMapping(value = "/403", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ModelAndView error403(Device device, HttpServletRequest request, ModelAndView mv) {
		mv.setViewName("errors/403");
		return mv;
	}

	@RequestMapping(value = "/404", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ModelAndView error404(Device device, HttpServletRequest request, ModelAndView mv) {
		mv.setViewName("errors/404");
		return mv;
	}

	@RequestMapping(value = "/notauthenticated", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ModelAndView notAuthenticated(Device device, HttpServletRequest request, ModelAndView mv) {
		mv.setViewName("errors/notAuthenticated");
		return mv;
	}

	@RequestMapping(value = "/system_error", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ModelAndView systemError(Device device, HttpServletRequest request, ModelAndView mv) {
		mv.setViewName("errors/system_error");
		return mv;
	}
}
