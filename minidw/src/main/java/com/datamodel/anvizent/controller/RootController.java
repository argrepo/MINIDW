package com.datamodel.anvizent.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Import;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;

/**
 * Controller for / requests.
 * @author
 */
@Controller
@Import(AppProperties.class)
public class RootController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Log LOG = LogFactory.getLog(RootController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView user(Device device, HttpServletRequest request, ModelAndView mv) {
		try {
			User user = CommonUtils.getUserDetails(request, null, null);
			if (user != null) {
				int roleId = user.getRoleId();
				if (roleId == Constants.Role.SUPERADMIN || roleId == Constants.Role.SUPERADMIN_ETLADMIN) {
					mv.setViewName("redirect:/admin/ETLAdmin");
				} else if (roleId == Constants.Role.SCHEDULER_ADMIN) {
					mv.setViewName("redirect:/sadmin");
				} else {
					mv.setViewName("redirect:/adt/standardpackage");
				} 
			} else {
				mv.setViewName("redirect:/login");
			}
		} catch (Exception e) {
			e.printStackTrace();
			mv.setViewName("redirect:/errors/500");
			return mv;
		}

		return mv;
	}
	@RequestMapping(value = "/applicationstatus", method = RequestMethod.GET)
	@ResponseBody
	public DataResponse user(HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		dataResponse.addMessage(new Message("SUCCESS"));
		return dataResponse;
	}
	
	@RequestMapping(value = {"/adt", "/adt/package"}, method = RequestMethod.GET)
	public ModelAndView standardPackage(Device device, HttpServletRequest request, ModelAndView mv) {
		mv.setViewName("redirect:/adt/standardpackage");
		return mv;
	}
}
