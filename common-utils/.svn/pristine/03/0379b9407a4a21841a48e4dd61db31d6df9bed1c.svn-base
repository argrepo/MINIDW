package com.datamodel.anvizent.helper;

import java.io.File;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

/**
 * Application constants.
 * 
 * rakesh.gajula
 */
final public class Constants {
	private Constants() {
	}

	public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");

	final public static class Config {
		private Config() {
		}

		public static final String APP_ID = "MINIDW_WEBSERVICE";
		public static final String MINIDW_CONFIG_BASE_DIRECTORY_WINDOWS = "C:";
		public static final String MINIDW_CONFIG_BASE_DIRECTORY_OTHER = "/usr";
		public static final String MINIDW_CONFIG_DIRECTORY = "_MINIDW";
		
		public static final String CONFIG_HOME = getBaseConfigPath();
		public static final String EXTERNAL_CONFIG = CONFIG_HOME + "external-end-points.config";
		public static final String ETL_JOBS = CONFIG_HOME + "ETLJOBS";
		public static final String COMMON_ETL_JOBS = ETL_JOBS + File.separator + "COMMON_ETLJOBS";
		public static final String SSL_FILES = ETL_JOBS + File.separator + "SSL_FILES";
		public static final String ETL_JOBS_LOGS = ETL_JOBS + File.separator + "logs";
		public static final String SYSTEM_PATH_SEPARATOR = "path.separator";

		public static final String ENCRYPTION_KEY = "anvizent";
		public static final String RESOURCES = "/resources/";

		public static final String TABLE_SCRIPTS = "/TABLE_SCRIPTS";

		public static final String TEMPLATES_CSV = "/TEMPLATES/csv";
		public static final String TEMPLATES_EXCEL = "/TEMPLATES/excel";

		public static final String TEMPLATES_XREF_CSV = "/TEMPLATES/xRef_Templates/csv";
		public static final String TEMPLATES_XREF_EXCEL = "/TEMPLATES/xRef_Templates/excel";

		public static final int BUFFER_SIZE = 4096;
		public static final String CSV_FROM_EXCEL = "CsvFromExcel";
		public static final String DEFAULT_DATE = "1900-01-01 00:00:00";

		public static final String ENCODING_TYPE = "UTF-8";

		public static final String ETL_BACKUP_FOLDER = "/jarbackups/";
		public static final String SSL_BACKUP_FOLDER = "/sslFiilesBackups/";
		public static final String TRUEVALUE = "true";
		public static final String FALSEVALUE = "false";
		
		public static final String NEW_LINE = "\n";
		
		public static final String STORAGE_TYPE_S3 = com.datamodel.anvizent.helper.minidw.Constants.Config.STORAGE_TYPE_S3;
		public static final String STORAGE_TYPE_LOCAL = com.datamodel.anvizent.helper.minidw.Constants.Config.STORAGE_TYPE_LOCAL;
		
		public static final String DEPLOYMENT_TYPE_ONPREM = com.datamodel.anvizent.helper.minidw.Constants.Config.DEPLOYMENT_TYPE_ONPREM;
		public static final String DEPLOYMENT_TYPE_HYBRID = com.datamodel.anvizent.helper.minidw.Constants.Config.DEPLOYMENT_TYPE_HYBRID;
		public static final String DEPLOYMENT_TYPE_CLOUD = com.datamodel.anvizent.helper.minidw.Constants.Config.DEPLOYMENT_TYPE_CLOUD;
		public static final String DEPLOYMENT_TYPE = com.datamodel.anvizent.helper.minidw.Constants.Config.DEPLOYMENT_TYPE;
		public static final String CSV_SAVE_PATH = com.datamodel.anvizent.helper.minidw.Constants.Config.CSV_SAVE_PATH;
		
		public static final String AI_JOBS = CONFIG_HOME + "AIJOBS";
		public static final String AI_BACKUP_FOLDER = "/aijarbackups/";
		
		public static final String AI_COMMON_JOBS = AI_JOBS +"/AI_COMMON_JOBS/";
		public static final String AI_ERROR_LOGS= AI_JOBS + File.separator + "r_logs/";
		public static String JAVA_OPT_PARAMS = "";
		
		public static final String ON = "ON";
		
		public static String getBaseConfigPath() {
			String baseConfigPath = System.getProperty("com.anvizent.minidw.config.path");
			if (StringUtils.isBlank(baseConfigPath)) {
				if (SystemUtils.IS_OS_WINDOWS) {
					baseConfigPath = MINIDW_CONFIG_BASE_DIRECTORY_WINDOWS;
					System.out.println("This is Windows.");
				} else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_UNIX || SystemUtils.IS_OS_MAC) {
					baseConfigPath = MINIDW_CONFIG_BASE_DIRECTORY_OTHER;
					System.out.println("This is Unix/Linux/Mac.");
				}
			}

			baseConfigPath += File.separator + MINIDW_CONFIG_DIRECTORY + File.separator;
			return baseConfigPath;
		}

	}

	public static class Temp {
		private Temp() {
		}

		public static final String TEMP_FILE_DIR ;
		public static final String TEMP_FILE_DIR_TABLE_SCRIPT ;
		static {
				TEMP_FILE_DIR = System.getProperty("java.io.tmpdir") + "/minidw/";
				TEMP_FILE_DIR_TABLE_SCRIPT = TEMP_FILE_DIR + "/TableScripts/";
		}

		public static String getTempFileDir() {
			return TEMP_FILE_DIR;
		}

		public static String getTempFileDirForTableScripts() {
			return TEMP_FILE_DIR_TABLE_SCRIPT;
		}
	}

	final public class FileStatus {
		private FileStatus() {
		}

		public static final String UPLOADED = "UPLOADED";
		public static final String DOWNLOADED = "DOWNLOADED";
		public static final String PROCESSED = "PROCESSED";
	}

	final public class PathVariables {
		private PathVariables() {
		}

		public static final String CLIENT_ID = "specificId";
		public static final String ACTIVATION_KEY = "activationKey";
	}

	final public class AnvizentURL {
		private AnvizentURL() {
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
		public static final String STATUS_SERVER_SCHEDULER = "Server Scheduler";
		public static final String STATUS_CLIENT_SCHEDULER = "Client Scheduler";
		public static final String STATUS_ERROR = "Error";
		public static final String STATUS_FAILED = "Failed";
		public static final String STATUS_STARTED = "Started";
		public static final String STATUS_STARTING = "Starting";

	}

	final public class TalendJob {
		private TalendJob() {
		}

		public static final String ctxPath = "D:\\ETLJob_sample\\samp_Mysql_Context_File.txt";

	}

	final public class Role {
		private Role() {
		}

		public static final int SUPERADMIN = -200;
		public static final int SUPERADMIN_ETLADMIN = -400;
		public static final int ETL_AND_DASHBOARD_DEVELOPER = 7;

	}




	final public class QueryType {
		private QueryType() {
		}

		public static final String QUERY = "Query";
		public static final String PROCEDURE = "procedure";

	}

	final public class FileType {
		private FileType() {
		}

		public static final String JAR = "jar";
		public static final String CSV = "csv";
		public static final String XLS = "xls";
		public static final String XLSX = "xlsx";
		public static final String TEXT = "txt";
	}
	final public class FileTypeDelimiter {  
		private FileTypeDelimiter() {
		}
		public static final String CSV_DELIMITER  = ",";
	 
	}
	final public class Regex {
		private Regex() {
		}
		// allows all alpha numerics with _ and -
		public static final String ALPHA_NUMERICS_WITH_SP_CHAR = "^([-a-zA-Z0-9_\\s])+|~$";
		// does not allow a string with only special characters
		public static final String REJECT_ONLY_SP_CHAR = ".*[a-zA-Z0-9]+.*";
		public static final String REGEX_TO_CHECK_DECIMAL_WITH_ONLY_ONE_ZERO = "^[0-9]*\\.?[0]$";
	} 
	 
	final public class WebService {
		private WebService() {
		}

		public static final int PIPEDRIVE_API = 1;
		public static final int KEYEDIN_API = 2;
		public static final int EXACTONLINE_API = 3;
		public static final int SKUVAULT_API = 4;
	}

	final public static class HttpRequestHeader {
		private HttpRequestHeader() {
		}

		public static final String CONTENT_TYPE = "Content-Type";
		public static final String JSON_CONTENT_TYPE = "application/json";
		public static final String X_AUTH_TOKEN = "X-Auth-Token";
		public static final String HTTP_400_BAD_REQUEST = "400 Bad Request";
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
		public static final String COMPLETED = "COMPLETED";
		public static final String STARTED = "STARTED";
		public static final String INPROGRESS = "INPROGRESS";
		public static final String ERROR = "ERROR";
		public static final String PAUSED = "PAUSED";
		public static final String RESUME = "PAUSERESUMED";
		public static final String FAILED = "FAILED";
		public static final String IGNORED = "IGNORED";
	}
	
	final public class ILCurrencyTemplate { 
		private ILCurrencyTemplate() {
		}
		public static final String IL_CURRENCY_RATE_TEMPLATE_FILE_NAME = "IL_Currency_Rate.csv";
	}
	
	final public class PackageQueueType {
		private PackageQueueType(){}
		public static final String PACKAGE_UPLOAD_QUEUE = "PACKAGE_UPLOAD_QUEUE";
		public static final String PACKAGE_EXECUTION_QUEUE = "PACKAGE_EXECUTION_QUEUE";
	}
	
	public enum Druid {
		HOUR {
			@Override
			public String getValue() {
				return "HOUR";
			}
		},
		RUN_NOW {
			@Override
			public String getValue() {
				return "RUN_NOW";
			}
		};
		public abstract String getValue();

		public static String getTimeFieldsGranularity(String value) {
			switch (value) {
				case "HOUR":
					return Druid.HOUR.getValue();
				default:
					return null;

			}
		}

		public static String getPriority(String value) {
			switch (value) {
				case "RUN_NOW":
					return Druid.RUN_NOW.getValue();
				default:
					return null;

			}
		}
	}

	private static   String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}
	
	public interface CrossReferenceExecutionType {
		String REGULAR = "Regular";
		String EDIT = "Edit";
		String DELETE = "Delete";
	}
	
}
