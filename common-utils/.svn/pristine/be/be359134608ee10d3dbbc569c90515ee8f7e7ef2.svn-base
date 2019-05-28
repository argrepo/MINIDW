package com.datamodel.anvizent.helper.minidw;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Application constants.
 * 
 * rakesh.gajula
 */
final public class Constants {
	
	protected final static Log LOG = LogFactory.getLog(Constants.class);
	private Constants() {
	}

	public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");
	public static final String MYSQL_DRIVER_CLASS = System.getProperty("anvizent.mysql.driver.className").trim();
	public static final String MYSQL_DB_URL = "jdbc:mysql://";

	final public static class Config {
		private Config() {
		}
		public static String STORAGE_LOCATION = "";
		public static final String APP_ID = "MINIDW_WEBAPP";
		public static final String MINIDW_CONFIG_BASE_DIRECTORY_WINDOWS = "C:";
		public static final String MINIDW_CONFIG_BASE_DIRECTORY_OTHER = "/usr";
		public static final String MINIDW_CONFIG_DIRECTORY = "_MINIDW";
		public static final String CONFIG_HOME = getBaseConfigPath();
		public static final String MINIDW_SERVICE_CONFIG = CONFIG_HOME + "minidw_webservice.config";
		public static final String MINIDW_CHANGE_LOG = CONFIG_HOME + "ChangeLog.txt";

		public static final String ENCRYPTION_KEY = "anvizent";
		public static final String RESOURCES = "/resources/";
		public static final int BUFFER_SIZE = 4096;
		public static final String WEBAPP_TRUE = "true";
		public static final String WEBAPP_FALSE = "false";
		public static final int PACKAGE_NAME_MIN = 3;
		public static final int PACKAGE_NAME_MAX = 255;
		public static final String DEFAULT_DATE = "1900-01-01 00:00:00";
		public static final String XREF = "X-";
		public static final String ENCODING_TYPE = "UTF-8";
		public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
		public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
		/* Storage Constants */
		public static final String STORAGE_TYPE_S3 = "s3";
		public static final String STORAGE_TYPE_LOCAL = "local";

		/* Deployment Types */
		public static final String DEPLOYMENT_TYPE_ONPREM = "onprem"; 
		public static final String DEPLOYMENT_TYPE_HYBRID = "hybrid"; 
		public static final String DEPLOYMENT_TYPE_CLOUD = "cloud";
		public static final String DEPLOYMENT_TYPE_MESSAGE = "deploymentMessage";
		public static final String DEPLOYMENT_TYPE_MESSAGE_INAVLID_TYPE = "Invalid deployment type. Please contact admin";
		public static final String DEPLOYMENT_TYPE_MESSAGE_INVALID_STORAGE_LOCATION = "Invalid storage location";
		public static final String DEPLOYMENT_TYPE_MESSAGE_INVALID_FOLDER_STORAGE_LOCATION = "Storage location should be a direcctory";
		public static final String DEPLOYMENT_TYPE_MESSAGE_INVALID_FOLDER_STORAGE_EXIST = "Storage location path does not exists";
		public static final String DEPLOYMENT_TYPE_MESSAGE_INVALID_CONTEXT_PATH = "Minidw cluster not configured.";
		public static final String DEPLOYMENT_TYPE_MESSAGE_INVALID_VERSION_DETAILS = "Vesrion details not found";

		public static final String DEPLOYMENT_TYPE = "deploymentType";
		public static final String BROWSER_DETAILS = "Browser-Detail";
		public static final String HEADER_CLIENT_ID = "clientId";
		public static final String TIME_ZONE = "tomeZone";
		public static final String WEBSERVICE_CONTEXT_URL = "webServiceContextUrl";
		public static final String HEADER_USER_CLIENT_ID = "clientIdOriginal";
		public static final String CSV_SAVE_PATH = "csvSavePath";
		public static final String S3_BUCKET_INFO = "s3BucketInfo";
		public static final String FILE_SETTINGS_INFO = "fileSettingsInfo";
		public static final String ACCESS_KEY = "access.key";
		
		public static final String USER_COOKIE_NAME = "CFDVXDXSFRDS";

		public static final String NEW_LINE = "\n"; 
		public static final String QUOTE = "\""; 
		public static final String BREAK_LINE = "<br>" ;
		public static final String CUSTOM_REQUEST = "customrequest"; 
		
		public static String getBaseConfigPath() {
			String baseConfigPath = System.getProperty("com.anvizent.minidw.config.path");
			if (StringUtils.isBlank(baseConfigPath)) {
			if (SystemUtils.IS_OS_WINDOWS) {
				baseConfigPath = MINIDW_CONFIG_BASE_DIRECTORY_WINDOWS;
			} else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_MAC) {
				baseConfigPath = MINIDW_CONFIG_BASE_DIRECTORY_OTHER;
			}
			}
			baseConfigPath += File.separator + MINIDW_CONFIG_DIRECTORY + File.separator;
			return baseConfigPath;
		}

	}

	public static class Temp {
		private Temp() {
		}

		public static final String TEMP_FILE_DIR;
		static {
			TEMP_FILE_DIR = System.getProperty("java.io.tmpdir") + "/minidw/";
			File tempPath = new File(TEMP_FILE_DIR);
			try {
				if ( !tempPath.exists() ) {
					tempPath.mkdirs();
				}
			} catch (Exception e) {
				LOG.info("Unable to create temp folder : " + TEMP_FILE_DIR +"; " + e.getMessage());
			}
		}

		public static String getTempFileDir() {
			return TEMP_FILE_DIR;
		}
	}
	
	public static interface Extensions {
		String PPK = ".ppk";
		String CSV = ".ppk";
	}


	public static class TempUpload {
		private TempUpload() {
		}

		private static final String TEMP_FILE_DIR;
		public static final String FILE_SEPARATOR = System.getProperty("file.separator");
		static {
			String tmpFolder = System.getProperty("java.io.tmpdir");
			String tmpMiniDwFolder = tmpFolder + FILE_SEPARATOR + "MiniDwUpload" + FILE_SEPARATOR;
			TEMP_FILE_DIR = tmpMiniDwFolder;

		}

		public static String getTempFileDir(String clientId) {
			return TEMP_FILE_DIR + clientId + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + FILE_SEPARATOR;
		}

	}
	final public class SourceType {
		private SourceType() {
		}
		public static final String FLATFILE = "flatfile";
		public static final String DATABASE = "database";
		public static final String WEBSERVICE = "webservice";
	}
	final public class ExecutionStatus {
		private ExecutionStatus() {
		}

		public static final String UPLOADED = "UPLOADED";
		public static final String DOWNLOADED = "DOWNLOADED";
		public static final String PROCESSED = "PROCESSED";
		public static final String COMPLETED = "COMPLETED";
		public static final String STARTED = "STARTED";
		public static final String RESUME = "RESUME";
		public static final String PAUSE = "PAUSE";
		public static final String IGNORED = "IGNORED";
		public static final String INPROGRESS = "INPROGRESS";
		public static final String ERROR = "ERROR";
		public static final String FAILED = "FAILED";
	 
	}
	final public class jobType {
		private jobType() {
		}
		public static final String ALL = "all";
		public static final String DL = "dl";
		public static final String IL = "il";
		public static final String R = "r";
	}
	
	
	final public class PathVariables {
		private PathVariables() {
		}

		public static final String CLIENT_ID = "clientId";
		public static final String ACTIVATION_KEY = "activationKey";
	}

	final public class AnvizentURL {
		private AnvizentURL() {
		}

		public static final String ANVIZENT_SERVICES_BASE_URL = "/anvizentServices/user/{" + PathVariables.CLIENT_ID + "}";
		public static final String ADMIN_SERVICES_BASE_URL = "/adminServices/user/{" + PathVariables.CLIENT_ID + "}";
		public static final String MINIDW_BASE_URL = "/app/user/{" + PathVariables.CLIENT_ID + "}";
		public static final String MINIDW_ADMIN_BASE_URL = "/app_Admin/user/{" + PathVariables.CLIENT_ID + "}";

	}

	final public class AnvizentWSURL {
		private AnvizentWSURL() {
		}

		public static final String ANVIZENT_SERVICES_BASE_URL = "/anvizentWServices/user/{" + PathVariables.CLIENT_ID + "}";

		public static final String COMMON_SERVICES_BASE_URL = "/anvizentCommonWServices/user/{" + PathVariables.CLIENT_ID + "}";

		public static final String ADMIN_SERVICES_BASE_URL = "/adminWServices/user/{" + PathVariables.CLIENT_ID + "}";

	}

	final public class Status {
		private Status() {
		}

		public static final String STATUS_PENDING = "Pending";
		public static final String STATUS_SCHEDULED = "Scheduled";
		public static final String STATUS_RUNNING = "Running";
		public static final String STATUS_EXECUTED = "Executed";
		public static final String STATUS_DONE = "Done";

	}

	final public class TalendJob {
		public TalendJob() {
		}

		public static final String ctxPath = "D:\\ETLJob_sample\\samp_Mysql_Context_File.txt";

	}

	final public class Role {
		public Role() {
		}

		public static final int SUPERADMIN = -200;
		public static final int SCHEDULER_ADMIN = -300;
		public static final int SUPERADMIN_ETLADMIN = -400;
		public static final int ETL_AND_DASHBOARD_DEVELOPER = 7;

	}

	final public class FileType {
		public FileType() {
		}

		public static final String JAR = "jar";
		public static final String CSV = "csv";
		public static final String XLS = "xls";
		public static final String XLSX = "xlsx";
		public static final String TEXT = "txt";
		public static final String R = "r";
		public static final String JSON = "json";
		public static final String XML = "xml";
	}

	final public class FileTypeDelimiter {  
		private FileTypeDelimiter() {
		}
		public static final String CSV_DELIMITER  = ",";
	 
	}
	
	final public class ScheduleType {
		public ScheduleType() {
		}

		public static final String RUNNOW = "runnow";
		public static final String SCHEDULE = "schedule";
		public static final String RUN_WITH_SCHEDULER = "runwithscheduler";
		 
	}
	
	final public class Regex {
		public Regex() {
		}

		// allows all alpha numerics with _ and -
		public static final String ALPHA_NUMERICS_WITH_SP_CHAR = "^([-a-zA-Z0-9_\\s])+|~$";
		// does not allow a string with only special characters
		public static final String REJECT_ONLY_SP_CHAR = ".*[a-zA-Z0-9]+.*";
		public static final String NUMERICS_ONLY = "^[0-9]*$";
	}

	public static class SchedulerFileNames {
		public static final String QUARTZ_PROPERTIES = "quartz.properties";
		public static final String END_POINTS_PROPERTIES = "end.points.properties";
		public static final String SCHEDULER_PROPERTIES = "scheduler.properties";
		private static final String PROPERTIES = "CLIENT_SCHEDULER_PROPERTIES";

		public static final String getBaseConfigPath() {
			String baseConfigPath = System.getProperty("com.anvizent.minidw.config.path");
			if (StringUtils.isBlank(baseConfigPath)) {
				if (SystemUtils.IS_OS_WINDOWS) {
					baseConfigPath = Constants.Config.MINIDW_CONFIG_BASE_DIRECTORY_WINDOWS;
				} else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_MAC) {
					baseConfigPath = Constants.Config.MINIDW_CONFIG_BASE_DIRECTORY_OTHER;
				}
			}
			baseConfigPath += File.separator + Constants.Config.MINIDW_CONFIG_DIRECTORY;
			LOG.info("BaseConfigPath: " + baseConfigPath + ", which is: " + new File(baseConfigPath).getAbsolutePath());

			return baseConfigPath;
		}

		public static final File getQuartzPropertiesFile(String anvizentHome) {
			return new File(anvizentHome, PROPERTIES + File.separator + Constants.SchedulerFileNames.QUARTZ_PROPERTIES);
		}

		public static final File getEndPointsPropertiesFile(String anvizentHome) {
			return new File(anvizentHome, PROPERTIES + File.separator + Constants.SchedulerFileNames.END_POINTS_PROPERTIES);
		}

		public static final File getSchedulerPropertiesFile(String anvizentHome) {
			return new File(anvizentHome, PROPERTIES + File.separator + Constants.SchedulerFileNames.SCHEDULER_PROPERTIES);
		}
	}

	public static class  DataTypeConstants {
		public static final String[] MEASURE_TYPES={
				 "int","INT","INT UNSIGNED","bigint","BIGINT","BIGINT UNSIGNED","INTEGER","INTEGER UNSIGNED",
				 "SMALLINT","SMALLINT UNSIGNED","TINYINT","TINYINT UNSIGNED","MEDIUMINT",
				 "MEDIUMINT UNSIGNED","DECIMAL","DECIMAL UNSIGNED","NUMERIC","NUMERIC UNSIGNED",
				 "FLOAT","FLOAT UNSIGNED","DOUBLE","DOUBLE UNSIGNED","tinyint","smallint",
				 "decimal","numeric","smallmoney","money","float","real"
				  };
		 
		 public static final String[] DIMENSION_TYPES={
				 "nCHAR","nVARCHAR","ntext","CHAR","VARCHAR","BINARY","VARBINARY","BLOB","LONGBLOB",
				 "TEXT","ENUM","SET","DATE","DATETIME","TIMESTAMP","YEAR","TIME","datetime","datetime2",
				 "smalldatetime","date","time","datetimeoffset","timestamp","char","varchar","text",
				 "nchar","nvarchar","nvarchar","ntext","bit","BIT","binary","varbinary","varbinary","image"
				 };
	}
	
	public interface SparkSubmitType {
		String LOCAL = "local";
		String YARN = "yarn";
		String STAND_ALONE = "standalone";
		String MESOS = "mesos";
		String KUBERNETES = "kubernetes";
		List<String> CLUSTER_MODE_SUBMIT_TYPE = Arrays.asList(STAND_ALONE, MESOS, KUBERNETES);
	}
}
