package com.datamodel.anvizent.data.RestController;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.ELTLoadParametersService;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.EltLoadParameters;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;

@RestController("EltLoadParametersDataRestController")
@RequestMapping("" + Constants.AnvizentURL.ADMIN_SERVICES_BASE_URL + "/eltconfig")
@CrossOrigin
public class EltLoadParametersDataRestController {

	protected static final Log LOGGER = LogFactory.getLog(EltLoadParametersDataRestController.class);

	@Autowired
	MessageSource messageSource;
	@Autowired
	ELTLoadParametersService eltLoadParametersService;

	@RequestMapping(value="/getLoadParametersList", method = RequestMethod.GET)
	public DataResponse getLoadParametersList(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		List<EltLoadParameters> eltLoadPatametersList = null;
		try {
			eltLoadPatametersList = eltLoadParametersService.getLoadParametersList(null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(eltLoadPatametersList);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value="/saveLoadParameters", method = RequestMethod.POST)
	public DataResponse saveLoadParameters(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@RequestBody EltLoadParameters eltLoadParameters, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			Modification modification = new Modification(new Date());
			modification.setCreatedBy(clientId);
			eltLoadParameters.setModification(modification);
			eltLoadParametersService.saveLoadParameters(eltLoadParameters, null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText(messageSource.getMessage("anvizent.message.success.text.savedSuccessfully", null, locale));
			dataResponse.setObject(eltLoadParameters);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/getLoadParametersById/{id}", method = RequestMethod.GET)
	public DataResponse getLoadParametersById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long id, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		EltLoadParameters eltLoadParameters = null;
		try {
			eltLoadParameters = eltLoadParametersService.getLoadParametersById(id, null);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			dataResponse.setObject(eltLoadParameters);
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

	@RequestMapping(value = "/deleteParamtersById/{id}", method = RequestMethod.DELETE)
	public DataResponse deleteLoadParametersById(@PathVariable(Constants.PathVariables.CLIENT_ID) String clientId,
			@PathVariable("id") long id, HttpServletRequest request, Locale locale) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		try {
			EltLoadParameters loadParametersById = eltLoadParametersService.deleteLoadParametersById(id, null);
			dataResponse.setObject(loadParametersById);
			message.setCode(com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.SUCCESS);
			message.setText("Deleted successfully");
		} catch (Throwable t) {
			MinidwServiceUtil.getErrorMessage(message, "ERROR", t);
		}
		return dataResponse;
	}

}
