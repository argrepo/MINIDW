package com.datamodel.anvizent.data.RestController;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.datamodel.anvizent.helper.CommonUtils;
import com.datamodel.anvizent.helper.ParseExcel;
import com.datamodel.anvizent.service.model.Column;
import com.opencsv.CSVReader;
import com.sun.jersey.server.impl.BuildId;

public class ExcelParseBatchExecution {
	private static final Logger logger= LoggerFactory.getLogger(ExcelParseBatchExecution.class);
	
	
	
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
	
	public Map<String, Object> parseXLSToData(File xlsfile, JdbcTemplate clientJdbcTemplate, String clientId, String tableName,List<Column> tcolumns, String separatorChar, String stringQuoteChar) throws IOException {
		Map<String, Object> responseMap= new HashMap<>();
		Workbook wb=null;
		try{
			String xlsFilePath=xlsfile.getAbsolutePath();
			ParseExcel parseExcel = new ParseExcel(xlsFilePath);
			boolean isValid=parseExcel.isValidExcel();
			List<String> xlsHeaders= parseExcel.getHeadersFromFile(xlsFilePath);
			String insertScript = buildInsertScript(tcolumns,tableName);
			if(isValid){
				wb=parseExcel.getWorkbookInstance(xlsFilePath);
				Sheet sheet= wb.getSheetAt(0);
				Iterator<Row> rows= sheet.rowIterator();
				int total=0, success=0;
				List<String> failMessageList = new ArrayList<String>();
				int rowNumber=0;
				while(rows.hasNext()){
					rowNumber++;
					Row row = rows.next();
					int rowno= row.getRowNum();
					if(rowno==0){
						//skip
						continue;
					}
					Object[] params = new Object[row.getLastCellNum()];
					for(int i=0;i<xlsHeaders.size();i++){
						String value= null;
						Column column = tcolumns.get(i);
						String colname = column.getColumnName();
						String datatype = column.getDataType();
						Cell cell= row.getCell(i);
						if(cell!=null){
							Cell formattedCell = CommonUtils.formatExcelCellData(row.getCell(i));
							 value=String.valueOf(formattedCell);
							params[i]=String.valueOf(formattedCell);
						} else {
							params[i]="";
						}
						if(StringUtils.isBlank(value)){
							params[i]="coalesce(null,DEFAULT("+colname+")) ";
						}
						else {
							if (NumberUtils.isNumber(value) || "bit".equalsIgnoreCase(datatype)) {
								params[i]=value;
							} else {
								params[i]= value.replaceAll("'", "''");
							}
						}
					}
					try{
						success  = clientJdbcTemplate.update(insertScript, params);
					}catch(Exception de){
						String failMessage = "Row Number:::: "+rowNumber +" Error Messsage:::: "+ MinidwServiceUtil.getErrorMessageString(de);
						failMessageList.add(failMessage);
						de.printStackTrace();
					}
				}
				responseMap.put("Total Records:: ", total);
				responseMap.put("Success Records:: ", success);
				responseMap.put("Failed Records:: ", failMessageList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			logger.debug(" Closing workbook Object .. ");
			if (wb != null)
				wb.close();
		}
		return responseMap;
	}
	
	
	public Map<String, Object> parseXLSToDataBatch(File xlsfile, JdbcTemplate clientJdbcTemplate, String clientId, String tableName,List<Column> tcolumns, String separatorChar, String stringQuoteChar){
		Map<String, Object> responseMap= new HashMap<>();
		try{
			String insertScript = buildInsertScript(tcolumns, tableName);
			
			clientJdbcTemplate.execute(new ConnectionCallback<String>() {
				Workbook wb=null;
				String xlsFilePath=xlsfile.getAbsolutePath();
				ParseExcel parseExcel = new ParseExcel(xlsFilePath);
				boolean isValid=parseExcel.isValidExcel();
				List<String> xlsHeaders= parseExcel.getHeadersFromFile(xlsFilePath);
				public String doInConnection(Connection con) throws SQLException, DataAccessException{
					  PreparedStatement ps = con.prepareStatement(insertScript);
					  try{
						  if(isValid){
							  int total=0, success=0;
							  List<String> failMessageList = new ArrayList<String>();
							  int rowNumber=0;
							  wb=parseExcel.getWorkbookInstance(xlsFilePath);
							  Sheet sheet=wb.getSheetAt(0);
							  
							  
						  }
					  }catch(Exception e){
						  e.printStackTrace();
					  }
					  
					return "Insertion Complete";
				}
			});
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return responseMap;
	}

}
