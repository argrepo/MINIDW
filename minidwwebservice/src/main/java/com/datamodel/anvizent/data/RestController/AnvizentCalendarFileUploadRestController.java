package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.processor.FileDataParser;
import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;

@RestController
@RequestMapping(value = "/anvizcal")
@CrossOrigin
public class AnvizentCalendarFileUploadRestController {

	@Autowired
	UserDetailsService userService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	FileDataParser fileDataParser;

	@Autowired
	PackageService packageService;

	@Autowired
	FileService fileService;

	@RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Object> hello() {

		return new ResponseEntity<Object>("Hello", HttpStatus.OK);
	}

	@RequestMapping(value = "/calenderDataUpload", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> calendarFileUpload(@RequestParam(value = "clientId") String clientId,
			@RequestParam(value = "file") MultipartFile multipartfile,
			@RequestParam(value = "calendarKey") Integer calendarKey,
			@RequestParam(value = "operationType") String operationType,
			@RequestParam(value = "fileMandatoryHeaders", required = false) String fileMandatoryHeaders,
			HttpServletRequest httpServeletRequest, Locale locale) {

		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = null;
		JdbcTemplate clientAppDbJdbcTemplate = null;
		File tempFile = null;
		long filesize = 0;
		 Map<String, Object> mapResponse = new LinkedHashMap<String, Object>();
		 List<Map<String, Object>> sheets = new ArrayList<>();
		boolean isFileTypeValid = Boolean.FALSE;
		String separatorChar = ",";
		String stringQuoteChar = "\"";
		String filetype = null;
		String fileName = null;
		List<LinkedHashMap<String, Object>> fileDataMapList = new ArrayList<LinkedHashMap<String, Object>>();
		String insertDateIntoTableName = "ANVI_Custom_Calender_Dates";
		List<String> fileMandatoryHeadersList = Arrays.asList(fileMandatoryHeaders.split(","));
		try {
			ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
			clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
			clientAppDbJdbcTemplate = clientJdbcInstance.getClientAppdbJdbcTemplate();

			if (multipartfile != null && !multipartfile.isEmpty()) {
				tempFile = CommonUtils.multipartToFile(multipartfile);
				fileName = multipartfile.getOriginalFilename();
				filetype = FilenameUtils.getExtension(fileName);
				filesize = FileUtils.sizeOf(tempFile);
				isFileTypeValid = fileDataParser.isValidFile(multipartfile);
				mapResponse.put("filename", fileName);
				mapResponse.put("filesize", FileUtils.byteCountToDisplaySize(filesize));

				List<Column> columns = packageService.getTableStructure(null, insertDateIntoTableName, 0, clientId,
						clientJdbcTemplate);
				if (isFileTypeValid) {
					List<String> headers = fileDataParser.getHeadersFromFile(tempFile.getAbsolutePath(), filetype,
							separatorChar, stringQuoteChar, null);
					Map<Integer, String> noOfSheets = fileDataParser.getTotalNoOfSheets(tempFile.getAbsolutePath(), filetype, fileName);
					if (operationType.equals("truncate_insert")) {
						//fileService.truncateTable(null, insertDateIntoTableName, clientJdbcTemplate);
						fileService.deleteTableDataByCalenderKey(null, insertDateIntoTableName, calendarKey, clientJdbcTemplate);
					}
					 Set<Integer> keySet = noOfSheets.keySet();
					  for(Integer sheetNo : keySet){
						 fileDataMapList = fileDataParser.getDataFromFile(sheetNo, tempFile, fileMandatoryHeadersList,
								filetype, separatorChar, stringQuoteChar, null);
						  LinkedHashMap<String, Object> sheetInfo = new LinkedHashMap<>();
						  sheetInfo.put("sheetname", noOfSheets.get(sheetNo));
						  LinkedHashMap<String, Object> sheetInsertResponse = fileDataParser.processFileDataMapList(calendarKey, clientJdbcTemplate,
								columns, insertDateIntoTableName, new ArrayList<String>(headers), fileDataMapList);
						  sheetInfo.putAll(sheetInsertResponse);
						  sheets.add(sheetInfo);
					  }
					  if (mapResponse != null) {
						    message.setCode("SUCCESS");
							message.setText(messageSource.getMessage("anvizent.message.text.parsedSuccessfully", null, locale));
							messages.add(0, message);
						} 
				} else {
					message.setCode("FAILED");
					message.setText("file type is not valid.");
					messages.add(message);
				}
			} else {
				message.setCode("FAILED");
				message.setText(messageSource.getMessage("anvizent.message.text.uploadedEmptyFile", null, locale));
				messages.add(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String error = MinidwServiceUtil.getErrorMessageString(e);
			message.setCode("FAILED");
			message.setText(error);
			messages.add(message);
		} finally {
			deleteQuietly(tempFile);
		}
		 mapResponse.put("sheets", sheets);
		 dataResponse.addMessages(messages);
		 dataResponse.setObject(mapResponse);
		return new ResponseEntity<Object>(dataResponse, HttpStatus.OK);
	}

	public boolean deleteQuietly(File file) {
		if (file == null) {
			return false;
		}
		try {
			if (file.isDirectory()) {
				FileUtils.cleanDirectory(file);
			}
		} catch (Exception ignored) {
		}

		try {
			return file.delete();
		} catch (Exception ignored) {
			return false;
		}
	}

}
