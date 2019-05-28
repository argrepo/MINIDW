/**
 * 
 */
package com.datamodel.anvizent.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datamodel.anvizent.helper.ParseCSV.DBDataOperations;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.Table;
import com.monitorjbl.xlsx.StreamingReader;

/**
 * @author apurva.deshmukh
 *
 */
public class ParseExcel {
	private String filePath = null;

	public ParseExcel(String filePath) {
		this.filePath = filePath;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ParseExcel.class);

	public boolean isValidExcel() {
		LOGGER.debug("validation excel file .. ");

		boolean valid = false;

		if (filePath != null && filePath.length() > 0) {
			File excelFile = new File(filePath);
			valid = excelFile.exists();
		} else {
			LOGGER.info("File path is empty or null .. {}", filePath);
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

		for (int i = 0; i < colslen; i++) {
			String column = tColumns.get(i);
			String colname = column;
			insertScript.append("`").append(colname).append("`");

			if (i < colslen - 1)
				insertScript.append(", ");
		}
		insertScript.append(" \n ) VALUES ( \n ");

		return insertScript.toString();
	}

	public Workbook getWorkbookInstance(String filePath) throws EncryptedDocumentException, InvalidFormatException, IOException {
		Workbook wb = null;
		InputStream excelFileToRead = new FileInputStream(filePath);
		wb = (Workbook) WorkbookFactory.create(excelFileToRead);
		return wb;
	}

	@SuppressWarnings("deprecation")
	public List<String> getHeadersFromFile(String filePath) throws EncryptedDocumentException, InvalidFormatException, IOException {

		// get headers from excel file
		List<String> headers = new ArrayList<>();
		Workbook wb = getWorkbookInstance(filePath);
		Sheet sheet = wb.getSheetAt(0);

		Iterator<Row> rows = sheet.rowIterator();
		while (rows.hasNext()) {
			Row row = (Row) rows.next();
			Iterator<Cell> cells = row.cellIterator();

			while (cells.hasNext()) {
				String data = null;
				Cell cell = (Cell) cells.next();
				switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						data = cell.getStringCellValue().trim();
						if(StringUtils.isNotEmpty(data))
							headers.add(data.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
						break;
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell))
							
							data = cell.getDateCellValue().toString().trim();
						else {
							data = String.valueOf(cell.getNumericCellValue());
						}
						if(StringUtils.isNotEmpty(data))
						   headers.add(data.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
						break;
					case Cell.CELL_TYPE_BOOLEAN:
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

	public List<String> getColumnsDataType(String filePath) {
		LOGGER.debug("get column data types of excel file.. ");
		List<String> datatypes = new ArrayList<>();
		boolean valid = isValidExcel();

		if (valid) {
			LOGGER.info("valid excel file .. ");
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
				LOGGER.error("", e);
			} finally {
				if (wb != null) {
					try {
						wb.close();
					} catch (IOException e) {
						LOGGER.error("", e);
					}
				}
			}
		}
		return datatypes;
	}

	public Map<String, Object> processExcelDataBatch(String filePath, ClientData clientData, DataSource dataSource, String clientSchemaName) {
		Map<String, Object> result = new HashMap<>();
		Table table = clientData.getUserPackage().getTable();
		LOGGER.debug("processing excel data batch.. ");

		boolean valid = isValidExcel();
		Workbook wb = null;
		if (valid) {
			LOGGER.info("valid excel file .. ");
			try {
				List<String> columns = getHeadersFromFile(filePath);

				LOGGER.debug("columns before applying pattern : {}", columns);

				for (int i = 0; i < columns.size(); i++) {
					String col = columns.get(i);
					col = col.trim().replaceAll("\\s+", "_");
					columns.set(i, col);
				}

				LOGGER.debug("columns after applying pattern : {}", columns);

				String tableName = table.getTableName();
				String schemaName = table.getSchemaName();

				List<Column> tColumns = table.getColumns();
				int colslen = tColumns.size();

				LOGGER.debug("Schema Name : {} --> Table Name : {} ", schemaName, tableName);

				LOGGER.debug("Columns Size : {} ", colslen);

				String insertScript = buildInsertScript(table);

				DBDataOperations dbOp = new DBDataOperations();
				dbOp.setDataSource(dataSource);

				int total = 0, success = 0, fail = 0, duplicates = 0;

				int index = 0;

				List<String> queries = new ArrayList<>();

				wb = getWorkbookInstance(filePath);
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rows = sheet.rowIterator();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
				SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startTime = startDateFormat.format(new Date());
				String batchId = clientData.getUserId() + "_" + clientData.getUserPackage().getPackageId() + "_" + clientData.getUserPackage().getPackageName()
						+ "_" + sdf.format(new Date());

				while (rows.hasNext()) {
					StringBuilder insertQuery = new StringBuilder(insertScript);
					Row row = (Row) rows.next();
					int getRowNumb = row.getRowNum();

					if (getRowNumb == 0) {
						// just skip the rows if row number is 0
						continue;
					}

					for (int i = 0; i < colslen; i++) {
						String value = null;

						Column column = tColumns.get(i);
						String colname = column.getColumnName();
						String datatype = column.getDataType();
						int colIndex = columns.indexOf(colname);
						Cell cell = row.getCell(colIndex);
						if (cell != null) {
							Cell formattedCell = CommonUtils.formatExcelCellData(row.getCell(colIndex));
							value = String.valueOf(formattedCell);
						} else {
							value = "";
						}
						if (StringUtils.isBlank(value)) {
							insertQuery.append("coalesce(null,DEFAULT(").append(colname).append(")) ");
						} else {
							if ("text".equalsIgnoreCase((String) datatype) || "varchar".equalsIgnoreCase(datatype)) {
								insertQuery.append("'").append(value).append("'");
							} else if ("datetime".equalsIgnoreCase(datatype)) {
								insertQuery.append("'").append(value).append("'");
							} else {
								if (NumberUtils.isNumber(value) || "bit".equalsIgnoreCase(datatype)) {
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
						int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId() + "",
								clientData.getUserPackage().getPackageName(), clientData.getUserId(), batchId);

						success += updates;
						fail += (queries.size() - updates);

						queries.clear();
						index = 0;
						LOGGER.debug("executed records : " + total);
					}

				}
				if (queries.size() > 0) {
					String[] querriesArr = queries.toArray(new String[0]);
					int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId() + "",
							clientData.getUserPackage().getPackageName(), clientData.getUserId(), batchId);

					success += updates;
					fail += (queries.size() - updates);
				}

				String endTime = startDateFormat.format(new Date());
				StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
				loadSummerySql.append(clientSchemaName).append(
						".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
				loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId).append("', '").append(tableName).append("', ")
						.append(queries.size()).append(", ").append(success).append(", ").append(fail).append(", '").append(startTime).append("', '")
						.append(endTime).append("', 'Success', 'unknown', now(), 'Custom');");
				try {
					dbOp.getJdbcTemplate().update(loadSummerySql.toString());
				} catch (Exception er) {
				}

				LOGGER.debug("Total Records : {} ", total);
				LOGGER.debug("Sccuess Records : {} ", success);
				LOGGER.debug("Failed Records : {} ", fail);
				LOGGER.debug("Duplicate Records : {} ", duplicates);

				result.put("totalRecords", total);
				result.put("successRecords", success);
				result.put("failedRecords", fail);
				result.put("duplicateRecords", duplicates);

			} catch (Exception e) {
				LOGGER.error("", e);
			} finally {
				if (wb != null) {
					try {
						wb.close();
					} catch (IOException e) {
						LOGGER.error("", e);
					}
				}
			}
		}
		return result;
	}

	public List<List<String>> processExcelDataForPreview(String filePath) throws Exception {

		List<List<String>> processedData = new ArrayList<>();
		LOGGER.debug("processing excel data for preview .. ");

		boolean valid = isValidExcel();
		Workbook wb = null;
		if (valid) {
			LOGGER.debug("valid excel file .. ");
			wb = getWorkbookInstance(filePath);
			Sheet sheet = wb.getSheetAt(0);
			try {
				List<String> columns = getHeadersFromFile(filePath);
				processedData.add(columns);
				int colslen = columns.size();
				int total = 1;
				Iterator<Row> rows = sheet.rowIterator();

				while (rows.hasNext()) {
					if (total > 10)
						break;
					Row row = (Row) rows.next();

					int getRowNumb = row.getRowNum();
					if (getRowNumb == 0) {
						continue;
					}

					List<String> rowlist = new ArrayList<String>();
					for (int i = 0; i < colslen; i++) {
						String value = null;
						Cell cell = row.getCell(i);
						if (cell != null) {
							Cell formattedCell = CommonUtils.formatExcelCellData(cell);
							value = String.valueOf(formattedCell);
						} else {
							value = "";
						}
						rowlist.add(value);
					}
					total++;
					processedData.add(rowlist);
				}
			} finally {
				if (wb != null)
					wb.close();
			}
		}
		return processedData;
	}

	public static void main(String args[]) throws IOException {
		String filePath = "C:\\Users\\apurva.deshmukh\\Desktop\\ORDR_DTL_ExcelFile_2016-08-08T17_54_11.xls";
		ParseExcel p = new ParseExcel(filePath);
		List<String> list;
		try {
			list = p.getHeadersFromFile(filePath);
			System.out.println(list);
		} catch (Exception e) {
			LOGGER.error("", e);
		}

	}

	public boolean processXLSDataToFile(String outputFilePath, List<String> iLColumnNames, List<String> selectedFileHeaders, List<String> dafaultValues) {
		boolean isProcessed = Boolean.FALSE;
		LOGGER.debug("in processExcelDataToFile .. ");

		boolean valid = isValidExcel();

		if (valid) {
			LOGGER.debug("valid excel file .. ");
			Workbook wb = null;
			Workbook wbNew = null;
			FileOutputStream out = null;
			try {
				wbNew = new HSSFWorkbook();
				Sheet sheetNew = wbNew.createSheet("Sheet1");

				wb = getWorkbookInstance(filePath);
				Sheet sheet = wb.getSheetAt(0);
				List<String> columns = getHeadersFromFile(filePath);

				Row new_row1 = sheetNew.createRow(0);
				int i = 0;
				for (String iLColumn : iLColumnNames) {
					Cell newCell = new_row1.createCell(i);
					newCell.setCellValue(iLColumn);
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
						Cell new_cell = newRows.createCell(j);
						Cell cell = null;
						String fileHeader = selectedFileHeaders.get(j);
						int colIndex;
						String data = null;
						if (StringUtils.isNotBlank(fileHeader)) {
							colIndex = columns.indexOf(fileHeader.trim());
							cell = row.getCell(colIndex);
							if (cell != null) {
								Cell formattedCell = CommonUtils.formatExcelCellData(cell);
								data = String.valueOf(formattedCell);
								new_cell.setCellValue(data);
							} else {
								data = "";
								new_cell.setCellValue(data);
							}

							if (StringUtils.contains(data, ",")) {
								new_cell.setCellValue(data);
							}
						} else {
							String dafaultValue = dafaultValues.get(j);
							if (StringUtils.isNotBlank(dafaultValue)) {
								data = dafaultValue;
								new_cell.setCellValue(data);
							}
						}
						if (StringUtils.isBlank(data)) {
							data = "";
							new_cell.setCellValue(data);
						}
					}
					newRowCount++;
				}

				out = new FileOutputStream(outputFilePath);
				wbNew.write(out);

				isProcessed = Boolean.TRUE;
			} catch (Exception e) {
				LOGGER.error("", e);
			} finally {
				try {
					if (wb != null) {
						wb.close();
					}
					if (wbNew != null) {
						wbNew.close();
					}
					if (out != null) {
						out.close();
					}

				} catch (Exception e2) {
				}
			}

		}
		return isProcessed;
	}

	/* NEW CODE FOR XLSX */

	@SuppressWarnings("deprecation")
	public List<String> getHeadersFromXLSXFile(String filePath) {

		LOGGER.debug("in getHeadersFromXLSXFile .. ");

		List<String> headers = new ArrayList<>();
		boolean valid = isValidExcel();

		if (valid) {
			LOGGER.debug("valid excel file .. ");
			try {
				File file = new File(filePath);
				InputStream is = new FileInputStream(file);

				StreamingReader reader = StreamingReader.builder()
						// number of rows to keep in memory (defaults to 10)
						.rowCacheSize(100)
						// buffer size to use when reading InputStream to file
						// (defaults to 1024)
						.bufferSize(4096)
						// index of sheet to use (defaults to 0)
						.sheetIndex(0)
						// InputStream or File for XLSX file (required)
						.read(is);

				int rowCount = 0;
				for (Row r : reader) {
					if (rowCount > 0) {
						break;
					}

					for (Cell c : r) {
						String colVal = c.getStringCellValue().trim();
						if(StringUtils.isNotEmpty(colVal))
							headers.add(colVal.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
					}

					rowCount++;
				}
			} catch (FileNotFoundException e) {
				LOGGER.error("", e);
			}
		}

		return headers;
	}

	@SuppressWarnings("deprecation")
	public List<String> getColumnDataTypesFromXLSXFile(String filePath) {
		LOGGER.debug("in getColumnDataTypesFromXLSXFile .. ");
		List<String> dataTypes = new ArrayList<>();
		boolean valid = isValidExcel();

		if (valid) {
			LOGGER.debug("valid excel file .. ");
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
				LOGGER.error("", e);
			} finally {
				if (wb != null)
					try {
						wb.close();
					} catch (IOException e) {
						LOGGER.error("", e);
					}
			}
		}
		return dataTypes;
	}

	@SuppressWarnings("deprecation")
	public List<List<String>> processXLSXDataForPreview(String filePath) {
		List<List<String>> processedData = new ArrayList<>();
		LOGGER.debug("in processXLSXDataForPreview .. ");

		boolean valid = isValidExcel();

		if (valid) {
			LOGGER.debug("valid excel file .. ");
			File file = new File(filePath);
			InputStream is = null;
			Workbook wb = null;
			try {
				is = new FileInputStream(file);
				wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(is);

				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rows = sheet.rowIterator();
				List<String> columns = getHeadersFromXLSXFile(filePath);
				processedData.add(columns);
				int colslen = columns.size();
				int total = 0;
				while (rows.hasNext()) {
					if (total > 10)
						break;
					Row row = (Row) rows.next();

					int getRowNumb = row.getRowNum();

					if (getRowNumb == 0) {
						continue;
					}
					List<String> rowlist = new ArrayList<>();

					for (int i = 0; i < colslen; i++) {
						Cell cell = row.getCell(i);
						if (cell == null) {
							rowlist.add("");
						} else {
							if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
								rowlist.add(cell.getStringCellValue());
							} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
								rowlist.add(cell.getBooleanCellValue() + "");
							} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

								if (DateUtil.isCellDateFormatted(cell)) {
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									String data = dateFormat.format(cell.getDateCellValue());
									rowlist.add(data);
								} else {
									DecimalFormat dFormat = new DecimalFormat("#.###");

									String changeValue = dFormat.format(cell.getNumericCellValue());
									rowlist.add(changeValue);
								}
							} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								rowlist.add("");
							}
						}

					}
					total++;
					processedData.add(rowlist);
				}
			} catch (FileNotFoundException e) {
				LOGGER.error("", e);
			} finally {
				if (wb != null)
					try {
						wb.close();
					} catch (IOException e) {
						LOGGER.error("", e);
					}
			}
		}
		return processedData;
	}

	@SuppressWarnings("deprecation")
	public boolean processXLSXDataToFile(String filePath, String outputFilePath, List<String> iLColumnNames, List<String> selectedFileHeaders,
			List<String> dafaultValues) {
		boolean isProcessed = Boolean.FALSE;
		LOGGER.debug("in processXLSXDataToFile .. ");

		boolean valid = isValidExcel();

		if (valid) {
			LOGGER.debug("valid excel file .. ");
			LOGGER.debug("writing xlsx into new xlsx file started..." + CommonDateHelper.formatDateAsString(new Date()));
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
				LOGGER.debug("writing xlsx into new xlsx file completed..." + CommonDateHelper.formatDateAsString(new Date()));
				System.out.println("total rows processed --- " + newRowCount);
				out = new FileOutputStream(outputFilePath);
				wbNew.write(out);

				isProcessed = Boolean.TRUE;
			} catch (Exception e) {
				LOGGER.error("", e);
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
				}
			}

		}
		return isProcessed;
	}

	@SuppressWarnings("deprecation")
	public Map<String, Object> processXLSXDataBatch(String filePath, ClientData clientData, DataSource dataSource, String clientSchemaName) {
		Map<String, Object> result = new HashMap<>();
		Table table = clientData.getUserPackage().getTable();
		LOGGER.debug("processing XLSX data batch.. ");

		boolean valid = isValidExcel();
		Workbook wb = null;
		if (valid) {
			try {
				LOGGER.debug("valid excel file .. ");
				LOGGER.debug("processing XLSX data batch started..." + CommonDateHelper.formatDateAsString(new Date()));
				File file = new File(filePath);
				LOGGER.info("valid excel file .. ");
				List<String> columns = getHeadersFromXLSXFile(filePath);

				LOGGER.debug("columns before applying pattern : {}", columns);

				for (int i = 0; i < columns.size(); i++) {
					String col = columns.get(i);
					col = col.trim().replaceAll("\\s+", "_");
					columns.set(i, col);
				}

				LOGGER.debug("columns after applying pattern : {}", columns);

				String tableName = table.getTableName();
				String schemaName = table.getSchemaName();

				List<Column> tColumns = table.getColumns();
				int colslen = tColumns.size();

				LOGGER.debug("Schema Name : {} --> Table Name : {} ", schemaName, tableName);

				LOGGER.debug("Columns Size : {} ", colslen);

				String insertScript = buildInsertScript(table);

				DBDataOperations dbOp = new DBDataOperations();
				dbOp.setDataSource(dataSource);

				int total = 0, success = 0, fail = 0, duplicates = 0;

				int index = 0;

				List<String> queries = new ArrayList<>();

				wb = StreamingReader.builder().rowCacheSize(1000).bufferSize(4096).open(file);
				Sheet sheet = wb.getSheetAt(0);
				Iterator<Row> rows = sheet.rowIterator();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
				SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String startTime = startDateFormat.format(new Date());
				String batchId = clientData.getUserId() + "_" + clientData.getUserPackage().getPackageId() + "_" + clientData.getUserPackage().getPackageName()
						+ "_" + sdf.format(new Date());

				while (rows.hasNext()) {
					StringBuilder insertQuery = new StringBuilder(insertScript);
					Row row = (Row) rows.next();
					int getRowNumb = row.getRowNum();

					if (getRowNumb == 0) {
						// just skip the rows if row number is 0
						continue;
					}

					for (int i = 0; i < colslen; i++) {
						String value = null;

						Column column = tColumns.get(i);
						String colname = column.getColumnName();
						String datatype = column.getDataType();
						int colIndex = columns.indexOf(colname);
						Cell cell = row.getCell(colIndex);
						if (cell != null) {
							if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
								value = cell.getStringCellValue();
							} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
								value = String.valueOf(cell.getBooleanCellValue());
							} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

								if (DateUtil.isCellDateFormatted(cell)) {
									SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									value = dateFormat.format(cell.getDateCellValue());
								} else {
									DecimalFormat dFormat = new DecimalFormat("#.######");
									String changeValue = dFormat.format(cell.getNumericCellValue());
									value = String.valueOf(changeValue);
								}
							} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
								value = "";
							}
						} else {
							value = "";
						}
						if (StringUtils.isBlank(value)) {
							insertQuery.append("coalesce(null,DEFAULT(").append(colname).append(")) ");
						} else {
							if ("text".equalsIgnoreCase((String) datatype) || "varchar".equalsIgnoreCase(datatype)) {
								insertQuery.append("'").append(value).append("'");
							} else if ("datetime".equalsIgnoreCase(datatype)) {
								LOGGER.debug(value);
								insertQuery.append("'").append(value).append("'");
							} else {
								if (NumberUtils.isNumber(value) || "bit".equalsIgnoreCase(datatype)) {
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
						int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId() + "",
								clientData.getUserPackage().getPackageName(), clientData.getUserId(), batchId);

						success += updates;
						fail += (queries.size() - updates);

						queries.clear();
						index = 0;
						LOGGER.debug("executed records : " + total);
					}

				}
				if (queries.size() > 0) {
					String[] querriesArr = queries.toArray(new String[0]);
					int updates = dbOp.batchExecute(querriesArr, tableName, clientSchemaName, clientData.getUserPackage().getPackageId() + "",
							clientData.getUserPackage().getPackageName(), clientData.getUserId(), batchId);

					success += updates;
					fail += (queries.size() - updates);
				}

				String endTime = startDateFormat.format(new Date());
				StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
				loadSummerySql.append(clientSchemaName).append(
						".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
				loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId).append("', '").append(tableName).append("', ")
						.append(queries.size()).append(", ").append(success).append(", ").append(fail).append(", '").append(startTime).append("', '")
						.append(endTime).append("', 'Success', 'unknown', now(), 'Custom');");
				try {
					dbOp.getJdbcTemplate().update(loadSummerySql.toString());
				} catch (Exception er) {
				}

				LOGGER.debug("Total Records : {} ", total);
				LOGGER.debug("Sccuess Records : {} ", success);
				LOGGER.debug("Failed Records : {} ", fail);
				LOGGER.debug("Duplicate Records : {} ", duplicates);

				result.put("totalRecords", total);
				result.put("successRecords", success);
				result.put("failedRecords", fail);
				result.put("duplicateRecords", duplicates);
				LOGGER.debug("processing XLSX data batch completed..." + CommonDateHelper.formatDateAsString(new Date()));

			} catch (Exception e) {
				LOGGER.error("", e);
			} finally {
				try {
					if (wb != null) {
						wb.close();
					}

				} catch (IOException e) {
					LOGGER.error("", e);
				}
			}
		}
		return result;
	}
}
