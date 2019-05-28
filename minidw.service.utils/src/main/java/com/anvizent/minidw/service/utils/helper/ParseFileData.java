package com.anvizent.minidw.service.utils.helper;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.model.Column;

public class ParseFileData {

	protected static final Log logger = LogFactory.getLog(ParseFileData.class);
	
	
	public boolean isValidFile(MultipartFile multipartfile){
			String fileName;
			String filetype;
			fileName= multipartfile.getOriginalFilename();
			filetype=FilenameUtils.getExtension(fileName);
			boolean validFile = filetype.equalsIgnoreCase("csv") ||  filetype.equals("xls") || filetype.equals("xlsx");
		  return validFile;
		}
	
	public List<String> getHeadersFromFile(String filePath, String fileType, String separatorChar,
			String stringQuoteChar, String charset) throws Exception {

		List<String> headers = null;
		switch (fileType) {
		case Constants.FileType.CSV:
			// get the headers from the csv file
			logger.info("in getHeadersFromCSV()");
			ParseCSVDynamicCharset parseCSV = new ParseCSVDynamicCharset(filePath);
			headers = parseCSV.readColumns(separatorChar, stringQuoteChar, charset);
			break;
		case Constants.FileType.XLS:
			// get the headers from the xls file
			logger.info("in getHeadersFromXLS()");
			ParseExcel parseExcel = new ParseExcel(filePath);
			headers = parseExcel.getHeadersFromFile(filePath);
			break;
		case Constants.FileType.XLSX:
			// get the headers from the xlsx file
			logger.info("in getHeadersFromXLSX()");
			ParseExcel parseExcel1 = new ParseExcel(filePath);
			headers = parseExcel1.getHeadersFromXLSXFile(filePath);
			break;
		}
		return headers;
	}
	
	public List<LinkedHashMap<String, Object>> getDataFromFile(File tempFile, String filetype, String separatorChar, String stringQuoteChar, String charset) {
		List<LinkedHashMap<String, Object>> listOfMapFileData=null;
		try{
			switch(filetype) {
			 case Constants.FileType.CSV:
				 ParseCSVDynamicCharset parseCSV = new ParseCSVDynamicCharset(tempFile.getAbsolutePath());
				 listOfMapFileData = parseCSV.parseCSVToListOfMap(tempFile.getAbsolutePath(), separatorChar, stringQuoteChar, charset);
				 break;
			 
			 case Constants.FileType.XLS:
				 ParseExcel parseExcel = new ParseExcel(tempFile.getAbsolutePath());
				 listOfMapFileData = parseExcel.parseXLSToListofMap(tempFile.getAbsolutePath());
				 break;
				 
			 case Constants.FileType.XLSX:
				 ParseExcel parseXLSX = new ParseExcel(tempFile.getAbsolutePath());
				 listOfMapFileData = parseXLSX.parseXLSXToListofMap(tempFile.getAbsolutePath());
				 break;
			 }
		}catch(Exception e){
			e.printStackTrace();
		}
			 
		return listOfMapFileData;
	}
	
	public LinkedHashMap<String, Object> processFileDataMapList(JdbcTemplate clientJdbcTemplate, List<Column> tcolumns,
			String tableName, List<String> headers, List<LinkedHashMap<String, Object>> fileDataMapList) {
		LinkedHashMap<String, Object> processedBatchData = new LinkedHashMap<String,Object>();
		try{
			String insertScript = buildInsertScript(tcolumns,tableName);
			clientJdbcTemplate.execute(new ConnectionCallback<Integer>() {
				int noOfCon=0;
				int total=0, success=0;
				List<String> failMessageList = new ArrayList<String>();
				@Override
				public Integer doInConnection(Connection con) throws SQLException, DataAccessException{
					noOfCon++;
					PreparedStatement ps=con.prepareStatement(insertScript);
					int rowNumber=0;
					for(Map<String,Object> map: fileDataMapList){
						rowNumber++;
						int index=1;
						for(String header: headers){
							Object data = map.get(header);
							if ( data != null ) {
								String trimmedData = data.toString().trim().toLowerCase();
								if ( StringUtils.isBlank(trimmedData) || trimmedData.equals("null") ) {
									data = null;
								}
							}
							ps.setObject(index++, data);
						}
						try{
							success+=ps.executeUpdate();
						}catch(Exception e){
							//e.printStackTrace();
							String failMessage = "Row Number:::: "+rowNumber +" Error Messsage:::: "+ MinidwServiceUtil.getErrorMessageString(e);
							failMessageList.add(failMessage);
						}
						total++;
					}
					processedBatchData.put("totalrecords", total);
					processedBatchData.put("successrecords", success);
					processedBatchData.put("failedrecords", failMessageList);
					return Integer.valueOf(noOfCon);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		return processedBatchData;
	}
	
	private String buildInsertScript(List<Column> columns, String tableName) {
		StringBuilder insertScript = new StringBuilder();
		 int collength=columns.size();
		insertScript.append("INSERT INTO ").append(tableName).append(" ( \n ");
		 for(int i=0;i<collength;i++){
			 String column = columns.get(i).getColumnName();
			 String colname = column;
			 insertScript.append(colname);
			 if(i<collength-1)
				 insertScript.append(", ");
		 }
		 insertScript.append(" \n ) VALUES ( \n ");
		 for(int j=0;j<collength;j++){
			 insertScript.append("?");
				if(j<collength-1)
					insertScript.append(", ");
		 }
		 insertScript.append(" \n )");
		
		return insertScript.toString();
	}

}
