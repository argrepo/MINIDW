package com.anvizent.logger;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

import com.anvizent.logger.bean.EmailDetails;

public class Logger {

	private org.apache.log4j.Logger logger;

	private final LogLevel info;
	private final LogLevel debug;
	private final LogLevel error;
	private final LogLevel warn;

	private EmailDetails errorEmaildetails;
	private EmailDetails warningEmaildetails;

	public Logger(org.apache.log4j.Logger logger, LogLevel infoLog, LogLevel debugLog, LogLevel errorLog, LogLevel warnLog, EmailDetails errorEmaildetails,
			EmailDetails warningEmaildetails) {
		this.logger = logger;
		this.info = infoLog;
		this.debug = debugLog;
		this.error = errorLog;
		this.warn = warnLog;
		this.errorEmaildetails = errorEmaildetails;
		this.warningEmaildetails = warningEmaildetails;
	}

	public void distroy() {
		logger.removeAllAppenders();
		logger = null;
	}

	public void debug(Object message) {
		debug(LogLevel.MINIMAL, message);
	}

	public void debug(LogLevel logLevel, Object message) {
		if (logLevel.canLogFor(this.debug)) {
			setFormat();
			logger.debug(message);
		}
	}

	public void debug(Object message, Throwable throwable) {
		debug(LogLevel.MINIMAL, message, throwable);
	}

	public void debug(LogLevel logLevel, Object message, Throwable throwable) {
		if (logLevel.canLogFor(this.debug)) {
			setFormat();
			logger.debug(message, throwable);
		}
	}

	public void error(Object message) {
		error(LogLevel.MINIMAL, message);
	}

	public void error(LogLevel logLevel, Object message) {
		if (logLevel.canLogFor(this.error)) {
			setFormat();
			logger.error(message);
			try {
				sendEmail(message.toString(), errorEmaildetails);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public void error(Object message, Throwable throwable) {
		error(LogLevel.MINIMAL, message, throwable);
	}

	public void error(LogLevel logLevel, Object message, Throwable throwable) {
		if (logLevel.canLogFor(this.error)) {
			setFormat();
			logger.error(message, throwable);
			try {
				sendEmail(message.toString() + "\n" + ExceptionUtils.getStackTrace(throwable), errorEmaildetails);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public void fatal(Object message) {
		setFormat();
		logger.fatal(message);
		sendEmail(message.toString(), errorEmaildetails);
	}

	public void fatal(Object message, Throwable throwable) {
		setFormat();
		logger.fatal(message, throwable);
		sendEmail(message.toString() + "\n" + ExceptionUtils.getStackTrace(throwable), errorEmaildetails);
	}

	public Level getEffectiveLevel() {
		return logger.getEffectiveLevel();
	}

	public Level getLevel() {
		return logger.getLevel();
	}

	public String getName() {
		return logger.getName();
	}

	public void info(Object message) {
		info(LogLevel.MINIMAL, message);
	}

	public void info(LogLevel logLevel, Object message) {
		if (logLevel.canLogFor(this.info)) {
			setFormat();
			logger.info(message);
		}
	}

	public void info(Object message, Throwable throwable) {
		info(LogLevel.MINIMAL, message, throwable);
	}

	public void info(LogLevel logLevel, Object message, Throwable throwable) {
		if (logLevel.canLogFor(this.info)) {
			setFormat();
			logger.info(message, throwable);
		}
	}

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public boolean isEnabledFor(Level level) {
		return logger.isEnabledFor(level);
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public void warn(Object message) {
		warn(LogLevel.MINIMAL, message);
	}

	public void warn(LogLevel logLevel, Object message) {
		if (logLevel.canLogFor(this.warn)) {
			setFormat();
			logger.warn(message);
			sendEmail(message.toString(), warningEmaildetails);
		}
	}

	public void warn(Object message, Throwable throwable) {
		warn(LogLevel.MINIMAL, message, throwable);
	}

	public void warn(LogLevel logLevel, Object message, Throwable throwable) {
		if (logLevel.canLogFor(this.warn)) {
			setFormat();
			logger.warn(message, throwable);
			sendEmail(message.toString() + "\n" + ExceptionUtils.getStackTrace(throwable), warningEmaildetails);
		}
	}

	public void sendEmail(String msgBody, EmailDetails emailDetails) {
		if (emailDetails != null) {
			Session session = getSession(emailDetails.getProperties(), emailDetails);

			MimeMessage message = getMessage(session, msgBody, emailDetails);

			try {
				Transport.send(message);
				System.out.println("message sent successfully");
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private MimeMessage getMessage(Session session, String body, EmailDetails emailDetails) {
		MimeMessage message = new MimeMessage(session);

		try {
			String systemAddress = InetAddress.getLocalHost().toString();

			String totalBody = "Message: " + body + "\n" + "System Address: " + systemAddress;
			if (emailDetails.getMessageDetails().getBccList() != null) {
				message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(emailDetails.getMessageDetails().getBccList()));
			}
			if (emailDetails.getMessageDetails().getCcList() != null) {
				message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(emailDetails.getMessageDetails().getCcList()));
			}
			if (emailDetails.getReplyToAddress() != null) {
				message.setReplyTo(InternetAddress.parse(emailDetails.getReplyToAddress()));
			}
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDetails.getMessageDetails().getToList()));
			message.setSubject(emailDetails.getMessageDetails().getSubject());
			message.setText(totalBody);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return message;
	}

	private Session getSession(Properties props, EmailDetails emailDetails) {
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailDetails.getFromAddress(), emailDetails.getPassword());
			}
		});

		return session;
	}

	private void setFormat() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String className = stackTraceElements[3].getClassName();
		String methodName = stackTraceElements[3].getMethodName();
		String fileName = stackTraceElements[3].getFileName();
		int lineNumber = stackTraceElements[3].getLineNumber();

		changeAppenders(className, methodName, fileName, lineNumber);

	}

	@SuppressWarnings("unchecked")
	private void changeAppenders(String className, String methodName, String fileName, int lineNumber) {
		Enumeration<Appender> enumeration = logger.getAllAppenders();

		while (enumeration.hasMoreElements()) {
			Appender appender = (Appender) enumeration.nextElement();
			String pattern = ((PatternLayout) appender.getLayout()).getConversionPattern();
			String newPattern = getNewPattern(pattern, className, methodName, fileName, lineNumber);
			appender.setLayout(new PatternLayout(newPattern));
		}
	}

	private String getNewPattern(String format, String className, String methodName, String fileName, int lineNumber) {
		format = replaceFormat(format, 'C', className, true);
		format = replaceFormat(format, 'F', fileName);
		format = replaceFormat(format, 'l', className + "." + methodName + "(" + fileName + ":" + lineNumber + ")");
		format = replaceFormat(format, 'L', "" + lineNumber);
		format = replaceFormat(format, 'M', methodName);

		return format;
	}

	private String replaceFormat(String format, char check, String value) {
		return replaceFormat(format, check, value, false);
	}

	private String replaceFormat(String format, char check, String value, boolean postPrecision) {
		String patternStr = "%[-]*[0-9]*[.]*[0-9]*" + check;
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(format);

		if (matcher.find()) {
			String sub1 = format.substring(0, matcher.start());
			String sub3 = format.substring(matcher.end());

			if (postPrecision) {
				String[] array = postPrecesion(value, sub3);
				value = array[0];
				sub3 = array[1];
			}

			value = formatString(value, format.substring(matcher.start() + 1, matcher.end() - 1));

			format = sub1 + value + sub3;
		}

		return format;
	}

	private String[] postPrecesion(String className, String sub3) {
		if (sub3.startsWith("{")) {
			int index = sub3.indexOf("}");

			if (!sub3.startsWith("{-")) {
				try {
					int precisions = Integer.parseInt(sub3.substring(1, index));
					String[] array = className.split("[.]");
					if (array.length > precisions) {
						className = String.join(".", Arrays.copyOfRange(array, array.length - precisions, array.length));
					}
				} catch (Exception exception) {
					sub3 = sub3.substring(index);
				}
			}

			sub3 = sub3.substring(index + 1);
		}
		return new String[] { className, sub3 };
	}

	private String formatString(String className, String precisions) {
		String stringFormat[] = precisions.split("[.]");

		if (stringFormat.length == 2) {
			try {
				int max = Integer.parseInt(stringFormat[1]);
				if (className.length() > max) {
					return className.substring(className.length() - max);
				}
			} catch (Exception exception) {
			}
		} else if (stringFormat.length == 1) {
			return String.format("%" + stringFormat[0] + "s", className);
		}

		return className;
	}

}