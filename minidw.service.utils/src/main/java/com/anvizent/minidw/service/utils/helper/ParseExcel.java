package com.anvizent.minidw.service.utils.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.minidw.service.utils.helper.ParseCSV.DBDataOperations;
import com.datamodel.anvizent.helper.CommonDateHelper;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.Table;
import com.monitorjbl.xlsx.StreamingReader;

public class ParseExcel {

private String FILE_PATH = null;
	
	public ParseExcel(String filePath) {
		this.FILE_PATH = filePath;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ParseExcel.class);
	
	private boolean isValidExcel() {
		logger.debug("validation excel file .. ");

		boolean valid = false;

			if (FILE_PATH != null && FILE_PATH.length() > 0) {
				File excelFile = new File(FILE_PATH);
				valid = excelFile.exists();
			} else {
				logger.info("File path is empty or null .. {}", FILE_PATH);
			}

		return valid;
	}
	
	private String buildInsertScript(Table table) {
		StringBuilder insertScript = new StringBuilder();
		
		String tableName = table.getTableName();
		String schemaName = table.getSchemaName();
		List<String> tColumns = table.getOriginalColumnNames();
		int colslen = tColumns.size();
				
		insertScript.append("INSERT INTO ").append(schemaName).append(".").append(tableName).append(" ( \n ");
		
		for (int i =0 ; i< colslen; i++) {		
			String column = tColumns.get(i);
			String colname = column;
			insertScript.append(colname);

			if (i< colslen-1)
				insertScript.append(", ");
		}
		insertScript.append(" \n ) VALUES ( \n ");
		
		return insertScript.toString();
	}
	
	public Workbook getWorkbookInstance(String filePath) throws Exception{
		Workbook wb = null;			
		InputStream excelFileToRead = new FileInputStream(filePath);
		wb = (Workbook) WorkbookFactory.create(excelFileToRead);		
		return wb;
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public List<String> getHeadersFromFile(String filePath) throws Exception{		
		List<String> headers = new ArrayList<>();			 
				 Workbook wb = getWorkbookInstance(filePath);
				 Sheet sheet = wb.getSheetAt(0);
				
				 Iterator rows = sheet.rowIterator();
				 while(rows.hasNext()){
					  Row row = (Row) rows.next();			      	  
					  Iterator cells = row.cellIterator();
					  
					  while (cells.hasNext())
					  {
						  String data = null;
					      Cell cell = (Cell) cells.next();
					      switch (cell.getCellType()) {
								case Cell.CELL_TYPE_STRING: 
									data = cell.getStringCellValue().trim();
									if(StringUtils.isNotEmpty(data))
									   headers.add(data.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));							
									break;
								case Cell.CELL_TYPE_NUMERIC: 
									if(DateUtil.isCellDateFormatted(cell))
										data = cell.getDateCellValue().toString().trim();									 
									else{
										data = String.valueOf(cell.getNumericCellValue()).trim();										
									}
									if(StringUtils.isNotEmpty(data))
										headers.add(data.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
									break;
								case Cell.CELL_TYPE_BOOLEAN : 
									data = String.valueOf(cell.getBooleanCellValue()).trim();
									if(StringUtils.isNotEmpty(data))
										headers.add(data.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));	
									break;										
								default:
									break;
					      	}					      
					    
					  }					  
					 break;
				 	}		
		
				 return headers;
		}	
	
	public boolean hasDataforXLSX(String filePath){
        boolean valid = isValidExcel();
		
		if (valid) {
			File file = new File(filePath);
			InputStream is = null;
			try {
				is = new FileInputStream(file);
			
			
			StreamingReader reader = StreamingReader.builder()
			        .rowCacheSize(100)   
			        .bufferSize(4096)    
			        .sheetIndex(0)       
			        .read(is);            
			
			int rowCount = 0;
			for (Row r : reader) {
				rowCount++;
				if(rowCount>1){
					return true;
				}
			}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(is!=null){
					is.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String, Object> processExcelDataBatch(String filePath, ClientData clientData, DataSource dataSource,String clientSchemaName) {
		
		Map<String, Object> result = new HashMap<>();
		Table table = clientData.getUserPackage().getTable();
		logger.debug("processing excel data batch.. ");
		
		boolean valid = isValidExcel();
		Workbook wb = null;
		if(valid){			
			logger.info("valid excel file .. ");
			try {
				List<String> columns = getHeadersFromFile(filePath);
				
				logger.debug("columns before applying pattern : {}", columns);
				
				for (int i = 0; i < columns.size(); i++) {
					String col = columns.get(i);
					col = col.trim().replaceAll("\\s+", "_");
					columns.set(i, col);
				}			 
				
				logger.debug("columns after applying pattern : {}", columns);
				
				String tableName = table.getTableName();
				String schemaName = table.getSchemaName();

				List<Column> tColumns = table.getColumns();
				int colslen = tColumns.size();

				logger.debug("Schema Name : {} --> Table Name : {} ", schemaName, tableName);

				logger.debug("Columns Size : {} ", colslen);
				
				String insertScript = buildInsertScript(table);

				DBDataOperations dbOp = new DBDataOperations();
				dbOp.setDataSource(dataSource);

				int total = 0, success = 0, fail = 0, duplicates = 0;
				
				int index = 0;
				 
				List<String> queries = new ArrayList<>();
				 
				wb = getWorkbookInstance(filePath);
				Sheet sheet = wb.getSheetAt(0);				
				Iterator rows = sheet.rowIterator();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
				SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startTime = startDateFormat.format(new Date());
				String batchId = clientData.getUserId() +"_"+ clientData.getUserPackage().getPackageId() + "_" + clientData.getUserPackage().getPackageName() + "_" + sdf.format(new Date());
				
				while(rows.hasNext()){
				  StringBuilder insertQuery = new StringBuilder(insertScript);	
				  Row row = (Row) rows.next(); 
				  int getRowNumb = row.getRowNum();
				  
				  if(getRowNumb == 0){
					   continue; 
				  }
				   				 
				  for (int i = 0; i < colslen; i++) {
					    String value = null;				     
					  
					  	Column column = tColumns.get(i);
						String colname = column.getColumnName();
						String datatype = column.getDataType();
						int colIndex = columns.indexOf(colname);
						Cell cell = row.getCell(colIndex);
						if(cell != null){
							Cell formattedCell = CommonUtils.formatExcelCellData(row.getCell(colIndex));
							value = String.valueOf(formattedCell);
						}else{
							value = "";	
						}
						if (StringUtils.isBlank(value)) {
							insertQuery.append("coalesce(null,DEFAULT(").append(colname).append(")) ");
						} else {
						if("text".equalsIgnoreCase((String) datatype) || "varchar".equalsIgnoreCase(datatype)){
							
							insertQuery.append("'").append(value).append("'");
						} else if ("datetime".equalsIgnoreCase(datatype)) {
							
							insertQuery.append("'").append(value).append("'");
						} else {
							if ( NumberUtils.isNumber(value) || "bit".equalsIgnoreCase( datatype ) ) {
								insertQuery.append(value);
							} else {
								value = value.replaceAll("'", "''");
								insertQuery.append("'").append(value).append("'");
							}
						}
						}
						if (i < colslen - 1) {
							insertQuery.append(", ");

						}
					}			 	  
				  	
				  insertQuery.append(" \n )");
					 
					queries.add(insertQuery.toString());
					total++;
					index++;
					
					if (index % 1000 == 0) {
						String[] querriesArr = queries.toArray(new String[0]);
						int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId()+"", clientData.getUserPackage().getPackageName(),  clientData.getUserId(),batchId);
						
						success += updates;
						fail += (queries.size()-updates);
						
						queries.clear();
						index = 0;
						logger.debug("executed records : "+total);
					}
				 
				}
					if (queries.size()>0) {
						String[] querriesArr = queries.toArray(new String[0]);
						int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId()+"", clientData.getUserPackage().getPackageName(),  clientData.getUserId(),batchId);
						
						success += updates;
						fail += (queries.size()-updates);
					}
					
					String endTime = startDateFormat.format(new Date());
					StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
					loadSummerySql.append(clientSchemaName).append(".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
					loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId).append("', '").append(tableName).append("', ").append(queries.size()).append(", ").append(success).append(", ").append(fail).append(", '").append(startTime).append("', '").append(endTime).append("', 'Success', 'unknown', now(), 'Custom');");
					try{dbOp.getJdbcTemplate().update(loadSummerySql.toString());}catch(Exception er){}
					
					logger.debug("Total Records : {} ", total);
					logger.debug("Sccuess Records : {} ", success);
					logger.debug("Failed Records : {} ", fail);
					logger.debug("Duplicate Records : {} ", duplicates);

					result.put("totalRecords", total);
					result.put("successRecords", success);
					result.put("failedRecords", fail);
					result.put("duplicateRecords", duplicates);					
			 				
				
			} catch (Exception e) {				
				e.printStackTrace();
			}
			finally{
				if(wb != null){
					try {
						wb.close();
					} catch (IOException e) {						
						e.printStackTrace();
					}
				}	
			}	
		}		
		return result;
	}
	
	
	@SuppressWarnings("rawtypes")
	public List<List<String>> processExcelDataForPreview(String filePath) throws Exception{

		List<List<String>> processedData = new ArrayList<>();
		logger.debug("processing excel data for preview .. ");
		
		boolean valid = isValidExcel();
		
		if (valid) {
			logger.debug("valid excel file .. ");
			Workbook wb = getWorkbookInstance(filePath);
			Sheet sheet = wb.getSheetAt(0);	
			try{					
				List<String> columns = getHeadersFromFile(filePath);
				processedData.add(columns);
				int colslen = columns.size();
				int total = 1;				
				Iterator rows = sheet.rowIterator();				
				
				while(rows.hasNext()){
					  if(total > 10) break;	
					  Row row = (Row) rows.next(); 
					  
					  int getRowNumb = row.getRowNum();					  
					  if(getRowNumb == 0){
						   continue;  
					  }				  
					 
					  List<String> rowlist = new ArrayList<String>();
					  for (int i = 0; i < colslen; i++) {						  
						  	String value = String.valueOf(row.getCell(i));					
							rowlist.add(value);							 
						}					  	
						total++;
						processedData.add(rowlist);
				}
			}
			finally{
				wb.close();
			}
		}
	return processedData;
	
}
	public boolean hasData(String filePath){
		logger.debug("processing excel data for preview .. ");
		boolean valid = isValidExcel();
		if(valid){
			Workbook wb = null;
			try {
				wb = getWorkbookInstance(filePath);
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rows = sheet.rowIterator();
				int rowCount = sheet.getPhysicalNumberOfRows();
					if(rowCount>1){
						return true;
					}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(wb!=null){
					wb.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean processXLSDataToFile(String outputFilePath, List<String> iLColumnNames,
			List<String> selectedFileHeaders, List<String> dafaultValues) {			
			boolean isProcessed = Boolean.FALSE;
			logger.debug("in processExcelDataToFile .. ");
			
			boolean valid = isValidExcel();
			
			if (valid) {
				logger.debug("valid excel file .. ");
				Workbook wb = null;
				Workbook wb_new = null;
				FileOutputStream out = null;
				try {
					wb_new = new HSSFWorkbook();
					Sheet sheet_new = wb_new.createSheet("Sheet1");
					
					wb = getWorkbookInstance(FILE_PATH);
					Sheet sheet = wb.getSheetAt(0);
					List<String> columns = getHeadersFromFile(FILE_PATH);
					
					Row new_row1 = sheet_new.createRow(0);
					int i = 0;
					for (String iLColumn : iLColumnNames) {
						Cell new_cell = new_row1.createCell(i);
						new_cell.setCellValue(iLColumn);
						i++;
					}
					
					Iterator rows = sheet.rowIterator();				
					int new_row_count = 1;
					while(rows.hasNext()){
						  Row row = (Row) rows.next(); 
						  Row new_rows = sheet_new.createRow(new_row_count);
						  int getRowNumb = row.getRowNum();					  
						  if(getRowNumb == 0){
							   continue;  
						  }
						  
						  for (int j = 0; j < iLColumnNames.size(); j++) {
							  Cell new_cell = new_rows.createCell(j);
							  Cell cell = null;
							  String fileHeader = selectedFileHeaders.get(j);
							  int colIndex;
							  String data = null;
							  if(StringUtils.isNotBlank(fileHeader)){
								  colIndex = columns.indexOf(fileHeader.trim());
								  cell = row.getCell(colIndex);
								  if(cell != null){
										Cell formattedCell = CommonUtils.formatExcelCellData(cell);
										data = String.valueOf(formattedCell);
										new_cell.setCellValue(data);
								  }else{
									  	data = "";	
									  	new_cell.setCellValue(data);
								  }
								  
								  if(StringUtils.contains(data, ",")){
										data = data.replaceAll(",", " ").replaceAll("\\s+", " ");
										new_cell.setCellValue(data);
								  }
							  }
							  else{
									String dafaultValue =  dafaultValues.get(j);
									if(StringUtils.isNotBlank(dafaultValue)){
										data = dafaultValue;
										new_cell.setCellValue(data);
									}
							  }
							  if (StringUtils.isBlank(data)) {
								  data = "";
								  new_cell.setCellValue(data);
							  }
						  }
						  new_row_count++;
					}
					
					out = new FileOutputStream(outputFilePath);
					wb_new.write(out);
					
					isProcessed = Boolean.TRUE;
				} 
				catch (Exception e) {					
					e.printStackTrace();
				}
				finally{
					try {
						if(wb != null){
							wb.close();
						}
						if(wb_new != null){
							wb_new.close();						
						}
						if(out != null){
							out.close();		
						}
						
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
				
				
			}
			return isProcessed;
		}
 
	public boolean processXLSXDataToFile(String filePath, String outputFilePath, List<String> iLColumnNames, List<String> selectedFileHeaders,
			List<String> dafaultValues) {
		boolean isProcessed = Boolean.FALSE;
		logger.debug("in processXLSXDataToFile .. ");

		boolean valid = isValidExcel();

		if (valid) {
			logger.debug("valid excel file .. ");
			logger.debug("writing xlsx into new xlsx file started..." + CommonDateHelper.formatDateAsString(new Date()));
			Workbook wb = null;
			SXSSFWorkbook wbNew = null;
			FileOutputStream out = null;
			File file = new File(filePath);
			try {
				wbNew = new SXSSFWorkbook(1000);
				Sheet sheetNew = wbNew.createSheet("Sheet1");
				wb = StreamingReader.builder().rowCacheSize(1000).bufferSize(4096).open(file);
				Sheet sheet = wb.getSheetAt(0);
				List<String> columns = getHeadersFromXLSXFile(filePath);

				Row newRow1 = sheetNew.createRow(0);
				int i = 0;
				for (String iLColumn : iLColumnNames) {
					Cell new_cell = newRow1.createCell(i);
					new_cell.setCellValue(iLColumn);
					i++;
				}

				Iterator<Row> rows = sheet.rowIterator();
				int newRowCount = 1;
				while (rows.hasNext()) {
					Row row = (Row) rows.next();
					Row newRows = sheetNew.createRow(newRowCount);
					int getRowNumb = row.getRowNum();
					if (getRowNumb == 0) {
						continue;
					}

					for (int j = 0; j < iLColumnNames.size(); j++) {
						Cell newCell = newRows.createCell(j);
						Cell cell = null;
						String fileHeader = selectedFileHeaders.get(j);
						int colIndex;
						String data = null;
						if (StringUtils.isNotBlank(fileHeader)) {
							colIndex = columns.indexOf(fileHeader.trim());
							cell = row.getCell(colIndex);
							if (cell != null) {
								if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
									data = cell.getStringCellValue();
									newCell.setCellValue(cell.getStringCellValue());
								} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
									newCell.setCellValue(cell.getBooleanCellValue());
									data = String.valueOf(cell.getBooleanCellValue());
								} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

									if (DateUtil.isCellDateFormatted(cell)) {
										newCell.setCellValue(cell.getDateCellValue());
										SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										data = dateFormat.format(cell.getDateCellValue());
									} else {
										newCell.setCellValue(cell.getNumericCellValue());
										data = String.valueOf(cell.getNumericCellValue());
									}
								} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
									newCell.setCellValue(data);
								}

							} else {
								data = "";
								newCell.setCellValue(data);
							}
							if (StringUtils.contains(data, ",")) {
								newCell.setCellValue(data);
							}
						} else {
							String dafaultValue = dafaultValues.get(j);
							if (StringUtils.isNotBlank(dafaultValue)) {
								data = dafaultValue;
								newCell.setCellValue(data);
							}
						}
						if (StringUtils.isBlank(data)) {
							data = "";
							newCell.setCellValue(data);
						}
					}
					newRowCount++;
				}
				logger.debug("writing xlsx into new xlsx file completed..." + CommonDateHelper.formatDateAsString(new Date()));
				System.out.println("total rows processed --- " + newRowCount);
				out = new FileOutputStream(outputFilePath);
				wbNew.write(out);

				isProcessed = Boolean.TRUE;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (wb != null) {
						wb.close();
					}
					/*if (wbNew != null) {
						wbNew.close();
					}*/
					if (out != null) {
						out.close();
					}

				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}

		}
		return isProcessed;
	}
public List<String> getHeadersFromXLSXFile(String filePath){
		
		logger.debug("in getHeadersFromXLSXFile .. ");
		
    	List<String> headers = new ArrayList<>();
    	boolean valid = isValidExcel();
		
		if (valid) {
			logger.debug("valid excel file .. ");
	    	try {
	    		File file = new File(filePath);
				Workbook workbook = StreamingReader.builder().rowCacheSize(1000).bufferSize(4096).open(file);
				int rowCount = 0;
				Sheet sheet = workbook.getSheetAt(0);
				for (Row r : sheet) {
						if(rowCount > 0){
							break;
						}
					
					  for (Cell c : r) {
						  String cellVal = c.getStringCellValue().trim();
						  
						  if(StringUtils.isNotEmpty(cellVal))
						    headers.add(cellVal.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
					  }
					  
					  rowCount++; 
				} 
			}catch (Exception e) {				 
				e.printStackTrace();
			}
		}	
    	
    	return headers;
    }
@SuppressWarnings("rawtypes")
public Map<String, Object> processXLSXDataBatch(String filePath, ClientData clientData, DataSource dataSource,String clientSchemaName) {
	
	Map<String, Object> result = new HashMap<>();
	Table table = clientData.getUserPackage().getTable();
	logger.debug("processing XLSX data batch.. ");
	
	boolean valid = isValidExcel();
	Workbook wb = null;
	InputStream is = null;
	if(valid){
		try {
			logger.debug("valid excel file .. ");
			logger.debug("processing XLSX data batch started..."+CommonDateHelper.formatDateAsString(new Date()));
			File file = new File(filePath);			
			is = new FileInputStream(file);
		
			logger.info("valid excel file .. ");
		
			List<String> columns = getHeadersFromXLSXFile(filePath);
			
			logger.debug("columns before applying pattern : {}", columns);
			
			for (int i = 0; i < columns.size(); i++) {
				String col = columns.get(i);
				col = col.trim().replaceAll("\\s+", "_");
				columns.set(i, col);
			}			 
			
			logger.debug("columns after applying pattern : {}", columns);
			
			String tableName = table.getTableName();
			String schemaName = table.getSchemaName();

			List<Column> tColumns = table.getColumns();
			int colslen = tColumns.size();

			logger.debug("Schema Name : {} --> Table Name : {} ", schemaName, tableName);

			logger.debug("Columns Size : {} ", colslen);
			
			String insertScript = buildInsertScript(table);

			DBDataOperations dbOp = new DBDataOperations();
			dbOp.setDataSource(dataSource);

			int total = 0, success = 0, fail = 0, duplicates = 0;
			
			int index = 0;
			 
			List<String> queries = new ArrayList<>();
			 
			wb = StreamingReader.builder().rowCacheSize(1000).bufferSize(4096).open(file);
			Sheet sheet = wb.getSheetAt(0);				
			Iterator rows = sheet.rowIterator();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
			SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = startDateFormat.format(new Date());
			String batchId = clientData.getUserId() +"_"+ clientData.getUserPackage().getPackageId() + "_" + clientData.getUserPackage().getPackageName() + "_" + sdf.format(new Date());
			
			while(rows.hasNext()){
			  StringBuilder insertQuery = new StringBuilder(insertScript);	
			  Row row = (Row) rows.next(); 
			  int getRowNumb = row.getRowNum();
			  
			  if(getRowNumb == 0){
				   continue; 
			  }
			   				 
			  for (int i = 0; i < colslen; i++) {
				    String value = null;				     
				  
				  	Column column = tColumns.get(i);
					String colname = column.getColumnName();
					String datatype = column.getDataType();
					int colIndex = columns.indexOf(colname);
					Cell cell = row.getCell(colIndex);
					if(cell != null){							
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							value = cell.getStringCellValue();
						}
						else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
							value = String.valueOf(cell.getBooleanCellValue());
						} 
						else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							
						  	if (DateUtil.isCellDateFormatted(cell)) {
						  		value = String.valueOf(cell.getDateCellValue());
							} 
							else{
								DecimalFormat dFormat = new DecimalFormat("#.######");

								String changeValue = dFormat.format(cell.getNumericCellValue());
								value = String.valueOf(changeValue);
							}
						}
						else if(cell.getCellType() == Cell.CELL_TYPE_BLANK){
							value = "";
						}
					}else{
						value = "";	
					}
					if (StringUtils.isBlank(value)) {
						insertQuery.append("coalesce(null,DEFAULT(").append(colname).append(")) ");
					} else {
					if("text".equalsIgnoreCase((String) datatype) || "varchar".equalsIgnoreCase(datatype)){
						insertQuery.append("'").append(value).append("'");
					} else if ("datetime".equalsIgnoreCase(datatype)) {
						logger.debug(value);
						insertQuery.append("'").append(value).append("'");
					} else {
						if ( NumberUtils.isNumber(value) || "bit".equalsIgnoreCase( datatype )) {
							insertQuery.append(value);
						} else {
							value = value.replaceAll("'", "''");
							insertQuery.append("'").append(value).append("'");
						}
					}

					}if (i < colslen - 1) {
						insertQuery.append(", ");

					}
				}
			 	  
			  	
			  insertQuery.append(" \n )");
				 
				queries.add(insertQuery.toString());
				total++;
				index++;
				
				if (index % 1000 == 0) {
					String[] querriesArr = queries.toArray(new String[0]);
					int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId()+"", clientData.getUserPackage().getPackageName(),  clientData.getUserId(),batchId);
					
					success += updates;
					fail += (queries.size()-updates);
					
					queries.clear();
					index = 0;
					logger.debug("executed records : "+total);
				}
			 
			}
				if (queries.size()>0) {
					String[] querriesArr = queries.toArray(new String[0]);
					int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId()+"", clientData.getUserPackage().getPackageName(),  clientData.getUserId(),batchId);
					
					success += updates;
					fail += (queries.size()-updates);
				}
				
				String endTime = startDateFormat.format(new Date());
				StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
				loadSummerySql.append(clientSchemaName).append(".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
				loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId).append("', '").append(tableName).append("', ").append(queries.size()).append(", ").append(success).append(", ").append(fail).append(", '").append(startTime).append("', '").append(endTime).append("', 'Success', 'unknown', now(), 'Custom');");
				try{dbOp.getJdbcTemplate().update(loadSummerySql.toString());}catch(Exception er){}
				
				logger.debug("Total Records : {} ", total);
				logger.debug("Sccuess Records : {} ", success);
				logger.debug("Failed Records : {} ", fail);
				logger.debug("Duplicate Records : {} ", duplicates);

				result.put("totalRecords", total);
				result.put("successRecords", success);
				result.put("failedRecords", fail);
				result.put("duplicateRecords", duplicates);					
				logger.debug("processing XLSX data batch completed..."+CommonDateHelper.formatDateAsString(new Date()));		
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		finally{
			try {
				if(wb != null){
					wb.close();
				} 
				if(is != null){
					is.close();
				}
				
			}catch (IOException e) {				
				e.printStackTrace();
			}	
		}	
	}		
	return result;
}

	public static void main(String args[]) throws  IOException{
			String filePath = "C:\\Users\\apurva.deshmukh\\Desktop\\ORDR_DTL_ExcelFile_2016-08-08T17_54_11.xls";				 
			ParseExcel p = new ParseExcel(filePath);
			List<String> list;
			try {
				list = p.getHeadersFromFile(filePath);
				System.out.println(list);				
			} catch (Exception e) {				
				e.printStackTrace();
			}
			
		}
	public List<String> getColumnsDataType(String filePath) {
		logger.debug("get column data types of excel file.. ");
		List<String> datatypes = new ArrayList<>();
		boolean valid = isValidExcel();

		if (valid) {
			logger.info("valid excel file .. ");
			Workbook wb = null;
			try {
				List<String> columns = getHeadersFromFile(filePath);
				wb = getWorkbookInstance(filePath);
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rows = sheet.rowIterator();

				while (rows.hasNext()) {
					Row row = (Row) rows.next();
					int getRowNumb = row.getRowNum();

					if (getRowNumb == 0) {
						// just skip the rows if row number is 0
						continue;
					}
					int colslen = columns.size();
					for (int i = 0; i < colslen; i++) {
						String value = String.valueOf(row.getCell(i));
						if (value.matches("^[-]?[0-9*]{1,10}$")) {
							datatypes.add("INT");
						} else if (value.matches("^[-]?[0-9]{10,}$")) {
							datatypes.add("BIGINT");
						} else if (value.matches("^[0-1]{1}$")) {
							datatypes.add("BIT");
						} else if (value.matches("^([-]?\\d*\\.\\d*)$")) {
							datatypes.add("DECIMAL");
						} else if (value.matches("[0-9a-zA-z!\"\',/@#$*\\s]*")) {
							datatypes.add("VARCHAR");
						} else {
							datatypes.add("VARCHAR");
						}

					}
					if (getRowNumb > 0) {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (wb != null) {
					try {
						wb.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return datatypes;
	}
	public List<String> getColumnDataTypesFromXLSXFile(String filePath) {
		logger.debug("in getColumnDataTypesFromXLSXFile .. ");
		List<String> dataTypes = new ArrayList<>();
		boolean valid = isValidExcel();

		if (valid) {
			logger.debug("valid excel file .. ");
			File file = new File(filePath);
			InputStream is = null;
			Workbook wb = null;
			try {
				is = new FileInputStream(file);
				wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);

				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rows = sheet.rowIterator();
				List<String> columns = getHeadersFromXLSXFile(filePath);
				int colslen = columns.size();
				int total = 0;
				while (rows.hasNext()) {
					if (total >= 1)
						break;
					Row row = (Row) rows.next();

					int getRowNumb = row.getRowNum();

					if (getRowNumb == 0) {
						continue;
					}

					for (int i = 0; i < colslen; i++) {
						Cell cell = row.getCell(i);
						if (cell == null) {
							dataTypes.add("VARCHAR");
						} else {
							if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
								dataTypes.add("VARCHAR");
							} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
								dataTypes.add("VARCHAR");
							} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

								if (DateUtil.isCellDateFormatted(cell)) {
									dataTypes.add("VARCHAR");
								} else {
									DecimalFormat dFormat = new DecimalFormat("#.###");

									String changeValue = dFormat.format(cell.getNumericCellValue());
									if (changeValue.matches("^[-]?[0-9*]{1,10}$")) {
										dataTypes.add("INT");
									} else if (changeValue.matches("^[-]?[0-9]{10,}$")) {
										dataTypes.add("BIGINT");
									} else if (changeValue.matches("^([-]?\\d*\\.\\d*)$")) {
										dataTypes.add("DECIMAL");
									}

								}
							} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								dataTypes.add("VARCHAR");
							}
						}

					}
					total++;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (wb != null)
					try {
						wb.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		return dataTypes;
	}

	public List<LinkedHashMap<String, Object>> parseXLSToListofMap(String absolutePath) throws IOException{
		LinkedHashMap<String, Object> excelDataMap = null;
		List<LinkedHashMap<String, Object>> parsedXLSToListMap= new ArrayList<>();
		ParseExcel parseExcel = new ParseExcel(absolutePath);
		Workbook wb=null;
		try{
			boolean isValid=isValidExcel();
			List<String> xlsHeaders= getHeadersFromFile(absolutePath);
			if(isValid){
				wb=getWorkbookInstance(absolutePath);
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rows= sheet.rowIterator();
				while(rows.hasNext()){
					excelDataMap = new LinkedHashMap<String,Object>();
					Row row = rows.next();
					if(row.getRowNum() == 0){
						//skip
						continue;
					}
					for(int i=0; i<xlsHeaders.size();i++){
						String  value=null;
						Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
						logger.info("cell data:::: "+cell);
						if(cell != null){
							Cell cellFormat= CommonUtils.formatExcelCellData(cell);
							value= String.valueOf(cellFormat);
						}
						else if (cell == null) {
							value=null;
					    }
						excelDataMap.put(xlsHeaders.get(i), value);
					}
					parsedXLSToListMap.add(excelDataMap);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if(wb!=null){
				wb.close();
			}
		}
		return parsedXLSToListMap;
	}

	public List<LinkedHashMap<String, Object>> parseXLSXToListofMap(String absolutePath) {
		LinkedHashMap<String, Object> xlsxDataMap = null;
		List<LinkedHashMap<String, Object>> parsedXLSXToListMap= new ArrayList<>();
		ParseExcel parseExcel = new ParseExcel(absolutePath);
		Workbook wb = null;
		try{
			boolean isValid=isValidExcel();
			List<String> xlsxheaders= getHeadersFromFile(absolutePath);
			if(isValid){
				wb= getWorkbookInstance(absolutePath);
				//File file = new File(absolutePath);
				//wb= StreamingReader.builder().rowCacheSize(1000).bufferSize(4096).open(file);
				Sheet sheet = wb.getSheetAt(0);
				 Iterator<Row> rows = sheet.rowIterator();
				 while(rows.hasNext()){
					 xlsxDataMap = new LinkedHashMap<String, Object>();
					 Row row = rows.next();
					 if(row.getRowNum()==0){
						 continue;
					 }
					 for(int i=0; i < xlsxheaders.size();i++){
						 String value=null;
						 Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
						 if(cell != null){
								Cell cellFormat= CommonUtils.formatExcelCellData(cell);
								value= String.valueOf(cellFormat);
							}
							else if (cell == null) {
								value=null;
						    }

						 xlsxDataMap.put(xlsxheaders.get(i), value);
					 }
					 parsedXLSXToListMap.add(xlsxDataMap);
				 }
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(wb!=null){
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return parsedXLSXToListMap;
	}
}
