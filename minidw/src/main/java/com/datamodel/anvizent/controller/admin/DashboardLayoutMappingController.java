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
import com.datamodel.anvizent.service.model.ClientDLMappingForm;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientSpecificILDLForm;
import com.datamodel.anvizent.service.model.CloudClient;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ETLAdmin;
import com.datamodel.anvizent.service.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author apurva.deshmukh
 *
 */
@Controller
@RequestMapping(value = "/admin/dashBoardLayoutMaster/clientMapping")
public class DashboardLayoutMappingController {
	protected static final Log LOGGER = LogFactory.getLog(DashboardLayoutMappingController.class);

	@Autowired
	@Qualifier("etlAdminServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("etlAdminServiceslbRestTemplateUtilities")
	private RestTemplateUtilities restUtilitieslb;

	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities packageRestUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userServicesRestTemplate;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView clientDLMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, @ModelAttribute("clientDLMappingForm") ClientDLMappingForm clientDLMappingForm,
			Locale locale) {
		CommonUtils.setActiveScreenName("dashBoardLayoutMasterclientMapping", session);
		mv.setViewName("tiles-anvizent-admin:clientDLMapping");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView clientDLMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientDLMappingForm") ClientDLMappingForm clientDLMappingForm,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in clientDLMapping()");

		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataResponse dataResponse = restUtilities.getRestObject(request,
					"/getVerticalMappedDLsByClientId/{client_id}", user.getUserId(), clientDLMappingForm.getClientId());

			if (dataResponse != null && dataResponse.getHasMessages()) {

				if (dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("success")) {

					List<LinkedHashMap<String, Object>> verticalMappedDLsObj = (List<LinkedHashMap<String, Object>>) dataResponse
							.getObject();
					// all DLs
					List<DLInfo> allDLsList = mapper.convertValue(verticalMappedDLsObj,
							new TypeReference<List<DLInfo>>() {
							});

					DataResponse allDLsdataResponse = restUtilities.getRestObject(request, "/getDlClientidMapping/{Id}",
							user.getUserId(), clientDLMappingForm.getClientId());
					List<LinkedHashMap<String, Object>> dLsList = (List<LinkedHashMap<String, Object>>) allDLsdataResponse
							.getObject();
					// client mapped DLs if any
					List<DLInfo> clientDLs = mapper.convertValue(dLsList, new TypeReference<List<DLInfo>>() {
					});

					if (allDLsList != null && allDLsList.size() > 0) {
						for (DLInfo allDLInfo : allDLsList) {
							for (DLInfo clientDL : clientDLs) {
								if (allDLInfo.getdL_id() == clientDL.getdL_id()) {
									allDLInfo.setCheckedDl(Boolean.TRUE);
									allDLInfo.setIsLocked(clientDL.getIsLocked());
								}
							}
						}
						mv.addObject("dLList", allDLsList);
					}
				} else {
					mv.addObject("messagecode", "FAILED");
					mv.addObject("errors", dataResponse.getMessages().get(0).getText());
				}
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
		mv.setViewName("tiles-anvizent-admin:clientDLMapping");
		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView saveClientDLMapping(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientDLMappingForm") ClientDLMappingForm clientDLMappingForm,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.debug("in saveClientDLMapping()");
		User user = CommonUtils.getUserDetails(request, null, null);
		try {

			List<DLInfo> dlInfoList = clientDLMappingForm.getdLInfo();
			List<DLInfo> dLInfoModifiedList = new ArrayList<>();

			if (dlInfoList != null) {
				for (DLInfo dl : dlInfoList) {
					if (dl.getCheckedDl()) {
						dLInfoModifiedList.add(dl);
					}
				}
			}

			ETLAdmin etlAdmin = new ETLAdmin();
			etlAdmin.setClientId(clientDLMappingForm.getClientId().toString());
			etlAdmin.setdLInfo(dLInfoModifiedList);

			DataResponse dataResponse = restUtilities.postRestObject(request, "/saveDlClientidMapping", etlAdmin,
					user.getUserId());

			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
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
		mv.setViewName("redirect:/admin/dashBoardLayoutMaster/clientMapping");
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

	@RequestMapping(value = "/specificDL", method = RequestMethod.GET)
	public ModelAndView clientSpecificDL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session,
			@ModelAttribute("clientSpecificILDLForm") ClientSpecificILDLForm clientSpecificILDLForm) {
		LOGGER.info("in clientSpecificDL()");
		CommonUtils.setActiveScreenName("specificDL", session);
		mv.setViewName("tiles-anvizent-admin:clientSpecificDL");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/specificDL", method = RequestMethod.POST)
	public ModelAndView clientSpecificDL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientSpecificILDLForm") ClientSpecificILDLForm clientSpecificILDLForm,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.info("in clientSpecificDL()");

		if (clientSpecificILDLForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/dashBoardLayoutMaster/clientMapping/specificDL");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			DataResponse object = restUtilities.getRestObject(request, "/getDLInfoByClientId/{client_Id}",
					user.getUserId(), clientSpecificILDLForm.getClientId());
			if (object != null && object.getHasMessages()) {

				if (object.getMessages().get(0).getCode().equals("SUCCESS")) {

					List<LinkedHashMap<String, Object>> data = (List<LinkedHashMap<String, Object>>) object.getObject();
					List<DLInfo> dLsInfo = mapper.convertValue(data, new TypeReference<List<DLInfo>>() {
					});
					Map<Object, Object> map = new HashMap<Object, Object>();
					for (DLInfo dL : dLsInfo) {
						map.put(dL.getdL_id(), dL.getdL_name());
					}
					mv.addObject("dLsInfo", map);

					if (clientSpecificILDLForm.getClientId() != 0 && clientSpecificILDLForm.getdLId() != null) {
						DataResponse dataResponse = restUtilities.getRestObject(request, "/getDLData/{dl_id}",
								user.getUserId(), clientSpecificILDLForm.getdLId());

						if (dataResponse != null && dataResponse.getHasMessages()) {

							if (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {

								LinkedHashMap<String, Object> obj = (LinkedHashMap<String, Object>) dataResponse
										.getObject();
								ETLAdmin etlAdmin = mapper.convertValue(obj, new TypeReference<ETLAdmin>() {
								});
								if (etlAdmin.getdLInfo() != null && etlAdmin.getdLInfo().size() > 0) {
									DLInfo dLInfo = etlAdmin.getdLInfo().get(0);

									clientSpecificILDLForm.setDefaultJobName(dLInfo.getJobName());
									clientSpecificILDLForm.setDefaultJobJarFileNames(dLInfo.getJobFileNames());
									clientSpecificILDLForm.setDefaultJobVersion(dLInfo.getVersion());

									MultiValueMap<Object, Object> multiValueMap = new LinkedMultiValueMap<>();
									multiValueMap.add("dLId", clientSpecificILDLForm.getdLId());
									multiValueMap.add("client_id", clientSpecificILDLForm.getClientId());
									DataResponse clientSpecificDataResponse = restUtilities.postRestObject(request,
											"/getClientSpecificDLInfoById", multiValueMap, user.getUserId());
									if (clientSpecificDataResponse != null
											&& clientSpecificDataResponse.getHasMessages() && clientSpecificDataResponse
													.getMessages().get(0).getCode().equals("SUCCESS")) {
										LinkedHashMap<String, Object> clientSpecificObj = (LinkedHashMap<String, Object>) clientSpecificDataResponse
												.getObject();
										DLInfo clientSpecificDLInfo = mapper.convertValue(clientSpecificObj,
												new TypeReference<DLInfo>() {
												});
										List<String> jobFileNamesList = new ArrayList<>();
										if (clientSpecificDLInfo.getJobFileNames() != null) {
											String[] jobFileNames = clientSpecificDLInfo.getJobFileNames().split(",");
											for (String fileName : jobFileNames) {
												jobFileNamesList.add(fileName);
											}
										}
										clientSpecificILDLForm.setCurrentClientJarFileNameList(jobFileNamesList);
										clientSpecificILDLForm
												.setCurrentClientJobName(clientSpecificDLInfo.getJobName());
										clientSpecificILDLForm
												.setCurrentClientJarFileNames(clientSpecificDLInfo.getJobFileNames());
										clientSpecificILDLForm.setUseDefault(clientSpecificDLInfo.getIsDefaultDL());
										clientSpecificILDLForm
												.setCurrentClientJobVersion(clientSpecificDLInfo.getVersion());
									}
									String type = "dl";
									mv.addObject("jarFilesList",
											CommonUtils.getAvailableJarsList(request, restUtilities, type));
								}

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
		mv.setViewName("tiles-anvizent-admin:clientSpecificDL");
		return mv;
	}

	@RequestMapping(value = "/specificDL/save", method = RequestMethod.POST)
	public ModelAndView saveClientSpecificDL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("clientSpecificILDLForm") ClientSpecificILDLForm clientSpecificILDLForm,
			RedirectAttributes redirectAttributes, Locale locale) {
		LOGGER.info("in saveClientSpecificDL()");

		if (clientSpecificILDLForm.getClientId() == null) {
			redirectAttributes.addFlashAttribute("errors",
					messageSource.getMessage("anvizent.message.error.text.clientNameShouldNotbeEmpty", null, locale));
			mv.setViewName("redirect:/admin/dashBoardLayoutMaster/clientMapping/specificDL");
			return mv;
		}
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			redirectAttributes.addFlashAttribute("messagecode", "ERROR");
			redirectAttributes.addFlashAttribute("errors", messageSource
					.getMessage("anvizent.message.error.text.errorWhileSavingClientSpecificDLInfo", null, locale));
			DataResponse dataResponse = null;
			ClientData clientData = new ClientData();
			DLInfo dlInfo = new DLInfo();
			dlInfo.setdL_id(clientSpecificILDLForm.getdLId());
			clientData.setUserId(clientSpecificILDLForm.getClientId().toString());
			dlInfo.setJobName(clientSpecificILDLForm.getClientSpecificJobName());
			dlInfo.setJobFileNames(clientSpecificILDLForm.getClientSpecificJobJarFileNames());
			dlInfo.setVersion(clientSpecificILDLForm.getClientSpecificJobVersion());
			clientData.setDlInfo(dlInfo);
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
					dataResponse = restUtilities.postRestObject(request, "/saveClientSpecificDLInfo", clientData,
							user.getUserId());
				} else if (clientSpecificILDLForm.getCurrentClientJobName() != null
						&& clientSpecificILDLForm.getCurrentClientJarFileNames() != null
						&& !clientSpecificILDLForm.getUseDefault()) {
					dataResponse = restUtilities.postRestObject(request, "/updateClientSpecificDLInfo", clientData,
							user.getUserId());
				}
			}

			if (clientSpecificILDLForm.getCurrentClientJobName() != null
					&& clientSpecificILDLForm.getCurrentClientJarFileNames() != null
					&& clientSpecificILDLForm.getUseDefault()) {
				dataResponse = restUtilities.postRestObject(request, "/updateclientSpecificDLToDefault", clientData,
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
		mv.setViewName("redirect:/admin/dashBoardLayoutMaster/clientMapping/specificDL");
		return mv;
	}

	@RequestMapping(value = "/save", method = RequestMethod.GET)
	public ModelAndView handleGetRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {
		mv.setViewName("redirect:/admin/dashBoardLayoutMaster/clientMapping");
		return mv;
	}

	@RequestMapping(value = "/specificDL/save", method = RequestMethod.GET)
	public ModelAndView handleClientSpecificDLGetRequest(HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv) {
		mv.setViewName("redirect:/admin/dashBoardLayoutMaster/clientMapping/specificDL");
		return mv;
	}
}
