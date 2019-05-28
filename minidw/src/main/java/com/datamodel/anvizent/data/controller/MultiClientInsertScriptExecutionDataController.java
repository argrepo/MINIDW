/*
package com.datamodel.anvizent.data.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

*//**
 * @author rajesh.anthari
 *
 *//*
@RestController("MultiClientInsertScriptExecutionDataController")
@RequestMapping("" + Constants.AnvizentURL.MINIDW_ADMIN_BASE_URL + "/etlAdmin/multiClientInsertScriptExcecution")

public class MultiClientInsertScriptExecutionDataController {

	protected static final Log LOGGER = LogFactory.getLog(MultiClientInsertScriptExecutionDataController.class);
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;
	
	@Autowired
	@Qualifier("multiClientInsertScriptExecRestTemplateUtilities")
	private RestTemplateUtilities clientInsertRestUtilities;
	
	@Autowired
	private MessageSource messageSource;

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


}
*/