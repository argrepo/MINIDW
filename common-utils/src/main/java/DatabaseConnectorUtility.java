import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DatabaseConnectorUtility {
	private StringBuilder stringBuilder = new StringBuilder();
	private boolean logRequired = false;
	private boolean dataRequired = false;
	public boolean isDataRequired() {
		return dataRequired;
	}

	public void setDataRequired(boolean dataRequired) {
		this.dataRequired = dataRequired;
	}

	private int errorThreshouldForTermination = 1;

	public boolean isLogRequired() {
		return logRequired;
	}

	public void setLogRequired(boolean logRequired) {
		this.logRequired = logRequired;
	}

	public int getErrorThreshouldForTermination() {
		return errorThreshouldForTermination;
	}

	public void setErrorThreshouldForTermination(int errorThreshouldForTermination) {
		this.errorThreshouldForTermination = errorThreshouldForTermination;
	}

	public static void main(String[] args) {
		DatabaseConnectorUtility utility = new DatabaseConnectorUtility();
		try {
			Properties props = utility.getDbProperties();
			String driverName = props.getProperty("db.driver");
			String jdbcUrl = props.getProperty("db.url");
			String userName = props.getProperty("db.username");
			String password = props.getProperty("db.password");
			String query = props.getProperty("db.testing.query");
			try {
				utility.setLogRequired(Boolean.parseBoolean(props.getProperty("log.required", "false").trim()));
				utility.setDataRequired(Boolean.parseBoolean(props.getProperty("data.required", "true").trim()));
				utility.setErrorThreshouldForTermination(Integer.parseInt(props.getProperty("error.count.for.terminating.the.process", "1").trim()));
			} catch (Exception e) {
				System.out.println("error while reading props:" + e.getMessage());
			}
			utility.executeQuery(driverName, jdbcUrl, userName, password, query);
		} catch (Exception e) {
			System.out.println("Error occured :");
			e.printStackTrace();
		}
	}

	void executeQuery(String driverName, String jdbcUrl, String userName, String password, String query) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet resultSet = null;
		try {
			System.out.println("trying to connectdatabase");
			connection = getConnection(driverName, jdbcUrl, userName, password);
			pStatement = connection.prepareStatement(query);
			resultSet = pStatement.executeQuery();
			populateResultSet(resultSet);
		} finally {
			try {
				if (resultSet != null && !resultSet.isClosed()) {
					resultSet.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (pStatement != null && !pStatement.isClosed()) {
					pStatement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (resultSet != null && !resultSet.isClosed()) {
					resultSet.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void populateResultSet(ResultSet resultSet) throws SQLException {

		ResultSetHelperService resultSetHelperService = new ResultSetHelperService();
		List<String[]> dataSet = new ArrayList<>();
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int columnCount = resultSetMetaData.getColumnCount();
		appendData("Column count: " + columnCount);
		String[] columnNames = resultSetHelperService.getColumnNames(resultSet);
		dataSet.add(columnNames);
		appendData("Column Names " + String.join(", ", columnNames));
		// List<Map<String, Object>> data = new ArrayList<>();
		int rowCount = 0;
		int errorCount = 0;
		rtt: while (resultSet.next()) {
			rowCount++;
				try {
					String[] data = resultSetHelperService.getColumnValues(resultSet, true, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
					dataSet.add(data);
				} catch (SQLException | IOException e) {
					errorCount++;
					String expString = "Exception occured at row number " + rowCount 
							+ "; error message=" + e.getMessage();
					appendData(expString);
					e.printStackTrace();
					if (errorCount >= getErrorThreshouldForTermination()) {
						appendData("Given Threshould reached for process termination");
						break rtt;
					}  else {
						break;
					}
				}
		}
		appendData("No Of records found " + rowCount);
		if (this.isLogRequired()) {
			printData();
		}
		
		if (this.isLogRequired()) {
			writeData(dataSet);
		}
		
	}

	private void writeData(List<String[]> dataSet) {
		System.out.println("Data Writing started");
		Path tempFile = null;
		Writer writer = null;
		OutputStream stream = null;
		try {
			tempFile = Files.createTempFile("csvData", ".csv");
			stream = new FileOutputStream(tempFile.toFile(), false);
			writer = new OutputStreamWriter(stream, "UTF-8");
			for (String[] data : dataSet) {
				writer.write(String.join(",", data));
				writer.write("\n");
			}
			System.out.println("Data file created at " + tempFile.toFile().getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Data Writing completed");
		
	}

	private void printData() {
		Path tempFile = null;
		FileWriter fileWriter = null;
		try {
			tempFile = Files.createTempFile("tempfiles", ".txt");
			fileWriter = new FileWriter(tempFile.toFile());
			fileWriter.write(stringBuilder.toString());
			fileWriter.flush();
			System.out.println("Log file created at " + tempFile.toFile().getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	List<String> getColumnNames(ResultSetMetaData resultSetMetaData) throws SQLException {
		int columnCount = resultSetMetaData.getColumnCount();
		List<String> columnNames = new ArrayList<>();
		for (int i = 1; i <= columnCount; i++) {
			columnNames.add(resultSetMetaData.getColumnLabel(i));
		}
		return columnNames;
	}

	void appendData(String string) {
		stringBuilder.append(string);
		stringBuilder.append("\n");
	}

	private Connection getConnection(String driverName, String jdbcUrl, String userName, String password) throws ClassNotFoundException, SQLException {
		Class.forName(driverName);
		return DriverManager.getConnection(jdbcUrl, userName, password);
	}

	public Properties getDbProperties() throws IOException {
		final Properties properties = new Properties();
		File file = new File("db.properties");
		System.out.println(file.getAbsolutePath());
		FileReader minidwServiceConfigReader = new FileReader(file);
		properties.load(minidwServiceConfigReader);
		return properties;
	}
}
	class ResultSetHelperService {
		   public static final int CLOBBUFFERSIZE = 2048;

		   // note: we want to maintain compatibility with Java 5 VM's
		   // These types don't exist in Java 5
		   static final int NVARCHAR = -9;
		   static final int NCHAR = -15;
		   static final int LONGNVARCHAR = -16;
		   static final int NCLOB = 2011;

		   static final String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";
		   static final String DEFAULT_TIMESTAMP_FORMAT = "dd-MMM-yyyy HH:mm:ss";

		   /**
		    * Default Constructor.
		    */
		   public ResultSetHelperService() {
		   }

		   private  String read(Clob c) throws SQLException, IOException {
		      StringBuilder sb = new StringBuilder((int) c.length());
		      Reader r = c.getCharacterStream();
		      char[] cbuf = new char[CLOBBUFFERSIZE];
		      int n;
		      while ((n = r.read(cbuf, 0, cbuf.length)) != -1) {
		         sb.append(cbuf, 0, n);
		      }
		      return sb.toString();
		   }

		   /**
		    * Returns the column names from the result set.
		    * @param rs - ResultSet
		    * @return - a string array containing the column names.
		    * @throws SQLException - thrown by the result set.
		    */
		   public String[] getColumnNames(ResultSet rs) throws SQLException {
		      List<String> names = new ArrayList<>();
		      ResultSetMetaData metadata = rs.getMetaData();

		      for (int i = 0; i < metadata.getColumnCount(); i++) {
		         names.add(metadata.getColumnLabel(i + 1));
		      }

		      String[] nameArray = new String[names.size()];
		      return names.toArray(nameArray);
		   }
		   
		   public List<String> getColumnNamesWithList(ResultSet rs) throws SQLException {
			      List<String> names = new ArrayList<>();
			      ResultSetMetaData metadata = rs.getMetaData();
			      for (int i = 0; i < metadata.getColumnCount(); i++) {
			         names.add(metadata.getColumnLabel(i + 1));
			      }
			      return names;
			   }

		   /**
		    * Get all the column values from the result set.
		    * @param rs - the ResultSet containing the values.
		    * @return - String array containing all the column values.
		    * @throws SQLException - thrown by the result set.
		    * @throws IOException - thrown by the result set.
		    */
		   public String[] getColumnValues(ResultSet rs) throws SQLException, IOException {
		      return this.getColumnValues(rs, false, DEFAULT_DATE_FORMAT, DEFAULT_TIMESTAMP_FORMAT);
		   }

		   /**
		    * Get all the column values from the result set.
		    * @param rs - the ResultSet containing the values.
		    * @param trim - values should have white spaces trimmed.
		    * @return - String array containing all the column values.
		    * @throws SQLException - thrown by the result set.
		    * @throws IOException - thrown by the result set.
		    */
		   public String[] getColumnValues(ResultSet rs, boolean trim) throws SQLException, IOException {
		      return this.getColumnValues(rs, trim, DEFAULT_DATE_FORMAT, DEFAULT_TIMESTAMP_FORMAT);
		   }

		   /**
		    * Get all the column values from the result set.
		    * @param rs - the ResultSet containing the values.
		    * @param trim - values should have white spaces trimmed.
		    * @param dateFormatString - format String for dates.
		    * @param timeFormatString - format String for timestamps.
		    * @return - String array containing all the column values.
		    * @throws SQLException - thrown by the result set.
		    * @throws IOException - thrown by the result set.
		    */
		   public List<String> getColumnValuesWithArray(ResultSet rs, boolean trim, String dateFormatString, String timeFormatString) throws SQLException, IOException {
			      List<String> values = new ArrayList<>();
			      ResultSetMetaData metadata = rs.getMetaData();

			      for (int i = 0; i < metadata.getColumnCount(); i++) {
			         values.add(getColumnValue(rs, metadata.getColumnType(i + 1), i + 1, trim, dateFormatString, timeFormatString));
			      }

			      return values;
			   }
		   public String[] getColumnValues(ResultSet rs, boolean trim, String dateFormatString, String timeFormatString) throws SQLException, IOException {
		      List<String> values = new ArrayList<>();
		      ResultSetMetaData metadata = rs.getMetaData();

		      for (int i = 0; i < metadata.getColumnCount(); i++) {
		         //try {
		        	 values.add(getColumnValue(rs, metadata.getColumnType(i + 1), i + 1, trim, dateFormatString, timeFormatString));
				/*} catch (Exception e) {
					System.out.println("at column number " + (i+1) + e.getMessage());
					throw new SQLException("at column number " + (i+1) + e.getMessage(),e);
				}*/
		      }

		      String[] valueArray = new String[values.size()];
		      return values.toArray(valueArray);
		   }

		   /**
		    * changes an object to a String.
		    * @param obj - Object to format.
		    * @return - String value of an object or empty string if the object is null.
		    */
		   protected String handleObject(Object obj) {
		      return obj == null ? "" : String.valueOf(obj);
		   }

		   /**
		    * changes a BigDecimal to String.
		    * @param decimal - BigDecimal to format
		    * @return String representation of a BigDecimal or empty string if null
		    */
		   protected String handleBigDecimal(BigDecimal decimal) {
		      return decimal == null ? "" : decimal.toString();
		   }

		   /**
		    * Retrieves the string representation of an Long value from the result set.
		    * @param rs - Result set containing the data.
		    * @param columnIndex - index to the column of the long.
		    * @return - the string representation of the long
		    * @throws SQLException - thrown by the result set on error.
		    */
		   protected String handleLong(ResultSet rs, int columnIndex) throws SQLException {
		      long lv = rs.getLong(columnIndex);
		      return rs.wasNull() ? "" : Long.toString(lv);
		   }

		   /**
		    * Retrieves the string representation of an Integer value from the result set.
		    * @param rs - Result set containing the data.
		    * @param columnIndex - index to the column of the integer.
		    * @return - string representation of the Integer.
		    * @throws SQLException - returned from the result set on error.
		    */
		   protected String handleInteger(ResultSet rs, int columnIndex) throws SQLException {
		      int i = rs.getInt(columnIndex);
		      return rs.wasNull() ? "" : Integer.toString(i);
		   }

		   /**
		    * Retrieves a date from the result set.
		    * @param rs - Result set containing the data
		    * @param columnIndex - index to the column of the date
		    * @param dateFormatString - format for the date
		    * @return - formatted date.
		    * @throws SQLException - returned from the result set on error.
		    */
		   protected String handleDate(ResultSet rs, int columnIndex, String dateFormatString) throws SQLException {
		      java.sql.Date date = rs.getDate(columnIndex);
		      String value = null;
		      if (date != null) {
		         SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString);
		         value = dateFormat.format(date);
		      }
		      return value;
		   }

		   /**
		    * Return time read from ResultSet.
		    *
		    * @param time time read from ResultSet
		    * @return String version of time or null if time is null.
		    */
		   protected String handleTime(Time time) {
		      return time == null ? null : time.toString();
		   }

		   /**
		    * The formatted timestamp.
		    * @param timestamp - timestamp read from resultset
		    * @param timestampFormatString - format string
		    * @return - formatted time stamp.
		    */
		   protected String handleTimestamp(Timestamp timestamp, String timestampFormatString) {
		      SimpleDateFormat timeFormat = new SimpleDateFormat(timestampFormatString);
		      return timestamp == null ? null : timeFormat.format(timestamp);
		   }

		   private String getColumnValue(ResultSet rs, int colType, int colIndex, boolean trim, String dateFormatString, String timestampFormatString)
		         throws SQLException, IOException {

		      String value = "";

		      switch (colType) {
		         case Types.BIT:
		         case Types.JAVA_OBJECT:
		            value = handleObject(rs.getObject(colIndex));
		            break;
		         case Types.BOOLEAN:
		            boolean b = rs.getBoolean(colIndex);
		            value = Boolean.valueOf(b).toString();
		            break;
		         case NCLOB: // todo : use rs.getNClob
		         case Types.CLOB:
		            Clob c = rs.getClob(colIndex);
		            if (c != null) {
		               value = read(c);
		            }
		            break;
		         case Types.BIGINT:
		            value = handleLong(rs, colIndex);
		            break;
		         case Types.DECIMAL:
		         case Types.DOUBLE:
		         case Types.FLOAT:
		         case Types.REAL:
		         case Types.NUMERIC:
		            value = handleBigDecimal(rs.getBigDecimal(colIndex));
		            break;
		         case Types.INTEGER:
		         case Types.TINYINT:
		         case Types.SMALLINT:
		            value = handleInteger(rs, colIndex);
		            break;
		         case Types.DATE:
		            value = handleDate(rs, colIndex, dateFormatString);
		            break;
		         case Types.TIME:
		            value = handleTime(rs.getTime(colIndex));
		            break;
		         case Types.TIMESTAMP:
		            value = handleTimestamp(rs.getTimestamp(colIndex), timestampFormatString);
		            break;
		         case NVARCHAR: // todo : use rs.getNString
		         case NCHAR: // todo : use rs.getNString
		         case LONGNVARCHAR: // todo : use rs.getNString
		         case Types.LONGVARCHAR:
		         case Types.VARCHAR:
		         case Types.CHAR:
		            String columnValue = rs.getString(colIndex);
		            if (trim && columnValue != null) {
		               value = columnValue.trim();
		            } else {
		               value = columnValue;
		            }
		            break;
		         default:
		            value = "";
		      }


		      if (value == null) {
		         value = "";
		      }

		      return value;
		   }
}