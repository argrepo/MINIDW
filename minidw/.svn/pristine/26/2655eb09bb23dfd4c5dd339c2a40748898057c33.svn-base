 
	package com.datamodel.anvizent.data.controller;
 
	import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.TableScriptsForm;

	/**
	 * @author rajesh.anthari
	 *
	 */
	@RestController("clientInstantScriptExecutionDataController")
	@RequestMapping("" + Constants.AnvizentURL.MINIDW_ADMIN_BASE_URL + "/etlAdmin/instantScriptExcecution")
	@CrossOrigin
	public class ClientInstantScriptExecutionDataController {

		protected static final Log LOGGER = LogFactory.getLog(ETLAdminDataController.class);
		@Autowired
		@Qualifier("clientInstantScriptExecutionDataRestController")
		private RestTemplateUtilities restUtilities;
		
		@Autowired
		@Qualifier("etlAdminServiceslbRestTemplateUtilities")
		private RestTemplateUtilities restUtilitieslb;

		@Autowired
		@Qualifier("userServicesRestTemplateUtilities")
		private RestTemplateUtilities userServicesRestTemplate;

		@RequestMapping(value = "/viewClientInstantExecutionResults", method = RequestMethod.POST)
		public DataResponse viewResultsByScriptExecutionId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody TableScriptsForm tableScriptsForm,
				Locale locale, HttpServletRequest request) {
			return restUtilities.postRestObject(request, "/viewClientInstantExecutionResults", tableScriptsForm, clientId);
		}
		@RequestMapping(value = "/viewSqlScriptByExecutionId", method = RequestMethod.POST)
		public DataResponse viewSqlScriptByExecutionId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, @RequestBody TableScriptsForm tableScriptsForm,
				Locale locale, HttpServletRequest request) {
			return restUtilities.postRestObject(request, "/viewSqlScriptByExecutionId", tableScriptsForm, clientId);
		}
	}
 
