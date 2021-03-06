package com.datamodel.anvizent.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.ClientDataSources;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.OAuth1;
import com.datamodel.anvizent.service.model.OAuth2;
import com.datamodel.anvizent.service.model.TimeZones;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebServiceAuthenticationTypes;
import com.datamodel.anvizent.service.model.WebServiceConnectionMaster;
import com.datamodel.anvizent.service.model.WebServiceTemplateMaster;
import com.datamodel.anvizent.spring.AppProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Controller
@Import(AppProperties.class)
@RequestMapping(value = "/adt/package/webServiceConnection")
public class WebServiceConnectionController {

	protected static final Log LOGGER = LogFactory.getLog(WebServiceConnectionController.class);

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userRestUtilities;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	@Qualifier("commonServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilitiesCommon;


	@SuppressWarnings("unchecked")
	@ModelAttribute("webServiceList")
	public Map<Object, Object> getAllWebServices(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilities.getRestObject(request, "/getAllWebServices", user.getUserId());

		Map<Object, Object> webServiceMap = new LinkedHashMap<>();
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			Map<Integer, String> map = (Map<Integer, String>) dataResponse.getObject();
			for (Map.Entry<Integer, String> entry : map.entrySet()) {
				webServiceMap.put(entry.getKey(), entry.getValue());
			}
		}
		return webServiceMap;
	}

	@RequestMapping(value = "/webServiceOAuth2Authenticationcallback", method = RequestMethod.GET)
	public ModelAndView webServiceOAuth2Authentication(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, Locale locale) {
		try {
			OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
			String authcodeorizationCode = oar.getCode();
			mv.addObject("authcodeorizationCode", authcodeorizationCode);
			mv.addObject("messagecode", "SUCCESS");
			mv.addObject("errors", "Authentication Success");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", "Authentication failed");
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin-plain:webServiceOAuth2Authentication");
		return mv;
	}
	@RequestMapping(value = "/webServiceOAuth1Authenticationcallback", method = RequestMethod.GET)
	public ModelAndView webServiceOAuth1Authenticationcallback(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, Locale locale) {
		try {
			String oauthVerifier = request.getParameter("oauth_verifier");
			mv.addObject("oauth_verifier", oauthVerifier);
			mv.addObject("messagecode", "SUCCESS");
			mv.addObject("errors", "Authentication Success");
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", "Authentication failed");
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin-plain:webServiceOAuth1Authentication");
		return mv;
	}
	@RequestMapping( method = RequestMethod.GET)
	public ModelAndView webServiceConnection(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session,
			@ModelAttribute("webServiceConnectionMaster") WebServiceConnectionMaster webServiceConnectionMaster, Locale locale) {
		CommonUtils.setActiveScreenName("webServiceConnection", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			DataResponse dataResponse = restUtilitiesCommon.getRestObject(request, "/getWebServiceConnections", user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					mv.addObject("webServiceConnectionMasterList", dataResponse.getObject());
					webServiceConnectionMaster.setPageMode("list");
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
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
		mv.setViewName("tiles-anvizent-entry:webServiceConnection");
		return mv;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addWebServiceConnection(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("webServiceConnectionMaster") WebServiceConnectionMaster webServiceConnectionMaster, Locale locale) {
		webServiceConnectionMaster.setPageMode("add");
		mv.setViewName("tiles-anvizent-entry:webServiceConnection");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ModelAndView addWebServiceConnections(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @ModelAttribute("webServiceConnectionMaster") WebServiceConnectionMaster webServiceConnectionMaster, Locale locale)
	{
		User user = CommonUtils.getUserDetails(request, null, null);
		try
		{
			if( webServiceConnectionMaster.getWebServiceTemplateMaster().getId() != null )
			{
				DataResponse dataResponse = restUtilitiesCommon.getRestObject(request, "/getWebserviceTempleteDetails/{templateId}", user.getUserId(), webServiceConnectionMaster.getWebServiceTemplateMaster().getId());
				if( dataResponse != null && dataResponse.getHasMessages() )
				{
					if( dataResponse.getMessages().get(0).getCode().equals("SUCCESS") )
					{
						LinkedHashMap<String, Object> listObj = (LinkedHashMap<String, Object>) dataResponse.getObject();
						ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						WebServiceTemplateMaster webServiceTmpMaster = mapper.convertValue(listObj, new TypeReference<WebServiceTemplateMaster>()
						{
						});
						webServiceConnectionMaster.setDataSourceName("-1");
						webServiceConnectionMaster.setWebServiceConName(webServiceTmpMaster.getWebServiceName() + "_" + RandomStringUtils.randomAlphanumeric(4) + MinidwServiceUtil.currentTime());
						webServiceConnectionMaster.setDataSourceNameOther(webServiceTmpMaster.getWebServiceName() + "_" + RandomStringUtils.randomAlphanumeric(4) + MinidwServiceUtil.currentTime());

						webServiceConnectionMaster.setWebServiceTemplateMaster(webServiceTmpMaster);
						webServiceConnectionMaster.setPageMode("add");
						webServiceConnectionMaster.setResponseStatusCode(null);
						webServiceConnectionMaster.setResponseStatusText(null);
					}
				}

			}
			else
			{
				mv.addObject("messagecode", "FAILED");
				mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		}
		catch ( Exception e )
		{
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}

		mv.setViewName("tiles-anvizent-entry:webServiceConnection");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView editWebServiceConnections(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("webServiceConnectionMaster") WebServiceConnectionMaster webServiceConnectionMaster, Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (webServiceConnectionMaster.getId() != null) {
				DataResponse dataResponse = restUtilitiesCommon.getRestObject(request, "/getWebServiceConnectionDetailsById/{wsConId}", user.getUserId(),
						webServiceConnectionMaster.getId());
				if (dataResponse != null && dataResponse.getHasMessages()) {
					if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
						LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
						ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						WebServiceConnectionMaster webServiceConnMaster = mapper.convertValue(map, new TypeReference<WebServiceConnectionMaster>() {
						});
						webServiceConnectionMaster.setId(webServiceConnMaster.getId());
						webServiceConnectionMaster.setWebServiceConName(webServiceConnMaster.getWebServiceConName());
						webServiceConnectionMaster.setRequestParams(webServiceConnMaster.getRequestParams());
						webServiceConnectionMaster.setBodyParams(webServiceConnMaster.getBodyParams());
						webServiceConnectionMaster.setAuthPathParams(webServiceConnMaster.getAuthPathParams());
						webServiceConnectionMaster.setHeaderKeyvalues(webServiceConnMaster.getHeaderKeyvalues());
						webServiceConnectionMaster.setAuthenticationToken(webServiceConnMaster.getAuthenticationToken());
						webServiceConnectionMaster.setAuthenticationRefreshToken(webServiceConnMaster.getAuthenticationRefreshToken());
						webServiceConnectionMaster.setBodyParams(webServiceConnMaster.getBodyParams());

						webServiceConnectionMaster.setDataSourceName(webServiceConnMaster.getDataSourceName());
						//webServiceConnectionMaster.setDataSourceNameOther(webServiceConnMaster.getDataSourceNameOther());
						webServiceConnectionMaster.setActive(webServiceConnMaster.isActive());

						WebServiceTemplateMaster webTemp = new WebServiceTemplateMaster();
						webTemp.setId(webServiceConnMaster.getWebServiceTemplateMaster().getId());
						webTemp.setWebServiceName(webServiceConnMaster.getWebServiceTemplateMaster().getWebServiceName());
						webTemp.setTimeZone(webServiceConnMaster.getWebServiceTemplateMaster().getTimeZone());
						webTemp.setApiAuthBodyParams(webServiceConnMaster.getWebServiceTemplateMaster().getApiAuthBodyParams());
						webTemp.setWebserviceType(webServiceConnMaster.getWebServiceTemplateMaster().getWebserviceType());
						webTemp.setSoapBodyElement(webServiceConnMaster.getWebServiceTemplateMaster().getSoapBodyElement());
						webTemp.setApiSessionAuthURL(webServiceConnMaster.getWebServiceTemplateMaster().getApiSessionAuthURL());

						WebServiceAuthenticationTypes wsAuthTypes = new WebServiceAuthenticationTypes();
						wsAuthTypes.setId(webServiceConnMaster.getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId());
						wsAuthTypes.setAuthenticationType(
								webServiceConnMaster.getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getAuthenticationType());
						webTemp.setWebServiceAuthenticationTypes(webServiceConnMaster.getWebServiceTemplateMaster().getWebServiceAuthenticationTypes());

						webTemp.setBaseUrl(webServiceConnMaster.getWebServiceTemplateMaster().getBaseUrl());
						webTemp.setAuthenticationUrl(webServiceConnMaster.getWebServiceTemplateMaster().getAuthenticationUrl());
						webTemp.setBaseUrlRequired(webServiceConnMaster.getWebServiceTemplateMaster().isBaseUrlRequired());
						webTemp.setAuthenticationMethodType(webServiceConnMaster.getWebServiceTemplateMaster().getAuthenticationMethodType());
						webTemp.setApiAuthRequestParams(webServiceConnMaster.getWebServiceTemplateMaster().getApiAuthRequestParams());
						webTemp.setApiAuthBodyParams(webServiceConnMaster.getWebServiceTemplateMaster().getApiAuthBodyParams());
						webTemp.setApiAuthRequestHeaders(webServiceConnMaster.getWebServiceTemplateMaster().getApiAuthRequestHeaders());
						webTemp.setAuthenticationBodyParams(webServiceConnMaster.getWebServiceTemplateMaster().getAuthenticationBodyParams());
						

						OAuth2 oAuth = new OAuth2();
						oAuth.setRedirectUrl(webServiceConnMaster.getWebServiceTemplateMaster().getoAuth2().getRedirectUrl());
						oAuth.setAccessTokenUrl(webServiceConnMaster.getWebServiceTemplateMaster().getoAuth2().getAccessTokenUrl());
						oAuth.setClientIdentifier(webServiceConnMaster.getWebServiceTemplateMaster().getoAuth2().getClientIdentifier());
						oAuth.setClientSecret(webServiceConnMaster.getWebServiceTemplateMaster().getoAuth2().getClientSecret());
						oAuth.setGrantType(webServiceConnMaster.getWebServiceTemplateMaster().getoAuth2().getGrantType());
						oAuth.setScope(webServiceConnMaster.getWebServiceTemplateMaster().getoAuth2().getScope());
						oAuth.setState(webServiceConnMaster.getWebServiceTemplateMaster().getoAuth2().getState());
						webTemp.setoAuth2(oAuth);
						webTemp.setSslDisable(webServiceConnMaster.getWebServiceTemplateMaster().isSslDisable());
						webTemp.setWebServiceTemplateAuthRequestparams(
								webServiceConnMaster.getWebServiceTemplateMaster().getWebServiceTemplateAuthRequestparams());
						webTemp.setoAuth1(webServiceConnMaster.getWebServiceTemplateMaster().getoAuth1());
						webServiceConnectionMaster.setWebServiceTemplateMaster(webTemp);
						webServiceConnectionMaster.setResponseStatusCode(null);
						webServiceConnectionMaster.setResponseStatusText(null);
						 
						webServiceConnectionMaster.setPageMode("edit");
					}
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

		mv.setViewName("tiles-anvizent-entry:webServiceConnection");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute("timesZoneList")
	public Map<Object, Object> getTimesZoneList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		DataResponse dataResponse = restUtilitiesCommon.getRestObject(request, "/getTimeZones", user.getUserId());
		List<TimeZones> zoneNames = null;
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

			List<LinkedHashMap<String, Object>> timeZoneResponse = (List<LinkedHashMap<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			zoneNames = mapper.convertValue(timeZoneResponse, new TypeReference<List<TimeZones>>() {
			});

		} else {
			zoneNames = new ArrayList<>();
		}

		Map<Object, Object> zonesMap = new LinkedHashMap<>();
		for (TimeZones timeZone : zoneNames) {
			zonesMap.put(timeZone.getZoneName(), timeZone.getZoneNameDisplay());
		}

		return zonesMap;
	}
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("allDataSourceList")
	public Map<Object, Object> getDataSourceList(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<ClientDataSources> dataSourceList = null;

		dataSourceList = new ArrayList<>();
		DataResponse databseSourceDataResponse = restUtilities.getRestObject(request, "/getDataSourceList", user.getUserId());
		if (databseSourceDataResponse != null && databseSourceDataResponse.getHasMessages()
				&& databseSourceDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			
			List<LinkedHashMap<String, Object>> dataSourceResponse = (List<LinkedHashMap<String, Object>>) databseSourceDataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			dataSourceList = mapper.convertValue(dataSourceResponse, new TypeReference<List<ClientDataSources>>() {
			});
			
		} else {
			dataSourceList = new ArrayList<>();
		}

		Map<Object, Object> dataSourcesMap = new LinkedHashMap<>();
		for (ClientDataSources dataSource : dataSourceList) {
			dataSourcesMap.put(dataSource.getDataSourceName(),dataSource.getDataSourceName());
		}
		return dataSourcesMap;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveWebServiceConnection(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("webServiceConnectionMaster") WebServiceConnectionMaster webServiceConnectionMaster, RedirectAttributes redirectAttributes,
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		mv.setViewName("tiles-anvizent-entry:webServiceConnection");
		try {
			webServiceConnectionMaster.setWsApiAuthUrl(webServiceConnectionMaster.getWebServiceTemplateMaster().getAuthenticationUrl());
			webServiceConnectionMaster.setBaseUrl(webServiceConnectionMaster.getWebServiceTemplateMaster().getBaseUrl());
			webServiceConnectionMaster.setBaseUrlRequired(webServiceConnectionMaster.getWebServiceTemplateMaster().isBaseUrlRequired());
			if (webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2() == null) {
				webServiceConnectionMaster.setoAuth2(new OAuth2());
			} else {
				webServiceConnectionMaster.setoAuth2(webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2());
			}
			if (webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth1() == null) {
				webServiceConnectionMaster.setoAuth1(new OAuth1());
			} else {
				OAuth1 oAuth1 = webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth1();
				webServiceConnectionMaster.setoAuth1(oAuth1);
			}
			DataResponse dataResponse = null;
			if (webServiceConnectionMaster.getId() != null) {
				dataResponse = restUtilitiesCommon.postRestObject(request, "/updateWebserviceMasterConnectionMapping", webServiceConnectionMaster,
						user.getUserId());
			} else {
				dataResponse = restUtilitiesCommon.postRestObject(request, "/saveWebserviceMasterConnectionMapping", webServiceConnectionMaster,
						user.getUserId());
			}
			if (dataResponse != null && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
				mv.setViewName("redirect:/adt/package/webServiceConnection");
			} else {
				mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
				mv.addObject("errors", dataResponse.getMessages().get(0).getText());
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public ModelAndView testAuthenticateWebServiceApi(ModelAndView mv,
			@ModelAttribute("webServiceConnectionMaster") WebServiceConnectionMaster webServiceConnectionMaster, HttpServletRequest request, RedirectAttributes redirectAttributes, Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			Long id = webServiceConnectionMaster.getId();  
			webServiceConnectionMaster.setId(null);
			DataResponse dataResponse = restUtilitiesCommon.postRestObject(request, "/testAuthenticationUrl", webServiceConnectionMaster, user.getUserId());
			webServiceConnectionMaster.setId(id);
			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
					webServiceConnectionMaster.setResponseStatusText(dataResponse.getMessages().get(0).getText());
					webServiceConnectionMaster.setResponseStatusCode("SUCCESS");
					if (webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId().equals(Long.valueOf(5)) ) {
						LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
						webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().setAccessTokenValue(map.get("access_token").toString());
						webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth2().setRefreshTokenValue((String)map.get("refresh_token"));
					}
					if (webServiceConnectionMaster.getWebServiceTemplateMaster().getWebServiceAuthenticationTypes().getId().equals(Long.valueOf(4)) ) {
						LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) dataResponse.getObject();
						webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth1().setToken(map.get("oauth_token").toString());
						webServiceConnectionMaster.getWebServiceTemplateMaster().getoAuth1().setTokenSecret((String)map.get("oauth_token_secret"));
					}
				} else {
					webServiceConnectionMaster.setResponseStatusText(dataResponse.getMessages().get(0).getText());
					webServiceConnectionMaster.setResponseStatusCode("FAILED");
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
		mv.setViewName("tiles-anvizent-entry:webServiceConnection");
		return mv;
	}

	@RequestMapping(value = { "/validate", "/save", "/edit" }, method = RequestMethod.GET)
	public ModelAndView handleGetRequests(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) {
		mv.setViewName("redirect:/adt/package/webServiceConnection");
		return mv;
	}

}
