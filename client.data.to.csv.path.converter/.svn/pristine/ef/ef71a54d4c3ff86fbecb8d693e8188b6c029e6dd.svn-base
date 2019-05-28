package com.anvizent.client.data.to.csv.path.converter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anvizent.client.data.to.csv.path.converter.constants.Constants;
import com.anvizent.client.data.to.csv.path.converter.exception.QueryParcingException;
import com.anvizent.client.data.to.csv.path.converter.utilities.SQLUtil;
import com.datamodel.anvizent.service.model.ILConnectionMapping;
import com.opencsv.CSVWriter;
import com.opencsv.ResultSetHelper;
import com.opencsv.ResultSetHelperService;

/**
 * 
 * 
 */
public class ClientDBProcessor {
	private Logger logger = LoggerFactory.getLogger(ClientDBProcessor.class);

	private int sourceCount = 0;
	private int sourceFileSize = 0;

	public ClientDBProcessor() {

	}

	public ClientDBProcessor(Logger logger, Logger ilQuerylogger, String userId, Integer packageId) {
	}

	public HashMap<String, Object> processClientDBData(String csvSavePath, String incrementalUpdateDate,
			ILConnectionMapping iLConnectionMapping, int connectorId, Boolean isIncrementalUpdate, Integer limit)
			throws QueryParcingException, InstantiationException, IllegalAccessException, ClassNotFoundException,
			SQLException, IOException {
		return processClientDBData(csvSavePath, incrementalUpdateDate, iLConnectionMapping, connectorId,
				isIncrementalUpdate, limit, false, 0);
	}

	public HashMap<String, Object> processClientDBData(String csvSavePath, String incrementalUpdateDate,
			ILConnectionMapping iLConnectionMapping, int connectorId, Boolean isIncrementalUpdate, Integer limit,
			boolean isMultiPartRequired, int noOfRecordsPerFile) throws QueryParcingException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		String ilQuery = iLConnectionMapping.getiLquery();
		logger.info("in App,dataBaseCall method OriginalQuery " + ilQuery.replace("\n", " ").replace("\r", " "));

		String maxDate = null;

		if (isIncrementalUpdate) {
			if (limit == null) {
				maxDate = getMaxDate(connectorId, iLConnectionMapping, incrementalUpdateDate);
				ilQuery = SQLUtil.replaceDateValue(incrementalUpdateDate, ilQuery);

			} else {
				throw new QueryParcingException("Exception due to existance of limit for incremental Update");
			}
		}

		logger.info("in App,dataBaseCall method removeCommentsFromOriginalQuery "
				+ ilQuery.replace("\n", " ").replace("\r", " "));

		HashMap<String, Object> tempDirectoryLocationMap = saveDBDataToFile(csvSavePath, ilQuery, connectorId,
				iLConnectionMapping, limit, isMultiPartRequired, noOfRecordsPerFile);
		tempDirectoryLocationMap.put("maxDate", maxDate);

		return tempDirectoryLocationMap;
	}

	private String getMaxDate(int connectorId, ILConnectionMapping iLConnectionMapping, String incrementalUpdateDate)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException,
			QueryParcingException {
		String maxDate = null;

		String maxDateQuery = iLConnectionMapping.getMaxDateQuery();
		maxDateQuery = SQLUtil.replaceDateValue(incrementalUpdateDate, maxDateQuery);
		maxDate = getMaxDateFromDB(maxDateQuery, connectorId, iLConnectionMapping,
				iLConnectionMapping.getConnectionMappingId(), incrementalUpdateDate);
		logger.info("in App,dataBaseCall method maxDate is" + maxDate);

		return maxDate;
	}

	private HashMap<String, Object> saveDBDataToFile(String csvSavePath, String ilQuery, int connectorId,
			ILConnectionMapping iLConnectionMapping, Integer limit, boolean isMultiPartRequired, int noOfRecordsPerFile)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		JSONArray jsonDBVariablesArr = null;
		String dVariables = iLConnectionMapping.getiLConnection().getDbVariables();
		if(StringUtils.isNotBlank(dVariables)) {
			jsonDBVariablesArr = new JSONArray(dVariables);
		}
		return saveDBDataToFile(csvSavePath, ilQuery, connectorId,
				iLConnectionMapping, limit, isMultiPartRequired, noOfRecordsPerFile,0,jsonDBVariablesArr);
	}

	private HashMap<String, Object> saveDBDataToFile(String csvSavePath, String ilQuery, int connectorId,
			ILConnectionMapping iLConnectionMapping, Integer limit, boolean isMultiPartRequired, int noOfRecordsPerFile,int retryCount,JSONArray dbVariables)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		HashMap<String, Object> tempDirectoryLocationMap = null;
		String formatQuery = null;
		try {

			connection = SQLUtil.getConnectionForConnectorID(connectorId, iLConnectionMapping);
			formatQuery = com.anvizent.minidw.client.jdbc.utils.CommonUtils.getFormatedQuery(ilQuery, dbVariables);
			preparedStatement = connection.prepareStatement(formatQuery);
			try {
				preparedStatement.setFetchSize(500);
			} catch (SQLException e) {
				logger.error("error while setting the fetch size " , e);
			}
			resultSet = preparedStatement.executeQuery();
			Path tempDir = null;

			if (csvSavePath != null && !csvSavePath.isEmpty()) {
				tempDir = FileSystems.getDefault().getPath(csvSavePath);
				tempDir = Files.createTempDirectory(tempDir, "tempfiles");
			} else {
				tempDir = Files.createTempDirectory("tempfiles");
			}

			tempDirectoryLocationMap = new HashMap<String, Object>();
			tempDirectoryLocationMap.put("tempDir", tempDir);

			if (isMultiPartRequired) {
				boolean isResultSetCompleted = false;
				do {
					Path tempFile = Files.createTempFile(tempDir, "tempfiles", ".csv");
					isResultSetCompleted = writeCSVFile(ilQuery, resultSet, tempFile.toFile().getAbsolutePath(),
							connectorId, noOfRecordsPerFile);
					sourceFileSize += tempFile.toFile().length();
				} while (!isResultSetCompleted);

			} else {
				Path tempFile = Files.createTempFile(tempDir, "datafiles", ".csv");
				writeCSVFile(ilQuery, resultSet, tempFile.toFile().getAbsolutePath(), connectorId, limit);
				tempDirectoryLocationMap.put("tempFile", tempFile);
				sourceFileSize += tempFile.toFile().length();
			}
			tempDirectoryLocationMap.put("sourceCount", sourceCount);
			tempDirectoryLocationMap.put("sourceFileSize", sourceFileSize);

		} catch (Throwable e) {
			retryCount++;
			if ( e instanceof SQLException && retryCount >=3 ) {
				return saveDBDataToFile(csvSavePath, ilQuery, connectorId,
						iLConnectionMapping, limit, isMultiPartRequired, noOfRecordsPerFile,retryCount,dbVariables);
			}
			String sqlMessage = "Source Query execution failed \n" + ilQuery + "\n" + e;
			throw new SQLException(sqlMessage, e);
		} finally {
			closeDataBaseResources(connection, preparedStatement, resultSet);
		}

		return tempDirectoryLocationMap;
	}

	private boolean writeCSVFile(String query, ResultSet resultSet, String filePath, int connectorId, Integer limit)
			throws SQLException, IOException {
		logger.info("file path : " + filePath);

		boolean isResultSetCompleted = true;
		ResultSetHelper resultService = new ResultSetHelperService();

		FileWriterWithEncoding fileWriter = null;
		CSVWriter csvWriter = null;
		try {
			fileWriter = new FileWriterWithEncoding(filePath, Constants.Config.CSV_ENCODING_TYPE);

			csvWriter = new CSVWriter(fileWriter, ',');
			csvWriter.writeNext(SQLUtil.getColumnLabelNames(resultSet));
			if (limit != null) {
				int count = 0;
				try {
					while (resultSet.next()) {
						sourceCount++;
						String[] datRow = resultService.getColumnValues(resultSet, false,
								Constants.Config.DEFAULT_DATE_FORMAT, Constants.Config.DEFAULT_TIMESTAMP_FORMAT);
						csvWriter.writeNext(datRow, false);

						count++;
						if (count >= limit) {
							isResultSetCompleted = false;
							break;
						}
					}
				} catch (Throwable e) {
					String sqlMessage = query + " at row number " + (sourceCount + 1) + "\n" + e;
					throw new SQLException(sqlMessage, e);
				}
			} else {
				try {
					while (resultSet.next()) {
						sourceCount++;
						String[] datRow = resultService.getColumnValues(resultSet, false,
								Constants.Config.DEFAULT_DATE_FORMAT, Constants.Config.DEFAULT_TIMESTAMP_FORMAT);
						csvWriter.writeNext(datRow, false);
					}
				} catch (Throwable e) {
					String sqlMessage = query + " at row number " + (sourceCount + 1) + "\n" + e;
					throw new SQLException(sqlMessage, e);
				}
			}
		} finally {
			try {
				if (csvWriter != null) {
					csvWriter.close();
				}
			} catch (IOException e) {
				logger.info("Unable to close csv file '" + filePath + "' ");
			}
		}
		return isResultSetCompleted;
	}

	private String getMaxDateFromDB(String maxDateQuery, int connectorId,
			ILConnectionMapping iLConnectionMappingDetails, Integer connectionMappingId, String incrementalUpdateDate)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String maxValue = null;

		logger.info("in App,dataBaseCall method maxDateQuery " + maxDateQuery);

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSetHelper resultService = new ResultSetHelperService();

		try {
			connection = SQLUtil.getConnectionForConnectorID(connectorId, iLConnectionMappingDetails);
			preparedStatement = connection.prepareStatement(maxDateQuery);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				String[] datRow = resultService.getColumnValues(resultSet, false, Constants.Config.DEFAULT_DATE_FORMAT,
						Constants.Config.DEFAULT_TIMESTAMP_FORMAT);
				maxValue = datRow[0];
			}

			logger.info("in  App getdataBaseExecutionForMAXDate method,maxValue from resultSet is " + maxValue);
		} catch (Throwable e) {
			String sqlMessage = "\n max date query execution failed \n" + maxDateQuery + "\n" + e;
			throw new SQLException(sqlMessage, e);
		} finally {
			closeDataBaseResources(connection, preparedStatement, resultSet);
		}

		return maxValue;
	}

	public void closeDataBaseResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet)
			throws SQLException {
		if (resultSet != null) {
			resultSet.close();
		}
		if (preparedStatement != null) {
			preparedStatement.close();
		}
		if (connection != null) {
			connection.close();
		}
	}
	

	public int getSourceCount() {
		return sourceCount;
	}

	public void setSourceCount(int sourceCount) {
		this.sourceCount = sourceCount;
	}

	public int getSourceFileSize() {
		return sourceFileSize;
	}

	public void setSourceFileSize(int sourceFileSize) {
		this.sourceFileSize = sourceFileSize;
	}
	
	public void reset() {
		this.setSourceCount(0);
		this.setSourceFileSize(0);
	}
	
}
