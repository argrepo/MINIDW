package com.datamodel.anvizent.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.DataTypesInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.QueryBuilderForm;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import(AppProperties.class)
@Controller
@RequestMapping(value = "/adt/package")
public class TableBuilderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TableBuilderController.class);

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/tableBuilder/{packageId}", method = RequestMethod.GET)
	public ModelAndView getCustomTempTables(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@PathVariable("packageId") String packageId, @ModelAttribute("queryBuilderForm") QueryBuilderForm queryBuilderForm, BindingResult result,
			RedirectAttributes redirectAttributes, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);
		Package userPackage = null;
		Table table = null;

		LOGGER.info(" packageId : {}, User id :  {} ", packageId, user.getUserId());
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		try {

			DataResponse userDataResponse = restUtilities.getRestObject(request, "/getPackagesById/{" + packageId + "}", user.getUserId(), packageId);
			if (userDataResponse != null && userDataResponse.getHasMessages() && userDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) userDataResponse.getObject();
				ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				userPackage = mapper1.convertValue(map, new TypeReference<Package>() {
				});
				mv.addObject("userPackage", userPackage);
			}

			String industryId = userPackage.getIndustry().getId() + "";

			queryBuilderForm.setIndustryId(industryId);
			queryBuilderForm.setPackageId(packageId);

			DataResponse tableDataResponse = restUtilities.getRestObject(request, "/getTargetTablesStructure/{packageId}/{industryId}", user.getUserId(),
					packageId, industryId);

			if (tableDataResponse != null && tableDataResponse.getHasMessages() && tableDataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) tableDataResponse.getObject();
				ObjectMapper mapper1 =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				table = mapper1.convertValue(map, new TypeReference<Table>() {
				});
				mv.addObject("table", table);
			}

			if (table != null && table.getColumns() != null) {

				mv.addObject("targetTable", table);
				mv.setViewName("tiles-anvizent-entry:tableBuilder");
			} else {
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.message.targetTableDoesNotExist", null, locale));
				mv.setViewName("redirect:/adt/package/customPackage/edit/" + packageId);
			}

		} catch (Exception e) {
			LOGGER.error("Error while getting the custom temp tables data from the rest service ", e);

			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.ERROR);
			message.setText(messageSource.getMessage("anvizent.message.label.errorWhileGettingTableStructure", null, locale));
			messages.add(message);
			dataResponse.setMessages(messages);
		}

		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@ModelAttribute("dataTypesList")
	public List<DataTypesInfo> getDataTypesList(HttpServletRequest request)
	{

		User user = CommonUtils.getUserDetails(request, null, null);
		List<DataTypesInfo> dataTypeInfoList = new ArrayList<>();

		DataResponse dataTypeDataResponse = restUtilities.getRestObject(request, "/getDataTypesList", user.getUserId());
		List<LinkedHashMap<String, Object>> dataTypeInfo = null;
		if( dataTypeDataResponse != null && dataTypeDataResponse.getHasMessages() && dataTypeDataResponse.getMessages().get(0).getCode().equals("SUCCESS") )
		{
			dataTypeInfo = (List<LinkedHashMap<String, Object>>) dataTypeDataResponse.getObject();
		}
		else
		{
			dataTypeInfo = new ArrayList<>();
		}

		for (LinkedHashMap<String, Object> map : dataTypeInfo)
		{
			DataTypesInfo dataType = new DataTypesInfo();
			int id = Integer.parseInt(map.get("id").toString());
			dataType.setId(id);
			dataType.setDataTypeName(map.get("dataTypeName").toString());
			//dataType.setRange(map.get("range").toString());
			dataTypeInfoList.add(dataType);
		}

		return dataTypeInfoList;
	}

}
