package com.datamodel.anvizent.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anvizent.client.scheduler.constant.Constant;
import com.anvizent.client.scheduler.listner.ClientScheduler;
import com.anvizent.client.scheduler.util.PropertiesPlaceHolder;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.CheckServerURLStatus;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.AESConverter;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.admin.SaveProperties;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.HybridClientsGrouping;
import com.datamodel.anvizent.service.model.LoginForm;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Authentication controller for login and logout.
 * 
 * @author Rajesh.Anthari
 *
 */
@Import(AppProperties.class)
@Controller
public class AuthenticationController
{
	protected final static Log LOG = LogFactory.getLog(AuthenticationController.class);

	private @Value("${deployment.type:cloud}") String clientAdminContextPath;
	
	private @Value("${service.contextPath:}") String contextPath;
	
	private @Value("${endpoint.urls:}") String endPointUrls;

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userRestUtilities;

	@Autowired
	@Qualifier("anvizentServiceslbRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("anvizentServiceslbRestTemplateUtilities")
	private RestTemplateUtilities anvizentcorews;

	@Autowired
	@Qualifier("loginServicesRestTemplateUtilities")
	private RestTemplateUtilities loginServicesRestTemplateUtilities;

	@Autowired
	ClientScheduler clientScheduler;
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restTemplateUtilities;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/securityCheck", method = RequestMethod.GET)
	public ModelAndView securityCheck(@RequestParam(value = "error", required = false) boolean error, ModelAndView mv, HttpServletRequest request, Locale locale, @ModelAttribute("loginForm") LoginForm loginForm, BindingResult result)
	{
		mv.setViewName("redirect:/login");
		return mv;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView getLoginPage(@RequestParam(value = "error", required = false) boolean error, ModelAndView mv, HttpServletRequest request, Locale locale, @ModelAttribute("loginForm") LoginForm loginForm, Locale defaultLocale, RedirectAttributes redirectAttributes, BindingResult result,
			HttpServletResponse response)
	{

		User user = CommonUtils.getUserDetails(request, null, null);
		if( user != null )
		{
			int roleId = user.getRoleId();
			if(  roleId == Constants.Role.SUPERADMIN || roleId == Constants.Role.SUPERADMIN_ETLADMIN )
			{
				mv.setViewName("redirect:/admin/ETLAdmin");
			}
			else
			{
				mv.setViewName("redirect:/adt/standardpackage");
			}
			return mv;
		}

		if( !preLoginCheckings(redirectAttributes, request, mv) )
		{
			return mv;
		}
		mv.addObject("action", "showLoginForm");
		mv.setViewName("auth/login");
		return mv;
	}

	public boolean preLoginCheckings(RedirectAttributes redirectAttributes, HttpServletRequest request, ModelAndView mv)
	{
		mv.setViewName("redirect:/unableToLoggedIn");
		String deploymentMessages = (String) request.getServletContext().getAttribute(Constants.Config.DEPLOYMENT_TYPE_MESSAGE);
		if( StringUtils.isNotBlank(deploymentMessages) )
		{
			redirectAttributes.addFlashAttribute("errors", deploymentMessages);
			return false;
		}

		boolean isWebApp = Boolean.parseBoolean(request.getServletContext().getAttribute("isWebApp").toString());
		DataResponse dataResponse = loginServicesRestTemplateUtilities.getRestObject(request, "/latestMinidwVesrion");
		if( dataResponse != null && dataResponse.getHasMessages() )
		{
			if( dataResponse.getMessages().get(0).getCode().equals("SUCCESS") )
			{
				String cloudVersion = dataResponse.getObject().toString().trim();
				String localVersion = (String) request.getServletContext().getAttribute("version");
				if( !isWebApp )
				{
					if( !StringUtils.equals(cloudVersion, localVersion) )
					{
						redirectAttributes.addFlashAttribute("errors", "Version mismatch with cloud minidw");
						return false;
					}
					if( StringUtils.isNotBlank(clientScheduler.getPropertiesPlaceHolder().getAccessKey()) )
					{
						try
						{
							EncryptionServiceImpl.getInstance().decrypt(clientScheduler.getPropertiesPlaceHolder().getAccessKey());
						}
						catch ( Exception e )
						{
							redirectAttributes.addFlashAttribute("errors", "Invalid client access code");
							return false;
						}
					}
					else
					{
						redirectAttributes.addFlashAttribute("action", "showClientAccessForm");
						mv.setViewName("redirect:/authentication");
						return false;
					}
				}

			}
			else
			{
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				return false;
			}
		}
		else
		{
			redirectAttributes.addFlashAttribute("errors", "Unable to connect minidw services. Please contact admin");
			return false;
		}

		return true;
	}

	@RequestMapping(value = "/loginOld", method = RequestMethod.GET)
	public ModelAndView getLoginPageOld(@RequestParam(value = "error", required = false) boolean error, ModelAndView mv, HttpServletRequest request, Locale locale, @ModelAttribute("loginForm") LoginForm loginForm, RedirectAttributes redirectAttributes, BindingResult result,
			HttpServletResponse response)
	{
		try
		{
			Boolean isWebApp = Boolean.parseBoolean(request.getServletContext().getAttribute("isWebApp").toString());
			User user = CommonUtils.getUserDetails(request, null, null);

			if( user != null )
			{
				int roleId = user.getRoleId();
				if( roleId == Constants.Role.SUPERADMIN || roleId == Constants.Role.SUPERADMIN_ETLADMIN )
				{
					mv.setViewName("redirect:/admin/ETLAdmin");
				}
				else
				{
					mv.setViewName("redirect:/adt/standardpackage");
				}
			}
			else
			{
				try
				{
					if( !isWebApp )
					{
						PropertiesPlaceHolder properties = clientScheduler.getPropertiesPlaceHolder();
						if( StringUtils.isEmpty(properties.getAccessKey()) )
						{
							/* shutdown if already started */
							if( clientScheduler.isSchedulerRunning() )
							{
								LOG.info("scheduler stopped");
								clientScheduler.stopScheduler();
							}
							redirectAttributes.addFlashAttribute("action", "showClientAccessForm");
							mv.setViewName("redirect:/authentication");
							return mv;
						}
					}

				}
				catch ( Exception e )
				{
					LOG.error("", e);
					redirectAttributes.addFlashAttribute("errors", "Unable to setup environment. Please contact admin");
					mv.setViewName("redirect:/unableToLoggedIn");
					return mv;
				}

				mv.addObject("action", "showLoginForm");
				mv.setViewName("auth/login");
			}
		}
		catch ( Exception e )
		{
			LOG.error("", e);
			mv.setViewName("redirect:/errors/500");
			return mv;
		}
		return mv;
	}

	@RequestMapping(value = "/authentication", method = RequestMethod.GET)
	public ModelAndView authenticateClient(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, @ModelAttribute("loginForm") LoginForm loginForm, Locale locale)
	{
		LOG.debug("in authentication()");

		mv.setViewName("auth/clientAuthentication");
		return mv;
	}

	@SuppressWarnings({ "unchecked" })
	@RequestMapping(value = "/authentication", method = RequestMethod.POST)
	public ModelAndView authenticateClientAccess(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, @ModelAttribute("loginForm") LoginForm loginForm, Locale locale, RedirectAttributes redirectAttributes)
	{
		LOG.debug("in authentication()");

		if( StringUtils.isNotEmpty(loginForm.getClientId()) )
		{
			try
			{
				String clientId = loginForm.getClientId();

				String getHybridGroupDetailsUrl = "/getHybridClientGroupingDetailsByAccessKey?accessKey={accessKey}";
				DataResponse dataResponseGetClientDetail = loginServicesRestTemplateUtilities.postRestObject(request, getHybridGroupDetailsUrl, new LinkedMultiValueMap<>(), DataResponse.class, clientId);

				if( dataResponseGetClientDetail != null )
				{

					if( dataResponseGetClientDetail.getMessages().get(0).getCode().equals("SUCCESS") )
					{

						LinkedHashMap<String, Object> clientDetails = (LinkedHashMap<String, Object>) dataResponseGetClientDetail.getObject();
						ObjectMapper mapper1 = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						HybridClientsGrouping hybridClientsGrouping = mapper1.convertValue(clientDetails, new TypeReference<HybridClientsGrouping>()
						{
						});

						if( !hybridClientsGrouping.isActive() )
						{
							redirectAttributes.addFlashAttribute("loginForm", loginForm);
							redirectAttributes.addFlashAttribute("action", "showClientAccessForm");
							redirectAttributes.addFlashAttribute("loginError", "Group not activated yet");
							mv.setViewName("redirect:/authentication");
							return mv;
						}

						redirectAttributes.addFlashAttribute("hybridClientsGrouping", hybridClientsGrouping);
						mv.setViewName("redirect:/configurationSettings");
						return mv;
					}
					else
					{
						redirectAttributes.addFlashAttribute("loginForm", loginForm);
						redirectAttributes.addFlashAttribute("action", "showClientAccessForm");
						redirectAttributes.addFlashAttribute("loginError", dataResponseGetClientDetail.getMessages().get(0).getText());
						mv.setViewName("redirect:/authentication");
						return mv;
					}
				}
				else
				{
					redirectAttributes.addFlashAttribute("loginForm", loginForm);
					redirectAttributes.addFlashAttribute("action", "showClientAccessForm");
					redirectAttributes.addFlashAttribute("loginError", "Unable to validate the access key");
					mv.setViewName("redirect:/authentication");
					return mv;
				}
			}
			catch ( Exception e )
			{
				LOG.error("", e);
			}
		}

		redirectAttributes.addFlashAttribute("loginForm", loginForm);
		redirectAttributes.addFlashAttribute("action", "showClientAccessForm");
		redirectAttributes.addFlashAttribute("loginError", "Invalid access Key");
		mv.setViewName("redirect:/authentication");
		return mv;

	}

	@RequestMapping(value = "/configurationSettings", method = RequestMethod.GET)
	public ModelAndView clientConfigurationSettings(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, @ModelAttribute("hybridClientsGrouping") HybridClientsGrouping hybridClientsGrouping, Locale locale)
	{
		LOG.debug("in configurationSettings()");
		mv.setViewName("auth/clientConfigurationSettings");
		return mv;

	}

	@RequestMapping(value = "/configurationSettings", method = RequestMethod.POST)
	public ModelAndView saveConfigurationSettings(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, @ModelAttribute("hybridClientsGrouping") HybridClientsGrouping hybridClientsGrouping, Locale locale, RedirectAttributes redirectAttributes)
	{
		LOG.debug("in saving configurationSettings()");

		try
		{
			String accessKey = hybridClientsGrouping.getAccessKey();
			SaveProperties.saveAccessKey(EncryptionServiceImpl.getInstance().encrypt(accessKey));

			clientScheduler.refreshProperties();
			// clientScheduler.startScheduler();
			redirectAttributes.addFlashAttribute("showSuccess", "Successfully Registered");
			mv.setViewName("redirect:/login");
			return mv;
		}
		catch ( Exception e )
		{
			redirectAttributes.addFlashAttribute("loginError", MinidwServiceUtil.getErrorMessageString(e));
		}

		redirectAttributes.addFlashAttribute("loginError", "Unable to configure client");
		mv.setViewName("redirect:/authentication");
		return mv;

	}

	@RequestMapping(value = "/unableToLoggedIn", method = RequestMethod.GET)
	public ModelAndView alreadyLoggedIn(@RequestParam(value = "error", required = false) boolean error, ModelAndView mv, HttpServletRequest request, Locale locale, @ModelAttribute("loginForm") LoginForm loginForm, BindingResult result)
	{
		// mv.addObject("hideLoginScreen", true);
		mv.setViewName("errorunableToLoggedIn");
		return mv;
	}

	public String getHost(HttpServletRequest request)
	{
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String resultPath = scheme + "://" + serverName + ":" + serverPort;
		return resultPath;

	}

	/**
	 * user login request
	 * 
	 * 
	 * @param request
	 * @param response
	 * @param mv
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/securityCheck", method = RequestMethod.POST)
	public ModelAndView getLoginPage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @ModelAttribute("loginForm") @Valid LoginForm loginForm, BindingResult result, Locale defaultLocale, RedirectAttributes redirectAttributes)
	{

		Boolean isWebApp = true;
		Boolean.parseBoolean(request.getServletContext().getAttribute("isWebApp").toString());
		String timeZone = request.getParameter("timeZone");
		LOG.debug("in securityCheck()");
		if( result.hasErrors() )
		{
			mv.addObject("action", "showLoginForm");
			mv.setViewName("auth/login");
			return mv;
		}
		String username = loginForm.getUsername();
		String password = loginForm.getPassword();
		String clientID = loginForm.getClientId();
		String lcoale = loginForm.getLocale();
		String accessKey = null;
		try
		{
			accessKey = EncryptionServiceImpl.getInstance().decrypt(clientScheduler.getPropertiesPlaceHolder().getAccessKey());
		}
		catch ( Exception exe )
		{
			redirectAttributes.addFlashAttribute("errors", "client id not found.");
			mv.setViewName("redirect:/login");
			return mv;
		}

		// create a method which accepts .... and will retunr modelandview
		// return callMethiod();

		try
		{
			return securityCheckProcess(request, response, isWebApp, redirectAttributes, mv, username, password, clientID, accessKey, timeZone, "login", lcoale,1);
		}
		catch ( Throwable e )
		{
			redirectAttributes.addFlashAttribute("errors", "Unable to setup environment. Please contact admin");
			mv.setViewName("redirect:/login");
			request.getSession().invalidate();
			LOG.error("", e);
			return mv;
		}

	}

	@SuppressWarnings("unchecked")
	private ModelAndView securityCheckProcess(HttpServletRequest request, HttpServletResponse response, Boolean isWebApp, RedirectAttributes redirectAttributes, ModelAndView mv, String username, String password, String clientID, String accessKey, String timeZone, String sourceFrom, String locale,Integer decryptedAppType)
	{

		if( StringUtils.isBlank(locale) )
		{
			locale = "en_US";
		}
		if( timeZone == null )
		{
			timeZone = TimeZone.getDefault().getID();
		}
		if( !isWebApp )
		{
			try
			{
				PropertiesPlaceHolder properties = clientScheduler.getPropertiesPlaceHolder();

				if( StringUtils.isEmpty(properties.getAccessKey()) )
				{
					redirectAttributes.addFlashAttribute("errors", "Unable to setup environment. Please contact admin");
					mv.setViewName("redirect:/unableToLoggedIn");
					return mv;
				}
			}
			catch ( Exception e )
			{
				LOG.error("", e);
				redirectAttributes.addFlashAttribute("errors", "Unable to setup environment. Please contact admin");
				mv.setViewName("redirect:/unableToLoggedIn");
				return mv;
			}
		}
		boolean isSchedulerLogin = false;
		if( clientID != null && clientID.equals("schedulers") )
		{
			isSchedulerLogin = true;
			clientID = "-1";
			accessKey = null;
		}
		String status = "true";
		try {
			if(clientAdminContextPath.equalsIgnoreCase("hybrid")) {
				Map<String, HashMap<String,String>> urlsMap =	CheckServerURLStatus.checkConnectionUrls(endPointUrls);
				Set<String> urls =  urlsMap != null ? urlsMap.keySet() : new HashSet<>();
				for (String url : urls) {
					HashMap<String, String> map = urlsMap.get(url);
					status = map.get("status");
					if(status.equals("false")) {
						redirectAttributes.addFlashAttribute("errors", url+", Not reachable Please contact Admin.."/*" "+map.get("msg")*/);
						mv.setViewName("redirect:/login");
						break;
					}
				}
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		if(status.equals("true"))
		{
		User user = new User();
		user.setUserName(username);
		user.setPassword(password);
		user.setClientId(clientID);
		user.setAccessKey(accessKey);
		DataResponse dataResponse = loginServicesRestTemplateUtilities.postRestObject(request, "/authetincateUserCredentials", user);
		DataResponse dataResponseEltUrl = loginServicesRestTemplateUtilities.getRestObject(request,"/getELTUrl");
		if( dataResponse != null && dataResponse.getHasMessages() )
		{
			Message respMessage = dataResponse.getMessages().get(0);
			if( respMessage.getCode().equals(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS) )
			{
				request.getSession(true);

				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				User userInfo = mapper.convertValue(map, new TypeReference<User>()
				{
				});

				SessionHelper.setSesionAttribute(request, Constants.Config.TIME_ZONE, timeZone);
				if( StringUtils.isNotBlank(userInfo.getAccessKey()) )
				{
					SessionHelper.setSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL, userInfo.getAccessKey());
				}
				else
				{
					SessionHelper.setSesionAttribute(request, Constants.Config.WEBSERVICE_CONTEXT_URL, contextPath);
				}
				userInfo.setMobileNo(userInfo.getUserId());
				// SessionHelper.setSesionAttribute(request,
				// Constants.Config.WEBSERVICE_CONTEXT_URL, contextPath);
				SessionHelper.setSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID, userInfo.getClientId());
				SessionHelper.setSesionAttribute(request, Constants.Config.HEADER_USER_CLIENT_ID, userInfo.getClientId());
				SessionHelper.setSesionAttribute(request, Constants.Config.DEPLOYMENT_TYPE, userInfo.getCloudClient().getDeploymentType());
				SessionHelper.setSesionAttribute(request, "selectedLocale", locale);
				try
				{
					SessionHelper.setSesionAttribute(request, Constant.General.CLIENT_ID, EncryptionServiceImpl.getInstance().encrypt(clientID));
				}
				catch ( Exception e1 )
				{
					e1.printStackTrace();
				}

				if( userInfo.getS3BucketInfo() != null )
				{
					SessionHelper.setSesionAttribute(request, Constants.Config.S3_BUCKET_INFO, userInfo.getS3BucketInfo());
				}
				if( userInfo.getFileSettings() != null )
				{
					SessionHelper.setSesionAttribute(request, Constants.Config.FILE_SETTINGS_INFO, userInfo.getFileSettings());
				}

				// String locale = "en_US";
				if( StringUtils.isNotBlank(locale) && !locale.equalsIgnoreCase("null") )
				{
					if( StringUtils.contains(locale, "_") )
					{
						String loc[] = locale.split("_");
						SessionLocaleResolver resolver = new SessionLocaleResolver();
						resolver.setLocale(request, response, new Locale(loc[0], loc[1]));
					}
					else
					{
						SessionLocaleResolver resolver = new SessionLocaleResolver();
						resolver.setLocale(request, response, new Locale(locale));
					}

				}
				else
				{
					locale = "";
				}

				try
				{
					userInfo.setUserId(EncryptionServiceImpl.getInstance().encrypt(userInfo.getUserId() + "#" + locale + "#" + new Date()));
					addUserCookie(request, response, userInfo.getUserId());
				}
				catch ( Exception e )
				{
					LOG.error("", e);
				}
				SessionHelper.setSesionAttribute(request, "tableLevelAccessUrl", clientAdminContextPath);

				if( userInfo.getRoleId() == Constants.Role.SUPERADMIN || userInfo.getRoleId() == Constants.Role.SUPERADMIN_ETLADMIN || userInfo.getRoleId() == 1 )
				{
					if( isSchedulerLogin )
					{
						userInfo.setRoleId(-300);
						mv.setViewName("redirect:/sadmin");
					}
					else
					{
						mv.setViewName("redirect:/admin/ETLAdmin");
					}
				}
				else
				{
					String encryptedAuthToken;
					try {
						encryptedAuthToken = AESConverter.encrypt(user.getUserName()+"#$#"+user.getPassword()+"#$#"+user.getClientId()+"#$#"+new Date());
						String endPointEltUrl = dataResponseEltUrl.getObject()+"#/login.html?auth_token="+URLEncoder.encode(encryptedAuthToken, "UTF-8");;
						SessionHelper.setSesionAttribute(request, "ELTURL", endPointEltUrl);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					/*DataResponse iLresponse = restTemplateUtilities.getRestObject(request, "/getUserILConnections", userInfo.getUserId());
					
					if (iLresponse != null && iLresponse.getHasMessages()) {
						if (iLresponse.getMessages().get(0).getCode().equals("SUCCESS")) {

							mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							List<?> list = (List<Map<String, Object>>) iLresponse.getObject();
							List<ILConnection> iLConnectionList = mapper.convertValue(list, new TypeReference<List<ILConnection>>() {
							});
							if (iLConnectionList != null && !iLConnectionList.isEmpty()) {
								if(decryptedAppType == 1 || decryptedAppType == 0)
								{
									mv.setViewName("redirect:/adt/standardpackage");
								}
								else if(decryptedAppType == 2)
								{
									mv.setViewName("redirect:/adt/package/ai/businessModels");
								}
							}else {
								mv.setViewName("redirect:/adt/package/databaseNewConnection");
							}
						} 

					}*/
					
				if(decryptedAppType == 1 || decryptedAppType == 0)
					{
						mv.setViewName("redirect:/adt/standardpackage");
					}
					else if(decryptedAppType == 2)
					{
						mv.setViewName("redirect:/adt/package/ai/businessModels");
					}
				}
				SessionHelper.setSesionAttribute(request, "appType", decryptedAppType);
				SessionHelper.setSesionAttribute(request, "principal", userInfo);
			}
			else
			{

				if( sourceFrom.equals("auto") )
				{
					redirectAttributes.addFlashAttribute("errors", respMessage.getText());
					mv.setViewName("redirect:/unableToLoggedIn");
				}
				else
				{
					redirectAttributes.addFlashAttribute("loginError", respMessage.getText());
					redirectAttributes.addFlashAttribute("action", "showLoginForm");
					mv.setViewName("redirect:/login");

				}
			}
		}
	}
		return mv;

	}

	private void addUserCookie(HttpServletRequest request, HttpServletResponse response, String userId) throws Exception
	{
		Cookie userCookie = new Cookie(Constants.Config.USER_COOKIE_NAME + StringUtils.replace(StringUtils.replace(EncryptionServiceImpl.getInstance().encrypt(userId).toUpperCase(), "_", ""), "-", ""), EncryptionServiceImpl.getInstance().encrypt(userId + "#" + new Date() + "#" + userId));
		userCookie.setHttpOnly(true);
		userCookie.setSecure(request.isSecure());
		userCookie.setMaxAge(-1);
		userCookie.setPath(request.getContextPath());
		response.addCookie(userCookie);
	}

	/**
	 * 
	 * login from anvizent client admin
	 * 
	 * @param request
	 * @param response
	 * @param mv
	 * @param clientId
	 * @param user_Id
	 * @param username
	 * @param schemaName
	 * @return
	 */
	@RequestMapping(value = "/autoLogin", method = RequestMethod.GET)
	public ModelAndView autoLogin(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, 
			@RequestParam("auth_token") String auth_token,
			@RequestParam(value = "locale", required = false) String locale,
			@RequestParam(value = "app", required = false) String app,
			RedirectAttributes redirectAttributes)
	{

		LOG.debug("in autoLogin()...token :" + auth_token + ", locale :" + locale);

		String decryptedToken = "";
		Integer decryptedAppType;

		try
		{
			decryptedToken = AESConverter.decrypt(auth_token);
			if(app == null)
			{
				decryptedAppType = 0;
			}
			else
			{
				decryptedAppType =Integer.parseInt(AESConverter.decrypt(app));
				if(decryptedAppType !=0 && decryptedAppType != 1 && decryptedAppType != 2)
				{
					redirectAttributes.addFlashAttribute("errors", "Invalid authentication details");
					mv.setViewName("redirect:/unableToLoggedIn");
					return mv;
				}
			}
		}
		catch ( Exception e )
		{
			LOG.error("", e);
			redirectAttributes.addFlashAttribute("errors", "Invalid authentication details");
			mv.setViewName("redirect:/unableToLoggedIn");
			return mv;
		}

		String[] credentials = StringUtils.split(decryptedToken, "#$#");
		if( credentials != null && credentials.length == 4 )
		{

			String userName = credentials[0];
			String password = credentials[1];
			String clientId = credentials[2];
			Boolean isWebApp = Boolean.parseBoolean(request.getServletContext().getAttribute("isWebApp").toString());
			
			return securityCheckProcess(request, response, isWebApp, redirectAttributes, mv, userName, password, clientId, null, request.getParameter("time_zone"), "auto", locale, decryptedAppType);
		}
		else
		{
			redirectAttributes.addFlashAttribute("errors", "Invalid authentication params");
			mv.setViewName("redirect:/unableToLoggedIn");
			return mv;
		}

	}

	/**
	 * Handles session expired through JavaScript setInterval().
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/sessionExpired", method = RequestMethod.GET)
	public String sessionExpired(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Session expired.");
		try
		{
			logoutAuthentication(request, response);
		}
		catch ( Exception ex )
		{
			LOG.error("Unable to logout.", ex);
		}
		return "redirect:/login";
	}

	private void logoutAuthentication(HttpServletRequest request, HttpServletResponse response)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if( auth != null && auth.isAuthenticated() )
		{
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
	}

	/**
	 * Handles and retrieves the denied JSP page. This is shown whenever a
	 * regular user tries to access an admin only page.
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
	public String getDeniedPage()
	{
		LOG.debug("Received request to show denied page");

		return "tiles-anvizent-entry:accessDenied";
	}

	/**
	 * user logout
	 * 
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, ModelMap model, RedirectAttributes redirectAttributes, Locale locale)
	{

		User user = CommonUtils.getUserDetails(request, null, null);
		invalidateUserToken(request, response);
		if( user != null )
		{
			request.getSession().invalidate();
			redirectAttributes.addFlashAttribute("sessionTimeOutMessage", "Successfully logged out");
		}
		else
		{
			redirectAttributes.addFlashAttribute("sessionTimeOutMessage", "Session Expired");
		}
		logoutAuthentication(request, response);
		return "redirect:/login";
	}

	private void invalidateUserToken(HttpServletRequest request, HttpServletResponse response)
	{
		Cookie[] cookies = request.getCookies();
		if( cookies != null && cookies.length > 0 )
		{
			for (Cookie cookie : cookies)
			{
				if( cookie.getName().startsWith(Constants.Config.USER_COOKIE_NAME) )
				{
					cookie.setMaxAge(0);
					cookie.setValue("");
					response.addCookie(cookie);
				}
			}
		}

	}

	@RequestMapping(value = "/sessionTimeOut", method = RequestMethod.GET)
	public String sessionTimeOut(HttpServletRequest request, HttpServletResponse response, ModelMap model, RedirectAttributes redirectAttributes, Locale locale)
	{

		SessionHelper.setSesionAttribute(request, "principal", null);
		invalidateUserToken(request, response);
		logoutAuthentication(request, response);
		redirectAttributes.addFlashAttribute("sessionTimeOutMessage", "Session Expired");
		return "redirect:/login";
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> refreshSessionURL(HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
	{
		Map<String, String> data = new HashMap<>();
		data.put("refreshed", "true");
		return data;
	}

	@RequestMapping(value = "/packageExecutorExceptionInfo", method = RequestMethod.GET)
	public ModelAndView packageExecutorExceptionInfo(@RequestParam(value = "accessToken") String accessToken, ModelAndView mv, Locale locale, HttpServletRequest httpServeletRequest, RedirectAttributes redirectAttributes)
	{

		mv.setViewName("packageExecutionDetails");
		try
		{
			RestTemplate restTemplate = new RestTemplate();
			String getHybridGroupDetailsUrl = contextPath + "/anvizentFileUpload/packageExecutorExceptionInfo?accessToken={accessToken}";
			LOG.info("URI::: " + getHybridGroupDetailsUrl);
			ResponseEntity<DataResponse> dataResponseGetPackageDetail = restTemplate.getForEntity(getHybridGroupDetailsUrl, DataResponse.class, accessToken);

			if( dataResponseGetPackageDetail != null )
			{

				if( dataResponseGetPackageDetail.getBody().getObject() != null )
				{
					mv.addObject("packageExecution", dataResponseGetPackageDetail.getBody().getObject());
					mv.addObject("messagecode", "SUCCESS");
					return mv;
				}
				else
				{
					throw new Exception("Package Execution Details not found");
				}
			}
			else
			{
				throw new Exception("Package Execution Details not found");
			}
		}
		catch ( Throwable e )
		{
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.packageExecutionDetailsNotFound", null, locale));
			LOG.error("", e);
		}
		return mv;
	}

}
