/**
 * 
 */
package com.datamodel.anvizent.data.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientCurrencyMapping;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.EncryptWebserviceAuthParams;
import com.datamodel.anvizent.service.model.ErrorLog;
import com.datamodel.anvizent.service.model.FileSettings;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.Industry;
import com.datamodel.anvizent.service.model.Internalization;
import com.datamodel.anvizent.service.model.Kpi;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.MultiClientInsertScriptsExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.TableScriptsForm;
import com.datamodel.anvizent.service.model.TemplateMigration;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rajesh.anthari
 *
 */
@RestController("user_etlAdminServiceDataRestController1")
@RequestMapping("" + Constants.AnvizentURL.MINIDW_ADMIN_BASE_URL + "/etlAdmin")
@CrossOrigin
public class ETLAdminDataController {

	protected static final Log LOGGER = LogFactory.getLog(ETLAdminDataController.class);
	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;
	@Autowired
	@Qualifier("etlAdminServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;
	
	@Autowired
	@Qualifier("appDBVersionTableScriptRestTemplateUtilities")
	private RestTemplateUtilities appDBScriptsRefRestUtilities;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/createContextParameter/{parameterName}", method = RequestMethod.POST)
	public DataResponse createContextParameter(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("parameterName") String parameterName, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/createContextParameter/{parameterName}",
				new LinkedMultiValueMap<>(), clientId, parameterName);
	}

	@RequestMapping(value = "/getContextParameters", method = RequestMethod.GET)
	public DataResponse getContextParameters(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getContextParameters", clientId);

	}

	@RequestMapping(value = "/getContextParamValue", method = RequestMethod.GET)
	public DataResponse getContextParamValue(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getContextParamValue", clientId);

	}

	@RequestMapping(value = "/getDlInfo", method = RequestMethod.GET)
	public DataResponse getDlInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getDlInfo", clientId);

	}

	@RequestMapping(value = "/getIlInfo/{dlId}", method = RequestMethod.GET)
	public DataResponse getIlInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("dlId") String dlId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getIlInfo/{dlId}", clientId, dlId);

	}

	@RequestMapping(value = "/getAllIlInfo", method = RequestMethod.GET)
	public DataResponse getAllIlInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getAllIlInfo", clientId);

	}

	@RequestMapping(value = "/saveEtlDlIlMapping", method = RequestMethod.POST)
	public DataResponse saveEtlDlIlMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ETLAdmin eTLAdmin, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/saveEtlDlIlMapping", eTLAdmin, clientId);

	}

	@RequestMapping(value = "/createIl", method = RequestMethod.POST)
	public DataResponse createIl(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ETLAdmin eTLAdmin, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/createIl", eTLAdmin, clientId);

	}

	@RequestMapping(value = "/uploadIlOrDlFiles", method = RequestMethod.POST)
	public DataResponse uploadIlOrDlFiles(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale, @RequestParam("files") MultipartFile[] multipartFiles) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("files", multipartFiles);
		return restUtilitieslb.postRestObject(request, "/uploadIlOrDlFiles", map, clientId);

	}

	@RequestMapping(value = "/createDl", method = RequestMethod.POST)
	public DataResponse createDl(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ETLAdmin eTLAdmin, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/createDl", eTLAdmin, clientId);

	}

	@RequestMapping(value = "/saveDlClientidMapping", method = RequestMethod.POST)
	public DataResponse saveClientAndDls(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ETLAdmin eTLAdmin, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/saveDlClientidMapping", eTLAdmin, clientId);
	}


	@RequestMapping(value = "/getDlClientidMapping/{Id}", method = RequestMethod.GET)
	public DataResponse getDlClientidMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("Id") String Id, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getDlClientidMapping/{Id}", userId, Id);

	}

	@RequestMapping(value = "/getClientSourceDetails/{Id}", method = RequestMethod.GET)
	public DataResponse getClientSourceDetails(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId,
			@PathVariable("Id") String Id, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getClientSourceDetails/{Id}", userId, Id);
	}

	@RequestMapping(value = "/updateIL", method = RequestMethod.POST)
	public DataResponse updateIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ETLAdmin eTLAdmin, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/updateIL", eTLAdmin, clientId);

	}

	@RequestMapping(value = "/getDLData/{dl_id}", method = RequestMethod.GET)
	public DataResponse getDLData(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("dl_id") int dlId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getDLData/{dl_id}", clientId, dlId);

	}

	@RequestMapping(value = "/updateDL", method = RequestMethod.POST)
	public DataResponse updateDL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ETLAdmin eTLAdmin, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/updateDL", eTLAdmin, clientId);

	}

	@RequestMapping(value = "/getMaxFileSize", method = RequestMethod.GET)
	public DataResponse getMaxFileSize(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getMaxFileSize", clientId);

	}

	@RequestMapping(value = "/updateFileSettings", method = RequestMethod.POST)
	public DataResponse updateFileSettings(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody FileSettings fileSettings, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/updateFileSettings", fileSettings, clientId);
	}

	@RequestMapping(value = "/deleteIL/{il_id}", method = RequestMethod.GET)
	public DataResponse deleteIL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("il_id") int ilId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/deleteIL/{il_id}", clientId, ilId);
	}

	@RequestMapping(value = "/deleteDL/{dl_id}", method = RequestMethod.GET)
	public DataResponse deleteDL(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("dl_id") int dlId, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/deleteDL/{dl_id}", clientId, dlId);
	}

	@RequestMapping(value = "/uploadILAndXrefTemplate", method = RequestMethod.POST)
	public DataResponse uploadILAndXrefTemplate(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @RequestParam("il_template") MultipartFile il_template,
			@RequestParam(value = "xref_template", required = false) MultipartFile xref_template,
			@RequestParam("iLId") String iLId, @RequestParam("iLName") String iLName,
			@RequestParam("isSameAsIL") Boolean isSameAsIL, @RequestParam("isInsert") Boolean isInsert) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();

		String iLTemplateName = null;
		String xrefTemplateName = null;
		String tempFolderName = null;
		DataResponse dataResponse = null;
		try {
			tempFolderName = Constants.TempUpload.getTempFileDir(clientId);
			iLTemplateName = tempFolderName + iLName + "." + Constants.FileType.CSV;
			CommonUtils.createFile(tempFolderName, iLTemplateName, il_template);
			map.add("iLTemplate", new FileSystemResource(iLTemplateName));
			if (!isSameAsIL && xref_template != null) {
				xrefTemplateName = tempFolderName + Constants.Config.XREF + iLName + "." + Constants.FileType.CSV;
				CommonUtils.createFile(tempFolderName, xrefTemplateName, xref_template);
				map.add("xrefTemplate", new FileSystemResource(xrefTemplateName));
			}
			map.add("iLName", iLName);
			map.add("iLID", iLId);
			map.add("isSameAsIL", isSameAsIL);
			map.add("isInsert", isInsert);
			dataResponse = restUtilities.postRestObject(request, "/uploadILAndXrefTemplate", map, clientId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (tempFolderName != null) {
				try {
					FileUtils.deleteDirectory(new File(tempFolderName));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getClientErrorLogById", method = RequestMethod.POST)
	public DataResponse getClientErrorLogById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ErrorLog errorLog, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getClientErrorLogById", errorLog, clientId);
	}

	@RequestMapping(value = "/searchClientErrorLog", method = RequestMethod.POST)
	public DataResponse searchClientErrorLog(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ErrorLog errorLog, HttpServletRequest request, Locale locale) {
		return restUtilities.postRestObject(request, "/searchClientErrorLog", errorLog, clientId);

	}

	@RequestMapping(value = "/getVerticalDetailsById", method = RequestMethod.POST)
	public DataResponse getVerticalDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Industry industry, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getVerticalDetailsById", industry, clientId);
	}

	@RequestMapping(value = "/getKpiDetailsById", method = RequestMethod.POST)
	public DataResponse getKpiDetailsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Kpi kpi, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getKpiDetailsById", kpi, clientId);
	}

	@RequestMapping(value = "/getILInfoById", method = RequestMethod.POST)
	public DataResponse getILInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request, @RequestParam(value = "iLId") String iLId) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("iLId", iLId);
		return restUtilities.postRestObject(request, "/getILInfoById", map, clientId);
	}

	@RequestMapping(value = "/updateWebServicesById", method = RequestMethod.POST)
	public DataResponse updateWebServicesById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody WebService webService, HttpServletRequest request, Locale locale) {

		return restUtilities.postRestObject(request, "/updateWebServicesById", webService, clientId);
	}

	@RequestMapping(value = "/getWebserviceClients", method = RequestMethod.POST)
	public DataResponse getWebserviceClients(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody WebService webService, HttpServletRequest request, Locale locale) {

		return restUtilities.postRestObject(request, "/getWebserviceClients", webService, clientId);
	}

	@RequestMapping(value = "/saveWebserviceClientMapping", method = RequestMethod.POST)
	public DataResponse saveWebserviceClientMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @RequestParam("webserviceId") Integer webserviceId,
			@RequestParam("checkedClients") String checkedClients) {

		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("webserviceId", webserviceId);
		map.add("checkedClients", checkedClients);

		return restUtilities.postRestObject(request, "/saveWebserviceClientMapping", map, clientId);

	}

	@RequestMapping(value = "/getKpiListByDlId", method = RequestMethod.POST)
	public DataResponse getKpiListByDlId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody DLInfo dLInfo, HttpServletRequest request, Locale locale) {
		return restUtilities.postRestObject(request, "/getKpiListByDlId", dLInfo, clientId);
	}

	@RequestMapping(value = "/saveDlKpiMapping", method = RequestMethod.POST)
	public DataResponse saveDlKpiMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request, @RequestParam("dlId") Integer dlId,
			@RequestParam("checkedKpis") String checkedKpis, @RequestParam("checkedIds") String checkedIds) {

		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("dlId", dlId);
		map.add("checkedKpis", checkedKpis);
		map.add("checkedIds", checkedIds);
		return restUtilities.postRestObject(request, "/saveDlKpiMapping", map, clientId);

	}

	@RequestMapping(value = "/getDefaultQuery", method = RequestMethod.POST)
	public DataResponse getDefaultQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("databaseTypeId") Integer databaseTypeId, HttpServletRequest request) {

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("databaseTypeId", databaseTypeId);
		return restUtilities.postRestObject(request, "/getDefaultQuery", map, clientId);
	}

	@RequestMapping(value = "/getIlQueryById/{ilId}/{databaseTypeId}", method = RequestMethod.GET)
	public DataResponse getIlQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("ilId") Integer ilId, @PathVariable("databaseTypeId") Integer databaseTypeId, Locale locale,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getIlQueryById/{ilId}/{databaseTypeId}", clientId, ilId,
				databaseTypeId);
	}

	@RequestMapping(value = "/getIlincrementalQueryById/{ilId}/{databaseTypeId}", method = RequestMethod.GET)
	public DataResponse getIlincrementalQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("ilId") Integer ilId, @PathVariable("databaseTypeId") Integer databaseTypeId, Locale locale,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/getIlincrementalQueryById/{ilId}/{databaseTypeId}", clientId,
				ilId, databaseTypeId);
	}

	@RequestMapping(value = "/gethistoryLoadById/{ilId}/{databaseTypeId}", method = RequestMethod.GET)
	public DataResponse gethistoryLoadById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("ilId") Integer ilId, @PathVariable("databaseTypeId") Integer databaseTypeId, Locale locale,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/gethistoryLoadById/{ilId}/{databaseTypeId}", clientId, ilId,
				databaseTypeId);
	}

	@RequestMapping(value = "/geMaxDateQueryById/{ilId}/{databaseTypeId}", method = RequestMethod.GET)
	public DataResponse geMaxDateQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("ilId") Integer ilId, @PathVariable("databaseTypeId") Integer databaseTypeId, Locale locale,
			HttpServletRequest request) {

		return restUtilities.getRestObject(request, "/geMaxDateQueryById/{ilId}/{databaseTypeId}", clientId, ilId,
				databaseTypeId);
	}

	@RequestMapping(value = "/getNotMappedILsByDBTypeId", method = RequestMethod.POST)
	public DataResponse getNotMappedILs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("databaseTypeId") Integer databaseTypeId, Locale locale, HttpServletRequest request) {

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("databaseTypeId", databaseTypeId);
		return restUtilities.postRestObject(request, "/getNotMappedILsByDBTypeId", map, clientId);
	}

	@RequestMapping(value = "/editILDefaultQuery", method = RequestMethod.POST)
	public DataResponse editILDefaultQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("ilid") Integer ilid, HttpServletRequest request,
			@RequestParam("databaseTypeId") Integer databaseTypeId, Locale locale) {

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("ilid", ilid);
		map.add("databaseTypeId", databaseTypeId);
		return restUtilities.postRestObject(request, "/editILDefaultQuery", map, clientId);
	}

	@RequestMapping(value = "/updateILDefaultQuery", method = RequestMethod.POST)
	public DataResponse updateILDefaultQuery(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ILConnectionMapping ilconnectionMapping, HttpServletRequest request, Locale locale) {

		return restUtilities.postRestObject(request, "/updateILDefaultQuery", ilconnectionMapping, clientId);
	}

	@RequestMapping(value = "/addOrUpdateTableScripts", method = RequestMethod.POST)
	public DataResponse addOrUpdateTableScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody TableScriptsForm tableScripts, HttpServletRequest request, Locale locale) {
		return restUtilities.postRestObject(request, "/addOrUpdateTableScripts", tableScripts, clientId);
	}

	@RequestMapping(value = "/getTableScripts", method = RequestMethod.GET)
	public DataResponse getTableScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getTableScripts", clientId);
	}

	@RequestMapping(value = "/getTableScriptView", method = RequestMethod.POST)
	public DataResponse getTableScriptView(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestBody TableScriptsForm tableScripts) {
		return restUtilities.postRestObject(request, "/getTableScriptViewById", tableScripts, clientId);
	}

	@RequestMapping(value = "/getTableScriptsMappingIsNotExecutedErrorMsg", method = RequestMethod.POST)
	public DataResponse getTableScriptsMappingIsNotExecutedErrorMsg(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody TableScriptsForm tableScriptsForm, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getTableScriptsMappingIsNotExecutedErrorMsg", tableScriptsForm,
				clientId);
	}

	@RequestMapping(value = "/saveDefaultTemplateMasterMappingData", method = RequestMethod.POST)
	public DataResponse updateConnectorsAsDefault(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam(value = "masterIds") List<Integer> masterIds, @RequestParam("templateId") int templateId,
			@RequestParam("mappingType") String mappingType, HttpServletRequest request) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		for (Integer masterId : masterIds) {
			map.add("masterIds", masterId);
		}
		map.add("templateId", templateId);
		map.add("mappingType", mappingType);
		return restUtilities.postRestObject(request, "/saveDefaultTemplateMasterMappingData", map, clientId);
	}

	@RequestMapping(value = "/saveDefaultTemplateMasterMappingDataForCurrency", method = RequestMethod.POST)
	public DataResponse saveDefaultTemplateMasterMappingDataForCurrency(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("templateId") int templateId, @RequestParam("currencyType") String currencyType,
			@RequestParam("currencyName") String currencyName,
			@RequestParam("basecurrencyCode") String basecurrencyCode,
			@RequestParam("accountingCurrencyCode") String accountingCurrencyCode, HttpServletRequest request) {
		ClientCurrencyMapping clientCurrencyMapping = new ClientCurrencyMapping();
		clientCurrencyMapping.setId(templateId);
		clientCurrencyMapping.setCurrencyType(currencyType);
		clientCurrencyMapping.setCurrencyName(currencyName);
		clientCurrencyMapping.setBasecurrencyCode(basecurrencyCode);
		clientCurrencyMapping.setAccountingCurrencyCode(accountingCurrencyCode);
		return restUtilities.postRestObject(request, "/createCurrencyMapping", clientCurrencyMapping, clientId);
	}

	@RequestMapping(value = "/getInstantTableScriptsIsNotExecutedErrorMsg", method = RequestMethod.POST)
	public DataResponse getInstantTableScriptsIsNotExecutedErrorMsg(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody TableScriptsForm tableScriptsForm, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getInstantTableScriptsIsNotExecutedErrorMsg", tableScriptsForm,
				clientId);
	}

	@RequestMapping(value = "/getPreviousTableScriptView", method = RequestMethod.POST)
	public DataResponse getPreviousTableScriptView(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, @RequestBody TableScriptsForm tableScripts) {

		return restUtilities.postRestObject(request, "/getPreviousTableScriptView", tableScripts, clientId);
	}

	@RequestMapping(value = "/getAllServerConfigurations", method = RequestMethod.GET)
	public DataResponse getAllServerConfigurations(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getAllServerConfigurations", clientId);
	}

	@RequestMapping(value = "/getAllClientsByServer/{serverId}", method = RequestMethod.GET)
	public DataResponse getAllClientsByServer(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("serverId") Integer serverId, Locale locale, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getAllClientsByServer/{serverId}", clientId, serverId);
	}

	@RequestMapping(value = "/getDependencyDetailsForSourceServerClient", method = RequestMethod.POST)
	public DataResponse getDependencyDetailsForSourceServerClient(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody TemplateMigration templateMigration, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getDependencyDetailsForSourceServerClient", templateMigration,
				clientId);
	}

	@RequestMapping(value = "/getDependencyDetailsForDestinationServerClient", method = RequestMethod.POST)
	public DataResponse getDependencyDetailsForDestinationServerClient(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody TemplateMigration templateMigration, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/getDependencyDetailsForDestinationServerClient",
				templateMigration, clientId);
	}

	@RequestMapping(value = "/migrateUserFromSourceToDestination", method = RequestMethod.POST)
	public DataResponse migrateUserFromSourceToDestination(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody TemplateMigration templateMigration, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/migrateUserFromSourceToDestination", templateMigration,
				clientId);
	}

	@RequestMapping(value = "/migratePackagesFromSourceToDestination", method = RequestMethod.POST)
	public DataResponse migratePackagesFromSourceToDestination(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody TemplateMigration templateMigration, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/migratePackagesFromSourceToDestination", templateMigration,
				clientId);
	}

	@RequestMapping(value = "/migrateDbConFromSourceToDestination", method = RequestMethod.POST)
	public DataResponse migrateDbConFromSourceToDestination(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody TemplateMigration templateMigration, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/migrateDbConFromSourceToDestination", templateMigration,
				clientId);
	}

	@RequestMapping(value = "/savePropertiesKeyValuePairs", method = RequestMethod.POST)
	public DataResponse savePropertiesKeyValuePairs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") int id, @RequestParam("locale") String locale,
			@RequestParam("localeName") String localeName, @RequestParam("properties") String properties,
			HttpServletRequest request) {
		Internalization internalizationProperties = new Internalization();
		internalizationProperties.setId(id);
		internalizationProperties.setLocale(locale);
		internalizationProperties.setLocaleName(localeName);
		internalizationProperties.setProperties(properties);
		return restUtilities.postRestObject(request, "/savePropertiesKeyValuePairs", internalizationProperties,
				clientId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getpropertiesKeyValuePairsById", method = RequestMethod.POST)
	public DataResponse getpropertiesKeyValuePairsById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		DataResponse dataResponse = restUtilities.postRestObject(request, "/getpropertiesKeyValuePairsById", map,
				clientId);
		if (dataResponse != null && dataResponse.getHasMessages()) {
			if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
				Map<String, Object> propertiesData = (Map<String, Object>) dataResponse.getObject();
				try {
					propertiesData.put("applicationProperties",
							MinidwServiceUtil.getApplicationPropertiesKeyValuePairs());
				} catch (IOException e) {
					LOGGER.error("Application properties not found",e);
				}
			}
		} else {
			dataResponse = MinidwServiceUtil.getErrorDataResponse("Data not found");
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getPropertyKeyValueFromminidw", method = RequestMethod.GET)
	public DataResponse getPropertyKeyValueFromminidw(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		dataResponse.addMessage(message);

		try {
			Map<String, String> propertiesData = MinidwServiceUtil.getApplicationPropertiesKeyValuePairs();
			dataResponse.setObject(propertiesData);
			message.setCode("SUCCESS");
			messages.add(message);
			dataResponse.setMessages(messages);
		} catch (IOException ae) {
			ae.printStackTrace();
			LOGGER.error("error while getPropertyKeyValueFromminidw() ", ae);
			message.setCode("ERROR");
			message.setText(messageSource.getMessage("anvizent.message.text.errorWhileGettingPackageProperties", null, locale));
			messages.add(message);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getPropertyKeyValueFromminidwWebService", method = RequestMethod.GET)
	public DataResponse getPropertyKeyValueFromminidwWebService(
			@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getPropertyKeyValueFromminidwWebService", clientId);
	}

	@RequestMapping(value = "/loadLocaleMappingList", method = RequestMethod.GET)
	public DataResponse loadLocaleMappingList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getPropertiesList", clientId);
	}

	@RequestMapping(value = "/getLocaleNamesList", method = RequestMethod.GET)
	public DataResponse getLocaleNamesList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			Locale locale, HttpServletRequest request) {

		DataResponse dataResponse = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			Locale[] list = Locale.getAvailableLocales();
			HashMap<String, Object> languageList = new LinkedHashMap<String, Object>();
			for (Locale localeInfo : list) {
				languageList.put(localeInfo.getLanguage()
						+ (StringUtils.isNotBlank(localeInfo.getCountry()) ? "-" + localeInfo.getCountry() : ""),
						localeInfo.getDisplayName());
			}
			message.setCode("SUCCESS");
			messages.add(message);
			dataResponse.setMessages(messages);
			dataResponse.setObject(languageList);
		} catch (Exception ae) {
			ae.printStackTrace();
			LOGGER.error("error while getLocaleNamesList() ", ae);
			message.setCode("ERROR");
			message.setText(messageSource.getMessage("anvizent.message.text.errorWhileGettingLocaleNamesList", null, locale));
			messages.add(message);
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getS3InfoList", method = RequestMethod.GET)
	public DataResponse getS3InfoList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getS3InfoList", clientId);
	}

	@RequestMapping(value = "/saveS3BucketInfo", method = RequestMethod.POST)
	public DataResponse saveS3BucketInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") int id, @RequestParam("bucketName") String bucketName,
			@RequestParam("secretKey") String secretKey, @RequestParam("accessKey") String accessKey,
			@RequestParam("isActive") Boolean isActive, HttpServletRequest request) {
		S3BucketInfo s3info = new S3BucketInfo();
		s3info.setId(id);
		s3info.setBucketName(bucketName);
		s3info.setSecretKey(secretKey);
		s3info.setAccessKey(accessKey);
		s3info.setIsActive(isActive);
		return restUtilities.postRestObject(request, "/saveS3BucketInfo", s3info, clientId);
	}

	@RequestMapping(value = "/getS3BucketInfoById", method = RequestMethod.POST)
	public DataResponse getS3BucketInfoById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("id") Integer id, HttpServletRequest request, Locale locale) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return restUtilities.postRestObject(request, "/getS3BucketInfoById", map, clientId);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getClientList", method = RequestMethod.GET)
	public DataResponse getClientList(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, Locale locale,
			HttpServletRequest request) {

		DataResponse dataResponseNew = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		dataResponseNew.addMessage(message);

		try {
			long clientId = Long
					.valueOf((String) SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID));
			User user = CommonUtils.getUserDetails(request, null, null);

			String activeClientsUrl = "/activeClients";
			DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, activeClientsUrl,
					new LinkedMultiValueMap<>(), DataResponse.class, user.getUserId());
			Map<Object, Object> clientListsMap = new LinkedHashMap<>();
			if (dataResponse != null) {
				List<Map<String, Object>> clientList = (List<Map<String, Object>>) dataResponse.getObject();
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<CloudClient> cloudClients = mapper.convertValue(clientList,
						new TypeReference<List<CloudClient>>() {
						});

				for (CloudClient cloudClient : cloudClients) {
					// TODO Add this condition
					if (clientId == cloudClient.getId()) {
						clientListsMap.put(cloudClient.getId(),
								cloudClient.getId() + " - " + cloudClient.getClientName());
					}
				}
				message.setCode("SUCCESS");
				messages.add(message);
				dataResponseNew.setMessages(messages);
				dataResponseNew.setObject(clientListsMap);
			}

		} catch (Exception ae) {
			ae.printStackTrace();
			LOGGER.error("error while getClientList() ", ae);
			message.setCode("ERROR");
			message.setText(messageSource.getMessage("anvizent.message.text.errorWhileGettingGetclientlist", null, locale));
			messages.add(message);
		}
		return dataResponseNew;
	}

	@RequestMapping(value = "/getBucketList", method = RequestMethod.GET)
	public DataResponse getBucketList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getBucketList", clientId);
	}
	
	@RequestMapping(value = "/getScheduledMasterInfoList", method = RequestMethod.GET)
	public DataResponse getScheduledMasterInfoList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getScheduledMasterInfoList", clientId);
	}
	
	@RequestMapping(value = "/getFileSettingsList", method = RequestMethod.GET)
	public DataResponse getFileSettingsList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId, Locale locale,
			HttpServletRequest request) {
		return restUtilities.getRestObject(request, "/getFileSettingsList", clientId);
	}

	@RequestMapping(value = "/saveClientMapping", method = RequestMethod.POST)
	public DataResponse saveClientMapping(@PathVariable(Constants.PathVariables.CLIENT_ID) String client_Id,
			@RequestParam("clientId") int clientId, @RequestParam("id") int id, HttpServletRequest request) {
		S3BucketInfo s3info = new S3BucketInfo();
		s3info.setClientId(clientId);
		s3info.setId(id);  
		return restUtilities.postRestObject(request, "/saveClientMapping", s3info, client_Id);
	}
	
	@RequestMapping(value = "/getAppDBScriptById/{id}", method = RequestMethod.GET)
	public DataResponse getIlQueryById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") Integer id, Locale locale,
			HttpServletRequest request) {

		return appDBScriptsRefRestUtilities.getRestObject(request, "/getAppDBScriptById/{id}", clientId, id);
	}
	
	@RequestMapping(value = "/getMinidwDBScriptById/{id}", method = RequestMethod.GET)
	public DataResponse getMinidwDBScriptById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") Integer id, Locale locale,
			HttpServletRequest request) {

		return appDBScriptsRefRestUtilities.getRestObject(request, "/getMinidwDBScriptById/{id}", clientId, id);
	}
	
	@RequestMapping(value = "/saveNotMappedVerticals", method = RequestMethod.POST)
	public DataResponse saveNotMappedVerticals(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Industry industry, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/saveNotMappedVerticals", industry, clientId);
	}
	
	@RequestMapping(value = "/saveNotMappedDatabases", method = RequestMethod.POST)
	public DataResponse saveNotMappedDatabases(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Database database, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/saveNotMappedDatabases", database, clientId);
	}
	
	@RequestMapping(value = "/saveNotMappedConnectors", method = RequestMethod.POST)
	public DataResponse saveNotMappedConnectors(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody Database database, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/saveNotMappedConnectors", database, clientId);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getMultiClientList", method = RequestMethod.GET)
	public DataResponse getMultiClientList(@PathVariable(Constants.PathVariables.CLIENT_ID) String userId, Locale locale,
			HttpServletRequest request) {

		DataResponse dataResponseNew = new DataResponse();
		List<Message> messages = new ArrayList<>();
		Message message = new Message();
		dataResponseNew.addMessage(message);

		try {
			User user = CommonUtils.getUserDetails(request, null, null);

			String activeClientsUrl = "/activeClients";
			DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, activeClientsUrl,
					new LinkedMultiValueMap<>(), DataResponse.class, user.getUserId());
			Map<Object, Object> clientListsMap = new LinkedHashMap<>();
			if (dataResponse != null) {
				List<Map<String, Object>> clientList = (List<Map<String, Object>>) dataResponse.getObject();
				ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				List<CloudClient> cloudClients = mapper.convertValue(clientList,
						new TypeReference<List<CloudClient>>() {
						});

				for (CloudClient cloudClient : cloudClients) {
						clientListsMap.put(cloudClient.getId(),cloudClient.getId() + " - " + cloudClient.getClientName());
				}
				message.setCode("SUCCESS");
				messages.add(message);
				dataResponseNew.setMessages(messages);
				dataResponseNew.setObject(clientListsMap);
			}

		} catch (Exception ae) {
			ae.printStackTrace();
			LOGGER.error("error while getClientList() ", ae);
			message.setCode("ERROR");
			message.setText(messageSource.getMessage("anvizent.message.text.errorWhileGettingGetclientlist", null, locale));
			messages.add(message);
		}
		return dataResponseNew;
	}
	
	@RequestMapping(value = "/executeInsertScripts", method = RequestMethod.POST)
	public DataResponse executeInsertScripts(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody MultiClientInsertScriptsExecution multiClientInsertScriptsExecution, HttpServletRequest request) {
		return restUtilities.postRestObject(request, "/executeInsertScripts", multiClientInsertScriptsExecution, clientId);
	}
	
	@RequestMapping(value = "/encryptWebServiceAuthParams", method = RequestMethod.POST)
	public DataResponse encryptWebServiceAuthParams(  	@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
		@RequestParam("tableName") String tableName,
		@RequestParam("whereConditionColumnInfo") String whereConditionColumnInfo,
		@RequestParam("columnInfo") String columnInfo, HttpServletRequest request) {
		EncryptWebserviceAuthParams encryptWebserviceAuthParams = new EncryptWebserviceAuthParams();
		encryptWebserviceAuthParams.setTableName(tableName);
		encryptWebserviceAuthParams.setColumnInfo(columnInfo);
		encryptWebserviceAuthParams.setWhereConditionColumnInfo(whereConditionColumnInfo);
		
		return restUtilities.postRestObject(request, "/encryptWebServiceAuthParams", encryptWebserviceAuthParams, clientId);
	}
}
