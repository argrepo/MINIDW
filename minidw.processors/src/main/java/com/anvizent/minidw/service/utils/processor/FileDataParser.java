package com.anvizent.minidw.service.utils.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.minidw.service.utils.helper.CommonUtils;
import com.anvizent.minidw.service.utils.helper.ParseCSVDynamicCharset;
import com.anvizent.minidw.service.utils.helper.ParseExcel;
import com.datamodel.anvizent.common.exception.UploadandExecutionFailedException;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.model.Column;
import com.monitorjbl.xlsx.StreamingReader;
import com.opencsv.CSVReader;

@Component
public class FileDataParser {

	private static final Logger logger = LoggerFactory.getLogger(FileDataParser.class);

	public boolean isValidFile(MultipartFile multipartfile) {
		String fileName;
		String filetype;
		fileName = multipartfile.getOriginalFilename();
		filetype = FilenameUtils.getExtension(fileName);
		boolean validFile = filetype.equalsIgnoreCase("csv") || filetype.equals("xls") || filetype.equals("xlsx");
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

	public static boolean isValidExcel(String FILE_PATH) {
		boolean valid = false;

		if (FILE_PATH != null && FILE_PATH.length() > 0) {
			File excelFile = new File(FILE_PATH);
			valid = excelFile.exists();
		} else {
			logger.info("File path is empty or null .. {}  ", FILE_PATH);
		}
		return valid;
	}

	public Map<Integer, String> getTotalNoOfSheets(String absolutePath, String fileType, String fileName) {
		Map<Integer, String> sheetsInfo = new LinkedHashMap<>(); 
		try {
			switch (fileType) {
			case Constants.FileType.CSV:
				sheetsInfo.put(1, fileName);
				break;
			case Constants.FileType.XLS:
				Workbook wb = getWorkbookInstance(absolutePath);
				for (int i=0; i < wb.getNumberOfSheets(); i++) {
					sheetsInfo.put(i, wb.getSheetName(i));
				}
				
				break;
			case Constants.FileType.XLSX:
				File file = new File(absolutePath);
				Workbook workbook = StreamingReader.builder().rowCacheSize(1000).bufferSize(4096).open(file);
				for (int i=0; i < workbook.getNumberOfSheets(); i++) {
					sheetsInfo.put(i, workbook.getSheetName(i));
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sheetsInfo;
	}

	public static Workbook getWorkbookInstance(String filePath) throws Exception {
		Workbook wb = null;
		InputStream excelFileToRead = new FileInputStream(filePath);
		wb = (Workbook) WorkbookFactory.create(excelFileToRead);
		return wb;
	}

	public List<LinkedHashMap<String, Object>> getDataFromFile(int sheetNo, File tempFile,
			List<String> fileMandatoryHeaders, String filetype, String separatorChar, String stringQuoteChar,
			String charset) throws IOException {
		List<LinkedHashMap<String, Object>> listOfMapFileData = null;
		switch (filetype) {
		case Constants.FileType.CSV:
			listOfMapFileData = parseCSVToListOfMap(sheetNo, tempFile.getAbsolutePath(), fileMandatoryHeaders,
					separatorChar, stringQuoteChar, charset);
			break;

		case Constants.FileType.XLS:
			listOfMapFileData = parseXLSToListofMap(sheetNo, tempFile.getAbsolutePath(), fileMandatoryHeaders);
			break;

		case Constants.FileType.XLSX:
			listOfMapFileData = parseXLSXToListofMap(sheetNo, tempFile.getAbsolutePath(), fileMandatoryHeaders);
			break;
		}
		return listOfMapFileData;
	}

	public List<LinkedHashMap<String, Object>> parseCSVToListOfMap(int sheetNo, String csvFilepath,
			List<String> fileMandatoryHeaders, String separatorChar, String stringQuoteChar, String charset)
			throws IOException {
		LinkedHashMap<String, Object> map;
		List<LinkedHashMap<String, Object>> parsedCSVToListMap = new ArrayList<>();
		CSVReader reader = null;
		try {
			reader = createReader(csvFilepath, separatorChar, stringQuoteChar, charset);
			List<String> headers = processCsvHeader(reader);
			String[] data = reader.readNext();
			int dataRow = 1; 
			while (data != null) {
				dataRow++;
				map = new LinkedHashMap<String, Object>();
				for (int i = 0; i < headers.size(); i++) {
					Object obj = data[i].isEmpty() ? null : data[i];
					if (obj == null) {
						if (fileMandatoryHeaders.contains(headers.get(i))) {
							throw new Exception(
									"Cell is empty at Column " + headers.get(i) + " and Row no : " + dataRow);
						} else {
							obj = null;	
						}
					}
					map.put(headers.get(i), obj);
				}
				parsedCSVToListMap.add(map);
				data = reader.readNext();
			}
		} catch (Throwable t) {
			throw new UploadandExecutionFailedException(t);
		} finally {
			logger.debug(" Closing CSV Reader Object .. ");
			if (reader != null)
				reader.close();
		}
		return parsedCSVToListMap;
	}

	public List<LinkedHashMap<String, Object>> parseXLSToListofMap(int sheetNo, String absolutePath,
			List<String> fileMandatoryHeaders) throws IOException {
		LinkedHashMap<String, Object> excelDataMap = null;
		List<LinkedHashMap<String, Object>> parsedXLSToListMap = new ArrayList<>();
		Workbook wb = null;
		try {
			boolean isValid = isValidExcel(absolutePath);
			List<String> xlsHeaders = getHeadersFromFile(absolutePath);
			if (isValid) {
				wb = getWorkbookInstance(absolutePath);
				Sheet sheet = wb.getSheetAt(sheetNo);
				Iterator<Row> rows = sheet.rowIterator();
				while (rows.hasNext()) {
					excelDataMap = new LinkedHashMap<String, Object>();
					Row row = rows.next();
					if (row.getRowNum() == 0) {
						// skip
						continue;
					}
					for (int i = 0; i < xlsHeaders.size(); i++) {
						String value = null;
						Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
						//logger.info("cell data:::: " + cell);
						if (cell != null) {
							/*Cell cellFormat = CommonUtils.formatExcelCellData1(cell);
							value = String.valueOf(cellFormat);*/
							value = CommonUtils.formatExcelCellData1(cell);
						} else if (cell == null) {
							if (fileMandatoryHeaders.contains(xlsHeaders.get(i))) {
								throw new Exception("Cell is empty at Column " + xlsHeaders.get(i)
										+ " and Row no : " + (row.getRowNum()+1) + " Sheet " + sheet.getSheetName());
							} else {
								value = null;
							}
						}
						excelDataMap.put(xlsHeaders.get(i), value);
					}
					parsedXLSToListMap.add(excelDataMap);
				}
			}
		} catch (Throwable t) {
			throw new UploadandExecutionFailedException(t);
		} finally {
			if (wb != null) {
				wb.close();
			}
		}
		return parsedXLSToListMap;
	}
	public static void main(String[] args) {
		String s = "Gregorian_DateTime_Val,Gregorian_Date_Val,Gregorian_Date_Key,Day_Of_Month,Day_Name,Day_Of_week,Week_Of_Year,Week_Of_Month,Calendar_Month,Calendar_Month_Name,Calendar_Year,Calendar_Yrmo,Calendar_Quarter,Calendar_Period";
		parseXLSXToListofMap(0, "C:/Users/vinodkumar.basireddy/Desktop/Calander_Excels/Playcore_IL_Account_Calendar_Data1.xlsx" ,Arrays.asList(s.split(",")));
	}

	public static List<LinkedHashMap<String, Object>> parseXLSXToListofMap(int sheetNo, String absolutePath,
			List<String> fileMandatoryHeaders) {
		LinkedHashMap<String, Object> xlsxDataMap = null;
		List<LinkedHashMap<String, Object>> parsedXLSXToListMap = new ArrayList<>();
		Workbook wb = null;
		try {
			boolean isValid = isValidExcel(absolutePath);
			List<String> xlsxheaders = getHeadersFromFile(absolutePath);
			if (isValid) {
				wb = getWorkbookInstance(absolutePath);
				Sheet sheet = wb.getSheetAt(sheetNo);
				Iterator<Row> rows = sheet.rowIterator();
				while (rows.hasNext()) {
					xlsxDataMap = new LinkedHashMap<String, Object>();
					Row row = rows.next();
					if (row.getRowNum() == 0) {
						continue;
					}
					for (int i = 0; i < xlsxheaders.size(); i++) {
						String value = null;
						Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
						if (cell != null) {
							 value = CommonUtils.formatExcelCellData1(cell);
						} else if (cell == null) {
							if (fileMandatoryHeaders.contains(xlsxheaders.get(i))) {
								throw new Exception("Cell is empty at Column " + xlsxheaders.get(i)
							     	+ " and Row no : " + (row.getRowNum()+1) + " Sheet " + sheet.getSheetName());
							} else {
								value = null;
							}
						}
						xlsxDataMap.put(xlsxheaders.get(i), value);
					}
					parsedXLSXToListMap.add(xlsxDataMap);
				}
			}
		} catch (Throwable t) {
			throw new UploadandExecutionFailedException(t);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return parsedXLSXToListMap;
	}

	@SuppressWarnings({ "deprecation", "rawtypes" })
	public static List<String> getHeadersFromFile(String filePath) throws Exception {
		List<String> headers = new ArrayList<>();
		Workbook wb = getWorkbookInstance(filePath);
		Sheet sheet = wb.getSheetAt(0);

		Iterator rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = (Row) rows.next();
			Iterator cells = row.cellIterator();

			while (cells.hasNext()) {
				String data = null;
				Cell cell = (Cell) cells.next();
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					data = cell.getStringCellValue();
					headers.add(data.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell))
						data = cell.getDateCellValue().toString();
					else {
						data = String.valueOf(cell.getNumericCellValue());
					}
					headers.add(data.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					data = String.valueOf(cell.getBooleanCellValue());
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

	public List<String> processCsvHeader(CSVReader reader) throws IOException {

		List<String> columns = null;

		String[] headerRow = reader.readNext();

		columns = new ArrayList<>();

		for (String col : headerRow) {
			columns.add(col.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
		}

		return columns;
	}

	public CSVReader createReader(String filepath, String separatorChar, String stringQuoteChar, String charset)
			throws FileNotFoundException, UnsupportedEncodingException {

		CSVReader reader = null;

		logger.debug("creating csv reader object using given file path.");

		File csvFile = new File(filepath);

		boolean separatorPresent = separatorChar != null && separatorChar.length() == 1;
		boolean quotecharPresent = stringQuoteChar != null && stringQuoteChar.length() == 1;
		if (StringUtils.isNotBlank(charset) && charset.equalsIgnoreCase(Constants.Config.ENCODING_TYPE)) {
			if (separatorPresent && quotecharPresent) {
				reader = new CSVReader(
						new InputStreamReader(new FileInputStream(csvFile), Constants.Config.ENCODING_TYPE),
						separatorChar.charAt(0), stringQuoteChar.charAt(0));
			} else if (separatorPresent) {
				reader = new CSVReader(
						new InputStreamReader(new FileInputStream(csvFile), Constants.Config.ENCODING_TYPE),
						separatorChar.charAt(0));
			} else {
				reader = new CSVReader(
						new InputStreamReader(new FileInputStream(csvFile), Constants.Config.ENCODING_TYPE), ',', '"');
			}
		} else {
			if (separatorPresent && quotecharPresent) {
				reader = new CSVReader(new FileReader(csvFile), separatorChar.charAt(0), stringQuoteChar.charAt(0));
			} else if (separatorPresent) {
				reader = new CSVReader(new FileReader(csvFile), separatorChar.charAt(0));
			} else {
				reader = new CSVReader(new FileReader(csvFile), ',', '"');
			}
		}
		return reader;
	}

	/*public String buildInsertScript(List<Column> columns, String tableName) {
		StringBuilder insertScript = new StringBuilder();
		int collength = columns.size();
		insertScript.append("INSERT INTO ").append(tableName).append(" ( \n ");
		for (int i = 0; i < collength; i++) {
			String column = columns.get(i).getColumnName();
			String colname = column;
			insertScript.append(colname);
			if (i < collength - 1)
				insertScript.append(", ");
		}
		insertScript.append(" \n ) VALUES ( \n ");
		for (int j = 0; j < collength; j++) {
			insertScript.append("?");
			if (j < collength - 1)
				insertScript.append(", ");
		}
		insertScript.append(" \n )");

		return insertScript.toString();
	}*/
	
	public String buildInsertScriptWithFileHeaders(List<String> headers, String tableName) {
		StringBuilder insertScript = new StringBuilder();
		int collength = headers.size();
		insertScript.append("INSERT INTO ").append(tableName).append(" ( \n ");
		for (int i = 0; i < collength; i++) {
			String column = headers.get(i);
			String colname = column;
			insertScript.append(colname);
			if (i < collength - 1)
				insertScript.append(", ");
		}
		insertScript.append(" \n ) VALUES ( \n ");
		for (int j = 0; j < collength; j++) {
			insertScript.append("?");
			if (j < collength - 1)
				insertScript.append(", ");
		}
		insertScript.append(" \n )");

		return insertScript.toString();
	}

	/*For only Anvizent Calender Data Upload */
	public LinkedHashMap<String, Object> processFileDataMapList(Integer calendarKey, JdbcTemplate clientJdbcTemplate,
			List<Column> columns, String insertDateIntoTableName, List<String> headers,
			List<LinkedHashMap<String, Object>> fileDataMapList) {

		LinkedHashMap<String, Object> processedBatchData = new LinkedHashMap<String, Object>();
		try {
			//String insertScript = buildInsertScript(columns, insertDateIntoTableName);
			headers.add(0, "Account_Calendar_Key");
			String insertScript = buildInsertScriptWithFileHeaders(headers, insertDateIntoTableName);
			clientJdbcTemplate.execute(new ConnectionCallback<Integer>() {
				int noOfCon = 0;
				int total = 0, success = 0;
				List<String> failMessageList = new ArrayList<String>();
				@Override
				public Integer doInConnection(Connection con) throws SQLException, DataAccessException {
					noOfCon++;
					PreparedStatement ps = con.prepareStatement(insertScript);
					int rowNumber = 0;
					for (Map<String, Object> map : fileDataMapList) {
						rowNumber++;
						int index = 1;
						for (String header : headers) {
							Object data = map.get(header);
							if (data != null) {
								String trimmedData = data.toString().trim().toLowerCase();
								if (StringUtils.isBlank(trimmedData) || trimmedData.equals("null")) {
									data = null;
								}
							}
							if(index == 1){
								ps.setObject(index++, calendarKey);
							}else{
								ps.setObject(index++, data);
							}
						}
						try {
							success += ps.executeUpdate();
						} catch (Exception e) {
							String failMessage = "Row Number:::: " + rowNumber + " Error Messsage:::: "
									+ MinidwServiceUtil.getErrorMessageString(e);
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return processedBatchData;
	}

}
