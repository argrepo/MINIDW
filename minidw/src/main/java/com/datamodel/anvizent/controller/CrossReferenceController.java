package com.datamodel.anvizent.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datamodel.anvizent.data.controller.RestTemplateUtilities;
import com.datamodel.anvizent.helper.SessionHelper;
import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.CrossReferenceForm;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobResult;
import com.datamodel.anvizent.service.model.JobResultForm;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.spring.AppProperties;
import com.datamodel.anvizent.validator.CustomPackageFormValidator;
import com.datamodel.anvizent.validator.StandardPackageFormValidator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author rajesh.anthari
 *
 */
@Controller
@Import(AppProperties.class)
@RequestMapping(value = "/adt/crossReference")
public class CrossReferenceController {

	protected static final Log LOGGER = LogFactory.getLog(CrossReferenceController.class);

	String homeRedirectUrl = "/adt/crossReference";
	@Autowired
	@Qualifier("anvizentServicesRestTemplateUtilities")
	private RestTemplateUtilities restUtilities;

	@Autowired
	@Qualifier("crossReferenceRestTemplateUtilities")
	private RestTemplateUtilities crossRefRestUtilities;

	@Autowired
	@Qualifier("userServicesRestTemplateUtilities")
	private RestTemplateUtilities userRestUtilities;

	@Autowired
	StandardPackageFormValidator spValidator;
	@Autowired
	CustomPackageFormValidator cpValidator;
	@Autowired
	private MessageSource messageSource;
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView crossReference(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			HttpSession session, Locale locale) {
		CommonUtils.setActiveScreenName("crossReference", session);
		mv.setViewName("tiles-anvizent-entry:crossReference");
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getIlList", method = RequestMethod.GET)
	public ModelAndView getIlList(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, HttpSession session, Locale locale) {
		CommonUtils.setActiveScreenName("crossReference", session);

		if (crossReferenceForm.getIlId() != null && crossReferenceForm.getIlId() != 0) {
			if (StringUtils.isNotBlank(crossReferenceForm.getCrossReferenceOption())) {
				if (crossReferenceForm.getCrossReferenceOption().equals("merge") || crossReferenceForm.getCrossReferenceOption().equals("split")) {
					mv.addObject("columnsNames", session.getAttribute("columnsNames"));

					mv.addObject("columnsValues", new HashMap<>());
					if (StringUtils.isNotBlank(crossReferenceForm.getIlColumnName()) && !crossReferenceForm.getIlColumnName().equals("0")
							&& crossReferenceForm.getCrossReferenceOption().equals("split")) {
						mv.addObject("columnsValues", session.getAttribute("columnsValues"));
					}

					mv.addObject("matchedRecordsListData", new HashMap<>());
					if (crossReferenceForm.getIlColumnValue() != null && crossReferenceForm.getIlColumnValue().size() > 0) {
						mv.addObject("columnsValues", session.getAttribute("columnsValues"));
						mv.addObject("columnSize", ((HashMap<Object, Object>) session.getAttribute("columnSize")).get(crossReferenceForm.getIlColumnName()));
						mv.addObject("columnDataType",
								((HashMap<Object, Object>) session.getAttribute("columnDataTypes")).get(crossReferenceForm.getIlColumnName()));
						mv.addObject("matchedRecordsListData", session.getAttribute("matchedRecordsListData"));
					}

					// getting existing Xref
					if (StringUtils.isNotBlank(crossReferenceForm.getIlColumnName()) && !crossReferenceForm.getIlColumnName().equals("0")
							&& crossReferenceForm.getCrossReferenceOption().equals("merge") && crossReferenceForm.getNewOrExistingXref() != null) {
						if (crossReferenceForm.getNewOrExistingXref().equals("existingXref")) {
							mv.addObject("existingXref", session.getAttribute("existingXref"));
						}
					}

					// getting existing Xref record
					if (StringUtils.isNotBlank(crossReferenceForm.getIlColumnName()) && !crossReferenceForm.getIlColumnName().equals("0")
							&& crossReferenceForm.getCrossReferenceOption().equals("merge") && crossReferenceForm.getNewOrExistingXref() != null) {
						if (crossReferenceForm.getNewOrExistingXref().equals("existingXref") && crossReferenceForm.getExistingXrefValue() != null) {
							mv.addObject("existingXrefRecords", session.getAttribute("existingXrefRecords"));
						}
					}
				}
			}

		}
		mv.setViewName("tiles-anvizent-entry:crossReference");
		return mv;
	}

	@RequestMapping(value = "/getIlList", method = RequestMethod.POST)
	public ModelAndView xReferenceIL(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, RedirectAttributes redirectAttributes, HttpSession session,
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getIlId() != null && crossReferenceForm.getIlId() != 0) {
				if (StringUtils.isNotBlank(crossReferenceForm.getCrossReferenceOption())) {
					if (crossReferenceForm.getCrossReferenceOption().equals("merge") || crossReferenceForm.getCrossReferenceOption().equals("bulkmerge") || crossReferenceForm.getCrossReferenceOption().equals("split")) {
						ILInfo ilInfo = restUtilities.getRestObject(request, "/getILById/{iLId}", ILInfo.class, user.getUserId(), crossReferenceForm.getIlId());
						crossReferenceForm.setIlName(ilInfo.getiL_name());
						String tableName = ilInfo.getiL_table_name();
						if (crossReferenceForm.getCrossReferenceOption().equals("split")) {
							tableName = ilInfo.getXref_il_table_name();
						}
						DataResponse columnsResponse = restUtilities.getRestObject(request, "/getTablesStructure/{industryId}/{dLOrILName}", user.getUserId(),
								0, tableName);
						List<Column> ilColumnsList = null;

						if (columnsResponse != null && columnsResponse.getHasMessages() && columnsResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
							ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
							ilColumnsList = mapper.convertValue(columnsResponse.getObject(), new TypeReference<List<Column>>() {
							});
						}
						List<String> exceptionColumnsList = new ArrayList<>();
						exceptionColumnsList.add("DataSource_Id");
						exceptionColumnsList.add("Added_Date");
						exceptionColumnsList.add("Added_User");
						exceptionColumnsList.add("Updated_Date");
						exceptionColumnsList.add("Updated_User");

						Map<String, String> columnsNames = new LinkedHashMap<>();
						Map<String, String> columnSize = new HashMap<>();
						Map<String, String> columnDataTypes = new HashMap<>();
						if (ilColumnsList != null) {
							ilColumnsList.forEach(col -> {
								if (!(col.getIsAutoIncrement() || col.getColumnName().startsWith("XRef_")
										|| exceptionColumnsList.indexOf(col.getColumnName()) != -1)) {
									columnsNames.put(col.getColumnName(), col.getColumnName());
									columnSize.put(col.getColumnName(), col.getColumnSize());
									columnDataTypes.put(col.getColumnName(), col.getDataType());
								}
							});
						}
						session.setAttribute("columnsNames", columnsNames);
						session.setAttribute("columnSize", columnSize);
						session.setAttribute("columnDataTypes", columnDataTypes);
						session.setAttribute("xrefIlTableName", ilInfo.getXref_il_table_name());
						crossReferenceForm.setIlColumnName(null);
						crossReferenceForm.setIlColumnValue(new ArrayList<>());
					}else{
						ILInfo ilInfo = restUtilities.getRestObject(request, "/getILById/{iLId}", ILInfo.class, user.getUserId(), crossReferenceForm.getIlId());
						String xrefIlTableName = ilInfo.getXref_il_table_name();
						crossReferenceForm.setIlName(ilInfo.getiL_name());
						MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
						map.add("xrefIlTableName", xrefIlTableName);
						DataResponse dataResponse = crossRefRestUtilities.postRestObject(request, "/getXrefTableRecordsWithMap", map, user.getUserId());
						if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) { 
							Map<String, Object> matchedRecordsListData = (Map<String, Object>) dataResponse.getObject();
							crossReferenceForm.setAutoincrementColumnName(matchedRecordsListData.get("autoincrement_column_name").toString());
							crossReferenceForm.setxRefKeyColumnName(matchedRecordsListData.get("xRefKeyColumnName").toString());
							SessionHelper.setSesionAttribute(request, "matchedRecordsListData", dataResponse.getObject());
							session.setAttribute("xrefIlTableName", ilInfo.getXref_il_table_name());
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("",e);
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		redirectAttributes.addFlashAttribute("crossReferenceForm", crossReferenceForm);
		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getIlList/columnsValues", method = RequestMethod.POST)
	public ModelAndView getXrefILColumnsValues(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, RedirectAttributes redirectAttributes, HttpSession session,
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getIlId() != null && crossReferenceForm.getIlId() != 0) {
				if (StringUtils.isNotBlank(crossReferenceForm.getCrossReferenceOption())) {
					if (crossReferenceForm.getCrossReferenceOption().equals("merge") || crossReferenceForm.getCrossReferenceOption().equals("split")) {
						if (StringUtils.isNotBlank(crossReferenceForm.getIlColumnName()) && !crossReferenceForm.getIlColumnName().equals("0")
								&& crossReferenceForm.getCrossReferenceOption().equals("split")) {
							MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
							map.add("columnName", crossReferenceForm.getIlColumnName());
							map.add("iLId", crossReferenceForm.getIlId());
							map.add("isXrefValueNull", crossReferenceForm.getCrossReferenceOption().equals("merge"));
							DataResponse dataResponse = crossRefRestUtilities.postRestObject(request, "/getDistinctValues", map, user.getUserId());
							if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
								List<Object> values = (List<Object>) dataResponse.getObject();
								Map<Object, Object> columnsValues = new LinkedHashMap<>();
								if (values != null) {
									values.forEach(val -> {
										columnsValues.put(StringUtils.replace(val.toString(), ",", "#$#$#$"), val);
									});
								}
								session.setAttribute("columnsValues", columnsValues);
							}
							crossReferenceForm.setIlColumnValue(new ArrayList<>());
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("",e);;
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		redirectAttributes.addFlashAttribute("crossReferenceForm", crossReferenceForm);
		return mv;
	}

	@RequestMapping(value = "/getIlList/records", method = RequestMethod.POST)
	public ModelAndView getXrefILColumnrecords(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, RedirectAttributes redirectAttributes, HttpSession session,
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getIlId() != null && crossReferenceForm.getIlId() != 0) {
				if (StringUtils.isNotBlank(crossReferenceForm.getCrossReferenceOption())) {
					if (crossReferenceForm.getCrossReferenceOption().equals("merge") || crossReferenceForm.getCrossReferenceOption().equals("split")) {
						if (crossReferenceForm.getIlColumnValue() != null && crossReferenceForm.getIlColumnValue().size() > 0) {

							if (crossReferenceForm.getCrossReferenceOption().equals("merge")) {
								Map<Object, Object> columnsValues = new LinkedHashMap<>();
								int valueIndex = crossReferenceForm.getIlColumnValue().size();
								for (int i = 0; i < valueIndex; i++) {
									columnsValues.put(crossReferenceForm.getIlColumnValue().get(i),
											StringUtils.replace(crossReferenceForm.getIlColumnValue().get(i), "#$#$#$", ","));
								}
								session.setAttribute("columnsValues", columnsValues);
							}
							
							fetchMarchedRecords(crossReferenceForm, request, user);

							
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("",e);;
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		redirectAttributes.addFlashAttribute("crossReferenceForm", crossReferenceForm);
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	void fetchMarchedRecords(CrossReferenceForm crossReferenceForm,HttpServletRequest request,User user) {
		MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
		map.add("columnName", crossReferenceForm.getIlColumnName());
		map.add("iLId", crossReferenceForm.getIlId());
		map.add("columnValues", String.join(",", crossReferenceForm.getIlColumnValue().toArray(new String[0])));
		map.add("isXrefValueNull", crossReferenceForm.getCrossReferenceOption().equals("merge"));
		DataResponse dataResponse = crossRefRestUtilities.postRestObject(request, "/getMatchedValues", map, user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			Map<String, Object> matchedRecordsListData = (Map<String, Object>) dataResponse.getObject();
			crossReferenceForm.setAutoincrementColumnName(matchedRecordsListData.get("autoincrement_column_name").toString());
			crossReferenceForm.setxRefKeyColumnName(matchedRecordsListData.get("xRefKeyColumnName").toString());
			SessionHelper.setSesionAttribute(request, "matchedRecordsListData", dataResponse.getObject());
		}
	}

	@RequestMapping(value = "/getIlList/existingXref", method = RequestMethod.POST)
	public ModelAndView getXrefILrecordsForExistingXref(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, RedirectAttributes redirectAttributes, HttpSession session,
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getIlId() != null && crossReferenceForm.getIlId() != 0) {
				if (StringUtils.isNotBlank(crossReferenceForm.getCrossReferenceOption())) {
					if (crossReferenceForm.getCrossReferenceOption().equals("merge") || crossReferenceForm.getCrossReferenceOption().equals("split")) {
						// getting existing Xref
						if (StringUtils.isNotBlank(crossReferenceForm.getIlColumnName()) && !crossReferenceForm.getIlColumnName().equals("0")
								&& crossReferenceForm.getCrossReferenceOption().equals("merge") && crossReferenceForm.getNewOrExistingXref() != null) {
							if (crossReferenceForm.getNewOrExistingXref().equals("existingXref")) {
								MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
								map.add("columnName", crossReferenceForm.getIlColumnName());
								map.add("iLId", crossReferenceForm.getIlId());
								map.add("isXrefValueNull", false);
								DataResponse dataResponse = crossRefRestUtilities.postRestObject(request, "/getDistinctValues", map, user.getUserId());
								if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
									session.setAttribute("existingXref", dataResponse.getObject());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("",e);;
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		redirectAttributes.addFlashAttribute("crossReferenceForm", crossReferenceForm);
		return mv;
	}

	@RequestMapping(value = "/getIlList/existingXrefRecord", method = RequestMethod.POST)
	public ModelAndView getExistingXrefRecord(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, RedirectAttributes redirectAttributes, HttpSession session,
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getIlId() != null && crossReferenceForm.getIlId() != 0) {
				if (StringUtils.isNotBlank(crossReferenceForm.getCrossReferenceOption())) {
					if (crossReferenceForm.getCrossReferenceOption().equals("merge") || crossReferenceForm.getCrossReferenceOption().equals("split")) {
						// getting existing Xref record
						if (StringUtils.isNotBlank(crossReferenceForm.getIlColumnName()) && !crossReferenceForm.getIlColumnName().equals("0")
								&& crossReferenceForm.getCrossReferenceOption().equals("merge") && crossReferenceForm.getNewOrExistingXref() != null) {
							if (crossReferenceForm.getNewOrExistingXref().equals("existingXref") && crossReferenceForm.getExistingXrefValue() != null) {
								MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
								map.add("tableName", session.getAttribute("xrefIlTableName"));
								map.add("columnName", crossReferenceForm.getIlColumnName());
								map.add("existingXrefValue", crossReferenceForm.getExistingXrefValue());
								DataResponse dataResponse = crossRefRestUtilities.postRestObject(request, "/getExistingXrefRecord", map, user.getUserId());
								if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
									session.setAttribute("existingXrefRecords", dataResponse.getObject());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("",e);;
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		redirectAttributes.addFlashAttribute("crossReferenceForm", crossReferenceForm);
		return mv;
	}

	@RequestMapping(value = "/unMergeSelectedCrossReferenceRecord", method = RequestMethod.POST)
	public ModelAndView unMergeSelectedCrossReferenceRecord(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, RedirectAttributes redirectAttributes, HttpSession session,
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getIlId() != null && crossReferenceForm.getIlId() != 0) {
				if (StringUtils.isNotBlank(crossReferenceForm.getCrossReferenceOption())) {
					MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
					
					map.add("iLId", crossReferenceForm.getIlId());
					map.add("ilColumnValue", crossReferenceForm.getIlValue());
					map.add("autoincrementColumnName", crossReferenceForm.getAutoincrementColumnName());
					map.add("xRefKeyColumnName", crossReferenceForm.getxRefKeyColumnName());
					DataResponse dataResponse = crossRefRestUtilities.postRestObject(request, "/unMergeSelectedCrossReferenceRecord", map, user.getUserId());
					if (dataResponse != null && dataResponse.getHasMessages()) {
						redirectAttributes.addFlashAttribute("messagecode", "SUCCESS");
						redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.success.recordsProcessedSuccessfully", null, locale));
					} else {
						redirectAttributes.addFlashAttribute("messagecode", "FAILED");
						redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.message.error.text.unmergingFailed", null, locale));
					}
					fetchMarchedRecords(crossReferenceForm, request, user);
				}
			}
		} catch (Exception e) {
			LOGGER.error("",e);;
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		redirectAttributes.addFlashAttribute("crossReferenceForm", crossReferenceForm);
		return mv;
	}

	@RequestMapping(value = "/mergeRecords", method = RequestMethod.POST)
	public ModelAndView mergeRecords(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, BindingResult result, Locale locale,
			RedirectAttributes redirectAttributes) {
		try {
			User user = CommonUtils.getUserDetails(request, null, null);
			DataResponse dataResponse = null;
			if (crossReferenceForm.getNewOrExistingXref().equals("newXref")) {
				MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
				map.add("columnName", crossReferenceForm.getIlColumnName());
				map.add("iLId", crossReferenceForm.getIlId());
				map.add("crossReferenceOption", crossReferenceForm.getCrossReferenceOption());
				map.add("ilColumnValue", crossReferenceForm.getIlColumnValue());
				map.add("newOrExistingXref", crossReferenceForm.getNewOrExistingXref());
				map.add("mergeColumnValues", String.join(",", crossReferenceForm.getIlMergeColumns().toArray(new String[0])));
				map.add("xReferenceColumnValue", crossReferenceForm.getIlXreferenceColumn());
				map.add("autoincrementColumnName", crossReferenceForm.getAutoincrementColumnName());
				map.add("xRefKeyColumnName", crossReferenceForm.getxRefKeyColumnName());
				map.add("ilXreferenceValue", crossReferenceForm.getIlXreferenceValue());
				dataResponse = crossRefRestUtilities.postRestObject(request, "/mergeCrossReferenceRecords", map, user.getUserId());
			} else {
				MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
				map.add("iLId", crossReferenceForm.getIlId());
				map.add("existingXrefKey", crossReferenceForm.getExistingXrefKey());
				map.add("mergeColumnValues", String.join(",", crossReferenceForm.getIlMergeColumns().toArray(new String[0])));
				map.add("xRefKeyColumnName", crossReferenceForm.getxRefKeyColumnName());
				map.add("autoincrementColumnName", crossReferenceForm.getAutoincrementColumnName());
				dataResponse = crossRefRestUtilities.postRestObject(request, "/mergeCrossReferenceRecordsWithExistingXref", map, user.getUserId());
			}
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			LOGGER.error("",e);;
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		return mv;
	}

	@RequestMapping(value = "/splitRecords", method = RequestMethod.POST)
	public ModelAndView splitRecords(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, BindingResult result, Locale locale,
			RedirectAttributes redirectAttributes) {
		try {
			User user = CommonUtils.getUserDetails(request, null, null);

			MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
			map.add("columnName", crossReferenceForm.getIlColumnName());
			map.add("iLId", crossReferenceForm.getIlId());
			map.add("splitColumnValues", String.join(",", crossReferenceForm.getIlMergeColumns().toArray(new String[0])));
			map.add("autoincrementColumnName", crossReferenceForm.getAutoincrementColumnName());
			map.add("xRefKeyColumnName", crossReferenceForm.getxRefKeyColumnName());

			DataResponse dataResponse = crossRefRestUtilities.postRestObject(request, "/splitCrossReferenceRecords", map, user.getUserId());
			if (dataResponse != null && dataResponse.getHasMessages()) {
				redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
				redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
			} else {
				redirectAttributes.addFlashAttribute("messagecode", "FAILED");
				redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			}

		} catch (Exception e) {
			LOGGER.error("",e);;
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		return mv;
	}

	@RequestMapping(value = { "/mergeRecords", "/splitRecords" }, method = RequestMethod.GET)
	public ModelAndView mergeRecordsRediectToHome(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, BindingResult result, Locale locale) {
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		return mv;
	}

	@SuppressWarnings("unchecked")
	//@ModelAttribute("getCrossRefILs")
	public Map<Object, Object> getCrossRefILs(HttpServletRequest request) {
		User user = CommonUtils.getUserDetails(request, null, null);
		Map<Object, Object> ilMap = new HashMap<>();
		List<Map<Object, Object>> ilsList = null;
		DataResponse dataResponse = crossRefRestUtilities.getRestObject(request, "/getAllClientILsForXref", user.getUserId());
		if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
			ilsList = (List<Map<Object, Object>>) dataResponse.getObject();
			if (ilsList != null) {
				for (Map<Object, Object> il : ilsList) {
					if (il.get("xref_il_table_name") != null) {
						ilMap.put(Integer.parseInt(il.get("iL_id").toString()), il.get("iL_name"));
					}
				}
			}
		}

		return ilMap;
	}
	
	
	@RequestMapping(value = "/autoMergeCrossReference", method = RequestMethod.POST)
	public ModelAndView autoMergeCrossReference(HttpServletRequest request, HttpServletResponse response, ModelAndView mv,
			@ModelAttribute("crossReferenceForm") CrossReferenceForm crossReferenceForm, RedirectAttributes redirectAttributes, HttpSession session,
			Locale locale) {
		User user = CommonUtils.getUserDetails(request, null, null);
		try {
			if (crossReferenceForm.getIlId() != null && crossReferenceForm.getIlId() != 0) {
				if (StringUtils.isNotBlank(crossReferenceForm.getCrossReferenceOption())) {
					
					DataResponse dataResponse = crossRefRestUtilities.postRestObject(request, "/autoMergeCrossReference", crossReferenceForm, user.getUserId());
					if (dataResponse != null && dataResponse.getHasMessages()) {
						redirectAttributes.addFlashAttribute("messagecode", dataResponse.getMessages().get(0).getCode());
						redirectAttributes.addFlashAttribute("errors", dataResponse.getMessages().get(0).getText());
					} else {
						redirectAttributes.addFlashAttribute("messagecode", "FAILED");
						redirectAttributes.addFlashAttribute("errors", "Unmerging failed");
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("",e);;
			redirectAttributes.addFlashAttribute("messagecode", "FAILED");
			redirectAttributes.addFlashAttribute("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
		}
		mv.setViewName("redirect:/adt/crossReference/getIlList");
		redirectAttributes.addFlashAttribute("crossReferenceForm", crossReferenceForm);
		return mv;
	}


	@RequestMapping(value = "/jobResults/{ilId}/{ilName}", method = RequestMethod.GET)
	public ModelAndView viewResults(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("ilId") Integer ilId,
			@PathVariable("ilName") String ilName, @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		if (ilId != null && ilName != null) {

			jobResultForm.setIlId(ilId);
			jobResultForm.setIlName(ilName);

			DataResponse dataResponse = crossRefRestUtilities.getRestObject(request, "/jobXrefJobResults/{ilId}", user.getUserId(),
					ilId);
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("success")) {
				mv.addObject("viewResultList", dataResponse.getObject());
			}

			mv.setViewName("tiles-anvizent-entry:viewResults");
			jobResultForm.setPageMode("xref");
		} else {
			mv.setViewName(homeRedirectUrl);
		}

		return mv;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/jobResults/{ilId}/{ilName}", method = RequestMethod.POST)
	public ModelAndView searchJobResult(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, @PathVariable("ilId") Integer ilId,
			@PathVariable("ilName") String ilName, @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			String fromDate = jobResultForm.getFromDate();
			String toDate = jobResultForm.getToDate();

			DataResponse dataResponse = crossRefRestUtilities.getRestObject(request,
					"/jobXrefJobResults/{ilId}/{fromDate}/{toDate}", user.getUserId(), ilId, fromDate, toDate);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.getObject();

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});

			mv.addObject("viewResultList", viewResultList);

			jobResultForm.setPageMode("xref");

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}
		mv.setViewName("tiles-anvizent-entry:viewResults");

		return mv;
	}
	
	
	@RequestMapping(value = "/viewCrossRefExecutionResultsById/{conditionId}/{conditionName}/{ilId}", method = RequestMethod.GET)
	public ModelAndView viewCrossRefExecutionResultsById(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, 
			@PathVariable("conditionId") Integer conditionId,
			@PathVariable("conditionName") String conditionName,
			@PathVariable("ilId") String ilId, 
			@ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		if (conditionId != null && conditionName != null) {

			jobResultForm.setXrefConditionId(conditionId);
			jobResultForm.setXrefConditionName(conditionName);

			DataResponse dataResponse = crossRefRestUtilities.getRestObject(request, "/jobXrefJobResultsById/{conditionId}/{ilId}", user.getUserId(),
					conditionId, ilId);
			if (dataResponse != null && dataResponse.getHasMessages() && dataResponse.getMessages().get(0).getCode().equalsIgnoreCase("success")) {
				mv.addObject("viewResultList", dataResponse.getObject());
			}
			mv.setViewName("tiles-anvizent-entry:viewResults");
			jobResultForm.setPageMode("xref");
		} else {
			mv.setViewName(homeRedirectUrl);
		}
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/viewCrossRefExecutionResultsById/{conditionId}/{conditionName}/{ilId}", method = RequestMethod.POST)
	public ModelAndView searchCrossRefExecutionResultsById(HttpServletRequest request, HttpServletResponse response, ModelAndView mv, 
			@PathVariable("ilId") Integer ilId,
			@PathVariable("conditionId") Integer conditionId,
			@PathVariable("conditionName") String conditionName, @ModelAttribute("jobResultForm") JobResultForm jobResultForm,
			BindingResult result, Locale locale) {

		User user = CommonUtils.getUserDetails(request, null, null);

		try {

			String fromDate = jobResultForm.getFromDate();
			String toDate = jobResultForm.getToDate();

			DataResponse dataResponse = crossRefRestUtilities.getRestObject(request,
					"/jobXrefJobResultsById/{conditionId}/{ilId}/{fromDate}/{toDate}", user.getUserId(), conditionId, ilId, fromDate, toDate);

			ObjectMapper mapper =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			List<?> l = (List<Map<String, Object>>) dataResponse.getObject();

			List<JobResult> viewResultList = mapper.convertValue(l, new TypeReference<List<JobResult>>() {
			});

			mv.addObject("viewResultList", viewResultList);

			jobResultForm.setPageMode("xref");

		} catch (Exception e) {
			mv.addObject("messagecode", "FAILED");
			mv.addObject("errors", messageSource.getMessage("anvizent.package.label.unableToProcessYourRequest", null, locale));
			LOGGER.error("",e);
		}
		mv.setViewName("tiles-anvizent-entry:viewResults");

		return mv;
	}
	
	/*
	 * @RequestMapping(value = "/getCrossInfo/{ilId}", method =
	 * RequestMethod.GET) public ModelAndView getCrossInfo(HttpServletRequest
	 * request, HttpServletResponse response, ModelAndView mv,
	 * 
	 * @ModelAttribute("crossReferenceForm") CrossReferenceForm
	 * crossReferenceForm, HttpSession session, Locale locale) { LOGGER.debug(
	 * "in getCrossInfo()"); DataResponse dataResponse = new DataResponse();
	 * 
	 * try { MultiValueMap<Object, Object> map = new LinkedMultiValueMap<>();
	 * map.add("iLId", crossReferenceForm.getIlId()); User user =
	 * CommonUtils.getUserDetails(request, null, null); dataResponse =
	 * crossRefRestUtilities.postRestObject(request, "/getCrossInfo", map,
	 * user.getUserId());
	 * 
	 * if (dataResponse != null && dataResponse.getHasMessages()) { if
	 * (dataResponse.getMessages().get(0).getCode().equals("SUCCESS")) {
	 * mv.addObject("crossInfoList", dataResponse.getObject()); } else {
	 * mv.addObject("messagecode", dataResponse.getMessages().get(0).getCode());
	 * mv.addObject("errors", dataResponse.getMessages().get(0).getText());
	 * mv.addObject("crossInfoList", dataResponse.getObject()); } } else {
	 * mv.addObject("messagecode", "FAILED"); mv.addObject("errors",
	 * messageSource.getMessage(
	 * "anvizent.package.label.unableToProcessYourRequest", null, locale)); }
	 * 
	 * } catch (Exception e) { mv.addObject("messagecode", "FAILED");
	 * mv.addObject("errors", messageSource.getMessage(
	 * "anvizent.package.label.unableToProcessYourRequest", null, locale));
	 * LOGGER.error("",e);; }
	 * mv.setViewName("tiles-anvizent-entry:crossReference"); return mv; }
	 */

}
