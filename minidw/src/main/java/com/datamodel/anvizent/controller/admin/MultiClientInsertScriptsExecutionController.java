package com.datamodel.anvizent.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.datamodel.anvizent.helper.minidw.CommonUtils;

@Controller
@RequestMapping(value = "/admin/multiClientScriptExecution")
public class MultiClientInsertScriptsExecutionController
{

	protected static final Log LOGGER = LogFactory.getLog(MultiClientInsertScriptsExecutionController.class);

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView multiClientInsertScriptExecution(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, HttpSession session, Locale locale)
	{
		LOGGER.debug("in multiClientInsertScriptExecution()");
		CommonUtils.setActiveScreenName("multiClientInsertScriptExecution", session);
		List<String> tableNamesList = null;
		tableNamesList = addTableList();
		mv.addObject("tableNames", tableNamesList);
		mv.setViewName("tiles-anvizent-admin:multiClientInsertScriptsExecution");
		return mv;

	}

	public List<String> addTableList()
	{
		List<String> tableList = new ArrayList<>();

		tableList.add("minidwcm_context_parameters");
		tableList.add("minidwcm_kpis");
		tableList.add("minidwcm_verticals");
		tableList.add("minidwcm_dl_info");
		tableList.add("minidwcm_il_info");
		tableList.add("minidwcm_dl_il_mapping");
		tableList.add("minidwcm_kpis_dl_mapping");
		tableList.add("minidwcm_context_parameters_dl_mapping");
		tableList.add("minidwcm_context_parameters_il_mapping");
		tableList.add("minidwcm_il_templates");
		tableList.add("minidwcm_dl_analytical_info");
		tableList.add("minidwcm_database_types");
		tableList.add("minidwcm_database_connectors");
		tableList.add("minidwcm_il_prebuild_queries_mapping");
		tableList.add("minidwcm_ws_authentication_types");
		tableList.add("minidwcm_ws_template_mst");
		tableList.add("minidwcm_ws_template_auth_request_params");
		tableList.add("minidwcm_ws_api_mapping");
		tableList.add("minidwcm_currency_list");
		return tableList;
	}

}
