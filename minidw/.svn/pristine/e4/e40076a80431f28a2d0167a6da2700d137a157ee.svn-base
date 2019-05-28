package com.datamodel.anvizent.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.UserForm;
import com.datamodel.anvizent.spring.AppProperties;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Controller
@Import(AppProperties.class)
public class UserController {

	protected static final Log LOGGER = LogFactory.getLog(UserController.class);

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	/**
	 * 
	 * @param userForm
	 * @return
	 */
	@RequestMapping(value = "/userProfile", method = RequestMethod.GET)
	public ModelAndView userProfile(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("userForm") UserForm userForm, BindingResult result) {
		CommonUtils.setActiveScreenName("", session);
		User user = CommonUtils.getUserDetails(request, null, null);

		userForm.setClientId(user.getClientId());
		String userId = user.getUserId();
		if (userId.length() > 8) {
			try {
				userId = EncryptionServiceImpl.getInstance().decrypt(userId);
				String[] userIdAndLocale = StringUtils.split(userId, '#');
				userId = userIdAndLocale[0];
			} catch (Exception e) {
				System.out.println("exception occured " + e.getMessage());
			}
		}
		userForm.setUserId(userId);
		userForm.setUserName(user.getUserName());
		userForm.setRoleId(String.valueOf(user.getRoleId()));
		userForm.setRoleName(user.getRoleName());

		try {
			if ((user.getRoleId() != Constants.Role.SUPERADMIN) && (user.getRoleId() != Constants.Role.SUPERADMIN_ETLADMIN) ) {
				List<Industry> userIndustryList = new ArrayList<Industry>();
				List<String> clientSchemaList = new ArrayList<>();

				String clientSchemaName = restUtilities.getRestObject(request, "/getClientSchemaName", String.class, user.getUserId());
				clientSchemaList.add(clientSchemaName);

				userForm.setIndustries(userIndustryList);
				mv.addObject("schemaNames", clientSchemaList);
			}
			mv.setViewName("tiles-anvizent-entry:userprofile");
		} catch (HttpClientErrorException e) {
			LOGGER.error("in userProfile()", e);
			mv.addObject("errors", e.getMessage());
		}

		return mv;
	}

}
