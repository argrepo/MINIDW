package com.anvizent.minidw.service.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.anvizent.amazon.AmazonS3Utilities;
import com.anvizent.minidw.service.utils.helper.CSVWriter;
import com.anvizent.minidw.service.utils.helper.CommonDateHelper;
import com.anvizent.minidw.service.utils.helper.CommonUtils;
import com.anvizent.minidw.service.utils.helper.ParseCSV;
import com.anvizent.minidw.service.utils.helper.ParseCSV.DBDataOperations;
import com.anvizent.minidw.service.utils.helper.ParseExcel;
import com.anvizent.minidw.service.utils.io.FileNameContainsFilter;
import com.anvizent.minidw.service.utils.model.ETLjobExecutionMessages;
import com.datamodel.anvizent.common.exception.AnvizentRuntimeException;
import com.datamodel.anvizent.common.exception.CSVConversionException;
import com.datamodel.anvizent.common.exception.ClassPathException;
import com.datamodel.anvizent.common.exception.OnpremFileCopyException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.helper.minidw.MasterAndSlaveEndPoints;
import com.datamodel.anvizent.security.EncryptionServiceImpl;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.ClientJobExecutionParameters;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Database;
import com.datamodel.anvizent.service.model.Hierarchical;
import com.datamodel.anvizent.service.model.ILConnection;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.InstanceInfo;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.PackageExecution;
import com.datamodel.anvizent.service.model.S3BucketInfo;
import com.datamodel.anvizent.service.model.SchedulerMaster;
import com.datamodel.anvizent.service.model.SchedulerSlave;
import com.datamodel.anvizent.service.model.SourceFileInfo;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.TableDerivative;
import com.datamodel.anvizent.service.model.User;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.monitorjbl.xlsx.StreamingReader;

/**
 * @author mahender.alaveni
 *
 */
public class MinidwServiceUtil {

	protected static final Log LOGGER = LogFactory.getLog(MinidwServiceUtil.class);
	private static EtlJobUtil etlJobUtil = new EtlJobUtil(Constants.Config.COMMON_ETL_JOBS, Constants.Config.ETL_JOBS,
			System.getProperty(Constants.Config.SYSTEM_PATH_SEPARATOR));

	/**
	 * @param contextParams
	 * @param paramsVals
	 */
	public static void parseContextParams(final Map<String, String> contextParams,
			final Map<String, String> paramsVals) {

		Set<Map.Entry<String, String>> set = contextParams.entrySet();

		for (Map.Entry<String, String> entry : set) {

			String paramval = entry.getValue();

			if (paramval.indexOf("{") != -1) {

				int endindex = paramval.indexOf("}");

				String key = paramval.substring(1, endindex);
				String value = paramsVals.get(key);

				if (value != null) {
					entry.setValue(paramval.replace("{" + key + "}", value));
				} else
					System.out.println(key + " --> " + paramval + " --> " + value);
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addFileToClassPath(URL u) throws ClassPathException {

		ClassLoader c = CommonUtils.class.getClassLoader();
		URLClassLoader sysloader = (URLClassLoader) c;
		Class sysclass = URLClassLoader.class;

		Method method;
		try {
			method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new ClassPathException("Unable to add jobs to claa path", e);
		}

	}

	/**
	 * @param webServiceApi
	 * @param finalformattedApiResponse
	 * @return
	 */
	public static String getFilePathForWsApi(WebServiceApi webServiceApi,
			List<LinkedHashMap<String, Object>> finalformattedApiResponse) {

		CSVWriter writer = new CSVWriter();

		String fileDir = createDir(
				com.datamodel.anvizent.helper.Constants.Temp.getTempFileDir() + "fileMappingWithIL/");

		File file = new File(fileDir);
		if (!file.exists() || !file.isDirectory()) {
			if (!file.isDirectory()) {
				file.delete();
			}

			file.mkdirs();
		}

		String newfilename = webServiceApi.getApiName().replaceAll("\\s+", "_") + "_" + generateUniqueIdWithTimestamp();
		String filePath = fileDir + newfilename + ".csv";

		if (finalformattedApiResponse != null && filePath != null) {
			try {
				writer.writeAsCSV(finalformattedApiResponse, filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}

	/**
	 * @param dirName
	 * @return
	 */
	public static String createDir(String dirName) {

		if (StringUtils.isNotBlank(dirName)) {
			if (!new File(dirName).exists()) {
				new File(dirName).mkdirs();
				System.out.println("dir created:" + dirName);
			}

		}
		return dirName;
	}

	/**
	 * @param params
	 * @return
	 */
	public static String[] convertToContextParamsArray(Map<String, String> params) {
		if (params == null || params.size() == 0)
			return new String[0];

		String[] parameters = new String[params.size()];

		Set<Map.Entry<String, String>> set = params.entrySet();

		int index = 0;
		for (Map.Entry<String, String> entry : set) {
			String param = "--context_param " + entry.getKey() + "=" + entry.getValue();
			parameters[index] = param;
			index++;

		}

		return parameters;
	}

	/**
	 * @return
	 */
	public static String getSystemPathSeparator() {
		return System.getProperty(Constants.Config.SYSTEM_PATH_SEPARATOR);
	}

	/**
	 * @param dir
	 * @param match
	 */
	public static void deleteFilesFromDirWithMatching(String dir, final String match) {

		if (StringUtils.isNotBlank(dir) && StringUtils.isNotBlank(match)) {
			System.out.println("deleting Job files...");
			File fileDir = new File(dir);

			File[] files = fileDir.listFiles(new FileNameContainsFilter(match));

			for (final File file : files) {
				if (!file.delete()) {
					System.err.println("Can't remove " + file.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * @return
	 */
	public static String generateUniqueIdWithTimestamp() { 

		String op = "";

		try {
			DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss_SS");
			Date currentTime = new Date();

			op = format.format(currentTime);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return op;
	}

	/**
	 * @param excelFile
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "deprecation"  })
	public static File getCsvFromXLSX(File excelFile) throws Exception {
		System.out.println("writing xlsx into csv started..." + CommonDateHelper.formatDateAsString(new Date()));
		File tempFile = excelFile;
		String excelFilePath = tempFile.getAbsolutePath();
		String fileName = StringUtils.substring(tempFile.getName(), 0,
				StringUtils.ordinalIndexOf(tempFile.getName(), ".", 1));
		System.out.println("excelFilePath " + excelFilePath);
		FileWriter fw = null;
		InputStream excelFileToRead = null;

		File newCsvFileName = null;
		Workbook wb = null;

		try {
			excelFileToRead = new FileInputStream(excelFilePath);
			wb = StreamingReader.builder().rowCacheSize(1000).bufferSize(4096).open(excelFile);
			// int nummberOfSheets = wb.getNumberOfSheets(); //keep this in
			// below for loop instead of '1' to iterate though multiple sheets.
			for (int j = 0; j < 1; j++) {
				Sheet sheet = wb.getSheetAt(0);
				String sheetName = sheet.getSheetName();
				String dirPath = CommonUtils
						.createDir(CommonUtils.getTempDir() + File.separator + Constants.Config.CSV_FROM_EXCEL);
				String timestamp = CommonDateHelper.formatDateAsTimeStamp(new Date());
				newCsvFileName = new File(dirPath + "/" + fileName + "_" + sheetName + "_" + timestamp + ".csv");
				fw = new FileWriter(newCsvFileName);

				Iterator rows = sheet.rowIterator();
				ParseExcel parseExcel = new ParseExcel(excelFilePath);
				List<String> columns = parseExcel.getHeadersFromXLSXFile(excelFilePath);
				int colslen = columns.size();

				for (int i = 0; i < colslen; i++) {
					fw.append(columns.get(i));

					if (i < colslen)
						fw.append(",");

				}
				fw.append(System.getProperty("line.separator"));

				while (rows.hasNext()) {
					Row row = (Row) rows.next();
					int getRowNumb = row.getRowNum();
					if (getRowNumb == 0) {
						continue; // just skip the rows if row number is 0
					}
					String value = null;
					for (int i = 0; i < colslen; i++) {
						Cell cell = row.getCell(i);
						if (cell != null) {
							if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
								value = cell.getStringCellValue();
							} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
								value = String.valueOf(cell.getBooleanCellValue());
							} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

								if (DateUtil.isCellDateFormatted(cell)) {
									value = String.valueOf(cell.getDateCellValue());
								} else {
									DecimalFormat dFormat = new DecimalFormat("#.######");

									String changeValue = dFormat.format(cell.getNumericCellValue());
									value = String.valueOf(changeValue);
								}
							}

						} else {
							value = "";
						}

						if (value != null) {
							if (StringUtils.contains(value, ",")) {
								value = value.replaceAll(",", " ").replaceAll("\\s+", " ");
							}
							fw.append(sanitizeForCsv(value));
						}
						if (i < colslen)
							fw.append(",");

					}
					fw.append(System.getProperty("line.separator"));
				}
			}

		} finally {
			try {
				if (fw != null) {
					fw.flush();
					fw.close();
				}
				if (excelFileToRead != null) {
					excelFileToRead.close();
				}
				if (wb != null) {
					wb.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		System.out.println("writing xlsx into csv completed..." + CommonDateHelper.formatDateAsString(new Date()));
		return newCsvFileName;
	}

	/**
	 * @param excelFile
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static File getCsvFromXLS(File excelFile) throws Exception {
		System.out.println("writing xls into csv started..." + CommonDateHelper.formatDateAsString(new Date()));
		File tempFile = excelFile;
		String excelFilePath = tempFile.getAbsolutePath();
		String fileName = StringUtils.substring(tempFile.getName(), 0,
				StringUtils.ordinalIndexOf(tempFile.getName(), ".", 1));
		System.out.println("excelFilePath " + excelFilePath);
		FileWriter fw = null;
		InputStream excelFileToRead = null;

		File newCsvFileName = null;
		HSSFWorkbook wb = null;

		try {
			excelFileToRead = new FileInputStream(excelFilePath);

			wb = new HSSFWorkbook(excelFileToRead);
			// int nummberOfSheets = wb.getNumberOfSheets();
			for (int j = 0; j < 1; j++) {
				Sheet sheet = wb.getSheetAt(0);
				String sheetName = sheet.getSheetName();
				String dirPath = CommonUtils
						.createDir(CommonUtils.getTempDir() + File.separator + Constants.Config.CSV_FROM_EXCEL);
				String timestamp = CommonDateHelper.formatDateAsTimeStamp(new Date());
				newCsvFileName = new File(dirPath + "/" + fileName + "_" + sheetName + "_" + timestamp + ".csv");
				fw = new FileWriter(newCsvFileName);

				Iterator rows = sheet.rowIterator();
				ParseExcel parseExcel = new ParseExcel(excelFilePath);
				List<String> columns = parseExcel.getHeadersFromFile(excelFilePath);
				int colslen = columns.size();

				for (int i = 0; i < colslen; i++) {
					fw.append(columns.get(i));

					if (i < colslen)
						fw.append(",");

				}
				fw.append(System.getProperty("line.separator"));

				while (rows.hasNext()) {
					Row row = (Row) rows.next();
					int getRowNumb = row.getRowNum();
					if (getRowNumb == 0) {
						continue;
					}
					String value = null;
					for (int i = 0; i < colslen; i++) {
						Cell cell = row.getCell(i);
						if (cell != null) {
							Cell formattedCell = formatExcelCellData(cell);
							value = String.valueOf(formattedCell);
						} else {
							value = "";
						}

						if (value != null) {
							if (StringUtils.contains(value, ",")) {
								value = value.replaceAll(",", " ").replaceAll("\\s+", " ");
							}
							fw.append(sanitizeForCsv(value));
						}
						if (i < colslen)
							fw.append(",");

					}
					fw.append(System.getProperty("line.separator"));
				}
			}

		} finally {
			try {
				if (fw != null) {
					fw.flush();
					fw.close();
				}
				if (excelFileToRead != null) {
					excelFileToRead.close();
				}
				if (wb != null) {
					wb.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		System.out.println("writing xls into csv completed..." + CommonDateHelper.formatDateAsString(new Date()));
		return newCsvFileName;
	}

	/**
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Cell formatExcelCellData(Cell cell) {
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				String s = cell.getStringCellValue();
				if (s.matches("^[0-9]*\\.?[0]$")) {
					String s2 = s.replace(".0", "");
					cell.setCellValue(s2);
				}

				break;
			case Cell.CELL_TYPE_NUMERIC:
				double numericCellValue = cell.getNumericCellValue();
				String numStr = String.valueOf(numericCellValue);
				if (DateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					String formattedCellValue = CommonDateHelper.formatDateAsString(date);
					cell.setCellValue(formattedCellValue);
				} else if (numStr.matches("^[0-9]*\\.?[0]$")) {
					String s2 = numStr.replace(".0", "");
					cell.setCellValue(s2);
				}

				break;
			case Cell.CELL_TYPE_BOOLEAN:
				break;
			default:
				break;
			}
		}

		return cell;
	}

	/**
	 * @param cellData
	 * @return
	 */
	public static String sanitizeForCsv(String cellData) {

		if (StringUtils.isEmpty(cellData))
			return "";
		cellData = cellData.trim();
		StringBuilder resultBuilder = new StringBuilder(cellData);
		int lastIndex = 0;
		while (resultBuilder.indexOf("\"", lastIndex) >= 0) {
			int quoteIndex = resultBuilder.indexOf("\"", lastIndex);
			resultBuilder.replace(quoteIndex, quoteIndex + 1, "\"\"");
			lastIndex = quoteIndex + 2;
		}

		char firstChar = cellData.charAt(0);
		char lastChar = cellData.charAt(cellData.length() - 1);

		if (cellData.contains(",") || cellData.contains("\r") || cellData.contains("\r\n") || cellData.contains("\n")
				|| Character.isWhitespace(firstChar) || Character.isWhitespace(lastChar)) {
			resultBuilder.insert(0, "\"").append("\"");
		}
		return resultBuilder.toString();
	}

	/**
	 * @param clientstagingJdbcTemplate
	 * @param ilConnectionMappingInfo
	 * @param clientSchemaStaging
	 * @return
	 */
	public static ILConnectionMapping getIlConnection(JdbcTemplate clientstagingJdbcTemplate,
			ILConnectionMapping ilConnectionMappingInfo, String clientSchemaStaging) {

		ILConnectionMapping ilConnectionMappings = new ILConnectionMapping();

		ILConnection ilConnections = new ILConnection();

		BasicDataSource basicDataSource = (BasicDataSource) clientstagingJdbcTemplate.getDataSource();

		String databaseInfo1[] = StringUtils.substringBetween(basicDataSource.getUrl(), "//", "/").split(":");
		String databaseHost1 = databaseInfo1[0];
		String databasePort1 = databaseInfo1[1];

		ilConnectionMappings.setiLquery(ilConnectionMappingInfo.getiLquery());
		ilConnections.setUsername(basicDataSource.getUsername());
		ilConnections.setPassword(basicDataSource.getPassword());
		ilConnections.setServer(databaseHost1 + ":" + databasePort1 + "/" + clientSchemaStaging);

		Database db = new Database();

		db.setId(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
		db.setConnector_id(com.anvizent.minidw.client.jdbc.utils.Constants.Database.MYSQL);
		db.setDriverName(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDrivers.MYSQL_DRIVER_CLASS);
		db.setProtocal(com.anvizent.minidw.client.jdbc.utils.Constants.DataBaseDriverURL.MYSQL_DB_URL);

		ilConnections.setDatabase(db);
		ilConnectionMappings.setTypeOfCommand(com.datamodel.anvizent.helper.Constants.QueryType.QUERY);
		ilConnectionMappings.setiLConnection(ilConnections);

		return ilConnectionMappings;
	}

	public static List<String> getListFromString(String data, String separatorChar) {
		return Arrays.asList(StringUtils.split(data, separatorChar));
	}

	public static String getListFromString(List<String> data, String separatorChar) {
		return String.join(separatorChar, data.toArray(new String[] {}));
	}

	public static void getConcatinatedString(List<String> curreniesList, String currency, String concatWith) {
		if (StringUtils.isNotBlank(currency)) {
			curreniesList.add(concatWith + currency + concatWith);
		}
	}

	/**
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getPropertiesKeyValuePairs(String filePath) throws IOException {
		File packagePropertiesFile = new File(filePath);
		Properties packageProperties = new Properties();
		FileReader packagePropertiesReader = null;
		Map<String, String> propertiesKeyValueMap = new LinkedHashMap<>();
		try {
			packagePropertiesReader = new FileReader(packagePropertiesFile);
			packageProperties.load(packagePropertiesReader);
			Enumeration<?> propertyNames = packageProperties.propertyNames();
			while (propertyNames.hasMoreElements()) {
				String propertyKey = propertyNames.nextElement().toString();
				String propertyValue = packageProperties.getProperty(propertyKey).toLowerCase().trim();
				propertiesKeyValueMap.put(propertyKey, propertyValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (packagePropertiesReader != null)
				packagePropertiesReader.close();
		}

		return propertiesKeyValueMap;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getServicePropertiesKeyValuePairs() throws IOException {
		Resource resourceService = new ClassPathResource("/messages/serviceMessages.properties");
		return getPropertiesKeyValuePairs(resourceService.getFile().getAbsolutePath());
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> getApplicationPropertiesKeyValuePairs() throws IOException {
		Resource resourceService = new ClassPathResource("/messages/applicationMessages.properties");
		return getPropertiesKeyValuePairs(resourceService.getFile().getAbsolutePath());
	}

	/**
	 * @param errorMessage
	 * @return
	 */
	public static DataResponse getErrorDataResponse(String errorMessage) {
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();
		dataResponse.addMessage(message);
		message.setCode("ERROR");
		message.setText(errorMessage);
		return dataResponse;
	}

	/**
	 * @param originalFile
	 * @param s3BucketInfo
	 * @param s3FolderName
	 * @return
	 * @throws OnpremFileCopyException
	 * @throws AmazonS3Exception
	 */
	
	public static SourceFileInfo uploadFileToS3(File originalFile, S3BucketInfo s3BucketInfo, String s3FolderName,boolean isEncryptionRequired)
			throws OnpremFileCopyException, AmazonS3Exception {
		SourceFileInfo sourceFileInfo = new SourceFileInfo();
		if (StringUtils.isNotBlank(s3BucketInfo.getAccessKey()) && StringUtils.isNotBlank(s3BucketInfo.getSecretKey())
				&& StringUtils.isNotBlank(s3BucketInfo.getBucketName())) {
			if (originalFile != null) {
				AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(),
						s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
				// String folderName = ""; //userId + SUFFIX + packegeId +
				// SUFFIX;
				sourceFileInfo.setUploadStartTime(CommonDateHelper.formatDateAsString(new Date()));
				LOGGER.info("file upload started..." + new Date());
				String s3filePath = amazonS3Utilities.uploadFileToS3(originalFile, s3FolderName, isEncryptionRequired);
				LOGGER.info("file upload completed..." + new Date());
				sourceFileInfo.setUploadEndTime(CommonDateHelper.formatDateAsString(new Date()));
				LOGGER.info("File uploaded to S3 :" + s3filePath);
				sourceFileInfo.setFilePath(s3filePath);
			} else {
				throw new AmazonS3Exception("Invalid details for s3 upload");
			}
		} else {
			throw new AmazonS3Exception("Invalid s3 bucket details ");
		}
		return sourceFileInfo;
	}

	/**
	 * @param originalFile
	 * @param s3BucketInfo
	 * @param isMultiPartRequired
	 * @param s3LogicalDirPath
	 * @return
	 * @throws OnpremFileCopyException
	 * @throws AmazonS3Exception
	 */
	public static SourceFileInfo uploadFileToS3(File originalFile, S3BucketInfo s3BucketInfo,
			boolean isMultiPartRequired, String s3LogicalDirPath,boolean isEncryptionRequired) throws OnpremFileCopyException, AmazonS3Exception {

		SourceFileInfo sourceFileInfo = null;
		String fileSizeInBytes = "";
		int fileSize = 0;
		if (isMultiPartRequired) {
			if (originalFile.isDirectory()) {
				s3LogicalDirPath += "/";
				if (originalFile.listFiles() != null) {
					sourceFileInfo = new SourceFileInfo();
					sourceFileInfo.setUploadStartTime(CommonDateHelper.formatDateAsString(new Date()));
					sourceFileInfo.setFilePath(s3LogicalDirPath);
					for (File file : originalFile.listFiles()) {
						if (!file.isDirectory()) {
							uploadFileToS3(file, s3BucketInfo, s3LogicalDirPath,isEncryptionRequired);
							fileSize = fileSize + Math.round(file.length());
						}
					}
					sourceFileInfo.setFileSize(String.valueOf(fileSize));
					sourceFileInfo.setUploadEndTime(CommonDateHelper.formatDateAsString(new Date()));
					sourceFileInfo.setMultiPartFile(true);
				} else {
					throw new OnpremFileCopyException("Multipart files not found");
				}
			} else {
				throw new OnpremFileCopyException("Multipart folder not found");
			}
		} else {
			sourceFileInfo = uploadFileToS3(originalFile, s3BucketInfo, "",isEncryptionRequired);
			fileSizeInBytes  = String.valueOf(Math.round(originalFile.length()));
			sourceFileInfo.setFileSize(fileSizeInBytes);
			sourceFileInfo.setMultiPartFile(false);
		}
		return sourceFileInfo;
	}

	/**
	 * @param s3BucketInfo
	 * @param originalFile
	 * @param userId
	 * @param packegeId
	 * @param userName
	 * @param ilConnectionMappingId
	 * @param deploymentType
	 * @return
	 * @throws OnpremFileCopyException
	 * @throws AmazonS3Exception
	 * @throws IOException 
	 */
	public static SourceFileInfo getS3UploadedFileInfo(S3BucketInfo s3BucketInfo, File originalFile, String userId,
			Integer packegeId, String userName, Integer ilConnectionMappingId, String deploymentType,boolean isEncryptionRequired)
			throws OnpremFileCopyException, AmazonS3Exception, IOException {

		return getS3UploadedFileInfo(s3BucketInfo, originalFile, userId, packegeId, userName, ilConnectionMappingId,
				deploymentType, "", false,isEncryptionRequired);
	}
	
	public static SourceFileInfo getS3UploadedFileInfo(S3BucketInfo s3BucketInfo, File originalFile, String userId,
			Integer packegeId, String userName, Integer ilConnectionMappingId, String deploymentType,
			String s3LogicalDirPath, boolean isMultiPartRequired,boolean isEncryptionRequired)
			throws OnpremFileCopyException, AmazonS3Exception, IOException {

		return getS3UploadedFileInfo(s3BucketInfo, originalFile, userId, packegeId, userName, ilConnectionMappingId,
				deploymentType, s3LogicalDirPath, isMultiPartRequired,isEncryptionRequired, null);
	}

	/**
	 * @param s3BucketInfo
	 * @param originalFile
	 * @param userId
	 * @param packegeId
	 * @param userName
	 * @param ilConnectionMappingId
	 * @param deploymentType
	 * @param s3LogicalDirPath
	 * @param isMultiPartRequired
	 * @return
	 * @throws OnpremFileCopyException
	 * @throws AmazonS3Exception
	 * @throws IOException 
	 */
	public static SourceFileInfo getS3UploadedFileInfo(S3BucketInfo s3BucketInfo, File originalFile, String userId,
			Integer packegeId, String userName, Integer ilConnectionMappingId, String deploymentType,
			String s3LogicalDirPath, boolean isMultiPartRequired,boolean isEncryptionRequired, String headerStoraggePath) throws OnpremFileCopyException, AmazonS3Exception, IOException {

		if (originalFile == null) {
			throw new OnpremFileCopyException("Source file should not be empty");
		}
		String fileType = FilenameUtils.getExtension(originalFile.getAbsolutePath());
		SourceFileInfo sourceFileInfo = null;
		String storageType = Constants.Config.STORAGE_TYPE_S3;
		if (StringUtils.isNotBlank(deploymentType)
				&& deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			storageType = Constants.Config.STORAGE_TYPE_LOCAL;
		}
		if (!deploymentType.equals(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			if (s3BucketInfo != null) {
				sourceFileInfo = uploadFileToS3(originalFile, s3BucketInfo, isMultiPartRequired, s3LogicalDirPath,isEncryptionRequired);
				sourceFileInfo.setBucketName(s3BucketInfo.getBucketName());
			} else {
				throw new OnpremFileCopyException("S3 file upload failed (Bucket details not found).");
			}
		} else {
			String destinationPath = null;
			if ( StringUtils.isBlank(com.datamodel.anvizent.helper.minidw.Constants.Config.STORAGE_LOCATION) ) {
				if (StringUtils.isBlank(headerStoraggePath)) {
					destinationPath = Constants.Temp.getTempFileDir()+"/";
				} else {
					destinationPath = headerStoraggePath;
				}
			} else {
				destinationPath = com.datamodel.anvizent.helper.minidw.Constants.Config.STORAGE_LOCATION+"/";
			}
			
			destinationPath += s3LogicalDirPath+"/";
			if(isMultiPartRequired){
				FileUtils.copyDirectory(originalFile,new File(destinationPath));
			} else{
				String fileName = originalFile.getName();
				String fileExtention = fileName.substring(fileName.lastIndexOf("."), fileName.length());
				String formattedFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_"+ generateUniqueIdWithTimestamp();
				String logicalFileName = formattedFileName + fileExtention;
				destinationPath += logicalFileName;
				FileUtils.copyFile(originalFile, new File(destinationPath));
			}
			sourceFileInfo = new SourceFileInfo();
			sourceFileInfo.setFilePath(destinationPath);
			sourceFileInfo.setFileSize("0");
		}

		String fileName = FilenameUtils.getName(originalFile.getAbsolutePath());
		if (originalFile.isDirectory()) {
			sourceFileInfo.setFileName(fileName);
		} else {
			sourceFileInfo.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
		}
		sourceFileInfo.setUserId(Integer.parseInt(userId));
		sourceFileInfo.setFileType(fileType);
		sourceFileInfo.setDelimeter(",");
		sourceFileInfo.setPackageId(packegeId);
		sourceFileInfo.setStorageType(storageType);
		sourceFileInfo.setIlConnectionMappingId(ilConnectionMappingId);
		sourceFileInfo.setS3BucketInfo(s3BucketInfo);
		sourceFileInfo.setEncryptionRequired(isEncryptionRequired);
		
		Modification modification = new Modification(new Date());
		modification.setCreatedBy(userName);
		sourceFileInfo.setModification(modification);
		return sourceFileInfo;
	}

	/**
	 * @param filePath
	 * @param fileType
	 * @param separatorChar
	 * @param stringQuoteChar
	 * @return
	 * @throws Exception
	 */
	public static List<String> getHeadersFromFile(String filePath, String fileType, String separatorChar,
			String stringQuoteChar) throws Exception {

		List<String> headers = null;
		switch (fileType) {
		case Constants.FileType.CSV:
			// get the headers from the csv file
			LOGGER.info("in getHeadersFromCSV()");
			ParseCSV parseCSV = new ParseCSV(filePath);
			headers = parseCSV.readColumns(separatorChar, stringQuoteChar);
			break;
		case Constants.FileType.XLS:
			// get the headers from the xls file
			LOGGER.info("in getHeadersFromXLS()");
			ParseExcel parseExcel = new ParseExcel(filePath);
			headers = parseExcel.getHeadersFromFile(filePath);
			break;
		case Constants.FileType.XLSX:
			// get the headers from the xlsx file
			LOGGER.info("in getHeadersFromXLSX()");
			ParseExcel parseExcel1 = new ParseExcel(filePath);
			headers = parseExcel1.getHeadersFromXLSXFile(filePath);
			break;
		}
		return headers;
	}
	
	public static Boolean hasData(String filePath, String fileType, String separatorChar,
			String stringQuoteChar) throws Exception {
		boolean isDataExists = false;
		switch (fileType) {
		case Constants.FileType.CSV:
			// get the headers from the csv file
			LOGGER.info("in getHeadersFromCSV()");
			ParseCSV parseCSV = new ParseCSV(filePath);
			//ParseCSV csv= new ParseCSV(tempFile.getAbsolutePath());
			 isDataExists = parseCSV.hasData(filePath, separatorChar, stringQuoteChar);
			break;
		case Constants.FileType.XLS:
			// get the headers from the xls file
			LOGGER.info("in getHeadersFromXLS()");
			ParseExcel parseExcel = new ParseExcel(filePath);
			isDataExists = parseExcel.hasData(filePath);
			break;
		case Constants.FileType.XLSX:
			// get the headers from the xlsx file
			LOGGER.info("in getHeadersFromXLSX()");
			ParseExcel parseExcel1 = new ParseExcel(filePath);
			//headers = parseExcel1.getHeadersFromXLSXFile(filePath);
			isDataExists = parseExcel1.hasDataforXLSX(filePath);
			break;
		}
		return isDataExists;
	}

	/**
	 * @param filePath
	 * @param fileType
	 * @param separatorChar
	 * @param stringQuoteChar
	 * @return
	 * @throws Exception
	 */
	public static List<String> getColumnsDataTypeFromFile(String filePath, String fileType, String separatorChar,
			String stringQuoteChar) throws Exception {

		List<String> datatypes = null;
		switch (fileType) {
		case Constants.FileType.CSV:
			// get column data types from the csv file
			LOGGER.info("in getDataTypesFromCSV()");
			ParseCSV parseCSV = new ParseCSV(filePath);
			datatypes = parseCSV.getColumnsDataType(separatorChar, stringQuoteChar);
			break;
		case Constants.FileType.XLS:
			// get column data types from the xls file
			LOGGER.info("in getDataTypesFromXLS()");
			ParseExcel parseExcel = new ParseExcel(filePath);
			datatypes = parseExcel.getColumnsDataType(filePath);
			break;
		case Constants.FileType.XLSX:
			// get column data types from the xlsx file
			LOGGER.info("in getDataTypesFromXLSX()");
			ParseExcel parseExcel1 = new ParseExcel(filePath);
			datatypes = parseExcel1.getColumnDataTypesFromXLSXFile(filePath);
			break;
		}
		return datatypes;
	}

	/**
	 * @param ilContextParams
	 * @param stagingDbHost
	 * @param stagingDbPort
	 * @param stagingClientSchema
	 * @param stagingDbUname
	 * @param stagingDbPwd
	 * @param tagetDbHost
	 * @param tagetDbPort
	 * @param tagetClientSchema
	 * @param tagetDbUname
	 * @param tagetDbPwd
	 * @param masterDbHost
	 * @param masterDbPort
	 * @param masterClientSchema
	 * @param masterDbUname
	 * @param masterDbPwd
	 * @param dbTypeName
	 * @param iLInfo
	 * @param clientId
	 * @param userPackage
	 * @param jobFilesPath
	 * @param executionId
	 * @return
	 */
	
	public static Map<String, String> getContextParams(Map<String, String> ilContextParams, String stagingDbHost,
			String stagingDbPort, String stagingClientSchema, String stagingDbUname, String stagingDbPwd,
			String tagetDbHost, String tagetDbPort, String tagetClientSchema, String tagetDbUname, String tagetDbPwd,
			String masterDbHost, String masterDbPort, String masterClientSchema, String masterDbUname,
			String masterDbPwd, String dbTypeName, ILInfo iLInfo, String clientId, int packageId, String jobFilesPath,
			long executionId,ClientJobExecutionParameters clientJobExecutionParameters,boolean loadType) {

		Map<String, String> ilParamsVals = new LinkedHashMap<>();
		String datasourcename = null;
		
		jobFilesPath = jobFilesPath + clientId + "_" + iLInfo.getiL_id()+"/";
		
		createDir(jobFilesPath);
		
		// staging properties
		ilParamsVals.put("src_host", stagingDbHost);
		ilParamsVals.put("src_port", stagingDbPort);
		ilParamsVals.put("src_database", stagingClientSchema);
		ilParamsVals.put("src_user", stagingDbUname);
		ilParamsVals.put("src_pass", stagingDbPwd);

		// target database properties
		ilParamsVals.put("tgt_host", tagetDbHost);
		ilParamsVals.put("tgt_port", tagetDbPort);
		ilParamsVals.put("tgt_database", tagetClientSchema);
		ilParamsVals.put("tgt_user", tagetDbUname);
		ilParamsVals.put("tgt_pass", tagetDbPwd);

		// master database properties
		ilParamsVals.put("master_host", masterDbHost);
		ilParamsVals.put("master_port", masterDbPort);
		ilParamsVals.put("master_database", masterClientSchema);
		ilParamsVals.put("master_user", masterDbUname);
		ilParamsVals.put("master_pass", masterDbPwd);

		// staging table and target table
		ilParamsVals.put("tgt_table", iLInfo.getiL_table_name());
		ilParamsVals.put("stg_table", iLInfo.getiL_table_name() + "_Stg");

		// client id and package id
		ilParamsVals.put("client_id", clientId);
		ilParamsVals.put("package_id", packageId + "_" + executionId);

		ilParamsVals.put("error_log_path", jobFilesPath);
		ilParamsVals.put("bulk_path", jobFilesPath);

		ilParamsVals.put("job_name", iLInfo.getiL_table_name());
		String jobType = "IL";
		ilParamsVals.put("job_type", jobType);

		datasourcename = StringUtils.isNotBlank(dbTypeName) ? dbTypeName : "unknown";
		ilParamsVals.put("datasourcename", datasourcename);
		//client jobs execution params
		ilParamsVals.put("src_time_zone", convertToGSTFormat(clientJobExecutionParameters.getSourceTimeZone()));
		ilParamsVals.put("tgt_time_zone", convertToGSTFormat(clientJobExecutionParameters.getDestTimeZone()));
		ilParamsVals.put("case_sensitive_data_set", clientJobExecutionParameters.getCaseSensitive()+"");
		ilParamsVals.put("null_replacement_string", clientJobExecutionParameters.getNullReplaceValues());
		ilParamsVals.put("load_type", loadType ?  "Incremental" : "Full");
		ilParamsVals.put("trailing_months", String.valueOf(clientJobExecutionParameters.getInterval()));
		
		System.out.println(ilContextParams);
		parseContextParams(ilContextParams, ilParamsVals);

		return ilContextParams;

	}
	

	public static Map<String, String> getXRefContextParams(String stagingDbHost,
			String stagingDbPort, String stagingClientSchema, String stagingDbUname, String stagingDbPwd,
			String tagetDbHost, String tagetDbPort, String tagetClientSchema, String tagetDbUname, String tagetDbPwd,
			ILInfo iLInfo, String clientId, String jobFilesPath,
			String sourcePath, String referenceField, String xReferenceField, String applicableDate, long xrefConditionId,
			String executionType, String conditionName) {

		Map<String, String> ilParamsVals = new LinkedHashMap<>();
		
		jobFilesPath = jobFilesPath + clientId + "_" + xrefConditionId+"/";
		
		createDir(jobFilesPath);
		
		// staging properties
		ilParamsVals.put("SRC", iLInfo.getXref_il_table_name());
		ilParamsVals.put("SRC_HOST", stagingDbHost);
		ilParamsVals.put("SRC_PORT", stagingDbPort);
		ilParamsVals.put("SRC_DBNAME", stagingClientSchema);
		ilParamsVals.put("SRC_UN", stagingDbUname);
		ilParamsVals.put("SRC_PW", stagingDbPwd);

		// main database properties
		ilParamsVals.put("TGT", iLInfo.getiL_table_name());
		ilParamsVals.put("TGT_HOST", tagetDbHost);
		ilParamsVals.put("TGT_PORT", tagetDbPort);
		ilParamsVals.put("TGT_DBNAME", tagetClientSchema);
		ilParamsVals.put("TGT_UN", tagetDbUname);
		ilParamsVals.put("TGT_PW", tagetDbPwd);

		// stating database properties
		ilParamsVals.put("MASTER_HOST", stagingDbHost);
		ilParamsVals.put("MASTER_PORT", stagingDbPort);
		ilParamsVals.put("MASTER_DBNAME", stagingClientSchema);
		ilParamsVals.put("MASTER_UN", stagingDbUname);
		ilParamsVals.put("MASTER_PW", stagingDbPwd);
		
		ilParamsVals.put("ETL_JOBS", "ETL_Jobs");
		ilParamsVals.put("ETL_CONTROL_JOB", "ETL_CNTRL");
		ilParamsVals.put("ETL_JOB_ERROR_LOG", "ETL_JOB_ERROR_LOG");
		ilParamsVals.put("ETL_JOB_LOAD_SMRY", "ETL_JOB_LOAD_SMRY");
		
		ilParamsVals.put("DateFormat",
				"yyyy-MM-dd'T'HH:mm:ss'Z'@#dd,MM,yyyy' 'HH:mm:ss@#yyyy-MM-dd@#yyyy-MM-dd'T'HH:mm.ss'Z'@#yyyy-MM-dd'T'HH:mm:ss@#yyyy-MM-dd' 'HH:mm:ss@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy/MM/dd' 'HH:mm:ss@#MM/dd/yyyy' 'HH:mm:ss@#MM-dd-yyyy' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss'Z'@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy-MM-dd' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssXXX@#yyyy-dd-MM' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssSSS@#yyyy-dd-MM' 'HH:mm:ssSSS@#MM/dd/yyyy' 'HH:MM' AM'");

		// staging table and target table
		// client id and package id
		ilParamsVals.put("CLIENT_ID", clientId);
		

		ilParamsVals.put("FILE_SRC", sourcePath);
		ilParamsVals.put("BULK_PATH", jobFilesPath);
		ilParamsVals.put("FILE_PATH", jobFilesPath);

		ilParamsVals.put("JOB_NAME", iLInfo.getXref_il_table_name());
		ilParamsVals.put("XREF_NAME", iLInfo.getXref_il_table_name());
		ilParamsVals.put("JOB_ORDER_SEQ", 1+"");
		ilParamsVals.put("DATASOURCENAME", "XREF");
		String jobType = "X-REF";
		ilParamsVals.put("JOB_TYPE", jobType);
		ilParamsVals.put("JOB_STARTDATETIME", currentDateTime());
		ilParamsVals.put("PACKAGE_ID", "xref_" + iLInfo.getiL_id() + "_" + xrefConditionId + "_");
		ilParamsVals.put("REF_FOR_XREF", referenceField);
		ilParamsVals.put("XREF_FIELD", xReferenceField);
		ilParamsVals.put("APPLICABLE_DATE", applicableDate);
		ilParamsVals.put("EXECUTIONTYPE", executionType);
		ilParamsVals.put("XREFNAME", conditionName);
		
		
		LOGGER.info(ilParamsVals);
		return ilParamsVals;

	}
	
	
	public static Map<String, String> getAutoMergeXRefContextParams(String stagingDbHost,
			String stagingDbPort, String stagingClientSchema, String stagingDbUname, String stagingDbPwd,
			String tagetDbHost, String tagetDbPort, String tagetClientSchema, String tagetDbUname, String tagetDbPwd,
			ILInfo iLInfo, String clientId, String jobFilesPath,
			String jsonObject, String referenceField, String xReferenceField, String applicableDate, int xrefConditionId,
			String executionType, String conditionName) {

		Map<String, String> ilParamsVals = new LinkedHashMap<>();
		
		jobFilesPath = jobFilesPath + clientId + "_" + xrefConditionId+"/";
		
		createDir(jobFilesPath);
		
		// staging properties
		ilParamsVals.put("SRC", iLInfo.getXref_il_table_name());
		ilParamsVals.put("SRC_HOST", stagingDbHost);
		ilParamsVals.put("SRC_PORT", stagingDbPort);
		ilParamsVals.put("SRC_DBNAME", stagingClientSchema);
		ilParamsVals.put("SRC_UN", stagingDbUname);
		ilParamsVals.put("SRC_PW", stagingDbPwd);

		// main database properties
		ilParamsVals.put("TGT", iLInfo.getiL_table_name());
		ilParamsVals.put("TGT_HOST", tagetDbHost);
		ilParamsVals.put("TGT_PORT", tagetDbPort);
		ilParamsVals.put("TGT_DBNAME", tagetClientSchema);
		ilParamsVals.put("TGT_UN", tagetDbUname);
		ilParamsVals.put("TGT_PW", tagetDbPwd);

		// stating database properties
		ilParamsVals.put("MASTER_HOST", stagingDbHost);
		ilParamsVals.put("MASTER_PORT", stagingDbPort);
		ilParamsVals.put("MASTER_DBNAME", stagingClientSchema);
		ilParamsVals.put("MASTER_UN", stagingDbUname);
		ilParamsVals.put("MASTER_PW", stagingDbPwd);
		
		ilParamsVals.put("ETL_JOBS", "ETL_Jobs");
		ilParamsVals.put("ETL_CONTROL_JOB", "ETL_CNTRL");
		ilParamsVals.put("ETL_JOB_ERROR_LOG", "ETL_JOB_ERROR_LOG");
		ilParamsVals.put("ETL_JOB_LOAD_SMRY", "ETL_JOB_LOAD_SMRY");
		
		ilParamsVals.put("DateFormat",
				"yyyy-MM-dd'T'HH:mm:ss'Z'@#dd,MM,yyyy' 'HH:mm:ss@#yyyy-MM-dd@#yyyy-MM-dd'T'HH:mm.ss'Z'@#yyyy-MM-dd'T'HH:mm:ss@#yyyy-MM-dd' 'HH:mm:ss@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy/MM/dd' 'HH:mm:ss@#MM/dd/yyyy' 'HH:mm:ss@#MM-dd-yyyy' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss'Z'@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy-MM-dd' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssXXX@#yyyy-dd-MM' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssSSS@#yyyy-dd-MM' 'HH:mm:ssSSS@#MM/dd/yyyy' 'HH:MM' AM'");
		ilParamsVals.put("CLIENT_ID", clientId);

		ilParamsVals.put("FILE_SRC", jobFilesPath);
		ilParamsVals.put("JSON", jsonObject);

		ilParamsVals.put("JOB_NAME", iLInfo.getXref_il_table_name());
		ilParamsVals.put("XREF_NAME", iLInfo.getXref_il_table_name());
		ilParamsVals.put("JOB_ORDER_SEQ", 1+"");
		String jobType = "XREF_AUTO";
		ilParamsVals.put("DATASOURCENAME", jobType);
		ilParamsVals.put("JOB_TYPE", jobType);
		ilParamsVals.put("JOB_STARTDATETIME", currentDateTime());
		ilParamsVals.put("PACKAGE_ID", "xref_" + iLInfo.getiL_id() + "_" + xrefConditionId + "_");
		ilParamsVals.put("REF_FOR_XREF", referenceField);
		ilParamsVals.put("XREF_FIELD", xReferenceField);
		ilParamsVals.put("APPLICABLE_DATE", applicableDate);
		ilParamsVals.put("XREF_FIELD", "");
		ilParamsVals.put("BULK_PATH", jobFilesPath);
		ilParamsVals.put("FILE_PATH", jobFilesPath);
		ilParamsVals.put("EXECUTIONTYPE", executionType);
		ilParamsVals.put("XREFNAME", conditionName);
		
		
		LOGGER.info(ilParamsVals);
		return ilParamsVals;

	}

	/**
	 * @param ilContextParams
	 * @param iLInfo
	 * @param clientId
	 * @param userPackage
	 * @param filePath
	 * @param modification
	 * @param jobFilesPath
	 * @param user
	 * @return
	 * @throws CSVConversionException
	 * @throws TalendJobNotFoundException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static JobExecutionInfo runILEtlJob(Map<String, String> ilContextParams, ILInfo iLInfo, String clientId,
			int packageId, String filePath, Modification modification, String jobFilesPath, User user)
			throws CSVConversionException, TalendJobNotFoundException, InterruptedException, IOException {
		String csvFilePath = null;
		String xlsFilePath = null;
		String xlsxFilePath = null;
		String jobClass = iLInfo.getJobName();
		String dependencyJARs = iLInfo.getDependencyJars();
		String jobName = iLInfo.getJobName();
		int statusAfterJARrun = -1;
		JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
		try {
			if (StringUtils.isNotBlank(jobClass)) {
				ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
				filePath = (filePath.indexOf('\\') != -1) ? filePath.replaceAll("\\\\", "/") : filePath;
				String decryptedFileName = StringUtils.substringAfterLast(filePath, "/");
				String decryptedFileType[] = decryptedFileName.split("\\.");
				String sourceFileType = decryptedFileType[1];
				if (sourceFileType.equals(Constants.FileType.XLS)) {
					File xlsFile = new File(filePath);
					File csvFile = null;
					try {
						csvFile = MinidwServiceUtil.getCsvFromXLS(xlsFile);
					} catch (Exception e) {
						throw new CSVConversionException("Error Occured while converting xls file into csv for IL "
								+ iLInfo.getiL_name() + " <br /><b>Error Details:</b>", e);
					}
					xlsFilePath = xlsFile.getAbsolutePath();
					csvFilePath = csvFile.getAbsolutePath();
					filePath = csvFilePath.replaceAll("\\\\", "/");
				} else if (sourceFileType.equals(Constants.FileType.XLSX)) {
					File xlsxFile = new File(filePath);
					File csvFile = null;
					try {
						csvFile = MinidwServiceUtil.getCsvFromXLSX(xlsxFile);
					} catch (Exception e) {
						throw new CSVConversionException("Error Occured while converting xlsx file into csv for IL "
								+ iLInfo.getiL_name() + " <br /><b>Error Details:</b>", e);
					}
					xlsxFilePath = xlsxFile.getAbsolutePath();
					csvFilePath = csvFile.getAbsolutePath();
					filePath = csvFilePath.replaceAll("\\\\", "/");
				}

				ilContextParams.put("FILE_SRC", filePath);
				ilContextParams.put("JOB_STARTDATETIME", currentDateTime());

				String[] ilContextParamsArr = convertToContextParamsArray(ilContextParams);
				etlJobExecutionMessages = etlJobUtil.runETLjar(jobName, dependencyJARs, ilContextParamsArr);

				statusAfterJARrun = etlJobExecutionMessages.getStatus();
				String executionmessages = etlJobExecutionMessages.getErrorStreamMsg()
						+ etlJobExecutionMessages.getInputStreamMsg()
						+ "\nExit code: " + etlJobExecutionMessages.getStatus()
						+ "\nJob Command: \n" + etlJobExecutionMessages.getJavaCommandForJobExection();
				
				
				jobExecutionInfo.setStatusCode(statusAfterJARrun);
				jobExecutionInfo.setJobName(iLInfo.getiL_table_name());
				jobExecutionInfo.setJobClass(iLInfo.getJobName());
				jobExecutionInfo.setDependencyJars(iLInfo.getDependencyJars());
				jobExecutionInfo.setJobId(currentDateTime());
				jobExecutionInfo.setExecutionMessages(executionmessages);
				jobExecutionInfo.setS3Path(filePath);
				jobExecutionInfo.setModification(modification);
			}
		} catch (InterruptedException | IOException e) {
			throw new TalendJobNotFoundException("IL <b>" + iLInfo.getiL_name() + "</b> failed. ", e.getMessage(), e);
		} finally {
			if (filePath != null) {
				new File(filePath).delete();
			}
			if (jobFilesPath != null) {
				String match = user.getUserId() + "_" + packageId + ".*";
				deleteFilesFromDirWithMatching(jobFilesPath, match);
			}
			if (csvFilePath != null) {
				new File(csvFilePath).delete();
			}
			if (xlsFilePath != null) {
				new File(xlsFilePath).delete();
			}
			if (xlsxFilePath != null) {
				new File(xlsxFilePath).delete();
			}

		}
		return jobExecutionInfo;
	}
	
	
	public static JobExecutionInfo runXrefJob(Map<String, String> ilContextParams, ILInfo iLInfo,final String JOB_CLASS,final String DEPENDENCY_JARS )
			throws CSVConversionException, TalendJobNotFoundException, InterruptedException, IOException {
		
		int statusAfterJARrun = -1;
		JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
		try {
			if (StringUtils.isNotBlank(JOB_CLASS)) {
				ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();

				String[] ilContextParamsArr = convertToContextParamsArray(ilContextParams);
				etlJobExecutionMessages = etlJobUtil.runETLjar(JOB_CLASS, DEPENDENCY_JARS, ilContextParamsArr);

				statusAfterJARrun = etlJobExecutionMessages.getStatus();
				String executionmessages = etlJobExecutionMessages.getErrorStreamMsg()
						+ etlJobExecutionMessages.getInputStreamMsg()
						+ "\nExit code: " + etlJobExecutionMessages.getStatus()
						+ "\nJob Command: \n" + etlJobExecutionMessages.getJavaCommandForJobExection();
				
				
				jobExecutionInfo.setStatusCode(statusAfterJARrun);
				jobExecutionInfo.setJobId(currentDateTime());
				jobExecutionInfo.setExecutionMessages(executionmessages);
			}
		} catch (InterruptedException | IOException e) {
			throw new TalendJobNotFoundException("XREF - IL <b>" + iLInfo.getiL_name() + "</b> failed. ", e.getMessage(), e);
		}
		return jobExecutionInfo;
	}

	/**
	 * @param stagingDbHost
	 * @param stagingDbPort
	 * @param stagingClientSchema
	 * @param stagingDbUname
	 * @param stagingDbPwd
	 * @param tagetDbHost
	 * @param tagetDbPort
	 * @param tagetClientSchema
	 * @param tagetDbUname
	 * @param tagetDbPwd
	 * @param masterDbHost
	 * @param masterDbPort
	 * @param masterClientSchema
	 * @param masterDbUname
	 * @param masterDbPwd
	 * @param clientId
	 * @param userPackage
	 * @param jobFilesPath
	 * @param executionId
	 * @return
	 */
	public static Map<String, String> getDlParamsVals(String stagingDbHost, String stagingDbPort,
			String stagingClientSchema, String stagingDbUname, String stagingDbPwd, String tagetDbHost,
			String tagetDbPort, String tagetClientSchema, String tagetDbUname, String tagetDbPwd, String masterDbHost,
			String masterDbPort, String masterClientSchema, String masterDbUname, String masterDbPwd, String clientId,
			Package userPackage, String jobFilesPath, long executionId,ClientJobExecutionParameters clientJobExecutionParameters,
			boolean loadType) {

		Map<String, String> dlParamsVals = new LinkedHashMap<>();
		
		jobFilesPath = jobFilesPath + clientId+"/";

		 createDir(jobFilesPath);
		// staging database
		dlParamsVals.put("src_host", stagingDbHost);
		dlParamsVals.put("src_port", stagingDbPort);
		dlParamsVals.put("src_database", stagingClientSchema);
		dlParamsVals.put("src_user", stagingDbUname);
		dlParamsVals.put("src_pass", stagingDbPwd);

		// target database
		dlParamsVals.put("tgt_host", tagetDbHost);
		dlParamsVals.put("tgt_port", tagetDbPort);
		dlParamsVals.put("tgt_database", tagetClientSchema);
		dlParamsVals.put("tgt_user", tagetDbUname);
		dlParamsVals.put("tgt_pass", tagetDbPwd);

		// master database

		dlParamsVals.put("master_host", masterDbHost);
		dlParamsVals.put("master_port", masterDbPort);
		dlParamsVals.put("master_database", masterClientSchema);
		dlParamsVals.put("master_user", masterDbUname);
		dlParamsVals.put("master_pass", masterDbPwd);

		// file path details
		dlParamsVals.put("error_log_path", jobFilesPath);
		dlParamsVals.put("bulk_path",  jobFilesPath);

		dlParamsVals.put("start_date_time", currentDateTime());

		// client id and package id
		dlParamsVals.put("client_id", clientId);
		dlParamsVals.put("package_id", userPackage.getPackageId() + "_" + executionId);
		dlParamsVals.put("datasourcename", "N");
		
		//client jobs execution params
		dlParamsVals.put("src_time_zone", convertToGSTFormat(clientJobExecutionParameters.getSourceTimeZone()));
		dlParamsVals.put("tgt_time_zone", convertToGSTFormat(clientJobExecutionParameters.getDestTimeZone()));
		dlParamsVals.put("case_sensitive_data_set", clientJobExecutionParameters.getCaseSensitive()+"");
		dlParamsVals.put("null_replacement_string", clientJobExecutionParameters.getNullReplaceValues());
		dlParamsVals.put("load_type", loadType ?  "Incremental" : "Full");
		dlParamsVals.put("trailing_months", String.valueOf(clientJobExecutionParameters.getInterval()));


		return dlParamsVals;
	}

	/**
	 * @param dlContextParams
	 * @param dlParamsVals
	 * @param dLInfo
	 * @param clientId
	 * @param userPackage
	 * @param modification
	 * @param jobFilesPath
	 * @param user
	 * @return
	 * @throws TalendJobNotFoundException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static JobExecutionInfo runDlEtlJob(Map<String, String> dlContextParams, Map<String, String> dlParamsVals,
			DLInfo dLInfo, String clientId, Package userPackage, Modification modification, String jobFilesPath,
			User user) throws TalendJobNotFoundException, InterruptedException, IOException {
		JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
		String dlTableName = dLInfo.getdL_table_name();
		dlParamsVals.put("tgt_table", dlTableName);
		dlParamsVals.put("job_name", dLInfo.getdL_table_name());
		String jobType = "DL";
		dlParamsVals.put("job_type", jobType);
		parseContextParams(dlContextParams, dlParamsVals);
		String[] dlContextParamsArr = convertToContextParamsArray(dlContextParams);
		LOGGER.info("DL job started...id : " + dLInfo.getdL_id() + "; DL Name :" + dLInfo.getdL_name());
		int dlStatus;
		ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
		try {
			etlJobExecutionMessages = etlJobUtil.runETLjar(dLInfo.getJobName(), dLInfo.getDependencyJars(),
					dlContextParamsArr);
			dlStatus = etlJobExecutionMessages.getStatus();
			String executionmessages = etlJobExecutionMessages.getErrorStreamMsg()
					+ etlJobExecutionMessages.getInputStreamMsg()
					+ "\nExit code: " + etlJobExecutionMessages.getStatus()
					+ "\nJob Command: \n" + etlJobExecutionMessages.getJavaCommandForJobExection();
					
			jobExecutionInfo.setStatusCode(dlStatus);
			jobExecutionInfo.setJobName(dLInfo.getdL_table_name());
			jobExecutionInfo.setJobClass(dLInfo.getJobName());
			jobExecutionInfo.setDependencyJars(dLInfo.getDependencyJars());
			jobExecutionInfo.setJobId(userPackage.getPackageId() + "_" + currentDateTime());
			jobExecutionInfo.setExecutionMessages(executionmessages);
			jobExecutionInfo.setS3Path("");
			jobExecutionInfo.setModification(modification);

		} catch (InterruptedException | IOException e) {
			throw new TalendJobNotFoundException("DL <b>" + dLInfo.getdL_name() + "</b> failed. ", e.getMessage(), e);
		} finally {
			if (jobFilesPath != null) {
				String match = user.getUserId() + "_" + userPackage.getPackageId() + ".*";
				deleteFilesFromDirWithMatching(jobFilesPath, match);
			}
		}
		return jobExecutionInfo;
	}

	/**
	 * @param dlContextParams
	 * @param anly_DL
	 * @param clientId
	 * @param userPackage
	 * @param modification
	 * @param jobFilesPath
	 * @param user
	 * @return
	 * @throws TalendJobNotFoundException
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static JobExecutionInfo runAnalyticalDlEtlJob(Map<String, String> dlContextParams, DLInfo anly_DL,
			String clientId, Package userPackage, Modification modification, String jobFilesPath, User user)
			throws TalendJobNotFoundException, InterruptedException, IOException {

		JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
		String anlyDLTableName = anly_DL.getAnalytical_DL_table_name();
		dlContextParams.put("SRC", anly_DL.getdL_table_name());
		dlContextParams.put("TGT", anlyDLTableName);
		dlContextParams.put("JOB_NAME", anlyDLTableName);
		String[] anlyDlContextParamsArr = convertToContextParamsArray(dlContextParams);
		LOGGER.info("Analytical  DL job started...id : " + anly_DL.getdL_id() + "; DL Name :" + anly_DL.getdL_name());
		ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
		int analyticalDLStatus = -1;
		try {
			etlJobExecutionMessages = etlJobUtil.runETLjar(anly_DL.getAnalytical_DL_Job_Name(),
					anly_DL.getDependencyJars(), anlyDlContextParamsArr);
			analyticalDLStatus = etlJobExecutionMessages.getStatus();
			String executionmessages = etlJobExecutionMessages.getErrorStreamMsg()
					+ etlJobExecutionMessages.getInputStreamMsg();
			jobExecutionInfo.setStatusCode(analyticalDLStatus);
			jobExecutionInfo.setJobName(anly_DL.getdL_table_name());
			jobExecutionInfo.setJobClass(anly_DL.getJobName());
			jobExecutionInfo.setDependencyJars(anly_DL.getDependencyJars());
			jobExecutionInfo.setJobId(userPackage.getPackageId() + "_" + currentDateTime());
			jobExecutionInfo.setExecutionMessages(executionmessages);
			jobExecutionInfo.setS3Path("");
			jobExecutionInfo.setModification(modification);

		} catch (InterruptedException | IOException e) {
			throw new TalendJobNotFoundException(
					"Analytical DL <b>" + anly_DL.getAnalytical_DL_Job_Name() + "</b> failed. ", e.getMessage(), e);
		} finally {
			if (jobFilesPath != null) {
				String match = user.getUserId() + "_" + userPackage.getPackageId() + "_*";
				deleteFilesFromDirWithMatching(jobFilesPath, match);
			}
		}
		return jobExecutionInfo;
	}

	public static String currentDateTime() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String cTime = currentTime.format(formatter);
		return cTime;
		
	}
	
	public static String currentTime() {
		SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
		String time = localDateFormat.format(new Date());
		return time.replace(":", "");
		
	}

	/**
	 * @param filePath
	 * @param fileType
	 * @param separatorChar
	 * @param stringQuoteChar
	 * @param iLColumnNames
	 * @param selectedFileHeaders
	 * @param dafaultValues
	 * @return
	 * @throws IOException
	 */
	public static String processFileMappingWithIL(String filePath, String fileType, String separatorChar,
			String stringQuoteChar, List<String> iLColumnNames, List<String> selectedFileHeaders,
			List<String> dafaultValues) throws IOException {
		String destFilePath = null;
		String outputFileDir = CommonUtils.createDir(Constants.Temp.getTempFileDir() + "fileMappingWithIL/new/" + UUID.randomUUID() + "/");
		String outputFilePath = outputFileDir + FilenameUtils.getName(filePath);
		boolean isProcessed = Boolean.FALSE;
		switch (fileType) {
		case Constants.FileType.CSV:
			ParseCSV parseCSV = new ParseCSV(filePath);
			isProcessed = parseCSV.processCSVDataToFile(outputFilePath, iLColumnNames, selectedFileHeaders,
					dafaultValues, separatorChar, stringQuoteChar);
			if (isProcessed)
				destFilePath = outputFilePath;
			break;

		case Constants.FileType.XLS:
			ParseExcel parseExcel = new ParseExcel(filePath);
			isProcessed = parseExcel.processXLSDataToFile(outputFilePath, iLColumnNames, selectedFileHeaders,
					dafaultValues);
			if (isProcessed)
				destFilePath = outputFilePath;
			break;

		case Constants.FileType.XLSX:
			ParseExcel parseExcel1 = new ParseExcel(filePath);
			isProcessed = parseExcel1.processXLSXDataToFile(filePath, outputFilePath, iLColumnNames, selectedFileHeaders, dafaultValues);

			if (isProcessed)
				destFilePath = outputFilePath;
			break;
		}
		return destFilePath;
	}
	public static String processFileMappingWithILWs(String filePath, String fileType, String separatorChar,
			String stringQuoteChar, List<String> iLColumnNames, List<String> selectedFileHeaders,
			List<String> dafaultValues) throws IOException {
		String destFilePath = null;
		String outputFileDir = CommonUtils.createDir(Constants.Temp.getTempFileDir() + "fileMappingWithIL/new/" + UUID.randomUUID() + "/");
		String outputFilePath = outputFileDir + FilenameUtils.getName(filePath);
		boolean isProcessed = Boolean.FALSE;
		switch (fileType) {
		case Constants.FileType.CSV:
			ParseCSV parseCSV = new ParseCSV(filePath);
			isProcessed = parseCSV.processCSVDataUsingCSVReaderWriter(outputFilePath, iLColumnNames, selectedFileHeaders,
					dafaultValues, separatorChar, stringQuoteChar);
			if (isProcessed)
				destFilePath = outputFilePath;
			break;

		case Constants.FileType.XLS:
			ParseExcel parseExcel = new ParseExcel(filePath);
			isProcessed = parseExcel.processXLSDataToFile(outputFilePath, iLColumnNames, selectedFileHeaders,
					dafaultValues);
			if (isProcessed)
				destFilePath = outputFilePath;
			break;

		case Constants.FileType.XLSX:
			ParseExcel parseExcel1 = new ParseExcel(filePath);
			isProcessed = parseExcel1.processXLSXDataToFile(filePath, outputFilePath, iLColumnNames, selectedFileHeaders, dafaultValues);

			if (isProcessed)
				destFilePath = outputFilePath;
			break;
		}
		return destFilePath;
	}

	/**
	 * @param filePath
	 * @param s3BucketInfo
	 * @return
	 */
	public static String downloadFileFromS3(String filePath, S3BucketInfo s3BucketInfo,boolean isEncryptionRequired) {
		AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(),
				s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
		return amazonS3Utilities.downloadFileFromS3(filePath, isEncryptionRequired);
	}

	/**
	 * @param executionId
	 * @param status
	 * @param comments
	 * @return
	 * @throws ParseException
	 */
	public static PackageExecution getUploadStatus(long executionId, String status, String comments, String timeZone) {
		PackageExecution packageExecution = new PackageExecution();
		packageExecution.setExecutionId(executionId);
		packageExecution.setUploadStatus(status);
		packageExecution.setUploadComments(comments);
		packageExecution.setUploadStartDate(TimeZoneDateHelper.getFormattedDateString());
		packageExecution.setLastUploadedDate(TimeZoneDateHelper.getFormattedDateString());
		return packageExecution;
	}

	public static PackageExecution getDruidStatusAndComments(long executionId, String status, String comments,
			String timeZone) {
		PackageExecution packageExecution = new PackageExecution();
		packageExecution.setExecutionId(executionId);
		packageExecution.setDruidStatus(status);
		packageExecution.setDruidComments(comments);
		packageExecution.setDruidStartDate(TimeZoneDateHelper.getFormattedDateString());
		packageExecution.setDruidEndDate(TimeZoneDateHelper.getFormattedDateString());
		return packageExecution;
	}

	/**
	 * @param executionId
	 * @param status
	 * @param comments
	 * @return
	 * @throws ParseException
	 */
	public static PackageExecution getExecutionStatus(long executionId, String status, String comments,
			String timeZone) {
		PackageExecution packageExecution = new PackageExecution();
		packageExecution.setExecutionId(executionId);
		packageExecution.setExecutionStatus(status);
		packageExecution.setExecutionComments(comments);
		packageExecution.setExecutionStartDate(TimeZoneDateHelper.getFormattedDateString());
		packageExecution.setLastExecutedDate(TimeZoneDateHelper.getFormattedDateString());
		return packageExecution;
	}

	/**
	 * @param status
	 * @param comments
	 * @param mappingId
	 * @param modification
	 * @return
	 * @throws ParseException
	 */
	public static PackageExecution packageExecutionMappingInfo(String status, String comments, long mappingId,
			Modification modification, String timeZone) throws ParseException {
		PackageExecution packageExecution = new PackageExecution();
		packageExecution = new PackageExecution();
		packageExecution.setExecutionStatus(status);
		packageExecution.setExecutionComments(comments);
		packageExecution.setExecutionStartDate(TimeZoneDateHelper.getFormattedDateString());
		packageExecution.setLastExecutedDate(TimeZoneDateHelper.getFormattedDateString());
		packageExecution.setMappingId(mappingId);
		packageExecution.setModification(modification);
		return packageExecution;
	}

	/**
	 * @param timeZoneString
	 * @return
	 * @throws ParseException
	 */

	public static boolean isTableExists(String clientId, String clientSchemaName, String tableName,
			JdbcTemplate clientJdbcTemplate) {
		Boolean exists = Boolean.FALSE;
		try {
			if (StringUtils.isEmpty(tableName)) {
				return exists;
			}
			Object tablesobj = JdbcUtils.extractDatabaseMetaData(clientJdbcTemplate.getDataSource(),
					new DatabaseMetaDataCallback() {
						@Override
						public Boolean processMetaData(DatabaseMetaData dbmd)
								throws SQLException, MetaDataAccessException {
							try {
								ResultSet tableMetaData = dbmd.getTables(null, null, tableName, null);
								while (tableMetaData.next()) {
									String dbtablename = tableMetaData.getString("TABLE_NAME");
									if (StringUtils.equalsIgnoreCase(dbtablename, tableName)) {
										return true;
									}
								}
							} catch (Exception e) {
								LOGGER.error("Error while reading database meta data ", e);
							}
							return false;
						}
					});
			exists = (Boolean) tablesobj;
		} catch (Exception e) {
			LOGGER.error("Error while getting all tables : ", e);
		}
		return exists;
	}

	/**
	 * @param schemaName
	 * @param tableName
	 * @param industryId
	 * @param clientId
	 * @param clientJdbcTemplate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Column> getTableStructure(String schemaName, String tableName, int industryId, String clientId,
			JdbcTemplate clientJdbcTemplate) {
		LOGGER.info("in getTableStructure()");
		List<Column> columnList = null;
		final String table = tableName;
		try {
			columnList = (List<Column>) clientJdbcTemplate.execute(new StatementCallback<Object>() {
				@Override
				public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
					java.sql.DatabaseMetaData metaData = stmt.getConnection().getMetaData();
					ResultSet primaryKeyRs = metaData.getPrimaryKeys(null, null, table);
					ResultSet rs = metaData.getColumns(null, null, table, null);

					List<Column> primaryKeyColumnInfo = new ArrayList<>();
					while (primaryKeyRs.next()) {
						Column primaryKeycolumn = new Column();
						primaryKeycolumn.setColumnName(primaryKeyRs.getString("COLUMN_NAME"));
						primaryKeycolumn
								.setIsPrimaryKey(primaryKeyRs.getString("PK_NAME").equals("PRIMARY") ? true : false);
						primaryKeyColumnInfo.add(primaryKeycolumn);
					}
					List<Column> columnInfo = new ArrayList<>();
					while (rs.next()) {

						Column column = new Column();
						column.setSchemaName(rs.getString("TABLE_SCHEM"));
						column.setTableName(rs.getString("TABLE_NAME"));
						column.setColumnName(rs.getString("COLUMN_NAME"));
						column.setDataType(rs.getString("TYPE_NAME"));
						if (rs.getString("TYPE_NAME").contains("FLOAT") || rs.getString("TYPE_NAME").contains("DOUBLE")
								|| rs.getString("TYPE_NAME").contains("DECIMAL")
								|| rs.getString("TYPE_NAME").contains("NUMERIC")) {
							String scale = rs.getString("DECIMAL_DIGITS");
							if (scale != null) {
								column.setColumnSize(
										rs.getString("COLUMN_SIZE") + "," + rs.getString("DECIMAL_DIGITS"));
							} else {
								column.setColumnSize(rs.getString("COLUMN_SIZE"));
							}

						} else {
							column.setColumnSize(rs.getString("COLUMN_SIZE"));
						}
						column.setIsPrimaryKey(false);
						column.setIsNotNull(rs.getString("IS_NULLABLE").equals("YES") ? false : true);
						column.setDefaultValue(rs.getString("COLUMN_DEF"));
						column.setIsAutoIncrement(rs.getString("IS_AUTOINCREMENT").equals("YES") ? true : false);
						columnInfo.add(column);
					}
					for (Column pkColumn : primaryKeyColumnInfo) {
						for (Column column : columnInfo) {
							if (column.getColumnName().equals(pkColumn.getColumnName())) {
								if (pkColumn.getIsPrimaryKey()) {
									column.setIsPrimaryKey(true);
								}

							}
						}

					}

					return columnInfo;
				}
			});

		} catch (DataAccessException ae) {
			LOGGER.error("error while getTableStructure()", ae);
			throw new AnvizentRuntimeException(ae);
		}
		return columnList;
	}

	/**
	 * @param schemaName
	 * @param tableName
	 * @param clientJdbcTemplate
	 */
	public static void truncateTable(String schemaName, String tableName, JdbcTemplate clientJdbcTemplate) {
		try {
			clientJdbcTemplate.execute("truncate table `" + tableName + "` ;");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param filePath
	 * @param clientId
	 * @param deploymentType
	 * @param isMultiPartEnabled
	 * @param s3BucketInfo
	 * @return
	 * @throws AmazonS3Exception
	 * @throws OnpremFileCopyException
	 * @throws IOException 
	 */
	public static List<String> downloadFilesFromS3(String filePath, String clientId, String deploymentType,
			boolean isMultiPartEnabled, S3BucketInfo s3BucketInfo,boolean isEncryptionRequired) throws AmazonS3Exception, OnpremFileCopyException, IOException {
		List<String> filesList = null;
		if (StringUtils.isNotBlank(deploymentType)
				&& deploymentType.equalsIgnoreCase(Constants.Config.DEPLOYMENT_TYPE_ONPREM)) {
			 String directoryPath = Constants.Temp.getTempFileDir() + "/LocalFileCopyDir/";
			if(isMultiPartEnabled){
				filePath = StringUtils.removeEnd(filePath, "/");
				filesList = new ArrayList<>();
				String fileName = FilenameUtils.getBaseName(filePath);
				String destPath = directoryPath + fileName;
				File destFolder = new File(destPath); 
				FileUtils.copyDirectory(new File(filePath),destFolder, false);
				if(destFolder.isDirectory()){
					for(File file : destFolder.listFiles()){
						filesList.add(file.getAbsolutePath());
					}
				}else{
					throw new OnpremFileCopyException("destination path is not a directory.the path is : "+destFolder);
				}
			}else{
				filesList = new ArrayList<>();
				String fileName = FilenameUtils.getName(filePath);
				String fileExtention = filePath.substring(filePath.lastIndexOf("."), filePath.length());
				String formattedFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + CommonUtils.generateUniqueIdWithTimestamp();
				String logicalFileName = formattedFileName + fileExtention;
				String destPath = directoryPath+logicalFileName;
				FileUtils.copyFile(new File(filePath),new File(destPath));
				filesList.add(destPath);
			}
			
		} else {
			if (s3BucketInfo != null) {
				AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(),
						s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
				filesList = amazonS3Utilities.downloadFilesFromS3(filePath, isMultiPartEnabled,isEncryptionRequired);
			} else {
				filesList = new ArrayList<>();
				filesList.add(filePath);
			}
		}
		return filesList;
	}

	/**
	 * @param filePath
	 * @param fileType
	 * @param separatorChar
	 * @param stringQuoteChar
	 * @param clientData
	 * @param clientSchemaStaging
	 * @param clientJdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> processDataFromFileBatch(String filePath, String fileType, String separatorChar,
			String stringQuoteChar, ClientData clientData, String clientSchemaStaging, JdbcTemplate clientJdbcTemplate)
			throws Exception {

		Map<String, Object> processedData = null;

		LOGGER.debug("Entered into process data from file .. " + filePath);

		DataSource dataSource = clientJdbcTemplate.getDataSource();

		switch (fileType) {
		case Constants.FileType.CSV:
			ParseCSV parseCSV = new ParseCSV(filePath);
			processedData = parseCSV.processCSVDataBatch(clientData, dataSource, separatorChar, stringQuoteChar,
					clientSchemaStaging);
			break;

		case Constants.FileType.XLS:
			ParseExcel parseExcel = new ParseExcel(filePath);
			processedData = parseExcel.processExcelDataBatch(filePath, clientData, dataSource, clientSchemaStaging);
			break;

		case Constants.FileType.XLSX:
			ParseExcel parseExcel1 = new ParseExcel(filePath);
			processedData = parseExcel1.processXLSXDataBatch(filePath, clientData, dataSource, clientSchemaStaging);
			break;
		}
		LOGGER.debug("processing completed for file .. " + filePath);
		return processedData;
	}

	/**
	 * @param tableDerivative
	 * @param targetTableName
	 * @param clientJdbcTemplate
	 * @return
	 */
	public static Map<String, Object> processCustomTargetDerivativeTable(TableDerivative tableDerivative,
			String targetTableName, JdbcTemplate clientJdbcTemplate) {

		Map<String, Object> processedData = new HashMap<>();
		try {

			String tableName = tableDerivative.getTableName();
			String query = tableDerivative.getQuery();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
			SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = startDateFormat.format(new Date());
			String batchId = tableDerivative.getClientId() + "_" + tableDerivative.getPackageId() + "_"
					+ tableDerivative.getPackageName() + "_" + sdf.format(new Date());

			DBDataOperations dbOp = new DBDataOperations();
			DataSource dataSource = clientJdbcTemplate.getDataSource();
			dbOp.setDataSource(dataSource);

			processedData.put("totalRecords", 0);
			processedData.put("successRecords", 0);
			processedData.put("failedRecords", 0);

			boolean isProcessed = false;
			try {
				clientJdbcTemplate.execute(query, new PreparedStatementCallback<Integer>() {
					@Override
					public Integer doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						int successCount = 0;
						String failureSql = "INSERT INTO " + tableDerivative.getSchemaName()
								+ "_staging.ETL_JOB_ERROR_LOG ( DataSource_Id, BATCH_ID, ERROR_CODE, ERROR_TYPE, ERROR_MSG, ERROR_SYNTAX, ADDED_DATETIME, ADDED_USER) VALUES( ";
						try {
							Map<String, Integer> results = processCustomDerivedTargetTable(query, tableName, ps);
							successCount = results.get("insertCount");
							processedData.put("totalRecords", results.get("selectQryCount"));
							processedData.put("successRecords", results.get("insertCount"));
							processedData.put("failedRecords",
									results.get("selectQryCount") - results.get("insertCount"));
						} catch (Exception e) {
							final StringBuilder sb = new StringBuilder(failureSql);
							String errMessage = MinidwServiceUtil.getErrorMessageString(e).replaceAll("'", "''");
							sb.append("'").append("unknown").append("', ").append("'").append(batchId).append("', ")
									.append(1).append(",'").append("Data Error").append("','").append(errMessage)
									.append("','").append(errMessage).append("', now(),'Custom')");
							try {
								ps.executeUpdate(sb.toString());
							} catch (Exception er) {
								LOGGER.error("Error while inserting etl job error log :" + MinidwServiceUtil.getErrorMessageString(er));
							}
						}
						return successCount;
					}
				});

				String endTime = startDateFormat.format(new Date());
				StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
				loadSummerySql.append(tableDerivative.getSchemaName()).append("_staging").append(
						".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
				loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId).append("', '")
						.append(tableName).append("', ").append((Integer) processedData.get("totalRecords"))
						.append(", ").append((Integer) processedData.get("successRecords")).append(", ")
						.append((Integer) processedData.get("failedRecords")).append(", '").append(startTime)
						.append("', '").append(endTime).append("', 'Success', 'unknown', now(), 'Custom');");
				try {
					dbOp.getJdbcTemplate().update(loadSummerySql.toString());
				} catch (Exception er) {
					LOGGER.error("Error while inserting etl job load summary :" + MinidwServiceUtil.getErrorMessageString(er));
				}

				System.out.println("processedData>>" + processedData);
				isProcessed = true;

			} catch (Exception e) {
				LOGGER.error("Error while exeucuting query ..", e);
				isProcessed = false;
			}

			processedData.put("isProcessed", isProcessed);

		} catch (Exception e) {
			LOGGER.error("error while processing custom target tables ", e);

		}

		return processedData;
	}

	/**
	 * @param clientData
	 * @param clientStagingSchema
	 * @param clientJdbcTemplate
	 * @param tables
	 * @param query
	 * @return
	 */
	public static Map<String, Object> processCustomTargetTableQuery(ClientData clientData, String clientStagingSchema,
			JdbcTemplate clientJdbcTemplate, List<Table> tables, String query) {

		Map<String, Object> processedData = new HashMap<>();
		try {

			if (StringUtils.isNotEmpty(query)) {

				for (Table t : tables) {
					query = query.replaceAll(t.getTableName().trim(), clientStagingSchema + "." + t.getTableName());
				}

				LOGGER.debug("query : {} " + query);

				Table targetTable = clientData.getUserPackage().getTable();

				processedData.put("totalRecords", 0);
				processedData.put("successRecords", 0);
				processedData.put("failedRecords", 0);

				StringBuilder insertScript = new StringBuilder();

				insertScript.append("INSERT INTO ").append(targetTable.getTableName()).append("(");

				List<Column> columns = targetTable.getColumns();

				int i = 1, length = columns.size();

				for (Column column : columns) {
					insertScript.append("`").append(column.getColumnName()).append("`");

					if (i < length) {
						insertScript.append(", ");
					}

					i++;
				}

				insertScript.append(") \n ");

				insertScript.append(" VALUES (");

				String insertQuery = insertScript.toString();

				LOGGER.debug("insert Query : {} " + insertQuery);

				List<String> queries = new ArrayList<>();

				clientJdbcTemplate.query(query, new RowCallbackHandler() {

					@Override
					public void processRow(ResultSet rs) throws SQLException {

						if (rs != null) {
							processedData.put("totalRecords", ((Integer) processedData.get("totalRecords")) + 1);

							Object[] args = new Object[length];

							for (int i = 0; i < length; i++) {
								args[i] = rs.getString(i + 1);
							}

							try {
								int columnsLength = columns.size();
								StringBuilder query = new StringBuilder(insertQuery);
								for (int i = 0; i < columnsLength; i++) {

									String dataType = columns.get(i).getDataType();
									Object data = args[i];

									if (StringUtils.isEmpty((String) data)) {
										query.append("coalesce(null,DEFAULT(").append(columns.get(i).getColumnName())
												.append(")) ");
									} else {
										if ("text".equalsIgnoreCase(dataType) || "varchar".equalsIgnoreCase(dataType)) {
											data = ((String) data).replaceAll("'", "''");
											query.append("'").append(data).append("'");
										} else if ("datetime".equalsIgnoreCase(dataType)) {
											query.append("'").append(data).append("'");
										} else {
											if (NumberUtils.isNumber(data.toString())
													|| "bit".equalsIgnoreCase(dataType)) {
												query.append(data);
											} else {
												data = data.toString().replaceAll("'", "''");
												query.append("'").append(data).append("'");
											}
										}
									}
									if (i < columnsLength - 1) {
										query.append(", ");

									}
								}
								query.append(" )");
								queries.add(query.toString());
							} catch (Exception e) {
								LOGGER.error("Error while inserting query data", e);
							}
						}
					}
				});
				String[] querriesArr = queries.toArray(new String[0]);
				int updated = clientJdbcTemplate.execute(query, new PreparedStatementCallback<Integer>() {
					@Override
					public Integer doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss_SS");
						String batchId = clientData.getUserId() + "_" + clientData.getUserPackage().getPackageId() + "_"
								+ clientData.getUserPackage().getPackageName() + "_" + sdf.format(new Date());
						int count = 0;
						String failureSql = "INSERT INTO " + clientStagingSchema
								+ ".ETL_JOB_ERROR_LOG ( DataSource_Id, BATCH_ID, ERROR_CODE, ERROR_TYPE, ERROR_MSG, ERROR_SYNTAX, ADDED_DATETIME, ADDED_USER) VALUES( ";
						SimpleDateFormat startDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String startTime = startDateFormat.format(new Date());

						for (String sqlStmt : queries) {
							try {
								int insertedStatus = ps.executeUpdate(sqlStmt);
								if (insertedStatus == 1) {
									count++;
								}
							} catch (Exception e) {
								final StringBuilder sb = new StringBuilder(failureSql);
								String errMessage = e.getMessage().replaceAll("'", "''");
								sb.append("'").append("unknown").append("', ").append("'").append(batchId).append("', ")
										.append(1).append(",'").append("Data Error").append("','").append(errMessage)
										.append("','").append(errMessage).append("', now(),'Custom')");
								try {
									ps.executeUpdate(sb.toString());
								} catch (Exception er) {
									LOGGER.error(
											"Error while inserting etl job error log :" + getErrorMessageString(e));
								}
							}

						}
						String endTime = startDateFormat.format(new Date());
						int fail = querriesArr.length - count;
						StringBuilder loadSummerySql = new StringBuilder("INSERT INTO ");
						loadSummerySql.append(clientStagingSchema).append(
								".ETL_JOB_LOAD_SMRY (DataSource_Id,BATCH_ID, JOB_NAME, SRC_COUNT, TGT_INSERT_COUNT, ERROR_ROWS_COUNT, JOB_START_DATETIME, JOB_END_DATETIME, JOB_RUN_STATUS, JOB_LOG_FILE_LINK, ADDED_DATETIME, ADDED_USER) VALUES( ");
						loadSummerySql.append("'").append("unknown").append("', ").append("'").append(batchId)
								.append("', '").append(targetTable.getTableName()).append("', ").append(queries.size())
								.append(", ").append(count).append(", ").append(fail).append(", '").append(startTime)
								.append("', '").append(endTime).append("', 'Success', 'unknown', now(), 'Custom');");
						try {
							ps.executeUpdate(loadSummerySql.toString());
						} catch (Exception er) {
							LOGGER.error("Error while inserting etl job load summary :" + getErrorMessageString(er));
						}

						return count;
					}
				});

				processedData.put("successRecords", ((Integer) processedData.get("successRecords")) + updated);
				processedData.put("failedRecords",
						((Integer) processedData.get("failedRecords")) + (queries.size() - updated));
				System.out.println("processedData>>" + processedData);

			}

		} catch (Exception e) {
			LOGGER.error("Error while inserting query data", e);
		}

		return processedData;
	}

	/**
	 * @param selectQuery
	 * @param insertTable
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	public static Map<String, Integer> processCustomDerivedTargetTable(String selectQuery, String insertTable,
			PreparedStatement pstmt) throws SQLException {
		int selectQryCount = 0;
		int insertCount = 0;
		Map<String, Integer> selectAndInsertCountMap = new HashMap<String, Integer>();
		String selectQry = "select count(*) from ( " + selectQuery + ") as q";
		ResultSet rs = pstmt.executeQuery(selectQry);
		if (rs.next())
			selectQryCount = rs.getInt(1);
		selectAndInsertCountMap.put("selectQryCount", selectQryCount);
		if (selectQryCount > 0) {
			pstmt.executeUpdate("truncate table " + insertTable + ";");
			String insertQry = "INSERT INTO " + insertTable + " ( " + selectQuery + " ) ";
			insertCount = pstmt.executeUpdate(insertQry);
			selectAndInsertCountMap.put("insertCount", insertCount);
		} else {
			throw new SQLException(selectQryCount + " records found in source query");
		}
		if (rs != null) {
			rs.close();
		}
		return selectAndInsertCountMap;
	}

	public static String getCurrentTimeString(Date date, String timeZoneString) {
		TimeZone timeZone = (timeZoneString == null || timeZoneString.isEmpty()) ? TimeZone.getDefault()
				: TimeZone.getTimeZone(timeZoneString);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		sdf.setTimeZone(timeZone);
		return sdf.format(date);
	}

	public static String getCurrentTimeString(String timeZoneString) {
		return getCurrentTimeString(new Date(), timeZoneString);
	}

	public static String getTimeStringFoTimeZone(Date date) {
		return getCurrentTimeString(date, null);
	}

	@SuppressWarnings("unused")
	public static String getParamaterPlaceHolders(List<?> list) {
		StringBuilder params = new StringBuilder();
		for (Object val : list) {
			params.append(" ?,");
		}

		return params.deleteCharAt(params.length() - 1).toString();
	}

	public static Throwable getRootCause(Throwable cause) {
		Throwable rootCause = null;
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	public static Message getErrorMessage(Throwable cause) {
		return getErrorMessage("FAILED", cause);
	}

	public static Message getErrorMessage(String errorCode, Throwable cause) {
		Message message = new Message();
		getErrorMessage(message, errorCode, cause);
		return message;
	}

	public static void getErrorMessage(Message message, String errorCode, Throwable cause) {
		message.setCode(errorCode);
		addErrorMessage(message, cause);
	}

	public static void addErrorMessage(Message message, Throwable cause) {
		if (StringUtils.isBlank(message.getCode())) {
			message.setCode("ERROR");
		}
		
		message.setText(getErrorMessageString(cause));
	}
	public static String getErrorMessageString(Throwable cause) {
		String errorMessage = null;
		errorMessage = getRootCause(cause).getMessage();
		if (errorMessage == null) {
			errorMessage = "Unable to trace the error";
		}
		return errorMessage;
	}

	public static ILConnectionMapping getILConnectionMapping(ClientData mappingInfo) throws Exception {

		ILConnectionMapping ilConnectionMapping = mappingInfo.getIlConnectionMapping();
		getILConnectionMapping(ilConnectionMapping);
		return ilConnectionMapping;
	}

	public static String framePackageTriggerKeyName(String startDate, String endDate, String recurrencePattern,
			String cronExpression, String timeZone, String packageId,String dlID) {
		return startDate + "_" + endDate + "_" + (!recurrencePattern.equals("once") ? cronExpression : "") + "_" + timeZone
				+ "_" + packageId+ "_" + dlID;
	}

	public static String getIpAddress(SchedulerMaster master) {
		String ipAddress = null;
		if (master.getType() == 1) {
			AwsEC2Utilities awsEC2Utilities = new AwsEC2Utilities();
			InstanceInfo insatnceInfo = awsEC2Utilities.getInstanceDetails(master.getAws(), master.getInstanceId());
			ipAddress = insatnceInfo.getPublicIpAddress();
		} else {
			ipAddress = master.getIpAddress();
		}
		return ipAddress;
	}

	public static String getIpAddress(SchedulerSlave slave) {
		String ipAddress = null;
		if (slave.getType() == 1) {
			AwsEC2Utilities awsEC2Utilities = new AwsEC2Utilities();
			InstanceInfo insatnceInfo = awsEC2Utilities.getInstanceDetails(slave.getAws(), slave.getInstanceId());
			ipAddress = insatnceInfo.getPublicIpAddress();
		} else {
			ipAddress = slave.getIpAddress();
		}
		return ipAddress;
	}

	public static String getMasterStatusEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.MASTER_SCHEDULER_ID;
		return url;
	}

	public static String getSlaveStatusEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Slave.SLAVE_STATUS;
		return url;
	}

	public static String getStartSlaveEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Slave.START_SLAVE;
		return url;
	}

	public static String getStopSlaveEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Slave.STOP_SLAVE;
		return url;
	}

	public static String getStartMasterEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.START_SERVER;
		return url;
	}

	public static String getStopMasterEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.STOP_SERVER;
		return url;
	}

	public static String getResumeMasterSchedulerJob(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.MASTER_SCHEDULER_RESUME_JOB;
		return url;
	}
	
	public static String getPauseMasterSchedulerJob(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.MASTER_SCHEDULER_PAUSE_JOB;
		return url;
	}
	
	public static String getUploadQueueAndPackageExecutionEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.MASTER_UPLOAD_EXECUTION_PACKAGE_PUSHER;
		return url;
	}
	


	public static String getPackageUploadInfoAndRunnerInfoEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Slave.PACKAGE_UPLOAD_RUNNER_INFO;
		return url;
	}
	
	public static String getPackageUploadEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Slave.PACKAGE_UPLOADER;
		return url;
	}
	
	public static String getStoreJobsEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.STORE_JOBS;
		return url;
	}
	public static String getReStoreJobsEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.RESTORE_JOBS;
		return url;
	}
	
	public static String getRefreshSlavesStatus(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.REFRESH_SLAVES_STATUS;
		return url;
	}
	
	public static String getRemoveUploadQJobsEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.REMOVE_UPLOAD_Q_JOBS;
		return url;
	}

	public static String getPackageExecutionEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Slave.PACKAGE_EXECUTER;
		return url;
	}
	
	public static String getPushUploadQueueToSlavesEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.PUSH_UPLOAD_QUEUE_TO_SLAVES;
		return url;
	}

	public static String getPushExecuteQueueToSlavesEndPoint(String ipAddress) {
		String url = ipAddress + MasterAndSlaveEndPoints.Master.PUSH_EXECUTE_QUEUE_TO_SLAVES;
		return url;
	}

	public static String getSaltString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		return RandomStringUtils.random(18,true,true).toUpperCase()+sdf.format(new Date());
	}

	private static String convertToGSTFormat(String timezoneFormat) {
		// as per the request we are passing full time zone to the job
		if ( timezoneFormat != null ) {
			return timezoneFormat;
		}
		String timezoneInGST= null;
		try{
			TimeZone tz = TimeZone.getTimeZone(timezoneFormat);
			Calendar cal = GregorianCalendar.getInstance(tz);
		    int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
		    String offset = String.format("%02d:%02d", Math.abs(offsetInMillis / 3600000), Math.abs((offsetInMillis / 60000) % 60));
		    offset = (offsetInMillis >= 0 ? "+" : "-") + offset;
		    timezoneInGST= "GMT"+offset;
		}catch(Exception e){
			e.printStackTrace();
		}
		return timezoneInGST;
	}
	

	public static void storeObjectToFile(Object obj,String path) throws IOException{
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(path);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} finally {
			if ( oos != null ) oos.close();
			if ( fos != null ) fos.close();
		}
	}
	
	public static Object readObjectFromFile(String path) throws IOException, ClassNotFoundException {
		Object obj = null;
		FileInputStream is = null;
		ObjectInputStream ois = null;
		try {
			is = new FileInputStream(path);
			ois = new ObjectInputStream(is);
			obj = ois.readObject();
		} finally {
			if ( ois != null ) ois.close();
			if ( is != null ) is.close();
		}
		return obj;
	}
	
	public static List<ILConnectionMapping> getILConnectionMapping(List<ILConnectionMapping> ilConnectionMappingList)
			throws Exception {

		for (ILConnectionMapping ilConnectionMapping : ilConnectionMappingList) {
			getILConnectionMapping(ilConnectionMapping);
		}

		return ilConnectionMappingList;
	}
	
	public static ILConnectionMapping getILConnectionMapping(ILConnectionMapping ilConnectionMapping) throws Exception {

		   
		   if (!ilConnectionMapping.getIsFlatFile() && !ilConnectionMapping.getIsWebservice()) {
				ilConnectionMapping
						.setiLquery(EncryptionServiceImpl.getInstance().decrypt(ilConnectionMapping.getiLquery()));
				if (StringUtils.isNotBlank(ilConnectionMapping.getMaxDateQuery())) {
					ilConnectionMapping.setMaxDateQuery(
							EncryptionServiceImpl.getInstance().decrypt(ilConnectionMapping.getMaxDateQuery()));
				}
				ilConnectionMapping.getiLConnection().setConnectionType(EncryptionServiceImpl.getInstance()
						.decrypt(ilConnectionMapping.getiLConnection().getConnectionType()));
				ilConnectionMapping.getiLConnection().setUsername(
						EncryptionServiceImpl.getInstance().decrypt(ilConnectionMapping.getiLConnection().getUsername()));
				ilConnectionMapping.getiLConnection().setServer(
						EncryptionServiceImpl.getInstance().decrypt(ilConnectionMapping.getiLConnection().getServer()));
				ilConnectionMapping.getiLConnection().setPassword(
						EncryptionServiceImpl.getInstance().decrypt(ilConnectionMapping.getiLConnection().getPassword()));

				String procedureParameters = ilConnectionMapping.getProcedureParameters();

				if (StringUtils.isNotEmpty(procedureParameters)) {
					ilConnectionMapping
							.setProcedureParameters(EncryptionServiceImpl.getInstance().decrypt(procedureParameters));
				}

			} else if (ilConnectionMapping.getIsFlatFile() && !ilConnectionMapping.getIsWebservice()) {
				String storageType = ilConnectionMapping.getStorageType();
				if (storageType == null)
					storageType = Constants.Config.STORAGE_TYPE_S3;

				if (ilConnectionMapping.isEncryptionRequired()) {
					if (storageType.equals(Constants.Config.STORAGE_TYPE_S3)
							&& StringUtils.contains(ilConnectionMapping.getFilePath(), "/")) {
						String encryptedFileName = StringUtils.substringAfterLast(ilConnectionMapping.getFilePath(), "/");
						String originalFileName = EncryptionServiceImpl.getInstance().decrypt(encryptedFileName);
						ilConnectionMapping.setFilePath(originalFileName);
					} else if (storageType.equals(Constants.Config.STORAGE_TYPE_S3)
							&& !StringUtils.contains(ilConnectionMapping.getFilePath(), "/")) {
						String originalFileName = EncryptionServiceImpl.getInstance()
								.decrypt(ilConnectionMapping.getFilePath());
						ilConnectionMapping.setFilePath(originalFileName);
					} else if (storageType.equals(Constants.Config.STORAGE_TYPE_LOCAL)) {
						ilConnectionMapping.setFilePath(FilenameUtils.getName(ilConnectionMapping.getFilePath()));
					}
				} else {
					ilConnectionMapping.setFilePath(FilenameUtils.getName(ilConnectionMapping.getFilePath()));
				}
				
			}
		   
		return ilConnectionMapping;
	}
	


	public static Map<String, String> getHierarchicalJobContextParams(String dbHost, String dbPort,
			String dbUserName, String dbPassword, String stagingClientSchema, String tagetClientSchema,
			Hierarchical hierarchical, String clientId, String jobFilesPath) {
		
		Map<String, String> ilParamsVals = new LinkedHashMap<>();
		// staging properties
		
		jobFilesPath = jobFilesPath + clientId + "_" + hierarchical.getId()+"/";
		
		createDir(jobFilesPath);
		
		ilParamsVals.put("Hierarchy_JSON", hierarchical.getHierarchicalFormData());
		ilParamsVals.put("SRC", "context.SRC");
		ilParamsVals.put("SRC_HOST", dbHost);
		ilParamsVals.put("SRC_PORT", dbPort);
		ilParamsVals.put("SRC_DBNAME", stagingClientSchema);
		ilParamsVals.put("SRC_UN", dbUserName);
		ilParamsVals.put("SRC_PW", dbPassword);

		// main database properties
		ilParamsVals.put("TGT", "IL_Heirarchy_Value");
		ilParamsVals.put("TGT_HOST", dbHost);
		ilParamsVals.put("TGT_PORT", dbPort);
		ilParamsVals.put("TGT_DBNAME", tagetClientSchema);
		ilParamsVals.put("TGT_UN", dbUserName);
		ilParamsVals.put("TGT_PW", dbPassword);

		// master database properties
		ilParamsVals.put("MASTER_HOST", dbHost);
		ilParamsVals.put("MASTER_PORT", dbPort);
		ilParamsVals.put("MASTER_DBNAME", stagingClientSchema);
		ilParamsVals.put("MASTER_UN", dbUserName);
		ilParamsVals.put("MASTER_PW", dbPassword);
		
		ilParamsVals.put("ETL_JOBS", "ETL_Jobs");
		ilParamsVals.put("ETL_CONTROL_JOB", "ETL_CNTRL");
		ilParamsVals.put("ETL_JOB_ERROR_LOG", "ETL_JOB_ERROR_LOG");
		ilParamsVals.put("ETL_JOB_LOAD_SMRY", "ETL_JOB_LOAD_SMRY");
		
		ilParamsVals.put("DateFormat",
				"yyyy-MM-dd'T'HH:mm:ss'Z'@#dd,MM,yyyy' 'HH:mm:ss@#yyyy-MM-dd@#yyyy-MM-dd'T'HH:mm.ss'Z'@#yyyy-MM-dd'T'HH:mm:ss@#yyyy-MM-dd' 'HH:mm:ss@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy/MM/dd' 'HH:mm:ss@#MM/dd/yyyy' 'HH:mm:ss@#MM-dd-yyyy' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss'Z'@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy-MM-dd' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssXXX@#yyyy-dd-MM' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssSSS@#yyyy-dd-MM' 'HH:mm:ssSSS@#MM/dd/yyyy' 'HH:MM' AM'");

		ilParamsVals.put("CLIENT_ID", clientId);
		ilParamsVals.put("PACKAGE_ID", String.valueOf(hierarchical.getId()));
		

		ilParamsVals.put("BULK_PATH", jobFilesPath);
		ilParamsVals.put("FILE_PATH", jobFilesPath);

		ilParamsVals.put("JOB_NAME", hierarchical.getName());
		ilParamsVals.put("JOB_DESCRIPTION", hierarchical.getDescription());
		ilParamsVals.put("JOB_STARTDATETIME", currentDateTime());
		
		ilParamsVals.put("DATASOURCENAME", "HIERARCHY");
			
		String jobType = "Hierarchical";
		ilParamsVals.put("JOB_TYPE", jobType);
		
		LOGGER.info(ilParamsVals);
		return ilParamsVals;
	}

	public static JobExecutionInfo runHierarchicalJob(Map<String, String> hierarchicalContextParams,
			Hierarchical hierarchical, String JOB_CLASS, String DEPENDENCY_JARS) {
		
		int statusAfterJARrun = -1;
		JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
		try {
			if (StringUtils.isNotBlank(JOB_CLASS)) {
				ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();

				String[] ilContextParamsArr = convertToContextParamsArray(hierarchicalContextParams);
				etlJobExecutionMessages = etlJobUtil.runETLjar(JOB_CLASS, DEPENDENCY_JARS, ilContextParamsArr);

				statusAfterJARrun = etlJobExecutionMessages.getStatus();
				String executionmessages = etlJobExecutionMessages.getErrorStreamMsg()
						+ etlJobExecutionMessages.getInputStreamMsg()
						+ "\nExit code: " + etlJobExecutionMessages.getStatus()
						+ "\nJob Command: \n" + etlJobExecutionMessages.getJavaCommandForJobExection();
				
				jobExecutionInfo.setStatusCode(statusAfterJARrun);
				jobExecutionInfo.setJobId(currentDateTime());
				jobExecutionInfo.setExecutionMessages(executionmessages);
			}
		} catch (InterruptedException | IOException e) {
			throw new TalendJobNotFoundException("Hierarchical - Hierarchy <b>" + hierarchical.getName() + "</b> failed. ", e.getMessage(), e);
		}
		return jobExecutionInfo;
	}
	
    
	public static Map<String, String> getHierarchicalMappingJobContextParams(String dbHost, String dbPort,
			String dbUserName, String dbPassword, String stagingClientSchema, String tagetClientSchema,
			Hierarchical hierarchical, String clientId,  String jobFilesPath) {
		Map<String, String> ilParamsVals = new LinkedHashMap<>();
		
		jobFilesPath = jobFilesPath + clientId + "_" + hierarchical.getId()+"/";
		
		createDir(jobFilesPath);
		
		// staging properties
		ilParamsVals.put("SRC_HOST", dbHost);
		ilParamsVals.put("SRC_PORT", dbPort);
		ilParamsVals.put("SRC_DBNAME", stagingClientSchema);
		ilParamsVals.put("SRC_UN", dbUserName);
		ilParamsVals.put("SRC_PW", dbPassword);

		// main database properties
		ilParamsVals.put("TGT", hierarchical.getAssociationName());
		ilParamsVals.put("TGT_HOST", dbHost);
		ilParamsVals.put("TGT_PORT", dbPort);
		ilParamsVals.put("TGT_DBNAME", tagetClientSchema);
		ilParamsVals.put("TGT_UN", dbUserName);
		ilParamsVals.put("TGT_PW", dbPassword);

		// master database properties
		ilParamsVals.put("MASTER_HOST", dbHost);
		ilParamsVals.put("MASTER_PORT", dbPort);
		ilParamsVals.put("MASTER_DBNAME", stagingClientSchema);
		ilParamsVals.put("MASTER_UN", dbUserName);
		ilParamsVals.put("MASTER_PW", dbPassword);
		
		ilParamsVals.put("ETL_JOBS", "ETL_Jobs");
		ilParamsVals.put("ETL_CONTROL_JOB", "ETL_CNTRL");
		ilParamsVals.put("ETL_JOB_ERROR_LOG", "ETL_JOB_ERROR_LOG");
		ilParamsVals.put("ETL_JOB_LOAD_SMRY", "ETL_JOB_LOAD_SMRY");
		
		ilParamsVals.put("DateFormat",
				"yyyy-MM-dd'T'HH:mm:ss'Z'@#dd,MM,yyyy' 'HH:mm:ss@#yyyy-MM-dd@#yyyy-MM-dd'T'HH:mm.ss'Z'@#yyyy-MM-dd'T'HH:mm:ss@#yyyy-MM-dd' 'HH:mm:ss@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy/MM/dd' 'HH:mm:ss@#MM/dd/yyyy' 'HH:mm:ss@#MM-dd-yyyy' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss@#yyyy-dd-MM' 'HH:mm:ss'Z'@#yyyy-MM-dd'T'HH:mm:ssXXX@#yyyy-MM-dd' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssXXX@#yyyy-dd-MM' 'HH:mm:ssXXX@#yyyy-dd-MM'T'HH:mm:ssSSS@#yyyy-dd-MM' 'HH:mm:ssSSS@#MM/dd/yyyy' 'HH:MM' AM'");

		ilParamsVals.put("CLIENT_ID", clientId);
		ilParamsVals.put("PACKAGE_ID", String.valueOf(hierarchical.getId()));

		ilParamsVals.put("BULK_PATH", jobFilesPath);
		ilParamsVals.put("FILE_PATH", jobFilesPath);

		ilParamsVals.put("JOB_NAME", hierarchical.getName());
		ilParamsVals.put("JOB_DESCRIPTION", hierarchical.getDescription());
		ilParamsVals.put("JOB_STARTDATETIME", currentDateTime());
		
		ilParamsVals.put("DATASOURCENAME", "HIERARCHY");
		
		ilParamsVals.put("HIERARCHY_NAME", hierarchical.getName());
		ilParamsVals.put("DL_IL_NAME", hierarchical.getTableName());
		ilParamsVals.put("ATTRIBUTES", hierarchical.getMeasures());
		ilParamsVals.put("DDL_NAME", hierarchical.getAssociationName());
		ilParamsVals.put("HEIRARCHY_VALUE_TABLE", "IL_Heirarchy_Value");
		
		String jobType = "Hierarchical";
		ilParamsVals.put("JOB_TYPE", jobType);
		ilParamsVals.put("HIERARCHY_LEVEL_JSON", hierarchical.getHierarchicalLevelData());
		
		LOGGER.info(ilParamsVals);
		return ilParamsVals;
	}
	
	
	public static boolean isValidateDataResponse(DataResponse response) {
		return isValidateDataResponse(response, true);
	}

	public static boolean isValidateDataResponse(DataResponse response, boolean isThrowError) {
		if (response != null && response.getHasMessages()) {
			if (response.getMessages().get(0).getCode().equals("SUCCESS")) {
				return true;
			} else {
				if (isThrowError) {
					throw new RuntimeException(response.getMessages().get(0).getText());
				}
			}
		} else {
			new RuntimeException("Unable to get responce from server");
		}
		return false;
	}
	
	public static List<String> downloadFolderFilesFromS3(String folderkey, S3BucketInfo s3BucketInfo, boolean isMultipartEnable, boolean isEncryptionRequired) {
		AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(),
				s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
		return amazonS3Utilities.getObjectslistFromFolder(folderkey, isMultipartEnable, isEncryptionRequired);
	}
	
	/**
	 * @param originalFile
	 * @param s3BucketInfo
	 * @param s3FolderName
	 * @return
	 * @throws OnpremFileCopyException
	 * @throws AmazonS3Exception
	 */
	public static SourceFileInfo uploadFileToS3(File originalFile, S3BucketInfo s3BucketInfo, String s3FolderName)
			throws OnpremFileCopyException, AmazonS3Exception {
		SourceFileInfo sourceFileInfo = new SourceFileInfo();
		if (StringUtils.isNotBlank(s3BucketInfo.getAccessKey()) && StringUtils.isNotBlank(s3BucketInfo.getSecretKey())
				&& StringUtils.isNotBlank(s3BucketInfo.getBucketName())) {
			if (originalFile != null) {
				AmazonS3Utilities amazonS3Utilities = new AmazonS3Utilities(s3BucketInfo.getAccessKey(),
						s3BucketInfo.getSecretKey(), s3BucketInfo.getBucketName());
				// String folderName = ""; //userId + SUFFIX + packegeId +
				// SUFFIX;
				sourceFileInfo.setUploadStartTime(CommonDateHelper.formatDateAsString(new Date()));
				LOGGER.info("file upload started..." + new Date());
				String s3filePath = amazonS3Utilities.uploadFileToS3(originalFile, s3FolderName, true);
				LOGGER.info("file upload completed..." + new Date());
				sourceFileInfo.setUploadEndTime(CommonDateHelper.formatDateAsString(new Date()));
				LOGGER.info("File uploaded to S3 :" + s3filePath);
				sourceFileInfo.setFilePath(s3filePath);
			} else {
				throw new AmazonS3Exception("Invalid details for s3 upload");
			}
		} else {
			throw new AmazonS3Exception("Invalid s3 bucket details ");
		}
		return sourceFileInfo;
	}

}
