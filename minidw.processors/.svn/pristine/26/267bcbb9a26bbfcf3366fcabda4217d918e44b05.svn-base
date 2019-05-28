
package com.anvizent.minidw.service.utils.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.anvizent.minidw.service.utils.helper.ParseExcel;
import com.anvizent.minidw.service.utils.helper.StreamGobbler;
import com.anvizent.minidw.service.utils.io.FileNameContainsFilter;
import com.anvizent.minidw.service.utils.model.ETLjobExecutionMessages;
import com.datamodel.anvizent.common.exception.CSVConversionException;
import com.datamodel.anvizent.common.exception.TalendJobNotFoundException;
import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.service.model.DLInfo;
import com.datamodel.anvizent.service.model.ILInfo;
import com.datamodel.anvizent.service.model.JobExecutionInfo;
import com.datamodel.anvizent.service.model.Modification;
import com.datamodel.anvizent.service.model.Package;
import com.datamodel.anvizent.service.model.User;
import com.monitorjbl.xlsx.StreamingReader;
import com.opencsv.CSVWriter;

@Component
public class ETLJobProcessor {
	
	protected static final Log log = LogFactory.getLog(ETLJobProcessor.class);
	
	@Autowired
	CommonProcessor commonProcessor;
	
	public JobExecutionInfo runILEtlJob(Map<String, String> ilContextParams, ILInfo iLInfo, String clientId,
			int packageId,String filePath, Modification modification, String jobFilesPath, User user)
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
						csvFile = getCsvFromXLS(xlsFile);
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
						csvFile = getCsvFromXLSX(xlsxFile);
					} catch (Exception e) {
						throw new CSVConversionException("Error Occured while converting xlsx file into csv for IL "
								+ iLInfo.getiL_name() + " <br /><b>Error Details:</b>", e);
					}
					xlsxFilePath = xlsxFile.getAbsolutePath();
					csvFilePath = csvFile.getAbsolutePath();
					filePath = csvFilePath.replaceAll("\\\\", "/");
				}

				ilContextParams.put("FILE_SRC", filePath);
				ilContextParams.put("JOB_STARTDATETIME", commonProcessor.currentDateTime());

				String[] ilContextParamsArr = convertToContextParamsArray(ilContextParams);
				etlJobExecutionMessages = runETLjar(jobName, dependencyJARs, ilContextParamsArr);
				
				commonProcessor.moveErrorLogFile(ilContextParams.get("CLIENT_ID"), ilContextParams.get("PACKAGE_ID"),
						ilContextParams.get("JOB_NAME"), ilContextParams.get("JOB_STARTDATETIME"),
						ilContextParams.get("ETL_JOB_ERROR_LOG"), ilContextParams.get("FILE_PATH"));

				statusAfterJARrun = etlJobExecutionMessages.getStatus();
				String executionmessages = etlJobExecutionMessages.getErrorStreamMsg()
						+ etlJobExecutionMessages.getInputStreamMsg()
						+ "\nExit code: " + etlJobExecutionMessages.getStatus()
						+ "\nJob Command: \n" + etlJobExecutionMessages.getJavaCommandForJobExection();
				
				jobExecutionInfo.setStatusCode(statusAfterJARrun);
				jobExecutionInfo.setJobName(iLInfo.getiL_table_name());
				jobExecutionInfo.setJobClass(iLInfo.getJobName());
				jobExecutionInfo.setDependencyJars(iLInfo.getDependencyJars());
				jobExecutionInfo.setJobId(commonProcessor.currentDateTime());
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
				String clientMatch = ilContextParams.get("CLIENT_ID");
				String packageMatch = ilContextParams.get("PACKAGE_ID");
				String match = clientMatch + "_" +packageMatch + "_.*";
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
	public JobExecutionInfo runILEtlJobForJson(Map<String, String> ilContextParams, ILInfo iLInfo, String clientId,
			int packageId,String filePathDirectory,String fileType,  Modification modification, String jobFilesPath, User user)
			throws CSVConversionException, TalendJobNotFoundException, InterruptedException, IOException {
		String jobClass = iLInfo.getJobName();
		String dependencyJARs = iLInfo.getDependencyJars();
		String jobName = iLInfo.getJobName();
		int statusAfterJARrun = -1;
		JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
		try {
			if (StringUtils.isNotBlank(jobClass)) {
				ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
				filePathDirectory = (filePathDirectory.indexOf('\\') != -1) ? filePathDirectory.replaceAll("\\\\", "/") : filePathDirectory;
				ilContextParams.put("FILE_SRC", filePathDirectory);
				ilContextParams.put("FILE_TYPE", fileType);
				ilContextParams.put("JOB_STARTDATETIME", commonProcessor.currentDateTime());

				String[] ilContextParamsArr = convertToContextParamsArray(ilContextParams);
				 
				etlJobExecutionMessages = runETLjar(jobName, dependencyJARs, ilContextParamsArr);
				
				commonProcessor.moveErrorLogFile(ilContextParams.get("CLIENT_ID"), ilContextParams.get("PACKAGE_ID"),
						ilContextParams.get("JOB_NAME"), ilContextParams.get("JOB_STARTDATETIME"),
						ilContextParams.get("ETL_JOB_ERROR_LOG"), ilContextParams.get("FILE_PATH"));

				statusAfterJARrun = etlJobExecutionMessages.getStatus();
				String executionmessages = etlJobExecutionMessages.getErrorStreamMsg()
						+ etlJobExecutionMessages.getInputStreamMsg()
						+ "\nExit code: " + etlJobExecutionMessages.getStatus()
						+ "\nJob Command: \n" + etlJobExecutionMessages.getJavaCommandForJobExection();
				
				jobExecutionInfo.setStatusCode(statusAfterJARrun);
				jobExecutionInfo.setJobName(iLInfo.getiL_table_name());
				jobExecutionInfo.setJobClass(iLInfo.getJobName());
				jobExecutionInfo.setDependencyJars(iLInfo.getDependencyJars());
				jobExecutionInfo.setJobId(commonProcessor.currentDateTime());
				jobExecutionInfo.setExecutionMessages(executionmessages);
				jobExecutionInfo.setS3Path(filePathDirectory);
				jobExecutionInfo.setModification(modification);
			}
		} catch (InterruptedException | IOException e) {
			throw new TalendJobNotFoundException("IL <b>" + iLInfo.getiL_name() + "</b> failed. ", e.getMessage(), e);
		} finally {
			String basePath = FilenameUtils.getFullPathNoEndSeparator(filePathDirectory);
			FileUtils.forceDelete(new File(basePath));
			if (jobFilesPath != null) {
				String clientMatch = ilContextParams.get("CLIENT_ID");
				String packageMatch = ilContextParams.get("PACKAGE_ID");
				String match = clientMatch + "_" +packageMatch + "_.*";
				deleteFilesFromDirWithMatching(jobFilesPath, match);
			}
		}
		return jobExecutionInfo;
	}
	public void deleteFilesFromDirWithMatching(String dir, final String match) {

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
	
	public String[] convertToContextParamsArray(Map<String, String> params) {
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
	

	public ETLjobExecutionMessages runETLjar(String jobFileName, String dependencyJARs, Map<String, String> ilContextParamsArr)
			throws InterruptedException, IOException {
		return runETLjar(jobFileName, dependencyJARs, convertToContextParamsArray(ilContextParamsArr));
	}
	
	public ETLjobExecutionMessages runETLjar(String jobFileName, String dependencyJARs, String[] ilContextParamsArr)
			throws InterruptedException, IOException {

		String[] commandforExecution = getETLExecCommand(dependencyJARs, jobFileName, ilContextParamsArr);
		Process proc;
		int exitStatus = 0;
		try {
			proc = Runtime.getRuntime().exec(commandforExecution);
		} catch (NullPointerException e) {
			throw new InterruptedException("Job name found null");
		}
		StreamGobbler errorStreamGobbler = new StreamGobbler(proc.getErrorStream());
		StreamGobbler inputStreamGobbler = new StreamGobbler(proc.getInputStream());
		inputStreamGobbler.start();
		errorStreamGobbler.start();
		
		exitStatus = proc.waitFor();
		
		String inputStreamMsg = inputStreamGobbler.getOutput();
		String errorStreamMsg = errorStreamGobbler.getOutput();

		if (StringUtils.isNotBlank(errorStreamMsg) && errorStreamMsg.contains("Could not find or load main class local_project")) {
			throw new InterruptedException(errorStreamMsg);
		}
		ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
		etlJobExecutionMessages.setInputStreamMsg(inputStreamMsg);
		etlJobExecutionMessages.setErrorStreamMsg(errorStreamMsg);
		etlJobExecutionMessages.setStatus(exitStatus);

		return etlJobExecutionMessages;
	}
	
	public String[] getETLExecCommand(String dependentJars, String jobMainClass, String[] contextparmsArray) {
		String commonEtlJobsFolder = (Constants.Config.COMMON_ETL_JOBS).replaceAll("\\\\", "/");; 
		String etlJobsFolder = (Constants.Config.ETL_JOBS).replaceAll("\\\\", "/");
		String systemPathSeperator = System.getProperty(Constants.Config.SYSTEM_PATH_SEPARATOR);
		List<String> list = new ArrayList<>();
		list.add("java");
		if (StringUtils.isNotBlank(Constants.Config.JAVA_OPT_PARAMS)) {
			list.addAll(Arrays.asList(StringUtils.split(Constants.Config.JAVA_OPT_PARAMS, " ")));
		}
		list.add("-cp");

		String commonlib = commonETLLib(commonEtlJobsFolder, systemPathSeperator);
		StringBuilder sb = new StringBuilder();
		sb.append(commonlib);

		String ildependentjars = dependentJars;
		String[] il_jars_array = ildependentjars.split(",");

		for (int i = 0; i < il_jars_array.length; i++) {
			String il_jar = il_jars_array[i];
			sb.append(etlJobsFolder + "/" + il_jar);
			if (i < il_jars_array.length - 1) {
				sb.append(systemPathSeperator);
			}
		}
		String jars = sb.toString();
		list.add(jars);
		list.add(jobMainClass);

		for (int i = 0; i < contextparmsArray.length; i++) {
			list.add(contextparmsArray[i]);
		}
		String[] command = new String[list.size()];
		command = list.toArray(command);
		/*System.out.println("Etl Jobs command --> ");
		for ( String commondLine : list ) {
			System.out.println(commondLine);
		}*/
		return command;
	}
	
	public String commonETLLib(String dir, String systemPathSeperator) {
		File[] files = getFiles(dir, Constants.FileType.JAR);
		StringBuilder sb = new StringBuilder();

		for (File file : files) {
			sb.append(file.getAbsolutePath());
			sb.append(systemPathSeperator);
		}
		return sb.toString();
	}
	
	public File[] getFiles(String dir, final String fileExtn) {
		File[] files = null;
		if (StringUtils.isNotBlank(dir)) {
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
				System.out.println("dir is created.." + dir);
			} else {
				files = fileDir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File fileDir, String name) {
						// TODO Auto-generated method stub
						return name.endsWith(fileExtn);
					}
				});
			}
		}
		return files;
	}
	
	@SuppressWarnings("rawtypes")
	public  File getCsvFromXLS(File excelFile) throws Exception {
		System.out.println("writing xls into csv started..." + formatDateAsString(new Date()));
		File tempFile = excelFile;
		String excelFilePath = tempFile.getAbsolutePath();
		String fileName = StringUtils.substring(tempFile.getName(), 0,
				StringUtils.ordinalIndexOf(tempFile.getName(), ".", 1));
		System.out.println("excelFilePath " + excelFilePath);
		InputStream excelFileToRead = null;

		File newCsvFileName = null;
		HSSFWorkbook wb = null;
		
		FileWriterWithEncoding fileWriter = null;
		CSVWriter csvWriter = null;

		try {
			excelFileToRead = new FileInputStream(excelFilePath);

			wb = new HSSFWorkbook(excelFileToRead);
			// int nummberOfSheets = wb.getNumberOfSheets();
			for (int j = 0; j < 1; j++) {
				Sheet sheet = wb.getSheetAt(0);
				String sheetName = sheet.getSheetName();
				String dirPath = createDir(getTempDir() + File.separator + Constants.Config.CSV_FROM_EXCEL);
				String timestamp = formatDateAsTimeStamp(new Date());
				newCsvFileName = new File(dirPath + "/" + fileName + "_" + sheetName + "_" + timestamp + ".csv");
				fileWriter = new FileWriterWithEncoding(newCsvFileName, com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.CSV_ENCODING_TYPE);
				csvWriter = new CSVWriter(fileWriter, ',');
				Iterator rows = sheet.rowIterator();
				ParseExcel parseExcel = new ParseExcel(excelFilePath);
				List<String> columns = parseExcel.getHeadersFromFile(excelFilePath);
				int colslen = columns.size();
				
				csvWriter.writeNext(columns.stream().toArray(String[]::new));
				
				while (rows.hasNext()) {
					Row row = (Row) rows.next();
					int getRowNumb = row.getRowNum();
					if (getRowNumb == 0) {
						continue;
					}
					
					String value = null;
					String[] rowArray = new String[colslen];
					for (int i = 0; i < colslen; i++) {
						Cell cell = row.getCell(i);
						if (cell != null) {
							Cell formattedCell = formatExcelCellData(cell);
							value = String.valueOf(formattedCell);
						} else {
							value = "";
						}
						rowArray[i] = value;
					}
					csvWriter.writeNext(rowArray, false);
				}
			}
		} finally {
			try {
				if (excelFileToRead != null) {
					excelFileToRead.close();
				}
				if (wb != null) {
					wb.close();
				}
				if (csvWriter != null) {
					csvWriter.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("writing xls into csv completed..." + formatDateAsString(new Date()));
		return newCsvFileName;
	}

	/**
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public  Cell formatExcelCellData(Cell cell) {
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
					String formattedCellValue = formatDateAsString(date);
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
	
	public  String sanitizeForCsv(String cellData) {

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
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public  File getCsvFromXLSX(File excelFile) throws Exception {
		System.out.println("writing xlsx into csv started..." + formatDateAsString(new Date()));
		File tempFile = excelFile;
		String excelFilePath = tempFile.getAbsolutePath();
		String fileName = StringUtils.substring(tempFile.getName(), 0,
				StringUtils.ordinalIndexOf(tempFile.getName(), ".", 1));
		System.out.println("excelFilePath " + excelFilePath);
		InputStream excelFileToRead = null;

		File newCsvFileName = null;
		Workbook wb = null;
		
		FileWriterWithEncoding fileWriter = null;
		CSVWriter csvWriter = null;

		try {
			excelFileToRead = new FileInputStream(excelFilePath);
			wb = StreamingReader.builder().rowCacheSize(1000).bufferSize(4096).open(excelFile);
			// int nummberOfSheets = wb.getNumberOfSheets(); //keep this in
			// below for loop instead of '1' to iterate though multiple sheets.
			for (int j = 0; j < 1; j++) {
				Sheet sheet = wb.getSheetAt(0);
				String sheetName = sheet.getSheetName();
				String dirPath = createDir(getTempDir() + File.separator + Constants.Config.CSV_FROM_EXCEL);
				String timestamp = formatDateAsTimeStamp(new Date());
				newCsvFileName = new File(dirPath + "/" + fileName + "_" + sheetName + "_" + timestamp + ".csv");
				fileWriter = new FileWriterWithEncoding(newCsvFileName, com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.CSV_ENCODING_TYPE);
				csvWriter = new CSVWriter(fileWriter, ',');

				Iterator rows = sheet.rowIterator();
				ParseExcel parseExcel = new ParseExcel(excelFilePath);
				List<String> columns = parseExcel.getHeadersFromXLSXFile(excelFilePath);
				int colslen = columns.size();

				csvWriter.writeNext(columns.stream().toArray(String[]::new));

				while (rows.hasNext()) {
					Row row = (Row) rows.next();
					int getRowNumb = row.getRowNum();
					if (getRowNumb == 0) {
						continue; // just skip the rows if row number is 0
					}
					String value = null;
					String[] rowArray = new String[colslen];
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
						rowArray[i] = value;
					}
					csvWriter.writeNext(rowArray, false);
				}
			}
		} finally {
			try {
				if (excelFileToRead != null) {
					excelFileToRead.close();
				}
				if (wb != null) {
					wb.close();
				}
				if(csvWriter != null) {
					csvWriter.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("writing xlsx into csv completed..." + formatDateAsString(new Date()));
		return newCsvFileName;
	}
	
	public  String createDir(String dirName) {

		if (StringUtils.isNotBlank(dirName)) {
			if (!new File(dirName).exists()) {
				new File(dirName).mkdirs();
				System.out.println("dir created:" + dirName);
			}
		}
		return dirName;
	}
	
	 public String formatDateAsTimeStamp(Date date) {
		final String TIMESTAMP = "yyyyMMddHHmmss";
		if (date == null)
			throw new IllegalArgumentException("Date is required.");
		return new SimpleDateFormat(TIMESTAMP).format(date);
	}
	 
	  public String formatDateAsString(Date date) {
			return formatDateAsString(date, null);
		}

		
	 public String formatDateAsString(Date date, String timeZoneString) {
		if (date == null) {
			throw new IllegalArgumentException("Date is required.");
		}

		TimeZone timeZone = (timeZoneString == null || timeZoneString.isEmpty()) ? TimeZone.getDefault() : TimeZone.getTimeZone(timeZoneString);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		simpleDateFormat.setTimeZone(timeZone);

		return simpleDateFormat.format(date);
	}
	 
	 public String getTempDir() {
			String tempPath = null;

			tempPath = System.getProperty("java.io.tmpdir");

			if (tempPath.contains("\\")) {
				tempPath = tempPath.replace('\\', '/');
			}

			if (!tempPath.endsWith("/")) {
				tempPath = tempPath + "/";
			}

			return tempPath;
		}
	 
	 

		public JobExecutionInfo runDlEtlJob(Map<String, String> dlContextParams, Map<String, String> dlParamsVals,
				DLInfo dLInfo, String clientId, Package userPackage, Modification modification, String jobFilesPath,
				User user) throws TalendJobNotFoundException, InterruptedException, IOException {
			JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
			String[] dlContextParamsArr = convertToContextParamsArray1(dlContextParams);
			log.info("DL job started...id : " + dLInfo.getdL_id() + "; DL Name :" + dLInfo.getdL_name());
			int dlStatus;
			ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
			try {
				etlJobExecutionMessages = runETLjar(dLInfo.getJobName(), dLInfo.getDependencyJars(),
						dlContextParamsArr);
				dlStatus = etlJobExecutionMessages.getStatus();
				String executionmessages = etlJobExecutionMessages.getErrorStreamMsg()
						+ etlJobExecutionMessages.getInputStreamMsg()
						+ "\nExit code: " + etlJobExecutionMessages.getStatus()
						+ "\nJob Command: \n" + etlJobExecutionMessages.getJavaCommandForJobExection();
					
				
			commonProcessor.moveErrorLogFile(dlContextParams.get("CLIENT_ID"), dlContextParams.get("PACKAGE_ID"),
					dlContextParams.get("JOB_NAME"), dlContextParams.get("JOB_STARTDATETIME"),
					dlContextParams.get("ETL_JOB_ERROR_LOG"), dlContextParams.get("FILE_PATH"));

				jobExecutionInfo.setStatusCode(dlStatus);
				jobExecutionInfo.setJobName(dLInfo.getdL_table_name());
				jobExecutionInfo.setJobClass(dLInfo.getJobName());
				jobExecutionInfo.setDependencyJars(dLInfo.getDependencyJars());
				jobExecutionInfo.setJobId(userPackage.getPackageId() + "_" + commonProcessor.currentDateTime());
				jobExecutionInfo.setExecutionMessages(executionmessages);
				jobExecutionInfo.setS3Path("");
				jobExecutionInfo.setModification(modification);

			} catch (InterruptedException | IOException e) {
				throw new TalendJobNotFoundException("DL <b>" + dLInfo.getdL_name() + "</b> failed. ", e.getMessage(), e);
			} finally {
				if (jobFilesPath != null) {
					String clientMatch = dlContextParams.get("CLIENT_ID");
					String packageMatch = dlContextParams.get("PACKAGE_ID");
					String match = clientMatch + "_" +packageMatch + "_.*";
					deleteFilesFromDirWithMatching(jobFilesPath, match);
				}
			}
			return jobExecutionInfo;
		}
		
		
		
		
		
		public String[] convertToContextParamsArray1(Map<String, String> params) {
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
		
		public JobExecutionInfo runAnalyticalDlEtlJob(Map<String, String> dlContextParams, DLInfo anly_DL,
				String clientId, Package userPackage, Modification modification, String jobFilesPath, User user)
				throws TalendJobNotFoundException, InterruptedException, IOException {

			JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
			String anlyDLTableName = anly_DL.getAnalytical_DL_table_name();
			dlContextParams.put("SRC", anly_DL.getdL_table_name());
			dlContextParams.put("TGT", anlyDLTableName);
			dlContextParams.put("JOB_NAME", anlyDLTableName);
			String[] anlyDlContextParamsArr = convertToContextParamsArray(dlContextParams);
			log.info("Analytical  DL job started...id : " + anly_DL.getdL_id() + "; DL Name :" + anly_DL.getdL_name());
			ETLjobExecutionMessages etlJobExecutionMessages = new ETLjobExecutionMessages();
			int analyticalDLStatus = -1;
			try {
				etlJobExecutionMessages = runETLjar(anly_DL.getAnalytical_DL_Job_Name(),
						anly_DL.getDependencyJars(), anlyDlContextParamsArr);
				analyticalDLStatus = etlJobExecutionMessages.getStatus();
				String executionmessages = etlJobExecutionMessages.getErrorStreamMsg()
						+ etlJobExecutionMessages.getInputStreamMsg();
				jobExecutionInfo.setStatusCode(analyticalDLStatus);
				jobExecutionInfo.setJobName(anly_DL.getdL_table_name());
				jobExecutionInfo.setJobClass(anly_DL.getJobName());
				jobExecutionInfo.setDependencyJars(anly_DL.getDependencyJars());
				jobExecutionInfo.setJobId(userPackage.getPackageId() + "_" + commonProcessor.currentDateTime());
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
		
		public static void main(String... args) {
			File file = null;
			ETLJobProcessor etl = new ETLJobProcessor();
			try {
				file = new File("/tmp/minidw/decryptedFiles/IL_Order_Dtl_Stg_20180417_045253_143.xls");
				etl.getCsvFromXLS(file);
			}catch(Exception e) {
				
			}
		}
		

}
