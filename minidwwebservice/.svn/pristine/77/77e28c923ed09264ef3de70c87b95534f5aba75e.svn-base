package com.datamodel.anvizent.data.RestController;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;

@RestController("fileUploadRestController")
@RequestMapping("/fileuploadpackage")
@CrossOrigin
public class FileUploadRestController {
	
	private static final Log logger= LogFactory.getLog(FileUploadRestController.class);
	
	@Autowired
	PackageService packageService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	UserDetailsService userService;
	
	@RequestMapping(value="/fileUpload", method=RequestMethod.POST, produces =  "application/json")
	public ResponseEntity<Object> testFileUpload(
			   @RequestParam(value = "clientId") String clientId,
               @RequestParam(value = "tableName") String tableName,
			   @RequestParam(value = "file") MultipartFile multipartfile, 
			   HttpServletRequest httpServeletRequest){
		
		System.out.println("clientId..."+clientId+" tableName..."+tableName);
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		DataResponse dataResponse = new DataResponse();
		Map<String, Object> mapResponse = null;
		boolean isFileTypeValid=Boolean.FALSE;
		File convertedFile = null ;
		String separatorChar=","; 
		String stringQuoteChar="\'";
		//CSVParseBatchExecution batchParseCSV = new CSVParseBatchExecution();
		ExcelParseBatchExecution  excelParseBatchExecution = new ExcelParseBatchExecution();
		JdbcTemplate clientJdbcTemplate = new JdbcTemplate();
		try{
			if(!multipartfile.isEmpty()){
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				boolean isTableExists=fileService.isTableExists(clientId, null, tableName, clientJdbcTemplate);
				if(isTableExists){
					List<Column> columns = packageService.getTableStructure(null, tableName, 0, clientId, clientJdbcTemplate);
					fileService.truncateTable(null, tableName, clientJdbcTemplate);
					
					//isFileTypeValid=batchParseCSV.isValidFile(multipartfile);
					//convertedFile = batchParseCSV.convertFile(multipartfile);
					if(isFileTypeValid){
						String fileName = multipartfile.getOriginalFilename();
						String filetype = FilenameUtils.getExtension(fileName);
						if(filetype.equalsIgnoreCase(Constants.FileType.XLS)){
							mapResponse=excelParseBatchExecution.parseXLSToData(convertedFile,clientJdbcTemplate,clientId,tableName,columns,separatorChar,stringQuoteChar);
						}
						else{
							//mapResponse=batchParseCSV.parseCSVToData(convertedFile,clientJdbcTemplate,clientId,tableName,columns,separatorChar,stringQuoteChar);
						}
						message.setCode("Success.");
						message.setText("Parsed CSV Successfully.");
						messages.add(message);
						dataResponse.setObject(mapResponse);
						dataResponse.addMessages(messages);
					}
					else{
						message.setCode("Failed !");
						message.setText("Allowed only  .csv, .xlx, .xlsx");
						messages.add(message);
						dataResponse.addMessages(messages);
					}
				}
				else{
					message.setCode("Failed !");
					message.setText("Given Table Not Exists");
					messages.add(message);
					dataResponse.addMessages(messages);
				}
			}
			else{
				message.setCode("Failed !");
				message.setText("Uploaded file is Empty.");
				messages.add(message);
				dataResponse.addMessages(messages);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<Object>(dataResponse, HttpStatus.OK);
	}
	
}
