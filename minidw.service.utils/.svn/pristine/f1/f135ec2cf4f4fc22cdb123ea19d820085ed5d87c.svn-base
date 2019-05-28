package com.anvizent.minidw.service.utils.helper;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datamodel.anvizent.helper.OAuthConstants;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.ClientData;
import com.datamodel.anvizent.service.model.Column;
import com.datamodel.anvizent.service.model.Table;
import com.datamodel.anvizent.service.model.WebServiceApi;
import com.opencsv.CSVWriter;

public class WebServiceCSVWriter
{
	protected static final Log LOG = LogFactory.getLog(WebServiceCSVWriter.class);

	public void writeAsCSV(List<LinkedHashMap<String, Object>> flatJson, String fileName) throws IOException
	{
		LOG.info("file writing to text file begin with count " + flatJson.size());
		CSVWriter csvWriter = null;
		try
		{
			csvWriter = getCSVWriter(flatJson, fileName, false);
			writeDataToFile(flatJson, csvWriter);
		}
		finally
		{
			close(csvWriter);
		}
	}

	@SuppressWarnings("rawtypes")
	public void writeAsCSVList(List dataList, String fileName) throws IOException
	{
		List<LinkedHashMap<String, Object>> flatJson = getFlattenData(dataList);
		writeAsCSV(flatJson, fileName);
	}

	@SuppressWarnings("rawtypes")
	public static CSVWriter getCSVWriter(List dataList, String fileName, boolean isDataAppendRequired) throws IOException
	{
		FileWriterWithEncoding fileWriter = null;
		CSVWriter csvWriter = null;
		List<LinkedHashMap<String, Object>> flatJson = getFlattenData(dataList);
		List<String> headers = collectHeaders(flatJson);
		LOG.info(" Headers list -> " + headers);

		fileWriter = new FileWriterWithEncoding(fileName, Constants.Config.ENCODING_TYPE);
		csvWriter = new CSVWriter(fileWriter, ',');
		csvWriter.writeNext(headers.toArray(new String[] {}));
		if( isDataAppendRequired )
		{
			writeDataToFile(flatJson, csvWriter, headers);
		}
		return csvWriter;
	}

	@SuppressWarnings("rawtypes")
	public static void writeDataToFile(CSVWriter writer, List dataList)
	{
		List<LinkedHashMap<String, Object>> flatJson = getFlattenData(dataList);
		writeDataToFile(flatJson, writer);
	}

	public static void writeDataToFile(List<LinkedHashMap<String, Object>> flatJson, CSVWriter writer)
	{
		List<String> headers = collectHeaders(flatJson);
		writeDataToFile(flatJson, writer, headers);
	}

	public static void writeDataToFile(List<LinkedHashMap<String, Object>> flatJson, CSVWriter writer, List<String> headers)
	{
		int sourceCount = flatJson.size();
		for (int i = 0; i < sourceCount; i++)
		{
			writer.writeNext(getDataArray(headers, flatJson.get(i)), false);
		}
	}

	@SuppressWarnings("rawtypes")
	public static List<LinkedHashMap<String, Object>> getFlattenData(List dataList)
	{
		LOG.debug("flatten Started");
		List<LinkedHashMap<String, Object>> flattenJson = new ApiJsonFlatten(dataList).getFlattenJson();
		LOG.debug("flatten Ended");
		// flattenJson = getResultsFromApiResponse(flattenJson);
		// LOG.debug("date formatting skipped ");
		return flattenJson;
	}

	public static List<LinkedHashMap<String, Object>> getResultsFromApiResponse(List<LinkedHashMap<String, Object>> flatJson)
	{

		List<LinkedHashMap<String, Object>> formattedApiResponse = new ArrayList<>();
		List<String> headers = new ArrayList<>();
		LinkedHashMap<String, Object> mainHeaders = new LinkedHashMap<String, Object>();
		for (LinkedHashMap<String, Object> h : flatJson)
		{
			h.forEach((k, v) ->
			{
				if( !headers.contains(k) )
				{
					headers.add(k);
					mainHeaders.put(k, "");
				}
			});
		}
		formattedApiResponse.add(mainHeaders);
		int headersSize = headers.size();

		for (LinkedHashMap<String, Object> data1 : flatJson)
		{
			LinkedHashMap<String, Object> currentData = new LinkedHashMap<String, Object>();
			Object currentValue = null;
			int i = 0;
			do
			{

				String mainHeader = headers.get(i);
				for (Map.Entry<String, Object> entry : data1.entrySet())
				{

					String key = entry.getKey();
					Object value = entry.getValue();
					if( value != null && value.toString().contains("/Date(") )
					{
						String date = value.toString();
						date = date.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
						Long dateInMilliseconds = Long.parseLong(date);
						SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String s = f.format(new Date(dateInMilliseconds));
						value = s;
					}
					if( mainHeader.equals(key) )
					{
						currentValue = value;
					}

				}
				i++;
				currentData.put(mainHeader, currentValue);
				currentValue = "";
			}
			while (i < headersSize);

			formattedApiResponse.add(currentData);
		}

		return formattedApiResponse;
	}

	/**
	 * Use BufferedWriter when number of write operations are more It uses
	 * internal buffer to reduce real IO operations and saves time
	 */
	public static void FwCSV(List<LinkedHashMap<String, Object>> flatJson, String fileName) throws FileNotFoundException
	{
		try
		{

			File file = new File(fileName);
			if( file.createNewFile() )
			{
				List<String> headers = collectHeaders(flatJson);
				System.out.println(new Date() + "Headers list -> " + headers);
				String output = StringUtils.join(headers.toArray(), ",") + System.getProperty("line.separator");
				Files.write(Paths.get(file.getPath()), output.getBytes(),StandardOpenOption.APPEND);
				int sourceCount = flatJson.size();
				System.out.println(new Date() + "file writing to csv file begin --> ");
				for (int i = 1; i < sourceCount; i++)
				{
					Files.write(Paths.get(file.getPath()), (getCommaSeperatedRow(headers, flatJson.get(i)) + System.getProperty("line.separator")).getBytes(), StandardOpenOption.APPEND);
				}
				System.out.println(new Date() + "file writing to csv file completed --> ");
			}

		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public static void writeAsCSVForWs(List<LinkedHashMap<String, Object>> flatJson, String fileName) throws IOException
	{
		System.out.println(new Date() + " file writing to text file begin with count " + flatJson.size());
		FileWriterWithEncoding fileWriter = null;
		com.opencsv.CSVWriter csvWriter = null;
		try
		{
			List<String> headers = collectHeaders(flatJson);
			System.out.println(new Date() + "Headers list -> " + headers);
			fileWriter = new FileWriterWithEncoding(fileName, com.anvizent.client.data.to.csv.path.converter.constants.Constants.Config.CSV_ENCODING_TYPE);
			csvWriter = new com.opencsv.CSVWriter(fileWriter, ',',CSVWriter.DEFAULT_QUOTE_CHARACTER);
			csvWriter.writeNext(headers.toArray(new String[] {}));
			int sourceCount = flatJson.size();
			System.out.println(new Date() + "file writing to csv file begin --> ");
			for (int i = 1; i < sourceCount; i++)
			{
				csvWriter.writeNext(getDataArray(headers, flatJson.get(i)), false);
			}
			System.out.println(new Date() + "file writing to csv file completed --> ");
		}
		finally
		{
			try
			{
				if( csvWriter != null )
				{
					csvWriter.close();
				}
			}
			catch ( IOException e )
			{
				System.out.println("Unable to close csv file '" + fileName + "'");
			}
			System.out.println(new Date() + " file writing to text file completed with count " + flatJson.size());
		}
	}

	@SuppressWarnings("unused")
	private static void writeToFile(String output, String fileName) throws FileNotFoundException
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new FileWriterWithEncoding(fileName, Constants.Config.ENCODING_TYPE));
			writer.write(output);
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			close(writer);
		}
	}

	public static void close(Closeable writer)
	{
		try
		{
			if( writer != null )
			{
				writer.close();
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	private static String getCommaSeperatedRow(List<String> headers, Map<String, Object> map)
	{
		List<String> items = new ArrayList<String>();
		for (String header : headers)
		{
			String value = map.get(header) == null ? "" : CommonUtils.sanitizeForCsv(map.get(header).toString());
			items.add(value);
		}
		return StringUtils.join(items.toArray(), ",");
	}

	private static String[] getDataArray(List<String> headers, Map<String, Object> map)
	{
		List<String> items = new ArrayList<String>();
		for (String header : headers)
		{
			String value = map.get(header) == null ? "" : CommonUtils.sanitizeForWsCsv(map.get(header).toString());
			items.add(value);
		}
		return items.toArray(new String[] {});
	}
 
	@SuppressWarnings("unused")
	private static List<String> getData(List<String> headers, Map<String, Object> map)
	{	
		List<String> items = new ArrayList<String>();
		for (String header : headers)
		{
			String value = map.get(header) == null ? "" : CommonUtils.sanitizeForCsv(map.get(header).toString());
			items.add(value);
		}
		return items;
	}
	public static List<String> collectHeaders(List<LinkedHashMap<String, Object>> flatJson)
	{
		List<String> headers = new ArrayList<String>();
		for (Map<String, Object> map : flatJson)
		{
			List<String> keyList = new ArrayList<String>(map.keySet());
			for (String key : keyList)
			{
				headers.add(key.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
			}
			break;
		}
		return headers;
	}

	
	public static String getFilePathForWsApi(WebServiceApi webServiceApi, String csvSavePath)
	{

		if( StringUtils.isBlank(csvSavePath) )
		{
			csvSavePath = Constants.Temp.getTempFileDir();
		}
		String fileDir = CommonUtils.createDir(csvSavePath + "fileMappingWithIL/");
		String newfilename = webServiceApi.getApiName().replaceAll("\\s+", "_") + "_" + CommonUtils.generateUniqueIdWithTimestamp();
		String filePath = fileDir + newfilename + ".csv";
		return filePath;
	}
	public static String getFilePathForWsApi(WebServiceApi webServiceApi, String csvSavePath,String fileType)
	{

		if( StringUtils.isBlank(csvSavePath) )
		{
			csvSavePath = Constants.Temp.getTempFileDir();
		}
		String fileDir = CommonUtils.createDir(csvSavePath + "fileMappingWithIL/");
		String newfilename = webServiceApi.getApiName().replaceAll("\\s+", "_") + "_" + CommonUtils.generateUniqueIdWithTimestamp();
		String filePath = fileDir + newfilename + "."+fileType;
		return filePath;
	}
	public static String getFilePathForWsApi(WebServiceApi webServiceApi, List<LinkedHashMap<String, Object>> finalformattedApiResponse, String csvSavePath)
	{

		if( StringUtils.isBlank(csvSavePath) )
		{
			csvSavePath = Constants.Temp.getTempFileDir();
		}
		String fileDir = CommonUtils.createDir(csvSavePath + "fileMappingWithIL/");
		String newfilename = webServiceApi.getApiName().replaceAll("\\s+", "_") + "_" + CommonUtils.generateUniqueIdWithTimestamp();
		String filePath = fileDir + newfilename + ".csv";

		if( finalformattedApiResponse != null && filePath != null )
		{
			try
			{
				writeAsCSVForWs(finalformattedApiResponse, filePath);
			}
			catch ( FileNotFoundException e )
			{
				e.printStackTrace();
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
		}
		return filePath;
	}
	public static Table tempTableForming(List<String> headers) {

		try {
			 
			Table table = new Table();

			StringBuilder tableName = new StringBuilder("anv_temp_");
			tableName.append(0).append("_").append(CommonUtils.generateUniqueIdWithTimestamp());

			LOG.debug("temp table name : " + tableName);

			table.setTableName(tableName.toString());

			List<Column> columns = new ArrayList<>();
			table.setColumns(columns);
			Column column = null;

			if ( headers == null) {
				throw new Exception();
			}

			for (String header : headers) {
				column = new Column();
				if( header.length() >= 65 )
				 {
					header = header.substring(0, 64);
				 }
				column.setColumnName(header);
				column.setColumnSize(null);
				column.setDataType("LONGTEXT");
				column.setDefaultValue("");
				column.setIsAutoIncrement(false);
				column.setIsNotNull(false);
				column.setIsPrimaryKey(false);
				column.setIsUnique(false);

				columns.add(column);
			}

			return table;
		} catch (Exception e) {
			LOG.error("Error while creating file object from file columns ", e);
		}

		return null;
	}
	
	public static String createTargetTable(ClientData clientData, Connection connection) throws Exception
	{
		PreparedStatement ps = null;
		String sql = null;
		try
		{
			sql = buildMySQLCreateTable(clientData);

			ps = connection.prepareStatement(sql);
			ps.executeUpdate();
			LOG.info(clientData.getUserPackage().getTable().getTableName() + " table created in client " + clientData.getUserId() + " schema");
		}
		finally
		{
			if( ps != null )
			{
				ps.close();
			}
		}
		return sql;

	}

	public static String buildMySQLCreateTable(ClientData clientData) {

		String script = null;
		StringBuilder query = new StringBuilder("CREATE TABLE");
		query.append(" `");
		query.append(clientData.getUserPackage().getTable().getTableName());
		query.append("`(\n");
		// add columns
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (StringUtils.isNotBlank(column.getColumnName())) {
				String columnName = column.getColumnName();
				columnName = columnName.trim().replaceAll("\\s+", "_").replaceAll("\\W+", "_");
				query.append("`").append(columnName).append("`");
				query.append(" ");
				query.append(column.getDataType());

				// append the column size
				if (!column.getDataType().equals("DATETIME")) {

					boolean addbraces = true;

					/*
					 * if (column.getColumnSize() == null) { addbraces = false;
					 * }
					 */
					if(!column.getDataType().equals("DATE") ){

						addbraces = (column.getColumnSize() != null);
	
						if (addbraces && column.getDataType().equals("DECIMAL")) {
							addbraces = (column.getDecimalPoints() != null);
						}
	
						if (addbraces)
							query.append("(");
	
						if (column.getDataType().equals("DECIMAL")) {
							if (column.getColumnSize() != null && column.getDecimalPoints() != null) {
								query.append(column.getColumnSize());
								query.append(",");
								query.append(column.getDecimalPoints());
							} /*
								 * else{ query.append("10,2"); }
								 */
						} else {
							if (StringUtils.isNotBlank(column.getColumnSize())) {
								query.append(column.getColumnSize());
							}
						}
	
						if (addbraces)
							query.append(")");
					}
			     }
				query.append(" ");
				if (column.getIsNotNull().equals(Boolean.TRUE)) {
					query.append("NOT NULL");
				} else {
					query.append("NULL");
				}
				if ((column.getDataType().equals("BIGINT") || column.getDataType().equals("INT"))
						&& column.getIsAutoIncrement().equals(Boolean.TRUE)) {
					query.append(" ");
					query.append("AUTO_INCREMENT");
				}
				if (StringUtils.isNotBlank(column.getDefaultValue())) {
					query.append(" DEFAULT ");

					if (column.getDataType().equals("BIGINT") || column.getDataType().equals("INT")
							|| column.getDataType().equals("DECIMAL") || column.getDataType().equals("BIT"))
						query.append(column.getDefaultValue());
					else
						query.append("'").append(column.getDefaultValue()).append("'");
				}
				query.append(",\n");
			}
		}
		query.replace(query.lastIndexOf(","), query.length(), "");
		// check for PKs
		boolean isHavingPK = Boolean.FALSE;
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (column.getIsPrimaryKey().equals(Boolean.TRUE)) {
				isHavingPK = Boolean.TRUE;
				break;
			}
		}
		if (isHavingPK) {
			query.append(",\n");
			query.append("PRIMARY KEY (");
		}
		// add PKs
		StringBuilder primaryKeyConstraint = new StringBuilder();
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (column.getIsPrimaryKey()) {
				if (column.getIsAutoIncrement()) {
					String primaryKeyConstraintStr = primaryKeyConstraint.toString();
					primaryKeyConstraint = new StringBuilder();
					primaryKeyConstraint.append("`").append(column.getColumnName()).append("`");
					primaryKeyConstraint.append(",").append(primaryKeyConstraintStr);

				} else {
					primaryKeyConstraint.append("`").append(column.getColumnName()).append("`");
					primaryKeyConstraint.append(",");
				}

			}
		}
		// remove ',' at the end of last PK
		if (isHavingPK) {
			primaryKeyConstraint.replace(primaryKeyConstraint.lastIndexOf(","), primaryKeyConstraint.length(), "");
			primaryKeyConstraint.append(")");
		}
		query.append(primaryKeyConstraint);
		// add UQs
		for (Column column : clientData.getUserPackage().getTable().getColumns()) {
			if (column.getIsUnique()) {
				query.append(",\n");
				query.append("UNIQUE INDEX ");
				query.append("`").append(column.getColumnName() + "_UNIQUE").append("`");
				query.append(" (");
				query.append("`").append(column.getColumnName()).append("`");
				query.append(" ASC)");
			}
		}
		query.append(") DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ");
		script = query.toString();
		LOG.debug("query>>>>>>\n" + script);
		return script;
	}
	
	public static int updateTokens(Long webserviceConnectionId, Connection connection, Map<String, String> tokens) throws SQLException {
		PreparedStatement ps = null;
		String sql = null;
		int result = 0;
		try
		{
			sql = "UPDATE minidwcs_ws_connections_mst SET authentication_token = ?, authentication_refresh_token= ? WHERE id = ?";
			ps = connection.prepareStatement(sql);
			ps.setString(1, tokens.get(OAuthConstants.ACCESS_TOKEN));
			ps.setString(2, tokens.get(OAuthConstants.REFRESH_TOKEN));
			ps.setInt(3, webserviceConnectionId.intValue());
			result = ps.executeUpdate();
		}
		finally
		{
			if( ps != null )
			{
				ps.close();
			}
		}
		return result;
	}
}