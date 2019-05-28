package com.anvizent.logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

import com.anvizent.encryptor.EncryptionUtility;
import com.anvizent.logger.bean.EmailDetails;
import com.anvizent.logger.bean.EncryptionKeys;

/**
 * @author sambaraju.m
 * 
 **/

/**
 * Logger objects may be obtained by calls on one of the getLogger factory
 * methods. These will either create a new Logger or return a suitable existing
 * Logger.
 * 
 **/
public class LoggerFactory {

	private static String logDirectory;
	private static String logFormat;
	private static Level level;
	private static LogLevel info;
	private static LogLevel debug;
	private static LogLevel error;
	private static LogLevel warn;

	private static EmailDetails errorEmailDetails;
	private static EmailDetails warningEmailDetails;
	private static EncryptionKeys encryptionKeys;

	private static HashMap<String, HashMap<String, Logger>> loggersMap = new HashMap<>();

	public static String getLogDirectory() {
		return logDirectory;
	}

	public static void setLogDirectory(String logDirectory) {
		LoggerFactory.logDirectory = logDirectory;
	}

	public static String getLogFormat() {
		return logFormat;
	}

	public static void setLogFormat(String logFormat) {
		LoggerFactory.logFormat = logFormat;
	}

	public static Level getLevel() {
		return level;
	}

	public static void setLevel(Level level) {
		LoggerFactory.level = level;
	}

	public static LogLevel getInfo() {
		return info;
	}

	public static void setInfo(LogLevel info) {
		LoggerFactory.info = info;
	}

	public static LogLevel getDebug() {
		return debug;
	}

	public static void setDebug(LogLevel debug) {
		LoggerFactory.debug = debug;
	}

	public static LogLevel getError() {
		return error;
	}

	public static void setError(LogLevel error) {
		LoggerFactory.error = error;
	}

	public static LogLevel getWarn() {
		return warn;
	}

	public static void setWarn(LogLevel warn) {
		LoggerFactory.warn = warn;
	}

	public static EmailDetails getErrorEmaildetails() {
		return errorEmailDetails;
	}

	public static void setErrorEmaildetails(EmailDetails errorEmaildetails) throws UnsupportedEncodingException {
		LoggerFactory.errorEmailDetails = errorEmaildetails;
		setDecriptedEmailPassword();
	}

	public static EmailDetails getWarningEmaildetails() {
		return warningEmailDetails;
	}

	public static void setWarningEmaildetails(EmailDetails warningEmailDetails) throws UnsupportedEncodingException {
		LoggerFactory.warningEmailDetails = warningEmailDetails;
		setDecriptedEmailPassword();
	}

	public static EncryptionKeys getEncryptionKeys() {
		return encryptionKeys;
	}

	public static void setEncryptionKeys(EncryptionKeys encryptionKeys) throws UnsupportedEncodingException {
		LoggerFactory.encryptionKeys = encryptionKeys;
		setDecriptedEmailPassword();
	}

	public static HashMap<String, HashMap<String, Logger>> getLoggersMap() {
		return loggersMap;
	}

	public static void setLoggersMap(HashMap<String, HashMap<String, Logger>> loggersMap) {
		LoggerFactory.loggersMap = loggersMap;
	}

	@SuppressWarnings("rawtypes")
	public static Logger getLogger(String postFix, String fileName, Class className) {
		return getLogger(postFix, fileName, false, className);
	}

	@SuppressWarnings({ "rawtypes" })
	public static Logger getLogger(String postFix, String fileName, boolean alsoLogIntoConsole, Class className) {
		String fileNameWithPostFix = getFileName(fileName, postFix);
		String mapKey = getMapKey(fileName, className);
		if (loggersMap.get(mapKey) == null || loggersMap.get(mapKey).size() == 0) {
			HashMap<String, Logger> loggerMap = new HashMap<>();
			loggerMap.put(fileNameWithPostFix, null);
			loggersMap.put(mapKey, loggerMap);
		} else {
			ArrayList<String> keys = new ArrayList<>(loggersMap.get(mapKey).keySet());
			for (String key : keys) {
				if (!key.equals(fileNameWithPostFix)) {
					loggersMap.get(mapKey).get(key).distroy();
					loggersMap.get(mapKey).remove(key);
				}
			}
		}

		if (loggersMap.get(mapKey).get(fileNameWithPostFix) == null) {
			org.apache.log4j.Logger logger4j = org.apache.log4j.Logger.getLogger(mapKey);
			logger4j.setLevel(level);
			addFileAppender(logger4j, mapKey, fileNameWithPostFix);

			if (alsoLogIntoConsole) {
				addConsoleAppender(logger4j, mapKey);
			}

			Logger logger = new Logger(logger4j, info, debug, error, warn, errorEmailDetails, warningEmailDetails);
			loggersMap.get(mapKey).put(fileNameWithPostFix, logger);
		}

		return loggersMap.get(mapKey).get(fileNameWithPostFix);
	}

	public static Logger getLogger(String postFix, String fileName, String className) {
		return getLogger(postFix, fileName, false, className);
	}

	public static Logger getLogger(String postFix, String fileName, boolean alsoLogIntoConsole, String className) {
		String fileNameWithPostFix = getFileName(fileName, postFix);
		String mapKey = getMapKey(fileName, className);
		if (loggersMap.get(mapKey) == null || loggersMap.get(mapKey).size() == 0) {
			HashMap<String, Logger> loggerMap = new HashMap<>();
			loggerMap.put(fileNameWithPostFix, null);
			loggersMap.put(mapKey, loggerMap);
		} else {
			ArrayList<String> keys = new ArrayList<>(loggersMap.get(mapKey).keySet());
			for (String key : keys) {
				if (!key.equals(fileNameWithPostFix)) {
					loggersMap.get(mapKey).get(key).distroy();
				}
			}
		}

		if (loggersMap.get(mapKey).get(fileNameWithPostFix) == null) {
			org.apache.log4j.Logger logger4j = org.apache.log4j.Logger.getLogger(mapKey);
			logger4j.setLevel(level);
			addFileAppender(logger4j, mapKey, fileNameWithPostFix);

			if (alsoLogIntoConsole) {
				addConsoleAppender(logger4j, mapKey);
			}

			Logger logger = new Logger(logger4j, info, debug, error, warn, errorEmailDetails, warningEmailDetails);
			loggersMap.get(mapKey).put(fileNameWithPostFix, logger);
		}

		return loggersMap.get(mapKey).get(fileNameWithPostFix);
	}

	private static String getFileName(String fileName, String postFix) {
		fileName = logDirectory + File.separatorChar + fileName;
		if (postFix != null && !postFix.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(postFix);
			fileName += dateFormat.format(new Date()) + ".log";
		}

		return fileName;
	}

	private static void addFileAppender(org.apache.log4j.Logger logger, String name, String fileName) {
		FileAppender fileAppender = new FileAppender();

		fileAppender.setName(name);
		fileAppender.setFile(fileName);
		fileAppender.setLayout(new PatternLayout(logFormat));
		fileAppender.setThreshold(level);
		fileAppender.setAppend(true);
		fileAppender.activateOptions();

		logger.addAppender(fileAppender);
	}

	@SuppressWarnings("rawtypes")
	private static String getMapKey(String fileName, Class className) {
		return fileName + "_" + className.getCanonicalName();
	}

	private static String getMapKey(String fileName, String className) {
		return fileName + "_" + className;
	}

	private static void setDecriptedEmailPassword() throws UnsupportedEncodingException {
		if (encryptionKeys != null) {
			EncryptionUtility encUtilityObj = new EncryptionUtility(encryptionKeys.getPrimaryKey(), encryptionKeys.getIV());
			try {
				if (errorEmailDetails != null) {
					errorEmailDetails.setPassword(encUtilityObj.decrypt(errorEmailDetails.getPassword()));
				}

				if (warningEmailDetails != null) {
					warningEmailDetails.setPassword(encUtilityObj.decrypt(warningEmailDetails.getPassword()));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings({ "rawtypes" })
	public static Logger getLogger(Class className) {
		String mapKey = getMapKey("", className);
		if (loggersMap.get(mapKey) == null || loggersMap.get(mapKey).size() == 0) {
			HashMap<String, Logger> loggerMap = new HashMap<>();
			loggerMap.put("", null);
			loggersMap.put(mapKey, loggerMap);
		} else {
			ArrayList<String> keys = new ArrayList<>(loggersMap.get(mapKey).keySet());
			for (String key : keys) {
				if (!key.equals("")) {
					loggersMap.get(mapKey).get(key).distroy();
				}
			}
		}

		if (loggersMap.get(mapKey).get("") == null) {
			org.apache.log4j.Logger logger4j = org.apache.log4j.Logger.getLogger(mapKey);
			logger4j.setLevel(level);

			addConsoleAppender(logger4j, mapKey);

			Logger logger = new Logger(logger4j, info, debug, error, warn, errorEmailDetails, warningEmailDetails);
			loggersMap.get(mapKey).put("", logger);
		}

		return loggersMap.get(mapKey).get("");
	}

	public static Logger getLogger(String className) {
		String mapKey = getMapKey("", className);
		if (loggersMap.get(mapKey) == null || loggersMap.get(mapKey).size() == 0) {
			HashMap<String, Logger> loggerMap = new HashMap<>();
			loggerMap.put("", null);
			loggersMap.put(mapKey, loggerMap);
		} else {
			ArrayList<String> keys = new ArrayList<>(loggersMap.get(mapKey).keySet());
			for (String key : keys) {
				if (!key.equals("")) {
					loggersMap.get(mapKey).get(key).distroy();
				}
			}
		}

		if (loggersMap.get(mapKey).get("") == null) {
			org.apache.log4j.Logger logger4j = org.apache.log4j.Logger.getLogger(mapKey);
			logger4j.setLevel(level);
			addConsoleAppender(logger4j, mapKey);

			Logger logger = new Logger(logger4j, info, debug, error, warn, errorEmailDetails, warningEmailDetails);
			loggersMap.get(mapKey).put("", logger);
		}

		return loggersMap.get(mapKey).get("");
	}

	private static void addConsoleAppender(org.apache.log4j.Logger logger, String name) {
		ConsoleAppender consoleAppender = new ConsoleAppender();

		consoleAppender.setName(name);
		consoleAppender.setLayout(new PatternLayout(logFormat));
		consoleAppender.setThreshold(level);
		consoleAppender.activateOptions();

		logger.addAppender(consoleAppender);
	}

}