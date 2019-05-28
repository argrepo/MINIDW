package com.datamodel.anvizent.controller;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.ApisDataInfo;
import com.datamodel.anvizent.service.model.CustomPackageForm;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.User;

@Controller
@RequestMapping(value = "/adt/package/apisDataInfo")
public class ApisDataController {

	protected static final Log LOGGER = LogFactory.getLog(ApisDataController.class);

	@Autowired
	@Qualifier("apisDataRestTemplateUtilities")
	private RestTemplateUtilities apiRestUtilities;
	
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	
	@Autowired
	private MessageSource messageSource;
 
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getApisDataInfo(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("apisDataInfo") ApisDataInfo apisDataInfo,
			Locale locale) {
		LOGGER.debug("in getApisDataInfo()");
		CommonUtils.setActiveScreenName("apisData", session);
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			//apisDataInfo.setPageMode("list");
			DataResponse dataResponse = apiRestUtilities.getRestObject(request, "/getApisDetails",user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
				mv.addObject("apisDetailsList", dataResponse.getObject());
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

		mv.setViewName("tiles-anvizent-entry:apisData");
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView easyQueryBuilderForStandardPackage(HttpServletRequest request, HttpServletResponse response,@ModelAttribute("customPackageForm") CustomPackageForm customPackageForm, ModelAndView mv,
			 Locale locale) {
		LOGGER.debug("in easyQueryBuilderForDDLayout()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
             if(user != null){
			
			DataResponse ddLayoutDataResponse = restUtilities.getRestObject(request, "/getClientSchemaName", user.getUserId());
			
			if (ddLayoutDataResponse != null && ddLayoutDataResponse.getHasMessages() && ddLayoutDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				   LinkedHashMap<String, Object>  clientDbDetails = ( LinkedHashMap<String, Object> )ddLayoutDataResponse.getObject();
				   if(clientDbDetails != null){
				   ILConnection ilConnection = new ILConnection();
				   List<String> schemaList  = new ArrayList<>();
				   schemaList.add(clientDbDetails.get("clientdb_schema").toString());
				   Database database = new Database();
				   ilConnection.setConnectionName("MYSQL");
				   ilConnection.setDataSourceName("MYSQL");
				   ilConnection.setConnectionId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
				   database.setProtocal(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);
				   database.setDriverName(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
			       ilConnection.setServer(clientDbDetails.get("region_hostname").toString()+":"+clientDbDetails.get("region_port").toString()+"/"+clientDbDetails.get("clientdb_schema").toString());
				   ilConnection.setUsername(clientDbDetails.get("clientdb_username").toString());
				   ilConnection.setPassword(clientDbDetails.get("clientdb_password").toString());
				   database.setSchema(clientDbDetails.get("clientdb_schema").toString());
				   database.setSchemas(schemaList);
				   ilConnection.setDatabase(database);
				   mv.addObject("iLConnection", ilConnection);
				   }
			   }
			mv.addObject("isApisData", true);
			mv.addObject("isDDlayout", false);
			//mv.addObject("isDDlayout", true);
			mv.addObject("isCustom", false);
			mv.addObject("databseList", getDatabaseTypes(request));
			mv.setViewName("tiles-anvizent-entry:easyquerybuilder");
             }
		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	public List<Database> getDatabaseTypes(HttpServletRequest request) {

		User user = CommonUtils.getUserDetails(request, null, null);
		List<Database> databseList = null;

		databseList = new ArrayList<>();
		DataResponse databseTypesDataResponse = restUtilities.getRestObject(request, "/getDatabasesTypes", user.getUserId());
		List<LinkedHashMap<String, Object>> databseTypes = null;
		if (databseTypesDataResponse != null && databseTypesDataResponse.getHasMessages()
				&& databseTypesDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			databseTypes = (List<LinkedHashMap<String, Object>>) databseTypesDataResponse.getObject();
		} else {
			databseTypes = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : databseTypes) {
			Database databse = new Database();
			int id = Integer.parseInt(map.get("id").toString());
			int connector_id = Integer.parseInt(map.get("connector_id").toString());
			databse.setId(id);
			databse.setName(map.get("name").toString());
			databse.setConnector_id(connector_id);
			databse.setProtocal((String)map.get("protocal"));  
			databse.setUrlFormat((String)map.get("urlFormat"));
			databseList.add(databse);
		}

		return databseList;
	}

	
	
}
