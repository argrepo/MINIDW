package com.anvizent.minidw.service.utils.helper;

import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.DDLayout;

public class CommonUtils {

	public static String getTempDir() {
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

	public static String createDir(String dirName) {

		if (StringUtils.isNotBlank(dirName)) {
			if (!new File(dirName).exists()) {
				new File(dirName).mkdirs();
				System.out.println("dir created:" + dirName);
			}

		}
		return dirName;
	}

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

	public static File[] getFiles(String dir, final String fileExtn) {
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
	public static String appendDQ(String str) {
		if( StringUtils.isEmpty(str) ) return "";
	    return "\"" + str + "\"";
	}
	
	public static String sanitizeForWsCsv(String cellData)
	{
	
		String data = null;
		if( StringUtils.isEmpty(cellData) ) 
		{
			return "";
		}
		else 
		{
	    data = cellData.trim();
		}
		return data;
	}

	public static String sanitizeCsvForWsTempTable(String cellData)
	{
		String data = null;
		if( StringUtils.isEmpty(cellData) ) 
		{
			return "";
		}
		else 
		{
	    data = forRegex(cellData.trim());
		}
		return data;
	}
	
	public static String forRegex(String aRegexFragment)
	{
		final StringBuilder result = new StringBuilder();

		final StringCharacterIterator iterator = new StringCharacterIterator(aRegexFragment);
		char character = iterator.current();
		while (character != CharacterIterator.DONE)
		{
			if( character == '\'' )
			{
				result.append("''");
			}
			else if( character == '\\' )
			{
				result.append("\\\\");
			}
			else
			{
				result.append(character);
			}
			character = iterator.next();
		}
		return result.toString();
	}
	
	public static String sanitizeForCsv(String cellData)
	{

		if( StringUtils.isEmpty(cellData) ) return "";
		cellData = cellData.trim();
		StringBuilder resultBuilder = new StringBuilder(cellData);
		int lastIndex = 0;
		while (resultBuilder.indexOf("\"", lastIndex) >= 0)
		{
			int quoteIndex = resultBuilder.indexOf("\"", lastIndex);
			resultBuilder.replace(quoteIndex, quoteIndex + 1, "\"\"");
			lastIndex = quoteIndex + 2;
		}

		char firstChar = cellData.charAt(0);
		char lastChar = cellData.charAt(cellData.length() - 1);

		if( cellData.contains(",") || cellData.contains(";") || cellData.contains("\r") || cellData.contains("\b") || cellData.contains("\f") || cellData.contains("\r\n") || cellData.contains("\n") || Character.isWhitespace(firstChar) || Character.isWhitespace(lastChar) )
		{
			resultBuilder.insert(0, "\"").append("\"");
		}
		return resultBuilder.toString();
	}
	
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
	
	@SuppressWarnings("deprecation")
	public static String formatExcelCellData1(Cell cell) {
		String cellValue = null;
		if (cell != null) {
			if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){
				switch (cell.getCachedFormulaResultType()) {
				 case Cell.CELL_TYPE_STRING:
					 cellValue = cell.getStringCellValue();
					if (cellValue.matches("^[0-9]*\\.?[0]$")) {
						String s2 = cellValue.replace(".0", "");
						//cell.setCellValue(s2);
						cellValue = s2;
					}
					break;
				 case Cell.CELL_TYPE_NUMERIC:
					double numericCellValue = cell.getNumericCellValue();
					String numStr = String.valueOf(numericCellValue);
					if (DateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						String formattedCellValue = CommonDateHelper.formatDateAsString(date);
						//cell.setCellValue(formattedCellValue);
						cellValue = formattedCellValue;
					} else if (numStr.matches("^[0-9]*\\.?[0]$")) {
						String s2 = numStr.replace(".0", "");
						//cell.setCellValue(s2);
						cellValue = s2;
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					break;
				default:
					break;
				}
			}else{
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
				cellValue = String.valueOf(cell);
			}
		}

		return cellValue;
	}
	
	public static int runDDlayoutTable(DDLayout ddlayout, JdbcTemplate clientJdbcTemplate) throws SQLException {
		int selectQryCount = -1;
		int insertCount = -1;
		String selectQry = "select count(*) from ( " + ddlayout.getSelectQry() + ") as q";
		selectQryCount = clientJdbcTemplate.queryForObject(selectQry, Integer.class);
		if (selectQryCount > 0) {
			clientJdbcTemplate.execute("truncate table " + ddlayout.getTableName() + ";");
			String insertQry = "INSERT INTO " + ddlayout.getTableName() + " ( " + ddlayout.getSelectQry() + " ) ";
			insertCount = clientJdbcTemplate.update(insertQry);
		} else {
			throw new SQLException(selectQryCount + " records found in source query");
		}
		return insertCount;
	}

	public static String getClientBrowserDetails(HttpServletRequest request) {
		StringBuilder clientInformation = new StringBuilder();
		if (request != null) {
			String userAgent = request.getHeader("User-Agent");
			String ipaddress = request.getRemoteAddr();
			clientInformation.append("IP Address: ").append(ipaddress).append("\t");
			clientInformation.append("Browser Details:").append(userAgent).append("");
		}
		return clientInformation.toString();
	}

	public static String getRandomPPKFileName() {
		return Constants.Temp.getTempFileDir() + File.separator + UUID.randomUUID().toString() + Constants.Extensions.PPK;
	}
	
	public static void closeInputStream(Closeable fileinput) {
		try {
			if (fileinput != null) {
				fileinput.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
