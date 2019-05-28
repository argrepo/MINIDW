package com.datamodel.anvizent.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientSpecificILDLForm;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author rakesh.gajula
 *
 */
@Controller
@RequestMapping(value = "/admin/iLInfo/clientMapping")
public class ILInfoMappingController {
	protected static final Log LOGGER = LogFactory.getLog(ILInfoMappingController.class);

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
	private MessageSource messageSource;

	@RequestMapping(value = "/specificIL", method = RequestMethod.GET)
	public ModelAndView clientSpecificIL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session,
			@ModelAttribute("clientSpecificILDLForm") ClientSpecificILDLForm clientSpecificILDLForm) {
		LOGGER.info("in clientSpecificIL()");
		CommonUtils.setActiveScreenName("specificIL", session);
		mv.setViewName("tiles-anvizent-admin:clientSpecificIL");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/specificIL", method = RequestMethod.POST)
	public ModelAndView clientSpecificIL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientSpecificILDLForm") ClientSpecificILDLForm clientSpecificILDLForm,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.info("in clientSpecificIL()");

		if (clientSpecificILDLForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/iLInfo/clientMapping/specificIL");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataResponse object = restUtilities.getRestObject(request, "/getILInfoByClientId/{client_Id}",
					user.getUserId(), clientSpecificILDLForm.getClientId());
			if (object != null && object.getHasMessages()) {
				if (object.getMessages().get(0).getCode().equals("SUCCESS")) {
					List<LinkedHashMap<String, Object>> data = (List<LinkedHashMap<String, Object>>) object.getObject();
					List<ILInfo> iLsInfo = mapper.convertValue(data, new TypeReference<List<ILInfo>>() {
					});
					Map<Object, Object> map = new HashMap<Object, Object>();
					for (ILInfo iL : iLsInfo) {
						map.put(iL.getiL_id(), iL.getiL_name());
					}
					mv.addObject("iLsInfo", map);

					if (clientSpecificILDLForm.getClientId() != 0 && clientSpecificILDLForm.getiLId() != null) {

						MultiValueMap<Object, Object> multiValueMap = new LinkedMultiValueMap<>();
						multiValueMap.add("iLId", clientSpecificILDLForm.getiLId());
						multiValueMap.add("client_id", clientSpecificILDLForm.getClientId());

						DataResponse dataResponse = restUtilities.postRestObject(request, "/getILInfoById",
								multiValueMap, user.getUserId());
						if (dataResponse != null && dataResponse.getHasMessages()) {
							if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
								LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataResponse
										.getObject();
								ETLAdmin etlAdmin = mapper.convertValue(obj, new TypeReference<ETLAdmin>() {
								});
								ILInfo iLInfo = etlAdmin.getIlInfo();

								clientSpecificILDLForm.setDefaultJobName(iLInfo.getJobName());
								clientSpecificILDLForm.setDefaultJobJarFileNames(iLInfo.getJobFileNames());
								clientSpecificILDLForm.setDefaultJobVersion(iLInfo.getVersion());

								DataResponse clientSpecificDataResponse = restUtilities.postRestObject(request,
										"/getClientSpecificILInfoById", multiValueMap, user.getUserId());
								if (clientSpecificDataResponse != null && clientSpecificDataResponse.getHasMessages()
										&& clientSpecificDataResponse.getMessages().get(0).getCode()
												.equals("SUCCESS")) {
									LinkedHashMap<String, Object> clientSpecificObj = (LinkedHashMap<String, Object>) clientSpecificDataResponse
											.getObject();
									ILInfo clientSpecificILInfo = mapper.convertValue(clientSpecificObj,
											new TypeReference<ILInfo>() {
											});
									List<String> jobFileNamesList = new ArrayList<>();
									if (clientSpecificILInfo.getJobFileNames() != null) {
										String[] jobFileNames = clientSpecificILInfo.getJobFileNames().split(",");
										for (String fileName : jobFileNames) {
											jobFileNamesList.add(fileName);
										}
									}
									clientSpecificILDLForm.setCurrentClientJarFileNameList(jobFileNamesList);
									clientSpecificILDLForm.setCurrentClientJobName(clientSpecificILInfo.getJobName());
									clientSpecificILDLForm
											.setCurrentClientJarFileNames(clientSpecificILInfo.getJobFileNames());
									clientSpecificILDLForm.setUseDefault(clientSpecificILInfo.getIsDefaultIL());
									clientSpecificILDLForm
											.setCurrentClientJobVersion(clientSpecificILInfo.getVersion());
								}
								String type = "il";
								mv.addObject("jarFilesList",
										CommonUtils.getAvailableJarsList(request, restUtilities, type));
							} else {
								redirectAttributes.addFlashAttribute("messagecode",
										dataResponse.getMessages().get(0).getCode());
								redirectAttributes.addFlashAttribute("errors",
										dataResponse.getMessages().get(0).getText());
							}
						}
					}
				} else {
					redirectAttributes.addFlashAttribute("messagecode", object.getMessages().get(0).getCode());
					redirectAttributes.addFlashAttribute("errors", object.getMessages().get(0).getText());
				}
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors",
						messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		mv.setViewName("tiles-anvizent-admin:clientSpecificIL");
		return mv;
	}

	@RequestMapping(value = "/specificIL/save", method = RequestMethod.POST)
	public ModelAndView saveClientSpecificIL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientSpecificILDLForm") ClientSpecificILDLForm clientSpecificILDLForm,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.info("in saveClientSpecificIL()");

		if (clientSpecificILDLForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/iLInfo/clientMapping/specificIL");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			redirectAttributes.addFlashAttribute("messagecode", "ERROR");
			redirectAttributes.addFlashAttribute("errors", messageSource
					.getMessage("anvizent.message.error.text.errorWhileSavingClientSpecificILInfo", null, locale));
			DataResponse dataResponse = null;
			ClientData clientData = new ClientData();
			ILInfo ilInfo = new ILInfo();
			ilInfo.setiL_id(clientSpecificILDLForm.getiLId());
			clientData.setUserId(clientSpecificILDLForm.getClientId().toString());
			ilInfo.setJobName(clientSpecificILDLForm.getClientSpecificJobName());
			ilInfo.setJobFileNames(clientSpecificILDLForm.getClientSpecificJobJarFileNames());
			ilInfo.setVersion(clientSpecificILDLForm.getClientSpecificJobVersion());
			clientData.setIlInfo(ilInfo);
			boolean isUploaded = false;

			List<FileSystemResource> multiPartFiles = new ArrayList<>();
			if (clientSpecificILDLForm.getJobJarFiles() != null && clientSpecificILDLForm.getJobJarFiles().size() > 0) {
				String tempFolderName = Constants.TempUpload.getTempFileDir(user.getUserId());
				clientSpecificILDLForm.getJobJarFiles().forEach(file -> {
					try {
						String tempFileName = tempFolderName + file.getOriginalFilename();
						CommonUtils.createFile(tempFolderName, tempFileName, file);
						multiPartFiles.add(new FileSystemResource(tempFileName));
					} catch (Exception e) {
						System.out.println("error while " + e.getMessage());
					}
				});
			}

			if (multiPartFiles.size() > 0) {
				MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
				multiPartFiles.forEach(multiPartFile -> {
					map.add("files", multiPartFile);
				});

				DataResponse uploadedFileResonse = restUtilitieslb.postRestObject(request, "/uploadIlOrDlFiles", map,
						user.getUserId());
				if (uploadedFileResonse.getHasMessages()
						&& uploadedFileResonse.getMessages().get(0).getCode().equals("SUCCESS")) {
					isUploaded = true;
				}
			}

			if (isUploaded) {
				if (clientSpecificILDLForm.getCurrentClientJobName() == null
						&& clientSpecificILDLForm.getCurrentClientJarFileNames() == null
						&& !clientSpecificILDLForm.getUseDefault()) {
					dataResponse = restUtilities.postRestObject(request, "/saveclientSpecificIL", clientData,
							user.getUserId());
				} else if (clientSpecificILDLForm.getCurrentClientJobName() != null
						&& clientSpecificILDLForm.getCurrentClientJarFileNames() != null
						&& !clientSpecificILDLForm.getUseDefault()) {
					dataResponse = restUtilities.postRestObject(request, "/updateclientSpecificIL", clientData,
							user.getUserId());
				}
			}

			if (clientSpecificILDLForm.getCurrentClientJobName() != null
					&& clientSpecificILDLForm.getCurrentClientJarFileNames() != null
					&& clientSpecificILDLForm.getUseDefault()) {
				dataResponse = restUtilities.postRestObject(request, "/updateclientSpecificILToDefault", clientData,
						user.getUserId());
			}

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			e.printStackTrace();
		}
		clientSpecificILDLForm.setClientId(0);
		mv.setViewName("redirect:/admin/iLInfo/clientMapping/specificIL");
		return mv;
	}

	@SuppressWarnings({ "unchecked" })
	@ModelAttribute("allClients")
	public Map<Object, Object> getAllClientInfo(HttpServletRequest request) {

		// TODO
		long clientId = Long
				.valueOf((String) SessionHelper.getSesionAttribute(request, Constants.Config.HEADER_CLIENT_ID));
		User user = CommonUtils.getUserDetails(request, null, null);

		String activeClientsUrl = "/activeClients";
		DataResponse dataResponse = userServicesRestTemplate.postRestObject(request, activeClientsUrl,
				new LinkedMultiValueMap<>(), DataResponse.class, user.getUserId());
		Map<Object, Object> clientMap = new LinkedHashMap<>();
		if (dataResponse != null) {
			List<Map<String, Object>> clientList = (List<Map<String, Object>>) dataResponse.getObject();
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<CloudClient> cloudClients = mapper.convertValue(clientList, new TypeReference<List<CloudClient>>() {
			});

			if (cloudClients == null || cloudClients.size() == 0) {
				return new HashMap<>();
			}

			for (CloudClient cloudClient : cloudClients) {
				// TODO Add this condition
				if (clientId == cloudClient.getId()) {
					clientMap.put(cloudClient.getId(), cloudClient.getId() + " - " + cloudClient.getClientName());
				}
			}
		}
		return clientMap;
	}

	@RequestMapping(value = "/specificIL/save", method = RequestMethod.GET)
	public ModelAndView handleClientSpecificDLGetRequest(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv) {
		mv.setViewName("redirect:/admin/iLInfo/clientMapping/specificIL");
		return mv;
	}

}
