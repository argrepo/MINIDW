package com.datamodel.anvizent.data.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.PreservedProperties;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ELTConfigTags;
import com.datamodel.anvizent.service.model.EltJobInfo;
import com.datamodel.anvizent.service.model.EltJobTagInfo;
import com.datamodel.anvizent.service.model.EltLoadParameters;
import com.datamodel.anvizent.service.model.EltMasterConfiguration;
import com.datamodel.anvizent.service.model.Message;

@RestController("eltDataRestController1")
@RequestMapping("" + Constants.AnvizentURL.MINIDW_ADMIN_BASE_URL + "/eltconfig")
@CrossOrigin
public class ELTDataController {

	protected static final Log LOG = LogFactory.getLog(ELTDataController.class);

	@Autowired
	@Qualifier("eltRestTemplateUtilities")
	private RestTemplateUtilities eltRestUtilities;

	@RequestMapping(value = "/saveEltStgKeyConfig", method = RequestMethod.POST)
	public DataResponse saveEltStgKeyConfig(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody List<ELTConfigTags> eltStgKeyConfig, HttpServletRequest request) {
		return eltRestUtilities.postRestObject(request, "/saveEltStgKeyConfig", eltStgKeyConfig, clientId);
	}

	@RequestMapping(value = "/deleteEltStgKeyConfigById", method = RequestMethod.POST)
	public DataResponse deleteEltStgKeyConfigById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam Integer id, HttpServletRequest request) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("id", id);
		return eltRestUtilities.postRestObject(request, "/deleteEltStgKeyConfigById", map, clientId);
	}
	
	@RequestMapping(value = "/deleteALLEltStgKeyConfigById", method = RequestMethod.POST)
	public DataResponse deleteALLEltStgKeyConfigById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam Integer tagId, HttpServletRequest request) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("tagId", tagId);
		return eltRestUtilities.postRestObject(request, "/deleteALLEltStgKeyConfigById", map, clientId);
	}

	@RequestMapping(value = "/saveEltConfigPairInfo", method = RequestMethod.POST)
	public DataResponse saveEltILStgKeyConfig(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ELTConfigTags eltilConfigs, HttpServletRequest request) {
		return eltRestUtilities.postRestObject(request, "/saveEltConfigPairInfo", eltilConfigs, clientId);
	}
	
	
	@RequestMapping(value = "/saveEltCloneTagInfo", method = RequestMethod.POST)
	public DataResponse saveEltCloneTagInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody ELTConfigTags eltilConfigs, HttpServletRequest request) {
		return eltRestUtilities.postRestObject(request, "/saveEltCloneTagInfo", eltilConfigs, clientId);
	}

	@RequestMapping(value = "/uploadBulkConfigInfo", method = RequestMethod.POST)
	public DataResponse uploadBulkConfigInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		PreservedProperties endPointProperties = new PreservedProperties();
		FileInputStream propertieFile = null;
		File file = null;
		try {
			file = CommonUtils.multipartToFile(multipartFile);
			propertieFile = new FileInputStream(file);
			endPointProperties.load(propertieFile);
			dataResponse.setObject(endPointProperties.getPropertiesMap().getData()); 
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
		} catch (Exception e) {
			LOG.error("", e);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", e);
		} finally {
			try {
				if (propertieFile != null) {
					propertieFile.close();
				}
				if (file != null) {
					file.delete();
				}
			} catch (Exception e2) {
				LOG.error("", e2);
			}
		}

		return dataResponse;
	}

	@RequestMapping(value = "/getEltConfigByTagId", method = RequestMethod.POST)
	public DataResponse getEltConfigByTagId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam Integer tagId, HttpServletRequest request) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("tagId", tagId);
		return eltRestUtilities.postRestObject(request, "/getEltConfigByTagId", map, clientId);
	}

	@RequestMapping(value = "/saveEltJobTagInfo", method = RequestMethod.POST)
	public DataResponse saveEltJobTagInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody EltJobTagInfo eltJobInfo, HttpServletRequest request) {
		return eltRestUtilities.postRestObject(request, "/saveEltJobTagInfo", eltJobInfo, clientId);
	}

	@RequestMapping(value = "/getEltJobById/{tagId}", method = RequestMethod.GET)
	public DataResponse getEltJobById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("tagId") Integer tagId, Locale locale, HttpServletRequest request) {
		return eltRestUtilities.getRestObject(request, "/eltJobTag/{tagId}", clientId, tagId);
	}

	@RequestMapping(value = "/saveEltJobMappingInfo", method = RequestMethod.POST)
	public DataResponse saveEltJobMappingInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody EltJobInfo eltJobInfo, HttpServletRequest request) {
		return eltRestUtilities.postRestObject(request, "/saveEltJobMappingInfo", eltJobInfo, clientId);
	}

	@RequestMapping(value = "/deleteDerivedByMappingId/{derivedId}", method = RequestMethod.GET)
	public DataResponse deleteDerivedByMappingId(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("derivedId") Integer derivedId, Locale locale, HttpServletRequest request) {
		return eltRestUtilities.getRestObject(request, "/deleteDerivedByMappingId/{derivedId}", clientId, derivedId);
	}

	@RequestMapping(value = "/saveEltMasterConfig", method = RequestMethod.POST)
	public DataResponse saveEltMasterConfig(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody EltMasterConfiguration eltMasterConfiguration,
			HttpServletRequest request) {
		return eltRestUtilities.postRestObject(request, "/saveEltMasterConfig", eltMasterConfiguration, clientId);
	}

	@RequestMapping(value = "/masterConfig/{id}", method = RequestMethod.GET)
	public DataResponse getMasterConfig(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") Integer id, Locale locale, HttpServletRequest request) {
		return eltRestUtilities.getRestObject(request, "/masterConfig/{id}", clientId, id);
	}

	@RequestMapping(value = "/saveEltLoadParameters", method = RequestMethod.POST)
	public DataResponse saveEltLoadParameters(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody EltLoadParameters eltLoadParameters, HttpServletRequest request) {
		return eltRestUtilities.postRestObject(request, "/saveLoadParameters", eltLoadParameters, clientId);
	}

	@RequestMapping(value = "/getLoadParamtersInfo/{id}", method = RequestMethod.GET)
	public DataResponse getLoadParamtersInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long id, Locale locale, HttpServletRequest request) {
		return eltRestUtilities.getRestObject(request, "/getLoadParametersById/{id}", clientId, id);
	}

	@RequestMapping(value = "/deleteParamtersById/{id}", method = RequestMethod.DELETE)
	public DataResponse deleteParamtersById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long id, Locale locale, HttpServletRequest request) {
		return eltRestUtilities.deleteRestObject(request, "/deleteParamtersById/{id}", clientId, id);
	}

	@RequestMapping(value = "/getEltConfigTagsByID/{id}", method = RequestMethod.GET)
	public DataResponse getEltConfigTagsByID(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") Integer id, Locale locale, HttpServletRequest request) {
		return eltRestUtilities.getRestObject(request, "/getEltConfigTagsByID/{id}", clientId, id);
	}

	@RequestMapping(value = "/saveEltJobSequenceInfo", method = RequestMethod.POST)
	public DataResponse saveEltJobSequenceInfo(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody List<EltJobInfo> eltJobInfo, HttpServletRequest request) {
		return eltRestUtilities.postRestObject(request, "/saveEltJobSequenceInfo", eltJobInfo, clientId);
	}
	
	@RequestMapping(value = "/uploadELTPPkFile", method = RequestMethod.POST)
	public DataResponse uploadELTPPkFile(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
		File file = CommonUtils.multipartToFile(multipartFile);
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("file", file.getAbsolutePath());
		return eltRestUtilities.postRestObject(request, "/uploadELTPPkFile", map, clientId);
	}
	
	@RequestMapping(value = "/downloadPPkFile/{filePath}", method = RequestMethod.GET)
	public DataResponse downloadPPkFile(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("filePath") String filePath, HttpServletRequest request,HttpServletResponse response) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("filePath", filePath);
		try {
			String decryptPath = EncryptionServiceImpl.getInstance().decrypt(filePath);
			CommonUtils.sendFIleToStream(decryptPath, request, response);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
