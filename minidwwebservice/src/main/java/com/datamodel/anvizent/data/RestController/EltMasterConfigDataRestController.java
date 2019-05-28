package com.datamodel.anvizent.data.RestController;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.ELTMasterConfigService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.EltMasterConfiguration;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;

@RestController("EltMasterConfigDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ADMIN_SERVICES_BASE_URL + "/eltconfig")
@CrossOrigin
public class EltMasterConfigDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(EltMasterConfigDataRestController.class);

	@Autowired
	MessageSource messageSource;
	@Autowired
	ELTMasterConfigService eltMasterConfigService;

	@RequestMapping(value = "/masterConfig", method = RequestMethod.GET)
	public DataResponse getAllEltMasterConfigs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<EltMasterConfiguration> eltMasterConfigurationList = null;
		try {
			eltMasterConfigurationList = eltMasterConfigService.getAllMasterConfigList(null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(eltMasterConfigurationList);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/saveEltMasterConfig", method = RequestMethod.POST)
	public DataResponse saveEltMasterConfig(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody EltMasterConfiguration eltMasterConfiguration, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			eltMasterConfiguration.setModification(modification);
			eltMasterConfigService.saveMasterConfig(eltMasterConfiguration, null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			dataResponse.setObject(eltMasterConfiguration);
		} catch (Throwable t) {
			LOGGER.error("While saving " , t);
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/masterConfig/{id}", method = RequestMethod.GET)
	public DataResponse getAllEltMasterConfigs(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long configId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		EltMasterConfiguration eltMasterConfiguration = null;
		try {
			eltMasterConfiguration = eltMasterConfigService.getMasterConfigById(configId, null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(eltMasterConfiguration);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/masterConfig/{id}", method = RequestMethod.DELETE)
	public DataResponse deleteEltMasterConfig(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long configId, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			EltMasterConfiguration masterConfigById = eltMasterConfigService.deleteMasterConfigById(configId, null);
			dataResponse.setObject(masterConfigById);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("Deleted successfully");
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}
	
	@RequestMapping(value = "/uploadELTPPkFile", method = RequestMethod.POST)
	public DataResponse uploadELTPPkFile(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestParam("file") String file, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(file);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}


	
}
