package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.type.TypeBindings;
import org.objectweb.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.datamodel.anvizent.helper.ClientJdbcInstance;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.UserDetailsService;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;

@RestController("batchFileUploadRestController")
@RequestMapping("/batchfileuploadpackage")
@CrossOrigin
public class BatchFileUploadRestController {
	
	private static final Log logger= LogFactory.getLog(BatchFileUploadRestController.class);
	
	@Autowired
	PackageService packageService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	UserDetailsService userService;
	
	@RequestMapping(value="/batchFileUpload", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<Object> batchFileUpload(
			   @RequestParam(value = "clientId") String clientId,
               @RequestParam(value = "tableName") String tableName,
			   @RequestParam(value = "file") MultipartFile multipartfile, 
			   HttpServletRequest request){
		
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		List<Message> messages = new ArrayList<>();
		JdbcTemplate clientJdbcTemplate = new JdbcTemplate();
		boolean isFileTypeValid=Boolean.FALSE;
		File convertedFile ;
		String separatorChar=","; 
		String stringQuoteChar="\'";
		Map<String, Object> mapResponse = null;
		try{
			if(!multipartfile.isEmpty()){
				ClientJdbcInstance clientJdbcInstance = userService.getClientJdbcInstance(clientId);
				clientJdbcTemplate = clientJdbcInstance.getClientJdbcTemplate();
				boolean isTableExists=fileService.isTableExists(clientId, null, tableName, clientJdbcTemplate);
				if(isTableExists){
					List<Column> columns = packageService.getTableStructure(null, tableName, 0, null, clientJdbcTemplate);
					fileService.truncateTable(null, tableName, clientJdbcTemplate);
					
					//isFileTypeValid=batchParseCSV.isValidFile(multipartfile);
					//convertedFile = batchParseCSV.convertFile(multipartfile);
					if(isFileTypeValid){
						//mapResponse=batchParseCSV.parseCSVToBatchExecute(convertedFile,clientJdbcTemplate,clientId,tableName,columns,separatorChar,stringQuoteChar);
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
				}else{
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
	
	
	@RequestMapping(value="/cardata", method=RequestMethod.POST,produces="application/json", consumes="application/json")
	public ResponseEntity<Object> getCarInfo(@RequestBody Object vehicle,  HttpServletRequest request){
		  DataResponse dataResponse = new DataResponse();
		  ObjectMapper objM =new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		 //List<Car> cars= objM.convertValue(vehicle, new TypeReference<List<Car>>() {});
		  try{
			  Enumeration<String> headerNames=request.getHeaderNames();
			  while(headerNames.hasMoreElements()){
				  String header1= headerNames.nextElement();
				  logger.info(":: Http Header:::: "+header1+"::::::::: "+request.getHeader(header1) == null ? "" : request.getHeader(header1));
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		return new ResponseEntity<Object>(vehicle, HttpStatus.OK);
		
	}

}
